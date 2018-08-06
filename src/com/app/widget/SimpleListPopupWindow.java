package com.app.widget;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.app.xstore.App;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;


/**
 * 
 * @author pythoner
 * 
 */
public class SimpleListPopupWindow<T> extends PopupWindow {

	private Context context;
	private CommonAdapter<T> adapter;
	private List<T> beans;
	private int type;
	public SimpleListPopupWindow(Context context, View view, int width,List<T> b) {
		this(context, view, width, b,0);
	}
	public SimpleListPopupWindow(Context context, View view, int width,List<T> b,final int type) {
		super(view, width, LayoutParams.WRAP_CONTENT, true);
		this.context = context;
		this.beans = b;
		this.type = type;
		this.setBackgroundDrawable(App.res.getDrawable(R.drawable.bg_popupwindow));
		this.setOutsideTouchable(true);
		this.setAnimationStyle(android.R.style.Animation_Dialog);
		// this.update();
		// this.setTouchable(true);
		// this.setFocusable(false);

		ListView listView = (ListView) view.findViewById(R.id.listView);
		listView.setAdapter(adapter = new CommonAdapter<T>(context, beans,R.layout.item_for_popupwindow_simple) {
			@Override
			public void setValues(ViewHolder helper, T item, int position) {
				helper.setText(R.id.item_0, item.toString());
			}
		});

		listView.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> pearnt, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				dismiss();
				if (onItemClickListener != null) {
					onItemClickListener.onItemClick(position,beans.get(position));
				}
			}
		});

	}

	public interface OnItemClickListener<T> {
		void onItemClick(int position, T item);
	}

	OnItemClickListener<T> onItemClickListener;

	public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

}
