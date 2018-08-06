package com.app.model.response;

import java.util.List;

import com.app.model.EmailInfo;

public class GetEmailInfoResponse extends BaseResponse {

	List<EmailInfo> HeadInfo;

	public List<EmailInfo> getHeadInfo() {
		return HeadInfo;
	}

	public void setHeadInfo(List<EmailInfo> headInfo) {
		HeadInfo = headInfo;
	}

}
