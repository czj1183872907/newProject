package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.dto.CustomerReferenceCarDataDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: zhen.liu@nttdata.com
 * @Date: 2020/12/23 18:21
 * @Description:
 */
@Mapper
public interface ExCustomerReferenceCarDataMapper extends CustomerReferenceCarDataMapper {

    List<CustomerReferenceCarDataDTO> listByCustomer(Integer customerId);
}
