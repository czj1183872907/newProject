package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.dto.*;
import com.chery.exeed.crm.lead.model.Lead;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface LeadSearchMapper {
    List<Lead> searchLeadByPhoneNo(Map<String, Object> param);
    LeadResultDTO getLeadById(Map<String, Object> param);
    LeadDTO detailForUpdate(Integer leadId);
    List<LeadResultDTO> queryLeadList(LeadDTO lead);
    Integer queryLeadListTodaycount(LeadDTO lead);
    List<LeadResultDTO> queryLeadListByAssisant(LeadDTO lead);
    Integer queryLeadListByAssisantTotal(LeadDTO lead);
    Integer unAssignLead(LeadDTO lead);
    List<LeadResultDTO> getLeadListByCustId(LeadDTO lead);

    List<LeadResultDTO> listExportLead(ExportLeadSearchDTO exportLeadSearchDTO);

    List<Lead> selectByDealerIdAndPhone(@Param("dealerId") String dealerId,@Param("phone") String phone);

    String getChannelNameByLeadId(Integer leadId);

    List<LeadCountDTO> searchFollowLeadCountByManager(@Param("dealerOrderManager") String dealerOrderManager);

    List<ConsultantLeadCountDTO> searchLeadCountByConsultant(@Param("ownerIds") String ownerIds, @Param("dealer") String dealer);

    List<LevelCountDTO> searchLeadLevelCount(@Param("leadStatusList") List<Integer> leadStatusList,
                                             @Param("dealer") String dealer,
                                             @Param("owner") Integer owner,
                                             @Param("startTime") String startTime,
                                             @Param("endTime") String endTime);

    List<Lead> selectSendedLeadByDealerId(@Param("dealerId")String dealerId, @Param("num")Integer num);

    List<LeadResultDTO> queryNewLeadList(LeadDTO leadDTO);

    List<LeadResultDTO> queryDeliveryLeadsList(LeadDTO leadDTO);

    List<LeadResultDTO> queryUndistributedLeadList(@Param("dealerId") String dealerId, @Param("rstList") List<Integer> rstList,@Param("invailUserId") List<Integer> invailUserId,@Param("seriesName")String seriesName,
                                                   @Param("modelName") String modelName,@Param("subChannel") Integer subChannel,@Param("pageNo") Integer pageNo,@Param("pageSize") Integer pageSize);

    List<LeadResultDTO> queryUndistributedLeadLists(@Param("dealerId") String dealerId, @Param("rstList") List<Integer> rstList,@Param("userId") Integer userId,@Param("seriesName")String seriesName,
                                                   @Param("modelName") String modelName,@Param("subChannel") Integer subChannel);

    Integer queryUndistributedLeadCount(@Param("dealerId") String dealerId, @Param("rstList") List<Integer> rstList, @Param("userId")Integer userId);

    List<LevelDistributionDTO> queryLevelDistribution(@Param("dealerId") String dealerId,@Param("owner") Integer owner);

    List<LevelDistributionDTO> queryLevelDistributionOnThisMonth(@Param("dealerId") String dealerId,@Param("owner") Integer owner);
    List<LevelDistributionDTO> queryLevelDistributionOnLastMonth(@Param("dealerId") String dealerId,@Param("owner") Integer owner);

    List<LeadResultDTO> queryByPhoneAndCustName(@Param("key") String key, @Param("dealer") String dealer,@Param("owner")Integer owner);

    List<LeadResultDTO> queryTaskLeadList(@Param("dealer") String dealer, @Param("owner") Integer owner,@Param("status") List<Integer> status);

    List<LeadResultDTO> queryTaskLeadListByCondition(@Param("dealer") String dealer,@Param("owner")  Integer owner, @Param("status") List<Integer> status,@Param("begin") Date begin,
                                                     @Param("end") Date end, @Param("level") String level, @Param("modelName") String modelName,
                                                     @Param("taskStatus")Integer taskStatus,@Param("pageNo")Integer pageNo,@Param("pageSize")Integer pageSize);

    Integer queryTaskLeadListByConditionTotal(@Param("dealer") String dealer,@Param("owner")  Integer owner, @Param("status") List<Integer> status,@Param("begin") Date begin,
                                                     @Param("end") Date end, @Param("level") String level, @Param("modelName") String modelName,
                                                     @Param("taskStatus")Integer taskStatus);

    List<LeadResultDTO> queryFollowupLeadList(@Param("dealer") String dealerId,@Param("owner") Integer owner,@Param("status") List<Integer> status);

    List<LeadResultDTO> queryDefeatLeadList(LeadDTO lead);

    Integer queryDefeatLeadCount(LeadDTO lead);

    List<LeadResultDTO> queryDefeatLeadApprovalList(LeadDTO lead);


    Integer queryNewLeadListCount(LeadDTO leadDTO);

    Integer queryTodayCount(LeadDTO leadDTO);

    Integer queryNotPredictAndNotArrivalLeadCount(@Param("dealer") String dealer, @Param("owner") Integer owner);

    Integer queryOrderLeadCount(LeadDTO leadDTO);

    Integer queryDeliveryLeadCount(LeadDTO leadDTO);

    Integer queryFollowupLeadCount(@Param("dealer") String dealerId,@Param("owner") Integer owner);

    Integer queryWinLeadCount(LeadDTO leadDTO);

    List<LeadResultDTO> queryNewTaskLeadList(@Param("dealer") String dealer,@Param("owner")  Integer owner,@Param("begin") Date begin,
                                             @Param("end") Date end, @Param("level") String level,@Param("seriesName") String seriesName, @Param("modelName") String modelName,
                                             @Param("pageNo") Integer pageNo,@Param("pageSize") Integer pageSize);

    Integer queryNewTaskLeadListTotal(@Param("dealer") String dealer,@Param("owner")  Integer owner,@Param("begin") Date begin,
                                             @Param("end") Date end, @Param("level") String level,@Param("seriesName") String seriesName, @Param("modelName") String modelName);

    List<LeadResultDTO> queryFollowLeadListByCondition(@Param("dealer") String dealer,@Param("owner")  Integer owner,@Param("begin") Date begin,
                                                       @Param("end") Date end, @Param("level") String level, @Param("modelName") String modelName,
                                                       @Param("pageNo") Integer pageNo,@Param("pageSize") Integer pageSize);

    Integer queryFollowLeadListByConditionTotal(@Param("dealer") String dealer,@Param("owner")  Integer owner,@Param("begin") Date begin,
                                                       @Param("end") Date end, @Param("level") String level, @Param("modelName") String modelName);

    List<Lead> searchLeadByPhoneNoForSended(Map<String, Object> param);

    List<Lead> selectDefeatAndInvalidLead(@Param("dealer") String dealer,@Param("phone")String phone,@Param("status")String[] status);

    Integer selectDefeatAndInvalidLeadCount(@Param("dealer")String dealer,@Param("phone")String phone);

    Integer getLeadHeatingCount(Integer leadId);

    int updateLeadToLeadHeatingCountById(@Param("leadId")Integer leadId,@Param("leadHeatingCount")Integer leadHeatingCount);

    /**
     * 通过主键和经销商查询线索
     * @param param
     * @return
     */
    List<Lead> selectByPrimaryKeyAndDealer(Map<String, Object> param);

    /**
     * 高级查询
     * @param advancedQueryDTO
     * @return
     */
    List<LeadResultDTO> advancedQueryLeadList(AdvancedQueryDTO advancedQueryDTO);

    int advancedQueryLeadListCount(AdvancedQueryDTO advancedQueryDTO);

    ConsultantLeadCountDTO searchDealerLeadCountOthers(@Param("dealer") String dealerId,@Param("userList") List<Integer> userList);

    List<LeadResultDTO> searchDealerLeadOthers(@Param("dealer")  String dealerId,@Param("userList") List<Integer> userList,@Param("leadDTO") LeadDTO leadDTO);

    List<Lead> getAllCustLeadList(Integer custId);

    int updateByPrimaryKeySelective(Lead record);

    Integer queryExpiredLeadCount(@Param("dealer") String dealerId,@Param("owner") Integer owner);

    Integer queryLoseLeadCount(LeadDTO leadDTO);

    List<LeadResultDTO> queryExpiredLeadList(@Param("dealer") String dealerId,@Param("owner") Integer owner,@Param("pageNo") Integer pageNo,@Param("pageSize") Integer pageSize);

    Integer queryExpiredLeadListTotal(@Param("dealer") String dealerId,@Param("owner") Integer owner);

    ConsultantSalesPerformanceDTO getConsultantSalesPerformance(@Param("timeStamp") Integer timeStamp, @Param("dealerId") String dealerId, @Param("userId") Integer userId);

    List<ConsultantSalesPerformanceDTO> getDealerSalesPerformance(@Param("timeStamp") Integer timeStamp, @Param("dealerId") String dealerId);

    Integer getArrivalInfoForPredict(@Param("leadId") Integer leadId);

    List<LeadResultDTO> queryDashboardLeadList(@Param("timeStamp") Integer timeStamp,@Param("owner") Integer owner,@Param("dealerId") String dealerId,@Param("queryType") Integer queryType);

    List<LeadResultDTO> queryDashboardLeadListByLeadStatus(@Param("timeStamp") Integer timeStamp,@Param("owner") Integer owner,@Param("dealerId") String dealerId,@Param("queryType") Integer queryType,@Param("leadResource") Integer leadResource);

    int updateByPrimaryKeySelectiveForTask(Lead lead);

    void updateLeadGenderById(@Param("custId") Integer custId,@Param("gender") Integer gender);

    int insertReturnPK (Lead record);

    List<LeadResultDTO> queryLeadListByPage(LeadDTO leadDTO);

    long countLead(LeadDTO leadDTO);

    List<LeadResultDTO> queryLeadlistByBigManagent(LeadDTO leadDTO);
    List<LeadResultDTO> leadUpdateRecordTime();

    List<Integer> selectInvalidManage(String dealerId);

    List<LeadHistoryDTO> queryLeadHistoryByLeadId(Integer leadId);

    List<Lead> selectFollowByDealerIdAndPhone(@Param("dealerId") String dealerId,@Param("phone") String phone);

    List<Lead> selectFailDealerIdAndPhone(@Param("dealerId") String dealerId,@Param("phone") String phone);

    Integer queryDefeatLeadApprovalCount(LeadDTO leadDTO);

    Integer queryDefeatLeadCounts(LeadDTO lead);

    Integer queryUndistributedCount(@Param("dealerId") String dealerId, @Param("rstList") List<Integer> rstList,@Param("invailUserId") List<Integer> invailUserId,@Param("seriesName")String seriesName,
                                @Param("modelName") String modelName,@Param("subChannel") Integer subChannel,@Param("pageNo") Integer pageNo,@Param("pageSize") Integer pageSize);

    int selectFlag(Integer id);


//    Integer queryLeadlistByBigManagentCount(LeadDTO leadDTO);
}

