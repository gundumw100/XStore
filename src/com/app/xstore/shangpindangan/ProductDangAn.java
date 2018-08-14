package com.app.xstore.shangpindangan;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.app.model.Discount;

/**
 * 
 * @author Ni Guijun
 *
 */
public class ProductDangAn extends DataSupport implements Parcelable{
	private long id;
	private String goods_name;//": "GN000",
	
	@Column(ignore=true)
	private String goods_sn;//": "GS000",
	@Column(ignore=true)
	private String goods_desc;//": "GD000",
	@Column(ignore=true)
	private String goods_thumb;//原厂货号
	@Column(ignore=true)
	private float goods_price;//实际支付的价格（折扣后或不折扣）
	@Column(ignore=true)
	private String goods_brand;//": "00",
	@Column(ignore=true)
	private String brand_name;//": "BN000",
	@Column(ignore=true)
	private String goods_color;//": "00",
	@Column(ignore=true)
	private String goods_color_image;//颜色对应的图片
	@Column(ignore=true)
	private String goods_sort;//": "00",
	@Column(ignore=true)
	private String goods_spec;//": "00",
	@Column(ignore=true)
	private String goods_season;//": "00",
	@Column(ignore=true)
	private String goods_sj_date;//": "2018-06-15",
	@Column(ignore=true)
	private float goods_jh_price;//进货价
	@Column(ignore=true)
	private float goods_ls_price;//零售价
	@Column(ignore=true)
	private float goods_db_price;//": 0.0,
	@Column(ignore=true)
	private float goods_discountRate;//折扣率
	@Column(ignore=true)
	private String goods_cs;//": "00",
	@Column(ignore=true)
	private String goods_jldw;//": "00",
	@Column(ignore=true)
	private String goods_zxs;//": "ZXS000",
	@Column(ignore=true)
	private String goods_cw;//": "00",
	@Column(ignore=true)
	private String goods_style;//": "0000"
	@Column(ignore=true)
	private String goods_other;//": 
	@Column(ignore=true)
	private String goods_spec_desc;
	@Column(ignore=true)
	private String goods_color_desc;
	@Column(ignore=true)
	private String goods_other_desc;
	@Column(ignore=true)
	private String goods_cw_desc;
	@Column(ignore=true)
	private String goods_season_desc;
	@Column(ignore=true)
	private String goods_sort_desc;
	@Column(ignore=true)
	private String goods_jldw_desc;
	@Column(ignore=true)
	private String goods_cs_desc;
	@Column(ignore=true)
	private String goods_img;//主图，之间用;分隔
	
	@Column(ignore=true)
	private String sellerUser;//导购销售员
	@Column(ignore=true)
	private Discount discount;//折扣对象
	
	@Override
	public String toString() {
		return goods_name;
	}

	public String getGoods_thumb() {
		return goods_thumb;
	}

	public void setGoods_thumb(String goods_thumb) {
		this.goods_thumb = goods_thumb;
	}

	public String getSellerUser() {
		return sellerUser;
	}

	public void setSellerUser(String sellerUser) {
		this.sellerUser = sellerUser;
	}

	public Discount getDiscount() {
		return discount;
	}

	public void setDiscount(Discount discount) {
		this.discount = discount;
	}

	public String getGoods_color_image() {
		return goods_color_image;
	}

	public void setGoods_color_image(String goods_color_image) {
		this.goods_color_image = goods_color_image;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public String getGoods_sn() {
		return goods_sn;
	}

	public void setGoods_sn(String goods_sn) {
		this.goods_sn = goods_sn;
	}

	public String getGoods_desc() {
		return goods_desc;
	}

	public void setGoods_desc(String goods_desc) {
		this.goods_desc = goods_desc;
	}

	public float getGoods_price() {
		return goods_price;
	}

	public void setGoods_price(float goods_price) {
		this.goods_price = goods_price;
	}

	public String getGoods_brand() {
		return goods_brand;
	}

	public void setGoods_brand(String goods_brand) {
		this.goods_brand = goods_brand;
	}

	public String getBrand_name() {
		return brand_name;
	}

	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}

	public String getGoods_color() {
		return goods_color;
	}

	public void setGoods_color(String goods_color) {
		this.goods_color = goods_color;
	}

	public String getGoods_sort() {
		return goods_sort;
	}

	public void setGoods_sort(String goods_sort) {
		this.goods_sort = goods_sort;
	}

	public String getGoods_spec() {
		return goods_spec;
	}

	public void setGoods_spec(String goods_spec) {
		this.goods_spec = goods_spec;
	}

	public String getGoods_season() {
		return goods_season;
	}

	public void setGoods_season(String goods_season) {
		this.goods_season = goods_season;
	}

	public String getGoods_sj_date() {
		return goods_sj_date;
	}

	public void setGoods_sj_date(String goods_sj_date) {
		this.goods_sj_date = goods_sj_date;
	}

	public float getGoods_jh_price() {
		return goods_jh_price;
	}

	public void setGoods_jh_price(float goods_jh_price) {
		this.goods_jh_price = goods_jh_price;
	}

	public float getGoods_ls_price() {
		return goods_ls_price;
	}

	public void setGoods_ls_price(float goods_ls_price) {
		this.goods_ls_price = goods_ls_price;
	}

	public float getGoods_db_price() {
		return goods_db_price;
	}

	public void setGoods_db_price(float goods_db_price) {
		this.goods_db_price = goods_db_price;
	}

	public float getGoods_discountRate() {
		return goods_discountRate;
	}

	public void setGoods_discountRate(float goods_discountRate) {
		this.goods_discountRate = goods_discountRate;
	}

	public String getGoods_cs() {
		return goods_cs;
	}

	public void setGoods_cs(String goods_cs) {
		this.goods_cs = goods_cs;
	}

	public String getGoods_jldw() {
		return goods_jldw;
	}

	public void setGoods_jldw(String goods_jldw) {
		this.goods_jldw = goods_jldw;
	}

	public String getGoods_zxs() {
		return goods_zxs;
	}

	public void setGoods_zxs(String goods_zxs) {
		this.goods_zxs = goods_zxs;
	}

	public String getGoods_cw() {
		return goods_cw;
	}

	public void setGoods_cw(String goods_cw) {
		this.goods_cw = goods_cw;
	}

	public String getGoods_style() {
		return goods_style;
	}

	public void setGoods_style(String goods_style) {
		this.goods_style = goods_style;
	}

	public String getGoods_other() {
		return goods_other;
	}

	public void setGoods_other(String goods_other) {
		this.goods_other = goods_other;
	}

	public String getGoods_spec_desc() {
		return goods_spec_desc;
	}

	public void setGoods_spec_desc(String goods_spec_desc) {
		this.goods_spec_desc = goods_spec_desc;
	}

	public String getGoods_color_desc() {
		return goods_color_desc;
	}

	public void setGoods_color_desc(String goods_color_desc) {
		this.goods_color_desc = goods_color_desc;
	}

	public String getGoods_other_desc() {
		return goods_other_desc;
	}

	public void setGoods_other_desc(String goods_other_desc) {
		this.goods_other_desc = goods_other_desc;
	}

	public String getGoods_cw_desc() {
		return goods_cw_desc;
	}

	public void setGoods_cw_desc(String goods_cw_desc) {
		this.goods_cw_desc = goods_cw_desc;
	}

	public String getGoods_season_desc() {
		return goods_season_desc;
	}

	public void setGoods_season_desc(String goods_season_desc) {
		this.goods_season_desc = goods_season_desc;
	}

	public String getGoods_sort_desc() {
		return goods_sort_desc;
	}

	public void setGoods_sort_desc(String goods_sort_desc) {
		this.goods_sort_desc = goods_sort_desc;
	}

	public String getGoods_jldw_desc() {
		return goods_jldw_desc;
	}

	public void setGoods_jldw_desc(String goods_jldw_desc) {
		this.goods_jldw_desc = goods_jldw_desc;
	}

	public String getGoods_cs_desc() {
		return goods_cs_desc;
	}

	public void setGoods_cs_desc(String goods_cs_desc) {
		this.goods_cs_desc = goods_cs_desc;
	}

	public String getGoods_img() {
		return goods_img;
	}

	public void setGoods_img(String goods_img) {
		this.goods_img = goods_img;
	}

	public static Parcelable.Creator<ProductDangAn> getCreator()
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
        dest.writeString(goods_name);
        dest.writeString(goods_sn);
        dest.writeString(goods_desc);
    	dest.writeFloat(goods_price);
    	dest.writeFloat(goods_ls_price);
    	dest.writeFloat(goods_discountRate);
    	dest.writeString(goods_color);
    	dest.writeString(goods_color_desc);
    	dest.writeString(goods_spec);
    	dest.writeString(goods_spec_desc);
    	dest.writeString(sellerUser);
    	dest.writeParcelable(discount, flags);
    }

    public static final Parcelable.Creator<ProductDangAn> CREATOR = new Creator<ProductDangAn>()
    {
        public ProductDangAn createFromParcel(Parcel source)
        {
        	ProductDangAn instance = new ProductDangAn();
        	instance.goods_name = source.readString();
        	instance.goods_sn = source.readString();
        	instance.goods_desc = source.readString();
        	instance.goods_price = source.readFloat();
        	instance.goods_ls_price = source.readFloat();
        	instance.goods_discountRate = source.readFloat();
        	instance.goods_color = source.readString();
        	instance.goods_color_desc = source.readString();
        	instance.goods_spec = source.readString();
        	instance.goods_spec_desc = source.readString();
        	instance.sellerUser = source.readString();
        	instance.discount = source.readParcelable(Discount.class.getClassLoader());
            return instance;
        }

        public ProductDangAn[] newArray(int size)
        {
            // TODO Auto-generated method stub
            return new ProductDangAn[size];
        }
    };
    
}
