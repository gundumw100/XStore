package com.app.xstore.shangpindangan;

import java.util.List;

import com.app.model.response.BaseResponse;

public class GetProdCsListResponse extends BaseResponse {

//	{"ErrMessage":"","Result":true,"Info":[],"ErrSysTrackMessage":"","ErrSysMessage":"","Message":"OK"}

	List<ProdCs> Info;

	public List<ProdCs> getInfo() {
		return Info;
	}

	public void setInfo(List<ProdCs> info) {
		Info = info;
	}


}
