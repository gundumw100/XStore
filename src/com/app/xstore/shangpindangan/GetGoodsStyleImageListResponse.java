package com.app.xstore.shangpindangan;

import java.util.List;

public class GetGoodsStyleImageListResponse {

	GetGoodsStyleImageList Info;
	
	public GetGoodsStyleImageList getInfo() {
		return Info;
	}


	public void setInfo(GetGoodsStyleImageList info) {
		Info = info;
	}


	class GetGoodsStyleImageList{
		List<ProdColorImage> imageInfo;

		public List<ProdColorImage> getImageInfo() {
			return imageInfo;
		}

		public void setImageInfo(List<ProdColorImage> imageInfo) {
			this.imageInfo = imageInfo;
		}
		
	}
}
