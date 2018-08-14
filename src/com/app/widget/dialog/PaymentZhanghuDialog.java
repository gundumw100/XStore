package com.app.widget.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.util.AuthCode;
import com.app.widget.BaseDialog;
import com.app.widget.dialog.PaymentJiFenDialog.OnOkClickListener;
import com.app.xstore.R;

/**
 * 账户
 * 
 * @author pythoner
 * 
 */
public class PaymentZhanghuDialog extends BaseDialog {

	private AuthCode code=new AuthCode();
	private String codeText;
	private EditText et_jine, et_code;
	private TextView tv_yue;

	public PaymentZhanghuDialog(Context context) {
		this(context, R.style.Theme_Dialog_NoTitle);
		// TODO Auto-generated constructor stub
	}

	public PaymentZhanghuDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_payment_zhanghu);
		initViews();
	}

	private void initViews() {
		et_jine = (EditText) findViewById(R.id.et_jine);
		et_code = (EditText) findViewById(R.id.et_code);
		tv_yue = (TextView) findViewById(R.id.tv_yue);
		
		final ImageView iv_code = (ImageView) findViewById(R.id.iv_code);
		codeText=code.genAuthCode(4);
		iv_code.setImageDrawable(code.createAuthCode(codeText));
		iv_code.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				codeText=code.genAuthCode(4);
				iv_code.setImageDrawable(code.createAuthCode(codeText));
			}
		});

		findViewById(R.id.btn_right).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						

						dismiss();
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
	}

	private OnOkClickListener onOkClickListener;

	public interface OnOkClickListener {
		public void onOkClick(View v, float jifenJine);
	}

	public void setOnOkClickListener(OnOkClickListener onOkClickListener) {
		this.onOkClickListener = onOkClickListener;
	}


}
