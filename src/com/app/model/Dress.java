package com.app.model;

public class Dress {

	int id;
	String DressCode;
	String shop_id;
	int total_qty;
	String create_user;
	String create_time;
	String last_modify_user;
	String last_modify_time;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDressCode() {
		return DressCode;
	}

	public void setDressCode(String dressCode) {
		DressCode = dressCode;
	}

	public String getShop_id() {
		return shop_id;
	}

	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}

	public int getTotal_qty() {
		return total_qty;
	}

	public void setTotal_qty(int total_qty) {
		this.total_qty = total_qty;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getLast_modify_user() {
		return last_modify_user;
	}

	public void setLast_modify_user(String last_modify_user) {
		this.last_modify_user = last_modify_user;
	}

	public String getLast_modify_time() {
		return last_modify_time;
	}

	public void setLast_modify_time(String last_modify_time) {
		this.last_modify_time = last_modify_time;
	}

}
