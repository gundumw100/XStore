package com.app.xstore.shangpindangan;

import java.util.List;

import com.app.model.response.BaseResponse;

public class GetProdCwListResponse extends BaseResponse {

//	{"ErrMessage":"","Result":true,"Info":[],"ErrSysTrackMessage":"","ErrSysMessage":"","Message":"OK"}

	List<ProdCw> Info;

	public List<ProdCw> getInfo() {
		return Info;
	}

	public void setInfo(List<ProdCw> info) {
		Info = info;
	}



}
