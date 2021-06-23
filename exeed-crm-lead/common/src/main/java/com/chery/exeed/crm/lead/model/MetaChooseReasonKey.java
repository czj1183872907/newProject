package com.chery.exeed.crm.lead.model;

public class MetaChooseReasonKey {
    private String chooseReason;

    private String chooseReasonDetail;

    public String getChooseReason() {
        return chooseReason;
    }

    public void setChooseReason(String chooseReason) {
        this.chooseReason = chooseReason == null ? null : chooseReason.trim();
    }

    public String getChooseReasonDetail() {
        return chooseReasonDetail;
    }

    public void setChooseReasonDetail(String chooseReasonDetail) {
        this.chooseReasonDetail = chooseReasonDetail == null ? null : chooseReasonDetail.trim();
    }
}