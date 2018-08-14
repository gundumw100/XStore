package com.app.xstore.shangpindangan;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.net.Commands;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.base.util.D;
import com.base.util.luban.Luban;
import com.qq.cloud.PicCloud;
import com.qq.cloud.UploadResult;
import com.squareup.picasso.Picasso;
import com.widget.flowlayout.FlowLayout;
import com.widget.flowlayout.TagAdapter;
import com.widget.flowlayout.TagFlowLayout;
import com.widget.flowlayout.TagView;
import com.widget.imagepicker.ImageConfig;
import com.widget.imagepicker.ImageSelector;
import com.widget.imagepicker.ImageSelectorActivity;
import com.widget.imagepicker.PicassoLoader;
import com.widget.photoView.PhotoView;

/**
 * 商品详情
 * @author pythoner
 * 
 */
public class ProductDetailActivity extends BaseActivity implements View.OnClickListener{

	private int curPosition;//主图位置
	private ViewPager viewPager;
	private ArrayList<View> listViews = new ArrayList<View>();
	private List<ProdColorImage> styleImageList=new ArrayList<ProdColorImage>();
	private TagFlowLayout flowLayout_img;
	private MyPagerAdapter myPagerAdapter;
	private CheckBox btn_favorites;
	private String goods_sn;
	private List<ProductDangAn> beans=new ArrayList<ProductDangAn>();
	private TagFlowLayout flowLayout_color,flowLayout_size;
	private TextView tv_name,tv_ls_price,tv_sn;
	private ProductDangAn curBean;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shangpindangan_product_detail);
		context = this;
		initActionBar("商品详情", null, null);
		goods_sn=getIntent().getStringExtra("goods_sn");
		if(isEmpty(goods_sn)||goods_sn.length()<6){
			showToast("编码有误");
			return;
		}
		initViews();
		doCommandGetGoodsListBySKU();
		//请求主图数据
		doCommandGetGoodsStyleImageList();
		//判断该商品是否收藏了
		doCommandGetCollectGoodsBySKU(goods_sn);
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		viewPager=(ViewPager)findViewById(R.id.viewPager);
		
		flowLayout_img=(TagFlowLayout)findViewById(R.id.flowLayout_img);
		flowLayout_img.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
			
			@Override
			public boolean onTagClick(FlowLayout parent, View view, int position) {
				// TODO Auto-generated method stub
				curPosition=position;
				viewPager.setCurrentItem(position);
				return true;
			}
		});
		flowLayout_img.setmOnTagLongClickListener(new TagFlowLayout.OnTagLongClickListener() {
			
			@Override
			public boolean onTagLongClick(FlowLayout parent, View view, int position) {
				// TODO Auto-generated method stub
				curPosition=position;
				showDialogItems2();
				return true;
			}
		});

		tv_name=$(R.id.tv_name);
		tv_ls_price=$(R.id.tv_ls_price);
		tv_sn=$(R.id.tv_sn);
		final TextView btn_save=$(R.id.btn_save);
		final TextView tv_maidian=$(R.id.tv_maidian);
		final EditText et_maidian=$(R.id.et_maidian);
		et_maidian.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence text, int start, int before, int count) {
				// TODO Auto-generated method stub
				int length=text.length();
				btn_save.setVisibility(length==0?View.GONE:View.VISIBLE);
				tv_maidian.setText(length+"/100");
			}
			
			@Override
			public void beforeTextChanged(CharSequence text, int start, int count,int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable edit) {
				// TODO Auto-generated method stub
				
			}
		});
		btn_save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String text=et_maidian.getText().toString().trim();
				if(isEmpty(text)){
					doShake(context, et_maidian);
					return;
				}
				
			}
		});
		
		flowLayout_color=(TagFlowLayout)findViewById(R.id.flowLayout_color);
		flowLayout_size=(TagFlowLayout)findViewById(R.id.flowLayout_size);
		flowLayout_color.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
			
			@Override
			public boolean onTagClick(FlowLayout parent, View view, int position) {
				// TODO Auto-generated method stub
				if(!isEmptyList(flowLayout_size.getCheckedItems())){
					int colorIndex=-1;
					String tag=(String)(((TagView)view).getChildAt(0).getTag());
					ProductDangAn size=(ProductDangAn)flowLayout_size.getCheckedItems().get(0);
					for(int i=0;i<beans.size();i++){
						ProductDangAn bean=beans.get(i);
						if(bean.getGoods_color().equals(tag)&&size.getGoods_spec().equals(bean.getGoods_spec())){
							colorIndex=i;
							break;
						}
					}
					
					if(colorIndex==-1){
						updateHeadViews(null);
					}else{
						ProductDangAn bean=beans.get(colorIndex);
						updateHeadViews(bean);
					}
				}
				if(isEmptyList(flowLayout_size.getCheckedItems())||isEmptyList(flowLayout_color.getCheckedItems())){
					updateHeadViews(null);
				}
				return true;
			}
		});
		flowLayout_color.setmOnTagLongClickListener(new TagFlowLayout.OnTagLongClickListener() {
			
			@Override
			public boolean onTagLongClick(FlowLayout parent, View view, int position) {
				// TODO Auto-generated method stub
				curColorPosition=position;
				showDialogItems();
				return true;
			}
		});
		flowLayout_size.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
			
			@Override
			public boolean onTagClick(FlowLayout parent, View view, int position) {
				// TODO Auto-generated method stub
				if(!isEmptyList(flowLayout_color.getCheckedItems())){
					int sizeIndex=-1;
					String tag=(String)(((TagView)view).getChildAt(0).getTag());
					ProductDangAn color=(ProductDangAn)flowLayout_color.getCheckedItems().get(0);
					for(int i=0;i<beans.size();i++){
						ProductDangAn bean=beans.get(i);
						if(bean.getGoods_spec().equals(tag)&&color.getGoods_color().equals(bean.getGoods_color())){
							sizeIndex=i;
							break;
						}
					}
					
					if(sizeIndex==-1){
						updateHeadViews(null);
					}else{
						ProductDangAn bean=beans.get(sizeIndex);
						updateHeadViews(bean);
					}
				}
				if(isEmptyList(flowLayout_size.getCheckedItems())||isEmptyList(flowLayout_color.getCheckedItems())){
					updateHeadViews(null);
				}
				return true;
			}
		});
		
		btn_favorites=$(R.id.btn_favorites);
		btn_favorites.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(btn_favorites.isChecked()){
					doCommandAddCollectGoods(goods_sn);
				}else{
					doCommandDelCollectGoods(goods_sn);
				}
			}
		});
		
		$(R.id.btn_addToShoppingcart).setOnClickListener(this);
		$(R.id.btn_print).setOnClickListener(this);
		
	}
	
	private void doCommandAddCollectGoods(String goods_sn){
		Commands.doCommandAddCollectGoods(context, goods_sn, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					showToast("添加收藏");
				}
			}
		});
	}
	
	private void doCommandDelCollectGoods(String goods_sn){
		Commands.doCommandDelCollectGoods(context, goods_sn, new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					showToast("取消收藏");
				}
			}
		});
	}
	
	private int curColorPosition=-1;//选中的颜色的位置
	private List<ProductDangAn> colorsList=new ArrayList<ProductDangAn>();//商品详情颜色值列表
	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
		if(isEmptyList(beans)){
			return;
		}
		int curIndex=-1;
		for(int i=0;i<beans.size();i++){
			if(goods_sn.equals(beans.get(i).getGoods_sn())){
				curIndex=i;
				break;
			}
		}
		if(curIndex==-1){
			updateHeadViews(null);
		}else{
			ProductDangAn bean=beans.get(curIndex);
			updateHeadViews(bean);
			
//			//保存最近浏览
//			saveProductDangAnRecentlyBrowse(bean);
		}
		
//		//主图
//		HashMap<String,ProductDangAn> imgMap=new HashMap<String,ProductDangAn>();
//		imgList.clear();
//		for(ProductDangAn item:beans){//debug
//			String goodsImg=item.getGoods_img();
//			if(!isEmpty(goodsImg)){
//				String[] imgs=goodsImg.split(";");
//				
//				for(String img:imgs){
//					if(imgMap.containsKey(img)){
//						
//					}else{
//						imgMap.put(img, item);
//						imgList.add(img);
//					}
//				}
//				
//			}
//		}
//		flowLayout_img.setAdapter(new TagAdapter<String>(imgList){
//			@Override
//			public View getView(FlowLayout parent, int position, String item){
//				ImageView iv = (ImageView) LayoutInflater.from(context).inflate(R.layout.item_image_for_product_detail,flowLayout_img, false);
//				loadImageByPicasso(item, iv);
//				iv.setTag(item);
//				return iv;
//			}
//		});
//		
//		//
//		for(int i=0;i<imgList.size();i++){
//			View view=LayoutInflater.from(context).inflate(R.layout.item_product_img, null);
//			listViews.add(view);
//		}
//		if(myPagerAdapter==null){
//			myPagerAdapter=new MyPagerAdapter(listViews);
//			viewPager.setAdapter(myPagerAdapter);
//		}else{
//			myPagerAdapter.setViews(listViews);
//			myPagerAdapter.notifyDataSetChanged();
//		}
//		viewPager.setCurrentItem(curPosition);
		
		//色图,需要去掉重复的
		HashMap<String,ProductDangAn> colorsMap=new HashMap<String,ProductDangAn>();
		colorsList.clear();
		for(ProductDangAn item:beans){
			if(colorsMap.containsKey(item.getGoods_color())){
				
			}else{
				colorsMap.put(item.getGoods_color(), item);
				colorsList.add(item);
			}
		}
//		
//		flowLayout_color.setAdapter(new TagAdapter<ProductDangAn>(colorsList){
//			@Override
//			public View getView(FlowLayout parent, int position, ProductDangAn item){
//				View root = LayoutInflater.from(context).inflate(R.layout.item_image_text,flowLayout_color, false);
//				root.setTag(item.getGoods_color());
//				
//				ImageView item_0=(ImageView)root.findViewById(R.id.item_0);
//				TextView item_1=(TextView)root.findViewById(R.id.item_1);
//				item_1.setText("00".equals(item.getGoods_color())?"均色":item.getGoods_color_desc());
//				loadImageByPicasso(item.getGoods_color_image(), item_0);
//				return root;
//			}
//		});
//		
//		if(goods_sn.length()>=10){//初始化预选项
//			int curColorIndex=0;
//			int i=0;
//			String sub=goods_sn.substring(6, 8);//截取颜色
//			for(ProductDangAn item:colorsList){
//				if(sub.equals(item.getGoods_color())){
//					curColorIndex=i;
//					break;
//				}
//				i++;
//			}
//			flowLayout_color.setCheckedAt(curColorIndex);
//		}
		
		//尺码
		HashMap<String,ProductDangAn> sizesMap=new HashMap<String,ProductDangAn>();
		List<ProductDangAn> sizesList=new ArrayList<ProductDangAn>();
		for(ProductDangAn item:beans){
			if(sizesMap.containsKey(item.getGoods_spec())){
				ProductDangAn bean=sizesMap.get(item.getGoods_spec());
				if(isEmpty(bean.getGoods_color_image())){//如果当前没有颜色图片，则用后面的替代
					bean.setGoods_color_image(item.getGoods_color_image());
				}
			}else{
				sizesMap.put(item.getGoods_spec(), item);
				sizesList.add(item);
			}
		}
		flowLayout_size.setAdapter(new TagAdapter<ProductDangAn>(sizesList){
			@Override
			public View getView(FlowLayout parent, int position, ProductDangAn item){
				TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_text,flowLayout_size, false);
				tv.setText("00".equals(item.getGoods_spec())?"均码":item.getGoods_spec_desc());
				tv.setTag(item.getGoods_spec());
				return tv;
			}
		});
		
		if(goods_sn.length()>=10){//初始化预选项
			int curSizeIndex=0;
			int i=0;
			String sub=goods_sn.substring(8, 10);//截取尺码
			for(ProductDangAn item:sizesList){
				if(sub.equals(item.getGoods_spec())){
					curSizeIndex=i;
					break;
				}
				i++;
			}
			flowLayout_size.setCheckedAt(curSizeIndex);
		}
		
	}

	private void updateHeadViews(ProductDangAn bean){
		if(bean==null){
			tv_name.setText("");
			tv_ls_price.setText("￥");
			tv_sn.setText("编码：");
		}else{
			tv_name.setText(bean.getGoods_name());
			tv_ls_price.setText("￥"+bean.getGoods_ls_price());
			tv_sn.setText("编码："+bean.getGoods_sn());
		}
		curBean=bean;
	}
	
	private void doCommandGetGoodsListBySKU(){
		String styleCode=goods_sn.substring(0,6);
		Commands.doCommandGetGoodsListBySKU(context, styleCode, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					GetGoodsListResponse obj=mapperToObject(response, GetGoodsListResponse.class);
					if(obj!=null&&obj.getGoodsInfo()!=null){
						beans.clear();
						beans.addAll(obj.getGoodsInfo());
						updateViews(beans);
						//请求颜色色图，成功后需要与颜色Code做比较显示相应的图片
						doCommandGetGoodsColorImageList();
					}
				}
			}
		});
	}
	
	private void doCommandGetCollectGoodsBySKU(String goods_sn){
		Commands.doCommandGetCollectGoodsBySKU(context, goods_sn, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					GetProdColorListResponse obj=mapperToObject(response, GetProdColorListResponse.class);
					if(obj!=null&&!isEmptyList(obj.getInfo())){
						btn_favorites.setChecked(true);
					}else{
						btn_favorites.setChecked(false);
					}
				}
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_addToShoppingcart:
			if(curBean==null){
				showToast("未找到颜色尺码匹配的商品");
				return;
			}
			showToast("btn_addToShoppingcart");
			break;
		case R.id.btn_print:
			showToast("btn_print");
			break;

		default:
			break;
		}
	}
	
	private void saveProductDangAnRecentlyBrowse(ProductDangAn bean){
		//存在则更新，不存在则保存
		ProductDangAnRecentlyBrowse item=DataSupport.where("goods_sn = ?",bean.getGoods_sn()).findFirst(ProductDangAnRecentlyBrowse.class);
		if(item==null){
//			DataSupport.count(ProductDangAnRecentlyBrowse.class);
			item=new ProductDangAnRecentlyBrowse();
			item.setGoods_sn(bean.getGoods_sn());
			item.setGoods_name(bean.getGoods_name());
			item.setBrand_name(bean.getBrand_name());
			item.setGoods_brand(bean.getGoods_brand());
			item.setGoods_ls_price(bean.getGoods_ls_price());
			item.setGoods_img(bean.getGoods_img());
			item.setTimeMillis(System.currentTimeMillis());
			item.saveFast();
		}else{
			item.setGoods_img(bean.getGoods_img());
			item.setTimeMillis(System.currentTimeMillis());
			item.update(item.getId());
		}
	}
	
	private void doCommandGetGoodsColorImageList(){
		String styleCode=goods_sn.substring(0,6);
		String colorCode=null;
		Commands.doCommandGetGoodsColorImageList(context, styleCode, colorCode, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					GetGoodsColorImageListResponse obj=mapperToObject(response, GetGoodsColorImageListResponse.class);
					if(obj!=null&&obj.getInfo()!=null){
						final List<ProdColorImage> colorImageList=obj.getInfo().getImageInfo();
						if(!isEmptyList(colorImageList)){
							if(colorsList.size()!=colorImageList.size()){
								showToast("颜色"+colorsList.size()+"，图片"+colorImageList.size());
							}
							flowLayout_color.setAdapter(new TagAdapter<ProductDangAn>(colorsList){
								@Override
								public View getView(FlowLayout parent, final int position, final ProductDangAn item){
									View root = LayoutInflater.from(context).inflate(R.layout.item_image_text,flowLayout_color, false);
									root.setTag(item.getGoods_color());
									
									ImageView item_0=(ImageView)root.findViewById(R.id.item_0);
									TextView item_1=(TextView)root.findViewById(R.id.item_1);
									item_1.setText("00".equals(item.getGoods_color())?"均色":item.getGoods_color_desc());
									
									for(ProdColorImage colorImage:colorImageList){
										if(item.getGoods_color().equals(colorImage.getColorCode())){
											loadImageByPicasso(colorImage.getImgUrl(), item_0);
											break;
										}
									}
									return root;
								}
							});
							
							if(goods_sn.length()>=10){//初始化预选项
								int curColorIndex=0;
								int i=0;
								String sub=goods_sn.substring(6, 8);//截取颜色
								for(ProductDangAn item:colorsList){
									if(sub.equals(item.getGoods_color())){
										curColorIndex=i;
										break;
									}
									i++;
								}
								flowLayout_color.setCheckedAt(curColorIndex);
							}
							
						}
					}
				}
			}
		});
	}
	
	
	/**
	 * 提供一个简单快捷的列表Dialog
	 * @param activity
	 * @param items
	 * @param onPositiveListener
	 */
	private void showDialogItems() {
		String[] items=new String[]{"修改图片"};
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setItems(items, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if(which==0){
					mode="updateColorImage";
					openImageSelector();
				}
			}
		})
		.setCancelable(true);
		AlertDialog dialog=builder.create();
		dialog.show();
	}
	
	
	private void doCommandGetGoodsStyleImageList(){
		String styleCode=goods_sn.substring(0,6);
		Commands.doCommandGetGoodsStyleImageList(context, styleCode, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					GetGoodsStyleImageListResponse obj=mapperToObject(response, GetGoodsStyleImageListResponse.class);
					if(obj!=null&&obj.getInfo()!=null){
						styleImageList=obj.getInfo().getImageInfo();
						if(styleImageList!=null){
							flowLayout_img.setAdapter(new TagAdapter<ProdColorImage>(styleImageList){
								@Override
								public View getView(FlowLayout parent, int position, ProdColorImage item){
									ImageView iv = (ImageView) LayoutInflater.from(context).inflate(R.layout.item_image_for_product_detail,flowLayout_img, false);
									loadImageByPicasso(item.getImgUrl(), iv);
									iv.setTag(item);
									return iv;
								}
							});
							
							//
							listViews.clear();
							for(int i=0;i<styleImageList.size();i++){
								View view=LayoutInflater.from(context).inflate(R.layout.item_product_img, null);
								listViews.add(view);
							}
							if(myPagerAdapter==null){
								myPagerAdapter=new MyPagerAdapter(listViews);
								viewPager.setAdapter(myPagerAdapter);
							}else{
								myPagerAdapter.setViews(listViews);
								myPagerAdapter.notifyDataSetChanged();
							}
							viewPager.setCurrentItem(curPosition);
						}
						
						//保存最近浏览
						if(curBean!=null){
							if(styleImageList!=null){
								for(ProdColorImage styleImage:styleImageList){
									if(!isEmpty(styleImage.getImgUrl())){
										curBean.setGoods_img(styleImage.getImgUrl());
										break;
									}
								}
							}
							saveProductDangAnRecentlyBrowse(curBean);
						}
					}
				}
			}
		});
	}
	
	private String mode=null;
	private void showDialogItems2() {
		String[] items=new String[]{"修改图片","新增图片","删除图片"};
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setItems(items, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if(which==0){
					mode="updateStyleImage";
					openImageSelector();
				}else if(which==1){//新增
					mode="addStyleImage";
					openImageSelector();
				}else if(which==2){//删除
					mode="deleteStyleImage";
					doCommandDeleteGoodsStyleImage();
				}
			}
		})
		.setCancelable(true);
		AlertDialog dialog=builder.create();
		dialog.show();
	}
	
	private void doCommandDeleteGoodsStyleImage(){
		List<ProdColorImage> styleImages=new ArrayList<ProdColorImage>();
		ProdColorImage curProdColorImage=styleImageList.get(curPosition);
		styleImages.add(curProdColorImage);
		Commands.doCommandDeleteGoodsStyleImage(context, styleImages, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					flowLayout_img.removeViewAt(curPosition);
					listViews.remove(curPosition);
					if(myPagerAdapter!=null){
						myPagerAdapter.setViews(listViews);
						myPagerAdapter.notifyDataSetChanged();
					}
				}
			}
		});
		
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////
	private void openImageSelector(){
		ImageConfig imageConfig=null;
		if(mode.equals("updateStyleImage")||mode.equals("addStyleImage")){//修改添加主图，不需要裁剪
			imageConfig
	        = new ImageConfig.Builder(new PicassoLoader())
	        .singleSelect()// 开启单选   （默认为多选） 
	        .showCamera()// 开启拍照功能 （默认关闭）
	        .filePath("/xStore/picture")// 拍照后存放的图片路径（默认 /temp/picture） （会自动创建）
	        .build();
		}else if(mode.equals("updateColorImage")){//修改色图，需要裁剪
			imageConfig
	        = new ImageConfig.Builder(new PicassoLoader())
	        .crop()  // (截图默认配置：关闭    比例 1：1    输出分辨率  500*500)
	        .singleSelect()// 开启单选   （默认为多选） 
	        .showCamera()// 开启拍照功能 （默认关闭）
	        .filePath("/xStore/picture")// 拍照后存放的图片路径（默认 /temp/picture） （会自动创建）
	        .build();
		}
		
		ImageSelector.open(this, imageConfig);   // 开启图片选择器
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ImageSelector.IMAGE_REQUEST_CODE) {
			if (data != null) {
				if(mode.equals("updateStyleImage")){//修改主图
					List<String> list = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
					List<File> files=new ArrayList<File>();
					for (String path : list) {
						File file = new File(path);
						files.add(file);
					}
					if(files.size()>0){
						View view=flowLayout_img.getChildAt(curPosition);
						ImageView item_0=(ImageView)view.findViewById(R.id.item_0);
						loadImageByPicasso(item_0,80,files.get(0));//从本地加载图片
						luban(files);
					}
					
				}else if(mode.equals("addStyleImage")){//添加主图
					List<String> list = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
					List<File> files=new ArrayList<File>();
					for (String path : list) {
						File file = new File(path);
						files.add(file);
					}
					luban(files);
				}else if(mode.equals("updateColorImage")){//修改色图
					List<String> list = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
					List<File> files=new ArrayList<File>();
					for (String path : list) {
						File file = new File(path);
						files.add(file);
					}
					if(files.size()>0){
						View view=flowLayout_color.getChildAt(curColorPosition);
						ImageView item_0=(ImageView)view.findViewById(R.id.item_0);
						loadImageByPicasso(item_0,80,files.get(0));//从本地加载图片
						luban(files);
					}
				}
			}
		}
	}
	
	private void loadImageByPicasso(ImageView iv,int width,File file){
		Picasso.with(context).load(file)
//		.transform(new CropCircleTransformation())
		.resize(width, width).centerCrop()
		.placeholder(R.drawable.default_img)
		.error(R.drawable.default_img)
		.into(iv);
	}
	
	private void luban(List<File> files) {
		Luban.get(context)
			.load(files)
			.putGear(Luban.THIRD_GEAR)// 传人要压缩的图片
			.setOnMultiCompressListener(// 设定压缩档次，默认三挡
				new Luban.OnMultiCompressListener() {

					@Override
					public void onStart(){
						context.showProgress();
					}
					
					@Override
					public void onSuccess(List<File> files,long timeSpent) {
						Log.i("tag", "timeSpent=" + timeSpent);
						context.closeProgress();
						uploadImages(files);
					}

					@Override
					public void onError(long timeSpent) {
						context.closeProgress();
					}
				}).launch();
	}
	
	private void uploadImages(final List<File> files) {
		TimerTask task=new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
//					Looper.prepare();
					int count=files.size();
					for(int i=0;i<count;i++){
						uploadToQQcloud(files.get(i).getAbsolutePath());
						Thread.sleep(25);
					}
//					Looper.loop();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		Timer t=new Timer();
		t.schedule(task, 5);
	}
	
	private void uploadToQQcloud(String filePath) throws Exception {
		PicCloud pc = new PicCloud(App.APP_ID_V2, App.SECRET_ID_V2, App.SECRET_KEY_V2, App.BUCKET);
		picBase(pc, filePath);
	}
	
	private void picBase(PicCloud pc, String fileName) throws Exception {
		UploadResult result = new UploadResult();
		int ret=-1;
		// 2. 文件流的方式
		FileInputStream fileStream = new FileInputStream(fileName);
		ret = pc.upload(fileStream, result);
		
		if(ret==0){
//			if(App.NEED_CHECK_PORN){
//				if(checkPorn(result.downloadUrl)){
////					 ret = pc.delete(result.fileId);// 删除一张图片
////					 return;
//					Log.w("tag","It maybe a porn picture.");
//				}else{
//					
//				}
//			}
//			http://zstore-10009153.image.myqcloud.com/63cfd067-5fc5-467a-9ce9-186821168aa3
//			view.setTag(result);
			if(mode.equals("updateStyleImage")){//修改主图
				doCommandUpdateGoodsStyleImage(result);
			}else if(mode.equals("addStyleImage")){//添加主图
				doCommandAddGoodsStyleImage(result);
			}else if(mode.equals("updateColorImage")){//修改色图
				doCommandUpdateGoodsColorImage(result);
			}
		}else{
			showToast("上传图片失败");
		}
	}
	
	private void doCommandAddGoodsStyleImage(final UploadResult result){
		//主图集合
		List<ProdColorImage> styleImages=new ArrayList<ProdColorImage>();
		final ProdColorImage curProdColorImage=new ProdColorImage();
		curProdColorImage.setCompanyCode(App.user.getShopInfo().getCompany_code());
		curProdColorImage.setStyleCode(goods_sn.substring(0,6));
		curProdColorImage.setImgUrl(result.downloadUrl);
		styleImages.add(curProdColorImage);
				
		Commands.doCommandAddGoodsStyleImage(context, styleImages, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					styleImageList.add(curProdColorImage);
					flowLayout_img.notifyDataChanged();
					
					View view=LayoutInflater.from(context).inflate(R.layout.item_product_img, null);
					listViews.add(view);
					
					if(myPagerAdapter!=null){
						myPagerAdapter.setViews(listViews);
						myPagerAdapter.notifyDataSetChanged();
					}
				}
			}
		});
	}
		
	private void doCommandUpdateGoodsStyleImage(final UploadResult result){
		List<ProdColorImage> styleImages=new ArrayList<ProdColorImage>();
		ProdColorImage curProdColorImage=styleImageList.get(curPosition);
		curProdColorImage.setImgUrl(result.downloadUrl);
		styleImages.add(curProdColorImage);
		
		Commands.doCommandUpdateGoodsStyleImage(context, styleImages, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					ProdColorImage curProdColorImage=styleImageList.get(curPosition);
					curProdColorImage.setImgUrl(result.downloadUrl);
					if(myPagerAdapter!=null){
						myPagerAdapter.setViews(listViews);
						myPagerAdapter.notifyDataSetChanged();
					}
				}
			}
		});
	}
	
	private void doCommandUpdateGoodsColorImage(UploadResult result){
		if(curColorPosition<0){
			return;
		}
		ProductDangAn bean=colorsList.get(curColorPosition);
		
		ProdColorImage colorImage=new ProdColorImage();
		colorImage.setColorCode(bean.getGoods_color());
		colorImage.setCompanyCode(App.user.getShopInfo().getCompany_code());
		colorImage.setStyleCode(goods_sn.substring(0,6));
		colorImage.setImgUrl(result.downloadUrl);
		
		List<ProdColorImage> imageInfos=new ArrayList<ProdColorImage>();
		imageInfos.add(colorImage);
		Commands.doCommandUpdateGoodsColorImage(context, imageInfos, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
//					showToast("操作成功");
				}
			}
		});
	}
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////
	public class MyPagerAdapter extends PagerAdapter {
		public List<View> views;

		public MyPagerAdapter(List<View> views) {
			this.views = views;
		}

		public List<View> getViews() {
			return views;
		}

		public void setViews(List<View> views) {
			this.views = views;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View v, int position, Object object) {
			((ViewPager) v).removeView((View) object);
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public Object instantiateItem(View v, int position) {
			View view = views.get(position);
			ProdColorImage styleImage=styleImageList.get(position);
			PhotoView photoView=(PhotoView)view.findViewById(R.id.item_0);
			// 禁用图片缩放功能 (默认为禁用，会跟普通的ImageView一样，缩放功能需手动调用enable()启用)
			photoView.disenable();//photoView.enable();
//			Log.i("tag", "url="+url);
			Picasso.with(context).load(styleImage.getImgUrl()).error(R.drawable.picture_not_found).into(photoView);
			
			photoView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ArrayList<String> imgUrls=new ArrayList<String>();
					for(ProdColorImage bean:styleImageList){
						imgUrls.add(bean.getImgUrl());
					}
					context.changeToImageGalleryActivity(imgUrls,curPosition);
				}
			});
			((ViewPager) v).addView(view, 0);
			return view;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

	}

}
