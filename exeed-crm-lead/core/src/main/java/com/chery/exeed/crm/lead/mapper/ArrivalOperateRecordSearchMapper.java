package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.ArrivalOperateRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArrivalOperateRecordSearchMapper extends  ArrivalOperateRecordMapper {
    int insertArrivalOperateRecord(ArrivalOperateRecord arrivalOperateRecord);
}
