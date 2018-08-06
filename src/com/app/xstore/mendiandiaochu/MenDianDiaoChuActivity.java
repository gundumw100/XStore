package com.app.xstore.mendiandiaochu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.net.Commands;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;

/**
 * 
 * @author pythoner
 * 
 */
public class MenDianDiaoChuActivity extends BaseActivity {

	private String describeDate,startDate,endDate;
	private String statusName,statusCode;
	private TextView tv_header,tv_footer;
	private ListView listView;
	private CommonAdapter<ChuRuKuHead> adapter;
	private List<ChuRuKuHead> beans=new ArrayList<ChuRuKuHead>();
	public static List<Item> statusList=new ArrayList<Item>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mendian_diaochu);
		context = this;
		
		statusList.add(new Item("已出库","S",getColorCompat(android.R.color.holo_blue_light)));
		statusList.add(new Item("对方已收货","P",getColorCompat(R.color.fittting_red)));
		
		initActionBar("门店调出", null, getDrawableCompat(R.drawable.icon_order_more));
		initViews();
		
		resetDatas();
		doCommandGetOutStorageList(startDate,endDate,statusCode);
	}

	private void resetDatas(){
		//初始化显示最近一周的数据
		String format="yyyy-MM-dd";
		Calendar calendar = Calendar.getInstance();
		String today=formatTime(calendar.getTimeInMillis(), format);
		calendar.add(Calendar.DAY_OF_MONTH, -7);
		String weekAgo=formatTime(calendar.getTimeInMillis(), format);
		
		describeDate="最近一周";
		startDate=weekAgo;
		endDate=today;
		
		statusName="";
		statusCode=null;
	}
	
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		$(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,MenDianDiaoChuCreateActivity.class);
				startActivity(intent);
			}
		});
		tv_header=$(R.id.tv_header);
		tv_footer=$(R.id.tv_footer);
		listView=$(R.id.listView);
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,MenDianDiaoChuCreateActivity.class);
				intent.putExtra("isCheckOnly", true);
				intent.putExtra("ChuRuKuHead", beans.get(position));
				startActivity(intent);
			}
		});
		updateViews(beans);
	}

	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
//		if(beans.size()==0){
//			if(listView.getEmptyView()==null){
//				setEmptyView(listView, "暂无数据");
//			}
//		}
		
		tv_header.setText((describeDate==null?"":describeDate)+" "+(statusName==null?"":statusName));
		tv_footer.setText("总计"+beans.size()+"条数据");
		
		notifyDataSetChanged();
	}

	@Override
	public void doRightButtonClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=new Intent(context,FilterActivity.class);
		startActivityForResult(intent, 1000);
	}
	
	private void notifyDataSetChanged(){
		if(adapter==null){
			listView.setAdapter(adapter = new CommonAdapter<ChuRuKuHead>( context, beans,
					  R.layout.item_for_mendiandiaochu){
					  
					@Override
					public void setValues(ViewHolder helper, final ChuRuKuHead item, final int position) {
						// TODO Auto-generated method stub
						helper.setText(R.id.item_0,"出库单号："+item.getDoc_code());
						
						TextView item_1=helper.getView(R.id.item_1);
						Item statusItem=getStatusItem(item.getDoc_status());
						item_1.setTextColor(statusItem.numberValue);
						item_1.setText(statusItem.describe);
						
						helper.setText(R.id.item_2,"收货门店/仓库："+item.getTarShopName()+"("+item.getTar_shop()+")");
						helper.setText(R.id.item_3,"数量："+item.getQty());
						helper.setText(R.id.item_4,"单据日期："+getCorrectDate(item.getDoc_date()));
					}
			});
		}else{
			adapter.notifyDataSetChanged();
		}
	}
	
	private void doCommandGetOutStorageList(String startDate,String endDate,String docStatus){
		Commands.doCommandGetOutStorageList(context, startDate,endDate, docStatus, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				{"ErrMessage":"","Result":true,"Info":[{"expType_code":"01","src_type":"","doc_code":"00S001OSDC1807230008","last_modify_date":"0001-01-01T00:00:00","remark":"","doc_date":"2018-07-23T17:24:54.173","src_shop":null,"doc_type":"OTI","exp_num":"1234567890","qty":1,"last_modify_user":"test","tar_shop":"S010","id":0,"shopCode":null,"create_date":"0001-01-01T00:00:00","doc_status":"I","src_code":"S001","tar_type":"","create_user":"test","tar_code":"S010"},{"expType_code":"01","src_type":"","doc_code":"00S001OSDC1807240010","last_modify_date":"0001-01-01T00:00:00","remark":"","doc_date":"2018-07-24T16:14:11.857","src_shop":null,"doc_type":"","exp_num":"0987563214","qty":3,"last_modify_user":"test","tar_shop":"S010","id":0,"shopCode":null,"create_date":"0001-01-01T00:00:00","doc_status":"I","src_code":"S001","tar_type":"","create_user":"test","tar_code":"S010"}],"ErrSysTrackMessage":"","ErrSysMessage":"","Message":"OK"}
				Log.i("tag", response.toString());
				if(isSuccess(response)){
					GetOutStorageListResponse obj=mapperToObject(response, GetOutStorageListResponse.class);
					if(obj!=null){
						List<ChuRuKuHead> infos=obj.getInfo();
						beans.clear();
						beans.addAll(infos);
						updateViews(beans);
					}
				}
			}
		});
	}
	
	private Item getStatusItem(String statusCode){
		for(Item item:statusList){
			if(item.startValue.equals(statusCode)){
				return item;
			}
		}
		return new Item("未知","",getColorCompat(R.color.grayMiddle));
	}
	
	@Subscriber
	void updateByEventBus(String event) {
		if (event.equals(App.EVENT_REFRESH)) {
			resetDatas();
			doCommandGetOutStorageList(startDate,endDate,statusCode);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==1000){
			if(resultCode==0){
				if(data!=null){
					describeDate=data.getStringExtra("describeDate");
					startDate=data.getStringExtra("startDate");
					endDate=data.getStringExtra("endDate");
					statusName=data.getStringExtra("statusName");
					statusCode=data.getStringExtra("statusCode");
//					beans.clear();
//					notifyDataSetChanged();
					doCommandGetOutStorageList(startDate,endDate,statusCode);
				}
			}
		}
	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		if(context!=null){
			context.setThemeDrawable(context,R.id.btn_add);
		}
	}
	
}
