package com.app.model.response;

import java.util.List;

import com.app.model.ClerkSpaceInfo;
import com.app.model.ClerkSpaceInfoWhole;
import com.app.model.JvclerkspaceInfo;

public class GetClerkSpaceListResponse {
	//JvclerkspaceInfo
	List<JvclerkspaceInfo> HeadInfo;

	public List<JvclerkspaceInfo> getHeadInfo() {
		return HeadInfo;
	}

	public void setHeadInfo(List<JvclerkspaceInfo> headInfo) {
		HeadInfo = headInfo;
	}
	
//	List<ClerkSpaceInfoWhole> SpaceInfoWholes;
//	List<ClerkSpaceInfo> SpaceInfos;
//	public List<ClerkSpaceInfoWhole> getSpaceInfoWholes() {
//		return SpaceInfoWholes;
//	}
//	public void setSpaceInfoWholes(List<ClerkSpaceInfoWhole> spaceInfoWholes) {
//		SpaceInfoWholes = spaceInfoWholes;
//	}
//	public List<ClerkSpaceInfo> getSpaceInfos() {
//		return SpaceInfos;
//	}
//	public void setSpaceInfos(List<ClerkSpaceInfo> spaceInfos) {
//		SpaceInfos = spaceInfos;
//	}

}
