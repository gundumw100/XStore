package com.app.model;

public class JvbillsaleInfo {

	String shopCode;
	String saleNo;
	String oldSaleNo;
	String createuser;
	String currencyCode;
	String saleType;//销售模式:XS(销售)、TH(退货)、HH(换货)
	double totalMoney;
	int totalQty;
	String userId;
	String vipId;//会员ID
	String vipCode;//卡号
	double vipConsumeValue;//消费积分
	String remark;
	String createtimeStr;
	
	public String getShopCode() {
		return shopCode;
	}
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}
	
	public String getSaleNo() {
		return saleNo;
	}
	public void setSaleNo(String saleNo) {
		this.saleNo = saleNo;
	}
	public String getOldSaleNo() {
		return oldSaleNo;
	}
	public void setOldSaleNo(String oldSaleNo) {
		this.oldSaleNo = oldSaleNo;
	}
	public String getCreateuser() {
		return createuser;
	}
	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getSaleType() {
		return saleType;
	}
	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}
	public double getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(double totalMoney) {
		this.totalMoney = totalMoney;
	}
	public int getTotalQty() {
		return totalQty;
	}
	public void setTotalQty(int totalQty) {
		this.totalQty = totalQty;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getVipCode() {
		return vipCode;
	}
	public void setVipCode(String vipCode) {
		this.vipCode = vipCode;
	}
	public String getVipId() {
		return vipId;
	}
	public void setVipId(String vipId) {
		this.vipId = vipId;
	}
	public double getVipConsumeValue() {
		return vipConsumeValue;
	}
	public void setVipConsumeValue(double vipConsumeValue) {
		this.vipConsumeValue = vipConsumeValue;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreatetimeStr() {
		return createtimeStr;
	}
	public void setCreatetimeStr(String createtimeStr) {
		this.createtimeStr = createtimeStr;
	}

}
