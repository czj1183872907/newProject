package com.chery.exeed.crm.lead.dto;

import com.chery.exeed.crm.lead.model.DefeatApprovalOperateRecord;

/**
 * @Description:
 * @author: gang.li@nttdata.com
 * @date: 2019/3/13 18:22
 */
public class DefeatApprovalOperateRecordDTO extends DefeatApprovalOperateRecord {
    private String lostType;
    private String feedback;

    public String getLostType() {
        return lostType;
    }

    public void setLostType(String lostType) {
        this.lostType = lostType;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}

