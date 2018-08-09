package com.app.xstore;

import com.app.model.UserInfo;
import com.app.xstore.R;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

/**
 * 分类汇总
 * @author pythoner
 * 
 */
public class UserInfoActivity extends BaseActivity{

	private Context context;
	private TextView tv_content;
	private String goods_sn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		context = this;
		initActionBar("用户信息", null, null);
		initViews();
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		tv_content=(TextView)findViewById(R.id.tv_content);
		UserInfo bean=App.user.getUserInfo();
		tv_content.setText("门店ID："+bean.getShop_code()+"\n\n门店名称："+App.user.getShopInfo().getShop_name()+"\n\n用户ID："+bean.getUser_code()+"\n\n用户名："+bean.getUser_name());
	}
	
	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		updateTheme();
	}
	
	private void updateTheme() {
	}
	
}
