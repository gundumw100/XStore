package com.app.xstore.space;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.model.ClerkSpaceInfo;
import com.app.model.JvclerkspaceInfo;
import com.app.model.JvclerkspacestylelabelInfo;
import com.app.model.response.GetClerkSpaceListResponse;
import com.app.model.response.GetClerkSpaceStyleLabelResponse;
import com.app.net.Commands;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.base.model.KeyValue;
import com.base.util.comm.DisplayUtils;
import com.squareup.picasso.Picasso;
import com.widget.common.recycler.BaseRecyclerAdapter;
import com.widget.common.recycler.SpacesItemDecoration;
import com.widget.common.recycler.ViewHolder;

/**
 * 店员空间
 * @author pythoner
 *
 */
public class SpaceActivity extends BaseActivity implements OnClickListener{

	private Context context;
    private int startNum = 0;
	private int size = 10;
	private String keyWords;
	private RecyclerView gallery;
	private BaseRecyclerAdapter<JvclerkspaceInfo> galleryAdapter;
	private List<JvclerkspaceInfo> beans=new ArrayList<JvclerkspaceInfo>();
	private SwipeRefreshLayout swipeRefreshLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_space);
		context=this;
		initActionBar("店员空间", null, null);
		initViews();
		initGallery();
		doCommandGetClerkSpaceList();
	}

	private void doCommandGetClerkSpaceList(){
//		doCommandGetClerkSpaceStyleLabel();
		doCommandGetClerkSpaceList5(keyWords,null,null,null,null,startNum);//店铺晒单
		doCommandGetClerkSpaceList2(keyWords,null,null,null,null,startNum);//销售技巧
		doCommandGetClerkSpaceList3(keyWords,null,null,null,null,startNum);//商品知识
		doCommandGetClerkSpaceList4(keyWords,null,null,null,null,startNum);//店铺管理
//		doCommandGetClerkSpaceList1(keyWords,null,null,null,null,startNum);//店铺陈列
	}
	
	@Override
	public void initViews() {
		$(R.id.btn_publish_5).setOnClickListener(this);
		$(R.id.btn_more_5).setOnClickListener(this);
		$(R.id.btn_publish_2).setOnClickListener(this);
		$(R.id.btn_more_2).setOnClickListener(this);
		$(R.id.btn_publish_3).setOnClickListener(this);
		$(R.id.btn_more_3).setOnClickListener(this);
		$(R.id.btn_publish_4).setOnClickListener(this);
		$(R.id.btn_more_4).setOnClickListener(this);
		
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
		swipeRefreshLayout.setColorScheme(R.color.fittting_green, R.color.secondary, R.color.fittting_gray, R.color.primary);
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh()
            {
                // TODO Auto-generated method stub
				if (galleryAdapter != null) {
					startNum=0;
					galleryAdapter.removeAllItem();
				}
            	doCommandGetClerkSpaceList();
            }
        });
		
	}
	
	private void closeRefreshing() {
		if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
			swipeRefreshLayout.setRefreshing(false);
		}
	}
	
	private void doCommandGetClerkSpaceStyleLabel(){
		String shopId=String.valueOf(App.user.getShopInfo().getShop_id());
		Log.i("tag", "shopId="+shopId);
		Commands.doCommandGetClerkSpaceStyleLabel(context,shopId, new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				Log.i("tag", "response="+response.toString());
				if(isSuccess(response)){
					GetClerkSpaceStyleLabelResponse obj=mapperToObject(response, GetClerkSpaceStyleLabelResponse.class);
					List<JvclerkspacestylelabelInfo> list = obj.getHeadInfo();
					if(!isEmptyList(list)){
						Log.i("tag", "list.size="+list.size());
					}
				}
			}
		});
	}
	
	private void doCommandGetClerkSpaceList5(String KeyWords ,String StartDate,String EndDate,String Order,String FormatTag,int StartNum){
//		KeyValue curKeyValue=new KeyValue("5","精品榜");//test
		String labelid="5";//
		Commands.doCommandGetClerkSpaceList(context, labelid,KeyWords,StartDate, EndDate, Order, FormatTag, StartNum, size, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				closeRefreshing();
				Log.i("tag", "response="+response.toString());
				if(isSuccess(response)){
					GetClerkSpaceListResponse obj=mapperToObject(response, GetClerkSpaceListResponse.class);
					List<JvclerkspaceInfo> list = obj.getHeadInfo();
					if(!isEmptyList(list)){
						galleryAdapter.addItems(list);
					}
				}
			}
		});
	}
	
	private void doCommandGetClerkSpaceList2(String KeyWords ,String StartDate,String EndDate,String Order,String FormatTag,int StartNum){
		int size=5;
		String labelid="2";//销售技巧
		Commands.doCommandGetClerkSpaceList(context,labelid, KeyWords, StartDate, EndDate, Order, FormatTag, StartNum, size, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				//{"ErrMessage":"店员空间信息不存在","Result":false,"ErrSysTrackMessage":"","ErrSysMessage":"","HeadInfo":null,"Message":"店员空间信息不存在"}
//				Log.i("tag", "response="+response.toString());
				if(isSuccess(response)){
					GetClerkSpaceListResponse obj=mapperToObject(response, GetClerkSpaceListResponse.class);
					List<JvclerkspaceInfo> list = obj.getHeadInfo();
					if(!isEmptyList(list)){
						LinearLayout container=$(R.id.container_2);
						createItem(container,list);
					}
				}
			}
		});
	}
	
	private void doCommandGetClerkSpaceList3(String KeyWords ,String StartDate,String EndDate,String Order,String FormatTag,int StartNum){
		int size=5;
		String labelid="3";//商品知识
		Commands.doCommandGetClerkSpaceList(context, labelid,KeyWords, StartDate, EndDate, Order, FormatTag, StartNum, size, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				if(isSuccess(response)){
					GetClerkSpaceListResponse obj=mapperToObject(response, GetClerkSpaceListResponse.class);
					List<JvclerkspaceInfo> list = obj.getHeadInfo();
					if(!isEmptyList(list)){
						LinearLayout container=$(R.id.container_3);
						createItem(container,list);
					}
				}
			}
		});
	}
	
	private void doCommandGetClerkSpaceList4(String KeyWords ,String StartDate,String EndDate,String Order,String FormatTag,int StartNum){
		int size=5;
		String labelid="4";//店铺管理
		Commands.doCommandGetClerkSpaceList(context, labelid,KeyWords, StartDate, EndDate, Order, FormatTag, StartNum, size, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				closeRefreshing();
				if(isSuccess(response)){
					GetClerkSpaceListResponse obj=mapperToObject(response, GetClerkSpaceListResponse.class);
					List<JvclerkspaceInfo> list = obj.getHeadInfo();
					if(!isEmptyList(list)){
						LinearLayout container=$(R.id.container_4);
						createItem(container,list);
					}
				}
			}
		});
	}
	
//	private void doCommandGetClerkSpaceList1(String KeyWords ,String StartDate,String EndDate,String Order,String FormatTag,int StartNum){
////		KeyValue curKeyValue=new KeyValue("1","店铺陈列");//test
//		int size=5;
//		Commands.doCommandGetClerkSpaceList(context, KeyWords,"1", StartDate, EndDate, Order, FormatTag, StartNum, size, new Listener<JSONObject>() {
//
//			@Override
//			public void onResponse(JSONObject response) {
//				// TODO Auto-generated method stub
//				if(isSuccess(response)){
//					GetClerkSpaceListResponse obj=mapperToObject(response, GetClerkSpaceListResponse.class);
//					List<ClerkSpaceInfo> list = obj.getSpaceInfos();
//					if(!isEmptyList(list)){
//						LinearLayout container=$(R.id.container_1);
//						createLine(container,list);
//					}
//				}
//			}
//		},true);
//	}
	
	private void createItem(LinearLayout container,List<JvclerkspaceInfo> list){
		container.removeAllViews();
		LinearLayout.LayoutParams p0=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		p0.weight=1;
		p0.gravity=Gravity.CENTER_VERTICAL;
		LinearLayout.LayoutParams p1=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		p1.gravity=Gravity.CENTER_VERTICAL;
		int large=DisplayUtils.dip2px(context, 48);
		LinearLayout.LayoutParams p2=new LinearLayout.LayoutParams(large,large);
		LinearLayout.LayoutParams p3=new LinearLayout.LayoutParams(large,LinearLayout.LayoutParams.WRAP_CONTENT);
		p3.gravity=Gravity.CENTER_VERTICAL;
		for(final JvclerkspaceInfo bean:list){
			LinearLayout line=new LinearLayout(context);
			line.setOrientation(LinearLayout.HORIZONTAL);
			line.setPadding(8, 8, 8, 8);
			line.setTag(bean);
			line.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if("FromDress".equals(bean.getSource())){
						/*Intent intent=new Intent(context,MatchDetailsActivity.class);
						intent.putExtra("ID", bean.getSourceID());
						intent.putExtra("Description", bean.getTitle());
						startActivity(intent);*/
					}else{
						JvclerkspaceInfo b=(JvclerkspaceInfo)v.getTag();
						Intent intent=new Intent(context,SpaceDetailActivity.class);
						intent.putExtra("ID", b.getId());
						startActivity(intent);
					}
				}
			});
			
			ImageView iv=new ImageView(context);
			iv.setLayoutParams(p2);
			
//			int px=DisplayUtils.dip2px(context, dp);
			Picasso.with(context).load(bean.getHeadportraiturl()).transform(new com.app.util.CropCircleTransformation()).resize(large, large).centerCrop().placeholder(R.drawable.default_user_circle).error(R.drawable.default_user_circle).into(iv);
			
//			Commands.loadImageByVolley(bean.getHeadportraiturl(), iv, R.drawable.default_user_circle,0);
			TextView tv_0=new TextView(context);
			tv_0.setLayoutParams(p0);
			if("FromDress".equals(bean.getSource())){
				tv_0.setText("搭配分享："+bean.getTitle());
			}else{
				tv_0.setText(bean.getTitle());
			}
			tv_0.setSingleLine();
			tv_0.setEllipsize(TruncateAt.END);
			tv_0.setTextColor(App.res.getColor(R.color.grayDark));
			tv_0.setPadding(8, 0, 8, 0);
			
			line.addView(iv);
			line.addView(tv_0);
			if("FromDress".equals(bean.getSource())){
			}else{
				TextView tv_1=new TextView(context);
				tv_1.setLayoutParams(p3);
				tv_1.setText("浏览:"+bean.getBrowsingnumber());
				tv_1.setTextColor(App.res.getColor(R.color.grayLight));
				tv_1.setSingleLine();
				
				com.widget.iconify.IconTextView itv=new com.widget.iconify.IconTextView(context);
				itv.setLayoutParams(p1);
				itv.setText("{fa-heart}"+bean.getPraisecount());
				itv.setPadding(8, 0, 8, 0);
				if(bean.getPraisecount()>0){
					itv.setTextColor(App.res.getColor(R.color.primary));
				}else{
					itv.setTextColor(App.res.getColor(R.color.grayLight));
				}
				line.addView(itv);
				line.addView(tv_1);
			}
			container.addView(line,p0);
		}
	}
	
	@Override
	public void doRightButtonClick(View v) {
		// TODO Auto-generated method stub
//		if(curKeyValue==null){
//			return;
//		}
//		Intent intent=new Intent(context,SpacePublishActivity.class);
//		intent.putExtra("LabelID",Integer.parseInt(curKeyValue.getKey()));
//		startActivity(intent);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=null;
		KeyValue kv=null;
		switch (v.getId()) {
		case R.id.btn_publish_5:
			intent=new Intent(context,SpacePublishActivity.class);
			intent.putExtra("LabelID",5);
			startActivity(intent);
			break;
		case R.id.btn_more_5:
			kv=new KeyValue("5","店铺晒单");
			intent=new Intent(context,RefreshListActivity.class);
			intent.putExtra("KeyValue",kv);
			startActivity(intent);
			break;
		case R.id.btn_publish_2:
			intent=new Intent(context,SpacePublishActivity.class);
			intent.putExtra("LabelID",2);
			startActivity(intent);
			break;
		case R.id.btn_more_2:
			kv=new KeyValue("2","销售技巧");
			intent=new Intent(context,RefreshListActivity.class);
			intent.putExtra("KeyValue",kv);
			startActivity(intent);
			break;
		case R.id.btn_publish_3:
			intent=new Intent(context,SpacePublishActivity.class);
			intent.putExtra("LabelID",3);
			startActivity(intent);
			break;
		case R.id.btn_more_3:
			kv=new KeyValue("3","商品知识");
			intent=new Intent(context,RefreshListActivity.class);
			intent.putExtra("KeyValue",kv);
			startActivity(intent);
			break;
		case R.id.btn_publish_4:
			intent=new Intent(context,SpacePublishActivity.class);
			intent.putExtra("LabelID",4);
			startActivity(intent);
			break;
		case R.id.btn_more_4:
			kv=new KeyValue("4","店铺管理");
			intent=new Intent(context,RefreshListActivity.class);
			intent.putExtra("KeyValue",kv);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
	
	@Override
	public void updateViews(Object obj){
		
	}

	@Subscriber
	void updateByEventBus(String event) {
//		if(App.EVENT_UPDATE_SPACE.equals(event)){
//			doCommandGetClerkSpaceList();
//		}
	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
//		setThemeDrawable(context, R.id.indicator);
	}
	
	private void initGallery(){
		gallery = (RecyclerView) findViewById(R.id.gallery);
//		gallery.setHasFixedSize(true);
		gallery.addItemDecoration(new SpacesItemDecoration(8));
		gallery.setItemAnimator(new DefaultItemAnimator());
        final LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        gallery.setLayoutManager(manager);
        
        galleryAdapter=new BaseRecyclerAdapter<JvclerkspaceInfo>(context, beans) {

			@Override
			public int onCreateViewLayoutID(int viewType) {
				// TODO Auto-generated method stub
				return R.layout.item_image_gallery;
			}

			@Override
			public void onBindViewHolder(ViewHolder holder, JvclerkspaceInfo item, int position) {
				// TODO Auto-generated method stub
				Log.i("tag", "item.getImageurl==="+item.getImageurl());
				String imageURL=null;
				if(!isEmpty(item.getImageurl())){
					String[] imageURLs=item.getImageurl().split(";");
					if(imageURLs.length>0){
						imageURL=imageURLs[0];
					}
				}
//				holder.setImageUrl(R.id.item_0,imageURL, R.drawable.default_img);
				ImageView iv=holder.getView(R.id.item_0);
				int large=DisplayUtils.dip2px(context, 112);
				Log.i("tag", "imageURL==="+imageURL);
				Picasso.with(context).load(imageURL).resize(large, large).centerCrop().placeholder(R.drawable.default_img).error(R.drawable.default_img).into(iv);
				
			}
		};
		galleryAdapter.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View child, int position,long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,SpaceDetailActivity.class);
				intent.putExtra("ID", beans.get(position).getId());
				startActivity(intent);
			}
		});
        gallery.setAdapter(galleryAdapter);
		gallery.setOnScrollListener(new RecyclerView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(RecyclerView recyclerView,int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				int lastVisibleItem = manager.findLastVisibleItemPosition();
				int totalItemCount = manager.getItemCount();
				int visibleItemCount = manager.getChildCount();//当前屏幕所看到的子项个数
				if (newState == RecyclerView.SCROLL_STATE_IDLE
						&& lastVisibleItem + 1 == totalItemCount&&visibleItemCount>0) {
					startNum+=size;
					doCommandGetClerkSpaceList5(keyWords,null,null,null,null,startNum);
				}
			}

		});
	}
	
}
