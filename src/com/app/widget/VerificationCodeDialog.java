package com.app.widget;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.app.util.AuthCode;
import com.app.xstore.R;

/**
 * 
 * @author pythoner
 * 
 */
public class VerificationCodeDialog extends BaseDialog {

	private EditText et_code;
	private AuthCode code=new AuthCode();
	private String codeText;
	
	public VerificationCodeDialog(Context context) {
		this(context, R.style.Theme_Dialog_From_Bottom);
		// TODO Auto-generated constructor stub
	}

	public VerificationCodeDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_verification_code);
		initViews();
		setToBottom();
	}
	
	private void initViews() {
		et_code = (EditText) findViewById(R.id.et_code);
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
		findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String text = et_code.getText().toString().trim();
				if (TextUtils.isEmpty(text)) {
					doShake(et_code);
					return;
				}
				if(!codeText.equals(text)){
					doShake(et_code);
					return;
				}
				if (onClickListener != null) {
					onClickListener.onClick(v, text);
				}
				dismiss();
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

}
