package com.app.model;

public class LocAreaInfo {

	/**
	 * 门店编码			
		货位编码			
		区域			
		创建者			
		创建时间			
	 */
	int id;
	String shop_code;
	String loc_id;
	String area;
	String creator;
	String create_time;
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getShop_code() {
		return shop_code;
	}


	public void setShop_code(String shop_code) {
		this.shop_code = shop_code;
	}


	public String getLoc_id() {
		return loc_id;
	}


	public void setLoc_id(String loc_id) {
		this.loc_id = loc_id;
	}


	public String getArea() {
		return area;
	}


	public void setArea(String area) {
		this.area = area;
	}


	public String getCreator() {
		return creator;
	}


	public void setCreator(String creator) {
		this.creator = creator;
	}


	public String getCreate_time() {
		return create_time;
	}


	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return area;
	}
}
