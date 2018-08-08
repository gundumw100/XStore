package com.app.xstore.shangpindangan;

public class ProdColor extends ProdCommon{

    public String colorCode;
    public String description;
    public String imgUrl;
    
	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return description;
	}
}
