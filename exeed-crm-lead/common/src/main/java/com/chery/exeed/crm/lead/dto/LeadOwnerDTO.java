package com.chery.exeed.crm.lead.dto;

import io.swagger.models.auth.In;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: zhen.liu@nttdata.com
 * @Date: 2019/2/21 17:00
 * @Description:
 */
public class LeadOwnerDTO implements Serializable {
    private String managerDealerId;

    private String manager;

    private Integer owner;

    private String consultant;

    private List<Integer> leadIdList;//线索id

    private String leadStatus; //线索状态

    private String oldDealerId; //转移前的经销商id

    private String newDealerId; //转移后的经销商id

    private Integer userId; //当前用户id

    private String userName; //当前用户名字

    private String changeName; //操作名字

    private Integer type; //当前用户id

    private Integer id; //id

    private Integer count ; //总数

    private String time; //操作时间

    private String oldDealer; //转移前经销商

    private String newDealer; //转移后经销商

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOldDealer() {
        return oldDealer;
    }

    public void setOldDealer(String oldDealer) {
        this.oldDealer = oldDealer;
    }

    public String getNewDealer() {
        return newDealer;
    }

    public void setNewDealer(String newDealer) {
        this.newDealer = newDealer;
    }

    public String getChangeName() {
        return changeName;
    }

    public void setChangeName(String changeName) {
        this.changeName = changeName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getOldDealerId() {
        return oldDealerId;
    }

    public void setOldDealerId(String oldDealerId) {
        this.oldDealerId = oldDealerId;
    }

    public String getNewDealerId() {
        return newDealerId;
    }

    public void setNewDealerId(String newDealerId) {
        this.newDealerId = newDealerId;
    }

    public String getLeadStatus() {
        return leadStatus;
    }

    public void setLeadStatus(String leadStatus) {
        this.leadStatus = leadStatus;
    }

    public String getManagerDealerId() {
        return managerDealerId;
    }

    public void setManagerDealerId(String managerDealerId) {
        this.managerDealerId = managerDealerId;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    public String getConsultant() {
        return consultant;
    }

    public void setConsultant(String consultant) {
        this.consultant = consultant;
    }

    public List<Integer> getLeadIdList() {
        return leadIdList;
    }

    public void setLeadIdList(List<Integer> leadIdList) {
        this.leadIdList = leadIdList;
    }
}
