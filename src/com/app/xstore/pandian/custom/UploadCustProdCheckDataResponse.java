package com.app.xstore.pandian.custom;

import java.util.List;

import com.app.model.ProdCheckDataInfo;
import com.app.model.response.BaseResponse;

public class UploadCustProdCheckDataResponse extends BaseResponse{

	List<ProdCheckDataInfo> ProdCheckDataResult;

	public List<ProdCheckDataInfo> getProdCheckDataResult() {
		return ProdCheckDataResult;
	}

	public void setProdCheckDataResult(List<ProdCheckDataInfo> prodCheckDataResult) {
		ProdCheckDataResult = prodCheckDataResult;
	}
	
}
