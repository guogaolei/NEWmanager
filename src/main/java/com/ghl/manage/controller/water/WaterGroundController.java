package com.ghl.manage.controller.water;

import com.ghl.manage.entity.common.CommonResponse;
import com.ghl.manage.service.water.WaterService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class WaterGroundController {


    @Resource
    WaterService  WaterService;
    @RequestMapping(value = "waterground",method = {RequestMethod.POST})
    @ResponseBody
    public CommonResponse waterground() {
        return WaterService.waterground();
    }
}
