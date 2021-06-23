package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.PreLead;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PreLeadSearchMapper {
    PreLead selectPreLeadBySourceId(String sourceId);
    List<PreLead> selectPreLeadByLeadId(Integer leadId);

    String selectChannelByLeadId(Integer leadId);
}