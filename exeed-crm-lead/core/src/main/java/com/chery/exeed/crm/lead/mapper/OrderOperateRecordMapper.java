package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.OrderOperateRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderOperateRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderOperateRecord record);

    int insertSelective(OrderOperateRecord record);

    OrderOperateRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderOperateRecord record);

    int updateByPrimaryKey(OrderOperateRecord record);
}