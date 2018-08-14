package com.app.widget.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.app.widget.ScanerBaseDialog;
import com.app.xstore.R;

/**
 * 
 * @author pythoner
 * 
 */
public class ProductCodeDialog extends ScanerBaseDialog implements
		View.OnClickListener {

	private String defaultText;
	private View btn_left;
	private TextView tv_tip;
	private String tip;
	private EditText tv_scan_result;

	public ProductCodeDialog(Context context, String defaultText, String tip) {
		this(context, R.style.Theme_Dialog_NoTitle, defaultText, tip);
		// TODO Auto-generated constructor stub
	}

	public ProductCodeDialog(Context context, int theme, String defaultText,
			String tip) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.defaultText = defaultText;
		this.tip = tip;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_product_id);
		initViews();
	}

	private void initViews() {
		tv_tip = (TextView) findViewById(R.id.tv_tip);
		tv_tip.setText(tip);
		tv_scan_result = (EditText) findViewById(R.id.tv_scan_result);
		tv_scan_result.setText(defaultText);
		findViewById(R.id.iv_scan).setOnClickListener(this);
		btn_left = findViewById(R.id.btn_left);
		btn_left.setOnClickListener(new View.OnClickListener() {

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

						String text = tv_scan_result.getText().toString()
								.trim();
						if (text.length() >= 6) {
							if (onClickListener != null) {
								onClickListener.onClick(v, text);
							}
							dismiss();
						} else {
							doShake(tv_scan_result);
						}
					}
				});
	}

	private OnClickListener onClickListener;

	public interface OnClickListener {
		public void onClick(View v, String text);
	}

	public void setOnClickListener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_scan:
			doScan(context.resultHandler);
			break;

		default:
			break;
		}
	}

	@Override
	public void onScanProductHandleMessage(String prodID) {
		tv_scan_result.setText(prodID);
		btn_left.performClick();
	}

}
