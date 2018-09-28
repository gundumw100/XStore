package com.app.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.app.xstore.R;
import com.printer.sdk.PrinterConstants.Connect;
import com.printer.sdk.PrinterInstance;


/**
 * SPRT打印机(思普瑞特)工具类
 * @author Jet
 *
 */
public class PrintUtilsSPRT {
	private PrinterInstance myPrinter = null;
	private static boolean ISCONNECTED;
	private Context mContext;
	// 用于接受连接状态消息的 Handler
	private Handler mHandler = new Handler() {
		@SuppressLint("ShowToast")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Connect.SUCCESS:
				ISCONNECTED = true;
				if(mContext != null){
					Toast.makeText(mContext, "连接成功", Toast.LENGTH_SHORT).show();
				}
				break;
			case Connect.FAILED:
				ISCONNECTED = false;
				if(mContext != null){
					Toast.makeText(mContext, "连接失败", Toast.LENGTH_SHORT).show();
				}
				break;
			case Connect.CLOSED:
				ISCONNECTED = false;
				if(mContext != null){
					Toast.makeText(mContext, "连接关闭", Toast.LENGTH_SHORT).show();
				}
				break;
			case Connect.NODEVICE:
				ISCONNECTED = false;
				if(mContext != null){
					Toast.makeText(mContext, "没有可连接的设备", Toast.LENGTH_SHORT).show();
				}
				break;
			case 0:
				if(mContext != null){
					Toast.makeText(mContext, "打印机通信正常!", Toast.LENGTH_SHORT).show();
				}
				break;
			case -1:
				if(mContext != null){
					Toast.makeText(mContext, "打印机通信异常常，请检查蓝牙连接!", Toast.LENGTH_SHORT).show();
				}
				vibrator();
				break;
			case -2:
				if(mContext != null){
					Toast.makeText(mContext, "打印机缺纸!", Toast.LENGTH_SHORT).show();
				}
				vibrator();
				break;
			case -3:
				if(mContext != null){
					Toast.makeText(mContext, "打印机开盖!", Toast.LENGTH_SHORT).show();
				}
				vibrator();
				break;
			default:
				break;
			}
		}
	};
	public void vibrator() {
		if(mContext != null){
			MediaPlayer player = new MediaPlayer().create(mContext, R.raw.beep);
			player.start();
		}
	}
	
	/**
	 * 根据ip地址,端口号,Handler实例	获取PrinterInstance实例
	 * @param ipAddress
	 * @param portNumber
	 * @param handler
	 * @return
	 */
	public PrinterInstance getPrinterInstance(String ipAddress,int port){
		if(myPrinter != null){
			myPrinter.closeConnection();
			myPrinter = null;
		}
		myPrinter = PrinterInstance.getPrinterInstance(ipAddress, port, mHandler);
		return myPrinter;
	}
	
}