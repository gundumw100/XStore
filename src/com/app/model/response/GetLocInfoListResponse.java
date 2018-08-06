package com.app.model.response;

import java.util.List;

import com.app.model.LocAreaInfo;

public class GetLocInfoListResponse extends BaseResponse{

	List<LocAreaInfo> LocAreaInfoList;

	public List<LocAreaInfo> getLocAreaInfoList() {
		return LocAreaInfoList;
	}

	public void setLocAreaInfoList(List<LocAreaInfo> locAreaInfoList) {
		LocAreaInfoList = locAreaInfoList;
	}
	
}
