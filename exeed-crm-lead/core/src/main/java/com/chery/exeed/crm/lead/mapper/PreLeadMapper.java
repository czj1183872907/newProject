package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.PreLead;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PreLeadMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PreLead record);

    int insertSelective(PreLead record);

    PreLead selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PreLead record);

    int updateByPrimaryKey(PreLead record);
}