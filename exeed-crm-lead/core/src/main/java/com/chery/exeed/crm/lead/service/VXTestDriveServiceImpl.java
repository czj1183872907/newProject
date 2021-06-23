package com.chery.exeed.crm.lead.service;

import com.chery.exeed.crm.common.dto.ResponseData;
import com.chery.exeed.crm.lead.dto.VXTestDriveDTO;
import com.chery.exeed.crm.lead.mapper.VXTestDriveMapper;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestSchema(schemaId = "vxtestdrive-service")
@RequestMapping("/exeed/crm/api")
public class VXTestDriveServiceImpl implements VXTestDriveService {

    private final Logger logger = LoggerFactory.getLogger(VXTestDriveServiceImpl.class);

    @Autowired
    private VXTestDriveMapper vxTestDriveMapper;

    @Override
    @RequestMapping(value = "/vxtestdrive/list",method = RequestMethod.GET)
    public ResponseData<List<VXTestDriveDTO>> getVxTestDriveList() {
        List<VXTestDriveDTO> list = vxTestDriveMapper.list();
        return ResponseData.success(list);
    }

    @Override
    @RequestMapping(value = "/vxtestdrive/update",method = RequestMethod.POST)
    public ResponseData<Integer> updateVxTestDrive( @RequestParam("id")String id, @RequestParam("carType")String carType,@RequestParam("status")Integer status) {
        return ResponseData.success(vxTestDriveMapper.updateVxTestDrive(id, carType, status));
    }

//    @Override
//    @RequestMapping(value = "/vxtestdrive/delete",method = RequestMethod.POST)
//    public ResponseData<Integer> deleteVxTestDrive( @RequestParam("id")String id) {
//        return ResponseData.success(vxTestDriveMapper.updateVxTestDrive(id, null, 1));
//    }

    @Override
    @RequestMapping(value = "/vxtestdrive/add",method = RequestMethod.POST)
    public ResponseData<Integer> addVxTestDrive(@RequestParam("carType")String  carType,@RequestParam("status")Integer status) {
        return ResponseData.success(vxTestDriveMapper.addVxTestDrive("VX"+(vxTestDriveMapper.queryMaxId()+1),
                carType,status));
    }
}
