package com.app.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Images.ImageColumns;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

/**
 * 
 * @author Ni Guijun
 * 
 */
public class Util {

	/**
	 * 将文件转化成字节
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray(String filename) throws IOException {
		File f = new File(filename);
		if (!f.exists()) {
			throw new FileNotFoundException(filename);
		}

		ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(f));
			int buf_size = 1024;
			byte[] buffer = new byte[buf_size];
			int len = 0;
			while (-1 != (len = in.read(buffer, 0, buf_size))) {
				bos.write(buffer, 0, len);
			}
			return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			bos.close();
		}
	}

	// 将 s 进行 BASE64 编码
	public static String getBASE64(byte[] s) {
		if (s == null)
			return null;
		return (new BASE64Encoder()).encode(s); // s.getBytes()
	}

	// 将 BASE64 编码的字符串 s 进行解码
	public static String getFromBASE64(String s) {
		if (s == null)
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(s);
			return new String(b);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获得文件扩展名
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getExtName(String fileName) {
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	}
	public static String getFileName(String fileName) {
		return fileName.substring(fileName.lastIndexOf("/") + 1);
	}
	public static String fileToString(String filePath) {
		try {
			return getBASE64(toByteArray(filePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获得文件大小
	 * @param filePath
	 * @return
	 */
	public static long getFileSize(String filePath) {
		File f = new File(filePath);
		if (f.exists() && f.isFile()) {
			return f.length();
		} else {
			return 0;
		}
	}
	
	/**
	 * Try to return the absolute file path from the given Uri
	 * 
	 * @param context
	 * @param uri
	 * @return
	 */
	public static String getFilePathByUri(final Context context, final Uri uri){
		if ( null == uri ) return null;
	    final String scheme = uri.getScheme();
	    String data = null;
	    if ( scheme == null )
	        data = uri.getPath();
	    else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
	        data = uri.getPath();
	    } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
	        Cursor cursor = context.getContentResolver().query( uri, new String[] { ImageColumns.DATA }, null, null, null );
	        if ( null != cursor ) {
	            if ( cursor.moveToFirst() ) {
	                int index = cursor.getColumnIndex( ImageColumns.DATA );
	                if ( index > -1 ) {
	                    data = cursor.getString( index );
	                }
	            }
	            cursor.close();
	        }
	    }
	    return data;
	}
}
