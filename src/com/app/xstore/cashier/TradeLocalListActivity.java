package com.app.xstore.cashier;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.model.JvbillsaleInfo;
import com.app.model.JvbillsalepayInfo;
import com.app.model.response.GetBillSaleByDateResponse;
import com.app.net.Commands;
import com.app.widget.SimpleListDialog;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;
import com.base.util.comm.TimeUtils;
import com.widget.view.DatePickerDialog;
import com.widget.view.LoadMoreListView;

/**
 * 
 * 交易列表
 * @author pythoner
 * 
 */
public class TradeLocalListActivity extends BaseActivity implements OnClickListener{

	private Context context;
	private TextView btn_beginDate,btn_endDate,btn_state;
	private LoadMoreListView listView;
	private CommonAdapter<JvbillsaleInfo> adapter;
	private List<JvbillsaleInfo> beans=new ArrayList<JvbillsaleInfo>();
	private JvbillsaleInfo curBean;
	private int pageNum = 1;//startNum
	private int pageSize = 20;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trade_list);
		context = this;
		initActionBar("交易列表",null,null);
		initViews();
		doCommandGetBillSaleByDate();
	}

	private void doCommandGetBillSaleByDate(){
		String beginDateS = btn_beginDate.getText().toString();
		String endDateS = btn_endDate.getText().toString();
		String shopCode=App.user.getShopInfo().getShop_code();
		Commands.doCommandGetBillSaleByDate(context, shopCode, beginDateS, endDateS,pageNum,pageSize, new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				Log.i("tag", "response="+response.toString());
				if (isSuccess(response)) {
					GetBillSaleByDateResponse obj=mapperToObject(response, GetBillSaleByDateResponse.class);
					if(obj!=null){
						List<JvbillsaleInfo> list=obj.getHeadInfo();
						updateViews(list);
					}
				}
			}
		});
	}
	
	@Override
	public void initViews() {
		btn_beginDate = (TextView) findViewById(R.id.btn_beginDate);
		btn_beginDate.setOnClickListener(this);
		btn_endDate = (TextView) findViewById(R.id.btn_endDate);
		btn_endDate.setOnClickListener(this);
		btn_state = (TextView) findViewById(R.id.btn_state);
		btn_state.setOnClickListener(this);
		btn_state.setVisibility(View.GONE);
		
		btn_beginDate.setText(TimeUtils.getAWeekAgo("yyyy-MM-dd"));
		btn_endDate.setText(TimeUtils.getNow("yyyy-MM-dd"));
		
		listView = (LoadMoreListView) findViewById(R.id.listView);
		listView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
//				pageNum+=pageSize;
				pageNum+=1;
				doCommandGetBillSaleByDate();
			}
		});
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> pearnt, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,TradeLocalDetailActivity.class);
				intent.putExtra("TradeInfo", beans.get(position).getSaleNo());
				startActivity(intent);
			}
		});

		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				curBean=beans.get(position);
				showDialog();
				return true;
			}
		});
	}

	private void showDialog(){
		List<String> menus=new ArrayList<String>();
		menus.add("复制订单号");
		SimpleListDialog<String> d=new SimpleListDialog<String>(context, menus);
		d.setOnItemClickListener(new SimpleListDialog.OnItemClickListener<String>() {

			@Override
			public void onItemClick(View v, String item, int position) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					copyText(curBean.getSaleNo());
					break;

				default:
					break;
				}
			}
		});
		d.show();
	}
	
	private void copyText(String text){
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
//            clipboard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
//				
//				@Override
//				public void onPrimaryClipChanged() {
//					// TODO Auto-generated method stub
//					
//				}
//			});
            ClipData clip = ClipData.newPlainText("Simple text", text);
            clipboard.setPrimaryClip(clip);
    }
	
	private void notifyDataSetChanged() {
		if (adapter == null) {
			listView.setAdapter(adapter = new CommonAdapter<JvbillsaleInfo>(
					context, beans, R.layout.item_trade_local_list) {

				@Override
				public void setValues(ViewHolder helper,
						final JvbillsaleInfo item, final int position) {
					// TODO Auto-generated method stub
					helper.setText(R.id.item_0, "订单号："+item.getSaleNo());
					helper.setText(R.id.item_5, item.getCreatetimeStr());
					helper.setText(R.id.item_1, item.getTotalQty()+"件");
					helper.setText(R.id.item_2, "￥"+formatMoney(item.getTotalMoney()));
					
					if(item.getVipId()==null||item.getVipId().length()==0){
						helper.setText(R.id.item_3,"");
					}else{
						helper.setText(R.id.item_3,"会员消费");
					}
					TextView item_4=helper.getView(R.id.item_4);
					if(item.getTotalQty()>0){
						item_4.setTextColor(getColorCompat(R.color.fittting_green));
						item_4.setText("已支付");
					}else{
						item_4.setText("已退单");
						item_4.setTextColor(getColorCompat(R.color.fittting_red));
					}
				}

			});
		} else {
			adapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void updateViews(Object obj) {
		listView.onLoadMoreComplete();
		if(obj==null){
			return;
		}
		List<JvbillsaleInfo> list = (List<JvbillsaleInfo>)obj;
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
		pageNum = 1;
		beans.clear();
		notifyDataSetChanged();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_state:
//			showSimplePopupWindow(v);
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
				doCommandGetBillSaleByDate();
			}
		 });
       dialog.show();
	}
	
	@Subscriber
	void updateByEventBus(String event) {
		if (event.equals(App.EVENT_REFRESH)) {
			refreshListView();
			doCommandGetBillSaleByDate();
		}
	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
//		updateTheme();
	}
	
}
