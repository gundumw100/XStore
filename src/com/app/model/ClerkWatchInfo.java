package com.app.model;

public class ClerkWatchInfo {

	Integer ID;
	String MasterUserID;
	String SlaveUserID;
	String SlaveUserName;
	String SlaveNickName;
	String Sex;
	String HeadportraitURL;
	
	public Integer getID() {
		return ID;
	}
	public void setID(Integer iD) {
		ID = iD;
	}
	public String getMasterUserID() {
		return MasterUserID;
	}
	public void setMasterUserID(String masterUserID) {
		MasterUserID = masterUserID;
	}
	public String getSlaveUserID() {
		return SlaveUserID;
	}
	public void setSlaveUserID(String slaveUserID) {
		SlaveUserID = slaveUserID;
	}
	public String getSlaveUserName() {
		return SlaveUserName;
	}
	public void setSlaveUserName(String slaveUserName) {
		SlaveUserName = slaveUserName;
	}
	public String getSlaveNickName() {
		return SlaveNickName;
	}
	public void setSlaveNickName(String slaveNickName) {
		SlaveNickName = slaveNickName;
	}
	public String getSex() {
		return Sex;
	}
	public void setSex(String sex) {
		Sex = sex;
	}
	public String getHeadportraitURL() {
		return HeadportraitURL;
	}
	public void setHeadportraitURL(String headportraitURL) {
		HeadportraitURL = headportraitURL;
	}

}
