package com.app.xstore.inventory;

import java.util.ArrayList;

import org.litepal.crud.DataSupport;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

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

import com.app.model.Bill;
import com.app.model.BillProduct;
import com.app.widget.SimpleNumberDialog;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.GoodsDetailActivity;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;
import com.base.util.D;
import com.base.util.comm.TimeUtils;

/**
 * 新增or修改单据
 * 
 * @author pythoner
 * 
 */
public class OfflineDocumentCreateActivity extends BaseActivity implements OnClickListener {

	private Context context;
	private TextView tv_no, tv_scanNum;
	private EditText et_remark;//描述
	private EditText et_locID;//货架
	private String documentNo;// 单据号，根据时间生成
	private EditText et_prodID;
	private ListView listView;
	private CommonAdapter<BillProduct> adapter;
	private Bill bill;
	private int curPosition = -1;
	private boolean isModify=false;//创建or修改（指的是创建单据还是修改单据）
	private boolean isChange=false;//是否改变了单据中货品数据（无论货架，remark&商品数量增删改等操作）
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offline_document_create);
		context = this;
		bill = getIntent().getParcelableExtra("Bill");
		if (bill == null) {
			isModify=false;
			initActionBar("创建单据", null, null);
			documentNo = String.valueOf(System.currentTimeMillis());
			bill = new Bill();// 生成单据
			bill.setDocNum(documentNo);
			bill.setUserId(App.user.getUserInfo()==null?"":App.user.getUserInfo().getUser_code());
			bill.setCreateTime(TimeUtils.getCurrentTimeInString());
			bill.setBillProducts(new ArrayList<BillProduct>());
			bill.setStatus(0);// 未上传
		} else {
			isModify=true;
			initActionBar("修改单据", null, null);
			documentNo = bill.getDocNum();
		}
		initViews();
		updateViews(bill.getBillProducts());
		createFloatView(0);
	}

	@Override
	public void doRightButtonClick(View v) {
		// TODO Auto-generated method stub
//		if (isEmptyList(bill.getBillProducts())) {
//			showToast("商品列表不能为空");
//			return;
//		}
	}

//	private void doCommandScanBarCodeIntoBillEntityFor11or13(String prodID) {
//		doCommandScanBarCodeIntoBillEntityFor11or13(prodID,
//				new OnSearchProductListener() {
//
//					@Override
//					public void doResult(List<BillSaleDetailInfo> list) {
//						// TODO Auto-generated method stub
//						for (BillSaleDetailInfo bean : list) {
//							int position = isExist(bean);
//							if (position >= 0) {
//								BillProduct o = bill.getBillProducts().get(position);
//								o.setQty(o.getQty() + 1);
//								curPosition = position;
//								
//								// 存在则更新
//								update(o);
//							} else {
//								BillProduct o = new BillProduct();
//								o.setBarcode(bean.getIntnlBarcode());
//								o.setProdNum(bean.getProdID());
//								o.setQty(1);
//								bill.getBillProducts().add(o);
//								curPosition = bill.getBillProducts().size() - 1;
//								
//								// 保存
//								save(o);
//							}
//						}
//						updateViews(bill.getBillProducts());
//					}
//				});
//	}
//
//	private int isExist(BillSaleDetailInfo bean) {
//		int size = bill.getBillProducts().size();
//		for (int i = 0; i < size; i++) {
//			BillProduct dtl = bill.getBillProducts().get(i);
//			if (!isEmpty(bean.getProdID())
//					&& bean.getProdID().equals(dtl.getProdNum())) {
//				return i;
//			} else if (!isEmpty(bean.getIntnlBarcode())
//					&& bean.getIntnlBarcode().equals(dtl.getBarcode())) {
//				return i;
//			}
//		}
//		return -1;
//	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		tv_no = $(R.id.tv_no);
		tv_no.setText(bill.getDocNum());
		tv_scanNum = $(R.id.tv_scanNum);
		tv_scanNum.setText("条数：扫描数：");
		et_remark = $(R.id.et_remark);
		et_remark.setText(bill.getRemark());
		et_remark.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				//如果改变了remark，就简单的认为单据中的货品也改变了
				isChange=true;
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		et_locID = $(R.id.et_locID);
		et_locID.setText(bill.getShelfCode());
		et_locID.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				//如果改变了货架，就简单的认为单据中的货品也改变了
				isChange=true;
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		et_prodID = $(R.id.et_prodID);
		$(R.id.btn_ok).setOnClickListener(this);

		listView = $(R.id.listView);
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				BillProduct bean = bill.getBillProducts().get(position);
				if (/*!isEmpty(bean.getBarcode()) &&*/ !isEmpty(bean.getProdNum())) {
					Intent intent = new Intent(context,
							GoodsDetailActivity.class);
					intent.putExtra("ProdNum", bean.getProdNum());
					startActivity(intent);
				}
			}

		});
		listView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				final BillProduct bean = bill.getBillProducts().get(position);
				D.showDialog(OfflineDocumentCreateActivity.this, "是否删除？", "确定",
						"取消", new D.OnPositiveListener() {

							@Override
							public void onPositive() {
								// TODO Auto-generated method stub
								bill.getBillProducts().remove(bean);
								updateViews(bill.getBillProducts());

								delete(bean);
							}
						});
				return true;
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
		et_prodID.setText("");
		int count = 0;
		for (BillProduct prod : bill.getBillProducts()) {
			count += prod.getQty();
		}
		bill.setTotalQty(count);
		tv_scanNum.setText("条数：" + bill.getBillProducts().size() + "	扫描数："
				+ bill.getTotalQty());
	}

	private void notifyDataSetChanged() {
		if (adapter == null) {
			listView.setAdapter(adapter = new CommonAdapter<BillProduct>(
					context, bill.getBillProducts(),
					R.layout.item_create_document) {

				@Override
				public void setValues(ViewHolder helper,
						final BillProduct item, final int position) {
					// TODO Auto-generated method stub
					helper.setText(R.id.item_0, item.getProdNum());
					helper.setText(R.id.item_1, item.getProdName());
//					helper.setText(R.id.item_1, item.getBarcode());
					final TextView item_2 = helper.getView(R.id.item_2);
					item_2.setText(String.valueOf(item.getQty()));
					item_2.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							SimpleNumberDialog d = new SimpleNumberDialog(
									context, item_2.getText().toString(), "数量");
							d.setOnClickListener(new SimpleNumberDialog.OnClickListener() {

								@Override
								public void onClick(View v, String text) {
									// TODO Auto-generated method stub
									item.setQty(Integer.parseInt(text));
									updateViews(bill.getBillProducts());

									update(item);
								}
							});
							d.show();
						}
					});
					View container = helper.getView(R.id.container);
					if (OfflineDocumentCreateActivity.this.isEmpty(item
							.getProdName())
							|| OfflineDocumentCreateActivity.this.isEmpty(item
									.getProdNum())) {
						container.setBackgroundColor(Color.TRANSPARENT);
					} else {
						container.setBackgroundColor(0xFF50BB50);
					}
					if (curPosition == position) {
						container.setBackgroundColor(0xFFFF6633);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_ok:// 扫描商品
			doAdd();
			break;

		default:
			break;
		}
	}

	private void doAdd() {
		String prodID = et_prodID.getText().toString();
		if(isEmpty(prodID)){
			return;
		}
//		if (prodID.length() > 11) {
//			showToast("商品编码为11位");
//			return;
//		} else if (prodID.length() < 11) {
//			doCommandScanBarCodeIntoBillEntityFor11or13(prodID);
//		} else if (prodID.length() == 11) {
			doSaveOrUpdateData(prodID);
			updateViews(bill.getBillProducts());
//		}
	}

	private void doSaveOrUpdateData(String prodID){
		int i = 0;
		boolean isExist = false;
		for (BillProduct bean : bill.getBillProducts()) {
			if (bean.getProdNum().equals(prodID)) {
				bean.setQty(bean.getQty() + 1);
				isExist = true;
				curPosition = i;
				
				// 存在则更新
				update(bean);
				break;
			}
			i++;
		}
		if (!isExist) {
			BillProduct o = new BillProduct();
//			o.setBarcode("");
			o.setProdNum(prodID);
			o.setProdName("");
			o.setQty(1);
			bill.getBillProducts().add(o);
			curPosition = bill.getBillProducts().size() - 1;
			
			// 保存
			save(o);
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
		saveOrUpdateDatas();// 返回时保存一次
	}

	@Override
	public void onDestroy() {
		removeFloatView();
		super.onDestroy();
	}

	private void saveOrUpdateDatas() {
		if(isChange){
			if(isModify){
				String remark=et_remark.getText().toString().trim();
				bill.setRemark(remark);
				String shelfCode=et_locID.getText().toString().trim();
				if(isEmpty(shelfCode)){
					doShake(context, et_locID);
					showToast("请输入货架编号");
					return;
				}
				bill.setShelfCode(shelfCode);
				bill.update(bill.getId());
				EventBus.getDefault().post(App.EVENT_UPDATE_DOCUMENT_LIST);
			}else{
				if (!isEmptyList(bill.getBillProducts())) {
					String remark=et_remark.getText().toString().trim();
					bill.setRemark(remark);
					String shelfCode=et_locID.getText().toString().trim();
					if(isEmpty(shelfCode)){
						doShake(context, et_locID);
						showToast("请输入货架编号");
						return;
					}
					bill.setShelfCode(shelfCode);
					bill.save();
					EventBus.getDefault().post(App.EVENT_UPDATE_DOCUMENT_LIST);
				}
			}
		}
		super.onBackPressed();
	}

	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		updateTheme();
	}

	private void updateTheme() {
		setThemeDrawable(this, R.id.btn_ok);
	}

	@Override
	public void onScanProductHandleMessage(String prodID) {
//		if (prodID.length() == 13) {
//			int i = 0;
//			boolean isExist = false;
//			for (BillProduct bean : bill.getBillProducts()) {
//				if (bean.getBarcode().equals(prodID)) {// !isEmpty(bean.getBarcode())&&
//					bean.setQty(bean.getQty() + 1);
//					isExist = true;
//					curPosition = i;
//
//					// 存在则更新
//					update(bean);
//					break;
//				}
//				i++;
//			}
//			if (!isExist) {
//				BillProduct o = new BillProduct();
//				o.setBarcode(prodID);
//				o.setProdNum("");
//				o.setQty(1);
//				bill.getBillProducts().add(o);
//				curPosition = bill.getBillProducts().size() - 1;
//
//				// 保存
//				save(o);
//			}
//		} else if (prodID.length() == 11) {
			int i = 0;
			boolean isExist = false;
			for (BillProduct bean : bill.getBillProducts()) {
				if (bean.getProdNum().equals(prodID)) {// !isEmpty(bean.getProdNum())&&
					bean.setQty(bean.getQty() + 1);
					isExist = true;
					curPosition = i;

					// 存在则更新
					update(bean);
					break;
				}
				i++;
			}
			if (!isExist) {
				BillProduct o = new BillProduct();
//				o.setBarcode("");
				o.setProdNum(prodID);
				o.setProdName("");
				o.setQty(1);
				bill.getBillProducts().add(o);
				curPosition = bill.getBillProducts().size() - 1;

				// 保存
				save(o);
			}
//		} else {
//			showToast("错误的商品码：" + prodID);
//		}
		updateViews(bill.getBillProducts());
	}

	private void save(BillProduct bean) {
		isChange=true;
		bean.setDocumentNo(documentNo);
		// bean.setDocID(bill.getId());
		bean.save();
	}

	private void update(BillProduct bean) {
		isChange=true;
		bean.update(bean.getId());
	}

	private void delete(BillProduct bean) {
		isChange=true;
//		bean.delete();
		DataSupport.delete(BillProduct.class, bean.getId());
	}

}
