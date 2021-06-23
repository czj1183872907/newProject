package com.chery.exeed.crm.lead.dto;

import java.util.List;
import java.util.Map;

/**
 * @Auther: yueyun.pan
 * @Date: 2019/1/21 21:02
 * @Description:
 */
public class ImportDTO {
    private String fileName;
    private List<List<String>> reslutData;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<List<String>> getReslutData() {
        return this.reslutData;
    }

    public void setReslutDate(List<List<String>> reslutData) {
        this.reslutData = reslutData;
    }
}
