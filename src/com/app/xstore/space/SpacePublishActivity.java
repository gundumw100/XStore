package com.app.xstore.space;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.base.util.D;
import com.app.model.ClerkSpaceInfo;
import com.app.model.JvclerkspaceInfo;
import com.app.model.response.ClerkUpLoadFileResponse;
import com.app.model.response.GetWXYTImageServerInfoResponse;
import com.app.net.Commands;
import com.app.util.FileUploader;
import com.app.util.Util;
import com.app.util.ZipUtil;
import com.app.xstore.App;
import com.app.xstore.BaseActivity;
import com.app.xstore.cashier.TradeDetailActivity;
import com.app.xstore.cashier.TradeLocalListActivity;
import com.app.xstore.R;
import com.widget.exfilepicker.ExFilePicker;
import com.widget.exfilepicker.ExFilePickerActivity;
import com.widget.exfilepicker.ExFilePickerParcelObject;
import com.zltd.decoder.DecoderManager;
import com.app.util.ImageUploadUtil;

/**
 * 我要发布
 * @author pythoner
 *
 */
public class SpacePublishActivity extends BaseActivity implements View.OnClickListener,DecoderManager.IDecoderStatusListener{

	private Context context;
	private int labelID;
	private EditText et_title,et_content,et_saleNo;
	private CheckBox cb_0;//在首页展示
	private ImageView[] btns;
	private static final int EX_FILE_PICKER_RESULT = 100;
	private Map<View,String> imageMap=new HashMap<View,String>();//
	private View targetView;//当前点击的ImageView
	private GridLayout gridLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_space_publish);
		context=this;
		labelID=getIntent().getIntExtra("LabelID", 0);
		initViews();
		initActionBar("我要发布", "发布", null);
		if(App.qqCloudInfo==null){
			doCommandGetWXYTImageServerInfo();
		}
	}
	
	private void doCommandGetWXYTImageServerInfo(){
		String nameSpace="zstore";
		Commands.doCommandGetWXYTImageServerInfo(context,nameSpace, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
//				Log.i("tag", response.toString());
				if(isSuccess(response)){
					GetWXYTImageServerInfoResponse obj=mapperToObject(response, GetWXYTImageServerInfoResponse.class);
					if(obj!=null){
						if(obj.getHeadInfo()!=null&&obj.getHeadInfo().size()>0){
							App.qqCloudInfo=obj.getHeadInfo().get(0);
						}
					}
				}
			}
		});
	}
	
//	private void updatePictures(GetDraftMacthInfoResponse obj){
//		map.clear();
//		List<DressMatchPictureInfo> p=obj.getDressMatchPictureInfos();
//		for(int i=0;i<p.size();i++){
//			DressMatchPictureInfo bean=p.get(i);
//			if(bean.getIsMainPicture().equals("1")){
//				Commands.loadImageByVolley(bean.getPictureURL(), btns[0], R.drawable.default_img, 0);
//			}else{
//				Commands.loadImageByVolley(bean.getPictureURL(), btns[i], R.drawable.default_img, 0);
//			}
//			
//			map.put(btns[i], bean);
//		}
//		
//	}
	
	@Override
	public void doRightButtonClick(View v) {
		// TODO Auto-generated method stub
		if(et_title.getText().length()==0){
			doShake(context, et_title);
			return;
		}
		if(et_content.getText().length()==0){
			doShake(context, et_content);
			return;
		}
		
		if(labelID==5){
			if(et_saleNo.getText().length()==0){
				showToast("晒单必须输入小票号");
				doShake(context, et_saleNo);
				return;
			}
			if(imageMap.size()==0){
				showToast("晒单必须要有图片");
				return;
			}
		}
		
		doCommandClerkSpaceSave();
	}
	
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		et_title=(EditText)findViewById(R.id.et_title);
		et_content=(EditText)findViewById(R.id.et_content);
		View layout_saleNo=findViewById(R.id.layout_saleNo);
		if(labelID==5){
			layout_saleNo.setVisibility(View.VISIBLE);
			et_saleNo=(EditText)findViewById(R.id.et_saleNo);
			findViewById(R.id.btn_saleNo).setOnClickListener(this);
		}else{
			layout_saleNo.setVisibility(View.GONE);
		}
		cb_0=(CheckBox)findViewById(R.id.cb_0);//在首页展示
		cb_0.setVisibility(View.GONE);
		
		gridLayout=(GridLayout)findViewById(R.id.gridLayout);
		gridLayout.setVisibility(View.GONE);
		/*if(labelID==5){
			gridLayout.setVisibility(View.GONE);
		}else{
			gridLayout.setVisibility(View.VISIBLE);
			ImageView btn_add_file=(ImageView)findViewById(R.id.btn_add_file);
			btn_add_file.setOnClickListener(new View.OnClickListener() {
	
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int maxFiles=6;
					if(gridLayout.getChildCount()>maxFiles){
						showToast("最多上传"+maxFiles+"个文件");
						return;
					}
					
					Intent intent = new Intent(getApplicationContext(), ExFilePickerActivity.class);
					//https://github.com/bartwell/ExFilePicker
					intent.putExtra(ExFilePicker.SET_ONLY_ONE_ITEM, true);
					intent.putExtra(ExFilePicker.SET_FILTER_LISTED, new String[] { "3gp", "mp4","ppt","pptx" });
	//				intent.putExtra(ExFilePicker.SET_FILTER_EXCLUDE, new String[] { "rar","zip" });
					intent.putExtra(ExFilePicker.DISABLE_NEW_FOLDER_BUTTON, true);
					intent.putExtra(ExFilePicker.DISABLE_SORT_BUTTON, false);
					intent.putExtra(ExFilePicker.ENABLE_QUIT_BUTTON, false);
					intent.putExtra(ExFilePicker.SET_CHOICE_TYPE, ExFilePicker.CHOICE_TYPE_FILES);
	//				intent.putExtra(ExFilePicker.SET_CHOICE_TYPE, ExFilePicker.CHOICE_TYPE_DIRECTORIES);
			        startActivityForResult(intent, EX_FILE_PICKER_RESULT);
					
				}
				
			});
		}*/
		
		btns=new ImageView[6];
		btns[0]=(ImageView)findViewById(R.id.btn_0);
		btns[1]=(ImageView)findViewById(R.id.btn_1);
		btns[2]=(ImageView)findViewById(R.id.btn_2);
		btns[3]=(ImageView)findViewById(R.id.btn_3);
		btns[4]=(ImageView)findViewById(R.id.btn_4);
		btns[5]=(ImageView)findViewById(R.id.btn_5);
		for(int i=0;i<btns.length;i++){
			btns[i].setTag(String.valueOf(i));
			btns[i].setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(imageMap.get(v)==null){
						showImageUploadDialog(v);
					}else{
						targetView=v;
						Intent intent=new Intent(context,ImageActivity.class);
						intent.putExtra("URL", imageMap.get(v));
						intent.putExtra("ShowTool", true);
						startActivity(intent);
					}
				}
			});
			
			btns[i].setOnLongClickListener(new View.OnLongClickListener() {
				
				@Override
				public boolean onLongClick(final View v) {
					// TODO Auto-generated method stub
					if(imageMap.get(v)!=null){
						D.showDialog(SpacePublishActivity.this, R.string.alert_detele_image, new D.OnPositiveListener() {
							
							@Override
							public void onPositive() {
								// TODO Auto-generated method stub
								((ImageView)v).setScaleType(ScaleType.CENTER);
								((ImageView)v).setImageResource(R.drawable.upload_picture);
								removeImage(v);
								imageMap.remove(v);
								targetView=null;
							}
						});
					}
					return true;
				}
			});
		}
		
	}

	private ImageUploadUtil imageUploadUtil;
	private void showImageUploadDialog(final View v){
		imageUploadUtil =new ImageUploadUtil(SpacePublishActivity.this,App.qqCloudInfo);
		imageUploadUtil.setOnUploadListener(new ImageUploadUtil.OnUploadListener() {

			@Override
			public void onUploadSuccess(final Bitmap photo,String downloadUrl,File file) {
				// TODO Auto-generated method stub
				((ImageView)v).setScaleType(ScaleType.CENTER_INSIDE);
				((ImageView)v).setImageBitmap(photo);
				removeImage(v);
				imageMap.put(v, downloadUrl);
			}

			@Override
			public void onUploadFailed(Bitmap photo,File file) {
				// TODO Auto-generated method stub
			}
			
		});
		imageUploadUtil.showDialog();
	}
	
	private void removeImage(View v){
		if(imageUploadUtil==null){
			imageUploadUtil =new ImageUploadUtil(SpacePublishActivity.this,App.qqCloudInfo);
		}
		if(imageMap.get(v)!=null){// 删除一张图片
			String url = imageMap.get(v);
			String fileId=url.substring(url.lastIndexOf("/")+1);
			imageUploadUtil.delete(fileId);
		}
	}
	
	@Override
	public void updateViews(Object obj) {
		// TODO Auto-generated method stub
//		notifyDataSetChanged();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_saleNo:
//			if(et_saleNo.getText().toString().trim().length()==0){
//				doShake(context, et_saleNo);
//				return;
//			}
			
			Intent intent=new Intent(context,TradeLocalListActivity.class);
			intent.putExtra("TradeInfo", et_saleNo.getText().toString().trim());
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	private JvclerkspaceInfo spaceInfo=new JvclerkspaceInfo();
	private void doCommandClerkSpaceSave(){
		StringBuffer imageUrls=new StringBuffer();
		Iterator<Entry<View, String>> iter = imageMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<View, String> entry = (Map.Entry<View, String>) iter.next();
			imageUrls.append(entry.getValue()).append(";");
		}
		spaceInfo.setImageurl(imageUrls.toString());
		spaceInfo.setOrgcode(App.user.getShopInfo().getShop_code());
		spaceInfo.setOrgname(App.user.getShopInfo().getShop_name());
		spaceInfo.setCreator(App.user.getUserInfo().getUser_code());
		spaceInfo.setLabelid(labelID);
		spaceInfo.setTitle(et_title.getText().toString().trim());
		spaceInfo.setDescription(et_content.getText().toString().trim());
		if(labelID==5){
			spaceInfo.setSaleno(et_saleNo.getText().toString().trim());
			spaceInfo.setTopmark((cb_0.isChecked()?1:0));
		}
		Commands.doCommandClerkSpaceSave(context, spaceInfo, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				Log.i("tag", "response="+response.toString());
				if(isSuccess(response)){
					Intent intent=new Intent(context,SpacePublishResultActivity.class);
					intent.putExtra("LabelID", labelID);
					startActivity(intent);
					finish();
				}
			}
		});
	}
	
	@Subscriber
	void updateByEventBus(String event) {
		if (event.equals(App.EVENT_SELECT_PICTURE)) {
			if(targetView!=null){
				showImageUploadDialog(targetView);
			}
		}
	}
	
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        super.onActivityResult(requestCode, resultCode, data);  
        Log.i("tag", "requestCode==" + requestCode + ";resultCode==" + resultCode);
        if (requestCode == EX_FILE_PICKER_RESULT) {
            if (data != null) {
                ExFilePickerParcelObject object = (ExFilePickerParcelObject) data.getParcelableExtra(ExFilePickerParcelObject.class.getCanonicalName());
                if (object.count > 0) {
                    // Here is object contains selected files names and path
                	for(String name:object.names){
                		final String filePath=object.path+name;
                		long fileSize=Util.getFileSize(filePath);
                		final long maxSize=10;//最大10MB
                		if(fileSize>maxSize*1024*1024){
                			showToast("文件大小不能超过"+maxSize+"MB");
                			return;
                		}
//                		String userId=getUser(context).getUserID();
                		String userId=App.user.getUserInfo().getUser_code();//debug
        				final String ext=Util.getExtName(filePath);
        				if(isEmpty(ext)){
        					showToast("未知格式文件");
        					return;
        				}
        				
        				String srcPath=filePath;
        				String zipPath=object.path;
        				String zipFileName="temp.zip";
        				try {
							ZipUtil.zip(srcPath, zipPath, zipFileName);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
        				
        				String newFilePath=object.path+zipFileName;
        				String newFileName=zipFileName;
        				String newExt="zip";
        				final TextView tv=createFileItem();
                		FileUploader uploader=new FileUploader(Commands.UPLOAD_ZIP_URL,userId,newFilePath,newFileName,newExt);
                		uploader.setOnUploadListener(new FileUploader.OnUploadListener() {
							
							@Override
							public void onUploadSuccess(String filePath, JSONObject response) {
								// TODO Auto-generated method stub
								if(isSuccess(response)){
									ClerkUpLoadFileResponse obj=mapperToObject(response, ClerkUpLoadFileResponse.class);
									tv.setText("上传完成");
//									if(ext.toLowerCase(Locale.ENGLISH).equals("png")||ext.toLowerCase(Locale.ENGLISH).equals("jpg")||ext.toLowerCase(Locale.ENGLISH).equals("jpeg")){
//										if(isEmpty(spaceInfo.getImageURL())){
//											spaceInfo.setImageURL(obj.getDownLoadURL());
//										}else{
//											spaceInfo.setImageURL(spaceInfo.getImageURL()+";"+obj.getDownLoadURL());
//										}
//									}else 
									if(ext.toLowerCase(Locale.ENGLISH).equals("3gp")||ext.toLowerCase(Locale.ENGLISH).equals("mp4")){
										if(isEmpty(spaceInfo.getVideourl())){
											spaceInfo.setVideourl(obj.getDownLoadURL());
										}else{
											spaceInfo.setVideourl(spaceInfo.getVideourl()+";"+obj.getDownLoadURL());
										}
									}else if(ext.toLowerCase(Locale.ENGLISH).equals("ppt")||ext.toLowerCase(Locale.ENGLISH).equals("pptx")){
										if(isEmpty(spaceInfo.getPpturl())){
											spaceInfo.setPpturl(obj.getDownLoadURL());
										}else{
											spaceInfo.setPpturl(spaceInfo.getPpturl()+";"+obj.getDownLoadURL());
										}
									}else{
										Log.i("tag", "不支持的格式");
										showToast("不支持的格式");
									}
								}
								
							}
							
							@Override
							public void onUploadFailed(String filePath) {
								// TODO Auto-generated method stub
								showToast("上传失败");
							}

							@Override
							public void onUploading(int progress) {
								// TODO Auto-generated method stub
								tv.setText(progress+"%");
							}
						});
                		uploader.uploadFile();
                	}
                }
            }
        }else{
        	if(imageUploadUtil!=null){
            	imageUploadUtil.onActivityResult(requestCode,resultCode,data);
            }
        }
        
    }  
	
	private TextView createFileItem(){
		Drawable drawable = getResources().getDrawable(R.drawable.efp__ic_file);
		drawable.setBounds(0, 0, 80, 96);
		TextView tv=new TextView(context);
		tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		tv.setGravity(Gravity.CENTER);
		tv.setText("0%");
		tv.setCompoundDrawables(null, drawable, null, null);
		gridLayout.addView(tv, 0);
		return tv;
	}
	
	@Override
	public void updateTheme(int color) {
		super.updateTheme(color);
		updateTheme();
	}
	
	private void updateTheme() {
		setThemeDrawable(context, R.id.btn_saleNo);
	}
	
	
}
