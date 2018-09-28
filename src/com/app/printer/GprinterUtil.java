package com.app.printer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import org.simple.eventbus.EventBus;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Base64;
import android.widget.Toast;

import com.app.model.JvbillsaleInfo;
import com.app.model.JvbillsaledetailInfo;
import com.app.model.response.GetBillSaleByNumResponse;
import com.app.util.StringUtil;
import com.app.xstore.App;
import com.gprinter.aidl.GpService;
import com.gprinter.command.EscCommand;
import com.gprinter.command.EscCommand.ENABLE;
import com.gprinter.command.EscCommand.FONT;
import com.gprinter.command.EscCommand.JUSTIFICATION;
import com.gprinter.command.GpCom;
import com.gprinter.command.GpUtils;
import com.gprinter.command.LabelCommand;
import com.gprinter.command.LabelCommand.BARCODETYPE;
import com.gprinter.command.LabelCommand.DIRECTION;
import com.gprinter.command.LabelCommand.FONTMUL;
import com.gprinter.command.LabelCommand.FONTTYPE;
import com.gprinter.command.LabelCommand.MIRROR;
import com.gprinter.command.LabelCommand.READABEL;
import com.gprinter.command.LabelCommand.ROTATION;
import com.gprinter.io.GpDevice;
import com.gprinter.service.GpPrintService;

public class GprinterUtil {

	private final int printerID = 1;
	private final int portType = 4;// 使用端口1，4代表模式为蓝牙模式，蓝牙地址，最后默认为0
	public static final String EVENT_CONNECTING_PRINTER = "connectingPriner";
	public static final String EVENT_CONNECT_PRINTER_RESULT = "connectPrinerResult";

	private Context context;
	private GpService mGpService = null;
	private PrinterServiceConnection conn;
	private static GprinterUtil gprinterUtil = null;

	public static GprinterUtil getInstance(Context context) {
		if (gprinterUtil == null) {
			gprinterUtil = new GprinterUtil(context);
		}
		return gprinterUtil;
	}

	private GprinterUtil(Context context) {
		this.context = context;
	}

	// 开启打印机服务监听
	public void bindPrinterService() {
		// 注册打印机广播
		registerPrinterStatusBroadcastReceiver();
		conn = new PrinterServiceConnection();
		Intent intent = new Intent(context, GpPrintService.class);
		context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
	}

	class PrinterServiceConnection implements ServiceConnection {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mGpService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mGpService = GpService.Stub.asInterface(service);
		}
	}

	public void registerPrinterStatusBroadcastReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(GpCom.ACTION_CONNECT_STATUS);
		context.registerReceiver(printerStatusBroadcastReceiver, filter);
	}

	// 开启打印端口
	public void connectPrinter(String diviceName) {
		closePort();
		int rel = 0;
		try {// 使用端口1，4代表模式为蓝牙模式，蓝牙地址，最后默认为0
			if (mGpService != null) {
				rel = mGpService.openPort(printerID, portType, diviceName, 0);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		// GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
		// if(r != GpCom.ERROR_CODE.SUCCESS) {
		// if(r == GpCom.ERROR_CODE.DEVICE_ALREADY_OPEN) {
		// Toast.makeText(context, "开启成功", Toast.LENGTH_SHORT).show();
		// }else{
		// Toast.makeText(context, GpCom.getErrorText(r),
		// Toast.LENGTH_SHORT).show();
		// }
		// }else{
		// Toast.makeText(context, "失败", Toast.LENGTH_SHORT).show();
		// }

	}

	public boolean isBluetoothAvailable() {
		// Get local Bluetooth adapter
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		// If the adapter is null, then Bluetooth is not supported
		if (bluetoothAdapter == null) {
			// showToast("设备不支持蓝牙");
			return false;
		} else {
			// If BT is not on, request that it be enabled.
			// setupChat() will then be called during onActivityResult
			if (!bluetoothAdapter.isEnabled()) {
				// showToast("蓝牙未开启");
				return false;
			}
		}
		return true;
	}

	public boolean isPrinterConnected() {
		try {
			if (mGpService != null) {
				int status = mGpService.getPrinterConnectStatus(printerID);
				if (status == GpDevice.STATE_CONNECTED) {// 3
					return true;
				}
			} else {
				Toast.makeText(context, "mGpService is null.",
						Toast.LENGTH_SHORT).show();
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public void closePort() {
		try {
			if (mGpService != null)
				mGpService.closePort(printerID);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private BroadcastReceiver printerStatusBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (GpCom.ACTION_CONNECT_STATUS.equals(intent.getAction())) {
				int type = intent.getIntExtra(GpPrintService.CONNECT_STATUS, 0);
				int id = intent.getIntExtra(GpPrintService.PRINTER_ID, 0);
				if (type == GpDevice.STATE_CONNECTING) {
					Toast.makeText(context, "连接中...", Toast.LENGTH_SHORT)
							.show();
					EventBus.getDefault().post(EVENT_CONNECTING_PRINTER);
				} else if (type == GpDevice.STATE_NONE) {
					Toast.makeText(context, "未找到打印机", Toast.LENGTH_SHORT)
							.show();
					EventBus.getDefault().post(EVENT_CONNECT_PRINTER_RESULT);
				} else if (type == GpDevice.STATE_VALID_PRINTER) {
					Toast.makeText(context, "连接成功", Toast.LENGTH_SHORT).show();
					EventBus.getDefault().post(EVENT_CONNECT_PRINTER_RESULT);
				} else if (type == GpDevice.STATE_INVALID_PRINTER) {
					Toast.makeText(context, "连接失败", Toast.LENGTH_SHORT).show();
					EventBus.getDefault().post(EVENT_CONNECT_PRINTER_RESULT);
				}
			}
		}
	};

	public void closePrinterService() {
		if (printerStatusBroadcastReceiver != null) {
			context.unregisterReceiver(printerStatusBroadcastReceiver);
		}
		if (conn != null) {
			context.unbindService(conn); // unBindService
		}
		// closePort();
	}

	// 获取打印机的命令类型，是ESC命令还是Label命令
	public int getPrinterCommandType() {
		try {
			return mGpService.getPrinterCommandType(printerID);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	public void sendTestLabel() {
		sendLabel("男休闲短袖", "1234567890", "白色", "XXL", "99");
	}

	/**
	 * 打印标签
	 * 
	 * @param name
	 * @param sku
	 * @param color
	 * @param size
	 * @param ls_price
	 */
	public void sendLabel(String name, String sku, String color, String size,
			String ls_price) {
		int x = 40;
		int y = 50;
		int paddingTop = 50;

		LabelCommand tsc = new LabelCommand();
		tsc.addSize(40, 80); // 设置标签尺寸，按照实际尺寸设置
		tsc.addGap(2); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
		tsc.addDirection(DIRECTION.BACKWARD, MIRROR.NORMAL);// 设置打印方向
		tsc.addReference(0, 0);// 设置原点坐标
		tsc.addTear(ENABLE.ON); // 撕纸模式开启
		tsc.addCls();// 清除打印缓冲区
		tsc.addText(x, paddingTop + 1 * y, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1, "品名：" + name);
		tsc.addText(x, paddingTop + 2 * y, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				"款号：" + sku.substring(0, 6));
		tsc.addText(x, paddingTop + 3 * y, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1, "颜色："
						+ color);
		tsc.addText(x, paddingTop + 4 * y, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1, "尺码：" + size);
		tsc.addText(x, paddingTop + 5 * y, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1, "零售价：￥"
						+ ls_price);
		// 绘制一维条码
		tsc.add1DBarcode(x, paddingTop + 7 * y, BARCODETYPE.CODE128, 100,
				READABEL.EANBEL, ROTATION.ROTATION_0, sku);

		tsc.addPrint(1, 1); // 打印标签
		tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
		tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
		sendLabelCommand(tsc);
	}

	private void sendLabelCommand(LabelCommand tsc) {
		if (mGpService == null) {
			return;
		}
		Vector<Byte> datas = tsc.getCommand(); // 发送数据
		byte[] bytes = GpUtils.ByteTo_byte(datas);
		String str = Base64.encodeToString(bytes, Base64.DEFAULT);
		int rel;
		try {
			rel = mGpService.sendLabelCommand(printerID, str);
			GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
			if (r != GpCom.ERROR_CODE.SUCCESS) {
				Toast.makeText(context, GpCom.getErrorText(r),
						Toast.LENGTH_SHORT).show();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 打印小票
	 * 
	 * @param billSale
	 * @param billsaleDetailList
	 * @param payList
	 */
	public void sendReceipt(GetBillSaleByNumResponse response) {
		JvbillsaleInfo billsale = response.getBillSale();

		String charset = "GB2312";

		EscCommand esc = new EscCommand();
		esc.addInitializePrinter();
		esc.addPrintAndFeedLines((byte) 3);
		esc.addSelectJustification(JUSTIFICATION.CENTER);// 设置打印居中
		esc.addSelectPrintModes(FONT.FONTA, ENABLE.OFF, ENABLE.ON, ENABLE.ON,
				ENABLE.OFF);// 设置为倍高倍宽
		esc.addText("销售小票\n"); // 打印文字
		esc.addPrintAndLineFeed();// 打印分割线

		esc.addSelectPrintModes(FONT.FONTA, ENABLE.OFF, ENABLE.OFF, ENABLE.OFF,ENABLE.OFF);// 取消倍高倍宽
		esc.addSelectJustification(JUSTIFICATION.LEFT);// 设置打印左对齐
		esc.addText("店名：" + App.user.getShopInfo().getShop_name() + "\n",charset);
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
		esc.addText("打印时间：" + sdf.format(d) + "\n", charset);
		esc.addPrintAndLineFeed();
		esc.addText("小 票 号：" + billsale.getSaleNo() + "\n", charset);
		esc.addText("交易时间：" + billsale.getCreatetimeStr() + "\n");
		esc.addText("收 银 员：" + App.user.getUserInfo().getUser_code() + "\n");
//		esc.addText("交易类型：销售" + "\n");
		esc.addText("交易类型：" + billsale.getSaleType() + "\n");
		esc.addText("支付方式：" + billsale.getRemark() + "\n");
		esc.addText("交易金额：￥" + billsale.getTotalMoney() + "\n");
		esc.addText("交易数量：" + billsale.getTotalQty() + "\n");

		esc.addPrintAndLineFeed();

		String gap = "   ";
		esc.addText("商品" + gap + "数量" + gap + "单价" + gap + "折率" + gap + "金额\n");

		List<JvbillsaledetailInfo> list = response.getDetailList();
		if (list != null) {
			for (JvbillsaledetailInfo item : list) {
				String divSaleRate = "无";
				if (item.getDivSaleRate() >= 100) {
//					divSaleRate = "";
				} else {
					divSaleRate = StringUtil.formatNumber(
							item.getDivSaleRate() / 10, "###0.#")+ "折";
				}
				esc.addText(item.getProdNum() + " " + item.getQty() + "件"
						+ " ￥" + StringUtil.formatMoney(item.getRetailPrice())
						+ " " + divSaleRate + " ￥"
						+ StringUtil.formatMoney(item.getSalePrice()) + "\n");

			}
		}

		esc.addPrintAndLineFeed();

		// 开钱箱
		esc.addGeneratePlus(LabelCommand.FOOT.F5, (byte) 255, (byte) 255);
		esc.addPrintAndFeedLines((byte) 4);
		esc.addCutPaper();//切纸

		sendEscCommand(esc);
	}

	private void sendEscCommand(EscCommand esc) {
		if (mGpService == null) {
			return;
		}
		Vector<Byte> datas = esc.getCommand(); // 发送数据
		byte[] bytes = GpUtils.ByteTo_byte(datas);
		String str = Base64.encodeToString(bytes, Base64.DEFAULT);
		int rs;
		try {
			rs = mGpService.sendEscCommand(printerID, str);
			GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rs];
			if (r != GpCom.ERROR_CODE.SUCCESS) {
				Toast.makeText(context, GpCom.getErrorText(r),
						Toast.LENGTH_SHORT).show();
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// exsample
	/* 打印繁体中文 需要打印机支持繁体字库 */
	// esc.addText(message,"BIG5");
	/* 绝对位置 具体详细信息请查看GP58编程手册 */
	// esc.addText("智汇");
	// esc.addSetHorAndVerMotionUnits((byte) 7, (byte) 0);
	// esc.addSetAbsolutePrintPosition((short) 6);
	// esc.addText("网络");
	// esc.addSetAbsolutePrintPosition((short) 10);
	// esc.addText("设备");
	// esc.addPrintAndLineFeed();

	/* 打印图片 */
	// esc.addText("Print bitmap!\n"); // 打印文字
	// Bitmap b = BitmapFactory.decodeResource(getResources(),
	// R.drawable.gprinter);
	// esc.addRastBitImage(b, 384, 0); // 打印图片

	/* 打印一维条码 */
	// esc.addText("Print code128\n"); // 打印文字
	// esc.addSelectPrintingPositionForHRICharacters(HRI_POSITION.BELOW);//
	// // 设置条码可识别字符位置在条码下方
	// esc.addSetBarcodeHeight((byte) 60); // 设置条码高度为60点
	// esc.addSetBarcodeWidth((byte) 1); // 设置条码单元宽度为1
	// esc.addCODE128(esc.genCodeB("SMARNET")); // 打印Code128码
	// esc.addPrintAndLineFeed();

	/*
	 * QRCode命令打印 此命令只在支持QRCode命令打印的机型才能使用。 在不支持二维码指令打印的机型上，则需要发送二维条码图片
	 */
	// esc.addText("Print QRcode\n"); // 打印文字
	// esc.addSelectErrorCorrectionLevelForQRCode((byte) 0x31); // 设置纠错等级
	// esc.addSelectSizeOfModuleForQRCode((byte) 3);// 设置qrcode模块大小
	// esc.addStoreQRCodeData("www.smarnet.cc");// 设置qrcode内容
	// esc.addPrintQRCode();// 打印QRCode
	// esc.addPrintAndLineFeed();

	/* 打印文字 */
	// esc.addSelectJustification(JUSTIFICATION.LEFT);// 设置打印左对齐
	// esc.addText("Completed!\r\n"); // 打印结束
}
