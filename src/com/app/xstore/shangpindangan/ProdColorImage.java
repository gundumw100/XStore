package com.app.xstore.shangpindangan;

/**
 * 颜色对应的图片
 * @author Ni Guijun
 *
 */
public class ProdColorImage extends ProdCommon{

	public long id;
    public String colorCode;
    public String styleCode;
    public String imgUrl;
    public String description;
    
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getStyleCode() {
		return styleCode;
	}

	public void setStyleCode(String styleCode) {
		this.styleCode = styleCode;
	}

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
		return imgUrl;
	}
}
