package com.app.model.response;

import java.util.ArrayList;

import com.app.model.ShopInfo;
import com.app.model.UserInfo;

public class LoginCheckDeviceResponse extends BaseResponse {

	ArrayList<ShopInfo> companyShop;//商户用户所有的门店
	int companyUser;//1表示是商户用户

	ArrayList<ShopInfo> Shop_Info;
	ArrayList<UserInfo> User_Info;

	public ArrayList<ShopInfo> getShop_Info() {
		return Shop_Info;
	}

	public void setShop_Info(ArrayList<ShopInfo> shop_Info) {
		Shop_Info = shop_Info;
	}

	public ArrayList<UserInfo> getUser_Info() {
		return User_Info;
	}

	public void setUser_Info(ArrayList<UserInfo> user_Info) {
		User_Info = user_Info;
	}

	public ArrayList<ShopInfo> getCompanyShop() {
		return companyShop;
	}

	public void setCompanyShop(ArrayList<ShopInfo> companyShop) {
		this.companyShop = companyShop;
	}

	public int getCompanyUser() {
		return companyUser;
	}

	public void setCompanyUser(int companyUser) {
		this.companyUser = companyUser;
	}

}
