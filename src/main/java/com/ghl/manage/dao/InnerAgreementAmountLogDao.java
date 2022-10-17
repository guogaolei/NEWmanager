package com.ghl.manage.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ghl.manage.entity.table.InnerAgreementAmountLogEntity;

@Repository
public interface InnerAgreementAmountLogDao {

	void addInnerAgreementAmountLog(InnerAgreementAmountLogEntity entity);

	List<InnerAgreementAmountLogEntity> getAmountLogs(String no, String start,
			String end);
	
	List<Map<String, String>> getAmountLogsByNo(String no);
}
