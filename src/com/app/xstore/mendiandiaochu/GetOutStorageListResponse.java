package com.app.xstore.mendiandiaochu;

import java.util.List;

import com.app.model.response.BaseResponse;

public class GetOutStorageListResponse extends BaseResponse{

//	{"ErrMessage":"","Result":true,"Info":[{"expType_code":"01","src_type":"","doc_code":"00S001OSDC1807230008","last_modify_date":"0001-01-01T00:00:00","remark":"","doc_date":"2018-07-23T17:24:54.173","src_shop":null,"doc_type":"OTI","exp_num":"1234567890","qty":1,"last_modify_user":"test","tar_shop":"S010","id":0,"shopCode":null,"create_date":"0001-01-01T00:00:00","doc_status":"I","src_code":"S001","tar_type":"","create_user":"test","tar_code":"S010"},{"expType_code":"01","src_type":"","doc_code":"00S001OSDC1807240010","last_modify_date":"0001-01-01T00:00:00","remark":"","doc_date":"2018-07-24T16:14:11.857","src_shop":null,"doc_type":"","exp_num":"0987563214","qty":3,"last_modify_user":"test","tar_shop":"S010","id":0,"shopCode":null,"create_date":"0001-01-01T00:00:00","doc_status":"I","src_code":"S001","tar_type":"","create_user":"test","tar_code":"S010"}],"ErrSysTrackMessage":"","ErrSysMessage":"","Message":"OK"}
	List<ChuRuKuHead> Info;

	public List<ChuRuKuHead> getInfo() {
		return Info;
	}

	public void setInfo(List<ChuRuKuHead> info) {
		Info = info;
	}
	
}
