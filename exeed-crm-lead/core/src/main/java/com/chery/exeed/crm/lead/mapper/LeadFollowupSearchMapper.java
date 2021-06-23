package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.dto.LeadFollowupDTO;
import com.chery.exeed.crm.lead.model.LeadFollowup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Author: zhen.liu@nttdata.com
 * @Date: 2019/2/24 14:31
 * @Description:
 */
@Mapper
public interface LeadFollowupSearchMapper extends LeadFollowupMapper {
    List<LeadFollowup> selectLeadFollowupByLeadId(@Param("leadId") Integer leadId);

    int deleteByLeadIdInActive(@Param("leadId") Integer leadId);

    int updateLeadIdInActive(@Param("leadId") Integer leadId);

    int insertSelectiveReturn(LeadFollowup record);

    List<LeadFollowup> selectByLeadIdAndStatus(@Param("leadId") Integer leadId,@Param("status")Integer status);

    List<LeadFollowupDTO> querySuperviseLeadList(@Param("dealerId") String dealerId, @Param("leadType") Integer leadType,
                                                 @Param("dateType") Integer dateType, @Param("createBy") String createBy,@Param("level") Integer level,
                                                 @Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize );

    List<LeadFollowupDTO> querySuperviseLeadListByConsultant(@Param("dealerId") String dealerId,@Param("userId") Integer userId);

    List<LeadFollowupDTO> queryFollowupPlan(@Param("id")Integer id);

    List<LeadFollowupDTO> queryExpiredLeadList(@Param("dealerId") String dealerId,@Param("userId") Integer userId);

    Integer queryFollowupTimes(@Param("leadId") Integer leadId);

    Integer queryConsultantPerformance(@Param("selectedTime") Integer selectedTime, @Param("distributeTo") Integer distributeTo, @Param("status") Integer status);

    Integer queryExpiredLeadOfConsultant(@Param("selectedTime") Integer selectedTime,@Param("distributeTo") Integer distributeTo);

    Integer queryConsultantPerformanceNotFollow(@Param("selectedTime") Integer selectedTime, @Param("distributeTo") Integer distributeTo, @Param("status") Integer status);

    List<Integer> queryConsultantPerformanceFollow(@Param("selectedTime") Integer selectedTime, @Param("distributeTo") Integer distributeTo, @Param("status") Integer status);

    void rollbackLastFollowup(@Param("leadId") Integer leadId);

    List<LeadFollowupDTO> queryDistributedLeadList(@Param("dealerId")String dealerId);

    List<LeadFollowupDTO> queryAcceptLeadList(@Param("dealerId") String dealerId, @Param("dateType") Integer dateType ,@Param("createBy") String createBy,@Param("level") Integer level);

    List<LeadFollowupDTO> queryOrderLeadList(@Param("dealerId") String dealerId, @Param("dateType") Integer dateType ,@Param("createBy") String createBy,@Param("level") Integer level);

    List<LeadFollowupDTO> getSuperviseLeadTotal(String dealerId);

    List<LeadFollowupDTO> queryOrderLeadListTotal(String dealerId);

    List<LeadFollowupDTO> queryAcceptLeadListTotal(String dealerId);

    List<LeadFollowupDTO> queryFollowupPlannvalid(Integer id);

    Integer querySuperviseLeadCount(@Param("dealerId") String dealerId, @Param("leadType") Integer leadType,
                                @Param("dateType") Integer dateType, @Param("createBy") String createBy,@Param("level") Integer level,
                                @Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize);
}

