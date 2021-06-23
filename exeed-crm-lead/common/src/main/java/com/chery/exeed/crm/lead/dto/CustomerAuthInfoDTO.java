package com.chery.exeed.crm.lead.dto;

import com.chery.exeed.crm.lead.model.CustomerAuthInfo;

/**
 * @Author: zhen.liu@nttdata.com
 * @Date: 2019/4/3 11:38
 * @Description:
 */
public class CustomerAuthInfoDTO extends CustomerAuthInfo {
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
