package com.app.model.response;

import java.util.List;

import com.app.model.JvclerkspacestylelabelInfo;

public class GetClerkSpaceStyleLabelResponse extends BaseResponse {

	List<JvclerkspacestylelabelInfo> HeadInfo;

	public List<JvclerkspacestylelabelInfo> getHeadInfo() {
		return HeadInfo;
	}

	public void setHeadInfo(List<JvclerkspacestylelabelInfo> headInfo) {
		HeadInfo = headInfo;
	}
	
	
}
