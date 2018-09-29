package com.app.net;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response.Listener;
import com.app.model.Customer;
import com.app.model.JvSimpleCheckInfo;
import com.app.model.JvbillsaleInfo;
import com.app.model.JvbillsalebankInfo;
import com.app.model.JvbillsalepayInfo;
import com.app.model.JvclerkspaceInfo;
import com.app.model.JvclerkspacecommentInfo;
import com.app.model.LocAreaInfo;
import com.app.model.LocQty;
import com.app.model.ProdCheckDataInfo;
import com.app.model.SimpleGoods;
import com.app.xstore.App;
import com.app.xstore.mendiandiaochu.ChuRuKuHead;
import com.app.xstore.mendiandiaochu.ChuRuKuProduct;
import com.app.xstore.shangpindangan.ProdColor;
import com.app.xstore.shangpindangan.ProdColorGroup;
import com.app.xstore.shangpindangan.ProdColorImage;
import com.app.xstore.shangpindangan.ProductDangAn;
import com.base.net.VolleyHelper;
import com.google.gson.Gson;

/**
 * 
 * @author pythoner
 *
 */
public final class Commands {

	public static String BASE_URL = "http://www.app.z-sh.com:8091/handler/user_handler.ashx";// 测试

	public static String IP = "http://139.196.53.136:8080";
	public static String PORT_Upload = "8078";
	public static final String UPLOAD_ZIP_URL="http://"+IP+":"+PORT_Upload+"/UploadFile/zipSave.ashx";
	
	static{
		if(App.isLog){
			BASE_URL = "http://www.app.z-sh.com:8091/handler/user_handler.ashx";// 测试
		}else{
			BASE_URL = "http://www.fig.z-sh.com:8091/handler/user_handler.ashx";// 正式
		}
	}
	
	private Commands() {
		throw new UnsupportedOperationException("cannot be instantiated");
	}
	
	private static String getCompanyCode(){
		if(App.user==null||App.user.getShopInfo()==null){
			Log.i("tag", "App.user.getShopInfo() is null");
			return null;
		}
		return App.user.getShopInfo().getCompany_code();
	}
	private static String getShopCode(){
		if(App.user==null||App.user.getShopInfo()==null){
			Log.i("tag", "App.user.getShopInfo() is null");
			return null;
		}
		return App.user.getShopInfo().getShop_code();
	}
	
	/**
	 * 更新款卖点
	 * @param context
	 * @param sellingPoint
	 * @param styleCode
	 * @param dateCode
	 * @param onSuccessListener
	 */
	public static void doCommandUpdateProdStyleSellingPoint(Context context,String sellingPoint,String styleCode,String dateCode,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
//		pars.setShopCode(getShopCode());
		pars.setSellingPoint(sellingPoint);
		pars.setStyleCode(styleCode);
		pars.setDateCode(dateCode);
		doCommand(context, "UpdateProdStyleSellingPoint", pars, onSuccessListener);
	}
	
	/**
	 * 请求一组商品的商品详情（可用于校验，主图，颜色图.....）
	 * @param context
	 * @param goodsSns
	 * @param onSuccessListener
	 */
	public static void doCommandGetGoodsListBySKUs(Context context,List<String> goodsSns,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setGoodsSns(goodsSns);
		doCommand(context, "GetGoodsListBySKUs", pars, onSuccessListener);
	}
	
	/**
	 * 保存商品标签信息
	 * @param context
	 * @param description
	 * @param onSuccessListener
	 */
//	public static void doCommandAddProdLabel(Context context,String description,Listener<JSONObject> onSuccessListener) {
//		Pars pars = new Pars();
//		pars.setCompanyCode(getCompanyCode());
//		pars.setDescription(description);
//		doCommand(context, "AddProdLabel", pars, onSuccessListener);
//	}
	
	public static void doCommandAddProdLabel(Context context,String classification,String property,String description,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setClassification(classification);
		pars.setProperty(property);
		pars.setDescription(description);
		doCommand(context, "AddProdLabel", pars, onSuccessListener);
	}
	/**
	 * 删除商品标签
	 * @param context
	 * @param labelCode
	 * @param onSuccessListener
	 */
	public static void doCommandDeleteProdLabel(Context context,String labelCode,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setLabelCode(labelCode);
		doCommand(context, "DeleteProdLabel", pars, onSuccessListener);
	}
	
//	/**
//	 * 变更商品标签状态
//	 * @param context
//	 * @param labelCode
//	 * @param enabled
//	 * @param onSuccessListener
//	 */
//	public static void doCommandUpdateProdLabelEnabled(Context context,String labelCode,String enabled,Listener<JSONObject> onSuccessListener) {
//		Pars pars = new Pars();
//		pars.setCompanyCode(getCompanyCode());
//		pars.setLabelCode(labelCode);
//		pars.setEnabled(enabled);
//		doCommand(context, "UpdateProdLabelEnabled", pars, onSuccessListener);
//	}
	/**
	 * 获取商品标签列表
	 * @param context
	 * @param onSuccessListener
	 */
	public static void doCommandGetProdLabelList(Context context,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		doCommand(context, "GetProdLabelList", pars, onSuccessListener);
	}
	
	/**
	 * 获取商品标签对应的标签
	 * @param context
	 * @param goodsCode
	 * @param onSuccessListener
	 */
	public static void doCommandGetProdLabelMappingList(Context context,String goodsCode,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setGoodsCode(goodsCode);
		doCommand(context, "GetProdLabelMappingList", pars, onSuccessListener);
	}
	
	/**
	 * 保存商品标签对应的标签
	 * @param context
	 * @param goodsCode
	 * @param labelCode
	 * @param description
	 * @param onSuccessListener
	 */
	public static void doCommandAddProdLabelMapping(Context context,String goodsCode,String labelCode,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setGoodsCode(goodsCode);
		pars.setLabelCode(labelCode);
		doCommand(context, "AddProdLabelMapping", pars, onSuccessListener);
	}
	/**
	 * 删除商品标签对应的标签
	 * @param context
	 * @param goodsCode
	 * @param labelCode
	 * @param onSuccessListener
	 */
	public static void doCommandDeleteProdLabelMapping(Context context,String goodsCode,String labelCode,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setGoodsCode(goodsCode);
		pars.setLabelCode(labelCode);
		doCommand(context, "DeleteProdLabelMapping", pars, onSuccessListener);
	}
	
	/**
	 * 查询商品款图片
	 * @param context
	 * @param styleCode
	 * @param onSuccessListener
	 */
	public static void doCommandGetGoodsStyleImageList(Context context,String styleCode,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setStyleCode(styleCode);
		doCommand(context, "GetGoodsStyleImageList", pars, onSuccessListener);
	}
	/**
	 * 修改商品款图片
	 * @param context
	 * @param imageInfo
	 * @param onSuccessListener
	 */
	public static void doCommandUpdateGoodsStyleImage(Context context,List<ProdColorImage> imageInfo,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setImageInfo(imageInfo);
		doCommand(context, "UpdateGoodsStyleImage", pars, onSuccessListener,false);
	}
	
	/**
	 * 添加商品款图片
	 * @param context
	 * @param imageInfo
	 * @param onSuccessListener
	 */
	public static void doCommandAddGoodsStyleImage(Context context,List<ProdColorImage> imageInfo,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
//		pars.setCompanyCode(getCompanyCode());
		pars.setImageInfo(imageInfo);
		doCommand(context, "AddGoodsStyleImage", pars, onSuccessListener,false);
	}
	/**
	 * 删除商品款图片
	 * @param context
	 * @param imageInfo
	 * @param onSuccessListener
	 */
	public static void doCommandDeleteGoodsStyleImage(Context context,List<ProdColorImage> imageInfo,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
//		pars.setCompanyCode(getCompanyCode());
		pars.setImageInfo(imageInfo);
		doCommand(context, "DeleteGoodsStyleImage", pars, onSuccessListener,false);
	}
	/**
	 * 添加商品颜色图片
	 * @param context
	 * @param Pars
	 * @param onSuccessListener
	 */
	public static void doCommandAddGoodsColorImage(Context context,List<ProdColorImage> imageInfo,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
//		pars.setCompanyCode(getCompanyCode());
		pars.setImageInfo(imageInfo);
		doCommand(context, "AddGoodsColorImage", pars, onSuccessListener);
	}
	/**
	 * 查询商品颜色图片
	 * @param context
	 * @param styleCode
	 * @param colorCode 颜色值null表示全部
	 * @param onSuccessListener
	 */
	public static void doCommandGetGoodsColorImageList(Context context,String styleCode,String colorCode,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setStyleCode(styleCode);
		pars.setColorCode(colorCode);
		doCommand(context, "GetGoodsColorImageList", pars, onSuccessListener);
	}
	/**
	 * 删除商品颜色图片
	 * @param context
	 * @param Pars
	 * @param onSuccessListener
	 */
	public static void doCommandDeleteGoodsColorImage(Context context,List<ProdColorImage> imageInfo,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
//		pars.setCompanyCode(getCompanyCode());
		pars.setImageInfo(imageInfo);
		doCommand(context, "DeleteGoodsColorImage", pars, onSuccessListener);
	}
	/**
	 * 修改商品颜色图片
	 * @param context
	 * @param Pars
	 * @param onSuccessListener
	 */
	public static void doCommandUpdateGoodsColorImage(Context context,List<ProdColorImage> imageInfo,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
//		pars.setCompanyCode(getCompanyCode());
		pars.setImageInfo(imageInfo);
		doCommand(context, "UpdateGoodsColorImage", pars, onSuccessListener,false);
	}
	
	/**
	 * 获取入库单详情
	 * @param context
	 * @param shopCode
	 * @param docCode
	 * @param onSuccessListener
	 */
	public static void doCommandGetInStorageDetailList(Context context,String docCode,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShopCode(getShopCode());
		pars.setDocCode(docCode);
		doCommand(context, "GetInStorageDetailList", pars, onSuccessListener);
	}
	/**
	 * 创建入库单
	 * @param context
	 * @param headInfo
	 * @param inStoDetailList
	 * @param onSuccessListener
	 */
	public static void doCommandCreateInStorage(Context context,ChuRuKuHead inStoHeadInfo,List<ChuRuKuProduct> inStoDetailList,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setInStoHeadInfo(inStoHeadInfo);
		pars.setInStoDetailList(inStoDetailList);
		doCommand(context, "CreateInStorage", pars, onSuccessListener);
	}
	
	/**
	 * 获取入库单列表
	 * @param context
	 * @param startDate
	 * @param endDate
	 * @param onSuccessListener
	 */
	public static void doCommandGetInStorageList(Context context,String startDate,String endDate,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShopCode(getShopCode());
		pars.setStartDate(startDate);
		pars.setEndDate(endDate);
		doCommand(context, "GetInStorageList", pars, onSuccessListener);
	}
	
	/**
	 * 获取入库单列表
	 * @param context
	 * @param doc_date
	 * @param doc_status
	 * @param onSuccessListener
	 */
//	public static void doCommandGetInStorageList(Context context,String startDate,String endDate,String docStatus,Listener<JSONObject> onSuccessListener) {
//		Pars pars = new Pars();
//		pars.setShopCode(getShopCode());
//		pars.setStartDate(startDate);
//		pars.setEndDate(endDate);
//		pars.setDocStatus(docStatus);
//		doCommand(context, "GetInStorageList", pars, onSuccessListener);
//	}
	
	/**
	 * 入库门店获取出库单列表
	 * @param context
	 * @param onSuccessListener
	 */
	public static void doCommandGetOutStorageListByShopCode(Context context,String startDate,String endDate,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShopCode(getShopCode());
//		pars.setShopCode("S001");
		pars.setStartDate(startDate);
		pars.setEndDate(endDate);
//		pars.setDocStatus(docStatus);
		doCommand(context, "GetOutStorageListByShopCode", pars, onSuccessListener);
	}
	/**
	 * 根据门店号获取其他门店信息
	 * @param context
	 * @param onSuccessListener
	 */
	public static void doCommandGetOtherShopByCode(Context context,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShopCode(getShopCode());
		doCommand(context, "GetOtherShopByCode", pars, onSuccessListener);
	}
	/**
	 * 创建出库单
	 * @param context
	 * @param headInfo
	 * @param detailList
	 * @param onSuccessListener
	 */
	public static void doCommandCreateOutStorage(Context context,ChuRuKuHead outStoHeadInfo,List<ChuRuKuProduct> outStoDetailList,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setOutStoHeadInfo(outStoHeadInfo);
		pars.setOutStoDetailList(outStoDetailList);
		doCommand(context, "CreateOutStorage", pars, onSuccessListener);
	}
	/**
	 * 提交出库单
	 * @param context
	 * @param doc_code
	 * @param onSuccessListener
	 */
	public static void doCommandSubmitOutStorage(Context context,String docCode,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShopCode(getShopCode());
		pars.setDocCode(docCode);
		doCommand(context, "SubmitOutStorage", pars, onSuccessListener);
	}
	
	/**
	 * 获取出库单列表
	 * @param context
	 * @param doc_date
	 * @param doc_status
	 * @param onSuccessListener
	 */
	public static void doCommandGetOutStorageList(Context context,String startDate,String endDate,String docStatus,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShopCode(getShopCode());
		pars.setStartDate(startDate);
		pars.setEndDate(endDate);
		pars.setDocStatus(docStatus);
		doCommand(context, "GetOutStorageList", pars, onSuccessListener);
	}
	
	/**
	 * 获取出库单详情
	 * @param context
	 * @param docCode
	 * @param onSuccessListener
	 */
	public static void doCommandGetOutStorageDetailList(Context context,String shopCode,String docCode,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShopCode(shopCode);
		pars.setDocCode(docCode);
		doCommand(context, "GetOutStorageDetailList", pars, onSuccessListener);
	}
	
	/**
	 * 获取快递列表
	 * @param context
	 * @param onSuccessListener
	 */
	public static void doCommandGetExpressList(Context context,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
//		pars.setCompanyCode(getCompanyCode());
		doCommand(context, "GetExpressList", pars, onSuccessListener);
	}
	
	/**
	 * 根据SKU获取库存
	 * @param context
	 * @param goods_sn
	 * @param onSuccessListener
	 */
	public static void doCommandGetStockBySKUList(Context context,List<String> goodsSnList,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShopCode(getShopCode());
		pars.setGoodsSnList(goodsSnList);
		doCommand(context, "GetStockBySKUList", pars, onSuccessListener);
	}
	
	/**
	 * 根据参数获取库存
	 * @param context
	 * @param goods_brand
	 * @param goods_color
	 * @param goods_cs
	 * @param goods_sort
	 * @param goods_spec
	 * @param goods_season
	 * @param goods_jldw
	 * @param goods_cw
	 * @param goods_other
	 * @param onSuccessListener
	 */
	public static void doCommandGetStockByParamList(Context context,List<String> goods_brand,List<String> goods_color,List<String> goods_cs,List<String> goods_sort,List<String> goods_spec,List<String> goods_season,List<String> goods_jldw,List<String> goods_cw,List<String> goods_other,List<String> goods_label,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setShopCode(getShopCode());
		pars.setGoodsBrand(goods_brand);
		pars.setGoodsColor(goods_color);
		pars.setGoodsCs(goods_cs);
		pars.setGoodsSort(goods_sort);
		pars.setGoodsSpec(goods_spec);
		pars.setGoodsSeason(goods_season);
		pars.setGoodsJldw(goods_jldw);
		pars.setGoodsCw(goods_cw);
		pars.setGoodsOther(goods_other);
		pars.setGoodsLabel(goods_label);
		doCommand(context, "GetStockByParamList", pars, onSuccessListener);
	}
	
	/**
	 * 通过SKU获取收藏，判断是否收藏了
	 * @param context
	 * @param goods_sn
	 * @param onSuccessListener
	 */
	public static void doCommandGetCollectGoodsBySKU(Context context,String goods_sn,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShopCode(getShopCode());
		pars.setGoodsSn(goods_sn);
		doCommand(context, "GetCollectGoodsBySKU", pars, onSuccessListener);
	}
	/**
	 * 收藏商品
	 * @param context
	 * @param goods_sn
	 * @param onSuccessListener
	 */
	public static void doCommandAddCollectGoods(Context context,String goods_sn,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShopCode(getShopCode());
		pars.setGoodsSn(goods_sn);
		doCommand(context, "AddCollectGoods", pars, onSuccessListener);
	}
	/**
	 * 删除收藏商品
	 * @param context
	 * @param goods_sn
	 * @param onSuccessListener
	 */
	public static void doCommandDelCollectGoods(Context context,String goods_sn,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShopCode(getShopCode());
		pars.setGoodsSn(goods_sn);
		doCommand(context, "DelCollectGoods", pars, onSuccessListener);
	}
	/**
	 * 获取收藏列表
	 * @param context
	 * @param onSuccessListener
	 */
	public static void doCommandGetCollectGoodsList(Context context,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShopCode(getShopCode());
		doCommand(context, "GetCollectGoodsList", pars, onSuccessListener);
	}
	/**
	 * 获取最新上架商品
	 * @param context
	 * @param onSuccessListener
	 */
	public static void doCommandGetGoodsListBySjDate(Context context,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		doCommand(context, "GetGoodsListBySjDate", pars, onSuccessListener);
	}
	/**
	 * 获取商品BySKU:(goods_sn 可以传 款码或者SKU)
	 * @param context
	 * @param goods_sn 长度可以为6
	 * @param onSuccessListener
	 */
	public static void doCommandGetGoodsListBySKU(Context context,String goods_sn,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setGoodsSn(goods_sn);
		doCommand(context, "GetGoodsListBySKU", pars, onSuccessListener);
	}
	/**
	 * 获取商品款通过属性:
	 * @param context
	 * @param goods_brand
	 * @param goods_color
	 * @param goods_cs
	 * @param goods_sort
	 * @param goods_spec
	 * @param goods_season
	 * @param goods_jldw
	 * @param goods_cw
	 * @param goods_other
	 * @param onSuccessListener
	 */
	public static void doCommandGetGoodsListByParam(Context context,List<String> goods_brand,List<String> goods_color,List<String> goods_cs,List<String> goods_sort,List<String> goods_spec,List<String> goods_season,List<String> goods_jldw,List<String> goods_cw,List<String> goods_other,List<String> goods_label,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setGoodsBrand(goods_brand);
		pars.setGoodsColor(goods_color);
		pars.setGoodsCs(goods_cs);
		pars.setGoodsSort(goods_sort);
		pars.setGoodsSpec(goods_spec);
		pars.setGoodsSeason(goods_season);
		pars.setGoodsJldw(goods_jldw);
		pars.setGoodsCw(goods_cw);
		pars.setGoodsOther(goods_other);
		pars.setGoodsLabel(goods_label);
		doCommand(context, "GetGoodsListByParam", pars, onSuccessListener);
	}
	
	/**
	 * 获取商品
	 * @param context
	 * @param start_date
	 * @param end_date
	 * @param onSuccessListener
	 */
	public static void doCommandGetGoodsListByDate(Context context,String start_date,String end_date,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setStartDate(start_date);
		pars.setEndDate(end_date);
		doCommand(context, "GetGoodsListByDate", pars, onSuccessListener);
	}
	/**
	 * 添加商品
	 * @param context
	 * @param GoodsInfo
	 * @param goodsImage
	 * @param onSuccessListener
	 */
	public static void doCommandAddGoodsInfo(Context context,List<ProductDangAn> goodsInfo,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setGoodsInfo(goodsInfo);
//		pars.setGoodsImage(goodsImage);
		doCommand(context, "AddGoodsInfo", pars, onSuccessListener);
	}
	
	/**
	 * 获取款式
	 * @param context
	 * @param date_code
	 * @param description
	 * @param onSuccessListener
	 */
	public static void doCommandGetProdStyleList(Context context,String date_code,String description,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setDateCode(date_code);
		pars.setDescription(description);
		doCommand(context, "GetProdStyleList", pars, onSuccessListener);
	}
	
	/**
	 * 添加款式
	 * @param context
	 * @param dateCode
	 * @param description
	 * @param onSuccessListener
	 */
	public static void doCommandAddProdStyle(Context context,String dateCode,String description,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setDateCode(dateCode);
		pars.setDescription(description);
		doCommand(context, "AddProdStyle", pars, onSuccessListener);
	}
	
	/**
	 * 添加其他
	 * @param context
	 * @param description
	 * @param onSuccessListener
	 */
	public static void doCommandAddProdOther(Context context,String description,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setDescription(description);
		doCommand(context, "AddProdOther", pars, onSuccessListener);
	}
	
	/**
	 * 获取其他
	 * @param context
	 * @param onSuccessListener
	 */
	public static void doCommandGetProdOtherList(Context context,String enabled,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setEnabled(enabled);
		doCommand(context, "GetProdOtherList", pars, onSuccessListener);
	}
	
	/**
	 * 获取厂商
	 * @param context
	 * @param onSuccessListener
	 */
	public static void doCommandGetProdCsList(Context context,String enabled,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setEnabled(enabled);
		doCommand(context, "GetProdCsList", pars, onSuccessListener);
	}
	/**
	 * 添加厂商
	 * @param context
	 * @param seasonCode
	 * @param description
	 * @param onSuccessListener
	 */
	public static void doCommandAddProdCs(Context context,String description,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setDescription(description);
		doCommand(context, "AddProdCs", pars, onSuccessListener);
	}
	/**
	 * 获取计量单位
	 * @param context
	 * @param onSuccessListener
	 */
	public static void doCommandGetProdJLDWList(Context context,String enabled,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setEnabled(enabled);
		doCommand(context, "GetProdJLDWList", pars, onSuccessListener);
	}
	/**
	 * 添加计量单位
	 * @param context
	 * @param seasonCode
	 * @param description
	 * @param onSuccessListener
	 */
	public static void doCommandAddProdJLDW(Context context,String description,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setDescription(description);
		doCommand(context, "AddProdJLDW", pars, onSuccessListener);
	}
	/**
	 * 获取季节
	 * @param context
	 * @param onSuccessListener
	 */
	public static void doCommandGetProdSeasonList(Context context,String enabled,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setEnabled(enabled);
		doCommand(context, "GetProdSeasonList", pars, onSuccessListener);
	}
	/**
	 * 添加季节
	 * @param context
	 * @param seasonCode
	 * @param description
	 * @param onSuccessListener
	 */
	public static void doCommandAddProdSeason(Context context,String description,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setDescription(description);
		doCommand(context, "AddProdSeason", pars, onSuccessListener);
	}
	/**
	 * 获取仓位
	 * @param context
	 * @param onSuccessListener
	 */
	public static void doCommandGetProdCwList(Context context,String enabled,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setEnabled(enabled);
		doCommand(context, "GetProdCwList", pars, onSuccessListener);
	}
	
	/**
	 * 添加仓位
	 * @param context
	 * @param sortCode
	 * @param description
	 * @param onSuccessListener
	 */
	public static void doCommandAddProdCw(Context context,String description,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setDescription(description);
		doCommand(context, "AddProdCw", pars, onSuccessListener);
	}
	/**
	 * 获取类别
	 * @param context
	 * @param onSuccessListener
	 */
	public static void doCommandGetProdSortList(Context context,String enabled,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setEnabled(enabled);
		doCommand(context, "GetProdSortList", pars, onSuccessListener);
	}
	
	/**
	 * 添加类别
	 * @param context
	 * @param sortCode
	 * @param description
	 * @param onSuccessListener
	 */
	public static void doCommandAddProdSort(Context context,String description,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setDescription(description);
		doCommand(context, "AddProdSort", pars, onSuccessListener);
	}
	/**
	 * 获取品牌
	 * @param context
	 * @param onSuccessListener
	 */
	public static void doCommandGetProdBrandList(Context context,String enabled,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setEnabled(enabled);
		doCommand(context, "GetProdBrandList", pars, onSuccessListener);
	}
	/**
	 * 添加品牌
	 * @param context
	 * @param specCode
	 * @param description
	 * @param onSuccessListener
	 */
	public static void doCommandAddProdBrand(Context context,String description,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setDescription(description);
		doCommand(context, "AddProdBrand", pars, onSuccessListener);
	}
	
	/**
	 * 获取尺码
	 * @param context
	 * @param enabled 0 表示失效 1 表示生效，null表示全部
	 * @param onSuccessListener
	 */
	public static void doCommandGetProdSpecList(Context context,String enabled,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setEnabled(enabled);
		doCommand(context, "GetProdSpecList", pars, onSuccessListener);
	}
	/**
	 * 添加尺码
	 * @param context
	 * @param specCode
	 * @param description
	 * @param onSuccessListener
	 */
	public static void doCommandAddProdSpec(Context context,String groupCode,String description,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setGroupCode(groupCode);
		pars.setDescription(description);
		doCommand(context, "AddProdSpec", pars, onSuccessListener);
	}

	/**
	 * 添加尺码分组
	 * @param context
	 * @param description
	 * @param onSuccessListener
	 */
	public static void doCommandAddSpecGroup(Context context,String description,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setDescription(description);
		doCommand(context, "AddSpecGroup", pars, onSuccessListener);
	}
	
	/**
	 * 获取尺码分组列表
	 * @param context
	 * @param enabled
	 * @param onSuccessListener
	 */
	public static void doCommandGetSpecGroupList(Context context,String enabled,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setEnabled(enabled);
		doCommand(context, "GetSpecGroupList", pars, onSuccessListener);
	}
	
	/**
	 * 获取颜色
	 * @param context
	 * @param enabled 0 表示失效 1 表示生效，null表示全部
	 * @param onSuccessListener
	 */
	public static void doCommandGetProdColorList(Context context,String enabled,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setEnabled(enabled);
		doCommand(context, "GetProdColorList", pars, onSuccessListener);
	}
	/**
	 * 添加颜色
	 * @param context
	 * @param groupCode
	 * @param description
	 * @param onSuccessListener
	 */
	public static void doCommandAddProdColor(Context context,String groupCode,String description,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setGroupCode(groupCode);
		pars.setDescription(description);
		doCommand(context, "AddProdColor", pars, onSuccessListener);
	}
	
	/**
	 * 保存颜色分组信息
	 * @param context
	 * @param description
	 * @param onSuccessListener
	 */
	public static void doCommandAddColorGroup(Context context,String description,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setDescription(description);
		doCommand(context, "AddColorGroup", pars, onSuccessListener);
	}
	
	/**
	 * 更新颜色分组信息
	 * @param context
	 * @param groupInfos
	 * @param colorInfos
	 * @param onSuccessListener
	 */
	public static void doCommandUpdateBackColorByGroup(Context context,List<ProdColorGroup> groupInfos,List<ProdColor> colorInfos,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setGroupInfos(groupInfos);
		pars.setColorInfos(colorInfos);
		doCommand(context, "UpdateBackColorByGroup", pars, onSuccessListener);
	}
	
	/**
	 * 获取颜色分组列表
	 * @param context
	 * @param enabled 0 表示失效 1 表示生效，null表示全部
	 * @param onSuccessListener
	 */
	public static void doCommandGetColorGroupList(Context context,String enabled,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setEnabled(enabled);
		doCommand(context, "GetColorGroupList", pars, onSuccessListener);
	}
	/**
	 * 更新品牌状态
	 * @param context
	 * @param brandCode
	 * @param enabled 0 表示失效 1 表示生效
	 * @param onSuccessListener
	 */
	public static void doCommandUpdateProdBrandEnabled(Context context,String brandCode,String enabled,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setEnabled(enabled);
		pars.setBrandCode(brandCode);
		doCommand(context, "UpdateProdBrandEnabled", pars, onSuccessListener);
	}
	public static void doCommandUpdateProdColorEnabled(Context context,String colorCode,String enabled,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setEnabled(enabled);
		pars.setColorCode(colorCode);
		doCommand(context, "UpdateProdColorEnabled", pars, onSuccessListener);
	}
	public static void doCommandUpdateProdSortEnabled(Context context,String sortCode,String enabled,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setEnabled(enabled);
		pars.setSortCode(sortCode);
		doCommand(context, "UpdateProdSortEnabled", pars, onSuccessListener);
	}
	public static void doCommandUpdateProdSpecEnabled(Context context,String specCode,String enabled,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setEnabled(enabled);
		pars.setSpecCode(specCode);
		doCommand(context, "UpdateProdSpecEnabled", pars, onSuccessListener);
	}
	public static void doCommandUpdateProdSeasonEnabled(Context context,String seasonCode,String enabled,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setEnabled(enabled);
		pars.setSeasonCode(seasonCode);
		doCommand(context, "UpdateProdSeasonEnabled", pars, onSuccessListener);
	}
	public static void doCommandUpdateProdJLDWEnabled(Context context,String JldwCode,String enabled,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setEnabled(enabled);
		pars.setJldwCode(JldwCode);
		doCommand(context, "UpdateProdJLDWEnabled", pars, onSuccessListener);
	}
	public static void doCommandUpdateProdCwEnabled(Context context,String CwCode,String enabled,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setEnabled(enabled);
		pars.setCwCode(CwCode);
		doCommand(context, "UpdateProdCwEnabled", pars, onSuccessListener);
	}
	public static void doCommandUpdateProdCsEnabled(Context context,String CsCode,String enabled,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setEnabled(enabled);
		pars.setCsCode(CsCode);
		doCommand(context, "UpdateProdCsEnabled", pars, onSuccessListener);
	}
	public static void doCommandUpdateProdOtherEnabled(Context context,String otherCode,String enabled,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setEnabled(enabled);
		pars.setOtherCode(otherCode);
		doCommand(context, "UpdateProdOtherEnabled", pars, onSuccessListener);
	}
	/**
	 * 更新款状态
	 * @param context
	 * @param styleCode
	 * @param dateCode
	 * @param enabled
	 * @param onSuccessListener
	 */
	public static void doCommandUpdateProdStyleEnabled(Context context,String styleCode,String dateCode,String enabled,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setEnabled(enabled);
		pars.setStyleCode(styleCode);
		pars.setDateCode(dateCode);
		doCommand(context, "UpdateProdStyleEnabled", pars, onSuccessListener);
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 销售数据单号查询
	 * @param context
	 * @param shopCode
	 * @param saleNo
	 * @param onSuccessListener
	 */
	public static void doCommandGetBillSaleByNum(Context context, String shopCode, String saleNo,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShopCode(shopCode);
		pars.setSaleNo(saleNo);
		doCommand(context, "GetBillSaleByNum", pars, onSuccessListener);
	}
	/**
	 * 销售数据上传-
	 * @param context
	 * @param billSale
	 * @param billsaleDetailList
	 * @param payList
	 * @param bankList
	 * @param onSuccessListener
	 */
	public static void doCommandSaveBillSale(Context context, JvbillsaleInfo billSale, List detailList,List<JvbillsalepayInfo> payList,List<JvbillsalebankInfo> bankList,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setBillSale(billSale);
		pars.setDetailList(detailList);
		pars.setPayList(payList);
		pars.setBankList(bankList);
		doCommand(context, "SaveBillSale", pars, onSuccessListener);
	}
	/**
	 *  销售数据日期查询
	 * @param context
	 * @param shopCode
	 * @param startDate
	 * @param endDate
	 * @param startNum
	 * @param size
	 * @param onSuccessListener
	 */
	public static void doCommandGetBillSaleByDate(Context context, String shopCode, String startDate, String endDate,int startNum,int size,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShopCode(shopCode);
		pars.setStartDate(startDate);
		pars.setEndDate(endDate);
		pars.setStartNum(startNum);
		pars.setSize(size);
		doCommand(context, "GetBillSaleByDate", pars, onSuccessListener);
	}
	/**
	 * 获取店铺空间详细信息
	 * @param context
	 * @param clerkSpaceID
	 * @param onSuccessListener
	 */
	public static void doCommandGetClerkSpaceDetailInfo(Context context, String clerkSpaceID, String Operator,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setClerkSpaceID(clerkSpaceID);
		pars.setOperator(Operator);
		doCommand(context, "GetClerkSpaceDetailInfo", pars, onSuccessListener);
	}
	/**
	 * 店铺空间点赞
	 * @param context
	 * @param clerkSpaceID
	 * @param Operator
	 * @param onSuccessListener
	 */
	public static void doCommandClerkSpacePraise(Context context, String clerkSpaceID, String Operator,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setClerkSpaceID(clerkSpaceID);
		pars.setOperator(Operator);
		doCommand(context, "ClerkSpacePraise", pars, onSuccessListener);
	}
	/**
	 * 店铺空间评论
	 * @param context
	 * @param commentInfo
	 * @param onSuccessListener
	 */
	public static void doCommandClerkSpaceComment(Context context, JvclerkspacecommentInfo commentInfo,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCommentInfo(commentInfo);
		doCommand(context, "ClerkSpaceComment", pars, onSuccessListener);
	}
	/**
	 * 获取万象优图服务器参数接口
	 * 
	 * @param context
	 * @param onSuccessListener
	 */
	public static void doCommandGetWXYTImageServerInfo(Context context, String NameSpace,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setNameSpace(NameSpace);
		doCommand(context, "GetWXYTImageServerInfo", pars, onSuccessListener);
		
	}
	/**
	 * 保存店铺空间信息
	 * @param context
	 * @param ClerkspaceInfo
	 * @param onSuccessListener
	 */
	public static void doCommandClerkSpaceSave(Context context,JvclerkspaceInfo ClerkspaceInfo,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setClerkspaceInfo(ClerkspaceInfo);
		doCommand(context, "ClerkSpaceSave", pars, onSuccessListener);
	}
	/**
	 * 获取店铺空间信息
	 * @param context
	 * @param labelid
	 * @param keyWords
	 * @param startDate
	 * @param endDate
	 * @param order
	 * @param formatTag
	 * @param startNum
	 * @param size
	 * @param onSuccessListener
	 */
	public static void doCommandGetClerkSpaceList(Context context,String labelid,String keyWords,String startDate,String endDate,String order,String formatTag,int startNum,int size,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setLabelid(labelid);
		pars.setKeyWords(keyWords);
		pars.setStartDate(startDate);
		pars.setEndDate(endDate);
		pars.setOrder(order);
		pars.setFormatTag(formatTag);
		pars.setStartNum(startNum);
		pars.setSize(size);
		doCommand(context, "GetClerkSpaceList", pars, onSuccessListener);
	}
	/**
	 * 获取店铺空间风格标签信息
	 * @param context
	 * @param shop_id
	 * @param onSuccessListener
	 */
	public static void doCommandGetClerkSpaceStyleLabel(Context context,String shop_id,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShop_id(shop_id);
		doCommand(context, "GetClerkSpaceStyleLabel", pars, onSuccessListener);
	}
	/**
	 * 简单盘点数据上传
	 * @param context
	 * @param shop_code
	 * @param emailDesc
	 * @param jvSimpleCheckInfos
	 * @param receiverList
	 * @param onSuccessListener
	 */
	public static void doCommandSendSimpleCheckData(Context context,String shop_code,String emailDesc,List<JvSimpleCheckInfo> jvSimpleCheckInfos,List<String> receiverList,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShop_code(shop_code);
		pars.setEmailDesc(emailDesc);
		pars.setJvSimpleCheckInfos(jvSimpleCheckInfos);
		pars.setReceiverList(receiverList);
		doCommand(context, "SendSimpleCheckData", pars, onSuccessListener);
	}
	/**
	 * 查询邮件列表接口
	 * @param context
	 * @param shop_id
	 * @param onSuccessListener
	 */
	public static void doCommandGetEmailInfo(Context context,String shop_id,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShop_id(shop_id);
		doCommand(context, "GetEmailInfo", pars, onSuccessListener);
	}
	/**
	 * 创建邮件接口
	 * @param context
	 * @param shop_Id
	 * @param user
	 * @param detailList
	 * @param onSuccessListener
	 */
	public static void doCommandSetEmailInfo(Context context,String shop_id,String user,List detailList,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShop_id(shop_id);
		pars.setUser(user);
		pars.setDetailList(detailList);
		doCommand(context, "SetEmailInfo", pars, onSuccessListener);
	}
	/**
	 * 读取会员信息
	 * @param context
	 * @param shopCode
	 * @param vipCode
	 * @param mobile
	 * @param onSuccessListener
	 */
	public static void doCommandGetVipInfo(Context context,String shopCode,String vipCode,String mobile,String name,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShopCode(shopCode);
		pars.setVipCode(vipCode);
		pars.setMobile(mobile);
		pars.setName(name);
		doCommand(context, "GetVipInfo", pars, onSuccessListener);
	}
	
	/**
	 * 注册会员
	 * @param context
	 * @param shopCode
	 * @param vipCode
	 * @param mobile
	 * @param sex
	 * @param name
	 * @param birth
	 * @param address
	 * @param createuser
	 * @param onSuccessListener
	 */
	public static void doCommandCreateVipInfo(Context context,String shopCode,String vipCode,String mobile,String sex,String name,String birth,String address,String createuser,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShopCode(shopCode);
		pars.setVipCode(vipCode);
		pars.setMobile(mobile);
		pars.setSex(sex);
		pars.setName(name);
		pars.setBirth(birth);
		pars.setAddress(address);
		pars.setCreateuser(createuser);
		doCommand(context, "CreateVipInfo", pars, onSuccessListener);
	}
	
	/**
	 * 创建打印机接口
	 * @param context
	 * @param shopId
	 * @param printerip
	 * @param description
	 * @param type
	 * @param creator
	 * @param onSuccessListener
	 */
	public static void doCommandCreatePrinterInfo(Context context,String shopId,String printerip,String description,String type,String creator,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShopId(shopId);
		pars.setPrinterip(printerip);
		pars.setDescription(description);
		pars.setType(type);
		pars.setCreator(creator);
		doCommand(context, "CreatePrinterInfo", pars, onSuccessListener);
	}
	/**
	 * 修改打印机接口
	 * @param context
	 * @param id
	 * @param shopId
	 * @param printerip
	 * @param description
	 * @param type
	 * @param creator
	 * @param onSuccessListener
	 */
	public static void doCommandUpdatePrinterInfo(Context context,int id,String shopId,String printerip,String description,String type,String creator,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setId(id);
		pars.setShopId(shopId);
		pars.setPrinterip(printerip);
		pars.setDescription(description);
		pars.setType(type);
		pars.setCreator(creator);
		doCommand(context, "UpdatePrinterInfo", pars, onSuccessListener);
	}
	
	/**
	 * 查询打印机接口
	 * @param context
	 * @param shopId
	 * @param onSuccessListener
	 */
	public static void doCommandGetPrinterInfo(Context context,String shopId,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShopId(shopId);
		doCommand(context, "GetPrinterInfo", pars, onSuccessListener);
	}
	/**
	 * 删除打印机
	 * @param context
	 * @param id
	 * @param creator
	 * @param onSuccessListener
	 */
	public static void doCommandDeletePrinterInfo(Context context,int id,String creator,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setId(id);
		pars.setCreator(creator);
		doCommand(context, "DeletePrinterInfo", pars, onSuccessListener);
	}

	public static void doCommandGetDressDataByNum(Context context,String shop_code,String doc_num,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShop_code(shop_code);
		pars.setDoc_num(doc_num);
		doCommand(context, "GetDressDataByNum", pars, onSuccessListener);
	}
	
	public static void doCommandSendFittingInfo(Context context,String shop_code,String starttime,String endtime,String createuser,List<String> receiverList, Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShopCode(shop_code);
		pars.setStarttime(starttime);
		pars.setEndtime(endtime);
		pars.setCreateuser(createuser);
		pars.setReceiverList(receiverList);
		doCommand(context, "SendFittingInfo", pars, onSuccessListener);
	}
	public static void doCommandGetDressDataByDate(Context context,String shop_code,String startDate,String endDate,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShop_code(shop_code);
		pars.setStartDate(startDate);
		pars.setEndDate(endDate);
		doCommand(context, "GetDressDataByDate", pars, onSuccessListener);
	}
	public static void doCommandUploadDressData(Context context,String Shop_Id,String Creator,List<SimpleGoods> DressDtlData,List<Customer> DressCustData,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShop_Id(Shop_Id);
		pars.setCreator(Creator);
		pars.setDressDtlData(DressDtlData);
		pars.setDressCustData(DressCustData);
		doCommand(context, "UploadDressData", pars, onSuccessListener);
	}
	
	public static void doCommandGetCustGoodsList(Context context,String shop_code,List<String> goodsCode,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShop_code(shop_code);
		pars.setGoodsCode(goodsCode);
		doCommand(context, "GetCustGoodsList", pars, onSuccessListener);
	}
	public static void doCommandGetCustBoxInfoByCode(Context context,String shop_code,String box_code,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShop_code(shop_code);
		pars.setBox_code(box_code);
		doCommand(context, "GetCustBoxInfoByCode", pars, onSuccessListener);
	}
	public static void doCommandGetGoodsList(Context context,String shop_code,List<String> GoodsCode,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShop_code(shop_code);
		pars.setGoodsCode(GoodsCode);
		doCommand(context, "GetGoodsList", pars, onSuccessListener);
	}
	public static void doCommandAddUserInfo(Context context,String shop_code,String user_code,String user_name,String user_pwd,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShop_code(shop_code);
		pars.setUser_code(user_code);
		pars.setUser_name(user_name);
		pars.setUser_pwd(user_pwd);
		doCommand(context, "AddUserInfo", pars, onSuccessListener);
	}
	public static void doCommandGetGoodsInfo(Context context,String shop_code,String goods_sn,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setCompanyCode(getCompanyCode());
		pars.setGoods_sn(goods_sn);
		doCommand(context, "GetGoodsInfo", pars, onSuccessListener);
	}
	public static void doCommandGetShopUserList(Context context,String shop_code,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShop_code(shop_code);
		doCommand(context, "GetShopUserList", pars, onSuccessListener);
	}
	public static void doCommandDeleteLocAreaInfo(Context context,int id,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setId(id);
		doCommand(context, "DeleteLocAreaInfo", pars, onSuccessListener);
	}
	public static void doCommandUploadLocAreaInfo(Context context,List<LocAreaInfo> LocAreaList,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setLocAreaList(LocAreaList);
		doCommand(context, "UploadLocAreaInfo", pars, onSuccessListener);
	}
	public static void doCommandGetLocInfoList(Context context,String shop_code,String loc_id,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShop_code(shop_code);
		pars.setLoc_id(loc_id);
		doCommand(context, "GetLocInfoList", pars, onSuccessListener);
	}
	public static void doCommandUploadProdCheckData(Context context,String shop_code,List<String> receiverList,List<String> docNumList,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShop_code(shop_code);
		pars.setReceiverList(receiverList);
		pars.setDocNumList(docNumList);
		doCommand(context, "SendProdCheckData", pars, onSuccessListener);
	}
	
	public static void doCommandUploadCustProdCheckData(
			Context context,String shop_code,String creator ,List<ProdCheckDataInfo> checkDataList,
			boolean isUploadWithoutCheck,int checkPriority,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShop_code(shop_code);
		pars.setCreator(creator);
		pars.setCheckDataList(checkDataList);
		pars.setIsUploadWithoutCheck(isUploadWithoutCheck);
		pars.setCheckPriority(checkPriority);
		doCommand(context, "UploadCustProdCheckData", pars, onSuccessListener);
	}
	public static void doCommandGetCustomGoodsInfo(
			Context context,String shop_code,String goods_sn ,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShop_code(shop_code);
		pars.setGoods_sn(goods_sn);
		doCommand(context, "GetCustomGoodsInfo", pars, onSuccessListener);
	}
	public static void doCommandUploadProdCheckData(Context context,String shop_code,String creator ,List<ProdCheckDataInfo> checkDataList,
			boolean isUploadWithoutCheck,int checkPriority,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShop_code(shop_code);
		pars.setCreator(creator);
//		pars.setShelf_code(shelf_code);
		pars.setCheckDataList(checkDataList);
		pars.setIsUploadWithoutCheck(isUploadWithoutCheck);
		pars.setCheckPriority(checkPriority);
		doCommand(context, "UploadProdCheckData", pars, onSuccessListener);
	}
	public static void doCommandGetProdCheckMarkList(Context context, 
			Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		doCommand(context, "GetProdCheckMarkList", pars, onSuccessListener);
	}
	public static void doCommandGetProdCheckDtlData(Context context, String shop_code,
			List<String> DocNumList,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShop_code(shop_code);
		pars.setDocNumList(DocNumList);
		doCommand(context, "GetProdCheckDtlData", pars, onSuccessListener);
	}
	public static void doCommandGetProdCheckData(Context context, 
			String shop_code,String doc_num, Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShop_code(shop_code);
		pars.setDoc_num(doc_num);
		doCommand(context, "GetProdCheckData", pars, onSuccessListener);
	}
	public static void doCommandDeleteCheckData(Context context, 
			String DocNum, Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setDocNum(DocNum);
		doCommand(context, "DeleteCheckData", pars, onSuccessListener);
	}
	public static void doCommandGetProdCheckDataHeadList(Context context, 
			String shop_code, String loc_id,String area_code,String shelf_code,String startDate,String endDate,
			String creator,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShop_code(shop_code);
		pars.setLoc_id(loc_id);
		pars.setArea_code(area_code);
		pars.setShelf_code(shelf_code);
		pars.setStartDate(startDate);
		pars.setEndDate(endDate);
		pars.setCreator(creator);
		doCommand(context, "GetProdCheckDataHeadList", pars, onSuccessListener);
	}
	public static void doCommandUpdateProdPrecheckData(Context context, String shop_id, String shelf_code,int total_qty,
			String create_user,int id,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShop_id(shop_id);
		pars.setShelf_code(shelf_code);
		pars.setTotal_qty(total_qty);
		pars.setCreate_user(create_user);
		pars.setId(id);
		doCommand(context, "UpdateProdPrecheckData", pars, onSuccessListener);
	}
	public static void doCommandDeletePrecheckData(Context context, String shop_id, String shelf_code,
			Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShop_id(shop_id);
		pars.setShelf_code(shelf_code);
		doCommand(context, "DeletePrecheckData", pars, onSuccessListener);
	}
	public static void doCommandUploadProdPreCheckData(Context context, String shop_code, String create_user,
			List<LocQty> prodPreCheck,Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShop_code(shop_code);
		pars.setCreate_user(create_user);
		pars.setProdPreCheck(prodPreCheck);
		doCommand(context, "UploadProdPreCheckData", pars, onSuccessListener);
	}
	public static void doCommandGetProdPreCheckData(Context context, String shop_id, String shelf_code,
			String start_date, String end_date, String creator, int start, int size,
			Listener<JSONObject> onSuccessListener,boolean showProgress) {
		Pars pars = new Pars();
		pars.setShop_id(shop_id);
		pars.setShelf_code(shelf_code);
		pars.setStart_date(start_date);
		pars.setEnd_date(end_date);
		pars.setCreator(creator);
		pars.setStart(start);
		pars.setSize(size);
		doCommand(context, "GetProdPreCheckData", pars, onSuccessListener,showProgress);
	}

	/**
	 * 登录接口
	 * 
	 * @param context
	 * @param shop_code
	 * @param user_code
	 * @param user_pwd
	 * @param imei
	 * @param role_type
	 * @param device_type
	 * @param onSuccessListener
	 */
	public static void doCommandLoginCheckDevice(Context context, String shop_code, String user_code, String user_pwd,
			String imei, String role_type, String device_type, Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setShop_code(shop_code);
		pars.setUser_code(user_code);
		pars.setUser_pwd(user_pwd);
		pars.setImei(imei);
		pars.setDevice_type(device_type);
		pars.setRole_type(role_type);
		doCommand(context, "LoginCheckDevice", pars, onSuccessListener);
	}

	/**
	 * 检查版本接口
	 * 
	 * @param context
	 * @param device_type
	 * @param version_id
	 * @param onSuccessListener
	 */
	public static void doCommandCheckVersion(Context context, String device_type, String version_id,
			Listener<JSONObject> onSuccessListener) {
		Pars pars = new Pars();
		pars.setDevice_type(device_type);
		pars.setVersion_id(version_id);
		doCommand(context, "CheckVersion", pars, onSuccessListener);
	}
	
	//////////////////////////////////////////////
	// base
	//////////////////////////////////////////////
	/**
	 * 统一调用VolleyHelper执行连接命令，有loading
	 * 
	 * @param context
	 * @param map
	 * @param onSuccessListener
	 */
//	private static void doCommand(Context context, Map<String, String> map, Listener<JSONObject> onSuccessListener) {
//		VolleyHelper.execPostRequest(context, BASE_URL, map, onSuccessListener);
//	}

	/**
	 * 统一调用VolleyHelper执行连接命令，可控制loading有无
	 * 
	 * @param context
	 * @param map
	 * @param onSuccessListener
	 * @param showProgress
	 */
	private static void doCommand(Context context, Map<String, String> map, Listener<JSONObject> onSuccessListener,
			boolean showProgress) {
		VolleyHelper.execPostRequest(context, null,BASE_URL, map, onSuccessListener, showProgress);
	}

	/**
	 * 将对象序列化为JSON字符串
	 * 
	 * @param object
	 * @return JSON字符串
	 */
	private static String toJSon(Object object) {
		return new Gson().toJson(object);// 使用Gson
	}

	public static void doCommand(Context context, String actionName, Pars pars,
			Listener<JSONObject> onSuccessListener,boolean showProgress) {
		Message message = new Message();
		message.setPars(pars);
		message.setActionName(actionName);
		Map<String, String> map = new HashMap<String, String>();
		map.put("inData", toJSon(message));
		if(App.isLog){
			Log.i("tag", "inData===="+toJSon(message));
		}
		doCommand(context, map, onSuccessListener,showProgress);
	}

	public static void doCommand(Context context, String actionName, Pars pars,
			Listener<JSONObject> onSuccessListener) {
		doCommand(context, actionName, pars, onSuccessListener, true);
	}

}
