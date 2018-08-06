package com.app.xstore.inventory;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.simple.eventbus.Subscriber;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.model.Bill;
import com.app.model.BillProduct;
import com.app.model.Goods;
import com.app.model.LocAreaInfo;
import com.app.model.ProdCheckDataInfo;
import com.app.model.ProdCheckDtl;
import com.app.model.response.GetGoodsListResponse;
import com.app.model.response.GetLocInfoListResponse;
import com.app.model.response.UploadProdCheckDataResponse;
import com.app.net.Commands;
import com.app.widget.BillUploadDialog;
import com.app.widget.SimpleListDialog;
import com.app.widget.SimplePairListPopupWindow;
import com.app.widget.VerificationCodeDialog;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;
import com.base.util.D;

/**
 * 扫描清单(货架详情)
 * @author pythoner
 * 
 */
public class OfflineDocumentListActivity extends BaseActivity implements OnClickListener{

	private Context context;
	private TextView btn_status,btn_startDate,/*btn_userId,btn_clear_userId,*/btn_clear_startDate,tv_count;
	private ListView listView;
	private CommonAdapter<Bill> adapter;
	private List<Bill> beans=new ArrayList<Bill>();
	private List<Bill> beansUnload=new ArrayList<Bill>();//当前页未上传的单据，是beans的子集
	private String locID=null;//货位
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offline_document_list);
		context = this;
//		boolean canUpload=getIntent().getBooleanExtra("CanUpload", false);
//		if(canUpload){
//			updateAll();
//			initActionBar("扫描清单", "校验并上传", null);
//		}else{
			initActionBar("扫描清单", "上传", null);
//		}
		initViews();
		findBill();
		updateViews(beans);
	}

	private void updateAll(){
		ContentValues values = new ContentValues();  
//		values.put("userId", getUser(context).getUserID());
		values.put("userId", App.user.getUserInfo().getUser_code());
		DataSupport.updateAll(Bill.class, values, "userId = ?", ""); 
	}
	private void findBill(){
		List<Bill> list=null;
		list = DataSupport.order("createTime desc").find(Bill.class); 
		beans.clear();
		beans.addAll(list);
	}
	
	@Override
	public void doRightButtonClick(View v) {
		// TODO Auto-generated method stub
		if(!isNetworkAvailable()){
			showToast("没有联网");
			return;
		}
		
		beansUnload.clear();
//		count=0;
//		countUnlaw=0;
		for(Bill bean:beans){
			if(bean.getStatus()==0){//未上传
				beansUnload.add(bean);
			}
		}
		if(isEmptyList(beansUnload)){
			showToast("没有需要上传的数据");
			return;
		}
//		doCommandGetLocInfoList();
		
		//直接上传不校验
//		doCommandUploadProdCheckData(beansUnload);
		
		BillUploadDialog d=new BillUploadDialog(context,beansUnload);
		d.show();
	}
	
	private void doCheckAndUpload(){
//		if(isFastMode){
			doCheck(beansUnload);
//		}else{
//			for(Bill bean:beansUnload){
//				doCheck(bean);
//			}
//		}
	}
	
	private void doCommandGetLocInfoList(){
		String shop_code=App.user.getShopInfo().getShop_code();
		String loc_id=null;
		Commands.doCommandGetLocInfoList(context,shop_code,loc_id, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				if(isSuccess(response)){
					GetLocInfoListResponse obj=mapperToObject(response, GetLocInfoListResponse.class);
					if(obj!=null){
//						List<ShopLocInfo> shopLocInfos=obj.getShopLocInfoList();
						List<LocAreaInfo> shopLocInfos=obj.getLocAreaInfoList();
						if(isEmptyList(shopLocInfos)){
							showToast("检测到没有货位，直接上传");
							doCheckAndUpload();
						}else{
//							showShopLocInfosDialog(obj.getShopLocInfoList());
							showShopLocInfosDialog(obj.getLocAreaInfoList());
						}
					}
				}
			}
		});
	}
	
	private void showShopLocInfosDialog(List<LocAreaInfo> list){
		SimpleListDialog<LocAreaInfo> d=new SimpleListDialog<LocAreaInfo>(context, list);
		d.setOnItemClickListener(new SimpleListDialog.OnItemClickListener<LocAreaInfo>() {
			
			@Override
			public void onItemClick(View v, LocAreaInfo item, int position) {
				// TODO Auto-generated method stub
//				locID=item.getLocID();
				locID=item.getLoc_id();
				doCheckAndUpload();
			}
		});
		d.show();
	}
	
	private boolean isFastMode=true;//高效率模式
	private void doCheck(final List<Bill> bills){
		//
		final List<BillProduct> products=new ArrayList<BillProduct>();
		for(Bill bill:bills){
			List<BillProduct> prods=DataSupport.where("documentNo = ?", bill.getDocNum()).find(BillProduct.class); 
			bill.setBillProducts(prods);
			products.addAll(prods);
		}
		
//		List<ProdCheckDtl> needCheck=new ArrayList<ProdCheckDtl>();
//		for(BillProduct product:products){//BillProduct转换成ProdCheckDtl
////			if(isEmpty(product.getBarcode())||isEmpty(product.getProdNum())){
//				ProdCheckDtl d=new ProdCheckDtl();
////				d.setBarcode(product.getBarcode());
////				d.setProdNum(product.getProdNum());
//				d.setProd_num(product.getProdNum());
//				d.setProd_name(product.getProdName());
//				needCheck.add(d);
////			}
//		}
		
		List<String> needCheck=new ArrayList<String>();
		for(BillProduct product:products){//BillProduct转换成ProdCheckDtl
			needCheck.add(product.getProdNum());
		}
		if(isEmptyList(needCheck)){
			return;
		}
		
		String shop_code=App.user.getShopInfo().getShop_code();
		Commands.doCommandGetGoodsList(context, shop_code,needCheck, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
//				Log.i("tag", response.toString());
				if (isSuccess(response)) {
					GetGoodsListResponse obj = mapperToObject(response, GetGoodsListResponse.class);
					if (obj != null) {
						List<Goods> dtls=obj.getGoodsInfo();
						for(Goods one:dtls){
							for(BillProduct two:products){
								if(!isEmpty(one.getGoods_sn())&&one.getGoods_sn().equals(two.getProdNum())){
									two.setProdName(one.getGoods_name());
									continue;
								}
							}
						}
//						notifyDataSetChanged();
//						for(BillProduct bean:products){
//							if(isEmpty(bean.getProd_name())||isEmpty(bean.getProd_num())){
//								showToast("校验失败，商品"+bean.getProd_num()+"不存在");
//								return;
//							}
//						}
//						
//						if(isNext){
//							doNext();
//						}
						
						updateViews(beans);
						
						if(checkLegal(products)){
							doCommandUploadProdCheckData(bills);
						}else{
							showToast("上传失败，有些商品编码不存在");
						}
						
					}
				}
			}
		});
		
//		Commands.doCommandGetProdCodeInfo(context, needCheck, new Listener<JSONObject>() {
//			@Override
//			public void onResponse(JSONObject response) {
////				Log.i("tag", response.toString());
//				if (isSuccess(response)) {
//					count++;
//					GetProdCodeInfoResponse obj = mapperToObject(response, GetProdCodeInfoResponse.class);
//						List<ProdCheckDtl> dtls=obj.getList();
//						for(ProdCheckDtl one:dtls){
//							for(BillProduct product:products){
//								if(!isEmpty(one.getBarcode())&&one.getBarcode().equals(product.getBarcode())){
//									product.setProdNum(one.getProdNum());
////									product.setProdName(one.getProdName());
////									update(product);
//									continue;
//								}else if(!isEmpty(one.getProdNum())&&one.getProdNum().equals(product.getProdNum())){
//									product.setBarcode(one.getBarcode());
////									product.setProdName(one.getProdName());
////									update(product);
//									continue;
//								}
//							}
//						}
//						updateViews(beans);
//						
//						if(checkLegal(products)){
//							doCommandUploadProdCheckData(bills);
//						}else{
//							showToast("上传失败，有些商品编码不存在");
//						}
//				}
//			}
//		});
		
	}
	
//	private int unLegalPosition=-1;//校验失败的Item位置
	private boolean checkLegal(List<BillProduct> products){
//		unLegalPosition=-1;
//		int count=0;
		for(BillProduct bean:products){
			if(isEmpty(bean.getProdName())||isEmpty(bean.getProdNum())){
//				for(Bill bill:beansUnload){
//					for(BillProduct product:bill.getBillProducts()){
//						if(!isEmpty(bean.getProdName())&&bean.getProdName().equals(product.getProdName())){
//							unLegalPosition=count;
//							break;//只匹配到第一个不合法的即可，提高效率
//						}else if(!isEmpty(bean.getProdNum())&&bean.getProdNum().equals(product.getProdNum())){
//							unLegalPosition=count;
//							break;
//						}
//					}
//					count++;
//				}
				return false;
			}
		}
		return true;
	}
	
//	private int count=0;//已经校验完成的Bill的数量
//	private int countUnlaw=0;//校验不通过的单据的数量
//	private void doCheck(final Bill bill){
//		List<BillProduct> prods=DataSupport.where("documentNo = ?", bill.getDocNum()).find(BillProduct.class); 
//		bill.setBillProducts(prods);
//		
//		final List<BillProduct> products=bill.getBillProducts();
//		List<ProdCheckDtl> needCheck=new ArrayList<ProdCheckDtl>();
//		if(products!=null){
//			for(BillProduct product:products){//BillProduct转换成ProdCheckDtl
////				if(isEmpty(product.getBarcode())||isEmpty(product.getProdNum())){
//					ProdCheckDtl d=new ProdCheckDtl();
//					d.setBarcode(product.getBarcode());
//					d.setProdNum(product.getProdNum());
//					needCheck.add(d);
////				}
//			}
//		}
//		if(isEmptyList(needCheck)){
//			return;
//		}
//		Commands.doCommandGetProdCodeInfo(context, needCheck, new Listener<JSONObject>() {
//			@Override
//			public void onResponse(JSONObject response) {
////				Log.i("tag", response.toString());
//				if (isSuccess(response)) {
//					count++;
//					GetProdCodeInfoResponse obj = mapperToObject(response, GetProdCodeInfoResponse.class);
//						List<ProdCheckDtl> dtls=obj.getList();
//						for(ProdCheckDtl one:dtls){
//							for(BillProduct product:products){
//								if(!isEmpty(one.getBarcode())&&one.getBarcode().equals(product.getBarcode())){
//									product.setProdNum(one.getProdNum());
////									product.setProdName(one.getProdName());
////									update(product);
//									continue;
//								}else if(!isEmpty(one.getProdNum())&&one.getProdNum().equals(product.getProdNum())){
//									product.setBarcode(one.getBarcode());
////									product.setProdName(one.getProdName());
////									update(product);
//									continue;
//								}
//							}
//						}
//						updateViews(beans);
//						
//						if(checkLegal(bill)){//如果都合法，就不需要校验
//							//最后一条加载完毕&全部校验通过
//							if(count==beansUnload.size()&&countUnlaw==0){
//								doCommandUploadProdCheckData(beansUnload);
//							}
//						}else{
//							countUnlaw++;
//							showToast("上传失败，有些商品编码不存在");
//							return;
//						}
//				}
//			}
//		});
//	}
	
	private void doCommandUploadProdCheckData(final List<Bill> bills){
		//将List<Bill>转换成List<ProdCheckData>
		List<ProdCheckDataInfo> datas=new ArrayList<ProdCheckDataInfo>();
		for(Bill bill:bills){
			ProdCheckDataInfo d=new ProdCheckDataInfo();
			d.setDoc_num("");
			d.setRemark(bill.getRemark());//
			d.setShelf_code(bill.getShelfCode());
			d.setTotal_qty(bill.getTotalQty());
//			d.setUserId(bill.getUserId());
			d.setCreate_user(bill.getUserId());
			d.setCreate_time(bill.getCreateTime());
			d.setLoc_id(locID);//补上货位
			d.setFloor(locID);//补上货位
			List<BillProduct> products=bill.getBillProducts();
			List<ProdCheckDtl> dtls=new ArrayList<ProdCheckDtl>();
			for(BillProduct product:products){
				ProdCheckDtl p=new ProdCheckDtl();
//				p.setBarcode(product.getBarcode());
				p.setProd_num(product.getProdNum());
				p.setProd_name(product.getProdName());
				p.setQty(product.getQty());
				dtls.add(p);
			}
			d.setProdcheckdtl(dtls);
			datas.add(d);
		}
		
		String shop_code=App.user.getShopInfo().getShop_code();
		String creator=App.user.getUserInfo().getUser_code();
//		String shelf_code=App.user.getUserInfo().getUser_code();
		int checkPriority=1;//0 校验,1不校验， 2 提示 （默认为0，校验）	
		boolean isUploadWithoutCheck=true;//直接上传或者上传前与预盘数据对比的标志，默认为false，上传前需要与预盘数据对比。
		Commands.doCommandUploadProdCheckData(context, shop_code,creator,datas, isUploadWithoutCheck, checkPriority,new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				if(isSuccess(response)){
					UploadProdCheckDataResponse obj=mapperToObject(response, UploadProdCheckDataResponse.class);
					if(obj!=null){
//						ProdCheckData bean=null;
						if(isEmptyList(obj.getProdCheckDataResult())){
							showToast("数据返回不正确");
							return;
						}
						for(Bill bill:bills){
							bill.setStatus(1);//已上传
							update(bill);
						}
						showToast("上传成功");
						//需要扫描清单界面需要刷新
						findBillByConditions(status,startDate,userId);
					}
				}
			}
		});
	}
	
	private boolean checkLegal(Bill bill){
		if(isEmptyList(bill.getBillProducts())){
			return false;
		}
		for(BillProduct bean:bill.getBillProducts()){
			if(isEmpty(bean.getBarcode())||isEmpty(bean.getProdNum())){
				return false;
			}
		}
		return true;
	}
	
//	private void update(BillProduct bean){
//		bean.update(bean.getId());
//	}
	private void update(Bill bean){
		bean.update(bean.getId());
	}
	
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		btn_status=$(R.id.btn_status);
		btn_status.setOnClickListener(this);
		btn_startDate=$(R.id.btn_startDate);
		btn_startDate.setOnClickListener(this);
		tv_count=$(R.id.tv_count);
//		btn_endDate=$(R.id.btn_endDate);
//		btn_endDate.setOnClickListener(this);
//		$(R.id.btn_search).setOnClickListener(this);
		$(R.id.btn_ok).setOnClickListener(this);
		$(R.id.btn_clear).setOnClickListener(this);
//		btn_userId=$(R.id.btn_userId);
//		btn_userId.setOnClickListener(this);
//		btn_clear_userId=$(R.id.btn_clear_userId);
//		btn_clear_userId.setOnClickListener(this);
//		btn_clear_userId.setVisibility(btn_userId.getText().length()==0?View.GONE:View.VISIBLE);
		btn_clear_startDate=$(R.id.btn_clear_startDate);
		btn_clear_startDate.setOnClickListener(this);
		btn_clear_startDate.setVisibility(btn_startDate.getText().length()==0?View.GONE:View.VISIBLE);
		
		listView=$(R.id.listView);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				//传递商品
				Bill bean=beans.get(position);
				List<BillProduct> prods=DataSupport.where("documentNo = ?", bean.getDocNum()).find(BillProduct.class); 
				bean.setBillProducts(prods);
				Intent intent=new Intent(context,OfflineDocumentCreateActivity.class);
				intent.putExtra("Bill", bean);
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
				D.showDialog(OfflineDocumentListActivity.this, text, "确定", "取消", new D.OnPositiveListener() {
					
					@Override
					public void onPositive() {
						// TODO Auto-generated method stub
						DataSupport.delete(Bill.class, beans.get(position).getId());
						DataSupport.deleteAll(BillProduct.class, "documentNo = ?", beans.get(position).getDocNum()); 
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
		tv_count.setText("单据数："+beans.size());
		notifyDataSetChanged();
	}

	private void notifyDataSetChanged(){
		if(adapter==null){
			listView.setAdapter(adapter = new CommonAdapter<Bill>( context, beans,
					  R.layout.item_shelves_detail_offline){
					  
					@Override
					public void setValues(ViewHolder helper, final Bill item, final int position) {
						// TODO Auto-generated method stub
						helper.setText(R.id.item_0, "单据："+item.getDocNum());
						TextView item_2=helper.getView(R.id.item_2);
						item_2.setText("货架："+item.getShelfCode());
						helper.setText(R.id.item_3, "扫描数："+item.getTotalQty());
						helper.setText(R.id.item_4, item.getCreateTime());
						helper.setText(R.id.item_5, item.getUserId());
						TextView item_6=helper.getView(R.id.item_6);
						if(item.getStatus()==0){
							item_6.setTextColor(getResources().getColor(R.color.fittting_red));
							item_6.setText("未上传");
						}else{
							item_6.setTextColor(getResources().getColor(R.color.fittting_green));
							item_6.setText("已上传");
						}
						
						if(item.getStatus()==0){
							if(checkLegal(item)){
								helper.getConvertView().setBackgroundColor(getResources().getColor(R.color.fittting_green));
							}else{
								helper.getConvertView().setBackgroundColor(Color.TRANSPARENT);
							}
						}else{//已上传的底色置灰
							helper.getConvertView().setBackgroundColor(getResources().getColor(R.color.grayWhite));
						}
						//检验不通过的Item
//						if(unLegalPosition==position){
//							helper.getConvertView().setBackgroundColor(getResources().getColor(R.color.fittting_red));
//						}
					}
			});
		}else{
			adapter.notifyDataSetChanged();
		}
	}
	
	@Subscriber
	void updateByEventBus(String event) {
		if(event.equals(App.EVENT_UPDATE_DOCUMENT_LIST)){
			reset();
			findBill();
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
				btn_clear_startDate.setVisibility(View.VISIBLE);
				findBillByConditions(status,startDate,userId);
			}
			
			@Override
			public void onCancelClick() {
				// TODO Auto-generated method stub
				v.setText("");
				startDate=v.getText().toString();
				btn_clear_startDate.setVisibility(View.GONE);
				findBillByConditions(status,startDate,userId);
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
//		case R.id.btn_search:
//			userId=btn_userId.getText().toString().trim();
//			findBillByConditions(status,startDate,userId);
//			break;
		case R.id.btn_ok://添加单据
			Intent intent=new Intent(context,OfflineDocumentCreateActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_clear:
			final VerificationCodeDialog d=new VerificationCodeDialog(context);
			d.setOnClickListener(new VerificationCodeDialog.OnClickListener() {
				
				@Override
				public void onClick(View v, String text) {
					// TODO Auto-generated method stub
					DataSupport.deleteAll(Bill.class);
					DataSupport.deleteAll(BillProduct.class);
					beans.clear();
					updateViews(beans);
				}
			});
			d.show();
			break;
//		case R.id.btn_userId:
//			doCommandGetSalesCombxObjectsAndShowDialog();
//			break;
//		case R.id.btn_clear_userId:
//			btn_userId.setText("");
//			btn_clear_userId.setVisibility(View.GONE);
//			userId=btn_userId.getText().toString().trim();
//			findBillByConditions(status,startDate,userId);
//			break;
		case R.id.btn_clear_startDate:
			btn_startDate.setText("");
			btn_clear_startDate.setVisibility(View.GONE);
			startDate=btn_startDate.getText().toString().trim();
			findBillByConditions(status,startDate,userId);
			break;

		default:
			break;
		}
	}
	
//	private void doCommandGetSalesCombxObjectsAndShowDialog(){
//		doCommandGetSalesCombxObjectsAndShowDialog(new OnSelectSysUserListener() {
//			
//			@Override
//			public void doResult(SysUser instance) {
//				// TODO Auto-generated method stub
//				btn_userId.setText(instance.getUserID());
//				btn_clear_userId.setVisibility(btn_userId.getText().length()==0?View.GONE:View.VISIBLE);
//				userId=btn_userId.getText().toString().trim();
//				findBillByConditions(status,startDate,userId);
//			}
//		});
//	}
	
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
				findBillByConditions(status,startDate,userId);
			}
		});
	}

	private void findBillByConditions(int status,String startDate ,String userId){
		List<Bill> list=null;
		if(status==-1){//全部
			list = DataSupport.where("createTime like ? and userId like ?", "%"+startDate+"%","%"+userId+"%").order("createTime desc").find(Bill.class); 
		}else{
			list = DataSupport.where("status = ? and createTime like ? and userId like ?", String.valueOf(status),"%"+startDate+"%","%"+userId+"%").order("createTime desc").find(Bill.class); 
		}
		beans.clear();
		beans.addAll(list);
		updateViews(beans);
	}
	private void reset(){
		status=-1;
		userId="";
		startDate="";
		btn_status.setText("");
		btn_startDate.setText("");
//		btn_userId.setText("");
//		btn_clear_userId.setVisibility(View.GONE);
		btn_clear_startDate.setVisibility(View.GONE);
	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		updateTheme();
	}
	
	private void updateTheme() {
		setThemeDrawable(this, R.id.btn_ok);
//		if(adapter!=null){
//			adapter.notifyDataSetChanged();
//		}
	}
}
