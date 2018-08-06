package com.app.xstore.inventory;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.model.Box;
import com.app.net.Commands;
import com.app.widget.VerificationCodeDialog;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.pandian.custom.CustomPanDianDan;
import com.app.xstore.pandian.custom.CustomPanDianUploadActivity;
import com.app.xstore.pandian.custom.GetCustBoxInfoByCodeResponse;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;
import com.base.util.D;

/**
 * 箱码列表
 * @author pythoner
 * 
 */
public class BoxListActivity extends BaseActivity implements OnClickListener{

	private Context context;
	private final int UNSAVEED=-1;
	private TextView tv_boxNum,tv_productNum;
	private EditText et_text;
	private ListView listView;
	private CommonAdapter<Box> adapter;
	private ArrayList<Box> beans=new ArrayList<Box>();
	private int curPosition=-1;
	private CustomPanDianDan dan;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_box_list);
		context = this;
		dan=getIntent().getParcelableExtra("CustomPanDianDan");
		initActionBar("货箱录入", dan.getStatus()==1?null:"上传",null);
		initViews();
		load();
		if(dan.getStatus()==1){
		}else{
			createFloatView(0);
		}
	}

	@Override
	public void doRightButtonClick(View v) {
		// TODO Auto-generated method stub
		if(beans.size()!=dan.getYds()){
			D.showDialog(BoxListActivity.this, "预输入的箱数和实际扫描的箱数不一致，是否继续？", "继续", "取消", new D.OnPositiveListener() {
				
				@Override
				public void onPositive() {
					// TODO Auto-generated method stub
					saveOrUpdateDatas();//下一步时保存一次
					dan.setBoxes(beans);
					Intent intent =new Intent(context,CustomPanDianUploadActivity.class);
					intent.putExtra("CustomPanDianDan", dan);
					startActivity(intent);
				}
			});
		}else{
			saveOrUpdateDatas();//下一步时保存一次
			dan.setBoxes(beans);
			Intent intent =new Intent(context,CustomPanDianUploadActivity.class);
			intent.putExtra("CustomPanDianDan", dan);
			startActivity(intent);
		}
	}
	
	@Override
	public void doLeftButtonClick(View v) {
		// TODO Auto-generated method stub
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		saveOrUpdateDatas();// 返回时保存一次
		super.onBackPressed();
	}
	
	private boolean isChange=false;
	private void saveOrUpdateDatas() {
		if (isChange) {
//			for (Box p : beans) {
//				p.setDanID(dan.getId());
//			}
//			DataSupport.saveAll(beans);
			isChange=false;
			updateDanStatus();
		}
	}
	
	private void updateDanStatus(){
		if(dan.getStatus()==1){//如果该单据已经上传过，则状态变成上传过但有更新
			dan.setStatus(2);
			ContentValues values = new ContentValues();
			values.put("status",2);
			DataSupport.update(CustomPanDianDan.class, values, dan.getId());
			EventBus.getDefault().post(App.EVENT_UPDATE_CUSTOM_PANDIANDAN_LIST);
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(dan.getStatus()==1){
		}else{
			removeFloatView();
		}
	}
	
	@Override
	public void onScanProductHandleMessage(String prodID){
		addItem(createBox(prodID));
	}
	
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		tv_boxNum=$(R.id.tv_boxNum);
		tv_productNum=$(R.id.tv_productNum);
		et_text=$(R.id.et_text);
		
		$(R.id.btn_clear).setOnClickListener(this);
		$(R.id.btn_ok).setOnClickListener(this);
		
		listView=$(R.id.listView);
		listView.setOnItemClickListener(new ListView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,BoxDetailActivity.class);
				intent.putExtra("Box", beans.get(position));
				startActivity(intent);
			}
			
		});
		if(dan.getStatus()==1){
		}else{
			listView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
				
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
					// TODO Auto-generated method stub
					D.showDialog(BoxListActivity.this, "是否删除 " +beans.get(position).getCode()+" ？", "确定", "取消", new D.OnPositiveListener() {
						
						@Override
						public void onPositive() {
							// TODO Auto-generated method stub
							removeItem(position);
						}
					});
					return true;
				}
			});
		}
		
	}
	
	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
		if(obj==null){
			return;
		}
		notifyDataSetChanged();
		tv_boxNum.setText("箱数："+beans.size());
		
		int qty=0;
		for(Box bean:beans){
			qty+=bean.getQty();
		}
		tv_productNum.setText("商品总数："+qty);
		
	}

	private void notifyDataSetChanged(){
		if(adapter==null){
			listView.setAdapter(adapter = new CommonAdapter<Box>( context, beans,
					  R.layout.item_box){
					  
					@Override
					public void setValues(ViewHolder helper, final Box item, final int position) {
						// TODO Auto-generated method stub
						helper.setText(R.id.item_0, item.getCode());
						helper.setText(R.id.item_1, String.valueOf(item.getQty()));
						
						View container=helper.getView(R.id.container);
						if(curPosition==position){
							container.setBackgroundColor(0xFFFF6633);
						}else{
							container.setBackgroundColor(Color.TRANSPARENT);
						}
						
					}
			});
		}else{
			adapter.notifyDataSetChanged();
		}
	}
	
	
	@Subscriber
	void updateByEventBus(String event) {
		if(App.EVENT_FINISH.equals(event)){
			finish();
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_ok:
			if(dan.getStatus()==1){
				showToast("已上传的单据不能修改");
				return;
			}
			String code=et_text.getText().toString().trim();
			if(isEmpty(code)){
				doShake(context, et_text);
				return;
			}
			addItem(createBox(code));
			et_text.setText("");
			break;
		case R.id.btn_clear:
			if(dan.getStatus()==1){
				showToast("已上传的单据不能修改");
				return;
			}
			final VerificationCodeDialog d=new VerificationCodeDialog(context);
			d.setOnClickListener(new VerificationCodeDialog.OnClickListener() {
				
				@Override
				public void onClick(View v, String text) {
					// TODO Auto-generated method stub
					beans.clear();
					updateViews(beans);
					DataSupport.deleteAll(Box.class,"danID = ?",String.valueOf(dan.getId()));
					isChange=true;
//					updateDanStatus();
				}
			});
			d.show();
			break;

		default:
			break;
		}
	}
	
	private Box createBox(String code){
		Box bean=new Box();
		bean.setCode(code);
		bean.setQty(1);
		bean.setId(UNSAVEED);
		return bean;
	}
	
	private boolean isExist(String code){
		int i=0;
		for(Box box:beans){
			if(box.getCode().equals(code)){
				curPosition=i;
				notifyDataSetChanged();
				return true;
			}
			i++;
		}
		return false;
	}
	private void addItem(Box bean){
		if(isExist(bean.getCode())){
			return;
		}
//		Log.i("tag", "Box="+bean.toString());
		//Box=-1;0;83703110999;1
		//Box=-1;0;82703130749;1
		Box box=DataSupport.where("code = ?",bean.getCode()).findLast(Box.class);//查找货箱是否已经存在
		if(box!=null){
			CustomPanDianDan dan=DataSupport.find(CustomPanDianDan.class, box.getDanID());
			if(dan!=null){
				//提示信息里包括：日期，单据号，操作人，货架号
				StringBuffer sb=new StringBuffer("该箱码已存在!");
				sb.append("\n货架号：").append(dan.getShelf());
				sb.append("\n操作人：").append(dan.getGh());
				sb.append("\n日期 ：").append(formatTime(dan.getDate(),"yyyy-MM-dd HH:mm"));
				D.showDialog(BoxListActivity.this, sb.toString(), "确定");
			}else{
//				String msg="该箱码已存在，但是没有找到对应的盘点单。";
//				D.showDialog(BoxListActivity.this, msg, "确定");
				DataSupport.delete(Box.class, box.getId());//删除脏数据
			}
		}else{
			doCommandGetCustBoxInfoByCode(bean);
		}
	}
	
	private void removeItem(int position){
		Box bean=beans.get(position);
		if(bean.getId()>=0){
			DataSupport.delete(Box.class, bean.getId());
		}
		beans.remove(position);
		updateViews(beans);
		
		isChange=true;
//		updateDanStatus();
	}
	
	private void load(){
		List<Box> list=DataSupport.where("danID = ?",String.valueOf(dan.getId())).order("id desc").find(Box.class);
		beans.clear();
		beans.addAll(list);
		updateViews(beans);
	}
	
	private void doCommandGetCustBoxInfoByCode(final Box bean){
		String shop_code=App.user.getShopInfo().getShop_code();
		Commands.doCommandGetCustBoxInfoByCode(context, shop_code, bean.getCode(), new Listener<JSONObject>(){

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				if (isSuccess(response)) {
					GetCustBoxInfoByCodeResponse obj = mapperToObject(response, GetCustBoxInfoByCodeResponse.class);
					if(obj!=null&&!isEmptyList(obj.getGoods())){
						bean.setDanID(dan.getId());//保存货箱对应的盘点单ID
						Log.i("tag", "dan.getId===="+dan.getId());
						bean.setQty(obj.getGoods().size());//
						boolean ok=bean.saveFast();
//						Log.i("tag", "保存货箱:"+ok);
						curPosition=0;
						beans.add(0,bean);
						updateViews(beans);
						
						isChange=true;
					}else{
						showToast("货箱中没有数据");
					}
				}
			}
			
		});
	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		setThemeDrawable(this, R.id.btn_ok);
		setThemeDrawable(this, R.id.btn_clear);
	}
	
}
