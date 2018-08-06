package com.app.xstore;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.util.ServerFtplet;
import com.app.xstore.R;

/**
 * 远程管理
 * @author pythoner
 * 
 */
public class RemoteManagementActivity extends BaseActivity implements View.OnClickListener{

	private Context context;
	private TextView tv_des;
	private EditText et_ftp;
	private WifiInfo wifiInfo;
	private boolean checked=false;
	private ServerFtplet ftp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remote_management);
		context = this;
		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		wifiInfo = wifiManager.getConnectionInfo();
		
		ftp=ServerFtplet.getInstance();
		
		initActionBar("远程管理", null, null);
		initViews();
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		TextView tv_state=$(R.id.tv_state);
		tv_state.setText("网络状态\n"+wifiInfo.getSSID());
		//0到-50表示信号最好，-50到-70表示信号偏差，小于-70表示最差，有可能连接不上或者掉线
		int level=Math.abs(wifiInfo.getRssi());
//		Log.i("tag", "level="+level);
		ImageView tv_rssi=$(R.id.tv_rssi);
		tv_rssi.setImageLevel(level);
		
		tv_des=$(R.id.tv_des);
		et_ftp=$(R.id.et_ftp);
		$(R.id.btn_ok).setOnClickListener(this);
	}

	@Override
	public void doRightButtonClick(View v) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		setThemeDrawable(context, R.id.btn_ok);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_ok:
			if(checked){
				tv_des.setText("打开后可以在计算机上管理本设备上的文件");
				et_ftp.setVisibility(View.GONE);
				et_ftp.setText("");
				((TextView)v).setText("打开");
				ftp.stop();
			}else{
				tv_des.setText("在计算机中输入:");
				et_ftp.setVisibility(View.VISIBLE);
				et_ftp.setText("ftp://"+getIP()+":"+ServerFtplet.PORT+"/");
				((TextView)v).setText("关闭");
				ftp.start();
			}
			checked=!checked;
			break;

		default:
			break;
		}
	}
	
	 @Override
    protected void onDestroy() {
        super.onDestroy();
        ftp.stop();
    }
	
}
