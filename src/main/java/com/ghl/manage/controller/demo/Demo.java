package com.ghl.manage.controller.demo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ghl.manage.dao.UserDao;
import com.ghl.manage.entity.table.UserEntity;

@Controller
public class Demo {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserDao UserDao;
    
	@RequestMapping("demo")
	@ResponseBody
	public String demo() {
		logger.info("----------success---------");
		List<UserEntity> allUser = UserDao.getAllUser();
		
		return "success";
	}
}
