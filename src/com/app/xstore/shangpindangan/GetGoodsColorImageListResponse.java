package com.app.xstore.shangpindangan;

import java.util.List;

import com.app.model.response.BaseResponse;

public class GetGoodsColorImageListResponse extends BaseResponse{

	GetGoodsColorImage Info;
	
	public GetGoodsColorImage getInfo() {
		return Info;
	}

	public void setInfo(GetGoodsColorImage info) {
		Info = info;
	}

	public class GetGoodsColorImage{
		List<ProdColorImage> imageInfo;

		public List<ProdColorImage> getImageInfo() {
			return imageInfo;
		}

		public void setImageInfo(List<ProdColorImage> imageInfo) {
			this.imageInfo = imageInfo;
		}
		
	}
}
