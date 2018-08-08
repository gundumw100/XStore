package com.app.xstore.mendiandiaochu;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ChuRuKuProduct implements Parcelable{

	private String goods_name;
	private String goods_sn;
	private int qty;
	
	private float goods_jh_price;//": 0.0,
	private float goods_ls_price;//": 0.0,

	private String goods_color;//": "00",
	private String goods_spec;//": "00",
	private int stock;//库存
	private int online_stock;//在途库存
	
	private String goods_spec_desc;
	private String goods_color_desc;
	private String goods_season_desc;
	private String goods_sort_desc;
	private String goods_img; 
	private String goods_color_image; 
	
	public String getGoods_color_image() {
		return goods_color_image;
	}
	public void setGoods_color_image(String goods_color_image) {
		this.goods_color_image = goods_color_image;
	}
	public String getGoods_season_desc() {
		return goods_season_desc;
	}
	public void setGoods_season_desc(String goods_season_desc) {
		this.goods_season_desc = goods_season_desc;
	}
	public String getGoods_sort_desc() {
		return goods_sort_desc;
	}
	public void setGoods_sort_desc(String goods_sort_desc) {
		this.goods_sort_desc = goods_sort_desc;
	}
	public String getGoods_spec_desc() {
		return goods_spec_desc;
	}
	public void setGoods_spec_desc(String goods_spec_desc) {
		this.goods_spec_desc = goods_spec_desc;
	}
	public String getGoods_color_desc() {
		return goods_color_desc;
	}
	public void setGoods_color_desc(String goods_color_desc) {
		this.goods_color_desc = goods_color_desc;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public int getOnline_stock() {
		return online_stock;
	}
	public void setOnline_stock(int online_stock) {
		this.online_stock = online_stock;
	}
	public float getGoods_jh_price() {
		return goods_jh_price;
	}
	public void setGoods_jh_price(float goods_jh_price) {
		this.goods_jh_price = goods_jh_price;
	}
	public float getGoods_ls_price() {
		return goods_ls_price;
	}
	public void setGoods_ls_price(float goods_ls_price) {
		this.goods_ls_price = goods_ls_price;
	}
	public String getGoods_color() {
		return goods_color;
	}
	public void setGoods_color(String goods_color) {
		this.goods_color = goods_color;
	}
	public String getGoods_spec() {
		return goods_spec;
	}
	public void setGoods_spec(String goods_spec) {
		this.goods_spec = goods_spec;
	}
	public String getGoods_name() {
		return goods_name;
	}
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
	public String getGoods_sn() {
		return goods_sn;
	}
	public void setGoods_sn(String goods_sn) {
		this.goods_sn = goods_sn;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	
	public String getGoods_img() {
		return goods_img;
	}
	public void setGoods_img(String goods_img) {
		this.goods_img = goods_img;
	}
	public static Parcelable.Creator<ChuRuKuProduct> getCreator() {
		return CREATOR;
	}

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(goods_name);
		dest.writeString(goods_sn);
		dest.writeInt(qty);
		dest.writeFloat(goods_jh_price);
		dest.writeFloat(goods_ls_price);
		dest.writeString(goods_color);
		dest.writeString(goods_spec);
		dest.writeString(goods_img);
	}

	public static final Parcelable.Creator<ChuRuKuProduct> CREATOR = new Creator<ChuRuKuProduct>() {
		public ChuRuKuProduct createFromParcel(Parcel source) {
			ChuRuKuProduct instance = new ChuRuKuProduct();
			instance.goods_name = source.readString();
			instance.goods_sn = source.readString();
			instance.qty = source.readInt();
			instance.goods_jh_price = source.readFloat();
			instance.goods_ls_price = source.readFloat();
			instance.goods_color = source.readString();
			instance.goods_spec = source.readString();
			instance.goods_img = source.readString();
			return instance;
		}

		public ChuRuKuProduct[] newArray(int size) {
			// TODO Auto-generated method stub
			return new ChuRuKuProduct[size];
		}
	};
	
}
