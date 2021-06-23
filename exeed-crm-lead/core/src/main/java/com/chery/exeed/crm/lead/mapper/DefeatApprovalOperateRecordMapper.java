package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.DefeatApprovalOperateRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DefeatApprovalOperateRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DefeatApprovalOperateRecord record);

    int insertSelective(DefeatApprovalOperateRecord record);

    DefeatApprovalOperateRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DefeatApprovalOperateRecord record);

    int updateByPrimaryKey(DefeatApprovalOperateRecord record);
}