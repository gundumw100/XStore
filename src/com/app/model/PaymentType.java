package com.app.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PaymentType implements Parcelable{

	private int type;
	private String name;
	public PaymentType(){
		
	}
	public PaymentType(int type, String name) {
		super();
		this.type = type;
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public static Parcelable.Creator<PaymentType> getCreator()
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

        dest.writeInt(type);
        dest.writeString(name);
    }

    public static final Parcelable.Creator<PaymentType> CREATOR = new Creator<PaymentType>()
    {
        public PaymentType createFromParcel(Parcel source)
        {
        	PaymentType instance = new PaymentType();
            instance.type = source.readInt();
            instance.name = source.readString();

            return instance;
        }

        public PaymentType[] newArray(int size)
        {
            // TODO Auto-generated method stub
            return new PaymentType[size];
        }
    };

}
