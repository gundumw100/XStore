package com.app.widget;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.app.xstore.R;

/**
 * 
 * @author pythoner
 * 
 */
public class SimpleTextDialog extends ScanerBaseDialog implements View.OnClickListener {

	private String defaultText;
	private String hint;
	private EditText tv_result;

	public SimpleTextDialog(Context context, String defaultText, String hint) {
		this(context, R.style.Theme_Dialog_NoTitle, defaultText, hint);
		// TODO Auto-generated constructor stub
	}

	public SimpleTextDialog(Context context, int theme, String defaultText, String hint) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.defaultText = defaultText;
		this.hint = hint;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_simple_text);
		initViews();
	}

	private void initViews() {
		tv_result = (EditText) findViewById(R.id.tv_result);
		tv_result.setHint(hint);
		tv_result.setText(defaultText);
		tv_result.requestFocus();//聚焦并自动弹出softkeyboard
		
		findViewById(R.id.iv_scan).setOnClickListener(this);
		findViewById(R.id.btn_cancel).setOnClickListener(this);
		findViewById(R.id.btn_ok).setOnClickListener(this);
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
		case R.id.btn_cancel:
			dismiss();
			break;
		case R.id.btn_ok:
			String text=tv_result.getText().toString().trim();
			if(text.length()==0){
				doShake(tv_result);
				return;
			}
			if (onClickListener != null) {
				onClickListener.onClick(v, text);
			}
			dismiss();
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onScanProductHandleMessage(String prodID) {
		// TODO Auto-generated method stub
		tv_result.setText(prodID);
	}
}
