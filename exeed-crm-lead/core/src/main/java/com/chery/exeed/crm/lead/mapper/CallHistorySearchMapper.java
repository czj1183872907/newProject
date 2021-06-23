package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.dto.CallHistoryDTO;
import com.chery.exeed.crm.lead.model.CallHistory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CallHistorySearchMapper {
    List<CallHistoryDTO> getCallHistory(Long taskId);
}