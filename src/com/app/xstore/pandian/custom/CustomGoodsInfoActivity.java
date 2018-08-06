package com.app.xstore.pandian.custom;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.net.Commands;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;

/**
 * 
 * @author pythoner
 * 
 */
public class CustomGoodsInfoActivity extends BaseActivity{

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
			doCommandGetCustomGoodsInfo(goods_sn);
		}
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		tv_content=(TextView)findViewById(R.id.tv_content);
	}
	
	private void doCommandGetCustomGoodsInfo(String goods_sn){
		String shop_code=App.user.getShopInfo().getShop_code();
		Commands.doCommandGetCustomGoodsInfo(context, shop_code, goods_sn, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				if(isSuccess(response)){
					GetCustomGoodsInfoResponse obj=mapperToObject(response, GetCustomGoodsInfoResponse.class);
					if(obj!=null){
						CustomGoods bean=obj.getGoods();
						if(bean!=null){
							StringBuffer sb=new StringBuffer();
							sb.append("SKU码："+bean.getSku_id()).append("\n\n");
							sb.append("内码："+bean.getStyle_inner_id()).append("\n\n");
							sb.append("国际码："+bean.getStyle_outer_id()).append("\n\n");
							sb.append("名称："+bean.getStyle_name()).append("\n\n");
							sb.append("styleID："+bean.getStyle_id()).append("\n\n");
							sb.append("颜色："+bean.getColor_name()).append("\n\n");
							sb.append("尺码："+bean.getSize_name()).append("\n\n");
							tv_content.setText(sb.toString());
						}else{
							tv_content.setText("未查到数据");
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
	
}
