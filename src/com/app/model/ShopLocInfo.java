package com.app.model;

public class ShopLocInfo {

	/**
	 * 门店编码			
		货位编码			
		描述			
		货位类型			
		货位状态			
	 */
	String OrgCode;
	String LocID;
	String DESCRIPTION;
	String TYPE;
	String STATUS;
	public String getOrgCode() {
		return OrgCode;
	}
	public void setOrgCode(String orgCode) {
		OrgCode = orgCode;
	}
	public String getLocID() {
		return LocID;
	}
	public void setLocID(String locID) {
		LocID = locID;
	}
	public String getDESCRIPTION() {
		return DESCRIPTION;
	}
	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}
	public String getTYPE() {
		return TYPE;
	}
	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return LocID;
	}
}
