package com.app.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class FileDownloader extends AsyncTask<String, Integer, File> {

	public static final String DOWNLOAD_DIR = "/cloudStore";//保存的文件夹
	private String filePath;

	public FileDownloader() {
	}
	public FileDownloader(OnDownloadListener onDownloadListener) {
		this.onDownloadListener=onDownloadListener;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		String rootDir = Environment.getExternalStorageDirectory().getAbsolutePath();
		filePath = rootDir + DOWNLOAD_DIR;
		File newDir = new File(filePath);
		if (!newDir.exists()) {
			newDir.mkdirs();
		}
		
	}

	@Override
	protected File doInBackground(String... args) {
		File file=null;
		try {
			String fileURL=args[0];
			URL url = new URL(fileURL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(false);
			connection.setDoInput(true);
			connection.setConnectTimeout(30 * 1000);
			if(connection.getResponseCode()!=200){
				Log.i("tag", "下载失败,返回码："+connection.getResponseCode());
				return null;
			}
			String fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1);
			file = new File(filePath, fileName);
			FileOutputStream fos = new FileOutputStream(file);
			InputStream in = connection.getInputStream();
			int fileSize = connection.getContentLength();
			if(fileSize==-1){
				fos.close();
				return null;
			}
			byte[] buffer = new byte[1024];
			int lenth = 0;
			long current = 0;
			while ((lenth = in.read(buffer)) > 0) {
				fos.write(buffer, 0, lenth);
				current += lenth;
				int progress = (int) (current * 100 / fileSize );
				publishProgress(progress);
			}
			fos.flush();
			in.close();
			fos.close();
			connection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
		}
		return file;
	}

	protected void onProgressUpdate(Integer... progress) {
		if(onDownloadListener!=null){
			onDownloadListener.onDownloading(progress[0]);
		}
	}

	@Override
	protected void onPostExecute(File file) {
		if(onDownloadListener!=null&&file!=null){
			onDownloadListener.onDownloaded(file);
		}
	}

	private OnDownloadListener onDownloadListener;
	
	public void setOnDownloadListener(OnDownloadListener onDownloadListener) {
		this.onDownloadListener = onDownloadListener;
	}

	public interface OnDownloadListener{
		void onDownloading(int progress);
		void onDownloaded(File file);
	}
	
}
