package com.ghl.manage.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ghl.manage.entity.table.InnerAgreementOrderFlowerEntity;

@Repository
public interface InnerAgreementOrderFlowerDao {

	void addInnerAgreementOrderFlower(InnerAgreementOrderFlowerEntity entity);
	int getCountByNo(String no);
	InnerAgreementOrderFlowerEntity getInnerAgreementOrderFlower(String no);
	void updateAgreementOrderFlower(InnerAgreementOrderFlowerEntity entity);
	void updateFlowerTotalAmountAndMainNo(InnerAgreementOrderFlowerEntity entity);
	List<InnerAgreementOrderFlowerEntity> getFlowerAmountList();
	List<InnerAgreementOrderFlowerEntity> getInnerAgreementOrderFlowerByMainNo(String no);
}
