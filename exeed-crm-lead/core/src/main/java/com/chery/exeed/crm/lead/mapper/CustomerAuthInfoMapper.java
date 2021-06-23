package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.CustomerAuthInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerAuthInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerAuthInfo record);

    int insertSelective(CustomerAuthInfo record);

    CustomerAuthInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerAuthInfo record);

    int updateByPrimaryKey(CustomerAuthInfo record);
}