package com.ghl.manage.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ghl.manage.entity.table.InnerAgreementOrderMasterEntity;

@Repository
public interface InnerAgreementOrderMasterDao {

	
	int getCountByNo(String no);

	void addInnerAgreementOrderMaster(InnerAgreementOrderMasterEntity entity);
	
	InnerAgreementOrderMasterEntity getInnerAgreementOrderMaster(String no);

	void updateInnerAgreementOrderMaster(InnerAgreementOrderMasterEntity innerAgreementOrderMaster);

	void updateMasterTotalAmount(InnerAgreementOrderMasterEntity entity);

	List<InnerAgreementOrderMasterEntity> getMasterAmountList();

	List<InnerAgreementOrderMasterEntity> getCurrentMouthMasterAmountList(String start,String end);

	List<InnerAgreementOrderMasterEntity> getCurrentMasterAmountList(String start,
			String end);
}
