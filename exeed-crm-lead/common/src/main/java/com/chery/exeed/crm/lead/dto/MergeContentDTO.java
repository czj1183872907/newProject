package com.chery.exeed.crm.lead.dto;

import com.chery.exeed.crm.lead.model.Customer;

import java.util.List;

/**
 * Author:xiaowei.zhu
 * 2019/5/8 17:44
 */
public class MergeContentDTO {
    private List<Integer> leadIds;
    private List<Integer> caseIds;
    private List<String> callHistoryIds;
    private List<Integer> relationIds;
    private Customer mergeCustomerInfo;
    private Customer targetCustomerInfo;

    public List<Integer> getLeadIds() {
        return leadIds;
    }

    public void setLeadIds(List<Integer> leadIds) {
        this.leadIds = leadIds;
    }

    public List<Integer> getCaseIds() {
        return caseIds;
    }

    public void setCaseIds(List<Integer> caseIds) {
        this.caseIds = caseIds;
    }

    public Customer getMergeCustomerInfo() {
        return mergeCustomerInfo;
    }

    public void setMergeCustomerInfo(Customer mergeCustomerInfo) {
        this.mergeCustomerInfo = mergeCustomerInfo;
    }

    public Customer getTargetCustomerInfo() {
        return targetCustomerInfo;
    }

    public void setTargetCustomerInfo(Customer targetCustomerInfo) {
        this.targetCustomerInfo = targetCustomerInfo;
    }

    public List<String> getCallHistoryIds() {
        return callHistoryIds;
    }

    public void setCallHistoryIds(List<String> callHistoryIds) {
        this.callHistoryIds = callHistoryIds;
    }

    public List<Integer> getRelationIds() {
        return relationIds;
    }

    public void setRelationIds(List<Integer> relationIds) {
        this.relationIds = relationIds;
    }
}
