package com.app.widget.dialog;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.app.xstore.App;
import com.app.xstore.R;
import com.app.xstore.shangpindangan.ProdColor;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;


/**
 * 
 * @author pythoner
 * 
 */
public class ColorListPopupWindow extends PopupWindow {

	private Context context;
	private ListView listView;
	private CommonAdapter<ProdColor> adapter;
	private List<ProdColor> beans;
	
	public ColorListPopupWindow(Context c, View view, int width,List<ProdColor> b) {
		super(view, width, LayoutParams.WRAP_CONTENT, true);
		this.context = c;
		this.beans = b;
		this.setBackgroundDrawable(App.res.getDrawable(R.drawable.bg_popupwindow));
		this.setOutsideTouchable(true);
		this.setAnimationStyle(android.R.style.Animation_Dialog);
		// this.update();
		// this.setTouchable(true);
		// this.setFocusable(false);

		listView = (ListView) view.findViewById(R.id.listView);
		listView.setAdapter(adapter = new CommonAdapter<ProdColor>(context, beans,R.layout.item_for_popupwindow_simple) {
			@Override
			public void setValues(ViewHolder helper, ProdColor item, int position) {
				TextView item_0=helper.getView(R.id.item_0);
				item_0.setText(item.getDescription());
				
				if(item.getBackgroundColor()==0){
					item_0.setBackgroundColor(Color.TRANSPARENT);
					item_0.setTextColor(ContextCompat.getColor(context, R.color.grayDark));
				}else{
					item_0.setBackgroundColor(item.getBackgroundColor());
					item_0.setTextColor(reverseColor(item.getBackgroundColor()));
				}
			}
		});

		listView.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> pearnt, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (onItemClickListener != null) {
					onItemClickListener.onItemClick(position,beans.get(position));
				}
				dismiss();
			}
		});

	}

	private int reverseColor(int color){
		int red=Color.red(color);
		int green=Color.green(color);
		int blue=Color.blue(color);
		int reverseColor=Color.rgb(255-red, 255-green, 255-blue);
		return reverseColor;
	}
	
	public interface OnItemClickListener {
		void onItemClick(int position, ProdColor item);
	}

	OnItemClickListener onItemClickListener;

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

}
