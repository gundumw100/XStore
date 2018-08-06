package com.app.widget;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.app.xstore.R;

/**
 * 退款
 * 
 * @author pythoner
 * 
 */
public class RefundDialog extends BaseDialog {
	private EditText et_refundFee, et_refundSubject;
	private String amt;

	public RefundDialog(Context context, String amt) {
		this(context, R.style.Theme_Dialog_NoTitle, amt);
		// TODO Auto-generated constructor stub
	}

	public RefundDialog(Context context, int theme, String amt) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.amt = amt;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_refund);
		initViews();
	}

	private void initViews() {
		et_refundFee = (EditText) findViewById(R.id.et_refundFee);
		et_refundFee.setText(amt);
		et_refundSubject = (EditText) findViewById(R.id.et_refundSubject);
		findViewById(R.id.btn_cancel).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dismiss();
					}
				});

		findViewById(R.id.btn_ok).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String refundFee = et_refundFee.getText().toString().trim();
						String refundSubject = et_refundSubject.getText().toString().trim();

						if (TextUtils.isEmpty(refundFee)|| Double.parseDouble(refundFee) == 0) {
							doShake(et_refundFee);
							return;
						}

						if (Double.parseDouble(refundFee) > Double.parseDouble(amt)) {
							doShake(et_refundFee);
							showToast("退款金额不能超过交易金额");
							return;
						}

						if(refundSubject.length()==0){
							doShake(et_refundSubject);
							return;
						}
						if (onClickListener != null) {
							onClickListener.onClick(v,Double.parseDouble(refundFee),refundSubject);
						}
						dismiss();
					}
				});

	}

	private OnClickListener onClickListener;

	public interface OnClickListener {
		public void onClick(View v, double refundFee, String refundSubject);
	}

	public void setOnClickListener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

}
