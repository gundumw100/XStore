package com.app.printer;

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

import com.gprinter.aidl.GpService;
import com.gprinter.command.EscCommand.ENABLE;
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

	public static final String EVENT_CONNECTING_PRINTER="connectingPriner";
	public static final String EVENT_CONNECT_PRINTER_RESULT="connectPrinerResult";
	
	private Context context;
	private GpService mGpService=null;
	private PrinterServiceConnection conn;
	private static GprinterUtil gprinterUtil=null;
	
	public static GprinterUtil getInstance(Context context){
		if(gprinterUtil==null){
			gprinterUtil=new GprinterUtil(context);
		}
		return gprinterUtil;
	}
	
	private GprinterUtil(Context context){
		this.context=context;
	}

	//开启打印机服务监听
    public void bindPrinterService() {
    	//注册打印机广播
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
	
	//开启打印端口
	public void connectPrinter(String diviceName) {
    	closePort();
    	int rel =0;
    	try{//使用端口1，4代表模式为蓝牙模式，蓝牙地址，最后默认为0
    		if(mGpService!=null){
    			rel = mGpService.openPort(1,4,diviceName,0);
    		}
    	}catch(RemoteException e) {
    		e.printStackTrace();
    	}
//	    	GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
//	    	if(r != GpCom.ERROR_CODE.SUCCESS) {
//		    	if(r == GpCom.ERROR_CODE.DEVICE_ALREADY_OPEN) {
//		    		Toast.makeText(context, "开启成功", Toast.LENGTH_SHORT).show();
//		    	}else{
//		    		Toast.makeText(context, GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
//		    	}
//	    	}else{
//		    	Toast.makeText(context, "失败", Toast.LENGTH_SHORT).show();
//	    	}
    	
    }
	 
	public boolean isBluetoothAvailable() {
		// Get local Bluetooth adapter
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		// If the adapter is null, then Bluetooth is not supported
		if (bluetoothAdapter == null) {
//			showToast("设备不支持蓝牙");
			return false;
		} else {
			// If BT is not on, request that it be enabled.
			// setupChat() will then be called during onActivityResult
			if (!bluetoothAdapter.isEnabled()) {
//				showToast("蓝牙未开启");
				return false;
			}
		}
		return true;
	}
	
	public boolean isPrinterConnected(){
    	try {
    		if(mGpService!=null){
    			int status=mGpService.getPrinterConnectStatus(1);
    			if(status == GpDevice.STATE_CONNECTED){//3
    				return true;
    			}
    		}else{
    			Toast.makeText(context, "mGpService is null.", Toast.LENGTH_SHORT).show();
    		}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return false;
    }
	
	public void closePort() {
		try {
			if(mGpService!=null)
				mGpService.closePort(1);
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
					Toast.makeText(context, "连接中...", Toast.LENGTH_SHORT).show();
					EventBus.getDefault().post(EVENT_CONNECTING_PRINTER);
				} else if (type == GpDevice.STATE_NONE) {
					Toast.makeText(context, "未找到打印机", Toast.LENGTH_SHORT).show();
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
	
	public void closePrinterService(){
		if(printerStatusBroadcastReceiver!=null){
        	context.unregisterReceiver(printerStatusBroadcastReceiver);
        }
        if (conn != null) {
        	context.unbindService(conn); // unBindService
		}
//        closePort();
	}
	
	//获取打印机的命令类型，是ESC命令还是Label命令
	public int getPrinterCommandType(){
		try {
			return mGpService.getPrinterCommandType(1);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	public void sendTestLabel() {
		sendLabel("男休闲短袖","1234567890","白色","XXL","99");
	}
	
	public void sendLabel(String name,String sku,String color,String size,String ls_price) {
		int x=40;
		int y=50;
		int paddingTop=50;
		
		LabelCommand tsc = new LabelCommand();
		tsc.addSize(40, 80); // 设置标签尺寸，按照实际尺寸设置
		tsc.addGap(2); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
		tsc.addDirection(DIRECTION.BACKWARD, MIRROR.NORMAL);// 设置打印方向
		tsc.addReference(0, 0);// 设置原点坐标
		tsc.addTear(ENABLE.ON); // 撕纸模式开启
		tsc.addCls();// 清除打印缓冲区
		tsc.addText(x, paddingTop+1*y, FONTTYPE.SIMPLIFIED_CHINESE, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
	            "品名："+name);
		tsc.addText(x, paddingTop+2*y, FONTTYPE.SIMPLIFIED_CHINESE, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				"款号："+sku.substring(0, 6));
		tsc.addText(x, paddingTop+3*y, FONTTYPE.SIMPLIFIED_CHINESE, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				"颜色："+color);
		tsc.addText(x, paddingTop+4*y, FONTTYPE.SIMPLIFIED_CHINESE, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				"尺码："+size);
		tsc.addText(x, paddingTop+5*y, FONTTYPE.SIMPLIFIED_CHINESE, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				"零售价：￥"+ls_price);
    	// 绘制一维条码
   		tsc.add1DBarcode(x, paddingTop+7*y, BARCODETYPE.CODE128, 100, READABEL.EANBEL, ROTATION.ROTATION_0, sku);
   		
   	// 绘制图片
//   	Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.gprinter);
//   	tsc.addBitmap(x, 7*y, BITMAP_MODE.OVERWRITE, b.getWidth(), b);
		//绘制二维码
//   	tsc.addQRCode(x, 8*y, EEC.LEVEL_L, 5, ROTATION.ROTATION_0, "www.smarnet.cc");
   		
		tsc.addPrint(1, 1); // 打印标签
		tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
		tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
		sendLabelCommand(tsc);
	}
	
	private void sendLabelCommand(LabelCommand tsc){
		if(mGpService==null){
			return;
		}
		Vector<Byte> datas = tsc.getCommand(); // 发送数据
		byte[] bytes = GpUtils.ByteTo_byte(datas);
		String str = Base64.encodeToString(bytes, Base64.DEFAULT);
		int rel;
		try {
			rel = mGpService.sendLabelCommand(1, str);
			GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
			if (r != GpCom.ERROR_CODE.SUCCESS) {
				Toast.makeText(context, GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
