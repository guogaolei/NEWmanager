package com.ghl.manage.service.login.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.ghl.manage.entity.common.CommonResponse;
import com.ghl.manage.entity.login.LoginRequestParam;
import com.ghl.manage.service.login.LoginService;

import cn.dev33.satoken.stp.StpUtil;

@Service
public class LoginServiceImpl implements LoginService{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public CommonResponse login(LoginRequestParam request) {
		CommonResponse response=new CommonResponse();
		if(StringUtils.isEmpty(request.getUsername()) || StringUtils.isEmpty(request.getPassword()) ) {
			response.setCode("100");
			response.setMsg("must param empty");
			return response;
		}
		if(request.getUsername().equals("aaa")) {
			StpUtil.login(10001);
		}else {
			response.setCode("100");
			response.setMsg("param error");
			return response;
		}
		response.setCode("200");
		response.setMsg("success");
		return response;
	}

	@Override
	public CommonResponse loginOut() {
		StpUtil.logout();
		CommonResponse response=new CommonResponse();
		response.setCode("200");
		response.setMsg("success");
		return response;
	}

}
