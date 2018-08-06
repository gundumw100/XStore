package com.app.model.response;

import java.util.List;

import com.app.model.ClerkWatchInfo;

public class CheckClerkWatchResponse extends BaseResponse{

	List<ClerkWatchInfo> WatchInfos ;

	public List<ClerkWatchInfo> getWatchInfos() {
		return WatchInfos;
	}

	public void setWatchInfos(List<ClerkWatchInfo> watchInfos) {
		WatchInfos = watchInfos;
	}
	
}
