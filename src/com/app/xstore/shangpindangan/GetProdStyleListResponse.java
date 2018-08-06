package com.app.xstore.shangpindangan;

import java.util.List;

import com.app.model.response.BaseResponse;

public class GetProdStyleListResponse extends BaseResponse {

//	{"ErrMessage":"","Result":true,"Info":[{"DateCode":"2018","ShopCode":"S001","Description":"羽绒服","StyleCode":"0005"}],"ErrSysTrackMessage":"","ErrSysMessage":"","Message":"OK"}
	
	List<ProdStyle> Info;

	public List<ProdStyle> getInfo() {
		return Info;
	}

	public void setInfo(List<ProdStyle> info) {
		Info = info;
	}


}
