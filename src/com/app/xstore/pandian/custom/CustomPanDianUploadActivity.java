package com.app.xstore.pandian.custom;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.simple.eventbus.EventBus;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.app.model.Box;
import com.app.model.ProdCheckDataInfo;
import com.app.model.ProdCheckDtl;
import com.app.net.Commands;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.R;
import com.base.util.D;

/**
 * 盘点
 * @author pythoner
 * 
 */
public class CustomPanDianUploadActivity extends BaseActivity implements View.OnClickListener{

	private Context context;
	private EditText et_cangku,et_remark;
	private CustomPanDianDan dan;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_pandian_upload);
		context = this;
		initActionBar("数据上传",null, null);//"校验设置"
		dan=getIntent().getParcelableExtra("CustomPanDianDan");
		if(dan==null){
			return;
		}
		initViews();
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		TextView tv_no=$(R.id.tv_no);
		TextView et_locId=$(R.id.et_locId);
		et_cangku=$(R.id.et_cangku);
		et_remark=$(R.id.et_remark);
//		if(!isEmpty(data.getRemark())){
//			et_remark.setText(data.getRemark());
//		}
		tv_no.setText("单据："+dan.getDate());
		et_locId.setText("货架："+dan.getShelf());
		$(R.id.btn_upload).setOnClickListener(this);
	}
	
	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_upload:
			doUpload();
			break;
		}
	}
	
	private void doUpload(){
		if(dan.getStatus()==1||dan.getStatus()==2){
			D.showDialog(CustomPanDianUploadActivity.this, "该单据已上传，不能修改！", "知道了");
//			D.showDialog(CustomPanDianUploadActivity.this, "该单据已上传，是否覆盖？", "是","否", new D.OnPositiveListener() {
//				
//				@Override
//				public void onPositive() {
//					// TODO Auto-generated method stub
//					doCommandUploadCustProdCheckData();
//				}
//			});
		}else{
			doCommandUploadCustProdCheckData();
		}
	}
	
	private void doCommandUploadCustProdCheckData(){//IsUploadWithoutCheck
		int checkPriority=1;//0 校验,1不校验， 2 提示 （默认为0，校验）	
		String cangku=et_cangku.getText().toString().trim();
		if(isEmpty(cangku)){
			doShake(context, et_cangku);
			return;
		}
		String docNo=App.user.getShopInfo().getShop_code()+App.user.getUserInfo().getUser_code()+dan.getDate();//主建、盘点单号
		ProdCheckDataInfo info=new ProdCheckDataInfo();
		info.setPddbh(docNo);//主建、盘点单号
		info.setZdrbh(dan.getGh());//制单人编号，能提供最好，不能提供默认几个字符
		info.setZdsj(formatTime(dan.getDate(),"yyyy-MM-dd HH:mm:ss"));//制单时间"2017-03-27T11:52:23.9370522+08:00"
		info.setPdck(cangku);//盘点仓库，能提供最好
		info.setPdbz(et_remark.getText().toString().trim());//备注
		info.setDcbj("1");//导出标记，0未导出，1已导出
		info.setPdkw(dan.getShelf());//
		List<ProdCheckDtl> items=new ArrayList<ProdCheckDtl>();
		if(!isEmptyList(dan.getPanDianProducts())){
			for(CustomPanDianProduct p:dan.getPanDianProducts()){
				ProdCheckDtl item=new ProdCheckDtl();
				item.setPddbh(docNo);
				item.setSm_code(p.getLsh());//
				item.setSl(p.getQty());
				items.add(item);
			}
		}
		if(!isEmptyList(dan.getBoxes())){
			for(Box p:dan.getBoxes()){
				ProdCheckDtl item=new ProdCheckDtl();
				item.setPddbh(docNo);
				item.setSm_code(p.getCode());//
				item.setSl(p.getQty());
				items.add(item);
			}
		}
		
		info.setProdcheckdtl(items);
		List<ProdCheckDataInfo> datas=new ArrayList<ProdCheckDataInfo>();
		datas.add(info);
		String shop_code=App.user.getShopInfo().getShop_code();
		String creator=App.user.getUserInfo().getUser_code();
		boolean isUploadWithoutCheck=false;//直接上传或者上传前与预盘数据对比的标志，默认为false，上传前需要与预盘数据对比。
		Commands.doCommandUploadCustProdCheckData(context, shop_code,creator,datas, isUploadWithoutCheck, checkPriority,new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				if(isSuccess(response)){
					UploadCustProdCheckDataResponse obj=mapperToObject(response, UploadCustProdCheckDataResponse.class);
					if(obj!=null){
						List<ProdCheckDataInfo> list=obj.getProdCheckDataResult();
						ProdCheckDataInfo bean=null;
						if(!isEmptyList(list)){
							bean=list.get(0);
						}
						if(bean==null){
							showToast("数据返回不正确");
							return;
						}
						ContentValues values = new ContentValues();
						values.put("status",1); 
						values.put("remark", bean.getPdbz());
						DataSupport.update(CustomPanDianDan.class, values, dan.getId());
						
						showToast("上传成功");
						//需要告诉扫描清单界面需要刷新
						EventBus.getDefault().post(App.EVENT_UPDATE_CUSTOM_PANDIANDAN_LIST);
						EventBus.getDefault().post(App.EVENT_FINISH);
						finish();
					}
				}
			}
		});
	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		setThemeDrawable(this, R.id.btn_upload);
	}
	
}
