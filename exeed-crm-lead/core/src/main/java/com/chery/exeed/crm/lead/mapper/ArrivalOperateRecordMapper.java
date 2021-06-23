package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.ArrivalOperateRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArrivalOperateRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ArrivalOperateRecord record);

    int insertSelective(ArrivalOperateRecord record);

    ArrivalOperateRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ArrivalOperateRecord record);

    int updateByPrimaryKey(ArrivalOperateRecord record);
}