package com.app.util;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 文件上传类
 * 
 * @author Ni Guijun
 *
 */
public class FileUploader {

	String userId;
	String filePath;
	String fileName;
	String ext;
	String urlString;
	public FileUploader(String urlString,String userId,String filePath, String fileName,String ext){
		this.userId=userId;
		this.urlString=urlString;
		this.filePath=filePath;
		this.fileName=fileName;
		this.ext=ext;
	}
	
	public void uploadFile() {
		TimerTask task=new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				uploadFile(urlString,userId,filePath,fileName,ext);
			}
		};
		Timer t=new Timer();
		t.schedule(task, 5);
	}
	
	/**
	 * 上传文件至Server
	 * @param photo
	 * @param filePath 文件路径
	 * @param urlString 连接地址
	 */
	private void uploadFile(String urlString,String userId,String filePath, String fileName,String ext) {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		fileName=toUnicode(fileName);
		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Charset", "UTF-8");
			connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary +";"+ext+";"+userId+";"+fileName);
			
			DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
//			dos.writeBytes(twoHyphens + boundary + end);
//			dos.writeBytes(
//					"Content-Disposition: form-data; " + "name=\"file1\";filename=\"" + IMAGE_FILE_NAME + "\"" + end);
//			dos.writeBytes(end);
			
			FileInputStream fis = new FileInputStream(filePath);
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int lenth = -1;
			long current = 0;
			long fileSize=Util.getFileSize(filePath);
			/* 从文件读取数据至缓冲区 */
			while ((lenth = fis.read(buffer)) != -1) {
				dos.write(buffer, 0, lenth);
				current += lenth;
				int progress = (int) (current * 100 / fileSize );
				sendMessage(ST_UPLOADING, progress);
			}
//			dos.writeBytes(end);
//			dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
			fis.close();
			dos.flush();
			dos.close();
			
//			int responseCode = connection.getResponseCode(); 
			
			/* 取得Response内容 */
			InputStream is = connection.getInputStream();
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			is.close();
			connection.disconnect();
			
			JSONObject response=null;
			response = new JSONObject(b.toString());
//			Log.i("tag", "b.toString===="+b.toString());
			DataWrap obj = new DataWrap(response);
			sendMessage(ST_UPLOAD_SUCCESS, obj);
		} catch (Exception e) {
			e.printStackTrace();
			DataWrap obj = new DataWrap(null);
			sendMessage(ST_UPLOAD_FAIL, obj);
		}

	}
	
	private void sendMessage(int st,Object obj){
    	Message message = new Message();      
        message.what = st;     
        message.obj=obj;
        handler.sendMessage(message);
    }
	
	private final int ST_UPLOAD_SUCCESS=0;
	private final int ST_UPLOAD_FAIL=1;
	private final int ST_UPLOADING=2;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ST_UPLOAD_SUCCESS:
				if (onUploadListener != null) {
					DataWrap datas = (DataWrap) msg.obj;
					onUploadListener.onUploadSuccess(filePath, datas.response);
				}
				break;
			case ST_UPLOAD_FAIL:
				if (onUploadListener != null) {
					// DataWrap datas=(DataWrap)msg.obj;
					onUploadListener.onUploadFailed(filePath);
				}
				break;
			case ST_UPLOADING:
				if (onUploadListener != null) {
					onUploadListener.onUploading((Integer)msg.obj);
				}
				break;
			default:
				break;
			}

		}

	};
	
    class DataWrap{
    	JSONObject response;
    	DataWrap(JSONObject response){
    		this.response=response;
    	}
    }
    
    //中文转Unicode
    private String toUnicode(String inStr) {
		StringBuffer unicode = new StringBuffer();
		char c;
		int bit;
		String tmp = null;
		for (int i = 0; i < inStr.length(); i++) {
			c = inStr.charAt(i);
			if (c > 255) {
				unicode.append("\\u");
				bit = (c >>> 8);
				tmp = Integer.toHexString(bit);
				if (tmp.length() == 1)
					unicode.append("0");
				unicode.append(tmp);
				bit = (c & 0xFF);
				tmp = Integer.toHexString(bit);
				if (tmp.length() == 1)
					unicode.append("0");
				unicode.append(tmp);
			} else {
				unicode.append(c);
			}
		}
		return (new String(unicode));
	}

    private OnUploadListener onUploadListener;

	public void setOnUploadListener(
			OnUploadListener onUploadListener) {
		this.onUploadListener = onUploadListener;
	}

	public interface OnUploadListener {
		void onUploadSuccess(String filePath, JSONObject response);
		void onUploadFailed(String filePath);
		void onUploading(int progress);
	}
	
}
