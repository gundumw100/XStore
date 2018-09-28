package com.app.xstore.cashier;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.widget.effect.text.Spanny;

/**
 * 现金支付
 * @author NGJ
 *
 */
public class PayCrashActivity extends BaseActivity implements OnClickListener {

	private Context context;
	private TextView tv_needPay,tv_cutMoney;
	private EditText et_shishouxianjin,et_daijinquan,et_yingfujine,et_shifujine,et_zhaolinjine;
	private Button btn_ok;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_crash);
		context = this;
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
		ying=getIntent().getDoubleExtra("Need", 0.00);
		ying=Math.floor(ying);//尚需支付金额,取整
		
		Spanny s=new Spanny();
		s.append("￥", new RelativeSizeSpan(1.0f));
		s.append(formatMoney(ying), new RelativeSizeSpan(2.0f));
		
		tv_needPay=(TextView)findViewById(R.id.tv_needPay);//尚需支付
		tv_needPay.setText(s);
		
		tv_cutMoney=(TextView)findViewById(R.id.tv_cutMoney);//抹零
		tv_cutMoney.setText("￥"+formatMoney(getIntent().getDoubleExtra("Need", 0.00)-ying));
		
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
