package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.dto.FollowupHistoryDTO;
import com.chery.exeed.crm.lead.dto.LeadResultDTO;
import com.chery.exeed.crm.lead.model.FollowupHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: zhen.liu@nttdata.com
 * @Date: 2019/2/26 11:15
 * @Description:
 */
@Mapper
public interface FollowupHistorySearchMapper extends FollowupHistoryMapper{
    List<FollowupHistoryDTO> selectByLeadId(@Param("leadId") Integer leadId,@Param("pageNo") Integer pageNo,@Param("pageSize") Integer pageSize);

    Integer selectByLeadIdTotal(@Param("leadId") Integer leadId);

    FollowupHistory selectLastRecordBeforeDefeatApply(@Param("leadId") Integer leadId, @Param("leadStatusList") List<Integer> leadStatusList);

    int insertHistory(FollowupHistory record);

    List<Integer>  queryDefeatLeadApprovalResult(@Param("dealer") String dealerId);
}
