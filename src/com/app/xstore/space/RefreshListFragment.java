package com.app.xstore.space;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.android.volley.Response.Listener;
import com.app.fragment.BaseFragment;
import com.app.model.ClerkSpaceInfo;
import com.app.model.JvclerkspaceInfo;
import com.app.model.response.GetClerkSpaceListResponse;
import com.app.net.Commands;
import com.app.xstore.App;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;
import com.base.model.KeyValue;
import com.base.util.comm.DisplayUtils;
import com.squareup.picasso.Picasso;
import com.widget.view.LoadMoreListView;

/**
 * 店员空间
 * @author Ni Guijun
 *
 */
public class RefreshListFragment extends BaseFragment implements OnClickListener{

	private LoadMoreListView listView;
	private CommonAdapter<JvclerkspaceInfo> adapter;
	private ArrayList<JvclerkspaceInfo> beans=new ArrayList<JvclerkspaceInfo>();
	private int startNum = 0;
	private int size = 20;
	private KeyValue curKeyValue;
	private String keyWords;
	private String startDate,endDate,order,category;
	private SwipeRefreshLayout swipeRefreshLayout;
	
	public static RefreshListFragment newInstance(KeyValue kv) {
		RefreshListFragment fragment = new RefreshListFragment();
        Bundle args = new Bundle();
        args.putParcelable("KeyValue", kv);
        fragment.setArguments(args);
        return fragment;
    }
	
    @Override
	public void onCreate(Bundle savedInstanceState) {  
	    super.onCreate(savedInstanceState);  
        Bundle args = getArguments();
        if (args != null) {  
        	curKeyValue = args.getParcelable("KeyValue");
        }
	} 
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.fragment_refresh_list,container, false);
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
//		int position = FragmentPagerItem.getPosition(getArguments());
		initViews(view);
		doCommandGetClerkSpaceList(keyWords,null,null,null,null,startNum);
	}
	
	private void doCommandGetClerkSpaceList(String KeyWords ,String StartDate,String EndDate,String Order,String FormatTag,int StartNum){
		doCommandGetClerkSpaceList(KeyWords, StartDate, EndDate, Order, FormatTag, StartNum,true);
	}
	
	private void doCommandGetClerkSpaceList(String KeyWords ,String StartDate,String EndDate,String Order,String FormatTag,int StartNum,boolean showLoading){
		if(curKeyValue==null){
			return;
		}
		
		Commands.doCommandGetClerkSpaceList(context,curKeyValue.getKey(), KeyWords, StartDate, EndDate, Order, FormatTag, StartNum, size, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				closeRefreshing();
				if(context.isSuccess(response)){
					GetClerkSpaceListResponse obj=context.mapperToObject(response, GetClerkSpaceListResponse.class);
					updateViews(obj);
				}
			}
		});
	}

	@Override
	public void initViews(View view){
		swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
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
		
		listView = (LoadMoreListView) view.findViewById(R.id.listView);
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
	
	private void refreshListView() {
		listView.resetLoadMoreListener();
		startNum = 0;
		beans.clear();
		notifyDataSetChanged();
	}
	
	private void closeRefreshing() {
		if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
			swipeRefreshLayout.setRefreshing(false);
		}
	}
	
	private void notifyDataSetChanged(){
		if(adapter==null){
			listView.setAdapter(adapter = new CommonAdapter<JvclerkspaceInfo>(
					context, beans, R.layout.item_space_0) {

				@Override
				public void setValues(ViewHolder helper,
						JvclerkspaceInfo item, int position) {
					// TODO Auto-generated method stub
					ImageView iv= helper.getView(R.id.item_0);
//					Commands.loadImageByVolley(item.getHeadportraitURL(),iv, R.drawable.default_user_circle,0);
					int large=DisplayUtils.dip2px(context, 48);
					Picasso.with(context).load(item.getHeadportraiturl()).transform(new com.app.util.CropCircleTransformation()).resize(large, large).centerCrop().placeholder(R.drawable.default_user_circle).error(R.drawable.default_user_circle).into(iv);
					
					if("FromDress".equals(item.getSource())){
						helper.setText(R.id.item_1, "搭配分享："+item.getTitle());
						helper.setText(R.id.item_2, "");
					}else{
						helper.setText(R.id.item_1, item.getTitle());
						helper.setText(R.id.item_2, "浏览："+item.getBrowsingnumber());
					}
				}

			});
		}else{
			adapter.notifyDataSetChanged();
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
		if (list.size() == 0 && startNum > 0) {//最后一页
			listView.removeLoadMoreListener();
			showToast("没有记录了");
			return;
		}
		beans.addAll(list);
		notifyDataSetChanged();
	}
	
	@Override
	public void onClick(View v) {
		
	}
	
	// 接收方法,默认的tag,执行在UI线程
	@Subscriber
	void updateByEventBus(String event) {
		if(App.EVENT_UPDATE_SPACE.equals(event)){
//			listView.resetLoadMoreListener();
//			beans.clear();
//			startNum=0;
//			doCommandGetClerkSpaceList(keyWords,null,null,null,null,startNum);
		}
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		adapter=null;
	}
	
}
