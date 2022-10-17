package com.ghl.manage.entity.agreement;

public class AgreementLogRequestParam {

	String no;//合同编号
	String amountForBill;//已开票待回款金额
	String amountForMoney;//付款金额
	String dateForBill;//开票日期
	String dateForMoney;//付款日期
	String toFinishAmount;//已收发票额
	String finishAmount;//已付款金额
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getAmountForBill() {
		return amountForBill;
	}
	public void setAmountForBill(String amountForBill) {
		this.amountForBill = amountForBill;
	}
	public String getAmountForMoney() {
		return amountForMoney;
	}
	public void setAmountForMoney(String amountForMoney) {
		this.amountForMoney = amountForMoney;
	}
	public String getDateForBill() {
		return dateForBill;
	}
	public void setDateForBill(String dateForBill) {
		this.dateForBill = dateForBill;
	}
	public String getDateForMoney() {
		return dateForMoney;
	}
	public void setDateForMoney(String dateForMoney) {
		this.dateForMoney = dateForMoney;
	}
	public String getToFinishAmount() {
		return toFinishAmount;
	}
	public void setToFinishAmount(String toFinishAmount) {
		this.toFinishAmount = toFinishAmount;
	}
	public String getFinishAmount() {
		return finishAmount;
	}
	public void setFinishAmount(String finishAmount) {
		this.finishAmount = finishAmount;
	}
	
	
}
