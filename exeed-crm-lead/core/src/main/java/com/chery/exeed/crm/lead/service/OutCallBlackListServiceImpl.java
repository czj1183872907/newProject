package com.chery.exeed.crm.lead.service;

import com.chery.exeed.crm.common.dto.ResponseData;
import com.chery.exeed.crm.common.util.SessionHelper;
import com.chery.exeed.crm.lead.constants.LeadConstants;
import com.chery.exeed.crm.lead.dto.OutCallBlackListDTO;
import com.chery.exeed.crm.lead.mapper.LeadHistoryMapper;
import com.chery.exeed.crm.lead.mapper.LeadMapper;
import com.chery.exeed.crm.lead.mapper.OutCallBlackListMapper;
import com.chery.exeed.crm.lead.model.Lead;
import com.chery.exeed.crm.lead.model.LeadHistory;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@ResponseBody
@RestSchema(schemaId = "blacklsit-service")
@RequestMapping(path = "/exeed/lead/blacklsit")
public class OutCallBlackListServiceImpl implements OutCallBlackListService{

    @Autowired
    private OutCallBlackListMapper outCallBlackListMapper;
    @Autowired
    LeadMapper leadMapper;
    @Autowired
    LeadHistoryMapper leadHistoryMapper;

    @Override
    @RequestMapping(value = "/sava", method = {RequestMethod.POST})
    public ResponseData<Integer> insertOutCallBlackList(String phone, String remarks) {
        String userName= SessionHelper.getCurrentUserName();
        //国内手机号码格式
        boolean matches = Pattern.compile("^1[3-9]\\d{9}$").matcher(phone).matches();
        //香港手机号码格式
        boolean matches2 = Pattern.compile("^(5|6|8|9)\\d{7}$").matcher(phone).matches();
        if(!matches && !matches2){
            return ResponseData.fail("号码格式不正确");
        }
        Integer count = outCallBlackListMapper.getOutCallBlackListTotal(phone);
        if(count>0){
            return ResponseData.fail("黑名单已存在该号码");
        }
        OutCallBlackListDTO outCallBlackList=new OutCallBlackListDTO();
        outCallBlackList.setPhone(phone);
        outCallBlackList.setRemarks(remarks);
        outCallBlackList.setOperateName(userName);
        return ResponseData.success(outCallBlackListMapper.insertOutCallBlackList(outCallBlackList));
    }

    @Override
    @RequestMapping(value = "/del", method = {RequestMethod.POST})
    public ResponseData<Integer> deleteOutCallBlackListById(Integer id) {
        return ResponseData.success(outCallBlackListMapper.deleteOutCallBlackListById(id));
    }

    @Override
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ResponseData<Map<String,Object>> getOutCallBlackList(String phone,Integer pageNo,Integer pageSize) {
        pageSize=pageSize==null?5:pageSize;
        pageNo=pageNo==null?0:(pageNo-1)*pageSize;
        Map<String,Object> map=new HashMap<>();
        map.put("data",outCallBlackListMapper.getOutCallBlackList("".equals(phone)?null:phone, pageNo, pageSize));
        map.put("totalCount",outCallBlackListMapper.getOutCallBlackListTotal("".equals(phone)?null:phone));
        return ResponseData.success(map);
    }

    @Override
    @RequestMapping(value = "/callLeadTask", method = {RequestMethod.POST})
    public ResponseData<Boolean> callLeadTask(Integer leadId,String phone) {
        Integer userId=SessionHelper.getCurrentUserId();
        if(phone != null && !"".equals(phone.trim())){
            //查询是否是黑名单号码
            Integer count = outCallBlackListMapper.getOutCallBlackListTotal(phone);
            //号码在黑名单中存在
            if(count>0){
                //修改线索和线索任务表中的状态为已拒绝，添加线索历史记录
                Lead lead=new Lead();
                lead.setId(leadId);
                lead.setLeadStatus(LeadConstants.LEAD_STATUS_ENUME.REJECT.getValue());//线索状态为已拒绝
                leadMapper.updateByPrimaryKeySelective(lead);
                LeadHistory leadHistory=new LeadHistory();
                leadHistory.setLeadId(leadId);
                leadHistory.setCreateBy(userId);
                leadHistory.setCreateTime(new Date());
                leadHistory.setStatus(LeadConstants.LEAD_STATUS_ENUME.REJECT.getValue());
                leadHistoryMapper.insert(leadHistory);
                leadMapper.updateLeadTaskByLeadId(leadId,LeadConstants.LEAD_TASK_STATUS_ENUME.DONT_CONTACT.getValue());//线索任务状态为已拒绝
                return ResponseData.fail("该客户为黑名单用户，无须联系");
            }
        }else{
            return ResponseData.fail("没有找到手机号码");
        }
        return ResponseData.success(true);
    }
}
