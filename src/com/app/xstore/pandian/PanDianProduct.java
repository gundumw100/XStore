package com.app.xstore.pandian;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * 盘点单中的商品
 * @author Ni Guijun
 *
 */
public class PanDianProduct extends DataSupport{

	private String barCode;// 条码 varchar(13)
	private String name;// 名称 varchar(60)
	private int qty;//扫描件数
	private PanDianDan panDianDan;//盘点单
	
	@Column(ignore = true)
	private String accountingCode;// 核算码 varchar(10)
	@Column(ignore = true)
	private String patternCode;// 款号 varchar(10)
	
	@Override
	public String toString(){
		return barCode+";"+name+";"+qty+";";
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public PanDianDan getPanDianDan() {
		return panDianDan;
	}

	public void setPanDianDan(PanDianDan panDianDan) {
		this.panDianDan = panDianDan;
	}

	public String getAccountingCode() {
		return accountingCode;
	}

	public void setAccountingCode(String accountingCode) {
		this.accountingCode = accountingCode;
	}

	public String getPatternCode() {
		return patternCode;
	}

	public void setPatternCode(String patternCode) {
		this.patternCode = patternCode;
	}
	
}
