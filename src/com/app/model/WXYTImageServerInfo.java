package com.app.model;


public class WXYTImageServerInfo{

	String NameSpace;
	String projectid;
	String secretid;
	String secretkey;
	public String getNameSpace() {
		return NameSpace;
	}
	public void setNameSpace(String nameSpace) {
		NameSpace = nameSpace;
	}
	public String getProjectid() {
		return projectid;
	}
	public void setProjectid(String projectid) {
		this.projectid = projectid;
	}
	public String getSecretid() {
		return secretid;
	}
	public void setSecretid(String secretid) {
		this.secretid = secretid;
	}
	public String getSecretkey() {
		return secretkey;
	}
	public void setSecretkey(String secretkey) {
		this.secretkey = secretkey;
	}

}
