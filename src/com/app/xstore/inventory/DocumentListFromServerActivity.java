package com.app.xstore.inventory;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import com.android.volley.Response.Listener;
import com.app.model.LocAreaInfo;
import com.app.model.ProdCheckDataInfo;
import com.app.model.ShopLocInfo;
import com.app.model.UserInfo;
import com.app.model.response.GetProdCheckDataHeadListResponse;
import com.app.model.response.GetProdCheckDataResponse;
import com.app.model.response.GetProdCheckDtlDataResponse;
import com.app.net.Commands;
import com.app.widget.LocAreaInfoListDialog;
import com.app.widget.SimpleListDialog;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;
import com.base.util.D;
import com.base.util.comm.TimeUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 判断汇总（服务端）
 * 
 * @author pythoner
 * 
 */
public class DocumentListFromServerActivity extends BaseActivity implements OnClickListener {

	private Context context;
	private TextView btn_startDate,btn_endDate,btn_userId,btn_floor,btn_areaCode;
	private ListView listView;
	private CommonAdapter<ProdCheckDataInfo> adapter;
	private List<ProdCheckDataInfo> beans = new ArrayList<ProdCheckDataInfo>();
	private ProdCheckDataInfo curBean;
//	private GetLocInfoListResponse obj;
	private View btn_clear_floor, btn_clear_areaCode,btn_clear_userId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_document_list_from_server);
		context = this;
		initActionBar("云汇总", null, null);
		initViews();
		
		doCommandGetProdCheckDataHeadList();
	}

	private void doCommandGetProdCheckDataHeadList() {
//		String Creator=btn_userId.getText().toString();
//		String StartDate=btn_startDate.getText().toString();
//		String EndDate=btn_endDate.getText().toString();
//		String LocID=btn_floor.getText().toString();
//		String AreaCode=btn_areaCode.getText().toString();
		
		String shop_code=App.user.getShopInfo().getShop_code();
		String loc_id=btn_floor.getText().toString();
		String area_code=btn_areaCode.getText().toString();
		String shelf_code=null;
		String startDate=btn_startDate.getText().toString();
		String endDate=btn_endDate.getText().toString();
		String creator=btn_userId.getText().toString();
		Commands.doCommandGetProdCheckDataHeadList(context, shop_code, loc_id, area_code, shelf_code, startDate, endDate,
				creator,new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				 Log.i("tag", response.toString());
				if (isSuccess(response)) {
					GetProdCheckDataHeadListResponse obj = mapperToObject(response,GetProdCheckDataHeadListResponse.class);
					if (obj != null) {
						beans.clear();
						beans.addAll(obj.getCheckDataList());
						updateViews(beans);
					}
				}
			}
		});
	}

	@Override
	public void doRightButtonClick(View v) {
		// TODO Auto-generated method stub
//		et_userId.setText("");
//		btn_date.setText("");
//		String EndDate=today;
//		
//		doCommandGetProdCheckDataHeadList();
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		String today=TimeUtils.getNow("yyyy-MM-dd");
		btn_startDate = $(R.id.btn_startDate);
		btn_startDate.setText(today);
		btn_startDate.setOnClickListener(this);
		btn_endDate = $(R.id.btn_endDate);
		btn_endDate.setText(today);
		btn_endDate.setOnClickListener(this);
		btn_floor=$(R.id.btn_floor);
		btn_floor.setOnClickListener(this);
		btn_areaCode=$(R.id.btn_areaCode);
		btn_areaCode.setOnClickListener(this);
		$(R.id.btn_search).setOnClickListener(this);
		$(R.id.btn_summary).setOnClickListener(this);
		
		btn_clear_floor=$(R.id.btn_clear_floor);
		btn_clear_floor.setOnClickListener(this);
		btn_clear_floor.setVisibility(btn_floor.getText().length()==0?View.GONE:View.VISIBLE);
		btn_clear_areaCode=$(R.id.btn_clear_areaCode);
		btn_clear_areaCode.setOnClickListener(this);
		btn_clear_areaCode.setVisibility(btn_areaCode.getText().length()==0?View.GONE:View.VISIBLE);

		btn_userId = $(R.id.btn_userId);
		btn_userId.setOnClickListener(this);
		btn_clear_userId = $(R.id.btn_clear_userId);
		btn_clear_userId.setOnClickListener(this);
		btn_clear_userId.setVisibility(btn_userId.getText().length()==0?View.GONE:View.VISIBLE);
		listView = $(R.id.listView);
		listView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				// TODO Auto-generated method stub
				String text = "是否删除单据 ？\n（此操作只针对服务端数据，不会影响本地数据!）";
				D.showDialog(DocumentListFromServerActivity.this, text, "确定", "取消", new D.OnPositiveListener() {

					@Override
					public void onPositive() {
						// TODO Auto-generated method stub
						curBean = beans.get(position);
						doCommandDeleteCheckData(curBean.getDoc_num());
					}
				});
				return true;
			}
		});
	}

	private void doCommandDeleteCheckData(String DocNum) {
		Commands.doCommandDeleteCheckData(context, DocNum, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				// Log.i("tag", response.toString());
				if (isSuccess(response)) {
					beans.remove(curBean);
					notifyDataSetChanged();
				}
			}
		});
	}

//	private void doCommandGetProdCheckDataInfo(String DocNum) {
//		Commands.doCommandGetProdCheckDataInfo(context, DocNum, new Listener<JSONObject>() {
//			
//			@Override
//			public void onResponse(JSONObject response) {
//				// TODO Auto-generated method stub
////				 Log.i("tag", response.toString());
//				if (isSuccess(response)) {
//					GetProdCheckDataInfoResponse obj = mapperToObject(response, GetProdCheckDataInfoResponse.class);
//					if (obj != null) {
//						curBean.setProdInfoList(obj.getDetailInfo());
//						
//						ProdCheckDataInfo head = obj.getHeadInfo();
//						head.setProdInfoList(obj.getDetailInfo());
//						
//						head.setProdPreChcekData_id(-1);//没shelf ID
//						
//						Intent intent = new Intent(context, DocumentDetailActivity.class);
//						intent.putExtra("ProdCheckData", head);
//						startActivity(intent);
//					}
//				}
//			}
//		});
//	}
	private void doCommandGetProdCheckData(String DocNum) {
		String shop_code=null;
		String doc_num=DocNum;
		Commands.doCommandGetProdCheckData(context, shop_code,doc_num, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				 Log.i("tag", response.toString());
				if (isSuccess(response)) {
					GetProdCheckDataResponse obj = mapperToObject(response, GetProdCheckDataResponse.class);
					if (obj != null) {
//						curBean.setProdInfoList(obj.getDetailInfo());
//						
//						ProdCheckDataInfo head = obj.getHeadInfo();
//						head.setProdInfoList(obj.getDetailInfo());
//						
//						head.setProdPreChcekData_id(-1);//没shelf ID
//						
//						Intent intent = new Intent(context, DocumentDetailActivity.class);
//						intent.putExtra("ProdCheckData", head);
//						startActivity(intent);
					}
				}
			}
		});
	}

	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
		if (obj == null) {
			return;
		}
		notifyDataSetChanged();
	}

	private void notifyDataSetChanged() {
		if (adapter == null) {
			listView.setAdapter(
					adapter = new CommonAdapter<ProdCheckDataInfo>(context, beans, R.layout.item_shelves_detail) {

						@Override
						public void setValues(ViewHolder helper, final ProdCheckDataInfo item, final int position) {
							// TODO Auto-generated method stub
							String tmp = null;
							if (item.getDoc_num().length() > 15) {
								tmp = "..." + item.getDoc_num().substring(10);
							} else {
								tmp = item.getDoc_num();
							}
							helper.setText(R.id.item_0, "单据：" + tmp);
							helper.setText(R.id.item_1, "预点数：" + item.getPrecheck_qty());
							TextView item_2 = helper.getView(R.id.item_2);
							item_2.setText(item.getShelf_code());
//							item_2.setOnClickListener(new OnClickListener() {
//
//								@Override
//								public void onClick(View v) {
//									// TODO Auto-generated method stub
//								}
//							});
							item_2.setBackgroundResource(R.drawable.bg_btn_gray_light);
							helper.setText(R.id.item_3, "扫描数：" + item.getTotal_qty());
							
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
//							helper.setText(R.id.item_5, item.Creator);//Creator
							helper.setText(R.id.item_5, "");//Creator
							
							TextView item_6 = helper.getView(R.id.item_6);
							item_6.setVisibility(View.GONE);
							if(item.isIsImportedToErp()){
								item_6.setTextColor(getResources().getColor(R.color.fittting_green));
								item_6.setText("已上传到ERP");
							}else{
								item_6.setTextColor(getResources().getColor(R.color.fittting_red));
								item_6.setText("未上传到ERP");
							}
						}
					});
		} else {
			adapter.notifyDataSetChanged();
		}
	}

	@Subscriber
	void updateByEventBus(String event) {
		if(event.equals(App.EVENT_UPDATE_DOCUMENT_LIST_FROM_SERVER)){
			doCommandGetProdCheckDataHeadList();
		}
	}

	private void showDateDialog(final TextView v) {
		com.widget.view.DatePickerDialog d=new com.widget.view.DatePickerDialog(context,v.getText().toString());
		d.setOnButtonClickListener(new com.widget.view.DatePickerDialog.OnButtonClickListener() {
			
			@Override
			public void onOKClick(String year, String month, String date) {
				// TODO Auto-generated method stub
				v.setText(year+"-"+month+"-"+date);
				doCommandGetProdCheckDataHeadList();
			}
			
			@Override
			public void onCancelClick() {
				// TODO Auto-generated method stub
			}
		});
		d.show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_startDate:
			showDateDialog(btn_startDate);
			break;
		case R.id.btn_endDate:
			showDateDialog(btn_endDate);
			break;
		case R.id.btn_search:
			doCommandGetProdCheckDataHeadList();
			break;
		case R.id.btn_floor:
//			if(obj==null){
//				doCommandGetLocInfoList(btn_floor);
//			}else{
//				showShopLocInfosDialog(obj.getShopLocInfoList());
//			}
			break;
		case R.id.btn_areaCode:
			String locID=btn_floor.getText().toString();
			LocAreaInfoListDialog d=new LocAreaInfoListDialog(context,locID);
			d.setOnItemClickListener(new LocAreaInfoListDialog.OnItemClickListener() {
				
				@Override
				public void onItemClick(View v, LocAreaInfo item) {
					// TODO Auto-generated method stub
					btn_areaCode.setText(item.getArea());
					btn_clear_areaCode.setVisibility(btn_areaCode.getText().length()==0?View.GONE:View.VISIBLE);
				}
			});
			d.show();
			break;
		case R.id.btn_summary:
			if(isEmptyList(beans)){
				showToast("没有需要汇总的单据");
				return;
			}
			doCommandGetProdCheckDtlData();
			break;
		case R.id.btn_clear_floor:
			btn_floor.setText("");
			btn_clear_floor.setVisibility(View.GONE);
			break;
		case R.id.btn_clear_areaCode:
			btn_areaCode.setText("");
			btn_clear_areaCode.setVisibility(View.GONE);
			break;
		case R.id.btn_userId:
			doCommandGetShopUserListAndShowDialog();
			break;
		case R.id.btn_clear_userId:
			btn_userId.setText("");
			btn_clear_userId.setVisibility(View.GONE);
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
				doCommandGetProdCheckDataHeadList();
			}
		});
	}
	
	private void doCommandGetProdCheckDtlData(){
		final ArrayList<String> DocNumList=new ArrayList<String>();
		for(ProdCheckDataInfo bean:beans){
			DocNumList.add(bean.getDoc_num());
		}
		String shop_code=App.user.getShopInfo().getShop_code();
		Commands.doCommandGetProdCheckDtlData(context,shop_code,DocNumList, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
//				{"ErrMessage":"","Result":true,"DetailInfo":[{"id":0,"barcode":"","qty":1,"prod_num":"21122820146","prod_name":"","pd_prod_check_doc_num":"00S0011602170007"},{"id":0,"barcode":"","qty":3,"prod_num":"6907303718325","prod_name":"","pd_prod_check_doc_num":"00S0011602170007"}],"ErrSysTrackMessage":"","ErrSysMessage":"","Message":"获取盘点明细数据成功"}
				if(isSuccess(response)){
					GetProdCheckDtlDataResponse obj=mapperToObject(response, GetProdCheckDtlDataResponse.class);
					if(obj!=null){
						Intent intent =new Intent(context,SummaryERPActivity.class);
						intent.putExtra("ProdCheckDtls", obj.getDetailInfo());
						intent.putStringArrayListExtra("CheckDataDocNumList", DocNumList);
						startActivity(intent);
					}
				}
			}
		});
		
	}
	
	private void showShopLocInfosDialog(List<ShopLocInfo> list){
		SimpleListDialog<ShopLocInfo> d=new SimpleListDialog<ShopLocInfo>(context, list);
		d.setOnItemClickListener(new SimpleListDialog.OnItemClickListener<ShopLocInfo>() {
			
			@Override
			public void onItemClick(View v, ShopLocInfo item, int position) {
				// TODO Auto-generated method stub
				btn_floor.setText(item.getLocID());
				btn_clear_floor.setVisibility(btn_floor.getText().length()==0?View.GONE:View.VISIBLE);
			}
		});
		d.show();
	}
	private void showLocAreaInfosDialog(List<LocAreaInfo> list){
		SimpleListDialog<LocAreaInfo> d=new SimpleListDialog<LocAreaInfo>(context, list);
		d.setOnItemClickListener(new SimpleListDialog.OnItemClickListener<LocAreaInfo>() {
			
			@Override
			public void onItemClick(View v, LocAreaInfo item, int position) {
				// TODO Auto-generated method stub
				btn_areaCode.setText(item.getArea());
				btn_clear_areaCode.setVisibility(btn_areaCode.getText().length()==0?View.GONE:View.VISIBLE);
			}
		});
		d.show();
	}
	
	private void doCommandGetLocInfoList(final View view){
//		Commands.doCommandGetLocInfoList(context, new Listener<JSONObject>() {
//
//			@Override
//			public void onResponse(JSONObject response) {
//				// TODO Auto-generated method stub
////				Log.i("tag", response.toString());
//				if(isSuccess(response)){
//					obj=mapperToObject(response, GetLocInfoListResponse.class);
//					if(obj!=null){
//						if(view==btn_floor){
//							showShopLocInfosDialog(obj.getShopLocInfoList());
//						}else if(view==btn_areaCode){
//							showLocAreaInfosDialog(obj.getLocAreaInfoList());
//						}
//					}
//				}
//			}
//		});
	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		updateTheme();
	}
	
	private void updateTheme() {
		setThemeDrawable(this, R.id.btn_search);
		setThemeDrawable(this, R.id.btn_summary);
//		if(adapter!=null){
//			adapter.notifyDataSetChanged();
//		}
	}
	
}
