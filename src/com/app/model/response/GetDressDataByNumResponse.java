package com.app.model.response;

import java.util.List;

import com.app.model.Customer;
import com.app.model.Dress;
import com.app.model.SimpleGoods;

public class GetDressDataByNumResponse extends BaseResponse{

	Dress HeadInfo;
	List<SimpleGoods> DetailInfo;
	List<Customer> CustInfo;
	public Dress getHeadInfo() {
		return HeadInfo;
	}
	public void setHeadInfo(Dress headInfo) {
		HeadInfo = headInfo;
	}
	public List<SimpleGoods> getDetailInfo() {
		return DetailInfo;
	}
	public void setDetailInfo(List<SimpleGoods> detailInfo) {
		DetailInfo = detailInfo;
	}
	public List<Customer> getCustInfo() {
		return CustInfo;
	}
	public void setCustInfo(List<Customer> custInfo) {
		CustInfo = custInfo;
	}
	
}
