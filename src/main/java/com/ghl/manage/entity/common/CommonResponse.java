package com.ghl.manage.entity.common;

public class CommonResponse {

	String code;
	String msg;
	Object info="";
	public CommonResponse() {}
	public CommonResponse(String code,String msg){
		this.code=code;
		this.msg=msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getInfo() {
		return info;
	}

	public void setInfo(Object info) {
		this.info = info;
	}
	
}
