package com.chery.exeed.crm.lead.service;

import com.chery.exeed.crm.common.dto.ResponseData;
import com.chery.exeed.crm.lead.dto.FollowupHistoryDTO;
import com.chery.exeed.crm.lead.model.FollowupHistory;

import java.util.List;

public interface LeadFollowupService {

    ResponseData<List<FollowupHistoryDTO>> leadFollowupDetailList(Integer leadId, String dealerId);
}
