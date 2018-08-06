package com.app.widget;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Response.Listener;
import com.app.model.LocAreaInfo;
import com.app.model.response.GetLocInfoListResponse;
import com.app.net.Commands;
import com.app.xstore.App;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;
import com.base.util.D;


/**
 * 
 * @author pythoner
 * 
 */
public class LocAreaInfoListDialog extends BaseDialog {

	private EditText et;
	private ListView listView;
	private List<LocAreaInfo> beans= new ArrayList<LocAreaInfo>();
	private CommonAdapter<LocAreaInfo> adapter;
	private LocAreaInfo curBean;
	private String curLocID;
	
	public LocAreaInfoListDialog(Context context,String curLocID) {
		this(context, R.style.Theme_Dialog_NoTitle,curLocID);
		// TODO Auto-generated constructor stub
	}

	public LocAreaInfoListDialog(Context context, int theme,String curLocID) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.curLocID = curLocID;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_loc_area_info_list);
		initViews();
		doCommandGetLocInfoList();
	}
	private void doCommandGetLocInfoList(){
		String shop_code=App.user.getShopInfo().getShop_code();
		String loc_id=null;
		Commands.doCommandGetLocInfoList(context,shop_code,loc_id, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				if(context.isSuccess(response)){
					GetLocInfoListResponse obj=context.mapperToObject(response, GetLocInfoListResponse.class);
					if(obj!=null){
//						shopLocInfos=obj.getShopLocInfoList();
						List<LocAreaInfo> locAreaInfos=obj.getLocAreaInfoList();
						updateViews(locAreaInfos);
					}
				}
			}
		});
	}
	
	private void doCommandUploadLocAreaInfo(List<LocAreaInfo> list){
		Commands.doCommandUploadLocAreaInfo(context, list, new Listener<JSONObject>() {
	
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				if(context.isSuccess(response)){
					et.setText("");
					beans.clear();
					doCommandGetLocInfoList();
				}
			}
		});
	}
	
	private void initViews() {
		et=(EditText)findViewById(R.id.et);
		findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String text=et.getText().toString();
				if (TextUtils.isEmpty(text)) {
					doShake(et);
					showToast("请输入分区");
					return;
				}
				boolean isExist=false;
				for(LocAreaInfo bean:beans){
					if(bean.getArea().equals(text)){
						isExist=true;
						curBean=bean;
						doShake(et);
						showToast("已存在该分区");
						break;
					}
				}
				if(isExist){
					return;
				}
				
				List<LocAreaInfo> list=new ArrayList<LocAreaInfo>();
				
				curBean=new LocAreaInfo();
				curBean.setArea(text);
				curBean.setShop_code(App.user.getShopInfo().getShop_code());
				curBean.setCreator(App.user.getUserInfo().getUser_code());
//				curBean.setLocID(locID);//货位
				list.add(curBean);
				
				doCommandUploadLocAreaInfo(list);
				
			}
		});
		listView = (ListView) findViewById(R.id.listView);
		
		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				// TODO Auto-generated method stub
				D.showDialog(context, "确定要删除吗？", "确定", "取消", new D.OnPositiveListener() {
					
					@Override
					public void onPositive() {
						// TODO Auto-generated method stub
						curBean=beans.get(position);
						doCommandDeleteLocAreaInfo(curBean.getId());
					}
				});
				return true;
			}
		});
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
				// TODO Auto-generated method stub
				curBean = beans.get(position);
				if(onItemClickListener!=null){
					onItemClickListener.onItemClick(v, curBean);
				}
				dismiss();
			}
		});
	}

	private void doCommandDeleteLocAreaInfo(int ID){
		Commands.doCommandDeleteLocAreaInfo(context, ID, new Listener<JSONObject>() {
	
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				if(context.isSuccess(response)){
					beans.remove(curBean);
					notifyDataSetChanged();
				}
			}
		});
	}
	
//	private void showShopLocInfosDialog(List<ShopLocInfo> list){
//		SimpleListDialog<ShopLocInfo> d=new SimpleListDialog<ShopLocInfo>(context, list);
//		d.setOnItemClickListener(new SimpleListDialog.OnItemClickListener<ShopLocInfo>() {
//			
//			@Override
//			public void onItemClick(View v, ShopLocInfo item, int position) {
//				// TODO Auto-generated method stub
//				iv_locID.setText(item.getLocID());
//			}
//		});
//		d.show();
//	}
	
	private void updateViews(Object obj){
		if(obj==null){
			return;
		}
		List<LocAreaInfo> list=(ArrayList<LocAreaInfo>) obj;
		beans.addAll(list);
		if(beans.size()==0){
			context.setEmptyView(listView, "请添加分区");
		}
		
		notifyDataSetChanged();
	}
	
	private void notifyDataSetChanged(){
		if(adapter==null){
			listView.setAdapter(adapter = new CommonAdapter<LocAreaInfo>(context, beans,
					R.layout.item_simple_list) {

				@Override
				public void setValues(ViewHolder helper, LocAreaInfo item, final int position) {
					// TODO Auto-generated method stub
					helper.setText(R.id.item_0, item.getArea());
				}

			});
		}else{
			adapter.notifyDataSetChanged();
		}
	}
	
	private OnItemClickListener onItemClickListener;

	public interface OnItemClickListener {
		public void onItemClick(View v, LocAreaInfo item);
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

}
