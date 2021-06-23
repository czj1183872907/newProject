package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.Campaign;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CampaignMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Campaign record);

    int insertSelective(Campaign record);

    Campaign selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Campaign record);

    int updateByPrimaryKey(Campaign record);
}