package com.app.xstore.member;

import java.util.ArrayList;

import org.json.JSONObject;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.fragment.BaseFragment;
import com.app.net.Commands;
import com.app.widget.SimplePairListPopupWindow;
import com.app.xstore.App;
import com.app.xstore.R;
import com.base.util.comm.TimeUtils;
import com.widget.view.UserBirthdayDialog;

/**
 * 会员注册
 * 
 * @author pythoner
 * 
 */
public class MemberRegisterFragment extends BaseFragment implements OnClickListener {

	private TextView et_sex, et_birthday;
	private EditText et_cardNo, et_phone, et_name, et_address;
	private TextView et_memberId, et_regStore, et_regDate;

	private volatile static MemberRegisterFragment instance;

	public static MemberRegisterFragment getInstance() {
		if (instance == null) {
			synchronized (MemberRegisterFragment.class) {
				if (instance == null) {
					instance = new MemberRegisterFragment();
				}
			}
		}
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.fragment_member_register, container,false);
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
		et_memberId = $(view,R.id.et_memberId);
		et_cardNo = $(view,R.id.et_cardNo);
		et_phone = $(view,R.id.et_phone);
		et_sex = $(view,R.id.et_sex);
		et_sex.setOnClickListener(this);
		et_name = $(view,R.id.et_name);
		et_birthday = $(view,R.id.et_birthday);
		et_birthday.setOnClickListener(this);
		et_address = $(view,R.id.et_address);
		et_regStore = $(view,R.id.et_regStore);
		et_regDate = $(view,R.id.et_regDate);

		et_memberId.setVisibility(View.GONE);
		et_regStore.setVisibility(View.GONE);
		et_regDate.setVisibility(View.GONE);
//		et_memberId.setText(App.user.getUserInfo().getShop_code()
//				+ TimeUtils.getNow("yyyyMMddHHmmssSSS"));
//		et_regStore.setText(App.user.getUserInfo().getShop_code());
//		et_regDate.setText(TimeUtils.getNow("yyyy-MM-dd"));
		

		$(view,R.id.btn_scan1).setOnClickListener(this);
		$(view,R.id.btn_submit).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_scan1:
			context.doScan(context.resultHandler);
			break;
		case R.id.btn_submit:
			doCommandCreateVipInfo();
			break;
		case R.id.et_sex:
			showSimplePopupWindow(et_sex);
			break;
		case R.id.et_birthday:
			showDatePickerDialog(et_birthday);
			break;

		default:
			break;
		}
	}

	private void doCommandCreateVipInfo(){
		String shopCode= App.user.getShopInfo().getShop_code();
		String vipCode=et_cardNo.getText().toString().trim();
//		if (context.isEmpty(vipCode)) {
//			showToast("请输入卡号");
//			context.doShake(context, et_cardNo);
//			return;
//		}
		String mobile = et_phone.getText().toString().trim();
		String name = et_name.getText().toString().trim();
		String sex = et_sex.getText().toString().trim();
		
		if (context.isEmpty(mobile)) {
			showToast("请输入手机号");
			context.doShake(context, et_phone);
			return;
		}
		if (context.isEmpty(name)) {
			showToast("请输入姓名");
			context.doShake(context, et_name);
			return;
		}
		if (context.isEmpty(sex)) {
			showToast("请输入性别");
			context.doShake(context, et_sex);
			return;
		}
		
		
		String birth = et_birthday.getText().toString().trim();
		String address = et_address.getText().toString().trim();
		String createuser=App.user.getUserInfo().getUser_code();
//		String regStore = et_regStore.getText().toString().trim();
//		String regDate = et_regDate.getText().toString().trim();
		
		Commands.doCommandCreateVipInfo(context, shopCode, vipCode, mobile, sex, name, birth, address, createuser, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				if(context.isSuccess(response)){
					showToast("创建成功");
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
		if (context != null && view != null) {
			context.setThemeDrawable(context, R.id.btn_scan1);
			context.setThemeDrawable(context, R.id.btn_submit);
		}
	}

	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub

	}

	private void showSimplePopupWindow(View v) {
		ArrayList<Pair<Integer, String>> beans = new ArrayList<Pair<Integer, String>>();
		beans.add(new Pair<Integer, String>(0, "男"));
		beans.add(new Pair<Integer, String>(1, "女"));

		View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
		final SimplePairListPopupWindow<Integer, String> popupWindow = new SimplePairListPopupWindow<Integer, String>(
				context, view, v.getWidth(), beans);
		popupWindow.showAsDropDown(v, 0, 0);
		popupWindow.setOnItemClickListener(new SimplePairListPopupWindow.OnItemClickListener<Integer, String>() {

					@Override
					public void onItemClick(int position,
							Pair<Integer, String> pair) {
						// TODO Auto-generated method stub
						et_sex.setText(pair.second);
					}
				});
	}

	private void showDatePickerDialog(final TextView tv) {
		UserBirthdayDialog d = new UserBirthdayDialog(context, tv.getText().toString());
		d.setOnOKClickListener(new UserBirthdayDialog.OnOKClickListener() {

			@Override
			public void onOKClick(View v, String year, String month, String day) {
				// TODO Auto-generated method stub
				tv.setText(year + "-" + month + "-" + day);
			}
		});
		d.show();
	}

	public void update(String data){
		et_cardNo.setText(data);
	}
	
}
