package com.app.xstore.shangpindangan;

import java.util.List;

import com.app.model.response.BaseResponse;

public class GetProdSortListResponse extends BaseResponse {

//	{"ErrMessage":"","Result":true,"Info":[],"ErrSysTrackMessage":"","ErrSysMessage":"","Message":"OK"}

	List<ProdSort> Info;

	public List<ProdSort> getInfo() {
		return Info;
	}

	public void setInfo(List<ProdSort> info) {
		Info = info;
	}



}
