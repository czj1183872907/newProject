package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.model.PreLeadImport;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PreLeadImportMapper {
    int insert(PreLeadImport record);

    int insertSelective(PreLeadImport record);

    int deletePreLeadImport();

    List<PreLeadImport> getAllPreLeadImportList();
}