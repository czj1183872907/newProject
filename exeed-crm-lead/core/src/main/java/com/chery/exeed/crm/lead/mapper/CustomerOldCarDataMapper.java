package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.CustomerOldCarData;

public interface CustomerOldCarDataMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerOldCarData record);

    int insertSelective(CustomerOldCarData record);

    CustomerOldCarData selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerOldCarData record);

    int updateByPrimaryKey(CustomerOldCarData record);
}