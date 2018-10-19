package com.app.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ShopInfo implements Parcelable{

	String shop_addr;
	String shop_code;
	String company_code;//商家编码
	String editor;
	String phone_number;
	String create_date;
	Integer shop_id;
	String remark;
	String address;
	String edit_date;
	String creater;
	String linkman;
	String shop_name;
	String companyTrade;//行业	Clothing服装；HomeFurnishing家居

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return shop_name+"("+shop_code+")";
	}
	
	public String getCompanyTrade() {
		return companyTrade;
	}

	public void setCompanyTrade(String companyTrade) {
		this.companyTrade = companyTrade;
	}

	public String getShop_addr() {
		return shop_addr;
	}

	public void setShop_addr(String shop_addr) {
		this.shop_addr = shop_addr;
	}

	public String getShop_code() {
		return shop_code;
	}

	public void setShop_code(String shop_code) {
		this.shop_code = shop_code;
	}

	public String getCompany_code() {
		return company_code;
	}

	public void setCompany_code(String company_code) {
		this.company_code = company_code;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public Integer getShop_id() {
		return shop_id;
	}

	public void setShop_id(Integer shop_id) {
		this.shop_id = shop_id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEdit_date() {
		return edit_date;
	}

	public void setEdit_date(String edit_date) {
		this.edit_date = edit_date;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getShop_name() {
		return shop_name;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	public static Parcelable.Creator<ShopInfo> getCreator()
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
    	dest.writeString(company_code);
    	dest.writeString(shop_code);
    	dest.writeString(shop_name);
    	dest.writeString(address);
    	dest.writeString(phone_number);
    }

    public static final Parcelable.Creator<ShopInfo> CREATOR = new Creator<ShopInfo>()
    {
        public ShopInfo createFromParcel(Parcel source)
        {
        	ShopInfo instance = new ShopInfo();
        	instance.company_code  = source.readString();
        	instance.shop_code = source.readString();
        	instance.shop_name = source.readString();
        	instance.address = source.readString();
        	instance.phone_number = source.readString();
            return instance;
        }

        public ShopInfo[] newArray(int size)
        {
            // TODO Auto-generated method stub
            return new ShopInfo[size];
        }
    };
    
}
