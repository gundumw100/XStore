package com.app.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import android.content.Context;

public class Socketmanager {
	public static final boolean MESSAGE_CONNECTED = true;
	public static final boolean MESSAGE_CONNECTED_ERROR = false;
	public static final boolean MESSAGE_WRITE_SUCCESS = true;
	public static final boolean MESSAGE_WRITE_ERROR = false;
	private Socket mMyWifiSocket = null;
	private BufferedReader BufReader = null;
	private OutputStream PriOut = null;
	private boolean iState = false;

	public String mstrIp = "192.168.1.248";
	public int mPort = 9100;

	int TimeOut = 2000;

	public Socketmanager(Context context) {
	}

//	public void threadconnect() {
//		new ConnectThread();
//		if (connect()) {
//			SetState(MESSAGE_CONNECTED);
//		}
//		close();
//	}

//	public void threadconnectwrite(byte[] str) {
////		new WriteThread(str);
//		if (ConnectAndWrite(str)) {
//			SetState(MESSAGE_WRITE_SUCCESS);
//		}
//	}

	private boolean connect() {
		close();
		try {
			mMyWifiSocket = new Socket();
			mMyWifiSocket
					.connect(new InetSocketAddress(mstrIp, mPort), TimeOut);
			PriOut = mMyWifiSocket.getOutputStream();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			setState(MESSAGE_CONNECTED_ERROR);
			return false;
		}
	}

	private boolean write(byte[] out) {
		if (PriOut != null) {
			try {
				PriOut.write(out);
				PriOut.flush();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}

	private void close() {
		if (mMyWifiSocket != null) {
			try {
				mMyWifiSocket.close();
				mMyWifiSocket = null;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if (BufReader != null) {
			try {
				BufReader.close();
				BufReader = null;
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
		if (PriOut != null) {
			try {
				PriOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			PriOut = null;
		}
	}

	public boolean connectAndWrite(byte[] out) {
		if (connect()) {
			write(out);
			close();
			setState(MESSAGE_WRITE_SUCCESS);
			return true;
		} else {
			setState(MESSAGE_CONNECTED_ERROR);
			return false;
		}
	}

	public void setState(Boolean state) {
		iState = state;
	}

	public boolean getState() {
		return iState;
	}
	
//	private class ConnectThread extends Thread {
//		public ConnectThread() {
//			start();
//		}
//
//		public void run() {
//			if (connect()) {
//				SetState(MESSAGE_CONNECTED);
//			}
//			close();
//		}
//	}

//	private class WriteThread extends Thread {
//		byte[] out;
//
//		public WriteThread(byte[] str) {
//			out = str;
//			start();
//		}
//
//		public void run() {
//			if (ConnectAndWrite(out)) {
//				SetState(MESSAGE_WRITE_SUCCESS);
//			}
//		}
//	}
}
