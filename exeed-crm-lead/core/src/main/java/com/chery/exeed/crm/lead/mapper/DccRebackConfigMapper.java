package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.DccRebackConfig;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DccRebackConfigMapper {
    int insert(DccRebackConfig record);

    int insertSelective(DccRebackConfig record);
}