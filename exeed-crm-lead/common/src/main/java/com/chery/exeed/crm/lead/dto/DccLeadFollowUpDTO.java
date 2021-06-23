package com.chery.exeed.crm.lead.dto;

public class DccLeadFollowUpDTO {
    private Integer leadId;//线索 ID
    private Integer leadType;//线索类别 1:订单线索 2:话单线索
    private String channel;//渠道
    private String dealerId;//经销商 ID
    private String contactTel;//跟进人电话
    private String contactName;//跟进人姓名
    private Integer type;//线索类型  1. 询价 2. 试驾 3.询价 4. 活动 5.置换
    private Integer followStatus;//跟进状态 1跟进 2无意向

    public Integer getLeadId() {
        return leadId;
    }

    public void setLeadId(Integer leadId) {
        this.leadId = leadId;
    }

    public Integer getLeadType() {
        return leadType;
    }

    public void setLeadType(Integer leadType) {
        this.leadType = leadType;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(Integer followStatus) {
        this.followStatus = followStatus;
    }
}
