package com.ghl.manage.controller.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ghl.manage.entity.common.CommonResponse;
import com.ghl.manage.entity.login.LoginRequestParam;
import com.ghl.manage.service.login.LoginService;

@Controller
public class LoginController {
	
	@Autowired
	LoginService loginService;
	@RequestMapping(value = "login",method = {RequestMethod.POST})
	@ResponseBody
	public CommonResponse login(@RequestBody LoginRequestParam request) {
		return loginService.login(request);
	}
	
	@RequestMapping(value = "loginOut",method = {RequestMethod.GET})
	@ResponseBody
	public CommonResponse loginOut() {
		return loginService.loginOut();
	}
	
}
