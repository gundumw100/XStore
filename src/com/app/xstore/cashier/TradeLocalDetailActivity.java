package com.app.xstore.cashier;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
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
import com.app.model.JvbillsalepayInfo;
import com.app.model.response.GetBillSaleByNumResponse;
import com.app.net.Commands;
import com.app.util.StringUtil;
import com.app.widget.dialog.PrinterListDialog;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;
import com.base.util.D;

/**
 * 
 * 交易
 * @author pythoner
 * 
 */
public class TradeLocalDetailActivity extends BaseActivity implements OnClickListener{

	private Context context;
	private TextView item_0,item_1,item_2,item_3,item_4,item_5,item_6,item_7,item_8;
	private ListView listView;
	private CommonAdapter<JvbillsaledetailInfo> adapter;
	private List<JvbillsaledetailInfo> beans=new ArrayList<JvbillsaledetailInfo>();
	private GetBillSaleByNumResponse response;
	private TextView btn_tuidan;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trade_local_detail);
		context = this;
		initActionBar("交易详情","打印小票",null);
		String chargeCode=getIntent().getStringExtra("TradeInfo");
		if(isEmpty(chargeCode)){
			return;
		}
		initViews();
		doCommandGetBillSaleByNum(chargeCode);
	}

	private void doCommandGetBillSaleByNum(String saleNo){
		String shopCode=App.user.getShopInfo().getShop_code();
		Commands.doCommandGetBillSaleByNum(context, shopCode, saleNo, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					GetBillSaleByNumResponse obj=mapperToObject(response, GetBillSaleByNumResponse.class);
					updateViews(obj);
				}
			}
		});
	}
	
	@Override
	public void initViews() {
		item_0 = (TextView) findViewById(R.id.item_0);
		item_1 = (TextView) findViewById(R.id.item_1);
		item_2 = (TextView) findViewById(R.id.item_2);
		item_3 = (TextView) findViewById(R.id.item_3);
		item_4 = (TextView) findViewById(R.id.item_4);
		item_5 = (TextView) findViewById(R.id.item_5);
		item_6 = (TextView) findViewById(R.id.item_6);
		item_7 = (TextView) findViewById(R.id.item_7);
		item_8 = (TextView) findViewById(R.id.item_8);
		
		listView = (ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> pearnt, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				startProductDetailActivity(beans.get(position).getProdNum());
			}
		});
		btn_tuidan=(TextView)findViewById(R.id.btn_tuidan);
		btn_tuidan.setOnClickListener(this);
	}

	private void notifyDataSetChanged() {
		if (adapter == null) {
			listView.setAdapter(adapter = new CommonAdapter<JvbillsaledetailInfo>(
					context, beans, R.layout.item_trade_detail) {

				@Override
				public void setValues(ViewHolder helper,
						final JvbillsaledetailInfo item, final int position) {
					// TODO Auto-generated method stub
					helper.setText(R.id.item_5, item.getProdName()+"	"+item.getSellerUser());
					helper.setText(R.id.item_0, item.getProdNum());
					helper.setText(R.id.item_1, item.getQty()+"件");
					
					String divSaleRate = "无折扣";
					if (item.getDivSaleRate() >= 100) {
//						divSaleRate = "";
					} else {
						divSaleRate = StringUtil.formatNumber(item.getDivSaleRate() *10, "###0.##")+ "折";
					}
					
					helper.setText(R.id.item_2, "￥"+StringUtil.formatMoney(item.getRetailPrice()));
					helper.setText(R.id.item_3, divSaleRate);
					helper.setText(R.id.item_4, "￥"+StringUtil.formatMoney(item.getSalePrice()));
				}

			});
		} else {
			adapter.notifyDataSetChanged();
		}
	}
	
	
	@Override
	public void updateViews(Object obj) {
		if(obj==null){
			return;
		}
		response = (GetBillSaleByNumResponse) obj;
		
		JvbillsaleInfo billsale=response.getBillSale();
		item_0.setText("交易单号："+billsale.getSaleNo());
		item_1.setText("交易日期："+billsale.getCreatetimeStr());
		item_2.setText("销售模式："+getSaleTypeStr(billsale.getSaleType()));
		item_3.setText("交易金额：￥"+formatMoney(billsale.getTotalMoney()));
		item_4.setText("商品数量："+billsale.getTotalQty());
		item_5.setText("交易状态："+(billsale.getTotalQty()>0?"已支付":"已退单"));
		
		item_6.setText("支付方式：");
		List<JvbillsalepayInfo> payList=response.getPayList();
		if(!isEmptyList(payList)){
			for(JvbillsalepayInfo item:payList){
				item_6.append(item.getPayCode());
			}
		}
		
		item_7.setText("备        注："+billsale.getRemark());
		
		if(isEmpty(billsale.getVipId())){
			item_8.setText("");
		}else{
			item_8.append("会  员  ID："+billsale.getVipId()+"\n");
			item_8.append("会员卡号："+"\n");
			item_8.append("消费积分："+formatNumber(billsale.getVipConsumeValue(), "###0.#"));
		}
		
		btn_tuidan.setVisibility(billsale.getTotalQty()>0?View.VISIBLE:View.GONE);
		
		List<JvbillsaledetailInfo> list = response.getDetailList();
		if(isEmptyList(list)){
			showToast("未查到商品");
			return;
		}
		beans.addAll(list);
		notifyDataSetChanged();
	}

	//销售模式:XS(销售)、TH(退货)、HH(换货)
	private String getSaleTypeStr(String saleType){
		if("XS".equals(saleType)){
			return "销售";
		}else if("TH".equals(saleType)){
			return "退货";
		}else if("HH".equals(saleType)){
			return "换货";
		}
		return "未知";
	}
	
	@Override
	public void doRightButtonClick(View v) {
		// TODO Auto-generated method stub
		PrinterListDialog d=new PrinterListDialog(context,response);
		d.show();
		
//		doPrintReceipt(null,response);//蓝牙打印小票
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_tuidan:
			String message="确定要退单吗？";
			D.showDialog(this, message, "确定", "取消", new D.OnPositiveListener() {
				
				@Override
				public void onPositive() {
					// TODO Auto-generated method stub
//					doCommandSaveBillSaleTH(beans,response.getBillSale().getTotalMoney(),"0");
					doCommandSaveBillSaleTH(response,"0");
				}
			});
			break;

		default:
			break;
		}
	}

	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		updateTheme();
	}
	
	private void updateTheme() {
		setThemeDrawable(context, R.id.btn_tuidan);
	}
	
}
