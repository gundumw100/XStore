package com.app.model.response;

import java.util.List;

import com.app.model.ProdPreChcekData;

public class UploadProdPreCheckDataResponse extends BaseResponse {

	List<ProdPreChcekData> Precheck_Info;

	public List<ProdPreChcekData> getPrecheck_Info() {
		return Precheck_Info;
	}

	public void setPrecheck_Info(List<ProdPreChcekData> precheck_Info) {
		Precheck_Info = precheck_Info;
	}

}
