package com.app.model.response;

import java.util.List;

import com.app.model.ProdCheckDataInfo;
import com.app.model.ProdCheckDtl;

public class GetProdCheckDataResponse extends BaseResponse {

	ProdCheckDataInfo HeadInfo;
	List<ProdCheckDtl> DetailInfo;
	
	public ProdCheckDataInfo getHeadInfo() {
		return HeadInfo;
	}
	public void setHeadInfo(ProdCheckDataInfo headInfo) {
		HeadInfo = headInfo;
	}
	public List<ProdCheckDtl> getDetailInfo() {
		return DetailInfo;
	}
	public void setDetailInfo(List<ProdCheckDtl> detailInfo) {
		DetailInfo = detailInfo;
	}
	
	
}
