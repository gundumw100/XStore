package com.app.widget.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.app.widget.BaseDialog;
import com.app.xstore.R;
import com.widget.colorpickerview.ColorListener;
import com.widget.colorpickerview.ColorPickerView;

/**
 * 
 * @author pythoner
 * 
 */
public class ColorPickerDialog extends BaseDialog {


	public ColorPickerDialog(Context context) {
		this(context, R.style.Theme_Dialog_NoTitle);
		// TODO Auto-generated constructor stub
	}
	
	public ColorPickerDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_color_picker);
		initViews();
	}

	private void initViews() {
		final TextView tv_title=(TextView)findViewById(R.id.tv_title);
		final ColorPickerView colorPickerView=(ColorPickerView)findViewById(R.id.colorPickerView);
		colorPickerView.setColorListener(new ColorListener() {
            @Override
            public void onColorSelected(int color, boolean fromUser) {
            	tv_title.setBackgroundColor(color);
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
				if(onOkClickListener!=null){
					onOkClickListener.onColor(colorPickerView.getColor());
				}
				dismiss();
			}
		});
		
	}
	
	private OnOkClickListener onOkClickListener;
	
	public void setOnOkClickListener(OnOkClickListener onOkClickListener) {
		this.onOkClickListener = onOkClickListener;
	}

	public interface OnOkClickListener{
		public void onColor(int color);
	}
	
}

