package com.app.xstore;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Response.Listener;
import com.app.model.Printer;
import com.app.model.response.GetPrinterInfoResponse;
import com.app.net.Commands;
import com.app.widget.dialog.SettingPrinterDialog;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;
import com.base.util.D;

/**
 * 打印机设置
 * 
 * @author pythoner
 * 
 */
public class SettingPrinterActivity extends BaseActivity{

	private Context context;
	private ListView listView;
	private CommonAdapter<Printer> adapter;
	private List<Printer> beans = new ArrayList<Printer>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_printer);
		context = this;
		initActionBar("打印机设置", "新增",null);
		initViews();
		doCommandGetPrinterInfo();
	}

	@Override
	public void doRightButtonClick(View v) {
		// TODO Auto-generated method stub
		SettingPrinterDialog d=new SettingPrinterDialog(context,null);
		d.setOnClickListener(new SettingPrinterDialog.OnClickListener() {
			
			@Override
			public void onClick(View v, String name, String ip,int type) {
				// TODO Auto-generated method stub
				doCommandCreatePrinterInfo(name,ip,String.valueOf(type));
			}
		});
		d.show();
	}

	private void doCommandCreatePrinterInfo(String description, String printerip,String type){
		String shopId=App.user.getShopInfo().getShop_code();
		String creator=App.user.getUserInfo().getUser_code();
		Commands.doCommandCreatePrinterInfo(context, shopId, printerip, description, type, creator, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				if (isSuccess(response)) {
					showToast("创建成功");
					doCommandGetPrinterInfo();
				}
			}
			
		});
	}
	
	private void doCommandGetPrinterInfo(){
		String shopId=App.user.getShopInfo().getShop_code();
		Commands.doCommandGetPrinterInfo(context, shopId,new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
//				Log.i("tag", response.toString());
				if (isSuccess(response)) {
					GetPrinterInfoResponse obj = mapperToObject(response, GetPrinterInfoResponse.class);
					List<Printer> list = obj.getHeadInfo();
					beans.clear();
					beans.addAll(list);
					notifyDataSetChanged();
				}
			}
		});
		
	}
	
	private void notifyDataSetChanged(){
		if(adapter==null){
			listView.setAdapter(adapter = new CommonAdapter<Printer>(context, beans,
					  R.layout.item_setting_printer){
					  
					@Override
					public void setValues(ViewHolder helper, final Printer item, int position) {
						// TODO Auto-generated method stub
						helper.setText(R.id.item_0, item.getDescription());
						helper.setText(R.id.item_1, item.getPrinterip());
						//修改
						helper.getView(R.id.item_2).setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								SettingPrinterDialog d=new SettingPrinterDialog(context,item);
								d.setOnClickListener(new SettingPrinterDialog.OnClickListener() {
									
									@Override
									public void onClick(View v, String name, String ip,int type) {
										// TODO Auto-generated method stub
										doCommandUpdatePrinterInfo(item, ip,name, String.valueOf(type));
									}
								});
								d.show();
							}
						});
						//删除
						helper.getView(R.id.item_3).setOnClickListener(new View.OnClickListener() {
															
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								D.showDialog(SettingPrinterActivity.this,"删除打印机吗？","删除","取消",new D.OnPositiveListener() {
									
									@Override
									public void onPositive() {
										// TODO Auto-generated method stub
										doCommandDeletePrinterInfo(item);
									}
								});
								
							}
						});
					}
			});
		}else{
			adapter.notifyDataSetChanged();
		}
	}
	
	private void doCommandUpdatePrinterInfo(Printer item,String printerip,String description,String type){
		String shopId=App.user.getShopInfo().getShop_code();
		String creator=App.user.getUserInfo().getUser_code();
		Commands.doCommandUpdatePrinterInfo(context, item.getId(), shopId, printerip, description, type, creator, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				if (isSuccess(response)) {
					doCommandGetPrinterInfo();
				}
			}
		});
	}
	
	private void doCommandDeletePrinterInfo(final Printer item){
		String creator=App.user.getUserInfo().getUser_code();
		Commands.doCommandDeletePrinterInfo(context, item.getId(), creator, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				if (isSuccess(response)) {
					beans.remove(item);
					notifyDataSetChanged();
//					showToast("删除成功");
//					doCommandGetPrinterInfo();
				}
			}
		});
	}
	
	
	@Override
	public void initViews() {
		listView = (ListView) findViewById(R.id.listView);
	}

	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
	}

}
