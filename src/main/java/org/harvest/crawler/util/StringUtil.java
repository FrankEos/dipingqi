//=======================================================================
package org.harvest.crawler.util;

//================================================================
/**
 * 功能描述: 该类提供基本的公用函数
 */

//================================================================
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	public static final boolean NEED_PRT = true;

	// =============================方法：q(String)====================
	/**
	 * 该方法在字符串的两端加上单引号 主要用于组成SQL语句
	 * 
	 * @param sSource
	 *            源字符串
	 * @return 加上引号后的结果字符串
	 */
	// ===============================================================
	public static String q(String sSource) {
		if (sSource == null) {
			return "''";
		}
		return "'" + sSource + "'";
	}

	public static String iso2Gb(String str) {
		String sRet = "";
		str = str == null ? "" : str;
		try {
			sRet = new String(str.getBytes("ISO8859_1"), "GB2312");
		} catch (Exception ex) {

		}
		return sRet;
	}

	/**
	 * 得到安全的sql 用于在sql里传变量时，过滤掉干扰sql执行的字符
	 * 
	 * @param strValue
	 * @return
	 */
	public static String getSafeSql(String strValue) {
		return replace(strValue, "'", "''").trim();
	}

	public static String getHou(String filename) {
		return filename.substring(filename.lastIndexOf("."));
	}

	/**
	 * 系统文件路径分隔符 在文件操作中，作为路径分隔符 此方法会根据不同的操作系统返回不同的路径分隔符 用于跨平台开发
	 */
	public static String getPathSpace() {
		return new Properties(System.getProperties()).getProperty("file.separator");
	}

	// =========================??????q(java.util.Date)=================
	/**
	 * 该方法把一个Date类型的变量转换成字符串，然后在两端加上单引号 目标字符串的格式为'yyyymmddhhnnss' 主要用于组成SQL语句字符串
	 * 
	 * @param ADatetime
	 *            待处理的Date类型变量
	 * @return 转换后的字符串
	 */
	// ===============================================================
	public static String q(Date dtSource) {
		if (dtSource == null) {
			return "''";
		}
		return q(datetimeToStr(dtSource));
	}

	public static String q(Object objSource) {
		if (objSource == null) {
			return "''";
		}
		return q((String) (objSource));
	}

	public static final String replace(String line, String oldString, String newString) {
		if (line == null) {
			return null;
		}
		int i = 0;
		if ((i = line.indexOf(oldString, i)) >= 0) {
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = line.indexOf(oldString, i)) > 0) {
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			return buf.toString();
		}
		return line;
	}

	public static final String encodeEditorString(String strData) {
		strData = replace(strData, "\r\n", "");
		strData = replace(strData, "\r", "");
		strData = replace(strData, ">", "&gt;");
		strData = replace(strData, "<", "&lt;");
		strData = replace(strData, "\"", "&quot;");
		strData = replace(strData, "'", "&#39;");

		return strData;
	}

	public static final String encode(String strData) {
		strData = replace(strData, "\r\n", "&khb");

		return strData;
	}

	public static final String decode(String strData) {
		strData = replace(strData, "&khb", "\r\n");
		return strData;
	}

	public static final String decodeEditorString(String strData) {
		strData = replace(strData, "&gt;", ">");
		strData = replace(strData, "&lt;", "<");
		strData = replace(strData, "&quot;", "");
		strData = replace(strData, "&#39;", "'");
		strData = replace(strData, "<P> </P>", "<P>&nbsp;</P>");

		return strData;
	}

	// ==================方法：left(String)===========================
	/**
	 * 该方法从左边取指定字符串的指定长度的子字符 如果字符串长度小于指定的长度，则返回源字符串
	 * 
	 * @param sSource
	 *            源待取子串的字符串
	 * @param nLength
	 *            自左边起长度
	 * @return 结果字符串
	 */
	// ===============================================================
	public static String left(String sSource, int nLength) {
		if (sSource == null || sSource.equals("")) {
			return sSource;
		}

		if (sSource.length() > nLength) {
			return sSource.substring(0, nLength);
		} else {
			return sSource;
		}
	}

	public static String leftNews(String sSource, int nLength) {
		if (sSource == null || sSource.equals("")) {
			return sSource;
		}

		if (sSource.length() > nLength) {
			return sSource.substring(0, nLength) + "...";
		} else {
			return sSource;
		}
	}

	// ==================方法：left(String)===========================
	/**
	 * 该方法从左边取指定字符串的指定长度的子字符 如果字符串长度小于指定的长度，则返回源字符串
	 * 
	 * @param sSource
	 *            源待取子串的字符串
	 * @param nLength
	 *            自左边起长度
	 * @return 结果字符串
	 */
	// ===============================================================
	public static String right(String sSource, int nLength) {
		if (sSource == null) {
			return null;
		}
		if (sSource.equals("")) {
			return "";
		}
		if (sSource.length() > nLength) {
			return sSource.substring(sSource.length() - nLength, sSource.length());
		} else {
			return sSource;
		}
	}

	// ==============
	/**
	 * the index base from 1
	 */
	public static String copy(String sSource, int nIndex, int nLength) {
		if (sSource == null) {
			return null;
		}
		if (sSource.equals("")) {
			return "";
		}
		if (nIndex > sSource.length()) {
			return null;
		} else if (sSource.length() < (nLength + nIndex)) {
			return sSource.substring(nIndex - 1, sSource.length());
		} else {
			return sSource.substring(nIndex - 1, nLength + nIndex - 1);
		}
	}

	// ===============方法：datetimeToStr(java.util.Date)=============
	/**
	 * 将Date类型的变量转换成yyyymmddhhnnss格式的字符串
	 * 
	 * @param dtSource待转换的Date类型变量
	 * @return 结果字符串
	 */
	// ===============================================================
	public static String datetimeToStr(Date dtSource) {
		// 'yyyy-mm-dd hh:nn:ss'
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		return formatter.format(dtSource);
	}

	/**
	 * 将Date类型的变量转换成显示字符串yyyy年mm月dd日hh时mm分ss秒格式的字符串
	 * 
	 * @param dtSource待转换的Date类型变量
	 * @return 中文的结果字符串
	 */
	public static String datetimeToChinese(Date dtSource) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy??MM??dd??HH?mm??");
		return formatter.format(dtSource);
	}

	public static String formatMoney(double dtSource) {
		java.text.DecimalFormat formatter = new java.text.DecimalFormat("#,###.00");
		return "??" + formatter.format(dtSource);
	}

	public static String formatDecimal(double dtSource, String nbit) {
		java.text.DecimalFormat formatter = null;
		try {
			formatter = new java.text.DecimalFormat("#,###." + nbit);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return formatter.format(dtSource);
	}

	/**
	 * 生成格式是yyyyMMddHHmmssSSS字串
	 */
	public static String nowMillis() {
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Date d = new Date();
		return formater.format(d);
	}

	/**
	 * 生成精确到毫秒的时刻
	 */
	public static String nowInMillis() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
		Date d = new Date();
		return formatter.format(d);
	}

	public static String datetimeToMillis(long _nTime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
		Date d = new Date();
		d.setTime(_nTime);
		return formatter.format(d);
	}

	// =====================方法：strToDatetime(String)===================
	/**
	 * 将字符串转换成java.util.Date类型
	 * 
	 * <pre>
	 * 支持的字符串格式包括：
	 * yyyy-MM-dd HH:mm:ss
	 * yyyyMMddHHmmss
	 * yyyy-MM-dd
	 * yyyyMMdd
	 * </pre>
	 * 
	 * @param sStr
	 *            待转换的字符串
	 * @return 转换成功后的java.util.Date对象
	 */
	// ==================================================================
	public static java.util.Date strToDatetime(String sStr) {
		if (sStr == null) {
			return null;
		}
		SimpleDateFormat formatter;
		if (sStr.length() == 19) {
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		} else if (sStr.length() == 10) {
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		} else if (sStr.length() == 8) {
			formatter = new SimpleDateFormat("yyyyMMdd");
		} else if (sStr.length() == 14) {
			formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		} else {
			formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		}
		ParsePosition pos = new ParsePosition(0);
		return formatter.parse(sStr, pos);
	}

	// ===============方法：getDimension(Object)=======================
	/**
	 * 取数组的维数。 一般变量是0维。 例如:
	 * 
	 * <pre>
	 * String[][][] strArray = new String[3][3][3];
	 * getDimension(strArray)的值为3
	 * </pre>
	 * 
	 * @param arrAny
	 *            任何类型的数组，也可以使普通变量
	 * @return 数组的维数
	 */
	// ===============方法：formatDatetime(java.util.Date)=============
	public static int getDimension(Object arrAny) {
		String strObjectClass = arrAny.getClass().getName();
		int nLength = strObjectClass.length();
		int nRet = 0;
		for (int i = 0; i < nLength; i++) {
			if (strObjectClass.charAt(i) != '[' && nRet == 0) {
				continue;
			} else if (strObjectClass.charAt(i) != '[' && nRet > 0) {
				return nRet;
			}
			nRet++;
		}
		return nRet;
	}

	// ===============方法：dispartString(String)======================
	/**
	 * 使用指定的分隔符将字符串分隔为字符串数组 本方法支持转义符，例如希望使用";"作为分隔符， 但是在字符串中某一处的“;”希望保留，不作分隔，在此“;”加上转义符即可。 在本方法中转义符是"~"。 如果待分隔的字符串的最后一个字符是分隔符，则结果字符串数组的最后补上一个空字符串 例如“2；1；”分隔成{"2","1",""}。（此处分隔符为“;”）.
	 * 
	 * @param sSourceString
	 *            待分隔的字符串
	 * @param separators
	 *            字符串的分隔符数组，如果值为null，则使用';'作为分隔符
	 * @return 分隔结果字符串数组
	 * @例如：sSourceString = "123;3247.34;dfj_rr",separator={';'} 则返回值为{"123","3247.34","dfj_rr"};
	 */
	// ====================================================================
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String[] dispartString(String sSourceString, char[] separators) {
		if (sSourceString == null) {
			return new String[0];
		}
		if (separators == null) {
			separators = new char[] { ';' };
		}
		// ??????
		StringBuffer sbTemp = new StringBuffer("");
		// ????????
		java.util.Vector v = new java.util.Vector();
		// ??"~"?????
		char charConv = '~';
		// ????????????
		boolean isConvChar = false;
		int nSourceLength = sSourceString.length();
		outerLoop: for (int i = 0; i < nSourceLength; i++) {
			innerLoop: for (int j = 0; j < separators.length; j++) {
				// ????????????????????????
				if (!isConvChar && sSourceString.charAt(i) == charConv) {
					isConvChar = true;
					continue outerLoop;
				}
				// ???????????????????????????????????????????ж?
				if (isConvChar) {
					sbTemp.append(sSourceString.charAt(i));
					isConvChar = false;
					continue outerLoop;
				}
				if (sSourceString.charAt(i) != separators[j]) {
					// ???????????????????????????????
					if (j == (separators.length - 1)) {
						sbTemp.append(sSourceString.charAt(i));
					}
					continue innerLoop;
				} else {
					// ????????????????
					v.add(new String(sbTemp));
					// ????????
					sbTemp.delete(0, sbTemp.length());
					continue outerLoop;
				}
			}
			if (i == sSourceString.length() - 1) {
				v.add(new String(sbTemp));
			}
		}
		String[] strArrRet = new String[v.size()];
		for (int i = 0; i < strArrRet.length; i++) {
			strArrRet[i] = (String) v.get(i);
		}
		return strArrRet;
	}

	// =================== 函数 : strToInt ===================================
	/**
	 * 将指定字符串转化为int
	 */
	// ===================================================================
	public static int strToInt(String sStr) {
		if (sStr == null) {
			return 0;
		}
		if (sStr.equals("")) {
			return 0;
		}
		return Integer.parseInt(sStr);
	}

	// =================== 函数 : strToLong ===================================
	/**
	 * 将指定字符串转化为long
	 * 
	 * @param sStr
	 *            待转换的字符串
	 * @return 返回简单类型类型long的变量
	 */
	// ===================================================================
	public static long strToLong(String sStr) {
		if (sStr == null || sStr.equals("")) {
			return 0;
		}
		return Long.parseLong(sStr);
	}

	// =================== 函数 : intToStr ===============================
	/**
	 * 将整数转换成字符串
	 */
	public static String intToStr(int nValue) {
		return new Integer(nValue).toString();
	}

	// =================== 函数 : intToStr ===============================
	/**
	 * 将整数转换成字符串
	 */
	public static String intToStr(long nValue) {
		return new Long(nValue).toString();
	}

	// =================== 函数 : strToFloat ===============================
	/**
	 * 将字符串转换成float型。
	 */
	// ======================================================================
	public static float strToFloat(String sStr) {
		if (sStr == null || sStr.equals("")) {
			return 0.0f;
		}
		return Float.parseFloat(sStr);
	}

	// =================== 函数 : strToDouble ===============================
	/**
	 * 将字符串转换成double型。
	 */
	// ======================================================================
	public static double strToDouble(String sStr) {
		if (sStr == null || sStr.equals("")) {
			return 0.0;
		}
		return Double.parseDouble(sStr);
	}

	// =================== 函数 : now ===================================
	/**
	 * 取出当前时间的字符串格式,格式为yyyymmddhhmmss
	 * 
	 * @return 字符串格式的当前时间。 例：2001-08-09 15:25:31
	 */
	// ===================================================================
	public static String now() {
		return datetimeToStr(new Date());
	}

	/**
	 * 取出当前时间的字符串格式,格式为yyyy-MM-dd HH:mm:ss
	 * 
	 * @return 字符串格式的当前时间。 例：2001-08-09 15:25:31
	 */
	// ===================================================================
	public static String nowDateTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(new Date());
	}

	// =================== 函数 : today ===================================
	/**
	 * 取出当前时间的字符串格式,格式为yyyymmdd
	 * 
	 * @return 字符串格式的当前日期。
	 * 
	 */
	// ===================================================================
	public static String today() {
		return datetimeToStr(new Date()).substring(0, 8);
	}

	/**
	 * 取昨天的日期,格式为YYYYMMDD
	 */
	public static String yesterday() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -1);
		String sYear = intToStr(c.get(Calendar.YEAR));
		String sMonth = padl(intToStr(c.get(Calendar.MONTH) + 1), 2, '0');
		String sDay = padl(intToStr(c.get(Calendar.DAY_OF_MONTH)), 2, '0');
		return sYear + sMonth + sDay;
	}

	/**
	 * 取与给定日期的相隔若干天的日期
	 * 
	 * @param sDate
	 *            给定日期YYYYMMDD
	 * @param nDays
	 *            相隔的天数,比指定日期早则为负数
	 * @return 日期YYYYMMDD
	 */
	public static String getDay(String sDate, int nDays) {
		Calendar c = Calendar.getInstance();
		int year = Integer.parseInt(sDate.substring(0, 4));
		int month = Integer.parseInt(sDate.substring(4, 6)) - 1;
		int day = Integer.parseInt(sDate.substring(6));
		c.set(year, month, day);
		c.add(Calendar.DAY_OF_MONTH, nDays);
		String sYear = StringUtil.intToStr(c.get(Calendar.YEAR));
		String sMonth = StringUtil.padl(StringUtil.intToStr(c.get(Calendar.MONTH) + 1), 2, '0');
		String sDay = StringUtil.padl(StringUtil.intToStr(c.get(Calendar.DAY_OF_MONTH)), 2, '0');
		return sYear + sMonth + sDay;
	}

	/**
	 * 取与给定月份相隔若干月的月份
	 * 
	 * @param sDate
	 *            格式:YYYYMMDD
	 */
	public static String getMonth(String sDate, int nMonths) {
		Calendar c = Calendar.getInstance();
		int year = Integer.parseInt(sDate.substring(0, 4));
		int month = Integer.parseInt(sDate.substring(4, 6)) - 1;
		int day = 15;
		c.set(year, month, day);
		c.add(Calendar.MONTH, nMonths);
		String sYear = StringUtil.intToStr(c.get(Calendar.YEAR));
		String sMonth = StringUtil.padl(StringUtil.intToStr(c.get(Calendar.MONTH) + 1), 2, '0');
		return sYear + sMonth;
	}

	/**
	 * 返回某个日期加上一定天数后的时间，格式为yyyymmddhh24mmss，如果要减去多少天，则ADays为负数????
	 */
	public static String getAddDays(String ADatetime, int ADays) {
		Date dt = new Date(convertDateTimeToSysTime(ADatetime));
		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		c.add(Calendar.DAY_OF_MONTH, ADays);
		String sYear = intToStr(c.get(Calendar.YEAR));
		String sMonth = padl(intToStr(c.get(Calendar.MONTH) + 1), 2, '0');
		String sDay = padl(intToStr(c.get(Calendar.DAY_OF_MONTH)), 2, '0');
		String sHour = padl(intToStr(c.get(Calendar.HOUR)), 2, '0');
		String sMinute = padl(intToStr(c.get(Calendar.MINUTE)), 2, '0');
		String sSecond = padl(intToStr(c.get(Calendar.SECOND)), 2, '0');
		return sYear + sMonth + sDay + sHour + sMinute + sSecond;
	}

	// =================== 函数 : fill ===================================
	/**
	 * 将指定字符重复指定次数填充到字符串中
	 * 
	 * @cChar 被重复填充的字符
	 * @nLen 重复的次数
	 * @return 填充后的字符串
	 */
	// ===================================================================
	public static String fill(char cChar, int nLen) {
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < nLen; i++) {
			s.append(cChar);
		}
		return s.toString();
	}

	// =================== 函数 : padl ===================================
	/**
	 * 得到左边填以空格或指定字符（右对齐）的指定长度的字符串
	 * 
	 * @param sStr
	 *            - 源字符串
	 * @param nLen
	 *            - 指定长度
	 * @param cChar
	 *            - 指定字符，如不指定，则缺省为空格
	 * @return 返回的填充过的结果字符串
	 */
	// ===================================================================
	public static String padl(String sStr, int nLen, char cChar) {
		// Added by Stone
		if (sStr == null) {
			return sStr;
		}

		// ==== ?ж??????????к???,?????????????????????????? ====
		nLen -= unicodeCount(sStr);
		// Add end

		if (sStr.length() > nLen) {
			return right(sStr, nLen);
		}

		return fill(cChar, nLen - sStr.length()) + sStr;
	}

	// =================== 函数 : padl ===================================
	/**
	 * 得到左边填以空格（右对齐）的指定长度的字符串
	 * 
	 * @param sStr
	 *            - 源字符串
	 * @param nLen
	 *            - 指定长度
	 * @return 返回的填充过的结果字符串
	 */
	// ===================================================================
	public static String padl(String sStr, int nLen) {
		return padl(sStr, nLen, ' ');
	}

	// =================== 函数 : padr ===================================
	/**
	 * 得到右边填以空格或指定字符（左对齐）的指定长度的字符串
	 * 
	 * @param sStr
	 *            - 源字符串
	 * @param nLen
	 *            - 指定长度
	 * @param cChar
	 *            - 指定字符，如不指定，则缺省为空格
	 * @return 返回的填充过的结果字符串
	 */
	// ===================================================================
	public static String StringKong(String kong) {

		if (kong == null || kong.equals("")) {
			return "";
		} else {
			return kong;
		}
	}

	public static String padr(String sStr, int nLen, char cChar) {
		// Added by Stone
		if (sStr == null) {
			return sStr;
		}

		// ==== ?ж??????????к???,?????????????????????????? ====
		nLen -= unicodeCount(sStr);
		// Add end

		if (sStr.length() > nLen) {
			return left(sStr, nLen);
		}

		return sStr + fill(cChar, nLen - sStr.length());
	}

	// =================== 函数 : padr ===================================
	/**
	 * 得到右边填以空格（左对齐）的指定长度的字符串
	 * 
	 * @param sStr
	 *            - 源字符串
	 * @param nLen
	 *            - 指定长度
	 * @return 返回的填充过的结果字符串
	 */
	// ===================================================================
	public static String padr(String sStr, int nLen) {
		return padr(sStr, nLen, ' ');
	}

	// =================== ???? : assert ==============================
	/**
	 * ????????????????????????????? ?????????????????? %%%% ???????Ч????????? %%%% ???????????趨???qζ??????в???????
	 * 
	 * @param test
	 *            - ??????????????
	 * @description - ?????????
	 */
	// ==================================================================
	// public static void assert(boolean test, String description)
	// {
	// if (!test)
	// {
	// System.out.println("Assert Failed.\nAssert description: "
	// + description);
	// System.exit( -591);
	// }
	// }

	/**
     *
     */
	public static void prt(String str) {
		if (NEED_PRT) {
			System.out.println(str);
		}

	}

	/**
	 * ???把null字符串转成"".?????????
	 */
	public static String noNull(String sSrc) {
		if (sSrc == null) {
			return "";
		}
		return sSrc.trim();
	}

	/**
	 * ??????A;StringUtil;C;&D;E;F&?????????UTLDataList?????
	 * 
	 * public static UTLDataList unpackList(String sList) { UTLDataList resultDataList = new UTLDataList(); if(sList==null) { return resultDataList; } String[] strArray1 = dispartString(sList,new
	 * char[]{'&'}); if(strArray1.length<1) { return resultDataList; } resultDataList.setColumnNames( new String[dispartString(strArray1[0],new char[]{';'}).length]); for(int
	 * i=0;i<strArray1.length;i++) { resultDataList.addLine(dispartString(strArray1[i],new char[]{';'})); } return resultDataList; }
	 */

	/**
	 * 用新字符串将原字符串中的指定子字符串替换
	 * 
	 * @param sSrc
	 *            原字符串
	 * @param sOldStr
	 *            将要被替换的子字符串
	 * @param sNewStr
	 *            用于替换的新字符串
	 * @return 替换后的字符串
	 */
	public static String strReplace(String sSrc, String sOldStr, String sNewStr) {
		if (sSrc == null || sOldStr == null || sNewStr == null || sSrc.equals("") || sOldStr.equals("")) {
			return sSrc;
		}

		StringBuffer sStrBuf = new StringBuffer(sSrc);
		int nStart = sSrc.indexOf(sOldStr);
		int nOldStrLen = sOldStr.length();
		int nNewStrLen = sNewStr.length();
		while (nStart >= 0) {
			sStrBuf.replace(nStart, nStart + nOldStrLen, sNewStr);
			nStart = sStrBuf.toString().indexOf(sOldStr, nStart + nNewStrLen);
		}
		return sStrBuf.toString();
	}

	// ===================isInArray(String[],String)===============================
	/**
	 * 检查某个字符串是否存在字符串数组中
	 */
	// ============================================================================
	public static boolean isInArray(String[] srcStrArray, String sStr) {
		for (int i = 0; i < srcStrArray.length; i++) {
			if (srcStrArray[i] != null && srcStrArray[i].equals(sStr)) {
				return true;
			}
		}
		return false;
	}

	// ===============================parseString====================================
	/**
	 * 析取以指定分隔符分隔的字符串，然后放入容器Collection中
	 * 
	 * @param Collection
	 *            _container
	 * @param String
	 *            _sSource 源串
	 * @param String
	 *            _sToken 分隔符
	 * @return boolean ---标识成功或失败
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean parseString(Collection _container, String _sSource, String _sDelim) {
		if (_sSource == null) {
			return true;
		}
		if (_sDelim == null) {
			System.out.println("?????????????");
			return false;
		}
		StringTokenizer tokenizer = new StringTokenizer(_sSource, _sDelim, false);
		String sToken = null;
		while (tokenizer.hasMoreTokens()) {
			sToken = tokenizer.nextToken();
			if ((sToken == null) || (sToken.equalsIgnoreCase(""))) {
				continue;
			}
			_container.add(sToken);
		}
		return true;
	}

	// 返回子串在父串中的位置，从１开始????
	public static long pos(String sSubStr, String sMainStr) {
		return sMainStr.indexOf(sSubStr) + 1;
	}

	// 将A;B;类型的字符串分割成A和B; vSign是分割的标识符???????
	public static String[] splitString(String vSource, String vSign) {
		String vSplitString, vLeftSource;
		int nPos, nSignLen;
		if ((vSource.length() == 0) || (vSign.length() == 0)) {
			vSplitString = "";
			vLeftSource = vSource;
			return new String[] { vSplitString, vLeftSource };
		}
		nPos = (int) pos(vSign, vSource);
		if (nPos > 0) {
			nSignLen = vSign.length();
			vSplitString = copy(vSource, nSignLen, nPos - nSignLen);
			vLeftSource = copy(vSource, nPos + 1, vSource.length() - nPos);
		} else {
			vSplitString = vSource;
			vLeftSource = "";
		}
		return new String[] { vSplitString, vLeftSource };
	}

	// 将从nStart开始的n个字符替换成sRepStr,nStart是从1开始???
	public static String replace(String sStr, long nStart, long n, String sRepStr) {
		String s = copy(sStr, (int) (nStart + n), sStr.length() - (int) (nStart - n + 1));
		if (StringUtil.isEmpty(s)) {
			s = "";
		}
		return copy(sStr, 1, (int) nStart - 1) + sRepStr + s;
	}

	public static String replaceAllSubStr(String sString, String sSubStr, String sRepStr) {
		long nStartPos, nPos;
		if (sSubStr == null || sSubStr.equals("")) {
			return sString;
		}

		nStartPos = 1;
		nPos = sString.indexOf(sSubStr, (int) nStartPos - 1);
		while (nPos >= 0) {
			sString = replace(sString, nPos + 1, sSubStr.length(), sRepStr);
			nStartPos = nPos + sRepStr.length();
			nPos = sString.indexOf(sSubStr, (int) nStartPos - 1);
		}
		return sString;
	}

	// 判断某个值是否为空值?
	public static boolean isEmpty(String Value) {
		return (Value == null || Value.trim().equals(""));
	}

	public static boolean isEmpty(Object Value) {
		return Value == null || isEmpty(Value.toString());
	}

	// 从字节数组中取出某一段内容，含nEnd，如从0-3将会取出4个的长度??????
	public static byte[] copyByte(byte[] ASource, int nFrom, int nEnd) {
		if (nEnd < nFrom) {
			return null;
		}
		if (nEnd >= ASource.length) {
			nEnd = ASource.length - 1;
		}
		int j = 0;
		byte[] sResult = new byte[nEnd - nFrom + 1];
		for (int i = nFrom; i <= nEnd; i++) {
			sResult[j] = ASource[i];
			j = j + 1;
		}
		return sResult;
	}

	public static String checkString(String check) {
		String cs = "";
		if (check == null || check.equals("") || check.equals("null")) {
			cs = "";
		} else {
			cs = check;
		}
		return cs;
	}

	public String[] checkshuzu(String shuzu[]) {
		if (shuzu == null) {
			return null;
		}
		return shuzu;
	}

	public static void clearArray(Object[] _Tmp) {
		for (int i = 0; i < _Tmp.length; i++) {
			_Tmp[i] = null;
		}
	}

	// 将汉字串按字节长度分隔??
	public static String[] splitChineseString(String Value, int nLength) {
		if (nLength <= 1) {
			return null;
		}
		byte[] byteLeft = Value.getBytes();
		byte[] byteTemp, byte2;
		String s2;
		String[] sResult = new String[byteLeft.length / nLength + 10];
		int nCount = 0;
		while (byteLeft != null && byteLeft.length > 0) {
			byteTemp = copyByte(byteLeft, 0, nLength - 1);
			// ??byteTemp?????????????byte[],?ж????????????????????????????
			s2 = new String(byteTemp);
			byte2 = s2.getBytes();
			if (byteTemp.length != byte2.length) { // ??????????????,?????????
				byteTemp = copyByte(byteLeft, 0, byteTemp.length - 2);
			}
			byteLeft = copyByte(byteLeft, byteTemp.length, byteLeft.length - 1);
			sResult[nCount] = new String(byteTemp);
			nCount = nCount + 1;
		}
		String[] sReturn = new String[nCount];
		for (int i = 0; i < nCount; i++) {
			sReturn[i] = sResult[i];
		}
		return sReturn;
	}

	/**
	 * 获取字符串中汉字的个数
	 * 
	 * @param sStr
	 *            源字符串
	 * @return 汉字的个数
	 */
	public static int unicodeCount(String sStr) {
		if (sStr == null || sStr.equals("")) {
			return 0;
		}

		int count = 0;
		for (int i = 0; i < sStr.length(); i++) {
			if ((int) sStr.charAt(i) > 255) {
				count++;
			}
		}

		return count;
	}

	/**
	 * 设置号码停开机状态
	 * 
	 * @param sOldState
	 *            :现在的状态
	 * @param sSoState
	 *            :某一位要变成的新值
	 * @param nStart
	 *            :从第几位开始,取值从1开始
	 * @param nLen
	 *            :要改变的长度
	 * @return 返回修改后的停开机状态 如原来的值是01000000000000000000现在要把第二位变成0, 则调用为setMsisdnHlrState("01000000000000000000","0",2,1) 返回值为00000000000000000000
	 */
	public static String setMsisdnHlrState(String sOldState, char cSoState, int nStart, int nLen) {
		char[] cOldStateArray = sOldState.toCharArray();
		int nIndex;
		if (nStart < 1) {
			nStart = 1;
		}
		for (int i = 0; i < nLen; i++) {
			nIndex = nStart + i - 1;
			if (nIndex >= cOldStateArray.length) {
				nIndex = cOldStateArray.length - 1;
			}
			cOldStateArray[nIndex] = cSoState;
		}
		return new String(cOldStateArray);
	}

	/**
	 * 转化byte[]为可打印的String输出,调试用
	 */
	public static String byteArrayToString(byte[] _byteArray) {
		StringBuffer sHex = new StringBuffer();
		for (int i = 0; i < _byteArray.length; i++) {
			sHex.append(Integer.toHexString((int) _byteArray[i]).toUpperCase());
			sHex.append(' ');
		}
		String sStr = new String(_byteArray);
		return sStr + ",HEX=" + sHex.toString();
	}

	/**
	 * 转化String[]为String输出
	 */
	public static String arrStrToString(String[] attStr) {
		if (StringUtil.isEmpty(attStr)) {
			return "";
		}
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < attStr.length; i++) {
			str.append(attStr[i].toString());
			if (i < attStr.length - 1)
				str.append(",");
		}
		return str.toString();
	}

	/**
	 * 取字符串的缺省值
	 * 
	 * @param value
	 *            － 源数值
	 * @param defaultValue
	 *            －缺省值
	 * @return 如果源数值为空，或者Trim后为空字符串，则返回缺省值，否则直接返回源数值trim后的值
	 */
	public static String getValueDefault(String value, String defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		if (value.trim().length() > 0) {
			return value.trim();
		}
		return defaultValue;
	}

	public static String getMsisdnByImsiMin(String sImsiMin) {
		// Imsi???46000H1H2H3NH0XXXXX
		// ??N??'5'??H0='0'
		// ????N=N+5
		StringBuffer sbMsisdn = new StringBuffer("13");
		char h0 = sImsiMin.charAt(9);
		if (h0 >= '5') {
			sbMsisdn.append(sImsiMin.charAt(9));
			sbMsisdn.append("0");
		} else {
			sbMsisdn.append(sImsiMin.charAt(9) + 5);
			sbMsisdn.append(sImsiMin.charAt(10));
		}
		sbMsisdn.append(sImsiMin.substring(5, 8));
		return sbMsisdn.toString();
	}

	/**
	 * 将8位或14位的字符串表示的时间转换为系统的时间
	 */
	public static long convertDateTimeToSysTime(String sDateTime) {
		int nYear = Integer.parseInt(sDateTime.substring(0, 4));
		int nMonth = Integer.parseInt(sDateTime.substring(4, 6));
		int nDay = Integer.parseInt(sDateTime.substring(6, 8));
		int nHour = 0, nMinute = 0, nSecond = 0;
		if (sDateTime.length() == 14) {
			nHour = Integer.parseInt(sDateTime.substring(8, 10));
			nMinute = Integer.parseInt(sDateTime.substring(10, 12));
			nSecond = Integer.parseInt(sDateTime.substring(12, 14));
		}
		Calendar calendar = Calendar.getInstance();
		calendar.set(nYear, nMonth - 1, nDay, nHour, nMinute, nSecond);
		return calendar.getTime().getTime();
	}

	/**
	 * 取两个以14位字符串表示的时间相隔的天数,不足一天按一天算.
	 * 
	 * @return 结果为sFirstDate 减去 sSecondDate所得到的天数
	 */
	public static long compareDate(String sFirstDate, String sSecondDate) {
		long time1 = convertDateTimeToSysTime(sFirstDate);
		long time2 = convertDateTimeToSysTime(sSecondDate);
		long nTime = (time1 - time2);
		final long nConst = (24 * 60 * 60 * 1000);
		if (nTime % nConst == 0) {
			return nTime / nConst;
		}

		return (nTime / nConst) + 1;
	}

	/**
	 * 比较当前时间是否在传入的10位字符时间之前
	 */
	public static boolean isBeforeDate(String sCompareDate) {
		SimpleDateFormat clsFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date compareDate = clsFormat.parse(sCompareDate);
			// Date compareDate = clsFormat.parse("2006-02-11");
			Date currentDate = clsFormat.parse(clsFormat.format(new Date()));
			// Date currentDate =clsFormat.parse("2006-02-06");
			if (currentDate.before(compareDate)) {
				return true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 比较两个对象是否相等，如果是字符串比较，忽略大小写. 两个值都为null时，不管是什么类型的对象，都认为相等
	 * 
	 * @param o1
	 *            对象1
	 * @param o2
	 *            对象2
	 * @return true-相等 false-不相等
	 */
	public static boolean equal(Object o1, Object o2) {
		if (o1 == null) {
			return o2 == null;
		}
		if ((o1 instanceof String) && (o2 instanceof String)) {
			return ((String) o1).equalsIgnoreCase((String) o2);
		}
		return o1.equals(o2);
	}

	/**
	 * 获取当前的时刻,24小时制
	 */
	public static String getCurrentTime(Date _d) {
		SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
		return formatter.format(_d);
	}

	/**
	 * 该方法用来精确到小数点后两位
	 * 
	 * @param number
	 *            double
	 * @return doublle
	 */
	public static double getxiaoshu(double number) {
		long n = Math.round(number * 100);
		return n / 100.0;
	}

	/**
	 * 该方法用来转换将小写金额转大写
	 * 
	 * @param money
	 *            double
	 *            <p>
	 *            输入一个带2位小数的浮点数
	 *            </p>
	 * @return String
	 *         <p>
	 *         返回转换成大写的字符串
	 *         </p>
	 */
	public static String change(String money) {
		String tmpString = money.substring(0, money.indexOf("."));
		String[] strSite = { "元", "拾", "佰", "仟", "万", "仟", "亿" };
		String strMoney = "";

		int strLen = tmpString.length();

		if (strLen > 12) {
			System.out.println("请输入小于等于仟亿的金额");
		} else {

			for (int i = 0; i < strLen; i++) {
				int m = strLen - i - 1;
				switch (tmpString.charAt(i)) {
				case '1': {
					if (m == 1) {
						strMoney = strMoney + strSite[m];
					}
					if (m == 5) {
						strMoney = strMoney + strSite[m - 4];
					} else if (m < 9 && m > 5) {
						strMoney = strMoney + "壹" + strSite[m - 4];
					} else if (m == 9) {
						strMoney = strMoney + strSite[m - 8];
					} else if (m > 9) {
						strMoney = strMoney + "壹" + strSite[m - 8];
					} else {
						strMoney = strMoney + "壹" + strSite[m];
					}
					break;
				}
				case '2': {
					if (m < 9 && m > 4) {
						strMoney = strMoney + "贰" + strSite[m - 4];
					} else if (m >= 9) {
						strMoney = strMoney + "贰" + strSite[m - 8];
					} else {
						strMoney = strMoney + "贰" + strSite[m];
					}
					break;
				}
				case '3': {
					if (m < 9 && m > 4) {
						strMoney = strMoney + "叁" + strSite[m - 4];
					} else if (m >= 9) {
						strMoney = strMoney + "叁" + strSite[m - 8];
					} else {
						strMoney = strMoney + "叁" + strSite[m];
					}
					break;
				}
				case '4': {
					if (m < 9 && m > 4) {
						strMoney = strMoney + "肆" + strSite[m - 4];
					} else if (m >= 9) {
						strMoney = strMoney + "肆" + strSite[m - 8];
					} else {
						strMoney = strMoney + "肆" + strSite[m];
					}
					break;
				}
				case '5': {
					if (m < 9 && m > 4) {
						strMoney = strMoney + "伍" + strSite[m - 4];
					} else if (m >= 9) {
						strMoney = strMoney + "伍" + strSite[m - 8];
					} else {
						strMoney = strMoney + "伍" + strSite[m];
					}
					break;
				}
				case '6': {
					if (m < 9 && m > 4) {
						strMoney = strMoney + "陆" + strSite[m - 4];
					} else if (m >= 9) {
						strMoney = strMoney + "陆" + strSite[m - 8];
					} else {
						strMoney = strMoney + "陆" + strSite[m];
					}
					break;
				}
				case '7': {
					if (m < 9 && m > 4) {
						strMoney = strMoney + "柒" + strSite[m - 4];
					} else if (m >= 9) {
						strMoney = strMoney + "柒" + strSite[m - 8];
					} else {
						strMoney = strMoney + "柒" + strSite[m];
					}
					break;
				}
				case '8': {
					if (m < 9 && m > 4) {
						strMoney = strMoney + "捌" + strSite[m - 4];
					} else if (m >= 9) {
						strMoney = strMoney + "捌" + strSite[m - 8];
					} else {
						strMoney = strMoney + "捌" + strSite[m];
					}
					break;
				}
				case '9': {
					if (m < 9 && m > 4) {
						strMoney = strMoney + "玖" + strSite[m - 4];
					} else if (m >= 9) {
						strMoney = strMoney + "玖" + strSite[m - 8];
					} else {
						strMoney = strMoney + "玖" + strSite[m];
					}
					break;
				}
				case '0': {
					if (m == 4) {
						strMoney = strMoney + "万";
					} else if (m == 8) {
						strMoney = strMoney + "亿";
					} else {
						strMoney = strMoney + "零";
					}
					break;
				}
				}
			}
		}
		String[] zero = { "零零零零零零零零零零零", "零零零零零零零零零零", "零零零零零零零零零", "零零零零零零零零", "零零零零零零零零", "零零零零零零零", "零零零零零零", "零零零零零", "零零零零", "零零零", "零零" };
		for (int n = 0; n < zero.length; n++) {
			if (strMoney.indexOf(zero[n]) != -1) {
				strMoney = strMoney.replaceAll(zero[n], "零");
			}
		}
		strMoney = strMoney.replaceAll("零亿", "亿");
		strMoney = strMoney.replaceAll("零万", "万");
		strMoney = strMoney.replaceAll("亿万", "亿零");
		for (int n = 0; n < zero.length; n++) {
			if (strMoney.indexOf(zero[n]) != -1) {
				strMoney = strMoney.replaceAll(zero[n], "零");
			}
		}
		if (strMoney.endsWith("零")) {
			strMoney = strMoney.substring(0, strMoney.length() - 1);
		}

		String xiaoshu = money.substring(money.indexOf(".") + 1);
		String tempString = "";
		System.out.println(xiaoshu.length());

		switch (xiaoshu.charAt(0)) {
		case '1': {
			tempString = "壹" + "角";
			break;
		}
		case '2': {
			tempString = "贰" + "角";
			break;
		}
		case '3': {
			tempString = "叁" + "角";
			break;
		}
		case '4': {
			tempString = "肆" + "角";
			break;
		}
		case '5': {
			tempString = "伍" + "角";
			break;
		}
		case '6': {
			tempString = "陆" + "角";
			break;
		}
		case '7': {
			tempString = "柒" + "角";
			break;
		}
		case '8': {
			tempString = "捌" + "角";
			break;
		}
		case '9': {
			tempString = "玖" + "角";
			break;
		}
		case '0': {
			tempString = "";
			break;
		}
		}

		if (xiaoshu.length() == 2) {

			switch (xiaoshu.charAt(1)) {
			case '1': {
				tempString = tempString + "壹" + "分";
				break;
			}
			case '2': {
				tempString = tempString + "贰" + "分";
				break;
			}
			case '3': {
				tempString = tempString + "叁" + "分";
				break;
			}
			case '4': {
				tempString = tempString + "肆" + "分";
				break;
			}
			case '5': {
				tempString = tempString + "伍" + "分";
				break;
			}
			case '6': {
				tempString = tempString + "陆" + "分";
				break;
			}
			case '7': {
				tempString = tempString + "柒" + "分";
				break;
			}
			case '8': {
				tempString = tempString + "捌" + "分";
				break;
			}
			case '9': {
				tempString = tempString + "玖" + "分";
				break;
			}
			case '0': {
				tempString = "";
				break;
			}
			}
		}
		return strMoney + tempString;

	}

	@SuppressWarnings("rawtypes")
	public static String[] ArrToStr(ArrayList arr) {
		String[] str = new String[arr.size()];
		for (int i = 0; i < arr.size(); i++) {
			str[i] = arr.get(i).toString();
		}
		return str;
	}

	public static String trimString(String sourceStr, int length) {
		String str = "";
		if (length > sourceStr.length()) {
			return sourceStr;
		}
		str = sourceStr.substring(0, length) + " ...";

		return str;
	}

	/**
	 * 把空字符串替换为指定的字符
	 * 
	 * @param str
	 *            　
	 * @param repStr
	 *            指定的字符，如"@"等
	 * @return
	 */
	public static String nullRepChar(String str, String repStr) {
		if (isEmpty(str))
			return repStr;
		else
			return str;
	}

	/**
	 * 把指定的字符替换为，空字符串
	 */
	public static String charRepNull(String str, String repStr) {
		if (noNull(str).trim().equals(noNull(repStr)))
			return "";
		else
			return str;

	}

	/**
	 * 把Ｏbject数组转为String[]数组
	 */
	public static String[] objToString(Object[] obj) {
		if (obj == null)
			return null;
		String[] str = new String[obj.length];
		for (int i = 0; i < obj.length; i++) {
			str[i] = obj[i].toString();
		}
		return str;
	}

	/**
	 * 把一个字符数组内非空的元素重新生成一个新的数组
	 */
	public static String[] arrToarr(String[] pStr) {
		if (pStr == null)
			return null;
		StringBuffer str = new StringBuffer("");
		for (int i = 0; i < pStr.length; i++) {
			if (!isEmpty(pStr[i])) {
				str.append(pStr[i]);
				str.append(",");
			}
		}
		if (str.toString().equals(""))
			return null;
		return str.toString().split(",");
	}

	/**
	 * 查找一个数组中是否存在指定的元素
	 */
	public static boolean isExist(String[] pArr, String pStr) {
		if (pStr == null || pArr == null)
			return false;
		for (int i = 0; i < pArr.length; i++) {
			if (pStr.trim().equals(pArr[i].trim()))
				return true;
		}
		return false;
	}

	/**
	 * 初始化一维数组
	 * 
	 * @param pArr
	 *            要初始化的数组
	 * @param pStr
	 *            初始化的值
	 */
	public static String[] iniArray(String[] pArr, String pStr) {
		if (pArr == null)
			return null;
		for (int i = 0; i < pArr.length; i++) {
			pArr[i] = pStr;
		}
		return pArr;
	}

	/**
	 * 把秒转成:小时:分:秒
	 * 
	 * @param pInt
	 * @return
	 */
	public static String secondToHour(long pInt) {
		String strHour = String.valueOf(pInt / (60 * 60));
		String strMinute = String.valueOf((pInt % (60 * 60)) / 60);
		String strSecond = String.valueOf((pInt % (60 * 60)) % 60);
		return strHour + "小时:" + strMinute + "分:" + strSecond + "秒";
	}

	/**
	 * obj 转 字符串
	 * 
	 * @return
	 */
	public static String objToString(Object obj) {
		if (obj == null)
			return "";
		else
			return ((String) obj).trim();
	}

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		return isNum.matches();
	}
}
