package com.chery.exeed.crm.lead.service;

import com.chery.exeed.crm.common.dto.ResponseData;
import com.chery.exeed.crm.lead.dto.LeadChannelDTO;
import com.chery.exeed.crm.lead.dto.LeadChannelInputDTO;
import com.chery.exeed.ifs.common.dto.ActinstanceDTO;
import com.chery.exeed.ifs.common.dto.ChannelConfigDTO;
import com.chery.exeed.ifs.common.dto.ChannelDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * Author:xiaowei.zhu
 * 2019/1/21 18:31
 */
public interface LeadChannelService {
    ResponseData<List<LeadChannelDTO>> getLeadChannelList(Integer leadId);

    @RequestMapping(
            value = {"/getActinstanceListForStore"},
            method = {RequestMethod.GET}
    )
    ResponseData<List<ActinstanceDTO>> getActinstanceListForStore(String code,String channelCode);

    @RequestMapping(
            value = {"/getChannelListForStore"},
            method = {RequestMethod.GET}
    )
    ResponseData<List<ChannelDTO>> getChannelListForStore(String code);

    ResponseData<Integer> createLeadChannel(LeadChannelInputDTO leadChannel);

    ResponseData<Boolean> updateLeadChannel(LeadChannelInputDTO leadChannel);

    ResponseData<Map<String,List<LeadChannelDTO>>> batchToGetChannelList(List<Integer> leadIdList);

    ResponseData<List<ChannelDTO>> getChannelList();

    ResponseData<List<ChannelConfigDTO>> getTrustChannelList();

    ResponseData<Boolean> insertTrustChannelList(ChannelConfigDTO channelConfigDTO);
}
