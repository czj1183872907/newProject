package com.chery.exeed.crm.lead.service;

import com.chery.exeed.crm.common.dto.PaginationResult;
import com.chery.exeed.crm.common.dto.ResponseData;
import com.chery.exeed.crm.lead.dto.LeadTaskAssignDTO;
import com.chery.exeed.crm.lead.dto.LeadTaskDTO;
import com.chery.exeed.crm.lead.dto.LeadTaskDetailDTO;
import com.chery.exeed.crm.lead.dto.LeadTaskParamDTO;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.List;

/**
 * Author:xiaowei.zhu
 * 2019/1/22 16:51
 */
public interface CallTaskService {
    ResponseData<PaginationResult<LeadTaskDTO>> getLeadTaskList(LeadTaskParamDTO leadTaskParam);

    ResponseData<LeadTaskDTO> getLeadTaskDetailForUpdate(Long taskId);

    ResponseData<LeadTaskDetailDTO> getTaskDetail(Long taskId);

    ResponseData<Integer> assignTask(LeadTaskAssignDTO leadTask);

    ResponseData<Boolean> assignAllTask(List<LeadTaskDTO> leadTaskList);

    ResponseData<Boolean> doneTask(Long taskId);

    ResponseData<Boolean> update(LeadTaskDTO task);

    void processCallSummary(String msg);
}
