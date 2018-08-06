package com.app.xstore.cashier;

import java.util.ArrayList;

import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.model.Discount;
import com.app.model.Goods;
import com.app.model.GuaDan;
import com.app.model.UserInfo;
import com.app.model.response.GetGoodsInfoReponse;
import com.app.net.Commands;
import com.app.widget.BadgeView;
import com.app.widget.JieDanListDialog;
import com.app.widget.ProductIdDialog;
import com.app.widget.SimpleEditTextDialog;
import com.app.widget.SimplePairListPopupWindow;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.GoodsDetailActivity;
import com.app.xstore.member.MemberRegisterActivity;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;
import com.base.util.D;
import com.base.util.comm.DisplayUtils;
import com.base.util.comm.TimeUtils;

/**
 * 购物车
 * @author Ni Guijun
 *
 */
public class ShoppingCartActivity extends BaseActivity implements OnClickListener{

	private Context context;
	private EditText tv_scan_result;
	private ListView listView;
	private CommonAdapter<Goods> adapter;
	private ArrayList<Goods> beans=new ArrayList<Goods>();
	private Goods curGoods;//当前选中的Item
	private int scanType=0;//扫描商品0，扫描会员1
	private String memo="";//备注
	private final int REQUEST_CODE_DISCOUNT=101;
	private BadgeView badgeView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shoppingcart);
		context=this;
		initHandler();
		initViews();
		initActionBar("收银台",null,App.res.getDrawable(R.drawable.icon_order_more));
		createFloatView(DisplayUtils.dip2px(context, 80));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		removeFloatView();
	}
	
	@Override
	public void doRightButtonClick(View v){
		showSimplePopupWindow(v);
	}
	
	private void showSimplePopupWindow(View v){
		ArrayList<Pair<Integer, String>> menus = new ArrayList<Pair<Integer, String>>();
		menus.add(new Pair<Integer, String>(3, "添加商品"));
		menus.add(new Pair<Integer, String>(2, "交易查询"));
		menus.add(new Pair<Integer, String>(5, "清空"));
		
		View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
		final SimplePairListPopupWindow<Integer, String> popupWindow = new SimplePairListPopupWindow<Integer, String>(context,view, (int)App.res.getDimension(R.dimen.popupwindow_width), menus);
		popupWindow.showAsDropDown(v, 0, 0);
		popupWindow.setOnItemClickListener(new SimplePairListPopupWindow.OnItemClickListener<Integer, String>() {

			@Override
			public void onItemClick(int position, Pair<Integer, String> pair) {
				// TODO Auto-generated method stub
				Intent intent=null;
				switch (pair.first) {
				case 2://交易查询
					startActivity(new Intent(context,TradeLocalListActivity.class));
					break;
				case 3://
					ProductIdDialog d=new ProductIdDialog(context,"","请输入商品号或扫描");
					d.setOnClickListener(new ProductIdDialog.OnClickListener() {
						@Override
						public void onClick(View v, String text) {
							// TODO Auto-generated method stub
							doCommandGetGoodsInfo(text);
						}
					});
					d.show();
					break;
				case 5://
					D.showDialog(ShoppingCartActivity.this, "确定要清空吗？",  "清空", "取消", new D.OnPositiveListener() {
						
						@Override
						public void onPositive() {
							// TODO Auto-generated method stub
							clearData();
							updateViews(beans);
						}
					});
					break;
				default:
					showToast(R.string.error_unknown_function);
					break;
				}
			}
		});
	}
	
	@Override
	public void initViews() {
		findViewById(R.id.iv_scan).setOnClickListener(this);//扫描会员
		findViewById(R.id.iv_ok).setOnClickListener(this);//钩钩
		tv_scan_result=(EditText)findViewById(R.id.tv_scan_result);//会员号
		findViewById(R.id.btn_register).setOnClickListener(this);//注册
		findViewById(R.id.btn_discount).setOnClickListener(this);//整单折扣
		findViewById(R.id.btn_memo).setOnClickListener(this);//购物袋
		findViewById(R.id.btn_guadan).setOnClickListener(this);//挂单
		findViewById(R.id.btn_jiedan).setOnClickListener(this);//解单
		findViewById(R.id.btn_payment).setOnClickListener(this);//结算
		listView = (ListView) findViewById(R.id.listView);
		
		View tv_payment=findViewById(R.id.tv_payment);
		
		badgeView = new BadgeView(context);
		// badgeView.setBackgroundColor(App.res.getColor(R.color.primary));
		badgeView.setBadgeMargin(0, 8, 8, 0);
		badgeView.setTargetView(tv_payment);
		badgeView.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
		badgeView.setShadowLayer(2, -1, -1, Color.GREEN);
		badgeView.setHideOnNull(true);
		badgeView.setBadgeCount(beans.size());
	}
	
	@Override
	public void updateViews(Object obj){
		badgeView.setBadgeCount(beans.size());
		notifyDataSetChanged();
	}
	
	private void notifyDataSetChanged(){
		if(adapter==null){
			listView.setAdapter(adapter = new CommonAdapter<Goods>( context, beans,
					  R.layout.item_shoppingcart){
					  
					@Override
					public void setValues(ViewHolder helper,final Goods item, final int position) {
						// TODO Auto-generated method stub
						View convertView=helper.getConvertView();
//						if(isExist.get(position)==null){
							convertView.setBackgroundColor(0xFFF2F2F2);//Color.TRANSPARENT
//						}else{
//							convertView.setBackgroundColor(isExist.get(position)?0xFFCCFFCC:0xFFF2F2F2);
//						}
						convertView.setOnLongClickListener(new View.OnLongClickListener() {

							@Override
							public boolean onLongClick(View v) {
								// TODO Auto-generated method stub
								final int index = position;
								Dialog alertDialog = new AlertDialog.Builder(context)
										.setMessage("您确定删除吗？")
										.setPositiveButton("删除",
												new DialogInterface.OnClickListener() {

													@Override
													public void onClick(DialogInterface dialog,
															int which) {
														// TODO Auto-generated method stub
//														isExist.remove(index);
														beans.remove(index);
														notifyDataSetChanged();
													}
												})
										.setNegativeButton("取消",
												new DialogInterface.OnClickListener() {

													@Override
													public void onClick(DialogInterface dialog,
															int which) {
														// TODO Auto-generated method stub
														dialog.dismiss();
													}
												}).create();
								alertDialog.show();
								return false;
							}
							
						});

						helper.setText(R.id.item_1, item.getGoods_name());
						helper.setText(R.id.item_2, "商品编码  "+item.getGoods_sn());
						helper.setText(R.id.item_3, "颜色/尺码");
//						helper.setText(R.id.item_3, item.getColorName()+"/"+item.getSizeName());
						TextView tv_TotalMoney=(TextView)helper.getView(R.id.item_4);
						tv_TotalMoney.setText("￥"+item.getGoods_price());//金额
						
						TextView tv_TotalMoneyReal=(TextView)helper.getView(R.id.item_9);
						if(item.getGoods_price()==item.getGoods_price_discount()){//没折扣
							tv_TotalMoney.getPaint().setFlags(Paint.LINEAR_TEXT_FLAG);//去掉横线效果
							tv_TotalMoney.setTextColor(App.res.getColor(R.color.primary));
							tv_TotalMoneyReal.setVisibility(View.GONE);
						}else{
							tv_TotalMoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//中间加横线 
							tv_TotalMoney.setTextColor(App.res.getColor(R.color.gray));
							tv_TotalMoneyReal.setVisibility(View.VISIBLE);
							String text=formatNumber(10*item.getGoods_price_discount()/item.getGoods_price(),"###0.##")+"折	￥"+item.getGoods_price_discount();
							tv_TotalMoneyReal.setText(text);//实际金额
						}
						//导购
						final TextView item_5=(TextView)helper.getView(R.id.item_5);
						setThemeDrawable(context, item_5);
						item_5.setText(App.user.getUserInfo().getUser_code());
						item_5.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								curGoods=item;
								doCommandGetShopUserListAndShowDialog(new OnSelectUserInfoListener(){

									@Override
									public void doResult(UserInfo instance) {
										// TODO Auto-generated method stub
										item_5.setText(instance.getUser_code());
										item.setSellerUser(instance.getUser_code());
									}
									
								});
							}
						});
						
						//单件折扣
						helper.getView(R.id.item_8).setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								curGoods=item;
								Intent intent=new Intent(context,DiscountActivity.class);
								intent.putExtra("WholeOrder", false);
								startActivityForResult(intent, REQUEST_CODE_DISCOUNT);
							}
						});
						//商品详情
						helper.getView(R.id.item_11).setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								curGoods=item;
								Intent intent =new Intent(context, GoodsDetailActivity.class);
								intent.putExtra("ProdNum", item.getGoods_sn());
								startActivity(intent);
							}
						});
					}

			});
		}else{
			adapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=null;
		switch (v.getId()) {
		case R.id.btn_memo://备注
			SimpleEditTextDialog dialog = new SimpleEditTextDialog(context, memo, "备注");
			dialog.setOnClickListener(new SimpleEditTextDialog.OnClickListener() {

				@Override
				public void onClick(View v, String text) {
					// TODO Auto-generated method stub
					if(!isEmpty(text)){
						memo=text;
					}
				}
			});
			dialog.show();
			break;
		case R.id.iv_scan:
//			if(App.user.isDefaultPhoneType()){
				scanType=1;
//			}
			doScan(resultHandler);
//			doScan(resultHandlerScanMember,this);
			break;
		case R.id.btn_register:
			intent=new Intent(context,MemberRegisterActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_discount://整单打折
			intent=new Intent(context,DiscountActivity.class);
			intent.putExtra("WholeOrder", true);
			startActivityForResult(intent, REQUEST_CODE_DISCOUNT);
			break;
		case R.id.btn_guadan://挂单
			if(isEmptyList(beans)){
				return;
			}
			//开辟新内存
			ArrayList<Goods> tmpBeans=new ArrayList<Goods>();
			tmpBeans.addAll(beans);
//			ArrayList<Boolean> tmpExist=new ArrayList<Boolean>();
//			tmpExist.addAll(isExist);
			
			GuaDan dan=new GuaDan();
			dan.setInfos(tmpBeans);//不能直接写入beans
			dan.setQty(tmpBeans.size());
			
			double money=0.0;
			for(Goods b:tmpBeans){
				if(b.getDiscount()==null){//实际金额
					money+=b.getGoods_price();
				}else{
					money+=b.getGoods_price_discount();//打折后金额
				}
			}
			dan.setMoney(formatMoney(money));
			
			dan.setCreateTime(TimeUtils.getNow("HH:mm:ss"));
//			dan.setExists(tmpExist);
			App.dans.add(dan);
			
			clearData();
			updateViews(beans);
			break;
		case R.id.btn_jiedan://解单
			if(isEmptyList(App.dans)){
				showToast("还没挂单");
				return;
			}
			JieDanListDialog d=new JieDanListDialog(context,App.dans);
			d.setOnItemClickListener(new JieDanListDialog.OnItemClickListener() {
				
				@Override
				public void onItemClick(View v, GuaDan item, int position) {
					// TODO Auto-generated method stub
					clearData();
					
					beans.addAll(item.getInfos());
//					isExist.addAll(item.getExists());
					updateViews(beans);
					
					App.dans.remove(item);
				}
			});
			d.show();
			break;
		case R.id.btn_payment://结算
			if(beans.size()==0){
				showToast("购物车空荡荡");
				return;
			}
			intent=new Intent(context,PaymentTypeActivity.class);
			intent.putParcelableArrayListExtra("Goods", beans);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	//清空beans的同时清空isExist
	private void clearData(){
		beans.clear();
//		isExist.clear();
	}
		
	private void doCommandGetGoodsInfo(String goods_sn){
		String shop_code=App.user.getShopInfo().getShop_code();
		Commands.doCommandGetGoodsInfo(context, shop_code, goods_sn, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				if(isSuccess(response)){
					GetGoodsInfoReponse obj=mapperToObject(response, GetGoodsInfoReponse.class);
					if(obj!=null){
						Goods bean=obj.getGoods();
						if(bean!=null){
							beans.add(bean);
							updateViews(beans);
						}else{
							showToast("查不到商品");
						}
					}
				}
			}
		});
	}
	
	@Override
	public void onScanProductHandleMessage(String prodID){
		doCommandGetGoodsInfo(prodID);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 100){
		}else
		if(requestCode == REQUEST_CODE_DISCOUNT){
			if(resultCode==1){
				if(data!=null){
					Discount discount=data.getParcelableExtra("DiscountWrap");
					if(discount.isWholeOrder()){
						for( Goods bean:beans){
							bean.setDiscount(discount);
						}
					}else{
						curGoods.setDiscount(discount);
					}
					updateViews(beans);
				}
			}
		}
	}
	
	@Override
	protected void onFloatViewClick() {
		// TODO Auto-generated method stub
		scanType=0;
		super.onFloatViewClick();
	}
	
	@Override
	public void initHandler(){
		resultHandler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				if(scanType==0){//扫描商品
					String message = (String) msg.obj;
					if(message.equalsIgnoreCase("time out")){
						showToast(R.string.alert_no_barcode_found);
						return;
					}
					String prodID = getProdID(message);
					if (TextUtils.isEmpty(prodID)) {
						showToast(R.string.alert_no_barcode_found);
					}else{
						onScanProductHandleMessage(prodID);
					}
				}else if(scanType==1){//扫描会员
					String data = (String) msg.obj;
					if(data.equalsIgnoreCase("time out")){
						showToast(R.string.alert_no_barcode_found);
						return;
					}
					tv_scan_result.setText(data);
				}
			}
		};
	}
	
	@Subscriber
	void updateByEventBus(String event) {
		if (event.equals(App.EVENT_CLEAR)) {
			beans.clear();
			updateViews(beans);
		}
	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		updateTheme();
	}
	
	private void updateTheme() {
		setThemeDrawable(context, R.id.btn_memo);
		setThemeDrawable(context, R.id.btn_guadan);
		setThemeDrawable(context, R.id.btn_jiedan);
		setThemeDrawable(context, R.id.btn_payment);
		if(adapter!=null){
			adapter.notifyDataSetChanged();
		}
	}
	
}
