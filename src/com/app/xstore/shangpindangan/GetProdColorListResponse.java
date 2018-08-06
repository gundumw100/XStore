package com.app.xstore.shangpindangan;

import java.util.List;

import com.app.model.response.BaseResponse;

public class GetProdColorListResponse extends BaseResponse {

//	{"ErrMessage":"","Result":true,"Info":[],"ErrSysTrackMessage":"","ErrSysMessage":"","Message":"OK"}

	List<ProdColor> Info;

	public List<ProdColor> getInfo() {
		return Info;
	}

	public void setInfo(List<ProdColor> info) {
		Info = info;
	}


}
