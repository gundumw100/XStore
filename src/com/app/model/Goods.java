package com.app.model;

import java.text.DecimalFormat;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;

public class Goods implements Parcelable{

	 private int goods_id;
	 private String shop_code;
	 private String goods_sn;
	 private String goods_name;
	 private double goods_price;//商品原价
	 private String brand_name;
	 private String brand_logo;
	 private String goods_desc;
	 private String goods_thumb;
	 private String goods_img;
	 private String original_img;
	 private int status;
	 private String creater;
	 private String create_date;
	 private String editor;
	 private String edit_date;
	 private String remark;
	 private Discount discount;
	 
	 private String sellerUser;//导购
//	private float divAmount;//折扣金额
//	private float divSaleRate;//折扣率
//	private float retailPrice;//零售价
//	private float salePrice;//折扣价

	public String getSellerUser() {
		return sellerUser;
	}

	public void setSellerUser(String sellerUser) {
		this.sellerUser = sellerUser;
	}

//	//折扣后的金额
//	 public double getGoods_price_discount(){
//		 if(discount!=null){
//			 if(discount.getMode()==0){//折扣率
//				 if(discount.getDiscountValue()>=100){//表示不打折
//					 return goods_price;
//				 }else{
//					 String text=formatMoney(goods_price*discount.getDiscountValue()/100);
//					 return Double.parseDouble(text);
//				 }
//				 
//			 }else if(discount.getMode()==1){//折扣额
//				 if(discount.getDiscountValue()>=goods_price){//表示不打折
//					 return goods_price;
//				 }else{
//					 String text=formatMoney(discount.getDiscountValue());
//					 return Double.parseDouble(text);
//				 }
//			 }
//		 }
//		 return goods_price;
//	 }
	 
//	 //折扣了的金额
//	 public double getGoods_price_discount_off(){
//		 return goods_price-getGoods_price_discount();
//	 }
	 
	/**
	 * 格式化Money，结构为0.00，传入的是Number
	 * 
	 * @param money
	 * @return
	 */
	private String formatMoney(Number money) {
		return formatNumber(money, "###0.00");
	}

	/**
	 * 格式化数字，格式通过formatString自定义
	 * 
	 * @param money
	 * @return
	 */
	private String formatNumber(Number number, String formatString) {
		if (number == null) {
			return null;
		}
		if (TextUtils.isEmpty(formatString)) {
			return String.valueOf(number);
		}
		DecimalFormat format = new DecimalFormat(formatString);
		return format.format(number);
	}
		
	public Discount getDiscount() {
		return discount;
	}
	public void setDiscount(Discount discount) {
		this.discount = discount;
	}
	public double getGoods_price() {
		return goods_price;
	}
	public void setGoods_price(double goods_price) {
		this.goods_price = goods_price;
	}
	public int getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(int goods_id) {
		this.goods_id = goods_id;
	}
	public String getShop_code() {
		return shop_code;
	}
	public void setShop_code(String shop_code) {
		this.shop_code = shop_code;
	}
	public String getGoods_sn() {
		return goods_sn;
	}
	public void setGoods_sn(String goods_sn) {
		this.goods_sn = goods_sn;
	}
	public String getGoods_name() {
		return goods_name;
	}
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
	public String getBrand_name() {
		return brand_name;
	}
	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}
	public String getBrand_logo() {
		return brand_logo;
	}
	public void setBrand_logo(String brand_logo) {
		this.brand_logo = brand_logo;
	}
	public String getGoods_desc() {
		return goods_desc;
	}
	public void setGoods_desc(String goods_desc) {
		this.goods_desc = goods_desc;
	}
	public String getGoods_thumb() {
		return goods_thumb;
	}
	public void setGoods_thumb(String goods_thumb) {
		this.goods_thumb = goods_thumb;
	}
	public String getGoods_img() {
		return goods_img;
	}
	public void setGoods_img(String goods_img) {
		this.goods_img = goods_img;
	}
	public String getOriginal_img() {
		return original_img;
	}
	public void setOriginal_img(String original_img) {
		this.original_img = original_img;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public String getEdit_date() {
		return edit_date;
	}
	public void setEdit_date(String edit_date) {
		this.edit_date = edit_date;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
     
	public static Parcelable.Creator<Goods> getCreator()
    {
        return CREATOR;
    }

    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags)
    {
        // TODO Auto-generated method stub
    	dest.writeString(shop_code);
    	dest.writeString(goods_sn);
    	dest.writeString(goods_name);
    	dest.writeDouble(goods_price);
    	dest.writeString(goods_desc);
    	dest.writeString(goods_thumb);
    	dest.writeString(create_date);
    	dest.writeString(edit_date);
    	dest.writeParcelable(discount, flags);
    	dest.writeString(sellerUser);
    }

    public static final Parcelable.Creator<Goods> CREATOR = new Creator<Goods>()
    {
        public Goods createFromParcel(Parcel source)
        {
        	Goods instance = new Goods();
        	instance.shop_code = source.readString();
        	instance.goods_sn = source.readString();
        	instance.goods_name = source.readString();
        	instance.goods_price = source.readDouble();
        	instance.goods_desc = source.readString();
        	instance.goods_thumb = source.readString();
        	instance.create_date = source.readString();
        	instance.edit_date = source.readString();
        	instance.discount=source.readParcelable(Discount.class.getClassLoader());
        	instance.sellerUser = source.readString();
            return instance;
        }

        public Goods[] newArray(int size)
        {
            // TODO Auto-generated method stub
            return new Goods[size];
        }
    };
}
