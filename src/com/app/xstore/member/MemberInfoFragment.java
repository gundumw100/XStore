package com.app.xstore.member;

import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.fragment.BaseFragment;
import com.app.model.Member;
import com.app.model.response.GetVipInfoResponse;
import com.app.net.Commands;
import com.app.xstore.App;
import com.app.xstore.R;

/**
 * 会员信息
 * 
 * @author pythoner
 * 
 */
public class MemberInfoFragment extends BaseFragment implements OnClickListener {

	private EditText et_cardNo, et_name;
	private TextView tv_info;

	private volatile static MemberInfoFragment instance;

	public static MemberInfoFragment getInstance() {
		if (instance == null) {
			synchronized (MemberInfoFragment.class) {
				if (instance == null) {
					instance = new MemberInfoFragment();
				}
			}
		}
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.fragment_member_info, container,false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		initViews(view);
		updateTheme(view);
	}
	
	@Override
	public void initViews(View view) {
		et_cardNo = $(view,R.id.et_cardNo);
		et_name = $(view,R.id.et_name);
		tv_info = $(view,R.id.tv_info);
		
		$(view,R.id.btn_scan).setOnClickListener(this);
		$(view,R.id.btn_search).setOnClickListener(this);
	}

	private void doCommandGetVipInfo(String vipCode,String mobile,String name){
		Commands.doCommandGetVipInfo(context, vipCode, mobile,name, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				Log.i("tag", response.toString());
				if(context.isSuccess(response)){
					GetVipInfoResponse obj=context.mapperToObject(response, GetVipInfoResponse.class);
					updateViews(obj);
				}
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_scan:
			context.doScan(context.resultHandler);
			break;
		case R.id.btn_search:
			String cardNo=et_cardNo.getText().toString().trim();
			String name=et_name.getText().toString().trim();
			if(context.isEmpty(cardNo)&&context.isEmpty(name)){
				showToast("请输入查询条件");
				return;
			}
			String mobile=null;
			String vipCode=null;
			if(cardNo.length()==11){
				mobile=cardNo;
			}else{
				vipCode=cardNo;
			}
			if(context.isEmpty(name)){
				name=null;
			}
			doCommandGetVipInfo(vipCode,mobile,name);
			break;

		default:
			break;
		}
	}

	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		updateTheme(getView());
	}

	private void updateTheme(View view) {
		if (context != null && view != null) {
			context.setThemeDrawable(context, R.id.btn_scan);
			context.setThemeDrawable(context, R.id.btn_search);
		}
	}

	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
		if(obj==null){
			return;
		}
		
		GetVipInfoResponse res=(GetVipInfoResponse)obj;
		List<Member> list=res.getHeadInfo();
		if(context.isEmptyList(list)){
			tv_info.setText("未找到会员信息");
		}else{
			Member bean=list.get(0);
			tv_info.setText(
					"会员ID："+bean.getVipNo()+"\n"+
					"卡号："+bean.getVipCode()+"\n"+
					"会员积分："+bean.getTotalPoints()+"\n"+
					"可抵扣金额："+bean.getTotalValue()+"\n"+
					"手机号："+bean.getMobile()+"\n"+
					"姓名："+bean.getName()+"\n"+
					"性别："+bean.getSex()+"\n"+
					"出生年月："+bean.getBirth()+"\n"+
					"地址："+bean.getAddress()+"\n"+
					"注册门店：\n"+
					"注册时间："+bean.getCreatetimeStr()+"\n"
					);
		}
	}

	public void update(String data){
		et_cardNo.setText(data);
	}

}
