package com.app.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.app.model.WXYTImageServerInfo;
import com.app.xstore.App;
import com.app.xstore.R;
import com.base.util.L;
import com.qq.cloud.PicCloud;
import com.qq.cloud.PornDetectInfo;
import com.qq.cloud.UploadResult;

/**
 * 图片上传工具类
 * 
 * note: 图片可以上传到QQ万象优图
 * 
 * @author pythoner
 * 
 */
public class ImageUploadUtil {

	//QQ万象优图参数
	public static int APP_ID_V2 = 10009153;// 10000001
	public static String SECRET_ID_V2 = "AKIDfSSfovfua0dim2D6lbDo9uFHOQ29q1cO";// AKIDNZwDVhbRtdGkMZQfWgl2Gnn1dhXs95C0
	public static String SECRET_KEY_V2 = "leHXiJjxZeVYToYyYjKN2UXEKXqYkyfs";// ZDdyyRLCLv1TkeYOl5OCMLbyH4sJ40wp
	public static String BUCKET = "zstore"; // 空间名 testa
	
	public static final boolean NEED_CHECK_PORN = false; // 是否有必要检查porn图片
	
	private String[] items = new String[] { "图库", "拍照" };
	/* 头像名称 */
	private static String IMAGE_FILE_NAME = "image_20151105090000000.jpg";
	/* 请求码 */
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int SELECT_PIC_KITKAT = 3;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	private Activity context;
	private String uploadUrl;

	private final int ST_UPLOAD_SUCCESS=0;
	private final int ST_UPLOAD_FAIL=1;
	private int type=0;//0:方形，1，长方形4:3
	
	Handler handler = new Handler(){   
        public void handleMessage(Message msg) { 
        	super.handleMessage(msg);  
            switch (msg.what) {      
            case ST_UPLOAD_SUCCESS:
            	if (onUploadListener != null) {
            		DataWrap datas=(DataWrap)msg.obj;
            		onUploadListener.onUploadSuccess(datas.photo, datas.downloadUrl,file);
        		}
                break;  
            case ST_UPLOAD_FAIL:
            	if (onUploadListener != null) {
            		DataWrap datas=(DataWrap)msg.obj;
            		onUploadListener.onUploadFailed(datas.photo,file);
            	}
            	break;
            	default:
            		break;
            }      
            
        }  
          
    };  
    
    /**
     * 使用QQ云图保存图片
     * @param context
     * @param qqCloudInfo 
     */
	public ImageUploadUtil(Activity context,WXYTImageServerInfo qqCloudInfo) {
		this(context,qqCloudInfo,1);
	}
	public ImageUploadUtil(Activity context,WXYTImageServerInfo qqCloudInfo,int type) {
		this.context = context;
		this.type = type;
		IMAGE_FILE_NAME="image_"+System.currentTimeMillis()+".jpg";
		L.i("图片文件名："+IMAGE_FILE_NAME);
		if(qqCloudInfo==null){
			throw new IllegalArgumentException("The WXYTImageServerInfo can't be null.");
		}
		APP_ID_V2=Integer.parseInt(qqCloudInfo.getProjectid());
		SECRET_ID_V2 = qqCloudInfo.getSecretid();
		SECRET_KEY_V2 = qqCloudInfo.getSecretkey();
		BUCKET = qqCloudInfo.getNameSpace();
	}

	/**
	 * 使用本地服务器存储
	 * @param context
	 * @param uploadUrl
	 */
	public ImageUploadUtil(Activity context,String uploadUrl) {
		if(TextUtils.isEmpty(uploadUrl)||!uploadUrl.startsWith("http")){
			throw new IllegalArgumentException();
		}
//		String uploadUrl=Commands.URL_SYSTEMS_UPLOAD_FILE+"?user_session_key="+BaseApp.user.getUser_session_key()+"&file_category=1";
		this.context = context;
		this.uploadUrl = uploadUrl;
		IMAGE_FILE_NAME="image_"+System.currentTimeMillis()+".jpg";
		L.i("图片文件名："+IMAGE_FILE_NAME);
	}
	public void showDialog() {
		new AlertDialog.Builder(context)
				.setTitle("图片来源")
				.setCancelable(true)
				.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent=null;
						switch (which) {
						case 0:// Local Image
							intent = new Intent(Intent.ACTION_GET_CONTENT);
							intent.addCategory(Intent.CATEGORY_OPENABLE);
							intent.setType("image/*");
							if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
								context.startActivityForResult(intent, SELECT_PIC_KITKAT);
							} else {
								context.startActivityForResult(intent, IMAGE_REQUEST_CODE);
							}
							break;
						case 1:// Take Picture
							// 判断存储卡是否可以用，可用进行存储
							if (hasSdcard()) {
								if (App.user.getPhoneType()==0) {
									
								}else{
									App.scanUtil.doCloseScanner();
								}
//								Log.i("tag", "============== Scan Stop==================");
								intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
								///storage/sdcard0
								File file = getDiskCacheDir(context,IMAGE_FILE_NAME);
								Uri uri = Uri.fromFile(file);
								intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
								context.startActivityForResult(intent, CAMERA_REQUEST_CODE);
							}else{
								Toast.makeText(context, R.string.alert_no_sdcard, Toast.LENGTH_SHORT).show();
							}
							break;
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 结果码不等于取消时候
		if (resultCode != Activity.RESULT_CANCELED) {
			switch (requestCode) {
			case ImageUploadUtil.IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case ImageUploadUtil.SELECT_PIC_KITKAT:
				startPhotoZoom(data.getData());
				break;
			case ImageUploadUtil.CAMERA_REQUEST_CODE:
//				File file = new File(Environment.getExternalStorageDirectory(),ImageUploadUtil.IMAGE_FILE_NAME);
				File file = getDiskCacheDir(context,IMAGE_FILE_NAME);
				if (file.exists()) {
					startPhotoZoom(Uri.fromFile(file));
				}
				break;
			case ImageUploadUtil.RESULT_REQUEST_CODE:
				if (data != null) {
					setData(data);
				}
				break;
			}
		}
		if (App.user.getPhoneType()==0) {
			
		}else{
			App.scanUtil.reopenScanner();//
		}
	}

	private boolean hasSdcard() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	private void startPhotoZoom(Uri uri) {
		if (uri == null) {
			L.i("The uri is not exist.");
			return;
		}
		//计算剪切的最佳比例
		int aspectX=4,aspectY=3;//长宽比例
		Bitmap bitmap=getBitmapFromUri(uri);
		if(bitmap!=null){
			int width=bitmap.getWidth();
			int height=bitmap.getHeight();
			if(width<height){
				aspectX=3;
				aspectY=4;
			}
			bitmap=null;
		}
				
//		Intent intent=new Intent(context,CropImageActivity.class);
//		intent.putExtra("Uri", uri);
//		context.startActivityForResult(intent, RESULT_REQUEST_CODE);
		Intent intent = new Intent("com.android.camera.action.CROP");
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			String url = getPath(context, uri);
			intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
		} else {
			intent.setDataAndType(uri, "image/*");
		}
		int pixel=100;
		if(type==0){
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			// outputX outputY 是裁剪图片宽高
			//不要太大，否则报java.lang.SecurityException: Unable to find app for caller android.app.ApplicationThreadProxy...
//			int px=DisplayUtils.dip2px(context, App.res.getDimension(R.dimen.face_img_width));
			intent.putExtra("outputX", 3*pixel);
			intent.putExtra("outputY", 3*pixel);
		}else{
			intent.putExtra("aspectX", aspectX);
			intent.putExtra("aspectY", aspectY);
			intent.putExtra("outputX", aspectX*pixel);
			intent.putExtra("outputY", aspectY*pixel);
		}
		// 设置裁剪
		intent.putExtra("crop", "true");
		intent.putExtra("return-data", true);
		intent.putExtra("scale", true);//黑边
		intent.putExtra("scaleUpIfNeeded", true);//黑边
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f))
//		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		context.startActivityForResult(intent, RESULT_REQUEST_CODE);
	}

	/**
	 * 通过一个uri获取一个Bitmap对象
	 * @param uri
	 * @return
	 */
	private Bitmap getBitmapFromUri(Uri uri) {
		try {
			// 读取uri所在的图片
			Bitmap bitmap = MediaStore.Images.Media.getBitmap(
					context.getContentResolver(), uri);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Bitmap photo;
	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setData(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			photo = extras.getParcelable("data");
			if (photo != null) {
				
//				if(onUploadListener!=null){
//					onUploadListener.onUploadSuccess(photo, null);
//				}
				
				//这里是保存图片到本地
				file=saveBitmap(photo);
				//然后上传图片到服务端保存;
				//此处每个人实现是不一样的，一般需要重写uploadImage方法;
				//我这里没有抽成接口，为了快速迭代使用；
				//但是，我在uploadImage成功后回调onUploadSuccess方法；
				uploadImage(photo);
			}

		}
	}

	private File file;
	private OnUploadListener onUploadListener;

	public void setOnUploadListener(
			OnUploadListener onUploadListener) {
		this.onUploadListener = onUploadListener;
	}

	public interface OnUploadListener {
		void onUploadSuccess(Bitmap photo, String downloadUrl,File file);
		void onUploadFailed(Bitmap photo,File file);
	}

	private File saveBitmap(Bitmap bitmap) {
//		File file = new File(Environment.getExternalStorageDirectory(),IMAGE_FILE_NAME);
		File file = getDiskCacheDir(context,IMAGE_FILE_NAME);
		Log.i("tag", file.getAbsolutePath());
		try {
			file.createNewFile();
			FileOutputStream fos = null;
			fos = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * 缓存文件文件的位置
	 * @param context
	 * @param uniqueName
	 * @return
	 */
	private File getDiskCacheDir(Context context, String uniqueName) {
	      String cachePath;
	      //如果sd卡存在并且没有被移除
	      if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())||!Environment.isExternalStorageRemovable()) {
	          cachePath = context.getExternalCacheDir().getPath();
	      } else {
	          cachePath = context.getCacheDir().getPath();
	      }
	      return new File(cachePath + File.separator + uniqueName);
	  }
	
	private void uploadToQQcloud(String filePath) throws Exception {
//		Log.i("tag", "APP_ID_V2="+APP_ID_V2);
//		Log.i("tag", "SECRET_ID_V2="+SECRET_ID_V2);
//		Log.i("tag", "SECRET_KEY_V2="+SECRET_KEY_V2);
//		Log.i("tag", "BUCKET="+BUCKET);
//		Log.i("tag", "fileName="+fileName);
		PicCloud pc = new PicCloud(APP_ID_V2, SECRET_ID_V2, SECRET_KEY_V2, BUCKET);
		picBase(pc, filePath);
	}
	
	private void picBase(PicCloud pc, String fileName) throws Exception {
//		String url = "";
//		String downloadUrl = "";
		UploadResult result = new UploadResult();
//		UploadResult result2 = new UploadResult();
//		PicInfo info = new PicInfo();
		int ret=-1;
		// 上传一张图片�
		// 1. 直接指定图片文件名的方式
//		ret = pc.upload(fileName, result);
		
		// 2. 文件流的方式
		FileInputStream fileStream = new FileInputStream(fileName);
		ret = pc.upload(fileStream, result);
		L.i(result.downloadUrl);
//		L.i("url==="+result.url);
//		L.i("width,height==="+result.width+"*"+result.height);
//		L.i("fileId==="+result.fileId);
//		L.i("analyze==="+result.analyze.food+";"+result.analyze.fuzzy);
		//http://mecity-10009153.image.myqcloud.com/faea3c86-5574-4f24-9a37-7f417ff726fa
		//http://web.image.myqcloud.com/photos/v2/10009153/mecity/0/faea3c86-5574-4f24-9a37-7f417ff726fa
		//1800*1200
		//faea3c86-5574-4f24-9a37-7f417ff726fa
		//com.qcloud.PicAnalyze@519e0684
		
		if(ret==0){
			if(NEED_CHECK_PORN){
				if(checkPorn(result.downloadUrl)){
//					 ret = pc.delete(result.fileId);// 删除一张图片
//					 DataWrap obj = new DataWrap(photo, null);
//					 sendMessage(ST_UPLOAD_FAIL, obj);
//					 return;
					L.w("It maybe a porn picture.");
				}else{
					
				}
			}
			DataWrap obj = new DataWrap(photo, result.downloadUrl);
			sendMessage(ST_UPLOAD_SUCCESS, obj);
		}else{
			DataWrap obj = new DataWrap(photo, null);
			sendMessage(ST_UPLOAD_FAIL, obj);
		}
		
//		// 3. 字节流的方式
//		// ByteArrayInputStream inputStream = new ByteArrayInputStream(你自己的参数);
//		// ret = pc.upload(inputStream, result);
//		// 查询图片的状态��
//		ret = pc.stat(result.fileId, info);
//		
//		ret = pc.copy(result.fileId, result2);// 复制一张图片
//		ret = pc.delete(result.fileId);// 删除一张图片
	}
	
	// 删除一张图片
	public void delete(final String fileId){
		TimerTask task=new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				PicCloud pc = new PicCloud(APP_ID_V2, SECRET_ID_V2, SECRET_KEY_V2, BUCKET);
				pc.delete(fileId);
			}
		};
		Timer t=new Timer();
		t.schedule(task, 5);
	}
	
	private void uploadImage(final Bitmap photo) {
		TimerTask task=new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
//				File file = new File(Environment.getExternalStorageDirectory(),IMAGE_FILE_NAME);
				File file = getDiskCacheDir(context,IMAGE_FILE_NAME);
				String filePath = file.getAbsolutePath();
//				Log.i("tag", filePath);
//				Log.i("tag", "filePath==="+filePath);
				// /storage/emulated/0/image_1445229239193.jpg
				//如果本地上传地址不存在则使用QQ优图
				if(TextUtils.isEmpty(uploadUrl)||!uploadUrl.startsWith("http")){
					try {
						uploadToQQcloud(filePath);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					uploadFile(photo,filePath,uploadUrl);
				}
				
			}
		};
		Timer t=new Timer();
		t.schedule(task, 5);
		
		//使用volley上传，该方法可能会报错
		/*Commands.uploadFile(context, 1, App.user, filePath,
				new VolleyHelper.OnFileUploadListener() {
					@Override
					public void onFileUploadSuccess(String orignUrl,
							String imgUrl, String circleImgUrl) {
						// TODO Auto-generated method stub
						if (onFaceUploadSuccessListener != null) {
							onFaceUploadSuccessListener
									.onFaceUploadSuccess(view, photo, orignUrl,
											imgUrl, circleImgUrl);
						}
					}

				});*/
	}

	
	/**
	 * 上传文件至Server
	 * @param photo
	 * @param filePath 文件路径
	 * @param urlString 连接地址
	 */
	private void uploadFile(Bitmap photo, String filePath, String urlString) {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		try {
			URL url = new URL(urlString);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestMethod("POST");
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			DataOutputStream dos = new DataOutputStream(con.getOutputStream());
			dos.writeBytes(twoHyphens + boundary + end);
			dos.writeBytes(
					"Content-Disposition: form-data; " + "name=\"file1\";filename=\"" + IMAGE_FILE_NAME + "\"" + end);
			dos.writeBytes(end);
			FileInputStream fis = new FileInputStream(filePath);
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			/* 从文件读取数据至缓冲区 */
			while ((length = fis.read(buffer)) != -1) {
				dos.write(buffer, 0, length);
			}
			dos.writeBytes(end);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
			dos.flush();
			dos.close();
			fis.close();
			/* 取得Response内容 */
			InputStream is = con.getInputStream();
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			is.close();

			DataWrap obj = new DataWrap(photo, b.toString());
			sendMessage(ST_UPLOAD_SUCCESS, obj);
		} catch (Exception e) {
			e.printStackTrace();
			DataWrap obj = new DataWrap(photo, null);
			sendMessage(ST_UPLOAD_FAIL, obj);
		}

	}
    
    private void sendMessage(int st,Object obj){
    	Message message = new Message();      
        message.what = st;     
        message.obj=obj;
        handler.sendMessage(message);
    }
    
    
    /**
     * 
     * @param url eg:
     * public static final String PORN_URL = "http://b.hiphotos.baidu.com/image/pic/item/8ad4b31c8701a18b1efd50a89a2f07082938fec7.jpg";
     */
    private boolean checkPorn(String url) {
		PicCloud pc = new PicCloud(APP_ID_V2, SECRET_ID_V2, SECRET_KEY_V2, BUCKET);
		PornDetectInfo info = new PornDetectInfo();
		int ret = pc.pornDetect(url, info);
		if(ret==0){
			L.i("信心confidence="+info.confidence);
			L.i("热分数hotScore="+info.hotScore);
			L.i("normalScore="+info.normalScore);
			L.i("pornScore="+info.pornScore);
			if(info.pornScore>85){//
				return true;
			}
		}
		return false;
	}
    
    
    class DataWrap{
    	Bitmap photo;
    	String downloadUrl;
    	DataWrap(Bitmap photo,String downloadUrl){
    		this.photo=photo;
    		this.downloadUrl=downloadUrl;
    	}
    }
    
	@SuppressLint("NewApi")
	private static String getPath(final Context context, final Uri uri) {
		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}

			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {
				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 * 
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @param selection
	 *            (Optional) Filter used in the query.
	 * @param selectionArgs
	 *            (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	private static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	private static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	private static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	private static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	private static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}

}
