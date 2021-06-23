package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.LeadDistributeConfig;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LeadDistributeConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LeadDistributeConfig record);

    int insertSelective(LeadDistributeConfig record);

    LeadDistributeConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LeadDistributeConfig record);

    int updateByPrimaryKey(LeadDistributeConfig record);
}