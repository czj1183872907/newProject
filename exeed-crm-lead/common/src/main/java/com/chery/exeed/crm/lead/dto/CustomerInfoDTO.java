package com.chery.exeed.crm.lead.dto;

/**
 * @Author: zhen.liu@nttdata.com
 * @Date: 2019/1/17 20:43
 * @Description:
 */
public class CustomerInfoDTO {
    private Integer id;

    private String lastName;

    private String firstName;

    private Integer customerStatus;
    private String customerStatusDes;

    private Integer isSpecialCustomer;

    private String phone;

    private String headImage;

    private Integer provinceId;

    private String provinceName;

    private Integer cityId;

    private String cityName;

    private Integer subcityId;

    private String subcityName;

    private Integer specialCustomerType;
    private String specialCustomerTypeDes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getSubcityId() {
        return subcityId;
    }

    public void setSubcityId(Integer subcityId) {
        this.subcityId = subcityId;
    }

    public String getSubcityName() {
        return subcityName;
    }

    public void setSubcityName(String subcityName) {
        this.subcityName = subcityName;
    }

    public Integer getSpecialCustomerType() {
        return specialCustomerType;
    }

    public void setSpecialCustomerType(Integer specialCustomerType) {
        this.specialCustomerType = specialCustomerType;
    }

    public String getCustomerStatusDes() {
        return customerStatusDes;
    }

    public void setCustomerStatusDes(String customerStatusDes) {
        this.customerStatusDes = customerStatusDes;
    }

    public String getSpecialCustomerTypeDes() {
        return specialCustomerTypeDes;
    }

    public void setSpecialCustomerTypeDes(String specialCustomerTypeDes) {
        this.specialCustomerTypeDes = specialCustomerTypeDes;
    }
}
