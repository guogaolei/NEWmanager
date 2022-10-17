package com.ghl.manage.service.login;

import com.ghl.manage.entity.common.CommonResponse;
import com.ghl.manage.entity.login.LoginRequestParam;

public interface LoginService {

	CommonResponse login(LoginRequestParam request);

	CommonResponse loginOut();
}
