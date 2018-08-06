package com.app.xstore;

import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.app.net.Commands;
import com.app.xstore.R;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * 用户注册
 * @author pythoner
 * 
 */
public class RegisterActivity extends BaseActivity{

	private Context context;
	private EditText et_shop_code,et_user_code,et_name,et_password1,et_password2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		context = this;
		initActionBar("用户注册", "提交", null);
		initViews();
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		et_shop_code=$(R.id.et_shop_code);
		et_user_code=$(R.id.et_user_code);
		et_name=$(R.id.et_name);
		et_password1=$(R.id.et_password1);
		et_password2=$(R.id.et_password2);
	}
	
	@Override
	public void doRightButtonClick(View v) {
		// TODO Auto-generated method stub
		String shopCode=et_shop_code.getText().toString().trim();
		String userCode=et_user_code.getText().toString().trim();
		String userName=et_name.getText().toString().trim();
		String password1=et_password1.getText().toString().trim();
		String password2=et_password2.getText().toString().trim();
		
		if(isEmpty(shopCode)){
			showToast("请输入门店ID");
			return;
		}
		if(isEmpty(userCode)){
			showToast("请输入用户ID");
			return;
		}
		if(isEmpty(userName)){
			showToast("请输入用户名称");
			return;
		}
		if(isEmpty(password1)||isEmpty(password2)){
			showToast("请输入密码");
			return;
		}
		if(!password1.equals(password2)){
			showToast("两次输入的密码不一致");
			return;
		}
		doCommandAddUserInfo(shopCode,userCode,userName,password1);
	}
	
	private void doCommandAddUserInfo(String shopCode,String userCode,String userName,String userPwd){
		Commands.doCommandAddUserInfo(context, shopCode, userCode, userName, userPwd, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				if(isSuccess(response)){
					showToast("注册成功");
					finish();
				}
			}
		}); 
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
