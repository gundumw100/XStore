package com.app.xstore.pandian;

import org.litepal.crud.DataSupport;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 商品
 * @author Ni Guijun
 *
 */
public class Product extends DataSupport implements Parcelable {

	private String accountingCode;// 核算码 varchar(10)
	private String patternCode;// 款号 varchar(10)
	private String barCode;// 条码 varchar(13)
	private String color;// 颜色 varchar(20)
	private String size;// 码数 varchar(20)
	private String name;// 名称 varchar(60)
	private String price;// 标签价 numeric(9,2)
	private String cup;// 罩杯 varchar(20)
	private String quantity;// 包装含量 int

	@Override
	public String toString(){
		return accountingCode+";"+patternCode+";"+barCode+";"+color+";"+size+";"+name+";"+price+";"+cup+";"+quantity;
	}
	
	public String getAccountingCode() {
		return accountingCode;
	}

	public void setAccountingCode(String accountingCode) {
		this.accountingCode = accountingCode;
	}

	public String getPatternCode() {
		return patternCode;
	}

	public void setPatternCode(String patternCode) {
		this.patternCode = patternCode;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCup() {
		return cup;
	}

	public void setCup(String cup) {
		this.cup = cup;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public static Parcelable.Creator<Product> getCreator() {
		return CREATOR;
	}

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(accountingCode);
		dest.writeString(patternCode);
		dest.writeString(barCode);
		dest.writeString(color);
		dest.writeString(size);
		dest.writeString(name);
		dest.writeString(price);
		dest.writeString(cup);
		dest.writeString(quantity);
	}

	public static final Parcelable.Creator<Product> CREATOR = new Creator<Product>() {
		public Product createFromParcel(Parcel source) {
			Product instance = new Product();
			instance.accountingCode = source.readString();
			instance.patternCode = source.readString();
			instance.barCode = source.readString();
			instance.color = source.readString();
			instance.size = source.readString();
			instance.name = source.readString();
			instance.price = source.readString();
			instance.cup = source.readString();
			instance.quantity = source.readString();
			return instance;
		}

		public Product[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Product[size];
		}
	};

}
