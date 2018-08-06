package com.app.util;

import amobile.android.barcodesdk.api.IWrapperCallBack;
import amobile.android.barcodesdk.api.Wrapper;
import amobile.android.barcodesdk.api.Wrapper.LightMode2D;
import android.os.Handler;

/**
 * G0550 机型扫描工具类
 * 
 * @author pyhoner
 *
 */
public final class G0550ScanUtil {
	private IWrapperCallBack callBack;
	private Wrapper wrapper = null;
	private Handler handler = null;

	public G0550ScanUtil(IWrapperCallBack callBack) {
		this.callBack = callBack;
		initScanner();
	}

	private void initScanner() {
		// 扫描功能准备
		Wrapper wrapper = new Wrapper(callBack);
		// Implicit intents with startService are not safe: Intent {
		// act=amobile.android.barcodesdk.services.BarcodeService }
		// android.content.ContextWrapper.bindService:517
		// amobile.android.barcodesdk.api.Wrapper.Open:78
		// com.mb.goods.util.ScanUtil.initScanner:51
		if (wrapper.Open()) {
			wrapper.SetDispathBarCode(false);
			wrapper.SetTimeOut(5);
			wrapper.SetLightMode2D(LightMode2D.mix);
		} else {
			wrapper = null;
		}
		setWrapper(wrapper);
	}

	/**
	 * 关闭扫描功能
	 */
	public void doCloseScanner() {
		if (isOpen()) {
			wrapper.Stop();
			wrapper.Close();
			wrapper = null;
		}
	}

	/**
	 * 开始扫描
	 */
	public void doScan(Handler handler) {
		if (wrapper == null) {
			initScanner();
		}
		this.handler = handler;
		wrapper.Stop();
		wrapper.Scan();
	}

	public boolean isOpen() {
		if (wrapper == null) {
			return false;
		}
		return wrapper.IsOpen();
	}

	// public void doStopScanner() {
	// if (isOpen()) {
	// wrapper.Stop();
	// }
	// }
	public void reopenScanner() {
		if (isOpen()) {
			return;
		}
		initScanner();
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public Wrapper getWrapper() {
		return wrapper;
	}

	public void setWrapper(Wrapper wrapper) {
		this.wrapper = wrapper;
	}

}