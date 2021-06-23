package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.LeadDistributeConfig;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface LeadDistributeConfigSearchMapper {
    List<LeadDistributeConfig> getCitiDistributeConfigList(Map<String,Object> param);
}