package com.chery.exeed.crm.lead.service;

import com.alibaba.fastjson.JSON;
import com.chery.exeed.crm.common.dto.DealerDTO;
import com.chery.exeed.crm.common.dto.ResponseData;
import com.chery.exeed.crm.common.service.CarService;
import com.chery.exeed.crm.common.service.ChannelService;
import com.chery.exeed.crm.common.service.DealerService;
import com.chery.exeed.crm.common.service.RegionService;
import com.chery.exeed.crm.common.util.BeanUtils;
import com.chery.exeed.crm.common.util.DateUtils;
import com.chery.exeed.crm.common.util.SessionHelper;
import com.chery.exeed.crm.common.util.StringUtils;
import com.chery.exeed.crm.lead.constants.LeadConstants;
import com.chery.exeed.crm.lead.constants.LeadConstants.LEAD_STATUS_ENUME;
import com.chery.exeed.crm.lead.dto.*;
import com.chery.exeed.crm.lead.mapper.*;
import com.chery.exeed.crm.lead.model.*;
import com.chery.exeed.ifs.common.dto.*;
import com.chery.exeed.ifs.common.model.Region;
import com.chery.exeed.ifs.automedia.model.DccLead;
import com.chery.exeed.ifs.common.dto.ActinstanceChannelDTO;
import com.chery.exeed.ifs.common.dto.ChannelConfigDTO;
import com.chery.exeed.ifs.common.model.TestDriverRequestDTO;
import org.apache.commons.lang.math.NumberUtils;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Transactional
@RestSchema(schemaId = "prelead-service")
@RequestMapping(path = "/apis/prelead/")
public class PreLeadServiceImpl implements PreLeadService{
    private Logger logger = LoggerFactory.getLogger(PreLeadServiceImpl.class);
    public final static String STATUS = "status";
    public final static String SUCCESS = "success";
    public final static String MESSAGE = "message";
    public final static String ERROR = "error";

    @Autowired
    private PreLeadMapper preLeadMapper;
    @Autowired
    private PreLeadImportMapper preLeadImportMapper;
    @Autowired
    private LeadMapper leadMapper;
    @Autowired
    private LeadSearchMapper leadSearchMapper;
    @Autowired
    private LeadChannelMapper leadChannelMapper;
    @Autowired
    private LeadTaskMapper leadTaskMapper;
    @Autowired
    private DealerService dealerService;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private LeadCommonService leadCommonService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Value("${channel.self}")
    private String selfChannelCode;
    @Autowired
    private MetaService metaService;
    @Autowired
    private RegionService regionService;
    @Autowired
    private CarService carService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private FollowupHistorySearchMapper followupHistorySearchMapper;

    @Override
    @Transactional(rollbackFor = Exception.class,transactionManager = "leadTransactionManager")
    public ResponseData<Boolean> create(PreLeadDTO preLeadDTO) {
        PreLead entity = new PreLead();
        BeanUtils.copyProperties(preLeadDTO,entity);
        /**看是否有相同的*/
        LeadDTO leadDTO = new LeadDTO();
        String phone = preLeadDTO.getPhone();
        if(StringUtils.isEmpty(phone)){
            logger.error("联系方式为空,不能入库!");
            return ResponseData.fail("联系方式为空");
        }
        leadDTO.setPhone(phone);
        leadDTO.setLeadStatus(1);

        /** 经销商有效性校验 */
        String dealer = preLeadDTO.getDealer();




        DealerDTO dealerDTO = dealerService.detailsDealer(dealer);
        if(!StringUtils.isEmpty(dealer)){
            if(dealerDTO==null) {
                dealer = null;
                logger.error("经销商ID无效");
                preLeadDTO.setDealer(null);
            }
        }else{
            dealer = null;
            preLeadDTO.setDealer(null);
        }

        /**1213 需求：
         *1、	线索进线索池，判断是否需要生成外呼
         * 1）	若生成外呼任务，则根据线索合并逻辑清洗——验证通过
         * 2）	若不生成外呼任务，且线索没有经销商，则根据线索合并逻辑清洗——验证通过
         *
         * 3）	若不生成外呼任务，且线索有经销商，则直接下发到经销商——验证不通过
         * ——DCC等信任渠道、批量导入（最终拨打状态=已完成且有经销商）的线索，进线索池应该不触发线索合并逻辑，而是直接下发到经销商。
         *    但是下发到经销商时需要判断经销商有没有未闭环的同手机号线索，有就触发撞单逻辑
         * 2、无论哪里的线索线索下发经销商时，都要判断经销商有无未闭环的同手机号线索
         * 1）	若有，则触发撞单逻辑
         * 2）	若无，则正常下发
         *
         */

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String,Object> param = new HashMap<>();
        Lead lead = null;

        Date now = new Date();

        Integer leadId;
        boolean createFlag = false;
        Integer currentUserId = preLeadDTO.getCreatedBy()==null?1:preLeadDTO.getCreatedBy();
        Integer status = (lead==null?0:1);//0 非重复 1 重复

        //根据经销商和线索查询是否只是有失效和战败
        List<Lead> leads=leadSearchMapper.selectDefeatAndInvalidLead( dealer, phone, new String[]{"4","9","15"});//查询失效和战败的线索
        List<Lead> leads1=new ArrayList<>();//查询失效和战败的线索
        for(int i=0;i<leads.size();i++){
            //3,5,15,19--线索带这些状态也可以自动激活
            if(leads.get(i).getLeadStatus()==3 || leads.get(i).getLeadStatus()==5 || leads.get(i).getLeadStatus()==15 || leads.get(i).getLeadStatus()==19){
                continue;
            }else{
                leads1.add(leads.get(i));//保存战败和失效线索
            }
        }
        List<Lead> leads2=leadSearchMapper.selectDefeatAndInvalidLead( dealer, phone, null);//查询所有经销商
        if(leads.size()==leads2.size() && leads1.size()!=0){//只有失效和战败线索和闭环的线索
            String channelName="";
            if(null != preLeadDTO.getChannelCode()){
                ChannelDTO channelByCode = channelService.getChannelByCode(preLeadDTO.getChannelCode());
                channelName=channelByCode.getName();
            }
            FollowupHistory followupHistory=new FollowupHistoryDTO();
            //失效11  战败9
            if(null != leads1.get(0).getLeadStatus()){
                if(leads1.get(0).getLeadStatus()==9){
                    followupHistory.setBizType(9);
                }
                if(leads1.get(0).getLeadStatus()==4){
                    followupHistory.setBizType(11);
                }
            }
            leads1.get(0).setLeadStatus(2);//激活最新的一条线索状态为已下发待分配
            leads1.get(0).setLevel("3");//线索分级变为A。  meta_lead表中3对应A
            leadMapper.updateByPrimaryKeySelective(leads1.get(0));
            followupHistory.setLeadId(leads1.get(0).getId());//线索编号
            followupHistory.setOperateDesc("线索分配");
            followupHistory.setOperateResultDesc(channelName+"自动激活");//操作结果描述
            followupHistory.setOperateBy(currentUserId+"");//操作者
            followupHistory.setOperateTime(now);
            followupHistorySearchMapper.insertHistory(followupHistory);
            //线索热度次数
            Integer leadHeatingCount = leadSearchMapper.getLeadHeatingCount(leads1.get(0).getId());//获取热度次数
            logger.info("CZJ 原热度次数："+leadHeatingCount);
            if(leadHeatingCount == 0){
                Integer count = leadSearchMapper.selectDefeatAndInvalidLeadCount(dealer, phone);//查询除了赢单之外的线索总数量
                logger.info("CZJ 经销商编号："+dealer+"号码："+phone+"原热度次数："+count);
                leadHeatingCount=count+1;
            } else{
                leadHeatingCount=leadHeatingCount+1;
            }
            for(int i=0;i< leads2.size();i++){//查询到的所有非赢单线索都加上热度次数
                logger.info("CZJ 线索编号："+leads2.get(i).getId()+"热度次数："+leadHeatingCount);
                leadSearchMapper.updateLeadToLeadHeatingCountById(leads2.get(i).getId(),leadHeatingCount);//修改热度次数
            }
        }else {

            CustomerDTO customerDTO = leadCommonService.setLead2Customer(preLeadDTO, now, currentUserId, null);
            Integer leadFrequency = customerDTO.getLeadFrequency();//线索咨询次数
            Integer custId = customerDTO.getId();

            String level = getLevelByPurchaseTime(preLeadDTO.getPurchaseTime());
            if (level != null) {
                preLeadDTO.setLevel(level);
            }
            String channelCode = preLeadDTO.getChannelCode();
            String activityCode = preLeadDTO.getActivityCode();
            String campaignCode = preLeadDTO.getCampaignCode();

            boolean isContains = false;
            if (!StringUtils.isEmpty(channelCode) && !StringUtils.isEmpty(dealer)) {
//            Pattern pattern = Pattern.compile("[0-9]");
//            boolean matches = pattern.matcher(channelCode).matches();
//            if(matches){
                isContains = LeadConstants.IGNORE_DEALER_IS_DISTRIBUTED.ignoreDealerIsDistributed().contains(channelCode);
//            }
            }

            Integer taskStatus = preLeadDTO.getTaskStatus();

            Integer createTask = 1;
            ChannelConfigDTO channelConfig = channelService.getChannelConfigByChannelCode(channelCode);
            if (channelConfig != null && channelConfig.getCreateTask() != null && channelConfig.getCreateTask().intValue() == 0) {
                createTask = 0;
            }
            //若不生成外呼任务，且线索有经销商，则直接下发到经销商
            if ((createTask == 0 && !StringUtils.isEmpty(dealer) && dealerDTO != null && dealerDTO.getIsDistribute() == 1)
                    || (preLeadDTO.getImportFlag() != null && preLeadDTO.getImportFlag() == 1
                    && !StringUtils.isEmpty(dealer) && preLeadDTO.getTaskStatus() != null && preLeadDTO.getTaskStatus() == 7
                    && dealerDTO != null && dealerDTO.getIsDistribute() == 1)) {
                logger.info("无去重逻辑，直接下发到经销商;手机号[{}]===经销商[{}]", phone, dealer);
                lead = new Lead();
                BeanUtils.copyProperties(preLeadDTO, lead);
                lead.setLeadStatus(1);//初始状态为1:新建
                if (!StringUtils.isEmpty(preLeadDTO.getMemo())) {
                    lead.setDescription((lead.getDescription() == null ? "" : lead.getDescription()) + preLeadDTO.getMemo());
                }
                Integer leadStatus = LEAD_STATUS_ENUME.CREATED.getValue();
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("phone", phone);
                paramMap.put("dealer", dealer);
                logger.info("preLead-create-无去重逻辑，直接下发到经销商-下发线索-查重:线索[{}],经销商[{}]", phone, dealer);
                //查询手机号、经销商对应的线索  在这里进行判断如果集合中只存在战败和失效的线索就取最新的那条线索改变其状态为待已下发待分配
                List<Lead> leadList = leadSearchMapper.searchLeadByPhoneNoForSended(paramMap);
                logger.info("preLead-create-无去重逻辑，直接下发到经销商-下发线索-查重:线索[{}],经销商[{}],线索条数[{}]", phone, dealer, leadList == null ? null : leadList.size());
                Lead oldLead = null;
                if (leadList != null && leadList.size() > 0) {
                    for (Lead obj : leadList) {
                        oldLead = obj;
                        break;
                    }
                }
                if (oldLead != null) {
                    //如果查询除的线索存在数据就进行查询是否-只有战败或失效或者同时存在战败和失效线索。自动激活战败/失效线索中最新的一条，线索状态变为已下发待分配
                    //如果只有战败和失效线索
                    logger.info("preLead-create-已撞单,手机号:[{}]线索ID[{}]合并到的线索ID[{}]", phone, lead, oldLead.getId());
                    logger.info("preLead-create-合并线索,合并Lead:[{}]", JSON.toJSONString(lead));
                    logger.info("preLead-create-合并线索,合并前Lead:[{}]", JSON.toJSONString(oldLead));
                    BeanUtils.copyPropertiesHasValue(lead, oldLead);
                    System.out.println("preLead-create-合并线索,合并后:====" + JSON.toJSONString(oldLead));
                    oldLead.setModifyDate(now);
                    oldLead.setModifyBy(currentUserId);
                    if (!StringUtils.isEmpty(lead.getSeriesName()) && lead.getSeriesName() != null && !lead.getSeriesName().equals(oldLead.getSeriesName())) {
                        CarSeriesDTO carSeriesByCode = carService.getCarSeriesByCode(lead.getSeriesName());
                        if (StringUtils.isEmpty(oldLead.getDescription()) || oldLead.getDescription() == null) {
                            if (carSeriesByCode != null) {
                                oldLead.setDescription("preLead-create-合并车型线索 " + carSeriesByCode.getSeriesName());
                            } else {
                                oldLead.setDescription("preLead-create-合并车型线索 " + lead.getSeriesName());
                            }
                        } else {
                            if (carSeriesByCode != null) {
                                oldLead.setDescription(oldLead.getDescription() + ";合并车型线索 " + carSeriesByCode.getSeriesName());
                            } else {
                                oldLead.setDescription(oldLead.getDescription() + ";合并车型线索 " + lead.getSeriesName());
                            }
                        }
                    }
                    leadMapper.updateByPrimaryKeySelective(oldLead);//这里没有修改线索的状态
                    logger.info("preLead-无去重逻辑，直接下发到经销商-复制渠道信息newLead[{}]->oldLead[{}]...", lead.getId(), oldLead.getId());
                    leadCommonService.copyChannel2OldLead(oldLead.getId(), lead.getId());
                    leadStatus = LEAD_STATUS_ENUME.SAME_LIST.getValue();
                } else {
                    logger.info("下发到经销商");
                    leadStatus = LEAD_STATUS_ENUME.SEND.getValue();
                    lead.setSendTime(now);
                    createTask = 0;
                }
                if (StringUtils.isEmpty(lead.getLevel())) {
                    lead.setLevel(level);
                }
                lead.setLeadStatus(leadStatus);
                lead.setCustId(custId);
                lead.setCustomerCarDate(now);
                Integer rating = getLeadRing(leadFrequency, lead);
                lead.setRating(rating);
                leadSearchMapper.insertReturnPK(lead);
                leadId = lead.getId();
                logger.info("线索ID:" + leadId);
                lead = new Lead();
                lead.setLeadNumber("L" + (100000000 + leadId + "").substring(1));
                lead.setId(leadId);
                leadMapper.updateByPrimaryKeySelective(lead);
                leadCommonService.insertLeadHistory(leadId, now, currentUserId, LEAD_STATUS_ENUME.CREATED.getValue(), LEAD_STATUS_ENUME.CREATED.getDesc(),
                        dealer, null, null);
                if (LEAD_STATUS_ENUME.SEND.getValue().equals(leadStatus)) {
                    leadCommonService.insertLeadHistory(leadId, new Date(), currentUserId, LEAD_STATUS_ENUME.SEND.getValue(), LEAD_STATUS_ENUME.SEND.getDesc(),
                            dealer, null, null);
                }
                if (LEAD_STATUS_ENUME.SAME_LIST.getValue().equals(leadStatus)) {
                    leadCommonService.insertLeadHistory(leadId, new Date(), currentUserId, LEAD_STATUS_ENUME.SAME_LIST.getValue(), LEAD_STATUS_ENUME.SAME_LIST.getDesc(),
                            dealer, null, null);
                }
                //线索热度次数
                Integer leadHeatingCount = leadSearchMapper.getLeadHeatingCount(leadId);//获取热度次数
                logger.info("CZJ 原热度次数："+leadHeatingCount);
                if(leadHeatingCount == 0){
                    Integer count = leadSearchMapper.selectDefeatAndInvalidLeadCount(dealer, phone);//查询除了赢单之外的线索总数量
                    logger.info("CZJ 经销商编号："+dealer+"号码："+phone+"原热度次数："+count);
                    leadHeatingCount=count+1;
                } else{
                    leadHeatingCount=leadHeatingCount+1;
                }
                for(int i=0;i< leads2.size();i++){//查询到的所有非赢单线索都加上热度次数
                    logger.info("CZJ 线索编号："+leads2.get(i).getId()+"热度次数："+leadHeatingCount);
                    leadSearchMapper.updateLeadToLeadHeatingCountById(leads2.get(i).getId(),leadHeatingCount);//修改热度次数
                }
            } else {
                logger.info("清洗查重,查重条件:手机号和经销商...[{}][{}]", phone, preLeadDTO.getDealer());
                param.put("phone", phone);
                if (!StringUtils.isEmpty(dealer)) {
                    param.put("dealer", dealer);
                    param.put("time", 2);
                    logger.info("线索查重...");
                    List<Lead> leadList = leadSearchMapper.searchLeadByPhoneNo(param);
                    if (leadList != null && leadList.size() > 0) {
                        for (Lead obj : leadList) {
                            if (dealer != null && !StringUtils.isEmpty(dealer) && dealer.equals(obj.getDealer())) {
                                lead = obj;
                                break;
                            }
                        }
                    } else {
                        param.clear();
                        param.put("phone", phone);
                        param.put("time", 1);
                        List<Lead> leadList2 = leadSearchMapper.searchLeadByPhoneNo(param);
                        if (leadList2 != null && leadList2.size() > 0) {
                            lead = leadList2.get(0);
                        }
                    }
                } else {
                    param.put("phone", phone);
                    param.put("time", 2);
                    List<Lead> leadList = leadSearchMapper.searchLeadByPhoneNo(param);
                    if (leadList != null && leadList.size() > 0) {
                        lead = leadList.get(0);
                    }

                }
                /**没有则新建一个lead,否则更新;更新规则为,非空补空,新值补旧值*/
                if (lead == null) {
                    logger.info("不存在重复线索,新建一个新线索[{}]", phone);
                    lead = new Lead();
                    BeanUtils.copyProperties(preLeadDTO, lead);
                    lead.setLeadStatus(1);//初始状态为1:新建
                    if (!StringUtils.isEmpty(preLeadDTO.getMemo())) {
                        lead.setDescription((lead.getDescription() == null ? "" : lead.getDescription()) + preLeadDTO.getMemo());
                    }
                    Integer leadStatus = LEAD_STATUS_ENUME.CREATED.getValue();


                    if (preLeadDTO.getImportFlag() != null && preLeadDTO.getImportFlag() == 1) {
                        logger.info("批量导入线索数据,手机号[{}],外呼任务状态[{}]", phone, preLeadDTO.getTaskStatus());
                        if (preLeadDTO.getTaskStatus() == 7 && !StringUtils.isEmpty(dealer)) {
                            logger.info("由于该线索拨打状态为已完成 且有经销商 ,[{}]自动下发", phone);
                            int isDistribute = dealerDTO.getIsDistribute();
                            logger.info("下发的经销商isDistribute========>" + "isDistribute");
                            if (isDistribute == 1 || isContains) {
                                //TODO  1.下发经销商触发状态逻辑
                                Map<String, Object> paramMap = new HashMap<>();
                                paramMap.put("phone", phone);
                                paramMap.put("dealer", dealer);
                                logger.info("preLead-create-批量导入线索数据-下发线索-查重:线索[{}],经销商[{}]", phone, dealer);
                                List<Lead> leadList = leadSearchMapper.searchLeadByPhoneNoForSended(paramMap);
                                logger.info("preLead-create-批量导入线索数据-下发线索-查重:线索[{}],经销商[{}],线索条数[{}]", phone, dealer, leadList == null ? null : leadList.size());
                                Lead oldLead = null;
                                if (leadList != null && leadList.size() > 0) {
                                    for (Lead obj : leadList) {
                                        oldLead = obj;
                                        break;
                                    }
                                }
                                if (oldLead != null) {
                                    logger.info("preLead-create-已撞单,手机号:[{}]线索ID[{}]合并到的线索ID[{}]", phone, lead, oldLead.getId());
                                    logger.info("preLead-create-合并线索,合并Lead:[{}]", JSON.toJSONString(lead));
                                    logger.info("preLead-create-合并线索,合并前Lead:[{}]", JSON.toJSONString(oldLead));
                                    BeanUtils.copyPropertiesHasValue(lead, oldLead);
                                    System.out.println("preLead-create-合并线索,合并后:====" + JSON.toJSONString(oldLead));
                                    oldLead.setModifyDate(now);
                                    oldLead.setModifyBy(currentUserId);
                                    if (!StringUtils.isEmpty(lead.getSeriesName()) && lead.getSeriesName() != null && !lead.getSeriesName().equals(oldLead.getSeriesName())) {
                                        CarSeriesDTO carSeriesByCode = carService.getCarSeriesByCode(lead.getSeriesName());
                                        if (StringUtils.isEmpty(oldLead.getDescription()) || oldLead.getDescription() == null) {
                                            if (carSeriesByCode != null) {
                                                oldLead.setDescription("preLead-create-合并车型线索 " + carSeriesByCode.getSeriesName());
                                            } else {
                                                oldLead.setDescription("preLead-create-合并车型线索 " + lead.getSeriesName());
                                            }
                                        } else {
                                            if (carSeriesByCode != null) {
                                                oldLead.setDescription(oldLead.getDescription() + ";合并车型线索 " + carSeriesByCode.getSeriesName());
                                            } else {
                                                oldLead.setDescription(oldLead.getDescription() + ";合并车型线索 " + lead.getSeriesName());
                                            }
                                        }
                                    }
                                    leadMapper.updateByPrimaryKeySelective(oldLead);
                                    logger.info("preLead-create-复制渠道信息newLead[{}]->oldLead[{}]...", lead.getId(), oldLead.getId());
                                    leadCommonService.copyChannel2OldLead(oldLead.getId(), lead.getId());
                                    leadStatus = LEAD_STATUS_ENUME.SAME_LIST.getValue();
                                } else {
                                    logger.info("下发到经销商");
                                    leadStatus = LEAD_STATUS_ENUME.SEND.getValue();
                                    lead.setSendTime(now);
                                    createTask = 0;
                                }
                            } else {
                                logger.info("由于该经销商禁止线索下发，该线索[{}]不可下发", phone, channelCode);
                            }
                        }
                    } else {
                        logger.info("手动创建线索数据，手机号[{}] 渠道[{}]", phone, channelCode);
                        /**拨打状态已完成：按照正常的导入逻辑判断是否下发*/
                        if (!StringUtils.isEmpty(channelCode) && !StringUtils.isEmpty(dealer)) {
                            if (channelConfig == null || (channelConfig.getIsSend() != null && channelConfig.getIsSend() == 1)) {
                                /**新需求0726：判断经销商是否允许下发线索 */
                                int isDistribute = dealerDTO.getIsDistribute();
                            /*Pattern pattern = Pattern.compile("[0-9]*");
                            boolean matches = pattern.matcher(channelCode).matches();
                            boolean isContains ;
                            if(matches){
                                isContains = LeadConstants.IGNORE_DEALER_IS_DISTRIBUTED.ignoreDealerIsDistributed().contains(Integer.parseInt(channelCode));
                            }else{
                                isContains = false;
                            }*/
//                            logger.info("下发的经销商isDistribute[{}]========>是否是DCC线索[{}]",isDistribute,isContains);
                                if (isContains) {
                                    logger.info("该线索[{}]的渠道[{}]是可下发,则直接下发", phone, channelCode);
                                    //TODO  2.下发经销商触发状态逻辑
                                    Map<String, Object> paramMap = new HashMap<>();
                                    paramMap.put("phone", phone);
                                    paramMap.put("dealer", dealer);
                                    logger.info("preLead-create-自动下发线索-查重:线索[{}],经销商[{}]", phone, dealer);
                                    List<Lead> leadList = leadSearchMapper.searchLeadByPhoneNoForSended(paramMap);
                                    logger.info("preLead-create-自动下发线索-下发线索-查重:线索[{}],经销商[{}],线索条数[{}]", phone, dealer, leadList == null ? null : leadList.size());
                                    Lead oldLead = null;
                                    if (leadList != null && leadList.size() > 0) {
                                        for (Lead obj : leadList) {
                                            oldLead = obj;
                                            break;
                                        }
                                    }
                                    if (oldLead != null) {
                                        logger.info("preLead-create-已撞单,手机号:[{}]线索ID[{}]合并到的线索ID[{}]", phone, lead, oldLead.getId());
                                        logger.info("preLead-create-合并线索,合并Lead:[{}]", JSON.toJSONString(lead));
                                        logger.info("preLead-create-合并线索,合并前Lead:[{}]", JSON.toJSONString(oldLead));
                                        BeanUtils.copyPropertiesHasValue(lead, oldLead);
                                        System.out.println("preLead-create-合并线索,合并后:====" + JSON.toJSONString(oldLead));
                                        oldLead.setModifyDate(now);
                                        oldLead.setModifyBy(currentUserId);
                                        if (!StringUtils.isEmpty(lead.getSeriesName()) && lead.getSeriesName() != null && !lead.getSeriesName().equals(oldLead.getSeriesName())) {
                                            CarSeriesDTO carSeriesByCode = carService.getCarSeriesByCode(lead.getSeriesName());
                                            if (StringUtils.isEmpty(oldLead.getDescription()) || oldLead.getDescription() == null) {
                                                if (carSeriesByCode != null) {
                                                    oldLead.setDescription("preLead-create-合并车型线索 " + carSeriesByCode.getSeriesName());
                                                } else {
                                                    oldLead.setDescription("preLead-create-合并车型线索 " + lead.getSeriesName());
                                                }
                                            } else {
                                                if (carSeriesByCode != null) {
                                                    oldLead.setDescription(oldLead.getDescription() + ";合并车型线索 " + carSeriesByCode.getSeriesName());
                                                } else {
                                                    oldLead.setDescription(oldLead.getDescription() + ";合并车型线索 " + lead.getSeriesName());
                                                }
                                            }
                                        }
                                        leadMapper.updateByPrimaryKeySelective(oldLead);
                                        logger.info("preLead-create-复制渠道信息newLead[{}]->oldLead[{}]...", lead.getId(), oldLead.getId());
                                        leadCommonService.copyChannel2OldLead(oldLead.getId(), lead.getId());
                                        leadStatus = LEAD_STATUS_ENUME.SAME_LIST.getValue();
                                    } else {
                                        logger.info("下发到经销商");
                                        leadStatus = LEAD_STATUS_ENUME.SEND.getValue();
                                        lead.setSendTime(now);
                                        createTask = 0;
                                    }
                                } else {
                                    logger.info("由于该经销商禁止线索下发，该线索[{}]不可下发", phone, channelCode);
                                }
                            } else {
                                logger.info("该线索[{}]的渠道[{}]不可下发", phone, channelCode);
                            }
                        }
                    }

                    if (StringUtils.isEmpty(lead.getLevel())) {
                        lead.setLevel(level);
                    }
                    lead.setLeadStatus(leadStatus);
                    if (LEAD_STATUS_ENUME.CREATED.getValue().equals(leadStatus) && taskStatus != null) {
                        /**空号错号或已拒绝,线索状态为已拒绝*/
                        if (taskStatus == 8 || taskStatus == 9) {
                            lead.setLeadStatus(5);
                        }
                    }


                    lead.setCustId(custId);
                    lead.setCustomerCarDate(now);
                    Integer rating = getLeadRing(leadFrequency, lead);
                    lead.setRating(rating);
                    leadSearchMapper.insertReturnPK(lead);
                    leadId = lead.getId();
                    logger.info("线索ID:" + leadId);
                    lead = new Lead();
                    lead.setLeadNumber("L" + (100000000 + leadId + "").substring(1));
                    lead.setId(leadId);
                    leadMapper.updateByPrimaryKeySelective(lead);
                    leadCommonService.insertLeadHistory(leadId, now, currentUserId, LEAD_STATUS_ENUME.CREATED.getValue(), LEAD_STATUS_ENUME.CREATED.getDesc(),
                            dealer, null, null);
                    if (LEAD_STATUS_ENUME.SEND.getValue().equals(leadStatus)) {
                        leadCommonService.insertLeadHistory(leadId, new Date(), currentUserId, LEAD_STATUS_ENUME.SEND.getValue(), LEAD_STATUS_ENUME.SEND.getDesc(),
                                dealer, null, null);
                    }
                    if (LEAD_STATUS_ENUME.SAME_LIST.getValue().equals(leadStatus)) {
                        leadCommonService.insertLeadHistory(leadId, new Date(), currentUserId, LEAD_STATUS_ENUME.SAME_LIST.getValue(), LEAD_STATUS_ENUME.SAME_LIST.getDesc(),
                                dealer, null, null);
                    }
                    if (LEAD_STATUS_ENUME.REJECT.getValue().equals(leadStatus)) {
                        leadCommonService.insertLeadHistory(leadId, new Date(), currentUserId, LEAD_STATUS_ENUME.REJECT.getValue(), LEAD_STATUS_ENUME.REJECT.getDesc(),
                                dealer, null, null);
                    }

                    //线索热度次数
                    Integer leadHeatingCount = leadSearchMapper.getLeadHeatingCount(leadId);//获取热度次数
                    logger.info("CZJ 原热度次数："+leadHeatingCount);
                    if(leadHeatingCount == 0){
                        Integer count = leadSearchMapper.selectDefeatAndInvalidLeadCount(dealer, phone);//查询除了赢单之外的线索总数量
                        logger.info("CZJ 经销商编号："+dealer+"号码："+phone+"原热度次数："+count);
                        leadHeatingCount=count+1;
                    } else{
                        leadHeatingCount=leadHeatingCount+1;
                    }
                    for(int i=0;i< leads2.size();i++){//查询到的所有非赢单线索都加上热度次数
                        logger.info("CZJ 线索编号："+leads2.get(i).getId()+"热度次数："+leadHeatingCount);
                        leadSearchMapper.updateLeadToLeadHeatingCountById(leads2.get(i).getId(),leadHeatingCount);//修改热度次数
                    }
                    if (preLeadDTO.getImportFlag() != null && preLeadDTO.getImportFlag() == 1) {
                        logger.info("批量导入线索数据,手机号[{}],外呼任务状态[{}]", phone, preLeadDTO.getTaskStatus());
                        if (preLeadDTO.getTaskStatus() == 1) {
                            logger.info("批量导入数据[{}],外呼任务为新建，自动创建外呼任务", phone);
                            LeadTask leadTask = new LeadTask();
                            Date nextDate = DateUtils.dayOffset(now, 2);//48小时内3次联系不上
                            leadTask.setDueDatePlan(nextDate);
                            leadTask.setLeadId(leadId);
                            if (taskStatus == null) {
                                taskStatus = 1;
                            }
                            leadTask.setStatus(taskStatus);
                            leadTask.setTaskType(1);
                            leadTaskMapper.insert(leadTask);
                        }
                    } else {
                        //新建一个task(线索清洗)
                        if (LEAD_STATUS_ENUME.CREATED.getValue().equals(leadStatus) && createTask == 1) {
                            LeadTask leadTask = new LeadTask();
                            Date nextDate = DateUtils.dayOffset(now, 2);//48小时内3次联系不上
                            leadTask.setDueDatePlan(nextDate);
                            leadTask.setLeadId(leadId);
                            if (taskStatus == null) {
                                taskStatus = 1;
                            }
                            leadTask.setStatus(taskStatus);
                            leadTask.setTaskType(1);
                            leadTaskMapper.insert(leadTask);
                        }
                    }
                    createFlag = true;
                } else {
                    logger.info("存在重复线索,线索ID:[{}]", lead.getId());
                    status = 1;
                    leadId = lead.getId();

                    logger.info("合并线索,合并Lead:[{}]", JSON.toJSONString(preLeadDTO));
                    logger.info("合并线索,合并前Lead:[{}]", JSON.toJSONString(lead));
                    BeanUtils.copyPropertiesHasValue(preLeadDTO, lead);

                    String memo = lead.getDescription();
                    if (!StringUtils.isEmpty(memo) && memo != null && !StringUtils.isEmpty(preLeadDTO.getMemo())) {
                        lead.setDescription(lead.getDescription() + ";" + preLeadDTO.getMemo());
                    } else if ((StringUtils.isEmpty(memo) || memo == null) && !StringUtils.isEmpty(preLeadDTO.getMemo())) {
                        lead.setDescription(preLeadDTO.getMemo());
                    }

                    if (!StringUtils.isEmpty(preLeadDTO.getSeriesName()) && preLeadDTO.getSeriesName() != null && !preLeadDTO.getSeriesName().equals(lead.getSeriesName())) {
                        CarSeriesDTO carSeriesByCode = carService.getCarSeriesByCode(preLeadDTO.getSeriesName());
                        if (StringUtils.isEmpty(lead.getDescription()) || lead.getDescription() == null) {
                            if (carSeriesByCode != null) {
                                lead.setDescription("合并车型线索 " + carSeriesByCode.getSeriesName());
                            } else {
                                lead.setDescription("合并车型线索 " + preLeadDTO.getSeriesName());
                            }
                        } else {
                            if (carSeriesByCode != null) {
                                lead.setDescription(lead.getDescription() + ";合并车型线索 " + carSeriesByCode.getSeriesName());
                            } else {
                                lead.setDescription(lead.getDescription() + ";合并车型线索 " + preLeadDTO.getSeriesName());
                            }
                        }
                    }
                    lead.setModifyBy(preLeadDTO.getModifyBy());
                    logger.info("合并线索,合并后:[{}]", JSON.toJSONString(lead));
                    lead.setId(leadId);
                    lead.setCustId(custId);
                    lead.setModifyDate(now);
                    logger.info("获取线索热度...");
                    Integer rating = getLeadRing(leadFrequency, lead);
                    if (lead.getRating() == null) {
                        lead.setRating(rating);
                    }
                    if (lead.getLevel() == null) {
                        lead.setLevel(level);
                    }
                    leadMapper.updateByPrimaryKeySelective(lead);
                }


            }
            Integer num = 0;
            /**关联lead*/
            entity.setLeadId(leadId);
            entity.setStatus(status);
            entity.setChannelCode(channelCode);
            entity.setCampaignCode(campaignCode);
            entity.setActivityCode(activityCode);
            entity.setCustomerCarDate(now);
            if (entity.getCollectTime() == null) {
                entity.setCollectTime(now);
            }
            logger.info("CZJ entity中线索编号是："+leadId+"，渠道编码："+channelCode+",时间："+now);
            preLeadMapper.insertSelective(entity);
            logger.info("CZJ preLead新增成功：Id = "+entity.getId());
        }
        return ResponseData.success(true);
    }

    public static void main(String[] args) {
        PreLeadDTO preLeadDTO = new PreLeadDTO();
        preLeadDTO.setFirstName("xxxx");
        System.out.println("合并线索,合并Lead:==="+JSON.toJSONString(preLeadDTO));
        Lead lead = new Lead();
        lead.setId(1);
        System.out.println("合并线索,合并前Lead:===="+JSON.toJSONString(lead));
        BeanUtils.copyPropertiesHasValue(preLeadDTO,lead);
        System.out.println("合并线索,合并后:===="+JSON.toJSONString(lead));
    }
//    1	7日内
//    2	15日内
//    3	一个月内
//    4	三个月内
//    5	半年内
//    6	一年内
//    7	一年以上
//    O	赢单
//    P	报价
//    H	7日内
//    A	1个月内
//    B	3个月内
//    C	3个月以上


    private String getLevelByPurchaseTime(String purchaseTime){
        String level = null;
        if(purchaseTime!=null){
            switch (purchaseTime){
                case "1":level="4";break;
                case "2":level="3";break;
                case "3":level="3";break;
                case "4":level="2";break;
                case "5":level="1";break;
                case "6":level="1";break;
                case "7":level="1";break;
            }
        }
        return level;
    }

    /**
     * 线索热度
     * @param leadFrequency
     * @param lead
     * @return
     */
    private Integer getLeadRing(Integer leadFrequency,Lead lead){
        Integer num1;//咨询次数评分
        switch (leadFrequency){
            case 1:num1=0;break;
            case 2:num1=5;break;
            default:num1=10;
        }
        Integer num2=0;//预购日期得分 三个月内 10 半年内 6 一年内 2 一年以上 0
        if(!StringUtils.isEmpty(lead.getPurchaseTime()) && NumberUtils.isDigits(lead.getPurchaseTime())) {
            switch (Integer.parseInt(lead.getPurchaseTime())) {
                case 1:break;
                case 2:break;
                case 3:break;
                case 4:num2=10;break;
                case 5:num2=6;break;
                case 6:num2=2;break;
                default:num2=0;
            }
        }
        Integer num3=(lead.getModelName()==null?0:10);//意向车型 有值 10,无值 0
        Integer num4=(lead.getBudget()==null?0:10);//客户预算 有值 10,无值 0
        double num = (num1+num2+num3+num4)/4.0d; //信息权重：各占25%
        return num<6?3:(num>8?1:2); // >8 高热线索 6—8 中热线索 <6 低热线索
    }
    /**工单转线索*/
    //@RabbitListener(queues = "REQUEST_CASES_TO_LEAD", containerFactory="rabbitListenerContainerFactory")
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "REQUEST_CASES_TO_LEAD", durable = "true"),
            exchange = @Exchange("")), containerFactory="rabbitListenerContainerFactory")
    public void processLead(@Payload String msg) {
        logger.info("收到工单转线索信息:" + msg);
        PreLeadDTO preLeadDTO = JSON.parseObject(msg,PreLeadDTO.class);

        ResponseData responseData = leadCommonService.validateLeadInfo(preLeadDTO);
        if(responseData.getData()==null){
            logger.error("工单数据--异常数据:"+responseData.getMessage()+"===="+msg);
            this.rabbitTemplate.convertAndSend("error_lead",msg);
            return ;
        }
        CasesToLeadDTO casesToLeadDTO = JSON.parseObject(msg, CasesToLeadDTO.class);
        if(casesToLeadDTO.getCustId()!=null){
            Customer customer = customerMapper.selectByPrimaryKey(casesToLeadDTO.getCustId());
            preLeadDTO.setFirstName(customer.getFirstName());
        }
        preLeadDTO.setChannelCode(selfChannelCode);
        if(casesToLeadDTO.getDealer()!=null){
            preLeadDTO.setDealer(String.valueOf(casesToLeadDTO.getDealer()));
        }
        /**再进入线索池*/
        int priority=9;
        preLeadDTO.setImportFlag(0);
        rabbitTemplate.convertAndSend(null,"REQUEST_TO_LEAD",JSON.toJSONString(preLeadDTO),new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setPriority(priority);
                return message;
            }
        });
    }

    /**线索池*/
    //@RabbitListener(queues = "REQUEST_TO_LEAD", containerFactory="rabbitListenerContainerFactory")
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "REQUEST_TO_LEAD", durable = "true"),
            exchange = @Exchange("")), containerFactory="rabbitListenerContainerFactory")
    public void processFinal(@Payload String msg) {
        boolean lock = false;
        PreLeadDTO preLeadDTO = JSON.parseObject(msg,PreLeadDTO.class);
        String LEADLOCK = "request_to_lead_locked_"+preLeadDTO.getPhone();
        String leadInfo = String.format("[%s:%s:%s]",preLeadDTO.getPhone(),preLeadDTO.getDealer(),preLeadDTO.getChannelCode());
        try {
            /**获取锁*/
            lock = stringRedisTemplate.opsForValue().setIfAbsent(LEADLOCK, "线索去重");
            /**设置失效时间*/
            stringRedisTemplate.expire(LEADLOCK,5, TimeUnit.MINUTES);
            logger.debug("线索池"+leadInfo+"是否获取到锁:" + lock);
            if (lock) {
                logger.info("线索池收到线索信息:" + msg);
                create(preLeadDTO);
            }else{

            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            stringRedisTemplate.delete(LEADLOCK);
            if (lock) {
                rabbitTemplate.convertAndSend("REQUEST_TO_LEAD_ERROR",msg);
//                throw new RuntimeException(e.getMessage());
            }
        } finally {
            if (lock) {// 如果获取了锁，则释放锁
                stringRedisTemplate.delete(LEADLOCK);
                logger.debug("任务结束，释放锁!");
            }
        }
        if (!lock) {
            throw new RuntimeException("服务并存"+leadInfo);
        }
    }


    // @RabbitListener(queues = "IFS_CRM_DCC_LEADS_INPUT_QUEUE", containerFactory="rabbitListenerContainerFactory")
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "IFS_CRM_DCC_LEADS_INPUT_QUEUE", durable = "true"),
            exchange = @Exchange("")), containerFactory="rabbitListenerContainerFactory")
    public void dccQueue(@Payload String msg) {
        logger.info("收到DCC线索信息:" + msg);
        DccLead dccLead = JSON.parseObject(msg,DccLead.class);
        PreLeadDTO preLeadDTO = new PreLeadDTO();
        preLeadDTO.setProvince(dccLead.getProvinceId()==null?null:dccLead.getProvinceId().toString());
        preLeadDTO.setCity(dccLead.getCityId()==null?null:dccLead.getCityId().toString());
        preLeadDTO.setSubCity(dccLead.getCountyId()==null?null:dccLead.getCountyId().toString());
        preLeadDTO.setFirstName(dccLead.getName());
        preLeadDTO.setPhone(dccLead.getPhone());
        preLeadDTO.setDealer(dccLead.getDealerId());
        preLeadDTO.setModelName(dccLead.getModelId());
        preLeadDTO.setSeriesName(dccLead.getSeriesId());
        String createTime = dccLead.getCreateTime();
        if(createTime !=null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                preLeadDTO.setCollectTime(sdf.parse(createTime));
            } catch (ParseException e) {
                logger.error("手机时间转换错误");
            }
        }
        String campaignId = dccLead.getCampaignId();
        if(!StringUtils.isEmpty(campaignId)) {
            ActinstanceChannelDTO actinstanceChannel = channelService.getActinstanceChannelByCampaignCode(campaignId);
            if(actinstanceChannel!=null) {
                preLeadDTO.setCampaignCode(campaignId);
                preLeadDTO.setCampaignId(campaignId);
                preLeadDTO.setChannelCode(actinstanceChannel.getChannelCode());
                preLeadDTO.setActivityCode(actinstanceChannel.getActCode());
            }else{
                logger.error("DCC线索-渠道数据有误,campaignId="+campaignId);
            }
        }
        preLeadDTO.setResourceId(dccLead.getLeadsId()==null?null:dccLead.getLeadsId().toString());
        String gender = dccLead.getGender();
        if(gender!=null){
            Integer sex = null;
            if(NumberUtils.isDigits(gender)){
                sex = Integer.parseInt(gender);
            }
            preLeadDTO.setGender(sex);
        }
        if(!StringUtils.isEmpty(dccLead.getSalesName()) || !StringUtils.isEmpty(dccLead.getSalesPhone())){
            preLeadDTO.setMemo(dccLead.getSalesName()+(StringUtils.isEmpty(dccLead.getSalesPhone())?"":("/"+dccLead.getSalesPhone())));
        }
        preLeadDTO.setEmail(dccLead.getMail());
        preLeadDTO.setModelName(dccLead.getModelId()==null?null:dccLead.getModelId().toString());

        ResponseData responseData = leadCommonService.validateLeadInfo(preLeadDTO);
        if(responseData.getData()==null){
            logger.error("DCC数据--异常数据:["+responseData.getMessage()+"]======="+msg);
            this.rabbitTemplate.convertAndSend("error_lead",msg);
            return ;
        }
        rabbitTemplate.convertAndSend("REQUEST_TO_LEAD",JSON.toJSONString(preLeadDTO));
    }

    //@RabbitListener(queues = "TEST_DRIVER", containerFactory="rabbitListenerContainerFactory")
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "TEST_DRIVER", durable = "true"),
            exchange = @Exchange("")), containerFactory="rabbitListenerContainerFactory")
    public void testDrive(@Payload String msg) {
        logger.info("收到试驾线索信息:" + msg);

        TestDriverRequestDTO testDrive = null;
        try {
            testDrive = JSON.parseObject(msg, TestDriverRequestDTO.class);
        }catch(Exception e){
            logger.error("格式错误:",e);
            return;
        }
        PreLeadDTO preLeadDTO = new PreLeadDTO();
        preLeadDTO.setProvince(testDrive.getProvinceId()==null?null:testDrive.getProvinceId());
        preLeadDTO.setCity(testDrive.getCityId()==null?null:testDrive.getCityId());
        preLeadDTO.setSubCity(testDrive.getDistrictId());
        preLeadDTO.setFirstName(testDrive.getUsername());
        preLeadDTO.setPhone(testDrive.getMobile());
        preLeadDTO.setDealer(testDrive.getDealers());
        preLeadDTO.setChannelCode(testDrive.getChannelCode());
        preLeadDTO.setActivityCode(testDrive.getActCode());
        preLeadDTO.setCampaignCode(testDrive.getSiteCode());
        preLeadDTO.setSeriesName(testDrive.getExeedType());
        preLeadDTO.setResourceId("TD_"+testDrive.getId());
        preLeadDTO.setCollectTime(testDrive.getCreateTime());

        String gender = testDrive.getGender();
        if(gender!=null || NumberUtils.isDigits(gender)){
            preLeadDTO.setGender(Integer.parseInt(gender));
        }
        preLeadDTO.setEmail(testDrive.getEmail());
        preLeadDTO.setCallReason(testDrive.getLeaveWords());

        ResponseData responseData = leadCommonService.validateLeadInfo(preLeadDTO);
        if(responseData.getData()==null){
            logger.error("预约试驾--异常数据:"+responseData.getMessage()+"===="+msg);
            this.rabbitTemplate.convertAndSend("error_lead",msg);
            return ;
        }
        rabbitTemplate.convertAndSend("REQUEST_TO_LEAD",JSON.toJSONString(preLeadDTO));
    }

    private Map<String,Integer> getMetaValueMap(String metaName){
        ResponseData<List<MetaDataDTO>> metaData = metaService.getLeadMetaList(metaName,null);

        List<MetaDataDTO> metaList = metaData.getData();
        Map<String,Integer> map = new HashMap<>();
        for(MetaDataDTO vo:metaList){
            if(map.get(vo.getDescription())!=null){
                continue;
            }
            map.put(vo.getDescription(),vo.getMetaCode());
        }
        return map;
    }

    private Map<String,String> getSeriesMap(){
        return null;
    }

    private Map<String,String> getModelMap(){
        return null;
    }

    @Override
    @RequestMapping( value = {"import"},method = {RequestMethod.POST})
    public ResponseData<String> importPreLead(@RequestBody ImportDTO importDTO) throws Exception{
        List<List<String>> dataList = importDTO.getReslutData();
        if (dataList==null || dataList.isEmpty()) {
            return ResponseData.fail("导入失败，导入数据不存在！");
        }
        logger.info("线索池信息导入开始，导入文件名："+importDTO.getFileName());
        List<PreLeadImportDTO> preLeadList = new ArrayList<>();
        //线索级别{description,meta_code}
        //Map<String, Integer> levelMap = this.getMetaValueMap("lead_level");
        Map<String, Integer> budgetMap = this.getMetaValueMap("lead_budget");
        Map<String, Integer> purchaseTimeMap = this.getMetaValueMap("lead_purchase_time");
        Map<String, Integer> colorMap = this.getMetaValueMap("lead_color");
        Map<String, Integer> taskStatusMap = this.getMetaValueMap("lead_task_status");
        Map<String,String> provinceMap = new HashMap<>();
        Map<String,String> cityMap = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        List<CarSeriesDTO> carSeriesList = carService.getCarSeriesList();
        Map<String,String> seriesMap = new HashMap<>();
        Map<String,String> modelMap = new HashMap<>();
        for(CarSeriesDTO obj:carSeriesList){
            String seriesCode = obj.getSeriesCode();
            seriesMap.put(obj.getSeriesName(), seriesCode);
            List<CarModelDTO> carModelList = carService.getCarModelList(seriesCode);
            for(CarModelDTO model:carModelList){
                modelMap.put(seriesCode+"_"+model.getModelName(),model.getModelCode());
            }
        }
        Map<String,ActinstanceChannelDTO> campaignMap = new HashMap<>();
        Map<String,String> dealerMap = new HashMap<>();
        Map<String,String> channelMap = new HashMap<>();
        List<String> msgList = new ArrayList<>();
        Map<String,Integer> phoneMap = new HashMap<>();
        List<String> phoneList = new ArrayList<>();
        Integer currentUserId = SessionHelper.getCurrentUserId();
        for (int i=1;i<dataList.size();i++){//sheet内容 从第二行开始1
            List<String> data = dataList.get(i);
            PreLeadImportDTO preLeadDTO = new PreLeadImportDTO();
            String name  = data.get(0);
            preLeadDTO.setFirstName(name);
            if(StringUtils.isEmpty(name)){
                msgList.add("第["+(i+1)+"]行姓名为空");
                logger.info("第["+(i+1)+"]行姓名为空[{}]",name);
            }
            String phone  = data.get(1);
            if(StringUtils.isEmpty(phone)){
                msgList.add("第["+(i+1)+"]行手机号为空");
                logger.info("第["+(i+1)+"]行手机号为空[{}]",phone);
            }
            if(!StringUtils.isEmpty(phone) ){
                if(phone.length() != 11){
                    msgList.add("第["+(i+1)+"]行手机号长度不能超过11位");
                    logger.info("第["+(i+1)+"]行手机号长度不合法[{}]",phone);
                }
            }
            Integer cnt = phoneMap.get(phone);
            if(cnt==null){
                cnt=0;
            }
            phoneMap.put(phone,++cnt);
            if(cnt>1) {
                if (!phoneList.contains(phone)) {
                    phoneList.add(phone);
                }
            }
            preLeadDTO.setPhone(phone);
            String sex  = data.get(2);
            Integer gender = null;
            if(!StringUtils.isEmpty(sex)) {
                if ("男".equals(sex)) {
                    gender = 1;
                } else if ("女".equals(sex)) {
                    gender = 2;
                } else if ("未知".equals(sex)) {
                    gender = 3;
                }else{
                    msgList.add("第["+(i+1)+"]行性别填写错误");
                    logger.info("第["+(i+1)+"]行性别填写错误[{}]",sex);
                }
            }else{
                msgList.add("第["+(i+1)+"]行性别为空");
                logger.info("第["+(i+1)+"]行性别为空[{}]");
            }
            preLeadDTO.setGender(gender);
            String seriesName  = data.get(3);
            String modelName  = data.get(4);
            String seriesCode = null;
            if(!StringUtils.isEmpty(seriesName)){
                String code = seriesMap.get(seriesName);
                if(!StringUtils.isEmpty(code)){
                    preLeadDTO.setSeriesName(code);
                    if(!StringUtils.isEmpty(modelName)){
                        String modelKey = code+"_"+modelName;
                        String modelCode = modelMap.get(modelKey);
                        if(modelCode!=null){
                            preLeadDTO.setModelName(modelCode);
                        }else{
                            msgList.add("第["+(i+1)+"]行意向版型填写有误");
                            logger.info("第["+(i+1)+"]行意向版型填写有误[{}]",modelName);
                        }
                    }
                }else{
                    msgList.add("第["+(i+1)+"]行车型填写有误");
                    logger.info("第["+(i+1)+"]行车型[{}]填写有误",seriesCode);
                }
            }else if(!StringUtils.isEmpty(modelName)){
                msgList.add("第["+(i+1)+"]行车型/意向版型填写有误");
                logger.info("第["+(i+1)+"]行车型[{}]/意向版型[{}]填写有误",seriesName,modelName);
            }
            String colorName  = data.get(5);
            if(!StringUtils.isEmpty(colorName)) {
                Integer color = colorMap.get(colorName);
                if(color!=null) {
                    preLeadDTO.setColorName(String.valueOf(color));
                }else{
                    msgList.add("第["+(i+1)+"]行外观颜色填写错误");
                    logger.info("第["+(i+1)+"]行外观颜色填写错误[{}]",color);
                }
            }
            //客户预算
            String budgetStr  = data.get(6);
            if(!StringUtils.isEmpty(budgetStr)) {
                Integer budget = budgetMap.get(budgetStr);
                if (budget != null) {
                    preLeadDTO.setBudget(String.valueOf(budget));
                }else{
                    msgList.add("第["+(i+1)+"]行购车预算填写错误");
                    logger.info("第["+(i+1)+"]行购车预算填写错误[{}]",budgetStr);
                }
            }
            //预购日期
            String purchaseTimeDesc  = data.get(7);
            if(!StringUtils.isEmpty(purchaseTimeDesc)) {
                Integer purchaseTime = purchaseTimeMap.get(purchaseTimeDesc);
                if (purchaseTime != null) {
                    preLeadDTO.setPurchaseTime(String.valueOf(purchaseTime));
                }else{
                    msgList.add("第["+(i+1)+"]行预购时间填写错误");
                    logger.info("第["+(i+1)+"]行预购时间填写错误[{}]",purchaseTimeDesc);
                }
            }/**
             *预购日期暂无,去掉
             *else{
             msgList.add("第["+(i+1)+"]行预购时间为空");
             logger.info("第["+(i+1)+"]行预购时间为空");
             }*/
            String province  = data.get(8);
            if(!StringUtils.isEmpty(province)&&!"null".equals(province.trim())){
                String key=province+"_1";
                String provinceId = provinceMap.get(key);
                if(provinceId==null){
                    Region region = regionService.getRegionByNameAndType(1,province);
                    if(region!=null) {
                        provinceId = String.valueOf(region.getRegionId());
                        provinceMap.put(key, provinceId);
                    }
                }
                if(provinceId!=null){
                    preLeadDTO.setProvince(provinceId);
                }else{
                    msgList.add("第["+(i+1)+"]行省份名称填写错误");
                    logger.info("第["+(i+1)+"]行省份填写错误[{}]",province);
                }
            }
            String city  = data.get(9);
            if(!StringUtils.isEmpty(city)&& !"null".equals(city.trim())){
                city = city.trim();
                String key=city+"_2";
                String cityId = cityMap.get(key);
                String provId = provinceMap.get(key+"_1");
                if(cityId==null){
                    Region region = regionService.getRegionByNameAndType(2,city);
                    if(region!=null) {
                        cityId = String.valueOf(region.getRegionId());
                        cityMap.put(key, cityId);
                        provId = String.valueOf(region.getParentRegionId());
                        provinceMap.put(key+"_1",provId);
                    }
                }
                if(cityId!=null){
                    preLeadDTO.setCity(cityId);
                    preLeadDTO.setProvince(provId);
                }else{
                    msgList.add("第["+(i+1)+"]行城市名称填写错误");
                    logger.info("第["+(i+1)+"]行城市填写错误[{}]",city);
                }
            }
            String collectTimeStr = data.get(11);
            if(!StringUtils.isEmpty(collectTimeStr)){
                collectTimeStr = collectTimeStr.replace("-","/").replace(".","/");
                Date collectTime = null;
                try {
                    collectTime = sdf.parse(collectTimeStr);
                    preLeadDTO.setCollectTime(collectTime);
                }catch (ParseException e){
                    logger.info("线索导入-日期格式异常[{}]",collectTimeStr);
                }
                if(collectTime==null){
                    msgList.add("第["+(i+1)+"]行收集时间填写错误");
                }
            }
            /**用户诉求*/
            String callReason = data.get(12);
            preLeadDTO.setCallReason(callReason);
            String taskStatusStr = data.get(13);
            if(!StringUtils.isEmpty(taskStatusStr)){
                Integer status = taskStatusMap.get(taskStatusStr);
                if(status!=null){
                    preLeadDTO.setTaskStatus(status);
                }else{
                    msgList.add("第["+(i+1)+"]行最终拨打状态填写错误");
                    logger.info("第["+(i+1)+"]行最终拨打状态错误[{}]",taskStatusStr);
                }
            }else{
                msgList.add("第["+(i+1)+"]行最终拨打状态填写错误");
                logger.info("第["+(i+1)+"]行最终拨打状态错误[{}]",taskStatusStr);
            }
            String campaignId = data.get(14);
            String channelCode = data.get(15);
            if(!StringUtils.isEmpty(campaignId)){
                ActinstanceChannelDTO campaign = campaignMap.get(campaignId);
                if(campaign==null) {
                    campaign = channelService.getActinstanceChannelByCampaignCode(campaignId);
                    if (campaign != null) {
                        campaignMap.put(campaignId, campaign);
                    }
                }
                if(campaign!=null){
                    preLeadDTO.setActivityCode(campaign.getActCode());
                    preLeadDTO.setCampaignCode(campaign.getCampaignCode());
                    preLeadDTO.setCampaignId(campaignId);
                    preLeadDTO.setChannelCode(campaign.getChannelCode());
                }else{
                    msgList.add("第["+(i+1)+"]行Campign ID填写错误");
                    logger.info("第["+(i+1)+"]行Campign ID错误[{}]",campaignId);
                }
            }else if(!StringUtils.isEmpty(channelCode)){
                String code = channelMap.get(channelCode);
                if(code==null) {
                    ChannelDTO channel = channelService.getChannelByCode(channelCode);
                    if(channel!=null){
                        channelMap.put(channelCode,channel.getCode());
                        code = channelCode;
                    }
                }
                if(!StringUtils.isEmpty(code)){
                    preLeadDTO.setChannelCode(code);
                }else{
                    msgList.add("第["+(i+1)+"]行渠道CODE填写错误");
                    logger.info("第["+(i+1)+"]行渠道CODE错误[{}]",channelCode);
                }
            }else{
                msgList.add("第["+(i+1)+"]行Campign ID或渠道code必填");
                logger.info("第["+(i+1)+"]行Campign ID或渠道code必填");
            }
            /**备注*/
            String description  = data.get(16);
            if(!StringUtils.isEmpty(description)){
                preLeadDTO.setDescription(description);
            }
            /**经销商code*/
            String dealerCode  = data.get(17);
            if(!StringUtils.isEmpty(dealerCode)){
                String code = dealerMap.get(dealerCode);
                if(code==null) {
                    DealerDTO dealerDTO = dealerService.detailsDealer(dealerCode);
                    if(dealerDTO!=null){
                        dealerMap.put(dealerCode,dealerDTO.getDealerCode());
                        code = dealerDTO.getDealerCode();
                    }
                }
                if(code!=null){
                    preLeadDTO.setDealer(code);
                }else{
                    msgList.add("第["+(i+1)+"]行经销商编号填写错误");
                    logger.info("第["+(i+1)+"]行经销商编号错误[{}]",dealerCode);
                }
            }
            preLeadDTO.setCreatedBy(currentUserId);
            //手动导入线索标识
            preLeadDTO.setImportFlag(1);
            if(msgList.size()==0) {
                preLeadList.add(preLeadDTO);
            }
        }
        if(phoneList.size()>0){
            msgList.add("手机号重复"+phoneList);
            logger.info("手机号重复"+phoneList);
        }
        if(msgList.size()>0) {
            preLeadList.clear();
        }
        if (msgList.size()>0){
            logger.info("线索池信息导入失败.");
            return ResponseData.fail(msgList.toString());
        }
        if(preLeadList!=null && preLeadList.size()>0) {
            for (PreLeadImportDTO obj : preLeadList) {
                rabbitTemplate.convertAndSend("REQUEST_TO_LEAD",JSON.toJSONString(obj));
            }
        }
        logger.info("线索池信息结束，导入成功！");
        return ResponseData.success("导入成功");
    }

    @Override
    @RequestMapping( value = {"/import/deal"},method = {RequestMethod.GET})
    public void sendLead2PreLead(){
        List<PreLeadImport> preLeadImportList = preLeadImportMapper.getAllPreLeadImportList();
        if(preLeadImportList!=null && preLeadImportList.size()>0){
            logger.info("导入线索数量:"+preLeadImportList.size());
            Map<String,ActinstanceChannelDTO> campaignMap = new HashMap<>();
            for(PreLeadImport obj:preLeadImportList){
                String campaignId = obj.getCampaignId();
                if(!StringUtils.isEmpty(campaignId)){
                    ActinstanceChannelDTO campaign = campaignMap.get(campaignId);
                    if(campaign==null) {
                        campaign = channelService.getActinstanceChannelByCampaignCode(campaignId);
                        if (campaign != null) {
                            campaignMap.put(campaignId, campaign);
                        }
                    }
                    if(campaign!=null){
                        obj.setActivityCode(campaign.getActCode());
                        obj.setCampaignCode(campaign.getCampaignCode());
                        obj.setCampaignId(campaignId);
                        obj.setChannelCode(campaign.getChannelCode());
                    }
                }
                rabbitTemplate.convertAndSend("REQUEST_TO_LEAD",JSON.toJSONString(obj));
            }
            preLeadImportMapper.deletePreLeadImport();
        }
    }
}
