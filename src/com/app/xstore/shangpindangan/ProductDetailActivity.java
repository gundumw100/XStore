package com.app.xstore.shangpindangan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;

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
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.squareup.picasso.Picasso;
import com.widget.flowlayout.FlowLayout;
import com.widget.flowlayout.TagAdapter;
import com.widget.flowlayout.TagFlowLayout;
import com.widget.flowlayout.TagView;
import com.widget.photoView.PhotoView;

/**
 * 商品详情
 * @author pythoner
 * 
 */
public class ProductDetailActivity extends BaseActivity implements View.OnClickListener{

	private int curPosition;
	private ViewPager viewPager;
	private ArrayList<View> listViews = new ArrayList<View>();
	private List<String> imgList=new ArrayList<String>();
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
		if(isEmpty(goods_sn)){
			showToast("参数出错");
			return;
		}
		initViews();
		doCommandGetGoodsListBySKU(goods_sn);
		
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
				Log.i("tag", "response="+response.toString());
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
				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					showToast("取消收藏");
				}
			}
		});
	}
	
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
			
			//保存最近浏览
			saveProductDangAnRecentlyBrowse(bean);
		}
		final LayoutInflater inflater = LayoutInflater.from(context);
		
		HashMap<String,ProductDangAn> imgMap=new HashMap<String,ProductDangAn>();
		imgList.clear();
		for(ProductDangAn item:beans){
			String goodsImg=item.getGoods_img();
			if(!isEmpty(goodsImg)){
				String[] imgs=goodsImg.split(";");
				
				for(String img:imgs){
					if(imgMap.containsKey(img)){
						
					}else{
						imgMap.put(img, item);
						imgList.add(img);
					}
				}
				
			}
			
		}
		flowLayout_img.setAdapter(new TagAdapter<String>(imgList){
			@Override
			public View getView(FlowLayout parent, int position, String item){
				ImageView iv = (ImageView) inflater.inflate(R.layout.item_image_for_product_detail,flowLayout_img, false);
				loadImageByPicasso(item, iv);
				iv.setTag(item);
				return iv;
			}
		});
		
		//
		for(int i=0;i<imgList.size();i++){
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
		//
		
		HashMap<String,ProductDangAn> colorsMap=new HashMap<String,ProductDangAn>();
		List<ProductDangAn> colorsList=new ArrayList<ProductDangAn>();
		for(ProductDangAn item:beans){
			if(colorsMap.containsKey(item.getGoods_color())){
				
			}else{
				colorsMap.put(item.getGoods_color(), item);
				colorsList.add(item);
			}
		}
		
		flowLayout_color.setAdapter(new TagAdapter<ProductDangAn>(colorsList){
			@Override
			public View getView(FlowLayout parent, int position, ProductDangAn item){
				View root = inflater.inflate(R.layout.item_image_text,flowLayout_color, false);
				root.setTag(item.getGoods_color());
				
				ImageView item_0=(ImageView)root.findViewById(R.id.item_0);
				TextView item_1=(TextView)root.findViewById(R.id.item_1);
				item_1.setText("00".equals(item.getGoods_color())?"均色":item.getGoods_color_desc());
				loadImageByPicasso(item.getGoods_color_image(), item_0);
				return root;
				
//				TextView tv = (TextView) inflater.inflate(R.layout.item_text,flowLayout_color, false);
//				tv.setText("00".equals(item.getGoods_color())?"均色":item.getGoods_color_desc());
//				tv.setTag(item.getGoods_spec());
//				return tv;
				
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
				TextView tv = (TextView) inflater.inflate(R.layout.item_text,flowLayout_size, false);
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
	
	private void doCommandGetGoodsListBySKU(String sku){
		if(sku==null||sku.length()<6){
			showToast("编码有误");
			return;
		}
		String goods_sn=sku.substring(0,6);
		Commands.doCommandGetGoodsListBySKU(context, goods_sn, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					GetGoodsListResponse obj=mapperToObject(response, GetGoodsListResponse.class);
					if(obj!=null&&obj.getGoodsInfo()!=null){
						beans.clear();
						beans.addAll(obj.getGoodsInfo());
						updateViews(beans);
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
			DataSupport.count(ProductDangAnRecentlyBrowse.class);
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
			item.setTimeMillis(System.currentTimeMillis());
			item.update(item.getId());
		}
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
			String url=imgList.get(position);
			PhotoView photoView=(PhotoView)view.findViewById(R.id.item_0);
			// 禁用图片缩放功能 (默认为禁用，会跟普通的ImageView一样，缩放功能需手动调用enable()启用)
			photoView.disenable();//photoView.enable();
//			Log.i("tag", "url="+url);
			Picasso.with(context).load(url).error(R.drawable.picture_not_found).into(photoView);
			
			photoView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ArrayList<String> imgUrls=(ArrayList<String>)imgList;
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
