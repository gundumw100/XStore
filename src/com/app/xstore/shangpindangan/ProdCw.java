package com.app.xstore.shangpindangan;

public class ProdCw extends ProdCommon{

    public String cwCode;
    public String description;
    
	public String getCwCode() {
		return cwCode;
	}

	public void setCwCode(String cwCode) {
		this.cwCode = cwCode;
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
