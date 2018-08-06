package com.app.xstore;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.app.fragment.SettingFragment;
import com.app.xstore.R;

/**
 * 
 * @author pythoner
 * 
 */
public class SettingActivity extends BaseActivity {

	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		context = this;
		initActionBar("设置", null, null);
		initViews();
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.fragment_setting);
		if(fragment!=null){
			SettingFragment f=(SettingFragment)fragment;
			f.updateTheme(color);
		}
	}
	
}
