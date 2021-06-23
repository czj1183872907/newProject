package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.DefeatOperateRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DefeatOperateRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DefeatOperateRecord record);

    int insertSelective(DefeatOperateRecord record);

    DefeatOperateRecord selectByPrimaryKey(Integer id);

    Integer queryLoseTypeCount(Integer loseType);

    String queryLoseTypeName(Integer loseType);

    int updateByPrimaryKeySelective(DefeatOperateRecord record);

    int updateByPrimaryKey(DefeatOperateRecord record);
}