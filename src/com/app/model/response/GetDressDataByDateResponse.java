package com.app.model.response;

import java.util.List;

import com.app.model.Dress;

public class GetDressDataByDateResponse extends BaseResponse{

	List<Dress> HeadInfo;

	public List<Dress> getHeadInfo() {
		return HeadInfo;
	}

	public void setHeadInfo(List<Dress> headInfo) {
		HeadInfo = headInfo;
	}
	
}
