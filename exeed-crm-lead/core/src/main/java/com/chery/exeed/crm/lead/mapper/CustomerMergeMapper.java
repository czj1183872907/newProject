package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.CustomerMerge;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerMergeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerMerge record);

    int insertSelective(CustomerMerge record);

    CustomerMerge selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerMerge record);

    int updateByPrimaryKeyWithBLOBs(CustomerMerge record);

    int updateByPrimaryKey(CustomerMerge record);
}