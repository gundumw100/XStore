package com.app.xstore;

import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.app.model.Goods;
import com.app.model.response.GetGoodsInfoReponse;
import com.app.net.Commands;
import com.app.xstore.R;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * 
 * @author pythoner
 * 
 */
@Deprecated
public class GoodsDetailActivity extends BaseActivity{

	private Context context;
	private TextView tv_content;
	private String goods_sn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_detail);
		context = this;
		initActionBar("商品详情", null, null);
		initViews();
		goods_sn=getIntent().getStringExtra("ProdNum");
		if(!isEmpty(goods_sn)){
			doCommandGetGoodsInfo(goods_sn);
		}
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		tv_content=(TextView)findViewById(R.id.tv_content);
		
	}
	
	private void doCommandGetGoodsInfo(String goods_sn){
		String shop_code=App.user.getShopInfo().getShop_code();
		Commands.doCommandGetGoodsInfo(context, shop_code, goods_sn, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				if(isSuccess(response)){
					GetGoodsInfoReponse obj=mapperToObject(response, GetGoodsInfoReponse.class);
					if(obj!=null){
						Goods bean=obj.getGoods();
						if(bean!=null){
							tv_content.setText("编码："+bean.getGoods_sn()+"\n\n名称："+bean.getGoods_name()+"\n\n描述："+bean.getGoods_desc());
						}
					}
				}
			}
		});
	}
	
	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		updateTheme();
	}
	
	private void updateTheme() {
	}
	
}
