package com.app.xstore.shangpindangan;

import java.util.List;

import com.app.model.response.BaseResponse;

public class GetProdOtherListResponse extends BaseResponse{

	List<ProdOther> Info;

	public List<ProdOther> getInfo() {
		return Info;
	}

	public void setInfo(List<ProdOther> info) {
		Info = info;
	}
	
}
