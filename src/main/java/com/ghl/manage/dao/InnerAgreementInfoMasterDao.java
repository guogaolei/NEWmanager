package com.ghl.manage.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ghl.manage.entity.table.InnerAgreementInfoMasterEntity;

@Repository
public interface InnerAgreementInfoMasterDao {

	void addInnerAgreementInfoMaster(InnerAgreementInfoMasterEntity entity);

	InnerAgreementInfoMasterEntity getAgreementInfoMaster(String no);

	void updateInnerAgreementInfoMaster(InnerAgreementInfoMasterEntity entity);

	List<InnerAgreementInfoMasterEntity> getCurrentMasterList(Map map);

	List<InnerAgreementInfoMasterEntity> queryMasterListByPage();

	List<Map<String,String>> queryAllMasterNoAndName();

	List<InnerAgreementInfoMasterEntity> queryMasterListMsg(Map<String, String> map);
}
