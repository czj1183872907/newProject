package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.LeadHistory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LeadHistoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(LeadHistory record);

    int insertSelective(LeadHistory record);

    LeadHistory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(LeadHistory record);

    int updateByPrimaryKey(LeadHistory record);
}