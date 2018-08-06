package com.app.xstore.inventory;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import android.content.ContentValues;
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
import com.app.model.Goods;
import com.app.model.ProdCheckDataInfo;
import com.app.model.ProdCheckDtl;
import com.app.model.ProdPreChcekData;
import com.app.model.response.GetGoodsListResponse;
import com.app.net.Commands;
import com.app.widget.SimpleNumberDialog;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.GoodsDetailActivity;
import com.app.xstore.pandian.custom.CustomGoods;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;
import com.base.util.D;

/**
 * 单据详情
 * @author pythoner
 * 
 */
public class DocumentDetailActivity extends BaseActivity implements OnClickListener{

	private Context context;
	private TextView tv_scanNum;
	private EditText et_prodID;
	private ListView listView;
	private CommonAdapter<ProdCheckDtl> adapter;
	private ProdCheckDataInfo data;
	private ProdPreChcekData prodPreChcekData;//货架
	private boolean hasChanged=false;
//	private String tip="请先校验；长按可删除；";
	private int curPosition=-1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_document_detail);
		context = this;
		initActionBar("单据详情", "下一步", null);
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
		initViews();
		updateViews(data);
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
//			D.showDialog(DocumentDetailActivity.this, tip, "知道了");
//		}
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
				// o.setBarcode("");
				o.setProd_num(prodID);
				o.setProd_name("");
				o.setQty(1);
				o.setDocNum(data.getDoc_num());//
				o.setDocID(data.getId());
				data.getProdcheckdtl().add(o);
				curPosition = data.getProdcheckdtl().size() - 1;
			}
			updateViews(data.getProdcheckdtl());
		} catch (Exception e) {
			e.printStackTrace();
			showToast("商品列表中Prod_num可能存在null");
		}
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		TextView tv_no=$(R.id.tv_no);
		tv_scanNum=$(R.id.tv_scanNum);//扫描数
		TextView tv_yuNum=$(R.id.tv_yuNum);
		TextView tv_locID=$(R.id.tv_locID);
		
		String tmp=null;
		if(data.getDoc_num().length()>15){
			tmp="..."+data.getDoc_num().substring(10);
		}else{
			tmp=data.getDoc_num();
		}
		tv_no.setText("单据："+tmp);
		tv_scanNum.setText("扫描数："+data.getTotal_qty());
		tv_yuNum.setText("预点数："+(prodPreChcekData==null?"":prodPreChcekData.getTotal_qty()));
		tv_locID.setText("货架："+(prodPreChcekData==null?"未分配":prodPreChcekData.getShelf_code()));
		et_prodID=$(R.id.et_prodID);
		$(R.id.btn_ok).setOnClickListener(this);
		$(R.id.btn_summary).setOnClickListener(this);
		$(R.id.btn_check).setOnClickListener(this);
		$(R.id.btn_clear).setOnClickListener(this);
		
		getRightButton().setVisibility(View.VISIBLE);
		listView=$(R.id.listView);
		listView.setOnItemClickListener(new ListView.OnItemClickListener(){

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
				D.showDialog(DocumentDetailActivity.this, "是否删除？", "确定", "取消", new D.OnPositiveListener() {
					
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
					public void setValues(ViewHolder helper, final ProdCheckDtl item, int position) {
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
			hasChanged=true;//我们可以简单的认为：只要执行过notifyDataSetChanged，就表明数据有变化。
		}
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
//		isChange = true;
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
		case R.id.btn_check://校验
			doCheck();
			break;
		case R.id.btn_clear://清空
			D.showDialog(this, "确定要清空吗？", "清空", "取消", new D.OnPositiveListener() {
				
				@Override
				public void onPositive() {
					// TODO Auto-generated method stub
//					for(ProdCheckDtl bean:data.getProdcheckdtl()){
//						delete(bean);
//					}
					data.getProdcheckdtl().clear();
					updateViews(data.getProdcheckdtl());
				}
			});
			break;
		case R.id.btn_ok:
			doAdd();
			break;

		default:
			break;
		}
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
								if(!isEmpty(one.getGoods_sn())&&one.getGoods_sn().equals(two.getProd_num())){
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
	
	private boolean checkLegal(){
		for(ProdCheckDtl bean:data.getProdcheckdtl()){
			if(isEmpty(bean.getProd_name())||isEmpty(bean.getProd_num())){
				return false;
			}
		}
		return true;
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
				o.setDocNum(data.getDoc_num());//
				o.setDocID(data.getId());
				data.getProdcheckdtl().add(o);
				curPosition=data.getProdcheckdtl().size()-1;
			}
			updateViews(data.getProdcheckdtl());
//		}else{
//			showToast("商品编码为11或13位");
//		}
	}
	
	private void doNext(){
		if(prodPreChcekData==null){
			showToast("未分配货架");
			return;
		}
		if(data.getTotal_qty()!=Integer.parseInt(prodPreChcekData.getTotal_qty())){
			D.showDialog(DocumentDetailActivity.this, "预点数为"+prodPreChcekData.getTotal_qty()+"，扫描数为"+data.getTotal_qty()+"，确定要上传吗？", "确定", "取消", new D.OnPositiveListener() {
				
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
		updateDocument();//上传之前先保存或更新数据
		Intent intent =new Intent(context,DocumentUploadActivity.class);
		intent.putExtra("ProdCheckData", data);
		startActivity(intent);
	}
	
	private void updateDocument(){
		//只有检测到数据有变化时，才需要重新更新数据库里的数据
		if(hasChanged){
			ContentValues values = new ContentValues();  
			values.put("total_qty", data.getTotal_qty());
			values.put("status", 0);//修改后，需要变成 未上传 状态
//			values.put("DocNum", String.valueOf(System.currentTimeMillis()));//单号是否变成本地？？？
			DataSupport.update(ProdCheckDataInfo.class, values, data.getId());
			DataSupport.deleteAll(ProdCheckDtl.class, "DocID = ?", String.valueOf(data.getId()));
			
			if(!isEmptyList(data.getProdcheckdtl())){
				for(ProdCheckDtl b:data.getProdcheckdtl()){
					b.setDocID(data.getId());
				}
				DataSupport.saveAll(data.getProdcheckdtl());
			}
			//需要告诉扫描清单界面需要刷新
			EventBus.getDefault().post(App.EVENT_UPDATE_DOCUMENT_LIST);
			hasChanged=false;
		}
	}
	
	@Override
	public void doLeftButtonClick(View v) {
		// TODO Auto-generated method stub
		onBackPressed();
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		updateDocument();
		super.onBackPressed();
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
	}
	
	private void delete(ProdCheckDtl bean){
		bean.delete();
	}
	
}
