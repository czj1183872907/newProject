package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.OrderOperateRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderOperateRecordSearchMapper extends OrderOperateRecordMapper {
    int insertOrderOperatRecord (OrderOperateRecord record);

    List<OrderOperateRecord> selectByCertNo(String certNo);

    List<OrderOperateRecord> selectByOperateTime(@Param("startOperateTime") String startOperateTime, @Param("endOperateTime") String endOperateTime);
}
