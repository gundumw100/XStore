package com.app.util;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * 根据文件类型打开文件
 * 
 * @author pyhoner
 *
 */
public final class FileUtil {

	static Map<String, String> map =null ;
	static {
		map = new HashMap<String, String>();
		map.put(".3gp", "video/3gpp");
		map.put(".apk", "application/vnd.android.package-archive");
		map.put(".asf", "video/x-ms-asf");
		map.put(".avi", "video/x-msvideo");
		map.put(".bin", "application/octet-stream");
		map.put(".bmp", "image/bmp");
		map.put(".c", "text/plain");
		map.put(".class", "application/octet-stream");
		map.put(".conf", "text/plain");
		map.put(".cpp", "text/plain");
		map.put(".doc", "application/msword");
		map.put(".docx","application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		map.put(".xls", "application/vnd.ms-excel");
		map.put(".xlsx","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		map.put(".exe", "application/octet-stream");
		map.put(".gif", "image/gif");
		map.put(".gtar", "application/x-gtar");
		map.put(".gz", "application/x-gzip");
		map.put(".h", "text/plain");
		map.put(".htm", "text/html");
		map.put(".html", "text/html");
		map.put(".jar", "application/java-archive");
		map.put(".java", "text/plain");
		map.put(".jpeg", "image/jpeg");
		map.put(".jpg", "image/jpeg");
		map.put(".js", "application/x-javascript");
		map.put(".log", "text/plain");
		map.put(".m3u", "audio/x-mpegurl");
		map.put(".m4a", "audio/mp4a-latm");
		map.put(".m4b", "audio/mp4a-latm");
		map.put(".m4p", "audio/mp4a-latm");
		map.put(".m4u", "video/vnd.mpegurl");
		map.put(".m4v", "video/x-m4v");
		map.put(".mov", "video/quicktime");
		map.put(".mp2", "audio/x-mpeg");
		map.put(".mp3", "audio/x-mpeg");
		map.put(".mp4", "video/mp4");
		map.put(".mpc", "application/vnd.mpohun.certificate");
		map.put(".mpe", "video/mpeg");
		map.put(".mpeg", "video/mpeg");
		map.put(".mpg", "video/mpeg");
		map.put(".mpg4", "video/mp4");
		map.put(".mpga", "audio/mpeg");
		map.put(".msg", "application/vnd.ms-outlook");
		map.put(".ogg", "audio/ogg");
		map.put(".pdf", "application/pdf");
		map.put(".png", "image/png");
		map.put(".pps", "application/vnd.ms-powerpoint");
		map.put(".ppt", "application/vnd.ms-powerpoint");
		map.put(".pptx","application/vnd.openxmlformats-officedocument.presentationml.presentation");
		map.put(".prop", "text/plain");
		map.put(".rc", "text/plain");
		map.put(".rmvb", "audio/x-pn-realaudio");
		map.put(".rtf", "application/rtf");
		map.put(".sh", "text/plain");
		map.put(".tar", "application/x-tar");
		map.put(".tgz", "application/x-compressed");
		map.put(".txt", "text/plain");
		map.put(".wav", "audio/x-wav");
		map.put(".wma", "audio/x-ms-wma");
		map.put(".wmv", "audio/x-ms-wmv");
		map.put(".wps", "application/vnd.ms-works");
		map.put(".xml", "text/plain");
		map.put(".z", "application/x-compress");
		map.put(".zip", "application/x-zip-compressed");
		map.put("", "*/*");
	}
	
	/**
	 * 根据文件后缀名获得对应的MIME类型。
	 * @param file
	 */ 
	public static String getMIMEType(File file) { 
	    String type="*/*"; 
	    String fileName = file.getName(); 
	    int dotIndex = fileName.lastIndexOf("."); 
	    if(dotIndex < 0){ 
	        return type; 
	    } 
	    String ext=fileName.substring(dotIndex,fileName.length()).toLowerCase(Locale.ENGLISH); 
	    if(TextUtils.isEmpty(ext)){
	    	return type; 
	    }
	    return map.get(ext); 
	} 
	
	/**
	 * 使用默认Activity的打开文件
	 * 如果没有支持的文件则给出提示
	 * 
	 * @param context
	 * @param file
	 */
	public static void openFile(Context context,File file){
		try{
	        Intent intent = new Intent(Intent.ACTION_VIEW);
	        intent.addCategory("android.intent.category.DEFAULT");
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        intent.setDataAndType(Uri.fromFile(file), getMIMEType(file));
	        context.startActivity(intent);
		}catch(Exception e){
//			e.printStackTrace();
			Toast.makeText(context, "文件格式不支持", Toast.LENGTH_SHORT).show();
		}
	}
	
}
