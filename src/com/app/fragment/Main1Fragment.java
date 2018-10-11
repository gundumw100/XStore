package com.app.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.model.Member;
import com.app.model.response.GetVipInfoResponse;
import com.app.net.Commands;
import com.app.xstore.App;
import com.app.xstore.BaseActivity.OnScannerResult;
import com.app.xstore.R;
import com.app.xstore.member.MemberDetailActivity;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;


/**
 * 会员
 * @author pythoner
 *
 */
public class Main1Fragment extends BaseFragment implements View.OnClickListener{

	private EditText et_cardNo;
	private TextView btn_search;
	private ListView listView;
	private CommonAdapter<Member> adapter;
	private List<Member> beans=new ArrayList<Member>();
	    
	public static Main1Fragment newInstance(String param1) {
		Main1Fragment fragment = new Main1Fragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.fragment_main_1, container,false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		initViews(view);
		updateTheme(view);
		
		context.initScanner(new OnScannerResult() {
			
			@Override
			public void onResult(String data) {
				// TODO Auto-generated method stub
				et_cardNo.setText(data);
				btn_search.performClick();
			}
		});
	}
	
	@Override
	public void initViews(View view){
		et_cardNo=$(view,R.id.et_cardNo);
		if(App.isLog){
			et_cardNo.setText("13761083826");
		}
		$(view,R.id.btn_scan).setOnClickListener(this);
		btn_search=$(view,R.id.btn_search);
		btn_search.setOnClickListener(this);
		
		listView=$(view,R.id.listView);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> pearnt, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,MemberDetailActivity.class);
				intent.putExtra("Member", beans.get(position));
				startActivity(intent);
			}
		});
	}
	
	private void doCommandGetVipInfo(String vipCode,String mobile,String name){
		Commands.doCommandGetVipInfo(context, vipCode, mobile,name, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				if(context.isSuccess(response)){
					GetVipInfoResponse obj=context.mapperToObject(response, GetVipInfoResponse.class);
					List<Member> list=obj.getHeadInfo();
					updateViews(list);
				}
			}
		});
	}
	
	private void notifyDataSetChanged() {
		if (adapter == null) {
			listView.setAdapter(adapter = new CommonAdapter<Member>(
					context, beans, R.layout.item_member_list) {

				@Override
				public void setValues(ViewHolder helper,
						final Member item, final int position) {
					// TODO Auto-generated method stub
					helper.setText(R.id.item_name, item.getName());
					ImageView item_sex=helper.getView(R.id.item_sex);
					if("女".equals(item.getSex())){
						item_sex.setImageResource(R.drawable.ic_member_female48);
					}else{
						item_sex.setImageResource(R.drawable.ic_member_male48);
					}
					
					ImageView item_mobile=helper.getView(R.id.item_mobile);
					if(context.isEmpty(item.getMobile())){
						item_mobile.setVisibility(View.GONE);
					}else{
						item_mobile.setVisibility(View.VISIBLE);
						item_mobile.setImageResource(R.drawable.ic_member_mobile48);
					}
					
					helper.setText(R.id.item_vipNo, "会员ID："+item.getVipNo());
					helper.setText(R.id.item_vipCode, "会员卡号："+item.getVipCode());
					helper.setText(R.id.item_createtimeStr, "注册日期："+item.getCreatetimeStr());
					helper.setText(R.id.item_totalPoints, "可用积分："+item.getTotalPoints());
					helper.setText(R.id.item_totalValue, "可抵金额：￥"+item.getTotalValue());
					
				}

			});
		} else {
			adapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void updateViews(Object obj) {
		if(obj==null){
			return;
		}
		beans.clear();
		List<Member> list=(ArrayList<Member>)obj;
		beans.addAll(list);
		
		if(beans.size()==0){
			if(listView.getEmptyView()==null){
				context.setEmptyView(listView, "暂无数据");
			}
		}
		
		notifyDataSetChanged();
	}
	
//	@Subscriber
//	void updateByEventBus(String event) {
//		if(event.equals(App.EVENT_SAVE_FITTING)){
//		}
//	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		updateTheme(getView());
	}
	
	private void updateTheme(View view) {
		if(context!=null&&view!=null){
			context.setThemeDrawable(context,R.id.btn_scan);
			context.setThemeDrawable(context,R.id.btn_search);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_scan:
			context.doScan(context.resultHandler);
			break;
		case R.id.btn_search:
			String cardNo=et_cardNo.getText().toString().trim();
			if(context.isEmpty(cardNo)){
				context.doShake(context, et_cardNo);
				return;
			}
			String mobile=null;
			String vipCode=null;
			if(context.isMobilePhone(cardNo)){
				mobile=cardNo;
			}else{
				vipCode=cardNo;
			}
			doCommandGetVipInfo(vipCode,mobile,null);
			break;

		default:
			break;
		}
	}
	
}
