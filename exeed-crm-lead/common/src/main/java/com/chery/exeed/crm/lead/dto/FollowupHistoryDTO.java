package com.chery.exeed.crm.lead.dto;

import com.chery.exeed.crm.lead.model.FollowupHistory;

/**
 * @Author: zhen.liu@nttdata.com
 * @Date: 2019/2/26 11:10
 * @Description:
 */
public class FollowupHistoryDTO extends FollowupHistory {
    private String operationByDesc;

    private String feedback;

    private String leadStatusDesc;

    private transient String nickName;//用户名称

    private transient Integer totals;//总数量

    public String getLeadStatusDesc() {
        return leadStatusDesc;
    }

    public void setLeadStatusDesc(String leadStatusDesc) {
        this.leadStatusDesc = leadStatusDesc;
    }

    public String getOperationByDesc() {
        return operationByDesc;
    }

    public void setOperationByDesc(String operationByDesc) {
        this.operationByDesc = operationByDesc;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getTotals() {
        return totals;
    }

    public void setTotals(Integer totals) {
        this.totals = totals;
    }
}
