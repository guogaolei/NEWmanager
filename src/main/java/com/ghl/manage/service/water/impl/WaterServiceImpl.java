package com.ghl.manage.service.water.impl;

import com.ghl.manage.dao.WaterGroundDao;
import com.ghl.manage.entity.common.CommonResponse;
import com.ghl.manage.entity.table.WaterGroundEntity;
import com.ghl.manage.service.water.WaterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WaterServiceImpl implements WaterService {
    @Resource
    WaterGroundDao WaterGroundDao;
    @Override
    public CommonResponse waterground() {
        CommonResponse response=new CommonResponse();
        Map<String,Object> info=new HashMap<String,Object>();
        Map<String,Object> geoData=new HashMap<String,Object>();
        List<Map<String,Object>> features=new ArrayList<>();
        List<WaterGroundEntity> waterGround = WaterGroundDao.getWaterGround();
        for (WaterGroundEntity entity:waterGround
             ) {
            Map<String,Object> temp=new HashMap<String,Object>();
            Map<String,String> properties=new HashMap<String,String>();
            properties.put("pid",entity.getId());
            properties.put("layer",entity.getLayer());
            properties.put("name",entity.getNo());
            properties.put("type",entity.getType());
            properties.put("position",entity.getPosition());
            Map<String,String> geometry=new HashMap<String,String>();
            geometry.put("type","'Point'");
            geometry.put("coordinates","["+entity.getX()+","+entity.getY()+"]");
            temp.put("type","'Feature'");
            temp.put("properties",properties);
            temp.put("geometry",geometry);
            features.add(temp);
        }
        geoData.put("type","'FeatureCollection'");
        geoData.put("features",features);
        info.put("geoData",geoData);
        response.setCode("200");
        response.setMsg("success");
        response.setInfo(info);
        return response;
    }
}
