package com.app.xstore.shangpindangan;

/**
 * 标签
 * @author Ni Guijun
 *
 */
public class ProdLabel extends ProdCommon{

	
	public String classification;//服饰鞋帽首饰
	public String property;//分类
	public String labelCodeFullName;//classification+property+labelCode
	public String labelCode;
	public String labelDesc;
    public String description;
    
    public ProdLabel(){
    }
    
    public ProdLabel(String labelCode,String description){
    	this.labelCode=labelCode;
    	this.description=description;
    }
    
	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getLabelCodeFullName() {
		return labelCodeFullName;
	}

	public void setLabelCodeFullName(String labelCodeFullName) {
		this.labelCodeFullName = labelCodeFullName;
	}

	public String getLabelDesc() {
		return labelDesc;
	}

	public void setLabelDesc(String labelDesc) {
		this.labelDesc = labelDesc;
	}

	public String getLabelCode() {
		return labelCode;
	}

	public void setLabelCode(String labelCode) {
		this.labelCode = labelCode;
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
