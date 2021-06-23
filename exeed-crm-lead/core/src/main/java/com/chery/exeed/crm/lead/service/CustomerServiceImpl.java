package com.chery.exeed.crm.lead.service;

import com.alibaba.fastjson.JSON;
import com.chery.exeed.crm.common.constants.CustomerCarPurchaseEnum;
import com.chery.exeed.crm.common.constants.CustomerPostEnum;
import com.chery.exeed.crm.common.constants.OccupationEnum;
import com.chery.exeed.crm.common.dto.*;
import com.chery.exeed.crm.common.service.DmsCustomerService;
import com.chery.exeed.crm.common.service.MetaApiService;
import com.chery.exeed.crm.common.service.RegionService;
import com.chery.exeed.crm.common.util.BeanUtils;
import com.chery.exeed.crm.common.util.SessionHelper;
import com.chery.exeed.crm.lead.constants.LeadConstants;
import com.chery.exeed.crm.lead.dto.*;
import com.chery.exeed.crm.lead.mapper.*;
import com.chery.exeed.crm.lead.model.*;
import com.chery.exeed.crm.sysadmin.constants.SysConstants;
import com.chery.exeed.crm.sysadmin.dto.UserDetailDTO;
import com.chery.exeed.crm.sysadmin.service.SysUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

import static com.chery.exeed.crm.common.constants.Constants.CUSTOMER_BIND_FAIL_CODE;

/**
 * @Author: zhen.liu@nttdata.com
 * @Date: 2019/1/17 18:39
 * @Description:
 */
@RestSchema(schemaId = "customer-service")
@RequestMapping(path = "/apis/customer")
public class CustomerServiceImpl implements CustomerService{

    private Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private CustomerSearchMapper customerSearchMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private MetaApiService metaApiService;

    @Autowired
    private MetaService metaService;

    @Autowired
    public RabbitTemplate rabbitTemplate;
    @Autowired
    private LeadCommonService leadCommonService;

    @Autowired
    private CustomerDealerMapper customerDealerMapper;

    @Autowired
    private OrderOperateRecordSearchMapper orderOperateRecordSearchMapper;

    @Autowired
    private CustomerAuthInfoSearchMapper customerAuthInfoSearchMapper;

    @Autowired
    private ExCustomerReferenceCarDataMapper exCustomerReferenceCarDataMapper;

    @Autowired
    private ExCustomerOldCarDataMapper exCustomerOldCarDataMapper;

    @Value("${ifs.customer.mq.queue}")
    private String MQ_QUEUE_CUSTOMER;
    @Value("${ifs.customer.info.mq.queue}")
    private String MQ_QUEUE_CUSTOMER_TO_DMS;
    @Autowired
    private RegionService regionService;
    @Autowired
    private DmsCustomerService dmsCustomerService;

    @RpcReference(microserviceName = "exeed-crm-apis:exeed-crm-sysadmin", schemaId = "user-service")
    private SysUserService userService;

    @Autowired
    private LeadMapper leadMapper;

    /**
     * 根据客户电话获取保客信息
     * @param phone
     * @return
     */
    @Override
    @RequestMapping(
            value = "/getcust",
            method = {RequestMethod.GET}
    )
    public ResponseData<Customer> getCustByPhone(@RequestParam("phone")String phone){
        Customer customer = customerSearchMapper.getCustByPhone(phone);//根据电话号码获取保客信息
        return ResponseData.success(customer);
    }

    /**
     * 潜客下发dms
     * @param crmCode
     * @return
     */

    @Override
    @RequestMapping(
            value = "/senddms",
            method = {RequestMethod.GET}
    )
    public ResponseData<String> sendCustDms(@RequestParam("crmCode") Integer crmCode){
        Customer customer = customerMapper.selectByPrimaryKey(crmCode);
        //潜客重新下发
        List<OrderOperateRecord> recordList = orderOperateRecordSearchMapper.selectByCertNo(customer.getCredentialNumber());
        if (recordList!=null && recordList.size()>0) {//下发dms成功说明客户已经有下订
            try {
                CustomerTranferInfoDTO infoDTO = new CustomerTranferInfoDTO();
                infoDTO.setId(customer.getId());
                rabbitTemplate.convertAndSend(MQ_QUEUE_CUSTOMER, JSON.toJSONString(infoDTO));
            } catch (Exception e) {
                logger.info("下发DMS触发MQ异常。", e);
                return ResponseData.fail("客户下发DMS发送MQ消息异常！");
            }
        }else{
            return ResponseData.fail("客户没有下订信息不可下发dms");
        }
        return ResponseData.success("客户信息已下发DMS。");
    }

    public UserDetailDTO getUserInfo(Integer currentUserId){
        UserDetailDTO sysUser = null;
        try {
            ResponseData<UserDetailDTO> sysUserData = userService.monitorInfo(currentUserId);
            if (sysUserData!=null){
                sysUser = sysUserData.getData();
            }
        }catch (Exception e){
            logger.error("CustomerServiceimpl>>>>>>获取用户信息失败",e);
        }
        return sysUser;
    }

    @Override
    @RequestMapping(
            value = "/save",
            method = {RequestMethod.POST}
    )
    public ResponseData<CustomerDTO> saveCustomer(@RequestBody CustomerDTO customerDTO){
        Integer memberId = SessionHelper.getCurrentUserId();
        Customer obj = null;
        if (customerDTO.getId()!=null){//客户修改
            obj = customerMapper.selectByPrimaryKey(customerDTO.getId());
        }else{
            String credentialNumber = customerDTO.getCredentialNumber();
            Map<String,Object> carparm = new HashMap<>();
            if (credentialNumber!=null && !"".equals(credentialNumber)){
                carparm.put("credentialNumber",credentialNumber);
                List<Customer> carList = customerSearchMapper.getCustomerByFirstNameOrPhone(carparm);
                if (carList!=null && carList.size()>0){
                    return ResponseData.fail("证件号码已被使用！请重新输入。");
                }
            }
        }

        String phone = customerDTO.getPhone();
        Date modifyTime = new Date();
        Map<String,Object> param = new HashMap<>();
        if (phone!=null && !"".equals(phone)){
            param.put("phone",phone);
        }
        List<Customer> customerList = customerSearchMapper.getCustomerByFirstNameOrPhone(param);
        Customer customer = null;
        if (customerList!=null && customerList.size()>0){
            customer = customerList.get(0);
        }
        if (customer!=null && (customerDTO.getId()==null || "".equals(customerDTO.getId()))){//新增电话号码判断
            return ResponseData.fail("电话号码已被使用！请重新输入。");
        }else if (customer!=null && customer.getId().intValue() != customerDTO.getId().intValue()){//修改电话号码判断
            return ResponseData.fail("电话号码已被使用！请重新输入。");
        }else if (customer==null){
            customer = new Customer();
        }
        if (customerDTO!=null){
            if(customer.getId()!=null) {
                leadCommonService.createLog(customerDTO, customer, "customer", modifyTime, memberId,customer.getId());
            }
            BeanUtils.copyProperties(customerDTO,customer);
        }

        if (customer.getId()==null || "".equals(customer.getId())) {
            customer.setCreatedBy(memberId);
            customer.setCustomerCarDate(new Date());
        }
        if(customer.getCustomerType()==null || "".equals(customer.getCustomerType())){
            customer.setCustomerType(1);//1：个人客户  2：企业客户  默认个人客户
        }
        customer.setModifyBy(memberId);
        customer.setModifyDate(modifyTime);
        if (customer.getId()==null || "".equals(customer.getId())) {//新增
            int num = customerMapper.insertSelective(customer);
            if (num <= 0) {
                return ResponseData.fail("客户保存失败！");
            }
        }else{
            //修改
            int num = customerSearchMapper.updateCustomerSelective(customer);
            if (num <= 0) {
                return ResponseData.fail("客户保存失败！");
            }
            if (obj!=null && obj.getCustomerStatus()!=null && obj.getCustomerStatus().intValue()==2) {//保有客户修改下发到dms
                try {
                    CustomerTranferInfoDTO infoDTO = new CustomerTranferInfoDTO();
                    infoDTO.setId(customer.getId());
                    rabbitTemplate.convertAndSend(MQ_QUEUE_CUSTOMER,JSON.toJSONString(infoDTO));
                }catch (Exception e){
                    logger.info("同步DMS触发MQ失败。");
                }
            }

        }

        List<CustomerOldCarDataDTO> oldCarData = exCustomerOldCarDataMapper.listByCustomer(customer.getId());
        List<CustomerReferenceCarDataDTO> referenceCarData = exCustomerReferenceCarDataMapper.listByCustomer(customer.getId());

        this.saveOldCarData(customerDTO.getOldCarData(), oldCarData, customer.getId());
        this.saveReferenceCarData(customerDTO.getReferenceCarData(), referenceCarData, customer.getId());

        BeanUtils.copyProperties(customer,customerDTO);
        this.mateCustomerInfo(customerDTO);
        Integer gender = customerDTO.getGender();
        if(gender != null){
            Integer custId = customer.getId();
            leadCommonService.updateLeadGenderById(custId,gender);

        }
        return ResponseData.success(customerDTO);
    }

    private void saveOldCarData(List<CustomerOldCarDataDTO> newData, List<CustomerOldCarDataDTO> oldData, Integer customerId) {
        List<CustomerOldCarData> addList = new ArrayList<>();
        List<CustomerOldCarData> updList = new ArrayList<>();
        List<Integer> delList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(newData) && !CollectionUtils.isEmpty(oldData)) {
            addList = newData.stream().filter(item -> item.getId() == null).map(item -> {
                CustomerOldCarData data = new CustomerOldCarDataDTO();
                BeanUtils.copyProperties(item, data);
                data.setCustomerId(customerId);
                return data;
            }).collect(Collectors.toList());

            newData.stream().forEach(item -> {
                boolean find = oldData.stream().anyMatch(oldItem -> item.getId() != null && oldItem.getId().equals(item.getId()));
                if (find) {
                    CustomerOldCarData data = new CustomerOldCarDataDTO();
                    BeanUtils.copyProperties(item, data);
                    data.setCustomerId(customerId);
                    updList.add(data);
                }
            });

            oldData.stream().forEach(item -> {
                boolean find = newData.stream().anyMatch(newItem -> newItem.getId() != null && newItem.getId().equals(item.getId()));

                if (!find) {
                    delList.add(item.getId());
                }
            });


        } else {
            if (CollectionUtils.isEmpty(newData)) {
                delList.addAll(oldData.stream().map(item -> item.getId()).collect(Collectors.toList()));
            }

            if (CollectionUtils.isEmpty(oldData)) {
                addList = newData.stream().map(item -> {
                    CustomerOldCarData data = new CustomerOldCarDataDTO();
                    BeanUtils.copyProperties(item, data);
                    data.setCustomerId(customerId);
                    return data;
                }).collect(Collectors.toList());
            }
        }

        if (!CollectionUtils.isEmpty(addList)) {
            for (CustomerOldCarData data : addList) {
                exCustomerOldCarDataMapper.insertSelective(data);
            }
        }
        if (!CollectionUtils.isEmpty(updList)) {
            for (CustomerOldCarData data : updList) {
                exCustomerOldCarDataMapper.updateByPrimaryKeySelective(data);
            }
        }
        if (!CollectionUtils.isEmpty(delList)) {
            for (Integer id : delList) {
                exCustomerOldCarDataMapper.deleteByPrimaryKey(id);
            }
        }
    }

    private void saveReferenceCarData(List<CustomerReferenceCarDataDTO> newData, List<CustomerReferenceCarDataDTO> oldData, Integer customerId) {
        List<CustomerReferenceCarData> addList = new ArrayList<>();
        List<CustomerReferenceCarData> updList = new ArrayList<>();
        List<Integer> delList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(newData) && !CollectionUtils.isEmpty(oldData)) {
            addList = newData.stream().filter(item -> item.getId() == null).map(item -> {
                CustomerReferenceCarData data = new CustomerReferenceCarData();
                BeanUtils.copyProperties(item, data);
                data.setCustomerId(customerId);
                return data;
            }).collect(Collectors.toList());

            newData.stream().forEach(item -> {
                boolean find = oldData.stream().anyMatch(oldItem -> item.getId() != null && oldItem.getId().equals(item.getId()));
                if (find) {
                    CustomerReferenceCarData data = new CustomerReferenceCarData();
                    BeanUtils.copyProperties(item, data);
                    data.setCustomerId(customerId);
                    updList.add(data);
                }
            });

            oldData.stream().forEach(item -> {
                boolean find = newData.stream().anyMatch(newItem -> newItem.getId() != null && newItem.getId().equals(item.getId()));

                if (!find) {
                    delList.add(item.getId());
                }
            });
        } else {
            if (CollectionUtils.isEmpty(newData)) {
                delList.addAll(oldData.stream().map(item -> item.getId()).collect(Collectors.toList()));
            }

            if (CollectionUtils.isEmpty(oldData)) {
                addList = newData.stream().map(item -> {
                    CustomerReferenceCarData data = new CustomerReferenceCarData();
                    BeanUtils.copyProperties(item, data);
                    data.setCustomerId(customerId);
                    return data;
                }).collect(Collectors.toList());
            }
        }
        if (!CollectionUtils.isEmpty(addList)) {
            for (CustomerReferenceCarData data : addList) {
                exCustomerReferenceCarDataMapper.insertSelective(data);
            }
        }
        if (!CollectionUtils.isEmpty(updList)) {
            for (CustomerReferenceCarData data : updList) {
                exCustomerReferenceCarDataMapper.updateByPrimaryKeySelective(data);
            }
        }
        if (!CollectionUtils.isEmpty(delList)) {
            for (Integer id : delList) {
                exCustomerReferenceCarDataMapper.deleteByPrimaryKey(id);
            }
        }
    }

    /**
     * 工单用户检验
     * @param customerDTO
     * @return
     */
    @Override
    @RequestMapping(
            value = "/user",
            method = {RequestMethod.POST}
    )
    public ResponseData<CustomerDTO> getCustomer(@RequestBody CustomerDTO customerDTO){
        String phone = customerDTO.getPhone();
        String name = customerDTO.getFirstName();
        int id = customerDTO.getId()==null?0:customerDTO.getId().intValue();
        Map<String,Object> param = new HashMap<>();
        if (phone!=null && !"".equals(phone)){
            param.put("phone",phone);
        }
        if (name!=null && !"".equals(name)){
            param.put("firstName",name);
        }
        if (id!=0){
            param.put("id",id);
        }
        List<Customer> customerList = customerSearchMapper.getCustomerByFirstNameOrPhone(param);
        Customer customer = null;
        if (customerList!=null && customerList.size()>0){
            customer = customerList.get(0);
        }
        if (customer==null) {//呼入的电话不存在则创建用户
            return ResponseData.fail("客户不存在！");
        }

        Integer city = customer.getCity();
        if (city!=null && StringUtils.isEmpty(customer.getCityName())){
            RegionDTO regionDTO = regionService.detailsRegion(city);
            if (regionDTO!=null){
                customer.setCityName(regionDTO.getRegionName());
            }
        }

        Integer province = customer.getProvince();
        if (province!=null && StringUtils.isEmpty(customer.getProvinceName())){
            RegionDTO regionDTO = regionService.detailsRegion(province);
            if (regionDTO!=null){
                customer.setProvinceName(regionDTO.getRegionName());
            }
        }

        Integer subcity = customer.getSubcity();
        if (subcity!=null && StringUtils.isEmpty(customer.getSubcityName())){
            RegionDTO regionDTO = regionService.detailsRegion(subcity);
            if (regionDTO!=null){
                customer.setSubcityName(regionDTO.getRegionName());
            }
        }
        BeanUtils.copyProperties(customer,customerDTO);
        this.mateCustomerInfo(customerDTO);

        customerDTO.setOldCarData(exCustomerOldCarDataMapper.listByCustomer(customerDTO.getId()));
        customerDTO.setReferenceCarData(exCustomerReferenceCarDataMapper.listByCustomer(customerDTO.getId()));

        return ResponseData.success(customerDTO);
    }

    @Override
    @RequestMapping(
            value = "/list",
            method = {RequestMethod.POST}
    )
    public ResponseData<PaginationResult<CustomerInfoDTO>> getCustomerList(@RequestBody CustomerSearchDTO customerSearchDTO) {
        Integer currentUserId = SessionHelper.getCurrentUserId();
        UserDetailDTO userInfo = leadCommonService.getUserInfo(currentUserId);
        Integer userType = userInfo.getUserType();
        /**当用户是经销商用户,只能看到当前经销商的客户信息;当用户是经销商用户并且是销售顾问,只能看到自己的客户*/
        if(userType!=null && userType>0 && userType<=3){
            String dealerId = userInfo.getDealerId();
            if(StringUtils.isEmpty(dealerId)){
                dealerId = "0";
            }
            customerSearchDTO.setDealer(dealerId);
            if(userType==2){
                customerSearchDTO.setOwner(currentUserId);
            }
        }

        PageHelper.startPage(customerSearchDTO.getPageNo(), customerSearchDTO.getPageSize());

        List<CustomerInfoDTO> rstList = customerSearchMapper.listCustomersInfo(customerSearchDTO);
        this.mateCustomer(rstList);
        PaginationResult<CustomerInfoDTO> result = new PaginationResult<>();
        PageInfo<CustomerInfoDTO> pageInfo = new PageInfo<>(rstList);
        result.setData(rstList);
        result.setTotalCount(pageInfo.getTotal());
        return ResponseData.success(result);
    }

    @Override
    @RequestMapping(
            value = "/listinfo",
            method = {RequestMethod.POST}
    )
    public ResponseData<List<Customer>> getCustomerByPhoneAndName(@RequestBody Customer customer){
        List<Customer> resultList = customerSearchMapper.selectCustomerByPhoneAndName(customer);
        if (resultList==null){
            return ResponseData.fail("没有查询到数据！");
        }
        return ResponseData.success(resultList);
    }

    @Override
    @RequestMapping(
            value = "/listByCredNum",
            method = {RequestMethod.GET}
    )
    public ResponseData<List<Customer>> getCustomerListByCredNum(String credentialId,Integer custId) {
        return ResponseData.success(customerSearchMapper.getCustomerByCredentialNum(credentialId,custId));
    }

    //@RabbitListener(queues = "${ifs.customer.mq.queue}", containerFactory="rabbitListenerContainerFactory")
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "${ifs.customer.mq.queue}", durable = "true"),
            exchange = @Exchange("direct.lead")), containerFactory="rabbitListenerContainerFactory")
    public void sendDMSCustomer(@Payload String msg){
        logger.info("sendDMSCustomer-msg:"+msg);
        CustomerTranferInfoDTO infoDTO = JSON.parseObject(msg,CustomerTranferInfoDTO.class);
        if (infoDTO!=null){
            Customer customer = customerMapper.selectByPrimaryKey(infoDTO.getId());
            if (customer!=null && customer.getCredentialNumber()!=null && !"".equals(customer.getCredentialNumber())
                    && customer.getFirstName()!=null) {//证件号 性别  姓名过滤
                DMSCustomerDTO dmsCustomerDTO = new DMSCustomerDTO();
                dmsCustomerDTO.setCrmCode(customer.getId());
                dmsCustomerDTO.setDataSource(2);//1.DMS 2.CRM
                dmsCustomerDTO.setName(customer.getFirstName());
                Integer gender = customer.getGender();
                if (gender == null) {
                    gender = 3;//未知
                }
                dmsCustomerDTO.setGender(gender);
                Integer customerType = customer.getCustomerType();
                if (customerType==null){//1：个人客户  2：企业客户  默认1
                    customerType = Integer.valueOf(1);
                }
                dmsCustomerDTO.setCustomerType(customerType);
                dmsCustomerDTO.setCustomerStatus(customer.getCustomerStatus());
                dmsCustomerDTO.setAge(customer.getAge());
                Integer sarNumber = customer.getSubcity();
                if (sarNumber==null){
                    sarNumber = customer.getCity();
                    if (sarNumber==null){
                        sarNumber = customer.getProvince();
                    }
                }
                String addr = null;
                if (sarNumber!=null){
                    RegionDTO regionDTO = regionService.detailsRegion(sarNumber);
                    if (regionDTO!=null){
                        addr = String.valueOf(regionDTO.getZipCode());
                    }
                }
                dmsCustomerDTO.setSarNumber(addr==null?null:addr);//行政区域编号 ( 可能需要调整)
                dmsCustomerDTO.setAddress(customer.getAddress());
                dmsCustomerDTO.setCallPhoneNumber(customer.getPhone());
                dmsCustomerDTO.setIdDocumentType(customer.getCredentialType());
                dmsCustomerDTO.setIdDocumentNumber(customer.getCredentialNumber());
                dmsCustomerDTO.setBirthdate(customer.getPersonBirthdate());
                dmsCustomerDTO.setEmail(customer.getPersonEmail());
                dmsCustomerDTO.setDriveLicenseNumber(customer.getDriverLicenseNo());
                dmsCustomerDTO.setCompanyName(customer.getCompanyName());
                if (StringUtils.isNotBlank(customer.getIndustry())) {
                    dmsCustomerDTO.setIndustryType(Integer.valueOf(customer.getIndustry()));
                }
                dmsCustomerDTO.setCompanyType(customer.getOccupationType());
                dmsCustomerDTO.setOfficePhoneNumber(customer.getOccupationPhone());
                dmsCustomerDTO.setEducationLeve(customer.getEducationLevel());
                dmsCustomerDTO.setHobby(customer.getHobby());
                dmsCustomerDTO.setHouseholdIncome(customer.getRevenueLevel());
                dmsCustomerDTO.setMaritalStatus(customer.getIfMarried());
                dmsCustomerDTO.setFamilySize(customer.getMaritalStatus());
                dmsCustomerDTO.setOwningCarAge(customer.getOwningCarAge());
                dmsCustomerDTO.setReferral(customer.getRecomender());
                dmsCustomerDTO.setReferrerPhoneNumber(customer.getRecomenderPhone());
                dmsCustomerDTO.setContact(customer.getContact());
                dmsCustomerDTO.setContactNumber(customer.getContactPhone());
                dmsCustomerDTO.setRemark(customer.getDescription());
                dmsCustomerDTO.setWeChat(customer.getWechatOpenid());

                List<String> dealerList = customerSearchMapper.selectCusDealer(customer.getId());
                if (dealerList == null) {
                    dealerList = new ArrayList<>();
                }
                List<String> dealers = new ArrayList<>();
                for (String dealerCode:dealerList){
                    if (dealerCode!=null && !"".equals(dealerCode.trim())){
                        dealers.add(dealerCode);
                    }
                }
                dmsCustomerDTO.setDealerCodes(dealers);
                int sendFalg = 0;//发送标识 0未发送 1已发送
                try {
                    rabbitTemplate.convertAndSend(MQ_QUEUE_CUSTOMER_TO_DMS, JSON.toJSONString(dmsCustomerDTO));
                    sendFalg = 1;
                } catch (Exception e) {
                    logger.error("CustomerServiceImpl>>MQ_QUEUE_CUSTOMER_TO_DMS>>客户dms对象发送队列失败。客户Id："+customer.getId(), e);
                }
                try {
                    for (String dealerId : dealers) {
                        CustomerDealerKey key = new CustomerDealerKey();
                        key.setCustomerId(customer.getId());
                        key.setDealerId(dealerId);
                        CustomerDealer customerDealer = customerDealerMapper.selectByPrimaryKey(key);
                        if (customerDealer==null) {//新增经销商与客户关系
                            customerDealer = new CustomerDealer();
                            customerDealer.setDealerId(dealerId);
                            customerDealer.setCustomerId(customer.getId());
                            customerDealer.setSendFlag(sendFalg);
                            customerDealer.setCreateTime(new Date());
                            customerDealerMapper.insert(customerDealer);
                        }
                    }
                } catch (Exception e) {
                    logger.error("CustomerServiceImpl>>>>客户与经销商关系保存失败。", e);
                }
            }else{
                logger.info("CustomerServiceImpl>>>>客户证件号码或姓名不可为空。客户Id："+customer.getId());
            }
        }
    }

    //@RabbitListener(queues = "${ifs.customer.response.mq.queue}", containerFactory="rabbitListenerContainerFactory")
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "${ifs.customer.response.mq.queue}", durable = "true"),
            exchange = @Exchange("")), containerFactory="rabbitListenerContainerFactory")
    public void successSendDMSCustomer(@Payload String msg){
        CustomerResponseDTO infoDTO = JSON.parseObject(msg,CustomerResponseDTO.class);
        if (infoDTO!=null) {
            Customer customer = customerMapper.selectByPrimaryKey(infoDTO.getId());
            if (customer != null && customer.getModifyDate() != null) {
                customer.setSendModifyTime(customer.getModifyDate());
                customerSearchMapper.updateBySendDmsTime(customer);
            } else if (customer != null) {
                Date sendDate = new Date();
                customer.setModifyDate(sendDate);
                customer.setSendModifyTime(sendDate);
                customerSearchMapper.updateBySendDmsTime(customer);
            } else {
                logger.info("推送dms反馈客户信息不存在。");//客户不存在
            }
        }
    }



    /**
     * 列表字段转译
     * @param rstList
     */
    public void mateCustomer(List<CustomerInfoDTO> rstList){
        if (rstList!=null&&rstList.size()>0){
            for (CustomerInfoDTO customerInfoDTO:rstList){
                if (customerInfoDTO.getCustomerStatus()!=null){
                    MetaDTO respData = metaApiService.details(customerInfoDTO.getCustomerStatus(),"customer_status");
                    String customerStatusDes = "";
                    if (respData!=null&&respData.getDescription()!=null){
                        customerStatusDes = respData.getDescription();
                    }
                    customerInfoDTO.setCustomerStatusDes(customerStatusDes);
                }
                if (customerInfoDTO.getSpecialCustomerType()!=null){
                    MetaDTO respData = metaApiService.details(customerInfoDTO.getSpecialCustomerType(),"special_customer_type");
                    String specialCustomerTypeDes = "";
                    if (respData!=null&&respData.getDescription()!=null){
                        specialCustomerTypeDes = respData.getDescription();
                    }
                    customerInfoDTO.setSpecialCustomerTypeDes(specialCustomerTypeDes);
                }
                Integer city = customerInfoDTO.getCityId();
                if (city!=null && StringUtils.isEmpty(customerInfoDTO.getCityName())){
                    RegionDTO regionDTO = regionService.detailsRegion(city);
                    if (regionDTO!=null){
                        customerInfoDTO.setCityName(regionDTO.getRegionName());
                    }
                }

                Integer province = customerInfoDTO.getProvinceId();
                if (province!=null && StringUtils.isEmpty(customerInfoDTO.getProvinceName())){
                    RegionDTO regionDTO = regionService.detailsRegion(province);
                    if (regionDTO!=null){
                        customerInfoDTO.setProvinceName(regionDTO.getRegionName());
                    }
                }

                Integer subcity = customerInfoDTO.getSubcityId();
                if (subcity!=null && StringUtils.isEmpty(customerInfoDTO.getSubcityName())){
                    RegionDTO regionDTO = regionService.detailsRegion(subcity);
                    if (regionDTO!=null){
                        customerInfoDTO.setSubcityName(regionDTO.getRegionName());
                    }
                }
            }
        }
    }
    /**
     * 字典转译
     * @param customerDTO
     */
    public void mateCustomerInfo(CustomerDTO customerDTO){
        try {
            if (customerDTO.getCustomerStatus()!=null){
                MetaDTO respData = metaApiService.details(customerDTO.getCustomerStatus(),"customer_status");
                String customerStatusDes = "";
                if (respData!=null&&respData.getDescription()!=null){
                    customerStatusDes = respData.getDescription();
                }
                customerDTO.setCustomerStatusDes(customerStatusDes);
            }
            if (customerDTO.getGender()!=null){
                MetaDTO respData = metaApiService.details(customerDTO.getGender(),"sex");
                String genderDes = "";
                if (respData!=null&&respData.getDescription()!=null){
                    genderDes = respData.getDescription();
                }
                customerDTO.setGenderDes(genderDes);
            }
            if (customerDTO.getNation()!=null){
                MetaDTO respData = metaApiService.details(customerDTO.getNation(),"nation");
                String nation = "";
                if (respData!=null&&respData.getDescription()!=null){
                    nation = respData.getDescription();
                }
                customerDTO.setNationDes(nation);
            }
            if (customerDTO.getCredentialType()!=null){
                MetaDTO respData = metaApiService.details(customerDTO.getCredentialType(),"credential_type");
                String credentialType = "";
                if (respData!=null&&respData.getDescription()!=null){
                    credentialType = respData.getDescription();
                }
                customerDTO.setCredentialTypeDes(credentialType);
            }
            if (customerDTO.getOwner()!=null){
                ResponseData<String> respData = userService.getUserNameById(customerDTO.getOwner());;
                String name = "";
                if (respData!=null&&respData.getData()!=null){
                    name = respData.getData();
                }
                customerDTO.setOwnerName(name);
            }
            if (customerDTO.getCreatedBy()!=null){
                ResponseData<String> respData  = userService.getUserNameById(customerDTO.getCreatedBy());
                String name = "";
                if (respData!=null&&respData.getData()!=null){
                    name = respData.getData();
                }
                customerDTO.setCreatedByName(name);
            }
            if (customerDTO.getModifyBy()!=null){
                ResponseData<String> respData  = userService.getUserNameById(customerDTO.getModifyBy());
                String name = "";
                if (respData!=null&&respData.getData()!=null){
                    name = respData.getData();
                }
                customerDTO.setModifyByName(name);
            }
            if (customerDTO.getCustomerLevel()!=null){
                MetaDTO respData = metaApiService.details(customerDTO.getCustomerLevel(),"lead_cust_level");
                String customerLevel = "";
                if (respData!=null&&respData.getDescription()!=null){
                    customerLevel = respData.getDescription();
                }
                customerDTO.setCustomerLevelDes(customerLevel);
            }
            if (customerDTO.getSpecialCustomerType()!=null){
                MetaDTO respData = metaApiService.details(customerDTO.getSpecialCustomerType(),"special_customer_type");
                String specialCustomerTypeDes = "";
                if (respData!=null&&respData.getDescription()!=null){
                    specialCustomerTypeDes = respData.getDescription();
                }
                customerDTO.setSpecialCustomerTypeDes(specialCustomerTypeDes);
            }
            if (customerDTO.getHouseholdRegistration()!=null){
                MetaDTO respData = metaApiService.details(customerDTO.getHouseholdRegistration(),"household_registration");
                String householdRegistration = "";
                if (respData!=null&&respData.getDescription()!=null){
                    householdRegistration = respData.getDescription();
                }
                customerDTO.setHouseholdRegistrationDes(householdRegistration);
            }
            if (customerDTO.getEducationLevel()!=null){
                MetaDTO respData = metaApiService.details(customerDTO.getEducationLevel(),"lead_education_Level");
                String educationLevelDes = "";
                if (respData!=null&&respData.getDescription()!=null){
                    educationLevelDes = respData.getDescription();
                }
                customerDTO.setEducationLevelDes(educationLevelDes);
            }
            if (customerDTO.getRevenueLevel()!=null){
                MetaDTO respData = metaApiService.details(customerDTO.getRevenueLevel(),"lead_revenue_level");
                String revenueLevel = "";
                if (respData!=null&&respData.getDescription()!=null){
                    revenueLevel = respData.getDescription();
                }
                customerDTO.setRevenueLevelDes(revenueLevel);
            }
            if (customerDTO.getDrivingSkill()!=null){
                MetaDTO respData = metaApiService.details(customerDTO.getDrivingSkill(),"driving_skill");
                String drivingSkill = "";
                if (respData!=null&&respData.getDescription()!=null){
                    drivingSkill = respData.getDescription();
                }
                customerDTO.setDrivingSkillDes(drivingSkill);
            }
            if (customerDTO.getAutomotiveExpertise()!=null){
                MetaDTO respData = metaApiService.details(customerDTO.getAutomotiveExpertise(),"automotive_expertise");
                String automotiveExpertise = "";
                if (respData!=null&&respData.getDescription()!=null){
                    automotiveExpertise = respData.getDescription();
                }
                customerDTO.setAutomotiveExpertiseDes(automotiveExpertise);
            }
            if (customerDTO.getCommunicationDifficulty()!=null){
                MetaDTO respData = metaApiService.details(customerDTO.getCommunicationDifficulty(),"communication_difficult");
                String communicationDifficulty = "";
                if (respData!=null&&respData.getDescription()!=null){
                    communicationDifficulty = respData.getDescription();
                }
                customerDTO.setCommunicationDifficultyDes(communicationDifficulty);
            }
            if (customerDTO.getTreasureCarLevel()!=null){
                MetaDTO respData = metaApiService.details(customerDTO.getTreasureCarLevel(),"treasure_car_level");
                String treasureCarLevel = "";
                if (respData!=null&&respData.getDescription()!=null){
                    treasureCarLevel = respData.getDescription();
                }
                customerDTO.setTreasureCarLevelDes(treasureCarLevel);
            }
            if (customerDTO.getMaritalStatus()!=null){
                MetaDTO respData = metaApiService.details(customerDTO.getMaritalStatus(),"family_Size");
                String maritalStatus = "";
                if (respData!=null&&respData.getDescription()!=null){
                    maritalStatus = respData.getDescription();
                }
                customerDTO.setMaritalStatusDes(maritalStatus);
            }
            if (customerDTO.getIfMarried()!=null){
                MetaDTO respData = metaApiService.details(customerDTO.getIfMarried(),"if_married");
                String ifMarried = "";
                if (respData!=null&&respData.getDescription()!=null){
                    ifMarried = respData.getDescription();
                }
                customerDTO.setIfMarriedDes(ifMarried);
            }
            if (customerDTO.getHobby()!=null && !"".equals(customerDTO.getHobby())){
                MetaDTO respData = metaApiService.details(Integer.valueOf(customerDTO.getHobby()),"customer_hobby");
                String hobby = "";
                if (respData!=null&&respData.getDescription()!=null){
                    hobby = respData.getDescription();
                }
                customerDTO.setHobbyDes(hobby);
            }
            if (customerDTO.getOccupationType()!=null){
                MetaDTO respData = metaApiService.details(customerDTO.getOccupationType(),"occupation_type");
                String occupationType = "";
                if (respData!=null&&respData.getDescription()!=null){
                    occupationType = respData.getDescription();
                }
                customerDTO.setOccupationTypeDes(occupationType);
            }
            if (customerDTO.getIndustry()!=null && !"".equals(customerDTO.getIndustry())){
                MetaDTO respData = metaApiService.details(Integer.valueOf(customerDTO.getIndustry()),"industry");
                String industry = "";
                if (respData!=null&&respData.getDescription()!=null){
                    industry = respData.getDescription();
                }
                customerDTO.setIndustryDes(industry);
            }
            if (customerDTO.getOwningCarAge()!=null){
                MetaDTO respData = metaApiService.details(customerDTO.getOwningCarAge(),"owning_car_age");
                String owningCarAge = "";
                if (respData!=null&&respData.getDescription()!=null){
                    owningCarAge = respData.getDescription();
                }
                customerDTO.setOwningCarAgeDes(owningCarAge);
            }
            if (customerDTO.getFollowCar()!=null && !"".equals(customerDTO.getFollowCar())) {
                String followCars[] = customerDTO.getFollowCar().split(",");
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
                customerDTO.setFollowCarDes(followCarName);
            }
            if (customerDTO.getFollowInfo()!=null && !"".equals(customerDTO.getFollowInfo())) {
                String followInfos[] = customerDTO.getFollowInfo().split(",");
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
                customerDTO.setFollowInfoDes(followInfoName);
            }

            if (customerDTO.getPost() != null) {
                customerDTO.setPostDes(metaService.mdmMetaLeadTranslate(CustomerPostEnum.getModuleName(), customerDTO.getPost()));
            }

            if (customerDTO.getFirstTimeCarPurchase() != null) {
                customerDTO.setFirstTimeCarPurchaseDes(metaService
                        .mdmMetaLeadTranslate(CustomerCarPurchaseEnum.getModuleName(), customerDTO.getFirstTimeCarPurchase()));
            }

            if (NumberUtils.isDigits(customerDTO.getPersonTitle())) {
                customerDTO.setPersonTitleDes(metaService
                        .mdmMetaLeadTranslate(OccupationEnum.getModuleName(), Integer.valueOf(customerDTO.getPersonTitle())));
            }

            MetaChooseReason metaChooseReason = metaService.metaChooseReasonTranslate(customerDTO.getChooseReason(), customerDTO.getChooseReasonDetail());

            if (metaChooseReason != null) {
                customerDTO.setChooseReasonDes(metaChooseReason.getChooseReasonDesc());
                customerDTO.setChooseReasonDetailDes(metaChooseReason.getChooseReasonDetailDesc());
            }

            MetaKnowChannel metaKnowChannel = metaService.metaKnowChannelTranslate(customerDTO.getKnowChannel(), customerDTO.getKnowChannelDetail());

            if (metaKnowChannel != null) {
                customerDTO.setKnowChannelDes(metaKnowChannel.getKnowChannelDesc());
                customerDTO.setKnowChannelDetailDes(metaKnowChannel.getKnowChannelDetailDesc());
            }


        }catch (Exception e){
            logger.error("客户字典值翻译报错：",e);
        }
    }

    @Override
    @RequestMapping(value = "updateCustomerInfoFromDms", method = RequestMethod.POST)
    public ResponseData<Boolean> updateCustomerInfoFromDms(@RequestBody Customer customer) {
        if (customer.getId()==null || customer.getId()==0) {
            customerMapper.insert(customer);
        } else {
            customerMapper.updateByPrimaryKey(customer);
        }
        return ResponseData.success(true);
    }

    @Override
    @RequestMapping(value = "getCustomerById", method = RequestMethod.POST)
    public ResponseData<Customer> getCustomerById(String crmCode) {

        Map<String,Object> param = new HashMap<>();
        param.put("id",crmCode);
        List<Customer> customerList = customerSearchMapper.getCustomerByFirstNameOrPhone(param);
        if (customerList==null || customerList.size()==0) {
            return ResponseData.success(null);
        }
        Customer customer = customerList.get(0);
        return ResponseData.success(customer);
    }

    @Override
    @RequestMapping(value = "/third/binding", method = RequestMethod.POST)
    public ResponseData<Boolean> custThirdBinding(@RequestBody CustomerAuthInfoDTO dto) {
        // customer third binding
        Customer customer = customerSearchMapper.getCustomerByPhoneTotal(dto.getPhone());
        if (customer != null) {
            // 1. 已存在客户绑定
            CustomerAuthInfo customerAuthInfo = customerAuthInfoSearchMapper.selectByCustomerId(customer.getId());
            if (customerAuthInfo != null) {
                CustomerAuthInfo model = new CustomerAuthInfo();
                if (dto.getWxOpenId() != null && customerAuthInfo.getWxOpenId() != null) {
                    logger.info("微信绑定失败 ===> customerId[" + customer.getId() + "]; phone[" + dto.getPhone() + "]");
                    return ResponseData.fail(CUSTOMER_BIND_FAIL_CODE,"绑定失败，客户信息已绑定");
                } else {
                    model.setWxOpenId(dto.getWxOpenId());
                    model.setWxUnionId(dto.getWxUnionId());
                }
                if (dto.getQqOpenId() != null && customerAuthInfo.getQqOpenId() != null) {
                    logger.info("QQ绑定失败 ===> customerId[" + customer.getId() + "]; phone[" + dto.getPhone() + "]");
                    return ResponseData.fail(CUSTOMER_BIND_FAIL_CODE,"绑定失败，客户信息已绑定");
                } else {
                    model.setQqOpenId(dto.getQqOpenId());
                }
                if (dto.getSinaOpenId() != null && customerAuthInfo.getSinaOpenId() != null) {
                    logger.info("sina绑定失败 ===> customerId[" + customer.getId() + "]; phone[" + dto.getPhone() + "]");
                    return ResponseData.fail(CUSTOMER_BIND_FAIL_CODE,"绑定失败，客户信息已绑定");
                } else {
                    model.setSinaOpenId(dto.getSinaOpenId());
                }
                model.setId(customerAuthInfo.getId());
                model.setCustomerId(customer.getId());
                model.setBindTime(new Date());
                customerAuthInfoSearchMapper.updateByPrimaryKeySelective(model);
                return ResponseData.success(true, "绑定成功");
            } else {
                CustomerAuthInfo model = new CustomerAuthInfo();
                BeanUtils.copyProperties(dto, model, new String[]{"id", "password", "salt"});
                model.setCustomerId(customer.getId());
                model.setBindTime(new Date());
                customerAuthInfoSearchMapper.insertSelective(model);
                return ResponseData.success(true, "绑定成功");
            }
        }
        // 2. 新客户绑定
        Customer custModel = new Customer();
        custModel.setPhone(dto.getPhone());
        customerMapper.insertSelective(custModel);
        // 客户认证信息绑定
        CustomerAuthInfo model = new CustomerAuthInfo();
        BeanUtils.copyProperties(dto, model, new String[]{"id", "password", "salt"});
        model.setCustomerId(custModel.getId());
        model.setBindTime(new Date());
        customerAuthInfoSearchMapper.insertSelective(model);
        return ResponseData.success(true, "绑定成功");
    }

    @Override
    @RequestMapping(value = "/third/info/list", method = RequestMethod.POST)
    public ResponseData<List<CustomerDTO>> custThirdInfo(@RequestBody CustomerAuthInfoDTO dto) {
        List<CustomerDTO> rstList = new ArrayList<>();
        List<Customer> customerList = customerSearchMapper.getCustomerByAuthInfo(dto);
        if (customerList != null && customerList.size() > 0) {
            for (Customer customer : customerList) {
                CustomerDTO customerDTO = new CustomerDTO();
                BeanUtils.copyProperties(customer, customerDTO);
                // 判断客户状态
                if (LeadConstants.CUSTOMER_STATUS_ENUME.RETAIN_CUSTOMER.getValue()
                        .equals(customerDTO.getCustomerStatus())) {
                    // 保有客户
                    ResponseData<CustomerCarRelationDTO> relation = dmsCustomerService.findCustomerCarRelation(customerDTO.getId());
                    String message = (relation != null) ? relation.getMessage() : "findCustomerCarRelation response fail";
                    if (relation == null || relation.getData() == null || relation.getData().getTotalSize() <= 0) {
                        logger.info("保客customerId[" + customerDTO.getId() + "]; " + message);
                        customerDTO.setCustomerCarRelation(false);
                    } else {
                        logger.info("保客customerId[" + customerDTO.getId() + "]; " + message);
                        customerDTO.setCustomerCarRelation(true);
                    }
                } else {
                    logger.info("潜客customerId[" + customerDTO.getId() + "]");
                    customerDTO.setCustomerCarRelation(false);
                }
                rstList.add(customerDTO);
            }
        }
        return ResponseData.success(rstList);
    }

    @Override
    @RequestMapping(value = "/selectByPhone", method = RequestMethod.GET)
    public ResponseData<Customer> getCustomerListByPhone(String phone) {
        return ResponseData.success(customerSearchMapper.getCustomerByPhone(phone,1));
    }

    @Override
    @RequestMapping(value = "/client/modify", method = RequestMethod.POST)
    public ResponseData<Map<String, Object>> clientCustModify(@RequestBody Map<String, Object> map) {
//        String ptype = map.get("ptype").toString();
//        String clientInfo = map.get("clientInfo").toString();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        if (stringIsEmpty(ptype) || stringIsEmpty(clientInfo)) {
//            logger.info("Client modify Invalid parameter ===> ptype[{}]; clientInfo[{}]", ptype, clientInfo);
//            return ResponseData.fail(PARAM_IN_FAIL_CODE, "modify customer fail; Invalid parameter");
//        }
//        // 1. base64 decode
//        String infoJson = Base64Util.basicDecode(clientInfo);
//        String openId = ThirdAPICommonUtil.parseJsonObjValueExcEmpty(infoJson, new String []{"openid"});
//        String phone = ThirdAPICommonUtil.parseJsonObjValueExcEmpty(infoJson, new String []{"phone"});
//        if (stringIsEmpty(openId)) {
//            return ResponseData.fail(PARAM_IN_FAIL_CODE, "modify customer fail; Invalid client info, unknown customer " + clientInfo);
//        }
//        // 2. get customer info by openId
//        Customer oldCustomer = customerSearchMapper.getCustIdByThirdChannel(Integer.valueOf(ptype), openId);
//        phone = (phone == null || phone.trim().length() < 0 || "".equalsIgnoreCase(phone.trim())) ? oldCustomer.getPhone() : phone;
//        // 3. create modify log
//        if (oldCustomer == null) {
//            return ResponseData.fail(CUSTOMER_NOT_EXIST_CODE, "modify fail; Customer not exist, unique cert " + openId);
//        }
//        Customer newCustomer = new Customer();
//        newCustomer.setId(oldCustomer.getId());
//        newCustomer.setFirstName(ThirdAPICommonUtil.parseJsonObjValueExcEmpty(infoJson, new String []{"name"}));
//        newCustomer.setGender(Integer.valueOf(ThirdAPICommonUtil.parseJsonObjValueExcEmpty(infoJson, new String []{"gender"})));
//        newCustomer.setPhone(phone);
//        String birth = ThirdAPICommonUtil.parseJsonObjValueExcEmpty(infoJson, new String []{"birthday"});
//        try {
//            if (birth != null) {
//                Date parseDate = sdf.parse(birth);
//                newCustomer.setPersonBirthdate(parseDate);
//            }
//        } catch (ParseException e) {
//            logger.error("client customer openId[" + openId + "]; modify birth[" + birth + "] " + e.getMessage(), e);
//            return ResponseData.fail(PARAM_IN_FAIL_CODE, "modify customer fail; Invalid birth");
//        }
//        Date modifyTime = new Date();
//        leadCommonService.createLog(newCustomer, oldCustomer, "client_customer", modifyTime, newCustomer.getId());
//        // 4. update customer info
//        newCustomer.setModifyDate(modifyTime);
//        customerMapper.updateByPrimaryKeySelective(newCustomer);
        Map<String, Object> rstMap = new HashMap<>();
//        Customer resCustomer = customerSearchMapper.getCustomerByPhone(phone);
//
//        if (resCustomer != null) {
//            rstMap.put("name", resCustomer.getFirstName());
//            rstMap.put("phone", resCustomer.getPhone());
//            rstMap.put("gender", resCustomer.getGender());
//            rstMap.put("birth", sdf.format(resCustomer.getPersonBirthdate()));
//            // 5. check customer status ==> send DMS
//            if (SysConstants.CUSTOMER_STATUS_BAOKE.equals(resCustomer.getCustomerStatus())) {
//                // send to DMS
//                try {
//                    CustomerTranferInfoDTO infoDTO = new CustomerTranferInfoDTO();
//                    infoDTO.setId(resCustomer.getId());
//                    rabbitTemplate.convertAndSend(MQ_QUEUE_CUSTOMER,JSON.toJSONString(infoDTO));
//                }catch (Exception e){
//                    logger.info("同步DMS触发MQ失败。");
//                }
//            }
//        }
        return ResponseData.success(rstMap, "客户信息修改成功");
    }

    @Override
    @RequestMapping(value = "/advanced/query", method = RequestMethod.POST)
    public ResponseData<PaginationResult<CustomerInfoDTO>> advancedQuery(@RequestBody CustAdvancedQueryDTO custAdvancedQueryDTO) {
        Integer pageNo = custAdvancedQueryDTO.getPageNo() == null ? 1 : custAdvancedQueryDTO.getPageNo();
        Integer pageSize = custAdvancedQueryDTO.getPageSize() == null ? 10 : custAdvancedQueryDTO.getPageSize();
        PageHelper.startPage(pageNo, pageSize);
        List<CustomerInfoDTO> rstList = customerSearchMapper.advancedQueryCustomerList(custAdvancedQueryDTO);
        this.mateCustomer(rstList);
        PaginationResult<CustomerInfoDTO> result = new PaginationResult<>();
        PageInfo<CustomerInfoDTO> pageInfo = new PageInfo<>(rstList);
        result.setData(rstList);
        result.setTotalCount(pageInfo.getTotal());

        return ResponseData.success(result);
    }

    @Override
    @RequestMapping(value = "/advanced/updateLeadByCustId", method = RequestMethod.POST)
    public ResponseData<Integer> updateLeadByCustId(Integer custId,String vinNo,String carType) {
        logger.info("CZJ lead中执行根据客户编号修改语句--客户编号"+custId+"--车架号"+vinNo+"--车型"+carType);
        Lead lead=new Lead();
        lead.setVinNo(vinNo);
        lead.setCustId(custId);
        lead.setCategoryName(carType);
        return ResponseData.success(leadMapper.updateLeadByCustId(lead));
    }
}
