package com.app.xstore.shangpindangan;

import org.litepal.crud.DataSupport;

/**
 * 
 * @author Ni Guijun
 *
 */
public class ProductDangAnRecentlyBrowse extends DataSupport{
	private long id;
	private String goods_name;//": "GN000",
	private String goods_thumb;//": "GN000",
	
	private String goods_sn;//": "GS000",
	private String goods_desc;//": "GD000",
	private float goods_price;//": 0.0,
	private String goods_brand;//": "00",
	private String brand_name;//": "BN000",
	private String goods_color;//": "00",
	private String goods_sort;//": "00",
	private String goods_spec;//": "00",
	private String goods_season;//": "00",
	private String goods_sj_date;//": "2018-06-15",
	private float goods_jh_price;//": 0.0,
	private float goods_ls_price;//": 0.0,
	private float goods_db_price;//": 0.0,
	private float goods_discountRate;//": 0.00,
	private String goods_cs;//": "00",
	private String goods_jldw;//": "00",
	private String goods_zxs;//": "ZXS000",
	private String goods_cw;//": "00",
	private String goods_style;//": "0000"
	private String goods_other;//": 
	private String goods_img;
	private long timeMillis;
	
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

	public String getGoods_img() {
		return goods_img;
	}

	public void setGoods_img(String goods_img) {
		this.goods_img = goods_img;
	}

	public long getTimeMillis() {
		return timeMillis;
	}

	public void setTimeMillis(long timeMillis) {
		this.timeMillis = timeMillis;
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

	
}
