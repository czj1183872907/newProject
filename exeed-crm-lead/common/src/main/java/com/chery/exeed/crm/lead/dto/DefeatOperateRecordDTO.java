package com.chery.exeed.crm.lead.dto;

import com.chery.exeed.crm.lead.model.DefeatOperateRecord;

/**
 * @Description:
 * @author: gang.li@nttdata.com
 * @date: 2019/3/19 11:29
 */
public class DefeatOperateRecordDTO extends DefeatOperateRecord {

    private Integer approvalResult;

    public Integer getApprovalResult() {
        return approvalResult;
    }

    public void setApprovalResult(Integer approvalResult) {
        this.approvalResult = approvalResult;
    }
}

