package com.app.xstore.inventory;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.model.Box;
import com.app.net.Commands;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.pandian.custom.CustomGoods;
import com.app.xstore.pandian.custom.CustomGoodsInfoActivity;
import com.app.xstore.pandian.custom.GetCustBoxInfoByCodeResponse;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;

/**
 * 箱码
 * @author pythoner
 * 
 */
public class BoxDetailActivity extends BaseActivity implements OnClickListener{

	private Context context;
	private Box box;
	private TextView tv_boxCode,tv_productNum;
	private ListView listView;
	private CommonAdapter<CustomGoods> adapter;
	private List<CustomGoods> beans=new ArrayList<CustomGoods>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_box_detail);
		context = this;
		initActionBar("货箱详情", null,null);
		box=getIntent().getParcelableExtra("Box");
		if(box==null){
			return;
		}
		initViews();
//		doCommandGetCustBoxInfoByCode("01505240020");
		doCommandGetCustBoxInfoByCode(box.getCode());
	}

	private void doCommandGetCustBoxInfoByCode(String box_code){
		String shop_code=App.user.getShopInfo().getShop_code();
		Commands.doCommandGetCustBoxInfoByCode(context, shop_code, box_code, new Listener<JSONObject>(){

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				if (isSuccess(response)) {
					GetCustBoxInfoByCodeResponse obj = mapperToObject(response, GetCustBoxInfoByCodeResponse.class);
					if(obj!=null&&!isEmptyList(obj.getGoods())){
						beans.clear();
						beans.addAll(obj.getGoods());
						updateViews(beans);
					}else{
						showToast("没有货箱数据");
					}
				}
			}
			
		});
	}
	
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		tv_boxCode=$(R.id.tv_boxCode);
		tv_productNum=$(R.id.tv_productNum);
		
		listView=$(R.id.listView);
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context,CustomGoodsInfoActivity.class);
				intent.putExtra("ProdNum", beans.get(position).getSku_id());
				startActivity(intent);
			}

		});
	}
	
	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
		if(obj==null){
			return;
		}
		notifyDataSetChanged();
		tv_boxCode.setText("货箱号："+box.getCode());
		tv_productNum.setText("商品总数："+beans.size());
	}

	private void notifyDataSetChanged(){
		if(adapter==null){
			listView.setAdapter(adapter = new CommonAdapter<CustomGoods>( context, beans,
					  R.layout.item_box_product){
					  
					@Override
					public void setValues(ViewHolder helper, CustomGoods item, int position) {
						// TODO Auto-generated method stub
						helper.setText(R.id.item_0, item.getLsh().trim());
						helper.setText(R.id.item_1, item.getStyle_name());
						helper.setText(R.id.item_2, "1");
					}
			});
		}else{
			adapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}
	
}
