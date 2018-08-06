package com.app.xstore.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;

import com.android.volley.Response.Listener;
import com.app.model.ProdCheckDtl;
import com.app.net.Commands;
import com.app.widget.EmailSenderDialog;
import com.app.widget.SimplePairListPopupWindow;
import com.app.widget.SimpleTextDialog;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.GoodsDetailActivity;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;
import com.base.util.comm.SPUtils;

/**
 * 分类汇总
 * @author pythoner
 * 
 */
public class SummaryERPActivity extends BaseActivity{

	private Context context;
	private final int count = 3;//页数  
    private int curPosition=2;//当前页  
    private RadioButton[] rbs;  
    private ListView listView;
    private CommonAdapter<ProdCheckDtl> adapter;
    private List<ProdCheckDtl> allBeans;
    private List<ProdCheckDtl> beans=new ArrayList<ProdCheckDtl>();
    private HashMap<String,ProdCheckDtl> map=new HashMap<String,ProdCheckDtl>();
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_summary_erp);
		context = this;
		allBeans=getIntent().getParcelableArrayListExtra("ProdCheckDtls");
		if(allBeans==null){
			return;
		}
		initViews();
//		initActionBar("汇总", null, App.res.getDrawable(R.drawable.icon_order_more));
		initActionBar("汇总", "发送邮件", null);
	}

	@Override
	public void doRightButtonClick(View v) {
		// TODO Auto-generated method stub
//		showSimplePopupWindow(v);
		showSimpleDialog();
	}
	
	private void showSimplePopupWindow(View v){
		ArrayList<Pair<Integer, String>> beans = new ArrayList<Pair<Integer, String>>();
		beans.add(new Pair<Integer, String>(0, "上传到ERP"));
		beans.add(new Pair<Integer, String>(1, "发送邮件"));
		
		View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_simple, null);
		final SimplePairListPopupWindow<Integer, String> popupWindow = new SimplePairListPopupWindow<Integer, String>(context,view, (int)App.res.getDimension(R.dimen.popupwindow_width), beans);
		popupWindow.showAsDropDown(v, 0, 0);
		popupWindow.setOnItemClickListener(new SimplePairListPopupWindow.OnItemClickListener<Integer, String>() {

			@Override
			public void onItemClick(int position, Pair<Integer, String> pair) {
				// TODO Auto-generated method stub
//				SimpleTextDialog d=null;
				switch (pair.first) {
				case 0://上传到ERP
//					d = new SimpleTextDialog(context, "", "请输入ERP中盘点单号");
//					d.setOnClickListener(new SimpleTextDialog.OnClickListener() {
//
//						@Override
//						public void onClick(View v, String text) {
//							// TODO Auto-generated method stub
//							if (TextUtils.isEmpty(text)) {
//								showToast("请输入ERP中盘点单编码");
//								return;
//							}
//							List<String> CheckDataDocNumList=getIntent().getStringArrayListExtra("CheckDataDocNumList");
//							doCommandUploadCheckDataToErp(text,CheckDataDocNumList);
//						}
//					});
//					d.show();
					break;
				case 1://发送邮件
					showSimpleDialog();
					break;
				}
			}
		});
	}
	
	private void showSimpleDialog(){
		final List<String> docNumList=getIntent().getStringArrayListExtra("CheckDataDocNumList");
		EmailSenderDialog d=new EmailSenderDialog(context);
		d.setOnSenderListener(new EmailSenderDialog.OnSenderListener() {
			
			@Override
			public void onSender(List<String> emails) {
				// TODO Auto-generated method stub
				doCommandSendProdCheckData(emails,docNumList);
				
			}
		});
		d.show();
		
		/*String text=(String)SPUtils.get(context, "EmailAddress", "");
		
		SimpleTextDialog d = new SimpleTextDialog(context, text, "请输入邮箱");
		d.setOnClickListener(new SimpleTextDialog.OnClickListener() {

			@Override
			public void onClick(View v, String text) {
				// TODO Auto-generated method stub
				if (!((BaseActivity)context).isEmail(text)) {
					showToast("请输入邮箱");
					return;
				}
				SPUtils.put(context, "EmailAddress", text);
				//
				List<String> receiverList=new ArrayList<String>();
				receiverList.add(text);
				doCommandSendProdCheckData(receiverList,docNumList);
			}
		});
		d.show();*/
	}
	
	private void doCommandSendProdCheckData(List<String> receiverList,List<String> docNumList){
		String shop_code=App.user.getShopInfo().getShop_code();
		Commands.doCommandUploadProdCheckData(context, shop_code, receiverList, docNumList, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				if(isSuccess(response)){
					showToast("发送成功");
				}
			}
		});
	}
//	private void doCommandUploadCheckDataToErp(String ErpDocNum,List<String> CheckDataDocNumList){
//		Commands.doCommandUploadCheckDataToErp(context,ErpDocNum,CheckDataDocNumList, new Listener<JSONObject>() {
//
//			@Override
//			public void onResponse(JSONObject response) {
//				// TODO Auto-generated method stub
////				Log.i("tag", response.toString());
//				if(isSuccess(response)){
//					showToast("上传成功");
//					EventBus.getDefault().post(App.EVENT_UPDATE_DOCUMENT_LIST_FROM_SERVER);
//				}
//			}
//		});
//	}
	
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		listView=$(R.id.listView);
		listView.setOnItemClickListener(new ListView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				ProdCheckDtl bean=beans.get(position);
				if(!isEmpty(bean.getProd_num())){
					Intent intent =new Intent(context, GoodsDetailActivity.class);
					intent.putExtra("ProdNum", bean.getProd_num());
					startActivity(intent);
				}
			}
			
		});
		$(R.id.btn_pancha).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent=new Intent(context,PanChaActivity.class);
//				startActivity(intent);
			}
		});
		
		rbs = new RadioButton[count];  
        rbs[0] = $(R.id.rb_0);  
        rbs[1] = $(R.id.rb_1);  
        rbs[2] = $(R.id.rb_2);  
        for (int i = 0; i < rbs.length; i++) {  
            rbs[i].setOnClickListener(new View.OnClickListener() {  
  
                @Override  
                public void onClick(View v) {  
                    // TODO Auto-generated method stub  
                    String tag = (String) v.getTag();  
                    curPosition=Integer.parseInt(tag);  
                    filter(curPosition);
                    notifyDataSetChanged();
                }  
            });  
        }  
        rbs[curPosition].performClick();
	}
	
	private void filter(int position){
		map.clear();
		beans.clear();
//		List<ProdCheckDtl> all=new ArrayList<ProdCheckDtl>(allBeans);//这样不行,why？
		//注意，需要全新copy，开辟新内存
		List<ProdCheckDtl> all=new ArrayList<ProdCheckDtl>();
		for(ProdCheckDtl bean:allBeans){
			ProdCheckDtl dtl=new ProdCheckDtl();
			dtl.setDocID(bean.getDocID());
			dtl.setProd_name(bean.getProd_name());
			dtl.setQty(bean.getQty());
			dtl.setProd_num(bean.getProd_num());
//			dtl.setBarcode(bean.getBarcode());//debug
			dtl.setId(bean.getId());
			dtl.setDocNum(bean.getDocNum());
			all.add(dtl);
		}
		
		for(ProdCheckDtl bean:all){
        	String prodNumPre=getPreByPosition(position,bean.getProd_num());
        	if(map.containsKey(prodNumPre)){
        		ProdCheckDtl dtl=map.get(prodNumPre);
        		dtl.setQty(dtl.getQty()+bean.getQty());
        	}else{
        		map.put(prodNumPre, bean);
        	}
        }
        
		Iterator it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next().toString();
			beans.add(map.get(key));
		}
        
	}
	
	private String getPreByPosition(int position,String prodNum){
		String prodNumPre=null;
    	if(position==0){
    		prodNumPre=prodNum.substring(0, 6);
    	}else if(position==1){
    		prodNumPre=prodNum.substring(0, 8);
    	}else if(position==2){
//    		prodNumPre=prodNum.substring(0, 11);
    		prodNumPre=prodNum;
    	}
    	return prodNumPre;
	}
	
	private void notifyDataSetChanged(){
		if(adapter==null){
			listView.setAdapter(adapter = new CommonAdapter<ProdCheckDtl>( context, beans,
					  R.layout.item_summary_erp){
					  
					@Override
					public void setValues(ViewHolder helper, ProdCheckDtl item, int position) {
						// TODO Auto-generated method stub
						helper.setText(R.id.item_0, getPreByPosition(curPosition,item.getProd_num()));
						helper.setText(R.id.item_1, item.getProd_name());//
						helper.setText(R.id.item_2, String.valueOf(item.getQty()));
					}
			});
		}else{
			adapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		updateTheme();
	}
	
	private void updateTheme() {
		for (int i = 0; i < rbs.length; i++) {  
			setThemeDrawableChecked(this, rbs[i]);
		}
		
		setThemeDrawable(context, R.id.btn_pancha);
	}
	
}
