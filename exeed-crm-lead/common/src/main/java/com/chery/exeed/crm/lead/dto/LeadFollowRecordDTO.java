package com.chery.exeed.crm.lead.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author: zhen.liu@nttdata.com
 * @Date: 2019/2/24 15:19
 * @Description:
 */
public class LeadFollowRecordDTO implements Serializable {

    private Date leadFollowupPlan;

    private List<LeadFollowupDTO> leadFollowupList;

    public Date getLeadFollowupPlan() {
        return leadFollowupPlan;
    }

    public void setLeadFollowupPlan(Date leadFollowupPlan) {
        this.leadFollowupPlan = leadFollowupPlan;
    }

    public List<LeadFollowupDTO> getLeadFollowupList() {
        return leadFollowupList;
    }

    public void setLeadFollowupList(List<LeadFollowupDTO> leadFollowupList) {
        this.leadFollowupList = leadFollowupList;
    }
}
