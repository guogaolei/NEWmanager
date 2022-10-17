package com.ghl.manage.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ghl.manage.entity.table.InnerAgreementInfoFlowerEntity;

@Repository
public interface InnerAgreementInfoFlowerDao {

	void addInnerAgreementInfoFlower(InnerAgreementInfoFlowerEntity entity);

	InnerAgreementInfoFlowerEntity getInnerAgreementInfoFlower(String no);

	void updateInnerAgreementInfoFlower(InnerAgreementInfoFlowerEntity entity);

	List<InnerAgreementInfoFlowerEntity> getCurrentFlowerList(String start, String end);

	List<InnerAgreementInfoFlowerEntity> queryFlowerListByPage();

	List<InnerAgreementInfoFlowerEntity> queryFlowerListMsg(Map<String, String> map);
	
}
