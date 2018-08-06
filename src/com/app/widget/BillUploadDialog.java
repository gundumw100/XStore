package com.app.widget;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Response.Listener;
import com.app.model.Bill;
import com.app.model.BillProduct;
import com.app.model.JvSimpleCheckInfo;
import com.app.net.Commands;
import com.app.util.ServerFtplet;
import com.app.xstore.RemoteManagementActivity;
import com.app.xstore.R;
import com.base.util.comm.SPUtils;
import com.base.util.comm.TimeUtils;


/**
 * 单据上传Dialog
 * @author pythoner
 * 
 */
public class BillUploadDialog extends BaseDialog {

	private final static String KEY_EMAIL="email";//需要记住邮箱
	private EditText et_email,et_remark;
	private List<Bill> beans;
	
	public BillUploadDialog(Context context,List<Bill> beans) {
		this(context, R.style.Theme_Dialog_NoTitle,beans);
		// TODO Auto-generated constructor stub
	}

	public BillUploadDialog(Context context, int theme,List<Bill> beans) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.beans = beans;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_bill_upload);
		initViews();
	}
	
	private void initViews() {
		String email=(String)SPUtils.get(context, KEY_EMAIL, "");
		et_email=(EditText)findViewById(R.id.et_email);
		et_email.setText(email);
		et_remark=(EditText)findViewById(R.id.et_remark);
		findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String email=et_email.getText().toString().trim();
				if(!context.isEmail(email)){
					doShake(et_email);
					return;
				}
				String remark=et_remark.getText().toString().trim();
				
				SPUtils.put(context, KEY_EMAIL, email);
				//do next
				doCommandSendSimpleCheckData(email,remark,beans);
			}
		});
		findViewById(R.id.btn_ftp).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				writeFileToSD(beans);
				Intent intent=new Intent(context,RemoteManagementActivity.class);
				context.startActivity(intent);
			}
		});
		
	}

	 private void writeFileToSD(List<Bill> datas) {
	    	if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
	    		showToast("No SD Card found");
	    		return;
	    	}
	    	try {
//	    		String directory = Environment.getExternalStorageDirectory().getPath() + "/zStore/data/";//指定到SD卡某个目录
	    		String directory = ServerFtplet.directory;//指定到SD卡某个目录
	    		String fileName=TimeUtils.getNow("扫描清单yyyyMMddHHmmss")+".txt";
	    		File path = new File(directory);
	    		File file = new File(directory + fileName);
				if (!path.exists()) {
					path.mkdir();
				}
				if (!file.exists()) {
					file.createNewFile();
				}
	    		RandomAccessFile f = new RandomAccessFile(file, "rw"); 
	    		long fileLength = f.length();
	    		f.seek(fileLength);
	    		String head="商品码\t\t名称\t数量\r\n";
				f.write(head.getBytes());
				for (Bill data : datas) {
					List<BillProduct> prods=DataSupport.where("documentNo = ?", data.getDocNum()).find(BillProduct.class); 
					data.setBillProducts(prods);
					if(!context.isEmptyList(data.getBillProducts())){
						for (BillProduct item : data.getBillProducts()) {
							fileLength = f.length();
							f.seek(fileLength);
							String content=item.getProdNum()+"\t"+item.getProdName()+"\t"+item.getQty();
							f.write((content+"\r\n").getBytes());
						}
					}
				}
				f.close();
	    	} catch(Exception e) {
	    		e.printStackTrace();
	    	}
	    }
	 
	private void doCommandSendSimpleCheckData(String email,String remark,List<Bill> beans){
		String shop_code=null;
		String emailDesc=remark;
		List<JvSimpleCheckInfo> jvSimpleCheckInfos=new ArrayList<JvSimpleCheckInfo>();
		for(Bill bean:beans){
			List<BillProduct> prods=DataSupport.where("documentNo = ?", bean.getDocNum()).find(BillProduct.class); 
			bean.setBillProducts(prods);
			if(prods!=null){
				for(BillProduct p:prods){
					JvSimpleCheckInfo item=new JvSimpleCheckInfo();
					item.setDoc_num(bean.getDocNum());
					item.setProd_name(p.getProdName());
					item.setProd_num(p.getProdNum());
					item.setQty(String.valueOf(p.getQty()));
					
					jvSimpleCheckInfos.add(item);
				}
			}
		}
		
		List<String> receiverList=new ArrayList<String>();
		receiverList.add(email);
		Commands.doCommandSendSimpleCheckData(context, shop_code, emailDesc, jvSimpleCheckInfos, receiverList, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				if(context.isSuccess(response)){
					showToast("发送成功");
					dismiss();
				}
			}
		});
	}
	
}
