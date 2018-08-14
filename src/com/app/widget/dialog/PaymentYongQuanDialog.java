package com.app.widget.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.app.widget.BaseDialog;
import com.app.widget.dialog.PaymentJiFenDialog.OnOkClickListener;
import com.app.xstore.R;

/**
 * 用券
 * 
 * @author pythoner
 * 
 */
public class PaymentYongQuanDialog extends BaseDialog {

	private EditText et_jine, et_code;
	private TextView tv_youxiaoqi;

	public PaymentYongQuanDialog(Context context) {
		this(context, R.style.Theme_Dialog_NoTitle);
		// TODO Auto-generated constructor stub
	}

	public PaymentYongQuanDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_payment_yongquan);
		initViews();
	}

	private void initViews() {
		et_jine = (EditText) findViewById(R.id.et_jine);
		et_code = (EditText) findViewById(R.id.et_code);
		tv_youxiaoqi = (TextView) findViewById(R.id.tv_youxiaoqi);
		tv_youxiaoqi.setText("有效期：");

		findViewById(R.id.btn_code).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String code = et_code.getText().toString().trim();
						if (code.length() == 0) {
							doShake(et_code);
							return;
						}

					}
				});

		findViewById(R.id.btn_right).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String jine = et_jine.getText().toString().trim();
						String code = et_code.getText().toString().trim();

						if (code.length() == 0) {
							doShake(et_code);
							return;
						}
						if (jine.length() == 0) {
							doShake(et_jine);
							return;
						}

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
