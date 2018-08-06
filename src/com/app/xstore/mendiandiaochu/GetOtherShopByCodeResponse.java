package com.app.xstore.mendiandiaochu;

import java.util.List;

import com.app.model.ShopInfo;
import com.app.model.response.BaseResponse;

public class GetOtherShopByCodeResponse extends BaseResponse{

	List<ShopInfo> shopInfo;

	public List<ShopInfo> getShopInfo() {
		return shopInfo;
	}

	public void setShopInfo(List<ShopInfo> shopInfo) {
		this.shopInfo = shopInfo;
	}
	
}
