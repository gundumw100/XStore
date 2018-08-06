package com.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.widget.EmailListDialog;
import com.app.xstore.App;
import com.app.xstore.SettingPrinterActivity;
import com.app.xstore.ThemeChangeActivity;
import com.app.xstore.R;
/**
 * 设置
 * @author pythoner
 *
 */
public class SettingFragment extends BaseFragment implements OnClickListener {

	public SettingFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.fragment_setting, container,false);
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		initViews(view);
	}
	@Override
	public void initViews(View view){
		view.findViewById(R.id.tv_print).setOnClickListener(this);
		view.findViewById(R.id.tv_email).setOnClickListener(this);
		view.findViewById(R.id.tv_skin).setOnClickListener(this);
		
		TextView tv_local_ip=(TextView)view.findViewById(R.id.tv_local_ip);
		tv_local_ip.setText("本机IP:  "+(TextUtils.isEmpty(getIP())?"未知":getIP()));
		
		TextView tv_version=(TextView)view.findViewById(R.id.tv_version);
		tv_version.setText("软件版本:  "+App.config.getVersionName());
		
	}
	
	/**
	 * 获得本机IP
	 * 
	 * @return
	 */
	public String getIP() {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (wifiManager.isWifiEnabled()) {
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int ipAddress = wifiInfo.getIpAddress();// 获取本机IP地址
			return intToIp(ipAddress);
		}
		return null;
	}

	private String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
	}
	
	@Override
	public void onClick(View v) {
		Intent intent=null;
		switch (v.getId()) {
		case R.id.tv_print:
			intent=new Intent(context,SettingPrinterActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_email:
			showSimpleTextDialog();
			break;
		case R.id.tv_skin:
			intent=new Intent(context,ThemeChangeActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		
	}

	private void showSimpleTextDialog(){
		EmailListDialog d = new EmailListDialog(context);
		d.show();
	}

	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
//		((BaseActivity) context).setThemeDrawable(context, btn_phone);
//		((BaseActivity) context).setThemeDrawable(context, btn_ok);
	}
	
}
