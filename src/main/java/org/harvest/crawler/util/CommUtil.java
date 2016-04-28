package org.harvest.crawler.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;

public class CommUtil {

	private static final Logger logger = Logger.getLogger(CommUtil.class);

	public static String str2digi(String str) {

		return str = isBlank(str) ? "0" : str.replaceAll("[^0-9]", "");
	}

	/**
	 * @param curUrl
	 *            当前Url
	 * @param homeUrl
	 *            站点Url
	 * @return 重写后的Url
	 * @throws Exception
	 */
	public static final String rewriteUrl(String curUrl, String homeUrl) throws Exception {

		if (!homeUrl.startsWith("http") && !homeUrl.endsWith("/"))
			throw new Exception("Home url must starts with 'http' and ends with '/'");

		if (curUrl.startsWith("http")) {// 完整路径
			return curUrl;
		} else {// 相对路径
			if (curUrl.startsWith("../")) {
				homeUrl = homeUrl.substring(0, homeUrl.lastIndexOf("/"));// 当前目录
				homeUrl = homeUrl.substring(0, homeUrl.lastIndexOf("/"));// 上级目录
				curUrl = curUrl.substring(curUrl.indexOf("../") + 1);
			} else {
				if (curUrl.startsWith("./")) {
					curUrl = curUrl.substring(curUrl.indexOf("./") + 1);
				} else {
					if (curUrl.startsWith("/")) {
						curUrl = curUrl.substring(1);
					}
				}
			}
		}
		// 经过处理后,homeUrl可能不是以'/'结束
		if (!homeUrl.endsWith("/"))
			homeUrl = homeUrl + "/";

		return homeUrl + curUrl;
	}

	/**
	 * 通过URL下载一个文件存放本地
	 * 
	 * @return true 下载成功
	 */
	public static final boolean downFile(String url, String fileName) {
		// 通过URL下载一个文件存放本地

		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		HttpURLConnection httpUrl = null;
		URL url1 = null;
		byte[] buf = new byte[8096];
		int size = 0;

		String fileParrent = fileName.substring(0, fileName.lastIndexOf("/"));

		// 建立链接
		try {
			File parrent = new File(fileParrent);

			if (!parrent.isDirectory())
				parrent.mkdirs();

			url1 = new URL(url.replaceAll(" ", "%20"));
			httpUrl = (HttpURLConnection) url1.openConnection();
			httpUrl.connect();
			bis = new BufferedInputStream(httpUrl.getInputStream());
			fos = new FileOutputStream(fileName);
			// System.out.println("正在获取链接[" + url + "]的内容...\n将其保存为文件[" +
			// fileName
			// + "]");

			// 保存文件
			while ((size = bis.read(buf)) != -1)
				fos.write(buf, 0, size);

			fos.close();
			bis.close();
			httpUrl.disconnect();
		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
			return false;
		} catch (IOException e1) {
			logger.error(e1.getMessage());
			return false;
		}

		return true;
	}

	// /*长寿下载*/
	// public static final boolean downFile(String url, String fileName,String adductionUrl, String xmlName,String rootUrl) {
	// // 通过URL下载一个文件存放本地
	// // String xmlName="";
	// FileOutputStream fos = null;
	// BufferedInputStream bis = null;
	// HttpURLConnection httpUrl = null;
	// URL url1 = null;
	// byte[] buf = new byte[8096];
	// int size = 0;
	// Document dom = XmlUtil.parseXml(xmlName, true);
	//
	// List list= getAllNodes(dom);
	//
	// String fileParrent = fileName.substring(0, fileName.lastIndexOf("/"));
	// HttpClient httpClient = new HttpClient();
	// // 建立链接
	// try {
	// File parrent = new File(fileParrent);
	//
	// if (!parrent.isDirectory())
	// parrent.mkdirs();
	//
	// String loginUrl = (String)list.get(0);
	//
	// PostMethod postMethod = new PostMethod(loginUrl);
	//
	// NameValuePair[] data = { new NameValuePair("username", (String)list.get(1)),
	// new NameValuePair("password", (String)list.get(2)), };
	// postMethod.setRequestBody(data);
	//
	// httpClient.executeMethod(postMethod);
	// Cookie[] cookies = httpClient.getState().getCookies();
	// httpClient.getState().addCookies(cookies);
	// if (url.lastIndexOf("viewFile.asp") != -1)
	// url = url.substring(url.lastIndexOf("viewFile.asp"), url
	// .length());
	// postMethod.releaseConnection();
	// GetMethod redirect1 =null;
	// if(rootUrl!=null){
	// redirect1 = new GetMethod(rootUrl + url);
	// redirect1.setRequestHeader("Cookie", cookies.toString());
	// redirect1.addRequestHeader("Referer", rootUrl + adductionUrl);
	// }
	// else{
	// redirect1 = new GetMethod( url);
	// redirect1.setRequestHeader("Cookie", cookies.toString());
	// redirect1.addRequestHeader("Referer",adductionUrl);
	// }
	// httpClient.executeMethod(redirect1);
	//
	// if (!parrent.isDirectory())
	// parrent.mkdirs();
	// fos = new FileOutputStream(fileName);
	//
	// fos.write(redirect1.getResponseBody());
	// fos.close();
	// } catch (MalformedURLException e) {
	// logger.error(e.getMessage());
	// return false;
	// } catch (IOException e1) {
	// logger.error(e1.getMessage());
	// return false;
	// }
	//
	// return true;
	// }

	/**
	 * @param str
	 *            需要截短的字符串
	 * @param len
	 *            期望的字符串长度
	 * @return 期望长度的字符串
	 */
	public static String getSubstring(String str, int len) {

		if (CommUtil.isBlank(str))
			return "";

		return str.length() > len ? str.substring(0, len - 1) : str;
	}

	// 半角转全角
	public static final String BQchange(String QJstr) {
		String outStr = "";
		String Tstr = "";
		byte[] b = null;

		for (int i = 0; i < QJstr.length(); i++) {
			try {
				Tstr = QJstr.substring(i, i + 1);
				b = Tstr.getBytes("unicode");
			} catch (java.io.UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			if (b[3] != -1) {
				b[2] = (byte) (b[2] - 32);
				b[3] = -1;
				try {
					outStr = outStr + new String(b, "unicode");
				} catch (java.io.UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else
				outStr = outStr + Tstr;
		}

		return outStr;
	}

	// 全角转半角
	public static final String QBchange(String QJstr) {
		String outStr = "";
		String Tstr = "";
		byte[] b = null;

		for (int i = 0; i < QJstr.length(); i++) {
			try {
				Tstr = QJstr.substring(i, i + 1);
				b = Tstr.getBytes("unicode");
			} catch (java.io.UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			if (b[3] == -1) {
				b[2] = (byte) (b[2] + 32);
				b[3] = 0;
				try {
					outStr = outStr + new String(b, "unicode");
				} catch (java.io.UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else
				outStr = outStr + Tstr;
		}

		// 把两种空格乱码转换成半角空格 及 ：符号
		return outStr.replaceAll("　", " ").replaceAll(" ", " ").replaceAll("：", ":");
	}

	public static String getFileMD5(String filename) {

		try {
			return calcMD5(getFileByte(filename));
		} catch (IOException e) {
			logger.error(e.toString());

		}
		return null;
	}

	private static byte[] getFileByte(String fileName) throws IOException {
		FileInputStream inputtextfile = new FileInputStream(fileName);
		int len = inputtextfile.available();
		BufferedInputStream buffer = new BufferedInputStream(inputtextfile);
		byte bufferArray[] = new byte[len];
		int n = 0;
		while ((n = buffer.read(bufferArray)) != -1)
			new String(bufferArray, 0, n);
		buffer.close();
		inputtextfile.close();
		return bufferArray;
	}

	/**
	 * MD5 加密
	 * 
	 * @param data
	 * @return
	 */
	private static String calcMD5(byte[] data) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			return "";
		}

		// 生成MessageDigest
		md.update(data);
		byte[] hash = md.digest();

		// 转换为字符串
		StringBuffer sbRet = new StringBuffer();
		for (int i = 0; i < hash.length; i++) {
			int v = hash[i] & 0xFF;
			if (v < 16)
				sbRet.append("0");
			sbRet.append(Integer.toString(v, 16));
		}

		return sbRet.toString();
	}

	/**
	 * 
	 * @param str
	 * @return true if str is blank
	 */
	public static boolean isBlank(String str) {

		return str == null ? true : str.trim().equals("") ? true : str.equalsIgnoreCase("null") ? true : false;

	}

	public static String makeLocalDir(int fileCount) {

		return "/" + (int) fileCount / 10000;
	}

	public static String makeLocalFile(int fileCount) {

		return "/" + (int) fileCount % 10000;
	}

	/**
	 * @param filePath
	 *            完整文件路径
	 * @return 文件所在的目录
	 */
	public static String getFileParentPath(String filePath) {

		if (CommUtil.isBlank(filePath)) {
			return "";
		} else if (filePath.lastIndexOf("/") > 0) {
			return filePath.substring(0, filePath.lastIndexOf("/"));

		} else {
			return filePath;
		}

	}
	/**
	 * 取出一个字符串中的数字 返回这个数字的值
	 * 
	 * @param str
	 * @return
	 */
	public static int takeDigit(String str) {

		String digit = "0";

		for (int i = 0; i < str.length(); i++) {
			char j = str.charAt(i);
			if (j >= '0' && j <= '9') {
				digit = digit + j;
			}
		}
		return Integer.parseInt(digit);
	}

	@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
	private static List<String> getAllNodes(Document dom) {
		List<String> conList = new ArrayList();
		List<Element> list = dom.selectNodes("//config");
		Element node = (Element) list.get(0);
		for (Iterator i = node.elementIterator("http"); i.hasNext();) {
			Element newNode = (Element) i.next();
			conList.add(newNode.attributeValue("url"));
			for (Iterator m = newNode.elementIterator("http-param"); m.hasNext();) {
				Element childNode = (Element) m.next();
				if (("username").equals(childNode.attributeValue("name")) || ("password").equals(childNode.attributeValue("name")))
					conList.add(childNode.getText());
			}
		}
		return conList;
	}

}
