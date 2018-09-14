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

	int discountType;//0:折扣率;1:折扣价
	float discountRate;//折扣率
	float discountPrice;//折扣后的总金额
	boolean wholeOrder;//是否整单打折
	
	@Override
	public String toString() {
		return discountType+":"+discountRate+":"+discountPrice+":"+wholeOrder;
	};
	
	public float getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(float discountRate) {
		this.discountRate = discountRate;
	}

	public float getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(float discountPrice) {
		this.discountPrice = discountPrice;
	}

	public int getDiscountType() {
		return discountType;
	}

	public void setDiscountType(int discountType) {
		this.discountType = discountType;
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
        dest.writeInt(discountType);
        dest.writeFloat(discountRate);
    	dest.writeFloat(discountPrice);
    	dest.writeInt(wholeOrder?0:1);
    }

    public static final Parcelable.Creator<Discount> CREATOR = new Creator<Discount>()
    {
        public Discount createFromParcel(Parcel source)
        {
        	Discount instance = new Discount();
        	instance.discountType = source.readInt();
        	instance.discountRate = source.readFloat();
        	instance.discountPrice = source.readFloat();
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
