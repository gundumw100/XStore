package com.app.xstore.mendiandiaochu;

public class Express {

//	{"ErrMessage":"","Result":true,"Info":[{"ID":0,"enabled":1,"shopCode":"S001","description":"申通快递","expCode":"01"},{"ID":0,"enabled":1,"shopCode":"S001","description":"圆通快递","expCode":"02"}],"ErrSysTrackMessage":"","ErrSysMessage":"","Message":"OK"}

	String shopCode;
	String expCode;
	String description;
	int enabled;
	public String getShopCode() {
		return shopCode;
	}
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}
	public String getExpCode() {
		return expCode;
	}
	public void setExpCode(String expCode) {
		this.expCode = expCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getEnabled() {
		return enabled;
	}
	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return description;
	}
	
	
}
