package com.ghl.manage.entity.table;

public class InnerAgreementOrderMasterEntity {

	String no;//合同编号
	String totalAmount;//总金额
	String finishAmount;//已开票金额
	String noFinishAmount;//未开票金额
	String inComeAmount;//已收款金额
	String noComeAmount;//未收款金额
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getFinishAmount() {
		return finishAmount;
	}
	public void setFinishAmount(String finishAmount) {
		this.finishAmount = finishAmount;
	}
	public String getNoFinishAmount() {
		return noFinishAmount;
	}
	public void setNoFinishAmount(String noFinishAmount) {
		this.noFinishAmount = noFinishAmount;
	}
	public String getInComeAmount() {
		return inComeAmount;
	}
	public void setInComeAmount(String inComeAmount) {
		this.inComeAmount = inComeAmount;
	}
	public String getNoComeAmount() {
		return noComeAmount;
	}
	public void setNoComeAmount(String noComeAmount) {
		this.noComeAmount = noComeAmount;
	}
}
