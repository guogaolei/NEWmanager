package com.ghl.manage.service.agreement.impl;

import com.ghl.manage.dao.InnerAgreementAmountLogDao;
import com.ghl.manage.dao.InnerAgreementInfoFlowerDao;
import com.ghl.manage.dao.InnerAgreementInfoMasterDao;
import com.ghl.manage.dao.InnerAgreementOrderFlowerDao;
import com.ghl.manage.entity.agreement.*;
import com.ghl.manage.entity.common.CommonResponse;
import com.ghl.manage.entity.table.InnerAgreementAmountLogEntity;
import com.ghl.manage.entity.table.InnerAgreementInfoFlowerEntity;
import com.ghl.manage.entity.table.InnerAgreementInfoMasterEntity;
import com.ghl.manage.entity.table.InnerAgreementOrderFlowerEntity;
import com.ghl.manage.service.agreement.AgreementServiceSide;
import com.ghl.manage.utils.DateUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
@Transactional(rollbackFor=Exception.class)
public class AgreementServiceSideImpl implements AgreementServiceSide{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	InnerAgreementOrderFlowerDao InnerAgreementOrderFlowerDao;
	@Autowired
	InnerAgreementInfoFlowerDao InnerAgreementInfoFlowerDao;
	@Autowired
	InnerAgreementAmountLogDao InnerAgreementAmountLogDao;
	@Autowired
	InnerAgreementInfoMasterDao InnerAgreementInfoMasterDao;
	@Override
	public CommonResponse addAgreementSide(AgreementSideRequestParam request) {
		CommonResponse commonResponse=new CommonResponse();
		InnerAgreementOrderFlowerEntity entity=new InnerAgreementOrderFlowerEntity();
		BeanUtils.copyProperties(request, entity);
		entity.setFinishAmount("0");entity.setLeavelAmount(entity.getTotalAmount());entity.setNofinishAmount("0");entity.setToFinishAmount("0");
		InnerAgreementOrderFlowerDao.addInnerAgreementOrderFlower(entity);
		String time = DateUtils.getTime();
		InnerAgreementInfoFlowerEntity entity2=new InnerAgreementInfoFlowerEntity();
		BeanUtils.copyProperties(request, entity2);
		entity2.setUpdateTime(time);
		InnerAgreementInfoFlowerDao.addInnerAgreementInfoFlower(entity2);
		commonResponse.setCode("200");
		commonResponse.setMsg("success");
		return commonResponse;
	}
	@SuppressWarnings("deprecation")
	@Override
	public CommonResponse addAgreementLog(AgreementLogRequestParam request) {
		CommonResponse commonResponse=new CommonResponse();
		if(!StringUtils.isEmpty(request.getToFinishAmount())){
			//增加收票记录
			logger.info("################增加已收票记录：no="+request.getNo()+"########################");
			InnerAgreementAmountLogEntity entity=new InnerAgreementAmountLogEntity();
			entity.setAmount(request.getToFinishAmount());
			entity.setDate(request.getDateForBill());
			entity.setType("3");
			entity.setNo(request.getNo());
			InnerAgreementAmountLogDao.addInnerAgreementAmountLog(entity);
			//更新已收票金额
			InnerAgreementOrderFlowerEntity innerAgreementOrderFlower = InnerAgreementOrderFlowerDao.getInnerAgreementOrderFlower(request.getNo());
			String toFinishAmount = innerAgreementOrderFlower.getToFinishAmount();//已收票金额
			String finishAmount=innerAgreementOrderFlower.getFinishAmount();//已付款金额
			String NofinishAmount=innerAgreementOrderFlower.getNofinishAmount();//应付款金额
			String LeavelAmount=innerAgreementOrderFlower.getLeavelAmount();//实际待付款金额
			String totalAmount = innerAgreementOrderFlower.getTotalAmount();
			if(StringUtils.isEmpty(toFinishAmount)) {
				toFinishAmount="0";
			}
			if(StringUtils.isEmpty(finishAmount)) {
				finishAmount="0";
			}
			if(StringUtils.isEmpty(NofinishAmount)) {
				NofinishAmount="0";
			}
			if(StringUtils.isEmpty(LeavelAmount)) {
				LeavelAmount="0";
			}
			BigDecimal liveToFinishAmountBig=new BigDecimal(request.getToFinishAmount());//本次收票金额
			BigDecimal toFinishAmountBig=new BigDecimal(toFinishAmount);
			BigDecimal finishAmountBig=new BigDecimal(finishAmount);
			toFinishAmountBig=liveToFinishAmountBig.add(toFinishAmountBig);//已收票金额
			BigDecimal nofinishAmountBig=toFinishAmountBig.subtract(finishAmountBig);//应付款金额=已收票金额-已付款金额
			BigDecimal totalAmountBig=new BigDecimal(totalAmount);
			BigDecimal LeavelAmountBig =totalAmountBig.subtract(new BigDecimal(LeavelAmount));
			innerAgreementOrderFlower.setToFinishAmount(toFinishAmountBig.toString());
			innerAgreementOrderFlower.setNofinishAmount(nofinishAmountBig.toString());
			innerAgreementOrderFlower.setLeavelAmount(LeavelAmountBig.toString());
			logger.info("################更新已收票金额：no="+request.getNo()+"########################");
			logger.info("################分包合同金额："+innerAgreementOrderFlower.getTotalAmount()+",已收票金额："+toFinishAmount+",应付款金额："+NofinishAmount+"########################");
			logger.info("################本次收票金额:"+request.getToFinishAmount()+",计算后已收票金额："+toFinishAmountBig.toString()+",计算后应付款金额："+nofinishAmountBig.toString()+"########################");
			InnerAgreementOrderFlowerDao.updateAgreementOrderFlower(innerAgreementOrderFlower);
		}
		if(!StringUtils.isEmpty(request.getFinishAmount())){
			//增加付款票据记录
			logger.info("################增加付款记录：no="+request.getNo()+"########################");
			InnerAgreementAmountLogEntity entity=new InnerAgreementAmountLogEntity();
			entity.setAmount(request.getFinishAmount());
			entity.setDate(request.getDateForMoney());
			entity.setType("4");
			entity.setNo(request.getNo());
			InnerAgreementAmountLogDao.addInnerAgreementAmountLog(entity);
			//更新已付款金额
			InnerAgreementOrderFlowerEntity innerAgreementOrderFlower = InnerAgreementOrderFlowerDao.getInnerAgreementOrderFlower(request.getNo());
			String toFinishAmount = innerAgreementOrderFlower.getToFinishAmount();//已收票金额
			String finishAmount=innerAgreementOrderFlower.getFinishAmount();//已付款金额
			String NofinishAmount=innerAgreementOrderFlower.getNofinishAmount();//应付款金额
			String LeavelAmount=innerAgreementOrderFlower.getLeavelAmount();//实际待付款金额
			String totalAmount = innerAgreementOrderFlower.getTotalAmount();//总金额
			if(StringUtils.isEmpty(toFinishAmount)) {
				toFinishAmount="0";
			}
			if(StringUtils.isEmpty(finishAmount)) {
				finishAmount="0";
			}
			if(StringUtils.isEmpty(NofinishAmount)) {
				NofinishAmount="0";
			}
			if(StringUtils.isEmpty(LeavelAmount)) {
				LeavelAmount="0";
			}
			BigDecimal finishAmountBig=new BigDecimal(finishAmount);
			BigDecimal toFinishAmountBig=new BigDecimal(toFinishAmount);
			BigDecimal CurrentfinishAmountBig=new BigDecimal(request.getFinishAmount());
			finishAmountBig=finishAmountBig.add(CurrentfinishAmountBig);//已收票金额
			BigDecimal noFinishAmountBig = toFinishAmountBig.subtract(finishAmountBig);//应付款金额=已收票金额-已付款金额
			BigDecimal totalAmountBig=new BigDecimal(totalAmount);
			BigDecimal LeavelAmountBig=totalAmountBig.subtract(finishAmountBig);//实际待付款金额
			innerAgreementOrderFlower.setFinishAmount(finishAmountBig.toString());
			innerAgreementOrderFlower.setNofinishAmount(noFinishAmountBig.toString());
			innerAgreementOrderFlower.setLeavelAmount(LeavelAmountBig.toString());
			logger.info("################更新已付款金额：no="+request.getNo()+"########################");
			logger.info("################分包合同金额："+innerAgreementOrderFlower.getTotalAmount()+",已付款金额："+finishAmount+",本次付款金额:"+request.getFinishAmount()+",应付款金额："+NofinishAmount+",实际待付款金额："+LeavelAmount+"########################");
			logger.info("################本次付款金额:"+request.getFinishAmount()+",计算后已付款金额："+finishAmountBig.toString()+",计算后应付款金额："+noFinishAmountBig.toString()+",实际待付款金额："+LeavelAmountBig.toString()+"########################");
			InnerAgreementOrderFlowerDao.updateAgreementOrderFlower(innerAgreementOrderFlower);
		}
		commonResponse.setCode("200");
		commonResponse.setMsg("success");
		return commonResponse;
	}
	@Override
	public CommonResponse getAgreementSide(String no) {
		CommonResponse commonResponse=new CommonResponse();
		InnerAgreementInfoFlowerEntity entity=InnerAgreementInfoFlowerDao.getInnerAgreementInfoFlower(no);
		InnerAgreementOrderFlowerEntity innerAgreementOrderFlower = InnerAgreementOrderFlowerDao.getInnerAgreementOrderFlower(no);
		if(!com.alibaba.druid.util.StringUtils.isEmpty(innerAgreementOrderFlower.getMainNo())) {
			InnerAgreementInfoMasterEntity agreementInfoMaster = InnerAgreementInfoMasterDao.getAgreementInfoMaster(innerAgreementOrderFlower.getMainNo());
			entity.setMainNo(agreementInfoMaster.getNo());
			entity.setMainAgreeName(agreementInfoMaster.getAgreeName());
		}else {
			logger.info("################分包合同编号：no="+innerAgreementOrderFlower.getNo()+"没有主合同编号########################");
		}
		commonResponse.setCode("200");
		commonResponse.setMsg("success");
		commonResponse.setInfo(entity);
		return commonResponse;
	}
	@Override
	public CommonResponse updateAgreementSide(AgreementSideRequestParam request) {
		CommonResponse commonResponse=new CommonResponse();
		InnerAgreementInfoFlowerEntity entity=new InnerAgreementInfoFlowerEntity();
		BeanUtils.copyProperties(request, entity);
		entity.setUpdateTime(DateUtils.getTime());
		InnerAgreementInfoFlowerDao.updateInnerAgreementInfoFlower(entity);
		InnerAgreementOrderFlowerEntity entity2=new InnerAgreementOrderFlowerEntity();
		BeanUtils.copyProperties(request, entity2);
		InnerAgreementOrderFlowerDao.updateFlowerTotalAmountAndMainNo(entity2);
		commonResponse.setCode("200");
		commonResponse.setMsg("success");
		return commonResponse;
	}
	@Override
	public CommonResponse flowerFirstPage(MainFirstRequestParam request) {
		CommonResponse commonResponse=new CommonResponse();
		Map<String,Object> result=new HashMap<>();
		List<FlowerMainPageEntity> listInfo=new ArrayList<>();
		PageHelper.startPage(Integer.valueOf(request.getPage()),10);
		PageInfo<InnerAgreementInfoFlowerEntity> pageInfo;
		if(com.alibaba.druid.util.StringUtils.isEmpty(request.getNo()) && com.alibaba.druid.util.StringUtils.isEmpty(request.getBussinessName())) {
			pageInfo = new PageInfo<>(InnerAgreementInfoFlowerDao.queryFlowerListByPage());
		}else {
			Map<String ,String> map=new HashMap<>();
			map.put("no", request.getNo());
			map.put("bussinessName", request.getBussinessName());
			pageInfo = new PageInfo<>(InnerAgreementInfoFlowerDao.queryFlowerListMsg(map));
		}
		List<InnerAgreementInfoFlowerEntity> list = pageInfo.getList();
		for(InnerAgreementInfoFlowerEntity entity:list) {
			FlowerMainPageEntity body=new FlowerMainPageEntity();
			BeanUtils.copyProperties(entity, body);
			String no = entity.getNo();
			InnerAgreementOrderFlowerEntity innerAgreementOrderFlower = InnerAgreementOrderFlowerDao.getInnerAgreementOrderFlower(no);
			BeanUtils.copyProperties(innerAgreementOrderFlower, body);
			//查询主合同名称
			InnerAgreementOrderFlowerEntity OrderFlower = InnerAgreementOrderFlowerDao.getInnerAgreementOrderFlower(entity.getNo());
			if(!com.alibaba.druid.util.StringUtils.isEmpty(OrderFlower.getMainNo())) {
				InnerAgreementInfoMasterEntity agreementInfoMaster = InnerAgreementInfoMasterDao.getAgreementInfoMaster(OrderFlower.getMainNo());
				body.setMainNo(agreementInfoMaster.getNo());
				body.setMainAgreementName(agreementInfoMaster.getAgreeName());
			}else {
				logger.info("################分包合同编号：no="+OrderFlower.getNo()+"没有主合同编号########################");
			}
			listInfo.add(body);
		}
		result.put("totalPage", pageInfo.getTotal());
		result.put("nextPage", pageInfo.isHasNextPage());
		result.put("list", listInfo);
		commonResponse.setCode("200");
		commonResponse.setMsg("success");
		commonResponse.setInfo(result);	
		return commonResponse;
	}
	@Override
	public CommonResponse masterFlowerInfo(String no) {
		CommonResponse commonResponse=new CommonResponse();
		List<FlowerInfoResponseEntity> result=new ArrayList<>();
		List<InnerAgreementOrderFlowerEntity> list = InnerAgreementOrderFlowerDao.getInnerAgreementOrderFlowerByMainNo(no);
		BigDecimal totalAmount=new BigDecimal(0);
		BigDecimal toFinishAmount=new BigDecimal(0);
		BigDecimal finishAmount=new BigDecimal(0);
		BigDecimal nofinishAmount=new BigDecimal(0);
		BigDecimal leavelAmount=new BigDecimal(0);
		for(InnerAgreementOrderFlowerEntity entity:list) {
			totalAmount=totalAmount.add(new BigDecimal(entity.getTotalAmount()));
			toFinishAmount=toFinishAmount.add(new BigDecimal(entity.getToFinishAmount()));
			finishAmount=finishAmount.add(new BigDecimal(entity.getFinishAmount()));
			nofinishAmount=nofinishAmount.add(new BigDecimal(entity.getNofinishAmount()));
			leavelAmount=leavelAmount.add(new BigDecimal(entity.getLeavelAmount()));
			FlowerInfoResponseEntity target=new FlowerInfoResponseEntity();
			BeanUtils.copyProperties(entity, target);
			InnerAgreementInfoFlowerEntity innerAgreementInfoFlower = InnerAgreementInfoFlowerDao.getInnerAgreementInfoFlower(entity.getNo());
			target.setAgreeName(innerAgreementInfoFlower.getAgreeName());
			target.setAgreeYear(innerAgreementInfoFlower.getAgreeYear());
			result.add(target);
		}
		Map<String,Object> map=new HashMap<>();
		map.put("totalAmount", totalAmount.toString());
		map.put("toFinishAmount", toFinishAmount.toString());
		map.put("finishAmount", finishAmount.toString());
		map.put("nofinishAmount", nofinishAmount.toString());
		map.put("leavelAmount", leavelAmount.toString());
		map.put("list",result );
		commonResponse.setCode("200");
		commonResponse.setMsg("success");
		commonResponse.setInfo(map);
		return commonResponse;
	}
	
	

}
