package com.app.xstore.pandian;

import org.litepal.crud.DataSupport;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.app.xstore.BaseActivity;
import com.app.xstore.R;

/**
 * 
 * @author pythoner
 * 
 */
public class ProductActivity extends BaseActivity{

	private Context context;
	private TextView tv_content;
	private Product product;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product);
		context = this;
		initActionBar("商品详情", null, null);
		initViews();
		String barCode=getIntent().getStringExtra("BarCode");
		if(!isEmpty(barCode)){
			//barCode=6908741125362
			product=DataSupport.where("barCode = ?",barCode).findFirst(Product.class);
			updateViews(product);
		}
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		tv_content=(TextView)findViewById(R.id.tv_content);
	}
	
	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
		if(product!=null){
//			tv_content.append("\n核算码：" + product.getAccountingCode());
			tv_content.append("\n款号：" + product.getPatternCode());
			tv_content.append("\n条码：" + product.getBarCode());
			tv_content.append("\n颜色：" + product.getColor());
			tv_content.append("\n码数：" + product.getSize());
			tv_content.append("\n名称：" + product.getName());
			tv_content.append("\n标签价：" + product.getPrice());
			tv_content.append("\n罩杯：" + product.getCup());
			tv_content.append("\n包装含量：" + product.getQuantity());
		}else{
			showToast("product is null");
		}
	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
	}
	
}
