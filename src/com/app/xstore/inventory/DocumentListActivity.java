package com.app.xstore.inventory;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import com.android.volley.Response.Listener;
import com.app.model.ProdCheckDataInfo;
import com.app.model.ProdCheckDtl;
import com.app.model.ProdPreChcekData;
import com.app.model.UserInfo;
import com.app.model.response.GetProdPreCheckDataResponse;
import com.app.net.Commands;
import com.app.widget.ShelfListDialog;
import com.app.widget.SimplePairListPopupWindow;
import com.app.widget.VerificationCodeDialog;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;
import com.base.util.D;
import com.base.util.comm.TimeUtils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 扫描清单(货架详情)
 * @author pythoner
 * 
 */
public class DocumentListActivity extends BaseActivity implements OnClickListener{

	private Context context;
	private TextView btn_status,btn_startDate,btn_userId,btn_clear_userId;
	private ListView listView;
	private CommonAdapter<ProdCheckDataInfo> adapter;
	private List<ProdCheckDataInfo> beans=new ArrayList<ProdCheckDataInfo>();
	private ProdCheckDataInfo curBean;
	private int prodPreChcekData_id;
	private ProdPreChcekData prodPreChcekData;//货架
	private List<ProdPreChcekData> prodPreChcekDatas;//货架
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_document_list);
		context = this;
		prodPreChcekData_id=getIntent().getIntExtra("ProdPreChcekData_id", 0);
//		initActionBar("扫描清单(本地)", "", App.res.getDrawable(R.drawable.icon_order_more));
		initActionBar("扫描清单", "云端", null);
		initViews();
		findProdCheckData();
		updateViews(beans);
	}

	private void findProdCheckData(){
		List<ProdCheckDataInfo> list=null;
		if(prodPreChcekData_id==0){//全部单据
//			DataSupport.select("title", "content")  
//	        .where("ProdPreChcekData_id = ?", "0").order("CreateTime desc").limit(10).offset(10)
//	        .find(ProdCheckData.class);  
//			list = DataSupport.findAll(ProdCheckData.class); 
			list = DataSupport.order("create_time desc").find(ProdCheckDataInfo.class); 
			if(!isEmptyList(list)){
				for(ProdCheckDataInfo bean:list){
					if(bean.getProdPreChcekData_id()!=0){
//						查询对应的货架
						List<ProdPreChcekData> infos = DataSupport.where("ProdPreChcekData_id = ?", String.valueOf(bean.getProdPreChcekData_id())).find(ProdPreChcekData.class);
						if(!isEmptyList(infos)){
							bean.setProdPreChcekData(infos.get(0));
						}
					}
				}
			}
		}else{
			list = DataSupport.where("ProdPreChcekData_id = ?", String.valueOf(prodPreChcekData_id)).order("create_time desc").find(ProdCheckDataInfo.class); 
			if(!isEmptyList(list)){
				List<ProdPreChcekData> infos = DataSupport.where("ProdPreChcekData_id = ?", String.valueOf(prodPreChcekData_id)).find(ProdPreChcekData.class);
				if(!isEmptyList(infos)){
					prodPreChcekData=infos.get(0);
				}
				if(prodPreChcekData!=null){
					for(ProdCheckDataInfo bean:list){
						bean.setProdPreChcekData(prodPreChcekData);
					}
				}else{
//					prodPreChcekData =new ProdPreChcekData();
//					prodPreChcekData.setProdPreChcekData_id(prodPreChcekData_id);
//					prodPreChcekData.setShelfCode(shelfCode);
//					prodPreChcekData.setTotalQty(TotalQty);
				}
			}else{
				showToast("没有记录，请添加");
				Intent intent=new Intent(context,CreateDocumentActivity.class);
				intent.putExtra("ProdPreChcekData_id", prodPreChcekData_id);
				intent.putExtra("ShelfCode", getIntent().getStringExtra("ShelfCode"));
				intent.putExtra("TotalQty", getIntent().getStringExtra("TotalQty"));
				intent.putExtra("ID", getIntent().getIntExtra("ID", 0));
				startActivity(intent);
				finish();
			}
		}
		beans.clear();
		beans.addAll(list);
	}
	
	@Override
	public void doRightButtonClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=new Intent(context,DocumentListFromServerActivity.class);
		startActivity(intent);
		
//		ArrayList<Pair<Integer, String>> beans = new ArrayList<Pair<Integer, String>>();
//		beans.add(new Pair<Integer, String>(0, "云汇总"));
//		beans.add(new Pair<Integer, String>(1, "清空"));
//		
//		View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
//		final SimplePairListPopupWindow<Integer, String> popupWindow = new SimplePairListPopupWindow<Integer, String>(context,view, (int)App.res.getDimension(R.dimen.popupwindow_width), beans);
//		popupWindow.showAsDropDown(v, 0, 0);
//		popupWindow.setOnItemClickListener(new SimplePairListPopupWindow.OnItemClickListener<Integer, String>() {
//
//			@Override
//			public void onItemClick(int position, Pair<Integer, String> pair) {
//				// TODO Auto-generated method stub
//				Intent intent=null;
//				switch (pair.first) {
//				case 0://汇总
//					intent=new Intent(context,DocumentListFromServerActivity.class);
//					startActivity(intent);
//					break;
//				case 1://清空
//					final VerificationCodeDialog d=new VerificationCodeDialog(context);
//					d.setOnClickListener(new VerificationCodeDialog.OnClickListener() {
//						
//						@Override
//						public void onClick(View v, String text) {
//							// TODO Auto-generated method stub
//							DataSupport.deleteAll(ProdCheckDataInfo.class);
//							DataSupport.deleteAll(ProdCheckDtl.class);
//							DocumentListActivity.this.beans.clear();
//							notifyDataSetChanged();
//							d.dismiss();
//						}
//					});
//					d.show();
//					break;
//				}
//			}
//		});
		
	}
	
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		btn_status=$(R.id.btn_status);
		btn_status.setOnClickListener(this);
		btn_startDate=$(R.id.btn_startDate);
		btn_startDate.setOnClickListener(this);
//		btn_endDate=$(R.id.btn_endDate);
//		btn_endDate.setOnClickListener(this);
		$(R.id.btn_search).setOnClickListener(this);
		View btn_ok=$(R.id.btn_ok);
		btn_ok.setOnClickListener(this);
		$(R.id.btn_clear).setOnClickListener(this);
		btn_userId=$(R.id.btn_userId);
		btn_userId.setOnClickListener(this);
		btn_clear_userId=$(R.id.btn_clear_userId);
		btn_clear_userId.setOnClickListener(this);
		btn_clear_userId.setVisibility(btn_userId.getText().length()==0?View.GONE:View.VISIBLE);
		
		listView=$(R.id.listView);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				//传递商品
				ProdCheckDataInfo bean=beans.get(position);
				List<ProdCheckDtl> prods=DataSupport.where("DocID = ?", String.valueOf(bean.getId())).find(ProdCheckDtl.class); 
				bean.setProdcheckdtl(prods);
				Intent intent=new Intent(context,DocumentDetailActivity.class);
				intent.putExtra("ProdCheckData", bean);
				startActivity(intent);
			}
		});
		listView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				// TODO Auto-generated method stub
				String text="是否删除单据 ？";
				if(beans.get(position).getStatus()==1){
					text="是否删除单据 ？\n（此操作只针对本地数据，不会影响服务端数据！）";
				}
				D.showDialog(DocumentListActivity.this, text, "确定", "取消", new D.OnPositiveListener() {
					
					@Override
					public void onPositive() {
						// TODO Auto-generated method stub
						if(beans.get(position).getProdPreChcekData()!=null){
							beans.get(position).delete();
						}
						DataSupport.delete(ProdCheckDataInfo.class, beans.get(position).getId());
						DataSupport.deleteAll(ProdCheckDtl.class, "DocID = ?", String.valueOf(beans.get(position).getId())); 
						beans.remove(position);
						updateViews(beans);
					}
				});
				return true;
			}
		});
	}
	
	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
		if(obj==null){
			return;
		}
		notifyDataSetChanged();
	}

	private void notifyDataSetChanged(){
		if(adapter==null){
			listView.setAdapter(adapter = new CommonAdapter<ProdCheckDataInfo>( context, beans,
					  R.layout.item_shelves_detail){
					  
					@Override
					public void setValues(ViewHolder helper, final ProdCheckDataInfo item, final int position) {
						// TODO Auto-generated method stub
						String tmp=null;
						if(item.getDoc_num().length()>15){
							tmp="..."+item.getDoc_num().substring(10);
						}else{
							tmp=item.getDoc_num();
						}
						helper.setText(R.id.item_0, "单据："+tmp);
						TextView item_2=helper.getView(R.id.item_2);
						if(item.getProdPreChcekData()==null){//
							helper.setText(R.id.item_1, "预点数：");
							item_2.setText("未分配");
							item_2.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									curBean=item;
									showSimpleListDialog();
								}
							});
//							item_2.setBackgroundResource(R.drawable.bg_btn_primary);
						}else{
							helper.setText(R.id.item_1, "预点数："+item.getProdPreChcekData().getTotal_qty());
							item_2.setText(item.getProdPreChcekData().getShelf_code());
							item_2.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									curBean=item;
									showSimpleListDialog();
								}
							});
//							item_2.setBackgroundResource(R.drawable.bg_btn_primary);
						}
						setThemeDrawable(context, item_2);
						helper.setText(R.id.item_3, "扫描数："+item.getTotal_qty());
						TextView item_1=helper.getView(R.id.item_1);
						TextView item_3=helper.getView(R.id.item_3);
						if(item.getProdPreChcekData()!=null&&String.valueOf(item.getTotal_qty()).equals(item.getProdPreChcekData().getTotal_qty())){
							item_1.setTextColor(App.res.getColor(R.color.fittting_green));
							item_3.setTextColor(App.res.getColor(R.color.fittting_green));
						}else{
							item_1.setTextColor(App.res.getColor(R.color.fittting_red));
							item_3.setTextColor(App.res.getColor(R.color.fittting_red));
						}
						
						helper.setText(R.id.item_4, item.getCreate_time());
						helper.setText(R.id.item_5, item.getCreate_user());
						TextView item_6=helper.getView(R.id.item_6);
						if(item.getStatus()==0){
							item_6.setTextColor(getResources().getColor(R.color.fittting_red));
							item_6.setText("未上传");
						}else{
							item_6.setTextColor(getResources().getColor(R.color.fittting_green));
							item_6.setText("已上传");
						}
					}
			});
		}else{
			adapter.notifyDataSetChanged();
		}
	}
	private void showSimpleListDialog(){
		if(prodPreChcekDatas==null){
			doCommandGetProdPreCheckData();
		}else{
			showSimpleListDialog(prodPreChcekDatas);
		}
	}
	private void doCommandGetProdPreCheckData(){
		String shop_code=App.user.getShopInfo().getShop_code();
		String creator=null;
		String shelf_code=null;
//		String start_date=null;//"2016-02-14"
//		String end_date=null;//"2016-02-14"
		String today=TimeUtils.getNow("yyyy-MM-dd");
		String start_date=today;
		String end_date=today;
		Commands.doCommandGetProdPreCheckData(context, shop_code, shelf_code, start_date, end_date, creator, 1,200,new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				if(isSuccess(response)){
					GetProdPreCheckDataResponse obj=mapperToObject(response, GetProdPreCheckDataResponse.class);
					if(obj!=null){
						prodPreChcekDatas=obj.getPrecheck_Info();
						for(ProdPreChcekData b:prodPreChcekDatas){//备份赋值
							b.setProdPreChcekData_id(b.getId());
						}
						showSimpleListDialog(prodPreChcekDatas);
					}
				}
			}
		},true);
	}
	
	private void showSimpleListDialog(List<ProdPreChcekData> list){
		final ShelfListDialog d=new ShelfListDialog(context, list);
		d.setOnItemClickListener(new ShelfListDialog.OnItemClickListener() {
			
			@Override
			public void onItemClick(View v, ProdPreChcekData item, int position) {
				// TODO Auto-generated method stub
				List<ProdCheckDataInfo> docs=DataSupport.where("prodPreChcekData_id = ?", ""+item.getId()).find(ProdCheckDataInfo.class); 
				if(isEmptyList(docs)){
					ContentValues values = new ContentValues();
					values.put("ProdPreChcekData_id", item.getId());
					values.put("Status", 0);
					DataSupport.update(ProdCheckDataInfo.class, values, curBean.getId());
					curBean.setProdPreChcekData_id(item.getId());//
					curBean.setProdPreChcekData(item);
					curBean.setStatus(0);
					notifyDataSetChanged();
					item.save();
					d.dismiss();
				}else{
					showToast("该货架已被占用");
				}
			}
		});
		
		d.setOnResultListener(new ShelfListDialog.OnResultListener() {

			@Override
			public void onResult(List<ProdPreChcekData> list) {
				// TODO Auto-generated method stub
				if(!isEmptyList(list)){
					ProdPreChcekData item=list.get(0);
					
					ContentValues values = new ContentValues();
					values.put("ProdPreChcekData_id", item.getId());
					values.put("status", 0);
					int id = DataSupport.update(ProdCheckDataInfo.class, values, curBean.getId());
					curBean.setProdPreChcekData_id(item.getId());//
					curBean.setProdPreChcekData(item);
					curBean.setStatus(0);
					notifyDataSetChanged();
					item.save();
					
					EventBus.getDefault().post(App.EVENT_UPDATE_INVENTORY);
					d.dismiss();
				}else{
					showToast("No data");
				}
				
			}
		});
		d.show();
	}
	
	@Subscriber
	void updateByEventBus(String event) {
		if(event.equals(App.EVENT_UPDATE_DOCUMENT_LIST)){
			reset();
			findProdCheckData();
			updateViews(beans);
		}
	}
	
	private void showDateDialog(final TextView v){
		com.widget.view.DatePickerDialog d=new com.widget.view.DatePickerDialog(context,v.getText().toString());
		d.setOnButtonClickListener(new com.widget.view.DatePickerDialog.OnButtonClickListener() {
			
			@Override
			public void onOKClick(String year, String month, String date) {
				// TODO Auto-generated method stub
				v.setText(year+"-"+month+"-"+date);
				startDate=v.getText().toString();
				findProdCheckData(status,startDate,userId);
			}
			
			@Override
			public void onCancelClick() {
				// TODO Auto-generated method stub
				v.setText("");
				startDate=v.getText().toString();
				findProdCheckData(status,startDate,userId);
			}
		});
		d.show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_status:
			showSimplePopupWindow(v);
			break;
		case R.id.btn_startDate:
			showDateDialog(btn_startDate);
			break;
//		case R.id.btn_endDate:
//			showDateDialog(btn_endDate);
//			break;
		case R.id.btn_search:
			userId=btn_userId.getText().toString().trim();
			findProdCheckData(status,startDate,userId);
			break;
		case R.id.btn_ok://添加单据
			Intent intent=new Intent(context,CreateDocumentActivity.class);
			if(prodPreChcekData!=null){
				intent.putExtra("ProdPreChcekData_id", prodPreChcekData.getProdPreChcekData_id());
			}
			startActivity(intent);
			break;
		case R.id.btn_clear:
			final VerificationCodeDialog d=new VerificationCodeDialog(context);
			d.setOnClickListener(new VerificationCodeDialog.OnClickListener() {
				
				@Override
				public void onClick(View v, String text) {
					// TODO Auto-generated method stub
					DataSupport.deleteAll(ProdCheckDataInfo.class);
					DataSupport.deleteAll(ProdCheckDtl.class);
					DocumentListActivity.this.beans.clear();
					notifyDataSetChanged();
				}
			});
			d.show();
			break;
		case R.id.btn_userId:
			doCommandGetShopUserListAndShowDialog();
			break;
		case R.id.btn_clear_userId:
			btn_userId.setText("");
			btn_clear_userId.setVisibility(View.GONE);
			userId=btn_userId.getText().toString().trim();
			findProdCheckData(status,startDate,userId);
			break;

		default:
			break;
		}
	}
	
	private void doCommandGetShopUserListAndShowDialog(){
		doCommandGetShopUserListAndShowDialog(new OnSelectUserInfoListener() {
			
			@Override
			public void doResult(UserInfo instance) {
				// TODO Auto-generated method stub
				btn_userId.setText(instance.getUser_code());
				btn_clear_userId.setVisibility(btn_userId.getText().length()==0?View.GONE:View.VISIBLE);
				userId=btn_userId.getText().toString().trim();
				findProdCheckData(status,startDate,userId);
			}
		});
	}
	
	private int status=-1;
	private String userId="",startDate="";
	private void showSimplePopupWindow(final View v){
		ArrayList<Pair<Integer, String>> beans = new ArrayList<Pair<Integer, String>>();
		beans.add(new Pair<Integer, String>(0, "全部"));
		beans.add(new Pair<Integer, String>(1, "已上传"));
		beans.add(new Pair<Integer, String>(2, "未上传"));
		
		View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
		final SimplePairListPopupWindow<Integer, String> popupWindow = new SimplePairListPopupWindow<Integer, String>(context,view, (int)App.res.getDimension(R.dimen.popupwindow_width), beans);
		popupWindow.showAsDropDown(v, 0, 0);
		popupWindow.setOnItemClickListener(new SimplePairListPopupWindow.OnItemClickListener<Integer, String>() {

			@Override
			public void onItemClick(int position, Pair<Integer, String> pair) {
				// TODO Auto-generated method stub
				TextView tv=(TextView)v;
				tv.setText(pair.second);
				switch (pair.first) {
				case 0://全部
					status=-1;
					break;
				case 1://已上传
					status=1;
					break;
				case 2://未上传
					status=0;
					break;
				}
				findProdCheckData(status,startDate,userId);
			}
		});
	}

	private void findProdCheckData(int status,String startDate ,String userId){
		List<ProdCheckDataInfo> list=null;
		if(prodPreChcekData_id==0){//全部单据
			if(status==-1){
				list = DataSupport.where("create_time like ? and create_user like ?", "%"+startDate+"%","%"+userId+"%").order("create_time desc").find(ProdCheckDataInfo.class); 
			}else{
				list = DataSupport.where("status = ? and create_time like ? and create_user like ?", String.valueOf(status),"%"+startDate+"%","%"+userId+"%").order("create_time desc").find(ProdCheckDataInfo.class); 
			}
			if(!isEmptyList(list)){
				for(ProdCheckDataInfo bean:list){
					if(bean.getProdPreChcekData_id()!=0){
//						查询对应的货架
						List<ProdPreChcekData> infos = DataSupport.where("ProdPreChcekData_id = ?", String.valueOf(bean.getProdPreChcekData_id())).order("create_time_string desc").find(ProdPreChcekData.class);
						if(!isEmptyList(infos)){
							bean.setProdPreChcekData(infos.get(0));
						}
					}
				}
			}else{
				showToast("没有找到记录");
			}
		}else{
			if(status==-1){
				list = DataSupport.where("ProdPreChcekData_id = ? and create_time like ? and create_user like ?", String.valueOf(prodPreChcekData_id),"%"+startDate+"%","%"+userId+"%").order("create_time desc").find(ProdCheckDataInfo.class); 
			}else{
				list = DataSupport.where("ProdPreChcekData_id = ? and status = ? and create_time like ? and create_user like ?", String.valueOf(prodPreChcekData_id), String.valueOf(status),"%"+startDate+"%","%"+userId+"%").order("create_time desc").find(ProdCheckDataInfo.class); 
			}
			if(!isEmptyList(list)){
				List<ProdPreChcekData> infos = DataSupport.where("ProdPreChcekData_id = ?", String.valueOf(prodPreChcekData_id)).find(ProdPreChcekData.class);
				if(!isEmptyList(infos)){
					prodPreChcekData=infos.get(0);
				}
				if(prodPreChcekData!=null){
					for(ProdCheckDataInfo bean:list){
						bean.setProdPreChcekData(prodPreChcekData);
					}
				}else{
//					prodPreChcekData =new ProdPreChcekData();
//					prodPreChcekData.setProdPreChcekData_id(prodPreChcekData_id);
//					prodPreChcekData.setShelfCode(shelfCode);
//					prodPreChcekData.setTotalQty(TotalQty);
				}
			}else{
				showToast("没有找到记录");
			}
		}
		beans.clear();
		beans.addAll(list);
		notifyDataSetChanged();
	}
	private void reset(){
		status=-1;
		userId="";
		startDate="";
		btn_status.setText("");
		btn_startDate.setText("");
		btn_userId.setText("");
		btn_clear_userId.setVisibility(View.GONE);
	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		updateTheme();
	}
	
	private void updateTheme() {
		setThemeDrawable(this, R.id.btn_ok);
		setThemeDrawable(this, R.id.btn_clear);
		if(adapter!=null){
			adapter.notifyDataSetChanged();
		}
	}
}
