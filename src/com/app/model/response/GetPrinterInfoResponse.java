package com.app.model.response;

import java.util.List;

import com.app.model.Printer;

public class GetPrinterInfoResponse extends BaseResponse{

	List<Printer> HeadInfo;

	public List<Printer> getHeadInfo() {
		return HeadInfo;
	}

	public void setHeadInfo(List<Printer> headInfo) {
		HeadInfo = headInfo;
	}
	
}
