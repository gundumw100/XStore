package com.app.widget;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.app.xstore.R;

/**
 * 
 * @author pythoner
 * 
 */
public class CommentDialog extends BaseDialog {


	public CommentDialog(Context context) {
		this(context, R.style.Theme_Dialog_From_Bottom);
		// TODO Auto-generated constructor stub
	}

	public CommentDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_comment);
		initViews();
		setToBottom();
	}

	private void initViews() {
		final RatingBar rb = (RatingBar) findViewById(R.id.rb);
		final EditText et_content = (EditText) findViewById(R.id.et_content);
		findViewById(R.id.btn_ok).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String text=et_content.getText().toString().trim();
						if(TextUtils.isEmpty(text)){
							Toast.makeText(context, R.string.toast_no_data, Toast.LENGTH_SHORT).show();;
							return;
						}
						if(onClickListener!=null){
							onClickListener.onClick(v,text ,(int)rb.getRating());
						}
						dismiss();
					}
				});
	}

	private OnClickListener onClickListener;

	public interface OnClickListener {
		public void onClick(View v, String text,int rate);
	}

	public void setOnClickListener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

}
