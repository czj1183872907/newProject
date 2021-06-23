package com.chery.exeed.crm.lead.utils;

import com.alibaba.fastjson.JSON;
import com.chery.exeed.crm.common.util.BeanUtils;
import com.chery.exeed.crm.lead.constants.LeadConstants.LEAD_STATUS_ENUME;
import com.chery.exeed.crm.lead.model.Customer;
import com.chery.exeed.crm.lead.model.PreLead;

/**
 * @Author: zhen.liu@nttdata.com
 * @Date: 2019/2/26 9:44
 * @Description:
 */
public class LeadCommonUtil {

    public static Boolean checkAccessDistributeLeadStatus(Integer value) {
        if (value != LEAD_STATUS_ENUME.WIN.getValue()
//                && value != LEAD_STATUS_ENUME.LOSE.getValue()
                && value != LEAD_STATUS_ENUME.CREATED.getValue()
                && value != LEAD_STATUS_ENUME.SEND.getValue()
                && value != LEAD_STATUS_ENUME.REJECT.getValue()
                && value != LEAD_STATUS_ENUME.RETURN_VISIT.getValue()
                && value != LEAD_STATUS_ENUME.SAME_LIST.getValue()
                && value != LEAD_STATUS_ENUME.TEST.getValue()
//                && value != LEAD_STATUS_ENUME.ACCEPT.getValue()
        )
        {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
    }

}
