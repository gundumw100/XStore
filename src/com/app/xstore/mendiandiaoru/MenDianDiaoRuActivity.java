package com.app.xstore.mendiandiaoru;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.net.Commands;
import com.app.widget.BadgeView;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.app.xstore.mendiancaigouruku.CaiGouRuKuDetailActivity;
import com.app.xstore.mendiandiaochu.ChuRuKuHead;
import com.app.xstore.mendiandiaochu.GetOutStorageListResponse;
import com.app.xstore.mendiandiaochu.Item;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;

/**
 * 
 * @author pythoner
 * 
 */
public class MenDianDiaoRuActivity extends BaseActivity implements View.OnClickListener{

	private String describeDate,startDate,endDate;
	private TextView tv_header;
	private int curIndex=0;
	private RadioButton btn_0,btn_1;
	private ListView listView;
	private CommonAdapter<ChuRuKuHead> adapter;
	private List<ChuRuKuHead> beans=new ArrayList<ChuRuKuHead>();
	public static List<Item> statusList=new ArrayList<Item>();
	private int curPosition=-1;
	private BadgeView badgeViewLeft;
	private TextView view_0,view_1;//BadgeView不能直接在RadioButton添加
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mendian_diaoru);
		context = this;
		
		statusList.add(new Item("对方已出库","S",getColorCompat(android.R.color.holo_blue_light)));
		statusList.add(new Item("已收货","P",getColorCompat(R.color.fittting_red)));
		
		initActionBar("门店调入", null, getDrawableCompat(R.drawable.icon_order_more));
		resetDatas();
		initViews();
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
	}
	
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		btn_0=$(R.id.btn_0);
		btn_1=$(R.id.btn_1);
		btn_0.setOnClickListener(this);
		btn_1.setOnClickListener(this);
		view_0=$(R.id.view_0);
//		view_1=$(R.id.view_1);
		
		tv_header=$(R.id.tv_header);
		listView=$(R.id.listView);
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				// TODO Auto-generated method stub
				curPosition=position;
				Intent intent=null;
				if(curIndex==0){
					intent=new Intent(context,MenDianDiaoRuDetailActivity.class);
				}else if(curIndex==1){
					intent=new Intent(context,CaiGouRuKuDetailActivity.class);
				}
				intent.putExtra("ChuRuKuHead", beans.get(position));
				startActivityForResult(intent, 1000);
				
			}
		});
		
		btn_0.performClick();
	}

	@Override
	public void doRightButtonClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=new Intent(context,FilterActivity.class);
		startActivityForResult(intent, 1000);
	}
	
	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
//		if(beans.size()==0){
//			if(listView.getEmptyView()==null){
//				setEmptyView(listView, "暂无数据");
//			}
//		}
		
		if(curIndex==0){
			if(badgeViewLeft==null){
				badgeViewLeft = new BadgeView(context);
				badgeViewLeft.setTargetView(view_0);
				badgeViewLeft.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
				badgeViewLeft.setBadgeMargin(0, 8, 48, 0);
				badgeViewLeft.setHideOnNull(true);
			}
			badgeViewLeft.setBadgeCount(beans.size());
		}else if(curIndex==1){
			btn_1.setText("入库单("+beans.size()+")");
//			if(badgeViewRight==null){
//				badgeViewRight = new BadgeView(context);
//				badgeViewRight.setTargetView(view_1);
//				badgeViewRight.setBadgeMargin(0, 8, 48, 0);
//				badgeViewRight.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
//				badgeViewRight.setHideOnNull(true);
//			}
//			badgeViewRight.setBadgeCount(beans.size());
		}
		
		tv_header.setText((describeDate==null?"":describeDate));
//		tv_footer.setText("总计"+beans.size()+"条数据");
		
		notifyDataSetChanged();
		
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
						TextView item_2=helper.getView(R.id.item_2);
						if(curIndex==0){
							Item statusItem=getStatusItem(item.getDoc_status());
							item_1.setTextColor(statusItem.numberValue);
							item_1.setText(statusItem.describe);
							item_2.setText("发货门店/仓库："+item.getShopName()+"("+item.getShopCode()+")");
						}else if(curIndex==1){
							item_1.setTextColor(getColorCompat(R.color.fittting_red));
							item_1.setText("已收货");
							item_2.setVisibility(View.GONE);
						}
						helper.setText(R.id.item_3,"数量："+item.getQty());
						helper.setText(R.id.item_4,"单据日期："+getCorrectDate(item.getDoc_date()));
					}
			});
		}else{
			adapter.notifyDataSetChanged();
		}
	}
	
	private Item getStatusItem(String statusCode){
		for(Item item:statusList){
			if(item.startValue.equals(statusCode)){
				return item;
			}
		}
		return new Item("未知","",getColorCompat(R.color.grayMiddle));
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
					doCommand();
				}
			}else if(resultCode==1){
				if(curPosition>=0){
					beans.remove(curPosition);
					updateViews(beans);
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_0:
			curIndex=0;
			doCommand();
			break;
		case R.id.btn_1:
			curIndex=1;
			doCommand();
			break;

		default:
			break;
		}
		
	}
	
	private void doCommand(){
		if(curIndex==0){
			doCommandGetOutStorageListByShopCode(startDate,endDate);
		}else if(curIndex==1){
			doCommandGetInStorageList(startDate,endDate);
		}
	}
	
	private void doCommandGetOutStorageListByShopCode(String startDate,String endDate){
		Commands.doCommandGetOutStorageListByShopCode(context,startDate,endDate, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
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
	
	private void doCommandGetInStorageList(String startDate, String endDate){
		Commands.doCommandGetInStorageList(context, startDate, endDate, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
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
	
}
