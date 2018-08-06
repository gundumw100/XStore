package com.app.xstore.member;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.app.xstore.BaseActivity;
import com.app.xstore.R;

/**
 * 会员信息
 * 
 * @author pythoner
 * 
 */
@Deprecated
public class MemberInfoActivity extends BaseActivity{

	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_info);
		context = this;
		initActionBar("会员查询", null, null);
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

}
