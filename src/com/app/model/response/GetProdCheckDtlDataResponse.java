package com.app.model.response;

import java.util.ArrayList;

import com.app.model.ProdCheckDtl;

public class GetProdCheckDtlDataResponse extends BaseResponse {

	ArrayList<ProdCheckDtl> DetailInfo;

	public ArrayList<ProdCheckDtl> getDetailInfo() {
		return DetailInfo;
	}

	public void setDetailInfo(ArrayList<ProdCheckDtl> detailInfo) {
		DetailInfo = detailInfo;
	}
	
}
