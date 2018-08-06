package com.app.xstore.mendiandiaochu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.model.ShopInfo;
import com.app.net.Commands;
import com.app.widget.SimpleListPopupWindow;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.base.util.D;

/**
 * 
 * 
 * @author pythoner
 * 
 */
public class MenDianDiaoChuCreateSecondActivity extends BaseActivity implements OnClickListener {

	private ArrayList<ChuRuKuProduct> beans;
	private ShopInfo shopInfo;
	
	private int qty=0;
	private TextView tv_from_store,tv_to_store;
	private TextView tv_kuaidi;
	private EditText et_kuaidi,et_mark;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mendian_diaochu_create_second);
		initActionBar("门店调出" ,null, null);
		
		beans=getIntent().getParcelableArrayListExtra("ChuRuKuProducts");
		if(beans==null){
			return;
		}
		shopInfo=getIntent().getParcelableExtra("ShopInfo");
		if(shopInfo==null){
			return;
		}
		
		initViews();
		initScanner(new OnScannerResult() {
			
			@Override
			public void onResult(String data) {
				// TODO Auto-generated method stub
				et_kuaidi.setText(data);
			}
		});
		
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		tv_from_store = $(R.id.tv_from_store);
		tv_to_store = $(R.id.tv_to_store);
		
		tv_from_store.setText("发货门店/仓库："+App.user.getShopInfo().getShop_name());
		tv_to_store.setText("收货门店/仓库："+shopInfo.getShop_name());
		
		TextView tv_kuan = $(R.id.tv_kuan);
		TextView tv_jian = $(R.id.tv_jian);
		
		HashMap<String,ChuRuKuProduct> map=new HashMap<String,ChuRuKuProduct>();
		
		for(ChuRuKuProduct bean:beans){
			qty+=bean.getQty();
			String sku=bean.getGoods_sn().substring(0, 6);
			map.put(sku, bean);
		}
		tv_kuan.setText("款："+map.size());
		tv_jian.setText("件："+qty);
		
		tv_kuaidi=$(R.id.tv_kuaidi);
		tv_kuaidi.setOnClickListener(this);
		et_kuaidi = $(R.id.et_kuaidi);
		$(R.id.iv_scan).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doScan(resultHandler);
			}
		});
		
		et_mark = $(R.id.et_mark);
		$(R.id.btn_ok).setOnClickListener(this);

	}

	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_kuaidi:
			doCommandGetExpressList();
			break;
		case R.id.btn_ok:
			if((tv_kuaidi.getTag()!=null&&!isEmpty(et_kuaidi))||(tv_kuaidi.getTag()==null&&isEmpty(et_kuaidi))){
				String message="确认提交后，将自动扣减本店库存！\n需要提交吗？";
				D.showDialog(this, message, "确定", "取消", new D.OnPositiveListener() {
					
					@Override
					public void onPositive() {
						// TODO Auto-generated method stub
						doCommandCreateOutStorage();
					}
				});
			}else{
				showToast("请选择快递公司和输入快递单号");
			}
			break;

		default:
			break;
		}
	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		setThemeDrawable(this, R.id.btn_ok);
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
						if(!isFinishing()){
							List<Express> list=obj.getInfo();
							Express item=new Express();
							item.setDescription("<无需快递>");
							list.add(0,item);
							View view=LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
							SimpleListPopupWindow<Express> p=new SimpleListPopupWindow<Express>(context, view, tv_kuaidi.getWidth(), list);
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
					}
				}
			}
		});
	}
	
	private void doCommandCreateOutStorage(){
		ChuRuKuHead headInfo=new ChuRuKuHead();
		headInfo.setDoc_type("OTI");//门店调出
		headInfo.setShopCode(App.user.getShopInfo().getShop_code());
		headInfo.setSrc_shop(App.user.getShopInfo().getShop_code());//调出门店
		headInfo.setTar_shop(shopInfo.getShop_code());//目标门店
		headInfo.setQty(qty);
		if(tv_kuaidi.getTag()!=null){
			headInfo.setExpType_code((String)tv_kuaidi.getTag());
			headInfo.setExp_num(et_kuaidi.getText().toString());
		}
		
		headInfo.setRemark(et_mark.getText().toString().trim());
		headInfo.setCreate_user(App.user.getUserInfo().getUser_code());
		headInfo.setLast_modify_user(App.user.getUserInfo().getUser_code());
		
		Commands.doCommandCreateOutStorage(context, headInfo, beans, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				if(isSuccess(response)){
					CreateOutStorageResponse obj=mapperToObject(response, CreateOutStorageResponse.class);
					if(obj!=null&&obj.getDocCode()!=null){
						doCommandSubmitOutStorage(obj.getDocCode());
					}
				}
			}
		});
	}
	
	private void doCommandSubmitOutStorage(String docCode){
		Commands.doCommandSubmitOutStorage(context, docCode, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				if(isSuccess(response)){
					showToast("操作成功");
					EventBus.getDefault().post(App.EVENT_REFRESH);
					EventBus.getDefault().post(App.EVENT_FINISH);
					finish();
				}
			}
		});
	}

}
