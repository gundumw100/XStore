package com.app.xstore.cashier;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.simple.eventbus.Subscriber;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;

import com.app.net.SosopayTradeBatchQueryRequestTask;
import com.app.widget.SimplePairListPopupWindow;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;
import com.base.util.comm.TimeUtils;
import com.sosopay.SosopayConstants;
import com.sosopay.SosopayResponse;
import com.sosopay.response.SosopayTradeBatchQueryResponse;
import com.sosopay.vo.TradeInfo;
import com.widget.view.DatePickerDialog;
import com.widget.view.LoadMoreListView;

/**
 * 
 * 交易列表
 * @author pythoner
 * 
 */
@Deprecated
public class TradeListActivity extends BaseActivity implements OnClickListener{

	private Context context;
	private TextView btn_beginDate,btn_endDate,btn_state;
	private LoadMoreListView listView;
	private CommonAdapter<TradeInfo> adapter;
	private List<TradeInfo> beans=new ArrayList<TradeInfo>();
	private int pageNum = 1;
	private int pageSize = 20;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trade_list);
		context = this;
		initActionBar("交易列表",null,null);
		initViews();
		doSosopayTradeBatchQueryRequestTask();
	}

	private void doSosopayTradeBatchQueryRequestTask(){
		String beginDateS = btn_beginDate.getText().toString();
		String endDateS = btn_endDate.getText().toString();
		Integer state=(Integer)btn_state.getTag();
		if(state==null){
			state=-1;//全部订单
		}
		if (isEmpty(beginDateS) || isEmpty(endDateS)) {
			return;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Date beginDate = null, endDate = null;
		try {
			beginDate = sdf.parse(beginDateS);
			endDate = sdf.parse(endDateS);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		    
		String operid=App.user.getUserInfo().getUser_code();//操作员编号
		SosopayTradeBatchQueryRequestTask task=new SosopayTradeBatchQueryRequestTask(context,operid,state,beginDate,endDate,pageNum,pageSize,new SosopayTradeBatchQueryRequestTask.OnResponseListener() {
			
			@Override
			public void onResponse(SosopayResponse res) {
				// TODO Auto-generated method stub
				SosopayTradeBatchQueryResponse response=(SosopayTradeBatchQueryResponse)res;
				updateViews(response);
			}
		});
		task.execute(1);
	}
	
	@Override
	public void initViews() {
		btn_beginDate = (TextView) findViewById(R.id.btn_beginDate);
		btn_beginDate.setOnClickListener(this);
		btn_endDate = (TextView) findViewById(R.id.btn_endDate);
		btn_endDate.setOnClickListener(this);
		btn_state = (TextView) findViewById(R.id.btn_state);
		btn_state.setOnClickListener(this);
		
		btn_beginDate.setText(TimeUtils.getAWeekAgo("yyyy-MM-dd"));
		btn_endDate.setText(TimeUtils.getNow("yyyy-MM-dd"));
		
		listView = (LoadMoreListView) findViewById(R.id.listView);
		listView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				pageNum+=1;
				doSosopayTradeBatchQueryRequestTask();
			}
		});
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> pearnt, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,TradeDetailActivity.class);
				intent.putExtra("TradeInfo", beans.get(position).getChargeCode());
				startActivity(intent);
			}
		});

	}

	private void notifyDataSetChanged() {
		if (adapter == null) {
			listView.setAdapter(adapter = new CommonAdapter<TradeInfo>(
					context, beans, R.layout.item_trade_list) {

				@Override
				public void setValues(ViewHolder helper,
						final TradeInfo item, final int position) {
					// TODO Auto-generated method stub
					helper.setText(R.id.item_0, "订单号："+item.getChargeDownCode());
					helper.setText(R.id.item_4, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA).format(item.getBeginTime()));
					helper.setText(R.id.item_1, item.getPaySubject());
					helper.setText(R.id.item_2, formatMoney(item.getAmt()/100));
					if(item.getState()==SosopayConstants.WAIT_PAY){
						helper.setTextColor(R.id.item_3,0xFFFF6633);
					}else if(item.getState()==SosopayConstants.PAIED){
						helper.setTextColor(R.id.item_3,0xFF55FF55);
					}else{
						helper.setTextColor(R.id.item_3,0xFF000000);
					}
					helper.setText(R.id.item_3,convertStates(item));
				}

			});
		} else {
			adapter.notifyDataSetChanged();
		}
	}
	
	private String convertStates(TradeInfo item){
		switch (item.getState()) {
		case SosopayConstants.WAIT_PAY:
			return "待付款";
		case SosopayConstants.PAIED:
			return "已付款";
		case SosopayConstants.CANCELED:
			return "已撤单";
		case SosopayConstants.APPLY_REFUND:
			return "申请撤单";
		case SosopayConstants.PART_REFOUNDED:
			return "部分退款";
		case SosopayConstants.REFOUNDED:
			return "已退款";
		case SosopayConstants.TRADE_CLOSED:
			return "交易关闭";
		case SosopayConstants.QUERY_BACK:
			return "查询时预留，同时查询已退款和已撤单";
		case SosopayConstants.REFUND_ING:
			return "退款中";
		case SosopayConstants.REFOUNDED_FAIL:
			return "退款失败";
		case SosopayConstants.START_REFOUNDED_APPLY:
			return "待退款";
		case SosopayConstants.START_REFOUNDED_SUCCESS:
			return "退款成功";
		case SosopayConstants.START_REFOUNDED_FAIL:
			return "退款失败";
		case SosopayConstants.START_REFOUNDED_CANCELED:
			return "退款取消";
		case SosopayConstants.START_CANCELED_SUCCESS:
			return "撤单成功";

		default:
			return "未知";
		}
	}
	
	@Override
	public void updateViews(Object obj) {
		listView.onLoadMoreComplete();
		if(obj==null){
			return;
		}
		SosopayTradeBatchQueryResponse response = (SosopayTradeBatchQueryResponse) obj;
		List<TradeInfo> list = response.getTradeInfos();
		if (isEmptyList(list)) {
			listView.removeLoadMoreListener();
			if(isEmptyList(beans)){
				setEmptyView(listView, "没有记录");
				return;
			}
			showToast("没有记录了");
			return;
		}
		beans.addAll(list);
		notifyDataSetChanged();
	}

	private void refreshListView() {
		listView.resetLoadMoreListener();
		pageNum = 0;
		beans.clear();
		notifyDataSetChanged();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_state:
			showSimplePopupWindow(v);
			break;
		case R.id.btn_beginDate:
			showDatePickerDialog(btn_beginDate);
			break;
		case R.id.btn_endDate:
			showDatePickerDialog(btn_endDate);
			break;

		default:
			break;
		}
	}

	private void showSimplePopupWindow(final View v){
		ArrayList<Pair<Integer, String>> items = new ArrayList<Pair<Integer, String>>();
		items.add(new Pair<Integer, String>(-1, "全部状态"));
		items.add(new Pair<Integer, String>(SosopayConstants.WAIT_PAY, "待付款"));
		items.add(new Pair<Integer, String>(SosopayConstants.PAIED, "已付款"));
//		items.add(new Pair<Integer, String>(SosopayConstants.CANCELED, "已撤单"));
//		items.add(new Pair<Integer, String>(SosopayConstants.APPLY_REFUND, "申请撤单"));
//		items.add(new Pair<Integer, String>(SosopayConstants.PART_REFOUNDED, "部分退款"));
		items.add(new Pair<Integer, String>(SosopayConstants.REFOUNDED, "已退款"));
		items.add(new Pair<Integer, String>(SosopayConstants.TRADE_CLOSED, "交易关闭"));
//		items.add(new Pair<Integer, String>(SosopayConstants.QUERY_BACK, "查询时预留，同时查询已退款和已撤单"));
//		items.add(new Pair<Integer, String>(SosopayConstants.REFUND_ING, "退款中"));
//		items.add(new Pair<Integer, String>(SosopayConstants.REFOUNDED_FAIL, "退款失败"));
//		items.add(new Pair<Integer, String>(SosopayConstants.START_REFOUNDED_APPLY, "待退款"));
		items.add(new Pair<Integer, String>(SosopayConstants.START_REFOUNDED_SUCCESS, "退款成功"));
//		items.add(new Pair<Integer, String>(SosopayConstants.START_REFOUNDED_FAIL, "退款失败"));
//		items.add(new Pair<Integer, String>(SosopayConstants.START_REFOUNDED_CANCELED, "退款取消"));
//		items.add(new Pair<Integer, String>(SosopayConstants.START_CANCELED_SUCCESS, "撤单成功"));
		
		View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
		final SimplePairListPopupWindow<Integer, String> popupWindow = new SimplePairListPopupWindow<Integer, String>(context,view, v.getWidth(), items);
		popupWindow.showAsDropDown(v, 0, 0);
		popupWindow.setOnItemClickListener(new SimplePairListPopupWindow.OnItemClickListener<Integer, String>() {

			@Override
			public void onItemClick(int position, Pair<Integer, String> pair) {
				// TODO Auto-generated method stub
				refreshListView();
				((TextView)v).setText(pair.second);
				((TextView)v).setTag(pair.first);
				doSosopayTradeBatchQueryRequestTask();
			}
		});
	}
	
	private void showDatePickerDialog(final TextView tv){
		DatePickerDialog dialog = new DatePickerDialog(context, R.style.Theme_Dialog, tv.getText().toString());
		 dialog.setOnButtonClickListener(new DatePickerDialog.OnButtonClickListener() {

			@Override
			public void onCancelClick() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onOKClick(String year, String month, String date) {
				// TODO Auto-generated method stub
				refreshListView();
				tv.setText(year + "-" + month + "-" + date);
				doSosopayTradeBatchQueryRequestTask();
			}
		 });
       dialog.show();
	}
	
	@Subscriber
	void updateByEventBus(String event) {
		if (event.equals(App.EVENT_REFRESH)) {
			refreshListView();
			doSosopayTradeBatchQueryRequestTask();
		}
	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		updateTheme();
	}
	
	private void updateTheme() {
//		setThemeDrawable(context, R.id.btn_search);
	}
	
}
