package com.app.widget;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.model.LocQty;
import com.app.model.ProdPreChcekData;
import com.app.model.response.UploadProdPreCheckDataResponse;
import com.app.net.Commands;
import com.app.xstore.App;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;

/**
 * 
 * @author pythoner
 * 
 */
public class ShelfListDialog extends ScanerBaseDialog implements View.OnClickListener{
	private EditText et_locID;
	private ListView listView;
	private List<ProdPreChcekData> beans;
	private CommonAdapter<ProdPreChcekData> adapter;
	private List<ProdPreChcekData> datas=new ArrayList<ProdPreChcekData>();

	public ShelfListDialog(Context context, List<ProdPreChcekData> beans) {
		this(context, R.style.Theme_Dialog_NoTitle, beans);
		// TODO Auto-generated constructor stub
	}

	public ShelfListDialog(Context context, int theme, List<ProdPreChcekData> beans) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.beans = beans;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_shelf_list);
		initViews();
	}
	
	private void initViews() {
		if (beans == null) {
			beans=new ArrayList<ProdPreChcekData>();
		}

		datas.addAll(beans);
		
		et_locID = (EditText) findViewById(R.id.et_locID);
		et_locID.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				datas.clear();
				for(ProdPreChcekData b:beans){
					if(b.getShelf_code().startsWith(s.toString())){
						datas.add(b);
					}
				}
				notifyDataSetChanged();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		findViewById(R.id.iv_scan).setOnClickListener(this);
		findViewById(R.id.btn_save).setOnClickListener(this);

		listView = (ListView) findViewById(R.id.listView);
		notifyDataSetChanged();
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
				// TODO Auto-generated method stub
				ProdPreChcekData instance = beans.get(position);
				if (onItemClickListener != null) {
					onItemClickListener.onItemClick(v, instance, position);
				}
//				dismiss();
			}
		});
	}

	private void notifyDataSetChanged(){
		if(adapter==null){
			listView.setAdapter(adapter = new CommonAdapter<ProdPreChcekData>(context, datas, R.layout.item_simple_list) {
	
				@Override
				public void setValues(ViewHolder helper, ProdPreChcekData item, int position) {
					// TODO Auto-generated method stub
					TextView item_0=helper.getView(R.id.item_0);
					item_0.setText(item.getShelf_code());
				}
	
			});
		}else{
			adapter.notifyDataSetChanged();
		}
	}
	private OnItemClickListener onItemClickListener;

	public interface OnItemClickListener {
		public void onItemClick(View v, ProdPreChcekData item, int position);
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_scan:
			doScan(context.resultHandler);
			break;
		case R.id.btn_save:
			final String locID = et_locID.getText().toString().trim();
			if (TextUtils.isEmpty(locID)) {
				doShake(et_locID);
				return;
			}

			LocQty locQty=new LocQty();
			locQty.setShelf_code(locID);
			locQty.setTotal_qty(0);
			
			List<LocQty> list=new  ArrayList<LocQty>();
			list.add(locQty);
			
			doCommandUploadProdPreCheckData(list);
			break;

		default:
			break;
		}
	}
	
	private void doCommandUploadProdPreCheckData(final List<LocQty> list){
		String shop_code=App.user.getShopInfo().getShop_code();
		String create_user=App.user.getUserInfo().getUser_code();
		Commands.doCommandUploadProdPreCheckData(context, shop_code,create_user,list, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				if(context.isSuccess(response)){
					UploadProdPreCheckDataResponse obj=context.mapperToObject(response, UploadProdPreCheckDataResponse.class);
					if(obj!=null){
						List<ProdPreChcekData> beans=obj.getPrecheck_Info();
						if(beans!=null){
							for(ProdPreChcekData b:beans){
								b.setProdPreChcekData_id(b.getId());
							}
						}
						if(onResultListener!=null){
							onResultListener.onResult(beans);
						}
					}
				}
			}
		});
	}
	
	private OnResultListener onResultListener;
	
	public void setOnResultListener(OnResultListener onResultListener) {
		this.onResultListener = onResultListener;
	}

	public interface OnResultListener{
		public void onResult(List<ProdPreChcekData> list);
	}
	
	@Override
	public void onScanProductHandleMessage(String prodID){
		et_locID.setText(prodID);
	}
}
