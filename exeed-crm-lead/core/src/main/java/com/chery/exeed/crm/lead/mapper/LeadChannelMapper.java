package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.LeadChannel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LeadChannelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LeadChannel record);

    int insertSelective(LeadChannel record);

    LeadChannel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LeadChannel record);

    int updateByPrimaryKey(LeadChannel record);
}