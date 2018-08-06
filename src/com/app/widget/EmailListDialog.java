package com.app.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Response.Listener;
import com.app.model.EmailInfo;
import com.app.model.response.GetEmailInfoResponse;
import com.app.net.Commands;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;
import com.base.util.D;


/**
 * 设置邮件列表
 * @author pythoner
 * 
 */
public class EmailListDialog extends BaseDialog {

	private EditText et;
	private ListView listView;
	private List<EmailInfo> beans= new ArrayList<EmailInfo>();
	private CommonAdapter<EmailInfo> adapter;
	private EmailInfo curBean;

	public EmailListDialog(Context context) {
		this(context, R.style.Theme_Dialog_NoTitle);
		// TODO Auto-generated constructor stub
	}

	public EmailListDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_email_list);
		initViews();
		doCommandGetEmailInfo();
	}

	private void doCommandGetEmailInfo(){
		String shop_id=App.user.getShopInfo().getShop_code();
		Commands.doCommandGetEmailInfo(context, shop_id,new Listener<JSONObject>() {
	
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				if(context.isSuccess(response)){
					GetEmailInfoResponse obj=((BaseActivity)context).mapperToObject(response, GetEmailInfoResponse.class);
					if(obj!=null){
						List<EmailInfo> list=obj.getHeadInfo();
						beans.addAll(list);
						updateViews(beans);
					}
				}
			}
		});
	}
	
	private void doCommandSetEmailInfo(List<EmailInfo> detailList){
		String shop_id=App.user.getShopInfo().getShop_code();
		String user=App.user.getUserInfo().getUser_code();
		Commands.doCommandSetEmailInfo(context, shop_id,user,detailList, new Listener<JSONObject>() {
	
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				if(context.isSuccess(response)){
					if(curBean.getType().equals("Delete")){
						beans.remove(curBean);
						notifyDataSetChanged();
					}else if(curBean.getType().equals("Insert")){
//						beans.add(curBean);
						et.setText("");
						beans.clear();
						doCommandGetEmailInfo();
					}else if(curBean.getType().equals("Update")){
						et.setText("");
						notifyDataSetChanged();
					}
//					dismiss();
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
					showToast("请输入邮箱");
					return;
				}
				boolean isExist=false;
				for(EmailInfo bean:beans){
					if(bean.getEmail().equals(text)){
						isExist=true;
						curBean=bean;
						doShake(et);
						showToast("已存在该邮箱");
						break;
					}
				}
				if(isExist){
					return;
				}
				int maxSize=10;
				if(beans.size()>=maxSize){
					doShake(et);
					showToast("最多可创建"+maxSize+"个邮箱");
					return;
				}
				//电子邮件    
				String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";    
				Pattern regex = Pattern.compile(check);
				Matcher matcher = regex.matcher(text);
				if(!matcher.matches()){
					doShake(et);
					showToast("邮箱格式不正确");
					return;
				}
				
				List<EmailInfo> detailList=new ArrayList<EmailInfo>();
				curBean=new EmailInfo();
				curBean.setEmail(text);
				curBean.setShop_id(App.user.getShopInfo().getShop_code());
				curBean.setUser_name(App.user.getUserInfo().getUser_code());
				curBean.setType("Insert");
				detailList.add(curBean);
				doCommandSetEmailInfo(detailList);
			}
		});
		listView = (ListView) findViewById(R.id.listView);
//		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				// TODO Auto-generated method stub
				D.showDialog(context, "确定要删除吗？", "确定", "取消", new D.OnPositiveListener() {
					
					@Override
					public void onPositive() {
						// TODO Auto-generated method stub
						curBean=beans.get(position);
						curBean.setType("Delete");
						List<EmailInfo> list=new ArrayList<EmailInfo>();
						list.add(curBean);
						doCommandSetEmailInfo(list);
					}
				});
				return true;
			}
		});
	}

	private void updateViews(Object obj){
		if(obj==null){
			return;
		}
		if(beans.size()==0){
			
		}
		notifyDataSetChanged();
	}
	
	private void notifyDataSetChanged(){
		if(adapter==null){
			listView.setAdapter(adapter = new CommonAdapter<EmailInfo>(context, beans,
					R.layout.item_simple_list) {

				@Override
				public void setValues(ViewHolder helper, EmailInfo item, final int position) {
					// TODO Auto-generated method stub
					helper.setText(R.id.item_0, item.getEmail());
				}

			});
		}else{
			adapter.notifyDataSetChanged();
		}
	}
	
}
