package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.PredictOperateRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PredictOperateRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PredictOperateRecord record);

    int insertSelective(PredictOperateRecord record);

    PredictOperateRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PredictOperateRecord record);

    int updateByPrimaryKey(PredictOperateRecord record);
}