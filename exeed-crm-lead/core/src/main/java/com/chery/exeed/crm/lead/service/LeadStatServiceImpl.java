package com.chery.exeed.crm.lead.service;

import com.chery.exeed.crm.common.dto.ResponseData;
import com.chery.exeed.crm.common.util.SessionHelper;
import com.chery.exeed.crm.lead.constants.LeadConstants;
import com.chery.exeed.crm.lead.dto.*;
import com.chery.exeed.crm.lead.mapper.LeadSearchMapper;
import com.chery.exeed.crm.sysadmin.dto.UserDetailDTO;
import com.chery.exeed.crm.sysadmin.service.SysUserService;
import org.apache.servicecomb.provider.pojo.RpcReference;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.chery.exeed.crm.common.constants.Constants.LEAD_META_RESPONSE_FAIL;
import static com.chery.exeed.crm.lead.constants.LeadConstants.LEAD_LEVEL_META_NAME;

/**
 * @Author: zhen.liu@nttdata.com
 * @Date: 2019/2/20 17:21
 * @Description:
 */
@RestSchema(schemaId = "LeadStat-service")
@RequestMapping(path = "/apis/lead/stat")
public class LeadStatServiceImpl implements LeadStatService  {

    private static Logger logger = LoggerFactory.getLogger(LeadStatServiceImpl.class);

    @Autowired
    MetaService metaService;
    @Autowired
    private LeadCommonService leadCommonService;

    @Autowired
    LeadSearchMapper leadSearchMapper;

    @RpcReference(microserviceName = "exeed-crm-apis:exeed-crm-sysadmin", schemaId = "user-service")
    private SysUserService userService;

    @Override
    @RequestMapping(value = "/manager/mylead", method = RequestMethod.GET)
    public ResponseData<ManagerLeadStatDTO> salesAssLeadStat() {
        ManagerLeadStatDTO managerLeadStatDTO = new ManagerLeadStatDTO();
        Integer dealerOrderManager = SessionHelper.getCurrentUserId();
        // TODO dealer order manager lead stat
        return ResponseData.success(managerLeadStatDTO);
    }

    @Override
    @RequestMapping(value = "/consult", method = RequestMethod.GET)
    public ResponseData<List<ConsultantStatDTO>> salesAssConsultantStat() {
        List<ConsultantStatDTO> rstList = null;
        Integer dealerOrderManager = SessionHelper.getCurrentUserId();
        // TODO dealer order manager consultant stat
        List<LeadCountDTO> countList = leadSearchMapper.searchFollowLeadCountByManager(dealerOrderManager.toString());
        if (countList != null && countList.size() > 0) {
            Set<Integer> ownerIds = new HashSet<>();
            for (LeadCountDTO dto : countList) {
                ownerIds.add(dto.getOwner());
            }
            if (ownerIds.size() > 0) {
                // 已完成
                Integer countDone = 0;
                // 跟进中
                Integer countDoing = 0;
                // 逾期
                Integer countExpired = 0;
                // TODO
                ConsultantStatDTO consultantStatDTO = new ConsultantStatDTO();
                consultantStatDTO.setCountDone(countDone);
                consultantStatDTO.setCountDoing(countDoing);
                consultantStatDTO.setCountExpired(countExpired);
                rstList.add(consultantStatDTO);
            }
        }
        return ResponseData.success(rstList);
    }

    @Override
    @RequestMapping(value = "/consult/leadcount", method = RequestMethod.POST)
    public ResponseData<List<ConsultantLeadCountDTO>> consultantLeadCountStat(@RequestBody List<Integer> consultantIdList) {
        List<ConsultantLeadCountDTO> rstList = new ArrayList<>();
        String arrayStr = consultantIdList.toString();
        String idsStr = arrayStr.substring(1, arrayStr.length() - 1).trim();
        UserDetailDTO userInfo = leadCommonService.getUserInfo(SessionHelper.getCurrentUserId());
        List<ConsultantLeadCountDTO> dtoList = leadSearchMapper.searchLeadCountByConsultant(idsStr,userInfo.getDealerId());

        rstList.addAll(dtoList);
        // 匹配 consultantId 和 数据库中count返回的 consultantId，将未返回的 consultantId 数据的线索数量置为0
        for (Integer consultantId : consultantIdList) {
            Boolean find = false;
            for (ConsultantLeadCountDTO dto : dtoList) {
                if (dto.getConsultantId().intValue() == consultantId.intValue()) {
                    find = true;
                    break;
                }
            }
            if (!find) {
                ConsultantLeadCountDTO consultantLeadCountDTO = new ConsultantLeadCountDTO();
                consultantLeadCountDTO.setConsultantId(consultantId);
                consultantLeadCountDTO.setLeadCount(0);
                rstList.add(consultantLeadCountDTO);
            }
        }
        logger.info("==============dtoList.size"+dtoList.size());
        return ResponseData.success(rstList);
    }

    @Override
    @RequestMapping(value = "/consult/dealerLeadCountOthers", method = RequestMethod.POST)
    public ResponseData<ConsultantLeadCountDTO> dealerLeadCountOthers() {
        Integer userId = SessionHelper.getCurrentUserId();
        UserDetailDTO userInfo = leadCommonService.getUserInfo(userId);
        String dealerId = userInfo.getDealerId();
        ResponseData<List<Integer>> result = userService.getDeletedUserListByDealer(dealerId);
        List<Integer> userList = new ArrayList<>();
        if(result.getData() !=  null && result.getData().size()>0){
            userList = result.getData();
        }
        ConsultantLeadCountDTO others =  leadSearchMapper.searchDealerLeadCountOthers(dealerId,userList);
        if(others != null){
            others.setConsultantId(-1);
            others.setConsultant("其他");
            return ResponseData.success(others);
        }else{
            ConsultantLeadCountDTO consultantLeadCountDTO = new ConsultantLeadCountDTO();
            consultantLeadCountDTO.setLeadCount(0);
            consultantLeadCountDTO.setConsultantId(-1);
            consultantLeadCountDTO.setConsultant("其他");
            return ResponseData.success(consultantLeadCountDTO);
        }
    }

    @Override
    @RequestMapping(value = "/level/count", method = RequestMethod.GET)
    public ResponseData<List<LevelStatDTO>> levelStat(String dealer, Integer owner) {
        // 查询lead level meta
        ResponseData<List<MetaDataDTO>> leadLevelResp = metaService.getLeadMetaList(LEAD_LEVEL_META_NAME, null);
        if (leadLevelResp == null
                || leadLevelResp.getData() == null
                || leadLevelResp.getData().size() <= 0) {
            return ResponseData.fail(LEAD_META_RESPONSE_FAIL, "意向级别查询失败");
        }
        List<LevelStatDTO> rstList = new ArrayList<>();
        for (MetaDataDTO metaDataDTO : leadLevelResp.getData()) {
            LevelStatDTO levelStatDTO = new LevelStatDTO();
            levelStatDTO.setLevel(metaDataDTO.getMetaCode());
            levelStatDTO.setLevelDesc(metaDataDTO.getMetaName());
            rstList.add(levelStatDTO);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        String firstTime = sdf.format(calendar.getTime());

        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.SECOND, -1);
        String lastTime = sdf.format(calendar.getTime());

        List<LevelCountDTO> thisMonthCountDto = leadSearchMapper.searchLeadLevelCount(this.unCloseLeadStatusPack(), dealer, owner, firstTime, lastTime);
        List<LevelCountDTO> historyCountDto = leadSearchMapper.searchLeadLevelCount(this.unCloseLeadStatusPack(), dealer, owner, null, firstTime);

        for (LevelStatDTO levelStatDTO : rstList) {
            for (LevelCountDTO levelCountDTO : thisMonthCountDto) {
                if (levelStatDTO.getLevel() == levelCountDTO.getLevel()) {
                    levelStatDTO.setStatThisMonth(levelCountDTO.getLeadCount());
                }
            }

            for (LevelCountDTO levelCountDTO : historyCountDto) {
                if (levelStatDTO.getLevel() == levelCountDTO.getLevel()) {
                    levelStatDTO.setStatHistory(levelCountDTO.getLeadCount());
                }
            }
        }
        return ResponseData.success(rstList);
    }

    public List<Integer> unCloseLeadStatusPack() {
        List<Integer> rstList = new ArrayList<>();
        rstList.add(LeadConstants.LEAD_STATUS_ENUME.NOT_DISTRIBUTE.getValue());
        rstList.add(LeadConstants.LEAD_STATUS_ENUME.DISTRIBUTED.getValue());
        rstList.add(LeadConstants.LEAD_STATUS_ENUME.DEFEAT_APPLY.getValue());
        rstList.add(LeadConstants.LEAD_STATUS_ENUME.NOT_ARRIVAL.getValue());
        rstList.add(LeadConstants.LEAD_STATUS_ENUME.NOT_PREDICT.getValue());
        rstList.add(LeadConstants.LEAD_STATUS_ENUME.PREDICT.getValue());
        rstList.add(LeadConstants.LEAD_STATUS_ENUME.PRICE_OFFER.getValue());
        rstList.add(LeadConstants.LEAD_STATUS_ENUME.ORDER.getValue());
        return rstList;
    }
}
