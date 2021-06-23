package com.chery.exeed.crm.lead.dto;

import com.chery.exeed.crm.lead.model.OfferOperateRecord;

/**
 * @Description:
 * @author: gang.li@nttdata.com
 * @date: 2019/4/3 15:48
 */
public class OfferOperateRecordDTO extends OfferOperateRecord {

    private String seriesNameDesc;

    private String modelNameDesc;

    public String getSeriesNameDesc() {
        return seriesNameDesc;
    }

    public void setSeriesNameDesc(String seriesNameDesc) {
        this.seriesNameDesc = seriesNameDesc;
    }

    public String getModelNameDesc() {
        return modelNameDesc;
    }

    public void setModelNameDesc(String modelNameDesc) {
        this.modelNameDesc = modelNameDesc;
    }
}

