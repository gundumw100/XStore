package com.app.xstore.pandian;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.litepal.crud.DataSupport;
import org.simple.eventbus.Subscriber;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.app.widget.VerificationCodeDialog;
import com.app.widget.dialog.ProductCodeDialog;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.base.app.CommonAdapter;
import com.base.app.ViewHolder;
import com.base.util.D;
import com.base.util.comm.SPUtils;
import com.base.util.comm.TimeUtils;

/**
 * 盘点单List
 * @author pythoner
 * 
 */
public class PanDianListActivity extends BaseActivity implements OnClickListener{

	private Context context;
	private ListView listView;
	private CommonAdapter<PanDianDan> adapter;
	private List<PanDianDan> beans=new ArrayList<PanDianDan>();
	private ReadToDatabaseTask task;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pandian_list);
		context = this;
//		int sum=DataSupport.sum(PanDianDan.class, "sms", int.class);
		initActionBar("初始化数据",null,"扫描清单", "总共：0", null);
		initViews();
		findBill();
		updateViews(beans);
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			boolean hasData=(Boolean)SPUtils.get(context, App.KEY_HAS_DATA, false);
			if(!hasData){
				task = new ReadToDatabaseTask(context);    
				task.execute();
			}
		}
		createFloatView(150);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		removeFloatView();
	}
	
	private void findBill(){
		List<PanDianDan> list=null;
		list = DataSupport.order("date desc").find(PanDianDan.class); 
		beans.clear();
		beans.addAll(list);
	}
	
	@Override
	public void doLeftButtonClick(View v) {
		// TODO Auto-generated method stub
		D.showDialog(this, "初始化数据会覆盖已有数据，需要继续操作吗？", "确定", "取消", new D.OnPositiveListener() {
			
			@Override
			public void onPositive() {
				// TODO Auto-generated method stub
				resetDatabase();
			}
		});
	}
	
	private void resetDatabase(){
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			SPUtils.remove(context, App.KEY_HAS_DATA);
			task = new ReadToDatabaseTask(context);    
			task.execute();
		}
	}
	
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		$(R.id.btn_ok).setOnClickListener(this);
		$(R.id.btn_search).setOnClickListener(this);
		$(R.id.btn_export).setOnClickListener(this);
		$(R.id.btn_clear).setOnClickListener(this);
		
		listView=$(R.id.listView);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				PanDianDan bean = beans.get(position);
				Intent intent = new Intent(context, PanDianActivity.class);
				intent.putExtra("PanDianDan", bean.getDate());
				startActivity(intent);
			}
		});
		listView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				// TODO Auto-generated method stub
				String text="确定要删除【"+beans.get(position).getHw()+"】货位的盘点数据？";
				D.showDialog(PanDianListActivity.this, text, "确定", "取消", new D.OnPositiveListener() {
					
					@Override
					public void onPositive() {
						// TODO Auto-generated method stub
//						DataSupport.deleteAll(PanDianProduct.class, "panDianDan_id = ?",String.valueOf(beans.get(position).getId()));
//						beans.get(position).delete();
						DataSupport.delete(PanDianDan.class, beans.get(position).getId());
						beans.remove(position);
						updateViews(beans);
					}
				});
				return true;
			}
		});
	}
	
	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
		if(obj==null){
			return;
		}
		notifyDataSetChanged();
		int sum=DataSupport.sum(PanDianDan.class, "sms", int.class);
		getRightButton().setText("总共："+sum);
	}

	private void notifyDataSetChanged(){
		if(adapter==null){
			listView.setAdapter(adapter = new CommonAdapter<PanDianDan>( context, beans,
					  R.layout.item_pandian_list){
					  
					@Override
					public void setValues(ViewHolder helper, final PanDianDan item, final int position) {
						// TODO Auto-generated method stub
						helper.setText(R.id.item_0, "工号："+item.getGh());
						helper.setText(R.id.item_1, TimeUtils.getTime(item.getDate()));
						helper.setText(R.id.item_2, "货位："+item.getHw());
						helper.setText(R.id.item_3, "扫描数："+item.getSms());
						
					}
			});
		}else{
			adapter.notifyDataSetChanged();
		}
	}
	
	@Subscriber
	void updateByEventBus(String event) {
		if(event.equals(App.EVENT_UPDATE_PANDIAN_LIST)){
			findBill();
			updateViews(beans);
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_ok://添加单据
			Intent intent=new Intent(context,PanDianActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_search://
			ProductCodeDialog d=new ProductCodeDialog(context,"","请输入商品号或扫描");
			d.setOnClickListener(new ProductCodeDialog.OnClickListener() {
				@Override
				public void onClick(View v, String text) {
					// TODO Auto-generated method stub
					onScanProductHandleMessage(text);
				}
			});
			d.show();
			break;
		case R.id.btn_export://导出
			saveToFile();
			break;
		case R.id.btn_clear:
			final VerificationCodeDialog dialog=new VerificationCodeDialog(context);
			dialog.setOnClickListener(new VerificationCodeDialog.OnClickListener() {
				
				@Override
				public void onClick(View v, String text) {
					// TODO Auto-generated method stub
					DataSupport.deleteAll(PanDianDan.class);
					beans.clear();
					updateViews(beans);
				}
			});
			dialog.show();
			break;

		default:
			break;
		}
	}
	
	private void saveToFile(){
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return;
		}
		if(beans.size()==0){
			showToast("空数据");
			return;
		}
		String fileName="DSLR"+File.separator+"PDAINFO.INI";
		File file = new File(Environment.getExternalStorageDirectory(),fileName);
		if(!file.exists()){
			showToast("缺少"+fileName+"文件");
			return;
		}
		showProgress();
		try{
			Properties properties = new Properties();
			FileInputStream s = new FileInputStream(file); 
			properties.load(s);
			
			String PDACODE=properties.getProperty("PDACODE");//PDA编号
			String GZ=properties.getProperty("GZ");//分店号
			String PDRQ=properties.getProperty("PDRQ");//当前盘点日期
			/*String PDINI=properties.getProperty("PDINI");//初始化标志
			String PDDIF=properties.getProperty("PDDIF");//下发盘点差异标志
			 */				
			//2010002_20101215_1.TXT
			String newName="DSLR"+File.separator+GZ+"_"+PDRQ.replace("-", "")+"_"+PDACODE+".txt";
			File newFile = new File(Environment.getExternalStorageDirectory(),newName);
			if(!newFile.exists()){
				newFile.createNewFile();
			}
			FileWriter fw = new FileWriter(newFile);
			BufferedWriter writer = new BufferedWriter(fw);
			
			for(PanDianDan dan:beans){
				List<PanDianProduct> list=DataSupport.where("panDianDan_id = ?",""+dan.getId()).find(PanDianProduct.class);
				if(list!=null){
			        for(PanDianProduct bean:list){
			        	StringBuffer sb=new StringBuffer();
			        	sb.append(GZ).append("\t");//分店号
			        	sb.append(PDACODE).append("\t");//PDA编号
			        	sb.append(PDRQ).append("\t");//当前盘点日期
			        	sb.append(bean.getAccountingCode()).append("\t");//核算码
			        	sb.append(dan.getHw()).append("\t");//分区编号
			        	sb.append(bean.getPatternCode()).append("\t");//款号
			        	sb.append(bean.getBarCode()).append("\t");//条码
			        	sb.append(bean.getQty()).append("\t");//数量
			        	sb.append(dan.getGh()).append("\r\n");//盘点人(工号)，换行windows
			        	writer.write(sb.toString());
			//            writer.newLine();//换行
			        }
				}
			}
			writer.flush();
			writer.close();
	        fw.close();
	        showToast("导出成功");
		}catch(IOException e){
			e.printStackTrace();
			showToast("导出失败");
		}
		closeProgress();
	}
	
	@Override
	public void onScanProductHandleMessage(String prodID){
		Intent intent=new Intent(context,ProductPanDianActivity.class);
		intent.putExtra("BarCode", prodID);
		startActivity(intent);
	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		setThemeDrawable(this, R.id.btn_ok);
		setThemeDrawable(this, R.id.btn_search);
		setThemeDrawable(this, R.id.btn_export);
	}
}
