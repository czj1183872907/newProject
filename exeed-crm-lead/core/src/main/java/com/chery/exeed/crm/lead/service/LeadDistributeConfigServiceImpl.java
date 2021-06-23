package com.chery.exeed.crm.lead.service;

import com.chery.exeed.crm.common.dto.DealerDTO;
import com.chery.exeed.crm.common.dto.ResponseData;
import com.chery.exeed.crm.common.service.DealerService;
import com.chery.exeed.crm.common.service.RegionService;
import com.chery.exeed.crm.common.util.SessionHelper;
import com.chery.exeed.crm.common.util.StringUtils;
import com.chery.exeed.crm.lead.dto.ImportDTO;
import com.chery.exeed.crm.lead.mapper.LeadDistributeConfigMapper;
import com.chery.exeed.crm.lead.mapper.LeadDistributeConfigSearchMapper;
import com.chery.exeed.crm.lead.model.LeadDistributeConfig;
import com.chery.exeed.ifs.common.model.Region;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

/**
 * Author:xiaowei.zhu
 * 2019/6/18 18:55
*/


@Transactional
@RestSchema(schemaId = "lead-config-service")
@RequestMapping(path = "/apis/lead/config/")
public class LeadDistributeConfigServiceImpl implements LeadDistributeConfigService{
    private static Logger logger = LoggerFactory.getLogger(LeadDistributeConfigServiceImpl.class);
    @Autowired
    private RegionService regionService;
    @Autowired
    private DealerService dealerService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private LeadDistributeConfigSearchMapper leadDistributeConfigSearchMapper;
    @Autowired
    private LeadDistributeConfigMapper leadDistributeConfigMapper;

    @Override
    @RequestMapping( value = {"import"},method = {RequestMethod.POST})
    public ResponseData<String> importConfig(@RequestBody ImportDTO importDTO) {
        List<List<String>> dataList = importDTO.getReslutData();
        if (dataList==null || dataList.isEmpty()) {
            return ResponseData.fail("导入失败，导入数据不存在！");
        }
        Map<String,Integer> provinceMap = new HashMap<>();
        Map<String,Integer> cityMap = new HashMap<>();
        Map<String,String> dealerMap = new HashMap<>();
        logger.info("地区经销商信息导入开始，导入文件名："+importDTO.getFileName());
        List<String> msgList = new ArrayList<>();

        List<LeadDistributeConfig> configList = new ArrayList<>();
        for (int i=1;i<dataList.size();i++) {//sheet内容 从第二行开始1
            List<String> data = dataList.get(i);
            String province  = data.get(0);
            if(!StringUtils.isEmpty(province)&&!"null".equals(province.trim())){
                String key=province+"_1";
                Integer provinceId = provinceMap.get(key);
                if(provinceId==null){
                    Region region = regionService.getRegionByNameAndType(1,province);
                    if(region!=null) {
                        provinceId = region.getRegionId();
                        provinceMap.put(key, provinceId);
                    }
                }
                if(provinceId==null){
                    msgList.add("第["+(i+1)+"]行省份名称填写错误");
                    logger.info("第["+(i+1)+"]行省份填写错误[{}]",province);
                }
            }
            Integer cityId = null;
            String city  = data.get(1);
            if(!StringUtils.isEmpty(city)&& !"null".equals(city.trim())){
                city = city.trim();
                String key=city+"_2";
                cityId = cityMap.get(key);
                if(cityId==null){
                    Region region = regionService.getRegionByNameAndType(2,city);
                    if(region!=null) {
                        cityId = region.getRegionId();
                        cityMap.put(key, cityId);
                    }
                }
                if(cityId==null){
                    msgList.add("第["+(i+1)+"]行城市名称填写错误");
                    logger.info("第["+(i+1)+"]行城市填写错误[{}]",city);
                }
            }

            String dealerCode  = data.get(2);
            if(!StringUtils.isEmpty(dealerCode)){
                logger.info("下发城市[{}]dealerCode[{}]",city,dealerCode);
                if(dealerCode.contains(";")){
                    dealerCode = dealerCode.replace(";",",");
                }
                String dealers[] = dealerCode.split(",");
                if(dealers!=null) {
                    for(String dealer:dealers) {
                        dealer = dealer.trim();
                        LeadDistributeConfig config = new LeadDistributeConfig();
                        config.setCityId(cityId);
                        String code = dealerMap.get(dealer);
                        logger.info("下发城市[{}]下发经销商[{}]",city,dealer);
                        if (code == null) {
                            DealerDTO dealerDTO = dealerService.detailsDealer(dealer);
                            if (dealerDTO != null) {
                                code = dealer;
                                dealerMap.put(dealer, dealer);
                            }
                        }
                        if (code != null) {
                            config.setDealerCode(code);
                        } else {
                            msgList.add("第[" + (i + 1) + "]行经销商编号填写错误");
                            logger.info("第[" + (i + 1) + "]行经销商编号错误[{}]", dealerCode);
                        }
                        if(msgList.size()==0) {
                            configList.add(config);
                        }
                    }
                }
            }
        }
        if(msgList.size()>0) {
            configList.clear();
        }
        if (msgList.size()>0){
            logger.info("线索配置信息导入失败.");
            return ResponseData.fail(msgList.toString());
        }
        Integer currentUserId = SessionHelper.getCurrentUserId();
        Date now = new Date();
        List<LeadDistributeConfig> cityDistributeConfigList = leadDistributeConfigSearchMapper.getCitiDistributeConfigList(null);
        Map<String,LeadDistributeConfig> configMap = this.getConfigMap(cityDistributeConfigList);
        Map<String,LeadDistributeConfig> newConfigMap = this.getConfigMap(configList);
        if(configList!=null && configList.size()>0) {
            for (LeadDistributeConfig config : configList) {
                String dealerCode = config.getDealerCode();
                Integer cityId = config.getCityId();

                logger.info("cityId[{}]dealer[{}]",cityId,dealerCode);
                LeadDistributeConfig newConfig = new LeadDistributeConfig();
                newConfig.setCityId(cityId);
                newConfig.setDealerCode(dealerCode);
                newConfigMap.put(cityId+"_"+dealerCode,newConfig);
                LeadDistributeConfig obj = configMap.get(cityId + "_" + dealerCode);
                if (obj == null) {
                    obj = new LeadDistributeConfig();
                    obj.setDealerCode(dealerCode);
                    obj.setCityId(cityId);
                    obj.setStatus(1);
                    obj.setIsCurrent(0);
                    obj.setModifyBy(currentUserId);
                    obj.setModifyDate(now);
                    leadDistributeConfigMapper.insert(obj);
                    configMap.put(cityId + "_" + dealerCode, obj);
                } else if (obj.getStatus() != null && obj.getStatus() == 0) {
                    obj.setStatus(1);
                    obj.setIsCurrent(0);
                    obj.setModifyBy(currentUserId);
                    obj.setModifyDate(now);
                    leadDistributeConfigMapper.updateByPrimaryKey(obj);
                }
            }
        }
        if(cityDistributeConfigList!=null){
            for(LeadDistributeConfig config:cityDistributeConfigList){
                LeadDistributeConfig obj = newConfigMap.get(config.getCityId()+"_"+config.getDealerCode());
                if(obj==null){
                    config.setStatus(0);
                    config.setModifyBy(currentUserId);
                    config.setModifyDate(now);
                    leadDistributeConfigMapper.updateByPrimaryKey(config);
                }
            }
        }
        logger.info("线索配置信息结束，导入成功！");
        return ResponseData.success("导入成功");
    }

    public Map<String,LeadDistributeConfig> getConfigMap(List<LeadDistributeConfig> citiDistributeConfigList){
        Map<String,LeadDistributeConfig> map = new HashMap<>();
        if(citiDistributeConfigList!=null)
        for(LeadDistributeConfig config:citiDistributeConfigList){
            map.put(config.getCityId()+"_"+config.getDealerCode(),config);
        }
        return map;
    }
}
