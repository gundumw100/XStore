package com.app.xstore.pandian;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.litepal.crud.DataSupport;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.base.util.D;
import com.base.util.comm.SPUtils;

/**
 * 
 * @author Ni Guijun
 *
 */
public class ReadToDatabaseTask extends AsyncTask<String, Integer, Boolean> { 
	
	private Context context;
	private ProgressDialog dialog;
	private long minutes=0;
	
	public ReadToDatabaseTask(Context context){
		this.context=context;
	}
	
    @Override  
    protected void onPreExecute() {  
        super.onPreExecute();  
        showDialog();
    }  
    @Override  
    protected Boolean doInBackground(String... params) {
    	String fileName="DSLR"+File.separator+"T_GOODSBASE.TXT";
    	try {
    		publishProgress(100);
    		long start=System.currentTimeMillis();
    		DataSupport.deleteAll(Product.class);
    		long end=System.currentTimeMillis();
    		Log.i("tag", "清空数据消耗时间:"+(end-start));
    		publishProgress(101);
        	String encoding="GBK";
        	File file = new File(Environment.getExternalStorageDirectory(),fileName);
        	if(!file.exists()){
    			Log.i("tag", "缺少"+fileName+"文件");
    			publishProgress(-1);
    			return false;
    		}
//        	InputStreamReader reader = new InputStreamReader(getResources().getAssets().open(params[0]),encoding); 
        	FileInputStream fis = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(fis,encoding); 
            BufferedReader br = new BufferedReader(reader);
            String line="";
            start=System.currentTimeMillis();
            int i = 0;
            int capacity=2000;
            List<Product> products=new ArrayList<Product>(capacity);
            while((line = br.readLine()) != null){
            	if(isCancelled()){
            		break;
            	}
            	String[] split=line.split("\t");
            	if(split!=null&&split.length==9){
            		Product p=new Product();
            		p.setAccountingCode(split[0]);
            		p.setPatternCode(split[1]);
            		p.setBarCode(split[2]);
            		p.setColor(split[3]);
            		p.setSize(split[4]);
            		p.setName(split[5]);
            		p.setPrice(split[6]);
            		p.setCup(split[7]);
            		p.setQuantity(split[8]);
            		products.add(p);
            		if(products.size()==capacity){//每capacity保存一次
            			DataSupport.saveAll(products);
            			products.clear();//及时清空，防止OutOfMemoryError
            			publishProgress(103,i++);
            		}
            	}else{
            		Log.i("tag", "不规则数据："+line);
            	}
            }
            br.close();
            reader.close();
            fis.close();
            
            if(products.size()>0){//保存最后一组数据
    			DataSupport.saveAll(products);
    			products.clear();
    			publishProgress(103,i++);
    		}
            end=System.currentTimeMillis();
            minutes=end-start;
            Log.i("tag", "写入到数据库消耗时间:"+minutes);
            return true;
    	} catch (Exception e) {
            e.printStackTrace();
        }
    	return false;
    }  
    @Override  
    protected void onPostExecute(Boolean result) {  
        super.onPostExecute(result);
        if(result){
        	int count=DataSupport.count(Product.class);
        	SPUtils.put(context, App.KEY_HAS_DATA, true);
        	D.showDialog((BaseActivity)context,"写入数据完成，总共"+count+"条数据，耗时"+(minutes/1000)+"秒", "确定");
        }else{
        	Log.i("tag", "写入数据失败");
        }
        if(dialog!=null){
        	dialog.cancel();
        }
    }  
    @Override  
    protected void onProgressUpdate(Integer... values) {  
        super.onProgressUpdate(values); 
        switch (values[0]) {
        case -1:
//        	((BaseActivity)context).showToast("缺少基础文件");
        	D.showDialog((BaseActivity)context,"缺少基础文件", "确定");
        	break;
		case 100:
			if(dialog!=null)
			dialog.setTitle("正在删除旧数据...");
			break;
		case 101:
			if(dialog!=null)
			dialog.setTitle("正在加载文件...");
			break;
		case 102:
			if(dialog!=null)
			dialog.setTitle("正在将TXT写入数据库...");
			break;
		case 103:
			dialog.setMessage("正在导入数据，此操作一般在10分钟以内完成，请等待...("+values[1]+")");
			break;

		default:
			dialog.setMessage("正在导入数据，此操作比较耗时，请等待...("+(values[0]+1)+"/"+values[1]+")");
			break;
		}
        
    }

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
		D.showDialog((BaseActivity)context, "操作被中断，如需恢复，请点击‘初始化数据’", "确定");
	}
    
    private void showDialog(){
    	dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("准备数据中...");
        dialog.setMessage("正在初始化数据，此操作比较耗时，请等待...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "强行终止", new DialogInterface.OnClickListener() {  
        	  
            @Override  
            public void onClick(DialogInterface d, int which) {  
            	showForceDialog();
            }  
        });
        dialog.show();
    }
    
    private void showForceDialog(){
    	D.showDialog((BaseActivity)context, "强行终止会影响数据的完整性，需要中断操作吗？", "确定", "取消", new D.OnPositiveListener() {
			
			@Override
			public void onPositive() {
				// TODO Auto-generated method stub
				cancel(true);
			}
    	},new D.OnNegativeListener() {
			
			@Override
			public void onNegative() {
				// TODO Auto-generated method stub
				if(dialog!=null){
					dialog.show();
				}
			}
		});
    }
}
