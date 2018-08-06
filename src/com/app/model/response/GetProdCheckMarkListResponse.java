package com.app.model.response;

import java.util.List;

public class GetProdCheckMarkListResponse  extends BaseResponse{

	List<String> MarkList;

	public List<String> getMarkList() {
		return MarkList;
	}

	public void setMarkList(List<String> markList) {
		MarkList = markList;
	}
	
}
