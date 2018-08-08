package com.app.xstore.mendiandiaoru;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.net.Commands;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.app.xstore.mendiancaigouruku.GetGoodsListBySKUsResponse;
import com.app.xstore.mendiandiaochu.ChuRuKuHead;
import com.app.xstore.mendiandiaochu.ChuRuKuProduct;
import com.app.xstore.mendiandiaochu.GetOutStorageDetailListResponse;
import com.app.xstore.mendiandiaochu.Item;
import com.app.xstore.shangpindangan.ProductDangAn;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;
import com.base.util.D;

/**
 * 
 * 
 * @author pythoner
 * 
 */
public class MenDianDiaoRuDetailActivity extends BaseActivity implements OnClickListener {

	private ListView listView;
	private CommonAdapter<ChuRuKuProduct> adapter;
	private ArrayList<ChuRuKuProduct> beans=new ArrayList<ChuRuKuProduct>();
	private ChuRuKuHead head;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mendian_diaoru_detail);
		head=getIntent().getParcelableExtra("ChuRuKuHead");
		initActionBar("单据详情",null, null);
		initViews();
		
		if(head!=null){
			doCommandGetOutStorageDetailList(head.getShopCode(),head.getDoc_code());
		}
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		TextView tv_docCode = $(R.id.tv_docCode);
		TextView tv_docStatus = $(R.id.tv_docStatus);
		TextView tv_srcShop = $(R.id.tv_srcShop);
		TextView tv_qty = $(R.id.tv_qty);
		TextView tv_docDate = $(R.id.tv_docDate);
		Item statusItem=getStatusItem(head.getDoc_status());
		tv_docStatus.setTextColor(statusItem.numberValue);
		tv_docStatus.setText(statusItem.describe);
		tv_docCode.setText("出库单号："+head.getDoc_code());
		tv_srcShop.setText("发货门店/仓库："+head.getShopName()+"("+head.getShopCode()+")");
		tv_qty.setText("数量："+head.getQty());
		tv_docDate.setText("单据日期："+getCorrectDate(head.getDoc_date()));

		listView = $(R.id.listView);
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
			}

		});
		$(R.id.btn_ok).setOnClickListener(this);

	}

	private Item getStatusItem(String statusCode){
		for(Item item:MenDianDiaoRuActivity.statusList){
			if(item.startValue.equals(statusCode)){
				return item;
			}
		}
		return new Item("未知","",getColorCompat(R.color.grayMiddle));
	}
	
	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
		if(beans.size()==0){
			if(listView.getEmptyView()==null){
				setEmptyView(listView, "暂无数据");
			}
		}
		notifyDataSetChanged();
	}

	private void notifyDataSetChanged() {
		if (adapter == null) {
			listView.setAdapter(adapter = new CommonAdapter<ChuRuKuProduct>(context, beans,R.layout.item_check_product_for_mendiandiaochu) {

				@Override
				public void setValues(ViewHolder helper,
						final ChuRuKuProduct item, final int position) {
					// TODO Auto-generated method stub
					helper.setText(R.id.item_0, item.getGoods_sn());
					helper.setText(R.id.item_1, item.getGoods_name());
					helper.setText(R.id.item_2, String.valueOf(item.getQty()));
				}
			});
		} else {
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		setThemeDrawable(this, R.id.btn_ok);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_ok:
			String message="确定要收货吗？";
			D.showDialog(this, message, "确定", "取消", new D.OnPositiveListener() {
				
				@Override
				public void onPositive() {
					// TODO Auto-generated method stub
					doCommandCreateInStorage();
				}
			});
			break;

		default:
			break;
		}
	}

	private void doCommandGetOutStorageDetailList(String srcShopCode,String docCode){
		Commands.doCommandGetOutStorageDetailList(context, srcShopCode,docCode, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				Log.i("tag", response.toString());
				if(isSuccess(response)){
					GetOutStorageDetailListResponse obj=mapperToObject(response, GetOutStorageDetailListResponse.class);
					if(obj!=null&&obj.getInfo()!=null){
						beans.clear();
						beans.addAll(obj.getInfo());
//						updateViews(beans);
						List<String> goodsSns=new ArrayList<String>();
						for(ChuRuKuProduct item:beans){
							goodsSns.add(item.getGoods_sn());
						}
						doCommandGetGoodsListBySKUs(goodsSns);
					}
				}
			}
		});
	}
	
	private void doCommandGetGoodsListBySKUs(final List<String> goodsSns){
		Commands.doCommandGetGoodsListBySKUs(context, goodsSns, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				Log.i("tag", response.toString());
				if(isSuccess(response)){
					GetGoodsListBySKUsResponse obj=mapperToObject(response, GetGoodsListBySKUsResponse.class);
					if(obj!=null&&obj.getGoodsInfo()!=null){
						up:for(ChuRuKuProduct item:beans){
							for(ProductDangAn product:obj.getGoodsInfo()){
								if(item.getGoods_sn().equals(product.getGoods_sn())){
									item.setGoods_name(product.getGoods_name());
									item.setGoods_jh_price(product.getGoods_jh_price());
									item.setGoods_ls_price(product.getGoods_ls_price());
									item.setGoods_img(product.getGoods_img());
									continue up;
								}
							}
						}
						updateViews(beans);
					}
				}
			}
		});
	}
	
	private void doCommandCreateInStorage(){
		ChuRuKuHead headInfo=new ChuRuKuHead();
		headInfo.setDoc_type("OTS");
		headInfo.setShopCode(App.user.getShopInfo().getShop_code());
		headInfo.setSrc_shop(head.getShopCode());//发货门店
		headInfo.setSrc_code(head.getDoc_code());//出库单号
		
		headInfo.setQty(head.getQty());
		headInfo.setRemark("");
		headInfo.setCreate_user(App.user.getUserInfo().getUser_code());
		headInfo.setLast_modify_user(App.user.getUserInfo().getUser_code());
		
		Commands.doCommandCreateInStorage(context, headInfo, beans, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				Log.i("tag", response.toString());
				if(isSuccess(response)){
					showToast("收货成功");
					setResult(1);
					finish();
				}
			}
		});
	}
	
	
}
