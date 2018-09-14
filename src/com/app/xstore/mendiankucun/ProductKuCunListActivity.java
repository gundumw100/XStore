package com.app.xstore.mendiankucun;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response.Listener;
import com.app.net.Commands;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;

/**
 * 商品库存列表界面
 * @author pythoner
 * 
 */
public class ProductKuCunListActivity extends BaseActivity {

	private ListView listView;
	private CommonAdapter<Stock> adapter;
	private List<Stock> beans=new ArrayList<Stock>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mendian_kucun_list);
		context = this;
		initActionBar("商品库存", null, null);
		initViews();
		
		ArrayList<String> goods_brand=getIntent().getStringArrayListExtra("goods_brand");
		ArrayList<String> goods_year=getIntent().getStringArrayListExtra("goods_year");
		ArrayList<String> goods_season=getIntent().getStringArrayListExtra("goods_season");
		ArrayList<String> goods_sort=getIntent().getStringArrayListExtra("goods_sort");
		ArrayList<String> goods_other=getIntent().getStringArrayListExtra("goods_other");
		ArrayList<String> goods_label=getIntent().getStringArrayListExtra("goods_label");
		ArrayList<String> goods_color=null;
		ArrayList<String> goods_cs=null;
		ArrayList<String> goods_spec=null;
		ArrayList<String> goods_jldw=null;
		ArrayList<String> goods_cw=null;
		
		doCommandGetStockByParamList(goods_brand,goods_color,goods_cs,goods_sort,goods_spec, goods_season, goods_jldw,goods_cw,goods_other,goods_label);
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
				Intent intent=new Intent(context,KuCunQueryBySkuActivity.class);
				intent.putExtra("StyleCode", beans.get(position).getStyleCode());
				startActivity(intent);
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
			listView.setAdapter(adapter = new CommonAdapter<Stock>( context, beans,
					  R.layout.item_product_kucun_list){
					  
					@Override
					public void setValues(ViewHolder helper, final Stock item, final int position) {
						// TODO Auto-generated method stub
						helper.setText(R.id.item_0, item.getStyleCode());
						helper.setText(R.id.item_1, item.getStyleName());
						helper.setText(R.id.item_2, String.valueOf(item.getStock()));
						helper.setText(R.id.item_3, String.valueOf(item.getOnline_stock()));
					}
			});
		}else{
			adapter.notifyDataSetChanged();
		}
	}
	
	private void doCommandGetStockByParamList(List<String> goods_brand,List<String> goods_color,List<String> goods_cs,List<String> goods_sort,List<String> goods_spec,List<String> goods_season,List<String> goods_jldw,List<String> goods_cw,List<String> goods_other,List<String> goods_label) {
		Commands.doCommandGetStockByParamList(context, goods_brand, goods_color, goods_cs, goods_sort, goods_spec, goods_season, goods_jldw, goods_cw, goods_other, goods_label,new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					GetStockByParamListResponse obj=mapperToObject(response, GetStockByParamListResponse.class);
					if(obj!=null&&obj.getInfo()!=null){
						beans.addAll(obj.getInfo());
						updateViews(beans);
					}
				}
			}
		});
	}
	
}
