package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.CustomerReferenceCarData;

public interface CustomerReferenceCarDataMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerReferenceCarData record);

    int insertSelective(CustomerReferenceCarData record);

    CustomerReferenceCarData selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerReferenceCarData record);

    int updateByPrimaryKey(CustomerReferenceCarData record);
}