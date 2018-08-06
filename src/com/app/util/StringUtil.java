package com.app.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StringUtil {

	/**
	 * 将时间转换为时间戳
	 * @param time
	 * @param format
	 * @return
	 */
	public static long timeToStamp(String time,String format) {
		SimpleDateFormat df = new SimpleDateFormat(format,Locale.CHINA);
        Date date=null;
		try {
			date = df.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return date.getTime();
	}
	/**
	 * 将时间转换为时间戳
	 * @param time
	 * @return
	 */
	public static long timeToStamp(String time) {
		return timeToStamp(time,"yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 当天开始时间
	 * @return
	 */
	public static long getTodayStartInMillis(){
		Calendar currentDate = Calendar.getInstance(Locale.CHINA);
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		return currentDate.getTimeInMillis();
	}
	/**
	 * 当天结束时间
	 * @return
	 */
	public static long getTodayEndInMillis(){
		Calendar currentDate = Calendar.getInstance(Locale.CHINA);
		currentDate.set(Calendar.HOUR_OF_DAY, 23);  
		currentDate.set(Calendar.MINUTE, 59);  
		currentDate.set(Calendar.SECOND, 59);  
		return currentDate.getTimeInMillis();
	}
	/**
	 * 昨天开始时间
	 * @return
	 */
	public static long getYesterdayStartInMillis(){
		Calendar currentDate = Calendar.getInstance(Locale.CHINA);
		currentDate.add(Calendar.DATE,-1);
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		return currentDate.getTimeInMillis();
	}
	/**
	 * 昨天结束时间
	 * @return
	 */
	public static long getYesterdayEndInMillis(){
		Calendar currentDate = Calendar.getInstance(Locale.CHINA);
		currentDate.add(Calendar.DATE,-1);
		currentDate.set(Calendar.HOUR_OF_DAY, 23);
		currentDate.set(Calendar.MINUTE, 59);
		currentDate.set(Calendar.SECOND, 59);
		return currentDate.getTimeInMillis();
	}
	/**
	 * 当周开始时间
	 * 
	 * @return
	 */
	public static long getWeekStartInMillis() {
		Calendar currentDate = Calendar.getInstance(Locale.CHINA);
		currentDate.setFirstDayOfWeek(Calendar.MONDAY);
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		currentDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return currentDate.getTimeInMillis();
	}

	/**
	 * 当周结束时间
	 * 
	 * @return
	 */
	public static long getWeekEndInMillis() {
		Calendar currentDate = Calendar.getInstance(Locale.CHINA);
		currentDate.setFirstDayOfWeek(Calendar.MONDAY);
		currentDate.set(Calendar.HOUR_OF_DAY, 23);
		currentDate.set(Calendar.MINUTE, 59);
		currentDate.set(Calendar.SECOND, 59);
		currentDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return currentDate.getTimeInMillis();
	}
	
}
