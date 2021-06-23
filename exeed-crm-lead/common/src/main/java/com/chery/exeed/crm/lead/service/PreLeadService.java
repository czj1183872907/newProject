package com.chery.exeed.crm.lead.service;

import com.chery.exeed.crm.common.dto.ResponseData;
import com.chery.exeed.crm.lead.dto.ImportDTO;
import com.chery.exeed.crm.lead.dto.PreLeadDTO;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RequestBody;

public interface PreLeadService {

    ResponseData<Boolean> create(@RequestBody PreLeadDTO preLead);

    ResponseData<String> importPreLead(@RequestBody ImportDTO importDTO) throws Exception;

    void sendLead2PreLead();
    void processFinal(String msg);
    void testDrive(String msg);
    void processLead(String msg);
    void dccQueue(String msg);
}
