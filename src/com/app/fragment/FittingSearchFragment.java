package com.app.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.model.Dress;
import com.app.model.response.GetDressDataByDateResponse;
import com.app.net.Commands;
import com.app.widget.EmailSenderDialog;
import com.app.xstore.App;
import com.app.xstore.fitting.FittingDetailActivity;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;
import com.base.util.comm.TimeUtils;


/**
 * 试衣查询
 * @author pythoner
 *
 */
public class FittingSearchFragment extends BaseFragment implements OnClickListener{

	private TextView btn_startDate,btn_endDate;
	private ListView listView;
	private CommonAdapter<Dress> adapter;
	private List<Dress> beans=new ArrayList<Dress>();
	
	public FittingSearchFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.fragment_fitting_search, container,false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		initViews(view);
		updateTheme(view);
		doCommandGetDressDataByDate();
	}
	
	@Override
	public void initViews(View view){
		String today=TimeUtils.getNow("yyyy-MM-dd");
		btn_startDate=$(view,R.id.btn_startDate);
		btn_startDate.setOnClickListener(this);
		btn_startDate.setText(today);
		btn_endDate=$(view,R.id.btn_endDate);
		btn_endDate.setOnClickListener(this);
		btn_endDate.setText(today);
		
		listView=$(view,R.id.listView);
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,FittingDetailActivity.class);
				intent.putExtra("DocNum", beans.get(position).getDressCode());
				startActivity(intent);
			}

		});
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_startDate:
			showDatePickerDialog(btn_startDate);
			break;
		case R.id.btn_endDate:
			showDatePickerDialog(btn_endDate);
			break;
		}
	}

	private void doCommandGetDressDataByDate(){
		String shop_code=App.user.getShopInfo().getShop_code();
		String startDate=btn_startDate.getText().toString();
		String endDate=btn_endDate.getText().toString();
		
		Commands.doCommandGetDressDataByDate(context, shop_code, startDate, endDate,  new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				if(context.isSuccess(response)){
					GetDressDataByDateResponse obj=context.mapperToObject(response, GetDressDataByDateResponse.class);
					updateViews(obj);
				}
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
				doCommandGetDressDataByDate();
			}
			
			@Override
			public void onCancelClick() {
				// TODO Auto-generated method stub
			}
		});
		d.show();
	}
	
	@Override
	public void updateViews(Object obj) {
		if(obj==null){
			return;
		}
		beans.clear();
		GetDressDataByDateResponse res=(GetDressDataByDateResponse)obj;
		beans.addAll(res.getHeadInfo());
		notifyDataSetChanged();
	}
	
	private void notifyDataSetChanged(){
		if(adapter==null){
			listView.setAdapter(adapter = new CommonAdapter<Dress>( context, beans,
					  R.layout.item_for_fitting_search){
					  
					@Override
					public void setValues(ViewHolder helper, final Dress item, final int position) {
						// TODO Auto-generated method stub
						helper.setText(R.id.item_0, item.getDressCode());
						helper.setText(R.id.item_1, item.getCreate_time().substring(0, 16));
						helper.setText(R.id.item_2, String.valueOf(item.getTotal_qty()));
						helper.setText(R.id.item_3, item.getCreate_user());
					}
			});
		}else{
			adapter.notifyDataSetChanged();
		}
	}
	
	@Subscriber
	void updateByEventBus(String event) {
		if(event.equals(App.EVENT_EXPORT_FITTING)){
			if(context.isEmptyList(beans)){
				showToast("没有数据可以导出");
				return;
			}
			EmailSenderDialog d=new EmailSenderDialog(context);
			d.setOnSenderListener(new EmailSenderDialog.OnSenderListener() {
				
				@Override
				public void onSender(List<String> emails) {
					// TODO Auto-generated method stub
					doCommandSendFittingInfo(emails);
				}
			});
			d.show();
		}else if(event.equals(App.EVENT_UPDATE_FITTING)){
			doCommandGetDressDataByDate();
		}
	}
	
	private void doCommandSendFittingInfo(List<String> emails){
		String shop_code=App.user.getShopInfo().getShop_code();
		String createUser=App.user.getUserInfo().getUser_code();
		String startDate=btn_startDate.getText().toString();
		String endDate=btn_endDate.getText().toString();
		Commands.doCommandSendFittingInfo(context, shop_code, startDate, endDate, createUser, emails, new Listener<JSONObject>(){

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				if(context.isSuccess(response)){
					showToast("发送成功");
				}
			}
			
		});
	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		updateTheme(getView());
	}
	
	private void updateTheme(View view) {
//		if(context!=null&&view!=null){
//			context.setThemeDrawable(context,R.id.btn_search);
//		}
	}
	
}
