package com.app.model.response;

import java.util.List;

import com.app.model.ProdCheckDataInfo;
import com.app.model.ProdPreChcekData;

public class UploadProdCheckDataResponse extends BaseResponse {

	String UploadFailType;
	List<ProdCheckDataInfo> ProdCheckDataResult;
	List<ProdPreChcekData> NotEqualPreCheckData;
	List<String> NoProdNumRelatedBarcode;
	
	public String getUploadFailType() {
		return UploadFailType;
	}
	public void setUploadFailType(String uploadFailType) {
		UploadFailType = uploadFailType;
	}
	public List<ProdCheckDataInfo> getProdCheckDataResult() {
		return ProdCheckDataResult;
	}
	public void setProdCheckDataResult(List<ProdCheckDataInfo> prodCheckDataResult) {
		ProdCheckDataResult = prodCheckDataResult;
	}
	public List<ProdPreChcekData> getNotEqualPreCheckData() {
		return NotEqualPreCheckData;
	}
	public void setNotEqualPreCheckData(List<ProdPreChcekData> notEqualPreCheckData) {
		NotEqualPreCheckData = notEqualPreCheckData;
	}
	public List<String> getNoProdNumRelatedBarcode() {
		return NoProdNumRelatedBarcode;
	}
	public void setNoProdNumRelatedBarcode(List<String> noProdNumRelatedBarcode) {
		NoProdNumRelatedBarcode = noProdNumRelatedBarcode;
	}

	
}
