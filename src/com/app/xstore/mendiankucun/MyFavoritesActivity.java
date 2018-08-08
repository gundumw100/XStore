package com.app.xstore.mendiankucun;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Response.Listener;
import com.app.net.Commands;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.app.xstore.shangpindangan.GetGoodsListResponse;
import com.app.xstore.shangpindangan.ProductDangAn;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;

/**
 * 
 * @author pythoner
 * 
 */
public class MyFavoritesActivity extends BaseActivity {

	public static String GOODS_SN="Goods_sn";
	private ListView listView;
	private CommonAdapter<ProductDangAn> adapter;
	private List<ProductDangAn> beans=new ArrayList<ProductDangAn>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_favorites);
		context = this;
		initActionBar("我的收藏", null, null);
		initViews();
		doCommandGetCollectGoodsList();
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		listView=$(R.id.listView);
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				// TODO Auto-generated method stub
				ProductDangAn bean=beans.get(position);
				Intent intent=new Intent();
				intent.putExtra(GOODS_SN, bean.getGoods_sn());
				setResult(0, intent);
				finish();
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
		}
		notifyDataSetChanged();
	}

	private void notifyDataSetChanged(){
		if(adapter==null){
			listView.setAdapter(adapter = new CommonAdapter<ProductDangAn>( context, beans,
					  R.layout.item_for_product){
					  
					@Override
					public void setValues(ViewHolder helper, final ProductDangAn item, final int position) {
						// TODO Auto-generated method stub
						ImageView item_0=helper.getView(R.id.item_0);
						loadMultImageByPicasso(item.getGoods_img(),item_0);
						helper.setText(R.id.item_1,item.getGoods_name());
						helper.setText(R.id.item_2,"品牌："+item.getBrand_name());
						helper.setText(R.id.item_4,"编码："+item.getGoods_sn());
						helper.setText(R.id.item_3,"￥"+item.getGoods_ls_price());
						View item_delete=helper.getView(R.id.item_delete);
						item_delete.setVisibility(View.VISIBLE);
						item_delete.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								doCommandDelCollectGoods(item);
							}
						});
						
					}
			});
		}else{
			adapter.notifyDataSetChanged();
		}
	}
	
	private void doCommandGetCollectGoodsList(){
		Commands.doCommandGetCollectGoodsList(context, new Listener<JSONObject>() {

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
	
	private void doCommandDelCollectGoods(final ProductDangAn item){
		Commands.doCommandDelCollectGoods(context, item.getGoods_sn(), new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					beans.remove(item);
					updateViews(beans);
				}
			}
		});
	}
	
}
