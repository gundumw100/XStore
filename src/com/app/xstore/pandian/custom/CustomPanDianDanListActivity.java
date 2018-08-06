package com.app.xstore.pandian.custom;

import java.util.ArrayList;
import java.util.List;

import org.litepal.crud.DataSupport;
import org.simple.eventbus.Subscriber;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.app.model.Box;
import com.app.model.UserInfo;
import com.app.util.StringUtil;
import com.app.widget.SimplePairListPopupWindow;
import com.app.widget.VerificationCodeDialog;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.inventory.BoxListActivity;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;
import com.base.util.D;

/**
 * 盘点
 * @author pythoner
 * 
 */
public class CustomPanDianDanListActivity extends BaseActivity implements OnClickListener{

	private Context context;
	private TextView tv_userId,btn_startDate,btn_endDate;
	private View btn_clear_userId;
	private long startTime,endTime;
	private EditText et_locID;
	private ListView listView;
	private CommonAdapter<CustomPanDianDan> adapter;
	private List<CustomPanDianDan> beans = new ArrayList<CustomPanDianDan>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_pandiandan_list);
		context = this;
		//重写resultHandler
		resultHandler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				String data = (String) msg.obj;
				if(data.equalsIgnoreCase("time out")||data.equalsIgnoreCase("user canceled")){
					showToast(R.string.alert_no_barcode_found);
					return;
				}
				et_locID.setText(data);
				loadDataByShelf(data);
			}
		};
		initActionBar(App.user.getShopInfo().getShop_name(), null, null);
		initViews();
		
		startTime=StringUtil.getTodayStartInMillis();
		endTime=System.currentTimeMillis();
//		loadDataByDate(startTime,endTime);
		loadData(startTime,endTime);
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		View btn_add=findViewById(R.id.btn_add);
		btn_add.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final PanDianDanDialog d=new PanDianDanDialog(context,"预点",null);
				d.setOnClickListener(new PanDianDanDialog.OnClickListener() {

					@Override
					public void onClick(View v, CustomPanDianDan item) {
						// TODO Auto-generated method stub
						CustomPanDianDan dan=new CustomPanDianDan();
						dan.setDate(System.currentTimeMillis());
						dan.setGh(App.user.getUserInfo().getUser_code());
						dan.setShelf(item.getShelf());
						dan.setYds(item.getYds());
						dan.save();
						
						beans.add(0,dan);
						updateViews(beans);
					}
					
				});
				d.show();
			}
		});
		View btn_more=findViewById(R.id.btn_more);
		btn_more.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showSimplePopupWindow(v);
			}
		});
		setThemeDrawable(this, btn_add, R.drawable.btn_pressed);
		setThemeDrawable(this, btn_more, R.drawable.btn_pressed);
		
		btn_startDate=$(R.id.btn_startDate);
		btn_startDate.setOnClickListener(this);
		btn_startDate.setText(formatTime(System.currentTimeMillis(), "yyyy-MM-dd"));
		btn_endDate=$(R.id.btn_endDate);
		btn_endDate.setOnClickListener(this);
		btn_endDate.setText(formatTime(System.currentTimeMillis(), "yyyy-MM-dd"));
		tv_userId=$(R.id.tv_userId);
		tv_userId.setOnClickListener(this);
		btn_clear_userId=$(R.id.btn_clear_userId);
		btn_clear_userId.setOnClickListener(this);
		btn_clear_userId.setVisibility(tv_userId.getText().length()==0?View.GONE:View.VISIBLE);
		
		et_locID=$(R.id.et_locID);
		$(R.id.iv_scan).setOnClickListener(this);
		
		tv_userId=$(R.id.tv_userId);
		tv_userId.setOnClickListener(this);
		
		$(R.id.btn_search).setOnClickListener(this);
		listView=$(R.id.listView);
//		listView.setOnItemClickListener(new ListView.OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				// TODO Auto-generated method stub
//				changeTo(beans.get(position));
//			}
//			
//		});
		listView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				final CustomPanDianDan bean=beans.get(position);
				D.showDialog(CustomPanDianDanListActivity.this, "是否删除货架"+bean.getShelf()+" ？", "确定", "取消", new D.OnPositiveListener() {
					
					@Override
					public void onPositive() {
						// TODO Auto-generated method stub
						DataSupport.deleteAll(CustomPanDianProduct.class, "danID = ?",String.valueOf(bean.getId()));
						DataSupport.deleteAll(Box.class, "danID = ?",String.valueOf(bean.getId()));
						DataSupport.delete(CustomPanDianDan.class, bean.getId());
						beans.remove(bean);
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
		notifyDataSetChanged();
	}
	@Override
	public void doRightButtonClick(View v) {
		// TODO Auto-generated method stub
		changeTo(null);
	}
	
	private void showSimplePopupWindow(View v){
		ArrayList<Pair<Integer, String>> list = new ArrayList<Pair<Integer, String>>();
		list.add(new Pair<Integer, String>(5, "清空"));
		
		View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
		final SimplePairListPopupWindow<Integer, String> popupWindow = new SimplePairListPopupWindow<Integer, String>(context,view, (int)App.res.getDimension(R.dimen.popupwindow_width), list);
		popupWindow.showAsDropDown(v, 0, 0);
		popupWindow.setOnItemClickListener(new SimplePairListPopupWindow.OnItemClickListener<Integer, String>() {

			@Override
			public void onItemClick(int position, Pair<Integer, String> pair) {
				// TODO Auto-generated method stub
				switch (pair.first) {
				case 5://清空
					final VerificationCodeDialog d=new VerificationCodeDialog(context);
					d.setOnClickListener(new VerificationCodeDialog.OnClickListener() {
						
						@Override
						public void onClick(View v, String text) {
							// TODO Auto-generated method stub
							for(CustomPanDianDan bean:beans){
								DataSupport.deleteAll(CustomPanDianProduct.class, "danID = ?",String.valueOf(bean.getId()));
								DataSupport.deleteAll(Box.class, "danID = ?",String.valueOf(bean.getId()));
								DataSupport.delete(CustomPanDianDan.class, bean.getId());
							}
							beans.clear();
							updateViews(beans);
						}
					});
					d.show();
					break;
				default:
					showToast(R.string.error_unknown_function);
					break;
				}
			}
		});
	}
	
	private void notifyDataSetChanged(){
		if(adapter==null){
			listView.setAdapter(adapter = new CommonAdapter<CustomPanDianDan>( context, beans,
					  R.layout.item_custom_pandiandan){
					  
					@Override
					public void setValues(ViewHolder helper, final CustomPanDianDan item, final int position) {
						// TODO Auto-generated method stub
//						helper.getView(R.id.container).setBackgroundColor(curPosition==position?0xFFFF6633:0x00000000);
						helper.setText(R.id.item_0, "货架："+item.getShelf());
						helper.setText(R.id.item_1, formatTime(item.getDate(),"yyyy-MM-dd HH:mm"));
						helper.setText(R.id.item_2, "预点数："+item.getYds());
						helper.setText(R.id.item_3, item.getGh());
						
						TextView item_4=helper.getView(R.id.item_4);
						String status="";
						if(item.getStatus()==0){
							status="";
						}else if(item.getStatus()==1){
							item_4.setTextColor(App.res.getColor(R.color.primary));
							status="已上传";
						}else if(item.getStatus()==2){
							item_4.setTextColor(App.res.getColor(R.color.blue1));
							status="有更新";
						}
						item_4.setText(status);
						View btn_0=helper.getView(R.id.btn_0);
						if(App.user.getUserInfo().getUser_code().equals(item.getGh())){//不是自己建的不允许修改
							if(item.getStatus()==1||item.getStatus()==2){//已上传
								btn_0.setVisibility(View.GONE);
							}else{
								btn_0.setVisibility(View.VISIBLE);
							}
							//修改
							btn_0.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									final PanDianDanDialog d=new PanDianDanDialog(context,"预点",item);
									d.setOnClickListener(new PanDianDanDialog.OnClickListener() {
										
										@Override
										public void onClick(View v, CustomPanDianDan dan) {
											// TODO Auto-generated method stub
											item.setShelf(dan.getShelf());
											item.setYds(dan.getYds());
											updateViews(beans);
											item.update(item.getId());
										}
										
									});
									d.show();
								}
							});
						}else{
							btn_0.setVisibility(View.GONE);
						}
						
						//扫描明细
						helper.getView(R.id.btn_1).setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								changeTo(item);
							}
						});
						//货箱
						helper.getView(R.id.btn_2).setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Intent intent=new Intent(context,BoxListActivity.class);
								intent.putExtra("CustomPanDianDan", item);
								startActivity(intent);
							}
						});
						setThemeDrawable(context, helper.getView(R.id.btn_1), R.drawable.btn_pressed);
						setThemeDrawable(context, helper.getView(R.id.btn_2), R.drawable.btn_pressed);
					}
			});
		}else{
			adapter.notifyDataSetChanged();
		}
	}
	
	private void changeTo(CustomPanDianDan item){
		Intent intent=new Intent(context,CustomPanDianActivity.class);
		intent.putExtra("CustomPanDianDan", item);
		startActivity(intent);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_scan:
			setResultHandler(resultHandler);
			doScan(resultHandler);
			break;
		case R.id.tv_userId:
			doCommandGetShopUserListAndShowDialog();
			break;
		case R.id.btn_clear_userId:
			tv_userId.setText("");
			btn_clear_userId.setVisibility(tv_userId.getText().length()==0?View.GONE:View.VISIBLE);
			startTime=StringUtil.getTodayStartInMillis();
			endTime=System.currentTimeMillis();
//			loadDataByDate(startTime,endTime);
			loadData(startTime,endTime);
			break;
		case R.id.btn_startDate:
			showDatePickerDialog(btn_startDate);
			break;
		case R.id.btn_endDate:
			showDatePickerDialog(btn_endDate);
			break;
		case R.id.btn_search:
			String shelf=et_locID.getText().toString().trim();
			if(!isEmpty(shelf)){
				loadDataByShelf(shelf);
			}else{
//				startTime=StringUtil.getTodayStartInMillis();
//				endTime=System.currentTimeMillis();
				startTime=StringUtil.timeToStamp(btn_startDate.getText().toString()+" 00:00:00");
				endTime=StringUtil.timeToStamp(btn_endDate.getText().toString()+" 23:59:59");
//				loadDataByDate(startTime,endTime);
				loadData(startTime,endTime);
			}
			break;
		}
	}
	
	private void doCommandGetShopUserListAndShowDialog(){
		doCommandGetShopUserListAndShowDialog(new OnSelectUserInfoListener() {
			
			@Override
			public void doResult(UserInfo instance) {
				// TODO Auto-generated method stub
				tv_userId.setText(instance.getUser_code());
				btn_clear_userId.setVisibility(tv_userId.getText().length()==0?View.GONE:View.VISIBLE);
//				loadDataByUser(instance.getUser_code());
				loadData(startTime,endTime);
			}
		});
	}
	
	private void showDatePickerDialog(final TextView v){
		com.widget.view.DatePickerDialog d=new com.widget.view.DatePickerDialog(context,"取消","确定",v.getText().toString());
		d.setOnButtonClickListener(new com.widget.view.DatePickerDialog.OnButtonClickListener() {
			
			@Override
			public void onOKClick(String year, String month, String date) {
				// TODO Auto-generated method stub
				v.setText(year+"-"+month+"-"+date);
				if(v.getId()==R.id.btn_startDate){
					startTime=StringUtil.timeToStamp(v.getText().toString()+" 00:00:00");
					if(btn_endDate.getText().length()>0){
						endTime=StringUtil.timeToStamp(btn_endDate.getText().toString()+" 23:59:59");
					}
				}else if(v.getId()==R.id.btn_endDate){
					endTime=StringUtil.timeToStamp(v.getText().toString()+" 23:59:59");
					if(btn_startDate.getText().length()>0){
						startTime=StringUtil.timeToStamp(btn_startDate.getText().toString()+" 00:00:00");
					}
				}
				if(btn_startDate.getText().length()>0&&btn_endDate.getText().length()>0){
//					loadDataByDate(startTime,endTime);
					loadData(startTime,endTime);
				}
				
			}
			
			@Override
			public void onCancelClick() {
				// TODO Auto-generated method stub
			}
		});
		d.show();
	}
	
	private void loadData(long startTime,long endTime){
		String gh=tv_userId.getText().toString();
		List<CustomPanDianDan> list=null;
		if(isEmpty(gh)){
			list=DataSupport.where("date between ? and ?",String.valueOf(startTime),String.valueOf(endTime)).order("date desc").find(CustomPanDianDan.class);
		}else{
			list=DataSupport.where("gh = ? AND date between ? and ?",gh,String.valueOf(startTime),String.valueOf(endTime)).order("date desc").find(CustomPanDianDan.class);
		}
		beans.clear();
		beans.addAll(list);
		updateViews(beans);
	}
	
//	private void loadDataByDate(long startTime,long endTime){
//		List<CustomPanDianDan> list=DataSupport.where("date between ? and ?",String.valueOf(startTime),String.valueOf(endTime)).order("date desc").find(CustomPanDianDan.class);
//		beans.clear();
//		beans.addAll(list);
//		updateViews(beans);
//	}
	private void loadDataByShelf(String shelf){
		List<CustomPanDianDan> list=DataSupport.where("shelf like ?","%"+shelf+"%").order("date desc").find(CustomPanDianDan.class);
		beans.clear();
		beans.addAll(list);
		updateViews(beans);
	}
//	private void loadDataByUser(String gh){
//		List<CustomPanDianDan> list=DataSupport.where("gh = ?",gh).order("date desc").find(CustomPanDianDan.class);
//		beans.clear();
//		beans.addAll(list);
//		updateViews(beans);
//	}
	
	@Override
	public void onScanProductHandleMessage(String prodID){
		et_locID.setText(prodID);
	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		setThemeDrawable(context, R.id.btn_search);
		if(adapter!=null){
			adapter.notifyDataSetChanged();
		}
	}
	
	@Subscriber
	void updateByEventBus(String event) {
		if(event.equals(App.EVENT_UPDATE_CUSTOM_PANDIANDAN_LIST)){
			startTime=StringUtil.getTodayStartInMillis();
			endTime=System.currentTimeMillis();
//			loadDataByDate(startTime,endTime);
			loadData(startTime,endTime);
		}
	}
	
}
