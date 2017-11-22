package com.mywaytec.myway.utils;

import com.mywaytec.myway.APP;
import com.mywaytec.myway.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 类名称：TimeUtil.java <br>
 * 内容摘要： // 时间相关工具类。<br>
 * 属性描述：<br>
 * 方法描述：<br>
 * @author shemh<br>
 */
public class TimeUtil {

	/**
	 * 得到时间戳
	 * @return
	 */
	public static long getTime(){
		return System.currentTimeMillis();
	}

	public static String getYMDHMSTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = format.format(new Date());
		return str;
	}

	public static String toYMDHMSTime(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = format.format(time);
		return str;
	}

	public static String YMDHMStoYMDTime(String time) {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = "";
		try {
			Date date = format1.parse(time);
			SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
			str = format2.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str;
	}

	public static String YMDHMStoMDHMTime(String time) {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = "";
		try {
			Date date = format1.parse(time);
			SimpleDateFormat format2 = new SimpleDateFormat("MM-dd HH:mm");
			str = format2.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str;
	}

	public static String  getHMSTime()
	{
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		String str = format.format(new Date());
		return str;

	}


	public  static String getYMDTime()
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String str = format.format(new Date());
		return str;
	}
	/**
	 * 方法名：  formatFromNoformt	<br>
	 * 方法描述：返回时间差时间<br>
	 * 修改备注：<br>
	 * 创建时间： 2016-4-18上午10:54:52<br>
	 * @return
	 */
	public static String getTimeDelay(String timeStr){
//		Date date = new Date(time);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String timeStr = format.format(date);
		try
		{
			String str = format.format(new Date());
			Date d1 = format.parse(str);
			Date d2 = format.parse(timeStr);
			long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
			long days = diff / (1000 * 60 * 60 * 24);
			long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
			long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
			if (days==0.0&&hours==0.0&&minutes==0.0) {
				return APP.getInstance().getResources().getString(R.string.just_now);
			}
			if (days==0.0&&hours==0.0&&minutes>0.0) {
				return minutes+" "+APP.getInstance().getResources().getString(R.string.minute_ago);
			}
			if (days==0.0&&hours>0.0) {
				return hours+" "+APP.getInstance().getResources().getString(R.string.hour_ago);
			}
			if (1.0<=days&&days<365){
//				return days+"天前 "+time.substring(11,19);
				return days+" "+APP.getInstance().getResources().getString(R.string.day_ago);
			}
			if (days>365) {
				return days+" "+APP.getInstance().getResources().getString(R.string.year_ago);
			}
		}catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		return timeStr;

	}

	/**
	 * 返回日差
	 * @param time
	 * @return
	 */
	public static long getSentDays(String time){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try
		{
			Date d1 = df.parse(TimeUtil.getYMDHMSTime());
			Date d2 = df.parse(time);
			long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
			long days = diff / (1000 * 60 * 60 * 24);
//			long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
//			long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
			return days;
		}catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		return 0;
	}

}
