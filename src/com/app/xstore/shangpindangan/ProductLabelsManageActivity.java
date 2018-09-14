package com.app.xstore.shangpindangan;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.net.Commands;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.base.util.D;
import com.widget.flowlayout.FlowLayout;
import com.widget.flowlayout.TagAdapter;
import com.widget.flowlayout.TagFlowLayout;

/**
 * 商品标签管理
 * @author pythoner
 * 
 */
@Deprecated
public class ProductLabelsManageActivity extends BaseActivity implements View.OnClickListener{

	private EditText et_desc;
	private TagFlowLayout flowLayout;
	private TagAdapter<ProdLabel> tagAdapter;
	private List<ProdLabel> beans=new ArrayList<ProdLabel>();
	private String goods_sn;
	private List<ProdLabel> checkedBeans;
	private TagFlowLayout flowLayout_unenable;
	private TextView emptyView;
	
	private boolean isChanged=false;//判断标签是否发生改变
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shangpindangan_product_labels_manage);
//		context = this;
//		initActionBar("商品标签", "保存", null);
//		goods_sn=getIntent().getStringExtra("goods_sn");
//		if(isEmpty(goods_sn)||goods_sn.length()<6){
//			showToast("编码有误");
//			return;
//		}
//		initViews();
//		doCommandGetProdLabelList();
//		doCommandGetProdLabelListUnenable();
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
//		et_desc=(EditText)findViewById(R.id.et_desc);
//		findViewById(R.id.btn_add).setOnClickListener(this);
//		
//		flowLayout=(TagFlowLayout)findViewById(R.id.flowLayout);
//		flowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
//			
//			@Override
//			public boolean onTagClick(FlowLayout parent, View view, int position) {
//				// TODO Auto-generated method stub
//				isChanged=true;//只要有点击就认为改变了标签
//				ProdLabel curBean=beans.get(position);
//				if(flowLayout.isChecked(position)){
//					doCommandAddProdLabelMapping(goods_sn,curBean.getLabelCode(),curBean.getDescription());
//				}else{
//					doCommandDeleteProdLabelMapping(goods_sn,curBean.getLabelCode());
//				}
//				return true;
//			}
//		});
//		
//		flowLayout.setmOnTagLongClickListener(new TagFlowLayout.OnTagLongClickListener() {
//			
//			@Override
//			public boolean onTagLongClick(FlowLayout parent, View view, final int position) {
//				// TODO Auto-generated method stub
//				final ProdLabel curBean=beans.get(position);
//				if(flowLayout.isChecked(position)){
//					String message="选中的标签不能移到禁用列表。";
//					D.showDialog(ProductLabelsManageActivity.this, message, "知道了");
//					return true;
//				}
//				String message="需要移到禁用列表吗？";
//				D.showDialog(ProductLabelsManageActivity.this, message, "确定", "取消", new D.OnPositiveListener() {
//					
//					@Override
//					public void onPositive() {
//						// TODO Auto-generated method stub
//						doCommandUpdateProdLabelEnabled(curBean.getLabelCode(),"0");
//						
//					}
//				});
//				return true;
//			}
//		});
//		
//		
//		emptyView=(TextView)findViewById(R.id.emptyView);
//		emptyView.setVisibility(View.GONE);
//		flowLayout_unenable=(TagFlowLayout)findViewById(R.id.flowLayout_unenable);
//		flowLayout_unenable.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
//			
//			@Override
//			public boolean onTagClick(FlowLayout parent, View view, int position) {
//				// TODO Auto-generated method stub
//				final ProdLabel curBean=beansUnenable.get(position);
//				doCommandUpdateProdLabelEnabled(curBean.getLabelCode(),"1");
//				return true;
//			}
//		});
		
	}
	
	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
//		if(beans==null){
//			return;
//		}
//		
//		
//		notifyDataChanged();
//		
//		if(checkedBeans!=null){
//			HashSet<Integer> positions=new HashSet<Integer>();
//			int i=0;
//			for(ProdLabel bean:beans){
//				for(ProdLabel checkedBean:checkedBeans){
//					if(bean.getLabelCode().equals(checkedBean.getLabelCode())){
//						positions.add(i);
//					}
//				}
//				i++;
//			}
//			flowLayout.setCheckedAt(positions);
//		}
	}

//	private void notifyDataChanged(){
//		if(tagAdapter==null){
//			flowLayout.setAdapter(tagAdapter=new TagAdapter<ProdLabel>(beans){
//				@Override
//				public View getView(FlowLayout parent, int position, ProdLabel item){
//					TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_text,flowLayout, false);
//					tv.setText(item.getDescription());
//					tv.setTag(item);
//					return tv;
//				}
//			});
//		}else{
//			flowLayout.notifyDataChanged();
//		}
//	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
//		switch (v.getId()) {
//		case R.id.btn_add:
//			String desc=et_desc.getText().toString().trim();
//			if(isEmpty(desc)){
//				doShake(context, et_desc);
//				return;
//			}
//			doCommandAddProdLabel(desc);
//			break;
//
//		default:
//			break;
//		}
	}
	
//	private void doCommandAddProdLabel(String description){
//		Commands.doCommandAddProdLabel(context, description, new Listener<JSONObject>() {
//
//			@Override
//			public void onResponse(JSONObject response) {
//				// TODO Auto-generated method stub
////				Log.i("tag", "response="+response.toString());
//				if (isSuccess(response)) {
//					et_desc.setText("");
//					doCommandGetProdLabelList();//重新请求
//				}
//			}
//		});
//	}
//
//	private void doCommandGetProdLabelList(){
//		Commands.doCommandGetProdLabelList(context, "1",new Listener<JSONObject>() {
//
//			@Override
//			public void onResponse(JSONObject response) {
//				// TODO Auto-generated method stub
////				Log.i("tag", "response="+response.toString());
//				if (isSuccess(response)) {
//					GetProdLabelListResponse obj=mapperToObject(response, GetProdLabelListResponse.class);
//					beans.clear();
//					beans.addAll(obj.getInfo());
//					doCommandGetProdLabelMappingList(goods_sn);
//				}
//			}
//		});
//	}
//	
//	private void doCommandUpdateProdLabelEnabled(String labelCode,String enabled){
//		Commands.doCommandUpdateProdLabelEnabled(context, labelCode,enabled,new Listener<JSONObject>() {
//			
//			@Override
//			public void onResponse(JSONObject response) {
//				// TODO Auto-generated method stub
////				Log.i("tag", "response="+response.toString());
//				if (isSuccess(response)) {
//					//移动到禁用
//					doCommandGetProdLabelList();
//					doCommandGetProdLabelListUnenable();
//				}
//			}
//		});
//	}
//	
//	private void doCommandGetProdLabelMappingList(String goodsCode){
//		Commands.doCommandGetProdLabelMappingList(context, goodsCode,new Listener<JSONObject>() {
//			
//			@Override
//			public void onResponse(JSONObject response) {
//				// TODO Auto-generated method stub
////				Log.i("tag", "response="+response.toString());
//				if (isSuccess(response)) {
//					GetProdLabelListResponse obj=mapperToObject(response, GetProdLabelListResponse.class);
//					checkedBeans=obj.getInfo();
//					updateViews(checkedBeans);
//					
//				}
//			}
//		});
//	}
//	private void doCommandAddProdLabelMapping(String goodsCode,String labelCode,String description){
//		Commands.doCommandAddProdLabelMapping(context,  goodsCode, labelCode, description,new Listener<JSONObject>() {
//			
//			@Override
//			public void onResponse(JSONObject response) {
//				// TODO Auto-generated method stub
////				Log.i("tag", "response="+response.toString());
//				if (isSuccess(response)) {
//				}
//			}
//		});
//	}
//	private void doCommandDeleteProdLabelMapping(String goodsCode,String labelCode){
//		Commands.doCommandDeleteProdLabelMapping(context,  goodsCode, labelCode,new Listener<JSONObject>() {
//			
//			@Override
//			public void onResponse(JSONObject response) {
//				// TODO Auto-generated method stub
////				Log.i("tag", "response="+response.toString());
//				if (isSuccess(response)) {
//				}
//			}
//		});
//	}
//	
//	private void doCommandGetProdLabelListUnenable(){
//		Commands.doCommandGetProdLabelList(context, "0",new Listener<JSONObject>() {
//
//			@Override
//			public void onResponse(JSONObject response) {
//				// TODO Auto-generated method stub
////				Log.i("tag", "response="+response.toString());
//				if (isSuccess(response)) {
//					GetProdLabelListResponse obj=mapperToObject(response, GetProdLabelListResponse.class);
//					beansUnenable.clear();
//					beansUnenable.addAll(obj.getInfo());
//					
//					if(beansUnenable.size()==0){
//						emptyView.setVisibility(View.VISIBLE);
//						flowLayout_unenable.setVisibility(View.GONE);
//					}else{
//						emptyView.setVisibility(View.GONE);
//						flowLayout_unenable.setVisibility(View.VISIBLE);
//						notifyDataChangedUnenable();
//					}
//					
//				}
//			}
//		});
//	}
//	
//	private List<ProdLabel> beansUnenable=new ArrayList<ProdLabel>();
//	private TagAdapter<ProdLabel> tagAdapter_unenable;
//	private void notifyDataChangedUnenable(){
//		if(tagAdapter_unenable==null){
//			flowLayout_unenable.setAdapter(tagAdapter_unenable=new TagAdapter<ProdLabel>(beansUnenable){
//				@Override
//				public View getView(FlowLayout parent, int position, ProdLabel item){
//					TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_text,flowLayout_unenable, false);
//					tv.setText(item.getDescription());
//					tv.setTag(item);
//					return tv;
//				}
//			});
//		}else{
//			flowLayout_unenable.notifyDataChanged();
//		}
//	}
//	
//	@Override
//	public void onBackPressed() {
//		// TODO Auto-generated method stub
//		if(isChanged){
//			//如果标签发生改变，需要通知上一个页面刷新界面
//			setResult(1);
//			finish();
//		}else{
//			super.onBackPressed();
//		}
//	}
//	
//	@Override
//	public void doLeftButtonClick(View v) {
//		// TODO Auto-generated method stub
//		onBackPressed();
//	}
//	@Override
//	public void doRightButtonClick(View v) {
//		// TODO Auto-generated method stub
//		onBackPressed();
//	}
	
}
