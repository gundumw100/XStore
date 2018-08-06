package com.app.xstore.shangpindangan;

import java.util.List;

import com.app.model.response.BaseResponse;

public class GetProdJLDWListResponse extends BaseResponse {

//	{"ErrMessage":"","Result":true,"Info":[],"ErrSysTrackMessage":"","ErrSysMessage":"","Message":"OK"}

	List<ProdJLDW> Info;

	public List<ProdJLDW> getInfo() {
		return Info;
	}

	public void setInfo(List<ProdJLDW> info) {
		Info = info;
	}


}
