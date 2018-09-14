package com.app.xstore.shangpindangan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.android.volley.Response.Listener;
import com.app.net.Commands;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.app.xstore.mendiankucun.ProductKuCunListActivity;
import com.widget.flowlayout.FlowLayout;

/**
 * 
 * @author pythoner
 * 
 */
public class ProductQueryByParamsActivity extends BaseActivity {

	private FlowLayout flowLayout_brand,flowLayout_year,flowLayout_season,flowLayout_sort,flowLayout_other,flowLayout_label;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shangpindangan_product_query_by_params);
		context = this;
		initActionBar("商品查询", null, null);
		initViews();
		doCommandGetProdBrandList();
		doCommandGetProdSeasonList();
		doCommandGetProdSortList();
		doCommandGetProdOtherList();
		doCommandGetProdLabelList();
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		$(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doCommandGetGoodsListByParam();
			}
		});
		flowLayout_brand=$(R.id.flowLayout_brand);
		flowLayout_year=$(R.id.flowLayout_year);
		Calendar date = Calendar.getInstance();
		int curYear=date.get(Calendar.YEAR);
		int lastYear=curYear-1;
		int nextYear=curYear+1;
		List<String> list=new ArrayList<String>();
		list.add(String.valueOf(lastYear));
		list.add(String.valueOf(curYear));
		list.add(String.valueOf(nextYear));
		for(String item:list){
			MarginLayoutParams params=new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			CheckBox child=new CheckBox(context);
			child.setLayoutParams(params);
			child.setText(item);
			child.setTag(item);
			child.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
			flowLayout_year.addView(child);
		}
		
		doCheckAll(flowLayout_year);
		
		flowLayout_season=$(R.id.flowLayout_season);
		flowLayout_sort=$(R.id.flowLayout_sort);
		flowLayout_other=$(R.id.flowLayout_other);
		flowLayout_label=$(R.id.flowLayout_label);
	}
	
	private void doCheckAll(final FlowLayout flowLayout){
		CheckBox child=(CheckBox)flowLayout.getChildAt(0);//取出全部
		child.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean checked) {
				// TODO Auto-generated method stub
				int count=flowLayout.getChildCount();
				for(int i=1;i<count;i++){
					CheckBox child=(CheckBox)flowLayout.getChildAt(i);
					child.setChecked(checked);
					child.setEnabled(!checked);
				}
			}
		});
	}
	
	private void doCommandGetGoodsListByParam(){
		int startItemNo=1;
		int count=0;
		ArrayList<String> goods_brand=new ArrayList<String>();
		count=flowLayout_brand.getChildCount();
		CheckBox all_brand=(CheckBox)flowLayout_brand.getChildAt(0);
		if(all_brand.isChecked()){//查询全部不要传
			
		}else{
			for(int i=startItemNo;i<count;i++){
				CheckBox child=(CheckBox)flowLayout_brand.getChildAt(i);
				if(child.isChecked()){
					goods_brand.add((String)child.getTag());
				}
			}
		}
		
		ArrayList<String> goods_year=new ArrayList<String>();
		count=flowLayout_year.getChildCount();
		CheckBox all_year=(CheckBox)flowLayout_year.getChildAt(0);
		if(all_year.isChecked()){
			
		}else{
			for(int i=startItemNo;i<count;i++){
				CheckBox child=(CheckBox)flowLayout_year.getChildAt(i);
				if(child.isChecked()){
					goods_year.add((String)child.getTag());
				}
			}
		}
		
		ArrayList<String> goods_season=new ArrayList<String>();
		count=flowLayout_season.getChildCount();
		CheckBox all_season=(CheckBox)flowLayout_season.getChildAt(0);
		if(all_season.isChecked()){
			
		}else{
			for(int i=startItemNo;i<count;i++){
				CheckBox child=(CheckBox)flowLayout_season.getChildAt(i);
				if(child.isChecked()){
					goods_season.add((String)child.getTag());
				}
			}
		}
		
		ArrayList<String> goods_sort=new ArrayList<String>();
		count=flowLayout_sort.getChildCount();
		CheckBox all_sort=(CheckBox)flowLayout_sort.getChildAt(0);
		if(all_sort.isChecked()){
			
		}else{
			for(int i=startItemNo;i<count;i++){
				CheckBox child=(CheckBox)flowLayout_sort.getChildAt(i);
				if(child.isChecked()){
					goods_sort.add((String)child.getTag());
				}
			}
		}
		
		ArrayList<String> goods_other=new ArrayList<String>();
		count=flowLayout_other.getChildCount();
		CheckBox all_other=(CheckBox)flowLayout_other.getChildAt(0);
		if(all_other.isChecked()){
			
		}else{
			for(int i=startItemNo;i<count;i++){
				CheckBox child=(CheckBox)flowLayout_other.getChildAt(i);
				if(child.isChecked()){
					goods_other.add((String)child.getTag());
				}
			}
		}
		
		ArrayList<String> goods_label=new ArrayList<String>();
		count=flowLayout_label.getChildCount();
		CheckBox all_label=(CheckBox)flowLayout_label.getChildAt(0);
		if(all_label.isChecked()){
			
		}else{
			for(int i=startItemNo;i<count;i++){
				CheckBox child=(CheckBox)flowLayout_label.getChildAt(i);
				if(child.isChecked()){
					goods_label.add((String)child.getTag());
				}
			}
		}
		
//		boolean hasParam=false;
//		if(goods_brand.size()==0){
//			goods_brand=null;
//		}else{
//			hasParam=true;
//		}
//		if(goods_year.size()==0){
//			goods_year=null;
//		}else{
//			hasParam=true;
//		}
//		if(goods_season.size()==0){
//			goods_season=null;
//		}else{
//			hasParam=true;
//		}
//		if(goods_sort.size()==0){
//			goods_sort=null;
//		}else{
//			hasParam=true;
//		}
//		if(goods_other.size()==0){
//			goods_other=null;
//		}else{
//			hasParam=true;
//		}
		
//		ArrayList<String> goods_color=null;
//		ArrayList<String> goods_cs=null;
//		ArrayList<String> goods_spec=null;
//		ArrayList<String> goods_jldw=null;
//		ArrayList<String> goods_cw=null;
		
//		if(hasParam){
		String function=getIntent().getStringExtra("Function");
		if("cunku".equals(function)){
			Intent intent=new Intent(context,ProductKuCunListActivity.class);
			intent.putStringArrayListExtra("goods_brand", goods_brand);
			intent.putStringArrayListExtra("goods_year", goods_year);
			intent.putStringArrayListExtra("goods_season", goods_season);
			intent.putStringArrayListExtra("goods_sort", goods_sort);
			intent.putStringArrayListExtra("goods_other", goods_other);
			intent.putStringArrayListExtra("goods_label", goods_label);
			startActivity(intent);
//			finish();
		}else{
			Intent intent=new Intent(context,ProductQueryListByParamsActivity.class);
			intent.putStringArrayListExtra("goods_brand", goods_brand);
			intent.putStringArrayListExtra("goods_year", goods_year);
			intent.putStringArrayListExtra("goods_season", goods_season);
			intent.putStringArrayListExtra("goods_sort", goods_sort);
			intent.putStringArrayListExtra("goods_other", goods_other);
			intent.putStringArrayListExtra("goods_label", goods_label);
			startActivity(intent);
//			finish();
		}
//			doCommandGetGoodsListByParam(goods_brand,goods_color,goods_cs,goods_sort,goods_spec, goods_season, goods_jldw,goods_cw,goods_other);
//		}else{
//			showToast("没有查询条件");
//		}
		
	}
	
	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private void doCommandGetProdBrandList(){
		Commands.doCommandGetProdBrandList(context, "1", new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					GetProdBrandListResponse obj=mapperToObject(response, GetProdBrandListResponse.class);
					if(obj!=null&&obj.getInfo()!=null){
						List<ProdBrand> beans=obj.getInfo();
						for(ProdBrand item:beans){
							MarginLayoutParams params=new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
							CheckBox child=new CheckBox(context);
							child.setLayoutParams(params);
							child.setText(item.getDescription());
							child.setTag(item.getBrandCode());
							child.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
							flowLayout_brand.addView(child);
						}
						doCheckAll(flowLayout_brand);
					}
				}
			}
		});
	}
	
	private void doCommandGetProdSeasonList(){
		Commands.doCommandGetProdSeasonList(context, "1",new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					GetProdSeasonListResponse obj=mapperToObject(response, GetProdSeasonListResponse.class);
					if(obj!=null&&obj.getInfo()!=null){
						List<ProdSeason> beans=obj.getInfo();
						for(ProdSeason item:beans){
							MarginLayoutParams params=new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
							CheckBox child=new CheckBox(context);
							child.setLayoutParams(params);
							child.setText(item.getDescription());
							child.setTag(item.getSeasonCode());
							child.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
							flowLayout_season.addView(child);
						}
						doCheckAll(flowLayout_season);
					}
				}
			}
		});
	}
	
	private void doCommandGetProdSortList(){
		Commands.doCommandGetProdSortList(context, "1", new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					GetProdSortListResponse obj=mapperToObject(response, GetProdSortListResponse.class);
					if(obj!=null&&obj.getInfo()!=null){
						List<ProdSort> beans=obj.getInfo();
						for(ProdSort item:beans){
							MarginLayoutParams params=new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
							CheckBox child=new CheckBox(context);
							child.setLayoutParams(params);
							child.setText(item.getDescription());
							child.setTag(item.getSortCode());
							child.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
							flowLayout_sort.addView(child);
						}
						doCheckAll(flowLayout_sort);
					}
				}
			}
		});
	}
	
	private void doCommandGetProdOtherList(){
		Commands.doCommandGetProdOtherList(context, "1", new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					GetProdOtherListResponse obj=mapperToObject(response, GetProdOtherListResponse.class);
					if(obj!=null&&obj.getInfo()!=null){
						List<ProdOther> beans=obj.getInfo();
						for(ProdOther item:beans){
							MarginLayoutParams params=new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
							CheckBox child=new CheckBox(context);
							child.setLayoutParams(params);
							child.setText(item.getDescription());
							child.setTag(item.getOtherCode());
							child.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
							flowLayout_other.addView(child);
						}
						doCheckAll(flowLayout_other);
					}
				}
			}
		});
	}
	
	private void doCommandGetProdLabelList(){
		Commands.doCommandGetProdLabelList(context,new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					GetProdLabelListResponse obj=mapperToObject(response, GetProdLabelListResponse.class);
					if(obj!=null&&obj.getInfo()!=null){
						List<ProdLabel> beans=obj.getInfo();
						for(ProdLabel item:beans){
							MarginLayoutParams params=new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
							CheckBox child=new CheckBox(context);
							child.setLayoutParams(params);
							child.setText(item.getDescription());
							child.setTag(item.getLabelCode());
							child.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
							flowLayout_label.addView(child);
						}
						doCheckAll(flowLayout_label);
					}
				}
			}
		});
	}
	
}
