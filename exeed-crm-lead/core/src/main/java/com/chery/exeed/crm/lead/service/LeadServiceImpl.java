package com.chery.exeed.crm.lead.service;

import com.alibaba.fastjson.JSON;
import com.chery.exeed.crm.common.annotation.ExecutiveAnalysis;
import com.chery.exeed.crm.common.dto.*;
import com.chery.exeed.crm.common.service.*;
import com.chery.exeed.crm.common.util.*;
import com.chery.exeed.crm.lead.constants.LeadConstants.LEAD_STATUS_ENUME;
import com.chery.exeed.crm.lead.dto.*;
import com.chery.exeed.crm.lead.mapper.*;
import com.chery.exeed.crm.lead.model.Lead;
import com.chery.exeed.crm.lead.model.LeadTask;
import com.chery.exeed.crm.lead.model.OrderOperateRecord;
import com.chery.exeed.crm.sysadmin.dto.UserDetailDTO;
import com.chery.exeed.crm.sysadmin.service.SysUserService;
import com.chery.exeed.ifs.common.dto.CarModelDTO;
import com.chery.exeed.ifs.common.dto.CarSeriesDTO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.servicecomb.provider.pojo.RpcReference;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

import static com.chery.exeed.crm.common.constants.Constants.ILLEGAL_USER_OPERATE_CODE;
import static com.chery.exeed.crm.common.util.CommonUtil.stringIsNotEmpty;

@Transactional
@RestSchema(schemaId = "lead-service")
@RequestMapping(path = "/apis/lead")
public class LeadServiceImpl implements LeadService{
    private static Logger logger = LoggerFactory.getLogger(LeadServiceImpl.class);
    @Autowired
    private DealerService dealerService;
    @Autowired
    private RegionService regionService;
    @Autowired
    public RabbitTemplate rabbitTemplate;
    @Autowired
    private LeadMapper leadMapper;
    @Autowired
    private LeadSearchMapper leadSearchMapper;
    @Autowired
    private LeadTaskSearchMapper leadTaskSearchMapper;
    @Autowired
    private LeadCommonService leadCommonService;
    @Autowired
    private CarService carService;
    @Autowired
    private SalesAssistantService salesAssistantService;
    @Autowired
    private LeadFollowupSearchMapper leadFollowupSearchMapper;
    @Autowired
    private LeadChannelSearchMapper leadChannelSearchMapper;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private MetaApiService metaApiService;
    @Autowired
    private ViewService viewService;
    @RpcReference(microserviceName = "exeed-crm-apis:exeed-crm-sysadmin", schemaId = "user-service")
    private SysUserService userService;
    @Autowired
    private OrderOperateRecordSearchMapper orderOperateRecordSearchMapper;
    @Override
    @RequestMapping(
            value = {"/list"},
            method = {RequestMethod.POST}
    )
    public ResponseData<PaginationResult<LeadResultDTO>> list(@RequestBody LeadDTO leadDTO) {
        Integer currentUserId = SessionHelper.getCurrentUserId();
        logger.info("??????id???:"+currentUserId);
        UserDetailDTO userInfo = this.leadCommonService.getUserInfo(currentUserId);
        logger.info("????????????:"+userInfo);
        if ((userInfo.getIsConsultant() != null && userInfo.getIsConsultant().intValue() != 0) || (userInfo
                .getIsExpSpecialists() != null && userInfo.getIsExpSpecialists().intValue() != 0) || (userInfo
                .getIsOrderManager() != null && userInfo.getIsOrderManager().intValue() != 0))
            leadDTO.setDealer(userInfo.getDealerId());
        Integer currentUserType = SessionHelper.getCurrentUserType();
        if (currentUserType != null && currentUserType.intValue() == 3)
            leadDTO.setOwner(currentUserId);
        if (leadDTO.getSearchCode() != null && !StringUtils.isEmpty(leadDTO.getSearchCode()) && "3009".equals(leadDTO.getSearchCode()))
            leadDTO.setCurrentUserId(currentUserId);
        Integer pageStart = (leadDTO.getPageNo() - 1) * leadDTO.getPageSize();
        leadDTO.setPageStart(pageStart);

        Long totalCount = Long.valueOf(this.leadSearchMapper.countLead(leadDTO));
        //logger.info("Lead list total count : {}; pageNo : {}, pageSize : {}, pageStart : {}", new Object[] { totalCount, leadDTO.getPageNo(), leadDTO.getPageSize(), leadDTO.getPageStart() });
        List<LeadResultDTO> data = this.leadSearchMapper.queryLeadListByPage(leadDTO);
        if (!CollectionUtils.isEmpty(data)) {
            setChannelInfo(data);
            setValueForList(data);
        }
        PaginationResult<LeadResultDTO> result = new PaginationResult();
        result.setTotalCount(totalCount);
        result.setData(data);
        return ResponseData.success(result);
    }

    @Override
    @RequestMapping(
            value = {"/listByAssistant"},
            method = {RequestMethod.POST}
    )
    public ResponseData<PaginationResult<LeadResultDTO>>

    listByAssistant(@RequestBody LeadDTO leadDTO) {
        Integer currentUserId = SessionHelper.getCurrentUserId();
        UserDetailDTO userInfo = leadCommonService.getUserInfo(currentUserId);

        if((userInfo.getIsConsultant()!=null && userInfo.getIsConsultant()!=0)||(userInfo.getIsExpSpecialists()!=null && userInfo.getIsExpSpecialists()!=0)||(userInfo.getIsOrderManager()!=null && userInfo.getIsOrderManager()!=0)) {
            leadDTO.setDealer(userInfo.getDealerId());
        }
        if(leadDTO.getAssistantResourceType() != null && leadDTO.getAssistantResourceType().intValue() == 2){
            //????????????????????????
            leadDTO.setOwner(currentUserId);
        }
//        PageHelper.startPage(leadDTO.getPageNo(), leadDTO.getPageSize());
        logger.info("listByAssistant   ===> leadDTO ===>createDate = "+leadDTO.getCreateDate());
        List<LeadResultDTO> data = leadSearchMapper.queryLeadListByAssisant(leadDTO);
        Integer totals=leadSearchMapper.queryLeadListByAssisantTotal(leadDTO);
        logger.info("owner=[{}],dealer=[{}],leadStatus=[{}] listByAssistant========> result.size=[{}]",leadDTO.getOwner(),leadDTO.getDealer(),leadDTO.getLeadStatus(),data.size());
        List<LeadResultDTO> list = new ArrayList<>();
        /**????????????*/
        for (int i  = 0;i < data.size();i++) {

            //????????????????????????
//            data.get(i).setLeadHeatingCount(leadSearchMapper.getLeadHeatingCount(data.get(i).getId()));//????????????
            LeadResultDTO rs = data.get(i);
            rs.setTheKeyInformation("?????? "+rs.getFollowupTimes()+"???  ?????? "+rs.getArrivalTimes()+"???  ?????? "+rs.getPredictTimes()+"???");
            Integer followupTimes = rs.getFollowupTimes();
//            if(followupTimes != null && followupTimes != 0){
//                rs.setFollowupTimes(followupTimes -1);
//            }
            Integer leadId = rs.getId();
            List<LeadChannelDTO> leadChannelList = leadChannelSearchMapper.getLeadChannelList(leadId);
            if(leadChannelList != null){
                rs.setChannelList(leadChannelList);
            }
            if(rs.getLeadStatus().intValue() == LEAD_STATUS_ENUME.ORDER.getValue() || rs.getLeadStatus().intValue() == LEAD_STATUS_ENUME.WIN.getValue() ||rs.getLeadStatus().intValue() == LEAD_STATUS_ENUME.LOSE.getValue()  ){
                list.add(rs);
                data.remove(rs);
                i-- ;
            }

//            String seriesName = rs.getSeriesName();//???????????????
//            if(!StringUtils.isEmpty(seriesName)){
//                CarSeriesDTO carSeries = carService.getCarSeriesByCode(seriesName);
//                if(carSeries!=null){
//                    rs.setSeriesName(carSeries.getSeriesName());
//                }
//            }
//            String modelName = rs.getModelName();
//            if(!StringUtils.isEmpty(modelName) && !StringUtils.isEmpty(seriesName)){
//                CarModelDTO carmodel = carService.getCarModelByCode(seriesName,modelName);
//                if(carmodel!=null){
//                    rs.setModelName(carmodel.getModelName());
//                }
//            }
        }
        if(list != null && list.size() > 0){
            data.addAll(list);
        }
        this.setValueForList(data);
//        PageInfo<LeadResultDTO> page = new PageInfo(data);
//        page.setPageSize(leadDTO.getPageSize());
        PaginationResult<LeadResultDTO> result = new PaginationResult<>();
//        result.setTotalCount(page.getTotal());
        result.setTotalCount((long)totals);
        result.setData(data);
        return ResponseData.success(result);
    }

    @Override
    @RequestMapping(
            value = {"/otherLeadListByAssistant"},
            method = {RequestMethod.POST}
    )
    public ResponseData<PaginationResult<LeadResultDTO>> otherLeadListByAssistant(@RequestBody LeadDTO leadDTO) {
        Integer userId = SessionHelper.getCurrentUserId();
        UserDetailDTO user = leadCommonService.getUserInfo(userId);
        String dealerId = user.getDealerId();

        ResponseData<List<Integer>> resultData= userService.getDeletedUserListByDealer(dealerId);
        List<Integer> userList = new ArrayList<>();
        if(resultData.getData().size() > 0){
            userList = resultData.getData();
        }

        PageHelper.startPage(leadDTO.getPageNo(), leadDTO.getPageSize());
        List<LeadResultDTO> data = leadSearchMapper.searchDealerLeadOthers(dealerId,userList,leadDTO);
        PageInfo<LeadResultDTO> page = new PageInfo(data);
        page.setPageSize(leadDTO.getPageSize());
        PaginationResult<LeadResultDTO> result = new PaginationResult<>();
        result.setTotalCount(page.getTotal());
        result.setData(data);
        return ResponseData.success(result);
    }


    @ExecutiveAnalysis
    private void setValueForList(List<LeadResultDTO> data){
        try {
            List<AssistantProvinceDTO> assistantProvinceDTOS = regionService.queryProvinceByAssistant();
            List<AssistantCityDTO>  assistantCityDTOS= regionService.queryCityByAssistant();
            for (LeadResultDTO detail : data) {
                String province = detail.getProvince();
                String city = detail.getCity();
                String seriesCode = detail.getSeriesName();
                String modelCode = detail.getModelName();
                String dealer = detail.getDealer();
                for(AssistantProvinceDTO assistantProvinceDTO:assistantProvinceDTOS){
                    if(null!=province && province.equals(assistantProvinceDTO.getValue())){
                        detail.setProvince(assistantProvinceDTO.getName());
                        break;
                    }
                }
                for(AssistantCityDTO assistantCityDTO:assistantCityDTOS){
                    if(null!=city && city.equals(assistantCityDTO.getValue())){
                        detail.setCity(assistantCityDTO.getName());
                        break;
                    }
                }
//                detail.setProvince(viewService.matchRegionNameInRegion(province));
//                detail.setCity(viewService.matchRegionNameInRegion(city));
                detail.setSeriesName(viewService.matchSeriesNameInCarSeries(seriesCode));
                detail.setModelName(viewService.matchModelNameInCarModel(seriesCode, modelCode));
                DealerViewDTO dealerViewDTO = viewService.matchDealer(dealer);
                if (dealerViewDTO != null) {
                    detail.setDealerName(dealerViewDTO.getDealerName());
                }
            }
        } catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    @ExecutiveAnalysis
    private void setChannelInfo(List<LeadResultDTO> data) {
        data.stream().forEach(item -> {
            List<LeadChannelDTO> list = leadChannelSearchMapper.listMainDataByLeadId(item.getId());
            if (!CollectionUtils.isEmpty(list)) {
                list.stream().forEach(subItem -> {
                    String channelName = viewService.matchEntityFullNameInCampaignChannel(subItem.getChannelCode());
                    subItem.setChannelName(channelName);
                    subItem.setCustName(item.getFirstName());
                    subItem.setSubChannelName(channelName);
                });
            }

            item.setChannelList(list);
        });
    }

    @Override
    @RequestMapping(
            value = {"/unAssignLead"},
            method = {RequestMethod.POST}
    )
    public ResponseData<Integer> unAssignLead(@RequestBody LeadDTO leadDTO) {
        Integer num = leadSearchMapper.unAssignLead(leadDTO);
        return ResponseData.success(num);
    }

    @Override
    @RequestMapping(
            value = {"/detail"},
            method = {RequestMethod.POST}
    )
    public ResponseData<LeadResultDTO> detail(Integer leadId) {
        Integer currentUserId = SessionHelper.getCurrentUserId();
        UserDetailDTO userInfo = leadCommonService.getUserInfo(currentUserId);
        Map<String,Object> param = new HashMap<>();
        param.put("leadId",leadId);
        if(userInfo!=null && userInfo.getDealerId()!=null){
            param.put("dealer",userInfo.getDealerId());
        }
        LeadResultDTO detail = leadSearchMapper.getLeadById(param);
        if(detail==null){
            return ResponseData.success(detail);
        }
        if(detail.getCreatedBy()!=null) {
            detail.setCreatedByName(leadCommonService.getUserName(detail.getCreatedBy()));
        }
        if(detail.getModifyBy()!=null) {
            detail.setModifyByName(leadCommonService.getUserName(detail.getModifyBy()));
        }
        if(detail.getOwner()!=null){
            detail.setOwnerName(leadCommonService.getUserName(detail.getOwner()));
        }
        if(NumberUtils.isDigits(detail.getDealerOrderManager())){
            detail.setDealerOrderManager(leadCommonService.getUserName(Integer.parseInt(detail.getDealerOrderManager())));
        }

        String city = detail.getCity();
        if (city!=null && NumberUtils.isDigits(city)){
            RegionDTO regionDTO = regionService.detailsRegion(Integer.valueOf(city));
            if (regionDTO!=null){
                detail.setCity(regionDTO.getRegionName());
            }
        }

        String province = detail.getProvince();
        if (province!=null && NumberUtils.isDigits(province)){
            RegionDTO regionDTO = regionService.detailsRegion(Integer.valueOf(province));
            if (regionDTO!=null){
                detail.setProvince(regionDTO.getRegionName());
            }
        }

        String subcity = detail.getSubCity();
        if (subcity!=null && NumberUtils.isDigits(subcity)){
            RegionDTO regionDTO = regionService.detailsRegion(Integer.parseInt(subcity));
            if (regionDTO!=null){
                detail.setSubCity(regionDTO.getRegionName());
            }
        }
        String dealer = detail.getDealer();
        if(!StringUtils.isEmpty(dealer)) {
            DealerDTO dealerDTO = dealerService.detailsDealer(dealer);
            if(dealerDTO!=null) {
                detail.setDealerName(dealerDTO.getDealerName());
            }
        }
        //??????followPlan
        if(detail.getFollowPlan() == null){
            detail.setFollowPlan(DateUtils.dayOffset(new Date(),1));
        }

        String seriesName = detail.getSeriesName();
        if(!StringUtils.isEmpty(seriesName)){
            CarSeriesDTO carSeries = carService.getCarSeriesByCode(seriesName);
            if(carSeries!=null){
                detail.setSeriesName(carSeries.getSeriesName());
            }
        }
        String modelName = detail.getModelName();
        if(!StringUtils.isEmpty(modelName) && !StringUtils.isEmpty(seriesName)){
            CarModelDTO carmodel = carService.getCarModelByCode(seriesName,modelName);
            if(carmodel!=null){
                detail.setModelName(carmodel.getModelName());
            }
        }
        String followCar = detail.getFollowCar();
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
                                followCarName = followCarName + "???" + respData.getDescription();
                            } else {
                                followCarName = respData.getDescription();
                            }
                        }
                    }
                }
            }
            detail.setFollowCarDes(followCarName);
        }
        String followInfo = detail.getFollowInfo();
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
                                followInfoName = followInfoName + "???" + respData.getDescription();
                            } else {
                                followInfoName = respData.getDescription();
                            }
                        }
                    }
                }
            }
            detail.setFollowInfoDes(followInfoName);
        }
        return ResponseData.success(detail);
    }


    @Override
    @RequestMapping(
            value = {"/detailForUpdate"},
            method = {RequestMethod.GET}
    )
    public ResponseData<LeadDTO> detailForUpdate(Integer leadId) {
        LeadDTO detail = leadSearchMapper.detailForUpdate(leadId);
        Integer createdBy = detail.getCreatedBy();
        if(createdBy != null){
            detail.setCreateByDesc(leadCommonService.getUserName(createdBy));
        }
        String dealer = detail.getDealer();
        if(dealer!=null && NumberUtils.isDigits(dealer)) {
            DealerDTO dealerDTO = dealerService.detailsDealer(dealer);
            if(dealerDTO!=null) {
                detail.setDealerName(dealerDTO.getDealerName());
            }
        }

        return ResponseData.success(detail);
    }

    @Override
    @RequestMapping(
            value = {"/detailForUpdateByAssistant"},
            method = {RequestMethod.GET}
    )
    public ResponseData<LeadDTO> detailForUpdateByAssistant(Integer leadId) {
        LeadDTO detail = leadSearchMapper.detailForUpdate(leadId);
        /**????????????*/
        Integer followupTimes = leadFollowupSearchMapper.queryFollowupTimes(leadId);
        detail.setFollowupTimes(followupTimes);
        List<LeadChannelDTO> leadChannelList = leadChannelSearchMapper.getLeadChannelList(detail.getId());
        logger.debug("leadId="+leadId+",????????????="+followupTimes);
        detail.setConsultingFrequency(leadChannelList.size());
        Integer createdBy = detail.getCreatedBy();
        if(createdBy != null){
            detail.setCreateByDesc(leadCommonService.getUserName(createdBy));
        }
        String dealer = detail.getDealer();
        if(dealer!=null && NumberUtils.isDigits(dealer)) {
            DealerDTO dealerDTO = dealerService.detailsDealer(dealer);
            if(dealerDTO!=null) {
                detail.setDealerName(dealerDTO.getDealerName());
            }
        }

        return ResponseData.success(detail);
    }

    /**??????????????????*/
    @Override
    @RequestMapping(
            value = {"/distribute"},
            method = {RequestMethod.GET}
    )
    @Transactional(rollbackFor = Exception.class,transactionManager = "leadTransactionManager")
    public ResponseData distributeLead(String leadIds,String dealerId) {
        String leadIdStrs[] = leadIds.split(",");
        Date now = new Date();
        logger.info("????????????????????????,??????[" + leadIdStrs + "], dealerId:" + dealerId);
        DealerDTO dealer = dealerService.detailsDealer(dealerId);
        if(dealer==null){
            return new ResponseData(103001, "??????????????????");
        }

        for (String str : leadIdStrs) {
            Integer leadId = Integer.parseInt(str);
            Lead lead = leadMapper.selectByPrimaryKey(leadId);
            if (lead == null || lead.getLeadStatus() == null) {
                return new ResponseData(103001, "lead????????????");
            }
            if (lead.getLeadStatus().intValue() != 0) {
                return new ResponseData(103002, "lead????????????");
            }
            //??????????????????
            Date sendTime = lead.getSendTime();
            lead = new Lead();
            LeadTaskDTO leadTask = leadTaskSearchMapper.getLeadTaskByLeadId(leadId);
//            int i =  leadTaskSearchMapper.isBigManager(dealerId);//BigManager??????????????????????????????????????????
            /**??????????????????????????????,??????????????????;????????????????????????*/
            if(leadTask==null){
                lead.setLeadStatus(LEAD_STATUS_ENUME.SEND.getValue());
                lead.setSendTime(now);
            }
//            if(0 == i){
//                List list = leadTaskSearchMapper.selectManager(dealerId);
//                lead.setOwner((Integer) list.get(0));
//            }
            lead.setId(leadId);
            lead.setDealer(dealerId);
            lead.setModifyDate(now);
            leadMapper.updateByPrimaryKey(lead);
        }
        return ResponseData.success("????????????");
    }

    /***??????????????????*/
    @RequestMapping(
            value = {"/distributeAll"},
            method = {RequestMethod.GET}
    )
    @Override
    @Transactional(rollbackFor = Exception.class,transactionManager = "leadTransactionManager",propagation = Propagation.REQUIRED)
    public ResponseData<Boolean> distributeLeadAll(@RequestBody List<LeadDealerDTO> leadDealerList) {
        Date now = new Date();
        Integer currentUserId = SessionHelper.getCurrentUserId();
        if(leadDealerList==null){
            return ResponseData.fail("????????????????????????????????????");
        }
        logger.info("??????????????????..");
        Integer haveTaskNum = 0;//????????????????????????
        Map<String,com.chery.exeed.crm.common.dto.DealerDTO> dealerMap = new HashMap<>();
        for (LeadDealerDTO dto : leadDealerList) {
            String dealerId = dto.getDealerId();
            Integer leadId = dto.getLeadId();
            DealerDTO dealer = dealerService.detailsDealer(dealerId);
            if(dealer==null) {
                dealer = dealerService.detailsDealer(dealerId);
                dealerMap.put(dealerId,dealer);
            }
            if (dealer == null) {
                logger.error("??????????????????");
                continue;
            }
            Lead lead = leadMapper.selectByPrimaryKey(leadId);
            if (lead == null || lead.getLeadStatus() == null) {
                logger.error("??????????????????");
                continue;
            }
            if (lead.getLeadStatus()==LEAD_STATUS_ENUME.SEND.getValue() || !StringUtils.isEmpty(lead.getDealer())) {
                logger.error("??????[{}]?????????????????????[{}]",lead.getPhone(),lead.getDealer());
                dto.setDealerId(null);
            }
            String phone = lead.getPhone();
            if (StringUtils.isEmpty(phone)) {
                logger.error("??????????????????");
                continue;
            }
            if (lead.getLeadStatus().intValue() != LEAD_STATUS_ENUME.CREATED.getValue()) {
                logger.error("lead[{}]????????????",lead.getId());
                continue;
            }
            LeadTaskDTO leadTask = leadTaskSearchMapper.getLeadTaskByLeadId(leadId);
            if(leadTask==null) {
                Map<String,Object> param = new HashMap<>();
                param.put("phone",phone);
                param.put("dealer",dealerId);
                logger.info("????????????-??????:??????[{}],?????????[{}]",phone,dealerId);
                List<Lead> leadList = leadSearchMapper.searchLeadByPhoneNoForSended(param);
                logger.info("????????????-??????:??????[{}],?????????[{}],????????????[{}]",phone,dealerId,leadList==null?null:leadList.size());
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
                    logger.info("?????????,?????????:[{}]??????ID[{}]??????????????????ID[{}]",phone,leadId,oldLead.getId());
                    logger.info("????????????,??????Lead:[{}]",JSON.toJSONString(lead));
                    logger.info("????????????,?????????Lead:[{}]",JSON.toJSONString(oldLead));
                    BeanUtils.copyPropertiesHasValue(lead,oldLead);
                    System.out.println("????????????,?????????:===="+JSON.toJSONString(oldLead));
                    oldLead.setModifyDate(now);
                    oldLead.setModifyBy(currentUserId);
                    if(!StringUtils.isEmpty(lead.getSeriesName()) && lead.getSeriesName() != null && !lead.getSeriesName().equals(oldLead.getSeriesName())){
                        CarSeriesDTO carSeriesByCode = carService.getCarSeriesByCode(lead.getSeriesName());
                        if(StringUtils.isEmpty(oldLead.getDescription()) || oldLead.getDescription() == null){
                            if(carSeriesByCode != null){
                                oldLead.setDescription("?????????????????? "+carSeriesByCode.getSeriesName());
                            }else{
                                oldLead.setDescription("?????????????????? "+lead.getSeriesName());
                            }
                        }else{
                            if(carSeriesByCode != null){
                                oldLead.setDescription(oldLead.getDescription()+";?????????????????? "+carSeriesByCode.getSeriesName());
                            }else{
                                oldLead.setDescription(oldLead.getDescription()+";?????????????????? "+lead.getSeriesName());
                            }
                        }
                    }
                    leadMapper.updateByPrimaryKeySelective(oldLead);
                    logger.info("??????????????????newLead[{}]->oldLead[{}]...",lead.getId(),oldLead.getId());
                    leadCommonService.copyChannel2OldLead(oldLead.getId(),lead.getId());

                    lead = new Lead();
                    lead.setLeadStatus(LEAD_STATUS_ENUME.SAME_LIST.getValue());
                    leadCommonService.insertLeadHistory(leadId,now,currentUserId,LEAD_STATUS_ENUME.SAME_LIST.getValue(),LEAD_STATUS_ENUME.SAME_LIST.getDesc()
                            ,dealerId,null,null);
                }else{
                    logger.info("??????????????????");
                    lead.setSendTime(now);
                    lead.setLeadStatus(LEAD_STATUS_ENUME.SEND.getValue());
                    leadCommonService.insertLeadHistory(leadId,now,currentUserId,LEAD_STATUS_ENUME.SEND.getValue(),LEAD_STATUS_ENUME.SEND.getDesc()
                            ,dealerId,null,null);
                }
            }else{
                haveTaskNum++;
            }
            //??????????????????
            lead.setId(leadId);
            lead.setDealer(dto.getDealerId());
            lead.setModifyDate(now);
            lead.setModifyBy(currentUserId);
            leadMapper.updateByPrimaryKeySelective(lead);
        }
        String tip;
        if(haveTaskNum>0){
            tip="????????????!"+haveTaskNum+"????????????????????????????????????????????????????????????????????????";
            return ResponseData.fail(tip);
        }else{
            tip = "????????????!";
        }
        return ResponseData.success(true,tip);
    }

    @Override
    @RequestMapping(
            value = {"/assign"},
            method = {RequestMethod.GET}
    )
    @Transactional(rollbackFor = Exception.class,transactionManager = "leadTransactionManager",propagation = Propagation.REQUIRED)
    public ResponseData<Boolean> assign(Integer leadId,Integer userId){
        logger.info("leadId[{}]userId[{}]",leadId,userId);
        if(leadId==null || userId==null){
            return ResponseData.fail("????????????");
        }
        Lead lead = leadMapper.selectByPrimaryKey(leadId);
        if(lead==null){
            return ResponseData.fail("????????????");
        }
        if(lead.getLeadStatus().equals(LEAD_STATUS_ENUME.CREATED.getValue())){
            return ResponseData.fail("??????????????????????????????");
        }
        if(StringUtils.isEmpty(lead.getLevel())){
            return ResponseData.fail("???????????????????????????");
        }
        if(!lead.getLeadStatus().equals(LEAD_STATUS_ENUME.SEND.getValue())){
            return ResponseData.fail("???????????????'?????????'??????????????????");
        }
        if (org.apache.commons.lang.StringUtils.isEmpty(lead.getSeriesName())
                || org.apache.commons.lang.StringUtils.isEmpty(lead.getModelName())) {
            return ResponseData.fail("??????????????????????????????");
        }
        Integer currentUserId = SessionHelper.getCurrentUserId();
        UserDetailDTO currentUser = leadCommonService.getUserInfo(currentUserId);
        if(currentUser.getIsExpSpecialists()==null ||
                currentUser.getIsExpSpecialists()!=1
                || StringUtils.isEmpty(currentUser.getDealerId())){
            return ResponseData.fail("???????????????");
        }
        UserDetailDTO orderUser = leadCommonService.getUserInfo(userId);
        if(orderUser==null||orderUser.getIsOrderManager()==null||
                orderUser.getIsOrderManager()!=1
                ||!currentUser.getDealerId().equals(orderUser.getDealerId())){
            return ResponseData.fail("??????????????????????????????");
        }
        Integer leadStatus = lead.getLeadStatus();
        Date followTime = lead.getFollowTime();
        lead = new Lead();
        lead.setId(leadId);
        Date now = new Date();
        lead.setDealerOrderManager(String.valueOf(userId));
        /**??????????????????,?????????????????????*/
        if(leadStatus==LEAD_STATUS_ENUME.SEND.getValue()) {
            leadStatus = LEAD_STATUS_ENUME.NOT_DISTRIBUTE.getValue();
            if(currentUser.getIsExpSpecialists()!=null && currentUser.getIsExpSpecialists()==1 && followTime == null){
                lead.setFollowTime(now);
                lead.setFollowBy(currentUserId);
            }
        }
        lead.setLeadStatus(leadStatus);
        lead.setModifyDate(now);
        lead.setModifyBy(currentUserId);
        leadMapper.updateByPrimaryKeySelective(lead);
        leadCommonService.insertLeadHistory(leadId,now,currentUserId,leadStatus,LEAD_STATUS_ENUME.valueOf(leadStatus).getDesc()
                ,orderUser.getDealerId(),orderUser.getUserId(),null);

        return ResponseData.success(true);
    }

    @Override
    @RequestMapping(
            value = {"/batchAssign"},
            method = {RequestMethod.GET}
    )
    @Transactional(rollbackFor = Exception.class,transactionManager = "leadTransactionManager",propagation = Propagation.REQUIRED)
    public ResponseData<Integer> batchAssign(Integer userId, Integer num){
        Integer currentUserId = SessionHelper.getCurrentUserId();
        if(num==null||num<1){
            return ResponseData.fail("?????????????????????");
        }
        UserDetailDTO currentUser = leadCommonService.getUserInfo(currentUserId);
        if(currentUser.getIsExpSpecialists()==null ||
                currentUser.getIsExpSpecialists()!=1
                || StringUtils.isEmpty(currentUser.getDealerId())){
            return ResponseData.fail("???????????????");
        }
        UserDetailDTO orderUser = leadCommonService.getUserInfo(userId);
        List<String> dealerIdList = leadCommonService.getUserDealerList(currentUserId);
        if(orderUser==null||orderUser.getIsOrderManager()==null||
                orderUser.getIsOrderManager()!=1
                ||!dealerIdList.contains(orderUser.getDealerId())){
            return ResponseData.fail("??????????????????????????????");
        }
        Date now = new Date();
        List<Lead> leadList = leadSearchMapper.selectSendedLeadByDealerId(orderUser.getDealerId(),num);
        Integer assignNum = 0;
        Integer notAssignNum = 0;
        int i=0;
        if(leadList!=null){
            for(Lead lead:leadList){
                if(lead.getSeriesName() == null || StringUtils.isEmpty(lead.getSeriesName())){
                    notAssignNum += 1;
                    continue;
                }
                Integer leadId = lead.getId();
                lead = new Lead();
                lead.setId(leadId);
                Integer leadStatus = lead.getLeadStatus();
                if(leadStatus==LEAD_STATUS_ENUME.SEND.getValue()) {
                    if(currentUser.getIsExpSpecialists()!=null && currentUser.getIsExpSpecialists()==1 && lead.getFollowTime()==null){
                        lead.setFollowTime(now);
                        lead.setFollowBy(currentUserId);
                    }
                }
                lead.setFollowBy(currentUserId);
                lead.setDealerOrderManager(String.valueOf(userId));
                lead.setLeadStatus(LEAD_STATUS_ENUME.NOT_DISTRIBUTE.getValue());
                leadCommonService.insertLeadHistory(leadId,new Date(),currentUserId,LEAD_STATUS_ENUME.NOT_DISTRIBUTE.getValue(),
                        LEAD_STATUS_ENUME.NOT_DISTRIBUTE.getDesc(),currentUser.getDealerId(),userId,null);
                lead.setModifyDate(now);
                lead.setModifyBy(currentUserId);
                i=leadMapper.updateByPrimaryKeySelective(lead);
                assignNum+=i;
            }
        }
        if(assignNum==0){
            return ResponseData.fail("??????????????????????????????????????????");
        }
        if(notAssignNum > 0){
            ResponseData.success(assignNum,assignNum+"?????????????????????,"+notAssignNum+"?????????????????????????????????????????????");
        }
        return ResponseData.success(assignNum,assignNum+"?????????????????????");
    }

    @Override
    @RequestMapping(
            value = {"/pass"},
            method = {RequestMethod.GET}
    )
    @Transactional(rollbackFor = Exception.class,transactionManager = "leadTransactionManager")
    @Deprecated
    public ResponseData<Boolean> pass(Integer leadId){

        return ResponseData.success(true,"????????????!");
    }

    @Override
    @RequestMapping(
            value = {"/reject"},
            method = {RequestMethod.GET}
    )
    @Transactional(rollbackFor = Exception.class,transactionManager = "leadTransactionManager",propagation = Propagation.REQUIRED)
    public ResponseData<Boolean> reject(Integer leadId, String reason,Integer rejectReasonType){
        Integer currentUserId = SessionHelper.getCurrentUserId();
        UserDetailDTO userInfo = leadCommonService.getUserInfo(currentUserId);
        if(leadId==null){
            return ResponseData.fail("????????????!");
        }
        Lead lead = leadMapper.selectByPrimaryKey(leadId);
        if(lead==null){
            return ResponseData.fail("??????ID??????!");
        }
        if(StringUtils.isEmpty(lead.getDealer())){
            return ResponseData.fail("???????????????!");
        }
        if(!userInfo.getDealerId().equals(lead.getDealer())){
            return ResponseData.fail("????????????????????????????????????!");
        }
        if(lead.getLeadStatus()>LEAD_STATUS_ENUME.SEND.getValue()){
            return ResponseData.fail("??????????????????!");
        }
        Date now = new Date();
        lead.setLeadStatus(LEAD_STATUS_ENUME.ACCEPT.getValue());//?????????
        lead.setModifyDate(now);
        lead.setRejectReason(reason);
        if(rejectReasonType!=null) {
            lead.setRejectReasonType(String.valueOf(rejectReasonType));
        }
        lead.setModifyBy(currentUserId);
        if(lead.getFollowTime()==null){
            lead.setFollowTime(now);
            lead.setFollowBy(currentUserId);
        }
        leadMapper.updateByPrimaryKeySelective(lead);
        /**??????????????????*///rejectReasonType?????????  ?????????ifs???????????????????????????
        leadCommonService.insertLeadHistory(leadId,now,currentUserId,LEAD_STATUS_ENUME.ACCEPT.getValue(),lead.getRejectReasonType()
                ,userInfo.getDealerId(),null,null);
        return ResponseData.success(true,"????????????!");
    }

    @Override
    @RequestMapping( value = {"/expired"}, method = {RequestMethod.GET})
    public ResponseData<Boolean> expired(Integer leadId) {
        Integer currentUserId = SessionHelper.getCurrentUserId();
        UserDetailDTO userInfo = leadCommonService.getUserInfo(currentUserId);
        if(leadId==null){
            return ResponseData.fail("????????????!");
        }
        Lead lead = leadMapper.selectByPrimaryKey(leadId);
        if(lead==null){
            return ResponseData.fail("??????ID??????!");
        }
        if(lead.getLeadStatus()>= LEAD_STATUS_ENUME.SEND.getValue()){
            return ResponseData.fail("??????????????????");
        }
        LeadTaskDTO task = leadTaskSearchMapper.getLeadTaskByLeadId(leadId);
        if(task == null || task.getStatus() != 7){
            return ResponseData.fail("????????????????????????????????????????????????");
        }
        Date now = new Date();
        lead.setLeadStatus(LEAD_STATUS_ENUME.ACCEPT.getValue());//?????????
        lead.setModifyDate(now);
        lead.setRejectReason("????????????");
        lead.setRejectReasonType(String.valueOf(7));
        lead.setModifyBy(currentUserId);
        if(lead.getFollowTime()==null){
            lead.setFollowTime(now);
            lead.setFollowBy(currentUserId);
        }
        leadMapper.updateByPrimaryKeySelective(lead);
        /**??????????????????*/
        leadCommonService.insertLeadHistory(leadId,now,currentUserId,LEAD_STATUS_ENUME.ACCEPT.getValue(),LEAD_STATUS_ENUME.ACCEPT.getDesc()
                ,userInfo.getDealerId(),null,null);
        return ResponseData.success(true,"????????????!");
    }

    @Override
    @RequestMapping( value = {"/updateLeadToTimeout"}, method = {RequestMethod.POST})
    public ResponseData<Boolean> updateLeadToTimeout(@RequestBody  LeadDTO lead) {
        Integer currentUserId = SessionHelper.getCurrentUserId();
        UserDetailDTO userInfo = leadCommonService.getUserInfo(currentUserId);
        if(lead.getId()==null){
            return ResponseData.fail("????????????!");
        }
        Lead result = leadMapper.selectByPrimaryKey(lead.getId());
        if(result==null){
            return ResponseData.fail("??????ID??????!");
        }
        if(result.getLeadStatus()!= LEAD_STATUS_ENUME.CREATED.getValue()){
            return ResponseData.fail("????????????????????????");
        }
        LeadTaskDTO task = leadTaskSearchMapper.getLeadTask(lead.getId());
        if(task == null || task.getStatus() != 7){
            return ResponseData.fail("????????????????????????????????????????????????");
        }
        Date now = new Date();
        Integer leadId = lead.getId();
        result.setLeadStatus(LEAD_STATUS_ENUME.TIMEOUT.getValue());
        result.setModifyDate(now);
        leadMapper.updateByPrimaryKeySelective(result);
        leadCommonService.insertLeadHistory(leadId,now,currentUserId,LEAD_STATUS_ENUME.TIMEOUT.getValue(),LEAD_STATUS_ENUME.TIMEOUT.getDesc()
                ,null,null,null);
        return ResponseData.success(true,"????????????!");
    }



    @Override
    @RequestMapping(
            value = {"/create"},
            method = {RequestMethod.POST}
    )
    @Transactional(rollbackFor = Exception.class,transactionManager = "leadTransactionManager")
    public ResponseData<Integer> create(@RequestBody LeadDTO lead) {
        if(StringUtils.isEmpty(lead.getPhone())){
            logger.error("????????????-??????????????????,??????????????????");
            return ResponseData.fail("????????????????????????");
        }
        if (StringUtils.isEmpty(lead.getSeriesName()) || StringUtils.isEmpty(lead.getModelName())) {
            return ResponseData.fail("????????????????????????");
        }
        lead.setCreatedBy(SessionHelper.getCurrentUserId());
        PreLeadDTO preLeadDTO = new PreLeadDTO();
        BeanUtils.copyProperties(lead,preLeadDTO);

        ResponseData responseData = leadCommonService.validateLeadInfo(preLeadDTO);
        if(responseData.getData()==null){
            return responseData;
        }
        Integer currentUserId = SessionHelper.getCurrentUserId();
        UserDetailDTO userInfo = leadCommonService.getUserInfo(currentUserId);

        if(userInfo.getIsConsultant()!=null && userInfo.getIsConsultant().intValue()==1){
            lead.setDealer(userInfo.getDealerId());
            salesAssistantService.createLeadByConsultant(lead);
            return ResponseData.success(1,"????????????.");
        }else {
            if (!StringUtils.isEmpty(lead.getDealer())) {
                DealerDTO dealerDTO = dealerService.detailsDealer(lead.getDealer());
                if (dealerDTO == null) {
                    logger.error("????????????-??????????????????,???????????????dealer code[{}]??????,?????????[{}]", lead.getDealer(), lead.getPhone());
                    return ResponseData.fail("?????????????????????");
                }
            }
        }
        Map<String,Object> param = new HashMap<>();
        param.put("phone",lead.getPhone());
        if (!StringUtils.isEmpty(lead.getDealer())) {
            param.put("dealer",lead.getDealer());
        }

        preLeadDTO.setModifyBy(currentUserId);
        preLeadDTO.setCreatedBy(currentUserId);
        preLeadDTO.setMemo(lead.getDescription());
        int priority=10;
        this.rabbitTemplate.convertAndSend(null,"REQUEST_TO_LEAD",JSON.toJSONString(preLeadDTO),new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setPriority(priority);
                return message;
            }
        });

        List<Lead> leadList = leadSearchMapper.searchLeadByPhoneNo(param);//??????,????????????:?????????????????????
        if(leadList!=null && leadList.size()>0){
            return ResponseData.success(1,"?????????????????????.");
        }
        return ResponseData.success(1,"????????????.");
    }

    @Override
    @RequestMapping(
            value = {"/update"},
            method = {RequestMethod.POST}
    )
    @Transactional(rollbackFor = Exception.class,transactionManager = "leadTransactionManager",propagation = Propagation.REQUIRED)
    public ResponseData<Boolean> update(@RequestBody LeadDTO lead) {
        String phone = lead.getPhone();
        if(StringUtils.isEmpty(phone)){
            return ResponseData.fail("?????????????????????");
        }
        if (StringUtils.isEmpty(lead.getSeriesName()) || StringUtils.isEmpty(lead.getModelName())) {
            return ResponseData.fail("????????????????????????");
        }
        Lead entity = leadMapper.selectByPrimaryKey(lead.getId());
        Lead encp = new Lead();
        BeanUtils.copyProperties(entity,encp);
        if(entity==null){
            return ResponseData.fail("??????????????????");
        }
        Date sendTime = entity.getSendTime();
        Integer leadStatus = entity.getLeadStatus();
        Integer currentUserId = SessionHelper.getCurrentUserId();
        UserDetailDTO userInfo = leadCommonService.getUserInfo(currentUserId);
        if((userInfo.getIsExpSpecialists()!=null && userInfo.getIsExpSpecialists()==1)
                ||(userInfo.getIsOrderManager()!=null && userInfo.getIsOrderManager()==1)
                ||(userInfo.getIsConsultant()!=null && userInfo.getIsConsultant()==1)){
            if(LEAD_STATUS_ENUME.cantEditStatusForDealer().contains(leadStatus)) {
                return ResponseData.fail("?????????????????????");
            }
        }
        Date now = new Date();
        LeadTaskDTO leadTask = leadTaskSearchMapper.getLeadTaskByLeadId(lead.getId());
        /**?????????,?????????????????????; ??????,?????????*/
        if(entity.getLeadStatus().intValue()==LEAD_STATUS_ENUME.CREATED.getValue()
                && leadTask==null &&  !StringUtils.isEmpty(lead.getDealer())) {
            Map<String, Object> param = new HashMap<>();
            param.put("phone", phone);
            if (!StringUtils.isEmpty(lead.getDealer())) {
                param.put("dealer", lead.getDealer());
            }
            List<Lead> leads = leadSearchMapper.searchLeadByPhoneNoForSended(param);
            logger.info("????????????-??????:??????[{}],?????????[{}],????????????[{}]", phone, lead.getDealer(), leads == null ? null : leads.size());
            Lead oldLead = null;
            /**??????????????????,??????????????????????????????????????????????????????+??????????????????????????????????????????*/
            for (Lead obj : leads) {//?????????????????????????????????????????????
                if (lead.getId().intValue() != obj.getId().intValue()) {
                    if ((StringUtils.isEmpty(lead.getDealer()) && StringUtils.isEmpty(obj.getDealer())) ||
                            (!StringUtils.isEmpty(lead.getDealer()) && lead.getDealer().equals(obj.getDealer()))) {
                        oldLead = obj;
                    }
                }
            }
            if (oldLead != null) {
                logger.info("???????????????,leadId:" + lead.getId());
                logger.info("????????????,??????Lead:[{}]", JSON.toJSONString(lead));
                logger.info("????????????,?????????Lead:[{}]", JSON.toJSONString(oldLead));
                BeanUtils.copyPropertiesHasValue(lead, oldLead);
                logger.info("????????????,?????????:[{}]", JSON.toJSONString(oldLead));
                oldLead.setModifyBy(currentUserId);
                oldLead.setModifyDate(now);
                if(!StringUtils.isEmpty(lead.getSeriesName()) && lead.getSeriesName() != null && !lead.getSeriesName().equals(oldLead.getSeriesName())){
                    CarSeriesDTO carSeriesByCode = carService.getCarSeriesByCode(lead.getSeriesName());
                    if(StringUtils.isEmpty(oldLead.getDescription()) || oldLead.getDescription() == null){
                        if(carSeriesByCode != null){
                            oldLead.setDescription("?????????????????? "+carSeriesByCode.getSeriesName());
                        }else{
                            oldLead.setDescription("?????????????????? "+lead.getSeriesName());
                        }
                    }else{
                        if(carSeriesByCode != null){
                            oldLead.setDescription(oldLead.getDescription()+";?????????????????? "+carSeriesByCode.getSeriesName());
                        }else{
                            oldLead.setDescription(oldLead.getDescription()+";?????????????????? "+lead.getSeriesName());
                        }
                    }
                }
                leadMapper.updateByPrimaryKeySelective(oldLead);
                lead.setLeadStatus(LEAD_STATUS_ENUME.SAME_LIST.getValue());

                logger.info("??????????????????newLead[{}]->oldLead[{}]...",lead.getId(),oldLead.getId());
                leadCommonService.copyChannel2OldLead(oldLead.getId(),lead.getId());

                leadCommonService.insertLeadHistory(lead.getId(), now, currentUserId, LEAD_STATUS_ENUME.SAME_LIST.getValue(),
                        LEAD_STATUS_ENUME.SAME_LIST.getDesc(), lead.getDealer(), null, null);
            } else {
                if (!StringUtils.isEmpty(lead.getDealer())) {
                    lead.setLeadStatus(LEAD_STATUS_ENUME.SEND.getValue());
                    lead.setSendTime(now);
                    leadCommonService.insertLeadHistory(lead.getId(), now, currentUserId, LEAD_STATUS_ENUME.SEND.getValue(),
                            LEAD_STATUS_ENUME.SEND.getDesc(), lead.getDealer(), null, null);
                }
            }
        }
        leadCommonService.createLog(entity,lead,"lead",now,currentUserId,entity.getId());
        BeanUtils.copyProperties(lead,entity);
        entity.setLeadNumber(null);
        entity.setModifyDate(now);
        entity.setModifyBy(currentUserId);
        if(userInfo.getIsExpSpecialists()!=null && userInfo.getIsExpSpecialists()==1 && encp.getFollowTime()==null){
            entity.setFollowTime(now);
            entity.setFollowBy(currentUserId);
        }else{
            entity.setFollowTime(encp.getFollowTime());
        }
//        if(userInfo.getIsExpSpecialists()!=null && userInfo.getIsExpSpecialists()==1 && entity.getFollowTime()==null){
//            entity.setFollowTime(now);
//            entity.setFollowBy(currentUserId);
//        }
        if(leadStatus!=null && leadStatus.intValue()>1){
            entity.setDealer(null);
        }
        leadSearchMapper.updateByPrimaryKeySelective(entity);
        return ResponseData.success(true);
    }



    @Override
    @RequestMapping(
            value = {"/cust"},
            method = {RequestMethod.POST}
    )
    public ResponseData<PaginationResult<LeadResultDTO>> getLeadListByCustId(@RequestBody LeadDTO lead){
        PageHelper.startPage(lead.getPageNo(), lead.getPageSize());
        List<LeadResultDTO> data = leadSearchMapper.getLeadListByCustId(lead);
        this.setValueForList(data);
        PageInfo<LeadResultDTO> page = new PageInfo(data);
        page.setPageSize(lead.getPageSize());
        PaginationResult<LeadResultDTO> result = new PaginationResult<>();
        result.setTotalCount(page.getTotal());
        result.setData(data);
        return ResponseData.success(result);
    }

    @Override
    @RequestMapping(
            value = {"/city"},
            method = {RequestMethod.POST}
    )
    public ResponseData<List<CityDTO>> getCityByName(String cityName) {
        List<CityDTO> cityList = null;
        return ResponseData.success(cityList);
    }

    @Override
    @RequestMapping(
            value = {"/exportlist"},
            method = {RequestMethod.POST}
    )
    public ResponseData<List<LeadResultDTO>> listExportLead(@RequestBody ExportLeadSearchDTO exportLeadSearchDTO){
        List<LeadResultDTO> resultList = leadSearchMapper.listExportLead(exportLeadSearchDTO);
        if (resultList==null){
            resultList = new ArrayList<>();
        }else{
            for (LeadResultDTO leadResultDTO:resultList){
                String channelName = leadSearchMapper.getChannelNameByLeadId(leadResultDTO.getId());
                leadResultDTO.setChannelName(channelName);
                String dealerId = leadResultDTO.getDealer();
                DealerDTO dealerDTO = dealerService.detailsDealer(dealerId);
                if (dealerDTO!=null) {
                    leadResultDTO.setDealerName(dealerDTO.getDealerName());
                }
                String regionId = leadResultDTO.getProvince();
                if (regionId!=null) {
                    RegionDTO regionDTO = regionService.detailsRegion(Integer.parseInt(regionId));
                    if (regionDTO!=null){
                        leadResultDTO.setProvince(regionDTO.getRegionName());
                    }
                }
                String city = leadResultDTO.getCity();
                if (city!=null){
                    RegionDTO regionDTO = regionService.detailsRegion(Integer.parseInt(city));
                    if (regionDTO!=null){
                        leadResultDTO.setCity(regionDTO.getRegionName());
                    }
                }
            }
        }
        return ResponseData.success(resultList);
    }



    /**
     * ????????????-??????????????? ???????????????+????????? ????????????????????????
     * @param dealerId
     * @param phone
     * @return
     */
    @Override
    @RequestMapping(value = "/consultant/checkLeadExisted", method = RequestMethod.POST)
    public ResponseData<List<LeadDTO>> getLeadListByDealerIdAndPhone(String dealerId, String phone) {
//        List<Lead> result = leadSearchMapper.selectByDealerIdAndPhone(dealerId, phone);
        List<LeadDTO> resultList = new ArrayList<>();
        List<Lead> result = leadSearchMapper.selectFollowByDealerIdAndPhone(dealerId, phone);
        if(result.size() > 0) {
            for (Lead lead : result) {
                LeadDTO leadDTO = new LeadDTO();
                BeanUtils.copyProperties(lead, leadDTO);
                List<LeadFollowupDTO> lfList1 = leadFollowupSearchMapper.queryFollowupPlannvalid(lead.getId());
                if (lfList1 != null && lfList1.size() > 0 && lfList1.get(0).getFollowPlan() != null) {
                    leadDTO.setFollowPlan(lfList1.get(0).getFollowPlan());
                    leadDTO.setFollowDate(lfList1.get(0).getFollowDate());
                }
                leadDTO.setSignNum(3);//?????????
                resultList.add(leadDTO);
                return ResponseData.success(resultList);
            }
        }
            List<Lead> result1 = leadSearchMapper.selectFailDealerIdAndPhone(dealerId, phone);
//        logger.info("yyyyyyyyyyy");
//        JSONObject obj = new JSONObject();
//        obj.put("data",result1.get(0));
//        logger.info(obj.toString());
//        logger.info("ttttttttttttttt");
//        logger.info(String.valueOf(result1.get(0).getLeadStatus()));
            if (result1.size() > 0) {
                for (Lead lead : result1) {
                    LeadDTO leadDTO = new LeadDTO();
                    BeanUtils.copyProperties(lead, leadDTO);
                    logger.info("OOOOOOOOOOOOOOOOOOOOOOOOOO");
                    logger.info(String.valueOf(lead.getLeadStatus()));
                    if (lead.getLeadStatus() == 4) {
                        List<LeadFollowupDTO> lfList = leadFollowupSearchMapper.queryFollowupPlannvalid(lead.getId());
                        if (lfList != null && lfList.size() > 0 && lfList.get(0).getFollowPlan() != null) {
                            leadDTO.setFollowPlan(lfList.get(0).getFollowPlan());
                            leadDTO.setFollowDate(lfList.get(0).getFollowDate());
                        }
                        leadDTO.setSignNum(1);//??????
                        resultList.add(leadDTO);
                    } else if (lead.getLeadStatus() == 9) {
                        List<LeadFollowupDTO> lfList = leadFollowupSearchMapper.queryFollowupPlannvalid(lead.getId());
                        if (lfList != null && lfList.size() > 0 && lfList.get(0).getFollowPlan() != null) {
                            leadDTO.setFollowPlan(lfList.get(0).getFollowPlan());
                            leadDTO.setFollowDate(lfList.get(0).getFollowDate());
                        }
                        leadDTO.setSignNum(2);//??????
                        resultList.add(leadDTO);
                    }
                    return ResponseData.success(resultList);
                }
            }
            if (result.size() == 0 && result1.size() == 0) {
                return ResponseData.success(resultList);
            }
        return ResponseData.success(resultList);
    }


    // @RabbitListener(queues = "CAR_RELATION", containerFactory="rabbitListenerContainerFactory")
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "CAR_RELATION", durable = "true"),
            exchange = @Exchange("")), containerFactory="rabbitListenerContainerFactory")
    public void testDrive(@Payload String msg) {
        logger.info("??????DMS????????????:" + msg);

        CrmRelationDto crmRelationDto = null;
        try {
            crmRelationDto = JSON.parseObject(msg, CrmRelationDto.class);
        }catch(Exception e){
            logger.error("????????????:",e);
            return;
        }

        Map<String, Object> param = new HashMap<>();
        if (StringUtils.isEmpty(crmRelationDto.getCrmCode()) || StringUtils.isEmpty(crmRelationDto.getSalesDealerCode())) {
            logger.error("crmCode?????????????????????????????????crmCode = [{}], dealer = [{}]", crmRelationDto.getCrmCode(), crmRelationDto.getSalesDealerCode());
            return;
        }

        param.put("custId", crmRelationDto.getCrmCode());
        param.put("dealer", crmRelationDto.getSalesDealerCode());
        List<Lead> leadList = leadSearchMapper.selectByPrimaryKeyAndDealer(param);
        if (leadList==null || leadList.size()==0) {
            logger.error("????????????????????????????????????crmCode = [{}], dealer = [{}]", crmRelationDto.getCrmCode(), crmRelationDto.getSalesDealerCode());
            return;
        }

        Date now = new Date();
        Integer currentUserId = SessionHelper.getCurrentUserId();
        for (Lead leadFor : leadList) {
            leadFor.setLeadStatus(LEAD_STATUS_ENUME.WIN.getValue());
            leadCommonService.insertLeadHistory(leadFor.getId(), now, currentUserId, LEAD_STATUS_ENUME.WIN.getValue(),
                    LEAD_STATUS_ENUME.WIN.getDesc(), leadFor.getDealer(), null, null);
            leadFor.setModifyDate(now);
            leadFor.setModifyBy(currentUserId);
            leadFor.setCategoryName(crmRelationDto.getCategoryName());
            leadFor.setModelCode(crmRelationDto.getModelCode());
            leadFor.setProductCode(crmRelationDto.getProductCode());
            leadMapper.updateByPrimaryKeySelective(leadFor);
        }

    }

    @Override
    @RequestMapping(value = "/advanced/query", method = RequestMethod.POST)
    public ResponseData<PaginationResult<LeadResultDTO>> advancedQuery(@RequestBody AdvancedQueryDTO advancedQueryDTO) {
        Integer currentUserId = SessionHelper.getCurrentUserId();
        final UserDetailDTO userInfo = leadCommonService.getUserInfo(currentUserId);
        if (stringIsNotEmpty(userInfo.getDealerId())
                && userInfo.getIsExpSpecialists() == 0 && (
                // ?????????????????????&?????????????????????&????????????&??????????????? check
                userInfo.getIsOrderManager() != 0 || userInfo.getIsConsultant() != 0
        )) {
            return ResponseData.fail(ILLEGAL_USER_OPERATE_CODE, String.format("Current User Illegal Operate; userId[%s]", String.valueOf(currentUserId)));
        }
        if (stringIsNotEmpty(userInfo.getDealerId())) {
            // dealer exp specialists
            advancedQueryDTO.setCurrentDealer(userInfo.getDealerId());
            List<String> dealers = new ArrayList();
            dealers.add(userInfo.getDealerId());
            advancedQueryDTO.setDealers(dealers);
        } else {
            // CRM admin user/ CRM mgr
            advancedQueryDTO.setCurrentDealer(null);
        }
        logger.info(String.format("Advance Query Current User[%s], searchCondition ==> %s ", String.valueOf(currentUserId), JSON.toJSONString(advancedQueryDTO)));

//        Integer pageNo = advancedQueryDTO.getPageNo() == null ? 1 : advancedQueryDTO.getPageNo();
//        Integer pageSize = advancedQueryDTO.getPageSize() == null ? 10 : advancedQueryDTO.getPageSize();
        if(advancedQueryDTO.getPageSize() == null){
            advancedQueryDTO.setPageSize(10);
        }
        if(advancedQueryDTO.getPageNo() == null){
            advancedQueryDTO.setPageNo(0);
        }else{
            advancedQueryDTO.setPageNo((advancedQueryDTO.getPageNo()-1)*advancedQueryDTO.getPageSize());
        }
//        PageHelper.startPage(pageNo, pageSize);
        List<LeadResultDTO> data = leadSearchMapper.advancedQueryLeadList(advancedQueryDTO);
//        List<LeadChannelDTO> leadChannelList = leadChannelSearchMapper.getLeadChannelList(null);
        for(LeadResultDTO dto : data) {
            Integer leadId = dto.getId();
//            List<LeadChannelDTO> list=new ArrayList<>();
//            for(LeadChannelDTO leadChannel:leadChannelList){
//                if(leadChannel.getLeadId().equals(leadId)){
//                    list.add(leadChannel);
//                }
//            }
            List<LeadChannelDTO> list = leadChannelSearchMapper.getLeadChannelList(leadId);
            if (list.size()>0) {
                for (LeadChannelDTO obj : list) {
                    String channelCode = obj.getChannelCode();
                    if (!StringUtils.isEmpty(channelCode)) {
//                        ChannelDTO channel = channelService.getChannelByCode(channelCode);
//                        if (channel != null) {
//                            obj.setSubChannelName(channel.getName());
//                        }
                        obj.setSubChannelName(obj.getChannelName());//leadChannelList???sql??????????????????channelCode???????????????
                    }
                }
            }
            dto.setChannelList(list);
        }
//        this.setValueForList(data);
//        PageInfo<LeadResultDTO> page = new PageInfo(data);
//        page.setPageSize(pageSize);
        PaginationResult<LeadResultDTO> result = new PaginationResult<>();
//        result.setTotalCount(page.getTotal());
        result.setTotalCount((long)leadSearchMapper.advancedQueryLeadListCount(advancedQueryDTO));
        result.setData(data);
        return ResponseData.success(result);
    }


    @Override
    @RequestMapping(value = "/distribute", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class,transactionManager = "leadTransactionManager")
    public ResponseData<Boolean> distribution(@RequestBody LeadOwnerDTO dto) {
        if (dto.getLeadIdList() != null && dto.getLeadIdList().size() > 0) {
            for (Integer leadId :  dto.getLeadIdList()) {
                Lead lead = leadMapper.selectByPrimaryKey(leadId);
                if (lead == null) {
                    return ResponseData.fail("????????????");
                }
                if (StringUtils.isEmpty(lead.getLevel())) {
                    return ResponseData.fail("???????????????????????????");
                }
                if (org.apache.commons.lang.StringUtils.isEmpty(lead.getSeriesName())
                        || org.apache.commons.lang.StringUtils.isEmpty(lead.getModelName())) {
                    return ResponseData.fail("??????????????????????????????");
                }
                if (org.apache.commons.lang.StringUtils.isEmpty(lead.getModelName())
                        || org.apache.commons.lang.StringUtils.isEmpty(lead.getModelName())) {
                    return ResponseData.fail("??????????????????????????????");
                }
                Integer currentUserId = SessionHelper.getCurrentUserId();
                UserDetailDTO currentUser = leadCommonService.getUserInfo(currentUserId);
                if (currentUser.getIsExpSpecialists() == null ||
                        currentUser.getIsExpSpecialists() != 1
                        || StringUtils.isEmpty(currentUser.getDealerId())) {
                    return ResponseData.fail("???????????????");
                }
                UserDetailDTO orderUser = leadCommonService.getUserInfo(Integer.valueOf(dto.getOwner()));
                if (orderUser == null || orderUser.getIsOrderManager() == null ||
                        orderUser.getIsOrderManager() != 1
                        || !currentUser.getDealerId().equals(orderUser.getDealerId())) {
                    return ResponseData.fail("??????????????????????????????");
                }
                Integer leadStatus = lead.getLeadStatus();
                Date followTime = lead.getFollowTime();
                lead = new Lead();
                lead.setId(leadId);
                Date now = new Date();
                lead.setDealerOrderManager(String.valueOf(dto.getOwner()));
                /**??????????????????,?????????????????????*/
                if (leadStatus == LEAD_STATUS_ENUME.SEND.getValue()) {
                    leadStatus = LEAD_STATUS_ENUME.NOT_DISTRIBUTE.getValue();
                    if (currentUser.getIsExpSpecialists() != null && currentUser.getIsExpSpecialists() == 1 && followTime == null) {
                        lead.setFollowTime(now);
                        lead.setFollowBy(currentUserId);
                    }
                }
                lead.setLeadStatus(leadStatus);
                lead.setModifyDate(now);
                lead.setModifyBy(currentUserId);
                leadMapper.updateByPrimaryKeySelective(lead);
                leadCommonService.insertLeadHistory(leadId, now, currentUserId, leadStatus, LEAD_STATUS_ENUME.valueOf(leadStatus).getDesc()
                        , orderUser.getDealerId(), orderUser.getUserId(), null);

            }
        }
        return ResponseData.success(true);
    }




    @Override
    @RequestMapping(
            value = {"/listByBigManagent"},
            method = {RequestMethod.POST}
    )
    public ResponseData<PaginationResult<LeadResultDTO>> listByBigManagent(@RequestBody LeadDTO leadDTO) {
        Integer currentUserId = SessionHelper.getCurrentUserId();
        UserDetailDTO userInfo = leadCommonService.getUserInfo(currentUserId);

        if(userInfo.getIsExpSpecialists()!=null && userInfo.getIsExpSpecialists()!=0) {
            leadDTO.setDealer(userInfo.getDealerId());
        }
//            if(leadDTO.getAssistantResourceType() != null && leadDTO.getAssistantResourceType().intValue() == 2){
//                //????????????????????????
//                leadDTO.setOwner(currentUserId);
//            }
//        PageHelper.startPage(leadDTO.getPageNo(), leadDTO.getPageSize());
        List<LeadResultDTO> data = leadSearchMapper.queryLeadlistByBigManagent(leadDTO);
//        Integer total = leadSearchMapper.queryLeadlistByBigManagentCount(leadDTO);
        List<LeadResultDTO> list = new ArrayList<>();
        //????????????????????????--????????????????????????????????????????????????
        List<LeadResultDTO> updateRecordTimeList=leadSearchMapper.leadUpdateRecordTime();
        /**????????????*/
        for (int i  = 0;i < data.size();i++) {
            int count = leadSearchMapper.selectFlag(data.get(i).getId());
            if (count >0){
                data.get(i).setFlag(1);//?????????????????????????????? flag???1
            }else {
                data.get(i).setFlag(0);//????????????????????????????????? flag???0
            }
            //leadStatus==2??????????????????????????? ????????????????????????????????????????????????1??????????????????????????????????????????????????????????????????
            if(2==data.get(i).getLeadStatus()){
                //????????????????????????????????????1????????????
                for(int j=0;j<updateRecordTimeList.size();j++){
                    if(data.get(i).getId()==updateRecordTimeList.get(j).getId()){
                        Date updateRecordTime=null;//??????????????????????????????
                        if(null!=updateRecordTimeList.get(j).getOperateTime() && null!=updateRecordTimeList.get(j).getUpdateRecordTime()){
                            if(updateRecordTimeList.get(j).getOperateTime().getTime()>updateRecordTimeList.get(j).getUpdateRecordTime().getTime()){
                                updateRecordTime=updateRecordTimeList.get(j).getUpdateRecordTime();
                            }else{
                                updateRecordTime=updateRecordTimeList.get(j).getOperateTime();
                            }
                        }else if(null==updateRecordTimeList.get(j).getOperateTime() && null!=updateRecordTimeList.get(j).getUpdateRecordTime()){
                            updateRecordTime=updateRecordTimeList.get(j).getUpdateRecordTime();
                        }else if(null!=updateRecordTimeList.get(j).getOperateTime() && null==updateRecordTimeList.get(j).getUpdateRecordTime()){
                            updateRecordTime=updateRecordTimeList.get(j).getOperateTime();
                        }
                        if(null!=updateRecordTime && null!=data.get(i).getSendTime()){//????????????????????????????????????
                            //?????????1??????
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(updateRecordTime);
                            cal.add(Calendar.HOUR, 1);
                            Date oneHourDistributionTime = cal.getTime();
                            if(oneHourDistributionTime.getTime()<data.get(i).getSendTime().getTime()){
                                data.get(i).setIsItOneHourDistribution(1);//1?????????
                            }else{
                                data.get(i).setIsItOneHourDistribution(0);//??????1??????
                            }
                        }else{
                            data.get(i).setIsItOneHourDistribution(0);//????????????????????????????????????
                        }
                        break;
                    }
                }
                //????????????1????????????????????????????????????????????? ?????????--??????isItFloatingRed(????????????)???????????????1
                if(null != data.get(i).getIsItOneHourDistribution() && 0 == data.get(i).getIsItOneHourDistribution() &&
                        null != data.get(i).getUpdateRecordTime() ){
                    //??????????????????????????????????????????????????? ??????????????? ????????????
                    List<LeadHistoryDTO> leadHistoryDTOS = leadSearchMapper.queryLeadHistoryByLeadId(data.get(i).getId());
                    if(leadHistoryDTOS.size()>0){
                        data.get(i).setIsItFloatingRed(0);
                    }else{
                        data.get(i).setIsItFloatingRed(1);
                    }
                }else{
                    data.get(i).setIsItFloatingRed(0);
                }
            }
            LeadResultDTO rs = data.get(i);
            rs.setTheKeyInformation("?????? "+rs.getFollowupTimes()+"???  ?????? "+rs.getArrivalTimes()+"???  ?????? "+rs.getPredictTimes()+"???");
            Integer followupTimes = rs.getFollowupTimes();
//            if(followupTimes != null && followupTimes != 0){
//                rs.setFollowupTimes(followupTimes -1);
//            }
            Integer leadId = rs.getId();
            List<LeadChannelDTO> leadChannelList = leadChannelSearchMapper.getLeadChannelList(leadId);
            if(leadChannelList != null){
                rs.setChannelList(leadChannelList);
            }
            if(rs.getLeadStatus().intValue() == LEAD_STATUS_ENUME.ORDER.getValue() || rs.getLeadStatus().intValue() == LEAD_STATUS_ENUME.WIN.getValue() ||rs.getLeadStatus().intValue() == LEAD_STATUS_ENUME.LOSE.getValue()  ){
                list.add(rs);
                data.remove(rs);
                i-- ;
            }

            String seriesName = rs.getSeriesName();
            if(!StringUtils.isEmpty(seriesName)){
                CarSeriesDTO carSeries = carService.getCarSeriesByCode(seriesName);
                if(carSeries!=null){
                    rs.setSeriesName(carSeries.getSeriesName());
                }
            }
            String modelName = rs.getModelName();
            if(!StringUtils.isEmpty(modelName) && !StringUtils.isEmpty(seriesName)){
                CarModelDTO carmodel = carService.getCarModelByCode(seriesName,modelName);
                if(carmodel!=null){
                    rs.setModelName(carmodel.getModelName());
                }
            }
        }
        if(list != null && list.size() > 0){
            data.addAll(list);
        }
        this.setValueForList(data);
        PageInfo<LeadResultDTO> page = new PageInfo(data);
        page.setPageSize(leadDTO.getPageSize());
        PaginationResult<LeadResultDTO> result = new PaginationResult<>();
        result.setTotalCount(page.getTotal());
        result.setData(data);
        return ResponseData.success(result);
    }
    @Override
    @RequestMapping(
            value = "/receiver/dealerExport",
            method = {RequestMethod.POST}
    )
    public ResponseData<List<DealerDTO>> derlerInfoExport() {
        List<DealerDTO> list = leadMapper.getDealerInfo();
        return ResponseData.success(list);

    }

    @Override
    @RequestMapping(
            value = "/getDriveModel/Export",
            method = {RequestMethod.GET}
    )
    public List<DealerDTO> selectDealerName() {
        return leadMapper.selectDealerName();
    }



    @Override
    @RequestMapping(
            value = "/getDriveModel/listCar",
            method = {RequestMethod.GET}
    )
    public List<DealerDTO> selectCarType() {
        return leadMapper.selectCarType();
    }


    @Override
    @RequestMapping(
            value = "/getDriveModel/dealerCode",
            method = {RequestMethod.GET}
    )
    public String selectDealer(String currentUserName) {

        return (leadMapper.selectDealerCode(currentUserName)==""?null:leadMapper.selectDealerCode(currentUserName));

    }

    @Override
    @RequestMapping(
            value = "/lead/selectByOperateTime",
            method = {RequestMethod.GET}
    )
    public ResponseData<List<OrderOperateRecord>> selectByOperateTime(String startOperateTIme, String endOperateTIme) {
        List<OrderOperateRecord> orderOperateRecords = orderOperateRecordSearchMapper.selectByOperateTime(startOperateTIme, endOperateTIme);
//        for(int i=0;i< orderOperateRecords.size();i++){
//            if(null != orderOperateRecords.get(i).getModelCarName()){
//                orderOperateRecords.get(i).setModelCarName(carService.getCarSeriesByCode(orderOperateRecords.get(i).getModelCarName()).getSeriesName());
//            }
//        }
        return  ResponseData.success(orderOperateRecords);
    }



//    @Override
//    public Map<String, Object> sele<select id="selectDefeatAndInvalidLead" parameterType="java.util.Map" resultMap="BaseResultMap">ctStandardRate(String yearMonth, String userId) throws ParseException  {
//
//            Map<String,Object>  mmResultMap=new HashMap<>();
//
//            int year =Integer.parseInt(yearMonth.substring(0,4));
//            int  month = Integer.parseInt(yearMonth.substring(5));
//            int monDays = DateUtils.getDays(year,month);
//            mmResultMap.put("loopNum",monDays);
//
//            //?????????????????????????????????????????????????????????
//            List<Map<String, Object>> mapList = leadMapper.selectStandardRate(yearMonth);
//


//            //??????????????????:???????????????????????????????????????????????????
//            List<Map<String, Object>> dayCountsList = leadMapper.selectStandardRateBydayCounts(yearMonth);
//
//            //??????????????????1?????????.get("userId") + yearMonth
//            for(int i =0 ; i<mapList.size();i++) {
//                //userId
//                Map<String, Object> userIdMap = mapList.get(i);
//
//                Integer mmTotalLogin=0;
//
//                //??????????????????:???????????????????????????????????????????????????
//                for( int j=0; j<dayCountsList.size(); j++){
//                    String dayCountuserId=dayCountsList.get(j).get("userId")==null?"":dayCountsList.get(j).get("userId").toString();
//                    if(dayCountuserId.equals(mapList.get(i).get("userId").toString())){
//                        mmTotalLogin++;//???????????????????????????
//                        Object put = userIdMap.put(dayCountsList.get(j).get("loginDate").toString(), dayCountsList.get(j).get("count"));//????????????????????????
//                        userIdMap.put("actualActiveDays",mmTotalLogin);
//                        userIdMap.put("dayCountsPut",put);
//                    }
//                }
//                //??????????????????
//                String entryTime = userIdMap.get("createTime").toString();
//                Date entryMonthDate = DateUtils.entryMonth(entryTime);
//                Date yearMonthDate = DateUtils.entryMonth(yearMonth);
//
//                if(yearMonthDate.after(entryMonthDate)){
//                    userIdMap.put("shouldActualDays",monDays);
//                }else if(yearMonthDate.before(entryMonthDate)){
//                    userIdMap.put("shouldActualDays",monDays);
//                }else{
//                    int day = Integer.parseInt(entryTime.substring(8, 10));
//                    userIdMap.put("shouldActualDays",monDays- day + 1);
//
//                }
//            }
//            mmResultMap.put("mapList",mapList);
//
//            return mmResultMap;
//        }
//
//



    @Override
    @RequestMapping(
            value = "/leadChange",
            method = {RequestMethod.POST}
    )
    public ResponseData<Boolean> leadChange(Integer currentUserId, String leadStatus, String oldDealerId, String newDealerId, Integer type) {
        logger.info("111111111111111111"+leadStatus);
        String[] split = leadStatus.split(",");//string???list
        String dealer = oldDealerId;
        List<Lead> leadDetail = leadMapper.selectleadByLeadId(dealer,Arrays.asList(split));
        if (type == 1) {//????????????
            for (int i = 0; i < leadDetail.size(); i++) {
                //?????????????????? :??????????????????????????????????????????????????????
                List<Lead> leadList = leadMapper.selectLeadByPhone(leadDetail.get(i).getPhone(), newDealerId);
                Date now = new Date();
                Lead oldLead = null;
                if (leadList.size() > 0) {//????????????
                    for (Lead obj : leadList) {
                        oldLead = obj;
                        break;
                    }
                    if (oldLead != null) {
//                    logger.info("preLead-create-?????????,?????????:[{}]??????ID[{}]??????????????????ID[{}]", phone, lead, oldLead.getId());
//                    logger.info("preLead-create-????????????,??????Lead:[{}]", JSON.toJSONString(lead));
//                    logger.info("preLead-create-????????????,?????????Lead:[{}]", JSON.toJSONString(oldLead));
                        BeanUtils.copyPropertiesHasValue(leadDetail, oldLead);
                        System.out.println("preLead-create-????????????,?????????:====" + JSON.toJSONString(oldLead));
                        oldLead.setModifyDate(now);
                        oldLead.setModifyBy(currentUserId);
                        if (!StringUtils.isEmpty(leadDetail.get(i).getSeriesName()) && leadDetail.get(i).getSeriesName() != null && !leadDetail.get(i).getSeriesName().equals(oldLead.getSeriesName())) {
                            CarSeriesDTO carSeriesByCode = carService.getCarSeriesByCode(leadDetail.get(i).getSeriesName());
                            if (StringUtils.isEmpty(oldLead.getDescription()) || oldLead.getDescription() == null) {
                                if (carSeriesByCode != null) {
                                    oldLead.setDescription("preLead-create-?????????????????? " + carSeriesByCode.getSeriesName());
                                } else {
                                    oldLead.setDescription("preLead-create-?????????????????? " + leadDetail.get(i).getSeriesName());
                                }
                            } else {
                                if (carSeriesByCode != null) {
                                    oldLead.setDescription(oldLead.getDescription() + ";?????????????????? " + carSeriesByCode.getSeriesName());
                                } else {
                                    oldLead.setDescription(oldLead.getDescription() + ";?????????????????? " + leadDetail.get(i).getSeriesName());
                                }
                            }
                        }
                        leadMapper.updateByPrimaryKeySelective(oldLead);
//                    logger.info("preLead-create-??????????????????newLead[{}]->oldLead[{}]...", leadDetail.get(i).getId(), oldLead.getId());
//                    leadCommonService.copyChannel2OldLead(oldLead.getId(), lead.getId());
//                    leadStatus = LEAD_STATUS_ENUME.SAME_LIST.getValue();
                    }

                } else {//???????????????
                    leadDetail.get(i).setDealer(newDealerId);//??????????????????????????????????????????
                    if (leadDetail.get(i).getLeadStatus() == 3 || leadDetail.get(i).getLeadStatus() == 4 || leadDetail.get(i).getLeadStatus() == 9 || leadDetail.get(i).getLeadStatus() == 15) {
                        continue;//????????????????????????????????????????????????????????????
                    } else {
                        leadDetail.get(i).setLeadStatus(2);//???????????????????????????????????????leadStatus?????????2?????????????????????
                    }
                    leadMapper.updateLead(leadDetail.get(i).getLeadStatus(), leadDetail.get(i).getDealer(), leadDetail.get(i).getId());//???????????????lead????????????????????????????????????modify_date
                }
            }
        }if(type == 2){//????????????
            for (int i = 0; i < leadDetail.size(); i++) {
                if (newDealerId != null){
                    leadDetail.get(i).setDealer(newDealerId);
                }
                if (leadDetail.get(i).getLeadStatus() == 3 || leadDetail.get(i).getLeadStatus() == 4 || leadDetail.get(i).getLeadStatus() == 9 || leadDetail.get(i).getLeadStatus() == 15) {
                    continue;//????????????????????????????????????????????????????????????
                } else {
                    leadDetail.get(i).setLeadStatus(1);//???????????????????????????????????????leadStatus?????????1:???????????????????????????????????????????????????????????????
                    LeadTask leadTask = new LeadTask();
                    Date now = new Date();
                    Date nextDate = DateUtils.dayOffset(now, 2);//48?????????3???????????????
                    leadTask.setDueDatePlan(nextDate);
                    leadTask.setLeadId(leadDetail.get(i).getId());
//                    if (taskStatus == null) {
//                        taskStatus = 1;
//                    }
                    leadTask.setStatus(1);
                    leadTask.setTaskType(1);
                    leadMapper.insertLeadTask(leadTask);
                }
                leadMapper.updateLead(leadDetail.get(i).getLeadStatus(), leadDetail.get(i).getDealer(), leadDetail.get(i).getId());//???????????????lead????????????????????????????????????modify_date
            }
        }
        String userName = leadMapper.selectUserNameByUserId(currentUserId);
//        List<String> statusList = leadMapper.selectDescriptionByCode(leadStatus);
        String oldDealerName =  leadMapper.selectOlDDealerNameById(oldDealerId);
        String newDealerName =  leadMapper.selectNewDealerNameById(newDealerId);
        leadMapper.insertChangeLead(userName,type,leadStatus,oldDealerName,newDealerName);
        return ResponseData.success(true);
    }

    @Override
    @RequestMapping(
            value = "/list/leadChange",
            method = {RequestMethod.POST}
    )
    public ResponseData<Map<String, Object>> leadChangeList(Integer pageSize, Integer pageNo, String beginTime, String endTime) {
        Map<String,Object> map=new HashMap<>();
        List<LeadOwnerDTO> leadOwnerDTOS = leadMapper.selectChangeLeadList(pageSize,pageNo,beginTime,endTime);
        int count = leadMapper.selectChangeLeadCount(beginTime,endTime);
        map.put("data",leadOwnerDTOS);
        map.put("totalCount",count);
        return ResponseData.success(map);
    }


}





















