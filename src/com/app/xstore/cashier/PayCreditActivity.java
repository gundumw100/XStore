package com.app.xstore.cashier;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.widget.effect.text.Spanny;

/**
 * 信用卡支付
 * @author NGJ
 *
 */
public class PayCreditActivity extends BaseActivity implements OnClickListener {

	private Context context;
	private TextView tv_needPay;
	private EditText et_daijinquan,et_yingfujine;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_credit);
		context = this;
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
		double ying=getIntent().getDoubleExtra("Need", 0.00);
		yingfujine=formatMoney(ying);
		
		
		Spanny s=new Spanny();
		s.append("￥", new RelativeSizeSpan(1.0f));
		s.append(yingfujine, new RelativeSizeSpan(2.0f));
		
		tv_needPay=(TextView)findViewById(R.id.tv_needPay);
		tv_needPay.setText(s);
		
		
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
			break;

		default:
			break;
		}
	}

	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		setThemeDrawable(context, R.id.btn_ok);
	}
	
}
