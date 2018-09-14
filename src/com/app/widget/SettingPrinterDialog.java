package com.app.widget;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.app.model.Printer;
import com.app.xstore.R;

/**
 * 
 * @author pythoner
 * 
 */
public class SettingPrinterDialog extends BaseDialog {
	private Printer printer;
	private EditText et_name, et_ip;

	public SettingPrinterDialog(Context context, Printer printer) {
		this(context, R.style.Theme_Dialog_NoTitle, printer);
		// TODO Auto-generated constructor stub
	}

	public SettingPrinterDialog(Context context, int theme, Printer printer) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.printer = printer;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_setting_printer);
		initViews();
	}

	private void initViews() {
		et_name = (EditText) findViewById(R.id.et_name);
		et_ip = (EditText) findViewById(R.id.et_ip);
		final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		if (printer != null) {
			et_name.setText(printer.getDescription());
			et_ip.setText(printer.getPrinterip());

			if (printer.getType() == 0) {
				radioGroup.check(R.id.rb_0);
			} else if (printer.getType() == 1) {
				radioGroup.check(R.id.rb_1);
			}
		}

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
						String name = et_name.getText().toString().trim();
						String ip = et_ip.getText().toString().trim();
						int type = -1;
						if (radioGroup.getCheckedRadioButtonId() == R.id.rb_0) {
							type = 0;
						} else if (radioGroup.getCheckedRadioButtonId() == R.id.rb_1) {
							type = 1;
						}
						if (TextUtils.isEmpty(name)) {
							showToast(R.string.tip_printer_name);
							return;
						}
						if (TextUtils.isEmpty(ip)) {
							showToast(R.string.tip_printer_ip);
							return;
						}
						if (type == -1) {
							showToast(R.string.tip_printer_type);
							return;
						}
						if (onClickListener != null) {
							onClickListener.onClick(v, name, ip, type);
						}
						dismiss();
					}
				});

	}

	private OnClickListener onClickListener;

	public interface OnClickListener {
		public void onClick(View v, String name, String ip, int type);
	}

	public void setOnClickListener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

}
