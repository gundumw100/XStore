package com.app.widget.dialog;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.model.Printer;
import com.app.widget.BaseDialog;
import com.app.widget.SimpleListPopupWindow;
import com.app.widget.SimpleListPopupWindow.OnItemClickListener;
import com.app.xstore.R;

/**
 * 
 * @author pythoner
 * 
 */
public class SettingPrinterDialog extends BaseDialog {
	
	private TextView btn_type;
	private Printer printer;
	private EditText et_name, et_ip,et_port;
	private int type = -1;
	private List<String> list=new ArrayList<String>();
	
	
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
		list.add("DZ");
		list.add("T90");
		list.add("SPRT");
		list.add("Xprinter");
//		list.add("POSTEC");
		initViews();
	}

	private void initViews() {
		btn_type = (TextView)findViewById(R.id.btn_type);
		btn_type.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPopWindow(v);
			}
		});
		
		et_name = (EditText) findViewById(R.id.et_name);
		et_ip = (EditText) findViewById(R.id.et_ip);
		et_port = (EditText) findViewById(R.id.et_port);
		if (printer != null) {
			btn_type.setText(list.get(printer.getType()));
			
			et_name.setText(printer.getDescription());
			String[] s=printer.getPrinterip().split(":");
			et_ip.setText(s[0]);
			et_port.setText(s[1]);
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
						if (type == -1) {
							showToast(R.string.tip_printer_type);
							return;
						}
						
						String name = et_name.getText().toString().trim();
						String ip = et_ip.getText().toString().trim();
						String port = et_port.getText().toString().trim();
						
						if (TextUtils.isEmpty(name)) {
							showToast(R.string.tip_printer_name);
							return;
						}
						if (TextUtils.isEmpty(ip)) {
							showToast(R.string.tip_printer_ip);
							return;
						}
						if (TextUtils.isEmpty(port)) {
							port="9100";
						}
						
						ip=ip+":"+port;
						if (onClickListener != null) {
							onClickListener.onClick(v, name, ip, type);
						}
						dismiss();
					}
				});

	}

	private void showPopWindow(View v){
		View view=LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
		SimpleListPopupWindow<String> slpw=new SimpleListPopupWindow<String>(context, view, v.getWidth(), list);
		slpw.showAsDropDown(v, 0, 0);
		slpw.setOnItemClickListener(new OnItemClickListener<String>() {

			@Override
			public void onItemClick(int position, String item) {
				// TODO Auto-generated method stub
				type=position;
				btn_type.setText(list.get(position));
				et_name.setText(list.get(position));
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
