package com.chery.exeed.crm.lead.service;

import com.alibaba.fastjson.JSON;
import com.chery.exeed.cc.service.AliyunApiService;
import com.chery.exeed.crm.cases.service.CasesService;
import com.chery.exeed.crm.common.dto.*;
import com.chery.exeed.crm.common.service.CarService;
import com.chery.exeed.crm.common.service.DealerService;
import com.chery.exeed.crm.common.service.RegionService;
import com.chery.exeed.crm.common.util.BeanUtils;
import com.chery.exeed.crm.common.util.DateUtils;
import com.chery.exeed.crm.common.util.SessionHelper;
import com.chery.exeed.crm.lead.constants.LeadConstants;
import com.chery.exeed.crm.lead.constants.LeadConstants.DEFEAT_APPROVAL_STATUS_ENUME;
import com.chery.exeed.crm.lead.constants.LeadConstants.FOLLOWUP_BIZ_TYPE_ENUME;
import com.chery.exeed.crm.lead.constants.LeadConstants.LEAD_FOLLOW_STATUS_ENUME;
import com.chery.exeed.crm.lead.constants.LeadConstants.LEAD_STATUS_ENUME;
import com.chery.exeed.crm.lead.dto.*;
import com.chery.exeed.crm.lead.mapper.*;
import com.chery.exeed.crm.lead.model.*;
import com.chery.exeed.crm.lead.utils.LeadCommonUtil;
import com.chery.exeed.crm.sysadmin.dto.EstabMessageDTO;
import com.chery.exeed.crm.sysadmin.dto.UserDetailDTO;
import com.chery.exeed.crm.sysadmin.service.SysCommonService;
import com.chery.exeed.crm.sysadmin.service.SysUserService;
import com.chery.exeed.ifs.common.dto.APIResponse;
import com.chery.exeed.ifs.common.dto.CarModelDTO;
import com.chery.exeed.ifs.common.dto.CarSeriesDTO;
import com.chery.exeed.ifs.common.model.DriveBinding;
import com.chery.exeed.ifs.mdmManager.service.AppletService;
import com.chery.exeed.ifs.mdmManager.service.DMSLeadServie;
import org.apache.commons.lang.StringUtils;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.chery.exeed.crm.common.constants.Constants.*;

/**
 * @Description:
 * @author: gang.li@nttdata.com
 * @date: 2019/2/28 15:20
 */
@RestSchema(schemaId = "salesAssistant-service")
@RequestMapping(path = "/apis/salesAssistant")
public class SalesAssistantServiceImpl implements SalesAssistantService {

    private static Logger logger = LoggerFactory.getLogger(SalesAssistantServiceImpl.class);
    @Autowired
    private DealerService dealerService;
    @Autowired
    private LeadChannelSearchMapper leadChannelSearchMapper;
    @Autowired
    private RegionService regionService;
    @Autowired
    private LeadMapper leadMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private PreLeadMapper preLeadMapper;
    @Autowired
    private CustomerMergeMapper customerMergeMapper;
    @Autowired
    private LeadSearchMapper leadSearchMapper;
    @Autowired
    private CustomerSearchMapper customerSearchMapper;
    @Autowired
    private LeadFollowupSearchMapper leadFollowupSearchMapper;
    @Autowired
    private FollowupHistorySearchMapper followupHistorySearchMapper;
    @Autowired
    private HandleDefeatApprovalOperateRecordMapper handleDefeatApprovalOperateRecordMapper;
    @Autowired
    private DistributeOperateRecordSearchMapper distributeOperateRecordSearchMapper;
    @Autowired
    private OfferOperateRecordSearchMapper offerOperateRecordSearchMapper;
    @Autowired
    private OrderOperateRecordSearchMapper orderOperateRecordSearchMapper;
    @Autowired
    private PredictOperateRecordSearchMapper predictOperateRecordSearchMapper;
    @Autowired
    private DefeatOperateRecordSearchMapper defeatOperateRecordSearchMapper;
    @Autowired
    private ArrivalOperateRecordSearchMapper arrivalOperateRecordSearchMapper;
    @Autowired
    private MetaService metaService;
    @Autowired
    private PreLeadSearchMapper preLeadSearchMapper;
    @Autowired
    private LeadCommonService leadCommonService;
    @Autowired
    public RabbitTemplate rabbitTemplate;
    @Autowired
    private CarService carService;
    @Value("${ifs.customer.mq.queue}")
    private String MQ_QUEUE_CUSTOMER;

    @RpcReference(microserviceName = "exeed-crm-apis:exeed-crm-sysadmin", schemaId = "sys-common-service")
    private SysCommonService sysCommonService;
    @RpcReference(microserviceName = "exeed-crm-apis:exeed-crm-sysadmin", schemaId = "user-service")
    private SysUserService sysUserService;
    @RpcReference(microserviceName = "exeed-crm-apis:exeed-crm-case", schemaId = "case-service")
    private CasesService casesService;

    @RpcReference(microserviceName = "exeed-crm-apis:exeed-crm-aliyun", schemaId = "aliyunApi")
    private AliyunApiService aliyunApiService;

    @RpcReference(microserviceName = "ifs-parent-api:ifs-parent", schemaId = "exeed-openapi-customer")
    private DMSLeadServie dmsLeadServie;

    @RpcReference(microserviceName = "ifs-parent-api:ifs-parent", schemaId = "ifs-applet")
    private AppletService appletService;

    private void setValueForList(List<LeadResultDTO> data){
        try {
            for (LeadResultDTO detail : data) {
                String city = detail.getCity();
                if (city != null && NumberUtils.isDigits(city)) {
                    RegionDTO regionDTO = regionService.detailsRegion(Integer.valueOf(city));
                    if (regionDTO != null) {
                        detail.setCity(regionDTO.getRegionName());
                    }
                }

                String province = detail.getProvince();
                if (province != null && NumberUtils.isDigits(province)) {
                    RegionDTO regionDTO = regionService.detailsRegion(Integer.valueOf(province));
                    if (regionDTO != null) {
                        detail.setProvince(regionDTO.getRegionName());
                    }
                }
            }
        }catch (Exception e){
            logger.error("查询省市出错",e);
        }
    }

    @Override
    @RequestMapping(value = "/consult/distribute", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class,transactionManager = "leadTransactionManager")
    public ResponseData<Boolean> consultantDistribute(@RequestBody LeadOwnerDTO dto) {
        Integer currentDealerManager = SessionHelper.getCurrentUserId();
        String currentName = leadMapper.selectUserName(currentDealerManager);
        if (dto.getLeadIdList() != null && dto.getLeadIdList().size() > 0) {
            for (Integer leadId : dto.getLeadIdList()) {
                Lead lead = leadMapper.selectByPrimaryKey(leadId);
                // check 线索dealerId 和 操作用户的dealerId是否一致
                if (!String.valueOf(dto.getManagerDealerId()).equals(lead.getDealer())) {
                    logger.error("线索分配失败 ===> 线索dealerId[" + lead.getDealer() + "].经理dealerId[" + dto.getManagerDealerId() + "]");
                    return ResponseData.fail(ILLEGAL_USER_OPERATE_CODE, "线索分配失败，用户不合法");
                }
                // check 线索状态是否可分配
                if (!LeadCommonUtil.checkAccessDistributeLeadStatus(lead.getLeadStatus())) {
                    return ResponseData.fail(ILLEGAL_LEAD_STATUS_OPERATE_CODE, "线索分配失败，该线索状态不可操作");
                }
                Integer leadStatus = lead.getLeadStatus();
                Date now = new Date();
                if(leadStatus != 14){
                    logger.info("线索分配 ### 更新线索状态 ===> leadId[" + leadId + "]");
                    String dealer = lead.getDealer();
                    String dealerOrderManager =String.valueOf(currentDealerManager);
                    Integer owner = lead.getOwner();
                    lead = new Lead();
                    lead.setId(leadId);
                    lead.setOwner(dto.getOwner());
                    lead.setLeadStatus(LEAD_STATUS_ENUME.DISTRIBUTED.getValue());
                    lead.setModifyDate(now);
                    lead.setModifyBy(currentDealerManager);
                    lead.setDealerOrderManager(String.valueOf(currentDealerManager));
                    leadMapper.updateByPrimaryKeySelective(lead);
                    leadCommonService.insertLeadHistory(leadId,now,currentDealerManager,LEAD_STATUS_ENUME.DISTRIBUTED.getValue(),LEAD_STATUS_ENUME.DISTRIBUTED.getDesc()
                            ,dealer,Integer.parseInt(dealerOrderManager),owner);

                    FollowupHistory history = new FollowupHistory();
                    // check 线索是否有 线索跟进. 若无 线索跟进 则新增一条 线索跟进
                    List<LeadFollowup> leadFollowupList = leadFollowupSearchMapper.selectLeadFollowupByLeadId(leadId);
                    if (leadFollowupList.size() <= 0) {
                        // 无跟进,新增 线索跟进
                        logger.info("线索分配 ### 生成线索跟进开始 ===> Dealer Order Manager["
                                + SessionHelper.getCurrentUserId() + "].Lead follow owner["
                                + dto.getOwner() + "].leadId["
                                + leadId + "]");
                        LeadFollowup leadFollowup = new LeadFollowup();
                        leadFollowup.setStatus(1);
                        leadFollowup.setFollowPlan(DateUtils.dayOffset(now, 1));
                        leadFollowup.setFollowDate(DateUtils.dayOffset(now, 1));
                        leadFollowup.setLeadId(leadId);
                        leadFollowup.setDistributeTo(dto.getOwner());
                        leadFollowup.setCreateBy(String.valueOf(SessionHelper.getCurrentUserId()));
                        leadFollowup.setCreateTime(now);
                        leadFollowup.setLeadStatus(LEAD_STATUS_ENUME.DISTRIBUTED.getValue());
                        leadFollowupSearchMapper.insertSelectiveReturn(leadFollowup);
                        history.setBizId(leadFollowup.getId());

                        LeadFollowup nextFollowup = new LeadFollowup();
                        nextFollowup.setCreateTime(now);
                        nextFollowup.setLeadId(leadId);
                        nextFollowup.setStatus(0);
                        nextFollowup.setDistributeTo(currentDealerManager);
                        nextFollowup.setFollowPlan(DateUtils.dayOffset(now, 1));
                        leadFollowupSearchMapper.insertSelectiveReturn(nextFollowup);
                    }else {
                        // 有跟进,则是重新分配的线索,自动更新未跟进线索为已跟进,重新插入一条线索跟进
                        logger.info("线索重新分配 ### 生成线索跟进开始 ===> Dealer Order Manager["
                                + SessionHelper.getCurrentUserId() + "].Lead follow owner["
                                + dto.getOwner() + "].leadId["
                                + leadId + "]");
                        List<LeadFollowup> leadFollowups = leadFollowupSearchMapper.selectByLeadIdAndStatus(leadId, 0);
                        if ((leadFollowups != null && leadFollowups.size() > 0) ) {
                            LeadFollowup leadFollowupOld = leadFollowups.get(0);
                            leadFollowupOld.setStatus(1);
                            leadFollowupSearchMapper.updateByPrimaryKey(leadFollowupOld);

                            LeadFollowup leadFollowupNew = new LeadFollowup();
                            leadFollowupNew.setStatus(1);
                            leadFollowupNew.setFollowPlan(DateUtils.dayOffset(now, 1));
                            leadFollowupNew.setFollowDate(DateUtils.dayOffset(now, 1));
                            leadFollowupNew.setLeadId(leadId);
                            leadFollowupNew.setDistributeTo(dto.getOwner());
                            leadFollowupNew.setCreateBy(String.valueOf(SessionHelper.getCurrentUserId()));
                            leadFollowupNew.setCreateTime(now);
                            leadFollowupNew.setLeadStatus(LEAD_STATUS_ENUME.DISTRIBUTED.getValue());
                            leadFollowupSearchMapper.insertSelectiveReturn(leadFollowupNew);


                            LeadFollowup nextFollowup = new LeadFollowup();
                            nextFollowup.setCreateTime(now);
                            nextFollowup.setLeadId(leadId);
                            nextFollowup.setDistributeTo(currentDealerManager);
                            nextFollowup.setStatus(0);
                            nextFollowup.setFollowPlan(DateUtils.dayOffset(now, 1));
                            leadFollowupSearchMapper.insertSelectiveReturn(nextFollowup);
                        }
                        else{
                            if (leadStatus == 9 || leadStatus == 4 || leadStatus == 6){
                                LeadFollowup leadFollowupNew = new LeadFollowup();
                                leadFollowupNew.setStatus(1);
                                leadFollowupNew.setFollowPlan(DateUtils.dayOffset(now, 1));
                                leadFollowupNew.setFollowDate(DateUtils.dayOffset(now, 1));
                                leadFollowupNew.setLeadId(leadId);
                                leadFollowupNew.setDistributeTo(dto.getOwner());
                                leadFollowupNew.setCreateBy(String.valueOf(SessionHelper.getCurrentUserId()));
                                leadFollowupNew.setCreateTime(now);
                                leadFollowupNew.setLeadStatus(LEAD_STATUS_ENUME.DISTRIBUTED.getValue());
                                leadFollowupSearchMapper.insertSelectiveReturn(leadFollowupNew);


                                LeadFollowup nextFollowup = new LeadFollowup();
                                nextFollowup.setCreateTime(now);
                                nextFollowup.setLeadId(leadId);
                                nextFollowup.setDistributeTo(currentDealerManager);
                                nextFollowup.setStatus(0);
                                nextFollowup.setFollowPlan(DateUtils.dayOffset(now, 1));
                                leadFollowupSearchMapper.insertSelectiveReturn(nextFollowup);
                            }
                        }

                    }
                    // 生成线索分配操作记录
                    logger.info("线索分配 ### 新增线索分配操作记录 ===> leadId[" + leadId + "]");
                    DistributeOperateRecord record = new DistributeOperateRecord();
                    record.setLeadId(leadId);
                    record.setLeadStatus(LEAD_STATUS_ENUME.DISTRIBUTED.getValue());
                    record.setDistributeTo(dto.getOwner());
                    record.setOperateTime(now);
                    record.setOperateBy(SessionHelper.getCurrentUserId());
                    distributeOperateRecordSearchMapper.insertSelectiveReturn(record);
                    // 生成线索分配操作历史
                    logger.info("线索分配 ### 新增跟进历史记录 ===> leadId[" + leadId + "]");
                    ResponseData<List<MetaDataDTO>> leadMetaListResp = metaService.
                            getLeadMetaList(LeadConstants.LEAD_STATUS_META_NAME, LEAD_STATUS_ENUME.DISTRIBUTED.getValue());
                    history.setBizId(record.getId());
                    history.setBizType(FOLLOWUP_BIZ_TYPE_ENUME.DISTRIBUTE.getValue());
                    history.setLeadId(leadId);
                    history.setOperateTime(now);
                    history.setOperateDesc("线索分配");
                    history.setOperateBy(dto.getManager());
                    history.setOperateResultStatus(LEAD_STATUS_ENUME.DISTRIBUTED.getValue());
                    String consultant = leadCommonService.getUserName(dto.getOwner());
                    history.setOperateResultDesc(LeadConstants.DISTRIBUTE_MESSAGE.replace("${key}", consultant));
                    if(leadStatus == 9 ){//战败
                        history.setBizType(10);
                        history.setOperateResultDesc(currentName+"激活后" + "" + "重新分配");
                        leadMapper.updateLevelByLeadId(history.getLeadId());
                    }
                    if( leadStatus == 4){//失效
                        history.setBizType(12);
                        history.setOperateResultDesc(currentName+"激活后" + "" + "重新分配");
                        leadMapper.updateLevelByLeadId(history.getLeadId());
                    }
                    followupHistorySearchMapper.insertSelective(history);
                    sendFollowupToInterface(lead.getResourceId(),lead.getDealer(),dto.getOwner());
                }else{
                    Integer owner = lead.getOwner();
                    ResponseData<Boolean> userById = sysUserService.queryUserIsDeleted(owner);
                    /**判断用户是否离职*/
                    if(userById.getData() == true){
                        /**1:修改线索owner*/
                        lead = new Lead();
                        lead.setId(leadId);
                        lead.setOwner(dto.getOwner());
                        lead.setModifyDate(now);
                        lead.setModifyBy(currentDealerManager);
                        leadMapper.updateByPrimaryKeySelective(lead);
                        /**2:插入已分配操作记录*/
                        DistributeOperateRecord record = new DistributeOperateRecord();
                        record.setLeadId(leadId);
                        record.setLeadStatus(LEAD_STATUS_ENUME.ORDER.getValue());
                        record.setDistributeTo(dto.getOwner());
                        record.setOperateTime(now);
                        record.setOperateBy(SessionHelper.getCurrentUserId());
                        distributeOperateRecordSearchMapper.insertSelectiveReturn(record);
                        /**3:生成线索分配操作历史*/
                        FollowupHistory history = new FollowupHistory();
                        history.setBizId(record.getId());
                        history.setBizType(FOLLOWUP_BIZ_TYPE_ENUME.DISTRIBUTE.getValue());
                        history.setLeadId(leadId);
                        history.setOperateTime(now);
                        history.setOperateDesc("线索分配");
                        history.setOperateBy(dto.getManager());
                        history.setOperateResultStatus(LEAD_STATUS_ENUME.DISTRIBUTED.getValue());
                        String consultant = leadCommonService.getUserName(dto.getOwner());
                        history.setOperateResultDesc(LeadConstants.DISTRIBUTE_MESSAGE.replace("${key}", consultant));
                        followupHistorySearchMapper.insertSelective(history);
                        sendFollowupToInterface(lead.getResourceId(),lead.getDealer(),dto.getOwner());
                    }else{
                        return ResponseData.fail("该顾问未离职");
                    }
                }
            }
        }
        return ResponseData.success(true);
    }

    /** DCC跟进信息反馈 */
    private void sendFollowupToInterface(String sourceId,String dealerId,Integer contactId){
        if(sourceId==null || sourceId.startsWith("TD_")){
            return;
        }
        PreLead preLead = preLeadSearchMapper.selectPreLeadBySourceId(sourceId);
        if (preLead!=null){
            UserDetailDTO userInfo = leadCommonService.getUserInfo(contactId);
            logger.info("DCC跟进信息反馈,接口来源ID[{}]跟进人姓名[{}]跟进人电话[{}]",sourceId,
                    userInfo.getNickName(),userInfo.getMobile());

            DccLeadFollowUpDTO dccLeadFollowUp = new DccLeadFollowUpDTO();
            dccLeadFollowUp.setLeadId(Integer.parseInt(sourceId));
            dccLeadFollowUp.setChannel(preLead.getChannelCode());
            dccLeadFollowUp.setDealerId(dealerId);
            dccLeadFollowUp.setContactTel(userInfo.getMobile());
            dccLeadFollowUp.setContactName(userInfo.getNickName());
            dccLeadFollowUp.setFollowStatus(1);
            try {
                this.rabbitTemplate.convertAndSend("IFS_CRM_DCC_LEADS_OUTPUT_QUEUE", JSON.toJSONString(dccLeadFollowUp));
            }catch (Exception e){
                logger.info("dcc线索反馈跟进信息发送mq：失败"+dccLeadFollowUp,e);
            }
        }

    }

    @Override
    @RequestMapping(value = "/superviseLead/list", method = RequestMethod.GET)
    public ResponseData<Map<String, Object>> querySuperviseLeadList(Integer type, Integer dateType, Integer leadType, String createBy,
                                                                    Integer level, Integer pageNo, Integer pageSize){
        Map<String,Object> map=new HashMap<>();
        UserDetailDTO user = leadCommonService.getUserInfo(SessionHelper.getCurrentUserId());
        List<LeadFollowupDTO> resultList=new ArrayList<>();
        long startTime = System.currentTimeMillis(); //获取开始时间
        logger.info("HW 待督促列表开始执行"+ startTime);
        if (type == 1){
            //type为1时查询数量
            resultList = leadFollowupSearchMapper.getSuperviseLeadTotal(user.getDealerId());
            map.put("data",resultList);
        }else if (type == 2){
            resultList = leadFollowupSearchMapper.querySuperviseLeadList(user.getDealerId(),dateType,leadType,createBy,level,pageNo,pageSize);
            Integer total = leadFollowupSearchMapper.querySuperviseLeadCount(user.getDealerId(),dateType,leadType,createBy,level,pageNo,pageSize);
            map.put("data",resultList);
            map.put("totalCount",total);
            //type为2时查询列表
            if(resultList != null && resultList.size() > 0){
                for (LeadFollowupDTO lfdto: resultList ) {
                    lfdto.setTheKeyInformation("跟进 "+lfdto.getFollowupTimes()+"次  到店 "+lfdto.getArrivalTimes()+"次  试驾 "+lfdto.getPredictTimes()+"次");
                    //查询过期时间
                    List<LeadFollowupDTO> followPlan = leadFollowupSearchMapper.queryFollowupPlan(lfdto.getLeadId());
                    if(followPlan != null && followPlan.size()>0){
                        if(followPlan.get(0).getFollowPlan() != null){
                            lfdto.setFollowPlan(followPlan.get(0).getFollowPlan());
                        }
                    }
                    lfdto.setOwnerName(leadCommonService.getUserName(lfdto.getOwner()));
                    List<LeadChannelDTO> leadChannelList = leadChannelSearchMapper.getLeadChannelList(lfdto.getLeadId());
                    if(leadChannelList != null && leadChannelList.size() > 0){
                        LeadChannelDTO lcdto = leadChannelList.get(0);
                        lfdto.setChannelName(lcdto.getChannelName());
                        lfdto.setSubChannelName(lcdto.getSubChannelName());

                    }
                    String sn = lfdto.getSeriesName();
                    if(!com.chery.exeed.crm.common.util.StringUtils.isEmpty(sn)){
                        CarSeriesDTO carSeries = carService.getCarSeriesByCode(sn);
                        if(carSeries!=null){
                            lfdto.setSeriesName(carSeries.getSeriesName());
                        }
                    }
                    String mn = lfdto.getModelName();
                    if(!com.chery.exeed.crm.common.util.StringUtils.isEmpty(sn) && !com.chery.exeed.crm.common.util.StringUtils.isEmpty(mn)){
                        CarModelDTO carmodel = carService.getCarModelByCode(sn,mn);
                        if(carmodel!=null){
                            lfdto.setModelName(carmodel.getModelName());
                        }
                    }

                }
            }
        }
        long endTime = System.currentTimeMillis(); //获取结束时间
        long fenzhong = (endTime - startTime) / 1000;
        logger.info(" HW 程序运行时间：" + fenzhong + "秒"); //输出程序运行时间
        return ResponseData.success(map);
    }
    @Override
    @RequestMapping(value = "/distributedLead/list", method = RequestMethod.GET)
    public ResponseData<List<LeadFollowupDTO>> queryDistributedLeadList() {
        UserDetailDTO user = leadCommonService.getUserInfo(SessionHelper.getCurrentUserId());
        List<LeadFollowupDTO> resultList = leadFollowupSearchMapper.queryDistributedLeadList(user.getDealerId());
        if(resultList != null && resultList.size() > 0){
            for (LeadFollowupDTO lfdto: resultList ) {
                lfdto.setTheKeyInformation("跟进 "+lfdto.getFollowupTimes()+"次  到店 "+lfdto.getArrivalTimes()+"次  试驾 "+lfdto.getPredictTimes()+"次");
                //查询过期时间
                List<LeadFollowupDTO> followPlan = leadFollowupSearchMapper.queryFollowupPlan(lfdto.getLeadId());
                if(followPlan != null && followPlan.size()>0){
                    if(followPlan.get(0).getFollowPlan() != null){
                        lfdto.setFollowPlan(followPlan.get(0).getFollowPlan());
                    }
                }
                lfdto.setOwnerName(leadCommonService.getUserName(lfdto.getOwner()));
                List<LeadChannelDTO> leadChannelList = leadChannelSearchMapper.getLeadChannelList(lfdto.getLeadId());
                if(leadChannelList != null && leadChannelList.size() > 0){
                    LeadChannelDTO lcdto = leadChannelList.get(0);
                    lfdto.setChannelName(lcdto.getChannelName());
                    lfdto.setSubChannelName(lcdto.getSubChannelName());

                }
                String sn = lfdto.getSeriesName();
                if(!com.chery.exeed.crm.common.util.StringUtils.isEmpty(sn)){
                    CarSeriesDTO carSeries = carService.getCarSeriesByCode(sn);
                    if(carSeries!=null){
                        lfdto.setSeriesName(carSeries.getSeriesName());
                    }
                }
                String mn = lfdto.getModelName();
                if(!com.chery.exeed.crm.common.util.StringUtils.isEmpty(sn) && !com.chery.exeed.crm.common.util.StringUtils.isEmpty(mn)){
                    CarModelDTO carmodel = carService.getCarModelByCode(sn,mn);
                    if(carmodel!=null){
                        lfdto.setModelName(carmodel.getModelName());
                    }
                }

            }
        }
        return ResponseData.success(resultList);
    }
    @Override
    @RequestMapping(value = "/acceptLead/list", method = RequestMethod.GET)
    public ResponseData<List<LeadFollowupDTO>> queryAcceptLeadList(Integer type, Integer dateType,String createBy,Integer level) {
        UserDetailDTO user = leadCommonService.getUserInfo(SessionHelper.getCurrentUserId());
        LeadFollowupDTO  leadFollowupDTO = new LeadFollowupDTO();
        List<LeadFollowupDTO> resultList=new ArrayList<>();
        if (type == 1){
            //type为1时查询数量
            resultList = leadFollowupSearchMapper.queryAcceptLeadListTotal(user.getDealerId());
            if (resultList.size() == 0){
                leadFollowupDTO.setTotal(0);
                resultList.add(leadFollowupDTO);
            }
        }else if (type == 2) {
            //type为2时查询列表
            resultList = leadFollowupSearchMapper.queryAcceptLeadList(user.getDealerId(),dateType,createBy,level);
            if (resultList != null && resultList.size() > 0) {
                for (LeadFollowupDTO lfdto : resultList) {
                    lfdto.setTheKeyInformation("跟进 " + lfdto.getFollowupTimes() + "次  到店 " + lfdto.getArrivalTimes() + "次  试驾 " + lfdto.getPredictTimes() + "次");
                    //查询过期时间
                    List<LeadFollowupDTO> followPlan = leadFollowupSearchMapper.queryFollowupPlan(lfdto.getLeadId());
                    if (followPlan != null && followPlan.size() > 0) {
                        if (followPlan.get(0).getFollowPlan() != null) {
                            lfdto.setFollowPlan(followPlan.get(0).getFollowPlan());
                        }
                    }
                    lfdto.setOwnerName(leadCommonService.getUserName(lfdto.getOwner()));
                    List<LeadChannelDTO> leadChannelList = leadChannelSearchMapper.getLeadChannelList(lfdto.getLeadId());
                    if (leadChannelList != null && leadChannelList.size() > 0) {
                        LeadChannelDTO lcdto = leadChannelList.get(0);
                        lfdto.setChannelName(lcdto.getChannelName());
                        lfdto.setSubChannelName(lcdto.getSubChannelName());

                    }
                    String sn = lfdto.getSeriesName();
                    if (!com.chery.exeed.crm.common.util.StringUtils.isEmpty(sn)) {
                        CarSeriesDTO carSeries = carService.getCarSeriesByCode(sn);
                        if (carSeries != null) {
                            lfdto.setSeriesName(carSeries.getSeriesName());
                        }
                    }
                    String mn = lfdto.getModelName();
                    if (!com.chery.exeed.crm.common.util.StringUtils.isEmpty(sn) && !com.chery.exeed.crm.common.util.StringUtils.isEmpty(mn)) {
                        CarModelDTO carmodel = carService.getCarModelByCode(sn, mn);
                        if (carmodel != null) {
                            lfdto.setModelName(carmodel.getModelName());
                        }
                    }

                }
            }
        }
        return ResponseData.success(resultList);
    }
    @Override
    @RequestMapping(value = "/orderLead/list", method = RequestMethod.GET)
    public ResponseData<List<LeadFollowupDTO>> queryOrderLeadList(Integer type, Integer dateType,String createBy,Integer level) {
        UserDetailDTO user = leadCommonService.getUserInfo(SessionHelper.getCurrentUserId());
        List<LeadFollowupDTO> resultList=new ArrayList<>();
        if (type == 1){
            //type为1时查询数量
            resultList = leadFollowupSearchMapper.queryOrderLeadListTotal(user.getDealerId());
        }else if (type == 2) {
            //type为2时查询列表
            resultList = leadFollowupSearchMapper.queryOrderLeadList(user.getDealerId(),dateType,createBy,level);
            if (resultList != null && resultList.size() > 0) {
                for (LeadFollowupDTO lfdto : resultList) {
                    lfdto.setTheKeyInformation("跟进 " + lfdto.getFollowupTimes() + "次  到店 " + lfdto.getArrivalTimes() + "次  试驾 " + lfdto.getPredictTimes() + "次");
                    //查询过期时间
                    List<LeadFollowupDTO> followPlan = leadFollowupSearchMapper.queryFollowupPlan(lfdto.getLeadId());
                    if (followPlan != null && followPlan.size() > 0) {
                        if (followPlan.get(0).getFollowPlan() != null) {
                            lfdto.setFollowPlan(followPlan.get(0).getFollowPlan());
                        }
                    }
                    lfdto.setOwnerName(leadCommonService.getUserName(lfdto.getOwner()));
                    List<LeadChannelDTO> leadChannelList = leadChannelSearchMapper.getLeadChannelList(lfdto.getLeadId());
                    if (leadChannelList != null && leadChannelList.size() > 0) {
                        LeadChannelDTO lcdto = leadChannelList.get(0);
                        lfdto.setChannelName(lcdto.getChannelName());
                        lfdto.setSubChannelName(lcdto.getSubChannelName());

                    }
                    String sn = lfdto.getSeriesName();
                    if (!com.chery.exeed.crm.common.util.StringUtils.isEmpty(sn)) {
                        CarSeriesDTO carSeries = carService.getCarSeriesByCode(sn);
                        if (carSeries != null) {
                            lfdto.setSeriesName(carSeries.getSeriesName());
                        }
                    }
                    String mn = lfdto.getModelName();
                    if (!com.chery.exeed.crm.common.util.StringUtils.isEmpty(sn) && !com.chery.exeed.crm.common.util.StringUtils.isEmpty(mn)) {
                        CarModelDTO carmodel = carService.getCarModelByCode(sn, mn);
                        if (carmodel != null) {
                            lfdto.setModelName(carmodel.getModelName());
                        }
                    }

                }
            }
        }
        return ResponseData.success(resultList);
    }

    @Override
    @RequestMapping(value = "/consultant/expiredLeadList", method = RequestMethod.GET)
    public ResponseData<List<LeadFollowupDTO>> expiredLeadList() {
        Integer userId = SessionHelper.getCurrentUserId();
        UserDetailDTO user = leadCommonService.getUserInfo(userId);
        List<LeadFollowupDTO> result = leadFollowupSearchMapper.queryExpiredLeadList(user.getDealerId(),user.getUserId());
        return ResponseData.success(result);
    }

    @Override
    @RequestMapping(value = "/lead/queryUndistributedLeadList", method = RequestMethod.POST)
    public ResponseData<Map<String, Object>> queryUndistributedLeadList(String seriesName, String modelName, Integer subChannel, Integer pageNo, Integer pageSize) {
        Map<String,Object> map=new HashMap<>();
        Integer currentUserId = SessionHelper.getCurrentUserId();
        UserDetailDTO userInfo = leadCommonService.getUserInfo(currentUserId);
        Integer userId = userInfo.getUserId();
        String dealerId = userInfo.getDealerId();
        List<Integer> invailUserId = leadSearchMapper.selectInvalidManage(dealerId);//查失效展厅经理的userId
        invailUserId.add(userId);//把失效展厅经理和有效展厅经理userId放到一个list
        List<Integer> rstList = new ArrayList<>();
        rstList.add(LEAD_STATUS_ENUME.NOT_DISTRIBUTE.getValue());
        List<LeadResultDTO> data = leadSearchMapper.queryUndistributedLeadList(dealerId,rstList,invailUserId,seriesName,modelName,subChannel,pageNo,pageSize);
        Integer  total = leadSearchMapper.queryUndistributedCount(dealerId,rstList,invailUserId,seriesName,modelName,subChannel,pageNo,pageSize);
        if(data != null){
            for (LeadResultDTO rs:data) {
                Integer owner = rs.getOwner();
                if(owner != null){
                    rs.setOwnerDesc(leadCommonService.getUserName(owner));
                }
                List<LeadChannelDTO> leadChannelList = leadChannelSearchMapper.getLeadChannelList(rs.getId());
                rs.setChannelList(leadChannelList);
            }
        }
        map.put("data",data);
        map.put("totalCount",total);
        return ResponseData.success(map);
    }



    /**
     * 线索详情页 - 线索跟进情况列表
     * @param leadId
     * @param dealerId
     * @return
     */
    @Override
    @RequestMapping(value = "/followup/record", method = RequestMethod.GET)
    public ResponseData<List<FollowupHistoryDTO>> getLeadFollowRecord(Integer leadId, String dealerId,Integer pageNo,Integer pageSize) {
        if(null != leadId){
            leadMapper.updateLeadIsRedOrBlack(leadId);//查询线索时将其改成黑色---1条线索
        }
        Lead lead = leadMapper.selectByPrimaryKey(leadId);
        // check 线索dealerId 和 操作用户的dealerId是否一致
        if (!String.valueOf(dealerId).equals(lead.getDealer())) {
            logger.error("查询跟进情况失败 ===> 线索dealerId[" + lead.getDealer() + "].经销商用户dealerId[" + dealerId + "]");
            return ResponseData.fail(ILLEGAL_USER_OPERATE_CODE, "查询跟进情况失败，用户不合法");
        }
        List<FollowupHistoryDTO> rstList = new ArrayList<>();
        List<FollowupHistoryDTO> historyList = followupHistorySearchMapper.selectByLeadId(leadId,pageNo,pageSize);
//        Integer total = followupHistorySearchMapper.selectByLeadIdTotal(leadId);
        String dealerOrderManagerId = lead.getDealerOrderManager();
        String dealerOrderManager = "";
        if(StringUtils.isNotBlank(dealerOrderManagerId)){
            dealerOrderManager = leadCommonService.getUserName(Integer.parseInt(dealerOrderManagerId));
        }
        if (historyList.size() > 0) {
            for (FollowupHistoryDTO followupHistory : historyList) {
                if(StringUtils.isNotBlank(followupHistory.getOperateBy())){
                    if(followupHistory.getBizType().intValue() == 7 || followupHistory.getBizType().intValue() == 6 ){
//                        String dealerOrderManagerId = lead.getDealerOrderManager();
//                        String dealerOrderManager = "";
//                        if(StringUtils.isNotBlank(dealerOrderManagerId)){
//                            dealerOrderManager = leadCommonService.getUserName(Integer.parseInt(dealerOrderManagerId));
//                        }
                        followupHistory.setOperationByDesc("展厅经理  "+ dealerOrderManager );
                    }else{
//                        int i = Integer.parseInt(followupHistory.getOperateBy());
//                        UserDetailDTO userInfo = leadCommonService.getUserInfo(i);
//                        followupHistory.setOperationByDesc("星途顾问  "+ userInfo.getNickName());
                        followupHistory.setOperationByDesc("星途顾问  "+ followupHistory.getNickName());
                    }
                }
                rstList.add(followupHistory);
            }
        }
//        return ResponseData.success(rstList);
//        Map<String,Object> map=new HashMap<>();
//        map.put("data",historyList);
//        map.put("totalCount",total);
        return ResponseData.success(historyList);
    }

    @Override
    @RequestMapping(value = "/followup/leadFollowHistoryList", method = RequestMethod.GET)
    public ResponseData<Map<String, Object>> leadFollowHistoryList(Integer leadId, String dealerId) {
        Lead lead = leadMapper.selectByPrimaryKey(leadId);
        // check 线索dealerId 和 操作用户的dealerId是否一致
//        if (!String.valueOf(dealerId).equals(lead.getDealer())) {
//            logger.error("查询跟进情况失败 ===> 线索dealerId[" + lead.getDealer() + "].经销商用户dealerId[" + dealerId + "]");
//            return ResponseData.fail(ILLEGAL_USER_OPERATE_CODE, "查询跟进情况失败，用户不合法");
//        }
        HashMap<String, Object> resultMap = new HashMap<>();
        List<FollowupHistoryDTO> rstList = new ArrayList<>();
        if(lead.getLeadStatus().intValue() < 7 || lead.getLeadStatus().intValue()  == 17 ){
            logger.info("leadId===>"+lead.getId()+"||leadStatus===>"+lead.getLeadStatus()+"||该线索状态没有跟进记录");
            resultMap.put("list",rstList);
            resultMap.put("flag",false);
            resultMap.put("leadStatus",lead.getLeadStatus());
            return ResponseData.success(resultMap);
        }
        List<FollowupHistoryDTO> historyList = followupHistorySearchMapper.selectByLeadId(leadId,null,null);
        String dealerOrderManagerId = lead.getDealerOrderManager();
        String dealerOrderManager = "";
        if(StringUtils.isNotBlank(dealerOrderManagerId)){
            dealerOrderManager = leadCommonService.getUserName(Integer.parseInt(dealerOrderManagerId));
        }
        if (historyList.size() > 0) {
            for (FollowupHistoryDTO followupHistory : historyList) {
                if(StringUtils.isNotBlank(followupHistory.getOperateBy())){
                    if(followupHistory.getBizType().intValue() == 7 || followupHistory.getBizType().intValue() == 6 ){
//                        String dealerOrderManagerId = lead.getDealerOrderManager();
//                        String dealerOrderManager = "";
//                        if(StringUtils.isNotBlank(dealerOrderManagerId)){
//                            dealerOrderManager = leadCommonService.getUserName(Integer.parseInt(dealerOrderManagerId));
//                        }//这里每次查询的都是同一个dealerOrderManagerId
                        followupHistory.setOperationByDesc("展厅经理  "+ dealerOrderManager );
                    }else{
//                        int i = Integer.parseInt(followupHistory.getOperateBy());
//                        UserDetailDTO userInfo = leadCommonService.getUserInfo(i);
//                        followupHistory.setOperationByDesc("星途顾问  "+ userInfo.getNickName());//关联v_all_sys_user视图查询
                        followupHistory.setOperationByDesc("星途顾问  "+ followupHistory.getNickName());
                    }
                }
                rstList.add(followupHistory);
            }
        }
        Integer currentUserId = SessionHelper.getCurrentUserId();
        Boolean flag = false;
        logger.info("lead.getOwner()====>"+lead.getOwner()+"||currentUserId======>"+currentUserId);
        if(lead.getOwner().intValue() == currentUserId.intValue()){
            flag = true;
        }
//        resultMap.put("list",rstList);
        resultMap.put("list",historyList);
        resultMap.put("flag",flag);
        resultMap.put("leadStatus",lead.getLeadStatus());
        return ResponseData.success(resultMap);
    }




    /**
     * 销售顾问-新建线索前 根据手机号+经销商 判断线索是否存在
     * @param dealerId
     * @param phone
     * @return
     */
    @Override
    @RequestMapping(value = "/consultant/checkLeadExisted", method = RequestMethod.POST)
    public ResponseData<List<LeadDTO>> getLeadListByDealerIdAndPhone(String dealerId, String phone) {
        List<Lead> result = leadSearchMapper.selectByDealerIdAndPhone(dealerId,phone);
        List<LeadDTO> resultList = new ArrayList<>();
        if(result == null || result.size() == 0){
            return ResponseData.success(resultList);
        }else{
            for (Lead lead: result) {
                LeadDTO leadDTO = new LeadDTO();
                BeanUtils.copyProperties(lead,leadDTO);
                if(lead.getDealer() != null){
                    com.chery.exeed.crm.common.dto.DealerDTO dealerDTO = dealerService.detailsDealer(lead.getDealer());
                    if(dealerDTO != null ){
                        leadDTO.setDealerName(dealerDTO.getDealerName());
                    }
                }
                if(lead.getLeadStatus() != null){
                    ResponseData<List<MetaDataDTO>> leadStatus = metaService.getCommonMetaList("lead_status", lead.getLeadStatus());
                    if(leadStatus != null && leadStatus.getData().size()>0){
                        leadDTO.setLeadStatusDesc(leadStatus.getData().get(0).getDescription());
                    }
                }
                resultList.add(leadDTO);
            }
        }
        return ResponseData.success(resultList);
    }

    /**
     * 销售助手-销售顾问-创建线索
     * @param leadDTO
     * @return
     */
    @Override
    @RequestMapping(value = "/consultant/createLeadByConsultant", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class,transactionManager = "leadTransactionManager")
    public ResponseData<Integer> createLeadByConsultant(@RequestBody LeadDTO leadDTO) {
        if(StringUtils.isEmpty(leadDTO.getPhone())){
            logger.error("销售顾问新建线索-线索创建失败,联系电话为空");
            return ResponseData.fail("电话号码不能为空");
        }
        Map<String,Object> param = new HashMap<>();
        param.put("phone",leadDTO.getPhone());
        Integer currentUserId = SessionHelper.getCurrentUserId();
        UserDetailDTO userInfo = leadCommonService.getUserInfo(currentUserId);
        param.put("dealer",userInfo.getDealerId());

        logger.info("经销商新建查重,查重条件:手机号和经销商..[{}][{}]",leadDTO.getPhone(),userInfo.getDealerId());
        List<Lead> leadList = leadSearchMapper.searchLeadByPhoneNoForSended(param);
        logger.info("经销商新建查重,查重结果:手机号[{}]经销商[{}][{}]",
                leadDTO.getPhone(),userInfo.getDealerId(),leadList==null?null:leadList.size());
        Lead lead ;
        Date now = new Date();
        boolean isNew;//是否新线索
        if(leadList!=null && leadList.size()>0){
            return ResponseData.fail("已存在相同手机号!");
            /*isNew=false;
            lead = leadList.get(0);
            leadDTO.setDealerOrderManager(null);*/
        }else {
            isNew = true;
            lead = new Lead();
        }
        logger.info("合并线索,合并Lead:[{}]",JSON.toJSONString(leadDTO));
        logger.info("合并线索,合并前Lead:[{}]",JSON.toJSONString(lead));
        BeanUtils.copyPropertiesHasValue(leadDTO,lead);
        logger.info("合并线索,合并后:[{}]",JSON.toJSONString(lead));
        lead.setId(null);
        lead.setModifyBy(currentUserId);
        lead.setModifyDate(now);
        Integer custId = lead.getCustId();
        Integer leadId = null;
        if(isNew){
            lead.setCreatedBy(currentUserId);
            lead.setCustomerCarDate(now);
            lead.setDealer(userInfo.getDealerId());
            PreLeadDTO preLeadDTO = new PreLeadDTO();
            logger.info("销售助手新建线索--根据手机号查询关联客户..");
            logger.info("合并线索,合并Lead:[{}]",JSON.toJSONString(preLeadDTO));
            logger.info("合并线索,合并前Lead:[{}]",JSON.toJSONString(lead));
            BeanUtils.copyPropertiesHasValue(lead,preLeadDTO);
            logger.info("合并线索,合并后:[{}]",JSON.toJSONString(preLeadDTO));
            CustomerDTO customerDTO = leadCommonService.setLead2Customer(preLeadDTO, now,currentUserId,custId);
            lead.setCustId(customerDTO.getId());
            /**新建的线索为已分配,直接分配给自己*/
            lead.setLeadStatus(LEAD_STATUS_ENUME.DISTRIBUTED.getValue());
            lead.setOwner(currentUserId);
            lead.setFollowBy(currentUserId);
            lead.setFollowTime(now);
            leadSearchMapper.insertReturnPK(lead);
            leadId = lead.getId();
            /**insert lead_channel*/
            if(leadDTO.getChannelCode() != null || leadDTO.getActivityCode()!= null){
                LeadChannelInputDTO leadChannelInputDTO = new LeadChannelInputDTO();
                leadChannelInputDTO.setLeadId(leadId);
                if(leadDTO.getChannelCode() != null ){
                    leadChannelInputDTO.setChannelCode(leadDTO.getChannelCode());
                }
                if(leadDTO.getActivityCode() != null){
                    leadChannelInputDTO.setActCode(leadDTO.getActivityCode());
                }
                /**
                 *从线索池取渠道
                 *leadChannelService.createLeadChannel(leadChannelInputDTO);
                 */


            }
            logger.info("销售顾问创建线索=====lead_ID:"+leadId);
            lead.setLeadNumber("L"+(100000000+leadId+"").substring(1));
            leadMapper.updateByPrimaryKeySelective(lead);
            /**新建线索池*/
            PreLead preLead = new PreLead();
            BeanUtils.copyProperties(leadDTO,preLead);
            preLead.setCollectTime(now);
            preLead.setLeadId(leadId);
            preLead.setStatus(0);
            preLead.setCreatedBy(currentUserId);
            preLead.setCustomerCarDate(now);
            preLeadMapper.insert(preLead);
        }else{
            logger.info("线索撞单了,撞单线索ID:"+lead.getId());
            leadMapper.updateByPrimaryKeySelective(lead);
            return ResponseData.fail("insert lead failed");
        }

        /**2.insert lead_followup*/
        LeadFollowup thisFollowup = new LeadFollowup();
        thisFollowup.setLeadId(lead.getId());
        thisFollowup.setDistributeTo(currentUserId);
        thisFollowup.setCreateBy(currentUserId.toString());
        thisFollowup.setCreateTime(now);
        thisFollowup.setStatus(1);
        thisFollowup.setLeadStatus(lead.getLeadStatus());
        thisFollowup.setFollowPlan(DateUtils.dayOffset(now,1));
        thisFollowup.setFollowDate(DateUtils.dayOffset(now,1));
        leadFollowupSearchMapper.insert(thisFollowup);

        LeadFollowup nextFollowup = new LeadFollowup();
        nextFollowup.setCreateTime(now);
        nextFollowup.setLeadId(lead.getId());
        nextFollowup.setStatus(0);
        nextFollowup.setDistributeTo(currentUserId);
        nextFollowup.setFollowPlan(DateUtils.dayOffset(now,1));
        leadFollowupSearchMapper.insert(nextFollowup);
        /**3.insert followup_history  */
        /**新建*/
        FollowupHistory fh1 = new FollowupHistory();
        fh1.setLeadId(lead.getId());
        fh1.setBizId(thisFollowup.getId());
        fh1.setBizType(0);
        fh1.setOperateDesc("新建线索");
        fh1.setOperateTime(now);
        fh1.setOperateResultStatus(1);
        fh1.setOperateBy(currentUserId.toString());
        fh1.setOperateResultDesc("星途顾问 "+userInfo.getNickName()+" 新建线索");
        String dealerOrderManagerStr = lead.getDealerOrderManager();
        Integer dealerOrderManager=null;
        if(NumberUtils.isDigits(dealerOrderManagerStr)){
            dealerOrderManager = Integer.parseInt(dealerOrderManagerStr);
        }
        followupHistorySearchMapper.insertHistory(fh1);
        leadCommonService.insertLeadHistory(leadId,now,currentUserId,LEAD_STATUS_ENUME.CREATED.getValue(),LEAD_STATUS_ENUME.CREATED.getDesc(),
                userInfo.getDealerId(),dealerOrderManager,currentUserId);
        /**分配*/
        FollowupHistory fh2 = new FollowupHistory();
        fh2.setLeadId(lead.getId());
        fh2.setBizId(thisFollowup.getId());
        fh2.setBizType(7);
        fh2.setOperateDesc("线索分配");
        fh2.setOperateTime(now);
        fh2.setOperateResultStatus(7);
        fh2.setOperateBy(currentUserId.toString());
        fh2.setOperateResultDesc(LeadConstants.DISTRIBUTE_MESSAGE.replace("${key}", userInfo.getNickName()));
        followupHistorySearchMapper.insertHistory(fh2);
        leadCommonService.insertLeadHistory(leadId,now,currentUserId,LEAD_STATUS_ENUME.DISTRIBUTED.getValue(),LEAD_STATUS_ENUME.DISTRIBUTED.getDesc(),
                userInfo.getDealerId(),dealerOrderManager,currentUserId);
        return ResponseData.success(leadId);
    }

    /**
     * 销售助手-销售顾问-首页(1.新线索 2.待到店/待试驾 3.已下定 4.待交车 5.待跟进 6.已成交)线索列表
     * @return
     */
    @Override
    @RequestMapping(value = "/consultant/assistantIndexLeadList", method = RequestMethod.GET)
    public ResponseData<Map<String, List<LeadResultDTO>>> assistantIndexLeadList() {
        logger.info("销售助手首页  userId = " + SessionHelper.getCurrentUserId());
        HashMap<String, List<LeadResultDTO>> map = new HashMap<>();
        LeadDTO leadDTO = new LeadDTO();
        Integer currentUserId = SessionHelper.getCurrentUserId();
        UserDetailDTO userInfo = leadCommonService.getUserInfo(currentUserId);
        leadDTO.setDealer(userInfo.getDealerId());
        leadDTO.setOwner(currentUserId);
        //新线索=未跟进线索 and status = 6 ，显示个数，点击进入列表
        List<LeadResultDTO> data1 = leadSearchMapper.queryNewLeadList(leadDTO);
        data1 =this.getFollowPlan(data1);
        map.put("NEW",data1);
        //待到店/待试驾=邀约到店及预约试驾线索，显示个数，点击进入列表
        List<Integer> status = new ArrayList<>();
        status.add(LEAD_STATUS_ENUME.NOT_PREDICT.getValue());
        status.add(LEAD_STATUS_ENUME.NOT_ARRIVAL.getValue());
        List<LeadResultDTO> data2 = leadSearchMapper.queryTaskLeadList(userInfo.getDealerId(),currentUserId,status);
        data2 =this.getFollowPlan(data2);
        map.put("NOT_ARRIVAL AND NOT_PREDICT",data2);
        //已下订=已下订线索，显示个数，点击进入列表
        leadDTO.setLeadStatus(LEAD_STATUS_ENUME.ORDER.getValue());
        List<LeadResultDTO> data4 = leadSearchMapper.queryLeadList(leadDTO);
        data4 =this.getFollowPlan(data4);
        map.put("ORDER",data4);
        //待交车=已下订线索且交车日期不为空，显示个数，点击进入列表
        List<LeadResultDTO> data5 = leadSearchMapper.queryDeliveryLeadsList(leadDTO);
        data5 =this.getFollowPlan(data5);
        map.put("DELIVERY",data5);
        //待跟进=非,新线索,待到店，待试驾，待交车的跟进中线索，显示个数，点击进入列表
        List<Integer> status2 = new ArrayList<>();
        status2.add(LEAD_STATUS_ENUME.NOT_PREDICT.getValue());
        status2.add(LEAD_STATUS_ENUME.NOT_ARRIVAL.getValue());
        List<LeadResultDTO> data6 = leadSearchMapper.queryFollowupLeadList(userInfo.getDealerId(),currentUserId,status);
        map.put("FOLLOW_UP",data6);
        //已成交=赢单线索，显示个数，点击进入列表
        leadDTO.setLeadStatus(LEAD_STATUS_ENUME.WIN.getValue());
        List<LeadResultDTO> data7 = leadSearchMapper.queryLeadList(leadDTO);
        data7 =this.getFollowPlan(data7);
        map.put("WIN",data7);
        return ResponseData.success(map);
    }

    /**
     * 销售助手-销售顾问-首页(新)  [1].跟进任务（新线索，待跟进） [2].重要预约（预约到店/试驾，待交车）[3].我的业绩（已下订，已成交）
     * @return
     */
    @Override
    @RequestMapping(value = "/consultant/myTaskLeadCount", method = RequestMethod.GET)
    public ResponseData<Map<String, Integer>> myTaskLeadCount() {
        logger.info("销售助手首页  userId = " + SessionHelper.getCurrentUserId());
        HashMap<String, Integer> map = new HashMap<>();
        LeadDTO leadDTO = new LeadDTO();
        Integer currentUserId = SessionHelper.getCurrentUserId();
        UserDetailDTO userInfo = leadCommonService.getUserInfo(currentUserId);
        leadDTO.setDealer(userInfo.getDealerId());
        leadDTO.setOwner(currentUserId);

        //新线索=未跟进线索 and status = 6 ，显示个数，点击进入列表
        Integer newLead = leadSearchMapper.queryNewLeadListCount(leadDTO);
        map.put("NEW",newLead);
        //待跟进=非,新线索,待到店，待试驾，待交车的跟进中线索，显示个数，点击进入列表
        Integer FOLLOW_UP = leadSearchMapper.queryFollowupLeadCount(userInfo.getDealerId(),currentUserId);
        map.put("FOLLOW_UP",FOLLOW_UP);
        //已超期=线索跟进中，已超期
        Integer EXPIRED = leadSearchMapper.queryExpiredLeadCount(userInfo.getDealerId(),currentUserId);
        map.put("EXPIRED",EXPIRED);
        /**重要预约*/
        //试驾=邀约到店及预约试驾线索，显示个数，点击进入列表
        Integer NOT_ARRIVAL_AND_NOT_PREDICT = leadSearchMapper.queryNotPredictAndNotArrivalLeadCount(userInfo.getDealerId(),currentUserId);
        map.put("NOT_ARRIVAL AND NOT_PREDICT",NOT_ARRIVAL_AND_NOT_PREDICT);
        /**我的业绩*/
        //已下订=已下订线索，显示个数，点击进入列表
        leadDTO.setLeadStatus(LEAD_STATUS_ENUME.ORDER.getValue());
        Integer ORDER = leadSearchMapper.queryOrderLeadCount(leadDTO);
        map.put("ORDER",ORDER);
        //已成交=赢单线索，显示个数，点击进入列表
        leadDTO.setLeadStatus(LEAD_STATUS_ENUME.WIN.getValue());
        Integer WIN = leadSearchMapper.queryWinLeadCount(leadDTO);
        map.put("WIN",WIN);
        //已战败=战败线索
        Integer LOSE = leadSearchMapper.queryLoseLeadCount(leadDTO);
        map.put("LOSE",LOSE);
        logger.debug("NEW="+newLead+"|NOT_ARRIVAL AND NOT_PREDICT="+NOT_ARRIVAL_AND_NOT_PREDICT+"|ORDER"+ORDER+"|FOLLOW_UP"+FOLLOW_UP+"|WIN"+WIN);
        return ResponseData.success(map);
    }

    /**
     * 微信公众号消息推送
     * @return
     */
    @Override
    @RequestMapping(value = "/consultant/pushLeadNumToConsultant", method = RequestMethod.GET)
    public ResponseData<String> pushLeadNumToConsultant() {
        String returnMessage = "";
        ResponseData<Set<Integer>> consulList = sysUserService.listDealerUserIdByType(2);//销售顾问
        List<EstabMessageDTO> resultParan = new ArrayList<>();
        if (consulList!=null && !consulList.getData().isEmpty()){
            Iterator<Integer> iterator = consulList.getData().iterator();
            while (iterator.hasNext()){
                EstabMessageDTO estabMessageDTO = new EstabMessageDTO();
                Map<String, Object> map = new HashMap<>();
                Integer userId = iterator.next();
                LeadDTO leadDTO = new LeadDTO();
                UserDetailDTO userInfo = leadCommonService.getUserInfo(userId);
                String dealerId = userInfo.getDealerId();
                leadDTO.setDealer(userInfo.getDealerId());
                leadDTO.setOwner(userId);
                Integer newLead = leadSearchMapper.queryNewLeadListCount(leadDTO);
                Integer todaycount=leadSearchMapper.queryTodayCount(leadDTO);
                if (newLead==null){
                    newLead = 0;
                }
                if (newLead!=0) {
                    map.put("count", newLead);//新线索
                    map.put("todaycount",todaycount==null?0:todaycount);
                    estabMessageDTO.setMsgMap(map);
                    List<Integer> toUser = new ArrayList<Integer>();
                    toUser.add(userId);
                    estabMessageDTO.setToCrmUser(toUser);
                    estabMessageDTO.setModuleCode("10011003");//模板id
                    resultParan.add(estabMessageDTO);
                }
            }
        }else {
            returnMessage = "没有查询到销售顾问。";
        }
        if (resultParan!=null && resultParan.size()>0){
            sysCommonService.appSendMsg(resultParan);
            returnMessage = "发送消息完成。";
        }else{
            returnMessage = "没有生成可发送的消息。";
        }
        return ResponseData.success(returnMessage);
    }


    /**
     * 微信公众号消息推送
     * @return
     */
    @Override
    @RequestMapping(value = "/consultant/pushLeadNumToDealerManager", method = RequestMethod.GET)
    public ResponseData<String> pushLeadNumToDealerManager() {
        String returnMessage = "";
        ResponseData<Set<Integer>> manageList = sysUserService.listDealerUserIdByType(1);//订单经理
        List<EstabMessageDTO> resultParan = new ArrayList<>();
        if (manageList!=null && !manageList.getData().isEmpty()){
            Iterator<Integer> iterator = manageList.getData().iterator();
            while (iterator.hasNext()){
                Map<String, Object> map = new HashMap<>();
                Integer userId = iterator.next();
                UserDetailDTO user = leadCommonService.getUserInfo(userId);
                String dealerId = user.getDealerId();
                LeadDTO dto = new LeadDTO();
                //待审批
                dto.setLeadStatus(LEAD_STATUS_ENUME.DEFEAT_APPLY.getValue());
                dto.setDealer(dealerId);
                List<LeadResultDTO> leadResultDTOS = leadSearchMapper.queryDefeatLeadList(dto);
                int conunt = 0;
                if(leadResultDTOS != null ){
                    map.put("needcount",leadResultDTOS.size());
                    Integer todaycount=leadSearchMapper.queryDefeatLeadCount(dto);
                    map.put("todaycount",todaycount==null?0:todaycount);
                    conunt = leadResultDTOS.size();
                }else{
                    conunt = 0;
                    map.put("needcount",0);
                }
                List<Integer> toUser =new ArrayList<Integer>();
                toUser.add(userId);
                if (conunt!=0) {
                    EstabMessageDTO estab04 = new EstabMessageDTO();
                    estab04.setMsgMap(map);
                    estab04.setToCrmUser(toUser);
                    estab04.setModuleCode("10011004");//模板id
                    resultParan.add(estab04);
                }
                Map<String, Object> rstMap = new HashMap<>();
                //待分配
                List<Integer> rstList = new ArrayList<>();
                rstList.add(LEAD_STATUS_ENUME.NOT_DISTRIBUTE.getValue());
                List<LeadResultDTO> data = leadSearchMapper.queryUndistributedLeadLists(dealerId,rstList,userId,null,null,null);
                if(data != null){
                    rstMap.put("count",data.size());
                    Integer todaycount=leadSearchMapper.queryUndistributedLeadCount(dealerId,rstList,userId);
                    rstMap.put("todaycount1",todaycount==null?0:todaycount);
                    conunt = data.size();
                }else{
                    conunt = 0;
                    rstMap.put("count",0);
                }
                if (conunt!=0) {
                    EstabMessageDTO estab02 = new EstabMessageDTO();
                    estab02.setMsgMap(rstMap);
                    estab02.setToCrmUser(toUser);
                    estab02.setModuleCode("10011002");//模板id
                    resultParan.add(estab02);
                }
                //跟进逾期--本店共有xx条线索跟进逾期，请及时督促顾问跟进
                Map<String, Object> rateMap = new HashMap<>();
                List<LeadFollowupDTO> followUpMap = leadFollowupSearchMapper.getSuperviseLeadTotal(dealerId);//订单经理的经销商编号
                if(null != followUpMap && followUpMap.size()>0){
                    //带督促数量
                    rateMap.put("followcount",followUpMap.get(0).getTotal());
                    conunt = followUpMap.get(0).getTotal();
                }else{
                    conunt = 0;
                    rateMap.put("followcount",0);
                }
                if (conunt!=0) {
                    EstabMessageDTO estab = new EstabMessageDTO();
                    estab.setMsgMap(rateMap);
                    estab.setToCrmUser(toUser);
                    estab.setModuleCode("10011005");//模板id
                    resultParan.add(estab);
                }
            }
        }else {
            returnMessage = "没有查询到销售顾问。";
        }
        if (resultParan!=null && resultParan.size()>0){
            sysCommonService.appSendMsg(resultParan);
            returnMessage = "发送消息完成。";
        }else{
            returnMessage = "没有生成可发送的消息。";
        }
        return ResponseData.success(returnMessage);
    }

    /**
     * 微信公众号消息推送
     * @return
     */
    @Override
    @RequestMapping(value = "/consultant/pushLeadNumToExperienceManager", method = RequestMethod.GET)
    public ResponseData<String> pushLeadNumToExperienceManager() {
        String returnMessage = "";
        ResponseData<Set<Integer>> manageList = sysUserService.listDealerUserIdByType(3);//体验专员
        List<EstabMessageDTO> resultParan = new ArrayList<>();
        if (manageList!=null && !manageList.getData().isEmpty()){
            Iterator<Integer> iterator = manageList.getData().iterator();
            while (iterator.hasNext()){
                EstabMessageDTO estabMessageDTO = new EstabMessageDTO();
                Map<String, Object> map = new HashMap<>();
                Integer userId = iterator.next();
                UserDetailDTO user = leadCommonService.getUserInfo(userId);
                String dealerId = user.getDealerId();
                LeadDTO leadDTO = new LeadDTO();
                leadDTO.setDealer(dealerId);
                leadDTO.setLeadStatus(2);
                List<LeadResultDTO> data = leadSearchMapper.queryLeadList(leadDTO);
                //体验专员待处理线索
                int count = 0;
                if(data != null){
                    map.put("count",data.size());
                    Integer todaycount = leadSearchMapper.queryLeadListTodaycount(leadDTO);
                    map.put("todaycount",todaycount==null?0:todaycount);
                    count = data.size();
                }else{
                    map.put("count",0);
                }
                if (count!=0) {
                    estabMessageDTO.setMsgMap(map);
                    List<Integer> toUser = new ArrayList<Integer>();
                    toUser.add(userId);
                    estabMessageDTO.setToCrmUser(toUser);
                    estabMessageDTO.setModuleCode("10011001");//模板id
                    resultParan.add(estabMessageDTO);
                }
            }
        }else {
            returnMessage = "没有查询到体验专员。";
        }
        if (resultParan!=null && resultParan.size()>0){
            sysCommonService.appSendMsg(resultParan);
            returnMessage = "发送消息完成。";
        }else{
            returnMessage = "没有生成可发送的消息。";
        }
        return ResponseData.success(returnMessage);
    }

    @Override
    @RequestMapping(value = "/queryRegion", method = RequestMethod.GET)
    public ResponseData<List<Object>> queryAssistantRegion() {
        List<AssistantProvinceDTO> assistantProvinceDTOS = regionService.queryProvinceByAssistant();
        List<AssistantCityDTO> assistantCityDTOS = regionService.queryCityByAssistant();
        List<Object> result = new ArrayList<>();
        result.add(assistantProvinceDTOS);
        result.add(assistantCityDTOS);
        return ResponseData.success(result);
    }




    /**
     * 报价
     * @param leadId 线索ID
     * @param modelName 车型名称
     * @param feedback 客户反馈
     * @param userId 用户ID
     * @param
     * @return
     */
    @Override
    @RequestMapping(value = "/consultant/updateLeadToOffer", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class,transactionManager = "leadTransactionManager")
    public ResponseData<Integer> updateLeadToOffer(Integer leadId,String seriesName, String modelName, String feedback,Integer userId) {
        String modelNameDesc = "";
        CarModelDTO carModelByCode = carService.getCarModelByCode(seriesName, modelName);
        if(carModelByCode != null){
            modelNameDesc = carModelByCode.getModelName();
        }
        String seriesNameDesc = "";
        CarSeriesDTO carSeriesByCode = carService.getCarSeriesByCode(seriesName);
        if(carSeriesByCode != null){
            seriesNameDesc = carSeriesByCode.getSeriesName();
        }
        Map<String,Object> param = new HashMap<>();
        param.put("leadId",leadId);
        LeadResultDTO result = leadSearchMapper.getLeadById(param);
        if(result.getDealer() == null){
            return ResponseData.fail(LEAD_RESPONSE_FAIL,"该线索未分配经销商");
        }
        if(result.getLeadStatus().equals(LEAD_STATUS_ENUME.LOSE.getValue()) ||result.getLeadStatus().equals(LEAD_STATUS_ENUME.DEFEAT_APPLY.getValue())||result.getLeadStatus().equals(LEAD_STATUS_ENUME.WIN.getValue())||result.getLeadStatus().equals(LEAD_STATUS_ENUME.ORDER.getValue())){
            logger.info("leadNum:{"+result.getLeadNumber()+"} + leadStatus:{"+result.getLeadStatus()+"} 无法执行下定操作!");
            return ResponseData.fail(LEAD_STATUS_NOT_LEGLE,"该线索状态下不允许此操作");
        }
        String dealer = result.getDealer();
        String dealerOrderManager = result.getDealerOrderManager();
        Integer owner = result.getOwner();
        Lead lead = new Lead();
        lead.setId(leadId);
        //lead.setLevel("5");
        lead.setLeadStatus(LEAD_STATUS_ENUME.PRICE_OFFER.getValue());
        Integer currentUserId = SessionHelper.getCurrentUserId();
        lead.setModifyBy(currentUserId);
        Date now = new Date();
        lead.setModifyDate(now);
        leadMapper.updateByPrimaryKeySelective(lead);
        OfferOperateRecord oor = new OfferOperateRecord();
        oor.setLeadId(result.getId());
        oor.setFeedback(feedback);
        oor.setSeriesName(seriesName);
        oor.setModelName(modelName);
        oor.setOperateTime(now);
        oor.setLeadStatus(LEAD_STATUS_ENUME.PRICE_OFFER.getValue());
        oor.setOperateBy(userId);
        offerOperateRecordSearchMapper.insertOfferOperation(oor);
        FollowupHistory fh = new FollowupHistory();
        fh.setLeadId(result.getId());
        //3.报价
        fh.setLeadId(leadId);
        fh.setBizType(3);
        fh.setBizId(oor.getId());
        fh.setOperateResultStatus(LEAD_STATUS_ENUME.PRICE_OFFER.getValue());
        fh.setOperateTime(now);
        fh.setOperateBy(result.getOwner().toString());
        fh.setOperateResultDesc("意向车型 "+seriesNameDesc+" "+modelNameDesc);
        fh.setExtraInfo(feedback);
        followupHistorySearchMapper.insertHistory(fh);
        leadCommonService.insertLeadHistory(leadId,now,currentUserId,LEAD_STATUS_ENUME.PRICE_OFFER.getValue(),LEAD_STATUS_ENUME.PRICE_OFFER.getDesc()
                ,dealer,Integer.parseInt(dealerOrderManager),owner);
        return ResponseData.success(1);
    }

    /**下订前查询用户的证件号*/
    @Override
    @RequestMapping(value = "/consultant/queryCredentialIdForOrder", method = RequestMethod.GET)
    public ResponseData<CustomerDTO> queryCredentialIdForOrder(Integer leadId) {
        LeadDTO leadDTO = leadSearchMapper.detailForUpdate(leadId);
        CustomerDTO dto = new CustomerDTO();
        if(leadDTO == null){
            return ResponseData.fail("线索不存在!");
        }
        if(leadDTO.getLeadStatus().equals(LEAD_STATUS_ENUME.LOSE.getValue()) ||leadDTO.getLeadStatus().equals(LEAD_STATUS_ENUME.DEFEAT_APPLY.getValue())||leadDTO.getLeadStatus().equals(LEAD_STATUS_ENUME.WIN.getValue())){
            logger.info("leadNum:{"+leadDTO.getLeadNumber()+"} + leadStatus:{"+leadDTO.getLeadStatus()+"} 无法执行下定操作!");
            return ResponseData.fail(LEAD_STATUS_NOT_LEGLE,"该线索状态下不允许下订");
        }
        Integer custId = leadDTO.getCustId();
        logger.info("custId============>"+custId);
        Customer customer = customerSearchMapper.getSendDMSCustomerById(custId);
        BeanUtils.copyProperties(customer,dto);
        Integer custStatus = customer.getCustomerStatus();
        logger.info("custStatus ====="+custStatus);
        if(custStatus == 1 && custStatus == null){
            //潜客:查询 试乘试驾上传的证件号
            PredictOperateRecordDTO record = predictOperateRecordSearchMapper.selectByLeadId(leadId);
            if(record != null){
                String idCard = record.getIdCard();
                if(StringUtils.isNotBlank(idCard)){
                    dto.setCredentialNumber(idCard);
                    dto.setCredentialType(1);
                }
            }
        }
        return ResponseData.success(dto);
    }




    /**
     * 下订
     * @param leadId 线索ID
     * @param custName 客户姓名
     * @param phone 客户电话
     * @param documentType 证件类型
     * @param documentId 证件号
     * @param forDate 提车日期
     * @param userId 用户Id
     * @return
     */
    @Override
    @RequestMapping(value = "/consultant/updateLeadToOrder", method = RequestMethod.POST)
    public ResponseData<Integer> updateLeadToOrder(Integer leadId, String custName, String phone, Integer documentType, String documentId, Date forDate, Integer userId,String address,String orderMoney,String materialCode) {
        Lead lead = leadMapper.selectByPrimaryKey(leadId);
        Integer custId = lead.getCustId();
        if(lead == null){
            logger.info("销售助手-下定失败;leadId[" + leadId + "].Data return[NULL]");
        }
        if(lead.getLeadStatus().equals(LEAD_STATUS_ENUME.LOSE.getValue())
                ||lead.getLeadStatus().equals(LEAD_STATUS_ENUME.DEFEAT_APPLY.getValue())
                ||lead.getLeadStatus().equals(LEAD_STATUS_ENUME.WIN.getValue())){
            logger.info("leadNum:{"+lead.getLeadNumber()+"} + leadStatus:{"+lead.getLeadStatus()+"} 无法执行下定操作!");
            return ResponseData.fail(LEAD_STATUS_NOT_LEGLE,"该线索状态下不允许此操作");
        }

        Integer lead_cust_id=lead.getCustId();
        //潜客前台 保存前台修改的custName
        Customer customer = customerSearchMapper.getSendDMSCustomerById(custId);
        if(customer.getCustomerStatus() == 1){
            String firstName = customer.getFirstName();
            if(firstName == null ||(StringUtils.isNotBlank(custName) && !firstName.equals(custName))){
                Customer cust = new Customer();
                cust.setId(custId);
                cust.setFirstName(custName);
                customerSearchMapper.updateByPrimaryKeySelective(cust);
                Lead newLead = new Lead();
                newLead.setId(lead.getId());
                newLead.setFirstName(custName);
                leadMapper.updateByPrimaryKeySelective(newLead);
            }
        }
        //3.校验客户是否重复(根据 身份证号查重)
        List<Customer> custList = customerSearchMapper.getCustomerByCredentialNum(documentId,lead_cust_id);
        if(custList != null && custList.size()>0){
            Customer targetCustomer  = custList.get(0);
            logger.info("开始合并逻辑...");
            customer = customerMapper.selectByPrimaryKey(lead_cust_id);
            Customer result;
            Integer targetCustomerStatus = targetCustomer.getCustomerStatus();
            Integer customerStatus = customer.getCustomerStatus();
            if(targetCustomerStatus !=null && targetCustomerStatus==2) {
                result = mergeCustomerInfo(customer, targetCustomer);
            }else if(customerStatus !=null && customerStatus==2) {
                result = mergeCustomerInfo(targetCustomer,customer);
            }else{
                result = mergeCustomerInfo(customer, targetCustomer);
            }
            lead.setCustId(result.getId());
            custId = result.getId();
        }
        Integer i = this.updateLeadToOrderWithTrasaction(leadId,custName,phone,documentType,documentId,forDate,userId,lead,address,orderMoney,materialCode);
        if(i != 1){
            logger.info("cust_id={"+custId+"}下订时更新客户身份证信息失败");
            return ResponseData.fail(LEAD_RESPONSE_FAIL,"update customer credential_number failed");
        }
        try{
            CustomerTranferInfoDTO infoDTO = new CustomerTranferInfoDTO();
            infoDTO.setId(custId);
            rabbitTemplate.convertAndSend(MQ_QUEUE_CUSTOMER,JSON.toJSONString(infoDTO));
        }catch (Exception ex){
            logger.info("Send to MQ_QUEUE_CUSTOMER Exception! cust_id="+custId+",Msg:"+ex.getMessage());
        }
        return ResponseData.success(1);
    }

    @Transactional(rollbackFor = Exception.class,transactionManager = "leadTransactionManager")
    public Customer mergeCustomerInfo(Customer customer,Customer targetCustomer){
        /**
         * 不同手机号，相同证件号合并：
         * 1.	合并触发时间：下订时收集到客户证件号在系统中已存在；
         * 2.	合并规则：
         * a.	手机号：新号码作为主联系方式，旧号码填入备用联系方式；
         * b.	主档其他字段
         * i.	潜客和潜客合并：保留修改时间最新的档案，新覆盖旧，有覆盖无；
         * ii.	潜客和保客合并：保留保客档案，已有字段不更新，有覆盖无；
         * c.	子表 - 线索： 关联关系更新；
         * d.	子表 – 工单：复制；
         * e.	子表 – 沟通历史：关联关系更新；
         * f.	子表 – 人车关系：关联关系更新；
         * */
        Date now = new Date();
        Integer currentUserId = SessionHelper.getCurrentUserId();
        MergeContentDTO mergeContent = new MergeContentDTO();
        mergeContent.setMergeCustomerInfo(customer);
        mergeContent.setTargetCustomerInfo(targetCustomer);
        CustomerMerge customerMerge = new CustomerMerge();
        Integer targetCustId = targetCustomer.getId();
        Integer custId = customer.getId();
        customerMerge.setCreateBy(currentUserId);
        customerMerge.setCreateTime(now);
        String firstPhone = customer.getPhone();
        String secondPhone = targetCustomer.getPhone();
        customerMerge.setTargetId(targetCustId);
        customerMerge.setFromId(custId);
        logger.info("工单信息merge.custID[{}]targetCustId[{}]",custId,targetCustId);
        ResponseData<List<Integer>> listResponseData = casesService.mergeCustomerCase(custId, targetCustId);
        mergeContent.setCaseIds(listResponseData.getData());
        List<Lead> leadList = leadSearchMapper.getAllCustLeadList(custId);
        logger.info("线索信息merge.custID[{}]targetCustId[{}]",custId,targetCustId);
        if(leadList!=null){
            List<Integer> leadIds = new ArrayList<>();
            for(Lead obj:leadList){
                obj.setCustId(targetCustId);
                obj.setModifyDate(now);
                obj.setModifyBy(currentUserId);
                leadMapper.updateByPrimaryKeySelective(obj);
                leadIds.add(obj.getId());
            }
            mergeContent.setLeadIds(leadIds);
        }
        logger.info("呼叫历史信息merge.custID[{}]targetCustId[{}]",custId,targetCustId);
        ResponseData<List<String>> aliyunData = aliyunApiService.mergeCustCallHistory(custId, targetCustId);
        mergeContent.setCallHistoryIds(aliyunData.getData());
        logger.info("人车关系信息merge.custID[{}]targetCustId[{}]",custId,targetCustId);
        APIResponse<List<Integer>> relationResponse = dmsLeadServie.mergeCustomerRelation(custId, targetCustId);
        mergeContent.setRelationIds(relationResponse.getData());
        customerMerge.setMergeContent(JSON.toJSONString(mergeContent));
        logger.info("插入合并历史merge.custID[{}]targetCustId[{}]",custId,targetCustId);
        customerMergeMapper.insert(customerMerge);

        if(targetCustomer.getCustomerStatus()!=null && targetCustomer.getCustomerStatus().intValue()==2) {
            BeanUtils.copyPropertiesHasValue(customer, targetCustomer);
        }else{
            BeanUtils.copyPropertiesFromOld(customer, targetCustomer);
        }
        customer.setId(custId);
        targetCustomer.setId(targetCustId);
        if(firstPhone!=null && !firstPhone.equals(secondPhone)) {
            targetCustomer.setPhone(firstPhone);
            targetCustomer.setPhoneTwo(secondPhone);
        }
        if(leadList!=null){
            Integer leadFrequency = targetCustomer.getLeadFrequency();
            if(leadFrequency==null){
                leadFrequency = 0;
            }
            targetCustomer.setLeadFrequency(leadFrequency+leadList.size());
        }

        targetCustomer.setModifyBy(currentUserId);
        targetCustomer.setModifyDate(now);
        logger.info("更新客户信息custId[{}]",targetCustId);
        customerMapper.updateByPrimaryKeySelective(targetCustomer);

        customer.setModifyBy(currentUserId);
        customer.setModifyDate(now);
        customer.setCustomerStatus(4);//
        logger.info("更新被合并客户信息custId[{}]",custId);
        customerMapper.updateByPrimaryKeySelective(customer);
        return targetCustomer;
    }

    @Transactional(rollbackFor = Exception.class,transactionManager = "leadTransactionManager")
    public Integer updateLeadToOrderWithTrasaction(Integer leadId, String custName, String phone, Integer documentType, String documentId, Date forDate, Integer userId, Lead result,String address,String orderMoney,String materialCode){
        //1.update lead
        String dealer = result.getDealer();
        String dealerOrderManager = result.getDealerOrderManager();
        Integer owner = result.getOwner();
        Lead lead = new Lead();
        lead.setId(leadId);
        lead.setLevel("6");
        lead.setLeadStatus(LEAD_STATUS_ENUME.ORDER.getValue());
        Integer currentUserId = SessionHelper.getCurrentUserId();
        lead.setModifyBy(currentUserId);
        Date now = new Date();
        lead.setModifyDate(now);
        leadMapper.updateByPrimaryKeySelective(lead);

        //2.insert order_operate_record
        OrderOperateRecord oor = new OrderOperateRecord();
        oor.setLeadId(leadId);
        oor.setCustomerName(custName);
        oor.setPhone(phone);
        oor.setCertType(documentType);
        oor.setCertNo(documentId);
        oor.setCarApplyDate(forDate);
        oor.setOperateTime(now);
        oor.setOperateBy(userId);
        oor.setAddress(address);
        oor.setOrderMoney(orderMoney);
        oor.setMaterialCode(materialCode);
        oor.setLeadStatus(LEAD_STATUS_ENUME.ORDER.getValue());
        orderOperateRecordSearchMapper.insertOrderOperatRecord(oor);
        logger.info("leadNum={"+result.getLeadNumber()+"}插入下订操作记录数据");
        //3.insert follow_history
        String userName = leadCommonService.getUserName(userId);
        FollowupHistory fh = new FollowupHistory();
        fh.setLeadId(leadId);
        fh.setBizType(4);
        fh.setBizId(oor.getId());
        fh.setOperateResultStatus(LEAD_STATUS_ENUME.ORDER.getValue());
        fh.setOperateTime(now);
        fh.setOperateBy(userId.toString());
        fh.setOperateResultDesc("客户"+custName+"已下订");
        followupHistorySearchMapper.insertHistory(fh);

        ArrivalOperateRecord arrivalOperateRecord = new ArrivalOperateRecord();
        arrivalOperateRecord.setOperateBy(userId);//操作人
        arrivalOperateRecord.setOperateTime(now);//操作时间
        arrivalOperateRecord.setArrivalTime(now);//到店离店时间即为操作时间
        arrivalOperateRecord.setLeaveTime(now);//到店离店时间即为操作时间
        arrivalOperateRecord.setIsArrival(1);//是否到店
        arrivalOperateRecord.setLeadStatus(lead.getLeadStatus());//线索状态
        arrivalOperateRecord.setLeadId(lead.getId());//线索编号
        arrivalOperateRecordSearchMapper.insertArrivalOperateRecord(arrivalOperateRecord);
        logger.info("leadNum={"+lead.getLeadNumber()+"}插入客户到店操作记录数据");
        //默认添加一次到店的跟进记录
        fh.setId(fh.getId()+1);
        fh.setBizType(8);
        fh.setOperateBy(userId.toString());
        fh.setBizId(arrivalOperateRecord.getId());
        fh.setOperateResultDesc("客户 "+custName+"已到店");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String nowStr = sdf.format(now);
        fh.setExtraInfo(nowStr);
        followupHistorySearchMapper.insertHistory(fh);
        logger.info("leadNum={"+result.getLeadNumber()+"} ||{ followupHistoryId"+ fh.getId() +"}插入到店操作历史数据");
        List<LeadFollowup> leadFollowups = leadFollowupSearchMapper.selectByLeadIdAndStatus(leadId, LEAD_FOLLOW_STATUS_ENUME.STATUS_NOT_FOLLOW.getValue());
        if(leadFollowups != null && leadFollowups.size()>0){
            for (LeadFollowup lf: leadFollowups) {
                //leadFollowupSearchMapper.deleteByPrimaryKey(lf.getId());
                leadFollowupSearchMapper.updateLeadIdInActive(lf.getLeadId());
            }
        }
        //4.更新cust身份证号信息
        Integer custId = result.getCustId();
        Customer customer = new Customer();
        customer.setId(custId);
        customer.setCredentialType(documentType);
        customer.setCredentialNumber(documentId);
        customer.setAddress(address);
        int i = customerSearchMapper.updateByPrimaryKeySelective(customer);
        leadCommonService.insertLeadHistory(leadId,now,currentUserId,LEAD_STATUS_ENUME.ORDER.getValue(),LEAD_STATUS_ENUME.ORDER.getDesc()
                ,dealer,Integer.parseInt(dealerOrderManager),owner);
        return i;
    }

    /**
     * 试驾
     * @return
     */
    @Override
    @RequestMapping(value = "/consultant/updateLeadToPredict", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class,transactionManager = "leadTransactionManager",propagation = Propagation.REQUIRED)
    public ResponseData<Integer> updateLeadToPredict(@RequestBody PredictOperateRecordDTO predictOperateRecordDTO) {
        Integer leadId = predictOperateRecordDTO.getLeadId();
        Integer userId = SessionHelper.getCurrentUserId();
        Lead result = leadMapper.selectByPrimaryKey(leadId);
        if(result == null){
            logger.info("销售助手-下订失败;leadId[" + leadId + "].Data return[NULL]");
        }
        if(result.getLeadStatus().equals(LEAD_STATUS_ENUME.LOSE.getValue()) ||result.getLeadStatus().equals(LEAD_STATUS_ENUME.DEFEAT_APPLY.getValue())||result.getLeadStatus().equals(LEAD_STATUS_ENUME.WIN.getValue())){
            logger.info("leadNum:{"+result.getLeadNumber()+"} + leadStatus:{"+result.getLeadStatus()+"} 无法执行下订操作!");
            return ResponseData.fail(LEAD_STATUS_NOT_LEGLE,"该线索状态下不允许此操作 ");
        }
        //1.update lead
        String dealer = result.getDealer();
        String dealerOrderManager = result.getDealerOrderManager();
        Integer owner = result.getOwner();
        Lead lead = new Lead();
        lead.setId(leadId);
        lead.setLeadStatus(LEAD_STATUS_ENUME.PREDICT.getValue());
        Integer currentUserId = SessionHelper.getCurrentUserId();
        lead.setModifyBy(currentUserId);
        Date now = new Date();
        lead.setModifyDate(now);
        leadMapper.updateByPrimaryKeySelective(lead);
        leadCommonService.insertLeadHistory(leadId,now,currentUserId,LEAD_STATUS_ENUME.PREDICT.getValue(),LEAD_STATUS_ENUME.PREDICT.getDesc() ,
                dealer,Integer.parseInt(dealerOrderManager),owner);
        //2.insert predict_operate_record
        PredictOperateRecord por = new PredictOperateRecord();
        por.setLeadId(leadId);
        por.setSeriesName(predictOperateRecordDTO.getSeriesName());
        por.setModelName(predictOperateRecordDTO.getModelName());
        por.setIsSelf(predictOperateRecordDTO.getIsSelf());
        por.setDrivingLicenseUrl(predictOperateRecordDTO.getDrivingLicenseUrl());
        por.setDrivingLicense(predictOperateRecordDTO.getDrivingLicense());
        por.setIdCardUrl(predictOperateRecordDTO.getIdCardUrl());
        por.setIdCard(predictOperateRecordDTO.getIdCard());
        por.setFeedback(predictOperateRecordDTO.getFeedback());
        por.setOperateBy(userId);
        por.setOperateTime(now);
        por.setOperationType(predictOperateRecordDTO.getOperationType());
        predictOperateRecordSearchMapper.insertPredictOperateRecord(por);
        logger.info("leadNum={"+result.getLeadNumber()+"}插入试驾操作记录数据");
        //3.insert follow_history
        FollowupHistory fh = new FollowupHistory();
        fh.setLeadId(leadId);
        fh.setBizType(2);
        fh.setBizId(por.getId());
        fh.setOperateResultStatus(LEAD_STATUS_ENUME.PREDICT.getValue());
        fh.setOperateTime(now);
        fh.setOperateBy(userId.toString());
        if("1".equals(predictOperateRecordDTO.getOperationType())){
            fh.setOperateResultDesc("完成试乘");
        }else{
            fh.setOperateResultDesc("完成试驾");
        }
        String modelName = predictOperateRecordDTO.getModelName();
        String seriesName = predictOperateRecordDTO.getSeriesName();
        String modelNameDesc = "";
        CarModelDTO carModelByCode = carService.getCarModelByCode(seriesName, modelName);
        if(carModelByCode != null){
            modelNameDesc = carModelByCode.getModelName();
        }
        String seriesNameDesc = "";
        CarSeriesDTO carSeriesByCode = carService.getCarSeriesByCode(seriesName);
        if(carSeriesByCode != null){
            seriesNameDesc = carSeriesByCode.getSeriesName();
        }

        fh.setExtraInfo(seriesNameDesc+" "+modelNameDesc);
        followupHistorySearchMapper.insertHistory(fh);

        ArrivalOperateRecord arrivalOperateRecord = new ArrivalOperateRecord();
        arrivalOperateRecord.setOperateBy(userId);//操作人
        arrivalOperateRecord.setModelName(modelNameDesc);//试驾车型
        arrivalOperateRecord.setOperateTime(now);//操作时间
        arrivalOperateRecord.setArrivalTime(now);//到店离店时间即为操作时间
        arrivalOperateRecord.setLeaveTime(now);//到店离店时间即为操作时间
        arrivalOperateRecord.setIsArrival(1);//是否到店
        arrivalOperateRecord.setLeadStatus(lead.getLeadStatus());//线索状态
        arrivalOperateRecord.setLeadId(lead.getId());//线索编号
//        arrivalOperateRecord.setFeedback(predictOperateRecordDTO.getFeedback());
        arrivalOperateRecordSearchMapper.insertArrivalOperateRecord(arrivalOperateRecord);
        logger.info("leadNum={"+lead.getLeadNumber()+"}插入客户到店操作记录数据");

        //默认添加一次到店的跟进记录
        fh.setId(fh.getId()+1);
        fh.setBizType(8);
        fh.setOperateBy(userId.toString());
        fh.setBizId(arrivalOperateRecord.getId());
        fh.setOperateResultDesc("客户 "+result.getFirstName()+"已到店");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String nowStr = sdf.format(now);
        fh.setExtraInfo(nowStr);
        followupHistorySearchMapper.insertHistory(fh);
        logger.info("leadNum={"+result.getLeadNumber()+"}插入试驾操作记录历史数据");
        return ResponseData.success(1);
    }


    /**
     * 客户到店
     *
     *
     */
    @Override
    @RequestMapping(value = "/consultant/updateLeadToArrival", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class,transactionManager = "leadTransactionManager",propagation = Propagation.REQUIRED)
    public ResponseData<Integer> updateLeadToArrival(@RequestBody ArrivalOperateRecordDTO arrivalOperateRecordDTO) {
        logger.info("线索到店==>leadId/arrivalTime/isArrival:  "+arrivalOperateRecordDTO.getLeadId()+"/"+arrivalOperateRecordDTO.getArrivalTime()+"/"+arrivalOperateRecordDTO.getIsArrival());
        Integer leadId = arrivalOperateRecordDTO.getLeadId();
        Integer userId = SessionHelper.getCurrentUserId();
        Lead lead = leadMapper.selectByPrimaryKey(leadId);
        if(leadId == null){
            logger.info("销售助手-下订失败;leadId[null]");
            return ResponseData.fail(LEAD_STATUS_NOT_LEGLE,"leadId is null");
        }
        if(lead == null){
            logger.info("销售助手-下订失败;leadId[" + leadId + "].Data return[NULL]");
            return ResponseData.fail(LEAD_STATUS_NOT_LEGLE,"lead is null ");
        }
        if(lead.getLeadStatus().equals(LEAD_STATUS_ENUME.LOSE.getValue()) ||lead.getLeadStatus().equals(LEAD_STATUS_ENUME.DEFEAT_APPLY.getValue())||lead.getLeadStatus().equals(LEAD_STATUS_ENUME.WIN.getValue())){
            logger.info("leadNum:{"+lead.getLeadNumber()+"} + leadStatus:{"+lead.getLeadStatus()+"} 无法执行下订操作!");
            return ResponseData.fail(LEAD_STATUS_NOT_LEGLE,"该线索状态下不允许此操作 ");
        }
        Date now = new Date();
        //1.insert into arrival_operate_record
        ArrivalOperateRecord arrivalOperateRecord = new ArrivalOperateRecord();
        BeanUtils.copyProperties(arrivalOperateRecordDTO,arrivalOperateRecord);
        arrivalOperateRecord.setOperateBy(userId);
        arrivalOperateRecord.setOperateTime(now);
        arrivalOperateRecord.setIsArrival(1);
        arrivalOperateRecord.setLeadStatus(lead.getLeadStatus());
        int result = arrivalOperateRecordSearchMapper.insertArrivalOperateRecord(arrivalOperateRecord);
        logger.info("leadNum={"+lead.getLeadNumber()+"}插入客户到店操作记录数据");
        //2.insert into followup_history
        FollowupHistory fh = new FollowupHistory();
        fh.setLeadId(leadId);
        fh.setBizId(arrivalOperateRecord.getId());
        fh.setBizType(8);
        fh.setOperateResultStatus(lead.getLeadStatus());
        fh.setOperateTime(now);
        fh.setOperateBy(userId.toString());
        if(arrivalOperateRecord.getIsArrival() != null && arrivalOperateRecord.getIsArrival() == 1){
            //已到店
            fh.setOperateResultDesc("客户 "+lead.getFirstName()+"已到店");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String nowStr = sdf.format(now);
            fh.setExtraInfo(nowStr);
        }
        else if(arrivalOperateRecord.getIsArrival() != null && arrivalOperateRecord.getIsArrival() == 0){
            //已到店
            arrivalOperateRecord.setIsArrival(1);
            fh.setOperateResultDesc("客户 "+lead.getFirstName()+"已到店");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String nowStr = sdf.format(now);
            fh.setExtraInfo(nowStr);
        }

        followupHistorySearchMapper.insertHistory(fh);
        logger.info("leadNum={"+lead.getLeadNumber()+"}插入到店操作记录历史数据");
        return ResponseData.success(1);
    }



    /**
     * 战败申请
     * @param leadId
     * @param loseType
     * @param feedback
     * @param userId
     * @return
     */
    @Override
    @RequestMapping(value = "/consultant/updateLeadToDefeatApply", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class,transactionManager = "leadTransactionManager",propagation = Propagation.REQUIRED)
    public ResponseData<Integer> updateLeadToDefeatApply(Integer leadId, Integer loseType,Integer loseType2, String feedback, Integer userId,String competingGoods) {
//        if(loseType==null && loseType2!=null){
//            return ResponseData.fail(PARAM_IN_FAIL_CODE, "入参错误，一级类别不能为空");
//        }else if(loseType!=null && loseType2==null){
//            Integer count1 = defeatOperateRecordSearchMapper.queryLoseTypeCount(loseType);
//            if(count1!=1){
//                return ResponseData.fail(PARAM_IN_FAIL_CODE, "入参错误，一级类别值错误");
//            }
//        }else if(loseType!=null && loseType2!=null){
//            //判断1、2级值是否正确
//            Integer count1 = defeatOperateRecordSearchMapper.queryLoseTypeCount(loseType);
//            Integer count2 = defeatOperateRecordSearchMapper.queryLoseTypeCount(loseType2);
//            if(count1!=1){
//                return ResponseData.fail(PARAM_IN_FAIL_CODE, "入参错误，一级类别值错误");
//            }else if(count2!=2){
//                return ResponseData.fail(PARAM_IN_FAIL_CODE, "入参错误，二级类别值错误");
//            }
//        }
        //判断接收的二级内容是否是其他 如果是其他就必须填‘客服反馈’
        if(null != loseType2){
            String meatName=defeatOperateRecordSearchMapper.queryLoseTypeName(loseType2);
            if(null !=meatName){
                if("其他".equals(meatName)){
                    if(null == feedback){
                        return ResponseData.fail(PARAM_IN_FAIL_CODE, "二级类别为其他，客服反馈不能为空");
                    }
                }
            }
        }
        logger.info("lead[{}]申请战败",leadId);
        Lead result = leadMapper.selectByPrimaryKey(leadId);
        if(result == null){
            logger.info("销售助手-申请战败;leadId[" + leadId + "].Data return[NULL]");
        }
        if(result.getLeadStatus().equals(LEAD_STATUS_ENUME.LOSE.getValue()) ||result.getLeadStatus().equals(LEAD_STATUS_ENUME.DEFEAT_APPLY.getValue())||result.getLeadStatus().equals(LEAD_STATUS_ENUME.WIN.getValue())){
            logger.info("leadNum:{"+result.getLeadNumber()+"} + leadStatus:{"+result.getLeadStatus()+"} 无法执行战败申请操作!");
            return ResponseData.fail(LEAD_STATUS_NOT_LEGLE,"该线索状态下不允许此操作 ");
        }
        //1.update lead
        String dealer = result.getDealer();
        String dealerOrderManager = result.getDealerOrderManager();
        Integer owner = result.getOwner();
        Lead lead = new Lead();
        lead.setId(leadId);
        lead.setLeadStatus(LEAD_STATUS_ENUME.DEFEAT_APPLY.getValue());
        Integer currentUserId = SessionHelper.getCurrentUserId();
        lead.setModifyBy(currentUserId);
        Date now = new Date();
        lead.setModifyDate(now);
        leadMapper.updateByPrimaryKeySelective(lead);
        leadCommonService.insertLeadHistory(leadId,now,currentUserId,LEAD_STATUS_ENUME.DEFEAT_APPLY.getValue(),
                LEAD_STATUS_ENUME.DEFEAT_APPLY.getDesc(),dealer,Integer.parseInt(dealerOrderManager),owner);

        //2.insert defeat_operate_record
        DefeatOperateRecord dor = new DefeatOperateRecord();
        dor.setLeadId(leadId);
        dor.setLoseType(loseType);
        dor.setLoseType2(loseType2);
        dor.setOperateBy(userId);
        dor.setOperateTime(now);
        dor.setFeedback(feedback);
        if(StringUtils.isNotBlank(competingGoods)){
            dor.setCompetingGoods(competingGoods);
        }
        if(loseType != 4){
            dor.setCompetingGoods("");
        }
        dor.setLeadStatus(LEAD_STATUS_ENUME.DEFEAT_APPLY.getValue());
        defeatOperateRecordSearchMapper.insertDefeatOperateRecord(dor);

        //3.insert follow_history
        String userName = leadCommonService.getUserName(userId);
        FollowupHistory fh = new FollowupHistory();
        fh.setLeadId(leadId);
        fh.setBizType(5);
        fh.setBizId(dor.getId());
        fh.setOperateResultStatus(LEAD_STATUS_ENUME.DEFEAT_APPLY.getValue());
        fh.setOperateTime(now);
        fh.setOperateBy(userId.toString());
        ResponseData<List<MetaDataDTO>> lostType = metaService.getLeadMetaList("defeat_lost_type", loseType);
        if(lostType != null && lostType.getData().size() == 1){
            String description = lostType.getData().get(0).getDescription();
            fh.setOperateResultDesc("由于 "+description+",提交战败申请");
        }else{
            fh.setOperateResultDesc("由于其他原因提交战败申请");
        }
        followupHistorySearchMapper.insertHistory(fh);
        logger.info("leadNum={"+result.getLeadNumber()+"}插入申请战败操作记录历史数据");
        return ResponseData.success(1);
    }

    /**
     * 创建跟进
     * @param leadId
     * @param followupType 跟进类型
     * @param followupPlan 计划跟进事件
     * @param followupDate 跟进事件
     * @param userId
     * @return
     */
    @Override
    @RequestMapping(value = "/consultant/createLeadFollowup", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class,transactionManager = "leadTransactionManager")
    public ResponseData<Integer> createLeadFollowup(Integer leadId, Integer followupType, Date followupPlan, Date followupDate, String name, Integer userId) {
        Lead result = leadMapper.selectByPrimaryKey(leadId);
        Date now = new Date();
        if(result == null){
            logger.info("销售助手-跟进失败;leadId[" + leadId + "].Data return[NULL]");
            return ResponseData.fail(LEAD_DATA_NOT_EXIST,"该线索状态不存在");
        }
        if(result.getLeadStatus().equals(LEAD_STATUS_ENUME.LOSE.getValue()) ||result.getLeadStatus().equals(LEAD_STATUS_ENUME.DEFEAT_APPLY.getValue())||result.getLeadStatus().equals(LEAD_STATUS_ENUME.WIN.getValue())){
            logger.info("leadNum:{"+result.getLeadNumber()+"} + leadStatus:{"+result.getLeadStatus()+"} 无法执行跟进操作!");
            return ResponseData.fail(LEAD_STATUS_NOT_LEGLE,"该线索状态下不允许此操作 ");
        }

        Integer currentUserId = SessionHelper.getCurrentUserId();
        String dealer = result.getDealer();
        String dealerOrderManager = result.getDealerOrderManager();

        //1 生成一条新的未跟进
        LeadFollowup thisFollowup = new LeadFollowup();
        LeadFollowup nextFollowup = new LeadFollowup();
        FollowupHistory followupHistory = new FollowupHistory();
        //未跟进:0 已跟进:1
        //1.1 将前这条线索的未跟进跟进状态置成已跟进并生成新的未跟进(未跟进状态1条/线索)
        thisFollowup.setLeadId(leadId);
        //name==>feedback
        thisFollowup.setName(name);
        thisFollowup.setDistributeTo(result.getOwner());
        thisFollowup.setCreateBy(userId.toString());
        thisFollowup.setCreateTime(now);
        thisFollowup.setFollowType(followupType);
        thisFollowup.setLeadStatus(result.getLeadStatus());
        /**如果是 试驾邀请,待到店 线索的状态要相应的修改*/
//        thisFollowup.setStatus(LEAD_FOLLOW_STATUS_ENUME.STATUS_NOT_FOLLOW.getValue());
        if(thisFollowup.getFollowType() == 4) {
            result.setLeadStatus(LEAD_STATUS_ENUME.NOT_PREDICT.getValue());
            thisFollowup.setLeadStatus(LEAD_STATUS_ENUME.NOT_PREDICT.getValue());
            followupHistory.setOperateResultStatus(LEAD_STATUS_ENUME.NOT_PREDICT.getValue());
            leadMapper.updateByPrimaryKeySelective(result);
            leadCommonService.insertLeadHistory(leadId,now,currentUserId,LEAD_STATUS_ENUME.NOT_PREDICT.getValue(),LEAD_STATUS_ENUME.NOT_PREDICT.getDesc()
                    ,dealer,Integer.parseInt(dealerOrderManager),currentUserId);
        }
        else if(thisFollowup.getFollowType() == 3){
            result.setLeadStatus(LEAD_STATUS_ENUME.NOT_ARRIVAL.getValue());
            thisFollowup.setLeadStatus(LEAD_STATUS_ENUME.NOT_ARRIVAL.getValue());
            followupHistory.setOperateResultStatus(LEAD_STATUS_ENUME.NOT_ARRIVAL.getValue());
            leadMapper.updateByPrimaryKeySelective(result);
            leadCommonService.insertLeadHistory(leadId,now,currentUserId,LEAD_STATUS_ENUME.NOT_ARRIVAL.getValue(),LEAD_STATUS_ENUME.NOT_ARRIVAL.getDesc()
                    ,dealer,Integer.parseInt(dealerOrderManager),currentUserId);
        }
        /**如果是其他跟进 且线索状态 是 已分配  则更改为 跟进中 */
        if(result.getLeadStatus().intValue() == LEAD_STATUS_ENUME.DISTRIBUTED.getValue()){
            result.setLeadStatus(LEAD_STATUS_ENUME.FOLLOWUP.getValue());
            thisFollowup.setLeadStatus(LEAD_STATUS_ENUME.FOLLOWUP.getValue());
            followupHistory.setOperateResultStatus(LEAD_STATUS_ENUME.FOLLOWUP.getValue());
            leadMapper.updateByPrimaryKeySelective(result);
        }

        List<LeadFollowup> lfList = leadFollowupSearchMapper.selectByLeadIdAndStatus(leadId, LEAD_FOLLOW_STATUS_ENUME.STATUS_NOT_FOLLOW.getValue());
        if(lfList != null && lfList.size()>0){
            //非首次跟进
            LeadFollowup lastFollowup = lfList.get(0);
            Integer leadStatus = lastFollowup.getLeadStatus();
            if(leadStatus != null){
                //是老数据 1.update status=0的跟进,新建一条本次跟进并跟新一下本次跟进,再新建一条下次跟进
                lastFollowup.setStatus(1);
                lastFollowup.setFollowDate(followupDate);
                leadFollowupSearchMapper.updateByPrimaryKeySelective(lastFollowup);

                LeadFollowup newFollowup = new LeadFollowup();
                BeanUtils.copyProperties(thisFollowup,newFollowup);
                newFollowup.setStatus(1);
                newFollowup.setFollowPlan(followupDate);
                newFollowup.setFollowDate(followupDate);
                leadFollowupSearchMapper.insert(newFollowup);

                followupHistory.setBizId(newFollowup.getId());
            }else{
                //新数据
                Integer id = lastFollowup.getId();
                Date followPlan = lastFollowup.getFollowPlan();
                BeanUtils.copyProperties(thisFollowup,lastFollowup);
                lastFollowup.setId(id);
                lastFollowup.setFollowPlan(followPlan);
                lastFollowup.setFollowDate(followupDate);
                lastFollowup.setStatus(1);
                leadFollowupSearchMapper.updateByPrimaryKeySelective(lastFollowup);
                followupHistory.setBizId(lastFollowup.getId());
            }
            nextFollowup.setCreateTime(now);
            nextFollowup.setFollowPlan(followupDate);
            nextFollowup.setLeadId(leadId);
            nextFollowup.setDistributeTo(userId);
            nextFollowup.setStatus(0);
            leadFollowupSearchMapper.insert(nextFollowup);
            logger.debug("有老数据的跟进,setBizId===>"+lastFollowup.getId());

        }else{
            //自动生成首次跟进：预计完成时间=创建时间+24小时
            thisFollowup.setFollowPlan(DateUtils.dayOffset(now,1));
            thisFollowup.setFollowDate(DateUtils.dayOffset(now,1));
            thisFollowup.setStatus(1);
            leadFollowupSearchMapper.insert(thisFollowup);

            nextFollowup.setLeadId(leadId);
            nextFollowup.setDistributeTo(userId);
            nextFollowup.setFollowPlan(DateUtils.dayOffset(now,1));
            nextFollowup.setStatus(0);
            leadFollowupSearchMapper.insert(nextFollowup);

            leadCommonService.insertLeadHistory(leadId,now,currentUserId,LEAD_STATUS_ENUME.FOLLOWUP.getValue(),LEAD_STATUS_ENUME.FOLLOWUP.getDesc()
                    ,dealer,Integer.parseInt(dealerOrderManager),currentUserId);

            followupHistory.setBizId(thisFollowup.getId());
            logger.debug("新数据的跟进,setBizId===>"+thisFollowup.getId());
        }
        logger.debug("创建跟进:userId = "+userId+"||||||||| date"+now + "||||followup_id"+thisFollowup.getId());
        logger.info("创建跟进,线索lead_id[{}]====跟进前线索的状态lead_status[{}]====线索状态更改为[{}]",leadId,result.getLeadStatus(),thisFollowup.getLeadStatus());

        followupHistory.setLeadId(leadId);
        followupHistory.setBizType(1);
        followupHistory.setOperateTime(now);
        followupHistory.setOperateBy(userId.toString());
        ResponseData<List<MetaDataDTO>> followup_type = metaService.getLeadMetaList("followup_type", followupType);
        if(followup_type != null && followup_type.getData()!=null &&followup_type.getData().size()>0){
            String followupTypeDesc = followup_type.getData().get(0).getDescription();
            followupHistory.setOperateResultDesc("完成"+followupTypeDesc+"的跟进");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String followupDateStr = sdf.format(followupDate);
        followupHistory.setExtraInfo(followupDateStr);
        followupHistory.setOperateResultStatus(result.getLeadStatus());
        followupHistorySearchMapper.insertHistory(followupHistory);
        logger.info("leadNum={"+result.getLeadNumber()+"}插入创建跟进操作记录历史数据");
        return ResponseData.success(1);
    }

    @Override
    @RequestMapping(value = "/consultant/queryFollowPlan", method = RequestMethod.GET)
    public ResponseData<Date> queryFollowPlan(Integer leadId) {
        List<LeadFollowup> lfList = leadFollowupSearchMapper.selectByLeadIdAndStatus(leadId, LEAD_FOLLOW_STATUS_ENUME.STATUS_NOT_FOLLOW.getValue());
        if(lfList != null && lfList.size()>0){
            LeadFollowup fup = lfList.get(0);
            Date followDate = fup.getFollowDate();
            if(followDate == null){
                return ResponseData.success(DateUtils.dayOffset(new Date(),1));
            }else{
                return ResponseData.success(followDate);
            }
        }
        return ResponseData.fail("该线索没有查询到[未跟进]!");

    }

    @Override
    @RequestMapping(value = "/lead/queryByPhoneAndCustName", method = RequestMethod.GET)
    public ResponseData<List<LeadResultDTO>> queryByPhoneAndCustName(String key,Integer queryType) {
        Integer userId = SessionHelper.getCurrentUserId();
        UserDetailDTO user = leadCommonService.getUserInfo(userId);
        String dealer = user.getDealerId();
        Integer owner = null;
        if(queryType == 2){
            //销售顾问模糊查询
            owner = userId;
        }
        List<LeadResultDTO> data = leadSearchMapper.queryByPhoneAndCustName(key,dealer,owner);
        if(data == null || data.size() == 0){
            return ResponseData.success(data);
        }else{
            for (int i  = 0;i < data.size();i++) {
                LeadResultDTO rs = data.get(i);
                rs.setTheKeyInformation("跟进 "+rs.getFollowupTimes()+"次  到店 "+rs.getArrivalTimes()+"次  试驾 "+rs.getPredictTimes()+"次");
                Integer leadId = rs.getId();
                List<LeadChannelDTO> leadChannelList = leadChannelSearchMapper.getLeadChannelList(leadId);
                if(leadChannelList != null){
                    rs.setChannelList(leadChannelList);
                }
                String seriesName = rs.getSeriesName();
                if(!com.chery.exeed.crm.common.util.StringUtils.isEmpty(seriesName)){
                    CarSeriesDTO carSeries = carService.getCarSeriesByCode(seriesName);
                    if(carSeries!=null){
                        rs.setSeriesName(carSeries.getSeriesName());
                    }
                }
                String modelName = rs.getModelName();
                if(!com.chery.exeed.crm.common.util.StringUtils.isEmpty(modelName) && !com.chery.exeed.crm.common.util.StringUtils.isEmpty(seriesName)){
                    CarModelDTO carmodel = carService.getCarModelByCode(seriesName,modelName);
                    if(carmodel!=null){
                        rs.setModelName(carmodel.getModelName());
                    }
                }
            }
        }
        return ResponseData.success(data);
    }


    @Override
    @RequestMapping(value = "/defeatlead/list", method = RequestMethod.GET)
    public ResponseData<Map<String, Object>> defeatLeadApplyList(Integer pageSize, Integer pageNo, Integer queryType) {
        Integer userId = SessionHelper.getCurrentUserId();
        UserDetailDTO userInfo = leadCommonService.getUserInfo(userId);
        String dealerId = userInfo.getDealerId();
        LeadDTO lead = new LeadDTO();
//        if(pageNo == null || pageSize == null){
//            pageNo = 1;
//            pageSize = 20;
//        }
        lead.setPageNo(pageNo);
        lead.setPageSize(pageSize);
        lead.setDealer(dealerId);
//        PageHelper.startPage(lead.getPageNo(), lead.getPageSize());
        List<LeadResultDTO> data = null;
        if(queryType != 1 && queryType != 2){
            return ResponseData.fail("queryType 不合法 !");
        }
        Map<String,Object> map=new HashMap<>();
        if(queryType.intValue() == 1){
            //申请战败线索列表
            lead.setLeadStatus(LEAD_STATUS_ENUME.DEFEAT_APPLY.getValue());
            data = leadSearchMapper.queryDefeatLeadList(lead);
            Integer total = leadSearchMapper.queryDefeatLeadCounts(lead);
            if(data != null && data.size() > 0){
                for (LeadResultDTO dto:data) {
                    dto.setOwnerDesc(leadCommonService.getUserName(dto.getOwner()));
                    DefeatOperateRecordDTO defeatOperateRecord = defeatOperateRecordSearchMapper.queryByLeadId(dto.getId());
                    if(defeatOperateRecord.getLoseType()!=null){
                        dto.setLoseType(defeatOperateRecord.getLoseType().toString());
                        dto.setLoseTypeName(defeatOperateRecordSearchMapper.queryLoseTypeName(defeatOperateRecord.getLoseType()));
                    }
                    if(StringUtils.isNotBlank(defeatOperateRecord.getFeedback())){
                        dto.setFeedback(defeatOperateRecord.getFeedback());
                    }
                    if(defeatOperateRecord.getOperateTime() != null){
                        dto.setDefeatApplyTime(defeatOperateRecord.getOperateTime());
                    }
                }
            }
            map.put("data",data);
            map.put("totalCount",total);
        }
        if(queryType.intValue() == 2){
            //线索战败审批结果列表
            data = leadSearchMapper.queryDefeatLeadApprovalList(lead);
            Integer total = leadSearchMapper.queryDefeatLeadApprovalCount(lead);
            if(data != null && data.size() > 0){
                for (LeadResultDTO dto:data) {
                    DefeatOperateRecordDTO defeatOperateRecord = defeatOperateRecordSearchMapper.queryByLeadId(dto.getId());
                    if(defeatOperateRecord.getLoseType()!=null){
                        dto.setLoseType(defeatOperateRecord.getLoseType().toString());
                    }
                    if(StringUtils.isNotBlank(defeatOperateRecord.getFeedback())){
                        dto.setFeedback(defeatOperateRecord.getFeedback());
                    }
                    if(defeatOperateRecord.getOperateTime() != null){
                        dto.setDefeatApplyTime(defeatOperateRecord.getOperateTime());
                    }
                    dto.setOwnerDesc(leadCommonService.getUserName(dto.getOwner()));
                    if(defeatOperateRecord.getApprovalResult() != null) {
                        if (1 == defeatOperateRecord.getApprovalResult()) {
                            dto.setDefeatApprovalDesc("同意");
                        } else if (2 == defeatOperateRecord.getApprovalResult()) {
                            dto.setDefeatApprovalDesc("拒绝");
                        }
                    }
                }
            }
            map.put("data",data);
            map.put("totalCount",total);
        }
        return ResponseData.success(map);
    }




    @Override
    @RequestMapping(value = "/defeat/approval", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class,transactionManager = "leadTransactionManager",propagation = Propagation.REQUIRED)
    public ResponseData<Boolean> defeatLeadApproval(Integer leadId, String dealerId, Integer approvalStatus, String operator) {
        // 查询线索是否为 战败申请 状态
        Lead lead = leadMapper.selectByPrimaryKey(leadId);
        if (lead == null) {
            logger.info("### 战败审批失败 ===> leadId[" + leadId + "].Data return[NULL]");
            return ResponseData.fail(LEAD_DATA_NOT_EXIST, "战败审批失败，线索不存在");
        }
        if (!lead.getDealer().equals(String.valueOf(dealerId))) {
            logger.info("### 战败审批失败 ===> Dealer User dealerId[" + dealerId + "].Lead dealerId[" + lead.getDealer() + "]");
            return ResponseData.fail(ILLEGAL_USER_OPERATE_CODE,"战败审批失败，用户不合法");
        }
        if (!lead.getLeadStatus().equals(LEAD_STATUS_ENUME.DEFEAT_APPLY.getValue())) {
            logger.info("### 战败审批失败 ===> Lead leadStatus[" + lead.getLeadStatus() + "]");
            return ResponseData.fail(ILLEGAL_LEAD_STATUS_OPERATE_CODE, "战败审批失败，该线索无法战败审批");
        }
        // 战败申请处理：1.update lead, 2.insert defeat_approval_operate_record, 3.insert followup_history
        Date now =new Date();
        Integer currentUserId = SessionHelper.getCurrentUserId();
        if (approvalStatus.equals(DEFEAT_APPROVAL_STATUS_ENUME.APPROVE_STATUS.getValue())) {
            ResponseData<List<MetaDataDTO>> leadMetaListResp = metaService.
                    getLeadMetaList(LeadConstants.LEAD_STATUS_META_NAME, LEAD_STATUS_ENUME.LOSE.getValue());
            // 同意战败
            // 1. update lead
            logger.info("同意战败 ### 更新线索 ===> leadId[" + leadId + "]");
            String dealer = lead.getDealer();
            String dealerOrderManager = lead.getDealerOrderManager();
            Integer owner = lead.getOwner();
            lead = new Lead();
            lead.setId(leadId);
            lead.setLeadStatus(LEAD_STATUS_ENUME.LOSE.getValue());
            lead.setModifyDate(now);
            lead.setModifyBy(SessionHelper.getCurrentUserId());
            lead.setLevel("5");//5为meta_lead中lead_level（线索等级）的F
            leadMapper.updateByPrimaryKeySelective(lead);
            leadCommonService.insertLeadHistory(leadId,now,currentUserId,LEAD_STATUS_ENUME.LOSE.getValue(),LEAD_STATUS_ENUME.LOSE.getDesc()
                    ,dealer,Integer.parseInt(dealerOrderManager),owner);
            // 2. insert defeat_approval_operate_record
            logger.info("同意战败 ### 新增战败申请审批操作记录 ===> leadId[" + leadId + "]");
            DefeatApprovalOperateRecord record = new DefeatApprovalOperateRecord();
            record.setLeadId(leadId);
            record.setLeadStatus(LEAD_STATUS_ENUME.LOSE.getValue());
            record.setApprovalResult(approvalStatus);
            record.setOperateBy(SessionHelper.getCurrentUserId());
            record.setOperateTime(now);
            handleDefeatApprovalOperateRecordMapper.insertSelectiveReturn(record);
            // 3. insert followup_history
            logger.info("同意战败 ### 新增跟进历史记录 ===> leadId[" + leadId + "]");
            FollowupHistory history = new FollowupHistory();
            history.setBizType(FOLLOWUP_BIZ_TYPE_ENUME.DEFEAT_APPLY_APPROVAL.getValue());
            history.setBizId(record.getId());
            history.setLeadId(leadId);
            history.setOperateTime(now);
            history.setOperateDesc(LeadConstants.DEFEAT_APPROVAL_APPROVE_MESSAGE);
            history.setOperateBy(SessionHelper.getCurrentUserId().toString());
            history.setOperateResultStatus(LEAD_STATUS_ENUME.LOSE.getValue());
            history.setOperateResultDesc(LeadConstants.DEFEAT_APPROVAL_APPROVE_MESSAGE);
            followupHistorySearchMapper.insertSelective(history);

            // 4. delete lead followup
//            // 删除未完成的 线索跟进
            leadFollowupSearchMapper.deleteByLeadIdInActive(leadId);
            //leadFollowupSearchMapper.updateLeadIdInActive(leadId);
        } else {
            // 拒绝战败
            // 查询 申请战败 前一个状态
            FollowupHistory followupHistory = followupHistorySearchMapper.
                    selectLastRecordBeforeDefeatApply(leadId, this.getLeadStatusBeforeDefeatApplyPack());
            ResponseData<List<MetaDataDTO>> leadMetaListResp = metaService.
                    getLeadMetaList(LeadConstants.LEAD_STATUS_META_NAME, followupHistory.getOperateResultStatus());
            // 1. update lead
            logger.info("拒绝战败 ### 线索状态回退，更新线索 ===> leadId[" + leadId + "]");
            String dealer = lead.getDealer();
            String dealerOrderManager = lead.getDealerOrderManager();
            Integer owner = lead.getOwner();
            String purchaseTime = lead.getPurchaseTime();
            LEAD_STATUS_ENUME lead_status_enume = LEAD_STATUS_ENUME.valueOf(followupHistory.getOperateResultStatus());
            lead = new Lead();
            if(lead_status_enume.getValue() != 14 && lead_status_enume.getValue() != 13 && StringUtils.isNotBlank(purchaseTime)){
                if(Integer.parseInt(purchaseTime) < 4){
                    lead.setLevel("4");
                }
                if(Integer.parseInt(purchaseTime) == 4){
                    lead.setLevel("2");
                }
                if(Integer.parseInt(purchaseTime) > 4){
                    lead.setLevel("1");
                }
            }
            lead.setId(leadId);
            lead.setLeadStatus(lead_status_enume.getValue());
            lead.setModifyDate(now);
            lead.setModifyBy(currentUserId);
            leadMapper.updateByPrimaryKeySelective(lead);
            leadCommonService.insertLeadHistory(leadId,now,currentUserId,lead_status_enume.getValue(),lead_status_enume.getDesc()
                    ,dealer,Integer.parseInt(dealerOrderManager),owner);

            // 2. insert defeat_approval_operate_record
            logger.info("拒绝战败 ### 新增战败申请审批操作记录 ===> leadId[" + leadId + "]");
            DefeatApprovalOperateRecord record = new DefeatApprovalOperateRecord();
            record.setLeadId(leadId);
            record.setLeadStatus(followupHistory.getOperateResultStatus());
            record.setApprovalResult(approvalStatus);
            record.setOperateBy(SessionHelper.getCurrentUserId());
            record.setOperateTime(now);
            handleDefeatApprovalOperateRecordMapper.insertSelectiveReturn(record);
            // 3. insert followup_history
            logger.info("拒绝战败 ### 新增跟进历史记录 ===> leadId[" + leadId + "]");
            FollowupHistory history = new FollowupHistory();
            history.setBizType(FOLLOWUP_BIZ_TYPE_ENUME.DEFEAT_APPLY_APPROVAL.getValue());
            history.setBizId(record.getId());
            history.setLeadId(leadId);
            history.setOperateTime(now);
            history.setOperateDesc(LeadConstants.DEFEAT_APPROVAL_REJECT_MESSAGE);
            history.setOperateBy(SessionHelper.getCurrentUserId().toString());
            history.setOperateResultStatus(followupHistory.getOperateResultStatus());
            history.setOperateResultDesc(LeadConstants.DEFEAT_APPROVAL_REJECT_MESSAGE);
            followupHistorySearchMapper.insertSelective(history);
            //4.回滚lead_followup status = 0
            List<LeadFollowup> lfList = leadFollowupSearchMapper.selectByLeadIdAndStatus(leadId, 0);
            if(lfList == null || lfList.size() == 0){
                leadFollowupSearchMapper.rollbackLastFollowup(leadId);
            }
        }
        return ResponseData.success(true);
    }

    @Override
    @RequestMapping(value = "/customer/listByKeyword", method = RequestMethod.GET)
    public ResponseData<List<CustomerDTO>> queryCustomerList(String keyword,Integer userId,String dealerId) {
        List<CustomerDTO> result = customerSearchMapper.queryCustomerByConsultant(keyword,userId,dealerId);
        return ResponseData.success(result);
    }

    @Override
    @RequestMapping(value = "/index/queryLevelDistribution", method = RequestMethod.GET)
    public ResponseData<Map<String, LevelDistributionDTO>> queryLevelDistribution() {
        Integer userId = SessionHelper.getCurrentUserId();
        UserDetailDTO user = leadCommonService.getUserInfo(userId);
        if(userId == null){
            return ResponseData.fail(TOKEN_FAIL_CODE,"Token无效");
        }
        if(user == null){
            return ResponseData.fail(SYS_USER_NOT_EXIST_CODE,"该用户不存在");
        }
        String dealerId = user.getDealerId();
        if(StringUtils.isBlank(dealerId)){
            return ResponseData.fail(SYS_USER_NOT_EXIST_CODE,"用户未绑定经销商,无法查询");
        }
        HashMap<String, LevelDistributionDTO> map = new HashMap<>();
        //订单经理
        if(user.getIsOrderManager() == 1){
            List<LevelDistributionDTO> thisMonth = leadSearchMapper.queryLevelDistributionOnThisMonth(dealerId,null);
            List<LevelDistributionDTO> lastMonth = leadSearchMapper.queryLevelDistributionOnLastMonth(dealerId,null);
            for (LevelDistributionDTO ld:thisMonth ) {
                if(StringUtils.isNotBlank(ld.getLevel())){
                    ld.setTotal(ld.getThisMonth());
                    ld.setLastMonth(0);
                    map.put(ld.getLevel(),ld);
                }
            }
            for (LevelDistributionDTO ld:lastMonth ) {
                if(StringUtils.isNotBlank(ld.getLevel()) && !map.containsKey(ld.getLevel())){
                    ld.setTotal(ld.getLastMonth());
                    ld.setThisMonth(0);
                    map.put(ld.getLevel(),ld);
                }else{
                    LevelDistributionDTO levelDistributionDTO = map.get(ld.getLevel());
                    ld.setThisMonth(levelDistributionDTO.getThisMonth());
                    ld.setTotal(ld.getLastMonth()+levelDistributionDTO.getThisMonth());
                }
            }
        }

        //线上专员
        if(user.getIsExpSpecialists() == 1){
            List<LevelDistributionDTO> thisMonth = leadSearchMapper.queryLevelDistributionOnThisMonth(dealerId,null);
            List<LevelDistributionDTO> lastMonth = leadSearchMapper.queryLevelDistributionOnLastMonth(dealerId,null);
            for (LevelDistributionDTO ld:thisMonth ) {
                if(StringUtils.isNotBlank(ld.getLevel())){
                    ld.setTotal(ld.getThisMonth());
                    ld.setLastMonth(0);
                    map.put(ld.getLevel(),ld);
                }
            }
            for (LevelDistributionDTO ld:lastMonth ) {
                if(StringUtils.isNotBlank(ld.getLevel()) && !map.containsKey(ld.getLevel())){
                    ld.setTotal(ld.getLastMonth());
                    ld.setThisMonth(0);
                    map.put(ld.getLevel(),ld);
                }else{
                    LevelDistributionDTO levelDistributionDTO = map.get(ld.getLevel());
                    ld.setThisMonth(levelDistributionDTO.getThisMonth());
                    ld.setTotal(ld.getLastMonth()+levelDistributionDTO.getThisMonth());
                }
            }
        }


        //销售顾问
        if(user.getIsConsultant() == 1){
            List<LevelDistributionDTO> thisMonth = leadSearchMapper.queryLevelDistributionOnThisMonth(dealerId,userId);
            List<LevelDistributionDTO> lastMonth = leadSearchMapper.queryLevelDistributionOnLastMonth(dealerId,userId);
            for (LevelDistributionDTO ld:thisMonth ) {
                if(StringUtils.isNotBlank(ld.getLevel())){
                    ld.setTotal(ld.getThisMonth());
                    ld.setLastMonth(0);
                    map.put(ld.getLevel(),ld);
                }
            }
            for (LevelDistributionDTO ld:lastMonth ) {
                if(StringUtils.isNotBlank(ld.getLevel()) && !map.containsKey(ld.getLevel())){
                    ld.setTotal(ld.getLastMonth());
                    ld.setThisMonth(0);
                    map.put(ld.getLevel(),ld);
                }else{
                    LevelDistributionDTO levelDistributionDTO = map.get(ld.getLevel());
                    ld.setThisMonth(levelDistributionDTO.getThisMonth());
                    ld.setTotal(ld.getLastMonth()+levelDistributionDTO.getThisMonth());
                }
            }
        }
        return ResponseData.success(map);
    }


    @Override
    @RequestMapping(value = "/index/queryLevelDistributionNew", method = RequestMethod.GET)
    public ResponseData<Map<String, LevelDistributionDTO>> queryLevelDistributionNew(Integer resource) {
        Integer userId = SessionHelper.getCurrentUserId();
        logger.info("===============意向级别分布 userId = " + userId);
        UserDetailDTO user = leadCommonService.getUserInfo(userId);
        if(userId == null){
            return ResponseData.fail(TOKEN_FAIL_CODE,"Token无效");
        }
        if(user == null){
            return ResponseData.fail(SYS_USER_NOT_EXIST_CODE,"该用户不存在");
        }
        String dealerId = user.getDealerId();
        if(StringUtils.isBlank(dealerId)){
            return ResponseData.fail(SYS_USER_NOT_EXIST_CODE,"用户未绑定经销商,无法查询");
        }

        Map<String, LevelDistributionDTO> map = new HashMap<>();
        //订单经理
        if(user.getIsOrderManager() == 1 && resource == 1){
            List<LevelDistributionDTO> thisMonth = leadSearchMapper.queryLevelDistributionOnThisMonth(dealerId,null);
            List<LevelDistributionDTO> lastMonth = leadSearchMapper.queryLevelDistributionOnLastMonth(dealerId,null);
            for (LevelDistributionDTO ld:thisMonth ) {
                if(StringUtils.isNotBlank(ld.getLevel())){
                    ld.setTotal(ld.getThisMonth());
                    ld.setLastMonth(0);
                    map.put(ld.getLevel(),ld);
                }
            }
            for (LevelDistributionDTO ld:lastMonth ) {
                if(StringUtils.isNotBlank(ld.getLevel()) && !map.containsKey(ld.getLevel())){
                    ld.setTotal(ld.getLastMonth());
                    ld.setThisMonth(0);
                    map.put(ld.getLevel(),ld);
                }
                else{
                    if(StringUtils.isNotBlank(ld.getLevel())){
                        LevelDistributionDTO levelDistributionDTO = map.get(ld.getLevel());
                        if(levelDistributionDTO != null){
                            ld.setThisMonth(levelDistributionDTO.getThisMonth());
                            ld.setTotal(ld.getLastMonth()+levelDistributionDTO.getThisMonth());
                        }else{
                            ld.setThisMonth(0);
                            ld.setLastMonth(ld.getLastMonth());
                        }
                        map.put(ld.getLevel(),ld);
                    }
                }

            }
        }
        logger.debug("map:"+map.toString()+"map.size"+map.entrySet().size());



        //线上专员
        if(user.getIsExpSpecialists() == 1 && resource == 3){
            List<LevelDistributionDTO> thisMonth = leadSearchMapper.queryLevelDistributionOnThisMonth(dealerId,null);
            List<LevelDistributionDTO> lastMonth = leadSearchMapper.queryLevelDistributionOnLastMonth(dealerId,null);
            for (LevelDistributionDTO ld:thisMonth ) {
                if(StringUtils.isNotBlank(ld.getLevel())){
                    ld.setTotal(ld.getThisMonth());
                    ld.setLastMonth(0);
                    map.put(ld.getLevel(),ld);
                }
            }
            for (LevelDistributionDTO ld:lastMonth ) {
                if(StringUtils.isNotBlank(ld.getLevel()) && !map.containsKey(ld.getLevel())){
                    ld.setTotal(ld.getLastMonth());
                    ld.setThisMonth(0);
                    map.put(ld.getLevel(),ld);
                }
                else{
                    if(StringUtils.isNotBlank(ld.getLevel())){
                        LevelDistributionDTO levelDistributionDTO = map.get(ld.getLevel());
                        if(levelDistributionDTO != null){
                            ld.setThisMonth(levelDistributionDTO.getThisMonth());
                            ld.setTotal(ld.getLastMonth()+levelDistributionDTO.getThisMonth());
                        }else{
                            ld.setThisMonth(0);
                            ld.setLastMonth(ld.getLastMonth());
                        }
                        map.put(ld.getLevel(),ld);
                    }
                }

            }
        }
        logger.debug("map:"+map.toString()+"map.size"+map.entrySet().size());






        //销售顾问
        if(user.getIsConsultant() == 1 && resource == 2){
            List<LevelDistributionDTO> thisMonth = leadSearchMapper.queryLevelDistributionOnThisMonth(dealerId,userId);
            List<LevelDistributionDTO> lastMonth = leadSearchMapper.queryLevelDistributionOnLastMonth(dealerId,userId);
            for (LevelDistributionDTO ld:thisMonth ) {
                if(StringUtils.isNotBlank(ld.getLevel())){
                    ld.setTotal(ld.getThisMonth());
                    ld.setLastMonth(0);
                    map.put(ld.getLevel(),ld);
                }
            }
            for (LevelDistributionDTO ld:lastMonth ) {
                if(StringUtils.isNotBlank(ld.getLevel()) && !map.containsKey(ld.getLevel())){
                    ld.setTotal(ld.getLastMonth());
                    ld.setThisMonth(0);
                    map.put(ld.getLevel(),ld);
                }
                else{
                    if(StringUtils.isNotBlank(ld.getLevel())){
                        LevelDistributionDTO levelDistributionDTO = map.get(ld.getLevel());
                        if(levelDistributionDTO != null){
                            ld.setThisMonth(levelDistributionDTO.getThisMonth());
                            ld.setTotal(ld.getLastMonth()+levelDistributionDTO.getThisMonth());
                        }else{
                            ld.setThisMonth(0);
                            ld.setLastMonth(ld.getLastMonth());
                        }
                        map.put(ld.getLevel(),ld);
                    }
                }

            }
        }
        logger.debug("map:"+map.toString()+"map.size"+map.entrySet().size());
        return ResponseData.success(map);
    }

    @Override
    @RequestMapping(value = "/index/queryConsultantPerformance", method = RequestMethod.GET)
    public ResponseData<List<ConsultantPerformanceDTO>> queryConsultantPerformance(Integer selectedTime,@RequestBody Map<String,String> map) {
        //selectedTime 1:当月 2:本周 3:今日
        List<ConsultantPerformanceDTO> result = new ArrayList<>();
        Set<String> ids = map.keySet();
        Iterator<String> it = ids.iterator();
        while(it.hasNext()){
            ConsultantPerformanceDTO cp = new ConsultantPerformanceDTO();
            String id = it.next();
            String username = map.get(id);
            cp.setUserId(Integer.parseInt(id));
            cp.setUsername(username);
            //未跟进
            Integer num1 = leadFollowupSearchMapper.queryConsultantPerformanceNotFollow(selectedTime,Integer.parseInt(id),LEAD_FOLLOW_STATUS_ENUME.STATUS_NOT_FOLLOW.getValue());
            //已跟进
            List<Integer> num2 = leadFollowupSearchMapper.queryConsultantPerformanceFollow(selectedTime, Integer.parseInt(id), LEAD_FOLLOW_STATUS_ENUME.STATUS_FOLLOWED.getValue());
            //逾期
            Integer num3 = leadFollowupSearchMapper.queryExpiredLeadOfConsultant(selectedTime,Integer.parseInt(id));
            cp.setNotFollowed(num1);
            cp.setFollowed(num2.size());
            cp.setTimeout(num3);
            result.add(cp);
        }
        return ResponseData.success(result);
    }

    @Override
    @RequestMapping(value = "/followup/queryDetail", method = RequestMethod.POST)
    public ResponseData<List<Object>> queryFollowupDetail(@RequestBody FollowupHistoryDTO followupHistoryDTO) {
        List<Object> list = new ArrayList<>();
        Integer bizType = followupHistoryDTO.getBizType();
        Integer bizId = followupHistoryDTO.getBizId();
        /**1：跟进；2：试乘试驾；3：报价；4：下订；5：申请战败；8：到店；*/
        if(bizType == 1){
            LeadFollowup dto = leadFollowupSearchMapper.selectByPrimaryKey(bizId);
            if(dto != null){
                list.add(dto);
            }

        }
        if(bizType == 2){
            PredictOperateRecord por = predictOperateRecordSearchMapper.selectByPrimaryKey(bizId);
            PredictOperateRecordDTO dto = new PredictOperateRecordDTO();
            BeanUtils.copyProperties(por,dto);
            /**查询试驾的隐私条款签字*/
            APIResponse<DriveBinding> signOfDrive = appletService.getSignOfDrive(bizId);
            if(signOfDrive.isSuccess() && signOfDrive.getData()!= null && StringUtils.isNotBlank(signOfDrive.getData().getFilePath())){
                /**已签字试驾*/
                logger.info("bizId==>"+bizId+"=====>签字url"+signOfDrive.getData().getFilePath());
                dto.setIsSign("1");
            }else{
                /**未签字*/
                dto.setIsSign("0");
            }
            if(StringUtils.isNotBlank(dto.getSeriesName())){
                CarSeriesDTO carSeriesByCode = carService.getCarSeriesByCode(dto.getSeriesName());
                dto.setSeriesNameDesc(carSeriesByCode.getSeriesName());
            }
            if(StringUtils.isNotBlank(dto.getSeriesName()) && StringUtils.isNotBlank(dto.getModelName())){
                CarModelDTO carModelByCode = carService.getCarModelByCode(dto.getSeriesName(), dto.getModelName());
                dto.setModelNameDesc(carModelByCode.getModelName());
            }
            list.add(dto);
        }
        if(bizType == 3){
            OfferOperateRecord oor = offerOperateRecordSearchMapper.selectByPrimaryKey(bizId);
            OfferOperateRecordDTO dto = new OfferOperateRecordDTO();
            BeanUtils.copyProperties(oor,dto);
            if(StringUtils.isNotBlank(dto.getSeriesName())){
                CarSeriesDTO carSeriesByCode = carService.getCarSeriesByCode(dto.getSeriesName());
                dto.setSeriesNameDesc(carSeriesByCode.getSeriesName());
            }
            if(StringUtils.isNotBlank(dto.getSeriesName()) && StringUtils.isNotBlank(dto.getModelName())){
                CarModelDTO carModelByCode = carService.getCarModelByCode(dto.getSeriesName(), dto.getModelName());
                dto.setModelNameDesc(carModelByCode.getModelName());
            }
            list.add(dto);
        }
        if(bizType == 4){
            OrderOperateRecord dto = orderOperateRecordSearchMapper.selectByPrimaryKey(bizId);
            if(dto != null){
                list.add(dto);
            }
        }
        if(bizType == 5){
            DefeatOperateRecord dto = defeatOperateRecordSearchMapper.selectByPrimaryKey(bizId);
            if(dto != null){
                //不为空就是新值，新值不用判断直接赋值，因为新值在保存做判断---1级为空2级不为空不能保存，这里查询会给2级值，因为它属于1、2级
                if(null==dto.getLoseType2()){
                    if(null!=dto.getLoseType()){//1级不为空判断它是1级还是2级放值
                        Integer count = defeatOperateRecordSearchMapper.queryLoseTypeCount(dto.getLoseType());
                        if(count==2){
                            dto.setLoseType2(dto.getLoseType());
                            dto.setLoseType(null);
                        }
                    }
                }
                if(null!=dto.getLoseType()){
                    dto.setLoseTypeName(defeatOperateRecordSearchMapper.queryLoseTypeName(dto.getLoseType()));
                }
                if(null!=dto.getLoseType2()){
                    dto.setLoseType2Name(defeatOperateRecordSearchMapper.queryLoseTypeName(dto.getLoseType2()));
                }
                list.add(dto);
            }
        }

        if(bizType == 8){
            ArrivalOperateRecord dto = arrivalOperateRecordSearchMapper.selectByPrimaryKey(bizId);
            if(dto != null){
                list.add(dto);
            }
        }
        return ResponseData.success(list);
    }

    //根据父级编号查询一级所对应的二级类别

    @Override
    @RequestMapping(value = "/lead/getLoseTypeList", method = RequestMethod.GET)
    public ResponseData<List<Map<String,Object>>> getLoseTypeList(Integer parentCode) {
        List<Map<String, Object>> loseTypeList = defeatOperateRecordSearchMapper.getLoseTypeList(parentCode);
        logger.info("CZJ 战败类别总数量："+loseTypeList.size()+",参数是:"+parentCode);
        return ResponseData.success(loseTypeList);
    }

    @Override
    @RequestMapping(value = "/lead/consultant/queryLeadListByCondition", method = RequestMethod.GET)
    public ResponseData<PaginationResult<LeadResultDTO>> queryLeadListByCondition(Date begin, Date end, String level,String seriesName,String modelName, Integer taskStatus,Integer pageNo,Integer pageSize) {
        pageSize=pageSize==null || pageSize==0?5:pageSize;
        pageNo=pageNo==null || pageNo==0?0:(pageNo-1)*pageSize;
        //1.新线索 2.待到店/待试驾 3.已下定 4.待交车 5.待跟进 6.已成交)
        if(taskStatus == null){
            return ResponseData.fail(PARAM_IN_FAIL_CODE,"入参错误");
        }
        Integer owner = SessionHelper.getCurrentUserId();
        UserDetailDTO userInfo = leadCommonService.getUserInfo(owner);
        String dealer = userInfo.getDealerId();
        List<Integer> status = new ArrayList<>();
        List<LeadResultDTO> resultList = null;
        Integer total =0;
        if(taskStatus == 1){
            //新线索
            resultList = leadSearchMapper.queryNewTaskLeadList(dealer,owner,begin,end,level,seriesName,modelName,pageNo,pageSize);
            resultList = this.getFollowPlan(resultList);
            total=leadSearchMapper.queryNewTaskLeadListTotal(dealer,owner,begin,end,level,seriesName,modelName);
            for (LeadResultDTO rs:resultList) {
                rs.setTheKeyInformation("跟进 "+rs.getFollowupTimes()+"次  到店 "+rs.getArrivalTimes()+"次  试驾 "+rs.getPredictTimes()+"次");
                Integer leadId = rs.getId();
                //这里返回的是一个集合  无法在sql中进行处理
                List<LeadChannelDTO> leadChannelList = leadChannelSearchMapper.getLeadChannelList(leadId);
                if(leadChannelList != null){
                    rs.setChannelList(leadChannelList);
                }
//                String sn = rs.getSeriesName();//改为在resultList中关联视图查询
//                if(!com.chery.exeed.crm.common.util.StringUtils.isEmpty(sn)){
//                    CarSeriesDTO carSeries = carService.getCarSeriesByCode(sn);
//                    if(carSeries!=null){
//                        rs.setSeriesName(carSeries.getSeriesName());
//                    }
//                }
//                String mn = rs.getModelName();
//                if(!com.chery.exeed.crm.common.util.StringUtils.isEmpty(sn) && !com.chery.exeed.crm.common.util.StringUtils.isEmpty(mn)){
//                    CarModelDTO carmodel = carService.getCarModelByCode(sn,mn);
//                    if(carmodel!=null){
//                        rs.setModelName(carmodel.getModelName());
//                    }
//                }
            }

        }
        if(taskStatus == 2){
            //待到店/待试驾
            status.clear();
            status.add(LEAD_STATUS_ENUME.NOT_ARRIVAL.getValue());
            status.add(LEAD_STATUS_ENUME.NOT_PREDICT.getValue());
            resultList = leadSearchMapper.queryTaskLeadListByCondition(dealer,owner,status,begin,end,level,modelName,taskStatus,pageNo,pageSize);
            resultList = this.getFollowPlan(resultList);
            total=leadSearchMapper.queryTaskLeadListByConditionTotal(dealer,owner,status,begin,end,level,modelName,taskStatus);
            for (LeadResultDTO rs:resultList) {
                rs.setTheKeyInformation("跟进 "+rs.getFollowupTimes()+"次  到店 "+rs.getArrivalTimes()+"次  试驾 "+rs.getPredictTimes()+"次");
                Integer leadId = rs.getId();
                List<LeadChannelDTO> leadChannelList = leadChannelSearchMapper.getLeadChannelList(leadId);
                if(leadChannelList != null){
                    rs.setChannelList(leadChannelList);
                }
//                String sn = rs.getSeriesName();
//                if(!com.chery.exeed.crm.common.util.StringUtils.isEmpty(sn)){
//                    CarSeriesDTO carSeries = carService.getCarSeriesByCode(sn);
//                    if(carSeries!=null){
//                        rs.setSeriesName(carSeries.getSeriesName());
//                    }
//                }
//                String mn = rs.getModelName();
//                if(!com.chery.exeed.crm.common.util.StringUtils.isEmpty(sn) && !com.chery.exeed.crm.common.util.StringUtils.isEmpty(mn)){
//                    CarModelDTO carmodel = carService.getCarModelByCode(sn,mn);
//                    if(carmodel!=null){
//                        rs.setModelName(carmodel.getModelName());
//                    }
//                }
            }
        }
        if(taskStatus == 3){
            //已下定
            status.clear();
            status.add(LEAD_STATUS_ENUME.ORDER.getValue());
            resultList=leadSearchMapper.queryTaskLeadListByCondition(dealer,owner,status,begin,end,level,modelName,taskStatus,pageNo,pageSize);
            resultList = this.getFollowPlan(resultList);
            total=leadSearchMapper.queryTaskLeadListByConditionTotal(dealer,owner,status,begin,end,level,modelName,taskStatus);
            for (LeadResultDTO rs:resultList) {
                rs.setTheKeyInformation("跟进 "+rs.getFollowupTimes()+"次  到店 "+rs.getArrivalTimes()+"次  试驾 "+rs.getPredictTimes()+"次");
                Integer leadId = rs.getId();
                List<LeadChannelDTO> leadChannelList = leadChannelSearchMapper.getLeadChannelList(leadId);
                if(leadChannelList != null){
                    rs.setChannelList(leadChannelList);
                }
//                String sn = rs.getSeriesName();
//                if(!com.chery.exeed.crm.common.util.StringUtils.isEmpty(sn)){
//                    CarSeriesDTO carSeries = carService.getCarSeriesByCode(sn);
//                    if(carSeries!=null){
//                        rs.setSeriesName(carSeries.getSeriesName());
//                    }
//                }
//                String mn = rs.getModelName();
//                if(!com.chery.exeed.crm.common.util.StringUtils.isEmpty(sn) && !com.chery.exeed.crm.common.util.StringUtils.isEmpty(mn)){
//                    CarModelDTO carmodel = carService.getCarModelByCode(sn,mn);
//                    if(carmodel!=null){
//                        rs.setModelName(carmodel.getModelName());
//                    }
//                }
            }
        }
        if(taskStatus == 4){
            //待交车
            status.clear();
            status.add(LEAD_STATUS_ENUME.ORDER.getValue());
            resultList=leadSearchMapper.queryTaskLeadListByCondition(dealer,owner,status,begin,end,level,modelName,taskStatus,pageNo,pageSize);
            resultList = this.getFollowPlan(resultList);
            total=leadSearchMapper.queryTaskLeadListByConditionTotal(dealer,owner,status,begin,end,level,modelName,taskStatus);
            for (LeadResultDTO rs:resultList) {
                Integer leadId = rs.getId();
                List<LeadChannelDTO> leadChannelList = leadChannelSearchMapper.getLeadChannelList(leadId);
                if(leadChannelList != null){
                    rs.setChannelList(leadChannelList);
                }
//                String sn = rs.getSeriesName();
//                if(!com.chery.exeed.crm.common.util.StringUtils.isEmpty(sn)){
//                    CarSeriesDTO carSeries = carService.getCarSeriesByCode(sn);
//                    if(carSeries!=null){
//                        rs.setSeriesName(carSeries.getSeriesName());
//                    }
//                }
//                String mn = rs.getModelName();
//                if(!com.chery.exeed.crm.common.util.StringUtils.isEmpty(sn) && !com.chery.exeed.crm.common.util.StringUtils.isEmpty(mn)){
//                    CarModelDTO carmodel = carService.getCarModelByCode(sn,mn);
//                    if(carmodel!=null){
//                        rs.setModelName(carmodel.getModelName());
//                    }
//                }
            }
        }
        if(taskStatus == 5){
            //待跟进
            resultList=leadSearchMapper.queryFollowLeadListByCondition(dealer,owner,begin,end,level,modelName,pageNo,pageSize);
            resultList = this.getFollowPlan(resultList);
            total=leadSearchMapper.queryFollowLeadListByConditionTotal(dealer,owner,begin,end,level,modelName);
            for (LeadResultDTO rs:resultList) {
                rs.setTheKeyInformation("跟进 "+rs.getFollowupTimes()+"次  到店 "+rs.getArrivalTimes()+"次  试驾 "+rs.getPredictTimes()+"次");
                Integer leadId = rs.getId();
                List<LeadChannelDTO> leadChannelList = leadChannelSearchMapper.getLeadChannelList(leadId);
                if(leadChannelList != null){
                    rs.setChannelList(leadChannelList);
                }
//                String sn = rs.getSeriesName();
//                if(!com.chery.exeed.crm.common.util.StringUtils.isEmpty(sn)){
//                    CarSeriesDTO carSeries = carService.getCarSeriesByCode(sn);
//                    if(carSeries!=null){
//                        rs.setSeriesName(carSeries.getSeriesName());
//                    }
//                }
//                String mn = rs.getModelName();
//                if(!com.chery.exeed.crm.common.util.StringUtils.isEmpty(sn) && !com.chery.exeed.crm.common.util.StringUtils.isEmpty(mn)){
//                    CarModelDTO carmodel = carService.getCarModelByCode(sn,mn);
//                    if(carmodel!=null){
//                        rs.setModelName(carmodel.getModelName());
//                    }
//                }
            }
        }
        if(taskStatus == 6){
            //已成交
            status.clear();
            status.add(LEAD_STATUS_ENUME.WIN.getValue());
            resultList=leadSearchMapper.queryTaskLeadListByCondition(dealer,owner,status,begin,end,level,modelName,taskStatus,pageNo,pageSize);
            resultList = this.getFollowPlan(resultList);
            total=leadSearchMapper.queryTaskLeadListByConditionTotal(dealer,owner,status,begin,end,level,modelName,taskStatus);
            for (LeadResultDTO rs:resultList) {
                rs.setTheKeyInformation("跟进 "+rs.getFollowupTimes()+"次  到店 "+rs.getArrivalTimes()+"次  试驾 "+rs.getPredictTimes()+"次");
                Integer leadId = rs.getId();
                List<LeadChannelDTO> leadChannelList = leadChannelSearchMapper.getLeadChannelList(leadId);
                if(leadChannelList != null){
                    rs.setChannelList(leadChannelList);
                }
//                String sn = rs.getSeriesName();
//                if(!com.chery.exeed.crm.common.util.StringUtils.isEmpty(sn)){
//                    CarSeriesDTO carSeries = carService.getCarSeriesByCode(sn);
//                    if(carSeries!=null){
//                        rs.setSeriesName(carSeries.getSeriesName());
//                    }
//                }
//                String mn = rs.getModelName();
//                if(!com.chery.exeed.crm.common.util.StringUtils.isEmpty(sn) && !com.chery.exeed.crm.common.util.StringUtils.isEmpty(mn)){
//                    CarModelDTO carmodel = carService.getCarModelByCode(sn,mn);
//                    if(carmodel!=null){
//                        rs.setModelName(carmodel.getModelName());
//                    }
//                }
            }
        }

        if(taskStatus == 7){
            //已超期
            resultList = leadSearchMapper.queryExpiredLeadList(dealer,owner,pageNo,pageSize);
            resultList = this.getFollowPlan(resultList);
            total = leadSearchMapper.queryExpiredLeadListTotal(dealer, owner);
            for (LeadResultDTO rs:resultList) {
                rs.setTheKeyInformation("跟进 "+rs.getFollowupTimes()+"次  到店 "+rs.getArrivalTimes()+"次  试驾 "+rs.getPredictTimes()+"次");
                Integer leadId = rs.getId();
                List<LeadChannelDTO> leadChannelList = leadChannelSearchMapper.getLeadChannelList(leadId);
                if(leadChannelList != null){
                    rs.setChannelList(leadChannelList);
                }
//                String sn = rs.getSeriesName();
//                if(!com.chery.exeed.crm.common.util.StringUtils.isEmpty(sn)){
//                    CarSeriesDTO carSeries = carService.getCarSeriesByCode(sn);
//                    if(carSeries!=null){
//                        rs.setSeriesName(carSeries.getSeriesName());
//                    }
//                }
//                String mn = rs.getModelName();
//                if(!com.chery.exeed.crm.common.util.StringUtils.isEmpty(sn) && !com.chery.exeed.crm.common.util.StringUtils.isEmpty(mn)){
//                    CarModelDTO carmodel = carService.getCarModelByCode(sn,mn);
//                    if(carmodel!=null){
//                        rs.setModelName(carmodel.getModelName());
//                    }
//                }
            }
        }

        if(taskStatus == 8){
            //已战败
            status.clear();
            status.add(LEAD_STATUS_ENUME.LOSE.getValue());
            resultList=leadSearchMapper.queryTaskLeadListByCondition(dealer,owner,status,begin,end,level,modelName,taskStatus,pageNo,pageSize);
            total=leadSearchMapper.queryTaskLeadListByConditionTotal(dealer, owner, status, begin, end, level, modelName, taskStatus);
            for (LeadResultDTO rs:resultList) {
                rs.setTheKeyInformation("跟进 "+rs.getFollowupTimes()+"次  到店 "+rs.getArrivalTimes()+"次  试驾 "+rs.getPredictTimes()+"次");
                Integer leadId = rs.getId();
                List<LeadChannelDTO> leadChannelList = leadChannelSearchMapper.getLeadChannelList(leadId);
                if(leadChannelList != null){
                    rs.setChannelList(leadChannelList);
                }
//                String sn = rs.getSeriesName();
//                if(!com.chery.exeed.crm.common.util.StringUtils.isEmpty(sn)){
//                    CarSeriesDTO carSeries = carService.getCarSeriesByCode(sn);
//                    if(carSeries!=null){
//                        rs.setSeriesName(carSeries.getSeriesName());
//                    }
//                }
//                String mn = rs.getModelName();
//                if(!com.chery.exeed.crm.common.util.StringUtils.isEmpty(sn) && !com.chery.exeed.crm.common.util.StringUtils.isEmpty(mn)){
//                    CarModelDTO carmodel = carService.getCarModelByCode(sn,mn);
//                    if(carmodel!=null){
//                        rs.setModelName(carmodel.getModelName());
//                    }
//                }
            }
        }
        PaginationResult<LeadResultDTO> result = new PaginationResult<>();
        result.setTotalCount((long)total);
        result.setData(resultList);
        return ResponseData.success(result);
    }

    /**销售助手-顾问销售情况-顾问*/
    @Override
    @RequestMapping(value = "/queryConsultantSalesPerformance", method = RequestMethod.GET)
    public ResponseData<ConsultantSalesPerformanceDTO> getConsultantSalesPerformance(Integer timeStamp) {
        Integer userId = SessionHelper.getCurrentUserId();
        ResponseData<UserDetailDTO> userInfo = sysUserService.monitorInfo(userId);
        String dealerId = userInfo.getData().getDealerId();
        if(timeStamp == null || StringUtils.isBlank(dealerId)||userId == null){
            return ResponseData.fail("参数异常");
        }
        ConsultantSalesPerformanceDTO dto = leadSearchMapper.getConsultantSalesPerformance(timeStamp,dealerId,userId);
        if(dto == null){
            ConsultantSalesPerformanceDTO emptyDto = new ConsultantSalesPerformanceDTO();
            emptyDto.setUserId(userId);
            emptyDto.setUserName(userInfo.getData().getNickName());
            emptyDto.setNewLead(0);
            emptyDto.setArrival(0);
            emptyDto.setFollowedLead(0);
            emptyDto.setLose(0);
            emptyDto.setWin(0);
            emptyDto.setOrder(0);
            emptyDto.setPredict(0);
            return ResponseData.success(emptyDto);

        }
        return ResponseData.success(dto);
    }

    /**销售助手-顾问销售情况-经理*/
    @Override
    @RequestMapping(value = "/queryDealerSalesPerformance", method = RequestMethod.GET)
    public ResponseData<List<ConsultantSalesPerformanceDTO>> getDealerSalesPerformance(Integer timeStamp) {
        Integer userId = SessionHelper.getCurrentUserId();
        ResponseData<UserDetailDTO> userInfo = sysUserService.monitorInfo(userId);
        String dealerId = userInfo.getData().getDealerId();
        if(timeStamp == null || StringUtils.isBlank(dealerId)||userId == null){
            return ResponseData.fail("参数异常");
        }
        List<ConsultantSalesPerformanceDTO> result = leadSearchMapper.getDealerSalesPerformance(timeStamp,dealerId);
        ResponseData<List<UserDetailDTO>> dealerUserByUserType = sysUserService.getDealerUserByUserType(2);
        List<UserDetailDTO> data = dealerUserByUserType.getData();
        if(result == null || result.size() == 0){
            List<ConsultantSalesPerformanceDTO> emptyList = new ArrayList<>();
            if(data == null || data.size() == 0){
                ConsultantSalesPerformanceDTO dto = new ConsultantSalesPerformanceDTO();
                dto.setUserId(-1);
                dto.setUserName("");
                dto.setNewLead(0);
                dto.setArrival(0);
                dto.setFollowedLead(0);
                dto.setLose(0);
                dto.setWin(0);
                dto.setOrder(0);
                dto.setPredict(0);
//                dto.setFollowupRate(consultantSalesPerformanceFun(-1,timeStamp));
                emptyList.add(dto);
            }else{
                for (UserDetailDTO user : data) {
                    ConsultantSalesPerformanceDTO dto = new ConsultantSalesPerformanceDTO();
                    dto.setUserId(user.getUserId());
                    dto.setUserName(user.getNickName());
                    dto.setNewLead(0);
                    dto.setArrival(0);
                    dto.setFollowedLead(0);
                    dto.setLose(0);
                    dto.setWin(0);
                    dto.setOrder(0);
                    dto.setPredict(0);
//                    dto.setFollowupRate(consultantSalesPerformanceFun(user.getUserId(),timeStamp));
                    emptyList.add(dto);
                }

            }
            return ResponseData.success(emptyList);
        }else{
            Map<Integer, ConsultantSalesPerformanceDTO> userMap = new HashMap<>();
            for (ConsultantSalesPerformanceDTO dto : result){
                userMap.put(dto.getUserId(),dto);
            }
            List<ConsultantSalesPerformanceDTO> responseList = new ArrayList<>();
            for (UserDetailDTO user : data){
                if(!userMap.containsKey(user.getUserId())){
                    ConsultantSalesPerformanceDTO dto = new ConsultantSalesPerformanceDTO();
                    dto.setUserId(user.getUserId());
                    dto.setUserName(user.getNickName());
                    dto.setNewLead(0);
                    dto.setArrival(0);
                    dto.setFollowedLead(0);
                    dto.setLose(0);
                    dto.setWin(0);
                    dto.setOrder(0);
                    dto.setPredict(0);
//                    dto.setFollowupRate(consultantSalesPerformanceFun(user.getUserId(),timeStamp));
                    responseList.add(dto);
                }else{
//                    userMap.get(user.getUserId()).setFollowupRate(consultantSalesPerformanceFun(user.getUserId(),timeStamp));
                    responseList.add(userMap.get(user.getUserId()));
                }
            }
            return ResponseData.success(responseList);
        }
    }

    public String consultantSalesPerformanceFun(Integer userId,Integer timeStamp){
        if(userId == -1){
            return "0.00%";
        }
        List<LeadResultDTO> leadResults = leadMapper.queryOneHourFollowUpRateTwo(userId,timeStamp);//根据用户编号查询有几条线索跟进时间
        Integer toFollowup=0;//用于累计是否是一小时跟进的线索
        for(LeadResultDTO leadResult:leadResults){
            //取下发时间加一小时  如果跟进时间大于下发+的时间为null  否则为是
            if(leadResult.getDealerSendTime()!=null && !leadResult.getDealerSendTime().equals("") && leadResult.getFirstFollowupTime()!=null && !leadResult.getFirstFollowupTime().equals("")){
                if(stringFomatDateNumber(leadResult.getDealerSendTime())>=0 && stringFomatDateNumber(leadResult.getDealerSendTime())<9){//是凌晨之后，要在今天10点之前跟进
                    //获取下发时间当天的10点  如果跟进时间大于10点为null 否者为是
                    String firstFollowupTime = formatDay10(leadResult.getDealerSendTime());//下发的最晚时间今天10点
                    if(stringFomatDate(leadResult.getFirstFollowupTime()).getTime() <=
                            stringFomatDate(firstFollowupTime).getTime()){//跟进时间小于最晚下发时间
                        toFollowup++;
                    }
                }else if(stringFomatDateNumber(leadResult.getDealerSendTime())>=9 && stringFomatDateNumber(leadResult.getDealerSendTime())<16){//1小时内跟进
                    //当天时间加1小时
                    String str = formatAddHour(leadResult.getDealerSendTime(),1);//下发的最晚时间加1小时
                    if(stringFomatDate(leadResult.getFirstFollowupTime()).getTime() <=
                            stringFomatDate(str).getTime()){//跟进时间小于最晚下发时间
                        toFollowup++;
                    }
                }
//                else if(stringFomatDateNumber(leadResult.getDealerSendTime())>=16 && stringFomatDateNumber(leadResult.getDealerSendTime())<17){//第二天10点前某个时间之前跟进
//                    //如果跟进时间小于当天的16点 或者 第二天9-10点间的当天时间的分秒
//                    String str1 = formatDay17(leadResult.getDealerSendTime());//下发的最晚时间16点
//                    String str2 = formatNextDay9(leadResult.getDealerSendTime());//下发的最晚时间明天9点
//                    String str=leadResult.getDealerSendTime().replace(leadResult.getDealerSendTime().substring(leadResult.getDealerSendTime().indexOf(" ") + 1,
//                            leadResult.getDealerSendTime().indexOf(" ") + 9), "16:00:00");//获取下发时间为15点整时间如 2021-4-4 15:33:22 --2021-4-4 15:00:00
//                    if(leadResult.getDealerSendTime().equals(str)){//如果下发时间是15点整，就只能在16点整之前跟进
//                        if(stringFomatDate(leadResult.getFirstFollowupTime()).getTime() <=
//                                stringFomatDate(str1).getTime()){//跟进时间小于最晚下发时间
//                            toFollowup++;
//                        }
//                    }else{//否者可以在 跟进时间小于16点 或 第二天9点的相同分秒之前跟进
//                        if(stringFomatDate(leadResult.getFirstFollowupTime()).getTime() <=
//                                stringFomatDate(str1).getTime() ||
//                                stringFomatDate(leadResult.getFirstFollowupTime()).getTime() <=
//                                        stringFomatDate(str2).getTime() ){//跟进时间小于最晚下发时间
//                            toFollowup++;
//                        }
//                    }
//                }
                else if(stringFomatDateNumber(leadResult.getDealerSendTime())>=16){//最晚第二天早上10点之前
                    String time16=leadResult.getDealerSendTime().replace(leadResult.getDealerSendTime().substring(leadResult.getDealerSendTime().indexOf(" ") + 1,
                            leadResult.getDealerSendTime().indexOf(" ") + 9), "16:00:00");//替换下发时间时分秒为16:00:00
                    if(leadResult.getDealerSendTime().equals(time16)) {//如果下发时间等于16点整
                        String str = formatAddHour(leadResult.getDealerSendTime(),1);//下发的最晚时间加1小时
                        if(stringFomatDate(leadResult.getFirstFollowupTime()).getTime() <=
                                stringFomatDate(str).getTime()){//跟进时间小于最晚下发时间才算 1小时内跟进
                            toFollowup++;
                        }
                    }else{
                        String str = formatNextDay10(leadResult.getDealerSendTime());//下发的最晚时间第二天10点
                        if(stringFomatDate(leadResult.getFirstFollowupTime()).getTime() <=
                                stringFomatDate(str).getTime()){//跟进时间小于最晚下发时间
                            toFollowup++;
                        }
                    }
                }
            }
        }
        logger.info("CZJ 顾问编号："+userId+",线索总数量："+leadResults.size()+",跟进数量:"+toFollowup);
        logger.info("CZJ 返回值："+(leadResults.size()== 0 ? "0.00%":(String.format("%.2f", (float) toFollowup/ (float) leadResults.size() * 100)+"%")));
        return leadResults.size()== 0 ? "0.00%":(String.format("%.2f", (float) toFollowup/ (float) leadResults.size() * 100)+"%");
    }

    @Override
    @RequestMapping(value = "/getArrivalInfoForPredict", method = RequestMethod.GET)
    public ResponseData<Boolean> getArrivalInfoForPredict(Integer leadId) {
        Integer result = leadSearchMapper.getArrivalInfoForPredict(leadId);

        return result == 0 ? ResponseData.success(false) :  ResponseData.success(true);
    }

    @Override
    @RequestMapping(value = "/queryDashboardLeadList", method = RequestMethod.GET)
    public ResponseData<List<LeadResultDTO>> queryDashboardLeadList(Integer timeStamp, Integer owner, Integer queryType) {
        Integer userId = SessionHelper.getCurrentUserId();
        ResponseData<UserDetailDTO> userInfo = sysUserService.monitorInfo(userId);
        String dealerId = userInfo.getData().getDealerId();
        if(timeStamp == null || StringUtils.isBlank(dealerId)||userId == null){
            return ResponseData.fail("参数异常");
        }
        List<LeadResultDTO> resultList = leadSearchMapper.queryDashboardLeadList(timeStamp,owner,dealerId,queryType);
        if(resultList == null){
            return ResponseData.success(new ArrayList<>());
        }
        for (int i  = 0;i < resultList.size();i++) {
            LeadResultDTO rs = resultList.get(i);
            rs.setTheKeyInformation("跟进 "+rs.getFollowupTimes()+"次  到店 "+rs.getArrivalTimes()+"次  试驾 "+rs.getPredictTimes()+"次");
            Integer leadId = rs.getId();
            List<LeadChannelDTO> leadChannelList = leadChannelSearchMapper.getLeadChannelList(leadId);
            if(leadChannelList != null){
                rs.setChannelList(leadChannelList);
            }
            String seriesName = rs.getSeriesName();
            if(StringUtils.isNotBlank(seriesName)){
                CarSeriesDTO carSeries = carService.getCarSeriesByCode(seriesName);
                if(carSeries!=null){
                    rs.setSeriesName(carSeries.getSeriesName());
                }
            }
            String modelName = rs.getModelName();
            if(StringUtils.isNotBlank(modelName) && StringUtils.isNotBlank(seriesName)){
                CarModelDTO carmodel = carService.getCarModelByCode(seriesName,modelName);
                if(carmodel!=null){
                    rs.setModelName(carmodel.getModelName());
                }
            }
        }
        return ResponseData.success(resultList);
    }

    @Override
    @RequestMapping(value = "/queryDashboardLeadListByLeadStatus", method = RequestMethod.GET)
    public ResponseData<List<LeadResultDTO>> queryDashboardLeadListByLeadStatus(Integer timeStamp, Integer owner, Integer queryType, Integer leadResource) {
        Integer userId = SessionHelper.getCurrentUserId();
        ResponseData<UserDetailDTO> userInfo = sysUserService.monitorInfo(userId);
        String dealerId = userInfo.getData().getDealerId();
        if(timeStamp == null || StringUtils.isBlank(dealerId)||userId == null){
            return ResponseData.fail("参数异常");
        }
        List<LeadResultDTO> resultList = leadSearchMapper.queryDashboardLeadListByLeadStatus(timeStamp,owner,dealerId,queryType,leadResource);
        if(resultList == null){
            return ResponseData.success(new ArrayList<>());
        }
        for (int i  = 0;i < resultList.size();i++) {
            LeadResultDTO rs = resultList.get(i);
            rs.setTheKeyInformation("跟进 "+rs.getFollowupTimes()+"次  到店 "+rs.getArrivalTimes()+"次  试驾 "+rs.getPredictTimes()+"次");
            Integer leadId = rs.getId();
            List<LeadChannelDTO> leadChannelList = leadChannelSearchMapper.getLeadChannelList(leadId);
            if(leadChannelList != null){
                rs.setChannelList(leadChannelList);
            }
            String seriesName = rs.getSeriesName();
            if(StringUtils.isNotBlank(seriesName)){
                CarSeriesDTO carSeries = carService.getCarSeriesByCode(seriesName);
                if(carSeries!=null){
                    rs.setSeriesName(carSeries.getSeriesName());
                }
            }
            String modelName = rs.getModelName();
            if(StringUtils.isNotBlank(modelName) && StringUtils.isNotBlank(seriesName)){
                CarModelDTO carmodel = carService.getCarModelByCode(seriesName,modelName);
                if(carmodel!=null){
                    rs.setModelName(carmodel.getModelName());
                }
            }
        }
        return ResponseData.success(resultList);
    }

    private List<Integer> getLeadStatusBeforeDefeatApplyPack(){
        List<Integer> rstList = new ArrayList<>();
        rstList.add(LEAD_STATUS_ENUME.NOT_DISTRIBUTE.getValue());
        rstList.add(LEAD_STATUS_ENUME.DISTRIBUTED.getValue());
        rstList.add(LEAD_STATUS_ENUME.NOT_ARRIVAL.getValue());
        rstList.add(LEAD_STATUS_ENUME.NOT_PREDICT.getValue());
        rstList.add(LEAD_STATUS_ENUME.PREDICT.getValue());
        rstList.add(LEAD_STATUS_ENUME.PRICE_OFFER.getValue());
//        rstList.add(LEAD_STATUS_ENUME.ORDER.getValue());
        return rstList;
    }

    private List<LeadResultDTO> getFollowPlan(List<LeadResultDTO> list){
        if(list != null && list.size() > 0){
            for (int i = 0;i < list.size() ; i++){
                LeadResultDTO lrdto = list.get(i);
                List<LeadFollowupDTO> followPlan = leadFollowupSearchMapper.queryFollowupPlan(lrdto.getId());
                if(followPlan != null && followPlan.size()>0){
                    if(followPlan.get(0).getFollowPlan() != null){
                        lrdto.setFollowPlan(followPlan.get(0).getFollowPlan());
                    }
                }
            }
        }
        return list;
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "JOBS_APP_MESSAGE", durable = "true"),
            exchange = @Exchange("")), containerFactory="rabbitListenerContainerFactory")
    public void doAppMessageHandler(@Payload String msg){
        logger.info("AppMessageHandler>>>>>msg:"+msg + "定时器执行开始 date time :"+new Date());
        logger.debug("Task>>AppMessageSendScheduleServiceImpl>>微信公众好消息推送定时器启动");
        try {
            //执行定时任务
            this.pushLeadNumToConsultant();
            this.pushLeadNumToDealerManager();
            this.pushLeadNumToExperienceManager();
        }catch (Exception e){
            logger.error("微信公众好消息推送定时器执行异常。",e);
        }
        logger.debug("AppMessageHandler>>>>>msg:"+msg +"定时器执行结束 date time :"+new Date());
    }

    @Override
    @RequestMapping(value = "/consultant/updateLeadToActivationApply", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class,transactionManager = "leadTransactionManager",propagation = Propagation.REQUIRED)
    public ResponseData<Integer> updateLeadToActivationApply(Long leadId) {
        Lead data=leadMapper.selectLeadById(leadId);
        Integer sum =leadMapper.getCheck(data.getPhone(),data.getDealer());
        if(sum >=1 ){
            return ResponseData.success(400,"该客户已在您店中已有未闭环线索，暂时无法激活");
        }
        else{
            return ResponseData.success(200,"线索可以激活");
        }
    }

    @Override
    @RequestMapping(value = "/lead/querytToBeDistributedList", method = RequestMethod.POST)
    public ResponseData<Map<String, Object>> querytToBeDistributedList(String dealer, Integer type, String sericeName, String resourceType, Integer pageNo, Integer pageSize) {
        List<LeadResultDTO> resultList=new ArrayList<>();
        Map<String,Object> map=new HashMap<>();
        if (type == 1) {
            resultList = leadMapper.selectToBeDistributedList(dealer,sericeName,resourceType,pageNo,pageSize);
            Integer total = leadMapper.ToBeDistributedCount(dealer,sericeName,resourceType,pageNo,pageSize);

            if(resultList != null){
                for (LeadResultDTO rs:resultList) {
                    Integer owner = rs.getOwner();
                    if(owner != null){
                        rs.setOwnerDesc(leadCommonService.getUserName(owner));
                    }
                    List<LeadChannelDTO> leadChannelList = leadChannelSearchMapper.getLeadChannelList(rs.getId());
                    rs.setChannelList(leadChannelList);
                    int count = leadMapper.selectFlag(rs.getId());
                    if (count >0){
                        rs.setFlag(1);//线上自动激活显示蓝色 flag为1
                    }else {
                        rs.setFlag(0);//不是自动激活不显示蓝色 flag为0
                    }
                }
            }
            map.put("data",resultList);
            map.put("totalCount",total);
        } else if (type == 2){
            resultList = leadMapper.selectToBeDistributedCount(dealer);
            map.put("data",resultList);
        }
        return ResponseData.success(map);
    }

    public static void main(String[] args) {
        String startSendTime="";
        String endSendTime="";

    }
    @Override
    @RequestMapping(value = "/lead/oneHourFollowUpRate", method = RequestMethod.POST)
    public ResponseData<Map<String ,Object>> oneHourFollowUpRate(String startSendTime, String endSendTime) {
        //默认查询今天
        Date now=new Date();
        if((null == startSendTime && null == endSendTime) || (("").equals(startSendTime) && ("").equals(endSendTime))){
            startSendTime = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(now);
            endSendTime = new SimpleDateFormat("yyyy-MM-dd 23:59:59").format(now);
        }
        Integer userId = SessionHelper.getCurrentUserId();
        UserDetailDTO userInfo = leadCommonService.getUserInfo(userId);
        String dealer = userInfo.getDealerId();
        logger.info("CZJ 当前用户编号："+userId+",经销商编号："+dealer);
        List<LeadResultDTO> leadResults = leadMapper.queryOneHourFollowUpRateOne(dealer,startSendTime,endSendTime);
        logger.info("CZJ 线索总数："+leadResults.size());
        Map<String ,Object> map=new HashMap<>();
        Integer toFollowup=0;//用于累计是否是一小时跟进的线索
        for(LeadResultDTO leadResult:leadResults){
            //取下发时间加一小时  如果跟进时间大于下发+的时间为null  否则为是
            if(leadResult.getDealerSendTime()!=null && !leadResult.getDealerSendTime().equals("") && leadResult.getFirstFollowupTime()!=null && !leadResult.getFirstFollowupTime().equals("")){
                if(stringFomatDateNumber(leadResult.getDealerSendTime())>=0 && stringFomatDateNumber(leadResult.getDealerSendTime())<9){//是凌晨之后，要在今天10点之前跟进
                    //获取下发时间当天的10点  如果跟进时间大于10点为null 否者为是
                    String firstFollowupTime = formatDay10(leadResult.getDealerSendTime());//下发的最晚时间今天10点
                    if(stringFomatDate(leadResult.getFirstFollowupTime()).getTime() <=
                            stringFomatDate(firstFollowupTime).getTime()){//跟进时间小于最晚下发时间
                        toFollowup++;
                    }
                }else if(stringFomatDateNumber(leadResult.getDealerSendTime())>=9 && stringFomatDateNumber(leadResult.getDealerSendTime())<16){//1小时内跟进
                    //当天时间加1小时
                    String str = formatAddHour(leadResult.getDealerSendTime(),1);//下发的最晚时间加1小时
                    if(stringFomatDate(leadResult.getFirstFollowupTime()).getTime() <=
                            stringFomatDate(str).getTime()){//跟进时间小于最晚下发时间
                        toFollowup++;
                    }
                }
//                else if(stringFomatDateNumber(leadResult.getDealerSendTime())>=16 && stringFomatDateNumber(leadResult.getDealerSendTime())<17){//第二天10点前某个时间之前跟进
//                    //如果跟进时间小于当天的16点 或者 第二天9-10点间的当天时间的分秒
//                    String str1 = formatDay17(leadResult.getDealerSendTime());//下发的最晚时间16点
//                    String str2 = formatNextDay9(leadResult.getDealerSendTime());//下发的最晚时间明天9点
//                    String str=leadResult.getDealerSendTime().replace(leadResult.getDealerSendTime().substring(leadResult.getDealerSendTime().indexOf(" ") + 1,
//                            leadResult.getDealerSendTime().indexOf(" ") + 9), "16:00:00");//获取下发时间为15点整时间如 2021-4-4 15:33:22 --2021-4-4 15:00:00
//                    if(leadResult.getDealerSendTime().equals(str)){//如果下发时间是15点整，就只能在16点整之前跟进
//                        if(stringFomatDate(leadResult.getFirstFollowupTime()).getTime() <=
//                                stringFomatDate(str1).getTime()){//跟进时间小于最晚下发时间
//                            toFollowup++;
//                        }
//                    }else{//否者可以在 跟进时间小于16点 或 第二天9点的相同分秒之前跟进
//                        if(stringFomatDate(leadResult.getFirstFollowupTime()).getTime() <=
//                                stringFomatDate(str1).getTime() ||
//                                stringFomatDate(leadResult.getFirstFollowupTime()).getTime() <=
//                                        stringFomatDate(str2).getTime() ){//跟进时间小于最晚下发时间
//                            toFollowup++;
//                        }
//                    }
//                }
                else if(stringFomatDateNumber(leadResult.getDealerSendTime())>=16){//最晚第二天早上10点之前
                    String time16=leadResult.getDealerSendTime().replace(leadResult.getDealerSendTime().substring(leadResult.getDealerSendTime().indexOf(" ") + 1,
                            leadResult.getDealerSendTime().indexOf(" ") + 9), "16:00:00");//替换下发时间时分秒为16:00:00
                    if(leadResult.getDealerSendTime().equals(time16)) {//如果下发时间等于16点整
                        String str = formatAddHour(leadResult.getDealerSendTime(),1);//下发的最晚时间加1小时
                        if(stringFomatDate(leadResult.getFirstFollowupTime()).getTime() <=
                                stringFomatDate(str).getTime()){//跟进时间小于最晚下发时间才算 1小时内跟进
                            toFollowup++;
                        }
                    }else{
                        String str = formatNextDay10(leadResult.getDealerSendTime());//下发的最晚时间第二天10点
                        if(stringFomatDate(leadResult.getFirstFollowupTime()).getTime() <=
                                stringFomatDate(str).getTime()){//跟进时间小于最晚下发时间
                            toFollowup++;
                        }
                    }
                }
            }
        }
        map.put("total",leadResults.size());
        map.put("dealerRate",leadResults.size()== 0 ? "0.00%":(String.format("%.2f", (float) toFollowup/ (float) leadResults.size() * 100)+"%"));
        map.put("followup",toFollowup);
        return ResponseData.success(map);
    }


    /**
     * 获取字符串中小时数
     * @param str
     * @return
     */
    public Integer stringFomatDateNumber(String str){
        if(str!=null){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateTime = null;
            try {
                dateTime = simpleDateFormat.parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return dateTime.getHours();
        }
        return null;
    }

    /**
     * 字符串时间转换成时间对象
     * @param str
     * @return
     */
    public static Date stringFomatDate(String str){
        if(str!=null){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateTime = null;
            try {
                dateTime = simpleDateFormat.parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return dateTime;
        }
        return null;
    }
    //当天的10点
    public static String formatDay10(String str){
        if(str!=null){
            return str.replace(str.substring(str.indexOf(" ") + 1, str.indexOf(" ") + 9), "10:00:00");
        }
        return null;
    }
    //下一天的10点
    public static String formatNextDay10(String str){
        if(str!=null){
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(stringFomatDate(str));
            calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
            Date time = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return formatDay10(sdf.format(time));
        }
        return null;
    }

    //当天的16点-------列：2021-4-11 15:33:33    ---2021-4-11 16:00:00
    public static String formatDay17(String str){
        if(str!=null){
            return str.replace(str.substring(str.indexOf(" ") + 1, str.indexOf(" ") + 9), "17:00:00");
        }
        return null;
    }

    //下一天的9点-------列：2021-4-11 15:33:33    ---2021-4-12 9:33:33
    public static String formatNextDay9(String str){
        if(str!=null){
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(stringFomatDate(str));
            calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
            Date time = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = sdf.format(time);
            return format.replace(format.substring(format.indexOf(" ") + 1, format.indexOf(" ") + 3), "09");
        }
        return null;
    }

    //时间加一小时
    public static String formatAddHour(String str,Integer number){
        if(str==null){
            return null;
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(stringFomatDate(str));
        calendar.add(calendar.HOUR,number);//把日期往后增加一天.整数往后推,负数往前移动
        Date time = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(time);
    }
}

