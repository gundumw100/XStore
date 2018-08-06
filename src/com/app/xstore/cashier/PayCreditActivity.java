package com.app.xstore.cashier;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.model.Goods;
import com.app.model.JvbillsaleInfo;
import com.app.model.JvbillsalebankInfo;
import com.app.model.JvbillsaledetailInfo;
import com.app.model.JvbillsalepayInfo;
import com.app.net.Commands;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;

/**
 * 信用卡支付
 * @author NGJ
 *
 */
public class PayCreditActivity extends BaseActivity implements OnClickListener {

	private Context context;
	private TextView tv_needPay;
	private EditText et_daijinquan,et_yingfujine;
	private String SaleNo;
	private int SaleID;
	private boolean canPrint=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_credit);
		context = this;
//		initActionBar("刷卡支付","打印小票",null);
		initActionBar("刷卡支付",null,null);
		initViews();
	}

	@Override
	public void doRightButtonClick(View v){
//		if(!canPrint){
//			showToast("尚未支付成功，不可打印");
//			return;
//		}
//		doCommandGetBillAppointmentPrinter(v);
	}
	
	private String yingfujine;
	@Override
	public void initViews() {
		double ying=getIntent().getDoubleExtra("Ying", 0.00);
		yingfujine=formatMoney(ying);
		tv_needPay=(TextView)findViewById(R.id.tv_needPay);
		tv_needPay.setText(yingfujine);
		et_daijinquan=(EditText)findViewById(R.id.et_daijinquan);
		et_daijinquan.setText("0");
		et_yingfujine=(EditText)findViewById(R.id.et_yingfujine);
		et_yingfujine.setText(yingfujine);
		
		findViewById(R.id.btn_ok).setOnClickListener(this);
		
		et_daijinquan.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				double daijinquan=0.00;
				if(s.length()==0){
					
				}else{
					daijinquan=Double.parseDouble(s.toString());
				}
				
				double yingfujine2=Double.parseDouble(yingfujine);
				if(daijinquan>=yingfujine2){
					tv_needPay.setText("0.00");
				}else{
					tv_needPay.setText(formatMoney(yingfujine2-daijinquan));
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	@Override
	public void onClick(final View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_ok:
			doCommandSaveBillSale(3,null,null);
//			if(App.response==null){
//				showToast("数据丢失，请返回重新结算");
//				return;
//			}
//			billSaleWrap=new BillSaleWrap();
////			billSaleWrap.setBankEntitys(App.response.getBankEntitys());
//			billSaleWrap.setCouponEntitys(App.response.getCouponEntitys());
//			billSaleWrap.setDetailEntitys(App.response.getDetailEntitys());
//			billSaleWrap.setHeadEntity(App.response.getHeadEntity());
//			billSaleWrap.setOosDeliveryAddressID(App.response.getOosDeliveryAddressID());
//			billSaleWrap.setVoucherValue(App.response.getVoucherValue());
//			if(!isEmptyList(billSaleWrap.getHeadEntity().getSaleBankInfos())){
//				billSaleWrap.getHeadEntity().getSaleBankInfos().get(0).setAmount(Double.parseDouble(formatMoney(tv_needPay.getText().toString())));
//			}else{
//				showToast(R.string.toast_dirty_data);
//				return;
//			}
//			//Card-卡  Cash-现金 CashVouc-抵用券 Voucher-代金券
//			String[] PayCode={"Card","Cash","CashVouc","Voucher"};
//			ArrayList<BillSalePayInfo> PayEntitys= new ArrayList<BillSalePayInfo>();
//
//			BillSalePayInfo payInfo=null;
//			String orgCode=getUser(context).getOrgCode();
//			//Card
//			payInfo=new BillSalePayInfo();
//			payInfo.setPayCode(PayCode[0]);
//			payInfo.setPayValue(Double.parseDouble(formatMoney(tv_needPay.getText().toString())));
//			payInfo.setChange(0.0);
//			payInfo.setActPayValue(Double.parseDouble(formatMoney(tv_needPay.getText().toString())));
//			payInfo.setOrgCode(orgCode);
//			PayEntitys.add(payInfo);
//			//Cash
//			payInfo=new BillSalePayInfo();
//			payInfo.setPayCode(PayCode[1]);
//			payInfo.setPayValue(0.00);
//			payInfo.setChange(0.00);
//			payInfo.setActPayValue(0.00);
//			payInfo.setOrgCode(orgCode);
//			PayEntitys.add(payInfo);
//			//CashVouc-抵用券
//			ArrayList<CcCouponCardInfo> cards=App.response.getCouponEntitys();
//			if(cards!=null){
//				int size=App.response.getCouponEntitys().size();
//				double payValue=0;
//				double actPayValue=0;
//				for(int i=0;i<size;i++){
//					CcCouponCardInfo card=App.response.getCouponEntitys().get(i);
//					if(card.getDISP_NAME().equals("会员券")){
//						payValue+=card.getTRAN_VAL();
//					}
//				}
//				
//				if(App.response.getHeadEntity().getTotalMoney()>payValue){
//					actPayValue=payValue;
//				}else{
//					actPayValue=App.response.getHeadEntity().getTotalMoney();
//				}
//				
//				payInfo=new BillSalePayInfo();
//				payInfo.setPayCode(PayCode[2]);
//				payInfo.setPayValue(payValue);
//				payInfo.setChange(0.00);
//				payInfo.setActPayValue(actPayValue);
//				payInfo.setOrgCode(orgCode);
//				PayEntitys.add(payInfo);
//			}else{
//				payInfo=new BillSalePayInfo();
//				payInfo.setPayCode(PayCode[2]);
//				payInfo.setPayValue(0.00);
//				payInfo.setChange(0.00);
//				payInfo.setActPayValue(0.00);
//				payInfo.setOrgCode(orgCode);
//				PayEntitys.add(payInfo);
//			}
//			//Voucher-代金券
//			payInfo=new BillSalePayInfo();
//			payInfo.setPayCode(PayCode[3]);
//			payInfo.setPayValue(Double.parseDouble(formatMoney(et_daijinquan.getText().toString())));
//			payInfo.setChange(0.0);
//			if(Double.parseDouble(formatMoney(et_daijinquan.getText().toString()))>Double.parseDouble(formatMoney(et_yingfujine.getText().toString()))){
//				payInfo.setActPayValue(Double.parseDouble(formatMoney(et_yingfujine.getText().toString())));
//			}else{
//				payInfo.setActPayValue(Double.parseDouble(formatMoney(et_daijinquan.getText().toString())));
//			}
//			payInfo.setOrgCode(orgCode);
//			PayEntitys.add(payInfo);
//
//			App.response.setPayEntitys(PayEntitys);
//			
//			billSaleWrap.setPayEntitys(App.response.getPayEntitys());//
//			
//			Commands.doCommandSaveBillSale(context,billSaleWrap,new Listener<JSONObject>() {
//				
//				@Override
//				public void onResponse(JSONObject response) {
//					// TODO Auto-generated method stub
//					Log.i("tag", "response="+response.toString());
//					if (isSuccess(response)) {
////						BGFittingSaveResponse obj=mapperToObject(response, BGFittingSaveResponse.class);
////						SaleNo =obj.getSaleNo();
////						SaleID =obj.getSaleID();
////						showToast("支付成功");
////						v.setEnabled(false);
////						canPrint=true;
////						doCommandSaveBillSaleBankAppointmentMap();
//					}
//				}
//			});
			break;

		default:
			break;
		}
	}

//	private void doCommandSaveBillSaleBankAppointmentMap(){
//		BillSaleBankAppointmentInfo bean=new BillSaleBankAppointmentInfo();
//		bean.setSaleID(SaleID);
//		bean.setSaleNo(SaleNo);
//		bean.setTotalQty(App.response.getHeadEntity().getTotalQty());
//		bean.setTotalMoney(App.response.getHeadEntity().getTotalMoney());
//		bean.setCashierUserID(App.response.getHeadEntity().getCashierUserID());
//		bean.setOrgCode(App.user.getSysUser().getOrgCode());
//		bean.setOwnerOrgCode(App.user.getSysUser().getOwnerOrgCode());
//		bean.setAppointmentID(App.AppointmentID);
//		bean.setAppointmentNo(App.AppointmentNo);
//		if(!isEmptyList(App.response.getHeadEntity().getSaleBankInfos())){
//			bean.setSaleBankID(App.response.getHeadEntity().getSaleBankInfos().get(0).getSaleBankID());
//			bean.setMerchantID(App.response.getHeadEntity().getSaleBankInfos().get(0).getMerchantID());
//			bean.setSysTraceNo(App.response.getHeadEntity().getSaleBankInfos().get(0).getSysTraceNo());
//		}
//		Commands.doCommandSaveBillSaleBankAppointmentMap(context, bean, new Listener<JSONObject>() {
//
//			@Override
//			public void onResponse(JSONObject response) {
//				// TODO Auto-generated method stub
////				Log.i("tag", response.toString());
//				if (isSuccess(response)) {
//				}
//				if(!TextUtils.isEmpty(SaleNo)){
//					doCommandGetBillAppointmentPrinter(getRightButton());
//				}
//				EventBus.getDefault().post(App.EVENT_FINISH);
//				EventBus.getDefault().post(App.EVENT_CLEAR);
//			}
//		});
//	}
//	
//	@Override
//	public void doPrintTicket(BillAppointmentPrinterInfo item){
//		boolean isSuccess=doPrintTicket(item,billSaleWrap, SaleNo,1);
//		if(isSuccess){
//			finish();
//		}
//	}

	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		updateTheme();
	}
	
	private void updateTheme() {
		setThemeDrawable(context, R.id.btn_ok);
	}
	
}
