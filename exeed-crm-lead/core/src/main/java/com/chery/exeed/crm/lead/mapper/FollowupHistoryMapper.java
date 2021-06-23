package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.FollowupHistory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FollowupHistoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FollowupHistory record);

    int insertSelective(FollowupHistory record);

    FollowupHistory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FollowupHistory record);

    int updateByPrimaryKey(FollowupHistory record);
}