package com.app.xstore.shangpindangan;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.fragment.BaseFragment;
import com.app.net.Commands;
import com.app.widget.dialog.SimpleEditTextSingleDialog;
import com.app.xstore.R;
import com.base.util.D;
import com.widget.flowlayout.FlowLayout;
import com.widget.flowlayout.TagAdapter;
import com.widget.flowlayout.TagFlowLayout;

/**
 * 自定义标签A
 * @author Ni Guijun
 *
 */
public class ProductAFragment extends BaseFragment implements View.OnClickListener{

	private String goods_sn;
	
	public static ProductAFragment newInstance(String param1) {
		ProductAFragment fragment = new ProductAFragment();
        Bundle args = new Bundle();
        args.putString("goods_sn", param1);
        fragment.setArguments(args);
        return fragment;
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.fragment_product_a, container,false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		goods_sn=getArguments().getString("goods_sn");
		initViews(view);
		doCommandGetProdLabelList();
	}
	
	@Override
	public void initViews(View view){
		view.findViewById(R.id.tv_a).setOnClickListener(this);
		view.findViewById(R.id.tv_b).setOnClickListener(this);
		view.findViewById(R.id.tv_c).setOnClickListener(this);
		view.findViewById(R.id.tv_d).setOnClickListener(this);
		view.findViewById(R.id.tv_e).setOnClickListener(this);
		
		initAFlowLayout(view);
		initBFlowLayout(view);
		initCFlowLayout(view);
		initDFlowLayout(view);
		initEFlowLayout(view);
	}
	
	private TagFlowLayout flowLayout_a;
	private TagAdapter<ProdLabel> aAdapter;
	private List<ProdLabel> aList=new ArrayList<ProdLabel>();
	private void initAFlowLayout(View view){
		flowLayout_a=(TagFlowLayout)view.findViewById(R.id.flowLayout_a);
		flowLayout_a.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
			
			@Override
			public boolean onTagClick(FlowLayout parent, View view, int position) {
				// TODO Auto-generated method stub
				addOrDeleteLabelMapping(flowLayout_a,aList, position);
				return true;
			}
		});
		flowLayout_a.setmOnTagLongClickListener(new TagFlowLayout.OnTagLongClickListener() {
			
			@Override
			public boolean onTagLongClick(FlowLayout parent, View view, final int position) {
				// TODO Auto-generated method stub
				showDeleteDialog(aList.get(position));
				return true;
			}
		});
		
		notifyDataChangedA();
	}
	
	private void notifyDataChangedA(){
		if(aAdapter==null){
			flowLayout_a.setAdapter(aAdapter=new TagAdapter<ProdLabel>(aList){
				@Override
				public View getView(FlowLayout parent, int position, ProdLabel item){
					TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_text,flowLayout_a, false);
					tv.setText(item.getDescription());
					tv.setTag(item);
					return tv;
				}
			});
		}else{
			flowLayout_a.notifyDataChanged();
		}
	}
	
	private TagFlowLayout flowLayout_b;
	private TagAdapter<ProdLabel> bAdapter;
	private List<ProdLabel> bList=new ArrayList<ProdLabel>();
	private void initBFlowLayout(View view){
		flowLayout_b=(TagFlowLayout)view.findViewById(R.id.flowLayout_b);
		flowLayout_b.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
			
			@Override
			public boolean onTagClick(FlowLayout parent, View view, int position) {
				// TODO Auto-generated method stub
				addOrDeleteLabelMapping(flowLayout_b,bList, position);
				return true;
			}
		});
		flowLayout_b.setmOnTagLongClickListener(new TagFlowLayout.OnTagLongClickListener() {
			
			@Override
			public boolean onTagLongClick(FlowLayout parent, View view, final int position) {
				// TODO Auto-generated method stub
				showDeleteDialog(bList.get(position));
				return true;
			}
		});

		notifyDataChangedB();
	}
	
	private void notifyDataChangedB(){
		if(bAdapter==null){
			flowLayout_b.setAdapter(bAdapter=new TagAdapter<ProdLabel>(bList){
				@Override
				public View getView(FlowLayout parent, int position, ProdLabel item){
					TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_text,flowLayout_b, false);
					tv.setText(item.getDescription());
					tv.setTag(item);
					return tv;
				}
			});
		}else{
			flowLayout_b.notifyDataChanged();
		}
	}
	
	
	private TagFlowLayout flowLayout_c;
	private TagAdapter<ProdLabel> cAdapter;
	private List<ProdLabel> cList=new ArrayList<ProdLabel>();
	private void initCFlowLayout(View view){
		flowLayout_c=(TagFlowLayout)view.findViewById(R.id.flowLayout_c);
		flowLayout_c.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
			
			@Override
			public boolean onTagClick(FlowLayout parent, View view, int position) {
				// TODO Auto-generated method stub
				addOrDeleteLabelMapping(flowLayout_c,cList, position);
				return true;
			}
		});
		flowLayout_c.setmOnTagLongClickListener(new TagFlowLayout.OnTagLongClickListener() {
			
			@Override
			public boolean onTagLongClick(FlowLayout parent, View view, final int position) {
				// TODO Auto-generated method stub
				showDeleteDialog(cList.get(position));
				return true;
			}
		});
		notifyDataChangedC();
	}
	
	private void notifyDataChangedC(){
		if(cAdapter==null){
			flowLayout_c.setAdapter(cAdapter=new TagAdapter<ProdLabel>(cList){
				@Override
				public View getView(FlowLayout parent, int position, ProdLabel item){
					TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_text,flowLayout_c, false);
					tv.setText(item.getDescription());
					tv.setTag(item);
					return tv;
				}
			});
		}else{
			flowLayout_c.notifyDataChanged();
		}
	}
	
	private TagFlowLayout flowLayout_d;
	private TagAdapter<ProdLabel> dAdapter;
	private List<ProdLabel> dList=new ArrayList<ProdLabel>();
	private void initDFlowLayout(View view){
		flowLayout_d=(TagFlowLayout)view.findViewById(R.id.flowLayout_d);
		flowLayout_d.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
			
			@Override
			public boolean onTagClick(FlowLayout parent, View view, int position) {
				// TODO Auto-generated method stub
				addOrDeleteLabelMapping(flowLayout_d,dList, position);
				return true;
			}
		});
		flowLayout_d.setmOnTagLongClickListener(new TagFlowLayout.OnTagLongClickListener() {
			
			@Override
			public boolean onTagLongClick(FlowLayout parent, View view, final int position) {
				// TODO Auto-generated method stub
				showDeleteDialog(dList.get(position));
				return true;
			}
		});
		notifyDataChangedD();
	}
	
	private void notifyDataChangedD(){
		if(dAdapter==null){
			flowLayout_d.setAdapter(dAdapter=new TagAdapter<ProdLabel>(dList){
				@Override
				public View getView(FlowLayout parent, int position, ProdLabel item){
					TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_text,flowLayout_d, false);
					tv.setText(item.getDescription());
					tv.setTag(item);
					return tv;
				}
			});
		}else{
			flowLayout_d.notifyDataChanged();
		}
	}
	
	private TagFlowLayout flowLayout_e;
	private TagAdapter<ProdLabel> eAdapter;
	private List<ProdLabel> eList=new ArrayList<ProdLabel>();
	private void initEFlowLayout(View view){
		flowLayout_e=(TagFlowLayout)view.findViewById(R.id.flowLayout_e);
		flowLayout_e.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
			
			@Override
			public boolean onTagClick(FlowLayout parent, View view, int position) {
				// TODO Auto-generated method stub
				addOrDeleteLabelMapping(flowLayout_e,eList, position);
				return true;
			}
		});
		flowLayout_e.setmOnTagLongClickListener(new TagFlowLayout.OnTagLongClickListener() {
			
			@Override
			public boolean onTagLongClick(FlowLayout parent, View view, final int position) {
				// TODO Auto-generated method stub
				showDeleteDialog(eList.get(position));
				return true;
			}
		});
		notifyDataChangedE();
	}
	
	private void notifyDataChangedE(){
		if(eAdapter==null){
			flowLayout_e.setAdapter(eAdapter=new TagAdapter<ProdLabel>(eList){
				@Override
				public View getView(FlowLayout parent, int position, ProdLabel item){
					TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_text,flowLayout_e, false);
					tv.setText(item.getDescription());
					tv.setTag(item);
					return tv;
				}
			});
		}else{
			flowLayout_e.notifyDataChanged();
		}
	}
	
	@Override
	public void updateViews(Object obj) {
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_a:
			break;
		case R.id.tv_b:
			break;
		case R.id.tv_c:
			
			break;
		case R.id.tv_d:
			
			break;
		case R.id.tv_e:
			
			break;
		}
		
		showDialog(v);
		
	}
	
	private void showDialog(View v){
		final TextView tv=(TextView)v;
		SimpleEditTextSingleDialog d=new SimpleEditTextSingleDialog(context, "", "请输入标签名称");
		d.setOnClickListener(new SimpleEditTextSingleDialog.OnClickListener() {
			
			@Override
			public void onClick(View v, String text) {
				// TODO Auto-generated method stub
				String property=(String)tv.getTag();
				if(property==null){
					showToast("标签未定义");
					return;
				}
				doCommandAddProdLabel(property,text);
			}
		});
		d.show();
	}
	
	private void doCommandAddProdLabel(final String property,final String description){
		final String classification="A";
		Commands.doCommandAddProdLabel(context, classification, property, description, new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (context.isSuccess(response)) {
					AddProdLabelResponse obj=context.mapperToObject(response, AddProdLabelResponse.class);
					if(obj.getInfo()!=null){
						ProdLabel bean=new ProdLabel();
						bean.setClassification(classification);
						bean.setProperty(property);
						bean.setLabelCodeFullName(classification+property+obj.getInfo());
						bean.setLabelCode(obj.getInfo());
						bean.setLabelDesc(description);
						bean.setDescription(description);
						
						addOrDeleteLabel(true,bean);
					}
					
				}
			}
		});
				
	}
	
	private void doCommandDeleteProdLabel(final ProdLabel bean){
		Commands.doCommandDeleteProdLabel(context, bean.getLabelCode(), new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (context.isSuccess(response)) {
					addOrDeleteLabel(false,bean);
				}
			}
		});
	}
	
	private void addOrDeleteLabel(boolean isAdd,ProdLabel bean){
		if("A".equals(bean.getProperty())){
			if(isAdd){
				aList.add(bean);
			}else{
				aList.remove(bean);
			}
			notifyDataChangedA();
		}else if("B".equals(bean.getProperty())){
			if(isAdd){
				bList.add(bean);
			}else{
				bList.remove(bean);
			}
			notifyDataChangedB();
		}else if("C".equals(bean.getProperty())){
			if(isAdd){
				cList.add(bean);
			}else{
				cList.remove(bean);
			}
			notifyDataChangedC();
		}else if("D".equals(bean.getProperty())){
			if(isAdd){
				dList.add(bean);
			}else{
				dList.remove(bean);
			}
			notifyDataChangedD();
		}else if("E".equals(bean.getProperty())){
			if(isAdd){
				eList.add(bean);
			}else{
				eList.remove(bean);
			}
			notifyDataChangedE();
		}
	}
	
	private void doCommandGetProdLabelList(){
		Commands.doCommandGetProdLabelList(context, new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (context.isSuccess(response)) {
					GetProdLabelListResponse obj=context.mapperToObject(response, GetProdLabelListResponse.class);
					if(obj.getInfo()!=null){
						for(ProdLabel item:obj.getInfo()){
							if("A".equals(item.getClassification())){
								if("A".equals(item.getProperty())){
									aList.add(item);
								}else if("B".equals(item.getProperty())){
									bList.add(item);
								}else if("C".equals(item.getProperty())){
									cList.add(item);
								}else if("D".equals(item.getProperty())){
									dList.add(item);
								}else if("E".equals(item.getProperty())){
									eList.add(item);
								}
							}
						}
						
						notifyDataChangedA();
						notifyDataChangedB();
						notifyDataChangedC();
						notifyDataChangedD();
						notifyDataChangedE();
						
						doCommandGetProdLabelMappingList();
						
					}
					
				}
			}
		});
	}
	
	private void showDeleteDialog(final ProdLabel bean){
		String message="确定要删除吗？";
		D.showDialog(getActivity(), message, "确定", "取消", new D.OnPositiveListener() {
			
			@Override
			public void onPositive() {
				// TODO Auto-generated method stub
				doCommandDeleteProdLabel(bean);
			}
		});
	}
	
	private void doCommandAddProdLabelMapping(String labelCode){
		Commands.doCommandAddProdLabelMapping(context, goods_sn, labelCode,new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (context.isSuccess(response)) {
				}
			}
		});
	}
	private void doCommandDeleteProdLabelMapping(String labelCode){
		Commands.doCommandDeleteProdLabelMapping(context,  goods_sn, labelCode,new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (context.isSuccess(response)) {
				}
			}
		});
	}
	
	private void doCommandGetProdLabelMappingList(){
		Commands.doCommandGetProdLabelMappingList(context, goods_sn,new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (context.isSuccess(response)) {
					GetProdLabelListResponse obj=context.mapperToObject(response, GetProdLabelListResponse.class);
					List<ProdLabel> checkedBeans=obj.getInfo();
					if(!context.isEmptyList(checkedBeans)){
						setCheckToFlowLayout(flowLayout_a,aList,checkedBeans);
						setCheckToFlowLayout(flowLayout_b,bList,checkedBeans);
						setCheckToFlowLayout(flowLayout_c,cList,checkedBeans);
						setCheckToFlowLayout(flowLayout_d,dList,checkedBeans);
						setCheckToFlowLayout(flowLayout_e,eList,checkedBeans);
					}
					
				}
			}
		});
	}
	
	private void setCheckToFlowLayout(TagFlowLayout flowLayout,List<ProdLabel> allBeans,List<ProdLabel> checkedBeans){
		HashSet<Integer> aSet=new HashSet<Integer>();
		for(int i=0;i<allBeans.size();i++){
			for(ProdLabel checkedBean:checkedBeans){
				if(allBeans.get(i).getLabelCode().equals(checkedBean.getLabelCode())){
					aSet.add(i);
				}
			}
		}
		flowLayout.setCheckedAt(aSet);
	}
	
	private void addOrDeleteLabelMapping(TagFlowLayout flowLayout,List<ProdLabel> list,int position){
		ProdLabel bean=list.get(position);
		if(flowLayout.isChecked(position)){
			doCommandAddProdLabelMapping(bean.getLabelCode());
		}else{
			doCommandDeleteProdLabelMapping(bean.getLabelCode());
		}
	}
	
}
