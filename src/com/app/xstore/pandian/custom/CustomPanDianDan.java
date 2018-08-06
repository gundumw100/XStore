package com.app.xstore.pandian.custom;

import java.util.ArrayList;
import java.util.List;

import org.litepal.crud.DataSupport;

import com.app.model.Box;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class CustomPanDianDan extends DataSupport implements Parcelable{

	private long id;
//	private String documentNo;//单据号
	private String gh;// 工号
	private String shelf;// 货架
	private int yds;// 预点数
	private int sms;// 扫描数
	private long date;// 日期
	private List<CustomPanDianProduct> panDianProducts = new ArrayList<CustomPanDianProduct>();
	private int status;// 0未上传,1已上传，2上传过但有更新
	
	private String area;//分区
	private String remark;//备注
	private String markType;//子母台...
	private List<Box> boxes = new ArrayList<Box>();
	
	public List<Box> getBoxes() {
		return boxes;
	}
	public void setBoxes(List<Box> boxes) {
		this.boxes = boxes;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getMarkType() {
		return markType;
	}
	public void setMarkType(String markType) {
		this.markType = markType;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getShelf() {
		return shelf;
	}
	public int getYds() {
		return yds;
	}
	public void setShelf(String shelf) {
		this.shelf = shelf;
	}
	public void setYds(int yds) {
		this.yds = yds;
	}
	public String getGh() {
		return gh;
	}
	public void setGh(String gh) {
		this.gh = gh;
	}
	public int getSms() {
		return sms;
	}
	public void setSms(int sms) {
		this.sms = sms;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public List<CustomPanDianProduct> getPanDianProducts() {
		return panDianProducts;
	}
	public void setPanDianProducts(List<CustomPanDianProduct> panDianProducts) {
		this.panDianProducts = panDianProducts;
	}
	
	public static Parcelable.Creator<CustomPanDianDan> getCreator()
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
        dest.writeString(gh);
        dest.writeString(shelf);
    	dest.writeInt(yds);
    	dest.writeInt(sms);
    	dest.writeLong(date);
    	dest.writeInt(status);
    	dest.writeList(panDianProducts);
    	dest.writeList(boxes);
    }

    public static final Parcelable.Creator<CustomPanDianDan> CREATOR = new Creator<CustomPanDianDan>()
    {
        public CustomPanDianDan createFromParcel(Parcel source)
        {
        	CustomPanDianDan instance = new CustomPanDianDan();
        	instance.id = source.readLong();
        	instance.gh = source.readString();
        	instance.shelf = source.readString();
            instance.yds = source.readInt();
            instance.sms = source.readInt();
            instance.date = source.readLong();
            instance.status = source.readInt();
            instance.panDianProducts = source.readArrayList(CustomPanDianProduct.class.getClassLoader());
            instance.boxes = source.readArrayList(Box.class.getClassLoader());
            return instance;
        }

        public CustomPanDianDan[] newArray(int size)
        {
            // TODO Auto-generated method stub
            return new CustomPanDianDan[size];
        }
    };
}
