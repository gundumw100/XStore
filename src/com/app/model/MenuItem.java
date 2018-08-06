package com.app.model;

public class MenuItem {

	public int id;
	public int resID;
	public String text;
	public Class clazz;

	public MenuItem(int id, int resID, String text,Class clazz) {
		this.id = id;
		this.resID = resID;
		this.text = text;
		this.clazz=clazz;
	}
}
