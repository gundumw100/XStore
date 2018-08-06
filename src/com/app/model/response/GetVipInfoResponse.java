package com.app.model.response;

import java.util.List;

import com.app.model.Member;

public class GetVipInfoResponse extends BaseResponse{

	List<Member> HeadInfo;

	public List<Member> getHeadInfo() {
		return HeadInfo;
	}

	public void setHeadInfo(List<Member> headInfo) {
		HeadInfo = headInfo;
	}
	
	
}
