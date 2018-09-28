package com.app.xstore.space;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.model.JvbillsaleInfo;
import com.app.model.JvbillsaledetailInfo;
import com.app.model.JvclerkspaceInfo;
import com.app.model.JvclerkspacecommentInfo;
import com.app.model.response.GetBillSaleByNumResponse;
import com.app.model.response.GetClerkSpaceInfoResponse;
import com.app.net.Commands;
import com.app.util.FileDownloader;
import com.app.util.FileUtil;
import com.app.widget.CommentDialog;
import com.app.widget.SimpleListPopupWindow;
import com.app.widget.SimpleListPopupWindow.OnItemClickListener;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;
import com.base.util.comm.TimeUtils;
import com.squareup.picasso.Picasso;
import com.widget.flowlayout.FlowLayout;
import com.widget.view.MaterialFavoriteButton;

/**
 * 文章详情
 * @author pythoner
 *
 */
public class SpaceDetailActivity extends BaseActivity implements View.OnClickListener{

	private Context context;
	private LinearLayout layout_image,layout_sale,layout_order;
	private FlowLayout layout_video,layout_ppt;
	private TextView tv_title_b,tv_content,tv_name,tv_date,tv_order_head,tv_order_foot,tvCommentSize,tvfavoriteSize;
	private ImageView iv_icon;//头像
	private ListView listView;
	private CommonAdapter<JvclerkspacecommentInfo> adapter;
	private List<JvclerkspacecommentInfo> beans;
	private int ID;//文章ID
	private GetClerkSpaceInfoResponse obj;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_space_detail);
		context=this;
		ID=getIntent().getIntExtra("ID", 0);
		initActionBar("文章详情", null, null);
		initViews();
		doCommandGetClerkSpaceDetailInfo(ID);
	}
	
	private void doCommandGetClerkSpaceDetailInfo(int ID){
		String ClerkSpaceID=String.valueOf(ID);
		String Operator=App.user.getUserInfo().getUser_code();
		Commands.doCommandGetClerkSpaceDetailInfo(context, ClerkSpaceID, Operator, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if(isSuccess(response)){
					obj=mapperToObject(response, GetClerkSpaceInfoResponse.class);
					JvclerkspaceInfo clerkSpaceInfo=obj.getClerkspaceInfo();
					tv_title_b.setText(clerkSpaceInfo.getTitle());
					tv_content.setText(clerkSpaceInfo.getDescription());
//					tv_name.setText(isEmpty(clerkSpaceInfo.getNickName())?clerkSpaceInfo.getCreator():clerkSpaceInfo.getNickName());
					tv_name.setText(clerkSpaceInfo.getCreator());
					tv_date.setText("@"+clerkSpaceInfo.getCreatedatestr());
//					Commands.loadImageByVolley(clerkSpaceInfo.getHeadportraitURL(), iv_icon, R.drawable.default_user,0);
					Picasso.with(context).load(clerkSpaceInfo.getHeadportraiturl()).placeholder(R.drawable.default_user).error(R.drawable.default_user).into(iv_icon);
					
					tvfavoriteSize.setText(String.valueOf(clerkSpaceInfo.getPraisecount()));
					
					beans=obj.getCommentInfos();
					if(!isEmptyList(beans)){
						updateViews(beans);
					}
					
					if(!isEmpty(clerkSpaceInfo.getImageurl())){
						layout_image.removeAllViews();
						final String[] urls=clerkSpaceInfo.getImageurl().split(";");
						if(urls.length>0){
							LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
							params.topMargin=8;
							params.bottomMargin=8;
							params.leftMargin=8;
							params.rightMargin=8;
							int position=0;
							for(final String url:urls){
								if(!isEmpty(url)){
									final ImageView child=new ImageView(context);
									child.setLayoutParams(params);
									child.setScaleType(ScaleType.CENTER);
									child.setTag(position);
									child.setOnClickListener(new View.OnClickListener() {
										
										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
//											changeToImageActivity(child,url);
											ArrayList<String> imgs=new ArrayList<String>();
											for(String b:urls){
												imgs.add(b);
											}
											changeToImageGalleryActivity(imgs,(Integer)child.getTag());
										}
									});
//									Commands.loadImageByVolley(url, child, R.drawable.default_img,0);
									Picasso.with(context).load(url).placeholder(R.drawable.default_img).error(R.drawable.default_img).into(child);
									
									layout_image.addView(child);
									position++;
								}
							}
						}
					}
					if(!isEmpty(clerkSpaceInfo.getVideourl())){
						layout_video.removeAllViews();
						String[] urls=clerkSpaceInfo.getVideourl().split(";");
						if(urls.length>0){
							int index=1;
							LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
							params.gravity=Gravity.CENTER_HORIZONTAL;
							for(final String url:urls){
								if(!isEmpty(url)){
									final TextView child=new TextView(context);
									child.setLayoutParams(params);
									child.setPadding(8, 8, 8, 8);
									child.setGravity(Gravity.CENTER);
									child.setText("【下载视频"+index+"】");
									child.setTag("视频"+index);
									child.setOnClickListener(new View.OnClickListener() {
										
										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											doDownloadAndOpenFile(url,child);
										}
									});
									layout_video.addView(child);
									index++;
								}
							}
						}
					}
					if(!isEmpty(clerkSpaceInfo.getPpturl())){
						layout_ppt.removeAllViews();
						String[] urls=clerkSpaceInfo.getPpturl().split(";");
						if(urls.length>0){
							int index=1;
							LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
							for(final String url:urls){
								if(!isEmpty(url)){
									final TextView child=new TextView(context);
									child.setLayoutParams(params);
									child.setPadding(8, 8, 8, 8);
									child.setGravity(Gravity.CENTER);
									child.setText("【下载PPT"+index+"】");
									child.setTag("PPT"+index);
									child.setOnClickListener(new View.OnClickListener() {
										
										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											doDownloadAndOpenFile(url,child);
										}
									});
									layout_ppt.addView(child);
									index++;
								}
							}
						}
					}
					
					//小票号
					String saleNo=obj.getClerkspaceInfo().getSaleno();
					if(isEmpty(saleNo)){
						layout_sale.setVisibility(View.GONE);
					}else{
						doCommandGetBillSaleByNum(saleNo);
					}
					/*
					List<BillSaleDetailInfo> list=obj.getDetailEntitys();
					if(!isEmptyList(list)){
						layout_sale.setVisibility(View.VISIBLE);
						
						BillSaleHeadInfo head=obj.getHeadEntity();
						tv_order_head.append("店名："+clerkSpaceInfo.getOrgName()+"\n");
						if(head!=null){
							tv_order_head.append("店号："+clerkSpaceInfo.getOrgCode()+"\n");
							tv_order_head.append("小票号："+head.getSaleNo()+"\n");
							String saleType="";
							if("RT".equals(head.getSaleType())){
								saleType="零售";
							}else if("TH".equals(head.getSaleType())){
								saleType="退货";
							}else if("HH".equals(head.getSaleType())){
								saleType="换货";
							}
							tv_order_head.append("交易类型："+saleType+"\n");
							tv_order_head.append("OS订单号："+(head.getOOSRefErpNO()==null?"":head.getOOSRefErpNO())+"\n");
							tv_order_head.append("时间："+head.getLOCAL_TIME()+"\n");
							tv_order_head.append("收银员："+head.getCashierUserID()+"\n");
							tv_order_head.append("VIP号："+(head.getVIPCode()==null?"":head.getVIPCode()));
						}
						
						LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
						layout_order.removeAllViews();
						for (BillSaleDetailInfo bean : list) {
							TextView tv=new TextView(context);
							tv.setLayoutParams(params);
							tv.append(bean.getProdID()+ "		"+bean.getQty()+"        "+bean.getRetailPrice()+"              "+bean.getDiscountRate()+"%"+"              "+bean.getSalesValue()+"\n");
							tv.append(bean.getProdName()+(bean.getIsOOS()?"【线上发货】":"")+"\n");
							tv.append(bean.getColorName() + "		"+bean.getSizeName()+"\n");
							tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
							tv.setTextColor(App.res.getColor(R.color.grayDark));
							layout_order.addView(tv);
						}
						
						//debug 打印会员抵现券...
						ArrayList<BillSalePromotionInfo> promotions=obj.getPromotionEntitys();
						if(promotions!=null){
							for(BillSalePromotionInfo p:promotions){
								TextView tv=new TextView(context);
								tv.setLayoutParams(params);
								tv.append(p.getPresentDescription()+":"+p.getDiscountMoney()+"\n");
								tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
								tv.setTextColor(App.res.getColor(R.color.grayDark));
								layout_order.addView(tv);
							}
						}
						
						if(head!=null){
							double TRAN_VAL=0;
//							tv_order_foot.append("促销: -"+formatMoney(head.getDiscountMoney()-TRAN_VAL)+"\n");
							tv_order_foot.append("总数量："+head.getTotalQty()+"\n");
							tv_order_foot.append("应收金额："+formatMoney(head.getTotalMoney())+"\n");
							
							ArrayList<BillSalePayInfo> pays=obj.getPayEntitys();
							double shishoujine=0.00;
							double shishouzhensong=0.00;
							double zhaolin=0.00;
							if (pays != null) {
								for (BillSalePayInfo b : pays) {
									if ("Voucher".equals(b.getPayCode())) {// 代金券
										shishouzhensong += b.getPayValue();
//										shishouzhensong += b.getActPayValue();
									} else if ("Card".equals(b.getPayCode())) {// 信用卡
									} else if ("Cash".equals(b.getPayCode())) {
										shishoujine += b.getPayValue();
										zhaolin += b.getChange();
									} else if ("CashVouc".equals(b.getPayCode())) {// 抵用券
									// shishouzhensong+=b.getActPayValue();
									}
								}
							}
							tv_order_foot.append("实收现金："+formatMoney(shishoujine)+"\n");
							tv_order_foot.append("实收赠送："+formatMoney(shishouzhensong)+"\n");
							tv_order_foot.append("找零："+ formatMoney(zhaolin)+"\n");
							
							List<BillSaleBankInfo> banks=obj.getBankEntitys();
							double m=0.00;//支付宝的钱
							double n=0.00;//微信的钱
							double o=0.00;//信用卡的钱
							if(banks!=null){
								for(BillSaleBankInfo b:banks){
									if("01".equals(b.getCardType())){
										m+=b.getAmount();
									}else  if("02".equals(b.getCardType())){
										n+=b.getAmount();
									}else  if("03".equals(b.getCardType())){
										o+=b.getAmount();
									}
								}
							}
							
							tv_order_foot.append("支付宝："+formatMoney(m)+"\n");
							tv_order_foot.append("微信："+formatMoney(n)+"\n");
							tv_order_foot.append("实收信用卡："+formatMoney(o)+"\n");
							
							if(banks!=null){
								for(BillSaleBankInfo b:banks){
									if(b.getAdditional()!=null){
										tv_order_foot.append(b.getAdditional()+"\n");
									}
								}
							}
							
						}
					}else{
						layout_sale.setVisibility(View.GONE);
					}*/
				}
			}
		});
	}
	
	private void doCommandGetBillSaleByNum(String saleNo){
		String shopCode=App.user.getShopInfo().getShop_code();
		Commands.doCommandGetBillSaleByNum(context, shopCode, saleNo, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					GetBillSaleByNumResponse obj=mapperToObject(response, GetBillSaleByNumResponse.class);
					if(obj!=null){
						createSalebill(obj);
					}
				}
			}
		});
	}
	
	private void createSalebill(GetBillSaleByNumResponse obj){
		layout_sale.setVisibility(View.VISIBLE);
		JvbillsaleInfo billsale=obj.getBillSale();
		
		tv_order_head.append("交易单号：" + billsale.getSaleNo() + "\n");
		tv_order_head.append("交易日期：" + billsale.getCreatetimeStr() + "\n");
		tv_order_head.append("支付信息描述：" + billsale.getRemark() + "\n");
		tv_order_head.append("交易金额：￥" + formatMoney(billsale.getTotalMoney()) + "\n");
		tv_order_head.append("数量：" + billsale.getTotalQty() + "\n");
		tv_order_head.append("交易状态：已付款" + "\n");
		tv_order_head.append("销售模式：" + billsale.getSaleType() + "\n");
		
		List<JvbillsaledetailInfo> list = obj.getDetailList();
		if(isEmptyList(list)){
			return;
		}
		
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		layout_order.removeAllViews();
		for (JvbillsaledetailInfo bean : list) {
			TextView tv=new TextView(context);
			tv.setLayoutParams(params);
			tv.append(bean.getProdNum()+ "		"+bean.getQty()+"        "+bean.getRetailPrice()+"              "+(bean.getDivSaleRate()/10)+"折"+"              "+bean.getSalePrice()+"\n");
//			tv.append(bean.getProdName()+(bean.getIsOOS()?"【线上发货】":"")+"\n");
//			tv.append(bean.getColorName() + "		"+bean.getSizeName()+"\n");
			tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			tv.setTextColor(App.res.getColor(R.color.grayDark));
			layout_order.addView(tv);
		}
		
	}
	
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		layout_image=(LinearLayout)findViewById(R.id.layout_image);
		layout_video=(FlowLayout)findViewById(R.id.layout_video);
		layout_ppt=(FlowLayout)findViewById(R.id.layout_ppt);
		layout_sale=(LinearLayout)findViewById(R.id.layout_sale);
		layout_order=(LinearLayout)findViewById(R.id.layout_order);
		tv_order_head=(TextView)findViewById(R.id.tv_order_head);
//		tv_order_foot=(TextView)findViewById(R.id.tv_order_foot);
		tv_title_b=(TextView)findViewById(R.id.tv_title_b);
		tv_content=(TextView)findViewById(R.id.tv_content);
		tv_name=(TextView)findViewById(R.id.tv_name);
		tv_date=(TextView)findViewById(R.id.tv_date);
		iv_icon=(ImageView)findViewById(R.id.iv_icon);
		iv_icon.setOnClickListener(this);
		listView=(ListView)findViewById(R.id.listView);
		tvCommentSize=(TextView)findViewById(R.id.tvCommentSize);
		tvfavoriteSize=(TextView)findViewById(R.id.tvfavoriteSize);
		MaterialFavoriteButton btn_favorite = (MaterialFavoriteButton) findViewById(R.id.btn_favorite);
		btn_favorite.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
					@Override
					public void onFavoriteChanged(
							MaterialFavoriteButton buttonView, boolean favorite) {
						doCommandClerkSpacePraise();
					}
				});
		MaterialFavoriteButton btn_comment = (MaterialFavoriteButton) findViewById(R.id.btn_comment);
		btn_comment.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
			@Override
			public void onFavoriteChanged(
					MaterialFavoriteButton buttonView, boolean favorite) {
				showCommentDialog();
			}
		});
	}

	private void showCommentDialog(){
		CommentDialog d=new CommentDialog(context);
		d.setOnClickListener(new CommentDialog.OnClickListener() {
			
			@Override
			public void onClick(View v, String text, int rate) {
				// TODO Auto-generated method stub
				doCommandClerkSpaceComment(text);
			}
		});
		d.show();
	}
	
	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
		notifyDataSetChanged();
		tvCommentSize.setText("("+beans.size()+"条)");
	}

	private void notifyDataSetChanged(){
		if(adapter==null){
			listView.setAdapter(adapter = new CommonAdapter<JvclerkspacecommentInfo>(
					context, beans, R.layout.item_space_comment) {

				@Override
				public void setValues(ViewHolder helper,
						final JvclerkspacecommentInfo item, final int position) {
					// TODO Auto-generated method stub
					helper.setText(R.id.item_0, item.getOperator());
					helper.setText(R.id.item_1, item.getOperatedate());
					helper.setText(R.id.item_2, item.getSpacecomment());
				}

			});
		}else{
			adapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_icon:
			if(obj.getClerkspaceInfo()!=null){
				doCommandCheckClerkWatch(obj.getClerkspaceInfo().getCreator());
			}
			break;
		default:
			break;
		}
	}

	private void doCommandCheckClerkWatch(String SlaveUserID){
		/*String MasterUserID=getUser(context).getUserID();
		Commands.doCommandCheckClerkWatch(context, MasterUserID, SlaveUserID, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				if(isSuccess(response)){
					CheckClerkWatchResponse obj=mapperToObject(response, CheckClerkWatchResponse.class);
					boolean isFocus=!isEmptyList(obj.getWatchInfos());
					showPopWindow(isFocus);
				}
			}
		});*/
	}
	
	private void doCommandClerkSpacePraise(){
		String clerkSpaceID=String.valueOf(obj.getClerkspaceInfo().getId());
		String operator=App.user.getUserInfo().getUser_code();
		Commands.doCommandClerkSpacePraise(context, clerkSpaceID, operator, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				if(isSuccess(response)){
					int praiseCount=obj.getClerkspaceInfo().getPraisecount()+1;
					obj.getClerkspaceInfo().setPraisecount(praiseCount);
					showToast("点赞成功");
					tvfavoriteSize.setText(String.valueOf(praiseCount));
				}
			}
		});
	}
	
	private void showPopWindow(final boolean isFocus){
		List<String> list=new ArrayList<String>();
		list.add("聊天");
		list.add(isFocus?"取消关注":"关注");
		list.add("资料");
		View view=LayoutInflater.from(this).inflate(R.layout.popupwindow_simple, null);
		SimpleListPopupWindow<String> slpw=new SimpleListPopupWindow<String>(this, view, iv_icon.getWidth()*2, list);
		slpw.showAsDropDown(iv_icon, 0, 0);
		slpw.setOnItemClickListener(new OnItemClickListener<String>() {

			@Override
			public void onItemClick(int position, String item) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					showToast(R.string.info_wait);
					break;
				case 1:
					if(isFocus){
						doCommandClerkWatchCancel();
					}else{
						doCommandClerkWatchAdd();
					}
					break;
				case 2:
					/*Intent intent=new Intent(context,UserDetailActivity.class);
					intent.putExtra("UserID", obj.getClerkSpaceInfo().getCreator());
					intent.putExtra("Focus", isFocus);
					startActivity(intent);*/
					break;

				default:
					break;
				}
			}
		});
	}
	
	private void doCommandClerkWatchCancel(){
		/*ClerkWatchInfo bean=new ClerkWatchInfo();
		bean.setMasterUserID(getUser(context).getUserID());
		bean.setSlaveUserID(obj.getClerkSpaceInfo().getCreator());
		Commands.doCommandClerkWatchCancel(context, bean, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				if(isSuccess(response)){
					showToast("取消关注成功");
				}
			}
		});*/
	}
	
	private void doCommandClerkWatchAdd(){
		/*ClerkWatchInfo bean=new ClerkWatchInfo();
		bean.setMasterUserID(getUser(context).getUserID());
		bean.setSlaveUserID(obj.getClerkSpaceInfo().getCreator());
		Commands.doCommandClerkWatchAdd(context, bean, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				if(isSuccess(response)){
					showToast("关注成功！");
				}
			}
		});*/
	}
	
	private void doCommandClerkSpaceComment(String comment){
		final JvclerkspacecommentInfo bean=new JvclerkspacecommentInfo();
		bean.setClerkspaceid(String.valueOf(ID));
		bean.setSpacecomment(comment);
		bean.setOperator(App.user.getUserInfo().getUser_code());
		bean.setOperatedate(TimeUtils.getNow("yyyy-MM-dd HH:mm:ss"));
		Commands.doCommandClerkSpaceComment(context, bean, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				if(isSuccess(response)){
					showToast("发表成功！");
					beans.add(0,bean);
					updateViews(beans);
				}
			}
		});
	}
	
	private void doDownloadAndOpenFile(String url,final TextView tv){
		//是否存在文件（下载过）
		String downloadDir = FileDownloader.DOWNLOAD_DIR;//保存的文件夹
		String rootDir = Environment.getExternalStorageDirectory().getAbsolutePath();
		String filePath = rootDir + downloadDir;
		String fileName = url.substring(url.lastIndexOf("/") + 1);
		File file = new File(filePath, fileName);
		
		if(file.exists()){
			FileUtil.openFile(context,file);
			tv.setText("【打开"+tv.getTag()+"】");
		}else{
			FileDownloader downloader=new FileDownloader(new FileDownloader.OnDownloadListener() {
				
				@Override
				public void onDownloading(int progress) {
					// TODO Auto-generated method stub
					tv.setText("【下载中："+progress+"%"+"】");
				}
				
				@Override
				public void onDownloaded(File file) {
					// TODO Auto-generated method stub
					FileUtil.openFile(context,file);
					tv.setText("【打开"+tv.getTag()+"】");
				}
				
			});
			downloader.execute(url);
		}
	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
	}
	
}
