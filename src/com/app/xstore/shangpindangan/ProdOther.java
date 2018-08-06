package com.app.xstore.shangpindangan;

public class ProdOther extends ProdCommon{

    public String otherCode;
    public String description;
    
	public String getOtherCode() {
		return otherCode;
	}

	public void setOtherCode(String otherCode) {
		this.otherCode = otherCode;
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
