package com.app.xstore;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.UrlQuerySanitizer;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.model.JvbillsaleInfo;
import com.app.model.JvbillsalebankInfo;
import com.app.model.JvbillsaledetailInfo;
import com.app.model.JvbillsalepayInfo;
import com.app.model.Member;
import com.app.model.UserInfo;
import com.app.model.response.GetBillSaleByNumResponse;
import com.app.model.response.GetShopUserListResponse;
import com.app.model.response.SaveBillSaleResponse;
import com.app.net.Commands;
import com.app.printer.BluetoothDeviceListActivity;
import com.app.util.ScannerInterface;
import com.app.util.ThemeManager;
import com.app.util.ThimfoneScanUtil;
import com.app.widget.SimpleListDialog;
import com.app.widget.dialog.PrintTicketDialog;
import com.app.xstore.shangpindangan.ProductDangAn;
import com.app.xstore.shangpindangan.ProductDetailActivity;
import com.app.xstore.space.ImageActivity;
import com.app.xstore.space.ImageGalleryActivity;
import com.base.app.BaseAppActivity;
import com.base.util.D;
import com.base.util.comm.DisplayUtils;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.zltd.decoder.DecoderManager;
import com.zxing.activity.CaptureActivity;

public abstract class BaseActivity extends BaseAppActivity implements ThemeManager.IThemeListener,DecoderManager.IDecoderStatusListener {

	public BaseActivity context;
	public App app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		app = (App) getApplication();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		finishScannerInterface();
	}

	/**
	 * 初始化view组件，通常写在实现类的onCreate()中
	 */
	public abstract void initViews();

	/**
	 * 初始化view中内容，通常写在实现类的onResume()中
	 */
	// public abstract void initValues();
	/**
	 * 刷新view中内容，通常写在网络请求成功后的回调函数中
	 */
	public abstract void updateViews(Object obj);

	/**
	 * ActionBar,默认有返回键
	 * 
	 * @param title
	 * @param rightBtn
	 * @param drawableForRightBtn
	 */
	public void initActionBar(String title, String rightBtn, Drawable drawableForRightBtn) {
		createActionBar(null, getDrawableCompat(R.drawable.left), title, rightBtn, drawableForRightBtn);
		initActionBarTheme();
	}

	public void initActionBar(String leftBtn, Drawable drawableForLeftBtn, String title, String rightBtn,
			Drawable drawableForRightBtn) {
		createActionBar(leftBtn, drawableForLeftBtn, title, rightBtn, drawableForRightBtn);
		initActionBarTheme();
	}

	private TextView btn_left, btn_right;
	private TextView tv_title;
	private View actionView;
	/**
	 * 左右两边分别带一个按钮的通用的ActionBar
	 * 
	 * @param leftBtn
	 * @param drawableForLeftBtn
	 * @param title
	 * @param rightBtn
	 * @param drawableForRightBtn
	 */
	public void createActionBar(String leftBtn, Drawable drawableForLeftBtn,
			String title, String rightBtn, Drawable drawableForRightBtn) {
		
		actionView =findViewById(R.id.actionbar);
		tv_title = (TextView) findViewById(R.id.tv_title);
		if (tv_title != null) {
			tv_title.setText(title);
		}
		btn_left = (TextView) findViewById(R.id.btn_left);
		if (btn_left != null) {
			if(leftBtn==null&&drawableForLeftBtn==null){
				btn_left.setVisibility(View.GONE);
			}else{
				btn_left.setVisibility(View.VISIBLE);
				btn_left.setText(leftBtn==null?"":leftBtn);
				if (drawableForLeftBtn != null) {
					btn_left.setCompoundDrawablePadding(8);
					drawableForLeftBtn.setBounds(0, 0,
							drawableForLeftBtn.getMinimumWidth(),
							drawableForLeftBtn.getMinimumHeight());
					btn_left.setCompoundDrawables(drawableForLeftBtn, null, null,
							null);
				}else{
					btn_left.setCompoundDrawables(null, null, null,null);
				}
	
				btn_left.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						doLeftButtonClick(v);
					}
				});
			}
		}

		btn_right = (TextView) findViewById(R.id.btn_right);
		if (btn_right != null) {
			if(rightBtn==null&&drawableForRightBtn==null){
				btn_right.setVisibility(View.GONE);
			}else{
				btn_right.setVisibility(View.VISIBLE);
				btn_right.setText(rightBtn==null?"":rightBtn);
	
				if (drawableForRightBtn != null) {
					btn_right.setCompoundDrawablePadding(8);
					drawableForRightBtn.setBounds(0, 0,
							drawableForRightBtn.getMinimumWidth(),
							drawableForRightBtn.getMinimumHeight());
					btn_right.setCompoundDrawables(null, null, drawableForRightBtn,
							null);
				}else{
					btn_right.setCompoundDrawables(null, null, null,null);
				}
	
				btn_right.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						doRightButtonClick(v);
					}
				});
			}
		}
		
	}
	
	public View getActionView() {
//		actionView.setVisibility(View.VISIBLE);
		return actionView;
	}
	public TextView getTitleView() {
//		actionView.setVisibility(View.VISIBLE);
		return tv_title;
	}
	public TextView getRightButton() {
		btn_right.setVisibility(View.VISIBLE);
		return btn_right;
	}

	public TextView getLeftButton() {
		btn_left.setVisibility(View.VISIBLE);
		return btn_left;
	}
	
	/**
	 * 用于重设ActionBar中的标题
	 * 
	 * @param text
	 */
	public void setTitle(String text) {
		tv_title.setText(text);
	}

	/**
	 * 按下右上角执行该方法，需要override该方法
	 */
	public void doRightButtonClick(View v) {

	}

	/**
	 * 按下左上角执行该方法，默认关闭该activity，必要时需要override该方法
	 */
	public void doLeftButtonClick(View v) {
		finish();
	}

	protected Dialog dialog;

	/**
	 * 网络连接时显示Loading
	 */
	@Override
	public void showProgress() {
		if(isFinishing()){
			return;
		}
		if (dialog == null) {
			dialog = new Dialog(this, R.style.Theme_TransparentDialog);
//			dialog.setContentView(R.layout.custom_progress_dialog);
			dialog.setContentView(new ProgressBar(getApplicationContext()));
			dialog.setCancelable(true);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}
	}

	@Override
	public void closeProgress() {
		if (dialog != null&&!isFinishing()) {
			dialog.cancel();
			dialog = null;
		}
	}

	//////////////////////////////////////////////////////////////////
	// 换肤
	//////////////////////////////////////////////////////////////////
	public void initActionBarTheme() {
		ThemeManager.with(this).registerListener(this);
		updateTheme(ThemeManager.with(this).getCurrentColor());
	}

	@Override
	public void onThemeChange(int color) {
		// TODO Auto-generated method stub
		updateTheme(color);
	}

	public void updateTheme(int color) {
		if (getActionView() != null) {
			getActionView().setBackgroundColor(color);
			setThemeDrawable(this, getLeftButton(), R.drawable.btn_pressed);
			setThemeDrawable(this, getRightButton(), R.drawable.btn_pressed);
		}
	}

	public int getThemeColor() {
		return ThemeManager.with(context).getCurrentColor();
	}

	public void setThemeColor(View view) {
		if (view == null) {
			return;
		}
		view.setBackgroundColor(ThemeManager.with(context).getCurrentColor());
	}

	public void setThemeColor(int viewId) {
		setThemeColor(findViewById(viewId));
	}

	public void setThemeDrawable(Context context, View view, int drawableId) {
		if (view == null) {
			return;
		}
		ColorDrawable normal = new ColorDrawable(ThemeManager.with(context).getCurrentColor());
		StateListDrawable sDrawable = new StateListDrawable();
		sDrawable.addState(new int[] { android.R.attr.state_pressed }, context.getResources().getDrawable(drawableId));
		sDrawable.addState(new int[] { -android.R.attr.state_enabled}, context.getResources().getDrawable(R.color.grayMiddle));
		sDrawable.addState(new int[] {}, normal);
		view.setBackgroundDrawable(sDrawable);
		// view.setBackground(sDrawable);
	}

	public void setThemeCircleDrawable(Context context, View view) {
		if (view == null) {
			return;
		}
		int size = view.getMeasuredWidth();
		ShapeDrawable normal = new ShapeDrawable(new OvalShape());
		normal.setBounds(0, 0, size, size);
		normal.getPaint().setColor(ThemeManager.with(context).getCurrentColor());
		view.setBackgroundDrawable(normal);
		// view.setBackground(sDrawable);
	}

	public void setThemeDrawable(Context context, int viewId, int drawableId) {
		setThemeDrawable(context, findViewById(viewId), drawableId);
	}

	public void setThemeDrawable(Context context, View view) {
		setThemeDrawable(context, view, R.drawable.btn_pressed);
	}

	public void setThemeDrawable(Context context, int viewId) {
		setThemeDrawable(context, viewId, R.drawable.btn_pressed);
	}

	public void setThemeDrawableChecked(Context context, View view) {
		if (view == null) {
			return;
		}
		ColorDrawable checked = new ColorDrawable(ThemeManager.with(context).getCurrentColor());
		ColorDrawable normal = new ColorDrawable(Color.TRANSPARENT);
		StateListDrawable drawable = new StateListDrawable();
		drawable.addState(new int[] { android.R.attr.state_checked }, checked);
		drawable.addState(new int[] {}, normal);
		view.setBackgroundDrawable(drawable);
		// view.setBackground(sDrawable);
	}

	////////////////////////////////////////////////////////////////
	// 网络数据相关
	////////////////////////////////////////////////////////////////
	/**
	 * 网络请求返回成功与否,若不成功默认显示Toast
	 * 
	 * @param response
	 * @return
	 */
	public boolean isSuccess(JSONObject response) {
		return isSuccess(response, true);
	}

	public boolean isSuccess(JSONObject response, boolean showToast) {
		closeProgress();
		Boolean isSuccess = response.optBoolean("Result");
		if (isSuccess) {
			return true;
		} else {
			if (showToast) {
				String msg = response.optString("Message");
				// String msg = response.optString("ErrMessage");
				showToast(msg);
			}
			return false;
		}
	}

	public <T> T mapperToObject(JSONObject response, Class<T> clazz) {
		return new Gson().fromJson(response.toString(), clazz); 
		
//		ObjectMapper mapper = new ObjectMapper();
//		// 反序列化时，忽略不匹配或者Bean不存在的字段
//		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//		mapper.configure(Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, false);
//		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
//		try {
//			return mapper.readValue(result, clazz);
//		} catch (JsonParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
	}

	////////////////////////////////////////////////////////////////
	// FloatView相关
	////////////////////////////////////////////////////////////////
	private WindowManager wm;
	private ImageView view;// 浮动按钮

	/**
	 * 添加悬浮View
	 * 
	 * @param paddingBottom
	 *            悬浮View与屏幕底部的距离
	 */
	protected void createFloatView(int marginBottom) {
		int w = 56;// 大小
		int pxValue=DisplayUtils.dip2px(context, w);
		wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//		view = getLayoutInflater().inflate(R.layout.floatview, null);
//		view.setBackgroundColor(Color.TRANSPARENT);
		view=new ImageView(context);
		view.setVisibility(View.VISIBLE);
		view.setImageResource(R.drawable.ic_scan);
		final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
//		params.type = LayoutParams.TYPE_BASE_APPLICATION;// 所有程序窗口的“基地”窗口，其他应用程序窗口都显示在它上面。
		params.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
		params.format = PixelFormat.TRANSLUCENT;// 不设置这个弹出框的透明遮罩显示为黑色
		params.width = pxValue;
		params.height = pxValue;
		params.gravity = Gravity.TOP | Gravity.LEFT;
		int screenWidth = getResources().getDisplayMetrics().widthPixels;
		int screenHeight = getResources().getDisplayMetrics().heightPixels;
		params.x = screenWidth - pxValue;
		params.y = screenHeight - pxValue - marginBottom;
		view.setOnTouchListener(new OnTouchListener() {
			// 触屏监听
			float lastX, lastY;
			int oldOffsetX, oldOffsetY;
			int tag = 0;// 悬浮球 所需成员变量

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				final int action = event.getAction();
				float x = event.getX();
				float y = event.getY();
				if (tag == 0) {
					oldOffsetX = params.x; // 偏移量
					oldOffsetY = params.y; // 偏移量
				}
				if (action == MotionEvent.ACTION_DOWN) {
					lastX = x;
					lastY = y;
				} else if (action == MotionEvent.ACTION_MOVE) {
					params.x += (int) (x - lastX) / 3; // 减小偏移量,防止过度抖动
					params.y += (int) (y - lastY) / 3; // 减小偏移量,防止过度抖动
					tag = 1;
					wm.updateViewLayout(view, params);
				} else if (action == MotionEvent.ACTION_UP) {
					int newOffsetX = params.x;
					int newOffsetY = params.y;
					// 只要按钮一动位置不是很大,就认为是点击事件
					if (Math.abs(oldOffsetX - newOffsetX) <= 20 && Math.abs(oldOffsetY - newOffsetY) <= 20) {
						onFloatViewClick();
					} else {
						tag = 0;
					}
				}
				return true;
			}
		});
		wm.addView(view, params);
	}

	/**
	 * 点击浮动按钮触发事件，需要override该方法
	 */
	protected void onFloatViewClick() {
		scanType=0;
		doScan(resultHandler,this);
	}

	/**
	 * 将悬浮View从WindowManager中移除，需要与createFloatView()成对出现
	 */
	public void removeFloatView() {
		if (wm != null && view != null) {
			wm.removeViewImmediate(view);
			// wm.removeView(view);//不要调用这个，WindowLeaked
			view = null;
			wm = null;
		}
	}

	/**
	 * 隐藏悬浮View
	 */
	public void hideFloatView() {
		if (wm != null && view != null && view.isShown()) {
			view.setVisibility(View.GONE);
		}
	}

	/**
	 * 显示悬浮View
	 */
	public void showFloatView() {
		if (wm != null && view != null && !view.isShown()) {
			view.setVisibility(View.VISIBLE);
		}
	}

	////////////////////////////////////////////////////////////////
	// 
	////////////////////////////////////////////////////////////////
	/**
	 * 跳转到ImageGalleryActivity
	 * @param imgUrls 图片地址集合
	 * @param position 当前选中的图片位置
	 */
	public void changeToImageGalleryActivity(ArrayList<String> imgUrls,int position) {
		Intent intent=new Intent(context,ImageGalleryActivity.class);
		intent.putStringArrayListExtra("Imgs", imgUrls);
		intent.putExtra("Position", position);
		startActivity(intent);
	}
	////////////////////////////////////////////////////////////////
	// 通知相关
	////////////////////////////////////////////////////////////////
	// public Shop getShop(Context context) {
	// SharedPreferencesUtils u = new SharedPreferencesUtils(context,
	// "share_data");
	// Shop shop = u.getObject("Shop", Shop.class);
	// return shop;
	// }

	/**
	 * 左右抖动动画
	 * @param context
	 * @param view
	 */
	public void doShake(Context context, View view){
		doAnimation(context, view, R.anim.shake);
	}
	/**
	 * 
	 * @param listView
	 * @param text
	 */
	public void setEmptyView(ListView listView, String text) {
		TextView emptyView = new TextView(context);
		emptyView.setLayoutParams(
				new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		emptyView.setText(text);
		emptyView.setTextColor(Color.GRAY);
		emptyView.setGravity(Gravity.CENTER);
		emptyView.setVisibility(View.GONE);
		((ViewGroup) listView.getParent()).addView(emptyView);
		listView.setEmptyView(emptyView);
	}

	/**
	 * 获得本机IP
	 * 
	 * @return
	 */
	public String getIP() {
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
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

	/**
	 * 
	 * @param mobiles
	 * @return
	 */
	public boolean isMobilePhone(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 判断List是否是空列表
	 * 
	 * @param list
	 * @return
	 */
	public boolean isEmptyList(List list) {
		return list == null || list.size() == 0;
	}

	public String nullToEmpty(String text) {
		return text == null ? "" : text;
	}

	public boolean isEmpty(TextView tv){
		return isEmpty(tv.getText().toString().trim());
	}
	
	/**
	 * 获得url中对应key的参数值（value）
	 * 
	 * @param url
	 * @param key
	 * @return
	 */
	public String getParameter(String url, String key) {
		if (!TextUtils.isEmpty(url)) {
			UrlQuerySanitizer sanitizer = new UrlQuerySanitizer(url);
			sanitizer.setAllowUnregisteredParamaters(true);
			return sanitizer.getValue(key);
		}
		return null;
	}

	/**
	 * 从url中截获11位商品号
	 * 
	 * @param url
	 * @return
	 */
	public String getProdID(String url) {
		// http://shop.banggo.com/product/24409219130true
		if (!TextUtils.isEmpty(url)) {
			int index = url.lastIndexOf("/") + 1;
			String temp = url.substring(index);
			return temp.replace("true", "");
		}
		return null;
	}
	
	/**
	 * 
	 * @param text
	 * @return
	 */
	public boolean isEmail(String text){
		String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";    
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(text);
		return matcher.matches();
	}
	
	/**
	 * 是否是商户用户
	 * @return
	 */
	public boolean isCompanyUser(){
		return App.user.getUserInfo().getCompanyUser()==1;
	}
	////////////////////////////////////////////////////////////////////////////
	// Thimfone 机型扫描相关
	/////////////////////////////////////////////////////////////////////////////
	public int scanType=0;//扫描类型：默认0扫描商品，1扫描货位
	public void setResultHandler(Handler resultHandler){
		this.resultHandler=resultHandler;
	}
	/**
	 * 扫描商品后的Handler，需要覆盖onScanProductHandleMessage(id)
	 */
	public Handler resultHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			String message = (String) msg.obj;
			String prodID = getProdID(message);
			if (TextUtils.isEmpty(prodID)) {
				showToast(R.string.alert_no_barcode_found);
			}else{
				onScanProductHandleMessage(prodID);
			}
		}
	};
	
	/**
	 * 初始化resultHandler专用
	 * 凡是使用了扫描的界面都需要重写该方法
	 */
	@Deprecated
	public void initHandler(){
//		resultHandler = new Handler(){...};
		resultHandler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				String message = (String) msg.obj;
				String prodID = getProdID(message);
				if (TextUtils.isEmpty(prodID)) {
					showToast(R.string.alert_no_barcode_found);
				}else{
					onScanProductHandleMessage(prodID);
				}
				
			}
		};
	}
	
	public interface OnScannerResult{
		void onResult(String data);
	}
	public void initScanner(final OnScannerResult onScannerResult){
		resultHandler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				String data = (String) msg.obj;
				if(onScannerResult!=null){
					onScannerResult.onResult(data);
				}
			}
		};
	}
	
	/**
	 * 启动扫描
	 * 如果扫描的是商品，请覆盖onScanProductHandleMessage(id)，无需覆盖initHandler()；
	 * 如果扫描其他(货架，门店，用户等)，需要覆盖initHandler(),无需覆盖onScanProductHandleMessage(id)；
	 * @param resultHandler
	 */
	public void doScan(Handler resultHandler) {
		doScan(resultHandler, this);
	}
	
	/**
	 * 启动扫描
	 * 子类中已不推荐使用该方法，请使用doScan(Handler)替代
	 * @param resultHandler
	 */
	private void doScan(Handler resultHandler, DecoderManager.IDecoderStatusListener listener) {
		if (App.user.getPhoneType()==0) {
			doScan(listener);
		} else if (App.user.getPhoneType()==1){
			App.scanUtil.doScan(resultHandler);
		}else if (App.user.getPhoneType()==2){
			initScannerInterface();
		}else{
			Intent intent=new Intent(context,CaptureActivity.class);
			startActivityForResult(intent, BaseActivity.REQUEST_CODE_SCAN);
		}
	}

	public final static int REQUEST_CODE_SCAN= 1001;//扫描请求
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		Log.i("tag", "requestCode="+requestCode+";resultCode="+resultCode);
		if(requestCode==REQUEST_CODE_SCAN){//扫描
			if (resultCode == RESULT_OK) {
				if(data!=null){
		            Bundle bundle = data.getExtras();
		            String result = bundle.getString("result");
		            sendMessage(resultHandler,result);
				}
	        }
		}
	}
	
	private ThimfoneScanUtil util = null;

	private void onPauseScan() {
		if (App.user.getPhoneType()==0) {
			if (util != null) {
				util.onPause();
				util.onDestroy();
				util = null;
			}
		}
	}

	public void releaseScan(){
		onPauseScan();
	}
	
	private void doScan(final DecoderManager.IDecoderStatusListener listener) {
		if (App.user.getPhoneType()==0) {
			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (util == null) {
//						Log.i("tag", "============初始化扫描开始============");
						util = new ThimfoneScanUtil(context, listener);
						util.onCreate();
						util.onResume();
						// util.enableLight(false);
//						Log.i("tag", "============初始化扫描结束============");
					}
					if (util != null) {
						exitTime = 0;
						util.singleShoot();
					}
				}
			});
			t.start();
		}
	}

	private void sendMessage(Handler handler, String result) {
//		Log.i("tag", result);
		// Decode is interruptted or timeout ...
		if (result.startsWith("Decode")) {
			if (util != null) {
//				util.play(false);
				util.enableLight(false);
			}
			return;
		}
		String suffix1 = " \\| QR";
		String suffix2 = " \\| CODE128/ISBT";
		String suffix3 = " \\| EAN13";
		Message message = handler.obtainMessage();
		message.obj = result.replaceAll(suffix1, "").replaceAll(suffix2, "").replaceAll(suffix3, "");
		handler.sendMessage(message);
		if (util != null) {
			util.play(true);
			if (!running) {
				tick();
			}
		}
	}

	private long exitTime = 0;
	private boolean running = false;

	private void tick() {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				running = true;
				while (running) {
					if (exitTime >= 8) {
						if (util != null) {
							util.enableLight(false);
						}
						running = false;
					}
					exitTime++;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		t.start();
	}
	
	/**
	 * 扫描商品成功后的回调，需要@Override
	 * @param prodID
	 */
	public void onScanProductHandleMessage(String prodID){
		
	}
	
	@Override
	public void onDecoderResultChanage(String result, String time) {
		sendMessage(resultHandler, result);
	}

	@Override
	public void onDecoderStatusChanage(int arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BUTTON_A && event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getRepeatCount() == 0) {
			scanType=0;
			doScan(resultHandler);
			return true;
		}
		return super.dispatchKeyEvent(event);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		running = false;
		onPauseScan();
		super.onPause();
	}
	////////////////////////////////////////////////////////////////////////////
	//
	/////////////////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////////////////
	//首先请求导购列表并显示于SimpleListDialog
	/////////////////////////////////////////////////////////////////////////////
	public interface OnSelectUserInfoListener {
		void doResult(UserInfo instance);
	}

	// 请求导购列表
	public void doCommandGetShopUserListAndShowDialog(final OnSelectUserInfoListener onSelectUserInfoListener) {
		if (App.userInfos == null) {
			String shop_code=App.user.getShopInfo().getShop_code();
			Commands.doCommandGetShopUserList(context, shop_code, new Listener<JSONObject>() {
				
				@Override
				public void onResponse(JSONObject response) {
					// TODO Auto-generated method stub
//					Log.i("tag", response.toString());
					if(isSuccess(response)){
						GetShopUserListResponse obj = mapperToObject(response,GetShopUserListResponse.class);
						App.userInfos = obj.getUser_Info();
						showSysUserDialog(onSelectUserInfoListener);
					}
				}
			});
		} else {
			showSysUserDialog(onSelectUserInfoListener);
		}
	}

	private void showSysUserDialog(final OnSelectUserInfoListener onSelectUserInfoListener) {
		SimpleListDialog<UserInfo> d = new SimpleListDialog<UserInfo>(context, App.userInfos);
		d.setOnItemClickListener(new SimpleListDialog.OnItemClickListener<UserInfo>() {

			@Override
			public void onItemClick(View v, UserInfo instance, int position) {
				// TODO Auto-generated method stub
				if (onSelectUserInfoListener != null) {
					onSelectUserInfoListener.doResult(instance);
				}
			}
		});
		d.show();
	}
	
	/**
	 * 支付生成小票
	 * @param payMode {"0支付宝","1微信","2现金","3刷卡"};
	 * @param actPayValue 实付金额(需要去掉代金券？)
	 * @param zhaoLin 找零金额
	 */
	public void doCommandSaveBillSale(int payMode,String actPayValue,String zhaoLin){
		double need=getIntent().getDoubleExtra("Need", 0.00);
		ArrayList<ProductDangAn> beans=getIntent().getParcelableArrayListExtra("Products");
		Member member=getIntent().getParcelableExtra("Member");
		String remark=getIntent().getStringExtra("Remark");
		if(isEmptyList(beans)){
			showToast("缺少商品数据");
			return;
		}
		String needStr=formatMoney(need);
		double totalMoney=Double.parseDouble(needStr);
		
		String[] modes={"支付宝","微信","现金","刷卡"};
		final JvbillsaleInfo billSale=new JvbillsaleInfo();
		billSale.setShopCode(App.user.getShopInfo().getShop_code());
		billSale.setCreateuser(App.user.getUserInfo().getUser_code());
		billSale.setCurrencyCode("RMB");
		billSale.setSaleType("XS");//销售模式:XS(销售)、TH(退货)、HH(换货)
		billSale.setTotalMoney(totalMoney);//总金额60
		billSale.setTotalQty(beans.size());//
		billSale.setUserId(App.user.getUserInfo().getUser_code());//
		
		billSale.setRemark(remark);//备注
		
		if(member!=null){
			billSale.setVipId(member.getVipNo());//会员ID
			billSale.setVipCode(member.getVipNo());// 卡号
			billSale.setVipConsumeValue(member.getVipConsumeValue());//消费积分
		}
		
		final List<JvbillsaledetailInfo> billsaleDetailList=new ArrayList<JvbillsaledetailInfo>();
		for(ProductDangAn bean:beans){
			JvbillsaledetailInfo item=new JvbillsaledetailInfo();
			item.setShopCode(App.user.getShopInfo().getShop_code());
			item.setProdNum(bean.getGoods_sn());
//			item.setProdName(bean.getGoods_name());
			item.setSellerUser(isEmpty(bean.getSellerUser())?App.user.getUserInfo().getUser_code():bean.getSellerUser());//导购
			item.setSaleMode("XS");//销售模式:XS(销售)、TH(退货)、HH(换货)
			item.setQty(1);
			item.setRemarkCode(modes[payMode]);//注释编码
			item.setRemark(modes[payMode]);//注释
			
			if(bean.getDiscount()==null){//(不打折)
				item.setDivAmount(0.00);//折扣金额(不打折)
				item.setDivSaleRate(100.00);//折扣率(不打折)
				
				String goods_priceStr=formatMoney(bean.getGoods_price());
				double goods_price=Double.parseDouble(goods_priceStr);
				
				item.setRetailPrice(goods_price);//零售价
				item.setSalePrice(goods_price);//折扣价(不打折)
			}else{
//				item.setDivAmount(bean.getGoods_price_discount_off());//折扣金额
//				item.setDivSaleRate(bean.getDiscount().getDiscountValue());//折扣率
//				item.setRetailPrice(bean.getGoods_price());//零售价
//				item.setSalePrice(bean.getGoods_price_discount());//折扣价
				
				String rateStr=formatNumber(bean.getDiscount().getDiscountRate(), "###0.00");
				double rate=Double.parseDouble(rateStr);
				
				item.setDivSaleRate(rate);//折扣率
				item.setRetailPrice(bean.getGoods_ls_price());//零售价
				
//				item.setDivAmount(bean.getDiscount().getDiscountPrice()*item.getQty());//折扣金额
//				item.setSalePrice(bean.getDiscount().getDiscountPrice());//折扣价
				
				//debug
				String divAmountStr=formatMoney(bean.getGoods_price()*item.getQty());
				double divAmount=Double.parseDouble(divAmountStr);
				item.setDivAmount(divAmount);//折扣金额
				
				String goods_priceStr=formatMoney(bean.getGoods_price());
				double goods_price=Double.parseDouble(goods_priceStr);
				item.setSalePrice(goods_price);//折扣价
			}
			billsaleDetailList.add(item);
		}
		
		final List<JvbillsalepayInfo> payList=new ArrayList<JvbillsalepayInfo>();
		JvbillsalepayInfo item=new JvbillsalepayInfo();
		if(payMode==0){
			item.setPayCode(modes[payMode]);
			item.setShopCode(App.user.getShopInfo().getShop_code());
			item.setChange("0.00");//找零金额
			item.setPayValue(formatMoney(need));//应付金额
			item.setActPayValue(formatMoney(need));//实付金额(需要去掉代金券？)
		}else if(payMode==1){
			item.setPayCode(modes[payMode]);
			item.setShopCode(App.user.getShopInfo().getShop_code());
			item.setChange("0.00");//找零金额
			item.setPayValue(formatMoney(need));//应付金额
			item.setActPayValue(formatMoney(need));//实付金额(需要去掉代金券？)
		}
		else if(payMode==2){//现金
			item.setPayCode(modes[payMode]);
			item.setShopCode(App.user.getShopInfo().getShop_code());
			item.setChange(zhaoLin);//找零金额
			item.setPayValue(formatMoney(need));//应付金额
			item.setActPayValue(actPayValue);//实付金额(需要去掉代金券？)
		}else if(payMode==3){//刷卡
			item.setPayCode(modes[payMode]);
			item.setShopCode(App.user.getShopInfo().getShop_code());
			item.setChange("0.00");//找零金额
			item.setPayValue(formatMoney(need));//应付金额
			item.setActPayValue(formatMoney(need));//实付金额(需要去掉代金券？)
		}
		payList.add(item);
		
		List<JvbillsalebankInfo> bankList=new ArrayList<JvbillsalebankInfo>();
		
		Commands.doCommandSaveBillSale(context,billSale,billsaleDetailList,payList,bankList,new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					SaveBillSaleResponse obj=mapperToObject(response, SaveBillSaleResponse.class);
					showPaySuccessDialog(obj.getSaleNo());
				}
			}
		});
	}
	
	private void showPaySuccessDialog(String saleNo) {
		PrintTicketDialog d=new PrintTicketDialog(this,saleNo);
		d.show();
	}
	
	/**
	 * 退货支付生成小票，退货只能现金
	 * @param payMode {"0支付宝","1微信","2现金","3刷卡"};
	 * @param actPayValue 实付金额(需要去掉代金券？)
	 * @param zhaoLin 找零金额
	 */
	public void doCommandSaveBillSaleTH(GetBillSaleByNumResponse response,String zhaoLin){
		int payMode=2;
//		double need=getIntent().getDoubleExtra("Need", 0.00);
//		ArrayList<ProductDangAn> beans=getIntent().getParcelableArrayListExtra("Products");
		List<JvbillsaledetailInfo> beans=response.getDetailList();
		double actPayValue=response.getBillSale().getTotalMoney();
		if(isEmptyList(beans)){
			showToast("缺少商品数据");
			return;
		}
		String[] modes={"支付宝","微信","现金","刷卡"};
		final JvbillsaleInfo billSale=new JvbillsaleInfo();
		
		billSale.setSaleNo(response.getBillSale().getSaleNo());//原单号
		
		billSale.setShopCode(App.user.getShopInfo().getShop_code());
		billSale.setCreateuser(App.user.getUserInfo().getUser_code());
		billSale.setCurrencyCode("RMB");
		billSale.setSaleType("TH");//销售模式:XS(销售)、TH(退货)、HH(换货)
		billSale.setTotalMoney(-1*actPayValue);//总金额60
		billSale.setTotalQty(-1*beans.size());//
		billSale.setUserId(App.user.getUserInfo().getUser_code());//
		billSale.setRemark("");//退货理由?
		if(response.getBillSale().getVipId()!=null){
			billSale.setVipId(response.getBillSale().getVipId());//会员ID
			billSale.setVipCode(response.getBillSale().getVipId());// 卡号
			billSale.setVipConsumeValue(response.getBillSale().getVipConsumeValue());//消费积分
		}
		
		final List<JvbillsaledetailInfo> billsaleDetailList=new ArrayList<JvbillsaledetailInfo>();
		for(JvbillsaledetailInfo bean:beans){
			JvbillsaledetailInfo item=new JvbillsaledetailInfo();
			item.setShopCode(App.user.getShopInfo().getShop_code());
			item.setProdNum(bean.getProdNum());
//			item.setProdName(bean.getGoods_name());
			item.setSellerUser(isEmpty(bean.getSellerUser())?App.user.getUserInfo().getUser_code():bean.getSellerUser());//导购
			item.setSaleMode("TH");//销售模式:XS(销售)、TH(退货)、HH(换货)
			item.setQty(-1);
			item.setRemarkCode(modes[payMode]);//注释编码
			item.setRemark(modes[payMode]);//注释
			
//			if(bean.getDiscount()==null){//(不打折)
//				item.setDivAmount(0.00);//折扣金额(不打折)
//				item.setDivSaleRate(100.00);//折扣率(不打折)
//				item.setRetailPrice(bean.getGoods_price());//零售价
//				item.setSalePrice(-1*bean.getGoods_price());//折扣价(不打折)
//			}else{
//				item.setDivAmount(bean.getDiscount().getDiscountPrice()*item.getQty());//折扣金额
//				item.setDivSaleRate(bean.getDiscount().getDiscountRate());//折扣率
//				item.setRetailPrice(bean.getGoods_ls_price());//零售价
//				item.setSalePrice(-1*bean.getDiscount().getDiscountPrice());//折扣价
//			}
			item.setDivAmount(bean.getDivAmount());//折扣金额
			item.setDivSaleRate(bean.getDivSaleRate());//折扣率
			item.setRetailPrice(bean.getRetailPrice());//零售价
			item.setSalePrice(-1*bean.getSalePrice());//折扣价
			billsaleDetailList.add(item);
		}
		
		final List<JvbillsalepayInfo> payList=new ArrayList<JvbillsalepayInfo>();
		JvbillsalepayInfo item=new JvbillsalepayInfo();
//		if(payMode==0||payMode==1){//支付宝||微信
//			item.setPayCode(null);
//			item.setShopCode(App.user.getShopInfo().getShop_code());
//			item.setChange("0.00");//找零金额
//			item.setPayValue(formatMoney(need));//应付金额
//			item.setActPayValue(formatMoney(need));//实付金额(需要去掉代金券？)
//		}else 
			if(payMode==2){//现金
			item.setPayCode(modes[payMode]);
			item.setShopCode(App.user.getShopInfo().getShop_code());
			item.setChange(zhaoLin);//找零金额
			item.setPayValue("-"+actPayValue);//应付金额
			item.setActPayValue("-"+actPayValue);//实付金额(需要去掉代金券？)
		}
//		else if(payMode==3){//刷卡
//			item.setPayCode(null);
//			item.setShopCode(App.user.getShopInfo().getShop_code());
//			item.setChange("0.00");//找零金额
//			item.setPayValue(formatMoney(need));//应付金额
//			item.setActPayValue(formatMoney(need));//实付金额(需要去掉代金券？)
//		}
		payList.add(item);
		
		List<JvbillsalebankInfo> bankList=new ArrayList<JvbillsalebankInfo>();
		
		Commands.doCommandSaveBillSale(context,billSale,billsaleDetailList,payList,bankList,new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
//					SaveBillSaleResponse obj=mapperToObject(response, SaveBillSaleResponse.class);
//					showPaySuccessDialog(obj.getSaleNo());
					showToast("退单成功");
					finish();
				}
			}
		});
	}
	
	/** 
     * 检测当的网络（WLAN、3G/2G）状态 
     * @param context Context 
     * @return true 表示网络可用 
     */  
	public boolean isNetworkAvailable() {
		ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null && info.isConnected()) {
				// 当前网络是连接的
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					// 当前所连接的网络可用
					return true;
				}
			}
		}
		return false;
	}
	//////////////////////////////////////////////////////////////////////////////
	//ScannerInterface扫描相关
	//////////////////////////////////////////////////////////////////////////////
	private ScannerInterface scanner;
	private IntentFilter intentFilter;
	private BroadcastReceiver scanReceiver;
	private boolean isContinue = false;	//连续扫描的标志
	private static final String RES_ACTION = "android.intent.action.SCANRESULT";
	
	private void initScannerInterface(){
		if(scanner==null){
			scanner = new ScannerInterface(this);
			scanner.open();	//打开扫描头上电   scanner.close();//打开扫描头下电
			scanner.enablePlayBeep(true);//是否允许蜂鸣反馈
			scanner.enableFailurePlayBeep(false);//扫描失败蜂鸣反馈
			scanner.enablePlayVibrate(true);//是否允许震动反馈
			scanner.enableAddKeyValue(0);/**附加无、回车、Teble、换行*/
			scanner.timeOutSet(2);//设置扫描延时2秒
			scanner.intervalSet(1000); //设置连续扫描间隔时间
			scanner.lightSet(false);//关闭右上角扫描指示灯
			scanner.enablePower(true);//省电模式
			//scanner.addPrefix("AAA");//添加前缀
			//scanner.addSuffix("BBB");//添加后缀
			//scanner.interceptTrimleft(2); //截取条码左边字符
			//scanner.interceptTrimright(3);//截取条码右边字符
			//scanner.filterCharacter("R");//过滤特定字符
			scanner.setErrorBroadCast(true);//扫描错误换行
			//scanner.resultScan();//恢复iScan默认设置
	
			//scanner.lockScanKey();
			//锁定设备的扫描按键,通过iScan定义扫描键扫描，用户也可以自定义按键。
			scanner.unlockScanKey();
			//释放扫描按键的锁定，释放后iScan无法控制扫描按键，用户可自定义按键扫描。
	
			/**设置扫描结果的输出模式，参数为0和1：
			 * 0为模拟输出（在光标停留的地方输出扫描结果）；
			 * 1为广播输出（由应用程序编写广播接收者来获得扫描结果，并在指定的控件上显示扫描结果）
			 * 这里采用接收扫描结果广播并在TextView中显示*/
			scanner.setOutputMode(1);
	
			//扫描结果的意图过滤器的动作一定要使用"android.intent.action.SCANRESULT"
			intentFilter = new IntentFilter(RES_ACTION);
			//注册广播接受者
			scanReceiver = new ScannerResultReceiver();
			registerReceiver(scanReceiver, intentFilter);
			
			scanner.scan_start();
		}else{
			scanner.scan_start();
		}
	}
	
	/**
	 * 结束扫描
	 */
	public void finishScannerInterface(){
		if(scanner!=null){
			scanner.continceScan(false);
			scanner.scan_stop();
			scanner.close();	//关闭iscan  非正常关闭会造成iScan异常退出
			scanner=null;
			unregisterReceiver(scanReceiver);	//反注册广播接收者
		}
	}
	
	/**
	 * 扫描结果的广播接收者
	 */
	private class ScannerResultReceiver extends BroadcastReceiver{
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(RES_ACTION)){
				final String result = intent.getStringExtra("value");//获取扫描结果
				sendMessage(resultHandler, result);
			}
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == 139&&event.getRepeatCount()==0){//正面黄色按钮
			initScannerInterface();
		}else if(keyCode == 140&&event.getRepeatCount()==0){//左面黄色按钮
			initScannerInterface();
		}else if(keyCode == 141&&event.getRepeatCount()==0){//右面黄色按钮
			initScannerInterface();
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == 139||keyCode == 140||keyCode == 140){
			if(scanner!=null){
				scanner.scan_stop();
			}
		}
		return super.onKeyUp(keyCode, event);
	}
	
	//////////////////////////////////////////////////////////////////////////////
	//
	//////////////////////////////////////////////////////////////////////////////
	
	public int getColorCompat(int resId){
		return ContextCompat.getColor(context, resId);
	}
	
	public Drawable getDrawableCompat(int resId){
		return ContextCompat.getDrawable(context, resId);
	}
	
	public void changeToImageActivity(View child,String url) {
		Intent intent = new Intent(context, ImageActivity.class);
		intent.putExtra("URL", url);
		ActivityOptions options = ActivityOptions.makeScaleUpAnimation(
				child, 0, 0, child.getWidth(), child.getHeight());
		startActivity(intent, options.toBundle());
	}
	
	public void showSimpleTextPopupWindow(View anchor,String text,int width,int height){
		ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		TextView tv=new TextView(context);
		tv.setLayoutParams(params);
		tv.setText(text);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		tv.setBackground(getDrawableCompat(R.drawable.bg_popupwindow));
		tv.setPadding(4, 4, 4, 4);
		PopupWindow p= new PopupWindow(context);
		p.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.bg_popupwindow));
		p.setOutsideTouchable(true);
		p.setFocusable(true); 
	    p.setTouchable(true);
	    p.setWidth(DisplayUtils.dip2px(context, width));
	    p.setHeight(DisplayUtils.dip2px(context,height));
		p.setContentView(tv);
		p.showAsDropDown(anchor);
	}
	public void showSimpleTextPopupWindow(View anchor,String text){
		showSimpleTextPopupWindow(anchor,text,240,2*32);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////
	public void startProductDetailActivity(String goods_sn){
		Intent intent=new Intent(context,ProductDetailActivity.class);
		intent.putExtra("goods_sn",goods_sn);
		startActivity(intent);
	}
	
	public String getCorrectDate(String unCorrectDate){
		if(unCorrectDate==null){
			return "";
		}
		if(unCorrectDate.length()>16){
			return unCorrectDate.substring(0, 16).replace("T", " ");
		}
		return unCorrectDate;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////
	
	public void loadImageByPicasso(String imageUrl,ImageView iv){
		if(isEmpty(imageUrl)){
			iv.setImageResource(R.drawable.default_img);
			return;
		}
		Picasso.with(context).load(imageUrl).placeholder(R.drawable.default_img).error(R.drawable.default_img).into(iv);
	}
	
	/**
	 * 加载形如url1;url2;url3;
	 * @param imageUrls
	 * @param iv
	 */
	public void loadMultImageByPicasso(String imageUrls,ImageView iv){
		if(!context.isEmpty(imageUrls)){
			String[] urls=imageUrls.split(";");
			if(urls!=null&&urls.length>0){
				loadImageByPicasso(urls[0],iv);
			}else{
				iv.setImageResource(R.drawable.default_img);
			}
		}else{
			iv.setImageResource(R.drawable.default_img);
		}
	}
	
	/**
	 * 打印标签或者打开蓝牙界面连接打印机
	 * @param name
	 * @param sku
	 * @param color
	 * @param size
	 * @param ls_price
	 */
	public void doPrintLabel(String name, String sku, String color, String size, String ls_price){
		if(App.printerUtil.isBluetoothAvailable()){
			if(App.printerUtil.isPrinterConnected()){
				App.printerUtil.sendLabel( name, sku, color, size, ls_price);
			}else{
				showToast("未连接到打印机");
				startActivity(new Intent(context,BluetoothDeviceListActivity.class));
			}
		}else{
			showToast("蓝牙未开启");
			startActivity(new Intent(context,BluetoothDeviceListActivity.class));
		}
	}
	
	/**
	 * 打印小票或者打开蓝牙界面连接打印机
	 * @param d 打印后关闭的Dialog，如果没有就填null
	 * @param obj
	 */
	public void doPrintReceipt(Dialog d,GetBillSaleByNumResponse obj){
		if(App.printerUtil.isBluetoothAvailable()){
			if(App.printerUtil.isPrinterConnected()){
				App.printerUtil.sendReceipt(obj);
				if(d!=null&&d.isShowing()){
					d.dismiss();
				}
			}else{
				showToast("未连接到打印机");
				startActivity(new Intent(context,BluetoothDeviceListActivity.class));
			}
		}else{
			showToast("蓝牙未开启");
			startActivity(new Intent(context,BluetoothDeviceListActivity.class));
		}
	}
	
	
}
