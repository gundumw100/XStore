package com.app.model;

import org.litepal.crud.DataSupport;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 商品（简化）
 * @author Administrator
 *
 */
public class BillProduct extends DataSupport implements Parcelable{
	private int id;
	/** 商品编码 */
	private String prodNum;
	private String prodName;
	/** 国际编码 */
	private String barcode;
	/** 数量 */
	private int qty;
	
	String documentNo;//单据号
	String shelfCode;//货架
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProdNum() {
		return prodNum;
	}
	public void setProdNum(String prodNum) {
		this.prodNum = prodNum;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public String getDocumentNo() {
		return documentNo;
	}
	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}
	public String getShelfCode() {
		return shelfCode;
	}
	public void setShelfCode(String shelfCode) {
		this.shelfCode = shelfCode;
	}
	
	public String getProdName() {
		return prodName;
	}
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	public static Parcelable.Creator<BillProduct> getCreator()
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
        dest.writeString(prodNum);
        dest.writeString(prodName);
    	dest.writeString(barcode);
    	dest.writeInt(qty);
    }

    public static final Parcelable.Creator<BillProduct> CREATOR = new Creator<BillProduct>()
    {
        public BillProduct createFromParcel(Parcel source)
        {
        	BillProduct instance = new BillProduct();
        	instance.id = source.readInt();
        	instance.prodNum = source.readString();
        	instance.prodName = source.readString();
            instance.barcode = source.readString();
            instance.qty = source.readInt();
            return instance;
        }

        public BillProduct[] newArray(int size)
        {
            // TODO Auto-generated method stub
            return new BillProduct[size];
        }
    };
    
}
