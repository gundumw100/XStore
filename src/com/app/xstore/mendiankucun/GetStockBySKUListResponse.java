package com.app.xstore.mendiankucun;

import java.util.List;

import com.app.model.response.BaseResponse;
import com.app.xstore.mendiandiaochu.ChuRuKuProduct;

public class GetStockBySKUListResponse extends BaseResponse{

	List<ChuRuKuProduct> Info;

	public List<ChuRuKuProduct> getInfo() {
		return Info;
	}

	public void setInfo(List<ChuRuKuProduct> info) {
		Info = info;
	}
	
}
