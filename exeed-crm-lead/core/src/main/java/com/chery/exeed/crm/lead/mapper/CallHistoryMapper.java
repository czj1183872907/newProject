package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.CallHistory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CallHistoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CallHistory record);

    int insertSelective(CallHistory record);

    CallHistory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CallHistory record);

    int updateByPrimaryKey(CallHistory record);
}