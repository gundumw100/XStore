package com.app.xstore.mendiandiaochu;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ChuRuKuHead implements Parcelable{

//	long id;//":0,
    String doc_type;//":"OTI",
	String doc_code;//":null,
	String doc_status;//"I",
    int qty;//":2,
    String shopCode;//":"S001",
    String src_type;//":"srct",
    String src_shop;//":null,//门店code
    String src_code;//":"src001",单号
    String tar_shop;//":"S002",
    String tar_type;//":"tart",
    String tar_code;//":null,
    String shopName;//发货门店名称
    String tarShopName;//收货门店名称
    String doc_date;//":"0001-01-01T00:00:00",
    String expType_code;//":"EXPT001",
    String exp_num;//":"expn001",
    String remark;//":"rem",
    String create_date;//":"0001-01-01T00:00:00",
    String create_user;//":"U001",
    String last_modify_date;//":"0001-01-01T00:00:00",
    String last_modify_user;//":"U002"
    
    //入库单参数
    int jhQty;//":0,进货数量
    float jhcb;//":0,进货成本
    float xsgs;//":0,销售估算
    float mlgs;//":0,毛利估算
    
    
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getTarShopName() {
		return tarShopName;
	}
	public void setTarShopName(String tarShopName) {
		this.tarShopName = tarShopName;
	}
	public String getDoc_type() {
		return doc_type;
	}
	public void setDoc_type(String doc_type) {
		this.doc_type = doc_type;
	}
	public String getDoc_code() {
		return doc_code;
	}
	public void setDoc_code(String doc_code) {
		this.doc_code = doc_code;
	}
	public String getDoc_status() {
		return doc_status;
	}
	public void setDoc_status(String doc_status) {
		this.doc_status = doc_status;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	
	public String getShopCode() {
		return shopCode;
	}
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}
	public String getSrc_type() {
		return src_type;
	}
	public void setSrc_type(String src_type) {
		this.src_type = src_type;
	}
	public String getSrc_shop() {
		return src_shop;
	}
	public void setSrc_shop(String src_shop) {
		this.src_shop = src_shop;
	}
	public String getSrc_code() {
		return src_code;
	}
	public void setSrc_code(String src_code) {
		this.src_code = src_code;
	}
	public String getTar_shop() {
		return tar_shop;
	}
	public void setTar_shop(String tar_shop) {
		this.tar_shop = tar_shop;
	}
	public String getTar_type() {
		return tar_type;
	}
	public void setTar_type(String tar_type) {
		this.tar_type = tar_type;
	}
	public String getTar_code() {
		return tar_code;
	}
	public void setTar_code(String tar_code) {
		this.tar_code = tar_code;
	}
	public String getDoc_date() {
		return doc_date;
	}
	public void setDoc_date(String doc_date) {
		this.doc_date = doc_date;
	}
	public String getExpType_code() {
		return expType_code;
	}
	public void setExpType_code(String expType_code) {
		this.expType_code = expType_code;
	}
	public String getExp_num() {
		return exp_num;
	}
	public void setExp_num(String exp_num) {
		this.exp_num = exp_num;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getCreate_user() {
		return create_user;
	}
	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}
	public String getLast_modify_date() {
		return last_modify_date;
	}
	public void setLast_modify_date(String last_modify_date) {
		this.last_modify_date = last_modify_date;
	}
	public String getLast_modify_user() {
		return last_modify_user;
	}
	public void setLast_modify_user(String last_modify_user) {
		this.last_modify_user = last_modify_user;
	}
    
	public int getJhQty() {
		return jhQty;
	}
	public void setJhQty(int jhQty) {
		this.jhQty = jhQty;
	}
	public float getJhcb() {
		return jhcb;
	}
	public void setJhcb(float jhcb) {
		this.jhcb = jhcb;
	}
	public float getXsgs() {
		return xsgs;
	}
	public void setXsgs(float xsgs) {
		this.xsgs = xsgs;
	}
	public float getMlgs() {
		return mlgs;
	}
	public void setMlgs(float mlgs) {
		this.mlgs = mlgs;
	}
	public static Parcelable.Creator<ChuRuKuHead> getCreator() {
		return CREATOR;
	}

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
	    dest.writeString(doc_type);
		dest.writeString(doc_code);
		dest.writeString(doc_status);
		dest.writeInt(qty);
		dest.writeString(src_type);
		dest.writeString(src_shop);
		dest.writeString(src_code);
		dest.writeString(tar_shop);
		dest.writeString(tar_type);
		dest.writeString(tar_code);
		dest.writeString(doc_date);
		dest.writeString(expType_code);
		dest.writeString(exp_num);
		dest.writeString(remark);
		dest.writeString(shopCode);
		dest.writeString(shopName);
		dest.writeString(tarShopName);
	}

	public static final Parcelable.Creator<ChuRuKuHead> CREATOR = new Creator<ChuRuKuHead>() {
		public ChuRuKuHead createFromParcel(Parcel source) {
			ChuRuKuHead instance = new ChuRuKuHead();
			instance.doc_type = source.readString();
			instance.doc_code = source.readString();
			instance.doc_status = source.readString();
			instance.qty = source.readInt();
			instance.src_type = source.readString();
			instance.src_shop = source.readString();
			instance.src_code = source.readString();
			instance.tar_shop = source.readString();
			instance.tar_type = source.readString();
			instance.tar_code = source.readString();
			instance.doc_date = source.readString();
			instance.expType_code = source.readString();
			instance.exp_num = source.readString();
			instance.remark = source.readString();
			instance.shopCode = source.readString();
			instance.shopName = source.readString();
			instance.tarShopName = source.readString();
			return instance;
		}

		public ChuRuKuHead[] newArray(int size) {
			// TODO Auto-generated method stub
			return new ChuRuKuHead[size];
		}
	};
}
