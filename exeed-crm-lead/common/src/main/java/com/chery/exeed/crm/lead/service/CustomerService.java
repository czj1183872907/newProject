package com.chery.exeed.crm.lead.service;

import com.chery.exeed.crm.common.dto.PaginationResult;
import com.chery.exeed.crm.common.dto.ResponseData;
import com.chery.exeed.crm.lead.dto.*;
import com.chery.exeed.crm.lead.model.Customer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Author: zhen.liu@nttdata.com
 * @Date: 2019/1/17 18:32
 * @Description:
 */
public interface CustomerService {

    ResponseData<Customer> getCustByPhone(String phone);

    ResponseData<PaginationResult<CustomerInfoDTO>> getCustomerList(CustomerSearchDTO customerSearchDTO);


    ResponseData<CustomerDTO> getCustomer(CustomerDTO customerDTO);

    ResponseData<CustomerDTO> saveCustomer(CustomerDTO customerDTO);

    ResponseData<List<Customer>> getCustomerListByCredNum(String credentialId,Integer custId);

    /**
     * 将DMS传过来的用户同步到CRM（有则更新，无则新增）
     * @param customer
     * @return
     */
    ResponseData<Boolean> updateCustomerInfoFromDms(@RequestBody Customer customer);

    /**
     * 通过CRMCODE查询客户
     * @param crmCode
     * @return
     */
    ResponseData<Customer> getCustomerById(String crmCode);

    void sendDMSCustomer(String msg);

    void successSendDMSCustomer(String msg);

    /**
     * 客户第三方信息绑定
     * @param dto
     * @return
     */
    ResponseData<Boolean> custThirdBinding(@RequestBody CustomerAuthInfoDTO dto);

    /**
     * 根据第三方绑定信息查询客户基本信息
     * @param dto
     * @return
     */
    ResponseData<List<CustomerDTO>> custThirdInfo(@RequestBody CustomerAuthInfoDTO dto);

    /**
     * 通过手机号查询客户
     * @param phone
     * @return
     */
    ResponseData<Customer> getCustomerListByPhone(String phone);

    /**
     * C端用户修改客户信息
     * @param map
     * @return
     */
    ResponseData<Map<String, Object>> clientCustModify(@RequestBody Map<String, Object> map);

    /**
     * 客户姓名电话查询
     * @param customer
     * @return
     */
    ResponseData<List<Customer>> getCustomerByPhoneAndName(@RequestBody Customer customer);


    ResponseData<String> sendCustDms(Integer crmCode);

    /**
     * 客户高级查询 API
     * @param custAdvancedQueryDTO
     * @return
     */
    ResponseData<PaginationResult<CustomerInfoDTO>> advancedQuery(@RequestBody CustAdvancedQueryDTO custAdvancedQueryDTO);

    ResponseData<Integer> updateLeadByCustId(Integer custId,String vinNo,String carType);
}
