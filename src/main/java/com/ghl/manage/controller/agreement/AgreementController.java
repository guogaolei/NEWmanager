package com.ghl.manage.controller.agreement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ghl.manage.entity.agreement.AgreementLogRequestParam;
import com.ghl.manage.entity.agreement.AgreementRequestParam;
import com.ghl.manage.entity.agreement.AgreementSideRequestParam;
import com.ghl.manage.entity.agreement.MainFirstRequestParam;
import com.ghl.manage.entity.common.CommonResponse;
import com.ghl.manage.service.agreement.AgreementMainService;
import com.ghl.manage.service.agreement.AgreementService;
import com.ghl.manage.service.agreement.AgreementServiceSide;
@CrossOrigin(originPatterns = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@Controller
public class AgreementController {

	//增加合同
	@Autowired
	AgreementService agreementService;
	@Autowired
	AgreementServiceSide agreementServiceSide;
	@Autowired
	AgreementMainService agreementMainService;
	@RequestMapping(value = "addAgreement",method = {RequestMethod.POST})
	@ResponseBody
	public CommonResponse addAgreement(@RequestBody AgreementRequestParam request) {
		return agreementService.addAgreement(request);
	}
	
	//查询合同编号是否存在
	@RequestMapping(value = "checkAgreeNum",method = {RequestMethod.POST})
	@ResponseBody
	public CommonResponse checkAgreeNum(@RequestBody AgreementRequestParam request) {
		return agreementService.checkAgreeNum(request);
	}
	//新增分包合同
	@RequestMapping(value = "addAgreementSide",method = {RequestMethod.POST})
	@ResponseBody
	public CommonResponse addAgreementSide(@RequestBody AgreementSideRequestParam request) {
		return agreementServiceSide.addAgreementSide(request);
	}
	
	//主营合同新增票据记录
	@RequestMapping(value = "addLog",method = {RequestMethod.POST})
	@ResponseBody
	public CommonResponse addAgreementLog(@RequestBody AgreementLogRequestParam request) {
		return agreementService.addAgreementLog(request);
	}
	
	//分包合同新增票据记录
	@RequestMapping(value = "addSideLog",method = {RequestMethod.POST})
	@ResponseBody
	public CommonResponse addAgreementSideLog(@RequestBody AgreementLogRequestParam request) {
		return agreementServiceSide.addAgreementLog(request);
	}
	
	//主营合同查询
	@RequestMapping(value = "getAgreement",method = {RequestMethod.GET})
	@ResponseBody
	public CommonResponse getAgreement(String no) {
		return agreementService.getAgreement(no);
	}
	//所有主营合同编号及名称
	@RequestMapping(value = "queryAgreement",method = {RequestMethod.GET})
	@ResponseBody
	public CommonResponse queryAgreement() {
		return agreementService.queryAgreement();
	}
	
	//分包合同合同查询
	@RequestMapping(value = "getAgreementSide",method = {RequestMethod.GET})
	@ResponseBody
	public CommonResponse getAgreementSide(String no) {
		return agreementServiceSide.getAgreementSide(no);
	}
	
	//主营合同修改
	@RequestMapping(value = "updateAgreement",method = {RequestMethod.POST})
	@ResponseBody
	public CommonResponse updateAgreement(@RequestBody AgreementRequestParam request) {
		return agreementService.updateAgreement(request);
	}
	
	//分包合同修改
	@RequestMapping(value = "updateAgreementSide",method = {RequestMethod.POST})
	@ResponseBody
	public CommonResponse updateAgreementSide(@RequestBody AgreementSideRequestParam request) {
		return agreementServiceSide.updateAgreementSide(request);
	}
	//查询主页所需数据
	@RequestMapping(value = "getSumInfo",method = {RequestMethod.GET})
	@ResponseBody
	public CommonResponse getMainInfo() {
		return agreementMainService.getMainInfo();
	}
	
	//主营合同首页
	@RequestMapping(value = "masterFirstPage",method = {RequestMethod.POST})
	@ResponseBody
	public CommonResponse masterFirstPage(@RequestBody MainFirstRequestParam request) {
			return agreementService.masterFirstPage(request);
	}
	
	//分包合同首页
	@RequestMapping(value = "flowerFirstPage",method = {RequestMethod.POST})
	@ResponseBody
	public CommonResponse flowerFirstPage(@RequestBody MainFirstRequestParam requeste) {
			return agreementServiceSide.flowerFirstPage(requeste);
	}
	
	//查询票据记录
	@RequestMapping(value = "getLog",method = {RequestMethod.GET})
	@ResponseBody
	public CommonResponse getLog(String no) {
		return agreementService.getLog(no);
	}
	//主营合同信息查看分包合同信息
	@RequestMapping(value = "masterFlowerInfo",method = {RequestMethod.GET})
	@ResponseBody
	public CommonResponse masterFlowerInfo(String no) {
		return agreementServiceSide.masterFlowerInfo(no);
	}
}
