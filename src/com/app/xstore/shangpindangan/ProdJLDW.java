package com.app.xstore.shangpindangan;

public class ProdJLDW extends ProdCommon{

    public String jldwCode;
    public String description;
    

	public String getJldwCode() {
		return jldwCode;
	}

	public void setJldwCode(String jldwCode) {
		this.jldwCode = jldwCode;
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
