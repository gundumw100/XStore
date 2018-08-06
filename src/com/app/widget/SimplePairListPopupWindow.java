package com.app.widget;

import java.util.ArrayList;

import com.app.xstore.App;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;


/**
 * 
 * @author pythoner
 * 
 */
public class SimplePairListPopupWindow<F,S> extends PopupWindow {

	private Context context;
	private CommonAdapter<Pair<F,S>> adapter;
	private ArrayList<Pair<F,S>> beans;
	public SimplePairListPopupWindow(Context context, View view, int width,ArrayList<Pair<F,S>> b) {
		super(view, width, LayoutParams.WRAP_CONTENT, true);
		this.context = context;
		this.beans = b;
		this.setBackgroundDrawable(App.res.getDrawable(R.drawable.bg_popupwindow));
		this.setOutsideTouchable(true);
		this.setAnimationStyle(android.R.style.Animation_Dialog);
		// this.update();
		// this.setTouchable(true);
		// this.setFocusable(false);

		ListView listView = (ListView) view.findViewById(R.id.listView);
		listView.setAdapter(adapter = new CommonAdapter<Pair<F,S>>(context, beans,R.layout.item_for_popupwindow_simple) {
			@Override
			public void setValues(ViewHolder helper, Pair<F,S> item, int position) {
				helper.setText(R.id.item_0, item.second.toString());
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

	public interface OnItemClickListener<F,S> {
		void onItemClick(int position, Pair<F,S> pair);
	}

	OnItemClickListener<F,S> onItemClickListener;

	public void setOnItemClickListener(OnItemClickListener<F,S> onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

}
