package com.app.xstore.caigoukanhuo;

import java.util.ArrayList;
import java.util.List;

import org.simple.eventbus.Subscriber;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.app.xstore.pandian.PanDianDan;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;

/**
 * 采购看货列表
 * 
 * @author Ni Guijun
 * 
 */
public class CaiGouKanHuoActivity extends BaseActivity {

	private ListView listView;
	private CommonAdapter<PanDianDan> adapter;
	private List<PanDianDan> beans=new ArrayList<PanDianDan>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_caigoukanhuo);
		initActionBar("采购看货", "新增", null);
		beans.add(new PanDianDan());
		beans.add(new PanDianDan());
		beans.add(new PanDianDan());
		initViews();
		updateViews(beans);
	}

	@Override
	public void initViews() {
		listView=$(R.id.listView);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
			}
		});
	}

	@Override
	public void doRightButtonClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=new Intent(context,CaiGouKanHuoCreateActivity.class);
		startActivity(intent);
	}
	
	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
		notifyDataSetChanged();
	}

	private void notifyDataSetChanged(){
		if(adapter==null){
			listView.setAdapter(adapter = new CommonAdapter<PanDianDan>( context, beans,
					  R.layout.item_pandian_list){
					  
					@Override
					public void setValues(ViewHolder helper, final PanDianDan item, final int position) {
						// TODO Auto-generated method stub
						helper.setText(R.id.item_0, "图片");
						helper.setText(R.id.item_1, "类别");
						helper.setText(R.id.item_2, "描述");
						helper.setText(R.id.item_3, "报价起订");
						
					}
			});
		}else{
			adapter.notifyDataSetChanged();
		}
	}
	@Subscriber
	void updateByEventBus(String event) {
		if (event.equals(App.EVENT_FINISH)) {
		}
	}

}