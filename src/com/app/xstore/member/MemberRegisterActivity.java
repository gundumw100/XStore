package com.app.xstore.member;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;

import com.app.fragment.BaseFragment;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;

/**
 * 会员注册
 * 
 * @author pythoner
 * 
 */
public class MemberRegisterActivity extends BaseActivity {

	private Context context;
	private Fragment fragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_register);
		context = this;
		initHandler();
		initActionBar("会员注册", null, null);
		initViews();
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		fragment=getSupportFragmentManager().findFragmentById(R.id.fragment_register);
	}

	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
	}

	//重写resultHandler
	@Override
	public void initHandler(){
			resultHandler = new Handler(){
				public void handleMessage(android.os.Message msg) {
					String data = (String) msg.obj;
					if(data.equalsIgnoreCase("time out")||data.equalsIgnoreCase("user canceled")){
						showToast(R.string.alert_no_barcode_found);
						return;
					}
					((MemberRegisterFragment)fragment).update(data);
				}
			};
		}
		
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.fragment_register);
		if(fragment!=null){
			BaseFragment baseFragment=(BaseFragment)fragment;
			baseFragment.updateTheme(color);
		}
	}

}
