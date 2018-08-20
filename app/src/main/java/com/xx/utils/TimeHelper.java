package com.xx.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeHelper {
	public  static final String RECENTLY = "recently";
	
	/**时间，long转换为String*/
	public static String timeLongToString(long timeLong){
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(timeLong);
		String stime = sdf.format(date);
		//System.out.println(str);
		return stime;
	}
	/**
	 * 时间,String转换为long
	 * */
	public static long timeStringToLong(String stime) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		date = sdf.parse(stime);
		return date.getTime();
}

	/**
	 * 将时间转为日常使用的形式
	 * @param timeLast
	 * @return String
	 */
	public static String longToLocalTime(long timeLast){
		StringBuilder resultBuilder = new StringBuilder();
		Date dateLast = new Date(timeLast);
		Date dateNow = new Date();

		if(dateLast.getYear() != dateNow.getYear() ){
			return timeLongToString(timeLast);
		}else if(dateLast.getMonth() != dateNow.getMonth()){
			SimpleDateFormat sdf= new SimpleDateFormat("MM月dd HH:mm");
			Date date = new Date(timeLast);
			String stime = sdf.format(date);
			return stime;
		}else{
			int dayLast = dateLast.getDay();
			int dayNow = dateNow.getDay();
			if(dayLast == dayNow){
				SimpleDateFormat sdf= new SimpleDateFormat("HH:mm");
				Date date = new Date(timeLast);
				String stime = sdf.format(date);
				return stime;
			}else if ((dayNow - dayLast) == 1){
				resultBuilder.append("昨天:").
						append(dateLast.getHours()).
						append(dateLast.getMinutes());
				return resultBuilder.toString();
			}else if ((dayNow - dayLast) == 2){
				resultBuilder.append("前天:").
						append(dateLast.getHours()).
						append(dateLast.getMinutes());
				return resultBuilder.toString();
			}else{
				SimpleDateFormat sdf= new SimpleDateFormat("MM月dd HH:mm");
				Date date = new Date(timeLast);
				String stime = sdf.format(date);
				return stime;
			}
		}

	}
	public static boolean isBelow2Minutes(long lastTime,long thisTime){
		Date dateLast = new Date(lastTime);
		Date dateThis = new Date(thisTime);
		if(dateLast.getYear() == dateThis.getYear() &&
				dateLast.getMonth() == dateThis.getMonth() &&
				dateLast.getDay() == dateThis.getDay() &&
				dateLast.getHours() == dateThis.getHours() &&
				(dateThis.getMinutes() - dateLast.getMinutes())<=2)
			return true;
		return false;
	}
	public static Long getTimeNow(){
		Date date = new Date();
		return date.getTime();

	}
}
