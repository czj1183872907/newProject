package com.chery.exeed.crm.lead.service;

import com.chery.exeed.crm.common.dto.ResponseData;
import com.chery.exeed.crm.common.service.ChannelService;
import com.chery.exeed.crm.common.util.StringUtils;
import com.chery.exeed.crm.lead.dto.LeadChannelDTO;
import com.chery.exeed.crm.lead.dto.LeadChannelInputDTO;
import com.chery.exeed.crm.lead.mapper.LeadChannelMapper;
import com.chery.exeed.crm.lead.mapper.LeadChannelSearchMapper;
import com.chery.exeed.crm.lead.model.LeadChannel;
import com.chery.exeed.ifs.common.dto.ActinstanceDTO;
import com.chery.exeed.ifs.common.dto.ChannelConfigDTO;
import com.chery.exeed.ifs.common.dto.ChannelDTO;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:xiaowei.zhu
 * 2019/1/21 18:30
 */
@RestSchema(schemaId = "lead-channel-service")
@RequestMapping(path = "/apis/lead/channel")
public class LeadChannelServiceImpl implements LeadChannelService{
    @Autowired
    private LeadChannelSearchMapper leadChannelSearchMapper;
    @Autowired
    private LeadChannelMapper leadChannelMapper;
    @Autowired
    private ChannelService channelService;

    @Override
    @RequestMapping(
            value = {"/list"},
            method = {RequestMethod.GET}
    )
    public ResponseData<List<LeadChannelDTO>> getLeadChannelList(Integer leadId) {
        List<LeadChannelDTO> list = leadChannelSearchMapper.getLeadChannelList(leadId);
        if(list!=null){
            for(LeadChannelDTO obj:list){
                String channelCode = obj.getChannelCode();
                if(!StringUtils.isEmpty(channelCode)){
                    ChannelDTO channel = channelService.getChannelByCode(channelCode);
                    if(channel!=null){
                        obj.setSubChannelName(channel.getName());
                    }
                }
                String actCode = obj.getActCode();
                if(!StringUtils.isEmpty(actCode)){
                    ActinstanceDTO actinstance = channelService.getActinstanceByCode(actCode);
                    if(actinstance!=null){
                        obj.setChannelName(actinstance.getName());
                    }
                }
            }
        }
        return ResponseData.success(list);
    }


    @Override
    @RequestMapping(
            value = {"/getChannelList"},
            method = {RequestMethod.GET}
    )
    public ResponseData<List<ChannelDTO>> getChannelList() {
        List<ChannelDTO> list = channelService.getChannelList();
        return ResponseData.success(list);
    }

    @Override
    @RequestMapping(
            value = {"/getTrustChannelList"},
            method = {RequestMethod.GET}
    )
    public ResponseData<List<ChannelConfigDTO>> getTrustChannelList() {
        List<ChannelConfigDTO> list = channelService.getTrustChannelList();
        return ResponseData.success(list);
    }

    @Override
    @RequestMapping(
            value = {"/insertTrustChannelList"},
            method = {RequestMethod.GET}
    )
    public ResponseData<Boolean> insertTrustChannelList(@RequestBody ChannelConfigDTO channelConfigDTO) {
        if(channelConfigDTO == null || channelConfigDTO.getChannelCode() == null || channelConfigDTO.getCreateTask() == null || channelConfigDTO.getIsSend() == null){
            return ResponseData.fail(10010,"参数异常");
        }
        Boolean result = channelService.insertTrustChannelList(channelConfigDTO);
        return ResponseData.success(result);
    }


    @Override
    @RequestMapping(
            value = {"/getActinstanceListForStore"},
            method = {RequestMethod.POST}
    )
    public ResponseData<List<ActinstanceDTO>> getActinstanceListForStore(String code,String channelCode) {
        List<ActinstanceDTO> list = channelService.getActinstanceListForStore(code,channelCode);
        return ResponseData.success(list);
    }

    @Override
    @RequestMapping(
            value = {"/getChannelListForStore"},
            method = {RequestMethod.GET}
    )
    public ResponseData<List<ChannelDTO>> getChannelListForStore(String code) {
        List<ChannelDTO> list = channelService.getChannelListForStore(code);
        return ResponseData.success(list);
    }

    @Override
    @RequestMapping(
            value = {"/create"},
            method = {RequestMethod.POST}
    )
    public ResponseData<Integer> createLeadChannel(@RequestBody LeadChannelInputDTO leadChannel) {
        Map<String,Object> paramMap = new HashMap<>(2);
        paramMap.put("leadId",leadChannel.getLeadId());
        paramMap.put("subChannelId",leadChannel.getChannelId());
        Integer num = leadChannelSearchMapper.selectByChannelCode(paramMap);
        Integer id = null;
        if(num==0) {
            LeadChannel entity = new LeadChannel();
            entity.setLeadId(leadChannel.getLeadId());
            entity.setActCode(leadChannel.getActCode());
            entity.setChannelCode(leadChannel.getChannelCode());
            entity.setCampaignCode(leadChannel.getCampaignCode());
            entity.setCreateDate(new Date());
            leadChannelMapper.insert(entity);
            id = entity.getId();
        }
        return ResponseData.success(id);
    }

    @Override
    @RequestMapping(
            value = {"/update"},
            method = {RequestMethod.POST}
    )
    public ResponseData<Boolean> updateLeadChannel(@RequestBody LeadChannelInputDTO leadChannel) {
        Map<String,Object> paramMap = new HashMap<>(2);
        paramMap.put("leadId",leadChannel.getLeadId());
        paramMap.put("subChannelId",leadChannel.getChannelId());
        Integer num = leadChannelSearchMapper.selectByChannelCode(paramMap);
        if(num==0) {
            return ResponseData.success(false);
        }
        LeadChannel entity = new LeadChannel();
        entity.setId(leadChannel.getId());
        entity.setActCode(leadChannel.getActCode());
        entity.setChannelCode(leadChannel.getChannelCode());
        entity.setCampaignCode(leadChannel.getCampaignCode());
        leadChannelMapper.updateByPrimaryKeySelective(entity);
        return ResponseData.success(true);
    }

    @Override
    @RequestMapping(
            value = {"/batchChannelList"},
            method = {RequestMethod.POST}
    )
    public ResponseData<Map<String, List<LeadChannelDTO>>> batchToGetChannelList(@RequestBody List<Integer> list) {
        Map map = new HashMap<String, List<LeadChannelDTO>>();
        if(list != null && list.size() > 0){
            for (Integer leadId: list ) {
                List<LeadChannelDTO> leadChannelList = leadChannelSearchMapper.getLeadChannelList(leadId);
                for(LeadChannelDTO obj:leadChannelList){
                    String channelCode = obj.getChannelCode();
                    if(!StringUtils.isEmpty(channelCode)){
                        ChannelDTO channel = channelService.getChannelByCode(channelCode);
                        if(channel!=null){
                            obj.setSubChannelName(channel.getName());
                        }
                    }
                }
                map.put(leadId.toString(),leadChannelList);
            }
        }
        return ResponseData.success(map);
    }

}
