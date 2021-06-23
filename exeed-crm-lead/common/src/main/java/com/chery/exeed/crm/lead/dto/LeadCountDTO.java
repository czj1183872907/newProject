package com.chery.exeed.crm.lead.dto;

/**
 * @Author: zhen.liu@nttdata.com
 * @Date: 2019/2/21 12:26
 * @Description:
 */
public class LeadCountDTO {
    private Integer owner;

    private String dealerOrderManager;

    private Integer leadStatus;

    private Integer totalCount;

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    public String getDealerOrderManager() {
        return dealerOrderManager;
    }

    public void setDealerOrderManager(String dealerOrderManager) {
        this.dealerOrderManager = dealerOrderManager;
    }

    public Integer getLeadStatus() {
        return leadStatus;
    }

    public void setLeadStatus(Integer leadStatus) {
        this.leadStatus = leadStatus;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
