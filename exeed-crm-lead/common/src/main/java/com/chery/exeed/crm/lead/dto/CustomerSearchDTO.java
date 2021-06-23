package com.chery.exeed.crm.lead.dto;

import com.chery.exeed.crm.common.dto.PaginationSetting;

/**
 * @Auther: yueyun.pan
 * @Date: 2019/1/22 17:30
 * @Description:
 */
public class CustomerSearchDTO extends PaginationSetting {

    private Integer customerStatus;
    private Integer isSpecialCustomer;
    private Integer province;
    private Integer city;
    private Integer subcity;
    private String searchValue;
    private Integer owner;
    private String dealer;

    public Integer getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(Integer customerStatus) {
        this.customerStatus = customerStatus;
    }

    public Integer getIsSpecialCustomer() {
        return isSpecialCustomer;
    }

    public void setIsSpecialCustomer(Integer isSpecialCustomer) {
        this.isSpecialCustomer = isSpecialCustomer;
    }

    public Integer getProvince() {
        return province;
    }

    public void setProvince(Integer province) {
        this.province = province;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public Integer getSubcity() {
        return subcity;
    }

    public void setSubcity(Integer subcity) {
        this.subcity = subcity;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }
}
