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
public class ProductQueryListByParamsActivity extends BaseActivity {

	private ListView listView;
	private CommonAdapter<ProdStyle> adapter;
	private List<ProdStyle> beans=new ArrayList<ProdStyle>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shangpindangan_product_query_list_by_params);
		context = this;
		initActionBar("商品", null, null);
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
		
		doCommandGetGoodsListByParam(goods_brand,goods_color,goods_cs,goods_sort,goods_spec, goods_season, goods_jldw,goods_cw,goods_other,goods_label);
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
				startProductDetailActivity(beans.get(position).getStyleCode());
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
			listView.setAdapter(adapter = new CommonAdapter<ProdStyle>( context, beans,
					  R.layout.item_product_query_list){
					  
					@Override
					public void setValues(ViewHolder helper, final ProdStyle item, final int position) {
						// TODO Auto-generated method stub
						helper.setText(R.id.item_0, item.getDateCode()+item.getStyleCode());
						helper.setText(R.id.item_1, item.getDescription());
//						helper.setText(R.id.item_2, item.getGoods_spec());
//						helper.setText(R.id.item_3, String.valueOf(item.getGoods_ls_price()));
						
					}
			});
		}else{
			adapter.notifyDataSetChanged();
		}
	}
	
	
	private void doCommandGetGoodsListByParam(List<String> goods_brand,List<String> goods_color,List<String> goods_cs,List<String> goods_sort,List<String> goods_spec,List<String> goods_season,List<String> goods_jldw,List<String> goods_cw,List<String> goods_other,List<String> goods_label) {
		Commands.doCommandGetGoodsListByParam(context, goods_brand, goods_color, goods_cs, goods_sort, goods_spec, goods_season, goods_jldw, goods_cw, goods_other, goods_label,new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				{"ErrMessage":"","Result":true,"Info":[{"DateCode":"18","ShopCode":"S001","Description":"帅哥","StyleCode":"0008"}],"ErrSysTrackMessage":"","ErrSysMessage":"","Message":"获取成功"}
//				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					GetProdStyleListResponse obj=mapperToObject(response, GetProdStyleListResponse.class);
					if(obj!=null&&obj.getInfo()!=null){
						beans.addAll(obj.getInfo());
						updateViews(beans);
					}
				}
			}
		});
	}
	
}
