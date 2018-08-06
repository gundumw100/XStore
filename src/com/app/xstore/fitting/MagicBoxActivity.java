package com.app.xstore.fitting;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.app.model.MenuItem;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.widget.common.recycler.BaseRecyclerAdapter;
import com.widget.common.recycler.SpacesItemDecoration;
import com.widget.common.recycler.ViewHolder;

public class MagicBoxActivity extends BaseActivity {

	private Context context;
	private RecyclerView recyclerView;
	private BaseRecyclerAdapter<MenuItem> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_magic_box);
		context = this;
		initActionBar("百宝箱", null, null);
		initViews();
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
		recyclerView.setHasFixedSize(true);
//		 recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
		recyclerView.addItemDecoration(new SpacesItemDecoration(16));
		// 设置item动画
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		LinearLayoutManager manager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
//		GridLayoutManager manager = new GridLayoutManager(this, 2);
		recyclerView.setLayoutManager(manager);

		final ArrayList<MenuItem> list = new ArrayList<MenuItem>();
		list.add(new MenuItem(0, R.drawable.icon_main_fitting, "试衣采集",null));
		list.add(new MenuItem(1, R.drawable.icon_main_pandian, "简单盘点",null));
		list.add(new MenuItem(2, R.drawable.icon_main_shopping, "打包装箱",null));
		list.add(new MenuItem(3, R.drawable.icon_main_tool, "打印标签",null));
		list.add(new MenuItem(4, R.drawable.icon_main_tool, "无线扫描枪",null));

		recyclerView.setAdapter(adapter = new BaseRecyclerAdapter<MenuItem>(context, list) {

			@Override
			public int onCreateViewLayoutID(int viewType) {
				// TODO Auto-generated method stub
				return R.layout.item_for_magic_box;
			}

			@Override
			public void onBindViewHolder(ViewHolder holder, MenuItem item, int position) {
				// TODO Auto-generated method stub
				holder.setText(R.id.item_0, item.text);
			}
		});

		adapter.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = null;
				switch (list.get(position).id) {
				case 0:
					intent=new Intent(context,FittingActivity.class);
					startActivity(intent);
					break;
				case 1:
					intent=new Intent(context,SimpleDocumentListActivity.class);
					startActivity(intent);
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					break;
				default:
					break;
				}

			}
		});

	}

	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
//		if (adapter != null) {
//			adapter.notifyDataSetChanged();
//		}
	}

}
