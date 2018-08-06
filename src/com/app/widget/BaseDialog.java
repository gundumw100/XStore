package com.app.widget;

//import android.app.Dialog;
//import android.content.Context;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.View;
//
//import com.app.util.ScannerInterface;
//import com.app.util.ThimfoneScanUtil;
//import com.app.xstore.App;
//import com.app.xstore.BaseActivity;
//import com.app.xstore.R;
//import com.base.util.T;
//import com.zltd.decoder.DecoderManager;
//
///**
// * 需要扫描的Dialog都继承此类
// * 提供简便的扫描调用
// * 
// * @author Ni Guijun
// *
// */
//public class BaseDialog extends Dialog implements DecoderManager.IDecoderStatusListener{
//
//	Context context;
//	public Handler resultHandler;//公共的Handler，部分子类需要覆盖
//	private ThimfoneScanUtil util = null;
//	
//	public BaseDialog(Context context) {
//		super(context);
//		// TODO Auto-generated constructor stub
//		this.context=context;
//	}
//	
//	public BaseDialog(Context context, int themeResId) {
//		super(context, themeResId);
//		// TODO Auto-generated constructor stub
//		this.context=context;
//	}
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.dialog_inventory);
//		//如果Activity中有扫描，先释放，否则会同时执行Activity中的handler和Dialog中的handler
//		((BaseActivity)context).releaseScan();
//		((BaseActivity)context).finishScannerInterface();
//		initHandler();
//	}
//	
//	public void doShake(View view){
//		((BaseActivity) context).doAnimation(context, view, R.anim.shake);
//	}
//	
//	public void showToast(int stringId){
//		((BaseActivity) context).showToast(stringId);
//	}
//	
//	@Override
//	public void onDecoderResultChanage(String result, String time) {
//		// TODO Auto-generated method stub
//		sendMessage(resultHandler, result);
//	}
//
//	@Override
//	public void onDecoderStatusChanage(int arg0) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	protected void onStop() {
//		// TODO Auto-generated method stub
//		super.onStop();
//		onPauseScan();
//		finishScannerInterface();
//	}
//	
//	public void onPauseScan() {
//		if (App.user.getPhoneType()==0) {
//			if (util != null) {
//				util.onPause();
//				util.onDestroy();
//				util = null;
//			}
//		}
//	}
//	
//	/**
//	 * 启动扫描
//	 * 
//	 * @param resultHandler
//	 */
//	public void doScan(Handler resultHandler) {
//		doScan(resultHandler, this);
//	}
//	/**
//	 * 启动扫描
//	 * 
//	 * @param resultHandler
//	 */
//	private void doScan(Handler resultHandler, DecoderManager.IDecoderStatusListener listener) {
//		if (App.user.getPhoneType()==0) {
//			doScan(listener);
//		} else if (App.user.getPhoneType()==1){
//			App.scanUtil.doScan(resultHandler);
//		}else if (App.user.getPhoneType()==2){
//			initScannerInterface();
//		}else{
//			T.showToast(context,"该机型不支持扫描");
//		}
//	}
//	
//	private void doScan(final DecoderManager.IDecoderStatusListener listener) {
//		if (App.user.getPhoneType()==0) {
//			Thread t = new Thread(new Runnable() {
//
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					Looper.prepare();
//					if (util == null) {
//						Log.i("tag", "============初始化扫描开始============");
//						util = new ThimfoneScanUtil(context, listener);
//						util.onCreate();
//						util.onResume();
//						// util.enableLight(false);
//						Log.i("tag", "============初始化扫描结束============");
//					}
//					if (util != null) {
//						exitTime = 0;
//						util.singleShoot();
//					}
//					Looper.loop();
//				}
//			});
//			t.start();
//		}
//	}
//	
//	private void sendMessage(Handler handler, String result) {
////		Log.i("tag", result);
//		// Decode is interruptted or timeout ...
//		if (result.startsWith("Decode")) {
//			if (util != null) {
////				util.play(false);
//				util.enableLight(false);
//			}
//			return;
//		}
//		String suffix1 = " \\| QR";
//		String suffix2 = " \\| CODE128/ISBT";
//		String suffix3 = " \\| EAN13";
//		Message message = handler.obtainMessage();
//		message.obj = result.replaceAll(suffix1, "").replaceAll(suffix2, "").replaceAll(suffix3, "");
//		handler.sendMessage(message);
//		if (util != null) {
//			util.play(true);
//			if (!running) {
//				tick();
//			}
//		}
//	}
//
//	private long exitTime = 0;
//	private boolean running = false;
//
//	private void tick() {
//		Thread t = new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				running = true;
//				while (running) {
//					if (exitTime >= 10) {
//						if (util != null) {
//							util.enableLight(false);
//						}
//						running = false;
//					}
//					exitTime++;
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//		});
//		t.start();
//	}
//	
//	private void initHandler(){
//		//公共的Handler，部分子类需要覆盖；扫描商品可以不覆盖，但是需要覆盖onScanProductHandleMessage方法
//		resultHandler = new Handler(){
//			public void handleMessage(android.os.Message msg) {
//				String message = (String) msg.obj;
//				if(message.equalsIgnoreCase("time out")){
//					T.showToast(context,R.string.alert_no_barcode_found);
//					return;
//				}
//				String prodID = message;
//				if (TextUtils.isEmpty(prodID)) {
//					T.showToast(context,R.string.alert_no_barcode_found);
//				}else if(prodID.length()>20){
//					T.showToast(context,"编码过长");
//				}
//				else{
//					onScanProductHandleMessage(prodID);
//				}
//			}
//		};
//	}
//	
//	/**
//	 * 扫描商品成功后的回调，需要@Override
//	 * @param prodID
//	 */
//	public void onScanProductHandleMessage(String prodID){
//		
//	}
//	
//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
//		if (event.getKeyCode() == KeyEvent.KEYCODE_BUTTON_A && event.getAction() == KeyEvent.ACTION_DOWN
//				&& event.getRepeatCount() == 0) {
//			doScan(resultHandler);
//			return true;
//		}
//		return super.dispatchKeyEvent(event);
//	}
//	
//	// ////////////////////////////////////////////////////////////////////////////
//	// ScannerInterface扫描相关
//	// ////////////////////////////////////////////////////////////////////////////
//	private ScannerInterface scanner;
////	private IntentFilter intentFilter;
////	private BroadcastReceiver scanReceiver;
////	private boolean isContinue = false; // 连续扫描的标志
////	private static final String RES_ACTION = "android.intent.action.SCANRESULT";
//
//	private void initScannerInterface() {
//		if (scanner == null) {
//			Log.i("tag", "BaseDialog initScanner");
//			scanner = new ScannerInterface(context);
////			scanner = ScannerInterface.getIncetance(context);
//			scanner.open(); // 打开扫描头上电 scanner.close();//打开扫描头下电
//			scanner.enablePlayBeep(true);// 是否允许蜂鸣反馈
//			scanner.enableFailurePlayBeep(false);// 扫描失败蜂鸣反馈
//			scanner.enablePlayVibrate(true);// 是否允许震动反馈
//			scanner.enableAddKeyValue(0);/** 附加无、回车、Table、换行 */
//			scanner.timeOutSet(2);// 设置扫描延时2秒
//			scanner.intervalSet(1000); // 设置连续扫描间隔时间
//			scanner.lightSet(false);// 关闭右上角扫描指示灯
//			scanner.enablePower(true);// 省电模式
//			// scanner.addPrefix("AAA");//添加前缀
//			// scanner.addSuffix("BBB");//添加后缀
//			// scanner.interceptTrimleft(2); //截取条码左边字符
//			// scanner.interceptTrimright(3);//截取条码右边字符
//			// scanner.filterCharacter("R");//过滤特定字符
//			scanner.SetErrorBroadCast(true);// 扫描错误换行
//			// scanner.resultScan();//恢复iScan默认设置
//
//			// scanner.lockScanKey();
//			// 锁定设备的扫描按键,通过iScan定义扫描键扫描，用户也可以自定义按键。
//			scanner.unlockScanKey();
//			// 释放扫描按键的锁定，释放后iScan无法控制扫描按键，用户可自定义按键扫描。
//
//			/**
//			 * 设置扫描结果的输出模式，参数为0和1： 0为模拟输出（在光标停留的地方输出扫描结果）；
//			 * 1为广播输出（由应用程序编写广播接收者来获得扫描结果，并在指定的控件上显示扫描结果）
//			 * 这里采用接收扫描结果广播并在TextView中显示
//			 */
//			scanner.setOutputMode(0);
//
////			// 扫描结果的意图过滤器的动作一定要使用"android.intent.action.SCANRESULT"
////			intentFilter = new IntentFilter(RES_ACTION);
////			// 注册广播接受者
////			scanReceiver = new ScannerResultReceiver();
////			registerReceiver(scanReceiver, intentFilter);
//
//			scanner.scan_start();
//		} else {
//			Log.i("tag", "BaseDialog scan_start");
//			scanner.scan_start();
//		}
//	}
//
//	/**
//	 * 结束扫描
//	 */
//	private void finishScannerInterface() {
//		if (scanner != null) {
//			Log.i("tag", "BaseDialog finishScanner");
//			scanner.continceScan(false);
//			scanner.scan_stop();
//			scanner.close(); // 关闭iscan 非正常关闭会造成iScan异常退出
//			scanner=null;
////			unregisterReceiver(scanReceiver); // 反注册广播接收者
//		}
//	}
//}

import com.app.xstore.BaseActivity;
import com.app.xstore.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Dialog基类，所有Dialog都继承此类，提供一些基本的方法
 * 
 * @author Ni Guijun
 *
 */
public class BaseDialog extends Dialog{
	
	public BaseActivity context;//所有Dialog都基于BaseActivity
	
	public BaseDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context=(BaseActivity)context;
		init();
	}
	
	public BaseDialog(Context context, int themeResId) {
		super(context, themeResId);
		// TODO Auto-generated constructor stub
		this.context=(BaseActivity)context;
		init();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	private void init() {
		this.setCanceledOnTouchOutside(true);
		this.setCancelable(true);
	}
	
	//让Dialog出现在底部
	public void setToBottom(){
		// 不能写在init()中
		Window window = getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		lp.width = dm.widthPixels;
		lp.gravity = Gravity.BOTTOM;
		window.setAttributes(lp);
	}
	
	public void doShake(View view){
		context.doAnimation(context, view, R.anim.shake);
	}
	
	public void showToast(int stringId){
		context.showToast(stringId);
	}
	
	public void showToast(String text){
		context.showToast(text);
	}
	
}

