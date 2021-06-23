package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.dto.LeadFollowupDTO;
import com.chery.exeed.crm.lead.model.LeadFollowup;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LeadFollowupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LeadFollowup record);

    int insertSelective(LeadFollowup record);

    LeadFollowup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LeadFollowup record);

    int updateByPrimaryKey(LeadFollowup record);

    List<LeadFollowupDTO> selectByLeadId(Integer leadId);

}