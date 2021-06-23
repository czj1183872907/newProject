package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.DistributeOperateRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DistributeOperateRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributeOperateRecord record);

    int insertSelective(DistributeOperateRecord record);

    DistributeOperateRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DistributeOperateRecord record);

    int updateByPrimaryKey(DistributeOperateRecord record);
}