package com.app.xstore.cashier;

import java.util.ArrayList;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;

/**
 * 现金支付
 * @author NGJ
 *
 */
public class PayCrashActivity extends BaseActivity implements OnClickListener {

	private Context context;
	private TextView tv_needPay,tv_cutMoney;
	private EditText et_shishouxianjin,et_daijinquan,et_yingfujine,et_shifujine,et_zhaolinjine;
	private String SaleNo;
	private int SaleID;
	private boolean canPrint=false;
	private Button btn_ok;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_crash);
		context = this;
//		initActionBar("现金支付","打印小票",null);
		initActionBar("现金支付",null,null);
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
	
	private double ying=0.00;//去零后的应付金额
	@Override
	public void initViews() {
		ying=getIntent().getDoubleExtra("Ying", 0.00);
		ying=Math.floor(ying);//尚需支付金额,取整
		tv_needPay=(TextView)findViewById(R.id.tv_needPay);//尚需支付
		tv_needPay.setText(formatMoney(ying));
		
		tv_cutMoney=(TextView)findViewById(R.id.tv_cutMoney);//抹零
		tv_cutMoney.setText(formatMoney(getIntent().getDoubleExtra("Ying", 0.00)-ying));
		
		et_shishouxianjin=(EditText)findViewById(R.id.et_shishouxianjin);
		et_daijinquan=(EditText)findViewById(R.id.et_daijinquan);
		et_daijinquan.setText("0");
		
		et_yingfujine=(EditText)findViewById(R.id.et_yingfujine);
		et_yingfujine.setText(formatMoney(ying));
		
		et_shifujine=(EditText)findViewById(R.id.et_shifujine);
		et_zhaolinjine=(EditText)findViewById(R.id.et_zhaolinjine);
		
		et_shishouxianjin.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				double shishouxianjin=0.00;
				if(s.length()==0){
					
				}else{
					shishouxianjin=Double.parseDouble(s.toString());
				}
				
				double daijinquan=0.00;
				if(et_daijinquan.getText().length()==0){
					
				}else{
					daijinquan=Double.parseDouble(et_daijinquan.getText().toString());
				}
				double shifujine=shishouxianjin+daijinquan;//实付金额
				et_shifujine.setText(formatMoney(shifujine));
				
				double shangxuzhifu=ying-shifujine;
				if(shangxuzhifu>0){
					tv_needPay.setText(formatMoney(shangxuzhifu));
				}else{
					tv_needPay.setText("0.00");
				}
				
				if(daijinquan>=ying){
					et_zhaolinjine.setText(formatMoney(shishouxianjin));
				}else{
					double zhaolinjine=shishouxianjin+daijinquan-ying;
					if(zhaolinjine<0){
						et_zhaolinjine.setText("");
					}else{
						et_zhaolinjine.setText(formatMoney(zhaolinjine));
					}
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
		
		et_daijinquan.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				double daijinquan=0.00;
				if(s.length()==0){
					
				}else{
					daijinquan=Double.parseDouble(s.toString());
				}
				
				double shishouxianjin=0.00;
				if(et_shishouxianjin.getText().length()==0){
					
				}else{
					shishouxianjin=Double.parseDouble(et_shishouxianjin.getText().toString());
				}
				
				double shifujine=shishouxianjin+daijinquan;
				et_shifujine.setText(formatMoney(shifujine));
				
				double shangxuzhifu=ying-shifujine;
				if(shangxuzhifu>0){
					tv_needPay.setText(formatMoney(shangxuzhifu));
				}else{
					tv_needPay.setText("0.00");
				}
				
				if(daijinquan>=ying){
					et_zhaolinjine.setText(formatMoney(shishouxianjin));
				}else{
					double zhaolinjine=shishouxianjin+daijinquan-ying;
					if(zhaolinjine<0){
						et_zhaolinjine.setText("");
					}else{
						et_zhaolinjine.setText(formatMoney(zhaolinjine));
					}
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
		
		
		btn_ok=(Button)findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(this);
		
	}

	@Override
	public void onClick(final View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_ok:
			String shishouxianjin=et_shishouxianjin.getText().toString();
			if(shishouxianjin.length()==0){
				doShake(context, et_shishouxianjin);
				return;
			}
			String zhaolinjine=et_zhaolinjine.getText().toString();
			if(zhaolinjine.length()==0){
				showToast("输入金额不正确");
				return;
			}
			doCommandSaveBillSale(2,shishouxianjin,zhaolinjine);
			
//			if(App.response==null){
//				showToast("数据丢失，请返回重新结算");
//				return;
//			}
//			String tmp=et_zhaolinjine.getText().toString();
//			if(tmp.length()==0){
//				showToast("输入金额不正确");
//				return;
//			}
//			double zhaolinjine=Double.parseDouble(tmp);
//			if(zhaolinjine>=0){//debug
//				if(billSaleWrap!=null){
//					//Card-卡  Cash-现金 CashVouc-抵用券 Voucher-代金券
//					String[] PayCode={"Card","Cash","CashVouc","Voucher"};
//					ArrayList<BillSalePayInfo> PayEntitys= new ArrayList<BillSalePayInfo>();
//					BillSalePayInfo payInfo=null;
//					String orgCode=getUser(context).getOrgCode();
//					//Card 卡参与找零
//					payInfo=new BillSalePayInfo();
//					payInfo.setPayCode(PayCode[0]);
//					payInfo.setPayValue(0.00);
//					payInfo.setChange(0.00);
//					payInfo.setActPayValue(0.00);
//					payInfo.setOrgCode(orgCode);
//					PayEntitys.add(payInfo);
//					// Cash-现金
//					payInfo=new BillSalePayInfo();
//					payInfo.setPayCode(PayCode[1]);
//					payInfo.setPayValue(Double.parseDouble(formatMoney(et_shishouxianjin.getText().toString())));//实收
//					payInfo.setChange(zhaolinjine);
//					payInfo.setActPayValue(Double.parseDouble(formatMoney(et_shishouxianjin.getText().toString()))-zhaolinjine);//应收
//					payInfo.setOrgCode(orgCode);
//					PayEntitys.add(payInfo);
//					//CashVouc-抵用券
//					ArrayList<CcCouponCardInfo> cards=App.response.getCouponEntitys();
//					if(cards!=null){
//						int size=App.response.getCouponEntitys().size();
//						double payValue=0;
//						double actPayValue=0;
//						for(int i=0;i<size;i++){
//							CcCouponCardInfo card=App.response.getCouponEntitys().get(i);
//							if(card.getDISP_NAME().equals("会员券")){
//								payValue+=card.getTRAN_VAL();
//							}
//						}
//						
//						if(App.response.getHeadEntity().getTotalMoney()>payValue){
//							actPayValue=payValue;
//						}else{
//							actPayValue=App.response.getHeadEntity().getTotalMoney();
//						}
//						
//						payInfo=new BillSalePayInfo();
//						payInfo.setPayCode(PayCode[2]);
//						payInfo.setPayValue(payValue);
//						payInfo.setChange(0.00);
//						payInfo.setActPayValue(actPayValue);
//						payInfo.setOrgCode(orgCode);
//						PayEntitys.add(payInfo);
//					}else{
//						payInfo=new BillSalePayInfo();
//						payInfo.setPayCode(PayCode[2]);
//						payInfo.setPayValue(0.00);
//						payInfo.setChange(0.00);
//						payInfo.setActPayValue(0.00);
//						payInfo.setOrgCode(orgCode);
//						PayEntitys.add(payInfo);
//					}
//					//Voucher-代金券不参与找零
//					payInfo=new BillSalePayInfo();
//					payInfo.setPayCode(PayCode[3]);
//					payInfo.setPayValue(Double.parseDouble(formatMoney(et_daijinquan.getText().toString())));//et_daijinquan
//					payInfo.setChange(0.00);
//					if(Double.parseDouble(formatMoney(et_daijinquan.getText().toString()))>Double.parseDouble(formatMoney(et_yingfujine.getText().toString()))){
//						payInfo.setActPayValue(Double.parseDouble(formatMoney(et_yingfujine.getText().toString())));
//					}else{
//						payInfo.setActPayValue(Double.parseDouble(formatMoney(et_daijinquan.getText().toString())));
//					}
//					payInfo.setOrgCode(orgCode);
//					PayEntitys.add(payInfo);
//					App.response.setPayEntitys(PayEntitys);
//					
//					billSaleWrap.setPayEntitys(App.response.getPayEntitys());//
//					if(billSaleWrap.getHeadEntity()!=null){
//						if(billSaleWrap.getHeadEntity().getSaleBankInfos()!=null){
//							billSaleWrap.getHeadEntity().setSaleBankInfos(null);
//						}
//					}
//					
//					Commands.doCommandOffLineSave(context,billSaleWrap,new Listener<JSONObject>() {
//						
//						@Override
//						public void onResponse(JSONObject response) {
//							// TODO Auto-generated method stub
////							Log.i("tag", "response="+response.toString());
//							if (isSuccess(response)) {
//								BGFittingSaveResponse obj=mapperToObject(response, BGFittingSaveResponse.class);
//								SaleNo =obj.getSaleNo();
//								SaleID =obj.getSaleID();
//								showToast("支付成功");
//								v.setEnabled(false);
//								canPrint=true;
//								doCommandSaveBillSaleBankAppointmentMap();
//							}
//						}
//					});
//				}
//			}else{
//				showToast("输入金额不正确");
//			}
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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
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
