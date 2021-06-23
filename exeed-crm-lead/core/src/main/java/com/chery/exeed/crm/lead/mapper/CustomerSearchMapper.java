package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.dto.*;
import com.chery.exeed.crm.lead.model.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CustomerSearchMapper extends CustomerMapper {

    Customer getCustByPhone(@Param("phone") String phone);

    Customer getCustomerByPhone(@Param("phone") String phone,@Param("customerType") Integer customerType);

    List<Customer> getCustomerByFirstNameOrPhone(Map<String,Object> param);

    List<Customer> getCustomerByCredentialNum(@Param("credNum") String credNum,@Param("id")Integer id);

    List<CustomerInfoDTO> listCustomersInfo(CustomerSearchDTO customerSearchDTO);

    Customer getSendDMSCustomerById(@Param("customerId") Integer customerId);

    List<String> selectCusDealer(@Param("customerId") Integer customerId);

    Integer queryChannelResourceTimes(@Param("custId") Integer custId);

    int updateBySendDmsTime(Customer customer);

    List<CustomerDTO> queryCustomerByConsultant(@Param("keyword") String keyword, @Param("userId") Integer userId, @Param("dealerId") String dealerId);

    Customer getCustomerByPhoneTotal(@Param("phone") String phone);

    List<Customer> getCustomerByAuthInfo(CustomerAuthInfoDTO customerAuthInfoDTO);

    List<Customer> selectCustomerByPhoneAndName(Customer customer);

    List<CustomerInfoDTO> advancedQueryCustomerList(CustAdvancedQueryDTO custAdvancedQueryDTO);

    int updateCustomerSelective(Customer record);
}