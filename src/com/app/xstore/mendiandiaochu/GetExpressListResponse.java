package com.app.xstore.mendiandiaochu;

import java.util.List;

import com.app.model.response.BaseResponse;

public class GetExpressListResponse extends BaseResponse{

	List<Express> Info;

	public List<Express> getInfo() {
		return Info;
	}

	public void setInfo(List<Express> info) {
		Info = info;
	}
	
	
}
