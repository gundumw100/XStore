package com.app.xstore.cashier;

import java.util.ArrayList;
import java.util.UUID;

import org.simple.eventbus.Subscriber;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.model.PaymentType;
import com.app.net.SosopayRequest;
import com.app.net.SosopayTradePrecreateTask;
import com.app.net.SosopayTradePrecreateTask.OnResponseListener;
import com.app.net.SosopayTradeQueryRequestTask;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.app.xstore.shangpindangan.ProductDangAn;
import com.base.util.D;
import com.google.zxing.WriterException;
import com.sosopay.SosopayConstants;
import com.sosopay.SosopayResponse;
import com.sosopay.response.SosopayTradePrecreateResponse;
import com.sosopay.response.SosopayTradeQueryResponse;
import com.sosopay.vo.GoodsInfo;
import com.zxing.encoding.EncodingHandler;

/**
 * 二维码支付界面
 * @author Ni Guijun
 *
 */
@Deprecated
public class PaymentActivity extends BaseActivity{

	private Context context;
	private PaymentType paymentType;
	private ImageView iv_qr;
	private String chargeCode=null;//交易流水号,用户查询是否支付完毕
	private static final boolean isTest=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment);
		context=this;
		paymentType=getIntent().getParcelableExtra("PaymentType");
		if(paymentType==null){
			Log.i("tag", "paymentType is null");
			return;
		}
		initActionBar(paymentType.getName(),paymentType.getType()==0||paymentType.getType()==1?"":"打印二维码",null);
		initViews();
		doSosopayTradePrecreateTask();
	}

	//主扫支付交易发起（客户扫商家）
	private void doSosopayTradePrecreateTask() {
		showProgress();
		String busiCode=SosopayRequest.BUSI_CODE;
		String operid=App.user.getUserInfo().getUser_code();//操作员编号
		String devid=App.config.getDeviceId();//设备编号
		String storeid=App.user.getShopInfo().getShop_code();//门店编号
		double amt=getIntent().getDoubleExtra("Need", 0.00);//交易金额，元
		if(isTest){
			amt=0.01d;
		}
		int channelType=0;
		if(paymentType.getType()==0){
			channelType=SosopayConstants.CHANNEL_TYPE_ALI;//
		}else if(paymentType.getType()==1){
			channelType=SosopayConstants.CHANNEL_TYPE_WEIXIN;//
		}
		chargeCode=UUID.randomUUID().toString();//交易流水号
		String notifyUrl="http://test.sssyin.cn:8083/sosopayWebDemo/sosoNodify";//
		String paySubject="收款";//支付信息描述
		ArrayList<ProductDangAn> beans =getIntent().getParcelableArrayListExtra("Products");
		ArrayList<GoodsInfo> goodsInfos=null;
		if(!isEmptyList(beans)){
			paySubject="零售";//支付信息描述
			goodsInfos=new ArrayList<GoodsInfo>();
			for(ProductDangAn bean:beans){
				GoodsInfo goodsInfo = new GoodsInfo(bean.getGoods_sn(),bean.getGoods_name(),"类型",String.valueOf(bean.getGoods_price()),bean.getGoods_desc(),"1");
				goodsInfos.add(goodsInfo);
			}
		}
		SosopayTradePrecreateTask task=new SosopayTradePrecreateTask(context,busiCode, operid, devid, storeid, amt, channelType, chargeCode, notifyUrl, paySubject, goodsInfos,new OnResponseListener() {
			
			@Override
			public void onResponse(SosopayResponse res) {
				// TODO Auto-generated method stub
				SosopayTradePrecreateResponse response=(SosopayTradePrecreateResponse)res;
				updateViews(response);
				closeProgress();
				handler.postDelayed(runnable, delay);
			}
		});
		task.execute(1);
	}
	
	 private void createQRCode(ImageView iv , String QRCode){
			if(!TextUtils.isEmpty(QRCode)){
				try {
					//根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小
					Bitmap qrCodeBitmap = EncodingHandler.createQRCode(QRCode, 480);
					iv.setImageBitmap(qrCodeBitmap);
				} catch (WriterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	 
	@Override
	public void initViews() {
//		String MerchantName=getIntent().getStringExtra("MerchantName");
		TextView tv_merchantName=(TextView)findViewById(R.id.tv_merchantName);
		tv_merchantName.setText("收款帐号：未知");
		double ying=getIntent().getDoubleExtra("Need", 0.00);
		TextView tv_realMoney=(TextView)findViewById(R.id.tv_realMoney);
		tv_realMoney.setText("金额： ￥"+ying);
		iv_qr=(ImageView)findViewById(R.id.iv_qr);
	}
	
	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
		if(obj==null){
			return;
		}
		SosopayTradePrecreateResponse response=(SosopayTradePrecreateResponse)obj;
		createQRCode(iv_qr , response.getBarCode());
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		D.showDialog(PaymentActivity.this, "确定放弃支付？\n(若在支付途中，请稍后退出)", "是", "否", new D.OnPositiveListener() {
			
			@Override
			public void onPositive() {
				// TODO Auto-generated method stub
				handler.removeCallbacks(runnable);
				finish();
			}
		}, new D.OnNegativeListener() {
			
			@Override
			public void onNegative() {
				// TODO Auto-generated method stub
			}
		});
	}

	private int tick=0;
	private int delay=5000;
	private Handler handler = new Handler();
	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//检测是否已经支付
			doSosopayTradeQueryRequestTask(chargeCode);
		}
	};

	private void doSosopayTradeQueryRequestTask(String chargeCode){
		Log.i("tag", "开始检测是否已经支付"+(tick++));
		SosopayTradeQueryRequestTask task=new SosopayTradeQueryRequestTask(context,chargeCode,new SosopayTradeQueryRequestTask.OnResponseListener() {
			
			@Override
			public void onResponse(SosopayResponse res) {
				// TODO Auto-generated method stub
				SosopayTradeQueryResponse response=(SosopayTradeQueryResponse)res;
				if(response.getState()==SosopayConstants.PAIED){//已付款
					handler.removeCallbacks(runnable);
					doCommandSaveBillSale(paymentType.getType(),null,null);
				}else{
					//没有支付则继续检测，直到检测到支付成功为止
					handler.postDelayed(runnable, delay);
				}
			}
		});
		task.execute(1);
	}
	
	@Subscriber
	void updateByEventBus(String event) {
		if (event.equals(App.EVENT_FINISH)) {
			finish();
		}
	}
	
}
