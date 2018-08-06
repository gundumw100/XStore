package com.app.xstore.pandian.custom;

import com.app.model.response.BaseResponse;

public class GetCustomGoodsInfoResponse extends BaseResponse{

	CustomGoods goods;

	public CustomGoods getGoods() {
		return goods;
	}

	public void setGoods(CustomGoods goods) {
		this.goods = goods;
	}

	
}
