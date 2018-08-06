package com.app.xstore.shangpindangan;

import java.util.List;

import com.app.model.response.BaseResponse;

public class GetGoodsListResponse extends BaseResponse {

	List<ProductDangAn> GoodsInfo;

	public List<ProductDangAn> getGoodsInfo() {
		return GoodsInfo;
	}

	public void setGoodsInfo(List<ProductDangAn> goodsInfo) {
		GoodsInfo = goodsInfo;
	}
}
