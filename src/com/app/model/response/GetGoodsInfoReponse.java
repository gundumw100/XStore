package com.app.model.response;

import com.app.model.Goods;

public class GetGoodsInfoReponse extends BaseResponse{

	Goods goods;

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}
	
}
