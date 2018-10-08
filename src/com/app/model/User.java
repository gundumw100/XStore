package com.app.model;

import java.util.ArrayList;

public class User {

	private int phoneType=0;// 0->M5(thimfone);1->G0550;2->Android

	private UserInfo userInfo;
	private ShopInfo shopInfo;//当前登录的门店
	
	private ArrayList<ShopInfo> companyShop;//商户用户所有的门店

	public int getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(int phoneType) {
		this.phoneType = phoneType;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public ShopInfo getShopInfo() {
		return shopInfo;
	}

	public void setShopInfo(ShopInfo shopInfo) {
		this.shopInfo = shopInfo;
	}

	public ArrayList<ShopInfo> getCompanyShop() {
		return companyShop;
	}

	public void setCompanyShop(ArrayList<ShopInfo> companyShop) {
		this.companyShop = companyShop;
	}

}
