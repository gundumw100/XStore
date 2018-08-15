package com.app.xstore.mendiankucun;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.net.Commands;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.app.xstore.mendiancaigouruku.GetGoodsListBySKUsResponse;
import com.app.xstore.mendiandiaochu.ChuRuKuProduct;
import com.app.xstore.shangpindangan.GetGoodsColorImageListResponse;
import com.app.xstore.shangpindangan.ProdColorImage;
import com.app.xstore.shangpindangan.ProductDangAn;
import com.app.xstore.shangpindangan.ProductQueryByParamsActivity;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;
import com.widget.common.recycler.BaseRecyclerAdapter;
import com.widget.common.recycler.SpacesItemDecoration;
import com.widget.flowlayout.FlowLayout;
import com.widget.flowlayout.TagAdapter;

/**
 * 
 * @author pythoner
 * 
 */
public class KuCunQueryBySkuActivity extends BaseActivity {

	private EditText et_productSku;
	private TextView tv_name,tv_kucun,tv_year,tv_zaitu;
	private ListView listView;
	private CommonAdapter<ChuRuKuProduct> adapter;
	private ArrayList<ChuRuKuProduct> beans=new ArrayList<ChuRuKuProduct>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kucun_query_by_sku);
		context = this;
		initActionBar("库存查询", null, null);
		initViews();
		initScanner(new OnScannerResult() {
			
			@Override
			public void onResult(String data) {
				// TODO Auto-generated method stub
				et_productSku.setText(data);
				doCommandGetStockBySKUList();
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
		tv_name=$(R.id.tv_name);
		tv_kucun=$(R.id.tv_kucun);
		tv_year=$(R.id.tv_year);
		tv_zaitu=$(R.id.tv_zaitu);
		$(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doCommandGetStockBySKUList();
			}
		});
		$(R.id.btn_favorites).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,MyFavoritesActivity.class);
				startActivityForResult(intent, 1000);
				
			}
		});
		$(R.id.btn_more_params).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,ProductQueryByParamsActivity.class);
				intent.putExtra("Function", "cunku");
				startActivity(intent);
			}
		});
		initListView();
		initRecyclerView();
		
		String styleCode=getIntent().getStringExtra("StyleCode");
		if(isEmpty(styleCode)){
//			et_productSku.setText("180030");//debug
		}else{
			et_productSku.setText(styleCode);
			doCommandGetStockBySKUList();
		}
		
	}

	private void doCommandGetStockBySKUList(){
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
		productSku=productSku.substring(0, 6);
		doCommandGetStockBySKUList(productSku);
	}
	
	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
		if(beans.size()==0){
			if(listView.getEmptyView()==null){
				setEmptyView(listView, "扫一扫\n查询商品库存");
			}
		}
		updateHeadViews();
		notifyDataSetChanged();
	}

	private void updateHeadViews(){
		if(!isEmptyList(mergedBeans)){
			ChuRuKuProduct cur=mergedBeans.get(curPosition);
			tv_name.setText(cur.getGoods_sn().substring(0, 6)+" "+cur.getGoods_name());
			tv_year.setText("20"+cur.getGoods_sn().substring(0, 2)+" "+cur.getGoods_season_desc());
//			tv_kucun.setText("库存："+cur.getStock());
//			tv_zaitu.setText("在途："+cur.getOnline_stock());
			adapterRecyclerView.notifyDataSetChanged();
		}
		int stock=0;
		int stockOnline=0;
		for(ChuRuKuProduct item:beans){
			stock+=item.getStock();
			stockOnline+=item.getOnline_stock();
		}
		tv_kucun.setText("库存："+stock);
		tv_zaitu.setText("在途："+stockOnline);
		
	}
	
	private void initListView(){
		listView=$(R.id.listView);
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				// TODO Auto-generated method stub
			}
		});
		updateViews(beans);
	}
	
	private BaseRecyclerAdapter<ChuRuKuProduct> adapterRecyclerView;
	private void initRecyclerView(){
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
		// recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
		recyclerView.addItemDecoration(new SpacesItemDecoration(10));
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false);
		recyclerView.setLayoutManager(manager);
		
		recyclerView.setAdapter(adapterRecyclerView=new BaseRecyclerAdapter<ChuRuKuProduct>(context, mergedBeans) {

			@Override
			public int onCreateViewLayoutID(int viewType) {
				// TODO Auto-generated method stub
				return R.layout.item_for_recyclerview_kucun;
			}

			@Override
			public void onBindViewHolder(com.widget.common.recycler.ViewHolder holder,ChuRuKuProduct item, int position) {
				// TODO Auto-generated method stub
				ImageView item_0=holder.getView(R.id.item_0);
				loadMultImageByPicasso(item.getGoods_color_image(),item_0);
				
				holder.setText(R.id.item_1,item.getGoods_color_desc());
				holder.setText(R.id.item_2,""+item.getStock());
				
				if(curPosition==position){
					item_0.setBackgroundResource(R.drawable.bg_frame_blue);
				}else{
					item_0.setBackgroundColor(Color.TRANSPARENT);
				}
			}
		});

		adapterRecyclerView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				curPosition=position;
				adapterRecyclerView.notifyDataSetChanged();
				startProductDetailActivity(mergedBeans.get(position).getGoods_sn());
//				updateHeadViews();
			}
		});
		
	}
	private void notifyDataSetChanged(){
		if(adapter==null){
			listView.setAdapter(adapter = new CommonAdapter<ChuRuKuProduct>( context, beans,
					  R.layout.item_for_mendiankucun){
					  
					@Override
					public void setValues(ViewHolder helper, final ChuRuKuProduct item, final int position) {
						// TODO Auto-generated method stub
						helper.setText(R.id.item_0,item.getGoods_sn());
						helper.setText(R.id.item_1,item.getGoods_color_desc());
						helper.setText(R.id.item_2,item.getGoods_spec_desc());
						helper.setText(R.id.item_3,String.valueOf(item.getStock()));
						helper.setText(R.id.item_4,String.valueOf(item.getOnline_stock()));
						
					}
			});
		}else{
			adapter.notifyDataSetChanged();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==1000){
			if(resultCode==0){
				if(data!=null){
					String goods_sn=data.getStringExtra(MyFavoritesActivity.GOODS_SN);
					et_productSku.setText(goods_sn);
					doCommandGetStockBySKUList();
				}
			}
		}
	}
	
	private void doCommandGetStockBySKUList(String goods_sn){
		List<String> goodsSnList=new ArrayList<String>();
		goodsSnList.add(goods_sn);
		Commands.doCommandGetStockBySKUList(context, goodsSnList, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				if(isSuccess(response)){
					GetStockBySKUListResponse obj=mapperToObject(response, GetStockBySKUListResponse.class);
					if(obj!=null&&!isEmptyList(obj.getInfo())){
						beans.clear();
						beans.addAll(obj.getInfo());
//						updateViews(beans);
						List<String> goodsSns=new ArrayList<String>();
						for(ChuRuKuProduct item:beans){
							goodsSns.add(item.getGoods_sn());
						}
						doCommandGetGoodsListBySKUs(goodsSns);
						
					}else{
						showToast("未查询到库存");
					}
				}
			}
		});
	}
	
	private void doCommandGetGoodsListBySKUs(final List<String> goodsSns){
		Commands.doCommandGetGoodsListBySKUs(context, goodsSns, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				if(isSuccess(response)){
					GetGoodsListBySKUsResponse obj=mapperToObject(response, GetGoodsListBySKUsResponse.class);
					if(obj!=null&&obj.getGoodsInfo()!=null){
						completingItems(obj.getGoodsInfo());
						mergeItems();
						updateViews(beans);
					}
				}
			}
		});
	}
	
	private void completingItems(List<ProductDangAn> newBeans){
		up:for(ChuRuKuProduct item:beans){
			for(ProductDangAn product:newBeans){
				if(item.getGoods_sn().equals(product.getGoods_sn())){
					item.setGoods_name(product.getGoods_name());
					item.setGoods_jh_price(product.getGoods_jh_price());
					item.setGoods_ls_price(product.getGoods_ls_price());
					item.setGoods_color(product.getGoods_color());
					item.setGoods_spec(product.getGoods_spec());
					item.setGoods_color_desc(isEmpty(product.getGoods_color_desc())?"均色":product.getGoods_color_desc());
					item.setGoods_spec_desc(isEmpty(product.getGoods_spec_desc())?"均码":product.getGoods_spec_desc());
					item.setGoods_season_desc(isEmpty(product.getGoods_season_desc())?"全年":product.getGoods_season_desc());
					item.setGoods_sort_desc(product.getGoods_sort_desc());
					item.setGoods_img(product.getGoods_img());
					item.setGoods_color_image(product.getGoods_color_image());
					continue up;
				}
			}
		}
	}
	
	private int curPosition=0;
	private List<ChuRuKuProduct> mergedBeans=new ArrayList<ChuRuKuProduct>();
	private Map<String,ChuRuKuProduct> map=new HashMap<String,ChuRuKuProduct>();
	//根据颜色归类统计数量
	private void mergeItems(){
		//需要开辟新内存，用于叠加不改变原始数据
		ArrayList<ChuRuKuProduct> beansBak=new ArrayList<ChuRuKuProduct>();
		for(ChuRuKuProduct bean:beans){
			ChuRuKuProduct item=new ChuRuKuProduct();
			item.setStock(bean.getStock());
			item.setOnline_stock(bean.getOnline_stock());
			item.setGoods_sn(bean.getGoods_sn());
			item.setGoods_name(bean.getGoods_name());
			item.setGoods_jh_price(bean.getGoods_jh_price());
			item.setGoods_ls_price(bean.getGoods_ls_price());
			item.setGoods_color_desc(bean.getGoods_color_desc());
			item.setGoods_color(bean.getGoods_color());
			item.setGoods_spec_desc(bean.getGoods_spec_desc());
			item.setGoods_spec(bean.getGoods_spec());
			item.setGoods_season_desc(bean.getGoods_season_desc());
			item.setGoods_sort_desc(bean.getGoods_sort_desc());
//			item.setGoods_img(bean.getGoods_img());
//			item.setGoods_color_image(bean.getGoods_color_image());
			beansBak.add(item);
		}
		
		curPosition=0;
		mergedBeans.clear();
		map.clear();
		for(ChuRuKuProduct item:beansBak){
			if(map.get(item.getGoods_color())==null){
				map.put(item.getGoods_color(), item);
				mergedBeans.add(item);
			}else{
				ChuRuKuProduct product=map.get(item.getGoods_color());
//				if(isEmpty(product.getGoods_color_image())){//如果当前没有颜色图片，则用后面的替代
//					product.setGoods_color_image(item.getGoods_color_image());
//				}
				product.setStock(item.getStock()+product.getStock());
				product.setOnline_stock(item.getOnline_stock()+product.getOnline_stock());
			}
		}
		
		//请求颜色图片数据，补全颜色图片数据
		doCommandGetGoodsColorImageList();
	}
	
	private void doCommandGetGoodsColorImageList(){
		String productSku=et_productSku.getText().toString().trim();
		String styleCode=productSku.substring(0,6);
		String colorCode=null;
		Commands.doCommandGetGoodsColorImageList(context, styleCode, colorCode, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					GetGoodsColorImageListResponse obj=mapperToObject(response, GetGoodsColorImageListResponse.class);
					if(obj!=null&&obj.getInfo()!=null){
						List<ProdColorImage> colorImageList=obj.getInfo().getImageInfo();
						if(!isEmptyList(colorImageList)){
							//匹配数据，补全图片url
							up:for(ChuRuKuProduct bean:mergedBeans){
								for(ProdColorImage img:colorImageList){
									if(!isEmpty(img.getImgUrl())&&bean.getGoods_color().equals(img.getColorCode())){//如果当前没有颜色图片，则用后面的替代
										bean.setGoods_color_image(img.getImgUrl());
										continue up;
									}
								}
							}
							adapterRecyclerView.notifyDataSetChanged();
						}
					}
				}
			}
		});
	}
	
}
