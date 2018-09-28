package com.app.model;

public class Printer {

	int id;
	int printerno;
	String printerip;
	String description;
	String status;
	String shopId;
	String creator;
	String createdate;
	String updater;
	String updatedate;
	int type;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return printerip+";"+description+";"+type;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPrinterno() {
		return printerno;
	}
	public void setPrinterno(int printerno) {
		this.printerno = printerno;
	}
	public String getPrinterip() {
		return printerip;
	}
	public void setPrinterip(String printerip) {
		this.printerip = printerip;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getCreatedate() {
		return createdate;
	}
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}
	public String getUpdater() {
		return updater;
	}
	public void setUpdater(String updater) {
		this.updater = updater;
	}
	public String getUpdatedate() {
		return updatedate;
	}
	public void setUpdatedate(String updatedate) {
		this.updatedate = updatedate;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
}
