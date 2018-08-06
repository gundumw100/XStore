package com.app.model.response;

public class BaseResponse {
	private String Message;
	private String ErrMessage;
	private String ErrSysMessage;
	private String ErrSysTrackMessage;

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public String getErrMessage() {
		return ErrMessage;
	}

	public void setErrMessage(String errMessage) {
		ErrMessage = errMessage;
	}

	public String getErrSysMessage() {
		return ErrSysMessage;
	}

	public void setErrSysMessage(String errSysMessage) {
		ErrSysMessage = errSysMessage;
	}

	public String getErrSysTrackMessage() {
		return ErrSysTrackMessage;
	}

	public void setErrSysTrackMessage(String errSysTrackMessage) {
		ErrSysTrackMessage = errSysTrackMessage;
	}
}
