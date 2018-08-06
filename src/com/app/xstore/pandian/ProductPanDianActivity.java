package com.app.xstore.pandian;

import java.util.List;

import org.litepal.crud.DataSupport;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;

/**
 * 商品盘点信息
 * @author pythoner
 * 
 */
public class ProductPanDianActivity extends BaseActivity{

	private Context context;
	private TextView tv_content,tv_total;
	private ListView listView;
	private Product product;
	private CommonAdapter<PanDianProduct> adapter;
	private List<PanDianProduct> beans;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_pandian);
		context = this;
		initActionBar("商品盘点信息", null, null);
		initViews();
		String barCode=getIntent().getStringExtra("BarCode");
		if(!isEmpty(barCode)){
			beans=DataSupport.where("barCode = ?", barCode).find(PanDianProduct.class,true);
			product=DataSupport.where("barCode = ?",barCode).findFirst(Product.class);
			updateViews(product);
		}
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		tv_content=(TextView)findViewById(R.id.tv_content);
		tv_total=(TextView)findViewById(R.id.tv_total);
		listView=(ListView)findViewById(R.id.listView);
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
		if(beans!=null){
			int total=0;
			for(PanDianProduct p:beans){
				total+=p.getQty();
			}
			tv_total.setText("总扫描数："+total);
			notifyDataSetChanged();
		}
	}
	
	private void notifyDataSetChanged(){
		if(adapter==null){
			listView.setAdapter(adapter = new CommonAdapter<PanDianProduct>( context, beans,
					  R.layout.item_product_pandian){
					  
					@Override
					public void setValues(ViewHolder helper, final PanDianProduct item, final int position) {
						// TODO Auto-generated method stub
						if(item.getPanDianDan()!=null){
							helper.setText(R.id.item_0,item.getPanDianDan().getHw());
						}else{
							helper.setText(R.id.item_0,"【已删除】");
						}
						helper.setText(R.id.item_1, String.valueOf(item.getQty()));
						
					}
			});
		}else{
			adapter.notifyDataSetChanged();
		}
	}
	
}
