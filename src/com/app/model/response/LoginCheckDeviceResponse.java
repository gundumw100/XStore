package com.app.model.response;

import java.util.ArrayList;

import com.app.model.ShopInfo;
import com.app.model.UserInfo;

public class LoginCheckDeviceResponse extends BaseResponse {

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

}
