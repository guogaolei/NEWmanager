package com.ghl.manage.service.agreement.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.ghl.manage.dao.InnerAgreementInfoMasterDao;
import com.ghl.manage.dao.InnerAgreementOrderFlowerDao;
import com.ghl.manage.dao.InnerAgreementOrderMasterDao;
import com.ghl.manage.entity.agreement.AgreementLogRequestParam;
import com.ghl.manage.entity.agreement.AgreementLogsEntity;
import com.ghl.manage.entity.agreement.AgreementRequestParam;
import com.ghl.manage.entity.agreement.MainFirstRequestParam;
import com.ghl.manage.entity.agreement.MasterMainPageEntity;
import com.ghl.manage.entity.common.CommonResponse;
import com.ghl.manage.entity.table.InnerAgreementAmountLogEntity;
import com.ghl.manage.entity.table.InnerAgreementInfoMasterEntity;
import com.ghl.manage.entity.table.InnerAgreementOrderFlowerEntity;
import com.ghl.manage.entity.table.InnerAgreementOrderMasterEntity;
import com.ghl.manage.service.agreement.AgreementService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
@Transactional(rollbackFor=Exception.class)
@Service
public class AgreementServiceImpl implements AgreementService{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	InnerAgreementOrderMasterDao InnerAgreementOrderMasterDao;
	@Autowired
	InnerAgreementOrderFlowerDao InnerAgreementOrderFlowerDao;
	@Autowired
	InnerAgreementInfoMasterDao InnerAgreementInfoMasterDao;
	@Autowired
	com.ghl.manage.dao.InnerAgreementAmountLogDao InnerAgreementAmountLogDao;
	@Override
	public CommonResponse addAgreement(AgreementRequestParam request) {
		CommonResponse commonResponse=new CommonResponse();
		InnerAgreementOrderMasterEntity entity=new InnerAgreementOrderMasterEntity();
		BeanUtils.copyProperties(request, entity);
		entity.setFinishAmount("0");entity.setInComeAmount("0");entity.setNoComeAmount("0");entity.setNoFinishAmount("0");
		InnerAgreementOrderMasterDao.addInnerAgreementOrderMaster(entity);
		
		InnerAgreementInfoMasterEntity entity2=new InnerAgreementInfoMasterEntity();
		BeanUtils.copyProperties(request, entity2);
		InnerAgreementInfoMasterDao.addInnerAgreementInfoMaster(entity2);
		commonResponse.setCode("200");
		commonResponse.setMsg("success");
		return commonResponse;
	}
	@Override
	public CommonResponse checkAgreeNum(AgreementRequestParam request) {
		CommonResponse commonResponse=new CommonResponse();
		int countByNo = InnerAgreementOrderMasterDao.getCountByNo(request.getNo());
		int countByNo2 = InnerAgreementOrderFlowerDao.getCountByNo(request.getNo());
		if(countByNo >0 || countByNo2>0) {
			commonResponse.setCode("100");
			commonResponse.setMsg("合同编号已存在，请更改！");
		}else {
			commonResponse.setCode("200");
			commonResponse.setMsg("success");
		}
		return commonResponse;
	}
	@SuppressWarnings("deprecation")
	@Override
	public CommonResponse addAgreementLog(AgreementLogRequestParam request) {
		CommonResponse commonResponse=new CommonResponse();
		if(!StringUtils.isEmpty(request.getAmountForBill())){
			InnerAgreementAmountLogEntity entity=new InnerAgreementAmountLogEntity();
			entity.setAmount(request.getAmountForBill());
			entity.setDate(request.getDateForBill());
			entity.setNo(request.getNo());
			entity.setType("1");
			//增加开票记录
			logger.info("################增加开票记录：no="+request.getNo()+"########################");
			InnerAgreementAmountLogDao.addInnerAgreementAmountLog(entity);
			//更新未开票金额
			InnerAgreementOrderMasterEntity innerAgreementOrderMaster = InnerAgreementOrderMasterDao.getInnerAgreementOrderMaster(request.getNo());
			String totalAmount = innerAgreementOrderMaster.getTotalAmount();
			String finishAmount = innerAgreementOrderMaster.getFinishAmount();
			String inComeAmount = innerAgreementOrderMaster.getInComeAmount();
			BigDecimal totalAmountBig=new BigDecimal(totalAmount);
			BigDecimal CurrentFinishAmount=new BigDecimal(request.getAmountForBill());//本次开票金额
			if(com.alibaba.druid.util.StringUtils.isEmpty(finishAmount)) {
				finishAmount="0";
			}
			if(com.alibaba.druid.util.StringUtils.isEmpty(inComeAmount)) {
				inComeAmount="0";
			}
			BigDecimal finishAmountBig=new BigDecimal(finishAmount);
			finishAmountBig=finishAmountBig.add(CurrentFinishAmount);//已开票金额
			BigDecimal NoFinishAmount = totalAmountBig.subtract(finishAmountBig);//剩余开票金额
			BigDecimal noComeAmountBig=finishAmountBig.subtract(new BigDecimal(inComeAmount));//应收款金额
			innerAgreementOrderMaster.setNoFinishAmount(NoFinishAmount.toString());
			innerAgreementOrderMaster.setFinishAmount(finishAmountBig.toString());
			innerAgreementOrderMaster.setNoComeAmount(noComeAmountBig.toString());
			logger.info("################更新已开票金额：no="+request.getNo()+"########################");
			logger.info("################总金额："+totalAmount+",已开票金额："+finishAmount+",本次开票金额:"+request.getAmountForBill()+",剩余金额："+noComeAmountBig.toString()+"########################");
			InnerAgreementOrderMasterDao.updateInnerAgreementOrderMaster(innerAgreementOrderMaster);
		}
		if(!StringUtils.isEmpty(request.getAmountForMoney())){
			InnerAgreementAmountLogEntity entity=new InnerAgreementAmountLogEntity();
			entity.setAmount(request.getAmountForMoney());
			entity.setDate(request.getDateForMoney());
			entity.setNo(request.getNo());
			entity.setType("2");
			logger.info("################增加已收款记录：no="+request.getNo()+"########################");
			InnerAgreementAmountLogDao.addInnerAgreementAmountLog(entity);
			//更新已收款金额
			InnerAgreementOrderMasterEntity innerAgreementOrderMaster = InnerAgreementOrderMasterDao.getInnerAgreementOrderMaster(request.getNo());
			String InComeAmount = innerAgreementOrderMaster.getInComeAmount();//已收款金额
			String finishAmount = innerAgreementOrderMaster.getFinishAmount();//开票金额
			BigDecimal ForMoney=new BigDecimal(request.getAmountForMoney());
			BigDecimal finishMoney=new BigDecimal(finishAmount);
			if(com.alibaba.druid.util.StringUtils.isEmpty(InComeAmount)) {
				InComeAmount="0";
			}else {
				ForMoney=ForMoney.add(new BigDecimal(InComeAmount));//已收款金额+本次收款金额
			}
			BigDecimal subtract = finishMoney.subtract(ForMoney);//应收款金额
			innerAgreementOrderMaster.setInComeAmount(ForMoney.toString());
			innerAgreementOrderMaster.setNoComeAmount(subtract.toString());
			logger.info("################更新已收款金额：no="+request.getNo()+"########################");
			logger.info("################已收款金额："+InComeAmount+",本次收款金额："+request.getAmountForMoney()+",总收款金额:"+ForMoney.toString()+",应收款金额："+subtract.toString()+"########################");
			InnerAgreementOrderMasterDao.updateInnerAgreementOrderMaster(innerAgreementOrderMaster);
		}
		
		commonResponse.setCode("200");
		commonResponse.setMsg("success");
		return commonResponse;
	}
	@Override
	public CommonResponse getAgreement(String no) {
		CommonResponse commonResponse=new CommonResponse();
		InnerAgreementInfoMasterEntity agreementInfoMaster = InnerAgreementInfoMasterDao.getAgreementInfoMaster(no);
		commonResponse.setCode("200");
		commonResponse.setMsg("success");
		commonResponse.setInfo(agreementInfoMaster);
		return commonResponse;
	}
	@Override
	public CommonResponse updateAgreement(AgreementRequestParam request) {
		CommonResponse commonResponse=new CommonResponse();
		InnerAgreementInfoMasterEntity entity=new InnerAgreementInfoMasterEntity();
		BeanUtils.copyProperties(request, entity);
		InnerAgreementInfoMasterDao.updateInnerAgreementInfoMaster(entity);
		InnerAgreementOrderMasterEntity entity2=new InnerAgreementOrderMasterEntity();
		BeanUtils.copyProperties(request, entity2);
		InnerAgreementOrderMasterDao.updateMasterTotalAmount(entity2);
		commonResponse.setCode("200");
		commonResponse.setMsg("success");
		return commonResponse;
	}
	@Override
	public CommonResponse masterFirstPage(MainFirstRequestParam request) {
		CommonResponse commonResponse=new CommonResponse();
		Map<String,Object> result=new HashMap<>();
		List<MasterMainPageEntity> listInfo=new ArrayList<>();
		PageHelper.startPage(Integer.valueOf(request.getPage()),10);
		PageInfo<InnerAgreementInfoMasterEntity> pageInfo=null;
		if(com.alibaba.druid.util.StringUtils.isEmpty(request.getNo()) && com.alibaba.druid.util.StringUtils.isEmpty(request.getBussinessName())) {
			pageInfo = new PageInfo<>(InnerAgreementInfoMasterDao.queryMasterListByPage());
		}else {
			Map<String ,String> map=new HashMap<>();
			map.put("no", request.getNo());
			map.put("bussinessName", request.getBussinessName());
			pageInfo = new PageInfo<>(InnerAgreementInfoMasterDao.queryMasterListMsg(map));
		}
		//
		List<InnerAgreementInfoMasterEntity> list = pageInfo.getList();
		for(InnerAgreementInfoMasterEntity entity:list) {
			MasterMainPageEntity body=new MasterMainPageEntity();
			BeanUtils.copyProperties(entity, body);
			String no = entity.getNo();
			InnerAgreementOrderMasterEntity innerAgreementOrderMaster = InnerAgreementOrderMasterDao.getInnerAgreementOrderMaster(no);
			BeanUtils.copyProperties(innerAgreementOrderMaster, body);
			//判断是否有分包合同
			List<InnerAgreementOrderFlowerEntity> OrderFlowerList = InnerAgreementOrderFlowerDao.getInnerAgreementOrderFlowerByMainNo(no);
			BigDecimal totalAmountBig=new BigDecimal("0");
			BigDecimal toFinishAmountBig=new BigDecimal("0");
			BigDecimal finishAmountBig=new BigDecimal("0");
			BigDecimal nofinishAmountBig=new BigDecimal("0");
			BigDecimal leavelAmountBig=new BigDecimal("0");
			for(InnerAgreementOrderFlowerEntity OrderFlowerEntity:OrderFlowerList) {
				String totalAmount = OrderFlowerEntity.getTotalAmount();//分包合同总额
				String toFinishAmount = OrderFlowerEntity.getToFinishAmount();//已收发票额
				String finishAmount = OrderFlowerEntity.getFinishAmount();//已付款金额
				String nofinishAmount = OrderFlowerEntity.getNofinishAmount();//应付款
				String leavelAmount = OrderFlowerEntity.getLeavelAmount();//实际待付款金额
				if(com.alibaba.druid.util.StringUtils.isEmpty(totalAmount)) {
					totalAmount="0";
				}
				if(com.alibaba.druid.util.StringUtils.isEmpty(toFinishAmount)) {
					toFinishAmount="0";
				}
				if(com.alibaba.druid.util.StringUtils.isEmpty(finishAmount)) {
					finishAmount="0";
				}
				if(com.alibaba.druid.util.StringUtils.isEmpty(nofinishAmount)) {
					nofinishAmount="0";
				}
				if(com.alibaba.druid.util.StringUtils.isEmpty(leavelAmount)) {
					leavelAmount="0";
				}
				totalAmountBig=totalAmountBig.add(new BigDecimal(totalAmount));
				toFinishAmountBig=toFinishAmountBig.add(new BigDecimal(toFinishAmount));
				finishAmountBig=finishAmountBig.add(new BigDecimal(finishAmount));
				nofinishAmountBig=nofinishAmountBig.add(new BigDecimal(nofinishAmount));
				leavelAmountBig=leavelAmountBig.add(new BigDecimal(leavelAmount));
			}
			body.setFlowerTotalAmount(totalAmountBig.toString());
			body.setFlowerFinishAmount(finishAmountBig.toString());
			body.setFlowerToFinishAmount(toFinishAmountBig.toString());
			body.setFlowerNofinishAmount(nofinishAmountBig.toString());
			body.setFlowerLeavelAmount(leavelAmountBig.toString());
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
	public CommonResponse queryAgreement() {
		CommonResponse commonResponse=new CommonResponse();
		//查询所有主营合同
		List<Map<String,String>> list = InnerAgreementInfoMasterDao.queryAllMasterNoAndName();
		commonResponse.setCode("200");
		commonResponse.setMsg("success");
		commonResponse.setInfo(list);
		return commonResponse;
	}
	@Override
	public CommonResponse getLog(String no) {
		CommonResponse commonResponse=new CommonResponse();
		List<Map<String,String>> amountLogs = InnerAgreementAmountLogDao.getAmountLogsByNo(no);
		List<AgreementLogsEntity> coverList = coverList(amountLogs);
		commonResponse.setCode("200");
		commonResponse.setMsg("success");
		commonResponse.setInfo(coverList);
		return commonResponse;
	}
	private List<AgreementLogsEntity> coverList(List<Map<String, String>> list) {
		List<AgreementLogsEntity> result=new ArrayList<>();
		for(Map<String, String> map:list) {
			boolean addFlag=true;
			AgreementLogsEntity entity=new AgreementLogsEntity();
			String type = map.get("type");
			if("1".equals(type)) {
				entity.setAmountForBill(map.get("amount"));
				entity.setDateForBill(map.get("date"));
			}
			if("2".equals(type)) {
				//判断是否当有开票记录
				int index = checkThisDayHaveOtherLogs(result, "1", map.get("date"));
				if(-1 != index) {
					addFlag=false;
					entity=result.get(index);
				}
				entity.setDateForMoney(map.get("date"));
				entity.setAmountForMoney(map.get("amount"));
			}
			if("3".equals(type)) {
				entity.setToFinishAmount(map.get("amount"));
				entity.setToFinisDate(map.get("date"));
			}
			if("4".equals(type)) {
				int index = checkThisDayHaveOtherLogs(result, "3", map.get("date"));
				if(-1 != index) {
					addFlag=false;
					entity=result.get(index);
				}
				entity.setFinishAmount(map.get("amount"));
				entity.setFinishDate(map.get("date"));
			}
			if(addFlag) {
				result.add(entity);
			}
		}
		return result;
	}
	//check

	private int checkThisDayHaveOtherLogs(List<AgreementLogsEntity> result,String type,String date) {
			for(int i=0;i<result.size(); i++) {
				AgreementLogsEntity agreementLogsEntity = result.get(i);
				String DATE;
				if("1".equals(type)) {
					DATE=agreementLogsEntity.getDateForBill();
				}else {
					DATE=agreementLogsEntity.getToFinisDate();
				}
				if(DATE.equals(date)) {
					return i;
				}
			}
			return -1;
	}

	
}
