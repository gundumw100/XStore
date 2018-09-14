package com.app.xstore.shangpindangan;

import java.util.List;

import com.app.model.response.BaseResponse;

public class GetProdLabelListResponse extends BaseResponse {

	List<ProdLabel> Info;

	public List<ProdLabel> getInfo() {
		return Info;
	}

	public void setInfo(List<ProdLabel> info) {
		Info = info;
	}


}
