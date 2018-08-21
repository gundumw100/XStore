package com.app.xstore.shangpindangan;

import java.util.List;

public class ProdSpecGroup extends ProdCommon{

	int backgroundColor;
	String groupCode;
    String description;
    List<ProdColor> colors;
    
	public int getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ProdColor> getColors() {
		return colors;
	}

	public void setColors(List<ProdColor> colors) {
		this.colors = colors;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return description;
	}
}
