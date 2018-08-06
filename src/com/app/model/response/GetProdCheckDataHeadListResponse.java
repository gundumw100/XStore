package com.app.model.response;

import java.util.List;

import com.app.model.ProdCheckDataInfo;

public class GetProdCheckDataHeadListResponse extends BaseResponse{

	List<ProdCheckDataInfo> CheckDataList;

	public List<ProdCheckDataInfo> getCheckDataList() {
		return CheckDataList;
	}

	public void setCheckDataList(List<ProdCheckDataInfo> checkDataList) {
		CheckDataList = checkDataList;
	}
	
}
