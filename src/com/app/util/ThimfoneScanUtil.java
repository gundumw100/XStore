package com.app.util;

import com.base.util.T;
import com.zltd.decoder.Constants;
import com.zltd.decoder.DecoderManager;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

/**
 * Thimfone 机型扫描工具类
 * 
 * @author pyhoner
 *
 */
public final class ThimfoneScanUtil {

	private Context context;
	private DecoderManager.IDecoderStatusListener listener;
	protected DecoderManager mDecoderMgr = null;
	// 灯光控制的一个Handler
//	protected static LightControlHandler lightControlHandler = null;
	protected static int RETURN_CAMERA_CONN_ERR = 0x103;
	private static final int OPENLIGHT = 1;
	private static final int CLOSELIGHT = 2;

	private long lastPressTime;
	private final int SCAN_INTERVAL = 500;

	private SoundUtils soundUtils = null;

	public ThimfoneScanUtil(Context context, DecoderManager.IDecoderStatusListener listener) {
		this.context = context;
		this.listener = listener;
	}

	public void onCreate() {
//		if (lightControlHandler == null) {
//			lightControlHandler = new LightControlHandler();
//		}

		soundUtils = SoundUtils.getInstance();
		soundUtils.init(context);

		mDecoderMgr = DecoderManager.getInstance();
		if (mDecoderMgr != null) {
			// 设置为单扫模式
			int scanMode = 0;
			mDecoderMgr.setScanMode(scanMode);//如果非支持扫描的手机，此处会报错
		}
	}

	public void onResume() {
		if (mDecoderMgr != null) {
			// 2.连接扫描服务
			int res = mDecoderMgr.connectToDecoder();
			if (res == RETURN_CAMERA_CONN_ERR) {
//				Log.i("tag", "==========The Scan start error.===========");
				T.showToast(context, "The Scan start error.");
				return;
			}
			// 3.为Decodermanager添加一个扫描回调接口,用来接收扫描返回数据
			mDecoderMgr.addDecoderStatusListener(listener);
		}
	}

	/**
	 * type - 1:flash light 2:flood light 3: location light enable - true:enable
	 * false:disable
	 */
	public void enableLight(boolean enable) {
		if (mDecoderMgr != null) {
			mDecoderMgr.enableLight(3, enable);
		}
	}

	public void onPause() {
		if (soundUtils != null) {
			soundUtils.release();
		}
		if (mDecoderMgr != null) {
			mDecoderMgr.removeDecoderStatusListener(listener);
			mDecoderMgr.cancelDecode();
			mDecoderMgr.disconnectFromDecoder();
		}
	}

	public void onDestroy() {
		mDecoderMgr = null;
	}

	public void singleShoot() {
		if (System.currentTimeMillis() - lastPressTime <= SCAN_INTERVAL) {
			return;
		}
		lastPressTime = System.currentTimeMillis();
		if (mDecoderMgr != null) {
			mDecoderMgr.singleShoot();
		}
	}

	public void play(boolean isSuccess) {
		if (soundUtils != null) {
			if (isSuccess) {
				soundUtils.success();
			} else {
				soundUtils.warn();
			}
		}
	}

	// how to control location light and flash light
	class LightControlHandler extends Handler {
		public void handleMessage(android.os.Message msg) {
			// Log.i("tag", "lightControlHandler msg.what = " + msg.what + "
			// mDecoderMgr = " + mDecoderMgr);
			if (mDecoderMgr != null) {
				Log.i("tag", "FlashMode = " + mDecoderMgr.getFlashMode());
			}
			// 这边是为了在非 自动补光的情境下,可以动过手动的开关灯光进行补光
			switch (msg.what) {
			case OPENLIGHT:
				if (mDecoderMgr != null) {
					mDecoderMgr.enableLight(Constants.LOCATION_LIGHT, true);
					if (mDecoderMgr.getFlashMode() == Constants.FLASH_ALWAYS_ON_MODE) {
						mDecoderMgr.enableLight(Constants.FLASH_LIGHT, true);
						mDecoderMgr.enableLight(Constants.FLOOD_LIGHT, true);
					}
				}
				break;
			case CLOSELIGHT:
				if (mDecoderMgr != null) {
					mDecoderMgr.enableLight(Constants.LOCATION_LIGHT, false);
					if (mDecoderMgr.getFlashMode() == Constants.FLASH_ALWAYS_ON_MODE) {
						mDecoderMgr.enableLight(Constants.FLASH_LIGHT, false);
						mDecoderMgr.enableLight(Constants.FLOOD_LIGHT, false);
					}
				}
				break;

			default:
				break;
			}
		};
	}

}
