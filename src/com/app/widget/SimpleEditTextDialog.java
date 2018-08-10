package com.app.widget;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.app.xstore.R;

/**
 * 
 * @author pythoner
 * 
 */
public class SimpleEditTextDialog extends BaseDialog implements
		View.OnClickListener {

	private String defaultText;
	private String hint;
	private EditText et;

	public SimpleEditTextDialog(Context context, String defaultText, String hint) {
		this(context, R.style.Theme_Dialog_NoTitle, defaultText, hint);
		// TODO Auto-generated constructor stub

	}

	public SimpleEditTextDialog(Context context, int theme, String defaultText,
			String hint) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.defaultText = defaultText;
		this.hint = hint;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_simple_edittext);
		initViews();
	}

	private void initViews() {
		et = (EditText) findViewById(R.id.et);
		et.setHint(hint);
		et.setText(defaultText);
		findViewById(R.id.btn_left).setOnClickListener(this);
		findViewById(R.id.btn_right).setOnClickListener(this);
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
		case R.id.btn_left:
			dismiss();
			break;
		case R.id.btn_right:
			String text = et.getText().toString().trim();
			if (TextUtils.isEmpty(text)) {
				doShake(et);
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
}
