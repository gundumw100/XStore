package com.app.xstore.mendiandiaochu;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.model.ShopInfo;
import com.app.net.Commands;
import com.app.widget.SimpleListPopupWindow;
import com.app.widget.SimpleNumberDialog;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.app.xstore.mendiancaigouruku.GetGoodsListBySKUsResponse;
import com.app.xstore.shangpindangan.ProductDangAn;
import com.app.xstore.shangpindangan.ShangPinDangAnActivity;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;
import com.base.util.D;

/**
 * 
 * 
 * @author pythoner
 * 
 */
public class MenDianDiaoChuCreateActivity extends BaseActivity implements
		OnClickListener {

	private TextView tv_store,tv_scanQty,tv_kuaidi;
	private TextView et_kuaidi;
	private EditText et_prodID;
	private ListView listView;
	private CommonAdapter<ChuRuKuProduct> adapter;
	private ArrayList<ChuRuKuProduct> beans=new ArrayList<ChuRuKuProduct>();
	private int curPosition = -1;
	private boolean isCheckOnly;
	private ChuRuKuHead head;
	private List<Express> expressList=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mendian_diaochu_create);
		isCheckOnly=getIntent().getBooleanExtra("isCheckOnly", false);//查看详情标志
		head=getIntent().getParcelableExtra("ChuRuKuHead");
		
		initActionBar(isCheckOnly?"调出单详情":"门店调出" ,isCheckOnly?null:"下一步", null);
		initViews();
		if(!isCheckOnly){
			initScanner(new OnScannerResult() {
				
				@Override
				public void onResult(String data) {
					// TODO Auto-generated method stub
					addItemIfNecessary(data);
				}
			});
			createFloatView(16);
		}else{//请求详情
			if(head!=null){
				doCommandGetOutStorageDetailList(head.getShopCode(),head.getDoc_code());
			}
		}
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
		if(tv_store.getTag()==null){
			showToast("请选择收货门店");
			doShake(context, tv_store);
			return;
		}
		
		List<String> goodsSns=new ArrayList<String>();
		for(ChuRuKuProduct item:beans){
			if(isEmpty(item.getGoods_name())){
				goodsSns.add(item.getGoods_sn());
			}
		}
		if(goodsSns.size()>0){//存在未检验的商品
			doCommandGetGoodsListBySKUs_2(goodsSns);
		}else{//都检验过了
			startMenDianDiaoChuCreateSecondActivity();
		}
	}

	private void startMenDianDiaoChuCreateSecondActivity(){
		Intent intent =new Intent(context,MenDianDiaoChuCreateSecondActivity.class);
		intent.putParcelableArrayListExtra("ChuRuKuProducts", beans);
		intent.putExtra("ShopInfo", (ShopInfo)(tv_store.getTag()));
		startActivity(intent);
	}
	
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		tv_store = $(R.id.tv_store);
		if(isCheckOnly){
			tv_store.setText(head.getTarShopName());
//			tv_store.setTag(head.getTar_code());
		}else{
			tv_store.setOnClickListener(this);
		}
		tv_scanQty = $(R.id.tv_scanQty);
		et_prodID = $(R.id.et_prodID);

		listView = $(R.id.listView);
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
			}

		});
		if(!isCheckOnly){
			LinearLayout unCheckContainer = $(R.id.unCheckContainer);
			unCheckContainer.setVisibility(View.VISIBLE);
			$(R.id.btn_ok).setOnClickListener(this);
			$(R.id.btn_check).setOnClickListener(this);
			$(R.id.btn_clear).setOnClickListener(this);
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
		}else{
			
			LinearLayout checkContainer = $(R.id.checkContainer);
			checkContainer.setVisibility(View.VISIBLE);
			tv_kuaidi = $(R.id.tv_kuaidi);
			tv_kuaidi.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showExpressListPopupWindow();
				}
			});
			et_kuaidi = $(R.id.et_kuaidi);
			$(R.id.iv_scan).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					doScan(resultHandler);
				}
			});
			$(R.id.btn_kuaidi).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//debug
					showToast("wait");
				}
			});
			TextView tv_remark = $(R.id.tv_remark);
			
			et_kuaidi.setText(head.getExp_num());
			tv_remark.setText("备注："+head.getRemark());
			
			initScanner(new OnScannerResult() {
				
				@Override
				public void onResult(String data) {
					// TODO Auto-generated method stub
					et_kuaidi.setText(data);
				}
			});

			doCommandGetExpressList();//请求
		}

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
		for(ChuRuKuProduct bean:beans){
			qty+=bean.getQty();
		}
		tv_scanQty.setText("扫描数："+qty);
	}

	private void notifyDataSetChanged() {
		if (adapter == null) {
			listView.setAdapter(adapter = new CommonAdapter<ChuRuKuProduct>(context, beans,R.layout.item_check_product_for_mendiandiaochu) {

				@Override
				public void setValues(ViewHolder helper,
						final ChuRuKuProduct item, final int position) {
					// TODO Auto-generated method stub
					helper.setText(R.id.item_0, item.getGoods_sn());
					helper.setText(R.id.item_1, item.getGoods_name());
					
					TextView item_2=helper.getView(R.id.item_2);
					item_2.setText(String.valueOf(item.getQty()));
					if(!isCheckOnly){
						item_2.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								SimpleNumberDialog d = new SimpleNumberDialog(context, "", "数量");
								d.setOnClickListener(new SimpleNumberDialog.OnClickListener() {
									
									@Override
									public void onClick(View v, String text) {
										// TODO Auto-generated method stub
										item.setQty(Integer.parseInt(text));
										updateViews(beans);
									}
								});
								d.show();
							}
						});
					}
					
					
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
		setThemeDrawable(this, R.id.btn_kuaidi);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_store:
			doCommandGetOtherShopByCode();
			break;
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
		if(!isExist){
			ChuRuKuProduct bean=new ChuRuKuProduct();
			bean.setGoods_name("");
			bean.setGoods_sn(goods_sn);
			bean.setQty(1);
			beans.add(bean);
		}
		updateViews(beans);
	}
	
	private void doCheck(){
		if(isEmptyList(beans)){
			return;
		}
		List<String> goodsSns=new ArrayList<String>();
		for(ChuRuKuProduct item:beans){
			if(isEmpty(item.getGoods_name())){
				goodsSns.add(item.getGoods_sn());
			}
		}
		if(goodsSns.size()>0){
			doCommandGetGoodsListBySKUs(goodsSns);
		}
	}
	
	private void doCommandGetOtherShopByCode(){
		Commands.doCommandGetOtherShopByCode(context, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				Log.i("tag", response.toString());
				if(isSuccess(response)){
					GetOtherShopByCodeResponse obj=mapperToObject(response, GetOtherShopByCodeResponse.class);
					if(obj!=null&&obj.getShopInfo()!=null){
						if(!isFinishing()){
							List<ShopInfo> list=obj.getShopInfo();
							View view=LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
							SimpleListPopupWindow<ShopInfo> p=new SimpleListPopupWindow<ShopInfo>(context, view, tv_store.getWidth(), list);
							p.setOnItemClickListener(new SimpleListPopupWindow.OnItemClickListener<ShopInfo>() {
								
								@Override
								public void onItemClick(int position, ShopInfo item) {
									// TODO Auto-generated method stub
									tv_store.setText(item.getShop_name());
									tv_store.setTag(item);
								}
							});
							p.showAsDropDown(tv_store);
						}
					}else{
						showToast("没有其他门店");
					}
				}
			}
		});
	}
	
	private void doCommandGetOutStorageDetailList(String srcShopCode,String docCode){
		Commands.doCommandGetOutStorageDetailList(context,srcShopCode, docCode, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				Log.i("tag", response.toString());
				if(isSuccess(response)){
					GetOutStorageDetailListResponse obj=mapperToObject(response, GetOutStorageDetailListResponse.class);
					if(obj!=null&&obj.getInfo()!=null){
						beans.clear();
						beans.addAll(obj.getInfo());
						updateViews(beans);
					}
				}
			}
		});
	}
	
	private void doCommandGetExpressList(){
		Commands.doCommandGetExpressList(context, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				if(isSuccess(response)){
					GetExpressListResponse obj=mapperToObject(response, GetExpressListResponse.class);
					if(obj!=null&&obj.getInfo()!=null){
						expressList=obj.getInfo();
						Express item=new Express();
						item.setDescription("<无需快递>");
						item.setExpCode("");
						expressList.add(0,item);
						
						for(Express bean:expressList){
							if(bean.getExpCode().equals(head.getExpType_code())){
								tv_kuaidi.setText(bean.getDescription());
								break;
							}
						}
						
						
					}
				}
			}
		});
	}
	
	private void showExpressListPopupWindow(){
		if(expressList==null){
			return;
		}
		View view=LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
		SimpleListPopupWindow<Express> p=new SimpleListPopupWindow<Express>(context, view, tv_kuaidi.getWidth(), expressList);
		p.setOnItemClickListener(new SimpleListPopupWindow.OnItemClickListener<Express>() {

			@Override
			public void onItemClick(int position, Express item) {
				// TODO Auto-generated method stub
				if(position==0){
					tv_kuaidi.setText("");
					tv_kuaidi.setTag(null);
				}else{
					tv_kuaidi.setText(item.getDescription());
					tv_kuaidi.setTag(item.getExpCode());
				}
				
			}
		});
		p.showAsDropDown(tv_kuaidi);
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
							showDialogIfNecessary();
						}else{
							completingItems(obj.getGoodsInfo());
							updateViews(beans);
						}
					}
				}
			}
		});
	}
	
	private void doCommandGetGoodsListBySKUs_2(final List<String> goodsSns){
		Commands.doCommandGetGoodsListBySKUs(context, goodsSns, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				Log.i("tag", response.toString());
				if(isSuccess(response)){
					GetGoodsListBySKUsResponse obj=mapperToObject(response, GetGoodsListBySKUsResponse.class);
					if(obj!=null&&obj.getGoodsInfo()!=null){
						if(obj.getGoodsInfo().size()==0){//没有匹配到
						}else{
							completingItems(obj.getGoodsInfo());
							updateViews(beans);
						}
						//检测是不是全部校验通过
						boolean isCheckedPass=true;
						for(ChuRuKuProduct item:beans){
							if(isEmpty(item.getGoods_name())){
								isCheckedPass=false;
								break;
							}
						}
						
						if(isCheckedPass){
							startMenDianDiaoChuCreateSecondActivity();
						}else{
							showDialogIfNecessary();
						}
						
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
					item.setGoods_img(product.getGoods_img());
					continue up;
				}
			}
		}
	}
	
	private void showDialogIfNecessary(){
		for(ChuRuKuProduct item:beans){
			if(isEmpty(item.getGoods_name())){
				final String goods_sn= item.getGoods_sn();
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
				break;
			}
		}

	}
	
	@Subscriber
	void updateByEventBus(String event) {
		if (event.equals(App.EVENT_FINISH)) {
			finish();
		}
	}
}
