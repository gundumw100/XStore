package com.app.xstore.shangpindangan;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.net.Commands;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;

/**
 * 
 * @author pythoner
 * 
 */
public class ProductQueryListBySkuActivity extends BaseActivity {

	private TextView tv_name;
	private ListView listView;
	private CommonAdapter<ProductDangAn> adapter;
	private List<ProductDangAn> beans=new ArrayList<ProductDangAn>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shangpindangan_product_query_list);
		context = this;
		initActionBar("商品", null, null);
		initViews();
		String goods_sn=getIntent().getStringExtra("ProductSku");
		if(isEmpty(goods_sn)){
			return;
		}
		doCommandGetGoodsListBySKU(goods_sn);
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		tv_name=$(R.id.tv_name);
		listView=$(R.id.listView);
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				// TODO Auto-generated method stub
				startProductDetailActivity(beans.get(position).getGoods_sn());
			}
		});
	}

	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
		if(beans.size()==0){
			if(listView.getEmptyView()==null){
				setEmptyView(listView, "暂无数据");
			}
		}else{
			ProductDangAn item=beans.get(0);
			tv_name.setText(item.getGoods_name());
		}
		
		
		notifyDataSetChanged();
	}

	private void notifyDataSetChanged(){
		if(adapter==null){
			listView.setAdapter(adapter = new CommonAdapter<ProductDangAn>( context, beans,
					  R.layout.item_product_query_list){
					  
					@Override
					public void setValues(ViewHolder helper, final ProductDangAn item, final int position) {
						// TODO Auto-generated method stub
						helper.setText(R.id.item_0, item.getGoods_sn());
						helper.setText(R.id.item_1, item.getGoods_color_desc());
						helper.setText(R.id.item_2, item.getGoods_spec_desc());
						helper.setText(R.id.item_3, String.valueOf(item.getGoods_ls_price()));
						
					}
			});
		}else{
			adapter.notifyDataSetChanged();
		}
	}
	
	private void doCommandGetGoodsListBySKU(String goods_sn){
		Commands.doCommandGetGoodsListBySKU(context, goods_sn, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					GetGoodsListResponse obj=mapperToObject(response, GetGoodsListResponse.class);
					if(obj!=null&&obj.getGoodsInfo()!=null){
						beans.addAll(obj.getGoodsInfo());
						
						updateViews(beans);
					}
				}
			}
		});
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////
}
