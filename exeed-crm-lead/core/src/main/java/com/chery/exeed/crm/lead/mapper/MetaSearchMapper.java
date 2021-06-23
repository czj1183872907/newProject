package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.dto.MetaDataDTO;
import com.chery.exeed.crm.lead.model.MetaChooseReason;
import com.chery.exeed.crm.lead.model.MetaKnowChannel;
import com.chery.exeed.crm.lead.model.MetaMbm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MetaSearchMapper {
    List<MetaDataDTO> getCommonMetaList(Map<String,Object> paramMap);
    List<MetaDataDTO> getLeadMetaList(Map<String,Object> paramMap);

    List<MetaDataDTO> getMetaList(Map<String,Object> param);

    List<MetaDataDTO> getMetaLeadByMetaName(@Param("metaName") String metaName);

    List<MetaDataDTO> getMdmMetaLeadByMetaName(@Param("metaName") String metaName);

    List<MetaMbm> getAllMetaMbm();

    List<MetaChooseReason> getAllMetaChooseReason();

    List<MetaKnowChannel> getAllMetaKnowChannel();
}