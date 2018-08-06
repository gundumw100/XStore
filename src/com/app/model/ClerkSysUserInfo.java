package com.app.model;

public class ClerkSysUserInfo {

	String UserID;
	String UserName;
	String NickName;
	String PhoneNumber;
	String Sex;
	String Age;
	String Region;
	String HeadportraitURL;
	String OrgCode;
	String OwnerOrgCode;
	Boolean IsRegisted;
	
	public String getSex() {
		return Sex;
	}
	public void setSex(String sex) {
		Sex = sex;
	}
	public String getUserID() {
		return UserID;
	}
	public void setUserID(String userID) {
		UserID = userID;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getNickName() {
		return NickName;
	}
	public void setNickName(String nickName) {
		NickName = nickName;
	}
	public String getPhoneNumber() {
		return PhoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		PhoneNumber = phoneNumber;
	}
	public String getAge() {
		return Age;
	}
	public void setAge(String age) {
		Age = age;
	}
	public String getRegion() {
		return Region;
	}
	public void setRegion(String region) {
		Region = region;
	}
	public String getHeadportraitURL() {
		return HeadportraitURL;
	}
	public void setHeadportraitURL(String headportraitURL) {
		HeadportraitURL = headportraitURL;
	}
	public String getOrgCode() {
		return OrgCode;
	}
	public void setOrgCode(String orgCode) {
		OrgCode = orgCode;
	}
	public String getOwnerOrgCode() {
		return OwnerOrgCode;
	}
	public void setOwnerOrgCode(String ownerOrgCode) {
		OwnerOrgCode = ownerOrgCode;
	}
	public Boolean getIsRegisted() {
		return IsRegisted;
	}
	public void setIsRegisted(Boolean isRegisted) {
		IsRegisted = isRegisted;
	}

}
