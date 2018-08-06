package com.app.xstore.shangpindangan;

import java.util.List;

import com.app.model.response.BaseResponse;

public class GetProdBrandListResponse extends BaseResponse {

//	{"ErrMessage":"","Result":true,"Info":[],"ErrSysTrackMessage":"","ErrSysMessage":"","Message":"OK"}

	List<ProdBrand> Info;

	public List<ProdBrand> getInfo() {
		return Info;
	}

	public void setInfo(List<ProdBrand> info) {
		Info = info;
	}


}
