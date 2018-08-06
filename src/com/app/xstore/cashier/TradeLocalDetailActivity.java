package com.app.xstore.cashier;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.model.JvbillsaleInfo;
import com.app.model.JvbillsaledetailInfo;
import com.app.model.response.GetBillSaleByNumResponse;
import com.app.net.Commands;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.GoodsDetailActivity;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;

/**
 * 
 * 交易
 * @author pythoner
 * 
 */
public class TradeLocalDetailActivity extends BaseActivity implements OnClickListener{

	private Context context;
	private TextView item_0,item_1,item_2,item_3,item_4,item_5;
	private ListView listView;
	private CommonAdapter<JvbillsaledetailInfo> adapter;
	private List<JvbillsaledetailInfo> beans=new ArrayList<JvbillsaledetailInfo>();
	private GetBillSaleByNumResponse response;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trade_local_detail);
		context = this;
		initActionBar("交易详情",null,null);
		String chargeCode=getIntent().getStringExtra("TradeInfo");
		if(isEmpty(chargeCode)){
			return;
		}
		initViews();
//		doSosopayTradeQueryRequestTask(chargeCode);
		doCommandGetBillSaleByNum(chargeCode);
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
					updateViews(obj);
				}
			}
		});
	}
	
//	private void doSosopayTradeQueryRequestTask(String chargeCode){
//		SosopayTradeQueryRequestTask task=new SosopayTradeQueryRequestTask(context,chargeCode,new SosopayTradeQueryRequestTask.OnResponseListener() {
//			
//			@Override
//			public void onResponse(SosopayResponse res) {
//				// TODO Auto-generated method stub
//				SosopayTradeQueryResponse response=(SosopayTradeQueryResponse)res;
//				updateViews(response);
//			}
//		});
//		task.execute(1);
//	}
	
	@Override
	public void initViews() {
		item_0 = (TextView) findViewById(R.id.item_0);
		item_1 = (TextView) findViewById(R.id.item_1);
		item_2 = (TextView) findViewById(R.id.item_2);
		item_3 = (TextView) findViewById(R.id.item_3);
		item_4 = (TextView) findViewById(R.id.item_4);
		item_5 = (TextView) findViewById(R.id.item_5);
		listView = (ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> pearnt, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent =new Intent(context, GoodsDetailActivity.class);
				intent.putExtra("ProdNum", beans.get(position).getProdNum());
				startActivity(intent);
			}
		});
	}

	private void notifyDataSetChanged() {
		if (adapter == null) {
			listView.setAdapter(adapter = new CommonAdapter<JvbillsaledetailInfo>(
					context, beans, R.layout.item_trade_detail) {

				@Override
				public void setValues(ViewHolder helper,
						final JvbillsaledetailInfo item, final int position) {
					// TODO Auto-generated method stub
					helper.setText(R.id.item_0, item.getProdNum()+"\n"+item.getSellerUser());
					helper.setText(R.id.item_1, item.getQty()+"件");
					helper.setText(R.id.item_2, "￥"+item.getRetailPrice()+"    "+(item.getDivSaleRate()/10)+"折");
					helper.setText(R.id.item_3, "￥"+item.getSalePrice());
				}

			});
		} else {
			adapter.notifyDataSetChanged();
		}
	}
	
//	private String convertStates(SosopayTradeQueryResponse item){
//		switch (item.getState()) {
//		case SosopayConstants.WAIT_PAY:
//			return "待付款";
//		case SosopayConstants.PAIED:
//			return "已付款";
//		case SosopayConstants.CANCELED:
//			return "已撤单";
//		case SosopayConstants.APPLY_REFUND:
//			return "申请撤单";
//		case SosopayConstants.PART_REFOUNDED:
//			return "部分退款";
//		case SosopayConstants.REFOUNDED:
//			return "已退款";
//		case SosopayConstants.TRADE_CLOSED:
//			return "交易关闭";
//		case SosopayConstants.QUERY_BACK:
//			return "查询时预留，同时查询已退款和已撤单";
//		case SosopayConstants.REFUND_ING:
//			return "退款中";
//		case SosopayConstants.REFOUNDED_FAIL:
//			return "退款失败";
//		case SosopayConstants.START_REFOUNDED_APPLY:
//			return "待退款";
//		case SosopayConstants.START_REFOUNDED_SUCCESS:
//			return "退款成功";
//		case SosopayConstants.START_REFOUNDED_FAIL:
//			return "退款失败";
//		case SosopayConstants.START_REFOUNDED_CANCELED:
//			return "退款取消";
//		case SosopayConstants.START_CANCELED_SUCCESS:
//			return "撤单成功";
//
//		default:
//			return "未知";
//		}
//	}
	
	@Override
	public void updateViews(Object obj) {
		if(obj==null){
			return;
		}
		response = (GetBillSaleByNumResponse) obj;
		
		JvbillsaleInfo billsale=response.getBillSale();
		item_0.setText("交易单号："+billsale.getSaleNo());
		item_1.setText("交易日期："+billsale.getCreatetimeStr());
		item_2.setText("支付信息描述："+billsale.getRemark());
		item_3.setText("交易金额：￥"+formatMoney(billsale.getTotalMoney())+"\n数量："+billsale.getTotalQty());
		item_4.setText("交易状态：已付款");
		item_5.setText("销售模式："+billsale.getSaleType());
		
//		if(response.getState()==SosopayConstants.WAIT_PAY){
//			item_4.setTextColor(0xFFFF6633);
//		}else{
//			item_4.setTextColor(0xFF000000);
//		}
//		item_4.setText("交易状态："+convertStates(response));
		
//		if(response.getState()==SosopayConstants.PAIED){//已付款
//			getRightButton().setText("退款");
//			getRightButton().setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View view) {
//					// TODO Auto-generated method stub
//					doRightButtonClick(view);
//				}
//			});
//		}else{
//			getRightButton().setText("");
//		}
		
		List<JvbillsaledetailInfo> list = response.getDetailList();
		if(isEmptyList(list)){
			showToast("未查到商品");
			return;
		}
		beans.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public void doRightButtonClick(View v) {
		// TODO Auto-generated method stub
//		RefundDialog d=new RefundDialog(context,formatMoney(response.getAmt()/100));
//		d.setOnClickListener(new RefundDialog.OnClickListener() {
//			
//			@Override
//			public void onClick(View v, double refundFee, String refundSubject) {
//				// TODO Auto-generated method stub
//				String busiCode=SosopayRequest.BUSI_CODE;
//				String operid=App.user.getUserInfo().getUser_code();//操作员编号
//				String devid=App.config.getDeviceId();//设备编号
//				String chargeCode=response.getChargeCode();
//				String chargeDownCode=response.getChargeDownCode();
//				String outRefundNo=null;
//				SosopayTradeRefundRequestTask task=new SosopayTradeRefundRequestTask(context,busiCode,operid,devid,refundFee,refundSubject,chargeCode,chargeDownCode,outRefundNo,new OnResponseListener() {
//					
//					@Override
//					public void onResponse(SosopayResponse res) {
//						// TODO Auto-generated method stub
////						SosopayTradeRefundResponse response=(SosopayTradeRefundResponse)res;
////						Log.i("tag", "支付渠道:"+response.getChannelType());
////						Log.i("tag", "退款渠道:"+response.getTradeFundBillList().getTradeFundBill().size());
//						showToast("退款成功!");
//						EventBus.getDefault().post(App.EVENT_REFRESH);
//					}
//				});
//				task.execute(1);
//			}
//		});
//		d.show();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
//		updateTheme();
	}
	
//	private void updateTheme() {
//		setThemeDrawable(context, R.id.btn_search);
//	}
	
}
