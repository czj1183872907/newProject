package com.chery.exeed.crm.lead.service;

import com.chery.exeed.crm.common.dto.ResponseData;
import com.chery.exeed.crm.lead.dto.VXTestDriveDTO;

import java.util.List;

public interface VXTestDriveService {
    ResponseData<List<VXTestDriveDTO>> getVxTestDriveList();

    ResponseData<Integer>  updateVxTestDrive(String id,String carType,Integer status);

//    ResponseData<Integer>  deleteVxTestDrive(String id);

    ResponseData<Integer>  addVxTestDrive(String carType,Integer status);
}
