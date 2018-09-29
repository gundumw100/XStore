package com.app.widget.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.app.model.Member;
import com.app.widget.BaseDialog;
import com.app.xstore.R;

/**
 * 积分抵现
 * @author pythoner
 * 
 */
public class PaymentJiFenDialog extends BaseDialog {

	private TextView tv_jifen;
	private EditText et_jifen;
	private TextView tv_dixian;
	private Member member;
	
	public PaymentJiFenDialog(Context context,Member member) {
		this(context, R.style.Theme_Dialog_NoTitle,member);
		// TODO Auto-generated constructor stub
	}

	public PaymentJiFenDialog(Context context, int theme,Member member) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.member=member;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_payment_jifen);
		initViews();
	}

	private void initViews() {
		if(member==null){
			return;
		}
		tv_jifen = (TextView) findViewById(R.id.tv_jifen);
		tv_jifen.setText("可用积分："+member.getTotalPoints());
		tv_dixian = (TextView) findViewById(R.id.tv_dixian);
		et_jifen = (EditText) findViewById(R.id.et_jifen);
		et_jifen.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(s.length()>0){
					float jifenJine=Integer.parseInt(s.toString())/100.00f;
					tv_dixian.setText("抵现"+jifenJine+"元");
				}else{
					tv_dixian.setText("抵现0元");
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		findViewById(R.id.btn_left).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dismiss();
					}
				});
		findViewById(R.id.btn_right).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String text = et_jifen.getText().toString().trim();
						if (text.length() > 0) {
							doShake(et_jifen);
							return;
						}
						int points=Integer.parseInt(text);
//						int totalPoints=Integer.parseInt(member.getTotalPoints());
						int totalPoints=member.getTotalPoints();
						if(points>totalPoints){
							showToast("超过可用积分");
							doShake(et_jifen);
							return;
						}
						
						if (onOkClickListener != null) {
							onOkClickListener.onOkClick(v, points);
						}
						dismiss();
					}
				});
	}

	private OnOkClickListener onOkClickListener;

	public interface OnOkClickListener {
		public void onOkClick(View v, int points);
	}

	public void setOnOkClickListener(OnOkClickListener onOkClickListener) {
		this.onOkClickListener = onOkClickListener;
	}


}
