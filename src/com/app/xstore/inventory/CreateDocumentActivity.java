package com.app.xstore.inventory;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.model.Box;
import com.app.model.Goods;
import com.app.model.ProdCheckDataInfo;
import com.app.model.ProdCheckDtl;
import com.app.model.ProdPreChcekData;
import com.app.model.response.GetGoodsListResponse;
import com.app.model.response.GetProdPreCheckDataResponse;
import com.app.net.Commands;
import com.app.widget.ShelfListDialog;
import com.app.widget.SimpleNumberDialog;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.GoodsDetailActivity;
import com.app.xstore.pandian.custom.CustomGoods;
import com.app.xstore.pandian.custom.CustomPanDianProduct;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;
import com.base.util.D;
import com.base.util.comm.TimeUtils;

/**
 * 新增单据
 * @author pythoner
 * 
 */
public class CreateDocumentActivity extends BaseActivity implements OnClickListener{

	private Context context;
	private TextView tv_no,tv_scanNum;
	private TextView tv_yuNum,tv_locID;
	private String documentNo;//单据号，根据时间生成
	private EditText et_prodID;
	private ListView listView;
	private CommonAdapter<ProdCheckDtl> adapter;
	private ProdCheckDataInfo data;
	private int prodPreChcekData_id;
//	private String tip="请先校验；长按可删除；";
	private int curPosition=-1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_document);
		context = this;
		prodPreChcekData_id=getIntent().getIntExtra("ProdPreChcekData_id", 0);
		initActionBar(prodPreChcekData_id==0?"创建单据":"商品明细", "下一步", null);
		documentNo=String.valueOf(System.currentTimeMillis());
		
		// 是否读取暂存的数据,如果是从货架直接进来的，则不需要；如果点击的是右上角“扫描”则需要；
		int needRead = getIntent().getIntExtra("NeedRead", 0);
		List<ProdCheckDtl> list = null;
		if (needRead == 0) {
			list = DataSupport.where("DocID = ?", "-100").find(ProdCheckDtl.class);
		}
		
		data=new ProdCheckDataInfo();//生成单据
		data.setProdPreChcekData_id(prodPreChcekData_id);
		data.setDoc_num(documentNo);
//		data.setUserId(getUser(context).getUserID());
		data.setCreate_user(App.user.getUserInfo().getUser_code());
		data.setCreate_time(TimeUtils.getCurrentTimeInString());
		if(list==null){
			data.setProdcheckdtl(new ArrayList<ProdCheckDtl>());
		}else{
			data.setProdcheckdtl(list);
		}
		data.setStatus(0);//未上传
		initViews();
		updateViews(data.getProdcheckdtl());
		createFloatView(0);
		
//		$(R.id.btn_box).setOnClickListener(this);//箱码模式
//		tv_box=$(R.id.tv_box);
//		tv_box.setText("箱数:"+DataSupport.count(Box.class));
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		removeFloatView();
	}
	
	@Override
	public void doRightButtonClick(View v) {
		// TODO Auto-generated method stub
		if(isEmptyList(data.getProdcheckdtl())){
			showToast("商品列表不能为空");
			return;
		}
		doCheck(true);//先校验
//		if(checkLegal()){//如果都合法，就不需要校验
//			doNext();
//		}else{
//			D.showDialog(CreateDocumentActivity.this, tip, "知道了");
//		}
	}
	
	private void doNext(){
		ProdPreChcekData shelf=data.getProdPreChcekData();
		if(shelf==null){
			showToast("未分配货架");
			return;
		}
		if(data.getTotal_qty()>Integer.parseInt(shelf.getTotal_qty())){
			D.showDialog(CreateDocumentActivity.this, "预点数为"+shelf.getTotal_qty()+"，扫描数为"+data.getTotal_qty()+"，确定要上传吗？", "确定", "取消", new D.OnPositiveListener() {
				
				@Override
				public void onPositive() {
					// TODO Auto-generated method stub
					changeToDocumentUploadActivity();
				}
			});
		}else{
			changeToDocumentUploadActivity();
		}
	}

	private void changeToDocumentUploadActivity(){
		saveDatas();
		Intent intent =new Intent(context,DocumentUploadActivity.class);
		intent.putExtra("ProdCheckData", data);
		startActivity(intent);
	}
	
	@Override
	public void onScanProductHandleMessage(String prodID){
		try {
			int i = 0;
			boolean isExist = false;
			for (ProdCheckDtl bean : data.getProdcheckdtl()) {
				if (bean.getProd_num().equals(prodID)) {// !isEmpty(bean.getProdNum())&&
					bean.setQty(bean.getQty() + 1);
					isExist = true;
					curPosition = i;
					break;
				}
				i++;
			}
			if (!isExist) {
				ProdCheckDtl o = new ProdCheckDtl();
				o.setProd_num(prodID);
				o.setProd_name("");
				o.setQty(1);
				o.setDocNum(documentNo);//
				o.setDocID(data.getId());
				data.getProdcheckdtl().add(o);
				curPosition = data.getProdcheckdtl().size() - 1;
			}
			updateViews(data.getProdcheckdtl());
		} catch (Exception e) {
			e.printStackTrace();
			saveDatas();
			showToast("出错了，数据已保存");
		}
	}
	
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		tv_no=$(R.id.tv_no);
		tv_no.setText(documentNo);
		tv_scanNum=$(R.id.tv_scanNum);
		tv_scanNum.setText("扫描数：");
		
		tv_yuNum=$(R.id.tv_yuNum);
		tv_locID=$(R.id.tv_locID);
		if(prodPreChcekData_id==0){//创建单据
			tv_yuNum.setText("预点数：");
			tv_locID.setText("未分配");
			tv_locID.setTextColor(Color.WHITE);
			tv_locID.setOnClickListener(this);
			data.setProdPreChcekData_id(0);//
			data.setProdPreChcekData(null);
		}else{
			ProdPreChcekData bean=null;
			List<ProdPreChcekData> infos = DataSupport.where("ProdPreChcekData_id = ?", String.valueOf(prodPreChcekData_id)).find(ProdPreChcekData.class);
			if(!isEmptyList(infos)){
				bean=infos.get(0);
			}
			if(bean!=null){
				tv_yuNum.setText("预点数："+bean.getTotal_qty());
				tv_locID.setText(bean.getShelf_code());
				tv_locID.setTextColor(App.res.getColor(R.color.grayDark));
				tv_locID.setBackgroundColor(Color.TRANSPARENT);
				data.setProdPreChcekData_id(prodPreChcekData_id);//bean.getID()
				data.setProdPreChcekData(bean);
			}else{
				bean=new ProdPreChcekData();
				bean.setProdPreChcekData_id(prodPreChcekData_id);
				bean.setShelf_code(getIntent().getStringExtra("ShelfCode"));
				bean.setTotal_qty(getIntent().getStringExtra("TotalQty"));
				bean.setId(getIntent().getIntExtra("ID", 0));//???
				tv_yuNum.setText("预点数："+bean.getTotal_qty());
				tv_locID.setText(bean.getShelf_code());
				tv_locID.setTextColor(App.res.getColor(R.color.grayDark));
				tv_locID.setBackgroundColor(Color.TRANSPARENT);
				data.setProdPreChcekData_id(prodPreChcekData_id);//bean.getID()
				data.setProdPreChcekData(bean);
			}
		}
		
		et_prodID=$(R.id.et_prodID);
		
		$(R.id.btn_ok).setOnClickListener(this);
		$(R.id.btn_summary).setOnClickListener(this);
		$(R.id.btn_check).setOnClickListener(this);
		$(R.id.btn_clear).setOnClickListener(this);
		
		listView=$(R.id.listView);
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				ProdCheckDtl bean=data.getProdcheckdtl().get(position);
				if(!isEmpty(bean.getProd_num())){
					Intent intent =new Intent(context, GoodsDetailActivity.class);
					intent.putExtra("ProdNum", bean.getProd_num());
					startActivity(intent);
				}
			}
		});
		listView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				final ProdCheckDtl bean=data.getProdcheckdtl().get(position);
				D.showDialog(CreateDocumentActivity.this, "是否删除？", "确定", "取消", new D.OnPositiveListener() {
					
					@Override
					public void onPositive() {
						// TODO Auto-generated method stub
						data.getProdcheckdtl().remove(bean);
						updateViews(data.getProdcheckdtl());
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
		et_prodID.setText("");
		int count=0;
		for(ProdCheckDtl prod:data.getProdcheckdtl()){
			count+=prod.getQty();
		}
		data.setTotal_qty(count);
		tv_scanNum.setText("扫描数："+data.getTotal_qty());
	}

	private void notifyDataSetChanged(){
		if(adapter==null){
			listView.setAdapter(adapter = new CommonAdapter<ProdCheckDtl>( context, data.getProdcheckdtl(),
					  R.layout.item_create_document){
					  
					@Override
					public void setValues(ViewHolder helper, final ProdCheckDtl item, final int position) {
						// TODO Auto-generated method stub
						helper.setText(R.id.item_0, item.getProd_num());
						helper.setText(R.id.item_1, item.getProd_name());
//						helper.setText(R.id.item_2, String.valueOf(item.getQty()));
						final TextView item_2=helper.getView(R.id.item_2);
						item_2.setText(String.valueOf(item.getQty()));
						item_2.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								SimpleNumberDialog d = new SimpleNumberDialog(context, item_2.getText().toString(), "数量");
								d.setOnClickListener(new SimpleNumberDialog.OnClickListener() {
									
									@Override
									public void onClick(View v, String text) {
										// TODO Auto-generated method stub
										item.setQty(Integer.parseInt(text));
										updateViews(data.getProdcheckdtl());
									}
								});
								d.show();
							}
						});
						
						View container=helper.getView(R.id.container);
						if(curPosition==position){
							container.setBackgroundColor(0xFFFF6633);
						}else{
							container.setBackgroundColor(Color.TRANSPARENT);
						}
						
					}
			});
		}else{
			adapter.notifyDataSetChanged();
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
						List<ProdPreChcekData> list=obj.getPrecheck_Info();
						for(ProdPreChcekData b:list){//备份赋值
							b.setProdPreChcekData_id(b.getId());
						}
						showSimpleListDialog(list);
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
					data.setProdPreChcekData_id(item.getId());//
					data.setProdPreChcekData(item);
					tv_yuNum.setText("预点数："+item.getTotal_qty());
					tv_locID.setText(item.getShelf_code());
					
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
					
					data.setProdPreChcekData_id(item.getId());//
					data.setProdPreChcekData(item);
					tv_yuNum.setText("预点数："+item.getTotal_qty());
					tv_locID.setText(item.getShelf_code());
					
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
		if(event.equals(App.EVENT_FINISH)){
			finish();
		}
//		else if(event.equals(App.EVENT_UPDATE_BOX)){
//			tv_box.setText("箱数:"+DataSupport.count(Box.class));
//		}
	}
	
	@Subscriber(tag = App.EVENT_UPDATE_BOX2, mode = ThreadMode.POST)
	void updateWithMode(Object flag) {
		List<CustomGoods> list=(ArrayList<CustomGoods>)flag;
		for(CustomGoods item:list){
			ProdCheckDtl p=new ProdCheckDtl();
			p.setProd_num(item.getSku_id());
			p.setProd_name(item.getStyle_name());
			p.setQty(1);
			data.getProdcheckdtl().add(p);
		}
		updateViews(data.getProdcheckdtl());
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=null;
		switch (v.getId()) {
//		case R.id.btn_box://箱码模式
//			intent=new Intent(context,BoxListActivity.class);
//			startActivity(intent);
//			break;
		case R.id.tv_locID://
			doCommandGetProdPreCheckData();
			break;
		case R.id.btn_check://校验
			doCheck();
			break;
		case R.id.btn_clear://清空
			D.showDialog(this, "确定要清空吗？", "清空", "取消", new D.OnPositiveListener() {
				
				@Override
				public void onPositive() {
					// TODO Auto-generated method stub
					for(ProdCheckDtl bean:data.getProdcheckdtl()){
						delete(bean);
					}
					data.getProdcheckdtl().clear();
					updateViews(data.getProdcheckdtl());
				}
			});
			break;
		case R.id.btn_ok://扫描商品
			doAdd();
			break;

		default:
			break;
		}
	}
	
	private boolean checkLegal(){
		for(ProdCheckDtl bean:data.getProdcheckdtl()){
			if(isEmpty(bean.getProd_name())||isEmpty(bean.getProd_num())){
				return false;
			}
		}
		return true;
	}
	
	private void doCheck(){
		doCheck(false);
	}
	private void doCheck(final boolean isNext){
		final List<ProdCheckDtl> beans=data.getProdcheckdtl();
		List<String> dtls=null;
		if(beans!=null){
			dtls=new ArrayList<String>();
			for(ProdCheckDtl bean:beans){
				if(isEmpty(bean.getProd_name())||isEmpty(bean.getProd_num())){
					dtls.add(bean.getProd_num());
				}
			}
		}
		if(isEmptyList(dtls)){
			if(isNext){//校验过了
				doNext();
			}
			return;
		}
		String shop_code=App.user.getShopInfo().getShop_code();
		Commands.doCommandGetGoodsList(context, shop_code,dtls, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
//				Log.i("tag", response.toString());
				if (isSuccess(response)) {
					GetGoodsListResponse obj = mapperToObject(response, GetGoodsListResponse.class);
					if (obj != null) {
						List<Goods> dtls=obj.getGoodsInfo();
						for(Goods one:dtls){
							for(ProdCheckDtl two:beans){
								if(one.getGoods_sn().equals(two.getProd_num())){
									two.setProd_name(one.getGoods_name());
									continue;
								}
							}
						}
						notifyDataSetChanged();
						for(ProdCheckDtl bean:beans){
							if(isEmpty(bean.getProd_name())||isEmpty(bean.getProd_num())){
								showToast("校验失败，商品"+bean.getProd_num()+"不存在");
								return;
							}
						}
						
						if(isNext){
							doNext();
						}
					}
				}
			}
		});
	}
	
	private void doAdd(){
		String prodID=et_prodID.getText().toString();
		if(isEmpty(prodID)){
			return;
		}
//		if(prodID.length()>11){
//			showToast("商品编码为11位");
//			return;
//		}else if(prodID.length()<11){
//			doCommandScanBarCodeIntoBillEntityFor11or13(prodID);
//		}else if(prodID.length()==11){
//		if(prodID.length()==11||prodID.length()==13){
			int i=0;
			boolean isExist=false;
			for(ProdCheckDtl bean:data.getProdcheckdtl()){
				if(bean.getProd_num().equals(prodID)){
					bean.setQty(bean.getQty()+1);
					isExist=true;
					curPosition=i;
					break;
				}
				i++;
			}
			if(!isExist){
				ProdCheckDtl o=new ProdCheckDtl();
//				o.setBarcode("");
				o.setProd_num(prodID);
				o.setProd_name("");
				o.setQty(1);
				o.setDocNum(documentNo);//
				o.setDocID(data.getId());
				data.getProdcheckdtl().add(o);
				curPosition=data.getProdcheckdtl().size()-1;
			}
			updateViews(data.getProdcheckdtl());
//		}
//		else{
//			showToast("商品编码为11或13位");
//		}
	}
	
	@Override
	public void doLeftButtonClick(View v) {
		// TODO Auto-generated method stub
		onBackPressed();
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		saveDatas();//返回时保存一次
		super.onBackPressed();
	}
	
	private void saveDatas(){
		if(!isEmptyList(data.getProdcheckdtl())){
			ProdPreChcekData shelf=data.getProdPreChcekData();
			if(shelf!=null){
				shelf.save();
				data.save();
				List<ProdCheckDtl> prods=data.getProdcheckdtl();
				for(ProdCheckDtl prod:prods){
					prod.setDocID(data.getId());
				}
				DataSupport.saveAll(prods);
				EventBus.getDefault().post(App.EVENT_UPDATE_DOCUMENT_LIST);
			}else{
//				DataSupport.deleteAll(ProdCheckDtl.class, "DocID = ?", "-1");
				List<ProdCheckDtl> prods=data.getProdcheckdtl();
				for(ProdCheckDtl prod:prods){
					prod.setDocID(-100);//临时数据
				}
				DataSupport.saveAll(prods);
			}
		}
	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		updateTheme();
	}
	
	private void updateTheme() {
//		setThemeDrawable(this, R.id.tv_box_t);
		setThemeDrawable(this, R.id.btn_ok);
		setThemeDrawable(this, R.id.btn_summary);
		setThemeDrawable(this, R.id.btn_check);
		setThemeDrawable(this, R.id.btn_clear);
		if(prodPreChcekData_id==0){//创建单据
			setThemeDrawable(this,R.id.tv_locID);
		}else{
			$(R.id.tv_locID).setBackgroundColor(Color.TRANSPARENT);
		}
	}
	
	private void delete(ProdCheckDtl bean){
		bean.delete();
	}
	
}
