package com.app.xstore.shangpindangan;

/**
 * 类别
 * @author Ni Guijun
 *
 */
public class ProdSort extends ProdCommon{

    public String sortCode;
    public String description;
    
	public String getSortCode() {
		return sortCode;
	}

	public void setSortCode(String sortCode) {
		this.sortCode = sortCode;
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
