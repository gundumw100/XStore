package com.app.xstore.shangpindangan;

import java.util.List;

import com.app.model.response.BaseResponse;

public class GetProdSeasonListResponse extends BaseResponse {

//	{"ErrMessage":"","Result":true,"Info":[],"ErrSysTrackMessage":"","ErrSysMessage":"","Message":"OK"}

	List<ProdSeason> Info;

	public List<ProdSeason> getInfo() {
		return Info;
	}

	public void setInfo(List<ProdSeason> info) {
		Info = info;
	}


}
