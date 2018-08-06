package com.app.xstore.pandian.custom;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.app.widget.ScanerBaseDialog;
import com.app.xstore.R;

/**
 * 创建盘点单Dialog
 * @author pythoner
 * 
 */
public class PanDianDanDialog extends ScanerBaseDialog  implements View.OnClickListener{

	private CustomPanDianDan item;
	private View btn_left;
	private TextView tv_tip;
	private String tip;
	private EditText et_serial,et_quantity;

	public PanDianDanDialog(Context context,String tip,CustomPanDianDan item) {
		this(context, R.style.Theme_Dialog_NoTitle, tip,item);
		// TODO Auto-generated constructor stub
	}

	public PanDianDanDialog(Context context, int theme, String tip,CustomPanDianDan item) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.tip = tip;
		this.item = item;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_inventory);
		initViews();
	}

	private void initViews() {
		tv_tip = (TextView) findViewById(R.id.tv_tip);
		tv_tip.setText(tip);
		et_serial = (EditText) findViewById(R.id.et_serial);
		et_quantity = (EditText) findViewById(R.id.et_quantity);
		if(item!=null){
			et_serial.setText(item.getShelf());
			et_quantity.setText(String.valueOf(item.getYds()));
		}
		findViewById(R.id.iv_scan).setOnClickListener(this);
		btn_left=findViewById(R.id.btn_left);
		btn_left.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String locID=et_serial.getText().toString().trim();
				String totalQty=et_quantity.getText().toString().trim();
				if(TextUtils.isEmpty(locID)){
					doShake(et_serial);
					return;
				}
				if(TextUtils.isEmpty(totalQty)){
					doShake(et_quantity);
					return;
				}
				if(item==null){
					item=new CustomPanDianDan();
				}
				item.setShelf(locID);
				item.setYds(Integer.parseInt(totalQty));//去掉前面的0
				if(onClickListener!=null){
					onClickListener.onClick(v, item);
				}
				dismiss();
			}
		});
		findViewById(R.id.btn_right).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
	}

	private OnClickListener onClickListener;

	public interface OnClickListener {
		public void onClick(View v, CustomPanDianDan item);
	}

	public void setOnClickListener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_scan:
			context.setResultHandler(context.resultHandler);
			doScan(context.resultHandler);
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onScanProductHandleMessage(String prodID){
		et_serial.setText(prodID);
	}

}
