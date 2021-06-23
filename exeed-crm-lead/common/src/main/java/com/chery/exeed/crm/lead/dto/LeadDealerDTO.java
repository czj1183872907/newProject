package com.chery.exeed.crm.lead.dto;

import java.io.Serializable;

/**
 * Author:xiaowei.zhu
 * 2019/1/22 9:26
 */
public class LeadDealerDTO implements Serializable {
    private String dealerId;
    private Integer leadId;

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public Integer getLeadId() {
        return leadId;
    }

    public void setLeadId(Integer leadId) {
        this.leadId = leadId;
    }
}
