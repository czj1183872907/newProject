package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.OfferOperateRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OfferOperateRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OfferOperateRecord record);

    int insertSelective(OfferOperateRecord record);

    OfferOperateRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OfferOperateRecord record);

    int updateByPrimaryKey(OfferOperateRecord record);
}