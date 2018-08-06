package com.app.xstore.fitting;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.app.model.Customer;
import com.app.model.SimpleGoods;
import com.app.model.response.GetDressDataByNumResponse;
import com.app.net.Commands;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.GoodsDetailActivity;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * 试衣
 * @author pythoner
 * 
 */
public class FittingDetailActivity extends BaseActivity {

	private Context context;
	private String docNum;
	private ListView listView;
	private CommonAdapter<SimpleGoods> adapter;
	private List<SimpleGoods> beans=new ArrayList<SimpleGoods>();
	
	private RadioGroup rg_sex;
	private RadioGroup rg_age;
	private CheckBox cb_foreigner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fitting_detail);
		context = this;
		docNum=getIntent().getStringExtra("DocNum");
		initActionBar("试衣详情", null, null);
		if(isEmpty(docNum)){
			showToast("数据不完整");
			return;
		}
		initViews();
		doCommandGetDressDataByNum();
	}

	private void doCommandGetDressDataByNum(){
		String shop_code= App.user.getShopInfo().getShop_code();
		String doc_num= docNum;
		Commands.doCommandGetDressDataByNum(context, shop_code, doc_num, new Listener<JSONObject>() {
	
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				if(isSuccess(response)){
					GetDressDataByNumResponse obj=mapperToObject(response, GetDressDataByNumResponse.class);
					updateViews(obj);
				}
			}
		});
	}
	
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		rg_sex=$(R.id.rg_sex);
		rg_age=$(R.id.rg_age);
		cb_foreigner=$(R.id.cb_foreigner);
		
		listView=$(R.id.listView);
		listView.setOnItemClickListener(new ListView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent =new Intent(context, GoodsDetailActivity.class);
				intent.putExtra("ProdNum", beans.get(position).getProd_num());
				startActivity(intent);
			}
			
		});
	}

	@Override
	public void updateViews(Object obj) {
		if(obj==null){
			return;
		}
		beans.clear();
		GetDressDataByNumResponse res=(GetDressDataByNumResponse)obj;
		List<SimpleGoods> list=res.getDetailInfo();
		beans.addAll(list);
		notifyDataSetChanged();
		
		List<Customer> customers=res.getCustInfo();
		if(isEmptyList(customers)){
			return;
		}
		try{
			Customer customer=customers.get(0);
			int sex=Integer.parseInt(customer.getSex());
			int age=Integer.parseInt(customer.getAge());
			
			((RadioButton)rg_sex.getChildAt(sex)).setChecked(true);
			((RadioButton)rg_age.getChildAt(age)).setChecked(true);
			
			cb_foreigner.setChecked(customer.isForeigner());
		}catch(Exception e){
			
		}
	}
	
	private void notifyDataSetChanged(){
		if(adapter==null){
			listView.setAdapter(adapter = new CommonAdapter<SimpleGoods>( context, beans,
					  R.layout.item_for_fitting){
					  
					@Override
					public void setValues(ViewHolder helper, final SimpleGoods item, final int position) {
						// TODO Auto-generated method stub
						helper.setText(R.id.item_0, item.getProd_num());
						helper.setText(R.id.item_1, String.valueOf(item.getQty()));
					}
			});
		}else{
			adapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
//		setThemeDrawable(context,R.id.btn_ok);
	}

	
}
