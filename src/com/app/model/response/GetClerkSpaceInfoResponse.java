package com.app.model.response;

import java.util.ArrayList;

import com.app.model.JvclerkspaceInfo;
import com.app.model.JvclerkspacecommentInfo;

public class GetClerkSpaceInfoResponse {

	JvclerkspaceInfo ClerkspaceInfo;
	ArrayList<JvclerkspacecommentInfo> CommentInfos;
	/*BillSaleHeadInfo HeadEntity;
	ArrayList<BillSaleDetailInfo> DetailEntitys;
	ArrayList<BillSalePayInfo> PayEntitys;
	ArrayList<BillSaleBankInfo> BankEntitys;
	ArrayList<BillSalePromotionInfo> PromotionEntitys;
	
	public ArrayList<BillSalePromotionInfo> getPromotionEntitys() {
		return PromotionEntitys;
	}
	public void setPromotionEntitys(
			ArrayList<BillSalePromotionInfo> promotionEntitys) {
		PromotionEntitys = promotionEntitys;
	}
	public ArrayList<BillSaleBankInfo> getBankEntitys() {
		return BankEntitys;
	}
	public void setBankEntitys(ArrayList<BillSaleBankInfo> bankEntitys) {
		BankEntitys = bankEntitys;
	}
	public BillSaleHeadInfo getHeadEntity() {
		return HeadEntity;
	}
	public void setHeadEntity(BillSaleHeadInfo headEntity) {
		HeadEntity = headEntity;
	}
	public ArrayList<BillSaleDetailInfo> getDetailEntitys() {
		return DetailEntitys;
	}
	public void setDetailEntitys(ArrayList<BillSaleDetailInfo> detailEntitys) {
		DetailEntitys = detailEntitys;
	}
	public ArrayList<BillSalePayInfo> getPayEntitys() {
		return PayEntitys;
	}
	public void setPayEntitys(ArrayList<BillSalePayInfo> payEntitys) {
		PayEntitys = payEntitys;
	}*/
	
	public ArrayList<JvclerkspacecommentInfo> getCommentInfos() {
		return CommentInfos;
	}
	public JvclerkspaceInfo getClerkspaceInfo() {
		return ClerkspaceInfo;
	}
	public void setClerkspaceInfo(JvclerkspaceInfo clerkspaceInfo) {
		ClerkspaceInfo = clerkspaceInfo;
	}
	public void setCommentInfos(ArrayList<JvclerkspacecommentInfo> commentInfos) {
		CommentInfos = commentInfos;
	}

}
