package com.app.model;

import org.litepal.crud.DataSupport;

/**
 * 货架
 * 
 * @author Administrator
 * 
 */
public class ProdPreChcekData extends DataSupport {

	int id;// 服务端ID
	// String OrgCode;
	// String ShelfCode;
	// String TotalQty;
	// String Creator;
	// String CreateTime;
	int ProdPreChcekData_id;// 备份
	String shop_id;
	String shelf_code;
	String total_qty;
	String create_user;
	String create_time_string;

	public int getProdPreChcekData_id() {
		return ProdPreChcekData_id;
	}

	public void setProdPreChcekData_id(int prodPreChcekData_id) {
		ProdPreChcekData_id = prodPreChcekData_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getShop_id() {
		return shop_id;
	}

	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}

	public String getShelf_code() {
		return shelf_code;
	}

	public void setShelf_code(String shelf_code) {
		this.shelf_code = shelf_code;
	}

	public String getTotal_qty() {
		return total_qty;
	}

	public void setTotal_qty(String total_qty) {
		this.total_qty = total_qty;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public String getCreate_time_string() {
		return create_time_string;
	}

	public void setCreate_time_string(String create_time_string) {
		this.create_time_string = create_time_string;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return shelf_code;
	}
}
