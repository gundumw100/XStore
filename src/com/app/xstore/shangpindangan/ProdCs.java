package com.app.xstore.shangpindangan;

/**
 * 厂商
 * @author Ni Guijun
 *
 */
public class ProdCs extends ProdCommon{

    public String csCode;
    public String description;
    
	public String getCsCode() {
		return csCode;
	}

	public void setCsCode(String csCode) {
		this.csCode = csCode;
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
