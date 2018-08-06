package com.app.model.response;

import java.util.List;

import com.app.model.JvbillsaleInfo;

public class GetBillSaleByDateResponse extends BaseResponse {

	List<JvbillsaleInfo> HeadInfo;

	public List<JvbillsaleInfo> getHeadInfo() {
		return HeadInfo;
	}

	public void setHeadInfo(List<JvbillsaleInfo> headInfo) {
		HeadInfo = headInfo;
	}
	
}
