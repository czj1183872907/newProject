package com.chery.exeed.crm.lead.service;

import com.chery.exeed.crm.common.dto.DealerViewDTO;
import org.springframework.stereotype.Service;

/**
 * @Author: zhen.liu@nttdata.com
 * @Date: 2020/12/9 16:38
 * @Description:
 */
@Service
public interface ViewService {

    String matchNicknameInAllSysUser(Integer userId);

    String matchEntityFullNameInCampaignChannel(String channelCode);

    String matchActNameInCampaignAct(String actCode);

    String matchRegionNameInRegion(String regionId);

    String matchSeriesNameInCarSeries(String seriesCode);

    String matchModelNameInCarModel(String seriesCode, String modelCode);

    DealerViewDTO matchDealer(String dealerCode);

    Integer matchFinalCallResultInCallSummary(Long taskId);

    Integer matchFirstCallResultInCallSummary(Long taskId);
}
