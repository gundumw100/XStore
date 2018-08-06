package com.app.widget;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.app.model.GuaDan;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;

/**
 * 解单Dialog
 * @author pythoner
 * 
 */
public class JieDanListDialog extends BaseDialog {
	private ListView listView;
	private List<GuaDan> beans;
	private CommonAdapter<GuaDan> adapter;

	public JieDanListDialog(Context context, List<GuaDan> beans) {
		this(context, R.style.Theme_Dialog_NoTitle, beans);
		// TODO Auto-generated constructor stub
	}

	public JieDanListDialog(Context context, int theme, List<GuaDan> beans) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.beans = beans;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_simple_list);
		initViews();
	}

	private void initViews() {
		listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(adapter = new CommonAdapter<GuaDan>(context, beans,
				R.layout.item_simple_list_3) {

			@Override
			public void setValues(ViewHolder helper, GuaDan item, int position) {
				// TODO Auto-generated method stub
				helper.setText(R.id.item_0, item.getCreateTime());
				helper.setText(R.id.item_1, item.getQty()+"件");
				helper.setText(R.id.item_2, "￥ "+item.getMoney());
			}

		});
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
				// TODO Auto-generated method stub
				GuaDan instance = beans.get(position);
				if (onItemClickListener != null) {
					onItemClickListener.onItemClick(v, instance, position);
				}
				dismiss();
			}
		});
	}

	private OnItemClickListener onItemClickListener;

	public interface OnItemClickListener {
		public void onItemClick(View v, GuaDan item, int position);
	}

	public void setOnItemClickListener(
			OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

}
