package com.app.xstore.mendiandiaochu;

public class Item{
	public String describe;//描述
	public String startValue;
	public String endValue;
	public int numberValue;
	public Item(String describe,String startValue){
		this.describe=describe;
		this.startValue=startValue;
	}
	public Item(String describe,String startValue,String endValue){
		this.describe=describe;
		this.startValue=startValue;
		this.endValue=endValue;
	}
	public Item(String describe,String startValue,int numberValue){
		this.describe=describe;
		this.startValue=startValue;
		this.numberValue=numberValue;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return describe;
	}
}
