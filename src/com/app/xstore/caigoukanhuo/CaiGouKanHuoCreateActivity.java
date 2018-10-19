package com.app.xstore.caigoukanhuo;

import android.os.Bundle;

import com.app.xstore.BaseActivity;
import com.app.xstore.R;

/**
 * 采购看货列表
 * 
 * @author Ni Guijun
 * 
 */
public class CaiGouKanHuoCreateActivity extends BaseActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_caigoukanhuo_create);
		initActionBar("采购看货记录", null, null);
		initViews();
	}

	@Override
	public void initViews() {
	}

	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
	}


}