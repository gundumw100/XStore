package com.app.xstore.shangpindangan;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
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
public class ProductListActivity extends BaseActivity {

	private ListView listView;
	private CommonAdapter<ProductDangAn> adapter;
	private List<ProductDangAn> beans=new ArrayList<ProductDangAn>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shangpindangan_product_list);
		context = this;
		initActionBar("商品", null, null);
		initViews();
		String start_date="2018-01-01";
		String end_date="2018-12-31";
		doCommandGetGoodsListByDate( start_date, end_date);
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		listView=$(R.id.listView);
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
					  R.layout.item_product_list){
					  
					@Override
					public void setValues(ViewHolder helper, final ProductDangAn item, final int position) {
						// TODO Auto-generated method stub
						helper.setText(R.id.item_0, item.getGoods_sn());
						helper.setText(R.id.item_1, item.getGoods_sj_date().substring(0, 10));
						helper.setText(R.id.item_2, String.valueOf(item.getGoods_jh_price()));
						helper.setText(R.id.item_3, String.valueOf(item.getGoods_ls_price()));
						
					}
			});
		}else{
			adapter.notifyDataSetChanged();
		}
	}
	
	private void doCommandGetGoodsListByDate(String start_date,String end_date){
		Commands.doCommandGetGoodsListByDate(context, start_date, end_date, new Listener<JSONObject>() {

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
