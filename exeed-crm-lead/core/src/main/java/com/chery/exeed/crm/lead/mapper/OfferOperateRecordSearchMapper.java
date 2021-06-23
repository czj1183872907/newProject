package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.OfferOperateRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OfferOperateRecordSearchMapper extends OfferOperateRecordMapper {

    int insertOfferOperation(OfferOperateRecord oor);
}
