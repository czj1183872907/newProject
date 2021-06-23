package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.dto.CustomerOldCarDataDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: zhen.liu@nttdata.com
 * @Date: 2020/12/23 18:15
 * @Description:
 */
@Mapper
public interface ExCustomerOldCarDataMapper extends CustomerOldCarDataMapper {

    List<CustomerOldCarDataDTO> listByCustomer(Integer customerId);
}
