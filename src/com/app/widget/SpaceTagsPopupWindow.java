package com.app.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.app.xstore.App;
import com.app.xstore.R;
import com.base.model.KeyValue;
import com.widget.flowlayout.FlowLayout;
import com.widget.flowlayout.TagAdapter;
import com.widget.flowlayout.TagFlowLayout;
import com.widget.view.DatePickerDialog;


/**
 * 
 * @author pythoner
 * 
 */
public class SpaceTagsPopupWindow extends PopupWindow implements View.OnClickListener{

	private Context context;
	private ArrayList<KeyValue> orders,category;
	private TagFlowLayout tagsOrders,tagsCategory;
	private TextView btn_startDate,btn_endDate;
	public SpaceTagsPopupWindow(Context context, View view, int width) {
		super(view, width, LayoutParams.WRAP_CONTENT, true);
		this.context = context;
		this.setBackgroundDrawable(App.res.getDrawable(R.drawable.bg_popupwindow));
		this.setOutsideTouchable(true);
		this.setAnimationStyle(android.R.style.Animation_Dialog);
		// this.update();
		// this.setTouchable(true);
		// this.setFocusable(false);
		btn_startDate=(TextView)view.findViewById(R.id.btn_startDate);
		btn_startDate.setOnClickListener(this);
		btn_endDate=(TextView)view.findViewById(R.id.btn_endDate);
		btn_endDate.setOnClickListener(this);
		view.findViewById(R.id.btn_cancel).setOnClickListener(this);;
		view.findViewById(R.id.btn_ok).setOnClickListener(this);;
		
		final LayoutInflater mInflater = LayoutInflater.from(context);
        
        orders=new ArrayList<KeyValue>();
        orders.add(new KeyValue("ASC","按时间升序"));
        orders.add(new KeyValue("DESC","按时间降序"));
		
        tagsOrders=(TagFlowLayout)view.findViewById(R.id.tagsOrders);
        tagsOrders.setAdapter(new TagAdapter<KeyValue>(orders)
		{
			@Override
			public View getView(FlowLayout parent, int position, KeyValue item)
			{
				TextView tv = (TextView) mInflater.inflate(R.layout.item_text,tagsOrders, false);
				tv.setText(item.getValue());
				return tv;
			}
		});
        
        category=new ArrayList<KeyValue>();
        category.add(new KeyValue("1000","全部"));
        category.add(new KeyValue("0100","PPT"));
        category.add(new KeyValue("0010","视频"));
        category.add(new KeyValue("0001","文本"));
        
        tagsCategory=(TagFlowLayout)view.findViewById(R.id.tagsCategory);
        tagsCategory.setAdapter(new TagAdapter<KeyValue>(category)
		{
			@Override
			public View getView(FlowLayout parent, int position, KeyValue item)
			{
				TextView tv = (TextView) mInflater.inflate(R.layout.item_text,tagsCategory, false);
				tv.setText(item.getValue());
				return tv;
			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_startDate:
			showDialog(btn_startDate);
			break;
		case R.id.btn_endDate:
			showDialog(btn_endDate);
			break;
		case R.id.btn_ok:
			List<KeyValue> orders=tagsOrders.getCheckedItems();
			List<KeyValue> categorys=tagsCategory.getCheckedItems();
			String startDate=btn_startDate.getText().toString();
			String endDate=btn_endDate.getText().toString();
			if(onOkClickListener!=null){
				onOkClickListener.onOkClick(startDate,endDate,orders, categorys);
			}
			dismiss();
			break;
		case R.id.btn_cancel:
			dismiss();
			break;

		default:
			break;
		}
	}

	private void showDialog(final TextView tv){
		DatePickerDialog dialog = new DatePickerDialog(context, R.style.Theme_Dialog, tv.getText().toString().trim());
        dialog.setOnButtonClickListener(new DatePickerDialog.OnButtonClickListener() {
			
			@Override
			public void onOKClick(String year, String month, String date) {
				// TODO Auto-generated method stub
				tv.setText(year + "-" + month + "-" + date);
			}
			
			@Override
			public void onCancelClick() {
				// TODO Auto-generated method stub
				tv.setText("");
			}
		});
		dialog.show();
	}
	
	public interface OnOkClickListener {
		void onOkClick(String startDate,String endDate,List<KeyValue> orders,List<KeyValue> categorys);
	}

	private OnOkClickListener onOkClickListener;

	public void setOnOkClickListener(OnOkClickListener onOkClickListener) {
		this.onOkClickListener = onOkClickListener;
	}

}
