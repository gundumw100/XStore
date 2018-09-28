package com.app.util;

import java.io.OutputStream;
import java.net.Socket;

import android.os.Looper;
import android.util.Log;

/**
 * T90打印机工具类
 * @author Jet
 *
 */
public class PrintUtilsT90 {
	public static byte[] bytes = new byte[800];

	// 0:设置字符打印样式
	public void SetFontStyle(OutputStream out, boolean bFold,
			boolean bDoubleWidth, boolean bDoubleHeight, boolean bUnderLine) {
		try {
			bytes[0] = 0x1D;
			bytes[1] = 0x21;
			bytes[2] = 0x00;
			if (bDoubleHeight)
				bytes[2] |= 0x01;
			else
				bytes[2] &= (~0x01);
			if (bDoubleWidth)
				bytes[2] |= 0x10;
			else
				bytes[2] &= (~0x10);
			out.write(bytes, 0, 3);

			bytes[0] = 0x1B;
			bytes[1] = 0x2D;
			bytes[2] = 0x00;
			if (bDoubleHeight)
				bytes[2] = 0x01;
			else
				bytes[2] = (0x00);
			out.write(bytes, 0, 3);

			bytes[0] = 0x1B;
			bytes[1] = 0x45;
			bytes[2] = 0x00;
			if (bFold)
				bytes[2] = 0x01;
			else
				bytes[2] = (0x00);
			out.write(bytes, 0, 3);
			out.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 设置打印字符样式(宽度倍数/高度倍数)
	 * @param out
	 * @param width
	 * @param height
	 */
	public void SetFontStyle(OutputStream out, int width, int height){
		SetFontStyle(out, width, height,false);
	}
	/**
	 * 设置打印字符样式(宽度倍数/高度倍数/是否加粗)
	 * @author Tan Chenglong
	 * @param out
	 * @param height
	 * @param width
	 * @param bold
	 * 
	 *  经试验,在width,height<=2情况下,加粗有效果,>=3情况下,肉眼看不出差距,当然也可能是方法本身导致.
	 */
	public void SetFontStyle(OutputStream out, int height, int width, boolean bold) {
		height = height>8?8:(height<1?1:height);
		width = width>8?8:(width<1?1:width);
		try {
			//1:设置字符宽度及高度倍数
			bytes[0] = 0x1D;
			bytes[1] = 0x21;
			bytes[2] = 0x00;
			//1.1:高度
			switch (height) {
			case 1:
				bytes[2] |= 0x00;
				break;
			case 2:
				bytes[2] |= 0x01;
				break;
			case 3:
				bytes[2] |= 0x02;
				break;
			case 4:
				bytes[2] |= 0x03;
				break;
			case 5:
				bytes[2] |= 0x04;
				break;
			case 6:
				bytes[2] |= 0x05;
				break;
			case 7:
				bytes[2] |= 0x06;
				break;
			case 8:
				bytes[2] |= 0x07;
				break;
			default:
				break;
			}
			//1.2:宽度
			switch (width) {
			case 1:
				bytes[2] |= 0x00;
				break;
			case 2:
				bytes[2] |= 0x10;
				break;
			case 3:
				bytes[2] |= 0x20;
				break;
			case 4:
				bytes[2] |= 0x30;
				break;
			case 5:
				bytes[2] |= 0x40;
				break;
			case 6:
				bytes[2] |= 0x50;
				break;
			case 7:
				bytes[2] |= 0x60;
				break;
			case 8:
				bytes[2] |= 0x70;
				break;
			default:
				break;
			}
			out.write(bytes, 0, 3);
			//2:设置是否加粗
			bytes[0] = 0x1B;
			bytes[1] = 0x45;
			bytes[2] = 0x00;
			if (bold){
				bytes[2] = 0x01;
			}else{
				bytes[2] = (0x00);
			}
			out.write(bytes, 0, 3);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置条形码样式(宽度倍数 高度点数)
	 * @author Tan Chenglong
	 * @param out
	 * @param width	:2<=width<=6	默认:3
	 * @param height:1<=n<= 255		默认:162
	 */
	public void SetBarCodeStyle(OutputStream out, Integer width, Integer height){
		width = width==null?3:width;
		height = height==null?162:height;
		width = width<2?2:(width>6?6:width);
		height = height<1?1:(height>255?255:height);
		try {
			//1:设置条形码水平单条宽度
			bytes[0] = 0x1D; 
			bytes[1] = 0x77;
			switch (width) {
			case 2:
				bytes[2] = 0x02;
				break;
			case 3:
				bytes[2] = 0x03;
				break;
			case 4:
				bytes[2] = 0x04;
				break;
			case 5:
				bytes[2] = 0x05;
				break;
			case 6:
				bytes[2] = 0x06;
				break;
			default:
				bytes[2] = 0x03;
				break;
			}
			out.write(bytes, 0, 3);
			//2:设置条形码垂直方向的点数
			bytes[0] = 0x1D;
			bytes[1] = 0x68;
			bytes[2] = (byte) Integer.parseInt(Integer.toHexString(height), 16);
			Log.e("TCL", "bytes="+bytes[0]+":"+bytes[1]+":"+bytes[2]);
			out.write(bytes, 0, 3);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 1:打印字符串
	public void PrintString(OutputStream out, String strContent) {
		byte[] temps = new byte[2000];
		try {
			temps = strContent.getBytes("GB2312");
			out.write(temps, 0, temps.length);
			bytes[0] = 0x0a;
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 2:打印条形码
	public void PrintBarCode(OutputStream out, String strContent) {
		PrintString(out, "\n");
		byte[] temps = new byte[2000];
		try {
			temps = strContent.getBytes("GB2312");
			bytes[0] = 0x1B;
			bytes[1] = 0x4A;
			bytes[2] = 0x01;
			// out.write(bytes,0,3);
			bytes[0] = 0x1D;
			bytes[1] = 0x6B;
			bytes[2] = 0x49;
			bytes[3] = (byte) (2 + (byte) strContent.length());
			bytes[4] = 0x7B;
			bytes[5] = 0x42;
			out.write(bytes, 0, 6);
			out.write(temps, 0, strContent.length());
			bytes[0] = 0x0a;
			out.write(bytes, 0, 1);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 3:打印二维码
	public void PrintQR(OutputStream out, String strContent) {
		PrintString(out, "\n");
		byte[] temps = new byte[2000];
		int len = 3 + strContent.length();
		try {
			// / QR 二维码
			bytes[0] = 0x1D;
			bytes[1] = 0x28;
			bytes[2] = 0x6B;
			bytes[3] = 0x04;
			bytes[4] = 0x00;
			bytes[5] = 0x31;
			bytes[6] = 0x41;
			bytes[7] = 0x31;
			bytes[8] = 0x00;
			out.write(bytes, 0, 9);

			bytes[0] = 0x1D;
			bytes[1] = 0x28;
			bytes[2] = 0x6B;
			bytes[3] = 0x03;
			bytes[4] = 0x00;
			bytes[5] = 0x31;
			bytes[6] = 0x43;
			bytes[7] = 0x06;
			out.write(bytes, 0, 8);

			bytes[0] = 0x1D;
			bytes[1] = 0x28;
			bytes[2] = 0x6B;
			bytes[3] = 0x03;
			bytes[4] = 0x00;
			bytes[5] = 0x31;
			bytes[6] = 0x45;
			bytes[7] = 0x30;
			out.write(bytes, 0, 8);

			bytes[0] = 0x1D;
			bytes[1] = 0x28;
			bytes[2] = 0x6B;
			bytes[3] = (byte) (len % 256);
			bytes[4] = (byte) (len / 256);
			bytes[5] = 0x31;
			bytes[6] = 0x50;
			bytes[7] = 0x30;
			out.write(bytes, 0, 8);

			temps = strContent.getBytes("GB2312");
			out.write(temps, 0, len - 3);

			bytes[0] = 0x1D;
			bytes[1] = 0x28;
			bytes[2] = 0x6B;
			bytes[3] = 0x03;
			bytes[4] = 0x00;
			bytes[5] = 0x31;
			bytes[6] = 0x51;
			bytes[7] = 0x30;
			out.write(bytes, 0, 8);

			bytes[0] = 0x1D;
			bytes[1] = 0x28;
			bytes[2] = 0x6B;
			bytes[3] = 0x03;
			bytes[4] = 0x00;
			bytes[5] = 0x31;
			bytes[6] = 0x52;
			bytes[7] = 0x30;
			out.write(bytes, 0, 8);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 打印二维码改进版,设置纠错等级
	 * @author Tan Chenglong
	 * @param out
	 * @param strContent
	 */
	public void PrintQRAd(OutputStream out, String strContent, int level) {
		level = level<=1?1:(level>=4?4:level);
		PrintString(out, "\n");
		byte[] temps = new byte[2000];
		int len = 3 + strContent.length();
		try {
			// / QR 二维码
			bytes[0] = 0x1D;
			bytes[1] = 0x28;
			bytes[2] = 0x6B;
			bytes[3] = 0x04;
			bytes[4] = 0x00;
			bytes[5] = 0x31;
			bytes[6] = 0x41;
			bytes[7] = 0x31;
			bytes[8] = 0x00;
			out.write(bytes, 0, 9);

			bytes[0] = 0x1D;
			bytes[1] = 0x28;
			bytes[2] = 0x6B;
			bytes[3] = 0x03;
			bytes[4] = 0x00;
			bytes[5] = 0x31;
			bytes[6] = 0x43;
			bytes[7] = 0x06;
			out.write(bytes, 0, 8);

			//设置纠错等级
			bytes[0] = 0x1D;
			bytes[1] = 0x28;
			bytes[2] = 0x6B;
			bytes[3] = 0x03;
			bytes[4] = 0x00;
			bytes[5] = 0x31;
			bytes[6] = 0x45;
			switch (level) {
			case 1:
				bytes[7] = 0x30;
				break;
			case 2:
				bytes[7] = 0x31;
				break;
			case 3:
				bytes[7] = 0x32;
				break;
			case 4:
				bytes[7] = 0x33;
				break;
			default:
				bytes[7] = 0x30;
				break;
			}
			out.write(bytes, 0, 8);

			bytes[0] = 0x1D;
			bytes[1] = 0x28;
			bytes[2] = 0x6B;
			bytes[3] = (byte) (len % 256);
			bytes[4] = (byte) (len / 256);
			bytes[5] = 0x31;
			bytes[6] = 0x50;
			bytes[7] = 0x30;
			out.write(bytes, 0, 8);

			temps = strContent.getBytes("GB2312");
			out.write(temps, 0, len - 3);

			bytes[0] = 0x1D;
			bytes[1] = 0x28;
			bytes[2] = 0x6B;
			bytes[3] = 0x03;
			bytes[4] = 0x00;
			bytes[5] = 0x31;
			bytes[6] = 0x51;
			bytes[7] = 0x30;
			out.write(bytes, 0, 8);

			bytes[0] = 0x1D;
			bytes[1] = 0x28;
			bytes[2] = 0x6B;
			bytes[3] = 0x03;
			bytes[4] = 0x00;
			bytes[5] = 0x31;
			bytes[6] = 0x52;
			bytes[7] = 0x30;
			out.write(bytes, 0, 8);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 4:切纸
	public void CutPaper(OutputStream out) {
		try {
			bytes[0] = 0x0a;
			bytes[1] = 0x1D;
			bytes[2] = 0x56;
			bytes[3] = 0x42;
			bytes[4] = 0x00;
			out.write(bytes, 0, 5);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void print(final String ip, final int port){
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Socket sock = new Socket(ip, port); // ip and port of printer
					OutputStream out = sock.getOutputStream();
					//居中命令
					bytes[0] = 0x00;
					bytes[1] = 0x1B;
					bytes[2] = 0x61;
					bytes[3] = 0x01;
					out.write(bytes, 0, 4);
					Looper.prepare();
					
					//打印内容自行设定
					/*
					//测试打印:字符串,条形码,二维码
					PrintString(out, "http://sh.meituan.com/dianying/?mtt=1.movie%2Fmovies.0.0.igx3qydh");
					PrintBarCode(out, "A01339S468A01339U0520");
					PrintBarCode(out, "0123456789");
					PrintQR(out, "http://sh.meituan.com/dianying/?mtt=1.movie%2Fmovies.0.0.igx3qydh");
					//测试打印:字符串,条形码,二维码
					*/
					
					CutPaper(out);
					out.close();
					sock.close();
					Looper.loop();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}
	
	/*
	class Mythread extends Thread {
		private String ip;
		private int port;
		public Mythread(String ip, int port) {
			super();
			this.ip = ip;
			this.port = port;
		}
		@Override
		public void run() {
			super.run();
			try {
				Socket sock = new Socket(ip, port); // ip and port of printer
				OutputStream out = sock.getOutputStream();
				//居中命令
				bytes[0] = 0x00;
				bytes[1] = 0x1B;
				bytes[2] = 0x61;
				bytes[3] = 0x01;
				out.write(bytes, 0, 4);
				Looper.prepare();
				//测试打印:字符串,条形码,二维码
				PrintString(out, "http://sh.meituan.com/dianying/?mtt=1.movie%2Fmovies.0.0.igx3qydh");
				PrintBarCode(out, "A01339S468A01339U0520");
				PrintBarCode(out, "0123456789");
				PrintQR(out, "http://sh.meituan.com/dianying/?mtt=1.movie%2Fmovies.0.0.igx3qydh");
				CutPaper(out);
				//测试打印:字符串,条形码,二维码
				out.close();
				sock.close();
				Looper.loop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	*/
}