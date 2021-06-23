package com.chery.exeed.crm.lead.service;

import com.chery.exeed.crm.common.dto.ResponseData;
import com.chery.exeed.crm.lead.dto.MetaChooseReasonDTO;
import com.chery.exeed.crm.lead.dto.MetaDataDTO;
import com.chery.exeed.crm.lead.dto.MetaKnowChannelDTO;
import com.chery.exeed.crm.lead.dto.MetaMbmDTO;
import com.chery.exeed.crm.lead.model.MetaChooseReason;
import com.chery.exeed.crm.lead.model.MetaKnowChannel;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

import java.util.List;

/**
 * Author:xiaowei.zhu
 * 2019/1/22 11:30
 */
public interface MetaService {
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "Integer",name = "metaCode", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "String",name = "metaName", required = false)
    })
    ResponseData<List<MetaDataDTO>> getCommonMetaList(String metaName,Integer metaCode);

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "Integer",name = "metaCode", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "String",name = "metaName", required = false)
    })
    ResponseData<List<MetaDataDTO>> getLeadMetaList(String metaName,Integer metaCode);

    ResponseData<List<MetaDataDTO>> getMetaListByName(String[] metaName);


    String metaLeadTranslate(String metaName, Integer metaCode);

    String mdmMetaLeadTranslate(String metaName, Integer metaCode);

    List<MetaMbmDTO> listMetaMbm();

    List<MetaChooseReasonDTO> listMetaChooseReason();

    List<MetaKnowChannelDTO> listMetaKnowChannel();

    MetaChooseReason metaChooseReasonTranslate(String chooseReason, String chooseReasonDetail);

    MetaKnowChannel metaKnowChannelTranslate(String knowChannel, String knowChannelDetail);

}
