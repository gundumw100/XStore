package com.app.widget;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response.Listener;
import com.app.model.EmailInfo;
import com.app.model.response.GetEmailInfoResponse;
import com.app.net.Commands;
import com.app.xstore.App;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;


/**
 * 邮件发送
 * @author pythoner
 * 
 */
public class EmailSenderDialog extends BaseDialog {

	private ListView listView;
	private List<EmailInfo> beans= new ArrayList<EmailInfo>();
	private CommonAdapter<EmailInfo> adapter;
	private List<EmailInfo> checkedBeans = new ArrayList<EmailInfo>();
	
	public EmailSenderDialog(Context context) {
		this(context, R.style.Theme_Dialog_NoTitle);
		// TODO Auto-generated constructor stub
	}

	public EmailSenderDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_email_list_sender);
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
					GetEmailInfoResponse obj=context.mapperToObject(response, GetEmailInfoResponse.class);
					if(obj!=null){
						List<EmailInfo> list=obj.getHeadInfo();
						beans.addAll(list);
						updateViews(beans);
					}
				}
			}
		});
	}
	
	private void initViews() {
		findViewById(R.id.btn_left).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showEmailListDialog();
				dismiss();
			}
		});
		findViewById(R.id.btn_right).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (checkedBeans.size() == 0) {
					showToast("请选择邮箱");
					return;
				}
				List<String> mails=new ArrayList<String>();
				for(EmailInfo bean:checkedBeans){
					mails.add(bean.getEmail());
				}
				if(onSenderListener!=null){
					onSenderListener.onSender(mails);
				}
				dismiss();
			}
		});
		listView = (ListView) findViewById(R.id.listView);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
				// TODO Auto-generated method stub
				EmailInfo bean = beans.get(position);
				if (listView.isItemChecked(position)) {
					checkedBeans.add(bean);
				} else {
					if (checkedBeans.contains(bean)) {
						checkedBeans.remove(bean);
					}
				}
			}
		});
	}

	private void updateViews(Object obj){
		if(obj==null){
			return;
		}
		if(beans.size()==0){
			showToast("请先设置邮箱");
		}
		notifyDataSetChanged();
	}
	
	private void notifyDataSetChanged(){
		if(adapter==null){
			listView.setAdapter(adapter = new CommonAdapter<EmailInfo>(context, beans,
					R.layout.item_email_list_sender) {

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
	
	private void showEmailListDialog(){
		EmailListDialog d = new EmailListDialog(context);
		d.show();
	}
	
	private OnSenderListener onSenderListener;
	
	public void setOnSenderListener(OnSenderListener onSenderListener) {
		this.onSenderListener = onSenderListener;
	}

	public interface OnSenderListener{
		public void onSender(List<String> emails);
	}
}
