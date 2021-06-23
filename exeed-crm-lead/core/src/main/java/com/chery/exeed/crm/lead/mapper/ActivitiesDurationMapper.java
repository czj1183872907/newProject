package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.ActivitiesDuration;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ActivitiesDurationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ActivitiesDuration record);

    int insertSelective(ActivitiesDuration record);

    ActivitiesDuration selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ActivitiesDuration record);

    int updateByPrimaryKey(ActivitiesDuration record);
}