package com.app.xstore.pandian;

import java.util.ArrayList;
import java.util.List;

import org.litepal.crud.DataSupport;

/**
 * 盘点单
 * @author Ni Guijun
 *
 */
public class PanDianDan extends DataSupport{
	
	private long id;

	private String gh;// 工号
	private String hw;// 货位
	private int sms;// 扫描数
	private long date;// 日期
	private List<PanDianProduct> panDianProducts = new ArrayList<PanDianProduct>();
	
	@Override
	public String toString(){
		return gh+";"+hw+";"+sms+";"+date+";"+panDianProducts.size();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getGh() {
		return gh;
	}

	public void setGh(String gh) {
		this.gh = gh;
	}

	public String getHw() {
		return hw;
	}

	public void setHw(String hw) {
		this.hw = hw;
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

	public List<PanDianProduct> getPanDianProducts() {
		return panDianProducts;
	}

	public void setPanDianProducts(List<PanDianProduct> panDianProducts) {
		this.panDianProducts = panDianProducts;
	}

}
