package com.chery.exeed.crm.lead.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Author:xiaowei.zhu
 * 2019/1/22 9:06
 */
public class LeadDistributeDTO implements Serializable {
    private List<LeadDealerDTO> LeadDealerList;

    public List<LeadDealerDTO> getLeadDealerList() {
        return LeadDealerList;
    }

    public void setLeadDealerList(List<LeadDealerDTO> leadDealerList) {
        LeadDealerList = leadDealerList;
    }
}
