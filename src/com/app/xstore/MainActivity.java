package com.app.xstore;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fragment.Main0Fragment;
import com.app.fragment.Main1Fragment;
import com.app.fragment.Main2Fragment;
import com.app.fragment.Main3Fragment;
import com.app.model.City;
import com.app.model.ShopInfo;
import com.app.model.Weather;
import com.app.model.response.WeatherResponse;
import com.app.util.WeatherUtil;
import com.app.widget.SimpleListDialog;

public class MainActivity extends BaseActivity implements View.OnClickListener{

	private TextView tv_temperature,tv_city;
	private WeatherUtil weather;
	
	private Main0Fragment fragment0;
	private Main1Fragment fragment1;
	private Main2Fragment fragment2;
	private Main3Fragment fragment3;
	private final int COUNT=4;
	private RadioButton[] rbs;
	private int index = 0;//
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initActionBar(null, getDrawableCompat(R.drawable.ic_user), getShop(), "设置", null);
		initViews();
		initWeather();
		//开启打印机服务监听
		App.printerUtil.bindPrinterService();
		
	}

	private String getShop(){
		if(App.user==null||App.user.getShopInfo()==null){
			return "";
		}
		return App.user.getShopInfo().getShop_name();
	}
	
	private void initWeather(){
		weather=new WeatherUtil(this);
		weather.setOnWeatherListener(new WeatherUtil.OnWeatherListener() {
			
			@Override
			public void onWeather(WeatherResponse obj) {
				// TODO Auto-generated method stub
				try{
					City city=obj.getResults().get(0);
					Weather weather=city.getWeather_data().get(0);
					tv_temperature.setText(weather.getTemperature());
					tv_city.setText(city.getCurrentCity());
				}catch(Exception e){
					tv_temperature.setText("");
					tv_city.setText("");
				}
			}
		});
		weather.start();
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		if(isCompanyUser()){
			setCompoundDrawable(R.drawable.arrow_down40, getTitleView(), 2);
			getTitleView().setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showCompanyShopsDialog();
				}
			});
		}
		
		tv_temperature=(TextView)findViewById(R.id.tv_temperature);
		tv_city=(TextView)findViewById(R.id.tv_city);
		initIndicators();
	}

	private void showCompanyShopsDialog(){
		ArrayList<ShopInfo> companyShop=App.user.getCompanyShop();
		if(companyShop==null){
			return;
		}
		SimpleListDialog<ShopInfo> d=new SimpleListDialog<ShopInfo>(context, companyShop);
		d.setOnItemClickListener(new SimpleListDialog.OnItemClickListener<ShopInfo>() {

			@Override
			public void onItemClick(View v, ShopInfo item,
					int position) {
				// TODO Auto-generated method stub
				//商户用户切换门店后，赋值新门店
				App.user.setShopInfo(item);
				getTitleView().setText(getShop());
			}
		});
		d.show();
	}
	
	@Override
	public void doRightButtonClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(context, SettingActivity.class);
		startActivity(intent);
	}

	@Override
	public void doLeftButtonClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(context, UserInfoActivity.class);
		startActivity(intent);
	}
	
	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub

	}

	private long exitTime = 0;
	private void exitApp() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(context, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			finish();
			System.exit(0);
		}
	}

	@Override
	public void onBackPressed() {
		exitApp();
		// moveTaskToBack(true);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		weather.stop();
		App.printerUtil.closePrinterService();
	}

	private void initIndicators() {
		rbs = new RadioButton[COUNT];
		rbs[0] = $(R.id.btn_0);
		rbs[1] = $(R.id.btn_1);
		rbs[2] = $(R.id.btn_2);
		rbs[3] = $(R.id.btn_3);
		for (int i = 0; i < rbs.length; i++) {
			rbs[i].setOnClickListener(this);
		}
		rbs[index].performClick();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_0:
			index = 0;
			switchFragment(index);
			break;
		case R.id.btn_1:
			index = 1;
			switchFragment(index);
			break;
		case R.id.btn_2:
			index = 2;
			switchFragment(index);
			break;
		case R.id.btn_3:
			index = 3;
			switchFragment(index);
			break;
		}
	}
	
	/**
	 * 默认false
	 * 
	 * @param index
	 */
	private void switchFragment(int index) {
		switchFragment(index, true);
	}

	/**
	 * 
	 * @param index
	 * @param allowingStateLoss
	 *            为了解决IllegalStateException: Can not perform this action after
	 *            onSaveInstanceState
	 */
	private void switchFragment(int index, boolean allowingStateLoss) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		hideAllFragment(transaction);
		switch (index) {
		case 0:
			if (fragment0 == null) {
				fragment0 = new Main0Fragment();
				transaction.add(R.id.container, fragment0);
			}else{
				transaction.show(fragment0);
			}
			break;
		case 1:
			if (fragment1 == null) {
				fragment1 = new Main1Fragment();
				transaction.add(R.id.container, fragment1);
			} else{
				transaction.show(fragment1);
			}
			break;
		case 2:
			if (fragment2 == null) {
				fragment2 = new Main2Fragment();
				transaction.add(R.id.container, fragment2);
			}else{
				transaction.show(fragment2);
			}
			break;
		case 3:
			if (fragment3 == null) {
				fragment3 = new Main3Fragment();
				transaction.add(R.id.container, fragment3);
			} else{
				transaction.show(fragment3);
			}
			break;

		}
		// transaction.addToBackStack(null);
		if (allowingStateLoss) {
			transaction.commitAllowingStateLoss();
		} else {
			transaction.commit();
		}
	}

	private void hideAllFragment(FragmentTransaction transaction){
		if (fragment0 != null) {
			transaction.hide(fragment0);
		}
		if (fragment1 != null) {
			transaction.hide(fragment1);
		}
		if (fragment2 != null) {
			transaction.hide(fragment2);
		}
		if (fragment3 != null) {
			transaction.hide(fragment3);
		}
	}
	
	public void gotoFragment(int position) {
//		rbs[position].performClick();
		index = position;
		rbs[index].setChecked(true);
		switchFragment(index, true);
	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		if(fragment0!=null){
			fragment0.updateTheme(color);
		}
		if(fragment1!=null){
			fragment1.updateTheme(color);
		}
		if(fragment2!=null){
			fragment2.updateTheme(color);
		}
		if(fragment3!=null){
			fragment3.updateTheme(color);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
