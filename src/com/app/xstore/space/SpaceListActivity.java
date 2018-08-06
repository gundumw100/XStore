package com.app.xstore.space;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;

import com.android.volley.Response.Listener;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;
import com.base.model.KeyValue;
import com.app.model.ClerkSpaceInfo;
import com.app.model.JvclerkspaceInfo;
import com.app.model.response.GetClerkSpaceListResponse;
import com.app.net.Commands;
import com.app.widget.SpaceTagsPopupWindow;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.widget.view.LoadMoreListView;

/**
 * 空间列表
 * @author pythoner
 *
 */
public class SpaceListActivity extends BaseActivity implements OnClickListener{

	private Context context;
	private LoadMoreListView listView;
	private CommonAdapter<JvclerkspaceInfo> adapter;
	private ArrayList<JvclerkspaceInfo> beans=new ArrayList<JvclerkspaceInfo>();
	private int startNum = 0;
	private int size = 20;
	private String labelID;
	
	private SwipeRefreshLayout swipeRefreshLayout;
	private EditText et_search;
	private String keyWords;
	private String startDate,endDate,order,category;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_space_list);
		context=this;
		initActionBar("空间列表", "筛选", null);
		labelID=String.valueOf(getIntent().getIntExtra("LabelID", 1));
		initViews();
		doCommandGetClerkSpaceList(keyWords,null,null,null,null,startNum);
	}

	private void doCommandGetClerkSpaceList(String KeyWords ,String StartDate,String EndDate,String Order,String FormatTag,int StartNum){
		doCommandGetClerkSpaceList(KeyWords, StartDate, EndDate, Order, FormatTag, StartNum,true);
	}
	private void doCommandGetClerkSpaceList(String KeyWords ,String StartDate,String EndDate,String Order,String FormatTag,int StartNum,boolean showLoading){
		
		/*Commands.doCommandGetClerkSpaceList(context, KeyWords,labelID, StartDate, EndDate, Order, FormatTag, StartNum, size, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				closeRefreshing();
				if(isSuccess(response)){
					GetClerkSpaceListResponse obj=mapperToObject(response, GetClerkSpaceListResponse.class);
					updateViews(obj);
				}
			}
		},showLoading);*/
	}
	
	@Override
	public void initViews(){
		findViewById(R.id.btn_search).setOnClickListener(this);
		findViewById(R.id.btn_submit).setOnClickListener(this);
		et_search=(EditText)findViewById(R.id.et_search);
		
		swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
		swipeRefreshLayout.setColorScheme(R.color.fittting_green, R.color.secondary, R.color.fittting_gray, R.color.primary);
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh()
            {
                // TODO Auto-generated method stub
            	refreshListView();
            	doCommandGetClerkSpaceList(keyWords,startDate,endDate,order,category,startNum,false);
            }
        });
		listView = (LoadMoreListView) findViewById(R.id.listView);
		listView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				 startNum+=size;
				 doCommandGetClerkSpaceList(keyWords,startDate,endDate,order,category,startNum);
			}
		});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> pearnt, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,SpaceDetailActivity.class);
				intent.putExtra("ID", beans.get(position).getId());
				startActivity(intent);
			}
		});
	}

	private void closeRefreshing() {
		if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
			swipeRefreshLayout.setRefreshing(false);
		}
	}
	
	private void notifyDataSetChanged() {
		if (adapter == null) {
			listView.setAdapter(adapter = new CommonAdapter<JvclerkspaceInfo>(
					context, beans, R.layout.item_space_1) {

				@Override
				public void setValues(ViewHolder helper,
						final JvclerkspaceInfo item, final int position) {
					// TODO Auto-generated method stub
					helper.setText(R.id.item_0, item.getTitle());
					helper.setText(R.id.item_1, "浏览："+item.getBrowsingnumber());
				}

			});
		} else {
			adapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void doRightButtonClick(View v) {
		// TODO Auto-generated method stub
		showMatchTagsPopupWindow(v);
	}
	
	private void showMatchTagsPopupWindow(View v){
		View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_space_tags, null);
		SpaceTagsPopupWindow popupWindow = new SpaceTagsPopupWindow(context,view, App.config.getScreenWidth());
		popupWindow.setOnOkClickListener(new SpaceTagsPopupWindow.OnOkClickListener() {

			@Override
			public void onOkClick(String startDates, String endDates,
					List<KeyValue> orders, List<KeyValue> categorys) {
				// TODO Auto-generated method stub
				order=null;
				if(!isEmptyList(orders)){
					order=orders.get(0).getKey();
				}
				category=null;
				if(!isEmptyList(categorys)){
					category=categorys.get(0).getKey();
				}
				startDate=startDates;
				endDate=endDates;
				
				keyWords=null;
				refreshListView();
				doCommandGetClerkSpaceList(keyWords,startDate,endDate,order,category,startNum);
			}
			
		});
		popupWindow.showAsDropDown(v, 0, 0);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=null;
		switch (v.getId()) {
		case R.id.btn_search:
			keyWords=et_search.getText().toString().trim();
			if(isEmpty(keyWords)){
				doShake(context, et_search);
				return;
			}
			startDate=null;
			endDate=null;
			order=null;
			category=null;
			refreshListView();
			doCommandGetClerkSpaceList(keyWords,startDate,endDate,order,category,startNum);
			break;
		case R.id.btn_submit:
			intent=new Intent(context,SpacePublishActivity.class);
			intent.putExtra("LabelID",Integer.parseInt(labelID));
			startActivity(intent);
			break;

		default:
			break;
		}
	}
	
	@Override
	public void updateViews(Object obj) {
		listView.onLoadMoreComplete();
		if(obj==null){
			return;
		}
		GetClerkSpaceListResponse response=(GetClerkSpaceListResponse) obj;
		List<JvclerkspaceInfo> list = response.getHeadInfo();
		if (list.size() == 0) {
			listView.removeLoadMoreListener();
			showToast("没有记录了");
			return;
		}
		beans.addAll(list);
		notifyDataSetChanged();
	}

	private void refreshListView() {
		listView.resetLoadMoreListener();
		startNum = 0;
		beans.clear();
		notifyDataSetChanged();
	}
	
	@Subscriber
	void updateByEventBus(String event) {
		
	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		updateTheme();
	}
	private void updateTheme() {
		setThemeDrawable(context, R.id.btn_search);
		setThemeDrawable(context, R.id.btn_submit);
	}
}
