package com.app.util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.app.model.JvbillsaleInfo;
import com.app.model.JvbillsaledetailInfo;
import com.app.model.JvbillsalepayInfo;
import com.app.model.Printer;
import com.app.model.response.GetBillSaleByNumResponse;
import com.app.xstore.App;
import com.printer.ZQPrinter;
import com.printer.sdk.PrinterConstants.Command;
import com.printer.sdk.PrinterInstance;

/**
 * 具体打印小票
 * @author Ni Guijun
 *
 */
public class PrintUtil {
	private final String FOTMAT="yyyy-MM-dd HH:mm:ss";
	private Context context;
	public PrintUtil(Context context){
		this.context=context;
	}
	
	/**
	 * 打印小票
	 * @param item
	 * @param detailResponse
	 * @param curFittingRoomInfo
	 * @return
	 */
	public boolean doPrintTicket(Printer bean,GetBillSaleByNumResponse response){
		if(response==null){
			Toast.makeText(context, "打印对象不存在", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (TextUtils.isEmpty(bean.getPrinterip())) {
			Toast.makeText(context, "请设置IP地址及端口号", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(bean.getType()==0){
			return doPrintTicketByDefault(bean,response);
		}else if(bean.getType()==1){
			return doPrintTicketByT90(bean, response);
		}else if(bean.getType()==2){
			return doPrintTicketBySPRT(bean, response);
		}else if(bean.getType()==3){
			return doPrintTicketByXprinter(bean, response);
		}
		else{
			Toast.makeText(context, "未知型号", Toast.LENGTH_SHORT).show();
			return false;
		}
	}
	
	/**
	 * 创建打印机连接
	 * @param item
	 * @return
	 */
	private ZQPrinter createPrinter(Printer bean){
		ZQPrinter printer = new ZQPrinter();
		int nRet = printer.Connect(bean.getPrinterip());
		if (nRet != 0) {//101
			//java.net.ConnectException: failed to connect to /10.8.39.170 (port 9100): connect failed: ETIMEDOUT (Connection timed out)
			Toast.makeText(context, "打印机连接失败，请检查", Toast.LENGTH_LONG).show();
			return null;
		}
		System.setProperty("file.encoding", "gb2312");
		return printer;
	}
	
	private static byte[] bytes = new byte[800];
	// 4:切纸
	private void cutPaper(OutputStream out) {
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
	
	public static String printTestStr = "打印成功";
	public static String printTestSuccessStr = "打印成功";
	public String doPrintTest(Printer bean){
		if (TextUtils.isEmpty(bean.getPrinterip())) {
			return "请设置IP地址及端口号";
		}
		if(bean.getType()==0){
			return doPrintTestByDefault(bean);
		}
		else if(bean.getType()==1){
			return doPrintTestByT90(bean);
		}else if(bean.getType()==2){
			return doPrintTestBySPRT(bean);
		}else if(bean.getType()==3){
			return doPrintTestByXprinter(bean);
		}
		else{
			return printTestSuccessStr;
		}
	}
	private String doPrintTestByDefault(Printer bean){
		ZQPrinter printer = new ZQPrinter();
		int nRet = printer.Connect(bean.getPrinterip());
		if (nRet != 0) {
			bean = null;
		}
		if(bean!=null){
			System.setProperty("file.encoding", "gb2312");
			printer.PrintEscText(printTestStr);
			printer.PrintEscText("ESC|fP");//feed 6 line and paper cut.
			printer.Disconnect();
			printer = null;
			return printTestSuccessStr;
		}else{
			return "打印机连接失败，请检查";
		}
	}
	private String doPrintTestByT90(Printer bean){
		String[] s = bean.getPrinterip().split(":");
		int PORT = Integer.parseInt(s[1]);
		
		PrintUtilsT90 t90=new PrintUtilsT90();
		try {
			Socket sock = new Socket(s[0],PORT);
			OutputStream out = sock.getOutputStream();
			//左对齐命令
			bytes[0] = 0x00;
			bytes[1] = 0x1B;
			bytes[2] = 0x61;
			bytes[3] = 0x00;
			out.write(bytes, 0, 4);
			t90.SetFontStyle(out, 1, 1);
			t90.PrintString(out,printTestStr);
			cutPaper(out);
			out.close();
			sock.close();
			return printTestSuccessStr;
		}catch(Exception e){
			return e.getLocalizedMessage();
		}
	}
	private String doPrintTestBySPRT(Printer bean){
		String[] s = bean.getPrinterip().split(":");
		int PORT = Integer.parseInt(s[1]);
		PrinterInstance mPrinter = (new PrintUtilsSPRT()).getPrinterInstance(s[0],PORT);
		boolean connected = mPrinter.openConnection();
		if(!connected){
			return "打印机连接失败，请检查";
		}else{
			try {
				mPrinter.initPrinter();
				mPrinter.setFont(0, 0, 0, 0, 0);
				mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
				mPrinter.printText(printTestStr);
				mPrinter.cutPaper(65, 3);
				mPrinter.closeConnection();
				return printTestSuccessStr;
			}catch (Exception e) {
				mPrinter.closeConnection();
				return e.getLocalizedMessage();
			}
		}
	}
	private String doPrintTestByXprinter(Printer bean){
		String[] s = bean.getPrinterip().split(":");
		int PORT = Integer.parseInt(s[1]);
		XprinterUtil xprinterUtil=new XprinterUtil(context);
		boolean isSuccessful=xprinterUtil.print(s[0],PORT,printTestSuccessStr);
		xprinterUtil.cutPaper();
//			xprinterUtil.openCash();
		if(isSuccessful){
			return printTestSuccessStr;
		}else{
			return "打印机连接失败，请检查";
		}
	}
	/**
	 * 打印标签
	 * @param item
	 * @param tags	标签内容集合
	 * @param type	标签打印类型 0:字符;	1:条码;	2:二维码;
	 * @return
	 */
//	public boolean doPrintTags(Printer item,List<String> tags,int type){
//		if (TextUtils.isEmpty(item.getPrinterIp())) {
//			Toast.makeText(context, "请设置IP地址及端口号", Toast.LENGTH_SHORT).show();
//			return false;
//		}
//		//默认打印机型号
//		if(item.getType()==0){
//			ZQPrinter printer=createPrinter(item);
//			try {
//				switch (type) {
//				//字符
//				case 0:
//					for(String content:tags){
//						//根据字符数量调整字体大小,可能需要微调
//						if(content.length()<=2){
//							printer.PrintText("\r\n" + content + "\r\n", ZQPrinter.ALIGNMENT_CENTER,ZQPrinter.FT_DEFAULT, ZQPrinter.TS_7HEIGHT | ZQPrinter.TS_7WIDTH);
//						}else if(content.length()<=4){
//							printer.PrintText("\r\n" + content + "\r\n", ZQPrinter.ALIGNMENT_CENTER,ZQPrinter.FT_DEFAULT, ZQPrinter.TS_4HEIGHT | ZQPrinter.TS_4WIDTH);
//						}else if(content.length()<=8){
//							printer.PrintText("\r\n" + content + "\r\n", ZQPrinter.ALIGNMENT_CENTER,ZQPrinter.FT_DEFAULT, ZQPrinter.TS_2HEIGHT | ZQPrinter.TS_2WIDTH);
//						}else if(content.length()<=12){
//							printer.PrintText("\r\n" + content + "\r\n", ZQPrinter.ALIGNMENT_CENTER,ZQPrinter.FT_DEFAULT, ZQPrinter.TS_1HEIGHT | ZQPrinter.TS_1WIDTH);
//						}else{
//							printer.PrintText("\r\n" + content + "\r\n", ZQPrinter.ALIGNMENT_CENTER,ZQPrinter.FT_DEFAULT, ZQPrinter.TS_0HEIGHT | ZQPrinter.TS_0WIDTH);
//						}
//						printer.PrintEscText("ESC|fP");//feed 6 line and paper cut.
//					}
//					break;
//				//条码
//				case 1:
//						for(String content:tags){
//							//PrintBarcode(byte[] Data:数据总集,int DataSize 数据总集中的数量,int Symbology 类型,int Height 每一条线的高度, int  Width 每一条线的宽度,int Alignment,int TextPosition 文字相对于条形码的位置)
//							byte[] midbytes = content.getBytes("gb2312");
//							if(content.length()<=8){
//								printer.PrintText(content + "\r\n\r\n", ZQPrinter.ALIGNMENT_CENTER,ZQPrinter.FT_DEFAULT, ZQPrinter.TS_2HEIGHT | ZQPrinter.TS_2WIDTH);
//							}else{
//								printer.PrintText(content + "\r\n\r\n", ZQPrinter.ALIGNMENT_CENTER,ZQPrinter.FT_DEFAULT, ZQPrinter.TS_1HEIGHT | ZQPrinter.TS_1WIDTH);
//							}
//							if(content.length()<=9){
//								printer.PrintBarcode(midbytes, midbytes.length, ZQPrinter.BCS_Code39, 120, 4, ZQPrinter.ALIGNMENT_LEFT, ZQPrinter.BC_TEXT_BELOW);
//							}
//							//12
//							else if(content.length()<=13){
//								printer.PrintBarcode(midbytes, midbytes.length, ZQPrinter.BCS_Code39, 120, 3, ZQPrinter.ALIGNMENT_LEFT, ZQPrinter.BC_TEXT_BELOW);
//							}else{
//								printer.PrintBarcode(midbytes, midbytes.length, ZQPrinter.BCS_Code39, 120, 2, ZQPrinter.ALIGNMENT_LEFT, ZQPrinter.BC_TEXT_BELOW);
//							}
//							printer.PrintEscText("ESC|fP");//feed 6 line and paper cut.
//						}
//						break;
//				//二维码
//				case 2:
//						for(String content:tags){
//							//原始:为什么不用原始方法:发现有些情况下打印二维码,会一直不打印一直走纸
////							printer.PrintQRCode(ZQPrinter.QRCODE_ALPH, content);
////							printer.LineFeed(2);
////							printer.PrintEscText("ESC|fP");//feed 6 line and paper cut.
//							
//							//改为打印由ZXing转换成个Bitmap
//							/*
//							MODE_S8  	0  		8-dot single-density
//							MODE_D8  	1  			8-dot double-density
//							MODE_S24  	32  		24-dot single-density
//							MODE_D24  	33  		24-dot double-density
//							*/
//							Bitmap contentBitmap = EncodingHandler.createQRCode(content, 400);
//							printer.PrintBitmap1D76(contentBitmap, 0);//8-dot single-density
//							printer.LineFeed(2);
//							printer.PrintEscText("ESC|fP");//feed 6 line and paper cut.
//						}
//						break;
//				default:
//					break;
//				}
//			} catch (Exception e) {
//				return false;
//			}
//			
//			printer.Disconnect();
//			printer = null;
//			return true;
//		}
//		//T90打印机
//		else if(item.getType()==1){
//			PrintUtilsT90 t90=new PrintUtilsT90();
//			String internetIp = item.getPrinterIp();
//			String PORT = ":9100";
//			try {
//				Socket sock = new Socket(internetIp,9100);
//				OutputStream out = sock.getOutputStream();
//				//居中命令
//				bytes[0] = 0x00;
//				bytes[1] = 0x1B;
//				bytes[2] = 0x61;
//				bytes[3] = 0x01;
//				out.write(bytes, 0, 4);
//				
//				switch (type) {
//				//字符
//				case 0:
//					for(String content:tags){
//						if(content.length()<=3){
//							t90.SetFontStyle(out, 8, 8);
//						}else if(content.length()<=4){
//							t90.SetFontStyle(out, 6, 6);
//						}else if(content.length()<=6){
//							t90.SetFontStyle(out, 4, 4);
//						}else if(content.length()<=8){
//							t90.SetFontStyle(out, 3, 3);
//						}else if(content.length()<=12){
//							t90.SetFontStyle(out, 2, 2);
//						}else{
//							t90.SetFontStyle(out, 1, 1);
//						}
//						t90.PrintString(out, "\r\n\r\n\r\n\r\n" + content + "\r\n\r\n\r\n\r\n");
//						cutPaper(out);
//					}
//					break;
//				//条码
//				case 1:
//					for(String content:tags){
//						if(content.length()<=3){
//							t90.SetFontStyle(out, 8, 8);
//						}else if(content.length()<=4){
//							t90.SetFontStyle(out, 6, 6);
//						}else if(content.length()<=6){
//							t90.SetFontStyle(out, 4, 4);
//						}else if(content.length()<=8){
//							t90.SetFontStyle(out, 3, 3);
//						}else if(content.length()<=12){
//							t90.SetFontStyle(out, 2, 2);
//						}else{
//							t90.SetFontStyle(out, 1, 1);
//						}
//						t90.PrintString(out, "\r\n\r\n\r\n\r\n" + content + "\r\n\r\n");
//						//打印条码
//						if(content.length()<=5){
//							t90.SetBarCodeStyle(out, 6, 120);
//						}else if(content.length()<=7){
//							t90.SetBarCodeStyle(out, 5, 120);
//						}else if(content.length()<=9){
//							t90.SetBarCodeStyle(out, 4, 120);
//						}else if(content.length()<=14){
//							t90.SetBarCodeStyle(out, 3, 120);
//						}else{
//							t90.SetBarCodeStyle(out, 2, 120);
//						}
//						t90.PrintBarCode(out, content);
//						t90.PrintString(out, "\r\n\r\n");
//						cutPaper(out);
//					}
//					break;
//				//二维码
//				case 2:
//					for(String content:tags){
//						t90.PrintString(out, "\r\n\r\n");
//						//打印二维码
//						t90.PrintQRAd(out, content,4);
//						t90.PrintString(out, "\r\n\r\n");
//						cutPaper(out);
//					}
//					break;
//				default:
//					break;
//				}
//				out.close();
//				sock.close();
//				return true;
//			} catch (Exception e) {
//				Toast.makeText(context, "打印失败，请重新打印。。。", Toast.LENGTH_LONG).show();
//				return false;
//			}
//		}
//		//SPRT打印机
//		else if(item.getType()==2){
//			PrinterInstance mPrinter = (new PrintUtilsSPRT()).getPrinterInstance(item.getPrinterIp());
//			boolean connected = mPrinter.openConnection();
//			if(!connected){
//				return false;
//			}else{
//				try {
//					mPrinter.initPrinter();
//					mPrinter.setFont(0, 0, 0, 0, 0);
//					mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);
//					//
//					switch (type) {
//					case 0://字符
//						for(String content:tags){
//							if(content.length()<=3){
//								mPrinter.setFont(0, 4, 4, 0, 0);
//							}else if(content.length()<=4){
//								mPrinter.setFont(0, 3, 3, 0, 0);
//							}else if(content.length()<=6){
//								mPrinter.setFont(0, 2, 2, 0, 0);
//							}else if(content.length()<=8){
//								mPrinter.setFont(0, 2, 2, 0, 0);
//							}else if(content.length()<=12){
//								mPrinter.setFont(0, 1, 1, 0, 0);
//							}
//							mPrinter.printText(content);
//							mPrinter.printText("\n\n\n");
//							mPrinter.cutPaper(65, 3);
//						}
//						break;
//					case 1://条码
//						for(String content:tags){
//							Barcode barcode = new Barcode(BarcodeType.CODE128, 2, 150, 2,content);
//							mPrinter.printBarCode(barcode);
//							mPrinter.printText("\n\n\n");
//							mPrinter.cutPaper(65, 3);
//						}
//						break;
//					case 2://二维码
//						for(String content:tags){
//							Barcode barcode = new Barcode(BarcodeType.QRCODE, 2, 3, 6,content);
//							mPrinter.printBarCode(barcode);
//							mPrinter.printText("\n\n\n");
//							mPrinter.cutPaper(65, 3);
//						}
//						break;
//					default:
//						break;
//					}
//					//
//					mPrinter.closeConnection();
//					return true;
//				} catch (Exception e) {
//					mPrinter.closeConnection();
//					return false;
//				}
//			}
//		}
//		return false;
//	}
	/**
	 * 打印小票
	 * @param printerInfo
	 * @param billSaleWrap
	 * @param billSaleHeadInfo
	 * @param type
	 * @return
	 */
	private boolean doPrintTicketByDefault(Printer bean,GetBillSaleByNumResponse response) {
		JvbillsaleInfo billsale = response.getBillSale();
		ZQPrinter printer=createPrinter(bean);
		if(printer!=null){
			printer.PrintEscText("ESC|cA" + "ESC|1C" + "销售小票\r\n");
			
			printer.PrintText("-----------------------------------------\n", ZQPrinter.ALIGNMENT_LEFT, ZQPrinter.FT_DEFAULT,0);
			printer.PrintText("店名："+App.user.getShopInfo().getShop_name()+ "\n", ZQPrinter.ALIGNMENT_LEFT,ZQPrinter.FT_DEFAULT, 0);
			SimpleDateFormat df = new SimpleDateFormat(FOTMAT,Locale.CHINA);//设置日期格式
			printer.PrintText("打印时间：" + df.format(new Date())+ "\n", ZQPrinter.ALIGNMENT_LEFT, ZQPrinter.FT_DEFAULT,0);
			printer.PrintText("-----------------------------------------\n", ZQPrinter.ALIGNMENT_LEFT, ZQPrinter.FT_DEFAULT,0);
			printer.PrintText("小 票 号："+billsale.getSaleNo()+ "\n", ZQPrinter.ALIGNMENT_LEFT,ZQPrinter.FT_DEFAULT, 0);
			printer.PrintText("交易时间："+billsale.getCreatetimeStr()+ "\n", ZQPrinter.ALIGNMENT_LEFT,ZQPrinter.FT_DEFAULT, 0);
			printer.PrintText("收 银 员："+App.user.getUserInfo().getUser_code()+"\n", ZQPrinter.ALIGNMENT_LEFT,ZQPrinter.FT_DEFAULT, 0);
			printer.PrintText("交易类型："+getSaleTypeStr(billsale.getSaleType())+ "\n", ZQPrinter.ALIGNMENT_LEFT,ZQPrinter.FT_DEFAULT, 0);
			List<JvbillsalepayInfo> payList=response.getPayList();
			String payCode="";
			if(payList!=null){
				for(JvbillsalepayInfo item:payList){
					payCode+=item.getPayCode();
				}
			}
			printer.PrintText("支付方式："+payCode+ "\n", ZQPrinter.ALIGNMENT_LEFT,ZQPrinter.FT_DEFAULT, 0);
			printer.PrintText("交易金额：￥"+billsale.getTotalMoney()+ "\n", ZQPrinter.ALIGNMENT_LEFT,ZQPrinter.FT_DEFAULT, 0);
			printer.PrintText("交易数量："+billsale.getTotalQty()+ "\n", ZQPrinter.ALIGNMENT_LEFT,ZQPrinter.FT_DEFAULT, 0);
			printer.PrintText("-----------------------------------------\n", ZQPrinter.ALIGNMENT_LEFT, ZQPrinter.FT_DEFAULT,0);
			if(billsale.getVipId()==null||billsale.getVipId().length()==0){
			}else{
				printer.PrintText("会 员 ID："+billsale.getVipId()+"\n", ZQPrinter.ALIGNMENT_LEFT,ZQPrinter.FT_DEFAULT, 0);
				printer.PrintText("会员卡号："+"\n", ZQPrinter.ALIGNMENT_LEFT,ZQPrinter.FT_DEFAULT, 0);
				printer.PrintText("消费积分："+formatNumber(billsale.getVipConsumeValue(), "###0.#")+"\n", ZQPrinter.ALIGNMENT_LEFT,ZQPrinter.FT_DEFAULT, 0);
				printer.PrintText("-----------------------------------------\n", ZQPrinter.ALIGNMENT_LEFT, ZQPrinter.FT_DEFAULT,0);
			}
			//商品信息
			printer.PrintText("商品\t   数量\t单价\t折率\t金额\n", ZQPrinter.ALIGNMENT_LEFT,ZQPrinter.FT_DEFAULT, 0);
			List<JvbillsaledetailInfo> list = response.getDetailList();
			if (list != null) {
				for (JvbillsaledetailInfo item : list) {
					printer.PrintText(item.getProdName()+"\n", ZQPrinter.ALIGNMENT_LEFT, ZQPrinter.FT_DEFAULT,0);
					String divSaleRate = "无";
					if (item.getDivSaleRate() >= 100) {
//						divSaleRate = "";
					} else {
						divSaleRate = StringUtil.formatNumber(item.getDivSaleRate() *10, "###0.##")+ "折";
					}
					String gap="	";
					printer.PrintText(item.getProdNum() + "  " + item.getQty() + "件"
							+ gap+"￥" + StringUtil.formatMoney(item.getRetailPrice())
							+ gap + divSaleRate + gap+"￥"
							+ StringUtil.formatMoney(item.getSalePrice()) + "\n", ZQPrinter.ALIGNMENT_LEFT, ZQPrinter.FT_DEFAULT,0);
				}
			}
			
			printer.LineFeed(2);
//			printer.CutPaper();
			printer.PrintEscText("ESC|fP");//feed 6 line and paper cut.
			printer.Disconnect();
			printer = null;
			return true;
		}
		return false;
	}
	
	private boolean doPrintTicketBySPRT(Printer bean,GetBillSaleByNumResponse response){
		JvbillsaleInfo billsale = response.getBillSale();
		String[] s = bean.getPrinterip().split(":");
		int PORT = Integer.parseInt(s[1]);
		
		PrinterInstance mPrinter = (new PrintUtilsSPRT()).getPrinterInstance(s[0],PORT);
		boolean connected = mPrinter.openConnection();
		if(!connected){
			return false;
		}else{
			try {
				mPrinter.initPrinter();
				mPrinter.setFont(0, 0, 0, 0, 0);
				mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);
				mPrinter.printText("销售小票\r\n");
				
				mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
				mPrinter.printText("-----------------------------------------\n");
				mPrinter.printText("店名："+App.user.getShopInfo().getShop_name()+ "\n");
				SimpleDateFormat df = new SimpleDateFormat(FOTMAT,Locale.CHINA);//设置日期格式
				mPrinter.printText("打印时间：" + df.format(new Date())+ "\n");
				mPrinter.printText("-----------------------------------------\n");
				mPrinter.printText("小 票 号："+billsale.getSaleNo()+ "\n");
				mPrinter.printText("交易时间："+billsale.getCreatetimeStr()+ "\n");
				mPrinter.printText("收 银 员："+App.user.getUserInfo().getUser_code()+"\n");
				mPrinter.printText("交易类型："+getSaleTypeStr(billsale.getSaleType())+ "\n");
				List<JvbillsalepayInfo> payList=response.getPayList();
				String payCode="";
				if(payList!=null){
					for(JvbillsalepayInfo item:payList){
						payCode+=item.getPayCode();
					}
				}
				mPrinter.printText("支付方式："+payCode+ "\n");
				mPrinter.printText("交易金额：￥"+billsale.getTotalMoney()+ "\n");
				mPrinter.printText("交易数量："+billsale.getTotalQty()+ "\n");
				mPrinter.printText("-----------------------------------------\n");
				if(billsale.getVipId()==null||billsale.getVipId().length()==0){
				}else{
					mPrinter.printText("会 员 ID："+billsale.getVipId()+"\n");
					mPrinter.printText("会员卡号："+"\n");
					mPrinter.printText("消费积分："+formatNumber(billsale.getVipConsumeValue(), "###0.#")+"\n");
					mPrinter.printText("-----------------------------------------\n");
				}
				
				//商品信息
				mPrinter.printText("商品\t   数量\t单价\t折率\t金额\n");
				List<JvbillsaledetailInfo> list = response.getDetailList();
				if (list != null) {
					for (JvbillsaledetailInfo item : list) {
						mPrinter.printText(item.getProdName()+"\n");
						String divSaleRate = "无";
						if (item.getDivSaleRate() >= 100) {
//							divSaleRate = "";
						} else {
							divSaleRate = StringUtil.formatNumber(item.getDivSaleRate() *10, "###0.##")+ "折";
						}
						String gap="	";
						mPrinter.printText(item.getProdNum() + "  " + item.getQty() + "件"
								+ gap+"￥" + StringUtil.formatMoney(item.getRetailPrice())
								+ gap + divSaleRate + gap+"￥"
								+ StringUtil.formatMoney(item.getSalePrice()) + "\n");
					}
				}
				
				mPrinter.printText("\n\n\n");
				mPrinter.cutPaper(65, 3);
				mPrinter.closeConnection();
				return true;
			} catch (Exception e) {
				mPrinter.closeConnection();
				return false;
			}
		}
	}
	
	private boolean doPrintTicketByT90(Printer printer,GetBillSaleByNumResponse response) {
		JvbillsaleInfo billsale = response.getBillSale();
		
		String[] s = printer.getPrinterip().split(":");
		int PORT = Integer.parseInt(s[1]);
		
		PrintUtilsT90 t90=new PrintUtilsT90();
		try {
			Socket sock = new Socket(s[0],PORT);
			OutputStream out = sock.getOutputStream();
			//居中命令
			bytes[0] = 0x00;
			bytes[1] = 0x1B;
			bytes[2] = 0x61;
			bytes[3] = 0x01;
			out.write(bytes, 0, 4);
			t90.SetFontStyle(out, 1, 1);
			t90.PrintString(out, "销售小票\r\n");
			
			//左对齐命令
			bytes[0] = 0x00;
			bytes[1] = 0x1B;
			bytes[2] = 0x61;
			bytes[3] = 0x00;
			out.write(bytes, 0, 4);
			
			t90.PrintString(out,"-----------------------------------------\n");
			t90.PrintString(out,"店名："+App.user.getShopInfo().getShop_name()+ "\n");
			SimpleDateFormat df = new SimpleDateFormat(FOTMAT,Locale.CHINA);//设置日期格式
			t90.PrintString(out,"打印时间：" + df.format(new Date())+ "\n");
			t90.PrintString(out,"-----------------------------------------\n");
			t90.PrintString(out,"小 票 号："+billsale.getSaleNo()+ "\n");
			t90.PrintString(out,"交易时间："+billsale.getCreatetimeStr()+ "\n");
			t90.PrintString(out,"收 银 员："+App.user.getUserInfo().getUser_code()+ "\n");
			t90.PrintString(out,"交易类型："+getSaleTypeStr(billsale.getSaleType())+ "\n");
			List<JvbillsalepayInfo> payList=response.getPayList();
			String payCode="";
			if(payList!=null){
				for(JvbillsalepayInfo item:payList){
					payCode+=item.getPayCode();
				}
			}
			t90.PrintString(out,"支付方式："+payCode+ "\n");
			t90.PrintString(out,"交易金额：￥"+billsale.getTotalMoney()+ "\n");
			t90.PrintString(out,"交易数量："+billsale.getTotalQty()+ "\n");
			t90.PrintString(out,"-----------------------------------------\n");
			if(billsale.getVipId()==null||billsale.getVipId().length()==0){
			}else{
				t90.PrintString(out,"会 员 ID："+billsale.getVipId()+"\n");
				t90.PrintString(out,"会员卡号："+"\n");
				t90.PrintString(out,"消费积分："+formatNumber(billsale.getVipConsumeValue(), "###0.#")+"\n");
				t90.PrintString(out,"-----------------------------------------\n");
			}
			//商品信息
			t90.PrintString(out,"商品\t   数量\t单价\t折率\t金额\n");
			List<JvbillsaledetailInfo> list = response.getDetailList();
			if (list != null) {
				for (JvbillsaledetailInfo item : list) {
					t90.PrintString(out,item.getProdName()+"\n");
					String divSaleRate = "无";
					if (item.getDivSaleRate() >= 100) {
//						divSaleRate = "";
					} else {
						divSaleRate = StringUtil.formatNumber(item.getDivSaleRate() *10, "###0.##")+ "折";
					}
					String gap="	";
					t90.PrintString(out,item.getProdNum() + "  " + item.getQty() + "件"
							+ gap+"￥" + StringUtil.formatMoney(item.getRetailPrice())
							+ gap + divSaleRate + gap+"￥"
							+ StringUtil.formatMoney(item.getSalePrice()) + "\n");
				}
			}
			//居中命令
			bytes[0] = 0x00;
			bytes[1] = 0x1B;
			bytes[2] = 0x61;
			bytes[3] = 0x01;
			out.write(bytes, 0, 4);
			
			cutPaper(out);
			out.close();
			sock.close();
			return true;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // ip and port of printer
		
		return false;
	}
	
	private boolean doPrintTicketByXprinter(Printer bean,GetBillSaleByNumResponse response) {
		JvbillsaleInfo billsale = response.getBillSale();
		String[] s = bean.getPrinterip().split(":");
		int PORT = Integer.parseInt(s[1]);
		
		StringBuffer sb=new StringBuffer();
		XprinterUtil xprinterUtil=new XprinterUtil(context);
		
		String tabs="\t\t";
		sb.append(tabs+"销售小票\r\n");
		sb.append("-----------------------------------------\n");
		sb.append("店名："+App.user.getShopInfo().getShop_name()+ "\n");
		SimpleDateFormat df = new SimpleDateFormat(FOTMAT,Locale.CHINA);//设置日期格式
		sb.append("打印时间：" + df.format(new Date())+ "\n");
		sb.append("-----------------------------------------\n");
		sb.append("小 票 号："+billsale.getSaleNo()+ "\n");
		sb.append("交易时间："+billsale.getCreatetimeStr()+ "\n");
		sb.append("收 银 员："+App.user.getUserInfo().getUser_code()+ "\n");
		sb.append("交易类型："+getSaleTypeStr(billsale.getSaleType())+ "\n");
		
		List<JvbillsalepayInfo> payList=response.getPayList();
		String payCode="";
		if(payList!=null){
			for(JvbillsalepayInfo item:payList){
				payCode+=item.getPayCode();
			}
		}
		sb.append("支付方式："+payCode+ "\n");
		
		sb.append("交易金额：￥"+billsale.getTotalMoney()+ "\n");
		sb.append("交易数量："+billsale.getTotalQty()+ "\n");
		sb.append("-----------------------------------------\n");
		if(billsale.getVipId()==null||billsale.getVipId().length()==0){
		}else{
			sb.append("会 员 ID："+billsale.getVipId()+"\n");
			sb.append("会员卡号："+"\n");
			sb.append("消费积分："+formatNumber(billsale.getVipConsumeValue(), "###0.#")+"\n");
			sb.append("-----------------------------------------\n");
		}
		
		sb.append("商品\t   数量\t单价\t折率\t金额\n");
		List<JvbillsaledetailInfo> list = response.getDetailList();
		if (list != null) {
			for (JvbillsaledetailInfo item : list) {
				sb.append(item.getProdName()+"\n");
				String divSaleRate = "无";
				if (item.getDivSaleRate() >= 100) {
//					divSaleRate = "";
				} else {
					divSaleRate = StringUtil.formatNumber(item.getDivSaleRate() *10, "###0.##")+ "折";
				}
				String gap="	";
				sb.append(item.getProdNum() + "  " + item.getQty() + "件"
						+ gap+"￥" + StringUtil.formatMoney(item.getRetailPrice())
						+ gap + divSaleRate + gap+"￥"
						+ StringUtil.formatMoney(item.getSalePrice()) + "\n");
				
			}
		}
		
		sb.append("\n");
		sb.append("\n");
		sb.append("\n");
		
		boolean isSuccessful=xprinterUtil.print(s[0],PORT,sb.toString());
		xprinterUtil.cutPaper();
//			xprinterUtil.openCash();
		return isSuccessful;
	}
	
	/**
	 * 格式化Money，结构为0.00，传入的是Number
	 * @param money
	 * @return
	 */
	private String formatMoney(Number money){
    	return formatNumber(money,"###0.00");
    }

	/**
	 * 格式化数字，格式通过formatString自定义
	 * @param money
	 * @return
	 */
	private String formatNumber(Number number,String formatString){
    	if(number==null){
    		return null;
    	}
    	if(TextUtils.isEmpty(formatString)){
    		return String.valueOf(number);
    	}
    	DecimalFormat format = new DecimalFormat(formatString);
    	return format.format(number);
    }
	///////////////////////////////////////////////////////////////////////////////////////////////////
	//
	///////////////////////////////////////////////////////////////////////////////////////////////////
//	public boolean doPrintTicket(Printer item,QuanLiuTongFaHuoDan bean){
//		if (TextUtils.isEmpty(item.getPrinterIp())) {
//			Toast.makeText(context, "请设置IP地址及端口号", Toast.LENGTH_SHORT).show();
//			return false;
//		}
//		if(item.getType()==0){
//			return doPrintTicketByDefault(item,bean);
//		}else if(item.getType()==1){
//			return doPrintTicketByT90(item, bean);
//		}else if(item.getType()==2){
//			return doPrintTicketBySPRT(item, bean);
//		}else{
//			return false;
//		}
//	}
	
	/**
	 * 打印小票
	 * @param printerInfo
	 * @param billSaleWrap
	 * @param billSaleHeadInfo
	 * @param type
	 * @return
	 */
//	private boolean doPrintTicketByDefault(Printer item,QuanLiuTongFaHuoDan bean) {
//		ZQPrinter printer=createPrinter(item);
//		if(printer!=null){
//			printer.PrintEscText("ESC|cA" + "ESC|1C" + "MetersBonwe\r\n");
//			printer.PrintEscText("ESC|cA" + "ESC|1C" + "美特斯.邦威\r\n");
//			printer.PrintEscText("ESC|cA" + "ESC|1C" + "发货清单\r\n");
//			QuanLiuTongFaHuoDanHead head=bean.getHead();
//			printer.PrintText("-----------------------------------------\n", ZQPrinter.ALIGNMENT_LEFT, ZQPrinter.FT_DEFAULT,0);
//			printer.PrintText("OS订单号："+head.getOrderSn()+ "\n", ZQPrinter.ALIGNMENT_LEFT,ZQPrinter.FT_DEFAULT, 0);
//			printer.PrintText("收货人："+head.getConsigee()+ "\n", ZQPrinter.ALIGNMENT_LEFT,ZQPrinter.FT_DEFAULT, 0);
//			printer.PrintText("收货地址："+head.getAddress()+ "\n", ZQPrinter.ALIGNMENT_LEFT,ZQPrinter.FT_DEFAULT, 0);
//			printer.PrintText("收货电话："+head.getMobile()+ "\n", ZQPrinter.ALIGNMENT_LEFT,ZQPrinter.FT_DEFAULT, 0);
//			printer.PrintText("-----------------------------------------\n", ZQPrinter.ALIGNMENT_LEFT, ZQPrinter.FT_DEFAULT,0);
//			printer.PrintText("销售店铺："+head.getChannelCode()+ "\n", ZQPrinter.ALIGNMENT_LEFT,ZQPrinter.FT_DEFAULT, 0);
//			printer.PrintText("名称："+head.getChannelName()+ "\n", ZQPrinter.ALIGNMENT_LEFT,ZQPrinter.FT_DEFAULT, 0);
//			printer.PrintText("地址："+ "\n", ZQPrinter.ALIGNMENT_LEFT,ZQPrinter.FT_DEFAULT, 0);
//			printer.PrintText("电话："+ getText(head.getFromUserTel())+"\n", ZQPrinter.ALIGNMENT_LEFT,ZQPrinter.FT_DEFAULT, 0);
//			String createDate=head.getOrderDate();//debug
//			printer.PrintText("小票号："+ getText(head.getSaleNo())+"\t"+createDate+"\n", ZQPrinter.ALIGNMENT_LEFT,ZQPrinter.FT_DEFAULT, 0);
//			printer.PrintText("-----------------------------------------\n", ZQPrinter.ALIGNMENT_LEFT, ZQPrinter.FT_DEFAULT,0);
//			printer.PrintText("发货店铺："+head.getOwnerDeliveWarehCode()+ "\n", ZQPrinter.ALIGNMENT_LEFT,ZQPrinter.FT_DEFAULT, 0);
//			printer.PrintText("名称："+head.getOwnerDeliveWarehName()+ "\n", ZQPrinter.ALIGNMENT_LEFT,ZQPrinter.FT_DEFAULT, 0);
//			printer.PrintText("地址："+ getText(App.user.getShop().getShopAddress())+"\n", ZQPrinter.ALIGNMENT_LEFT,ZQPrinter.FT_DEFAULT, 0);
//			printer.PrintText("电话："+ getText(App.user.getShop().getShopPhone())+"\n", ZQPrinter.ALIGNMENT_LEFT,ZQPrinter.FT_DEFAULT, 0);
////			String sendDate=getDate(head.getSENDDATE());
//			String time=StringUtil.formatNow("yyyy/MM/dd HH:mm:ss");
//			printer.PrintText("发货单号："+ head.getDelivOrderCode()+"\t"+time+"\n", ZQPrinter.ALIGNMENT_LEFT,ZQPrinter.FT_DEFAULT, 0);
//			printer.PrintText("-----------------------------------------\n", ZQPrinter.ALIGNMENT_LEFT, ZQPrinter.FT_DEFAULT,0);
//			
//			//商品信息
//			printer.PrintText("商品\t\t数量\t商品名称\n", ZQPrinter.ALIGNMENT_LEFT,ZQPrinter.FT_DEFAULT, 0);
//			List<QuanLiuTongFaHuoDanBody> details=bean.getDetails();
//			for (QuanLiuTongFaHuoDanBody body : details) {
//				printer.PrintText(body.getProdSkuNum()+ "\t"+body.getOrderQty()+"\t"+body.getProdName() + "\n", ZQPrinter.ALIGNMENT_LEFT, ZQPrinter.FT_DEFAULT,0);
//				printer.PrintText(body.getProdColorName() + "\t\t"+body.getProdSpecName()+ "\n",ZQPrinter.ALIGNMENT_LEFT, ZQPrinter.FT_DEFAULT, 0);
//			}
//			printer.PrintText("-----------------------------------------\n", ZQPrinter.ALIGNMENT_LEFT, ZQPrinter.FT_DEFAULT,0);
//
////			//打印条形码
//			//byte[] midbytes = AppointmentNo.getBytes("gb2312");
//			//printer.PrintBarcode(midbytes, midbytes.length, ZQPrinter.BCS_Code39, 100, 4, ZQPrinter.ALIGNMENT_LEFT, ZQPrinter.BC_TEXT_BELOW);
//			printer.PrintText("衷心感谢您的光临！"+ "\n", ZQPrinter.ALIGNMENT_LEFT,ZQPrinter.FT_DEFAULT, ZQPrinter.TS_0HEIGHT);
//			printer.PrintText("www.metersbonwe.com"+ "\n", ZQPrinter.ALIGNMENT_LEFT,ZQPrinter.FT_DEFAULT, ZQPrinter.TS_0HEIGHT);
//			printer.PrintText("售后服务热线：400-820-0790"+ "\n", ZQPrinter.ALIGNMENT_LEFT,ZQPrinter.FT_DEFAULT, ZQPrinter.TS_0HEIGHT);
//			printer.PrintText("售后时间：每天9:00-24:00  全年无休", ZQPrinter.ALIGNMENT_LEFT,ZQPrinter.FT_DEFAULT, ZQPrinter.TS_0HEIGHT);
//			printer.LineFeed(2);
////			printer.CutPaper();
//			printer.PrintEscText("ESC|fP");//feed 6 line and paper cut.
//			printer.Disconnect();
//			printer = null;
//			return true;
//		}
//		return false;
//	}
//	
//	private boolean doPrintTicketBySPRT(Printer item,QuanLiuTongFaHuoDan bean) {
//		PrinterInstance mPrinter = (new PrintUtilsSPRT()).getPrinterInstance(item.getPrinterIp());
//		boolean connected = mPrinter.openConnection();
//		if(!connected){
//			return false;
//		}else{
//			try {
//				mPrinter.initPrinter();
//				mPrinter.setFont(0, 0, 0, 0, 0);
//				mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);
//				mPrinter.printText("MetersBonwe\r\n");
//				mPrinter.printText("美特斯.邦威\r\n");
//				mPrinter.printText("发货清单\r\n");
//				QuanLiuTongFaHuoDanHead head=bean.getHead();
//				mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
//				mPrinter.printText("-----------------------------------------\n");
//				mPrinter.printText("OS订单号："+head.getOrderSn()+ "\n");
//				mPrinter.printText("收货人："+head.getConsigee()+ "\n");
//				mPrinter.printText("收货地址："+head.getAddress()+ "\n");
//				mPrinter.printText("收货电话："+head.getMobile()+ "\n");
//				mPrinter.printText("-----------------------------------------\n");
//				mPrinter.printText("销售店铺："+head.getChannelCode()+ "\n");
//				mPrinter.printText("名称："+head.getChannelName()+ "\n");
//				mPrinter.printText("地址："+ "\n");
//				mPrinter.printText("电话："+ getText(head.getFromUserTel())+"\n");
//				String createDate=head.getOrderDate();//debug
//				mPrinter.printText("小票号："+ getText(head.getSaleNo())+"\t"+createDate+"\n");
//				mPrinter.printText("-----------------------------------------\n");
//				mPrinter.printText("发货店铺："+head.getOwnerDeliveWarehCode()+ "\n");
//				mPrinter.printText("名称："+head.getOwnerDeliveWarehName()+ "\n");
//				mPrinter.printText("地址："+ getText(App.user.getShop().getShopAddress())+"\n");
//				mPrinter.printText("电话："+ getText(App.user.getShop().getShopPhone())+"\n");
//	//			String sendDate=getDate(head.getSENDDATE());
//				String time=StringUtil.formatNow("yyyy/MM/dd HH:mm:ss");
//				mPrinter.printText("发货单号："+ head.getDelivOrderCode()+"\t"+time+"\n");
//				mPrinter.printText("-----------------------------------------\n");
//				//商品信息
//				mPrinter.printText("商品\t\t数量\t商品名称\n");
//				List<QuanLiuTongFaHuoDanBody> details=bean.getDetails();
//				for (QuanLiuTongFaHuoDanBody body : details) {
//					mPrinter.printText(body.getProdSkuNum()+ "\t"+body.getOrderQty()+"\t"+body.getProdName() + "\n");
//					mPrinter.printText(body.getProdColorName() + "\t\t"+body.getProdSpecName()+ "\n");
//				}
//				mPrinter.printText("-----------------------------------------\n");
//	//			//打印条形码
//				//byte[] midbytes = AppointmentNo.getBytes("gb2312");
//				//printer.PrintBarcode(midbytes, midbytes.length, ZQPrinter.BCS_Code39, 100, 4, ZQPrinter.ALIGNMENT_LEFT, ZQPrinter.BC_TEXT_BELOW);
//				mPrinter.printText("衷心感谢您的光临！"+ "\n");
//				mPrinter.printText("www.metersbonwe.com"+ "\n");
//				mPrinter.printText("售后服务热线：400-820-0790"+ "\n");
//				mPrinter.printText("售后时间：每天9:00-24:00  全年无休");
//				mPrinter.printText("\n\n\n");
//				mPrinter.cutPaper(65, 3);
//				mPrinter.closeConnection();
//				return true;
//			} catch (Exception e) {
//				mPrinter.closeConnection();
//				return false;
//			}
//		}
//	}
//
//	private boolean doPrintTicketByT90(Printer item,QuanLiuTongFaHuoDan bean) {
//		String internetIp = item.getPrinterIp();
//		int PORT = 9100;
//		PrintUtilsT90 t90=new PrintUtilsT90();
//		try {
//			Socket sock = new Socket(internetIp,PORT);
//			OutputStream out = sock.getOutputStream();
//			//居中命令
//			bytes[0] = 0x00;
//			bytes[1] = 0x1B;
//			bytes[2] = 0x61;
//			bytes[3] = 0x01;
//			out.write(bytes, 0, 4);
//			t90.SetFontStyle(out, 1, 1);
//			t90.PrintString(out, "MetersBonwe\r\n");
//			t90.PrintString(out, "美特斯.邦威\r\n");
//			t90.PrintString(out, "发货清单\r\n");
//			
//			//左对齐命令
//			bytes[0] = 0x00;
//			bytes[1] = 0x1B;
//			bytes[2] = 0x61;
//			bytes[3] = 0x00;
//			out.write(bytes, 0, 4);
//			
//			QuanLiuTongFaHuoDanHead head=bean.getHead();
//			t90.PrintString(out,"-----------------------------------------\n");
//			t90.PrintString(out,"OS订单号："+head.getOrderSn()+ "\n");
//			t90.PrintString(out,"收货人："+head.getConsigee()+ "\n");
//			t90.PrintString(out,"收货地址："+head.getAddress()+ "\n");
//			t90.PrintString(out,"收货电话："+head.getMobile()+ "\n");
//			t90.PrintString(out,"-----------------------------------------\n");
//			t90.PrintString(out,"销售店铺："+head.getChannelCode()+ "\n");
//			t90.PrintString(out,"名称："+head.getChannelName()+ "\n");
//			t90.PrintString(out,"地址："+ "\n");
//			t90.PrintString(out,"电话："+ getText(head.getFromUserTel())+"\n");
////			String createDate=getDate(head.getCREATE_DATE());
//			String createDate=head.getOrderDate();//debug
//			t90.PrintString(out,"小票号："+getText(head.getSaleNo())+"\t"+createDate+ "\n");
//			t90.PrintString(out,"-----------------------------------------\n");
//			t90.PrintString(out,"发货店铺："+head.getOwnerDeliveWarehCode()+ "\n");
//			t90.PrintString(out,"名称："+head.getOwnerDeliveWarehName()+ "\n");
//			t90.PrintString(out,"地址："+ getText(App.user.getShop().getShopAddress())+"\n");
//			t90.PrintString(out,"电话："+ getText(App.user.getShop().getShopPhone())+"\n");
////			String sendDate=getDate(head.getSENDDATE());
//			String time=StringUtil.formatNow("yyyy/MM/dd HH:mm:ss");
//			t90.PrintString(out,"发货单号："+head.getDelivOrderCode()+"\t"+time+ "\n");
//			t90.PrintString(out,"-----------------------------------------\n");
//			//商品信息
//			t90.PrintString(out,"商品\t\t数量\t商品名称\n");
//			List<QuanLiuTongFaHuoDanBody> details=bean.getDetails();
//			for (QuanLiuTongFaHuoDanBody body : details) {
//				t90.PrintString(out,body.getProdSkuNum()+ "\t"+body.getOrderQty()+"\t"+body.getProdName()+ "\n");
//				t90.PrintString(out,body.getProdColorName() + "\t\t"+body.getProdSpecName()+ "\n");
//			}
//			
//			t90.PrintString(out,"-----------------------------------------\n");
//			t90.PrintString(out,"衷心感谢您的光临！"+ "\n");
//			t90.PrintString(out,"www.metersbonwe.com"+ "\n");
//			t90.PrintString(out,"售后服务热线：400-820-0790"+ "\n");
//			t90.PrintString(out,"售后时间：每天9:00-24:00  全年无休"+"\n");
//			
//			//居中命令
//			bytes[0] = 0x00;
//			bytes[1] = 0x1B;
//			bytes[2] = 0x61;
//			bytes[3] = 0x01;
//			out.write(bytes, 0, 4);
//			
//			cutPaper(out);
//			out.close();
//			sock.close();
//			return true;
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} // ip and port of printer
//		
//		return false;
//	}
//	private String getText(String text){
//		if(text==null){
//			return "";
//		}
//		return text;
//	}
	
	//销售模式:XS(销售)、TH(退货)、HH(换货)
	private String getSaleTypeStr(String saleType){
		if("XS".equals(saleType)){
			return "销售";
		}else if("TH".equals(saleType)){
			return "退货";
		}else if("HH".equals(saleType)){
			return "换货";
		}
		return "未知";
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////
	//
	///////////////////////////////////////////////////////////////////////////////////////////////////
	
	
}
