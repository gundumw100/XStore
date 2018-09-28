package com.app.widget.dialog;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
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

/**
 * 打印小票对话框
 * @author pythoner
 * 
 */
public class PrintTicketDialog extends BaseDialog implements View.OnClickListener {

	private String saleNo;
	private ListView listView;
	private CommonAdapter<Printer> adapter;
	private List<Printer> beans = new ArrayList<Printer>();
	
	private GetBillSaleByNumResponse obj;
	
	public PrintTicketDialog(Context context,String saleNo) {
		this(context, R.style.Theme_Dialog_NoTitle,saleNo);
		// TODO Auto-generated constructor stub
	}
	
	public PrintTicketDialog(Context context, int theme,String saleNo) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.saleNo=saleNo;
		setCancelable(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_print_ticket);
		initViews();
		doCommandGetBillSaleByNum(saleNo);
	}

	private void initViews() {
		listView = (ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int postion,
					long id) {
				// TODO Auto-generated method stub
				final Printer printer=beans.get(postion);
					
					if(printer.getType()==0||printer.getType()==1||printer.getType()==2||printer.getType()==3){
						new Thread() {
							@Override
							public void run() {
								Looper.prepare();// 需要prepare()，否则可能报错
								PrintUtil util = new PrintUtil(context);
	//							String result=util.doPrintTest(printer);
								boolean result=util.doPrintTicket(printer,obj);
								Looper.loop();
							}
						}.start();
					}
//					else  
//					if(printer.getType()==4){//POSTEK打印机
//							//配置POSTEK打印机
//							cdf = new CDFPTKAndroidImpl(context, handler);
//							Thread thread=new Thread(new Runnable() {
//								
//								@Override
//								public void run() {
//									// TODO Auto-generated method stub
//									String ip=printer.getPrinterip().split(":")[0];
//									int port=Integer.parseInt(printer.getPrinterip().split(":")[1]);
//									
//									//连接POSTEK打印机
//									cdf.PTK_ConnectWiFi(ip, port);
//									cdf.PTK_PrintConfiguration();
//									cdf.PTK_CloseConnectWiFi();
//								}
//							});
//					    	thread.start();
//						}
					else{
						showToast("未知型号打印机");
					}
				dismiss();
				finishAndClearShoppingCart();
			}
		});
		findViewById(R.id.btn_left).setOnClickListener(this);
//		findViewById(R.id.btn_right).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_left:
			dismiss();
			finishAndClearShoppingCart();
			break;
//		case R.id.btn_right:
//			doCommandGetBillSaleByNum(saleNo);
//			break;

		default:
			break;
		}
	}
	
	private void doCommandGetBillSaleByNum(String saleNo){
		String shopCode=App.user.getShopInfo().getShop_code();
		Commands.doCommandGetBillSaleByNum(context, shopCode, saleNo, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (context.isSuccess(response)) {
					obj=context.mapperToObject(response, GetBillSaleByNumResponse.class);
					
					doCommandGetPrinterInfo();
					
//					context.doPrintReceipt(PrintTicketDialog.this,obj);//打印小票
//					finishAndClearShoppingCart();
				}
			}
		});
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
	
	private void finishAndClearShoppingCart(){
		EventBus.getDefault().post(App.EVENT_FINISH);//关闭PaymentTypeActivity界面
		EventBus.getDefault().post(App.EVENT_CLEAR);//清理ShoppingCartActivity数据
		context.finish();
	}
	
}

