package com.app.xstore.shangpindangan;

import java.util.List;

import com.app.model.response.BaseResponse;

public class GetProdSpecListResponse extends BaseResponse {

//	{"ErrMessage":"","Result":true,"Info":[],"ErrSysTrackMessage":"","ErrSysMessage":"","Message":"OK"}

	List<ProdSpec> Info;

	public List<ProdSpec> getInfo() {
		return Info;
	}

	public void setInfo(List<ProdSpec> info) {
		Info = info;
	}


}
