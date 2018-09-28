package com.app.model.response;

public class SaveBillSaleResponse extends BaseResponse{

	//{"ErrMessage":"","Result":true,"saleNo":"00S111BIL1809190025","ErrSysTrackMessage":"","ErrSysMessage":"","Message":"保存成功"}
	
	String saleNo;

	public String getSaleNo() {
		return saleNo;
	}

	public void setSaleNo(String saleNo) {
		this.saleNo = saleNo;
	}
	
}
