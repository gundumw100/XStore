package com.app.xstore.shangpindangan;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.app.widget.BaseDialog;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;

/**
 * 
 * @author pythoner
 * 
 */
public class SimpleTextListDialog<T> extends BaseDialog implements View.OnClickListener {

	public static final int COL_ONE=1;
	public static final int COL_TWO=2;
	private String defaultText;
	private String hint;
	private EditText tv_result;
	private ListView listView;
	private CommonAdapter<T> adapter;
	private List<T> beans=null;
	private int colNum=1;

	public SimpleTextListDialog(Context context, String defaultText,String hint, List<T> beans) {
		this(context, R.style.Theme_Dialog_NoTitle, defaultText, hint, beans,1);
		// TODO Auto-generated constructor stub
	}
	public SimpleTextListDialog(Context context, String defaultText,String hint, List<T> beans,int colNum) {
		this(context, R.style.Theme_Dialog_NoTitle, defaultText, hint, beans,colNum);
		// TODO Auto-generated constructor stub
	}

	public SimpleTextListDialog(Context context, int theme, String defaultText,String hint, List<T> beans,int colNum) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.defaultText = defaultText;
		this.hint = hint;
		this.beans = beans;
		this.colNum = colNum;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_simple_text_list);
		if(beans==null){
			return;
		}
		initViews();
	}

	private void initViews() {
		tv_result = (EditText) findViewById(R.id.tv_result);
		tv_result.setHint(hint);
		tv_result.setText(defaultText);
//		tv_result.requestFocus();//聚焦并自动弹出softkeyboard
		listView = (ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if(onItemClickListener!=null){
					onItemClickListener.onItemClick(beans.get(position), position);
				}
				dismiss();
			}
		});
		
		findViewById(R.id.btn_cancel).setOnClickListener(this);
		findViewById(R.id.btn_ok).setOnClickListener(this);
		notifyDataSetChanged();
	}
	
	private OnClickListener onClickListener;

	public interface OnClickListener {
		public void onClick(View v, String text);
	}

	public void setOnClickListener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	private OnItemClickListener<T> onItemClickListener;
	public interface OnItemClickListener<T> {
		public void onItemClick(T bean,int position);
	}
	
	public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}
	
	private OnCheckedChangeListener<T> onCheckedChangeListener;
	public interface OnCheckedChangeListener<T> {
		public void onCheckedChange(T bean,int position,boolean isChecked);
	}
	
	public void setOnCheckedChangeListener(OnCheckedChangeListener<T> onCheckedChangeListener) {
		this.onCheckedChangeListener = onCheckedChangeListener;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_cancel:
			dismiss();
			break;
		case R.id.btn_ok:
			String text=tv_result.getText().toString().trim();
			if(text.length()==0){
				doShake(tv_result);
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
	
	private void notifyDataSetChanged(){
		if(adapter==null){
			listView.setAdapter(adapter = new CommonAdapter<T>( context, beans,
					  R.layout.item_simple_text_list_2){
					  
					@Override
					public void setValues(ViewHolder helper, final T item, final int position) {
						// TODO Auto-generated method stub
						helper.setText(R.id.item_0, item.toString());
						if(colNum==COL_TWO){
							TextView item_1=helper.getView(R.id.item_1);
							item_1.setVisibility(View.VISIBLE);
							if(item instanceof ProdColor){
								ProdColor spec=(ProdColor)item;
								item_1.setText(spec.getColorCode());
							}else if(item instanceof ProdSpec){
								ProdSpec spec=(ProdSpec)item;
								item_1.setText(spec.getSpecCode());
							}
						}
						
						final com.app.widget.SwitchButton item_2=helper.getView(R.id.item_2);
						if(item instanceof ProdCommon){
							ProdCommon p=(ProdCommon)item;
							item_2.setChecked("1".equals(p.getEnabled()));
						}
						item_2.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								if(onCheckedChangeListener!=null){
									onCheckedChangeListener.onCheckedChange(item, position, item_2.isChecked());
								}
							}
						});
					}
			});
		}else{
			adapter.notifyDataSetChanged();
		}
	}
}
