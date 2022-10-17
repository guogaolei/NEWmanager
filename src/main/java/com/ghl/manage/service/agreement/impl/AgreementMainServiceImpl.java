package com.ghl.manage.service.agreement.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.ghl.manage.dao.InnerAgreementInfoMasterDao;
import com.ghl.manage.dao.InnerAgreementOrderFlowerDao;
import com.ghl.manage.dao.InnerAgreementOrderMasterDao;
import com.ghl.manage.dao.InnerAgreementAmountLogDao;
import com.ghl.manage.dao.InnerAgreementInfoFlowerDao;
import com.ghl.manage.entity.common.CommonResponse;
import com.ghl.manage.entity.table.InnerAgreementAmountLogEntity;
import com.ghl.manage.entity.table.InnerAgreementInfoFlowerEntity;
import com.ghl.manage.entity.table.InnerAgreementInfoMasterEntity;
import com.ghl.manage.entity.table.InnerAgreementOrderFlowerEntity;
import com.ghl.manage.entity.table.InnerAgreementOrderMasterEntity;
import com.ghl.manage.service.agreement.AgreementMainService;
import com.ghl.manage.utils.DateUtils;

@Service
public class AgreementMainServiceImpl implements AgreementMainService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	InnerAgreementOrderMasterDao InnerAgreementOrderMasterDao;
	@Autowired
	InnerAgreementOrderFlowerDao InnerAgreementOrderFlowerDao;
	@Autowired
	InnerAgreementInfoMasterDao InnerAgreementInfoMasterDao;
	@Autowired
	InnerAgreementInfoFlowerDao InnerAgreementInfoFlowerDao;
	@Autowired
	InnerAgreementAmountLogDao InnerAgreementAmountLogDao;
	@Override
	public CommonResponse getMainInfo() {
		CommonResponse commonResponse=new CommonResponse();
		//查询主营合通累计签单、合同总额、累计开票、累计收款、应收账款
		Map<String,Map<String,String>> map=new HashMap<String,Map<String,String>>();
		//查询所有主营合同
		logger.info("########################查询所有主营合同-------start------################");
		List<InnerAgreementOrderMasterEntity> masterAmountList = InnerAgreementOrderMasterDao.getMasterAmountList();
		Map<String,String> masterAllMonth=masterAmountDeal(masterAmountList);
		map.put("masterAllMonth", masterAllMonth);
		logger.info("########################查询所有主营合同-------end------################");

		//查询当月主营合同
		logger.info("########################查询当月主营合同-------start------################");
		Map<String, String> masterCurrentMonth = masterCurrentMonth();
		map.put("masterCurrentMonth", masterCurrentMonth);
		logger.info("########################查询当月主营合同-------end------################");

		//查询分包合同累计签单、合同总额、已收票金额、已付款金额、应付款金额
		
		logger.info("########################查询所有分包合同-------start------################");
		List<InnerAgreementOrderFlowerEntity> flowerAmountList = InnerAgreementOrderFlowerDao.getFlowerAmountList();
		Map<String, String> flowerAllMonth = flowerAmountDeal(flowerAmountList);
		map.put("flowerAllMonth", flowerAllMonth);
		logger.info("########################查询所有分包合同-------end------################");

		//查询分包当月合同
		logger.info("########################查询当月分包合同-------start------################");
		Map<String, String> flowerCurrentMonth=flowerCurrentMonth();
		map.put("flowerCurrentMonth", flowerCurrentMonth);
		logger.info("########################查询当月分包合同-------end------################");

		commonResponse.setCode("200");
		commonResponse.setMsg("success");
		commonResponse.setInfo(map);
		return commonResponse;
	}


	private Map<String, String> flowerCurrentMonth() {
		Map<String, String> map =new HashMap<String, String>();
		BigDecimal flowerTotalAmount=new BigDecimal("0");//分包合同总金额
		BigDecimal flowerToFinishAmount=new BigDecimal("0");//已收票金额
		BigDecimal flowerFinishAmount=new BigDecimal("0");//已付款金额
		BigDecimal flowerNofinishAmount=new BigDecimal("0");//应付款金额
		BigDecimal flowerLeavelAmount=new BigDecimal("0");//实际待付款金额
		List<InnerAgreementInfoFlowerEntity> flowerList = InnerAgreementInfoFlowerDao.getCurrentFlowerList(DateUtils.getCurrentMonthStartDay(),DateUtils.getCurrentMonthEndDay());
		for(InnerAgreementInfoFlowerEntity entity:flowerList) {
			String totalAmount = entity.getTotalAmount();
			if(StringUtils.isEmpty(totalAmount)) {
				totalAmount="0";
			}
			BigDecimal totalAmountBig=new BigDecimal(totalAmount);
			flowerTotalAmount=flowerTotalAmount.add(totalAmountBig);
			List<InnerAgreementAmountLogEntity> list=InnerAgreementAmountLogDao.getAmountLogs(entity.getNo(),DateUtils.getCurrentMonthStartDay(),DateUtils.getCurrentMonthEndDay());
			for(InnerAgreementAmountLogEntity LogEntity:list) {
				String type = LogEntity.getType();
				if("3".equals(type)) {
					//已收票金额
					String amount = LogEntity.getAmount();
					if(StringUtils.isEmpty(amount)) {
						amount="0";
					}
					flowerToFinishAmount=flowerToFinishAmount.add(new BigDecimal(amount));
				}
				if("4".equals(type)) {
					//已收票金额
					String amount = LogEntity.getAmount();
					if(StringUtils.isEmpty(amount)) {
						amount="0";
					}
					flowerNofinishAmount=flowerNofinishAmount.add(new BigDecimal(amount));
				}
			}
		}
		flowerNofinishAmount=flowerToFinishAmount.subtract(flowerFinishAmount);
		flowerLeavelAmount=flowerTotalAmount.subtract(flowerFinishAmount);
		map.put("flowerCount", String.valueOf(flowerList.size()));//累计签单
		map.put("flowerTotalAmount", flowerTotalAmount.toString());//分包合同总额
		map.put("flowerToFinishAmount", flowerToFinishAmount.toString());//已收发票金额
		map.put("flowerFinishAmount", flowerFinishAmount.toString());//已付款金额
		map.put("flowerNofinishAmount", flowerNofinishAmount.toString());//应付款金额
		map.put("flowerLeavelAmount", flowerLeavelAmount.toString());//实际待付款金额
		return map;
	}
	private Map<String ,String > masterCurrentMonth(){
		Map<String,String> map =new HashMap<String,String>();
		BigDecimal totalAmount=new BigDecimal("0");
		BigDecimal finishAmount=new BigDecimal("0");
		BigDecimal noFinishAmount=new BigDecimal("0");
		BigDecimal inComeAmount=new BigDecimal("0");
		BigDecimal noComeAmount=new BigDecimal("0");
		//先查询当月签订合同
		Map<String,String> date =new HashMap<String,String>();
		date.put("start", DateUtils.getCurrentMonthStartDay());
		date.put("end", DateUtils.getCurrentMonthEndDay());
		List<InnerAgreementInfoMasterEntity> masterList=InnerAgreementInfoMasterDao.getCurrentMasterList(date);
		for(InnerAgreementInfoMasterEntity entity:masterList) {
				String totalAmount2 = entity.getTotalAmount();
				if(StringUtils.isEmpty(totalAmount2)) {
					totalAmount2="0";
				}
				BigDecimal totalAmount2Big=new BigDecimal(totalAmount2);
				totalAmount=totalAmount.add(totalAmount2Big);
				//根据合同编号查询票据记录
				List<InnerAgreementAmountLogEntity> list=InnerAgreementAmountLogDao.getAmountLogs(entity.getNo(),DateUtils.getCurrentMonthStartDay(),DateUtils.getCurrentMonthEndDay());
				for(InnerAgreementAmountLogEntity LogEntity:list) {
					String type = LogEntity.getType();
					if("1".equals(type)) {
						//已开票金额
						String amount = LogEntity.getAmount();
						if(StringUtils.isEmpty(amount)) {
							amount="0";
						}
						finishAmount=finishAmount.add(new BigDecimal(amount));
					}
					if("2".equals(type)) {
						//已收款金额
						String amount = LogEntity.getAmount();
						if(StringUtils.isEmpty(amount)) {
							amount="0";
						}
						inComeAmount=inComeAmount.add(new BigDecimal(amount));
					}
				}
		}
		noFinishAmount=totalAmount.subtract(finishAmount);
		noComeAmount=finishAmount.subtract(inComeAmount);
		map.put("masterCount", String.valueOf(masterList.size()));//累计签单
		map.put("totalAmount", totalAmount.toString());//合同总额
		map.put("finishAmount", finishAmount.toString());//已开票金额
		map.put("noFinishAmount", noFinishAmount.toString());//未开票金额
		map.put("inComeAmount", inComeAmount.toString());//累计收款
		map.put("noComeAmount", noComeAmount.toString());//应收账款
		return map;
	}
	private Map<String, String> flowerAmountDeal(List<InnerAgreementOrderFlowerEntity> flowerAmountList) {
		Map<String, String> map =new HashMap<String, String>();
		if(null == flowerAmountList || flowerAmountList.size()<1) {
			map.put("flowerCount", "0");//累计签单
			map.put("flowerTotalAmount", "0");//分包合同总额
			map.put("flowerToFinishAmount", "0");//已收发票金额
			map.put("flowerFinishAmount", "0");//已付款金额
			map.put("flowerNofinishAmount", "0");//应付款金额
			map.put("flowerLeavelAmount", "0");//实际待付款金额
		}else {
			BigDecimal flowerTotalAmount=new BigDecimal("0");
			BigDecimal flowerToFinishAmount=new BigDecimal("0");
			BigDecimal flowerFinishAmount=new BigDecimal("0");
			BigDecimal flowerNofinishAmount=new BigDecimal("0");
			BigDecimal flowerLeavelAmount=new BigDecimal("0");
			for(InnerAgreementOrderFlowerEntity entity:flowerAmountList) {
				String totalAmount = entity.getTotalAmount();
				if(StringUtils.isEmpty(totalAmount)) {
					totalAmount="0";
				}
				BigDecimal totalAmountBig=new BigDecimal(totalAmount);
				flowerTotalAmount=flowerTotalAmount.add(totalAmountBig);
				
				String ToFinishAmount = entity.getToFinishAmount();
				if(StringUtils.isEmpty(ToFinishAmount)) {
					ToFinishAmount="0";
				}
				BigDecimal ToFinishAmountBig=new BigDecimal(ToFinishAmount);
				flowerToFinishAmount=flowerToFinishAmount.add(ToFinishAmountBig);
				
				String FinishAmount = entity.getFinishAmount();
				if(StringUtils.isEmpty(FinishAmount)) {
					FinishAmount="0";
				}
				BigDecimal FinishAmountBig=new BigDecimal(FinishAmount);
				flowerFinishAmount=flowerFinishAmount.add(FinishAmountBig);
				
				String NoFinishAmount = entity.getNofinishAmount();
				if(StringUtils.isEmpty(NoFinishAmount)) {
					NoFinishAmount="0";
				}
				BigDecimal NoFinishAmountBig=new BigDecimal(NoFinishAmount);
				flowerNofinishAmount=flowerNofinishAmount.add(NoFinishAmountBig);
				
				String LeavelAmount = entity.getLeavelAmount();
				if(StringUtils.isEmpty(LeavelAmount)) {
					LeavelAmount="0";
				}
				BigDecimal LeavelAmountBig=new BigDecimal(LeavelAmount);
				flowerLeavelAmount=flowerNofinishAmount.add(LeavelAmountBig);
			}
			flowerNofinishAmount=flowerToFinishAmount.subtract(flowerFinishAmount);
			flowerLeavelAmount=flowerTotalAmount.subtract(flowerFinishAmount);
			map.put("flowerCount", String.valueOf(flowerAmountList.size()));//累计签单
			map.put("flowerTotalAmount", flowerTotalAmount.toString());//分包合同总额
			map.put("flowerToFinishAmount", flowerToFinishAmount.toString());//已收发票金额
			map.put("flowerFinishAmount", flowerFinishAmount.toString());//已付款金额
			map.put("flowerNofinishAmount", flowerNofinishAmount.toString());//应付款金额
			map.put("flowerLeavelAmount", flowerLeavelAmount.toString());//实际待付款金额
		}
		return map;
	}

	private Map<String,String> masterAmountDeal(List<InnerAgreementOrderMasterEntity> masterAmountList){
		Map<String,String> map =new HashMap<String,String>();
		if(null == masterAmountList || masterAmountList.size()<1) {
			map.put("masterCount", "0");//累计签单
			map.put("totalAmount", "0");//合同总额
			map.put("finishAmount", "0");//已开票金额
			map.put("noFinishAmount", "0");//未开票金额
			map.put("inComeAmount", "0");//累计收款
			map.put("noComeAmount", "0");//应收账款
		}else {
			BigDecimal totalAmount=new BigDecimal("0");
			BigDecimal finishAmount=new BigDecimal("0");
			BigDecimal noFinishAmount=new BigDecimal("0");
			BigDecimal inComeAmount=new BigDecimal("0");
			BigDecimal noComeAmount=new BigDecimal("0");
			for(InnerAgreementOrderMasterEntity entity:masterAmountList) {
				String totalAmount2 = entity.getTotalAmount();
				if(StringUtils.isEmpty(totalAmount2)) {
					totalAmount2="0";
				}
				BigDecimal totalAmount2Big=new BigDecimal(totalAmount2);
				totalAmount=totalAmount.add(totalAmount2Big);
				
				String finishAmount2 = entity.getFinishAmount();
				if(StringUtils.isEmpty(finishAmount2)) {
					finishAmount2="0";
				}
				BigDecimal finishAmount2Big=new BigDecimal(finishAmount2);
				finishAmount=finishAmount.add(finishAmount2Big);
				
				String noFinishAmount2 = entity.getNoFinishAmount();
				if(StringUtils.isEmpty(noFinishAmount2)) {
					noFinishAmount2="0";
				}
				BigDecimal noFinishAmount2Big=new BigDecimal(noFinishAmount2);
				noFinishAmount=noFinishAmount.add(noFinishAmount2Big);
				
				String inComeAmount2 = entity.getInComeAmount();
				if(StringUtils.isEmpty(inComeAmount2)) {
					inComeAmount2="0";
				}
				BigDecimal inComeAmount2Big=new BigDecimal(inComeAmount2);
				inComeAmount=inComeAmount.add(inComeAmount2Big);
				
				String noComeAmount2 = entity.getNoComeAmount();
				if(StringUtils.isEmpty(noComeAmount2)) {
					noComeAmount2="0";
				}
				BigDecimal noComeAmount2Big=new BigDecimal(noComeAmount2);
				noComeAmount=noComeAmount.add(noComeAmount2Big);
			}
			map.put("masterCount", String.valueOf(masterAmountList.size()));//累计签单
			map.put("totalAmount", totalAmount.toString());//合同总额
			map.put("finishAmount", finishAmount.toString());//已开票金额
			map.put("noFinishAmount", noFinishAmount.toString());//未开票金额
			map.put("inComeAmount", inComeAmount.toString());//累计收款
			map.put("noComeAmount", noComeAmount.toString());//应收账款
		}
		return map;
	}
}
