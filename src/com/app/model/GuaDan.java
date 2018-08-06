package com.app.model;

import java.util.ArrayList;

public class GuaDan {

	String createTime;
	int qty;
	String money;
	
	ArrayList<Goods> infos;
	ArrayList<Boolean> exists;
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public ArrayList<Goods> getInfos() {
		return infos;
	}
	public void setInfos(ArrayList<Goods> infos) {
		this.infos = infos;
	}
	public ArrayList<Boolean> getExists() {
		return exists;
	}
	public void setExists(ArrayList<Boolean> exists) {
		this.exists = exists;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	
	
}
