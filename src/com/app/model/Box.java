package com.app.model;

import org.litepal.crud.DataSupport;

import com.app.xstore.pandian.custom.CustomPanDianDan;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Box extends DataSupport implements Parcelable {

	private long id;
	private String code;// 箱码
	private int qty;// 商品数量
	
	private CustomPanDianDan panDianDan;//盘点单
	private long danID;//盘点单ID

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return id+";"+danID+";"+code+";"+qty;
	}
	public CustomPanDianDan getPanDianDan() {
		return panDianDan;
	}

	public void setPanDianDan(CustomPanDianDan panDianDan) {
		this.panDianDan = panDianDan;
	}

	public long getDanID() {
		return danID;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setDanID(long danID) {
		this.danID = danID;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public static Parcelable.Creator<Box> getCreator() {
		return CREATOR;
	}

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(code);
		dest.writeInt(qty);
		dest.writeLong(danID);
	}

	public static final Parcelable.Creator<Box> CREATOR = new Creator<Box>() {
		public Box createFromParcel(Parcel source) {
			Box instance = new Box();
			instance.code = source.readString();
			instance.qty = source.readInt();
			instance.danID = source.readLong();
			return instance;
		}

		public Box[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Box[size];
		}
	};
}
