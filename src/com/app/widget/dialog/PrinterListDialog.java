package com.app.widget.dialog;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response.Listener;
import com.app.model.Printer;
import com.app.model.response.GetBillSaleByNumResponse;
import com.app.model.response.GetPrinterInfoResponse;
import com.app.net.Commands;
import com.app.util.PrintUtil;
import com.app.widget.BaseDialog;
import com.app.xstore.App;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;
import com.postek.cdf.CDFPTKAndroid;
import com.postek.cdf.CDFPTKAndroidImpl;

/**
 * 打印机列表
 * @author pythoner
 * 
 */
@SuppressLint("HandlerLeak")
public class PrinterListDialog extends BaseDialog {


	private Object obj;//需要打印的对象
	private ListView listView;
	private CommonAdapter<Printer> adapter;
	private List<Printer> beans = new ArrayList<Printer>();
	
	public static CDFPTKAndroid cdf = null;//POSTEK打印机
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == CDFPTKAndroid.PTK_MSG_WHAT_WIFICONNECT) {
				if (msg.arg1 == 0) {
					showToast("连接打印机成功");
				}else {
					showToast("连接打印机失败");
				}
			}
		}
	};
	
	public PrinterListDialog(Context context,Object obj) {
		this(context, R.style.Theme_Dialog_NoTitle,obj);
		// TODO Auto-generated constructor stub
	}
	
	public PrinterListDialog(Context context, int theme,Object obj) {
		super(context, theme);
		// TODO Auto-generated constructor stub
//		setCanceledOnTouchOutside(false);
		this.obj=obj;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_printer_list);
		initViews();
		doCommandGetPrinterInfo();
	}

	private void initViews() {
		listView = (ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int postion,
					long id) {
				// TODO Auto-generated method stub
				final Printer printer=beans.get(postion);
				if(obj instanceof GetBillSaleByNumResponse){
					
					if(printer.getType()==0||printer.getType()==1||printer.getType()==2||printer.getType()==3){
						new Thread() {
							@Override
							public void run() {
								Looper.prepare();// 需要prepare()，否则可能报错
								PrintUtil util = new PrintUtil(context);
	//							String result=util.doPrintTest(printer);
								boolean result=util.doPrintTicket(printer,(GetBillSaleByNumResponse)obj);
								Looper.loop();
							}
						}.start();
					}else  
					if(printer.getType()==4){//POSTEK打印机
							//配置POSTEK打印机
							cdf = new CDFPTKAndroidImpl(context, handler);
							Thread thread=new Thread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									String ip=printer.getPrinterip().split(":")[0];
									int port=Integer.parseInt(printer.getPrinterip().split(":")[1]);
									
									//连接POSTEK打印机
									cdf.PTK_ConnectWiFi(ip, port);
									cdf.PTK_PrintConfiguration();
									cdf.PTK_CloseConnectWiFi();
								}
							});
					    	thread.start();
						}
					else{
						showToast("未知型号打印机");
					}
				}
				dismiss();
			}
		});
//		findViewById(R.id.btn_left).setOnClickListener(this);
//		findViewById(R.id.btn_right).setOnClickListener(this);
	}

	
	private void doCommandGetPrinterInfo(){
		String shopId=App.user.getShopInfo().getShop_code();
		Commands.doCommandGetPrinterInfo(context, shopId,new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
//				Log.i("tag", response.toString());
				if (context.isSuccess(response)) {
					GetPrinterInfoResponse obj = context.mapperToObject(response, GetPrinterInfoResponse.class);
					List<Printer> list = obj.getHeadInfo();
					beans.clear();
					beans.addAll(list);
					if(beans.size()==0){
						if(listView.getEmptyView()==null){
							context.setEmptyView(listView, "未添加打印机");
						}
					}
					notifyDataSetChanged();
				}
			}
		});
		
	}
	
	private void notifyDataSetChanged(){
		if(adapter==null){
			listView.setAdapter(adapter = new CommonAdapter<Printer>(context, beans,
					  R.layout.item_simple_list){
					  
					@Override
					public void setValues(ViewHolder helper, final Printer item, int position) {
						// TODO Auto-generated method stub
						helper.setText(R.id.item_0, item.getDescription());
					}
			});
		}else{
			adapter.notifyDataSetChanged();
		}
	}
	
}

