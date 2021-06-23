package com.chery.exeed.crm.lead.model;

public class MetaChooseReason extends MetaChooseReasonKey {
    private String chooseReasonDesc;

    private String chooseReasonDetailDesc;

    public String getChooseReasonDesc() {
        return chooseReasonDesc;
    }

    public void setChooseReasonDesc(String chooseReasonDesc) {
        this.chooseReasonDesc = chooseReasonDesc == null ? null : chooseReasonDesc.trim();
    }

    public String getChooseReasonDetailDesc() {
        return chooseReasonDetailDesc;
    }

    public void setChooseReasonDetailDesc(String chooseReasonDetailDesc) {
        this.chooseReasonDetailDesc = chooseReasonDetailDesc == null ? null : chooseReasonDetailDesc.trim();
    }
}