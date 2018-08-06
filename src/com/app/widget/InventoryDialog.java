package com.app.widget;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.app.model.ProdPreChcekData;
import com.app.xstore.R;

/**
 * 
 * @author pythoner
 * 
 */
public class InventoryDialog extends ScanerBaseDialog  implements View.OnClickListener{

	private ProdPreChcekData item;
	private View btn_left;
	private TextView tv_tip;
	private String tip;
	private EditText et_serial,et_quantity;

	public InventoryDialog(Context context,String tip,ProdPreChcekData item) {
		this(context, R.style.Theme_Dialog_NoTitle, tip,item);
		// TODO Auto-generated constructor stub
	}

	public InventoryDialog(Context context, int theme, String tip,ProdPreChcekData item) {
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
			et_serial.setText(item.getShelf_code());
			et_quantity.setText(String.valueOf(item.getTotal_qty()));
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
					item=new ProdPreChcekData();
				}
				item.setShelf_code(locID);
				item.setTotal_qty(String.valueOf(Integer.parseInt(totalQty)));//去掉前面的0
				if(onClickListener!=null){
					onClickListener.onClick(v, item);
				}
//				dismiss();
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
		public void onClick(View v, ProdPreChcekData item);
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
	
}
