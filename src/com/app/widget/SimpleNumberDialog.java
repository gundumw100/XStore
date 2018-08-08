package com.app.widget;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.app.xstore.R;

/**
 * 
 * @author pythoner
 * 
 */
public class SimpleNumberDialog extends BaseDialog implements View.OnClickListener{

	private String defaultText;
	private String hint;
	private EditText tv_scan_result;

	public SimpleNumberDialog(Context context, String defaultText, String hint) {
		this(context, R.style.Theme_Dialog_NoTitle, defaultText, hint);
		// TODO Auto-generated constructor stub
	}

	public SimpleNumberDialog(Context context, int theme, String defaultText, String hint) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.defaultText = defaultText;
		this.hint = hint;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_simple_number);
		initViews();
	}

	private void initViews() {
		tv_scan_result = (EditText) findViewById(R.id.tv_scan_result);
		tv_scan_result.setHint(hint==null?"":hint);
		tv_scan_result.setText(defaultText==null?"":defaultText);
		tv_scan_result.setFocusable(true);
		tv_scan_result.setFocusableInTouchMode(true);
		if(defaultText!=null){
			tv_scan_result.setSelected(true);
			tv_scan_result.setSelectAllOnFocus(true);
//			tv_scan_result.setSelection(defaultText.length());
		}
		tv_scan_result.requestFocus();
		findViewById(R.id.btn_cancel).setOnClickListener(this);
		findViewById(R.id.btn_ok).setOnClickListener(this);
		showKeyboard(tv_scan_result);
	}

	// 延时显示虚拟键盘
	private void showKeyboard(final View v) {
		Timer timer = new Timer();  
        timer.schedule(new TimerTask() {  

            @Override  
            public void run() {  
                InputMethodManager inManager = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE); 
                inManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
            }  
        }, 300);
        
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
		case R.id.btn_cancel:
			dismiss();
			break;
		case R.id.btn_ok:
			String text=tv_scan_result.getText().toString().trim();
			if(TextUtils.isEmpty(text)){
				doShake(tv_scan_result);
				return;
			}
			int qty=Integer.parseInt(text);
			if(qty<=0){
				showToast("数量必须大于0");
				doShake(tv_scan_result);
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
