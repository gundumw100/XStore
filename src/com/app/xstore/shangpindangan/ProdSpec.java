package com.app.xstore.shangpindangan;

/**
 * 尺码
 * @author Ni Guijun
 *
 */
public class ProdSpec extends ProdCommon{

    public String specCode;
    public String description;
    
	public String getSpecCode() {
		return specCode;
	}

	public void setSpecCode(String specCode) {
		this.specCode = specCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return description;
	}
}
