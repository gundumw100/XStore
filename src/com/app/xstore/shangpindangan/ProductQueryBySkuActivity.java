package com.app.xstore.shangpindangan;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.net.Commands;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.app.xstore.mendiancaigouruku.GetGoodsListBySKUsResponse;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;

/**
 * 
 * @author pythoner
 * 
 */
public class ProductQueryBySkuActivity extends BaseActivity {

	private EditText et_productSku;
	private ListView listView;
	private CommonAdapter<ProductDangAn> adapter;
	private List<ProductDangAn> beans=new ArrayList<ProductDangAn>();
	private int curIndex=0;
	private TextView btn_clear;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shangpindangan_product_query_by_sku);
		context = this;
		initActionBar("商品查询", null, null);
		initViews();
		initScanner(new OnScannerResult() {
			
			@Override
			public void onResult(String data) {
				// TODO Auto-generated method stub
				et_productSku.setText(data);
				startProductQueryListActivity();
			}
		});
		createFloatView(16);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		removeFloatView();
	}
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		et_productSku=$(R.id.et_productSku);
		$(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startProductQueryListActivity();
			}
		});
		$(R.id.btn_more_params).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,ProductQueryByParamsActivity.class);
				startActivity(intent);
			}
		});
		initListView();
		
		RadioGroup rg=$(R.id.rg);
		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rb_0:
					curIndex=0;
					loadProductDangAns();
					break;
				case R.id.rb_1:
					curIndex=1;
					doCommandGetGoodsListBySjDate();
					break;
				case R.id.rb_2:
					curIndex=2;
					doCommandGetCollectGoodsList();
					break;
				}
			}
		});
//		rg.check(R.id.rb_0);//执行两次
		((RadioButton)rg.findViewById(R.id.rb_0)).setChecked(true);
		
	}

	private void startProductQueryListActivity(){
		String productSku=et_productSku.getText().toString().trim();
		if(isEmpty(productSku)){
			doShake(context, et_productSku);
			return;
		}
		if(productSku.length()<6){
			showToast("款码长度至少6位");
			doShake(context, et_productSku);
			return;
		}
		Intent intent=new Intent(context,ProductQueryListBySkuActivity.class);
		intent.putExtra("ProductSku", productSku);
		startActivity(intent);
	}
	
	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
		if(beans.size()==0){
			if(listView.getEmptyView()==null){
				setEmptyView(listView, "暂无数据");
			}
		}
		if(curIndex==0){
			btn_clear.setVisibility(beans.size()>0?View.VISIBLE:View.GONE);
		}else{
			btn_clear.setVisibility(View.GONE);
		}
		notifyDataSetChanged();
	}

	private void initListView(){
		btn_clear=$(R.id.btn_clear);
		btn_clear.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DataSupport.deleteAll(ProductDangAnRecentlyBrowse.class);
				beans.clear();
				updateViews(beans);
			}
		});
		listView=$(R.id.listView);
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				// TODO Auto-generated method stub
				ProductDangAn bean=beans.get(position);
				startProductDetailActivity(bean.getGoods_sn());
			}
		});
		
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
						if(curIndex==0){
							item_delete.setVisibility(View.VISIBLE);
							item_delete.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									DataSupport.deleteAll(ProductDangAnRecentlyBrowse.class, "goods_sn = ?",item.getGoods_sn());
									beans.remove(item);
									updateViews(beans);
								}
							});
						}else if(curIndex==1){
							item_delete.setVisibility(View.GONE);
						}else if(curIndex==2){
							item_delete.setVisibility(View.VISIBLE);
							item_delete.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									doCommandDelCollectGoods(item);
								}
							});
						}
						
					}
			});
		}else{
			adapter.notifyDataSetChanged();
		}
	}
	
	private void doCommandGetGoodsListBySjDate(){
		Commands.doCommandGetGoodsListBySjDate(context, new Listener<JSONObject>() {

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
						
						doCommandGetGoodsListBySKUs();
					}
				}
			}
		});
	}
	
	private void loadProductDangAns(){
		beans.clear();
		List<ProductDangAnRecentlyBrowse> list=DataSupport.order("timeMillis desc").limit(50).find(ProductDangAnRecentlyBrowse.class);
		if(list!=null){
			for(ProductDangAnRecentlyBrowse item:list){
				ProductDangAn bean=new ProductDangAn();
				bean.setGoods_sn(item.getGoods_sn());
				bean.setGoods_name(item.getGoods_name());
				bean.setGoods_thumb(item.getGoods_thumb());
				bean.setBrand_name(item.getBrand_name());
				bean.setGoods_brand(item.getGoods_brand());
				bean.setGoods_ls_price(item.getGoods_ls_price());
				bean.setGoods_img(item.getGoods_img());
				beans.add(bean);
			}
		}
		updateViews(beans);
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
						
						doCommandGetGoodsListBySKUs();
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
	private void doCommandGetGoodsListBySKUs(){
		List<String> goodsSns=new ArrayList<String>();
		for(ProductDangAn bean:beans){
			goodsSns.add(bean.getGoods_sn());
		}
		
		Commands.doCommandGetGoodsListBySKUs(context, goodsSns, new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					GetGoodsListBySKUsResponse obj=mapperToObject(response, GetGoodsListBySKUsResponse.class);
					if(obj!=null&&obj.getGoodsInfo()!=null){
						up:for(ProductDangAn bean:beans){
							for(ProductDangAn item:obj.getGoodsInfo()){
								if(!isEmpty(item.getGoods_sn())&&bean.getGoods_sn().equals(item.getGoods_sn())){
									bean.setGoods_img(item.getGoods_img());
									continue up;
								}
							}
						}
						notifyDataSetChanged();
					}
				}
			}
		});
	}
	
}
