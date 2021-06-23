package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.DistributeOperateRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DistributeOperateRecordSearchMapper extends DistributeOperateRecordMapper{
    int insertSelectiveReturn(DistributeOperateRecord record);
}