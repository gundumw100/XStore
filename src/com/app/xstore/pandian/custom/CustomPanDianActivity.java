package com.app.xstore.pandian.custom;

import java.util.ArrayList;
import java.util.Collections;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.net.Commands;
import com.app.widget.VerificationCodeDialog;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;
import com.base.util.D;

/**
 * 新增or修改单据
 * 
 * @author pythoner
 * 
 */
public class CustomPanDianActivity extends BaseActivity implements
		OnClickListener {

	private Context context;
	private TextView tv_no, tv_scanNum;
	private EditText et_locID;// 货架
	private EditText et_prodID;
	private ListView listView;
	private CommonAdapter<CustomPanDianProduct> adapter;
	private CustomPanDianDan dan;
	private int curPosition = -1;
	private boolean isModify = false;// 创建or修改（指的是创建单据还是修改单据）
	private boolean isChange = false;// 是否改变了单据中货品数据（无论货架，remark&商品数量增删改等操作）

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_pandian);
		context = this;
		dan = getIntent().getParcelableExtra("CustomPanDianDan");
		if (dan == null) {
			isModify = false;
			dan = new CustomPanDianDan();// 生成单据
			dan.setDate(System.currentTimeMillis());
			dan.setGh(App.user.getUserInfo().getUser_code());
			List<CustomPanDianProduct> list = DataSupport.where("danID=?",
					String.valueOf(0)).find(CustomPanDianProduct.class);
			dan.setPanDianProducts(list);
			dan.setStatus(0);// 未上传
		} else {
			isModify = true;
			List<CustomPanDianProduct> list = DataSupport.where("danID=?",
					String.valueOf(dan.getId())).find(CustomPanDianProduct.class);
			dan.setPanDianProducts(list);
		}
		initActionBar(isModify ? "单据明细" : "创建单据", "下一步", null);
		initViews();
		updateViews(dan.getPanDianProducts());
		if(dan.getStatus()==1||dan.getStatus()==2){
		}else{
			createFloatView(0);
		}
	}

	@Override
	public void doRightButtonClick(View v) {
		// TODO Auto-generated method stub
		 if (isEmptyList(dan.getPanDianProducts())) {
			 showToast("商品列表不能为空");
			 return;
		 }
		 doCheck(true);//先校验
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		tv_no = $(R.id.tv_no);
		tv_no.setText("" + dan.getDate());
		tv_scanNum = $(R.id.tv_scanNum);
		tv_scanNum.setText("条数:");
		TextView tv_yds = $(R.id.tv_yds);
		et_locID = $(R.id.et_locID);
		et_locID.setText(dan.getShelf());
		if (isModify) {
			et_locID.setEnabled(false);
			tv_yds.setText("预点数:" + dan.getYds());
		} else {
			tv_yds.setText("预点数:0");
			et_locID.setEnabled(true);
			et_locID.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {
					// TODO Auto-generated method stub
					// 如果改变了货架，就简单的认为单据中的货品也改变了
					isChange = true;
				}

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable arg0) {
					// TODO Auto-generated method stub

				}
			});
		}

		et_prodID = $(R.id.et_prodID);
		$(R.id.btn_ok).setOnClickListener(this);
		$(R.id.btn_check).setOnClickListener(this);
		$(R.id.btn_clear).setOnClickListener(this);

		listView = $(R.id.listView);
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				CustomPanDianProduct bean = dan.getPanDianProducts().get(position);
				if (isEmpty(bean.getSku_id())) {
					showToast("请先校验");
				}else{
					Intent intent = new Intent(context,CustomGoodsInfoActivity.class);
					intent.putExtra("ProdNum", bean.getSku_id());// sku_id
					startActivity(intent);
				}
			}

		});
		if(dan.getStatus()==1||dan.getStatus()==2){
		}else{
			listView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					final CustomPanDianProduct bean = dan.getPanDianProducts()
							.get(position);
					D.showDialog(CustomPanDianActivity.this, "是否删除 "+bean.getLsh()+" ？", "确定", "取消",
							new D.OnPositiveListener() {

								@Override
								public void onPositive() {
									// TODO Auto-generated method stub
									dan.getPanDianProducts().remove(bean);
									updateViews(dan.getPanDianProducts());
									DataSupport.delete(CustomPanDianProduct.class,bean.getId());
								}
							});
					return true;
				}
			});
		}

	}

	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
		if (obj == null) {
			return;
		}
		notifyDataSetChanged();
		et_prodID.setText("");// 搜索框清空
		int count = 0;
		for (CustomPanDianProduct prod : dan.getPanDianProducts()) {
			count += prod.getQty();
		}
		dan.setSms(count);
		tv_scanNum.setText("条数:" + dan.getPanDianProducts().size());
	}

	private void notifyDataSetChanged() {
		if (adapter == null) {
			listView.setAdapter(adapter = new CommonAdapter<CustomPanDianProduct>(
					context, dan.getPanDianProducts(),
					R.layout.item_custom_pandiandan_product) {

				@Override
				public void setValues(ViewHolder helper,
						final CustomPanDianProduct item, final int position) {
					// TODO Auto-generated method stub
					helper.setText(R.id.item_0, item.getLsh());
					helper.setText(R.id.item_1, item.getStyle_id());
					helper.setText(R.id.item_2, item.getColor_name()+" "+item.getSize_name());
					View container = helper.getView(R.id.container);
					if (CustomPanDianActivity.this.isEmpty(item.getName())
							|| CustomPanDianActivity.this.isEmpty(item
									.getBarCode())) {
						container.setBackgroundColor(Color.TRANSPARENT);
					} else {
						container.setBackgroundColor(0xFFCCFF99);
					}
					if (curPosition == position) {
						container.setBackgroundColor(0xFFFFCC99);
					}
				}
			});
		} else {
			adapter.notifyDataSetChanged();
		}
	}

	@Subscriber
	void updateByEventBus(String event) {
		if (event.equals(App.EVENT_FINISH)) {
			finish();
		}
	}

	@Subscriber(tag = App.EVENT_UPDATE_BOX2, mode = ThreadMode.POST)
	void updateWithMode(Object flag) {
		List<CustomGoods> list=(ArrayList<CustomGoods>)flag;
		for(CustomGoods item:list){
			CustomPanDianProduct p=new CustomPanDianProduct();
			p.setBarCode(item.getSku_id());
			p.setName(item.getStyle_name());
			p.setQty(1);
			dan.getPanDianProducts().add(p);
		}
		
		isChange = true;
		updateViews(dan.getPanDianProducts());
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(dan.getStatus()==1||dan.getStatus()==2){
			showToast("已上传的单据不能修改");
			return;
		}
		switch (v.getId()) {
		case R.id.btn_ok:// 扫描商品
			doAdd();
			break;
		case R.id.btn_check://
			doCheck();
			break;
		case R.id.btn_clear://
			final VerificationCodeDialog d=new VerificationCodeDialog(context);
			d.setOnClickListener(new VerificationCodeDialog.OnClickListener() {
				
				@Override
				public void onClick(View v, String text) {
					// TODO Auto-generated method stub
					dan.getPanDianProducts().clear();
					updateViews(dan.getPanDianProducts());
					DataSupport.deleteAll(CustomPanDianProduct.class, "danID=?",""+dan.getId());
				}
			});
			d.show();
			break;

		default:
			break;
		}
	}

	private void doAdd() {
		String prodID = et_prodID.getText().toString();
		if (isEmpty(prodID)) {
			return;
		}
		onScanProductHandleMessage(prodID);
	}

	@Override
	public void doLeftButtonClick(View v) {
		// TODO Auto-generated method stub
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(dan.getPanDianProducts().size()>0){
			String shelfCode = et_locID.getText().toString().trim();
			if(isEmpty(shelfCode)){
				doShake(context, et_locID);
				showToast("未分配货架");
				return;
			}
		}
		saveOrUpdateDatas();// 返回时保存一次
		super.onBackPressed();
	}

	@Override
	public void onDestroy() {
		if(dan.getStatus()==1||dan.getStatus()==2){
		}else{
			removeFloatView();
		}
		super.onDestroy();
	}

	private void saveOrUpdateDatas() {
		if (isChange) {
			if (isModify) {
				String shelfCode = et_locID.getText().toString().trim();
				if (isEmpty(shelfCode)) {
					doShake(context, et_locID);
					showToast("请输入货架编号");
					return;
				}
				dan.setShelf(shelfCode);
				dan.update(dan.getId());
				for (CustomPanDianProduct p : dan.getPanDianProducts()) {
					p.setDanID(dan.getId());
				}
				DataSupport.saveAll(dan.getPanDianProducts());
				EventBus.getDefault().post(
						App.EVENT_UPDATE_CUSTOM_PANDIANDAN_LIST);
			} else {
				if (!isEmptyList(dan.getPanDianProducts())) {
					String shelfCode = et_locID.getText().toString().trim();
					if (isEmpty(shelfCode)) {
						doShake(context, et_locID);
						showToast("请输入货架编号");
						return;
					}
					dan.setShelf(shelfCode);
					dan.save();
					for (CustomPanDianProduct p : dan.getPanDianProducts()) {
						p.setDanID(dan.getId());
					}
					DataSupport.saveAll(dan.getPanDianProducts());
					EventBus.getDefault().post(
							App.EVENT_UPDATE_CUSTOM_PANDIANDAN_LIST);
				}
			}
			isChange=false;
		}
	}

	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		setThemeDrawable(this, R.id.btn_ok);
		setThemeDrawable(this, R.id.btn_check);
		setThemeDrawable(this, R.id.btn_clear);
	}

	@Override
	public void onScanProductHandleMessage(String prodID) {
		int i = 0;
		boolean isExist = false;
		for (CustomPanDianProduct bean : dan.getPanDianProducts()) {
			if (prodID.equals(bean.getLsh())) {
				bean.setQty(bean.getQty() + 1);
				isExist = true;
				curPosition = 0;
				Collections.swap(dan.getPanDianProducts(), 0, i);//移动到第一项
				break;
			}
			i++;
		}
		if (!isExist) {
			CustomPanDianProduct o = new CustomPanDianProduct();
			o.setLsh(prodID);
			o.setStyle_id("");
			o.setColor_name("");
			o.setSize_name("");
			o.setQty(1);
			dan.getPanDianProducts().add(0,o);
			curPosition = 0;
		}
		isChange = true;
		updateViews(dan.getPanDianProducts());
	}

	private void doCheck(){
		doCheck(false);
	}
	private void doCheck(final boolean isNext){
		final List<CustomPanDianProduct> beans=dan.getPanDianProducts();
		List<String> goodsCode=null;
		if(beans!=null){
			goodsCode=new ArrayList<String>();
			for(CustomPanDianProduct bean:beans){
				if(isEmpty(bean.getStyle_id())||isEmpty(bean.getLsh())){
					goodsCode.add(bean.getLsh());
				}
			}
		}
		if(isEmptyList(goodsCode)){
			if(isNext){//校验过了
				doNext();
			}
			return;
		}
		String shop_code=App.user.getShopInfo().getShop_code();
		Commands.doCommandGetCustGoodsList(context, shop_code,goodsCode, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				if (isSuccess(response)) {
					GetCustGoodsListResponse obj = mapperToObject(response, GetCustGoodsListResponse.class);
					if (obj != null) {
						List<CustomGoods> dtls=obj.getGoods();
						for(CustomGoods one:dtls){
							for(CustomPanDianProduct two:beans){
								if(one.getLsh().equals(two.getLsh())){
									two.setStyle_id(one.getStyle_id());
									two.setColor_name(one.getColor_name());
									two.setSize_name(one.getSize_name());
									two.setSku_id(one.getSku_id());
									continue;
								}
							}
						}
						notifyDataSetChanged();
						for(CustomPanDianProduct bean:beans){
							if(isEmpty(bean.getStyle_id())||isEmpty(bean.getLsh())){
								showToast("校验失败，商品"+bean.getLsh()+"不存在");
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
	
	private void doNext(){
		String shelfCode = et_locID.getText().toString().trim();
		if(isEmpty(shelfCode)){
			doShake(context, et_locID);
			showToast("未分配货架");
			return;
		}
		if(isEmpty(dan.getShelf())){//临时赋个货架值
			dan.setShelf(shelfCode);
			dan.setYds(0);
		}
//		if(bill.getYds()!=bill.getSms()){
//			D.showDialog(CustomPanDianActivity.this, "预点数为"+bill.getYds()+"，扫描数为"+bill.getSms()+"，确定要上传吗？", "确定", "取消", new D.OnPositiveListener() {
//				
//				@Override
//				public void onPositive() {
//					// TODO Auto-generated method stub
//					changeToUploadActivity();
//				}
//			});
//		}else{
			changeToUploadActivity();
//		}
	}
	
	private void changeToUploadActivity(){
		saveOrUpdateDatas();//下一步时保存一次
		Intent intent =new Intent(context,CustomPanDianUploadActivity.class);
		intent.putExtra("CustomPanDianDan", dan);
		startActivity(intent);
	}
	
}
