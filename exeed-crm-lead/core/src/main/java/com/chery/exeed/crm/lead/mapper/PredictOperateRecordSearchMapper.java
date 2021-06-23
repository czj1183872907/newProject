package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.dto.PredictOperateRecordDTO;
import com.chery.exeed.crm.lead.model.PredictOperateRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PredictOperateRecordSearchMapper extends PredictOperateRecordMapper {
    int insertPredictOperateRecord (PredictOperateRecord por);

    PredictOperateRecordDTO selectByLeadId(Integer leadId);
}
