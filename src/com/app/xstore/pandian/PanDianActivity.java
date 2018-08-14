package com.app.xstore.pandian;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.litepal.crud.DataSupport;
import org.simple.eventbus.EventBus;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.app.widget.SimpleNumberDialog;
import com.app.widget.dialog.ProductCodeDialog;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;
import com.base.util.D;

/**
 * 盘点
 * @author Ni Guijun
 *
 */
public class PanDianActivity extends BaseActivity {

	private Context context;
	private ListView listView;
	private CommonAdapter<PanDianProduct> adapter;
	private List<PanDianProduct> beans = new ArrayList<PanDianProduct>();
	private int curPosition = -1;
	private boolean isAccumulation = true;// 累加
	private EditText et_gh, et_hw;
	private long date;
	private PanDianDan dan;//当前单据
	private boolean isChanged=false;//数据是否发生变化，用于判断是否需要更新数据库
	private String oldGH,oldHW;//初始时工号和货位，用于判断isChanged的值
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pandian);
		context=this;
		initActionBar("添加",null,"盘点", "扫描数:0", null);
		initViews();
		createFloatView(0);
		date=getIntent().getLongExtra("PanDianDan", -1);
		if(date==-1){
		}else{
			initValues();
		}
	}

	private void initValues(){
		dan=DataSupport.where("date = ?",String.valueOf(date)).findFirst(PanDianDan.class, true);
		if(dan!=null){
			et_gh.setText(dan.getGh());
			et_hw.setText(dan.getHw());
			getRightButton().setText("扫描数:"+dan.getSms());
			
			beans.clear();
			beans.addAll(dan.getPanDianProducts());
			notifyDataSetChanged();
			
			oldGH=et_gh.getText().toString();
			oldHW=et_hw.getText().toString();
		}
	}
	
	@Override
	public void doLeftButtonClick(View v) {
		// TODO Auto-generated method stub
		ProductCodeDialog d=new ProductCodeDialog(context,"","请输入商品号或扫描");
		d.setOnClickListener(new ProductCodeDialog.OnClickListener() {
			@Override
			public void onClick(View v, String text) {
				// TODO Auto-generated method stub
				onScanProductHandleMessage(text);
			}
		});
		d.show();
	}
	
//	@Override
//	public void doRightButtonClick(View v) {//导出
//		// TODO Auto-generated method stub
//		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//			String gh=et_gh.getText().toString().trim();//工号
//			String hw=et_hw.getText().toString().trim();//分区号
//			if(TextUtils.isEmpty(gh)){
//				showToast("缺少工号");
//				doShake(context, et_gh);
//				return;
//			}
//			if(TextUtils.isEmpty(hw)){
//				showToast("缺少货位");
//				doShake(context, et_hw);
//				return;
//			}
//			if(beans.size()==0){
//				showToast("空数据");
//				return;
//			}
//			String fileName="DSLR"+File.separator+"PDAINFO.INI";
//			File file = new File(Environment.getExternalStorageDirectory(),fileName);
//			if(!file.exists()){
//				showToast("缺少"+fileName+"文件");
//				return;
//			}
//			showProgress();
//			try{
//				Properties properties = new Properties();
//				FileInputStream s = new FileInputStream(file); 
//				properties.load(s);
//				
//				String PDACODE=properties.getProperty("PDACODE");//PDA编号
//				String GZ=properties.getProperty("GZ");//分店号
//				String PDRQ=properties.getProperty("PDRQ");//当前盘点日期
//				/*String PDINI=properties.getProperty("PDINI");//初始化标志
//				String PDDIF=properties.getProperty("PDDIF");//下发盘点差异标志
//				 */				
//				//2010002_20101215_1.TXT
//				String newName="DSLR"+File.separator+GZ+"_"+PDRQ.replace("-", "")+"_"+PDACODE+".txt";
//				File newFile = new File(Environment.getExternalStorageDirectory(),newName);
//				if(!newFile.exists()){
//					newFile.createNewFile();
//				}
//				FileWriter fw = new FileWriter(newFile);
//				BufferedWriter writer = new BufferedWriter(fw);
//	            for(PanDianProduct bean:beans){
//	            	StringBuffer sb=new StringBuffer();
//	            	sb.append(GZ).append("\t");//分店号
//	            	sb.append(PDACODE).append("\t");//PDA编号
//	            	sb.append(PDRQ).append("\t");//当前盘点日期
//	            	sb.append(bean.getAccountingCode()).append("\t");//核算码
//	            	sb.append(hw).append("\t");//分区编号
//	            	sb.append(bean.getPatternCode()).append("\t");//款号
//	            	sb.append(bean.getBarCode()).append("\t");//条码
//	            	sb.append(bean.getQty()).append("\t");//数量
//	            	sb.append(gh).append("\r\n");//盘点人(工号)，换行windows
//	            	writer.write(sb.toString());
////	                writer.newLine();//换行
//	            }
//				writer.flush();
//				writer.close();
//	            fw.close();
//	            showToast("导出成功");
//			}catch(IOException e){
//				e.printStackTrace();
//				showToast("导出失败");
//			}
//			closeProgress();
//		}
//		
//	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(date==-1){
			if(beans.size()>0){
				save();
	//	        clear();
		        EventBus.getDefault().post(App.EVENT_UPDATE_PANDIAN_LIST);
			}
		}else{
			//只要工号和货位任意一项发生变化，isChanged就为true
			if(!et_gh.getText().toString().trim().equals(oldGH)){
				isChanged=true;
			}
			if(!et_hw.getText().toString().trim().equals(oldHW)){
				isChanged=true;
			}
			if(dan!=null&&isChanged){
				dan.delete();//把先删后存看作更新
				save();
				EventBus.getDefault().post(App.EVENT_UPDATE_PANDIAN_LIST);
			}
		}
		super.onBackPressed();
	}
	private void save(){
		PanDianDan dan=new PanDianDan();
		dan.setDate(System.currentTimeMillis());
		dan.setGh(et_gh.getText().toString().trim());
		dan.setHw(et_hw.getText().toString().trim());
//		dan.setSms(Integer.parseInt(tv_scanNum.getText().toString()));
		
		String sms=getRightButton().getText().toString();
		
		dan.setSms(Integer.parseInt(sms.substring("扫描数:".length())));
		
		for(PanDianProduct bean:beans){
			bean.save();
			dan.getPanDianProducts().add(bean);
		}
		dan.save();
	}
	
	private void clear(){
		beans.clear();
//		et_gh.setText("");
//		et_hw.setText("");
		updateViews(beans);
//		int panDianDan=DataSupport.count(PanDianDan.class);
//		Log.i("tag", "panDianDan="+panDianDan);
//		int panDianProduct=DataSupport.count(PanDianProduct.class);
//		Log.i("tag", "panDianProduct="+panDianProduct);
	}
	
	@Override
    protected void onDestroy(){
        super.onDestroy();
        removeFloatView();
    }
	
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		$(R.id.btn_search).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ProductCodeDialog d=new ProductCodeDialog(context,"","请输入商品号或扫描");
				d.setOnClickListener(new ProductCodeDialog.OnClickListener() {
					@Override
					public void onClick(View v, String text) {
						// TODO Auto-generated method stub
						int position=-1;
						int size=beans.size();
						for(int i=0;i<size;i++){
							if(text.equals(beans.get(i).getBarCode())){
								position=i;
								curPosition=position;
								notifyDataSetChanged();
								listView.setSelection(position);
								break;
							}
						}
						if(position==-1){
							D.showDialog(PanDianActivity.this, "未找到商品【"+text+"】", "知道了");
						}
					}
				});
				d.show();
			}
		});
		
		listView=$(R.id.listView);
		et_gh=$(R.id.et_gh);
		et_hw=$(R.id.et_hw);
		$(R.id.iv_scan).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				scanType=1;
				doScan(resultHandler);
			}
		});
		
	}

	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
		notifyDataSetChanged();
		int count=0;
		for(PanDianProduct prod:beans){
			count+=prod.getQty();
		}
		getRightButton().setText("扫描数:"+count);
		isChanged=true;//只要商品发生变化，isChanged就为true
	} 
	
	@Override
	public void onScanProductHandleMessage(String prodID){
		if(scanType==0){
			Product product=DataSupport.where("barCode = ?", prodID).findFirst(Product.class);
			if(product==null){
				D.showDialog(this, "商品条码【"+prodID+"】商品主数据不存在！", "知道了");
			}else{
				if(isAccumulation){//逐条扫描数据累加
					int i = 0;
					boolean isExist = false;
					for (PanDianProduct bean : beans) {
						if (bean.getBarCode().equals(prodID)) {
							bean.setQty(bean.getQty() + 1);
							isExist = true;
//							curPosition = i;
							curPosition = 0;
							Collections.swap(beans, 0, i);//移动到第一项
							break;
						}
						i++;
					}
					if (!isExist) {
						beans.add(0,convert(product));
						curPosition = 0;
					}
				}else{//逐条扫描数据不累加
					beans.add(0,convert(product));
					curPosition = 0;
				}
				updateViews(beans);
				listView.setSelection(curPosition);
			}
		}else if(scanType==1){//货位
			et_hw.setText(prodID);
		}
		
	}
	
	private PanDianProduct convert(Product product){
		PanDianProduct p=new PanDianProduct();
		p.setQty(1);
		p.setBarCode(product.getBarCode());
		p.setName(product.getName());
		p.setAccountingCode(product.getAccountingCode());
		p.setPatternCode(product.getPatternCode());
		return p;
	}
	
	private void notifyDataSetChanged(){
		if(adapter==null){
			listView.setAdapter(adapter = new CommonAdapter<PanDianProduct>( context, beans,
					  R.layout.item_pandian){
					  
					@Override
					public void setValues(ViewHolder helper, final PanDianProduct item, final int position) {
						// TODO Auto-generated method stub
						helper.setText(R.id.item_0, item.getBarCode());
						helper.setText(R.id.item_1, item.getName());
						View container=helper.getView(R.id.container);
						container.setOnLongClickListener(new OnLongClickListener() {
							
							@Override
							public boolean onLongClick(View v) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(context, ProductPanDianActivity.class);
								intent.putExtra("BarCode", item.getBarCode());
								startActivity(intent);
								return true;
							}
						});
						
						final TextView item_2=helper.getView(R.id.item_2);
						item_2.setText(String.valueOf(item.getQty()));
						item_2.setOnLongClickListener(new OnLongClickListener() {
							
							@Override
							public boolean onLongClick(View v) {
								// TODO Auto-generated method stub
								SimpleNumberDialog d = new SimpleNumberDialog(context, item_2.getText().toString(), "数量");
								d.setOnClickListener(new SimpleNumberDialog.OnClickListener() {
									
									@Override
									public void onClick(View v, String text) {
										// TODO Auto-generated method stub
										item.setQty(Integer.parseInt(text));
										updateViews(beans);
									}
								});
								d.show();
								return true;
							}
						});
						
						TextView delete=helper.getView(R.id.tv_delete);
						delete.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								String text="是否删除？";
								D.showDialog(PanDianActivity.this, text, "确定", "取消", new D.OnPositiveListener() {
									
									@Override
									public void onPositive() {
										// TODO Auto-generated method stub
										beans.remove(position);
										updateViews(beans);
									}
								});
							}
						});
						
						if(curPosition==position){
							container.setBackgroundColor(0xFFFF6633);
						}else{
							container.setBackgroundColor(Color.TRANSPARENT);
						}
						
					}
			});
		}else{
			adapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		setThemeDrawable(this, R.id.btn_search);
	}
	
}
