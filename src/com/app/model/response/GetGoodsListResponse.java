package com.app.model.response;

import java.util.List;

import com.app.model.Goods;

public class GetGoodsListResponse {

//	{"GoodsInfo":[{"goods_sn":"0111100188308231","goods_name":"兰纳斯学生暖被青果绿","goods_desc":null,"goods_price":0.0},{"goods_sn":"0111100188308232","goods_name":"兰纳斯学生暖被青果绿","goods_desc":null,"goods_price":0.0},{"goods_sn":"0111100188308233","goods_name":"兰纳斯学生暖被青果绿","goods_desc":null,"goods_price":0.0}],"Result":true,"Message":"获取成功","ErrMessage":"","ErrSysMessage":"","ErrSysTrackMessage":""}
	
	List<Goods> GoodsInfo;

	public List<Goods> getGoodsInfo() {
		return GoodsInfo;
	}

	public void setGoodsInfo(List<Goods> goodsInfo) {
		GoodsInfo = goodsInfo;
	}
	
}
