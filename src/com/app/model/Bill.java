package com.app.model;

import java.util.List;

import org.litepal.crud.DataSupport;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 单据 商品盘点数据
 */
public class Bill extends DataSupport implements Parcelable {

	private int id;
	/** 盘点单据编码，由服务端产生。值为空，则做新增。值非空，则做更新 */
	private String docNum;
	/** 货位下的总商品数 */
	private int totalQty;// 扫描数
	/** 商品明细数据 */
	private List<BillProduct> billProducts;
	private String userId;
	private String createTime;
	private int status;// 0:未上传；1：已上传
	private String shelfCode;
	private String locID;//货位
	private String floor;//货位
	private String remark;//描述

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<BillProduct> getBillProducts() {
		return billProducts;
	}

	public void setBillProducts(List<BillProduct> billProducts) {
		this.billProducts = billProducts;
	}

	public String getDocNum() {
		return docNum;
	}

	public void setDocNum(String docNum) {
		this.docNum = docNum;
	}

	public int getTotalQty() {
		return totalQty;
	}

	public void setTotalQty(int totalQty) {
		this.totalQty = totalQty;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getShelfCode() {
		return shelfCode;
	}

	public void setShelfCode(String shelfCode) {
		this.shelfCode = shelfCode;
	}

	public String getLocID() {
		return locID;
	}

	public void setLocID(String locID) {
		this.locID = locID;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public static Parcelable.Creator<Bill> getCreator() {
		return CREATOR;
	}

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(id);
		dest.writeString(docNum);
		dest.writeInt(totalQty);
		dest.writeString(userId);
		dest.writeString(createTime);
		dest.writeInt(status);
		dest.writeList(billProducts);
		dest.writeString(shelfCode);
		dest.writeString(remark);
	}

	public static final Parcelable.Creator<Bill> CREATOR = new Creator<Bill>() {
		public Bill createFromParcel(Parcel source) {
			Bill instance = new Bill();
			instance.id = source.readInt();
			instance.docNum = source.readString();
			instance.totalQty = source.readInt();
			instance.userId = source.readString();
			instance.createTime = source.readString();
			instance.status = source.readInt();
			instance.billProducts = source.readArrayList(BillProduct.class.getClassLoader());
			instance.shelfCode = source.readString();
			instance.remark = source.readString();
			return instance;
		}

		public Bill[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Bill[size];
		}
	};

}
