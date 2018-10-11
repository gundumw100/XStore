package com.app.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Member implements Parcelable{

	String vipNo;//会员ID
	String vipCode;//卡号
	
	String shopCode;
	String birth;
	String sex;
	String createuser;
	String address;
	String createtimeStr;
	String name;
	String mobile;
	int totalValue;
	int totalPoints;
	int vipConsumeValue;//本次消费积分
	
	public int getVipConsumeValue() {
		return vipConsumeValue;
	}
	public void setVipConsumeValue(int vipConsumeValue) {
		this.vipConsumeValue = vipConsumeValue;
	}
	public String getVipNo() {
		return vipNo;
	}
	public void setVipNo(String vipNo) {
		this.vipNo = vipNo;
	}
	public String getShopCode() {
		return shopCode;
	}
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getCreateuser() {
		return createuser;
	}
	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCreatetimeStr() {
		return createtimeStr;
	}
	public void setCreatetimeStr(String createtimeStr) {
		this.createtimeStr = createtimeStr;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVipCode() {
		return vipCode;
	}
	public void setVipCode(String vipCode) {
		this.vipCode = vipCode;
	}
	
	public int getTotalValue() {
		return totalValue;
	}
	public void setTotalValue(int totalValue) {
		this.totalValue = totalValue;
	}
	public int getTotalPoints() {
		return totalPoints;
	}
	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
		
	public static Parcelable.Creator<Member> getCreator()
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
    	dest.writeString(vipNo);
    	dest.writeString(vipCode);
    	dest.writeString(birth);
    	dest.writeString(sex);
    	dest.writeString(address);
    	dest.writeString(createtimeStr);
    	dest.writeString(name);
    	dest.writeInt(totalValue);
    	dest.writeInt(totalPoints);
    	dest.writeString(mobile);
    	dest.writeInt(vipConsumeValue);
    }

    public static final Parcelable.Creator<Member> CREATOR = new Creator<Member>()
    {
        public Member createFromParcel(Parcel source)
        {
        	Member instance = new Member();
        	instance.vipNo = source.readString();
        	instance.vipCode  = source.readString();
        	instance.birth  = source.readString();
        	instance.sex  = source.readString();
        	instance.address  = source.readString();
        	instance.createtimeStr  = source.readString();
        	instance.name  = source.readString();
        	instance.totalValue  = source.readInt();
        	instance.totalPoints  = source.readInt();
        	instance.mobile  = source.readString();
        	instance.vipConsumeValue  = source.readInt();
            return instance;
        }

        public Member[] newArray(int size)
        {
            // TODO Auto-generated method stub
            return new Member[size];
        }
    };
	
}
