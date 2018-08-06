package com.app.xstore.inventory;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.simple.eventbus.EventBus;

import com.android.volley.Response.Listener;
import com.app.model.LocAreaInfo;
import com.app.model.ProdCheckDataInfo;
import com.app.model.ProdPreChcekData;
import com.app.model.response.GetLocInfoListResponse;
import com.app.model.response.GetProdCheckMarkListResponse;
import com.app.model.response.UploadProdCheckDataResponse;
import com.app.net.Commands;
import com.app.widget.LocAreaInfoListDialog;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.base.util.D;
import com.base.util.comm.SPUtils;
import com.widget.flowlayout.FlowLayout;
import com.widget.flowlayout.TagAdapter;
import com.widget.flowlayout.TagFlowLayout;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

/**
 * 盘点
 * @author pythoner
 * 
 */
public class DocumentUploadActivity extends BaseActivity implements View.OnClickListener{

	private Context context;
	private EditText et_remark;
	private RadioGroup rg_floor;
	private RadioGroup rg;
	private TextView btn_floor,btn_areaCode;
	private TagFlowLayout tagLayout;
	private ProdCheckDataInfo data;
	private ProdPreChcekData prodPreChcekData;//货架
	private GetLocInfoListResponse obj;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_document_upload);
		context = this;
		data=getIntent().getParcelableExtra("ProdCheckData");
		if(data==null){
			return;
		}
		if(data.getProdPreChcekData_id()==-1){//从服务端过来，没有Shelf ID
			ProdPreChcekData shelf=new ProdPreChcekData();
			shelf.setProdPreChcekData_id(data.getProdPreChcekData_id());//
			shelf.setShelf_code(data.getShelf_code());
			shelf.setTotal_qty(data.getPrecheck_qty());
			prodPreChcekData=shelf;
			data.setProdPreChcekData(prodPreChcekData);
		}else{
			List<ProdPreChcekData> infos = DataSupport.where("ProdPreChcekData_id = ?", String.valueOf(data.getProdPreChcekData_id())).find(ProdPreChcekData.class);
			if(!isEmptyList(infos)){
				prodPreChcekData=infos.get(0);
			}
			data.setProdPreChcekData(prodPreChcekData);
		}
		
		initActionBar("数据上传",null, null);//"校验设置"
		initViews();
		
		doCommandGetLocInfoList();
		doCommandGetProdCheckMarkList();
	}

	private void doCommandGetLocInfoList(){
		String shop_code=App.user.getShopInfo().getShop_code();
		String loc_id=null;
		Commands.doCommandGetLocInfoList(context, shop_code,loc_id,new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
//				{"ErrMessage":"","Result":true,"LocAreaInfoList":[],"ErrSysTrackMessage":"","ErrSysMessage":"","Message":"获取成功"}
				if(isSuccess(response)){
					obj=mapperToObject(response, GetLocInfoListResponse.class);
					if(obj!=null){
//						List<ShopLocInfo> shopLocInfos=obj.getShopLocInfoList();
//						if(isEmptyList(shopLocInfos)){
//							rg_floor.check(R.id.rb_1_floor);
//							btn_floor.setVisibility(View.GONE);
//							btn_floor.setText("");
//						}else{
//							rg_floor.check(R.id.rb_0_floor);
//							btn_floor.setVisibility(View.VISIBLE);
//							if(!isEmpty(data.getFloor())){
//								btn_floor.setText(data.getFloor());
//							}
//						}
						List<LocAreaInfo> locAreaInfos=obj.getLocAreaInfoList();
						if(isEmptyList(locAreaInfos)){
							rg.check(R.id.rb_1);
							btn_areaCode.setVisibility(View.GONE);
							btn_areaCode.setText("");
						}else{
							rg.check(R.id.rb_0);
							btn_areaCode.setVisibility(View.VISIBLE);
							if(!isEmpty(data.getAera_code())){
								btn_areaCode.setText(data.getAera_code());
							}
						}
						
					}
				}
			}
		});
	}
	
//	private void showShopLocInfosDialog(List<ShopLocInfo> list){
//		SimpleListDialog<ShopLocInfo> d=new SimpleListDialog<ShopLocInfo>(context, list);
//		d.setOnItemClickListener(new SimpleListDialog.OnItemClickListener<ShopLocInfo>() {
//			
//			@Override
//			public void onItemClick(View v, ShopLocInfo item, int position) {
//				// TODO Auto-generated method stub
//				btn_floor.setText(item.getLocID());
//			}
//		});
//		d.show();
//	}
	
	private void showLocAreaInfosDialog(List<LocAreaInfo> list){
		String curLocID=btn_floor.getText().toString();
		LocAreaInfoListDialog d=new LocAreaInfoListDialog(context,curLocID);
		d.setOnItemClickListener(new LocAreaInfoListDialog.OnItemClickListener() {
			
			@Override
			public void onItemClick(View v, LocAreaInfo item) {
				// TODO Auto-generated method stub
				btn_areaCode.setText(item.getArea());
			}
		});
		d.show();
	}
	
	private void doCommandGetProdCheckMarkList(){
		Commands.doCommandGetProdCheckMarkList(context, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				if(isSuccess(response)){
					GetProdCheckMarkListResponse obj=mapperToObject(response, GetProdCheckMarkListResponse.class);
					if(obj!=null){
						final LayoutInflater inflater = LayoutInflater.from(context);
						tagLayout.setAdapter(new TagAdapter<String>(obj.getMarkList())
						{
							@Override
							public View getView(FlowLayout parent, int position, String item)
							{
								TextView tv = (TextView) inflater.inflate(R.layout.item_text,tagLayout, false);
								tv.setText(item);
								return tv;
							}
						});
						if(!isEmpty(data.getMark_type())){
							int i=0;
							for(String m:obj.getMarkList()){
								if(m.equals(data.getMark_type())){
									tagLayout.setCheckedAt(i);
									tagLayout.notifyDataChanged();
									break;
								}
								i++;
							}
						}
					}
				}
			}
		});
		
	}
	
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		TextView tv_no=$(R.id.tv_no);
		TextView et_locId=$(R.id.et_locId);
		et_remark=$(R.id.et_remark);
		if(!isEmpty(data.getRemark())){
			et_remark.setText(data.getRemark());
		}
		
		tv_no.setText("单据："+data.getDoc_num());
		et_locId.setText("货架："+(prodPreChcekData==null?"未分配":prodPreChcekData.getShelf_code()));
		
		btn_areaCode=$(R.id.btn_areaCode);
		btn_areaCode.setOnClickListener(this);
		btn_floor=$(R.id.btn_floor);
		btn_floor.setOnClickListener(this);
		rg_floor=$(R.id.rg_floor);
		rg=$(R.id.rg);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rb_0:
					btn_areaCode.setVisibility(View.VISIBLE);
					break;
				case R.id.rb_1:
					btn_areaCode.setVisibility(View.GONE);
					break;

				default:
					break;
				}
			}
		});
		rg.check(R.id.rb_0);
		
		tagLayout=(TagFlowLayout)findViewById(R.id.tagLayout);
		View btn_upload=$(R.id.btn_upload);
		btn_upload.setOnClickListener(this);
	}
	
	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
//		case R.id.btn_floor://分楼层
//			if(obj==null){
//				return;
//			}
//			showShopLocInfosDialog(obj.getShopLocInfoList());
//			break;
		case R.id.btn_areaCode://分区域
			if(obj==null){
				return;
			}
			showLocAreaInfosDialog(obj.getLocAreaInfoList());
			break;
		case R.id.btn_upload:
//			int total_qty=Integer.parseInt(prodPreChcekData.getTotal_qty());
//			int count=data.getProdcheckdtl().size();
//			if(total_qty==count){
				doUpload();
//			}else{
//				D.showDialog(DocumentUploadActivity.this, "盘点数据与预盘的数据不相等", "继续上传", "取消", new D.OnPositiveListener(){
//
//					@Override
//					public void onPositive() {
//						// TODO Auto-generated method stub
//						doUpload();
//					}
//					
//				});
//			}
			break;

		default:
			break;
		}
	}
	
	private void doUpload(){
		String DocNum=data.getDoc_num();
		if(DocNum.length()==13){//临时单号
			data.setDoc_num("");//清空临时单号，作新增
		}else{//保留原始单号，作更新
		}
//		if(btn_floor.getVisibility()==View.VISIBLE){
//			if(btn_floor.getText().length()==0){
//				doAnimation(context, btn_floor, R.anim.shake);
//				return;
//			}
//		}
//		if(btn_areaCode.getVisibility()==View.VISIBLE){
//			if(btn_areaCode.getText().length()==0){
//				doAnimation(context, btn_areaCode, R.anim.shake);
//				return;
//			}
//		}
		
		data.setLoc_id(btn_floor.getText().toString());
		data.setShelf_code(prodPreChcekData.getShelf_code());
		
		data.setFloor(btn_floor.getText().toString());
		data.setAera_code(btn_areaCode.getText().toString());
		data.setRemark(et_remark.getText().toString().trim());
		
		ArrayList<String> labels=tagLayout.getCheckedItems();
		if(isEmptyList(labels)){
			doAnimation(context, tagLayout, R.anim.shake);
			return;
		}
		data.setMark_type(labels.get(0));
		//
//		data.setCreateTime(TimeUtils.getCurrentTimeInString());
		data.setPrecheck_qty(prodPreChcekData.getTotal_qty());
		
		int checkPriority=(Integer)SPUtils.get(context, App.KEY_CHECK_SETTING, 0);//默认校验
		doCommandUploadProdCheckData(checkPriority);
	}
	
	private void doCommandUploadProdCheckData(int checkPriority){//IsUploadWithoutCheck
		List<ProdCheckDataInfo> datas=new ArrayList<ProdCheckDataInfo>();
		datas.add(data);
		String shop_code=App.user.getShopInfo().getShop_code();
		String creator=App.user.getUserInfo().getUser_code();
//		String shelf_code=App.user.getUserInfo().getUser_code();
//		int checkPriority=1;//0 校验,1不校验， 2 提示 （默认为0，校验）	
		boolean isUploadWithoutCheck=true;//直接上传或者上传前与预盘数据对比的标志，默认为false，上传前需要与预盘数据对比。
		Commands.doCommandUploadProdCheckData(context, shop_code,creator,datas, isUploadWithoutCheck, checkPriority,new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
//				{"ErrMessage":"","Result":true,"UploadFailType":null,"ErrSysTrackMessage":"","ErrSysMessage":"","Message":"上传数据成功","NoProdNumRelatedBarcode":null,"ProdCheckDataResult":[{"total_qty":2,"shelf_code":"007","shop_id":null,"remark":"iiii","aera_code":"abc","precheck_qty":2,"last_modify_user":null,"mark_type":"四面架","id":2,"floor":"","prodcheckdtl":[{"id":2,"barcode":"","qty":2,"prod_num":"21122820146","prod_name":"","pd_prod_check_doc_num":"00S0011602160001"}],"loc_id":"","last_modify_time":"0001-01-01T00:00:00","create_time":"2016-02-16T16:33:05.545192+08:00","create_user":"U001","doc_num":"00S0011602160001"}],"NotEqualPreCheckData":[]}
				if(isSuccess(response)){
					UploadProdCheckDataResponse obj=mapperToObject(response, UploadProdCheckDataResponse.class);
					if(obj!=null){
						int checkPriority=(Integer)SPUtils.get(context, App.KEY_CHECK_SETTING, 0);
						String uploadFailType=obj.getUploadFailType();
						if(checkPriority==2){//提示
							if(!isEmpty(uploadFailType)&&uploadFailType.equalsIgnoreCase("ProdNumNotExist")){
								String message="商品编码在数据库中不存在，是否忽略继续上传？";//上传的商品编码在数据库中不存在
								showAlertDialog(message);
								return;
							}
						}else if(checkPriority==0){//校验
							if(!isEmpty(uploadFailType)){//ProdNumNotExist
								final List<String> list=obj.getNoProdNumRelatedBarcode();
								if(!isEmptyList(list)){
									StringBuffer sb=new StringBuffer();
									for(String b:list){
										sb.append(",").append(b);
									}
									D.showDialog(DocumentUploadActivity.this, "商品"+sb.substring(1).toString()+"不存在，校验失败", "返回","关闭",new D.OnPositiveListener(){

										@Override
										public void onPositive() {
											// TODO Auto-generated method stub
											EventBus.getDefault().post(list, App.EVENT_UPDATE_DOCUMENT_DETAIL);
											finish();
										}
										
									});
								}
								return;
							}
						}else{//不校验
							
						}
						
						ProdCheckDataInfo bean=null;
						if(!isEmptyList(obj.getProdCheckDataResult())){
							bean=obj.getProdCheckDataResult().get(0);
						}
						if(bean==null){
							showToast("数据返回不正确");
							return;
						}
						ContentValues values = new ContentValues();  
						values.put("status",1); 
						values.put("create_time", bean.getCreate_time());
						values.put("doc_num", bean.getDoc_num());
						values.put("remark", bean.getRemark());
						values.put("aera_code", bean.getAera_code());
						values.put("mark_type", bean.getMark_type());
						values.put("floor", bean.getFloor());
						DataSupport.update(ProdCheckDataInfo.class, values, data.getId());
						/*if(bean.getProdInfoList()!=null){
							for(ProdCheckDtl b:bean.getProdInfoList()){
								ContentValues val = new ContentValues();  
								val.put("Barcode",b.getBarcode());
								val.put("ProdNum",b.getProdNum());
								DataSupport.updateAll(ProdCheckDtl.class, val, "DocID = ?", String.valueOf(data.getId()));
							}
						}*/
						showToast("上传成功");
						//需要告诉扫描清单界面需要刷新
						EventBus.getDefault().post(App.EVENT_UPDATE_DOCUMENT_LIST);
						EventBus.getDefault().post(App.EVENT_FINISH);
						finish();
					}
				}
			}
		});
	}
	
	@Override
	public void doRightButtonClick(View v) {
		// TODO Auto-generated method stub
//		showSimplePopupWindow(v);
		String[] items=new String[]{"校验","不校验","提示"};
		int checkedItems=(Integer)SPUtils.get(context, App.KEY_CHECK_SETTING, 0);
		D.showDialog(DocumentUploadActivity.this, items, checkedItems, new D.OnItemSelectListener(){

			@Override
			public void onItemSelect(int which) {
				// TODO Auto-generated method stub
				SPUtils.put(context, App.KEY_CHECK_SETTING, which);
			}
			
		});
		
	}
	
//	private void showSimplePopupWindow(View v){
//		ArrayList<Pair<Integer, String>> beans = new ArrayList<Pair<Integer, String>>();
//		beans.add(new Pair<Integer, String>(0, "校验"));
//		beans.add(new Pair<Integer, String>(1, "不校验"));
//		beans.add(new Pair<Integer, String>(2, "提示"));
//		
//		View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
//		final SimplePairListPopupWindow<Integer, String> popupWindow = new SimplePairListPopupWindow<Integer, String>(context,view, (int)App.res.getDimension(R.dimen.popupwindow_width), beans);
//		popupWindow.showAsDropDown(v, 0, 0);
//		popupWindow.setOnItemClickListener(new SimplePairListPopupWindow.OnItemClickListener<Integer, String>() {
//
//			@Override
//			public void onItemClick(int position, Pair<Integer, String> pair) {
//				// TODO Auto-generated method stub
//				switch (pair.first) {
//				case 0://禁止
//				case 1://允许
//				case 2://提示
//					showToast("您选择了"+pair.second);
//					SPUtils.put(context, App.KEY_CHECK_SETTING, pair.first);
//					break;
//				default:
//					showToast(R.string.error_unknown_function);
//					break;
//				}
//			}
//		});
//	}
	
	private void showAlertDialog(String message){
		D.showDialog(DocumentUploadActivity.this, message, "继续上传", "取消", new D.OnPositiveListener() {
			
			@Override
			public void onPositive() {
				// TODO Auto-generated method stub
				int checkPriority=1;//0 校验,1不校验， 2 提示 （默认为0，校验）	
				doCommandUploadProdCheckData(checkPriority);
			}
		});
	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		updateTheme();
	}
	
	private void updateTheme() {
		setThemeDrawable(this, R.id.btn_upload);
	}
	
}
