package com.app.xstore.mendiandiaochu;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.TextView;

import com.app.widget.SimpleListPopupWindow;
import com.app.widget.dialog.DoubleDatePickerDialog;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;

/**
 * 
 * 
 * @author pythoner
 * 
 */
public class FilterActivity extends BaseActivity implements OnClickListener {

	private TextView tv_date,tv_status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mendian_diaochu_filter);
		initActionBar("查询" ,null, null);
		
		initViews();
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		$(R.id.btn_date).setOnClickListener(this);
		$(R.id.btn_status).setOnClickListener(this);
		
		tv_date = $(R.id.tv_date);
		tv_status = $(R.id.tv_status);
		
		$(R.id.btn_ok).setOnClickListener(this);

	}

	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
	}

	private Item curItemDay;
	private Item curItemStatus;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_date:
			String format="yyyy-MM-dd";
			Calendar calendar = Calendar.getInstance();
			
			String today=formatTime(calendar.getTimeInMillis(), format);
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			String yesterday=formatTime(calendar.getTimeInMillis(), format);
			calendar.add(Calendar.DAY_OF_MONTH, -6);
			String weekAgo=formatTime(calendar.getTimeInMillis(), format);
			calendar.add(Calendar.DAY_OF_MONTH, -24);
			String monthAgo=formatTime(calendar.getTimeInMillis(), format);
			
			final List<Item> dateList=new ArrayList<Item>();
			dateList.add(new Item("今天",today,today));
			dateList.add(new Item("昨天",yesterday,yesterday));
			dateList.add(new Item("最近一周",weekAgo,today));
			dateList.add(new Item("最近一月",monthAgo,today));
			dateList.add(new Item("自定义",null,null));
			View view=LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
			SimpleListPopupWindow<Item> p=new SimpleListPopupWindow<Item>(context, view, v.getWidth(), dateList);
			p.setOnItemClickListener(new SimpleListPopupWindow.OnItemClickListener<Item>() {

				@Override
				public void onItemClick(int position, final Item item) {
					// TODO Auto-generated method stub
					if(position==dateList.size()-1){
						Calendar c = Calendar.getInstance();
						// 最后一个false表示不显示日期，如果要显示日期，最后参数可以是true或者不用输入
						new DoubleDatePickerDialog(context, 0, new DoubleDatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
									int startDayOfMonth, DatePicker endDatePicker, int endYear, int endMonthOfYear,
									int endDayOfMonth) {
								String startDate = String.format("%d-%02d-%02d", startYear,startMonthOfYear + 1, startDayOfMonth);
								String endDate = String.format("%d-%02d-%02d", endYear, endMonthOfYear + 1, endDayOfMonth);
								tv_date.setText(startDate+"至"+endDate);
								curItemDay=item;
								curItemDay.startValue=startDate;
								curItemDay.endValue=endDate;
							}
						}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), true).show();
					}else{
						tv_date.setText(item.describe);
						curItemDay=item;
					}
					
				}
			});
			p.showAsDropDown(v);
			break;
		case R.id.btn_status:
			View viewPop=LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
			SimpleListPopupWindow<Item> pop=new SimpleListPopupWindow<Item>(context, viewPop, v.getWidth(), MenDianDiaoChuActivity.statusList);
			pop.setOnItemClickListener(new SimpleListPopupWindow.OnItemClickListener<Item>() {

				@Override
				public void onItemClick(int position, Item item) {
					// TODO Auto-generated method stub
					tv_status.setText(item.describe);
					curItemStatus=item;
				}
			});
			pop.showAsDropDown(v);
			break;
		case R.id.btn_ok:
			Intent intent=new Intent();
			if(curItemDay!=null){
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
					Date startDate = sdf.parse(curItemDay.startValue);
		            Date endDate = sdf.parse(curItemDay.endValue);
		            if(startDate.after(endDate)){
		            	String msg="开始时间不能大于结束时间";
						showToast(msg);
						return;
		            }
		            int days = (int) ((endDate.getTime() - startDate.getTime())/(1000 * 60 * 60 * 24));
		            if(days>90){
						String msg="时间跨度不能超过三个月";
						showToast(msg);
						return;
					}
		            intent.putExtra("describeDate", curItemDay.describe);
		            intent.putExtra("startDate", curItemDay.startValue);
		            intent.putExtra("endDate", curItemDay.endValue);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(curItemStatus!=null){
				intent.putExtra("statusName", curItemStatus.describe);
				intent.putExtra("statusCode", curItemStatus.startValue);
			}
			setResult(0, intent);
			finish();
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
	

}
