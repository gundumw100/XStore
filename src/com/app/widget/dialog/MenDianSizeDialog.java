package com.app.widget.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.net.Commands;
import com.app.widget.BaseDialog;
import com.app.xstore.R;
import com.app.xstore.shangpindangan.GetProdSpecListResponse;
import com.app.xstore.shangpindangan.GetSpecGroupListResponse;
import com.app.xstore.shangpindangan.ProdSpec;
import com.app.xstore.shangpindangan.ProdSpecGroup;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;

/**
 * 尺码管理Dialog
 * @author pythoner
 * 
 */
public class MenDianSizeDialog extends BaseDialog {

	private Map<String,List<ProdSpec>> map=new HashMap<String,List<ProdSpec>>();
	private List<ProdSpecGroup> beans=new ArrayList<ProdSpecGroup>();
	private EditText et_group;
	private ListView listView;
	private CommonAdapter<ProdSpecGroup> adapter;
//	private ProdSpecGroup curBean;

	public MenDianSizeDialog(Context context) {
		this(context, R.style.Theme_Dialog_NoTitle);
		// TODO Auto-generated constructor stub
	}
	
	public MenDianSizeDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_mendian_size);
		initViews();
		doCommandGetSpecGroupList();
	}

	private void initViews() {
		et_group = (EditText) findViewById(R.id.et_group);
		findViewById(R.id.btn_addGroup).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String groupName=et_group.getText().toString().trim();
				if(context.isEmpty(groupName)){
					context.doShake(context, et_group);
					return;
				}
				doCommandAddSpecGroup(groupName);
			}
		});
		
		listView = (ListView) findViewById(R.id.listView);
	}
	
	private void notifyDataSetChanged(){
		if(adapter==null){
			listView.setAdapter(adapter = new CommonAdapter<ProdSpecGroup>( context, beans,
					  R.layout.item_mendian_spec_group){
				
				  	@Override
					public boolean isEnabled(int position) {
						// TODO Auto-generated method stub
						return false;
					}
				  	
					@Override
					public void setValues(ViewHolder helper, final ProdSpecGroup item, final int position) {
						// TODO Auto-generated method stub
						helper.setText(R.id.item_0, item.getDescription());
						ImageView item_1=helper.getView(R.id.item_1);
						item_1.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								if(isShowing()){
									SimpleEditTextSingleDialog d=new SimpleEditTextSingleDialog(context, "", "请输入尺码名称");
									d.setOnClickListener(new SimpleEditTextSingleDialog.OnClickListener() {
										
										@Override
										public void onClick(View v, String text) {
											// TODO Auto-generated method stub
											doCommandAddProdSpec(item.getGroupCode(),text);
										}
									});
									d.show();
								}
							}
						});
//						final TextView item_color=helper.getView(R.id.item_color);
//						if(item.getBackgroundColor()==0){
//							item_color.setBackgroundColor(context.getColorCompat(R.color.grayWhite));
//							item_color.setTextColor(context.getColorCompat(R.color.grayDark));
//						}else{
//							item_color.setBackgroundColor(item.getBackgroundColor());
//							item_color.setTextColor(reverseColor(item.getBackgroundColor()));
//						}
//						item_color.setOnClickListener(new View.OnClickListener() {
//							
//							@Override
//							public void onClick(View v) {
//								// TODO Auto-generated method stub
//								if(isShowing()){
//									ColorPickerDialog d=new ColorPickerDialog(context);
//									d.setOnOkClickListener(new ColorPickerDialog.OnOkClickListener() {
//										
//										@Override
//										public void onColor(int color) {
//											// TODO Auto-generated method stub
//											curBean=item;
//											item_color.setBackgroundColor(color);
//											item_color.setTextColor(reverseColor(color));
//											//debug
//											doCommandUpdateBackColorByGroup(color);
//										}
//									});
//									d.show();
//								}
//							}
//						});
						
						LinearLayout item_container=helper.getView(R.id.item_container);
						item_container.removeAllViews();
						List<ProdSpec> children=map.get(item.getGroupCode());
						if(children!=null&&children.size()>0){
							for(final ProdSpec child:children){
								View view=LayoutInflater.from(context).inflate(R.layout.item_mendian_spec_child, null);
								/*view.setOnClickListener(new View.OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										if(onChildClickListener!=null){
											onChildClickListener.onChildClick(child,position);
										}
										dismiss();
									}
								});*/
								TextView item_child_0=(TextView)view.findViewById(R.id.item_0);
								TextView item_child_1=(TextView)view.findViewById(R.id.item_1);
								item_child_0.setText(child.getDescription());
								item_child_1.setText(child.getSpecCode());
								
								final com.app.widget.SwitchButton item_child_2=(com.app.widget.SwitchButton)view.findViewById(R.id.item_2);
								item_child_2.setChecked("1".equals(child.getEnabled()));
								item_child_2.setOnClickListener(new View.OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										doCommandUpdateProdSpecEnabled(child.getSpecCode(),item_child_2.isChecked()?"1":"0");
									}
								});
								item_container.addView(view);
							}
						}
						
					}
			});
		}else{
			adapter.notifyDataSetChanged();
		}
	}
	
	private OnChildClickListener onChildClickListener;
	
	public void setOnChildClickListener(OnChildClickListener onChildClickListener) {
		this.onChildClickListener = onChildClickListener;
	}

	public interface OnChildClickListener{
		void onChildClick(ProdSpec child,int position);
	}
	
//	private int reverseColor(int color){
//		int red=Color.red(color);
//		int green=Color.green(color);
//		int blue=Color.blue(color);
//		int reverseColor=Color.rgb(255-red, 255-green, 255-blue);
//		return reverseColor;
//	}
//	
//	private void doCommandUpdateBackColorByGroup(int color){
//		if(curBean==null){
//			return;
//		}
//		List<ProdColorGroup> groupInfos=new ArrayList<ProdColorGroup>();
//		curBean.setBackgroundColor(color);
//		groupInfos.add(curBean);
//		
//		List<ProdColor> colorInfos=null;
//		Commands.doCommandUpdateBackColorByGroup(context, groupInfos,colorInfos, new Listener<JSONObject>() {
//			
//			@Override
//			public void onResponse(JSONObject response) {
//				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
//				if (context.isSuccess(response)) {
//				}
//			}
//		});
//	}
	
	private void doCommandGetProdSpecList(){
		Commands.doCommandGetProdSpecList(context, null, new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				Log.i("tag", "response="+response.toString());
				if (isShowing()&&context.isSuccess(response)) {
					GetProdSpecListResponse obj=context.mapperToObject(response, GetProdSpecListResponse.class);
					List<ProdSpec> list=obj.getInfo();
					for(ProdSpec item:list){
						if(map.get(item.getGroupCode())==null){
							List<ProdSpec> items=new ArrayList<ProdSpec>();
							items.add(item);
							map.put(item.getGroupCode(), items);
						}else{
							List<ProdSpec> items=map.get(item.getGroupCode());
							items.add(item);
						}
					}
					
					notifyDataSetChanged();
					et_group.setText("");
				}
			}
		});
	}
	
	private void doCommandAddSpecGroup(String groupName){
		Commands.doCommandAddSpecGroup(context, groupName, new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				Log.i("tag", "response="+response.toString());
				if (context.isSuccess(response)) {
					doCommandGetSpecGroupList();
				}
			}
		});
	}
	
	private void doCommandGetSpecGroupList(){
		Commands.doCommandGetSpecGroupList(context, null, new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				Log.i("tag", "response="+response.toString());
				if (isShowing()&&context.isSuccess(response)) {
					GetSpecGroupListResponse obj=context.mapperToObject(response, GetSpecGroupListResponse.class);
					beans.clear();
					beans.addAll(obj.getInfo());
					
					map.clear();
					for(ProdSpecGroup bean:beans){
						map.put(bean.getGroupCode(), new ArrayList<ProdSpec>());
					}
					doCommandGetProdSpecList();
				}
			}
		});
	}
	
	private void doCommandAddProdSpec(final String groupCode,final String description){
		Commands.doCommandAddProdSpec(context, groupCode,description, new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				Log.i("tag", "response="+response.toString());
				if (context.isSuccess(response)) {
					doCommandGetSpecGroupList();
				}
			}
		});
	}
	
	private void doCommandUpdateProdSpecEnabled(String colorCode,String enabled){
		Commands.doCommandUpdateProdSpecEnabled(context,colorCode,enabled, new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", "response="+response.toString());
				if (context.isSuccess(response)) {
//					doCommandGetColorGroupList();
				}
			}
		});
	}
		
	
}

