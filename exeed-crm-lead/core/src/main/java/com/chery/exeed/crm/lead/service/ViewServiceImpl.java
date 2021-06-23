package com.chery.exeed.crm.lead.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.chery.exeed.crm.common.constants.ViewModuleEnum;
import com.chery.exeed.crm.common.dto.*;
import com.chery.exeed.crm.common.service.CacheService;
import com.chery.exeed.crm.common.util.StringUtils;
import com.chery.exeed.crm.lead.mapper.ExViewMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @Author: zhen.liu@nttdata.com
 * @Date: 2020/12/9 16:38
 * @Description:
 */
@Service
public class ViewServiceImpl implements ViewService {

    private final Logger logger = LoggerFactory.getLogger(ViewServiceImpl.class);

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    CacheService cacheService;

    @Autowired
    ExViewMapper exViewMapper;

    @Override
    public String matchNicknameInAllSysUser(Integer userId) {
        if (userId == null) return null;

        String cacheVal = cacheService.cachePull(ViewModuleEnum.V_ALL_SYS_USER.getCode(), String.valueOf(userId));

        SysUserViewDTO sysUser = null;

        if (StringUtils.isEmpty(cacheVal)) {
            logger.debug("matchNicknameInAllSysUser-Get sys_user[{}] from db.", userId);
            sysUser = exViewMapper.selectSysUserByUserId(userId);
            if (sysUser != null) {
                cacheService.cachePush(ViewModuleEnum.V_ALL_SYS_USER.getCode(), String.valueOf(userId), JSON.toJSONString(sysUser));
            }
        } else {
            logger.debug("matchNicknameInAllSysUser-Get sys_user[{}] from cache.", userId);
            sysUser = JSON.parseObject(cacheVal, SysUserViewDTO.class);
        }

        String nickname = null;
        if (sysUser != null) nickname = sysUser.getNickname();
        return nickname;
    }

    @Override
    public String matchEntityFullNameInCampaignChannel(String channelCode) {
        if (channelCode == null) return null;

        String cacheVal = cacheService.cachePull(ViewModuleEnum.V_CAMPAIGN_CHANNEL.getCode(), String.valueOf(channelCode));

        CampaignChannelViewDTO channel = null;

        if (StringUtils.isEmpty(cacheVal)) {
            logger.debug("matchEntityFullNameInCampaignChannel-Get campaign_channel[{}] from db.", channelCode);
            channel = exViewMapper.selectCampaignChannelByChannelCode(channelCode);
            if (channel != null) {
                cacheService.cachePush(ViewModuleEnum.V_CAMPAIGN_CHANNEL.getCode(), channelCode, JSON.toJSONString(channel));
            }
        } else {
            logger.debug("matchEntityFullNameInCampaignChannel-Get campaign_channel[{}] from cache.", channelCode);
            channel = JSON.parseObject(cacheVal, CampaignChannelViewDTO.class);
        }

        String entityFullName = null;
        if (channel != null) entityFullName = channel.getEntityFullName();
        return entityFullName;
    }

    @Override
    public String matchActNameInCampaignAct(String actCode) {
        if (actCode == null) return null;

        String cacheVal = cacheService.cachePull(ViewModuleEnum.V_CAMPAIGN_ACT.getCode(), String.valueOf(actCode));

        CampaignActViewDTO act = null;

        if (StringUtils.isEmpty(cacheVal)) {
            logger.debug("matchActNameInCampaignAct-Get campaign_act[{}] from db.", actCode);
            act = exViewMapper.selectCampaignActByActCode(actCode);
            if (act != null) {
                cacheService.cachePush(ViewModuleEnum.V_CAMPAIGN_ACT.getCode(), actCode, JSON.toJSONString(act));
            }
        } else {
            logger.debug("matchActNameInCampaignAct-Get campaign_act[{}] from cache.", actCode);
            act = JSON.parseObject(cacheVal, CampaignActViewDTO.class);
        }

        String actName = null;
        if (act != null) actName = act.getActName();
        return actName;
    }

    @Override
    public String matchRegionNameInRegion(String regionId) {
        if (regionId == null) return null;

        String cacheVal = cacheService.cachePull(ViewModuleEnum.V_REGION.getCode(), String.valueOf(regionId));

        RegionViewDTO region = null;

        if (StringUtils.isEmpty(cacheVal)) {
            logger.debug("matchRegionNameInRegion-Get region[{}] from db.", regionId);
            region = exViewMapper.selectRegionByRegionId(regionId);
            if (region != null) {
                cacheService.cachePush(ViewModuleEnum.V_REGION.getCode(), regionId, JSON.toJSONString(region));
            }
        } else {
            logger.debug("matchRegionNameInRegion-Get region[{}] from cache.", regionId);
            region = JSON.parseObject(cacheVal, RegionViewDTO.class);
        }

        String regionName = null;
        if (region != null) regionName = region.getRegionName();
        return regionName;
    }

    @Override
    public String matchSeriesNameInCarSeries(String seriesCode) {
        if (seriesCode == null) return null;

        String cacheVal = cacheService.cachePull(ViewModuleEnum.V_CAR_SERIES.getCode(), null);

        List<CarSeriesViewDTO> list = null;

        if (StringUtils.isEmpty(cacheVal)) {
            logger.debug("matchSeriesNameInCarSeries-Get car_series[{}] from db.", seriesCode);
            list = exViewMapper.selectCarSeries();
            if (!CollectionUtils.isEmpty(list)) {
                cacheService.cachePush(ViewModuleEnum.V_CAR_SERIES.getCode(), null, JSON.toJSONString(list));
            }
        } else {
            logger.debug("matchSeriesNameInCarSeries-Get car_series[{}] from cache.", seriesCode);
            list = JSONArray.parseArray(cacheVal, CarSeriesViewDTO.class);
        }

        String seriesName = null;

        if (!CollectionUtils.isEmpty(list)) {
            List<CarSeriesViewDTO> collect = list.stream().filter(item -> item.getSeriesCode().equals(seriesCode)).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(collect)) {
                seriesName = collect.get(0).getSeriesName();
            }
        }
        return seriesName;
    }

    @Override
    public String matchModelNameInCarModel(String seriesCode, String modelCode) {

        if (seriesCode == null || modelCode == null) return null;

        String cacheVal = cacheService.cachePull(ViewModuleEnum.V_CAR_MODEL.getCode(), seriesCode);

        List<CarModelViewDTO> list = null;

        if (StringUtils.isEmpty(cacheVal)) {
            logger.debug("matchModelNameInCarModel-Get car_model[{}] from db.", modelCode);
            list = exViewMapper.selectCarModelBySeriesCode(seriesCode);
            if (!CollectionUtils.isEmpty(list)) {
                cacheService.cachePush(ViewModuleEnum.V_CAR_MODEL.getCode(), seriesCode, JSON.toJSONString(list));
            }
        } else {
            logger.debug("matchModelNameInCarModel-Get car_model[{}] from cache.", modelCode);
            list = JSONArray.parseArray(cacheVal, CarModelViewDTO.class);
        }

        String modelName = null;

        if (!CollectionUtils.isEmpty(list)) {
            List<CarModelViewDTO> collect = list.stream().filter(item -> item.getModelCode().equals(modelCode)).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(collect)) {
                modelName = collect.get(0).getModelName();
            }
        }
        return modelName;
    }

    @Override
    public DealerViewDTO matchDealer(String dealerCode) {
        if (dealerCode == null) return null;

        String cacheVal = cacheService.cachePull(ViewModuleEnum.V_DEALERS.getCode(), String.valueOf(dealerCode));

        DealerViewDTO dto = null;

        if (StringUtils.isEmpty(cacheVal)) {
            logger.debug("matchDealer-Get dealers[{}] from db.", dealerCode);
            dto = exViewMapper.selectDealerByDealerCode(dealerCode);
            if (dto != null) {
                cacheService.cachePush(ViewModuleEnum.V_DEALERS.getCode(), dealerCode, JSON.toJSONString(dto));
            }
        } else {
            logger.debug("matchDealer-Get dealers[{}] from cache.", dealerCode);
            dto = JSON.parseObject(cacheVal, DealerViewDTO.class);
        }
        return dto;
    }

    @Override
    public Integer matchFinalCallResultInCallSummary(Long taskId) {
        if (taskId == null) return null;

        String cacheVal = cacheService.cachePull(ViewModuleEnum.V_CALL_SUMMARY.getCode(), String.valueOf(taskId));

        List<CallSummaryViewDTO> list = null;

        if (StringUtils.isEmpty(cacheVal)) {
            logger.debug("matchFinalCallResultInCallSummary-Get call_summary[{}] from db.", taskId);
            list = exViewMapper.selectCallSummaryByTaskId(taskId);
            if (!CollectionUtils.isEmpty(list)) {
                cacheService.cachePush(ViewModuleEnum.V_CALL_SUMMARY.getCode(), String.valueOf(taskId), JSON.toJSONString(list));
            }
        } else {
            logger.debug("matchFinalCallResultInCallSummary-Get call_summary[{}] from cache.", taskId);
            list = JSONArray.parseArray(cacheVal, CallSummaryViewDTO.class);
        }

        Integer callResult = null;

        if (!CollectionUtils.isEmpty(list)) {
            CallSummaryViewDTO dto = list.stream().findFirst().get();
            callResult = dto.getCallResult();
        }
        return callResult;
    }

    @Override
    public Integer matchFirstCallResultInCallSummary(Long taskId) {
        if (taskId == null) return null;

        String cacheVal = cacheService.cachePull(ViewModuleEnum.V_CALL_SUMMARY.getCode(), String.valueOf(taskId));

        List<CallSummaryViewDTO> list = null;

        if (StringUtils.isEmpty(cacheVal)) {
            logger.debug("matchFirstCallResultInCallSummary-Get call_summary[{}] from db.", taskId);
            list = exViewMapper.selectCallSummaryByTaskId(taskId);
            if (!CollectionUtils.isEmpty(list)) {
                cacheService.cachePush(ViewModuleEnum.V_CALL_SUMMARY.getCode(), String.valueOf(taskId), JSON.toJSONString(list));
            }
        } else {
            logger.debug("matchFirstCallResultInCallSummary-Get call_summary[{}] from cache.", taskId);
            list = JSONArray.parseArray(cacheVal, CallSummaryViewDTO.class);
        }

        Integer callResult = null;

        if (!CollectionUtils.isEmpty(list)) {
            CallSummaryViewDTO dto = list.stream().sorted(Comparator.comparing(CallSummaryViewDTO::getCreateTime)).findFirst().get();
            callResult = dto.getCallResult();
        }
        return callResult;
    }
}
