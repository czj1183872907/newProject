package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.CustomerHistory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerHistoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerHistory record);

    int insertSelective(CustomerHistory record);

    CustomerHistory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerHistory record);

    int updateByPrimaryKey(CustomerHistory record);
}