package com.app.xstore.pandian.custom;

public class CustomGoods {

	private String box_code;// varchar(50) not null,--箱条码
	private String lsh;// varchar(50) not NULL,--产品唯一码
	private String sku_id;// varchar(50) not NULL,--sku码
	private String style_id;// varchar(50) not NULL,--款号
	private String style_name;// varchar(50) not NULL,--成品名称
	private String color_name;// varchar(50) not NULL,--颜色
	private String size_name;// varchar(50) not NULL,--尺码
	private int box_weight;// numeric(18, 2) not NULL default 0,--箱重
	private String style_inner_id;
	private String style_outer_id;
	
	public String getStyle_inner_id() {
		return style_inner_id;
	}
	public void setStyle_inner_id(String style_inner_id) {
		this.style_inner_id = style_inner_id;
	}
	public String getStyle_outer_id() {
		return style_outer_id;
	}
	public void setStyle_outer_id(String style_outer_id) {
		this.style_outer_id = style_outer_id;
	}
	public String getBox_code() {
		return box_code;
	}
	public void setBox_code(String box_code) {
		this.box_code = box_code;
	}
	public String getLsh() {
		return lsh;
	}
	public void setLsh(String lsh) {
		this.lsh = lsh;
	}
	public String getSku_id() {
		return sku_id;
	}
	public void setSku_id(String sku_id) {
		this.sku_id = sku_id;
	}
	public String getStyle_id() {
		return style_id;
	}
	public void setStyle_id(String style_id) {
		this.style_id = style_id;
	}
	public String getStyle_name() {
		return style_name;
	}
	public void setStyle_name(String style_name) {
		this.style_name = style_name;
	}
	public String getColor_name() {
		return color_name;
	}
	public void setColor_name(String color_name) {
		this.color_name = color_name;
	}
	public String getSize_name() {
		return size_name;
	}
	public void setSize_name(String size_name) {
		this.size_name = size_name;
	}
	public int getBox_weight() {
		return box_weight;
	}
	public void setBox_weight(int box_weight) {
		this.box_weight = box_weight;
	}

	
}
