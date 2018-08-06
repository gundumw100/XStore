package com.app.xstore.mendiancaigouruku;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.net.Commands;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.app.xstore.mendiandiaochu.ChuRuKuHead;
import com.app.xstore.mendiandiaochu.ChuRuKuProduct;
import com.widget.view.DatePickerDialog;

/**
 * 
 * 
 * @author pythoner
 * 
 */
public class CaiGouRuKuCreateSecondActivity extends BaseActivity implements OnClickListener {

	private ArrayList<ChuRuKuProduct> beans;
	private TextView tv_date;
	private EditText et_mark;
	private int qty=0;
	private float jinhuochengben=0f;
	private float xiaolianggusuan=0f;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mendian_caigou_ruku_second);
		initActionBar("数据上传" ,null, null);
		
		beans=getIntent().getParcelableArrayListExtra("ChuRuKuProducts");
		if(beans==null){
			return;
		}
		initViews();
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		Calendar calendar=Calendar.getInstance();
		String today=formatTime(calendar.getTimeInMillis(), "yyyy-MM-dd");
		tv_date = $(R.id.tv_date);
		tv_date.setText(today);
//		tv_date.setOnClickListener(this);
		
		TextView tv_user = $(R.id.tv_user);
		tv_user.setText("经手人："+App.user.getUserInfo().getUser_code());
		
		
		TextView tv_jinhuoshuliang = $(R.id.tv_jinhuoshuliang);
		TextView tv_jinhuochengben = $(R.id.tv_jinhuochengben);
		TextView tv_xiaolianggusuan = $(R.id.tv_xiaolianggusuan);
		TextView tv_maoligusuan = $(R.id.tv_maoligusuan);
		
		
		for(ChuRuKuProduct bean:beans){
			qty+=bean.getQty();
			jinhuochengben+=bean.getGoods_jh_price()*bean.getQty();
			xiaolianggusuan+=bean.getGoods_ls_price()*bean.getQty();
		}
		tv_jinhuoshuliang.setText("进货数量："+qty);
		tv_jinhuochengben.setText("进货成本："+jinhuochengben);
		tv_xiaolianggusuan.setText("销售估算："+xiaolianggusuan);
		tv_maoligusuan.setText("毛利估算："+(xiaolianggusuan-jinhuochengben));
		
		et_mark = $(R.id.et_mark);
		$(R.id.btn_ok).setOnClickListener(this);

	}

	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
//		case R.id.tv_date:
//			Calendar calendar=Calendar.getInstance();
//			String today=formatTime(calendar.getTimeInMillis(), "yyyy-MM-dd");
//			DatePickerDialog d=new DatePickerDialog(context,today);
//			d.setOnButtonClickListener(new com.widget.view.DatePickerDialog.OnButtonClickListener() {
//				
//				@Override
//				public void onOKClick(String year, String month, String date) {
//					// TODO Auto-generated method stub
//					tv_date.setText(year+"-"+month+"-"+date);
//				}
//				
//				@Override
//				public void onCancelClick() {
//					// TODO Auto-generated method stub
//					tv_date.setText("");
//				}
//			});
//			d.show();
//			break;
		case R.id.btn_ok:
			doCommandCreateInStorage();
			break;

		default:
			break;
		}
	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		setThemeDrawable(this, R.id.btn_ok);
	}
	
	private void doCommandCreateInStorage(){
		ChuRuKuHead headInfo=new ChuRuKuHead();
		headInfo.setDoc_type("CTI");//采购入库
		headInfo.setShopCode(App.user.getShopInfo().getShop_code());
		headInfo.setSrc_shop(App.user.getShopInfo().getShop_code());
		
		headInfo.setQty(qty);
		headInfo.setJhQty(qty);//进货数量
		headInfo.setJhcb(jinhuochengben);//进货成本
		headInfo.setXsgs(xiaolianggusuan);//销售估算
		headInfo.setMlgs(xiaolianggusuan-jinhuochengben);//毛利估算
		headInfo.setRemark(et_mark.getText().toString().trim());
		headInfo.setCreate_user(App.user.getUserInfo().getUser_code());
		headInfo.setLast_modify_user(App.user.getUserInfo().getUser_code());
		
		Commands.doCommandCreateInStorage(context, headInfo,beans, new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				{"ErrMessage":"","Result":true,"ErrSysTrackMessage":"","ErrSysMessage":"","Message":"保存成功","docCode":"00S111ISDC1807300005"}
				Log.i("tag", response.toString());
				if(isSuccess(response)){
					showToast("操作成功");
					EventBus.getDefault().post(App.EVENT_FINISH);
					EventBus.getDefault().post(App.EVENT_REFRESH);
					finish();
				}
			}
		});
	}
	
}
