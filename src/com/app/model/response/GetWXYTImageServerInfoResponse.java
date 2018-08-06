package com.app.model.response;

import java.util.List;

import com.app.model.WXYTImageServerInfo;

public class GetWXYTImageServerInfoResponse extends BaseResponse{

	List<WXYTImageServerInfo> HeadInfo;

	public List<WXYTImageServerInfo> getHeadInfo() {
		return HeadInfo;
	}

	public void setHeadInfo(List<WXYTImageServerInfo> headInfo) {
		HeadInfo = headInfo;
	}
	
}
