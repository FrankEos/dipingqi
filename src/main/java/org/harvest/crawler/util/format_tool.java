package org.harvest.crawler.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;

/**
 * 格式转换工具
 */
public class format_tool {

	/**
	 * IP 地址转换，整型-->字符串 失败返回空字符串
	 */
	public static String toIpString(int ip) {
		StringBuffer sbIP = new StringBuffer();

		int[] iTmp = new int[4];
		for (int i = 3; i >= 0; i--) {
			iTmp[i] = ip & 0xff;

			if (i != 3)
				sbIP.insert(0, '.');
			sbIP.insert(0, iTmp[i]);
			ip = ip >> 8;
		}
		return sbIP.toString();
	}// end of toIpString()

	/**
	 * IP 地址转换，字符串-->整型 失败返回 0
	 */
	public static int toIpInteger(String ip) {
		int nRet = 0, nItem = 0, nTempRet = 0;
		if (ip == null)
			return nRet;

		int nStart = 0, nEnd = 0;
		for (int i = 0; i < 4; i++) {
			try {
				if (i < 3) // 取前三个域
					nEnd = ip.indexOf('.', nStart);
				else
					// 取第四个（最后一个域）
					nEnd = ip.length();
				if (nEnd > nStart) {
					int nTempNum = Integer.parseInt(ip.substring(nStart, nEnd));
					nTempRet = ((nTempRet << 8) + nTempNum); // WAIT TO FIT !!!
					nStart = nEnd + 1;
					nItem++;
				} else
					break;
			} catch (Exception e) {
				break; /* DISCARD EXCEPTION */
			}
		}

		if (nItem == 4) // 成功处理IP地址，四个域
			nRet = nTempRet;
		return nRet;
	}// end of toIpInteger()

	/**
	 * 转换字符串格式，使之适合数据库存储 WAIT TO FIT !!!!
	 * 
	 * @param input
	 * @return
	 */
	public static String toDbString(String input) {
		StringBuffer sbTemp = new StringBuffer();

		char cChar = 0x00;
		for (int i = 0; i < input.length(); i++) {
			cChar = input.charAt(i);
			if (cChar == '\'') // ???
				sbTemp.append("'");
			else
				sbTemp.append(cChar);
		}
		return sbTemp.toString();
	}// end of toDbString()

	/**
	 * 取时间中的月、日信息，组成short数值，“月*100+日”
	 * 
	 * @param time
	 *            需要转换的时间，单位：秒
	 * @return 如 1002 表示 10月2日
	 */
	public static short getShortDate(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time * 1000);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		return ((short) (month * 100 + day));
	}

	/**
	 * 取时间中的月、日信息，组成short数值，“(年-2000)*10000+月*100+日”
	 * 
	 * @param time
	 *            需要转换的时间，单位：秒
	 * @return 如 1002 表示 10月2日
	 */
	public static int getIntDate(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time * 1000);
		int year = calendar.get(Calendar.YEAR) - 2000; // 4 表示 2004 年
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		return ((int) (year * 10000 + month * 100 + day));
	}

	/**
	 * 取时间中的时、分信息，组成short数值，“时*100+分”
	 * 
	 * @param time
	 *            需要转换的时间，单位：秒
	 * @return 如 2130 表示 21点30分
	 */
	public static short getShortTime(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time * 1000);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		return ((short) (hour * 100 + minute));
	}

	/**
	 * 取时间中的时、分、秒信息，组成short数值，“时*10000+分*100+秒”
	 * 
	 * @param time
	 *            需要转换的时间，单位：秒
	 * @return 如 213055 表示 21点30分55秒
	 */
	public static int getIntTime(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time * 1000);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		return ((int) (hour * 10000 + minute * 100 + second));
	}

	/**
	 * 处理字符串尾部，截取多于部分，用'..'补充
	 * 
	 * @param input
	 *            输入字符串
	 * @param len
	 *            中文字的个数（英文字可显示两倍数量）
	 * @return 处理后的字符串
	 */
	public static String dealTail(String input, int len) {
		int nAllowByte = len * 2;
		if (nAllowByte < 2)
			nAllowByte = 2;

		String sRet = input;
		byte[] byteInput = input.getBytes();
		int nByteNum = byteInput.length;
		if (nByteNum > nAllowByte) {
			sRet = new String(byteInput, 0, nAllowByte);
			if (sRet.equals("")) // 如果截断了某个多字节字符，生成的String为空串 WAIT TO FIT !!
				sRet = new String(byteInput, 0, nAllowByte - 1);
			sRet += "..";
		}
		return sRet;
	}

	/**
	 * 将金额从单位“分”转换为符合习惯的显示方式
	 * 
	 * @param cent
	 *            分
	 * @return 50 --> 0.50， 300 --> 3
	 */
	public static String toShowMoney(int cent) {
		String sRet = "0.00";
		float dMoney = (float) cent / 100;
		DecimalFormat form = new DecimalFormat("#0.00");
		try {
			sRet = form.format(dMoney);
		} catch (Exception e) {/* DISCARD EXCEPTION */
		}
		return sRet;
	}

	/**
	 * 将时间转换为显示字符串,“简短”格式 2004-8-16 12:8:1
	 * 
	 * @param second
	 *            相对于 1970年1月1日零时的秒数
	 * @return
	 */
	public static String toTimeString(long second) {
		StringBuffer sbRet = new StringBuffer();
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(second * 1000);

			sbRet.append(calendar.get(Calendar.YEAR)).append("-");
			sbRet.append(calendar.get(Calendar.MONTH) + 1).append("-");
			sbRet.append(calendar.get(Calendar.DATE)).append(" ");
			sbRet.append(calendar.get(Calendar.HOUR_OF_DAY)).append(":");
			sbRet.append(calendar.get(Calendar.MINUTE)).append(":");
			sbRet.append(calendar.get(Calendar.SECOND));
		} catch (Exception e) {/* DISCARD EXEPTION */
		}
		return sbRet.toString();
	}

	/**
	 * 将时间转换为显示字符串，“全长”格式 2004-08-16 12:08:01
	 * 
	 * @param second
	 *            相对于 1970年1月1日零时的秒数
	 * @return
	 */
	public static String toTimeFullString(long second) {
		StringBuffer sbRet = new StringBuffer();
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(second * 1000);

			sbRet.append(calendar.get(Calendar.YEAR)).append("-");
			sbRet.append(integerToString((calendar.get(Calendar.MONTH) + 1), 2)).append("-");
			sbRet.append(integerToString(calendar.get(Calendar.DATE), 2)).append(" ");
			sbRet.append(integerToString(calendar.get(Calendar.HOUR_OF_DAY), 2)).append(":");
			sbRet.append(integerToString(calendar.get(Calendar.MINUTE), 2)).append(":");
			sbRet.append(integerToString(calendar.get(Calendar.SECOND), 2));
		} catch (Exception e) {/* DISCARD EXEPTION */
		}
		return sbRet.toString();
	}

	/**
	 * 将时间转换为显示字符串，“全长”格式 2004-08-16 12:08:01
	 * 
	 * @param second
	 *            相对于 1970年1月1日零时的秒数
	 * @return
	 */
	public static String toTimeFullString(long second, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(second * 1000);
		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat(format);
		dateFormat.format(calendar.getTime());
		return dateFormat.format(calendar.getTime());
	}

	/**
	 * 将时间字符串转化为秒值（相对 1970年１月１日）
	 * 
	 * @param time
	 *            时间字符串
	 * @param format
	 *            格式串，如　"yyyy-MM-dd-HH-mm", "yyyy-MM-dd HH:mm:ss"
	 * @return　　　失败返回　０
	 */
	public static int toTimeInteger(String time, String format) {
		int nRet = 0;
		try {
			java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat(format);
			Date de = dateFormat.parse(time);
			nRet = (int) (de.getTime() / 1000);
		} catch (Exception e) {/* DISCARD EXCEPTION */
		}
		return nRet;
	}

	/**
	 * 将时间字符串转化为秒值（相对 1970年１月１日）
	 * 
	 * @param time
	 *            时间字符串
	 * @param format
	 *            格式串，如　"yyyy-MM-dd-HH-mm", "yyyy-MM-dd HH:mm:ss"
	 * @return　　　失败返回　０
	 */
	public static long toTimeLong(String time, String format) {
		long nRet = 0;
		try {
			java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat(format);
			Date de = dateFormat.parse(time);
			nRet = (de.getTime() / 1000);
		} catch (Exception e) {/* DISCARD EXCEPTION */
		}
		return nRet;
	}

	/**
	 * 将时间转换为显示字符串 "19990412"
	 * 
	 * @param second
	 *            相对于 1970年1月1日零时的秒数
	 * @return
	 */
	public static String toDateString(long second) {
		StringBuffer sbRet = new StringBuffer();
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(second * 1000);

			sbRet.append(calendar.get(Calendar.YEAR));
			if (calendar.get(Calendar.MONTH) + 1 < 10)
				sbRet.append("0");
			sbRet.append(calendar.get(Calendar.MONTH) + 1);
			if (calendar.get(Calendar.DATE) < 10)
				sbRet.append("0");
			sbRet.append(calendar.get(Calendar.DATE));
		} catch (Exception e) {/* DISCARD EXEPTION */
		}
		return sbRet.toString();
	}

	/**
	 * 将输入的整数转换为指定长度的字符串，不足时首部用'0'填充, 整数超长时则直接输出
	 * 
	 * @param input_num
	 *            输入的整数
	 * @param output_length
	 *            输出字符串的长度
	 * @return 参数(32,4），输出：“0032”，参数(3390,3)，输出：“3390”
	 */
	public static String integerToString(int input_num, int output_length) {
		StringBuffer sbPrefix = new StringBuffer();
		String sTempInput = "" + input_num;

		if (sTempInput.length() < output_length) {
			for (int i = 0; i < output_length - sTempInput.length(); i++)
				sbPrefix.append('0');
		}

		return sbPrefix.append(sTempInput).toString();
	}

	/**
	 * 格式化字符串为指定长度，不足则增加空格，太长则截断，并用'..'填充
	 * 
	 * @param sInput
	 *            输入字符串
	 * @param nLen
	 *            需要输出的长度
	 * @param nFlag
	 *            标志，'..'或空格添加的位置，>0 在尾部，=0 在中部，<0 在首部
	 * @return
	 */
	public static String toFixLength(String sInput, int nLen, int nFlag) {
		String sRet = "";
		int nCurLen = sInput.length(), nTempLen = 0;

		if (nCurLen < nLen) {// 字符串太短，补充空格
			StringBuffer sbEmpty = new StringBuffer();
			for (int i = 0; i < nLen - nCurLen; i++)
				sbEmpty.append(" ");

			if (nFlag > 0)
				sRet = sInput + sbEmpty.toString();
			else
				sRet = sbEmpty.toString() + sInput;
		} else if (nCurLen > nLen) {// 字符串太长，截断
			if (nFlag > 0) {
				sRet = sInput.substring(0, nLen - 2) + "..";
			} else if (nFlag == 0) {
				nTempLen = nLen / 2;
				sRet = sInput.substring(0, nTempLen) + ".." + sInput.substring(nTempLen + nCurLen - nLen);
			} else {
				sRet = ".." + sInput.substring(nCurLen - nLen);
			}
		} else {
			sRet = sInput;
		}

		return sRet;
	}// end of toFixLength()

	/**
	 * 将时间字符串换算成相对于零点的秒数 "03:23:10" 三时二十三分十秒 "15:00:00" 十五时
	 */
	public static int getDayTime(String time) {
		int nRet = 0;
		try {
			String[] secTime = time.split(":");
			for (int i = 0; i < secTime.length; i++) {
				int nTemp = Integer.parseInt(secTime[i]); /* 时:分:秒 */
				for (int ii = 0; ii < 2 - i; ii++) {
					nTemp = nTemp * 60;
				}
				nRet += nTemp;
			}
		} catch (Exception e) {
			/* DISCARD EXCEPTION */
		}
		return nRet;
	}

	/**
	 * 将时间长度整数值转换成 时分秒 的可读字符串
	 * 
	 * @param ltime
	 * @return
	 */
	public static String getTimeString(long ltime) {
		return getDayTimeString_inner(ltime, true);
	}

	/**
	 * 根据时间戳,获取当前时分秒时间字符串,不包含日期信息
	 * 
	 * @param ltime
	 * @return
	 */
	public static String getDayTimeString(long ltime) {
		return getDayTimeString_inner(ltime, false);
	}

	/**
	 * 时间格式转换
	 * 
	 * @param ltime
	 *            - 相对于当天零时的秒数
	 * @return String - 带格式的时间字符串 “时:分:秒”
	 */
	private static String getDayTimeString_inner(long ltime, boolean zone_flag) {
		long daytime = ltime % (24 * 3600); // 将时间折算为一天之内
		Date dateTemp = new Date(daytime * 1000);
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		if (zone_flag) {
			formatter.setTimeZone(new SimpleTimeZone(0, "")); // 不应该是零时区
		}
		return formatter.format(dateTemp);
	}

	/**
	 * 时间格式转换
	 * 
	 * @param ltime
	 *            - 相对于1970年1月1日零时的秒数
	 * @param format
	 *            - 格式字符串
	 * @return String - 带格式的时间字符串
	 */
	public static String getTimeWithFormat(long ltime, String format) {
		Date dateTemp = new Date(ltime * 1000);
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(dateTemp);
	}

	/**
	 * IBS 中多处 char(1) 定义,实际使用 int 更加方便,这里 char(1) -- > 整数转换
	 * 
	 * @param value
	 *            "1" -> 1, "2" -> 2, "a" -> 10, "b" -> 11 ...
	 * @return 失败返回 0
	 */
	public static int charValue2Int(String value) {
		int nRet = 0;
		if (value != null && value.length() > 0) {
			char ch = value.charAt(0);
			if (ch >= '0' && ch <= '9') {
				nRet = ch - '0';
			} else if (ch >= 'a' && ch <= 'z') {
				nRet = ch - 'a' + 10;
			} else if (ch >= 'A' && ch <= 'Z') {
				nRet = ch - 'A' + 10;
			}
		}
		return nRet;
	}

	/**
	 * long型转换成的字符串 time="1314028800000"
	 * 
	 * @param time
	 * @return dateString
	 */
	public static String timeToString(String time) {
		Date date = new Date(Long.parseLong(time));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
		String dateString = formatter.format(date);
		return dateString;
	}
	
	public static String getToday(){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String dateString = formatter.format(new Date());
		return dateString;
	}

}
