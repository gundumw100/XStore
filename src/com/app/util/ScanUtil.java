package com.app.util;

import com.app.xstore.App;

import android.content.Context;
import android.os.Handler;

/**
 * 扫描管理类,管理不同机型的扫描ThimfoneScanUtil & G0550ScanUtil
 * 
 * @author pythoner
 *
 */
public final class ScanUtil {

	private Context context;
	private boolean isDefaultPhoneType = true;

	private G0550ScanUtil g0550ScanUtil;
	// private ThimfoneScanUtil thimfoneScanUtil;

	public ScanUtil(Context context, boolean isDefaultPhoneType) {
		this.context = context;
		this.isDefaultPhoneType = isDefaultPhoneType;
		initScan();
	}

	private void initScan() {
		if (isDefaultPhoneType) {
			// thimfoneScanUtil=new ThimfoneScanUtil(context);
		} else {
			g0550ScanUtil = new G0550ScanUtil((App) context);
		}
	}

	public void doScan(Handler handler) {
		if (isDefaultPhoneType) {
			// thimfoneScanUtil.doScan(handler);
		} else {
			g0550ScanUtil.doScan(handler);
		}
	}

	public void doCloseScanner() {
		if (isDefaultPhoneType) {
			// thimfoneScanUtil.doCloseScanner();
		} else {
			g0550ScanUtil.doCloseScanner();
		}
	}

	public void reopenScanner() {
		if (isDefaultPhoneType) {
			// thimfoneScanUtil.reopenScanner();
		} else {
			g0550ScanUtil.reopenScanner();//
		}
	}

	public Handler getHandler() {
		if (isDefaultPhoneType) {
			return null;
			// return thimfoneScanUtil.getHandler();
		} else {
			return g0550ScanUtil.getHandler();
		}
	}
}
