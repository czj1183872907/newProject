package com.chery.exeed.crm.lead.service;

import com.alibaba.fastjson.JSON;
import com.chery.exeed.crm.common.dto.*;
import com.chery.exeed.crm.common.service.*;
import com.chery.exeed.crm.common.util.BeanUtils;
import com.chery.exeed.crm.common.util.SessionHelper;
import com.chery.exeed.crm.common.util.StringUtils;
import com.chery.exeed.crm.lead.constants.LeadConstants;
import com.chery.exeed.crm.lead.constants.LeadConstants.LEAD_STATUS_ENUME;
import com.chery.exeed.crm.lead.constants.LeadConstants.LEAD_TASK_STATUS_ENUME;
import com.chery.exeed.crm.lead.dto.*;
import com.chery.exeed.crm.lead.mapper.*;
import com.chery.exeed.crm.lead.model.Lead;
import com.chery.exeed.crm.lead.model.LeadDistributeConfig;
import com.chery.exeed.crm.lead.model.LeadTask;
import com.chery.exeed.crm.sysadmin.dto.UserDetailDTO;
import com.chery.exeed.crm.sysadmin.service.SysUserService;
import com.chery.exeed.ifs.common.dto.CarModelDTO;
import com.chery.exeed.ifs.common.dto.CarSeriesDTO;
import com.chery.exeed.ifs.common.dto.ChannelDTO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.servicecomb.provider.pojo.RpcReference;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Author:xiaowei.zhu
 * 2019/1/22 16:55
 */
@RestSchema(schemaId = "task-service")
@RequestMapping(path = "/apis/lead/task")
public class CallTaskServiceImpl implements CallTaskService {
    private static Logger  logger = LoggerFactory.getLogger(CallTaskServiceImpl.class);
    @Autowired
    private LeadTaskSearchMapper leadTaskSearchMapper;
    @Autowired
    private LeadTaskMapper leadTaskMapper;
    @Autowired
    private DealerService dealerService;
    @Autowired
    private LeadMapper leadMapper;
    @Autowired
    private LeadSearchMapper leadSearchMapper;
    @Autowired
    private RegionService regionService;
    @Autowired
    private LeadCommonService leadCommonService;
    @Autowired
    private CarService carService;
    @Autowired
    private MetaApiService metaApiService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    public RabbitTemplate rabbitTemplate;
    @Autowired
    private LeadDistributeConfigSearchMapper leadDistributeConfigSearchMapper;
    @Autowired
    private LeadDistributeConfigMapper leadDistributeConfigMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private PreLeadSearchMapper preLeadSearchMapper;

    @RpcReference(microserviceName = "exeed-crm-apis:exeed-crm-sysadmin", schemaId = "user-service")
    private SysUserService userService;

    @Override
    @RequestMapping(
            value = {"/list"},
            method = {RequestMethod.POST}
    )
    public ResponseData<PaginationResult<LeadTaskDTO>> getLeadTaskList(@RequestBody LeadTaskParamDTO leadTaskParam) {
        Integer currentUserId = SessionHelper.getCurrentUserId();
        PaginationResult<LeadTaskDTO> result = new PaginationResult<>();
        String searchCode = leadTaskParam.getSearchCode();
        Integer status=leadTaskParam.getStatus();
        Date dueDateActual=null;
        if(searchCode !=null){
            switch (searchCode){
                case "2002":
                    break;
                case "2003":
                    break;
                case "2004":
                    break;
                case "2005":
                    break;
                case "2007":
                    dueDateActual=new Date();break;
                default:status=leadTaskParam.getStatus();
            }
        }
        leadTaskParam.setStatus(status);
        leadTaskParam.setDueDateActual(dueDateActual);
        ResponseData<UserDetailDTO> sysUserData = userService.monitorInfo(currentUserId);
        UserDetailDTO sysUser = sysUserData.getData();
        Byte cccAccount = sysUser.getCccAccount();
        Byte isMonitor = sysUser.getIsMonitor();
        /**如果是坐席,只能看到自己的任务*/
        if((cccAccount!=null && cccAccount.intValue()==1)&&(isMonitor==null || isMonitor.intValue()!=1)){
            leadTaskParam.setOwner(null);
            leadTaskParam.setOwner(currentUserId);
        }

        PageHelper.startPage(leadTaskParam.getPageNo(), leadTaskParam.getPageSize());
        List<LeadTaskDTO> data = leadTaskSearchMapper.getMyTaskList(leadTaskParam);
        setDataForList(data);
        PageInfo<LeadTaskDTO> page = new PageInfo(data);
        page.setPageSize(leadTaskParam.getPageSize());
        result.setTotalCount(page.getTotal());
        result.setData(data);
        return ResponseData.success(result);
    }

    @Override
    @RequestMapping(
            value = {"/getLeadTaskDetailForUpdate"},
            method = {RequestMethod.POST}
    )
    public ResponseData<LeadTaskDTO> getLeadTaskDetailForUpdate(Long taskId){
        LeadTaskDTO task = leadTaskSearchMapper.getLeadTaskDetailForUpdate(taskId);

        if(!StringUtils.isEmpty(task.getDealer())) {
            DealerDTO dealerDTO = dealerService.detailsDealer(task.getDealer());
            if (dealerDTO != null) {
                task.setDealerName(dealerDTO.getDealerName());
            }
        }
        return ResponseData.success(task);
    }

    @Override
    @RequestMapping(
            value = {"/update"},
            method = {RequestMethod.POST}
    )
    @Transactional(rollbackFor = Exception.class,transactionManager = "leadTransactionManager")
    public ResponseData<Boolean> update(@RequestBody LeadTaskDTO task){
        if(task.getId()==null){
            return ResponseData.fail("任务ID为空");
        }
        LeadTask leadTask = leadTaskMapper.selectByPrimaryKey(task.getId());
        if(leadTask==null){
            return ResponseData.fail("任务ID无效");
        }
        if(task.getStatus()!=null && task.getStatus()==LEAD_TASK_STATUS_ENUME.COMPLETED.getValue()){
            return ResponseData.fail("任务已完成不能修改");
        }
        if(task.getOwner()!=null) {
            leadTask.setOwner(task.getOwner());
        }
        if(task.getDueDatePlan()!=null){
            leadTask.setDueDatePlan(task.getDueDatePlan());
        }
        if(task.getDueDateActual()!=null){
            leadTask.setDueDateActual(task.getDueDateActual());
        }

        if(task.getTaskResult()!=null){
            leadTask.setTaskResult(task.getTaskResult());
        }

        if(task.getTaskType()!=null){
            leadTask.setTaskType(task.getTaskType());
        }

        if(task.getNextCallTime()!=null){
            leadTask.setNextCallTime(task.getNextCallTime());
        }
        leadTask.setMemo(task.getMemo());
        Integer currentUserId = SessionHelper.getCurrentUserId();
        leadTaskMapper.updateByPrimaryKeySelective(leadTask);
        logger.info("修改线索信息,leadId:"+leadTask.getLeadId());
        Lead lead = new Lead();
        lead.setId(task.getLeadId());
        lead.setLastName(task.getLastName());
        lead.setFirstName(task.getFirstName());
        lead.setRating(task.getRating());
        lead.setBrandName(task.getBrandName());
        lead.setSeriesName(task.getSeriesName());
        lead.setModelName(task.getModelName());
        String dealer = task.getDealer();
        if(StringUtils.isEmpty(dealer)){
            dealer = null;
        }
        lead.setDealer(dealer);
        lead.setBudget(task.getBudget());
        lead.setPurchaseTime(task.getPurchaseTime());
        lead.setConsultingFrequency(task.getConsultingFrequency());
        lead.setProvince(task.getProvince());
        lead.setAge(task.getAge());
        lead.setHobby(task.getHobby());
        lead.setPersontitle(task.getPersontitle());
        lead.setCity(task.getCity());
        lead.setGender(task.getGender());
        lead.setVehiclePurpose(task.getVehiclePurpose());
        lead.setTestDrivePlan(task.getTestDrivePlan());
        lead.setFollowCar(task.getFollowCar());
        lead.setFollowInfo(task.getFollowInfo());
        lead.setFollowRemarks(task.getFollowRemarks());
        lead.setModifyBy(currentUserId);
        lead.setModifyDate(new Date());
        leadSearchMapper.updateByPrimaryKeySelectiveForTask(lead);
        return ResponseData.success(true);
    }

    private void setDataForList(List<LeadTaskDTO> data){
        if(data!=null){
            for(LeadTaskDTO dto:data){
                if(dto.getOwner()!=null) {
                    ResponseData<String> user = userService.getUserNameById(dto.getOwner());
                    dto.setOwnerName(user.getData());
                }
                String channelCode = dto.getChannel();
                if (!StringUtils.isEmpty(channelCode)) {
                    ChannelDTO channel = channelService.getChannelByCode(channelCode);
                    if (channel != null) {
                        dto.setChannel(channel.getName());
                    }
                }
            }
        }
    }

    @Override
    @RequestMapping(
            value = {"/detail"},
            method = {RequestMethod.GET}
    )
    public ResponseData<LeadTaskDetailDTO> getTaskDetail(Long taskId){
        if(taskId==null){
            return ResponseData.fail("入参异常");
        }
        LeadTaskDetailDTO task = leadTaskSearchMapper.getTaskById(taskId);
        if(task.getOwner()!=null) {
            ResponseData<String> user = userService.getUserNameById(task.getOwner());
            task.setOwnerName(user.getData());
        }
        if(!StringUtils.isEmpty(task.getDealer())) {
            DealerDTO dealerDTO = dealerService.detailsDealer(task.getDealer());
            if (dealerDTO != null) {
                task.setDealerName(dealerDTO.getDealerName());
            }
        }
        String city = task.getCity();
        if (city != null && NumberUtils.isDigits(city)) {
            RegionDTO regionDTO = regionService.detailsRegion(Integer.parseInt(city));
            if (regionDTO != null) {
                task.setCity(regionDTO.getRegionName());
            }
        }

        String province = task.getProvince();
        if (province != null && NumberUtils.isDigits(province)) {
            RegionDTO regionDTO = regionService.detailsRegion(Integer.parseInt(province));
            if (regionDTO != null) {
                task.setProvince(regionDTO.getRegionName());
            }
        }
        String seriesName = task.getSeriesName();
        if(!StringUtils.isEmpty(seriesName)){
            CarSeriesDTO carSeries = carService.getCarSeriesByCode(seriesName);
            if(carSeries!=null){
                task.setSeriesName(carSeries.getSeriesName());
            }
        }
        String modelName = task.getModelName();
        if(!StringUtils.isEmpty(modelName) && !StringUtils.isEmpty(seriesName)){
            CarModelDTO carmodel = carService.getCarModelByCode(seriesName,modelName);
            if(carmodel!=null){
                task.setModelName(carmodel.getModelName());
            }
        }
        String followCar = task.getFollowCar();
        if (followCar!=null && !"".equals(followCar)){
            String followCars[] = followCar.split(",");
            String followCarName = "";
            if (followCars.length > 0) {
                for (int i = 0; i < followCars.length; i++) {
                    if (followCars[i] != null && !"".equals(followCars[i].trim())) {
                        Integer metaValue = Integer.valueOf(followCars[i]);
                        MetaDTO respData = metaApiService.details(metaValue, "follow_car");
                        if (respData != null && respData.getDescription() != null) {
                            if (!"".equals(followCarName)) {
                                followCarName = followCarName + "，" + respData.getDescription();
                            } else {
                                followCarName = respData.getDescription();
                            }
                        }
                    }
                }
            }
            task.setFollowCarDes(followCarName);
        }
        String followInfo = task.getFollowInfo();
        if (followInfo!=null && !"".equals(followInfo)){
            String followInfos[] = followInfo.split(",");
            String followInfoName = "";
            if (followInfos.length > 0) {
                for (int i = 0; i < followInfos.length; i++) {
                    if (followInfos[i] != null && !"".equals(followInfos[i].trim())) {
                        Integer metaValue = Integer.valueOf(followInfos[i]);
                        MetaDTO respData = metaApiService.details(metaValue, "follow_info");
                        if (respData != null && respData.getDescription() != null) {
                            if (!"".equals(followInfoName)) {
                                followInfoName = followInfoName + "，" + respData.getDescription();
                            } else {
                                followInfoName = respData.getDescription();
                            }
                        }
                    }
                }
            }
            task.setFollowInfoDes(followInfoName);
        }
        return ResponseData.success(task);
    }

    @Override
    @RequestMapping(
            value = {"/assign"},
            method = {RequestMethod.POST}
    )
    @Transactional(rollbackFor = Exception.class,transactionManager = "leadTransactionManager")
    public ResponseData<Integer> assignTask(@RequestBody LeadTaskAssignDTO leadTaskAssign) {
        //Integer userId = leadTaskAssign.getOwner();
        List<Integer> ownerList = leadTaskAssign.getOwnerList();
        if(ownerList==null || ownerList.size() == 0){
            return ResponseData.fail("请选择一个坐席");
        }
        Integer num = leadTaskAssign.getAssignTaskCount();
        if(num==null || num<1){
            return ResponseData.fail("请输入任务数量");
        }
        Integer updateNumTotal = 0 ;
        if(ownerList.size() == 1){
            leadTaskAssign.setOwner(ownerList.get(0));
            Integer updateNum = leadTaskSearchMapper.assignTask(leadTaskAssign);
            updateNumTotal = updateNum ;
            logger.info("owner=[{}],count=[{}],updateNum=[{}]",ownerList.get(0),leadTaskAssign.getAssignTaskCount(),updateNumTotal);
        }else{
            //平均分配给作息
            int size = ownerList.size();
            if(num < size){
                ResponseData.fail("任务数量不可以小于坐席人数");
            }
            Integer a = num / size ;
            Integer b = num % size ;
            for(Integer owner : ownerList){
                leadTaskAssign.setAssignTaskCount(a);
                leadTaskAssign.setOwner(owner);
                Integer updateNum = leadTaskSearchMapper.assignTask(leadTaskAssign);
                updateNumTotal = updateNumTotal + updateNum;
                logger.info("owner=[{}],count=[{}],updateNum=[{}]",owner,a,updateNum);
            }
            if(b != 0){
                leadTaskAssign.setOwner(ownerList.get(0));
                leadTaskAssign.setAssignTaskCount(b);
                Integer updateNum = leadTaskSearchMapper.assignTask(leadTaskAssign);
                updateNumTotal = updateNumTotal + updateNum;
                logger.info("owner=[{}],count=[{}],updateNum=[{}]",ownerList.get(0),b,updateNum);
            }
            logger.info("total=====>"+updateNumTotal);
        }
        return ResponseData.success(updateNumTotal);
    }

    @Override
    @RequestMapping(
            value = {"/assignAll"},
            method = {RequestMethod.POST}
    )
    @Transactional(rollbackFor = Exception.class,transactionManager = "leadTransactionManager")
    public ResponseData<Boolean> assignAllTask(@RequestBody List<LeadTaskDTO> leadTaskList) {
        for(LeadTaskDTO dto:leadTaskList) {
            Long taskId = dto.getId();
            Integer leadId = dto.getLeadId();
            Integer userId = dto.getOwner();
            //Date date = dto.getDueDatePlan(); //创建时间+24h
            LeadTaskDTO leadTaskDTO = leadTaskSearchMapper.getLeadTaskByLeadId(leadId);
            if (leadTaskDTO == null) {
                logger.error("taskId[{}]为空",taskId);
                continue;
            }
            LeadTask leadTask = new LeadTask();
            if (leadTaskDTO.getStatus() == LEAD_TASK_STATUS_ENUME.NEW.getValue()) {
                leadTask.setStatus(LEAD_TASK_STATUS_ENUME.ASSIGN.getValue());
            }
            leadTask.setId(leadTaskDTO.getId());
            leadTask.setOwner(userId);
            leadTaskMapper.updateByPrimaryKeySelective(leadTask);
        }
        return ResponseData.success(true);
    }

    @Override
    @RequestMapping(
            value = {"/done"},
            method = {RequestMethod.GET}
    )
    @Transactional(rollbackFor = Exception.class,transactionManager = "leadTransactionManager",propagation = Propagation.REQUIRED)
    public ResponseData<Boolean> doneTask(Long taskId) {
        Integer currentUserId = SessionHelper.getCurrentUserId();
        LeadTask leadTask = leadTaskMapper.selectByPrimaryKey(taskId);
        if(leadTask==null){
            return ResponseData.fail("任务不存在");
        }
        if(!currentUserId.equals(leadTask.getOwner())){
            return ResponseData.fail("不是任务所有人,不能修改");
        }
        if(leadTask.getStatus()>=LEAD_TASK_STATUS_ENUME.COMPLETED.getValue() && leadTask.getStatus()<=LEAD_TASK_STATUS_ENUME.TEST.getValue()){
            return ResponseData.fail("该任务状态已不能修改");
        }
        /**完成呼叫*/
        leadTask.setStatus(LEAD_TASK_STATUS_ENUME.COMPLETED.getValue());
        Date now = new Date();
        leadTask.setDueDateActual(now);
        leadTaskMapper.updateByPrimaryKeySelective(leadTask);
        logger.info("任务完成,更新线索状态为已下发.");
        Integer leadId = leadTask.getLeadId();
        Lead lead = leadMapper.selectByPrimaryKey(leadId);
        String dealerId = lead.getDealer();
        if(!StringUtils.isEmpty(dealerId)) {
            distributeLead(leadId,now,currentUserId);
        }else{
            this.rabbitTemplate.convertAndSend("NO_DEALER_LEAD",leadId);
        }
        return ResponseData.success(true,"任务完成!");
    }

    @Transactional(rollbackFor = Exception.class,transactionManager = "leadTransactionManager",propagation = Propagation.REQUIRED)
    public void distributeLead(Integer leadId,Date updateDate,Integer currentUserId) {
        Lead lead = leadMapper.selectByPrimaryKey(leadId);
        Date sendTime = lead.getSendTime();
        String phone = lead.getPhone();
        String dealerId = lead.getDealer();
        if(!StringUtils.isEmpty(dealerId) && lead.getLeadStatus()==LEAD_STATUS_ENUME.CREATED.getValue()) {
            Map<String,Object> param = new HashMap<>();
            param.put("phone",phone);
            param.put("dealer",dealerId);
            logger.info("任务完成-查重:线索[{}],经销商[{}]",leadId,dealerId);
            List<Lead> leadList = leadSearchMapper.searchLeadByPhoneNoForSended(param);
            logger.info("任务完成-查重结果:线索[{}],经销商[{}],线索条数[{}]",leadId,dealerId,leadList==null?null:leadList.size());
            Lead oldLead = null;
            if(leadList!=null){
                for(Lead obj:leadList){
                    if(obj.getId().intValue()!=leadId.intValue()){
                        oldLead = obj;
                        break;
                    }
                }
            }
            if(oldLead!=null){

                logger.info("已撞单,手机号:[{}]线索ID[{}]合并到的线索ID[{}]",phone,leadId,oldLead.getId());
                logger.info("合并线索,合并Lead:[{}]",JSON.toJSONString(lead));
                logger.info("合并线索,合并前Lead:[{}]",JSON.toJSONString(oldLead));
                BeanUtils.copyPropertiesHasValue(lead,oldLead);
                logger.info("合并线索,合并后:[{}]",JSON.toJSONString(oldLead));
                oldLead.setModifyDate(updateDate);
                oldLead.setModifyBy(currentUserId);
                if(!StringUtils.isEmpty(lead.getSeriesName()) && lead.getSeriesName() != null && !lead.getSeriesName().equals(oldLead.getSeriesName())){
                    CarSeriesDTO carSeriesByCode = carService.getCarSeriesByCode(lead.getSeriesName());
                    if(StringUtils.isEmpty(oldLead.getDescription()) || oldLead.getDescription() == null){
                        if(carSeriesByCode != null){
                            oldLead.setDescription("合并车型线索 "+carSeriesByCode.getSeriesName());
                        }else{
                            oldLead.setDescription("合并车型线索 "+lead.getSeriesName());
                        }
                    }else{
                        if(carSeriesByCode != null){
                            oldLead.setDescription(oldLead.getDescription()+";合并车型线索 "+carSeriesByCode.getSeriesName());
                        }else{
                            oldLead.setDescription(oldLead.getDescription()+";合并车型线索 "+lead.getSeriesName());
                        }
                    }
                }
                leadMapper.updateByPrimaryKeySelective(oldLead);
                lead = new Lead();
                lead.setId(leadId);
                lead.setDealer(dealerId);
                lead.setModifyDate(updateDate);
                lead.setModifyBy(currentUserId);
                lead.setLeadStatus(LEAD_STATUS_ENUME.SAME_LIST.getValue());
                leadMapper.updateByPrimaryKeySelective(lead);

                logger.info("复制渠道信息newLead[{}]->oldLead[{}]...",lead.getId(),oldLead.getId());
                leadCommonService.copyChannel2OldLead(oldLead.getId(),lead.getId());

                leadCommonService.insertLeadHistory(leadId, updateDate, currentUserId, LEAD_STATUS_ENUME.SAME_LIST.getValue(),
                        LEAD_STATUS_ENUME.SAME_LIST.getDesc(),dealerId,null,null);
            }else{
                logger.info("无撞单下发到经销商lead:[{}]dealer:[{}]",phone,dealerId);
                boolean isContains = false ;
               String channelCode = preLeadSearchMapper.selectChannelByLeadId(leadId);

                if(!StringUtils.isEmpty(channelCode)){
                    Pattern pattern = Pattern.compile("[0-9]*");
                    boolean matches = pattern.matcher(channelCode).matches();
                    if(matches){
                        isContains = LeadConstants.IGNORE_DEALER_IS_DISTRIBUTED.ignoreDealerIsDistributed().contains(Integer.parseInt(channelCode));
                    }
                }
                DealerDTO dealerDTO = dealerService.detailsDealer(dealerId);
                if(dealerDTO.getIsDistribute() == 1 || isContains){
                    lead = new Lead();
                    lead.setId(leadId);
                    lead.setLeadStatus(LEAD_STATUS_ENUME.SEND.getValue());
                    lead.setModifyDate(updateDate);
                    lead.setModifyBy(currentUserId);
                    lead.setSendTime(updateDate);
                    leadMapper.updateByPrimaryKeySelective(lead);
                    leadCommonService.insertLeadHistory(leadId, updateDate, currentUserId, LEAD_STATUS_ENUME.SEND.getValue(),
                            LEAD_STATUS_ENUME.SEND.getDesc(),dealerId,null,null);
                }else{
                    logger.info("无撞单，下发到经销商lead:[{}]dealer:[{}]==================>经销商禁止下发线索无法下发",phone,dealerId);
                }
            }
        }
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "NO_DEALER_LEAD", durable = "true"),
            exchange = @Exchange("")), containerFactory="rabbitListenerContainerFactory")
    public void dealWithNoDealerLead(@Payload String msg){
        logger.info("无经销商线索自动下发[{}]",msg);
        if(NumberUtils.isDigits(msg)){
            Integer leadId = Integer.parseInt(msg);
            dealerWithNoDealerLead(leadId);
        }
    }

    @Transactional(rollbackFor = Exception.class,transactionManager = "leadTransactionManager",propagation = Propagation.REQUIRED)
    public void dealerWithNoDealerLead(Integer leadId){
        Lead lead = leadMapper.selectByPrimaryKey(leadId);
        Date now = new Date();
        Integer currentUserId = SessionHelper.getCurrentUserId();
        if(NumberUtils.isDigits(lead.getCity())) {
            Integer cityId = Integer.parseInt(lead.getCity());
            Map<String,Object> param = new HashMap<>();
            param.put("cityId",cityId);
            List<LeadDistributeConfig> cityDistributeConfigList = leadDistributeConfigSearchMapper.getCitiDistributeConfigList(param);
            if(cityDistributeConfigList!=null && cityDistributeConfigList.size()>0){
                int size = cityDistributeConfigList.size();
                int currtIdx = 0;
                int nextIdx = 0;
                if(size>1){
                    for(int i = 0; i< size; i++){
                        LeadDistributeConfig config = cityDistributeConfigList.get(i);
                        Integer isCurrent = config.getIsCurrent();
                        if(isCurrent !=null && isCurrent.intValue()==1){
                            currtIdx = i;
                            nextIdx = (currtIdx+1)%size;
                        }
                    }
                }
                LeadDistributeConfig currtConfig;
                String dealerCode;
                if(currtIdx==nextIdx){
                    currtConfig = cityDistributeConfigList.get(currtIdx);
                    currtConfig.setIsCurrent(1);
                    dealerCode = currtConfig.getDealerCode();
                    leadDistributeConfigMapper.updateByPrimaryKey(currtConfig);
                }else{
                    currtConfig = cityDistributeConfigList.get(currtIdx);
                    currtConfig.setIsCurrent(0);
                    leadDistributeConfigMapper.updateByPrimaryKey(currtConfig);
                    LeadDistributeConfig nextConfig = cityDistributeConfigList.get(nextIdx);
                    nextConfig.setIsCurrent(1);
                    leadDistributeConfigMapper.updateByPrimaryKey(nextConfig);
                    dealerCode = nextConfig.getDealerCode();
                }
                Lead entity = new Lead();
                entity.setId(leadId);
                entity.setDealer(dealerCode);
                leadMapper.updateByPrimaryKeySelective(entity);
                distributeLead(leadId,now,currentUserId);
            }else{
                logger.info("无下发城市匹配=>leadId[{}]的城市无匹配的经销商",leadId);
            }
        }else{
            logger.info("无下发城市匹配=>leadId[{}]的无城市",leadId);
        }
    }

    //@RabbitListener(queues = "CALL_SUMMARY", containerFactory="rabbitListenerContainerFactory")
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "CALL_SUMMARY", durable = "true"),
            exchange = @Exchange("direct.lead")), containerFactory="rabbitListenerContainerFactory")
    public void processCallSummary(@Payload String msg) {
        logger.info("收到电话小结:" + msg);
        dealerWithCall(msg);

    }

    @Transactional(rollbackFor = Exception.class,transactionManager = "leadTransactionManager",propagation = Propagation.REQUIRED)
    void dealerWithCall(String msg){
        CallSummaryDTO callSummary = JSON.parseObject(msg,CallSummaryDTO.class);

        String nextCallTime = callSummary.getNextCallTime();
        Integer taskId = callSummary.getTaskId();
        if(taskId==null){
            return;
        }
        LeadTask leadTask = leadTaskMapper.selectByPrimaryKey(Long.valueOf(taskId));
        if(leadTask==null){
            logger.error("taskID[{}]无效",taskId);
            return;
        }
        leadTask.setNextCallTime(nextCallTime);
        if(leadTask.getFirstCallTime()==null) {
            leadTask.setFirstCallTime(callSummary.getCallTime());
        }
        leadTask.setCallTimes(callSummary.getCallCounts());
        Date cdrstoptime = callSummary.getCdrStopTime();
        if(cdrstoptime!=null){
            leadTask.setCdrStopTime(cdrstoptime);
        }
        Integer status=leadTask.getStatus();
        if(status>6 && status<11){
            logger.info("任务完结.taskid[]",taskId);
            return;
        }
        Date now = new Date();
        if(callSummary.getCallResult()!=null && NumberUtils.isDigits(callSummary.getCallResult())){
            Integer callResult = Integer.parseInt(callSummary.getCallResult());
            if(callResult==1||callResult==5){
                status = LEAD_TASK_STATUS_ENUME.CALLED.getValue();
            }else if(callResult==7){
                status = LEAD_TASK_STATUS_ENUME.DONT_CONTACT.getValue();
            }else if(callResult==8){
                status = LEAD_TASK_STATUS_ENUME.ERROR_NBR.getValue();
            }else if(callResult==9){
                status = LEAD_TASK_STATUS_ENUME.TEST.getValue();
            }else if(callResult== 11){
                status = LEAD_TASK_STATUS_ENUME.COMPLETED.getValue();
            }else{
                switch (status){
                    case 1:
                        status = LEAD_TASK_STATUS_ENUME.FAILED.getValue();
                        break;
                    case 2:
                        status = LEAD_TASK_STATUS_ENUME.FAILED.getValue();
                        break;
                    case 4:
                        status = LEAD_TASK_STATUS_ENUME.REFAILED.getValue();
                        break;
                    case 5:
                        status = LEAD_TASK_STATUS_ENUME.REJECT.getValue();
                        break;
                    case 6:
                        status = LEAD_TASK_STATUS_ENUME.FAILED_FOURTH.getValue();
                        break;
                    case 11:
                        status = LEAD_TASK_STATUS_ENUME.FAILED_FIFTH.getValue();
                        break;
                    case 12:
                        status = LEAD_TASK_STATUS_ENUME.FAILED_SIX.getValue();
                        break;
                    case 13:
                        status = LEAD_TASK_STATUS_ENUME.FAILED_SEVEN.getValue();
                        break;
                    case 14:
                        status = LEAD_TASK_STATUS_ENUME.FAILED_EIGHT.getValue();
                        break;
                    case 15:
                        status = LEAD_TASK_STATUS_ENUME.FAILED_NINE.getValue();
                        break;
                    case 16:
                        status = LEAD_TASK_STATUS_ENUME.FAILED_TEN.getValue();
                        break;
                    default:;
                }
            }
        }
        if(status>LEAD_TASK_STATUS_ENUME.COMPLETED.getValue() && status<LEAD_TASK_STATUS_ENUME.TEST.getValue()){
            Integer leadId = leadTask.getLeadId();
            Lead lead = new Lead();
            lead.setId(leadId);
            lead.setLeadStatus(LEAD_STATUS_ENUME.REJECT.getValue());
            lead.setModifyBy(callSummary.getCrmUserId());
            lead.setModifyDate(now);
            lead.setDealer(null);
            leadMapper.updateByPrimaryKeySelective(lead);
            leadCommonService.insertLeadHistory(leadId,now,callSummary.getCrmUserId(),
                    LEAD_STATUS_ENUME.REJECT.getValue(),LEAD_STATUS_ENUME.REJECT.getDesc()
                    ,null,null,null);
        }else if(status==LEAD_TASK_STATUS_ENUME.TEST.getValue().intValue()){
            Integer leadId = leadTask.getLeadId();
            Lead lead = new Lead();
            lead.setId(leadId);
            lead.setLeadStatus(LEAD_STATUS_ENUME.TEST.getValue());
            lead.setModifyBy(callSummary.getCrmUserId());
            lead.setModifyDate(now);
            lead.setDealer(null);
            leadMapper.updateByPrimaryKeySelective(lead);
            leadCommonService.insertLeadHistory(leadId,now,callSummary.getCrmUserId(),
                    LEAD_STATUS_ENUME.TEST.getValue(),LEAD_STATUS_ENUME.TEST.getDesc()
                    ,null,null,null);
        }
        leadTask.setStatus(status);
        leadTaskMapper.updateByPrimaryKeySelective(leadTask);
    }
}
