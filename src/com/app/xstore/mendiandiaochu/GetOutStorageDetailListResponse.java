package com.app.xstore.mendiandiaochu;

import java.util.List;

import com.app.model.response.BaseResponse;

public class GetOutStorageDetailListResponse extends BaseResponse {

	List<ChuRuKuProduct> Info;

	public List<ChuRuKuProduct> getInfo() {
		return Info;
	}

	public void setInfo(List<ChuRuKuProduct> info) {
		Info = info;
	}
	
}
