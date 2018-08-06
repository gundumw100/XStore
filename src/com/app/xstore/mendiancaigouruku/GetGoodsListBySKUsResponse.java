package com.app.xstore.mendiancaigouruku;

import java.util.List;

import com.app.model.response.BaseResponse;
import com.app.xstore.shangpindangan.ProductDangAn;

public class GetGoodsListBySKUsResponse extends BaseResponse{

	List<ProductDangAn> GoodsInfo;

	public List<ProductDangAn> getGoodsInfo() {
		return GoodsInfo;
	}

	public void setGoodsInfo(List<ProductDangAn> goodsInfo) {
		GoodsInfo = goodsInfo;
	}
	
}
