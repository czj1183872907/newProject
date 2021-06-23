package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.dto.LeadTaskAssignDTO;
import com.chery.exeed.crm.lead.dto.LeadTaskDTO;
import com.chery.exeed.crm.lead.dto.LeadTaskDetailDTO;
import com.chery.exeed.crm.lead.dto.LeadTaskParamDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface LeadTaskSearchMapper {
    List<LeadTaskDTO> getMyTaskList(LeadTaskParamDTO leadTaskParam);

    LeadTaskDetailDTO getTaskById(Long taskId);

    LeadTaskDTO getLeadTaskDetailForUpdate(Long taskId);

    LeadTaskDTO getLeadTaskByLeadId(Integer leadId);

    Integer assignTask(LeadTaskAssignDTO leadTaskAssign);

    LeadTaskDTO getLeadTask(Integer id);

//    int isBigManager(String dealerId);

//    List selectManager(String dealerId);
}