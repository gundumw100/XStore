package com.app.xstore.pandian.custom;

import java.util.List;

import com.app.model.response.BaseResponse;

public class GetCustBoxInfoByCodeResponse extends BaseResponse{

	List<CustomGoods> goods;

	public List<CustomGoods> getGoods() {
		return goods;
	}

	public void setGoods(List<CustomGoods> goods) {
		this.goods = goods;
	}
	
}
