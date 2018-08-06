package com.app.widget;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;

/**
 * 
 * @author pythoner
 * 
 */
public class SimpleListDialog<T> extends BaseDialog {
	private ListView listView;
	private List<T> beans;
	private CommonAdapter<T> adapter;

	public SimpleListDialog(Context context, List<T> beans) {
		this(context, R.style.Theme_Dialog_NoTitle, beans);
		// TODO Auto-generated constructor stub
	}

	public SimpleListDialog(Context context, int theme, List<T> beans) {
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
		if (beans == null || beans.size() == 0) {
			showToast(R.string.toast_no_data);
			if (isShowing())
				dismiss();
			return;
		}
		listView = (ListView) findViewById(R.id.listView);

		listView.setAdapter(adapter = new CommonAdapter<T>(context, beans,
				R.layout.item_simple_list) {

			@Override
			public void setValues(ViewHolder helper, T item, int position) {
				// TODO Auto-generated method stub
				// String text=item.toString().replace(";", "\n");
				String text = item.toString();
				helper.setText(R.id.item_0, text);
			}

		});
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
				// TODO Auto-generated method stub

				T instance = beans.get(position);
				if (onItemClickListener != null) {
					onItemClickListener.onItemClick(v, instance, position);
				}

				dismiss();
			}
		});
	}

	private OnItemClickListener<T> onItemClickListener;

	public interface OnItemClickListener<T> {
		public void onItemClick(View v, T item, int position);
	}

	public void setOnItemClickListener(
			OnItemClickListener<T> onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

}
