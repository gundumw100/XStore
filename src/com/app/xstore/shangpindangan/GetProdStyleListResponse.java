package com.app.xstore.shangpindangan;

import java.util.List;

import com.app.model.response.BaseResponse;

public class GetProdStyleListResponse extends BaseResponse {

	List<ProdStyle> Info;

	public List<ProdStyle> getInfo() {
		return Info;
	}

	public void setInfo(List<ProdStyle> info) {
		Info = info;
	}


}
