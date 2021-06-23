package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.MetaMbm;
import com.chery.exeed.crm.lead.model.MetaMbmKey;

public interface MetaMbmMapper {
    int deleteByPrimaryKey(MetaMbmKey key);

    int insert(MetaMbm record);

    int insertSelective(MetaMbm record);

    MetaMbm selectByPrimaryKey(MetaMbmKey key);

    int updateByPrimaryKeySelective(MetaMbm record);

    int updateByPrimaryKey(MetaMbm record);
}