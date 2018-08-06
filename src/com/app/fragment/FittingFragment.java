package com.app.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import com.android.volley.Response.Listener;
import com.app.model.Customer;
import com.app.model.SimpleGoods;
import com.app.net.Commands;
import com.app.xstore.App;
import com.app.xstore.GoodsDetailActivity;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;
import com.base.util.D;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;


/**
 * 试衣
 * @author pythoner
 *
 */
public class FittingFragment extends BaseFragment implements OnClickListener{

	private RadioGroup rg_sex;
	private RadioGroup rg_age;
	private EditText et_prodID;
	private CheckBox cb_foreigner;
	private View btn_ok;
	private ListView listView;
	private CommonAdapter<SimpleGoods> adapter;
	private List<SimpleGoods> beans=new ArrayList<SimpleGoods>();
	private int curPosition=-1;
	private Customer customer=new Customer();
	public FittingFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.fragment_fitting, container,false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		initViews(view);
		updateTheme(view);
	}
	
	@Override
	public void initViews(View view){
		rg_sex=$(view,R.id.rg_sex);
		rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				RadioButton rb=(RadioButton)group.findViewById(checkedId);
				customer.setSex((String)rb.getTag());
			}
		});
		rg_age=$(view,R.id.rg_age);
		rg_age.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				RadioButton rb=(RadioButton)group.findViewById(checkedId);
				customer.setAge((String)rb.getTag());
			}
		});
		
		et_prodID=$(view,R.id.et_prodID);
		btn_ok=$(view,R.id.btn_ok);
		btn_ok.setOnClickListener(this);
		cb_foreigner=$(view,R.id.cb_foreigner);
		
		listView=$(view,R.id.listView);
		listView.setOnItemClickListener(new ListView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent =new Intent(context, GoodsDetailActivity.class);
				intent.putExtra("ProdNum", beans.get(position).getProd_num());
				startActivity(intent);
			}
			
		});
		
		listView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				final int curPosition=position;
				D.showDialog(context, "确定要删除吗？", "确定","取消", new D.OnPositiveListener(){

					@Override
					public void onPositive() {
						// TODO Auto-generated method stub
						beans.remove(curPosition);
						notifyDataSetChanged();
					}
					
				});
				return true;
			}
		});
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			doAdd();
			break;
		}
	}

	private void doAdd(){
		String prodID=et_prodID.getText().toString();
//		if(prodID.length()>11){
//			showToast("商品编码为11位");
//			return;
//		}else if(prodID.length()<11){
//			doCommandScanBarCodeIntoBillEntityFor11or13(prodID);
//		}else if(prodID.length()==11){
		if(prodID.length()==11||prodID.length()==13){
			addItem(et_prodID.getText().toString());
			et_prodID.setText("");
		}else{
			showToast("商品编码为11或13位");
		}
	}
	
	private boolean summary=false;//汇总归类
	public void addItem(String text){
		if(TextUtils.isEmpty(text)){
			return;
		}
		if(summary){
			boolean isExist=false;
			SimpleGoods tmp=null;
			for(int i=0;i<beans.size();i++){
				if(beans.get(i).getProd_num().equals(text)){
					isExist=true;
					tmp=beans.get(i);
					curPosition=i;
					break;
				}
			}
			if(isExist){
				tmp.setQty(tmp.getQty()+1);
			}else{
				tmp=new SimpleGoods();
				tmp.setProd_num(text);
				tmp.setQty(1);
				beans.add(tmp);
				curPosition=beans.size()-1;
			}
		}else{
			SimpleGoods tmp=null;
			tmp=new SimpleGoods();
			tmp.setProd_num(text);
			tmp.setQty(1);
			beans.add(tmp);
			curPosition=beans.size()-1;
		}
		notifyDataSetChanged();
	}
	
	@Override
	public void updateViews(Object obj) {
		notifyDataSetChanged();
	}
	
	private void notifyDataSetChanged(){
		if(adapter==null){
			listView.setAdapter(adapter = new CommonAdapter<SimpleGoods>( context, beans,
					  R.layout.item_for_fitting){
					  
					@Override
					public void setValues(ViewHolder helper, final SimpleGoods item, final int position) {
						// TODO Auto-generated method stub
						helper.setText(R.id.item_0, item.getProd_num());
						helper.setText(R.id.item_1, String.valueOf(item.getQty()));
						if(curPosition==position){
							helper.getConvertView().setBackgroundResource(R.color.yellow);
						}else{
							helper.getConvertView().setBackgroundColor(Color.TRANSPARENT);
						}
					}
			});
		}else{
			adapter.notifyDataSetChanged();
		}
	}
	
	private void doCommandUploadDressData(){
		if(beans.size()==0){
			showToast("没有数据可以保存");
			return;
		}
		if(rg_sex.getCheckedRadioButtonId()==-1){
			showToast("请选择性别");
			context.doShake(context, rg_sex);
			return;
		}
		if(rg_age.getCheckedRadioButtonId()==-1){
			showToast("请选择年龄段");
			context.doShake(context, rg_age);
			return;
		}
		String Shop_Id=App.user.getShopInfo().getShop_code();
		String Creator=App.user.getUserInfo().getUser_code();
		List<SimpleGoods> DressDtlData=beans;
		
		customer.setForeigner(cb_foreigner.isChecked());//外宾
		List<Customer> DressCustData=new ArrayList<Customer>();
		DressCustData.add(customer);
		
		Commands.doCommandUploadDressData(context, Shop_Id, Creator, DressDtlData, DressCustData, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				Log.i("tag", response.toString());
//				{"ErrMessage":"部分商品编码不存在","Result":false,"ErrSysTrackMessage":"","ErrSysMessage":"","Message":"部分商品编码不存在"}
				if(context.isSuccess(response)){
					showToast("保存成功");
					curPosition=-1;
					beans.clear();
					notifyDataSetChanged();
					EventBus.getDefault().post(App.EVENT_UPDATE_FITTING);
				}
				
			}
		});
	}
	@Subscriber
	void updateByEventBus(String event) {
		if(event.equals(App.EVENT_SAVE_FITTING)){
			doCommandUploadDressData();
		}
	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		updateTheme(getView());
	}
	
	private void updateTheme(View view) {
		if(context!=null&&view!=null){
			context.setThemeDrawable(context,R.id.btn_ok);
		}
	}
	
}
