package com.app.xstore;

import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.model.ShopInfo;
import com.app.model.UserInfo;
import com.app.model.VersionInfo;
import com.app.model.response.CheckVersionResponse;
import com.app.model.response.LoginCheckDeviceResponse;
import com.app.net.Commands;
import com.app.util.StatusBarUtil;
import com.app.xstore.R;
import com.base.util.UpdateManager;
import com.base.util.UpdateManager.OnUpdateListener;
import com.base.util.comm.SPUtils;

/**
 * 用户登录
 * 
 * @author pythoner
 *
 */
public class UserAssistantLoginActivity extends BaseActivity implements OnClickListener {

	private Context context;
	private EditText et_assistantId, et_password;
	private CheckBox checkBox;
	private String KEY_SHOP_CODE = "shop_code";
	private String KEY_USER_CODE = "user_code";
	private String KEY_USER_PWD = "user_pwd";
	private String KEY_REMEMBER_USER = "remember_user";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_assistant_login);
		StatusBarUtil.setTranslucent(this);
		context = this;
		initHandler();
		initViews();
		initActionBarTheme();
		String device_type = "PAD";//无花果
		String version_id = String.valueOf(App.config.getVersionCode());
		doCommandCheckVersion(device_type,version_id);
	}

	private void doCommandCheckVersion(String device_type, String version_id) {
		Commands.doCommandCheckVersion(context, device_type, version_id, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
//				 Log.i("tag", response.toString());
				// {"Version_Info":null,"ErrMessage":"","Result":true,"ErrSysTrackMessage":"","ErrSysMessage":"","Message":"没有新版本"}
				 //{"ActionName":"CheckVersion","Pars":{"device_type":"PAD","version_id":"2018062401"}}
				//{"Version_Info":{"version_name":"V1.1","remark":null,"version_type":0,"device_type":null,"cancel_desc":null,"approver_date":"0001-01-01T00:00:00","modify_date":"0001-01-01T00:00:00","cancel_name":null,"approver_id":null,"version_date":"2018-06-25T17:59:55.693","file_name":"20180625175955.apk","downloads":0,"file_url":"http:\/\/www.jianve.top:8080\/Versions\/20180625175955.apk","forceupdate":false,"cancel_id":null,"approver_name":null,"version":"2018062501","doc_state":null,"modify_user":null,"create_date":"0001-01-01T00:00:00","create_user":null,"cancel_date":"0001-01-01T00:00:00","introduction":"更新；","approver_desc":null},"ErrMessage":"","Result":true,"ErrSysTrackMessage":"","ErrSysMessage":"","Message":"获取新版本成功"}

				 if (isSuccess(response)) {
					CheckVersionResponse obj = mapperToObject(response, CheckVersionResponse.class);
					final VersionInfo bean = obj.getVersion_Info();
					if (bean != null) {
						UpdateManager updateManager = new UpdateManager(context);
						updateManager.setOnUpdateListener(new OnUpdateListener() {

							@Override
							public void onUpdate(boolean isUpdate) {
								// TODO Auto-generated method
								if (isUpdate) {
									// doCommandDownLoadSuccessNotify(bean.getId());
								}
							}
						});
						int forceupdate = bean.getForceupdate() ? 1 : 0;
						updateManager.setUpdateParms(bean.getFile_url(), Integer.parseInt(bean.getVersion()),
								forceupdate, bean.getIntroduction(), bean.getVersion_name());
						updateManager.doUpdate();
					}

				}
			}
		});
	}

	@Override
	public void initViews() {
//		findViewById(R.id.btn_offline).setOnClickListener(this);
//		findViewById(R.id.btn_pandian).setOnClickListener(this);
		findViewById(R.id.btn_left).setOnClickListener(this);
		findViewById(R.id.btn_right).setOnClickListener(this);
		findViewById(R.id.btn_scan).setOnClickListener(this);
//		et_shopId = (EditText) findViewById(R.id.et_shopId);
		et_assistantId = (EditText) findViewById(R.id.et_assistantId);
		et_password = (EditText) findViewById(R.id.et_password);
		checkBox = (CheckBox) findViewById(R.id.checkBox);
//		et_shopId.setText((String) SPUtils.get(context, KEY_SHOP_CODE, ""));
		et_assistantId.setText((String) SPUtils.get(context, KEY_USER_CODE, ""));
		et_password.setText((String) SPUtils.get(context, KEY_USER_PWD, ""));
		checkBox.setChecked((Boolean) SPUtils.get(context, KEY_REMEMBER_USER, false));
		
		TextView tv_versionName = (TextView) findViewById(R.id.tv_versionName);
		tv_versionName.setText(App.config.getVersionName());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=null;
		switch (v.getId()) {
//		case R.id.btn_offline:
//			intent=new Intent(context,OfflineDocumentListActivity.class);
//			intent.putExtra("CanUpload", false);
//			startActivity(intent);
//			break;
//		case R.id.btn_pandian:
//			intent=new Intent(context,PanDianListActivity.class);
//			startActivity(intent);
//			break;
		case R.id.btn_left:
			intent = new Intent(context, RegisterActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_right:
//			String shop_code = et_shopId.getText().toString().trim();
			String user_code = et_assistantId.getText().toString().trim();
			String user_pwd = et_password.getText().toString().trim();
			if (TextUtils.isEmpty(user_code)) {
				showToast("员工ID不能为空");
				return;
			}
			if (checkBox.isChecked()) {
//				SPUtils.put(context, KEY_SHOP_CODE, shop_code);
				SPUtils.put(context, KEY_USER_CODE, user_code);
				SPUtils.put(context, KEY_USER_PWD, user_pwd);
				SPUtils.put(context, KEY_REMEMBER_USER, true);
			} else {
//				SPUtils.remove(context, KEY_SHOP_CODE);
				SPUtils.remove(context, KEY_USER_CODE);
				SPUtils.remove(context, KEY_USER_PWD);
				SPUtils.remove(context, KEY_REMEMBER_USER);
			}
			doCommandLoginCheck(null,user_code, user_pwd);
			break;
		case R.id.btn_scan:
			doScan(resultHandler);
			break;

		default:
			break;
		}
	}

	@Override
	public void initHandler(){
		resultHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				String data = (String) msg.obj;
				et_assistantId.setText(getParameter(data, "sid"));
				et_password.setText("");
			}
		};
	}
	
	private void doCommandLoginCheck(String shop_code,String user_code, String user_pwd) {
		String imei = App.config.getDeviceId();
		String role_type = "FFA";//
		String device_type = "phone";
		Commands.doCommandLoginCheckDevice(context, shop_code, user_code, user_pwd, imei, role_type, device_type,new Listener<JSONObject>() {
				
			@Override
			public void onResponse(JSONObject response) {
//				Log.i("tag", response.toString());
				if (isSuccess(response)) {
					LoginCheckDeviceResponse obj = mapperToObject(response, LoginCheckDeviceResponse.class);
					updateViews(obj);
				}
			}
		});
	}

	@Override
	public void updateViews(Object obj) {
		LoginCheckDeviceResponse instance = (LoginCheckDeviceResponse) obj;
		List<UserInfo> sysUsers = instance.getUser_Info();
		if (sysUsers == null || sysUsers.size() == 0) {
			showToast(R.string.error_no_data);
		} else {
			UserInfo user_Info = sysUsers.get(0);
			App.user.setUserInfo(user_Info);

		}
		List<ShopInfo> shops = instance.getShop_Info();
		if (shops == null || shops.size() == 0) {
			showToast(R.string.error_no_data);
		} else {
			ShopInfo shop_Info = shops.get(0);
			App.user.setShopInfo(shop_Info);
		}

		// bug at:
		// http://zmywly8866.github.io/2014/12/26/android-do-not-store-data-in-the-application-object.html
		// SharedPreferencesUtils u=new
		// SharedPreferencesUtils(context,"share_data");
		// u.putObject("UserInfo", App.user.getUserInfo());
		// u.putObject("ShopInfo", App.user.getShopInfo());

		Intent intent = new Intent(context, MainActivity.class);
		startActivity(intent);
		finish();
	}

//	@Override
//	public void updateTheme(int color) {
//		// TODO Auto-generated method stub
//		super.updateTheme(color);
//		updateTheme();
//	}
//	
//	private void updateTheme() {
//		setThemeDrawable(context, R.id.btn_right);
//	}
	
}
