package com.app.model.response;

import java.util.List;

import com.app.model.UserInfo;

public class GetShopUserListResponse extends BaseResponse{

	List<UserInfo> User_Info;

	public List<UserInfo> getUser_Info() {
		return User_Info;
	}

	public void setUser_Info(List<UserInfo> user_Info) {
		User_Info = user_Info;
	}
	
}
