package com.chery.exeed.crm.lead.service;

import com.alibaba.fastjson.JSON;
import com.chery.exeed.crm.common.constants.Constants;
import com.chery.exeed.crm.common.dto.ResponseData;
import com.chery.exeed.crm.common.service.CarService;
import com.chery.exeed.crm.common.util.BeanUtils;
import com.chery.exeed.crm.common.util.SessionHelper;
import com.chery.exeed.crm.common.util.StringUtils;
import com.chery.exeed.crm.lead.dto.CustomerDTO;
import com.chery.exeed.crm.lead.dto.PreLeadDTO;
import com.chery.exeed.crm.lead.dto.SysUserOperationAuditlog;
import com.chery.exeed.crm.lead.mapper.*;
import com.chery.exeed.crm.lead.model.Customer;
import com.chery.exeed.crm.lead.model.LeadChannel;
import com.chery.exeed.crm.lead.model.LeadHistory;
import com.chery.exeed.crm.lead.model.PreLead;
import com.chery.exeed.crm.sysadmin.dto.UserDetailDTO;
import com.chery.exeed.crm.sysadmin.service.SysUserService;
import com.chery.exeed.ifs.common.dto.CarModelDTO;
import com.chery.exeed.ifs.common.dto.CarSeriesDTO;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.servicecomb.provider.pojo.RpcReference;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

/**
 * Author:xiaowei.zhu
 * 2019/1/31 16:39
 */
@Transactional
@RestSchema(schemaId = "lead-common-service")
@RequestMapping(path = "/apis/lead/common")
public class LeadCommonServiceImpl implements LeadCommonService {
    @Autowired
    public RabbitTemplate rabbitTemplate;
    @Autowired
    private LeadHistoryMapper leadHistoryMapper;
    @Autowired
    private CustomerSearchMapper customerSearchMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private CarService carService;
    @Autowired
    private PreLeadSearchMapper preLeadSearchMapper;
    @Autowired
    private PreLeadMapper preLeadMapper;
    @Autowired
    private LeadSearchMapper leadSearchMapper;

    @Autowired
    private LeadChannelMapper leadChannelMapper;
    @Autowired
    private LeadChannelSearchMapper leadChannelSearchMapper;

    @RpcReference(microserviceName = "exeed-crm-apis:exeed-crm-sysadmin", schemaId = "user-service")
    private SysUserService userService;
    /**记录修改日志*/
    @Override
    public void createLog(Object newObject, Object oldObject, String title, Date now){
        SysUserOperationAuditlog sysUserOperationAuditlog = new SysUserOperationAuditlog();
        sysUserOperationAuditlog.setBusinessScope(title);
        sysUserOperationAuditlog.setNewValue(JSON.toJSONString(newObject));
        sysUserOperationAuditlog.setOldValue(JSON.toJSONString(oldObject));
        sysUserOperationAuditlog.setOperationTime(now);
        Integer currentUserId = SessionHelper.getCurrentUserId();
        sysUserOperationAuditlog.setUserId(currentUserId);
        sysUserOperationAuditlog.setUsername(SessionHelper.getCurrentUserName());
        rabbitTemplate.convertAndSend("OPER_AUDITLOG_QUEUE",JSON.toJSONString(sysUserOperationAuditlog));
    }

    @Override
    @RequestMapping(value = "/getSeriesList",method = {RequestMethod.GET})
    public ResponseData<List<CarSeriesDTO>> getSeriesList(){
        return ResponseData.success(carService.getCarSeriesList());
    }

    @Override
    @RequestMapping(value = "/getModelList",method = {RequestMethod.GET})
    public ResponseData<List<CarModelDTO>> getModelList(String seriesCode){
        return ResponseData.success(carService.getCarModelList(seriesCode));
    }


    @Override
    public String getUserName(Integer userId){
        if(userId!=null){
            ResponseData<String> user = userService.getUserNameById(userId);
            return user.getData();
        }
        return null;
    }

    @Override
    public UserDetailDTO getUserInfo(Integer currentUserId){
        ResponseData<UserDetailDTO> sysUserData = userService.monitorInfo(currentUserId);
        return sysUserData.getData();
    }

    @Override
    public List<String> getUserDealerList(Integer currentUserId){
        ResponseData<UserDetailDTO> sysUserData = userService.monitorInfo(currentUserId);
        List<String> list = new ArrayList<>();
        list.add(sysUserData.getData().getDealerId());
        return list;
    }

    @Override
    public Integer getUserType(Integer currentUserId){
        ResponseData<UserDetailDTO> sysUserData = userService.monitorInfo(currentUserId);
        return sysUserData.getData().getUserType();
    }

    /**
     * 线索状态更新历史
     * @param leadId
     * @param now
     * @param currentUserId
     * @param status
     * @param statusDesc
     */
    @Override
    @RequestMapping(value = "/insertLeadHistory",method = {RequestMethod.GET})
    @Transactional(rollbackFor = Exception.class,transactionManager = "leadTransactionManager")
    public void insertLeadHistory(Integer leadId,
                                  Date now,
                                  Integer currentUserId,
                                  Integer status,
                                  String statusDesc,
                                  String dealer,
                                  Integer orderManager,
                                  Integer owner){
        LeadHistory leadHistory = new LeadHistory();
        leadHistory.setLeadId(leadId);
        leadHistory.setStatus(status);
        leadHistory.setStatusDesc(statusDesc);
        leadHistory.setCreateTime(now);
        leadHistory.setCreateBy(currentUserId);
        leadHistory.setDealer(dealer);
        leadHistory.setOrderManager(orderManager);
        leadHistory.setOwner(owner);
        leadHistoryMapper.insert(leadHistory);
    }

    @Override
    @RequestMapping(value = "/copyChannel2OldLead", method = {RequestMethod.GET})
    @Transactional(rollbackFor = Exception.class,transactionManager = "leadTransactionManager")
    public int copyChannel2OldLead(Integer oldLeadId,Integer newLeadId){
        //List<LeadChannel> leadChannelList = leadChannelSearchMapper.getChannelListByLeadId(newLeadId);
        List<PreLead> preLeads = preLeadSearchMapper.selectPreLeadByLeadId(newLeadId);
        if(preLeads==null){
            return 0;
        }
        int num = 0;
        for(PreLead obj:preLeads){
            obj.setId(null);
            obj.setLeadId(oldLeadId);
            obj.setStatus(1);
            obj.setResourceId("OLD"+newLeadId);
            num += preLeadMapper.insert(obj);
        }
        return num;
    }

    /**线索关联客户*/
    @Override
    @RequestMapping(value = "/setLead2Customer",method = {RequestMethod.GET})
    @Transactional(rollbackFor = Exception.class,transactionManager = "leadTransactionManager")
    public CustomerDTO setLead2Customer(PreLeadDTO lead, Date now, Integer currentUserId, Integer custId){
        CustomerDTO customer = new CustomerDTO();
        customer.setCustomerCarDate(lead.getCustomerCarDate());
        customer.setFirstName(lead.getFirstName());
        customer.setLastName(lead.getLastName());
        customer.setPhone(lead.getPhone());
        customer.setAge(lead.getAge());
        customer.setGender(lead.getGender());
        String province = lead.getProvince();
        String city = lead.getCity();
        String subCity = lead.getSubCity();
        if(province!=null && NumberUtils.isDigits(province)) {
            customer.setProvince(Integer.parseInt(province));
        }
        if(city!=null && NumberUtils.isDigits(city)) {
            customer.setCity(Integer.parseInt(city));
        }
        if(subCity!=null && NumberUtils.isDigits(subCity)) {
            customer.setSubcity(Integer.parseInt(subCity));
        }
        customer.setAddress(lead.getAddress());
        if(lead.getPurchaeFrequency()!=null && NumberUtils.isDigits(lead.getPurchaeFrequency())) {
            if(lead.getPurchaeFrequency().length()<4
                    ) {
                customer.setPurchaeFrequency(Integer.parseInt(lead.getPurchaeFrequency()));
            }
        }
        customer.setHobby(lead.getHobby());
        customer.setRevenueLevelDes(lead.getRevenueLevel());
        customer.setWechatOpenid(lead.getWechatOpenid());
        customer.setCustomerType(1);/**1个人客户2企业客户*/
        customer.setCompanyName(lead.getCompanyName());
        String phone = lead.getPhone();
        if (lead.getHouseholdRegistration() != null && NumberUtils.isDigits(lead.getHouseholdRegistration())) {
            customer.setHouseholdRegistration(Integer.parseInt(lead.getHouseholdRegistration()));
        }
        if (lead.getAutomotiveExpertise() != null && NumberUtils.isDigits(lead.getAutomotiveExpertise())) {
            customer.setAutomotiveExpertise(Integer.parseInt(lead.getAutomotiveExpertise()));
        }

        if (lead.getInteriorYearlyBudget() != null) {
            customer.setInteriorYearlyBudget(String.valueOf(lead.getInteriorYearlyBudget()));
        }
        if (lead.getTreasureCarLevel() != null && NumberUtils.isDigits(lead.getTreasureCarLevel())) {
            customer.setTreasureCarLevel(Integer.parseInt(lead.getTreasureCarLevel()));
        }
        if(customer.getVehicleNo()!=null && NumberUtils.isDigits(lead.getVehicleNo())) {
            customer.setVehicleNo(Integer.parseInt(lead.getVehicleNo()));
        }
        if(lead.getIsSpecialCustomer()!=null && NumberUtils.isDigits(lead.getIsSpecialCustomer())) {
            customer.setIsSpecialCustomer(Integer.parseInt(lead.getIsSpecialCustomer()));
        }
        customer.setSpecialCareComments(lead.getSpecialCareComments());
        if(lead.getCustomerCharacteristicsDes()!=null) {
            customer.setCustomerCharacteristicsDes(String.valueOf(lead.getCustomerCharacteristicsDes()));
        }
        if (lead.getIndustry() != null) {
            customer.setIndustry(String.valueOf(lead.getIndustry()));
        }
        if(lead.getMaritalStatus()!=null&& NumberUtils.isDigits(lead.getMaritalStatus())) {
            customer.setMaritalStatus(Integer.parseInt(lead.getMaritalStatus()));
        }
        if(lead.getRevenueLevel()!=null&& NumberUtils.isDigits(lead.getRevenueLevel())) {
            customer.setRevenueLevel(Integer.parseInt(lead.getRevenueLevel()));
        }
        if(lead.getDrivingSkill()!=null&& NumberUtils.isDigits(lead.getDrivingSkill())) {
            customer.setDrivingSkill(Integer.parseInt(lead.getDrivingSkill()));
        }
        if(lead.getEducationLevel()!=null&& NumberUtils.isDigits(lead.getEducationLevel())) {
            customer.setEducationLevel(Integer.parseInt(lead.getEducationLevel()));
        }
        if (lead.getCommunicationDifficulty() != null&& NumberUtils.isDigits(lead.getCommunicationDifficulty())) {
            customer.setCommunicationDifficulty(Integer.parseInt(lead.getCommunicationDifficulty()));
        }
        customer.setCompanyName(lead.getCompanyName());
        if(lead.getIfMarried()!=null && NumberUtils.isDigits(lead.getIfMarried())){
            customer.setIfMarried(Integer.parseInt(lead.getIfMarried()));
        }
        if(lead.getOccupationType()!=null && NumberUtils.isDigits(lead.getOccupationType())){
            customer.setOccupationType(Integer.parseInt(lead.getOccupationType()));
        }
        if(lead.getOwningCarAge()!=null && NumberUtils.isDigits(lead.getOwningCarAge())) {
            customer.setOwningCarAge(Integer.parseInt(lead.getOwningCarAge()));
        }
        if (lead.getFollowCar()!=null){
            customer.setFollowCar(lead.getFollowCar());
        }

        customer.setPersonTitle(lead.getPersontitle());
        customer.setConsumptionCharacteristics(lead.getConsumptionCharacteristics());
        customer.setRecomender(lead.getRecomender());
        customer.setRecomenderPhone(lead.getRecomenderPhone());
        customer.setOccupationPhone(lead.getOccupationPhone());
        customer.setPersonDonotcall(lead.getPersonDonotcall());
        customer.setPersonEmail(lead.getEmail());
        customer.setPersonDonotcall(lead.getPersonDonotcall());
        if(NumberUtils.isDigits(lead.getVehicleNo())) {
            customer.setVehicleNo(Integer.parseInt(lead.getVehicleNo()));
        }
        Customer entity;
        if(custId==null){
            entity = customerSearchMapper.getCustomerByPhone(phone,1);
        }else{
            entity = customerMapper.selectByPrimaryKey(custId);
        }
        Integer leadFrequency=1;
        if (entity == null) {//不存在则新建,存在则有值替换空值,旧值不做替换
            entity = new Customer();
            BeanUtils.copyProperties(customer, entity);
            entity.setCustomerCarDate(now);
            entity.setLeadFrequency(leadFrequency);
            entity.setCustomerStatus(Constants.LEAD_CUSTOMER_STATUS.POTENTIAL.getValue());
            entity.setCreatedBy(currentUserId);
            customerMapper.insert(entity);
        } else {
            BeanUtils.copyPropertiesHasValue(customer, entity);
            entity.setModifyDate(now);
            leadFrequency = entity.getLeadFrequency();
            leadFrequency=(leadFrequency==null?1:(leadFrequency+1));
            entity.setLeadFrequency(leadFrequency);
            entity.setModifyDate(now);
            if(currentUserId!=null) {
                entity.setModifyBy(currentUserId);
            }
            customerMapper.updateByPrimaryKeySelective(entity);

            this.createLog(entity,customer,"lead2customer",now,currentUserId,entity.getId());
        }
        customer.setId(entity.getId());
        customer.setLeadFrequency(leadFrequency);
        return customer;
    }

    @Override
    public void createLog(Object newObject, Object oldObject, String title, Date now, Integer currentUserId,Integer bizId){
        SysUserOperationAuditlog sysUserOperationAuditlog = new SysUserOperationAuditlog();
        sysUserOperationAuditlog.setBusinessScope(title);
        sysUserOperationAuditlog.setNewValue(JSON.toJSONString(newObject));
        sysUserOperationAuditlog.setOldValue(JSON.toJSONString(oldObject));
        sysUserOperationAuditlog.setOperationTime(now);
        sysUserOperationAuditlog.setUserId(currentUserId);
        sysUserOperationAuditlog.setSerialsNumber(bizId);
        sysUserOperationAuditlog.setUsername(SessionHelper.getCurrentUserName());
        rabbitTemplate.convertAndSend("OPER_AUDITLOG_QUEUE",JSON.toJSONString(sysUserOperationAuditlog));
    }

    @Override
    public ResponseData<Integer> validateLeadInfo(PreLeadDTO lead){
        if(!StringUtils.isEmpty(lead.getBrandName()) &&
                (!NumberUtils.isDigits(lead.getBrandName()) || lead.getBrandName().length()>10)){
            return ResponseData.fail("品牌数据有误");
        }
        if(!StringUtils.isEmpty(lead.getSeriesName()) &&
                (lead.getSeriesName().length()>20)){
            return ResponseData.fail("车型数据有误");
        }
        if(!StringUtils.isEmpty(lead.getModelName()) &&
                (lead.getModelName().length()>20)){
            return ResponseData.fail("车型数据有误");
        }
        if(!StringUtils.isEmpty(lead.getColorName()) &&
                (!NumberUtils.isDigits(lead.getColorName()) || lead.getColorName().length()>10)){
            return ResponseData.fail("颜色数据有误");
        }
        if(!StringUtils.isEmpty(lead.getFirstName()) && lead.getFirstName().length()>50){
            return ResponseData.fail("姓名不能超过50个字符");
        }
        if(!StringUtils.isEmpty(lead.getEmail()) && lead.getEmail().length()>30){
            return ResponseData.fail("邮箱不能超过30个字符");
        }
        if(StringUtils.isEmpty(lead.getPhone()) ||  lead.getPhone().length()>14){
            return ResponseData.fail("联系方式输入有误");
        }
        if(!StringUtils.isEmpty(lead.getProvince()) &&
                (!NumberUtils.isDigits(lead.getProvince()) || lead.getProvince().length()>10)){
            return ResponseData.fail("省份数据有误");
        }
        if(!StringUtils.isEmpty(lead.getCity()) &&
                (!NumberUtils.isDigits(lead.getCity()) || lead.getCity().length()>10)){
            return ResponseData.fail("城市数据有误");
        }
        if(!StringUtils.isEmpty(lead.getSubCity()) &&
                (!NumberUtils.isDigits(lead.getSubCity()) || lead.getSubCity().length()>10)){
            return ResponseData.fail("区县数据有误");
        }
        if(!StringUtils.isEmpty(lead.getAddress()) && lead.getAddress().length()>255){
            return ResponseData.fail("地址输入字符不能超过125个字符");
        }
        if(!StringUtils.isEmpty(lead.getPurchaseTime()) &&
                (!NumberUtils.isDigits(lead.getPurchaseTime()) || lead.getPurchaseTime().length()>2)){
            return ResponseData.fail("预购日期数据有误");
        }
        if(!StringUtils.isEmpty(lead.getBudget()) &&
                (!NumberUtils.isDigits(lead.getBudget()) || lead.getBudget().length()>10)){
            return ResponseData.fail("客户预算数据有误");
        }
        if(!StringUtils.isEmpty(lead.getDealer()) &&
                (!NumberUtils.isDigits(lead.getDealer()) || lead.getDealer().length()>10)){
            return ResponseData.fail("经销商数据有误");
        }
        if(!StringUtils.isEmpty(lead.getMaritalStatus()) &&
                (!NumberUtils.isDigits(lead.getMaritalStatus()) || lead.getMaritalStatus().length()>2)){
            return ResponseData.fail("家庭结构数据有误");
        }
        if(!StringUtils.isEmpty(lead.getEducationLevel()) &&
                (!NumberUtils.isDigits(lead.getEducationLevel()) || lead.getEducationLevel().length()>2)){
            return ResponseData.fail("文化程度数据有误");
        }
        if(!StringUtils.isEmpty(lead.getPurchaeFrequency()) &&
                (!NumberUtils.isDigits(lead.getPurchaeFrequency()) || lead.getPurchaeFrequency().length()>3)){
            return ResponseData.fail("采购频次数据值太大");
        }
        if(!StringUtils.isEmpty(lead.getRevenueLevel()) &&
                (!NumberUtils.isDigits(lead.getRevenueLevel()) || lead.getRevenueLevel().length()>2)){
            return ResponseData.fail("收入水平数据有误");
        }
        if(!StringUtils.isEmpty(lead.getHouseholdRegistration()) &&
                (!NumberUtils.isDigits(lead.getHouseholdRegistration()) || lead.getHouseholdRegistration().length()>2)){
            return ResponseData.fail("户籍性质数据有误");
        }
        if(!StringUtils.isEmpty(lead.getHobby()) &&
                (!NumberUtils.isDigits(lead.getHobby()) || lead.getHobby().length()>10)){
            return ResponseData.fail("客户喜好数据有误");
        }
        if(lead.getOwner()!=null && lead.getOwner()>9999999){
            return ResponseData.fail("账户数据有误");
        }

        if(!StringUtils.isEmpty(lead.getVehicleNo()) &&
                lead.getVehicleNo().length()>8){
            return ResponseData.fail("车辆数不能超过8位");
        }
        if(!StringUtils.isEmpty(lead.getConsumptionCharacteristics()) && lead.getConsumptionCharacteristics().length()>100){
            return ResponseData.fail("客户消费特点不能超过100个字符");
        }
        if(!StringUtils.isEmpty(lead.getCustomerCharacteristicsDes()) && lead.getCustomerCharacteristicsDes().length()>100){
            return ResponseData.fail("客户特征描述长度太长");
        }
        if(!StringUtils.isEmpty(lead.getCompanyName()) && lead.getCompanyName().length()>125){
            return ResponseData.fail("单位名称输入长度太长");
        }
        if(!StringUtils.isEmpty(lead.getVehiclePurpose()) && lead.getVehiclePurpose().length()>255){
            return ResponseData.fail("购车用途不能超过255个字");
        }
        if(!StringUtils.isEmpty(lead.getInteriorYearlyBudget()) &&
                (!NumberUtils.isDigits(lead.getInteriorYearlyBudget()) || lead.getInteriorYearlyBudget().length()>2)){
            return ResponseData.fail("车饰年预算有误");
        }
        if(!StringUtils.isEmpty(lead.getDrivingSkill()) &&
                (!NumberUtils.isDigits(lead.getDrivingSkill()) || lead.getDrivingSkill().length()>2)){
            return ResponseData.fail("驾驶技术数据有误");
        }
        if(!StringUtils.isEmpty(lead.getIndustry()) &&
                (!NumberUtils.isDigits(lead.getIndustry()) || lead.getIndustry().length()>2)){
            return ResponseData.fail("行业数据有误");
        }
        if(!StringUtils.isEmpty(lead.getAutomotiveExpertise()) &&
                (!NumberUtils.isDigits(lead.getAutomotiveExpertise()) || lead.getAutomotiveExpertise().length()>2)){
            return ResponseData.fail("汽车专业知识数据有误");
        }
        if(!StringUtils.isEmpty(lead.getTreasureCarLevel()) &&
                (!NumberUtils.isDigits(lead.getTreasureCarLevel()) || lead.getTreasureCarLevel().length()>2)){
            return ResponseData.fail("珍惜车程度数据有误");
        }
        if(!StringUtils.isEmpty(lead.getCommunicationDifficulty()) &&
                (!NumberUtils.isDigits(lead.getCommunicationDifficulty()) || lead.getCommunicationDifficulty().length()>2)){
            return ResponseData.fail("沟通难度数据有误");
        }
        if(!StringUtils.isEmpty(lead.getRecomender()) && lead.getRecomender().length()>50){
            return ResponseData.fail("推荐人姓名不能超过50个字符");
        }
        if(!StringUtils.isEmpty(lead.getPersontitle()) && lead.getPersontitle().length()>100){
            return ResponseData.fail("职位不能超过50个字符");
        }
        if(!StringUtils.isEmpty(lead.getRecomenderPhone()) && lead.getRecomenderPhone().length()>12){
            return ResponseData.fail("推荐人点后不能超过12位");
        }
        if(!StringUtils.isEmpty(lead.getWechatOpenid()) && lead.getWechatOpenid().length()>64){
            return ResponseData.fail("微信openid不能超过64个字符");
        }
        if(!StringUtils.isEmpty(lead.getSpecialCustomerType()) &&
                (!NumberUtils.isDigits(lead.getSpecialCustomerType()) || lead.getSpecialCustomerType().length()>1)){
            return ResponseData.fail("特殊客户数据有误");
        }
        if(!StringUtils.isEmpty(lead.getSpecialCareComments()) && lead.getSpecialCareComments().length()>100){
            return ResponseData.fail("特殊客户说明不能超过255个字符");
        }
        if(!StringUtils.isEmpty(lead.getIfMarried())&&
                (!NumberUtils.isDigits(lead.getIfMarried()) || lead.getIfMarried().length()>1)){
            return ResponseData.fail("婚姻状况数据有误");
        }
        if(!StringUtils.isEmpty(lead.getFamilySize())&&
                (!NumberUtils.isDigits(lead.getFamilySize()) || lead.getFamilySize().length()>1)){
            return ResponseData.fail("家庭人数数据有误");
        }
        if(!StringUtils.isEmpty(lead.getOccupationType())&&
                (!NumberUtils.isDigits(lead.getOccupationType()) || lead.getOccupationType().length()>2)){
            return ResponseData.fail("单位类型数据有误");
        }
        if(!StringUtils.isEmpty(lead.getOccupationPhone()) && lead.getOccupationPhone().length()>15){
            return ResponseData.fail("单位电话不能超过15个字符");
        }
        if(!StringUtils.isEmpty(lead.getOwningCarAge()) &&
                (!NumberUtils.isDigits(lead.getOwningCarAge()) || lead.getOwningCarAge().length()>2)){
            return ResponseData.fail("购车年龄数据有误");
        }
        if(!StringUtils.isEmpty(lead.getCallReason()) && lead.getCallReason().length()>100){
            return ResponseData.fail("来电诉求不能超过15个字符");
        }
        if(!StringUtils.isEmpty(lead.getWechatNumber()) && lead.getWechatNumber().length()>20){
            return ResponseData.fail("微信号不能超过20个字符");
        }
        if(!StringUtils.isEmpty(lead.getDescription()) && lead.getDescription().length()>255){
            return ResponseData.fail("备注不能超过255个字符");
        }
        return ResponseData.success(1);
    }

    @Override
    public void updateLeadGenderById(Integer custId, Integer gender) {
        leadSearchMapper.updateLeadGenderById(custId,gender);
    }
}
