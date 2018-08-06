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
import android.widget.TextView;

import com.app.model.Goods;
import com.app.model.PaymentType;
import com.app.net.SosopayRequest;
import com.app.net.SosopayTradePayTask;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.base.util.T;
import com.sosopay.SosopayResponse;
import com.sosopay.vo.GoodsInfo;

/**
 * 支付类别界面
 * @author Ni Guijun
 *
 */
public class PaymentTypeActivity extends BaseActivity implements OnClickListener{

	private Context context;
	private ArrayList<PaymentType> paymentTypes = new ArrayList<PaymentType>();
	private int position = -1;
	private Button[] btns;
	private ArrayList<Goods> beans;
	double ding=0.00;//订单金额
	double you=0.00;//优惠金额
	double ying=0.00;//应收金额
	private static final boolean isTest=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment_type);
		context = this;
		paymentTypes.add(new PaymentType(0, "支付宝"));
		paymentTypes.add(new PaymentType(1, "微信"));
		paymentTypes.add(new PaymentType(2, "现金"));
		paymentTypes.add(new PaymentType(3, "刷卡"));
		initActionBar("提交订单",null,null);
		
		beans=getIntent().getParcelableArrayListExtra("Goods");
		initHandler();
		initViews();
		createFloatView(100);
	}
	
	@Override
	public void initViews() {
		for(Goods bean:beans){
			ding+=bean.getGoods_price();
			you+=bean.getGoods_price_discount_off();
			ying+=bean.getGoods_price_discount();
		}
		TextView item_0 = (TextView) findViewById(R.id.item_0);
//		TextView item_1 = (TextView) findViewById(R.id.item_1);
		TextView item_2 = (TextView) findViewById(R.id.item_2);
		TextView item_3 = (TextView) findViewById(R.id.item_3);
		item_0.setText("订单金额: ￥"+formatMoney(ding));
//		item_1.setText("抵用券: -￥");
		item_2.setText("优惠金额: -￥"+formatMoney(you));
		item_3.setText("应收金额:￥"+formatMoney(ying));
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
		
		if(ying<=0){//应收金额为0，只显示现金支付
			btns[0].setVisibility(View.GONE);
			btns[1].setVisibility(View.GONE);
			btns[3].setVisibility(View.GONE);
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
		if(position==0||position==1){
			Intent intent = new Intent(context, PaymentActivity.class);
			intent.putExtra("PaymentType", paymentTypes.get(position));
//			intent.putExtra("Ding", ding);
			intent.putExtra("Ying", ying);
			intent.putParcelableArrayListExtra("Goods", beans);
			startActivityForResult(intent, 400);
		}else if(position==2){
			Intent intent =new Intent(context,PayCrashActivity.class);
//			intent.putExtra("Ding", ding);
			intent.putExtra("Ying", ying);
			intent.putParcelableArrayListExtra("Goods", beans);
			startActivity(intent);
		}else if(position==3){
			Intent intent =new Intent(context,PayCreditActivity.class);
//			intent.putExtra("Ding", ding);
			intent.putExtra("Ying", ying);
			intent.putParcelableArrayListExtra("Goods", beans);
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
		double amt=ying;//交易金额，元
		if(isTest){
			amt=0.01d;
		}
		String dynamicId=br_code;//支付用户动态码,即扫描后的数据
		String chargeCode=UUID.randomUUID().toString();//交易流水号
		String paySubject="零售";//支付信息描述
		ArrayList<GoodsInfo> goodsInfos=new ArrayList<GoodsInfo>();
		ArrayList<Goods> beans = this.beans;
		for(Goods bean:beans){
			GoodsInfo goodsInfo = new GoodsInfo(bean.getGoods_sn(),bean.getGoods_name(),"类型",String.valueOf(bean.getGoods_price()),bean.getGoods_desc(),"1");
			goodsInfos.add(goodsInfo);
		}
		SosopayTradePayTask task=new SosopayTradePayTask(context,busiCode, operid, devid, storeid, amt, /*channelType, dynamicIdType,*/ dynamicId, chargeCode,paySubject, goodsInfos,new SosopayTradePayTask.OnResponseListener() {
			
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
