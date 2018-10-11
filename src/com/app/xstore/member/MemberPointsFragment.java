package com.app.xstore.member;

import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.fragment.BaseFragment;
import com.app.model.Member;
import com.app.model.response.GetVipInfoResponse;
import com.app.net.Commands;
import com.app.xstore.App;
import com.app.xstore.R;

/**
 * 会员积分
 * 
 * @author pythoner
 * 
 */
public class MemberPointsFragment extends BaseFragment implements
		OnClickListener {

	private EditText et_cardNo;
	private TextView tv_info;
//	private TextView tv_last,tv_next;
//	private EditText et_nowGet, et_nowSpent;
//	
//	private EditText et_prodID;
//	private ListView listView;
//	private CommonAdapter<ProdCheckDtl> adapter;
//	private ArrayList<ProdCheckDtl> beans = new ArrayList<ProdCheckDtl>();
//	private int curPosition=-1;
//	private int whichScan=0;//按了哪个扫描
	
	private static MemberPointsFragment instance;

	public static MemberPointsFragment getInstance() {
		if (instance == null) {
			synchronized (MemberPointsFragment.class) {
				if (instance == null) {
					instance = new MemberPointsFragment();
				}
			}
		}
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.fragment_member_points, container,false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		initViews(view);
		updateTheme(view);
	}

	@Override
	public void initViews(View view) {
		et_cardNo = $(view,R.id.et_cardNo);
		tv_info = $(view,R.id.tv_info);
		
		$(view,R.id.btn_scan2).setOnClickListener(this);
		$(view,R.id.btn_search2).setOnClickListener(this);
		
//		$(view,R.id.btn_ok).setOnClickListener(this);
//		tv_last = $(view,R.id.tv_last);
//		tv_next = $(view,R.id.tv_next);
//		et_nowGet = $(view,R.id.et_nowGet);
//		et_nowSpent = $(view,R.id.et_nowSpent);
//		et_nowGet.addTextChangedListener(new TextWatcher() {
//			
//			@Override
//			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//				// TODO Auto-generated method stub
//				if(arg0.length()>0&&et_nowSpent.getText().length()>0&&tv_last.getText().length()>0){
//					int a=Integer.parseInt(tv_last.getText().toString());
//					int b=Integer.parseInt(arg0.toString());
//					int c=Integer.parseInt(et_nowSpent.getText().toString());
//					
//					tv_next.setText(String.valueOf(a+b-c));
//				}else{
//					tv_next.setText("");
//				}
//			}
//			
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
//					int arg3) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void afterTextChanged(Editable arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//		et_nowSpent.addTextChangedListener(new TextWatcher() {
//			
//			@Override
//			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//				// TODO Auto-generated method stub
//				if(arg0.length()>0&&et_nowGet.getText().length()>0&&tv_last.getText().length()>0){
//					int a=Integer.parseInt(tv_last.getText().toString());
//					int b=Integer.parseInt(et_nowGet.getText().toString());
//					int c=Integer.parseInt(arg0.toString());
//					
//					tv_next.setText(String.valueOf(a+b-c));
//				}else{
//					tv_next.setText("");
//				}
//			}
//			
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
//					int arg3) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void afterTextChanged(Editable arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//		
//		$(view,R.id.btn_scan_prodID).setOnClickListener(this);
//		et_prodID = $(view,R.id.et_prodID);
//		listView = $(view,R.id.listView);
//		listView.setOnItemClickListener(new ListView.OnItemClickListener(){
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				// TODO Auto-generated method stub
//				Intent intent =new Intent(context, GoodsDetailActivity.class);
//				intent.putExtra("ProdNum", beans.get(position).getProd_num());
//				startActivity(intent);
//			}
//			
//		});
	}

	private void doCommandGetVipInfo(String vipCode,String mobile,String name){
		Commands.doCommandGetVipInfo(context, vipCode, mobile,name, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				if(context.isSuccess(response)){
					GetVipInfoResponse obj=context.mapperToObject(response, GetVipInfoResponse.class);
					updateViews(obj);
				}
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.btn_scan_prodID:
//			whichScan=1;
//			context.doScan(context.resultHandler);
//			break;
		case R.id.btn_scan2:
//			whichScan=0;
			context.doScan(context.resultHandler);
			break;
		case R.id.btn_search2:
			String cardNo=et_cardNo.getText().toString().trim();
			if(context.isEmpty(cardNo)/*&&context.isEmpty(name)*/){
				showToast("请输入查询条件");
				return;
			}
			String mobile=null;
			String vipCode=null;
			if(cardNo.length()==11){
				mobile=cardNo;
			}else{
				vipCode=cardNo;
			}
			doCommandGetVipInfo(vipCode,mobile,null);
			break;
//		case R.id.btn_ok:
//			String prodID=et_prodID.getText().toString();
//			if(context.isEmpty(prodID)){
//				context.doShake(context, et_prodID);
//				return;
//			}
//			addToList(prodID);
//			break;
		default:
			break;
		}
	}

//	private void notifyDataSetChanged(){
//		if(adapter==null){
//			listView.setAdapter(adapter = new CommonAdapter<ProdCheckDtl>( context, beans,
//					  R.layout.item_create_document){
//					  
//					@Override
//					public void setValues(ViewHolder helper, final ProdCheckDtl item, final int position) {
//						// TODO Auto-generated method stub
//						helper.setText(R.id.item_0, item.getProd_num());
//						helper.setText(R.id.item_1, item.getProd_name());
////						helper.setText(R.id.item_2, String.valueOf(item.getQty()));
//						final TextView item_2=helper.getView(R.id.item_2);
//						item_2.setText(String.valueOf(item.getQty()));
//						
//						View container=helper.getView(R.id.container);
//						if(curPosition==position){
//							container.setBackgroundColor(Color.LTGRAY);
//						}else{
//							container.setBackgroundColor(Color.TRANSPARENT);
//						}
//						
//					}
//			});
//		}else{
//			adapter.notifyDataSetChanged();
//		}
//	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		updateTheme(getView());
	}

	private void updateTheme(View view) {
		if (context != null && view != null) {
			context.setThemeDrawable(context, R.id.btn_scan2);
			context.setThemeDrawable(context, R.id.btn_search2);
//			context.setThemeDrawable(context, R.id.btn_ok);
//			context.setThemeDrawable(context, R.id.btn_scan_prodID);
		}
	}

	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
		if(obj==null){
			return;
		}
		
		GetVipInfoResponse res=(GetVipInfoResponse)obj;
		List<Member> list=res.getHeadInfo();
		if(context.isEmptyList(list)){
			tv_info.setText("未找到会员信息");
		}else{
			Member bean=list.get(0);
			tv_info.setText(
					"会员ID：     "+bean.getVipNo()+"\n"+
					"卡号 ：         "+bean.getVipCode()+"\n"+
					"会员积分： "+bean.getTotalPoints()+"\n"+
//					"可抵扣金额："+bean.getTotalValue()+"\n"+
					"手机号：     "+bean.getMobile()+"\n"+
					"姓名：          "+bean.getName()+"\n"+
					"性别：          "+bean.getSex()+"\n"+
					"出生年月："+bean.getBirth()+"\n"+
					"地址：          "+bean.getAddress()+"\n"+
					"注册时间： "+bean.getCreatetimeStr()+"\n"
					);
			
//			tv_last.setText(bean.getTotalPoints());
		}
	}
	
	public void update(String data){
//		if(whichScan==0){
			et_cardNo.setText(data);
//		}else if(whichScan==1){
//			if(data.length()>20){
//				showToast("编码过长");
//				return;
//			}
//			addToList(data);
//		}
		
	}
	
//	private void addToList(String data){
//		int i = 0;
//		boolean isExist = false;
//		for (ProdCheckDtl bean : beans) {
//			if (bean.getProd_num().equals(data)) {// !isEmpty(bean.getProdNum())&&
//				bean.setQty(bean.getQty() + 1);
//				isExist = true;
//				curPosition = i;
//				break;
//			}
//			i++;
//		}
//		if(!isExist){
//			ProdCheckDtl o=new ProdCheckDtl();
//			o.setProd_num(data);
//			o.setQty(1);
//			beans.add(o);
//			curPosition=beans.size()-1;
//		}
//		notifyDataSetChanged();
//	}
//	
//	@Override
//	public void onDestroy() {
//		// TODO Auto-generated method stub
//		//如果不适用new MemberPointsFragment()初始化实例，请释放数据
//		beans.clear();
//		adapter=null;
//		super.onDestroy();
//	}
}
