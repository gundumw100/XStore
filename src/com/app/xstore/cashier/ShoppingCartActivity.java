package com.app.xstore.cashier;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.model.Discount;
import com.app.model.GuaDan;
import com.app.model.Member;
import com.app.model.UserInfo;
import com.app.model.response.GetVipInfoResponse;
import com.app.net.Commands;
import com.app.widget.BadgeView;
import com.app.widget.JieDanListDialog;
import com.app.widget.SimpleEditTextDialog;
import com.app.widget.SimplePairListPopupWindow;
import com.app.widget.dialog.ProductCodeDialog;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.app.xstore.member.MemberRegisterActivity;
import com.app.xstore.mendiancaigouruku.GetGoodsListBySKUsResponse;
import com.app.xstore.shangpindangan.ProductDangAn;
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

	private EditText et_scan_result;
	private ListView listView;
	private CommonAdapter<ProductDangAn> adapter;
	private ArrayList<ProductDangAn> beans=null;
	private ProductDangAn curProduct;//当前选中的Item
	private int scanType=0;//扫描商品0，扫描会员1
	private String memo="";//备注
	private final int REQUEST_CODE_DISCOUNT=101;
	private BadgeView badgeView;
	private Member member;
	private FrameLayout container_member;
	private TextView tv_member;
	private ImageView btn_delete_member;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shoppingcart);
		initHandler();
		initActionBar("收银台",null,getDrawableCompat(R.drawable.icon_order_more));
		initViews();
		createFloatView(DisplayUtils.dip2px(context, 80));
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		beans=App.shoppingCartItems;//
		updateViews(beans);
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
		menus.add(new Pair<Integer, String>(0, "添加商品"));
		menus.add(new Pair<Integer, String>(1, "交易查询"));
		menus.add(new Pair<Integer, String>(2, "清空"));
		
		View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
		final SimplePairListPopupWindow<Integer, String> popupWindow = new SimplePairListPopupWindow<Integer, String>(context,view, (int)App.res.getDimension(R.dimen.popupwindow_width), menus);
		popupWindow.showAsDropDown(v, 0, 0);
		popupWindow.setOnItemClickListener(new SimplePairListPopupWindow.OnItemClickListener<Integer, String>() {

			@Override
			public void onItemClick(int position, Pair<Integer, String> pair) {
				// TODO Auto-generated method stub
				Intent intent=null;
				switch (pair.first) {
				case 0://
					String test="";
					if(App.isLog){
						test="1800711009";
					}
					ProductCodeDialog d=new ProductCodeDialog(context,test,"请输入商品号或扫描");
					d.setOnClickListener(new ProductCodeDialog.OnClickListener() {
						@Override
						public void onClick(View v, String text) {
							// TODO Auto-generated method stub
							doCommandGetGoodsListBySKUs(text);
						}
					});
					d.show();
					break;
				case 1://交易查询
					startActivity(new Intent(context,TradeLocalListActivity.class));
					break;
				case 2://
					D.showDialog(ShoppingCartActivity.this, "确定要清空吗？",  "清空", "取消", new D.OnPositiveListener() {
						
						@Override
						public void onPositive() {
							// TODO Auto-generated method stub
							clearAll();
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
		et_scan_result=(EditText)findViewById(R.id.et_scan_result);//会员号
		container_member=(FrameLayout)findViewById(R.id.container_member);
		tv_member=(TextView)findViewById(R.id.tv_member);//会员
		btn_delete_member=(ImageView)findViewById(R.id.btn_delete_member);
		btn_delete_member.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				member=null;
				updateMemberViews();
			}
		});
		updateMemberViews();
		findViewById(R.id.btn_register).setOnClickListener(this);//注册
		findViewById(R.id.btn_discount).setOnClickListener(this);//整单折扣
		findViewById(R.id.btn_memo).setOnClickListener(this);//备注
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
		badgeView.setBadgeCount(0);
	}
	
	@Override
	public void updateViews(Object obj){
		badgeView.setBadgeCount(beans.size());
		notifyDataSetChanged();
	}
	
	private void notifyDataSetChanged(){
		if(adapter==null){
			listView.setAdapter(adapter = new CommonAdapter<ProductDangAn>( context, beans,
					  R.layout.item_shoppingcart){
					  
					@Override
					public void setValues(ViewHolder helper,final ProductDangAn item, final int position) {
						// TODO Auto-generated method stub
						View convertView=helper.getConvertView();
						convertView.setBackgroundColor(0xFFF2F2F2);//Color.TRANSPARENT
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

						ImageView item_img=helper.getView(R.id.item_img);
						loadImageByPicasso(item.getGoods_img(), item_img);
						
						TextView item_name=helper.getView(R.id.item_name);
						item_name.setText(item.getGoods_name());
						item_name.setOnClickListener(new View.OnClickListener() {//商品详情
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								curProduct=item;
								startProductDetailActivity(item.getGoods_sn());
							}
						});
						
						TextView item_sn=helper.getView(R.id.item_sn);
						item_sn.setText("编码  "+item.getGoods_sn());
						item_sn.setOnClickListener(new View.OnClickListener() {//商品详情
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								curProduct=item;
								startProductDetailActivity(item.getGoods_sn());
							}
						});
						
						String color_desc=item.getGoods_color_desc();
						if(context.isEmpty(color_desc)){
							color_desc="均色";
						}
						String spec_desc=item.getGoods_spec_desc();
						if(context.isEmpty(spec_desc)){
							spec_desc="均码";
						}
						helper.setText(R.id.item_colorSize, color_desc+"  "+spec_desc);
						
						TextView tv_ls_price=(TextView)helper.getView(R.id.item_ls_price);
						tv_ls_price.setText("￥"+formatNumber(item.getGoods_ls_price(),"###0.##"));//零售金额
						TextView tv_discount_price=(TextView)helper.getView(R.id.item_discount_price);//打折后的金额
						if(item.getDiscount()==null){//没折扣
							tv_ls_price.getPaint().setFlags(Paint.LINEAR_TEXT_FLAG);//去掉横线效果
							tv_ls_price.setTextColor(getColorCompat(R.color.primary));
							tv_discount_price.setVisibility(View.GONE);
						}else{
							Discount discount=item.getDiscount();
							tv_ls_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//中间加横线 
							tv_ls_price.setTextColor(getColorCompat(R.color.gray));
							tv_discount_price.setVisibility(View.VISIBLE);
							
							String rate=formatNumber(10*discount.getDiscountRate(),"###0.##")+"折  ";
							String value="￥"+formatNumber(item.getGoods_price(),"###0.##");
							tv_discount_price.setText(rate+value);//实际金额
							
						}
						//导购
						final TextView item_seller=(TextView)helper.getView(R.id.item_seller);
						setThemeDrawable(context, item_seller);
						item_seller.setText(App.user.getUserInfo().getUser_code());
						item_seller.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								curProduct=item;
								doCommandGetShopUserListAndShowDialog(new OnSelectUserInfoListener(){

									@Override
									public void doResult(UserInfo instance) {
										// TODO Auto-generated method stub
										item_seller.setText(instance.getUser_code());
										item.setSellerUser(instance.getUser_code());
									}
									
								});
							}
						});
						
						//单件折扣
						helper.getView(R.id.item_discount).setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								curProduct=item;
								Intent intent=new Intent(context,DiscountActivity.class);
								intent.putExtra("TotalPrice", item.getGoods_ls_price());//
								intent.putExtra("WholeOrder", false);
								startActivityForResult(intent, REQUEST_CODE_DISCOUNT);
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
		case R.id.iv_ok:
			String cardNo=et_scan_result.getText().toString().trim();
			if(context.isEmpty(cardNo)){
				showToast("请输入查询条件");
				return;
			}
			String mobile=null;
			String vipCode=null;
			if(cardNo.length()==11){
				mobile=cardNo;
			}else{
				vipCode=cardNo;
			}
			doCommandGetVipInfo(vipCode,mobile,null);
			break;
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
			scanType=1;
			doScan(resultHandler);
			break;
		case R.id.btn_register:
			intent=new Intent(context,MemberRegisterActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_discount://整单打折
			float totalPrice=0;
			for(ProductDangAn bean:beans){
				totalPrice+=bean.getGoods_ls_price();
			}
			intent=new Intent(context,DiscountActivity.class);
			intent.putExtra("TotalPrice", totalPrice);//
			intent.putExtra("WholeOrder", true);
			startActivityForResult(intent, REQUEST_CODE_DISCOUNT);
			break;
		case R.id.btn_guadan://挂单
			if(isEmptyList(beans)){
				return;
			}
			//开辟新内存
			ArrayList<ProductDangAn> tmpBeans=new ArrayList<ProductDangAn>();
			tmpBeans.addAll(beans);
//			ArrayList<Boolean> tmpExist=new ArrayList<Boolean>();
//			tmpExist.addAll(isExist);
			
			GuaDan dan=new GuaDan();
			dan.setInfos(tmpBeans);//不能直接写入beans
			dan.setQty(tmpBeans.size());
			
			float money=0f;
			for(ProductDangAn b:tmpBeans){
				if(b.getDiscount()==null){//零售价
					money+=b.getGoods_ls_price();
				}else{//折扣价
					money+=b.getDiscount().getDiscountPrice();//打折后金额
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
			intent.putParcelableArrayListExtra("Products", beans);
			intent.putExtra("Member", member);
			intent.putExtra("Remark", memo);
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
	
	private void doCommandGetVipInfo(String vipCode,String mobile,String name){
		Commands.doCommandGetVipInfo(context, vipCode, mobile,name, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				if(context.isSuccess(response)){
					GetVipInfoResponse obj=context.mapperToObject(response, GetVipInfoResponse.class);
					List<Member> list=obj.getHeadInfo();
					if(context.isEmptyList(list)){
						showToast("未找到会员信息");
					}else{
						member=list.get(0);
						updateMemberViews();
					}
				}
			}
		});
	}
	
	private void updateMemberViews(){
		if(member==null){
			container_member.setVisibility(View.GONE);
			tv_member.setText("");
		}else{
			container_member.setVisibility(View.VISIBLE);
			tv_member.setText(member.getMobile()+member.getName()+"	积分："+member.getTotalPoints());
		}
		et_scan_result.setText("");
	}
	
	private void doCommandGetGoodsListBySKUs(String goods_sn){
		List<String> goodsSns=new ArrayList<String>();
		goodsSns.add(goods_sn);
		Commands.doCommandGetGoodsListBySKUs(context, goodsSns, new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				if(isSuccess(response)){
					GetGoodsListBySKUsResponse obj=mapperToObject(response, GetGoodsListBySKUsResponse.class);
					List<ProductDangAn> products=obj.getGoodsInfo();
					if(!isEmptyList(products)){
						
						for(ProductDangAn bean:products){
							bean.setGoods_price(bean.getGoods_ls_price());//实际售价预置成零售价
							bean.setGoods_discountRate(100);//不打折
						}
						
						//请求缩略图
						doCommandGetGoodsListBySKUs(products);
						
//						beans.addAll(products);
//						updateViews(beans);
					}else{
						showToast("查不到商品");
					}
				}
			}
		});
	}
	
	@Override
	public void onScanProductHandleMessage(String prodID){
		doCommandGetGoodsListBySKUs(prodID);
	}
	
	private void doCommandGetGoodsListBySKUs(final List<ProductDangAn> products){
		List<String> goodsSns=new ArrayList<String>();
		for(ProductDangAn bean:products){
			goodsSns.add(bean.getGoods_sn());
		}
		
		Commands.doCommandGetGoodsListBySKUs(context, goodsSns, new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					GetGoodsListBySKUsResponse obj=mapperToObject(response, GetGoodsListBySKUsResponse.class);
					if(obj!=null&&obj.getGoodsInfo()!=null){
						up:for(ProductDangAn bean:products){
							for(ProductDangAn item:obj.getGoodsInfo()){
								if(!isEmpty(item.getGoods_sn())&&bean.getGoods_sn().equals(item.getGoods_sn())){
									bean.setGoods_img(item.getGoods_img());
									continue up;
								}
							}
						}
//						notifyDataSetChanged();
					}
				}
				beans.addAll(products);
				updateViews(beans);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 100){
		}else
		if(requestCode == REQUEST_CODE_DISCOUNT){
			if(resultCode==1){
				if(data!=null){
					Discount discount=data.getParcelableExtra("Discount");
					if(discount.isWholeOrder()){
						//整单折扣时，折扣金额需要从新计算
						for(ProductDangAn bean:beans){
							bean.setGoods_price(bean.getGoods_ls_price()*discount.getDiscountRate());//实际支付的价格（折扣后或不折扣）
							bean.setGoods_discountRate(discount.getDiscountRate());
							discount.setDiscountPrice(bean.getGoods_price());//整单折扣时该值需要从新计算
							bean.setDiscount(discount);
						}
					}else{
						curProduct.setGoods_price(discount.getDiscountPrice());
						curProduct.setGoods_discountRate(discount.getDiscountRate());
						curProduct.setDiscount(discount);
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
					et_scan_result.setText(data);
				}
			}
		};
	}
	
	@Subscriber
	void updateByEventBus(String event) {
		if (event.equals(App.EVENT_CLEAR)) {
			clearAll();
		}
	}
	
	private void clearAll(){
		member=null;
		memo="";
		updateMemberViews();
		beans.clear();
		updateViews(beans);
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
