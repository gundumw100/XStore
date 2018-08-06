package com.app.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

/**
 * 折扣
 * @author Ni Guijun
 *
 */
public class Discount implements Parcelable{

	int mode;//0:折扣率;1:折扣价
	double discountValue;//折扣率
	boolean disOnBillPrice;//使用当前价
	boolean wholeOrder;//整单打折
	
	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public double getDiscountValue() {
		return discountValue;
	}

	public void setDiscountValue(double discountValue) {
		this.discountValue = discountValue;
	}

	public boolean isDisOnBillPrice() {
		return disOnBillPrice;
	}

	public void setDisOnBillPrice(boolean disOnBillPrice) {
		this.disOnBillPrice = disOnBillPrice;
	}


	public boolean isWholeOrder() {
		return wholeOrder;
	}

	public void setWholeOrder(boolean wholeOrder) {
		this.wholeOrder = wholeOrder;
	}

	public static Parcelable.Creator<Discount> getCreator()
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
        dest.writeInt(mode);
    	dest.writeDouble(discountValue);
    	dest.writeInt(disOnBillPrice?0:1);
    	dest.writeInt(wholeOrder?0:1);
    }

    public static final Parcelable.Creator<Discount> CREATOR = new Creator<Discount>()
    {
        public Discount createFromParcel(Parcel source)
        {
        	Discount instance = new Discount();
        	instance.mode = source.readInt();
        	instance.discountValue = source.readDouble();
        	instance.disOnBillPrice = source.readInt()==0;
        	instance.wholeOrder = source.readInt()==0;
            return instance;
        }

        public Discount[] newArray(int size)
        {
            // TODO Auto-generated method stub
            return new Discount[size];
        }
    };
	
}
