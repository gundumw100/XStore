package com.app.xstore.cashier;

import java.util.ArrayList;

import org.simple.eventbus.Subscriber;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.app.model.PaymentType;
import com.app.widget.dialog.PaymentJiFenDialog;
import com.app.widget.dialog.PaymentYongKaDialog;
import com.app.widget.dialog.PaymentYongQuanDialog;
import com.app.widget.dialog.PaymentZhanghuDialog;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.app.xstore.shangpindangan.ProductDangAn;

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
	private ArrayList<ProductDangAn> beans;
	double ding=0.00;//订单金额
	double ying=0.00;//应付金额
	double need=0.00;//尚需支付
	
	private TextView item_pay_price,item_need_pay_price;
	private TextView et_jifen,et_quan,et_ka,et_zhanghu;
	
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
		
		beans=getIntent().getParcelableArrayListExtra("Products");
		initViews();
	}
	
	@Override
	public void initViews() {
		for(ProductDangAn bean:beans){
			ding+=bean.getGoods_price();
			ying+=bean.getGoods_price();
			need+=bean.getGoods_price();
		}
		TextView item_price = (TextView) findViewById(R.id.item_price);
		item_price.setText("订单金额: ￥"+formatMoney(ding));
		
		item_pay_price = (TextView) findViewById(R.id.item_pay_price);
		item_need_pay_price = (TextView) findViewById(R.id.item_need_pay_price);
		
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
		
		et_jifen = (TextView) findViewById(R.id.et_jifen);
		et_quan = (TextView) findViewById(R.id.et_quan);
		et_ka = (TextView) findViewById(R.id.et_ka);
		et_zhanghu = (TextView) findViewById(R.id.et_zhanghu);
		
		TextView btn_jifen = (TextView) findViewById(R.id.btn_jifen);
		TextView btn_quan = (TextView) findViewById(R.id.btn_quan);
		TextView btn_ka = (TextView) findViewById(R.id.btn_ka);
		TextView btn_zhanghu = (TextView) findViewById(R.id.btn_zhanghu);
		btn_jifen.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PaymentJiFenDialog d=new PaymentJiFenDialog(context);
				d.setOnOkClickListener(new PaymentJiFenDialog.OnOkClickListener() {
					
					@Override
					public void onOkClick(View v, float jifenJine) {
						// TODO Auto-generated method stub
						et_jifen.setText(""+jifenJine);
						updatePriceViews();
					}
				});
				d.show();
			}
		});
		btn_quan.setOnClickListener(this);
		btn_quan.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PaymentYongQuanDialog d=new PaymentYongQuanDialog(context);
				d.show();
			}
		});
		btn_ka.setOnClickListener(this);
		btn_ka.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PaymentYongKaDialog d=new PaymentYongKaDialog(context);
				d.show();
			}
		});
		btn_zhanghu.setOnClickListener(this);
		btn_zhanghu.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PaymentZhanghuDialog d=new PaymentZhanghuDialog(context);
				d.show();
			}
		});
		
		updatePriceViews();
	}

	private void updatePriceViews(){
		float f_jifen=0f;
		String jifen=et_jifen.getText().toString();
		if(!isEmpty(jifen)){
			f_jifen=Float.parseFloat(jifen);
		}
		
		float f_quan=0f;
		String quan=et_quan.getText().toString();
		if(!isEmpty(quan)){
			f_quan=Float.parseFloat(quan);
		}
		
		ying=ying-f_jifen-f_quan;
		item_pay_price.setText("应付金额:￥"+formatMoney(ying));
		
		need=need-f_jifen-f_quan;
		item_need_pay_price.setText("￥"+formatMoney(need));
		
		if(need<=0){//尚需支付为0，只显示现金支付
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
		if(position==0){
			Intent intent = new Intent(context, PayZFBActivity.class);
			intent.putParcelableArrayListExtra("Products", beans);
			intent.putExtra("Need", need);
			startActivity(intent);
		}else if(position==1){
			Intent intent = new Intent(context, PayWXActivity.class);
			intent.putParcelableArrayListExtra("Products", beans);
			intent.putExtra("Need", need);
			startActivity(intent);
		}
		else if(position==2){
			Intent intent =new Intent(context,PayCrashActivity.class);
			intent.putParcelableArrayListExtra("Products", beans);
			intent.putExtra("Need", need);
			startActivity(intent);
		}else if(position==3){
			Intent intent =new Intent(context,PayCreditActivity.class);
			intent.putParcelableArrayListExtra("Products", beans);
			intent.putExtra("Need", need);
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
	public void updateTheme(int color) {
		super.updateTheme(color);
		updateTheme();
	}
	
	private void updateTheme() {
	}
	
}
