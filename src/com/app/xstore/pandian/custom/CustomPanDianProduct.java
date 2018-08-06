package com.app.xstore.pandian.custom;

import org.litepal.crud.DataSupport;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class CustomPanDianProduct extends DataSupport implements Parcelable{
	private long id;
	private String barCode;// 条码 varchar(13)
	private String name;// 名称 varchar(60)
	private int qty;//扫描件数
	private CustomPanDianDan panDianDan;//盘点单
	private long danID;//盘点单ID
	
	private String lsh;
	private String style_id;
	private String color_name;
	private String size_name;
	private String sku_id;
	
	
	@Override
	public String toString(){
		return barCode+";"+name+";"+qty+";";
	}

	public String getSku_id() {
		return sku_id;
	}

	public void setSku_id(String sku_id) {
		this.sku_id = sku_id;
	}

	public String getLsh() {
		return lsh;
	}

	public void setLsh(String lsh) {
		this.lsh = lsh;
	}

	public String getStyle_id() {
		return style_id;
	}

	public void setStyle_id(String style_id) {
		this.style_id = style_id;
	}

	public String getColor_name() {
		return color_name;
	}

	public void setColor_name(String color_name) {
		this.color_name = color_name;
	}

	public String getSize_name() {
		return size_name;
	}

	public void setSize_name(String size_name) {
		this.size_name = size_name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
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

	public void setDanID(long danID) {
		this.danID = danID;
	}

	public static Parcelable.Creator<CustomPanDianProduct> getCreator()
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
    	dest.writeLong(id);
        dest.writeString(barCode);
        dest.writeString(name);
    	dest.writeInt(qty);
//    	dest.writeParcelable(panDianDan, flags);
    	dest.writeLong(danID);
    	dest.writeString(lsh);
    	dest.writeString(style_id);
    	dest.writeString(color_name);
    	dest.writeString(size_name);
    	dest.writeString(sku_id);
    }

    public static final Parcelable.Creator<CustomPanDianProduct> CREATOR = new Creator<CustomPanDianProduct>()
    {
        public CustomPanDianProduct createFromParcel(Parcel source)
        {
        	CustomPanDianProduct instance = new CustomPanDianProduct();
        	instance.id = source.readLong();
        	instance.barCode = source.readString();
        	instance.name = source.readString();
            instance.qty = source.readInt();
            instance.danID = source.readLong();
            instance.lsh = source.readString();
            instance.style_id = source.readString();
            instance.color_name = source.readString();
            instance.size_name = source.readString();
            instance.sku_id = source.readString();
            return instance;
        }

        public CustomPanDianProduct[] newArray(int size)
        {
            // TODO Auto-generated method stub
            return new CustomPanDianProduct[size];
        }
    };
    
}
