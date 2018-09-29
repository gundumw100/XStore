package com.app.net;

import java.util.List;

import com.app.model.Customer;
import com.app.model.EmailInfo;
import com.app.model.JvSimpleCheckInfo;
import com.app.model.JvbillsaleInfo;
import com.app.model.JvbillsalebankInfo;
import com.app.model.JvbillsaledetailInfo;
import com.app.model.JvbillsalepayInfo;
import com.app.model.JvclerkspaceInfo;
import com.app.model.JvclerkspacecommentInfo;
import com.app.model.LocAreaInfo;
import com.app.model.LocQty;
import com.app.model.ProdCheckDataInfo;
import com.app.model.SimpleGoods;
import com.app.xstore.mendiandiaochu.ChuRuKuHead;
import com.app.xstore.mendiandiaochu.ChuRuKuProduct;
import com.app.xstore.shangpindangan.ProdColor;
import com.app.xstore.shangpindangan.ProdColorGroup;
import com.app.xstore.shangpindangan.ProdColorImage;
import com.app.xstore.shangpindangan.ProductDangAn;

public class Pars {

	String sellingPoint;
	
	public void setSellingPoint(String sellingPoint) {
		this.sellingPoint = sellingPoint;
	}

	String classification;
	String property;
	
	public void setClassification(String classification) {
		this.classification = classification;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	String goodsCode;
	String labelCode;
	
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public void setLabelCode(String labelCode) {
		this.labelCode = labelCode;
	}

	List<ProdColorGroup> groupInfos;
	List<ProdColor> colorInfos;
	
	public void setGroupInfos(List<ProdColorGroup> groupInfos) {
		this.groupInfos = groupInfos;
	}

	public void setColorInfos(List<ProdColor> colorInfos) {
		this.colorInfos = colorInfos;
	}

	String groupCode;
	
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	List<String> goodsSnList;
	
	public void setGoodsSnList(List<String> goodsSnList) {
		this.goodsSnList = goodsSnList;
	}

	List<ProdColorImage> imageInfo;
	
	public void setImageInfo(List<ProdColorImage> imageInfo) {
		this.imageInfo = imageInfo;
	}


	List<ProdColorImage> goodsImage;
	
	public void setGoodsImage(List<ProdColorImage> goodsImage) {
		this.goodsImage = goodsImage;
	}


	ChuRuKuHead inStoHeadInfo;
	List<ChuRuKuProduct> inStoDetailList;
	
	public void setInStoHeadInfo(ChuRuKuHead inStoHeadInfo) {
		this.inStoHeadInfo = inStoHeadInfo;
	}

	public void setInStoDetailList(List<ChuRuKuProduct> inStoDetailList) {
		this.inStoDetailList = inStoDetailList;
	}

	List<String> goodsSns;
	
	public void setGoodsSns(List<String> goodsSns) {
		this.goodsSns = goodsSns;
	}

	String docCode;
	
	public void setDocCode(String docCode) {
		this.docCode = docCode;
	}

	ChuRuKuHead outStoHeadInfo;
	List<ChuRuKuProduct> outStoDetailList;

	public void setOutStoHeadInfo(ChuRuKuHead outStoHeadInfo) {
		this.outStoHeadInfo = outStoHeadInfo;
	}

	public void setOutStoDetailList(List<ChuRuKuProduct> outStoDetailList) {
		this.outStoDetailList = outStoDetailList;
	}

	String docStatus;
	
	public void setDocStatus(String docStatus) {
		this.docStatus = docStatus;
	}

	String companyCode;
	
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}


	String enabled;
	String goodsSn;
	List<String> goodsBrand;
	List<String> goodsColor;
	List<String> goodsCs;
	List<String> goodsSort;
	List<String> goodsSpec;
	List<String> goodsSeason;
	List<String> goodsJldw;
	List<String> goodsCw;
	List<String> goodsOther;
	List<String> goodsLabel;
	
	public void setGoodsLabel(List<String> goodsLabel) {
		this.goodsLabel = goodsLabel;
	}

	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}
	
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	
	public void setGoodsBrand(List<String> goodsBrand) {
		this.goodsBrand = goodsBrand;
	}

	public void setGoodsColor(List<String> goodsColor) {
		this.goodsColor = goodsColor;
	}

	public void setGoodsCs(List<String> goodsCs) {
		this.goodsCs = goodsCs;
	}

	public void setGoodsSort(List<String> goodsSort) {
		this.goodsSort = goodsSort;
	}

	public void setGoodsSpec(List<String> goodsSpec) {
		this.goodsSpec = goodsSpec;
	}

	public void setGoodsSeason(List<String> goodsSeason) {
		this.goodsSeason = goodsSeason;
	}

	public void setGoodsJldw(List<String> goodsJldw) {
		this.goodsJldw = goodsJldw;
	}

	public void setGoodsCw(List<String> goodsCw) {
		this.goodsCw = goodsCw;
	}

	public void setGoodsOther(List<String> goodsOther) {
		this.goodsOther = goodsOther;
	}


	List<ProductDangAn> goodsInfo;
	String dateCode;
	String csCode;
	String jldwCode;
	String seasonCode;
	String cwCode;
	String specCode;
	String colorCode;
	String brandCode;
	String sortCode;
	String otherCode;
	String styleCode;
	

	public void setOtherCode(String otherCode) {
		this.otherCode = otherCode;
	}

	public void setStyleCode(String styleCode) {
		this.styleCode = styleCode;
	}

	public void setGoodsInfo(List<ProductDangAn> goodsInfo) {
		this.goodsInfo = goodsInfo;
	}

	public void setDateCode(String dateCode) {
		this.dateCode = dateCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public void setSpecCode(String specCode) {
		this.specCode = specCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public void setSortCode(String sortCode) {
		this.sortCode = sortCode;
	}

	public void setCwCode(String cwCode) {
		this.cwCode = cwCode;
	}

	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode;
	}

	public void setJldwCode(String jldwCode) {
		this.jldwCode = jldwCode;
	}

	public void setCsCode(String csCode) {
		this.csCode = csCode;
	}

	String box_code;
	
	public void setBox_code(String box_code) {
		this.box_code = box_code;
	}

	JvclerkspacecommentInfo commentInfo;
	
	public void setCommentInfo(JvclerkspacecommentInfo commentInfo) {
		this.commentInfo = commentInfo;
	}

	String saleNo;
	
	public void setSaleNo(String saleNo) {
		this.saleNo = saleNo;
	}

	List detailList;
	
	public void setDetailList(List detailList) {
		this.detailList = detailList;
	}


	JvbillsaleInfo billSale;
	List<JvbillsalepayInfo> payList;
	List<JvbillsalebankInfo> bankList;
	
	public void setBillSale(JvbillsaleInfo billSale) {
		this.billSale = billSale;
	}


	public void setPayList(List<JvbillsalepayInfo> payList) {
		this.payList = payList;
	}

	public void setBankList(List<JvbillsalebankInfo> bankList) {
		this.bankList = bankList;
	}

	String clerkSpaceID;
	String Operator;
	
	public void setOperator(String operator) {
		Operator = operator;
	}

	public void setClerkSpaceID(String clerkSpaceID) {
		this.clerkSpaceID = clerkSpaceID;
	}

	String NameSpace;
	public void setNameSpace(String nameSpace) {
		NameSpace = nameSpace;
	}

	JvclerkspaceInfo ClerkspaceInfo;
	public void setClerkspaceInfo(JvclerkspaceInfo clerkspaceInfo) {
		ClerkspaceInfo = clerkspaceInfo;
	}

	String labelid;
	String keyWords;
	String order;
	String formatTag;
	Integer startNum;
	
	public void setLabelid(String labelid) {
		this.labelid = labelid;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public void setFormatTag(String formatTag) {
		this.formatTag = formatTag;
	}

	public void setStartNum(Integer startNum) {
		this.startNum = startNum;
	}

	List<JvSimpleCheckInfo> jvSimpleCheckInfos;
	String EmailDesc;
	
	public void setJvSimpleCheckInfos(List<JvSimpleCheckInfo> jvSimpleCheckInfos) {
		this.jvSimpleCheckInfos = jvSimpleCheckInfos;
	}

	public void setEmailDesc(String emailDesc) {
		EmailDesc = emailDesc;
	}

	String starttime;
	String endtime;
	
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	String user;
//	List<EmailInfo> detailList;
	
	public void setUser(String user) {
		this.user = user;
	}

//	public void setDetailList(List<EmailInfo> detailList) {
//		this.detailList = detailList;
//	}

	String shopCode;
	String vipCode;
	String mobile;
	String sex;
	String name;
	String birth;
	String address;
	String createuser;
	
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public void setVipCode(String vipCode) {
		this.vipCode = vipCode;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	String shopId;
	String printerip;
	String description;
	String type;
	
	String Shop_Id;
	List<SimpleGoods> DressDtlData;
	List<Customer> DressCustData;
	
	List<String> GoodsCode;
	
	String user_name;
	String goods_sn;
	List<LocAreaInfo> LocAreaList;
	
	List<String> receiverList;
	List<String> docNumList;
	
	List<ProdCheckDataInfo> CheckDataList;
	Boolean IsUploadWithoutCheck;
	Integer CheckPriority;
	
	List<String> DocNumList;
	
	String doc_num;
	String DocNum;
	String loc_id;
	String area_code;
	String startDate;
	String endDate;
	
	String create_user;
	List<LocQty> ProdPreCheck;
	Integer total_qty;
	Integer id;
	
	String shop_id;
	String shelf_code;
	String start_date;
	String end_date;
	String creator;
	Integer start;
	Integer size;

	String shop_code;
	String user_code;
	String user_pwd;
	String imei;
	String role_type;
	String device_type;
	String version_id;

	String version_code;
	String downloader_id;
	String downloader_shopid;
	String ip_address;
	String computer;
	String downloads;

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public void setPrinterip(String printerip) {
		this.printerip = printerip;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setShop_Id(String shop_Id) {
		Shop_Id = shop_Id;
	}

	public void setDressDtlData(List<SimpleGoods> dressDtlData) {
		DressDtlData = dressDtlData;
	}

	public void setDressCustData(List<Customer> dressCustData) {
		DressCustData = dressCustData;
	}

	public void setGoodsCode(List<String> goodsCode) {
		GoodsCode = goodsCode;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public void setGoods_sn(String goods_sn) {
		this.goods_sn = goods_sn;
	}

	public void setLocAreaList(List<LocAreaInfo> locAreaList) {
		LocAreaList = locAreaList;
	}

	public void setReceiverList(List<String> receiverList) {
		this.receiverList = receiverList;
	}

	public void setCheckDataList(List<ProdCheckDataInfo> checkDataList) {
		CheckDataList = checkDataList;
	}

	public void setIsUploadWithoutCheck(Boolean isUploadWithoutCheck) {
		IsUploadWithoutCheck = isUploadWithoutCheck;
	}

	public void setCheckPriority(Integer checkPriority) {
		CheckPriority = checkPriority;
	}

	public void setDocNumList(List<String> docNumList) {
		DocNumList = docNumList;
	}

	public void setDoc_num(String doc_num) {
		this.doc_num = doc_num;
	}

	public void setDocNum(String docNum) {
		DocNum = docNum;
	}

	public void setLoc_id(String loc_id) {
		this.loc_id = loc_id;
	}

	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setTotal_qty(Integer total_qty) {
		this.total_qty = total_qty;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setProdPreCheck(List<LocQty> prodPreCheck) {
		ProdPreCheck = prodPreCheck;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}

	public void setShelf_code(String shelf_code) {
		this.shelf_code = shelf_code;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public void setShop_code(String shop_code) {
		this.shop_code = shop_code;
	}

	public void setUser_code(String user_code) {
		this.user_code = user_code;
	}

	public void setUser_pwd(String user_pwd) {
		this.user_pwd = user_pwd;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public void setRole_type(String role_type) {
		this.role_type = role_type;
	}

	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}

	public void setVersion_id(String version_id) {
		this.version_id = version_id;
	}

	public void setVersion_code(String version_code) {
		this.version_code = version_code;
	}

	public void setDownloader_id(String downloader_id) {
		this.downloader_id = downloader_id;
	}

	public void setDownloader_shopid(String downloader_shopid) {
		this.downloader_shopid = downloader_shopid;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

	public void setComputer(String computer) {
		this.computer = computer;
	}

	public void setDownloads(String downloads) {
		this.downloads = downloads;
	}

}
