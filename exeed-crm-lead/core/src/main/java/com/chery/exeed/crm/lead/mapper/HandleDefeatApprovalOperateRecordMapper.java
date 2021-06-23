package com.chery.exeed.crm.lead.mapper;

import com.chery.exeed.crm.lead.dto.DefeatApprovalOperateRecordDTO;
import com.chery.exeed.crm.lead.model.DefeatApprovalOperateRecord;
import com.chery.exeed.crm.lead.model.DefeatOperateRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: zhen.liu@nttdata.com
 * @Date: 2019/2/26 12:35
 * @Description:
 */
@Mapper
public interface HandleDefeatApprovalOperateRecordMapper extends DefeatApprovalOperateRecordMapper{
    int insertSelectiveReturn(DefeatApprovalOperateRecord record);

    DefeatApprovalOperateRecordDTO queryByLeadId(@Param("leadId") Integer leadId);
}
