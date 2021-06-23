package com.chery.exeed.crm.lead.service;

import com.chery.exeed.crm.common.dto.ResponseData;
import com.chery.exeed.crm.lead.dto.CustomerDTO;
import com.chery.exeed.crm.lead.dto.PreLeadDTO;
import com.chery.exeed.crm.sysadmin.dto.UserDetailDTO;
import com.chery.exeed.ifs.common.dto.CarModelDTO;
import com.chery.exeed.ifs.common.dto.CarSeriesDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;
import java.util.List;

/**
 * Author:xiaowei.zhu
 * 2019/3/18 10:46
 */
public interface LeadCommonService {

    void createLog(Object newObject, Object oldObject, String title, Date now);

    ResponseData<List<CarSeriesDTO>> getSeriesList();

    ResponseData<List<CarModelDTO>> getModelList(String seriesCode);

    String getUserName(Integer userId);

    UserDetailDTO getUserInfo(Integer currentUserId);

    List<String> getUserDealerList(Integer currentUserId);

    Integer getUserType(Integer currentUserId);

    void insertLeadHistory(Integer leadId,
                           Date now,
                           Integer currentUserId,
                           Integer status,
                           String statusDesc,
                           String dealer,
                           Integer orderManager,
                           Integer owner);


    int copyChannel2OldLead(Integer oldLeadId, Integer newLeadId);

    CustomerDTO setLead2Customer(PreLeadDTO lead, Date now, Integer currentUserId, Integer custId);

    void createLog(Object newObject, Object oldObject, String title, Date now, Integer currentUserId,Integer bizId);

    ResponseData<Integer> validateLeadInfo(PreLeadDTO lead);

    void updateLeadGenderById(Integer custId, Integer gender);
}
