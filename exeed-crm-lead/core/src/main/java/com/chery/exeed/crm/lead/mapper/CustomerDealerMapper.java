package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.CustomerDealer;
import com.chery.exeed.crm.lead.model.CustomerDealerKey;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerDealerMapper {
    int deleteByPrimaryKey(CustomerDealerKey key);

    int insert(CustomerDealer record);

    int insertSelective(CustomerDealer record);

    CustomerDealer selectByPrimaryKey(CustomerDealerKey key);

    int updateByPrimaryKeySelective(CustomerDealer record);

    int updateByPrimaryKey(CustomerDealer record);
}