package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.common.dto.AssistantCityAPIDTO;
import com.chery.exeed.crm.common.dto.DealerDTO;
import com.chery.exeed.crm.lead.dto.ExportRecord;
import com.chery.exeed.crm.lead.dto.LeadDTO;
import com.chery.exeed.crm.lead.dto.LeadOwnerDTO;
import com.chery.exeed.crm.lead.dto.LeadResultDTO;
import com.chery.exeed.crm.lead.model.Lead;
import com.chery.exeed.crm.lead.model.LeadTask;
import com.chery.exeed.crm.sysadmin.model.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface LeadMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Lead record);

    int insertSelective(Lead record);

    Lead selectByPrimaryKey(Integer id);

    int updateLeadIsRedOrBlack(Integer leadId);

    int updateByPrimaryKeySelective(Lead record);

    int updateLeadByCustId(Lead record);

    int updateByPrimaryKey(Lead record);

    Lead selectLeadById(Long leadId);

    Integer getCheck(@Param("phone") String phone, @Param("dealer") String dealer );

//    ResponseData<SysUser> getUserByUserId(Integer userId);

    void updateLevelByLeadId(Integer leadId);


    List<LeadResultDTO> selectToBeDistributedList(@Param("dealer")String dealer,@Param("sericeName") String sericeName, @Param("resourceType")  String resourceType,@Param("pageNo")  Integer pageNo,@Param("pageSize")  Integer pageSize);


    List<LeadResultDTO> selectToBeDistributedCount(String dealer);


    List<DealerDTO> getDealerInfo();


    List<DealerDTO> selectDealerName();


    List<DealerDTO> selectCarType();


    String selectDealerCode(String currentUserName);

    List<LeadResultDTO> queryOneHourFollowUpRateOne(@Param("dealer")String dealer,@Param("startSendTime")String startSendTime,@Param("endSendTime")String endSendTime);

    List<LeadResultDTO> queryOneHourFollowUpRateTwo(@Param("userId")Integer userId,@Param("timeStamp")Integer timeStamp);

    SysUser userInfo(Long userId);

    String selectUserName(Integer currentDealerManager);

    Integer ToBeDistributedCount(@Param("dealer") String dealer, @Param("sericeName") String sericeName,@Param("resourceType") String resourceType,
                                 @Param("pageNo") Integer pageNo,  @Param("pageSize") Integer pageSize);
   Integer updateLeadTaskByLeadId(@Param("leadId") Integer leadId,@Param("status") Integer status);

    List<Lead> selectLeadByPhone(@Param("phone") String phone, @Param("newDealerId") String newDealerId);

    void updateLead(Integer leadStatus, String dealer, Integer id);

    String selectUserNameByUserId(Integer userId);


    void insertLeadTask(LeadTask leadTask);

    List<Lead> selectleadByLeadId(@Param("dealer") String dealer, @Param("asList") List<String> asList);

    void insertChangeLead(@Param("userName") String userName, @Param("type") Integer type, @Param("leadStatus") String leadStatus,
                          @Param("oldDealerName") String oldDealerName,  @Param("newDealerName") String newDealerName);

    List<LeadOwnerDTO> selectChangeLeadList( @Param("pageSize") Integer pageSize, @Param("pageNo")  Integer pageNo,
                                             @Param("beginTime") String beginTime,@Param("endTime")  String endTime);

    int selectChangeLeadCount(@Param("beginTime") String beginTime,@Param("endTime") String endTime);

//    List<String> selectDescriptionByCode(String leadStatus);

    String selectOlDDealerNameById(String oldDealerId);

    String selectNewDealerNameById(String newDealerId);

    int selectFlag(Integer id);
}