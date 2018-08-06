package com.app.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.simple.eventbus.Subscriber;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.model.ProdCheckDataInfo;
import com.app.model.ProdCheckDtl;
import com.app.model.ProdPreChcekData;
import com.app.model.UserInfo;
import com.app.model.response.GetProdPreCheckDataResponse;
import com.app.net.Commands;
import com.app.widget.InventoryDialog;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.BaseActivity.OnSelectUserInfoListener;
import com.app.xstore.inventory.CreateDocumentActivity;
import com.app.xstore.inventory.DocumentDetailActivity;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;
import com.base.util.D;
import com.base.util.comm.TimeUtils;
import com.widget.view.LoadMoreListView;


/**
 * 预盘点列表
 * @author pythoner
 *
 */
public class InventoryFragment extends BaseFragment implements OnClickListener {

	private EditText et_locID;
	private TextView et_userId,btn_startDate,btn_endDate;
	private View btn_clear_userId;
	private View btn_search,btn_all;
	private SwipeRefreshLayout swipeRefreshLayout;
	private LoadMoreListView listView;
	private CommonAdapter<ProdPreChcekData> adapter;
	private List<ProdPreChcekData> beans=new ArrayList<ProdPreChcekData>();
	private int start = 1;
	private int size = 20;
	private int curPosition=-1;
	
	public InventoryFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.fragment_inventory, container,false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		initHandler();
		initViews(view);
		doCommandGetProdPreCheckData(start,size);
	}
	
	@Override
	public void initViews(View view){
		String today=TimeUtils.getNow("yyyy-MM-dd");
		et_locID=$(view,R.id.et_locID);
		et_userId=$(view,R.id.et_userId);
		et_userId.setOnClickListener(this);
		btn_clear_userId=$(view,R.id.btn_clear_userId);
		btn_clear_userId.setOnClickListener(this);
		btn_clear_userId.setVisibility(et_userId.getText().length()==0?View.GONE:View.VISIBLE);
		btn_startDate=$(view,R.id.btn_startDate);
		btn_startDate.setOnClickListener(this);
		btn_startDate.setText(today);
		btn_endDate=$(view,R.id.btn_endDate);
		btn_endDate.setOnClickListener(this);
		btn_endDate.setText(today);
		btn_search=$(view,R.id.btn_search);
		btn_search.setOnClickListener(this);
		$(view,R.id.iv_scan).setOnClickListener(this);
		btn_all=$(view,R.id.btn_all);
		btn_all.setOnClickListener(this);
		
		listView=$(view,R.id.listView);
		listView.setOnItemClickListener(new ListView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				ProdPreChcekData item=beans.get(position);
				changeTo(item);
			}
			
		});
		listView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				start+=size;
				 doCommandGetProdPreCheckData(start,size);
			}
		});
		listView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				final ProdPreChcekData bean=beans.get(position);
				D.showDialog(getActivity(), "是否删除货架"+bean.getShelf_code()+" ？", "确定", "取消", new D.OnPositiveListener() {
					
					@Override
					public void onPositive() {
						// TODO Auto-generated method stub
						doCommandDeletePrecheckData(bean);
					}
				});
				return true;
			}
		});
		
		swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
		swipeRefreshLayout.setColorScheme(R.color.fittting_green, R.color.secondary, R.color.fittting_gray, R.color.primary);
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh()
            {
                // TODO Auto-generated method stub
            	refreshListView();
            	doCommandGetProdPreCheckData(start,size,false);
            }
        });
	}
	
	private void changeTo(ProdPreChcekData item){
		List<ProdCheckDataInfo> docs=DataSupport.where("prodPreChcekData_id = ?", ""+item.getId()).find(ProdCheckDataInfo.class); 
		if(context.isEmptyList(docs)){
			Intent intent=new Intent(context,CreateDocumentActivity.class);
			intent.putExtra("ProdPreChcekData_id", item.getId());
			intent.putExtra("ShelfCode", item.getShelf_code());
			intent.putExtra("TotalQty", item.getTotal_qty());
			intent.putExtra("ID", item.getId());
			intent.putExtra("NeedRead", 1);
			startActivity(intent);
		}else{
			ProdCheckDataInfo bean=docs.get(0);
			List<ProdCheckDtl> prods=DataSupport.where("DocID = ?", String.valueOf(bean.getId())).find(ProdCheckDtl.class);
			bean.setProdcheckdtl(prods);
			Intent intent=new Intent(context,DocumentDetailActivity.class);
			intent.putExtra("ProdCheckData", bean);
			startActivity(intent);
		}
	}
	
	private void closeRefreshing() {
		if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
			swipeRefreshLayout.setRefreshing(false);
		}
	}
	
	//重写resultHandler
	private void initHandler(){
		context.resultHandler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				String data = (String) msg.obj;
				if(data.equalsIgnoreCase("time out")||data.equalsIgnoreCase("user canceled")){
					showToast(R.string.alert_no_barcode_found);
					return;
				}
				et_locID.setText(data);
				refreshListView();
				doCommandGetProdPreCheckData(start,size);
			}
		};
	}
	
	@Override
	public void onClick(View v) {
		if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
			return;
		}
		switch (v.getId()) {
		case R.id.iv_scan:
			context.doScan(context.resultHandler);
			break;
		case R.id.et_userId:
			doCommandGetShopUserListAndShowDialog();
			break;
		case R.id.btn_clear_userId:
			et_userId.setText("");
			btn_clear_userId.setVisibility(et_userId.getText().length()==0?View.GONE:View.VISIBLE);
			break;
		case R.id.btn_startDate:
			showDatePickerDialog(btn_startDate);
			break;
		case R.id.btn_endDate:
			showDatePickerDialog(btn_endDate);
			break;
		case R.id.btn_search:
			refreshListView();
			doCommandGetProdPreCheckData(start,size);
			break;
		case R.id.btn_all:
			doSearchPerformClick();
			break;
		}
	}

	private void doCommandGetShopUserListAndShowDialog(){
		context.doCommandGetShopUserListAndShowDialog(new OnSelectUserInfoListener() {
			
			@Override
			public void doResult(UserInfo instance) {
				// TODO Auto-generated method stub
				et_userId.setText(instance.getUser_code());
				btn_clear_userId.setVisibility(et_userId.getText().length()==0?View.GONE:View.VISIBLE);
				refreshListView();
				doCommandGetProdPreCheckData(start,size);
			}
		});
	}
	
	private void showDatePickerDialog(final TextView v){
		com.widget.view.DatePickerDialog d=new com.widget.view.DatePickerDialog(context,"取消","确定",v.getText().toString());
		d.setOnButtonClickListener(new com.widget.view.DatePickerDialog.OnButtonClickListener() {
			
			@Override
			public void onOKClick(String year, String month, String date) {
				// TODO Auto-generated method stub
				v.setText(year+"-"+month+"-"+date);
				refreshListView();
				doCommandGetProdPreCheckData(start,size);
			}
			
			@Override
			public void onCancelClick() {
				// TODO Auto-generated method stub
			}
		});
		d.show();
	}
	private void doCommandGetProdPreCheckData(int start, int size){
		doCommandGetProdPreCheckData(start, size,true);
	}
	private void doCommandGetProdPreCheckData(int start, int size,boolean showProgress){
		String shop_code=App.user.getShopInfo().getShop_code();
		String creator=et_userId.getText().toString().trim();
		String shelf_code=et_locID.getText().toString().trim();
		String start_date=btn_startDate.getText().toString().trim();
		String end_date=btn_endDate.getText().toString().trim();
		Commands.doCommandGetProdPreCheckData(context,shop_code, shelf_code, start_date, end_date, creator,start, size,new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				Log.i("tag", response.toString());
				if(context.isSuccess(response)){
					GetProdPreCheckDataResponse obj=context.mapperToObject(response, GetProdPreCheckDataResponse.class);
					List<ProdPreChcekData> list=obj.getPrecheck_Info();
					if(list!=null){
						for(ProdPreChcekData b:list){//备份赋值
							b.setProdPreChcekData_id(b.getId());
						}
					}
					updateViews(list);
				}
			}
		},showProgress);
		
	}
	
	@Override
	public void updateViews(Object obj) {
		closeRefreshing();
		listView.onLoadMoreComplete();
		if(obj==null){
			return;
		}
		ArrayList<ProdPreChcekData> list = (ArrayList<ProdPreChcekData>) obj;
		if (list.size() == 0) {
			listView.removeLoadMoreListener();
			if(beans.size()==0){
				context.setEmptyView(listView,"未找到该时段数据");
			}else{
				showToast("没有记录了");
			}
			return;
		}
		beans.addAll(list);
		notifyDataSetChanged();
		
	}
	
	private void refreshListView() {
		curPosition=-1;
		listView.resetLoadMoreListener();
		start = 1;
		beans.clear();
		notifyDataSetChanged();
	}
	
	private void notifyDataSetChanged(){
		if(adapter==null){
			listView.setAdapter(adapter = new CommonAdapter<ProdPreChcekData>( context, beans,
					  R.layout.item_inventory){
					  
					@Override
					public void setValues(ViewHolder helper, final ProdPreChcekData item, final int position) {
						// TODO Auto-generated method stub
						helper.getView(R.id.container).setBackgroundColor(curPosition==position?0xFFFF6633:0x00000000);
						helper.setText(R.id.item_0, "货架："+item.getShelf_code());
						helper.setText(R.id.item_1, item.getCreate_time_string());
						helper.setText(R.id.item_2, "预点数："+item.getTotal_qty());
						helper.setText(R.id.item_3, item.getCreate_user());
						if(App.user.getUserInfo().getUser_code().equals(item.getCreate_user())){//不是自己建的不允许修改
							helper.getView(R.id.btn_0).setVisibility(View.VISIBLE);
							//修改
							helper.getView(R.id.btn_0).setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									final String ShelfCode=item.getShelf_code();//备份，还原用
									final String totalQty=item.getTotal_qty();//备份，还原用
									final InventoryDialog d=new InventoryDialog(context,"预点",item);
									d.setOnClickListener(new InventoryDialog.OnClickListener() {
										
										@Override
										public void onClick(View v, ProdPreChcekData item) {
											// TODO Auto-generated method stub
											curPosition=position;
											doCommandUpdateProdPrecheckData(ShelfCode,totalQty,item);
											d.dismiss();
										}
										
									});
									d.show();
								}
							});
						}else{
							helper.getView(R.id.btn_0).setVisibility(View.GONE);
						}
						//明细
						helper.getView(R.id.btn_1).setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								changeTo(item);
							}
						});
						context.setThemeDrawable(context, helper.getView(R.id.btn_1), R.drawable.btn_pressed);
					}
			});
		}else{
			adapter.notifyDataSetChanged();
		}
	}
	
	private void doCommandUpdateProdPrecheckData(final String shelfCode,final String totalQty,final ProdPreChcekData bean){
		String shop_id=App.user.getShopInfo().getShop_code();
		String shelf_code=bean.getShelf_code();
		int total_qty=Integer.parseInt(bean.getTotal_qty());
		String create_user=App.user.getUserInfo().getUser_code();
		int id=bean.getId();
		Commands.doCommandUpdateProdPrecheckData(context, shop_id,shelf_code,total_qty,create_user,id, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				if(context.isSuccess(response)){
					notifyDataSetChanged();
					
					// 同步
					ContentValues values = new ContentValues();
					values.put("Shelf_code", bean.getShelf_code());
					values.put("Total_qty", bean.getTotal_qty());
					DataSupport.updateAll(ProdPreChcekData.class, values, "ProdPreChcekData_id = ?", String.valueOf(bean.getProdPreChcekData_id()));
				}else{
					//还原
					bean.setShelf_code(shelfCode);
					bean.setTotal_qty(totalQty);
					notifyDataSetChanged();
				}
			}
		});
	}
	
	private void doCommandDeletePrecheckData(final ProdPreChcekData bean){
		 String shop_id=App.user.getShopInfo().getShop_code();
		 String shelf_code=bean.getShelf_code();
		Commands.doCommandDeletePrecheckData(context, shop_id,shelf_code, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				if(context.isSuccess(response)){
					if(context.isEmpty(bean.getShelf_code())){
						//不传shelfcode，就是删除当前门店下的所有数据
						refreshListView();
//						doCommandGetProdPreCheckData(start,size);
					}else{
						beans.remove(bean);
					}
					notifyDataSetChanged();
				}
			}
		});
	}
	
	private void doSearchPerformClick(){
		btn_startDate.setText("");
		btn_endDate.setText("");
		et_locID.setText("");
		et_userId.setText("");
		btn_search.performClick();
	}
	
	@Subscriber
	void updateByEventBus(String event) {
		if(event.equals(App.EVENT_UPDATE_INVENTORY)){
			refreshListView();
			doCommandGetProdPreCheckData(start,size,false);
		}else if(event.equals(App.EVENT_CLEAR_SHELF)){
			//不传shelfcode，就是删除当前门店下的所有数据
			ProdPreChcekData bean=new ProdPreChcekData();
//			bean.setOrgCode(context.getUser(context).getOrgCode());
			doCommandDeletePrecheckData(bean);
		}
	}
	
	@Override
	public void updateTheme(int color) {
		((BaseActivity) context).setThemeDrawable(context, btn_search);
		((BaseActivity) context).setThemeDrawable(context, btn_all);
		if(adapter!=null){
			adapter.notifyDataSetChanged();
		}
	}
	
}
