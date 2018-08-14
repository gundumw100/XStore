package com.app.xstore.cashier;

import java.util.ArrayList;
import java.util.UUID;

import org.simple.eventbus.Subscriber;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.app.model.PaymentType;
import com.app.net.SosopayRequest;
import com.app.net.SosopayTradePayTask;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.base.util.T;
import com.sosopay.SosopayResponse;

/**
 * 收款界面
 * @author Ni Guijun
 *
 */
@Deprecated
public class ReceiptActivity extends BaseActivity implements OnClickListener{

	private Context context;
	private ArrayList<PaymentType> paymentTypes = new ArrayList<PaymentType>();
	private int position = -1;
	private Button[] btns;
	private EditText et_money;
	private static final boolean isTest=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receipt);
		context = this;
		paymentTypes.add(new PaymentType(0, "支付宝"));
		paymentTypes.add(new PaymentType(1, "微信"));
		paymentTypes.add(new PaymentType(2, "现金"));
		paymentTypes.add(new PaymentType(3, "刷卡"));
		initActionBar("收款","交易查询",null);
		
		initHandler();
		initViews();
		createFloatView(100);
	}
	
	@Override
	public void doRightButtonClick(View v) {
		// TODO Auto-generated method stub
		startActivity(new Intent(context,TradeListActivity.class));
	}
	
	@Override
	public void initViews() {
		et_money = (EditText) findViewById(R.id.et_money);
		
		btns = new Button[paymentTypes.size()];
		btns[0] = (Button) findViewById(R.id.btn_zhifubao);
		btns[1] = (Button) findViewById(R.id.btn_weixin);
		btns[2] = (Button) findViewById(R.id.btn_xianjin);
		btns[3] = (Button) findViewById(R.id.btn_shuaka);
		for (int i = 0; i < btns.length; i++) {
			btns[i].setBackgroundResource(R.color.gray);
			btns[i].setOnClickListener(this);
			btns[i].setText(paymentTypes.get(i).getName());
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_zhifubao:
			position = 0;
			break;
		case R.id.btn_weixin:
			position = 1;
			break;
		case R.id.btn_xianjin:
			position = 2;
			break;
		case R.id.btn_shuaka:
			position = 3;
			break;
		}
		switchState(position);
		
		double ying=Double.parseDouble(formatMoney(et_money.getText().toString()));
		
		if(ying<=0){
			doShake(context, et_money);
			return;
		}
		
		if(position==0||position==1){
			Intent intent = new Intent(context, PaymentActivity.class);
			intent.putExtra("PaymentType", paymentTypes.get(position));
			intent.putExtra("Ying", ying);
//			intent.putParcelableArrayListExtra("Goods", beans);
			startActivityForResult(intent, 400);
		}else if(position==2){
			Intent intent =new Intent(context,PayCrashActivity.class);
			intent.putExtra("Ying", ying);
			startActivity(intent);
		}else if(position==3){
			Intent intent =new Intent(context,PayCreditActivity.class);
			intent.putExtra("Ying", ying);
			startActivity(intent);
		}
		
	}

	private void switchState(int position) {
		for (int i = 0; i < btns.length; i++) {
			btns[i].setBackgroundResource(i == position ? android.R.color.holo_blue_light: R.color.gray);
		}
	}

	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
		
	}
	@Subscriber
	void updateByEventBus(String event) {
		if (event.equals(App.EVENT_FINISH)) {
			finish();
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		removeFloatView();
	}
	
	@Override
	public void initHandler(){
		resultHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				String data = (String) msg.obj;
				if(TextUtils.isEmpty(data)||data.equalsIgnoreCase("time out")){
					showToast(R.string.alert_no_barcode_found);
					return;
				}
				Log.i("tag", "data="+data);
				doSosopayTradePayTask(data);
			}
		};
	}
	
	//被扫支付交易发起（商家扫客户）
	private void doSosopayTradePayTask(String br_code){
		String busiCode=SosopayRequest.BUSI_CODE;
		String operid=App.user.getUserInfo().getUser_code();//操作员编号
		String devid=App.config.getDeviceId();//设备编号
		String storeid=App.user.getShopInfo().getShop_code();//门店编号
		double ying=Double.parseDouble(formatMoney(et_money.getText().toString()));
		if(ying<=0){
			doShake(context, et_money);
			return;
		}
		double amt=ying;//交易金额，元
		if(isTest){
			amt=0.01d;
		}
		String dynamicId=br_code;//支付用户动态码,即扫描后的数据
		String chargeCode=UUID.randomUUID().toString();//交易流水号
		String paySubject="收款";//支付信息描述
		
		SosopayTradePayTask task=new SosopayTradePayTask(context,busiCode, operid, devid, storeid, amt, /*channelType, dynamicIdType,*/ dynamicId, chargeCode,paySubject, null,new SosopayTradePayTask.OnResponseListener() {
			
			@Override
			public void onResponse(SosopayResponse res) {
				// TODO Auto-generated method stub
				T.showToast(context,res.getResult().getInfo());
			}
		});
		task.execute(1);
	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		updateTheme();
	}
	
	private void updateTheme() {
	}
	
}
