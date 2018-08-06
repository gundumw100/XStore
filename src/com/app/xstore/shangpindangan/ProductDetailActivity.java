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
	private MyPagerAdapter myPagerAdapter;
	private ArrayList<String> list = new ArrayList<String>();
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
		list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531307850102&di=01b7b89e1f33e669c528c45ea384e154&imgtype=0&src=http%3A%2F%2Fc.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F4e4a20a4462309f71a2b3ee97e0e0cf3d6cad681.jpg");
		list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531307850104&di=a746b6a4c4bffe22329697fb4e2e021f&imgtype=0&src=http%3A%2F%2Fc.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F72f082025aafa40f99d4e82aa764034f78f01932.jpg");
		list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531307850103&di=14761c22dc5332528d8853031098ceca&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F38dbb6fd5266d016ae9881d19b2bd40734fa3541.jpg");
		list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531307850902&di=65735007b15bac1a22c08fa401c09f35&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F43a7d933c895d14332bd91df7ff082025baf0706.jpg");
		list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531307850903&di=71488d9bace4291b28c0177089d0c227&imgtype=0&src=http%3A%2F%2Ff.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F42166d224f4a20a403c7e0319c529822730ed06f.jpg");
		initViews();
		doCommandGetGoodsListBySKU(goods_sn);
		
		doCommandGetCollectGoodsBySKU(goods_sn);
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		viewPager=(ViewPager)findViewById(R.id.viewPager);
		for(int i=0;i<list.size();i++){
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
		
		tv_name=$(R.id.tv_name);
		tv_ls_price=$(R.id.tv_ls_price);
		tv_sn=$(R.id.tv_sn);
		final TextView btn_save=$(R.id.btn_save);
		final EditText et_maidian=$(R.id.et_maidian);
		et_maidian.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence text, int start, int before, int count) {
				// TODO Auto-generated method stub
				btn_save.setVisibility(count==0?View.GONE:View.VISIBLE);
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
		
		
		HashMap<String,ProductDangAn> colorsMap=new HashMap<String,ProductDangAn>();
		List<ProductDangAn> colorsList=new ArrayList<ProductDangAn>();
		for(ProductDangAn item:beans){
			if(colorsMap.containsKey(item.getGoods_color())){
				
			}else{
				colorsMap.put(item.getGoods_color(), item);
				colorsList.add(item);
			}
		}
		
		final LayoutInflater inflater = LayoutInflater.from(context);
		flowLayout_color.setAdapter(new TagAdapter<ProductDangAn>(colorsList){
			@Override
			public View getView(FlowLayout parent, int position, ProductDangAn item){
				TextView tv = (TextView) inflater.inflate(R.layout.item_text,flowLayout_color, false);
				if("00".equals(item.getGoods_color())){
					tv.setText("均色");
				}else{
					tv.setText(item.getGoods_color_desc());
				}
				tv.setTag(item.getGoods_color());
				return tv;
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
				
			}else{
				sizesMap.put(item.getGoods_spec(), item);
				sizesList.add(item);
			}
		}
		flowLayout_size.setAdapter(new TagAdapter<ProductDangAn>(sizesList){
			@Override
			public View getView(FlowLayout parent, int position, ProductDangAn item){
				TextView tv = (TextView) inflater.inflate(R.layout.item_text,flowLayout_size, false);
				if("00".equals(item.getGoods_spec())){
					tv.setText("均码");
				}else{
					tv.setText(item.getGoods_spec_desc());
				}
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
//		{"ActionName":"GetGoodsListBySKU","Pars":{"companyCode":"C001","goodsSn":"180018"}}

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
//		{"ActionName":"GetCollectGoodsBySKU","Pars":{"goodsSn":"1800180101","shopCode":"S001"}}

		Commands.doCommandGetCollectGoodsBySKU(context, goods_sn, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				Log.i("tag", "response="+response.toString());
				//{"ErrMessage":"","Result":true,"Info":[],"ErrSysTrackMessage":"","ErrSysMessage":"","Message":"OK"}
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
			String url=list.get(position);
			PhotoView photoView=(PhotoView)view.findViewById(R.id.item_0);
			// 禁用图片缩放功能 (默认为禁用，会跟普通的ImageView一样，缩放功能需手动调用enable()启用)
			photoView.disenable();//photoView.enable();
//			Log.i("tag", "url="+url);
			Picasso.with(context).load(url).error(R.drawable.picture_not_found).into(photoView);
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
