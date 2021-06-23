package com.chery.exeed.crm.lead.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xiaowei.zhu
 * @date 2019/2/27 13:38
 */
public class CallSummaryDTO implements Serializable {

    private Integer id;

    private String connid;          //通话ID

    private String callNumber;      //被呼号码

    private String cdrId;           //在cdr表中对应的ID

    private Integer accoutId;        //accountID

    private Integer taskId;          //taskId

    private String callResult;      //电话结果

    private String reason;          //原因

    private String nextCallTime;    //下次呼叫时间

    private Date callTime;  //呼叫时间

    private Integer crmUserId; //用户id

    private Integer callCounts; //拨打次数

    private String remark;          //备注

    private Date createTime;        //创建时间

    private Date cdrStopTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConnid() {
        return connid;
    }

    public void setConnid(String connid) {
        this.connid = connid;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getCdrId() {
        return cdrId;
    }

    public void setCdrId(String cdrId) {
        this.cdrId = cdrId;
    }

    public Integer getAccoutId() {
        return accoutId;
    }

    public void setAccoutId(Integer accoutId) {
        this.accoutId = accoutId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getCallResult() {
        return callResult;
    }

    public void setCallResult(String callResult) {
        this.callResult = callResult;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNextCallTime() {
        return nextCallTime;
    }

    public void setNextCallTime(String nextCallTime) {
        this.nextCallTime = nextCallTime;
    }

    public Date getCallTime() {
        return callTime;
    }

    public void setCallTime(Date callTime) {
        this.callTime = callTime;
    }

    public Integer getCrmUserId() {
        return crmUserId;
    }

    public void setCrmUserId(Integer crmUserId) {
        this.crmUserId = crmUserId;
    }

    public Integer getCallCounts() {
        return callCounts;
    }

    public void setCallCounts(Integer callCounts) {
        this.callCounts = callCounts;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCdrStopTime() {
        return cdrStopTime;
    }

    public void setCdrStopTime(Date cdrStopTime) {
        this.cdrStopTime = cdrStopTime;
    }
}
