package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.dto.LeadChannelDTO;
import com.chery.exeed.crm.lead.model.LeadChannel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface LeadChannelSearchMapper {
    /**Integer subChannelId,String phone*/
    Integer selectByChannelCode(Map<String,Object> paramMap);

    List<LeadChannelDTO> getLeadChannelList(@Param("leadId") Integer leadId);

    List<LeadChannel> getChannelListByLeadId(Integer leadId);

    List<LeadChannelDTO> listMainDataByLeadId(Integer leadId);
}