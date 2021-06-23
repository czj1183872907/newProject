package com.chery.exeed.crm.lead.service;

import com.chery.exeed.crm.common.dto.ResponseData;
import com.chery.exeed.crm.lead.dto.FollowupHistoryDTO;
import com.chery.exeed.crm.lead.mapper.FollowupHistorySearchMapper;
import com.chery.exeed.crm.lead.mapper.LeadMapper;
import com.chery.exeed.crm.lead.model.FollowupHistory;
import com.chery.exeed.crm.lead.model.Lead;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

import static com.chery.exeed.crm.common.constants.Constants.ILLEGAL_USER_OPERATE_CODE;

/**
 * @Description:
 * @author: gang.li@nttdata.com
 * @date: 2019/2/24 16:30
 */
@RestSchema(schemaId = "leadFollowup-service")
@RequestMapping(path = "/apis/lead/followup")
public class LeadFollowupServiceImpl implements LeadFollowupService {
    private static Logger logger = LoggerFactory.getLogger(LeadFollowupServiceImpl.class);
    @Autowired
    private FollowupHistorySearchMapper followupHistorySearchMapper;
    @Autowired
    private LeadMapper leadMapper;

    @Override
    @RequestMapping(value = "/leadFollowupDetailList", method = RequestMethod.GET)
    public ResponseData<List<FollowupHistoryDTO>> leadFollowupDetailList(Integer leadId, String dealerId) {
        Lead lead = leadMapper.selectByPrimaryKey(leadId);
        // check 线索dealerId 和 操作用户的dealerId是否一致
        if (!dealerId.equals(lead.getDealer())) {
            logger.error("查询跟进情况失败 ===> 线索dealerId[" + lead.getDealer() + "].经销商用户dealerId[" + dealerId + "]");
            return ResponseData.fail(ILLEGAL_USER_OPERATE_CODE, "查询跟进情况失败，用户不合法");
        }
        List<FollowupHistoryDTO> result = followupHistorySearchMapper.selectByLeadId(leadId,null,null);
        return ResponseData.success(result);
    }
}

