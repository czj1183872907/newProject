package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.MetaChooseReason;
import com.chery.exeed.crm.lead.model.MetaChooseReasonKey;

public interface MetaChooseReasonMapper {
    int deleteByPrimaryKey(MetaChooseReasonKey key);

    int insert(MetaChooseReason record);

    int insertSelective(MetaChooseReason record);

    MetaChooseReason selectByPrimaryKey(MetaChooseReasonKey key);

    int updateByPrimaryKeySelective(MetaChooseReason record);

    int updateByPrimaryKey(MetaChooseReason record);
}