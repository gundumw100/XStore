package com.app.xstore.cashier;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.model.Discount;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;

/**
 * 单件折扣
 * @author NGJ
 *
 */
public class DiscountActivity extends BaseActivity implements OnClickListener{

	private Context context;
	private Spinner discountType;
	private EditText discountValue;
	private TextView tv_rate,tv_p;
	private boolean wholeOrder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_discount);
		context=this;
		initViews();
		wholeOrder=getIntent().getBooleanExtra("WholeOrder", false);
		initActionBar(wholeOrder?"整单折扣":"单件折扣",null,null);
	}
	@Override
	public void initViews() {
		findViewById(R.id.btn_comfirm).setOnClickListener(this);
		tv_rate=(TextView)findViewById(R.id.tv_rate);
		tv_p=(TextView)findViewById(R.id.tv_p);
		
		discountType=(Spinner)findViewById(R.id.discountType);
		discountType.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View v,
					int position, long id) {
				// TODO Auto-generated method stub
				String r=(String)discountType.getSelectedItem()+":";
				tv_rate.setText(r);
				tv_p.setVisibility(r.contains("率")?View.VISIBLE:View.GONE);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
		
		discountValue=(EditText)findViewById(R.id.discountValue);
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_comfirm:
			String text=discountValue.getText().toString().trim();
			if(text.length()==0){
				doShake(context, discountValue);
				return;
			}
			double value=Double.parseDouble(text);
			value=Double.parseDouble(formatMoney(value));
			
			Discount discountWrap=new Discount();
			if(discountType.getSelectedItemPosition()==0){//折扣率
				discountWrap.setMode(0);//DiscountMode.DiscountRate
				if(value>100){
					showToast(R.string.require_discount_max_rate);
					return;
				}
			}
			else if(discountType.getSelectedItemPosition()==1){//折扣额
				discountWrap.setMode(1);//DiscountMode.DiscountPrice
			}
			discountWrap.setDiscountValue(value);
//			if(discountOptions.getSelectedItemPosition()==0){
//				discountWrap.setDisOnBillPrice(true);//使用当前价,RetailPrice
//			}else{
//				discountWrap.setDisOnBillPrice(false);//使用当前价,RetailPrice
//			}
			discountWrap.setWholeOrder(wholeOrder);//整单打折
			
			Intent intent=new Intent();
			intent.putExtra("DiscountWrap", discountWrap);
			setResult(1,intent);
			finish();
			break;
		}
	}
	
	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		updateTheme();
	}
	
	private void updateTheme() {
		setThemeDrawable(context, R.id.btn_comfirm);
	}
	
}
