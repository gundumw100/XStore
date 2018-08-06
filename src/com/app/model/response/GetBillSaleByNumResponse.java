package com.app.model.response;

import java.util.List;

import com.app.model.JvbillsaleInfo;
import com.app.model.JvbillsalebankInfo;
import com.app.model.JvbillsaledetailInfo;
import com.app.model.JvbillsalepayInfo;

public class GetBillSaleByNumResponse extends BaseResponse {

	JvbillsaleInfo billSale;
	List<JvbillsaledetailInfo> detailList;
	List<JvbillsalepayInfo> payList;
	List<JvbillsalebankInfo> bankList;
	public JvbillsaleInfo getBillSale() {
		return billSale;
	}
	public void setBillSale(JvbillsaleInfo billSale) {
		this.billSale = billSale;
	}
	public List<JvbillsaledetailInfo> getDetailList() {
		return detailList;
	}
	public void setDetailList(List<JvbillsaledetailInfo> detailList) {
		this.detailList = detailList;
	}
	public List<JvbillsalepayInfo> getPayList() {
		return payList;
	}
	public void setPayList(List<JvbillsalepayInfo> payList) {
		this.payList = payList;
	}
	public List<JvbillsalebankInfo> getBankList() {
		return bankList;
	}
	public void setBankList(List<JvbillsalebankInfo> bankList) {
		this.bankList = bankList;
	}

	
}
