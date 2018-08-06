package com.app.model;

import org.litepal.crud.DataSupport;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

/**
 * 商品（简化）
 * @author Administrator
 *
 */
public class ProdCheckDtl extends DataSupport implements Parcelable{
	private int id;
	/** 商品编码 */
	private String prod_num;
	private String prod_name;
	/** 国际编码 */
//	private String barcode;
	/** 数量 */
	private int qty;
	
	private String DocNum;//单据编号，级联查询用
	private int DocID;//单据编号，级联查询用
	
	private String pddbh;//not null, --主建、盘点单号
	private String sm_code;//not null,--主键、箱条码（位）或产品唯一码（位）
	private int sl;//not null default 0,--箱内数量或唯一码=1
	public String getPddbh() {
		return pddbh;
	}
	public void setPddbh(String pddbh) {
		this.pddbh = pddbh;
	}
	public String getSm_code() {
		return sm_code;
	}
	public void setSm_code(String sm_code) {
		this.sm_code = sm_code;
	}
	public int getSl() {
		return sl;
	}
	public void setSl(int sl) {
		this.sl = sl;
	}
	public String getProd_num() {
		return prod_num;
	}
	public void setProd_num(String prod_num) {
		this.prod_num = prod_num;
	}
	public String getProd_name() {
		return prod_name;
	}
	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}
//	public String getBarcode() {
//		return barcode;
//	}
//	public void setBarcode(String barcode) {
//		this.barcode = barcode;
//	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public String getDocNum() {
		return DocNum;
	}
	public int getDocID() {
		return DocID;
	}
	public void setDocID(int docID) {
		DocID = docID;
	}
	public void setDocNum(String docNum) {
		DocNum = docNum;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public static Parcelable.Creator<ProdCheckDtl> getCreator()
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
    	dest.writeInt(id);
        dest.writeString(prod_num);
        dest.writeString(prod_name);
//    	dest.writeString(barcode);
    	dest.writeInt(qty);
    	dest.writeString(DocNum);
    }

    public static final Parcelable.Creator<ProdCheckDtl> CREATOR = new Creator<ProdCheckDtl>()
    {
        public ProdCheckDtl createFromParcel(Parcel source)
        {
        	ProdCheckDtl instance = new ProdCheckDtl();
        	instance.id = source.readInt();
        	instance.prod_num = source.readString();
        	instance.prod_name = source.readString();
//            instance.barcode = source.readString();
            instance.qty = source.readInt();
            instance.DocNum = source.readString();
            return instance;
        }

        public ProdCheckDtl[] newArray(int size)
        {
            // TODO Auto-generated method stub
            return new ProdCheckDtl[size];
        }
    };
    
}
