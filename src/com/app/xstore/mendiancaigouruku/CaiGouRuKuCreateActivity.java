package com.app.xstore.mendiancaigouruku;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.net.Commands;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.app.xstore.mendiandiaochu.ChuRuKuProduct;
import com.app.xstore.shangpindangan.ProductDangAn;
import com.app.xstore.shangpindangan.ShangPinDangAnActivity;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;
import com.base.util.D;

/**
 * 
 * @author pythoner
 * 
 */
public class CaiGouRuKuCreateActivity extends BaseActivity implements OnClickListener{

	private TextView tv_jinhuoshuliang,tv_jinhuochengben,tv_xiaolianggusuan,tv_maoligusuan;
	private EditText et_prodID;
	private ListView listView;
	private CommonAdapter<ChuRuKuProduct> adapter;
	private ArrayList<ChuRuKuProduct> beans=new ArrayList<ChuRuKuProduct>();
	private int curPosition = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mendian_caigou_ruku);
		
		initActionBar("采购入库" ,"下一步", null);
		initViews();
		initScanner(new OnScannerResult() {
			
			@Override
			public void onResult(String data) {
				// TODO Auto-generated method stub
				addItemIfNecessary(data);
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
	public void doRightButtonClick(View v) {
		// TODO Auto-generated method stub
		if(isEmptyList(beans)){
			showToast("请扫商品");
			return;
		}
		for(ChuRuKuProduct item:beans){
			if(isEmpty(item.getGoods_name())){
				final String goods_sn=item.getGoods_sn();
				String message="未知商品编码"+goods_sn+";请先完善商品编码等信息，长按可删除。";
				D.showDialog(context, message, "立即维护", "稍后维护", new D.OnPositiveListener() {
					
					@Override
					public void onPositive() {
						// TODO Auto-generated method stub
						Intent intent=new Intent(context,ShangPinDangAnActivity.class);
						intent.putExtra("goodsSn", goods_sn);
						startActivity(intent);
					}
				},new D.OnNegativeListener() {
					
					@Override
					public void onNegative() {
						// TODO Auto-generated method stub
						
					}
				});
				return;
			}
		}
		Intent intent =new Intent(context,CaiGouRuKuCreateSecondActivity.class);
		intent.putParcelableArrayListExtra("ChuRuKuProducts", beans);
		startActivity(intent);
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		tv_jinhuoshuliang = $(R.id.tv_jinhuoshuliang);
		tv_jinhuochengben = $(R.id.tv_jinhuochengben);
		tv_xiaolianggusuan = $(R.id.tv_xiaolianggusuan);
		tv_maoligusuan = $(R.id.tv_maoligusuan);
		
		et_prodID = $(R.id.et_prodID);
		$(R.id.btn_ok).setOnClickListener(this);
		$(R.id.btn_check).setOnClickListener(this);
		$(R.id.btn_clear).setOnClickListener(this);
		
		listView = $(R.id.listView);
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
			}

		});
		listView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				String message="确定要删除吗？";
				D.showDialog(context, message, "确定", "取消", new D.OnPositiveListener() {
					
					@Override
					public void onPositive() {
						// TODO Auto-generated method stub
						beans.remove(position);
						updateViews(beans);
					}
				});
				return true;
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
		
		int qty=0;
		float jinhuochengben=0f;
		float xiaolianggusuan=0f;
		for(ChuRuKuProduct bean:beans){
			qty+=bean.getQty();
			jinhuochengben+=bean.getGoods_jh_price()*bean.getQty();
			xiaolianggusuan+=bean.getGoods_ls_price()*bean.getQty();
		}
		tv_jinhuoshuliang.setText("进货数量："+qty);
		tv_jinhuochengben.setText("进货成本："+jinhuochengben);
		tv_xiaolianggusuan.setText("销售估算："+xiaolianggusuan);
		tv_maoligusuan.setText("毛利估算："+(xiaolianggusuan-jinhuochengben));
		
	}

	private void notifyDataSetChanged() {
		if (adapter == null) {
			listView.setAdapter(adapter = new CommonAdapter<ChuRuKuProduct>(context, beans,R.layout.item_check_product_for_caigouruku) {

				@Override
				public void setValues(ViewHolder helper,
						final ChuRuKuProduct item, final int position) {
					// TODO Auto-generated method stub
					helper.setText(R.id.item_0, item.getGoods_sn()+"\n"+item.getGoods_name());
					if(item.getGoods_jh_price()>0){
						helper.setText(R.id.item_1, String.valueOf(item.getGoods_jh_price()));
					}else{
						helper.setText(R.id.item_1, "");
					}
					
					if(item.getGoods_ls_price()>0){
						helper.setText(R.id.item_2, String.valueOf(item.getGoods_ls_price()));
					}else{
						helper.setText(R.id.item_2, "");
					}
					helper.setText(R.id.item_3, String.valueOf(item.getQty()));
//					View container = helper.getView(R.id.container);
//					if (curPosition == position) {
//						container.setBackgroundColor(0xFFFFCC99);
//					}
				}
			});
		} else {
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		setThemeDrawable(this, R.id.btn_ok);
		setThemeDrawable(this, R.id.btn_check);
		setThemeDrawable(this, R.id.btn_clear);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_ok:
			String prodID=et_prodID.getText().toString().trim();
			if(isEmpty(prodID)){
				doShake(context, et_prodID);
				return;
			}
			addItemIfNecessary(prodID);
			et_prodID.setText("");
			break;
		case R.id.btn_check:
			doCheck();
			break;
		case R.id.btn_clear:
			String message="确定要清空吗？";
			D.showDialog(context, message, "确定", "取消", new D.OnPositiveListener() {
				
				@Override
				public void onPositive() {
					// TODO Auto-generated method stub
					beans.clear();
					updateViews(beans);
				}
			});
			break;

		default:
			break;
		}
	}

	private void addItemIfNecessary(String goods_sn){
		if(goods_sn.length()<6){
			showToast("商品编码有误");
			return;
		}
		boolean isExist=false;
		for(ChuRuKuProduct bean:beans){
			if(bean.getGoods_sn().equals(goods_sn)){
				isExist=true;
				bean.setQty(bean.getQty()+1);
				break;
			}
		}
		if(!isExist){//不存在
			ChuRuKuProduct bean=new ChuRuKuProduct();
			bean.setGoods_name("");
			bean.setGoods_sn(goods_sn);
			bean.setQty(1);
			beans.add(bean);
		}
		updateViews(beans);
		
		if(!isExist){//不存在，请求数据补全
			List<String> goodsSns=new ArrayList<String>();
			goodsSns.add(goods_sn);
			doCommandGetGoodsListBySKUs(goodsSns);
		}
	}
	
	private void doCheck(){
		if(isEmptyList(beans)){
			return;
		}
		List<String> goodsSns=new ArrayList<String>();
		for(ChuRuKuProduct item:beans){
			goodsSns.add(item.getGoods_sn());
		}
		doCommandGetGoodsListBySKUs(goodsSns);
		
	}
	
	private void doCommandGetGoodsListBySKUs(final List<String> goodsSns){
		Commands.doCommandGetGoodsListBySKUs(context, goodsSns, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				Log.i("tag", response.toString());
				if(isSuccess(response)){
					GetGoodsListBySKUsResponse obj=mapperToObject(response, GetGoodsListBySKUsResponse.class);
					if(obj!=null&&obj.getGoodsInfo()!=null){
						if(obj.getGoodsInfo().size()==0){//没有匹配到
							if(!isFinishing()){
								StringBuffer sb=new StringBuffer();
								for(String sn:goodsSns){
									sb.append(sn).append(";");
								}
								String message="未知商品编码"+sb.toString()+"请先完善商品编码等信息，长按可删除。";
								D.showDialog(context, message, "立即维护", "稍后维护", new D.OnPositiveListener() {
									
									@Override
									public void onPositive() {
										// TODO Auto-generated method stub
										Intent intent=new Intent(context,ShangPinDangAnActivity.class);
										intent.putExtra("goodsSn", goodsSns.get(0));
										startActivity(intent);
									}
								},new D.OnNegativeListener() {
									
									@Override
									public void onNegative() {
										// TODO Auto-generated method stub
										
									}
								});
							}
						}else{
							up:for(ChuRuKuProduct item:beans){
								for(ProductDangAn product:obj.getGoodsInfo()){
									if(item.getGoods_sn().equals(product.getGoods_sn())){
										item.setGoods_name(product.getGoods_name());
										item.setGoods_jh_price(product.getGoods_jh_price());
										item.setGoods_ls_price(product.getGoods_ls_price());
										continue up;
									}
								}
							}
							updateViews(beans);
						}
					}
				}
			}
		});
	}
	
	@Subscriber
	void updateByEventBus(String event) {
		if (event.equals(App.EVENT_FINISH)) {
			finish();
		}
	}
	
}
