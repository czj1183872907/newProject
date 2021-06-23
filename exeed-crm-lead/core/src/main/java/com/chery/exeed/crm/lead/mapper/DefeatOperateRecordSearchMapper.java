package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.dto.DefeatOperateRecordDTO;
import com.chery.exeed.crm.lead.model.DefeatOperateRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DefeatOperateRecordSearchMapper extends DefeatOperateRecordMapper {

    int insertDefeatOperateRecord (DefeatOperateRecord record);

    List<Map<String,Object>> getLoseTypeList(Integer parentCode);

    DefeatOperateRecordDTO queryByLeadId(@Param("leadId") Integer leadId);
}