package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.LeadTask;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LeadTaskMapper {
    int deleteByPrimaryKey(Long id);

    int insert(LeadTask record);

    int insertSelective(LeadTask record);

    LeadTask selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(LeadTask record);

    int updateByPrimaryKey(LeadTask record);
}