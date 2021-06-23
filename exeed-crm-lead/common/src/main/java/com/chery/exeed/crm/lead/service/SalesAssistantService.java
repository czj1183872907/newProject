package com.chery.exeed.crm.lead.service;

import com.chery.exeed.crm.common.dto.PaginationResult;
import com.chery.exeed.crm.common.dto.ResponseData;
import com.chery.exeed.crm.lead.dto.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SalesAssistantService {

    ResponseData<Boolean> consultantDistribute(LeadOwnerDTO leadOwnerDTO);




    ResponseData<List<LeadDTO>> getLeadListByDealerIdAndPhone(String dealerId, String phone);

    ResponseData<Integer> createLeadByConsultant(LeadDTO leadDTO);

    ResponseData<List<FollowupHistoryDTO>> getLeadFollowRecord(Integer leadId, String dealerId,Integer pageNo,Integer pageSize);

    ResponseData<Boolean> defeatLeadApproval(Integer leadId, String dealerId, Integer approvalStatus, String operator);

    ResponseData<Map<String,List<LeadResultDTO>>> assistantIndexLeadList();

    ResponseData<Integer> updateLeadToOffer(Integer leadId,String seriesName, String modelName, String feedback,Integer userId);

    ResponseData<Integer> updateLeadToOrder(Integer leadId, String custName, String phone, Integer documentType, String documentId, Date forDate, Integer userId,String address,String orderMoney,String materialCode);

    ResponseData<Integer> updateLeadToPredict(PredictOperateRecordDTO predictOperateRecordDTO);

    ResponseData<Integer> updateLeadToDefeatApply(Integer leadId, Integer loseType, Integer loseType2, String feedback, Integer userId,String competingGoods);

    ResponseData<Integer> createLeadFollowup(Integer leadId, Integer followupType, Date followupPlan, Date followupDate, String feedBack, Integer userId);

    ResponseData<Map<String, Object>> defeatLeadApplyList(Integer pageSize, Integer pageNo, Integer queryType);

    ResponseData<Map<String, Object>> querySuperviseLeadList(Integer type, Integer dateType, Integer leadType, String createBy, Integer level, Integer pageNo, Integer pageSize);

    ResponseData<List<LeadFollowupDTO>> expiredLeadList();

    ResponseData<Map<String, Object>> queryUndistributedLeadList(String seriesName, String modelName, Integer subChannel, Integer pageNo, Integer pageSize);

    ResponseData<List<CustomerDTO>> queryCustomerList(String keyword,Integer userId,String dealerId);

    ResponseData<Map<String, LevelDistributionDTO>> queryLevelDistribution();

    ResponseData<Map<String, LevelDistributionDTO>> queryLevelDistributionNew(Integer resource);

    ResponseData<List<ConsultantPerformanceDTO>> queryConsultantPerformance(Integer selectedTime,Map<String,String> map);

    ResponseData<List<Object>> queryFollowupDetail(FollowupHistoryDTO followupHistoryDTO);

    ResponseData<List<Map<String,Object>>> getLoseTypeList(Integer parentCode);

    ResponseData<Date> queryFollowPlan(Integer leadId);

    ResponseData<List<LeadResultDTO>> queryByPhoneAndCustName(String key,Integer queryType);

    ResponseData<PaginationResult<LeadResultDTO>> queryLeadListByCondition(Date begin, Date end, String level,String seriesName ,String modelName, Integer taskStatus,Integer pageNo,Integer pageSize);

    ResponseData<Map<String,Integer>> myTaskLeadCount();

    ResponseData<String> pushLeadNumToConsultant();

    ResponseData<String> pushLeadNumToDealerManager();

    ResponseData<String> pushLeadNumToExperienceManager();

    ResponseData<List<Object>> queryAssistantRegion();

    ResponseData<CustomerDTO> queryCredentialIdForOrder(Integer leadId);

    ResponseData<Map<String,Object>> leadFollowHistoryList(Integer leadId, String dealerId);

    ResponseData<Integer> updateLeadToArrival(ArrivalOperateRecordDTO arrivalOperateRecordDTO);

    ResponseData<ConsultantSalesPerformanceDTO> getConsultantSalesPerformance(Integer timeStamp);

    ResponseData<List<ConsultantSalesPerformanceDTO>> getDealerSalesPerformance(Integer timeStamp);

    ResponseData<Boolean> getArrivalInfoForPredict(Integer leadId);

    ResponseData<List<LeadResultDTO>> queryDashboardLeadList(Integer timeStamp, Integer owner, Integer queryType);

    ResponseData<List<LeadResultDTO>> queryDashboardLeadListByLeadStatus(Integer timeStamp, Integer owner, Integer queryType, Integer leadResource);

    ResponseData<List<LeadFollowupDTO>> queryDistributedLeadList();

    ResponseData<List<LeadFollowupDTO>> queryAcceptLeadList(Integer type, Integer dateType,String createBy,Integer level);

    ResponseData<List<LeadFollowupDTO>> queryOrderLeadList(Integer type, Integer dateType,String createBy,Integer level);



    ResponseData<Integer> updateLeadToActivationApply(Long leadId);


    ResponseData<Map<String, Object>> querytToBeDistributedList(String dealer, Integer type, String sericeName, String resourceType , Integer pageNo, Integer pageSize);

    ResponseData<Map<String ,Object>> oneHourFollowUpRate(String startSendTime,String endSendTime);
}
