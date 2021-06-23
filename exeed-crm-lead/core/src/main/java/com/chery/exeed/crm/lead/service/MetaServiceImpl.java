package com.chery.exeed.crm.lead.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.chery.exeed.crm.common.constants.ChooseReasonModuleTypeEnum;
import com.chery.exeed.crm.common.constants.KnowChannelModuleTypeEnum;
import com.chery.exeed.crm.common.constants.MbmModuleTypeEnum;
import com.chery.exeed.crm.common.dto.ResponseData;
import com.chery.exeed.crm.common.util.StringUtils;
import com.chery.exeed.crm.lead.dto.MetaChooseReasonDTO;
import com.chery.exeed.crm.lead.dto.MetaDataDTO;
import com.chery.exeed.crm.lead.dto.MetaKnowChannelDTO;
import com.chery.exeed.crm.lead.dto.MetaMbmDTO;
import com.chery.exeed.crm.lead.mapper.MetaSearchMapper;
import com.chery.exeed.crm.lead.model.MetaChooseReason;
import com.chery.exeed.crm.lead.model.MetaKnowChannel;
import com.chery.exeed.crm.lead.model.MetaMbm;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.chery.exeed.crm.common.constants.Constants.CACHE_META_KEY;

/**
 * Author:xiaowei.zhu
 * 2019/1/22 11:34
 */
@RestSchema(schemaId = "meta-service")
@RequestMapping(path = "/apis/lead/meta")
public class MetaServiceImpl implements MetaService {

    private final Logger logger = LoggerFactory.getLogger(MetaServiceImpl.class);

    @Autowired
    private MetaSearchMapper metaSearchMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void initMetaCommon(){
        Map<String,Object> paramMap = new HashMap<>();
        List<MetaDataDTO> commonMetaList = metaSearchMapper.getCommonMetaList(paramMap);
    }

    @Override
    @RequestMapping(
            value = {"/common"},
            method = {RequestMethod.GET}
    )
    public ResponseData<List<MetaDataDTO>> getCommonMetaList(String metaName, Integer metaCode) {
        Map<String,Object> paramMap = new HashMap<>();
        if(metaCode!=null) {
            paramMap.put("metaCode", metaCode);
        }
        if(metaName!=null) {
            paramMap.put("metaName", metaName);
        }
        List<MetaDataDTO> commonMetaList = metaSearchMapper.getCommonMetaList(paramMap);
        return ResponseData.success(commonMetaList);
    }

    @Override
    @RequestMapping(
            value = {"/lead"},
            method = {RequestMethod.GET}
    )
    public ResponseData<List<MetaDataDTO>> getLeadMetaList(String metaName, Integer metaCode) {
        Map<String,Object> paramMap = new HashMap<>();
        if(metaCode!=null) {
            paramMap.put("metaCode", metaCode);
        }
        if(metaName!=null) {
            paramMap.put("metaName", metaName);
        }
        List<MetaDataDTO> leadMetaList = metaSearchMapper.getLeadMetaList(paramMap);
        return ResponseData.success(leadMetaList);
    }

    @Override
    @RequestMapping(
            value = {"/getMetaListByName"},
            method = {RequestMethod.GET}
    )
    @Cacheable(value ="getMetaListByName", key = "#metaName")
    public ResponseData<List<MetaDataDTO>> getMetaListByName(String[] metaName) {
        Map<String,Object> pram = new HashMap<>();;
        List<String> metaNameList = null;
        if(metaName!=null && metaName.length > 0) {
            metaNameList = Arrays.asList(metaName);
        }
        pram.put("metaNameList", metaNameList);
        List<MetaDataDTO> metaList = metaSearchMapper.getMetaList(pram);
        return ResponseData.success(metaList);
    }

    @Override
    public String metaLeadTranslate(String metaName, Integer metaCode) {

        if (StringUtils.isEmpty(metaName) || metaCode == null) return null;

        String redisKey = CACHE_META_KEY.replace("${metaName}", metaName);

        String value = stringRedisTemplate.opsForValue().get(redisKey);

        List<MetaDataDTO> list = null;
        if (StringUtils.isEmpty(value)) {
            logger.debug("Get metaLead[{}] from db.", metaName);
            list = metaSearchMapper.getMetaLeadByMetaName(metaName);

            stringRedisTemplate.opsForValue().set(redisKey, JSON.toJSONString(list), 30L, TimeUnit.MINUTES);
        } else {
            logger.debug("Get metaLead[{}] from cache.", metaName);
            list = JSONArray.parseArray(value, MetaDataDTO.class);
        }

        String result = null;

        for (MetaDataDTO metaDTO : list) {
            if (metaDTO.getMetaCode().equals(metaCode)) {
                result = metaDTO.getDescription();
                break;
            }
        }
        return result;
    }

    @Override
    public String mdmMetaLeadTranslate(String metaName, Integer metaCode) {

        if (StringUtils.isEmpty(metaName) || metaCode == null) return null;

        String redisKey = CACHE_META_KEY.replace("${metaName}", metaName);

        String value = stringRedisTemplate.opsForValue().get(redisKey);

        List<MetaDataDTO> list = null;
        if (StringUtils.isEmpty(value)) {
            logger.debug("Get mdmMetaLead[{}] from db.", metaName);
            list = metaSearchMapper.getMdmMetaLeadByMetaName(metaName);

            stringRedisTemplate.opsForValue().set(redisKey, JSON.toJSONString(list), 30L, TimeUnit.MINUTES);
        } else {
            logger.debug("Get mdmMetaLead[{}] from cache.", metaName);
            list = JSONArray.parseArray(value, MetaDataDTO.class);
        }

        String result = null;

        for (MetaDataDTO metaDTO : list) {
            if (metaDTO.getMetaCode().equals(metaCode)) {
                result = metaDTO.getDescription();
                break;
            }
        }
        return result;
    }

    @Override
    @RequestMapping(
            value = {"/meta-mbm-list"},
            method = {RequestMethod.GET}
    )
    public List<MetaMbmDTO> listMetaMbm() {

        String cacheKey = CACHE_META_KEY.replace("${metaName}", "metaMbm");

        String cacheVal = stringRedisTemplate.opsForValue().get(cacheKey);

        List<MetaMbmDTO> result = null;

        if (!StringUtils.isEmpty(cacheVal)) {
            logger.info("get metaMbm from cache");

            result = JSONArray.parseArray(cacheVal, MetaMbmDTO.class);

            return result;
        }

        logger.info("get metaMbm from db");

        List<MetaMbm> list = metaSearchMapper.getAllMetaMbm();

        result = this.formatMetaMbm(list, null, MbmModuleTypeEnum.MANUFACTURER.getCode());

        stringRedisTemplate.opsForValue().set(cacheKey, JSON.toJSONString(result), 30L, TimeUnit.MINUTES);

        return result;
    }

    private List<MetaMbmDTO> formatMetaMbm(List<MetaMbm> list, String parentModule, String moduleType) {

        List<MetaMbmDTO> result = null;

        if (CollectionUtils.isEmpty(list)) {
            return result;
        }

        if (MbmModuleTypeEnum.MANUFACTURER.getCode().equals(moduleType)) {

            Map<String, List<MetaMbm>> collect = list.stream().collect(Collectors.groupingBy(MetaMbm::getManufacturer));

            result = collect.entrySet().stream().map(item -> {
                MetaMbmDTO dto = new MetaMbmDTO();
                dto.setModule(item.getKey());
                dto.setModuleName(item.getValue().get(0).getManufacturerName());
                dto.setModuleType(moduleType);
                dto.setModuleTypeName((MbmModuleTypeEnum.MANUFACTURER.getValue()));
                dto.setSubList(this.formatMetaMbm(list, item.getKey(), MbmModuleTypeEnum.BRAND.getCode()));
                return dto;
            }).collect(Collectors.toList());
        }

        if (MbmModuleTypeEnum.BRAND.getCode().equals(moduleType)) {

            Map<String, List<MetaMbm>> collect = list.stream()
                    .filter(item -> parentModule.equals(item.getManufacturer()))
                    .collect(Collectors.groupingBy(MetaMbm::getBrand));

            result = collect.entrySet().stream().map(item -> {
                MetaMbmDTO dto = new MetaMbmDTO();
                dto.setModule(item.getKey());
                dto.setModuleName(item.getValue().get(0).getBrandName());
                dto.setModuleType(moduleType);
                dto.setModuleTypeName((MbmModuleTypeEnum.BRAND.getValue()));
                dto.setSubList(this.formatMetaMbm(list, item.getKey(), MbmModuleTypeEnum.MODEL.getCode()));
                return dto;
            }).collect(Collectors.toList());
        }

        if (MbmModuleTypeEnum.MODEL.getCode().equals(moduleType)) {

            Map<String, List<MetaMbm>> collect = list.stream()
                    .filter(item -> parentModule.equals(item.getBrand()))
                    .collect(Collectors.groupingBy(MetaMbm::getModel));

            result = collect.entrySet().stream().map(item -> {
                MetaMbmDTO dto = new MetaMbmDTO();
                dto.setModule(item.getKey());
                dto.setModuleName(item.getValue().get(0).getModelName());
                dto.setModuleType(moduleType);
                dto.setModuleTypeName((MbmModuleTypeEnum.MODEL.getValue()));
                return dto;
            }).collect(Collectors.toList());
        }

        return result;
    }

    @Override
    @RequestMapping(
            value = {"/meta-choose-reason-list"},
            method = {RequestMethod.GET}
    )
    public List<MetaChooseReasonDTO> listMetaChooseReason() {
        String cacheKey = CACHE_META_KEY.replace("${metaName}", "metaChooseReason");

        String cacheVal = stringRedisTemplate.opsForValue().get(cacheKey);

        List<MetaChooseReasonDTO> result = null;

        if (!StringUtils.isEmpty(cacheVal)) {
            logger.info("get metaChooseReason from cache");

            result = JSONArray.parseArray(cacheVal, MetaChooseReasonDTO.class);

            return result;
        }

        logger.info("get metaChooseReason from db");

        List<MetaChooseReason> list = metaSearchMapper.getAllMetaChooseReason();

        result = this.formatMetaChooseReason(list, null, ChooseReasonModuleTypeEnum.CHOOSE_REASON.getCode());

        stringRedisTemplate.opsForValue().set(cacheKey, JSON.toJSONString(result), 30L, TimeUnit.MINUTES);

        return result;
    }

    private List<MetaChooseReasonDTO> formatMetaChooseReason(List<MetaChooseReason> list, String parentModule, String moduleType) {

        List<MetaChooseReasonDTO> result = null;

        if (CollectionUtils.isEmpty(list)) {
            return result;
        }

        if (ChooseReasonModuleTypeEnum.CHOOSE_REASON.getCode().equals(moduleType)) {

            Map<String, List<MetaChooseReason>> collect = list.stream().collect(Collectors.groupingBy(MetaChooseReason::getChooseReason));

            result = collect.entrySet().stream().map(item -> {
                MetaChooseReasonDTO dto = new MetaChooseReasonDTO();
                dto.setModule(item.getKey());
                dto.setModuleName(item.getValue().get(0).getChooseReasonDesc());
                dto.setModuleType(moduleType);
                dto.setModuleTypeName((ChooseReasonModuleTypeEnum.CHOOSE_REASON.getValue()));
                dto.setSubList(this.formatMetaChooseReason(list, item.getKey(), ChooseReasonModuleTypeEnum.CHOOSE_REASON_DETAIL.getCode()));
                return dto;
            }).collect(Collectors.toList());
        }

        if (ChooseReasonModuleTypeEnum.CHOOSE_REASON_DETAIL.getCode().equals(moduleType)) {

            Map<String, List<MetaChooseReason>> collect = list.stream()
                    .filter(item -> parentModule.equals(item.getChooseReason()) && item.getChooseReasonDesc() != null)
                    .collect(Collectors.groupingBy(MetaChooseReason::getChooseReasonDetail));

            result = collect.entrySet().stream().map(item -> {
                MetaChooseReasonDTO dto = new MetaChooseReasonDTO();
                dto.setModule(item.getKey());
                dto.setModuleName(item.getValue().get(0).getChooseReasonDetailDesc());
                dto.setModuleType(moduleType);
                dto.setModuleTypeName((ChooseReasonModuleTypeEnum.CHOOSE_REASON_DETAIL.getValue()));
                return dto;
            }).collect(Collectors.toList());
        }

        return result;
    }

    @Override
    @RequestMapping(
            value = {"/meta-know-channel-list"},
            method = {RequestMethod.GET}
    )
    public List<MetaKnowChannelDTO> listMetaKnowChannel() {
        String cacheKey = CACHE_META_KEY.replace("${metaName}", "metaKnowChannel");

        String cacheVal = stringRedisTemplate.opsForValue().get(cacheKey);

        List<MetaKnowChannelDTO> result = null;

        if (!StringUtils.isEmpty(cacheVal)) {
            logger.info("get metaKnowChannel from cache");

            result = JSONArray.parseArray(cacheVal, MetaKnowChannelDTO.class);

            return result;
        }

        logger.info("get metaKnowChannel from db");

        List<MetaKnowChannel> list = metaSearchMapper.getAllMetaKnowChannel();

        result = this.formatMetaKnowChannel(list, null, KnowChannelModuleTypeEnum.KNOW_CHANNEL.getCode());

        stringRedisTemplate.opsForValue().set(cacheKey, JSON.toJSONString(result), 30L, TimeUnit.MINUTES);

        return result;
    }

    private List<MetaKnowChannelDTO> formatMetaKnowChannel(List<MetaKnowChannel> list, String parentModule, String moduleType) {

        List<MetaKnowChannelDTO> result = null;

        if (CollectionUtils.isEmpty(list)) {
            return result;
        }

        if (KnowChannelModuleTypeEnum.KNOW_CHANNEL.getCode().equals(moduleType)) {

            Map<String, List<MetaKnowChannel>> collect = list.stream().collect(Collectors.groupingBy(MetaKnowChannel::getKnowChannel));

            result = collect.entrySet().stream().map(item -> {
                MetaKnowChannelDTO dto = new MetaKnowChannelDTO();
                dto.setModule(item.getKey());
                dto.setModuleName(item.getValue().get(0).getKnowChannelDesc());
                dto.setModuleType(moduleType);
                dto.setModuleTypeName((KnowChannelModuleTypeEnum.KNOW_CHANNEL.getValue()));
                dto.setSubList(this.formatMetaKnowChannel(list, item.getKey(), KnowChannelModuleTypeEnum.KNOW_CHANNEL_DETAIL.getCode()));
                return dto;
            }).collect(Collectors.toList());
        }

        if (KnowChannelModuleTypeEnum.KNOW_CHANNEL_DETAIL.getCode().equals(moduleType)) {

            Map<String, List<MetaKnowChannel>> collect = list.stream()
                    .filter(item -> parentModule.equals(item.getKnowChannel()) && item.getKnowChannelDetail() != null)
                    .collect(Collectors.groupingBy(MetaKnowChannel::getKnowChannelDetail));

            result = collect.entrySet().stream().map(item -> {
                MetaKnowChannelDTO dto = new MetaKnowChannelDTO();
                dto.setModule(item.getKey());
                dto.setModuleName(item.getValue().get(0).getKnowChannelDetailDesc());
                dto.setModuleType(moduleType);
                dto.setModuleTypeName((KnowChannelModuleTypeEnum.KNOW_CHANNEL_DETAIL.getValue()));
                return dto;
            }).collect(Collectors.toList());
        }

        return result;
    }

    @Override
    public MetaChooseReason metaChooseReasonTranslate(String chooseReason, String chooseReasonDetail) {
        if (StringUtils.isEmpty(chooseReason)) {
            return null;
        }

        List<MetaChooseReasonDTO> metaList = this.listMetaChooseReason();

        List<MetaChooseReasonDTO> collectList = metaList.stream()
                .filter(item -> item.getModule().equals(chooseReason))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(collectList)) {
            return null;
        }

        MetaChooseReasonDTO dto = collectList.get(0);

        MetaChooseReason result = new MetaChooseReason();

        result.setChooseReasonDesc(dto.getModuleName());
        if (!CollectionUtils.isEmpty(dto.getSubList()) && !StringUtils.isEmpty(chooseReasonDetail)) {
            List<MetaChooseReasonDTO> detailList = dto.getSubList().stream()
                    .filter(item -> chooseReasonDetail.equals(item.getModule()))
                    .collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(detailList)) {
                result.setChooseReasonDetailDesc(detailList.get(0).getModuleName());
            }
        }
        return result;
    }

    @Override
    public MetaKnowChannel metaKnowChannelTranslate(String knowChannel, String knowChannelDetail) {
        if (StringUtils.isEmpty(knowChannel)) {
            return null;
        }

        List<MetaKnowChannelDTO> metaList = this.listMetaKnowChannel();

        List<MetaKnowChannelDTO> collectList = metaList.stream()
                .filter(item -> item.getModule().equals(knowChannel))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(collectList)) {
            return null;
        }

        MetaKnowChannelDTO dto = collectList.get(0);

        MetaKnowChannel result = new MetaKnowChannel();

        result.setKnowChannelDesc(dto.getModuleName());
        if (!CollectionUtils.isEmpty(dto.getSubList()) && !StringUtils.isEmpty(knowChannelDetail)) {
            List<MetaKnowChannelDTO> detailList = dto.getSubList().stream()
                    .filter(item -> knowChannelDetail.equals(item.getModule()))
                    .collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(detailList)) {
                result.setKnowChannelDetailDesc(detailList.get(0).getModuleName());
            }
        }
        return result;
    }
}
