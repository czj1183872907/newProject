package com.chery.exeed.crm.lead.service;

import com.chery.exeed.crm.common.dto.DealerDTO;
import com.chery.exeed.crm.common.dto.PaginationResult;
import com.chery.exeed.crm.common.dto.ResponseData;
import com.chery.exeed.crm.lead.dto.*;
import com.chery.exeed.crm.lead.model.OrderOperateRecord;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.web.bind.annotation.RequestBody;
import com.chery.exeed.crm.lead.dto.LeadOwnerDTO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public interface LeadService {


    ResponseData<PaginationResult<LeadResultDTO>> list(LeadDTO leadDTO);

    ResponseData<Integer> unAssignLead(@RequestBody LeadDTO leadDTO);

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "Integer",name = "leadId", required = true)
    })
    ResponseData<LeadResultDTO> detail(Integer leadId);

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "Integer",name = "leadId", required = true)
    })
    ResponseData<LeadDTO> detailForUpdate(Integer leadId);

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String",name = "leadIds", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String",name = "dealerId", required = true)
    })
    ResponseData distributeLead(String leadIds, String dealerId);

//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "query", dataType = "List",name = "leadDealerList", required = false)
//    })
    ResponseData<Boolean> distributeLeadAll(List<LeadDealerDTO> leadDealerList);

    ResponseData<Boolean> assign(Integer leadId, Integer userId);

    ResponseData<Integer> batchAssign(Integer userId, Integer num);

    /**
     * 接受线索
     * @param leadId 线索ID
     * @return
     */
    ResponseData<Boolean> pass(Integer leadId);

    /**
     * 拒绝线索
     * @param leadId 线索ID
     * @param reason 拒绝原因
     * @return
     */
    ResponseData<Boolean> reject(Integer leadId, String reason,Integer rejectReasonType);

    ResponseData<Integer> create(@RequestBody LeadDTO lead);

    ResponseData<Boolean> update(LeadDTO lead);

    ResponseData<PaginationResult<LeadResultDTO>> getLeadListByCustId(LeadDTO lead);

    ResponseData<List<CityDTO>> getCityByName(String cityName);

    ResponseData<List<LeadResultDTO>> listExportLead(ExportLeadSearchDTO exportLeadSearchDTO);

    ResponseData<List<LeadDTO>> getLeadListByDealerIdAndPhone(String dealerId, String phone);


    ResponseData<PaginationResult<LeadResultDTO>> listByAssistant(LeadDTO leadDTO);

    ResponseData<LeadDTO> detailForUpdateByAssistant(Integer leadId);

    ResponseData<PaginationResult<LeadResultDTO>> advancedQuery(AdvancedQueryDTO advancedQueryDTO);

    ResponseData<PaginationResult<LeadResultDTO>> otherLeadListByAssistant(@RequestBody LeadDTO lead);

    ResponseData<Boolean> expired(Integer leadId);

    ResponseData<Boolean> updateLeadToTimeout(LeadDTO lead);


    ResponseData<Boolean> distribution(LeadOwnerDTO leadOwnerDTO);

    ResponseData<PaginationResult<LeadResultDTO>> listByBigManagent(LeadDTO leadDTO);

    ResponseData<List<DealerDTO>> derlerInfoExport();


    List<DealerDTO> selectDealerName();

    List<DealerDTO> selectCarType();

    String selectDealer(String currentUserName);

    ResponseData<List<OrderOperateRecord>> selectByOperateTime(String startOperateTime, String endOperateTime);

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "Integer",name = "currentUserId", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String",name = "leadStatus", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String",name = "oldDealerId", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String",name = "newDealerId", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer",name = "type", required = true)
    })
    ResponseData<Boolean> leadChange( Integer currentUserId, String leadStatus, String oldDealerId, String newDealerId, Integer type);

    ResponseData<Map<String, Object>> leadChangeList(Integer pageSize, Integer pageNo, String beginTime, String endTime);
}


