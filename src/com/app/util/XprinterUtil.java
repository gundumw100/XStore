package com.app.util;

import java.io.UnsupportedEncodingException;

import android.content.Context;

/**
 * wifi打印机
 * @author Ni Guijun
 *
 */
public class XprinterUtil {

	private Context context;
	private Socketmanager mSockManager;
	
	public XprinterUtil(Context context){
		this.context=context;
		mSockManager = new Socketmanager(context);
	}
	
	
//	public void connect(String printerIp) {
//		mSockManager.mPort = 9100;
//		mSockManager.mstrIp = printerIp;
//		mSockManager.threadconnect();
//		try {
//			Thread.sleep(100);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		if (mSockManager.getIstate()) {
//			return true;
//		} else {
//			return false;
//		}
//	}
	
	public boolean print(String printerIp,int port,String text){
		mSockManager.mstrIp = printerIp;
		mSockManager.mPort = port;
		try {
			byte[] data=text.getBytes("GBK");
			mSockManager.connectAndWrite(data);
			Thread.sleep(100);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
		return mSockManager.getState();
	}
	
	//切纸
	public void cutPaper(){
		byte SendCut[] = { 0x0a, 0x0a, 0x1d, 0x56, 0x01 };
		mSockManager.connectAndWrite(SendCut);
//		if (mSockManager.getState()) {
//			PrintfLog("切纸成功...");
//		} else {
//			PrintfLog("切纸失败...");
//		}
	}
	
	//打开钱箱
	public void openCash(){
		byte SendCash[] = { 0x1b, 0x70, 0x00, 0x1e, (byte) 0xff, 0x00 };
		mSockManager.connectAndWrite(SendCash);
//		if (mSockManager.getState()) {
//			PrintfLog("打开成功...");
//		} else {
//			PrintfLog("打开失败...");
//		}
	}
	
	public boolean getState(){
		return mSockManager.getState();
	}
}
