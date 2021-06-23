package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.CampaignChannel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CampaignChannelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CampaignChannel record);

    int insertSelective(CampaignChannel record);

    CampaignChannel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CampaignChannel record);

    int updateByPrimaryKey(CampaignChannel record);
}