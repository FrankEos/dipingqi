/*
 * 创建日期 2005-9-22
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package org.harvest.crawler.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Administrator 这个类可以使用不同的方法得到不同精度的系统时间。 TODO 要更改此生成的类型注释的模板，请转至 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class RealTime {
	// 得到当前时间的Calendar形式
	public static Calendar calGetCalendarTime() {
		Calendar caltime = Calendar.getInstance();
		return caltime;
	}

	// 得到程序中标准的全显示时间
	public static String strGetAllTimeToSecond() {
		String dttime = null;
		Calendar temptime = RealTime.calGetCalendarTime();
		dttime = String.valueOf(temptime.get(Calendar.YEAR));
		dttime += "-" + LenMore1(String.valueOf(temptime.get(Calendar.MONTH) + 1));
		dttime += "-" + LenMore1(String.valueOf(temptime.get(Calendar.DAY_OF_MONTH)));
		dttime += " " + LenMore1(String.valueOf(temptime.get(Calendar.HOUR_OF_DAY)));
		dttime += ":" + LenMore1(String.valueOf(temptime.get(Calendar.MINUTE)));
		dttime += ":" + LenMore1(String.valueOf(temptime.get(Calendar.SECOND)));
		return dttime;
	}

	// 根据日期字符串得到年，返回数字
	public static int intGetYear(String tempStr) {
		int dttime = 0;
		dttime = Integer.parseInt(tempStr.substring(0, 4));
		return dttime;
	}

	// 根据日期字符串得到月，返回数字
	public static int intGetMonth(String tempStr) {
		int dttime = 0;
		dttime = Integer.parseInt(tempStr.substring(5, 7));
		return dttime;
	}

	// 根据日期字符串得到日，返回数字
	public static int intGetDay(String tempStr) {
		int dttime = 0;
		dttime = Integer.parseInt(tempStr.substring(8, 10));
		return dttime;
	}

	// 根据日期字符串得到年，返回数字
	public static String strGetYear(String tempStr) {
		String dttime = "";
		dttime = tempStr.substring(0, 4);
		return dttime;
	}

	// 根据日期字符串得到月，返回字符串
	public static String strGetMonth(String tempStr) {
		String dttime = "";
		dttime = tempStr.substring(5, 7);
		return dttime;
	}

	// 根据日期字符串得到日，返回字符串
	public static String strGetDay(String tempStr) {
		String dttime = "";
		dttime = tempStr.substring(8, 10);
		return dttime;
	}

	// 得到程序中标准时间到分
	public static String strGetTimeToMinute() {
		String dttime = null;
		Calendar temptime = RealTime.calGetCalendarTime();
		dttime = String.valueOf(temptime.get(Calendar.YEAR));
		dttime += "年" + LenMore1(String.valueOf(temptime.get(Calendar.MONTH) + 1));
		dttime += "月" + LenMore1(String.valueOf(temptime.get(Calendar.DAY_OF_MONTH)));
		dttime += "日 " + LenMore1(String.valueOf(temptime.get(Calendar.HOUR_OF_DAY)));
		dttime += ":" + LenMore1(String.valueOf(temptime.get(Calendar.MINUTE)));
		return dttime;
	}

	// 得到程序中标准时间到秒
	public static String strGetTimeToSecond() {
		String time = "";
		Date dNow = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
		time = formatter.format(dNow);
		return time;
	}

	// 得到程序中标准时间
	public static String strGetTime() {
		String time = "";
		Date dNow = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		time = formatter.format(dNow);
		return time;
	}

	public static String strGetTimes() {
		String time = "";
		Date dNow = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		time = formatter.format(dNow);
		return time;
	}

	// 得到程序中标准时间到月
	public static String strGetTimeToMonth() {
		/**
		 * 生成时间类型如：2005年3月
		 */
		String dttime = null;
		Calendar temptime = RealTime.calGetCalendarTime();
		dttime = String.valueOf(temptime.get(Calendar.YEAR)) + "年";
		dttime += String.valueOf(temptime.get(Calendar.MONTH) + 1) + "月";
		return dttime;
	}

	// 得到程序中标准时间到小时
	public static String strGetTimeToHour() {
		String dttime = null;
		Calendar temptime = RealTime.calGetCalendarTime();
		dttime = String.valueOf(temptime.get(Calendar.YEAR));
		dttime += "-" + LenMore1(String.valueOf(temptime.get(Calendar.MONTH) + 1));
		dttime += "-" + LenMore1(String.valueOf(temptime.get(Calendar.DAY_OF_MONTH)));
		// dttime+="
		// "+LenMore1(String.valueOf(temptime.get(Calendar.HOUR_OF_DAY)));
		return dttime;
	}

	// 得到程序中标准时间到天
	public static String strGetTimeToDay() {
		String dttime = null;
		Calendar temptime = RealTime.calGetCalendarTime();
		dttime = String.valueOf(temptime.get(Calendar.YEAR));
		dttime += "-" + LenMore1(String.valueOf(temptime.get(Calendar.MONTH) + 1));
		dttime += "-" + LenMore1(String.valueOf(temptime.get(Calendar.DAY_OF_MONTH)));
		return dttime;
	}

	// 如果数字长度小于2位则在前面补"0"
	public static String LenMore1(String param1) {
		if (param1.length() < 2) {
			param1 = "0" + param1;
		}
		return param1;
	}

	/**
	 * 得到本月的月底日期
	 * 
	 * @param 2006-9-1
	 * @return 2006-9-30
	 */
	public static String getMonthEndDate(String pDate) {
		String[] arr = pDate.split("-");

		Calendar nowTime = Calendar.getInstance();
		nowTime.set(Calendar.YEAR, Integer.parseInt(arr[0]));
		nowTime.set(Calendar.MONTH, Integer.parseInt(arr[1]) - 1);
		nowTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(arr[2]));
		arr[2] = String.valueOf(nowTime.getActualMaximum(Calendar.DAY_OF_MONTH));
		// System.out.println(nowTime.getActualMaximum(Calendar.DAY_OF_MONTH));
		return arr[0] + "-" + arr[1] + "-" + arr[2];
	}

	/**
	 * 得到前一个月的月底
	 * 
	 * @param 2006-9-1
	 * @return 2006-9-30
	 */
	public static String getbeforeMonthEndDate(String pDate) {
		String[] arr = pDate.split("-");
		int mm = Integer.parseInt(arr[1]) - 1;
		String yy = String.valueOf(Integer.parseInt(arr[0]) - 1);
		if (mm <= 0) {
			arr[0] = yy;
			arr[1] = "12";
			arr[2] = "30";
			return arr[0] + "-" + arr[1] + "-" + arr[2];
		} else {
			Calendar nowTime = Calendar.getInstance();
			nowTime.set(Calendar.YEAR, Integer.parseInt(arr[0]));
			nowTime.set(Calendar.MONTH, Integer.parseInt(arr[1]));
			nowTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(arr[2]));
			arr[2] = String.valueOf(nowTime.getActualMaximum(Calendar.DAY_OF_MONTH));
			// System.out.println(nowTime.getActualMaximum(Calendar.DAY_OF_MONTH));
			return arr[0] + "-" + mm + "-" + arr[2];
		}
	}

	/**
	 * 得到前一个月的开始
	 * 
	 * @param 2006-9-1
	 * @return 2006-9-30
	 */
	public static String getbeforeMonthStartDate(String pDate) {
		String[] arr = pDate.split("-");
		int mm = Integer.parseInt(arr[1]) - 1;
		String yy = String.valueOf(Integer.parseInt(arr[0]) - 1);
		if (mm <= 0) {
			arr[0] = yy;
			arr[1] = "12";
			arr[2] = "30";
			return arr[0] + "-" + arr[1] + "-" + arr[2];
		} else {
			Calendar nowTime = Calendar.getInstance();
			nowTime.set(Calendar.YEAR, Integer.parseInt(arr[0]));
			nowTime.set(Calendar.MONTH, Integer.parseInt(arr[1]));
			nowTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(arr[2]));
			arr[2] = "1";
			// System.out.println(nowTime.getActualMaximum(Calendar.DAY_OF_MONTH));
			return arr[0] + "-" + mm + "-" + arr[2];
		}
	}

	/**
	 * 得到本月的开始日期
	 * 
	 * @param 2006-9-1
	 * @return 2006-9-30
	 */
	public static String getMonthStartDate(String pDate) {
		String[] arr = pDate.split("-");
		Calendar nowTime = Calendar.getInstance();
		nowTime.set(Calendar.YEAR, Integer.parseInt(arr[0]));
		nowTime.set(Calendar.MONTH, Integer.parseInt(arr[1]) - 1);
		nowTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(arr[2]));
		arr[2] = "1";
		return arr[0] + "-" + arr[1] + "-" + arr[2];
	}

	/**
	 * 得到前一天的日期
	 * 
	 * @param 2006-9-1
	 * @return 2006-9-30
	 */
	public static String getbeforeDate(String pDate) {
		String[] arr = pDate.split("-");
		Calendar nowTime = Calendar.getInstance();
		nowTime.set(Calendar.YEAR, Integer.parseInt(arr[0]));
		nowTime.set(Calendar.MONTH, Integer.parseInt(arr[1]) - 1);
		arr[2] = String.valueOf(Integer.parseInt(arr[2]) - 1);
		return arr[0] + "-" + arr[1] + "-" + arr[2];
	}

	/**
	 * 得到当前时间,以秒的形式表示
	 * 
	 * @return 1157958545468
	 */
	public static int getNowDate() {
		long time = System.currentTimeMillis() / 1000;
		return (int) time;
	}

	/**
	 * 把秒转换成小时+分钟+秒的形式
	 * 
	 * @return 小时+分钟+秒
	 */
	public static String SecondToHms(long nSecond) {
		long hour = nSecond / 3600;
		long mintues = (nSecond % 3600) / 60;
		long second = (nSecond % 3600) % 60;
		return hour + "小时" + mintues + "分钟" + second + "秒";
	}

	/**
	 * 把秒转换成小时+分钟的形式
	 * 
	 * @return 小时+分钟
	 */
	public static String SecondToHm(long nSecond) {
		long hour = nSecond / 3600;
		long mintues = (nSecond % 3600) / 60;
		return hour + "小时" + mintues + "分钟";
	}

	/**
	 * 得到当天的0时0分0秒的 时间
	 * 
	 * @return long
	 */
	public static long getTodayBeginTime() {
		Calendar nowTime = Calendar.getInstance();
		nowTime.set(Calendar.HOUR_OF_DAY, 0);
		nowTime.set(Calendar.MINUTE, 0);
		nowTime.set(Calendar.SECOND, 0);
		return nowTime.getTimeInMillis() / 1000;
	}
	
	public static int getHourByTime(Long time){
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		return c.get(Calendar.HOUR_OF_DAY);
	}

	public static void main(String[] args) {

		System.out.println("dttime===" + RealTime.getNowDate());
		System.out.println(strGetTimeToHour());
		
		System.out.println(getHourByTime(System.currentTimeMillis()));
	}
}
