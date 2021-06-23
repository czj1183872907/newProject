package com.chery.exeed.crm.lead.service;

import com.chery.exeed.crm.common.dto.ResponseData;
import com.chery.exeed.crm.lead.dto.OutCallBlackListDTO;

import java.util.List;
import java.util.Map;

public interface OutCallBlackListService {
    //新增外呼黑名单信息
    ResponseData<Integer> insertOutCallBlackList(String phone, String remarks);

    //删除外呼黑名单信息
    ResponseData<Integer> deleteOutCallBlackListById(Integer id);

    //查询外呼黑名单信息
    ResponseData<Map<String,Object>> getOutCallBlackList(String phone, Integer pageNo, Integer pageSize);

    //呼叫时判断是否在黑名单中
    ResponseData<Boolean> callLeadTask(Integer leadId,String phone);
}
