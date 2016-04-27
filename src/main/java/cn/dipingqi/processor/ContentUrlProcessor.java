package cn.dipingqi.processor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.harvest.crawler.dao.HarvestDBManager;
import org.harvest.crawler.framework.BaseProcessor;
import org.harvest.crawler.util.StringUtil;
import org.harvest.crawler.util.Util;
import org.harvest.crawler.util.XmlUtil;
import org.harvest.web.bean.UrlContent;
import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.exception.HttpException;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.variables.Variable;
import org.xml.sax.InputSource;

public class ContentUrlProcessor extends BaseProcessor {

	private static final String WORK_DIR = "~/harvest/feng/";

	private static final String TOP_DIR = "/home/spider/spiderdownload/aguo";

	private static final String HOST_URL = "http://down.aguo.com/d2";

	HarvestDBManager mDBManager = HarvestDBManager.getInstance();

	HashMap<String, String> mAppMap = new HashMap<String, String>();

	HashMap<String, String> mDistinctMap = new HashMap<String, String>();

	private static final Logger logger = Logger.getLogger(ContentUrlProcessor.class);

	private static final String VAR_IN_XML_APP_INFO_URL = "pageUrl";

	private static final String VAR_IN_XML_APP_INFO = "xmlcontent";

	private static final String FENG_URL = "http://game.feng.com";

	private String mSoftsay = "";

	@Override
	protected void extractContentUrls(UrlContent urlContent) {
		super.extractContentUrls(urlContent);
		Document dom = XmlUtil.parseXml(this.getRulefile(), true);
		String rule = XmlUtil.dom2str(dom);// 将dom转换成String

		ScraperConfiguration config = new ScraperConfiguration(new InputSource(new StringReader(rule)));
		Scraper scraper = new Scraper(config, WORK_DIR);

		String url = urlContent.getUrl();
		logger.debug("app url : " + url);
		try {
			extractContentAppInfo(urlContent, scraper, url);
		} catch (HttpException e) {
			logger.error("URL地址请求超时：" + e.getMessage());
			urlContent.setOper_flag(3);// 非正常下载
		} catch (IllegalArgumentException e) {
			logger.error("无效的URL地址：" + e.getMessage());
			urlContent.setOper_flag(3);// 非正常下载
		} catch (Exception e) {
			logger.error("在解析网页  " + urlContent.getUrl() + " 时发生错误 ：" + e.getMessage());
			urlContent.setOper_flag(3);// 非正常下载
		} finally {
			// 释放内存资源
			scraper.dispose();
		}
	}

	private void extractContentAppInfo(UrlContent urlContent, Scraper scraper, String value) {
		// 1. 初始化scraper
		scraper.addVariableToContext(VAR_IN_XML_APP_INFO_URL, new String(value));
		scraper.getHttpClientManager().getHttpClient().getParams().setConnectionManagerTimeout(16 * 1000);
		scraper.getHttpClientManager().getHttpClient().getHttpConnectionManager().getParams().setSoTimeout(16 * 1000);
		scraper.getHttpClientManager().getHttpClient().getHttpConnectionManager().getParams()
				.setConnectionTimeout(16 * 1000);
		scraper.setDebug(true);
		// 2. 开始抓取
		scraper.execute();

		// 3. 返回抓取的数据
		Variable xml = (Variable) scraper.getContext().getVar(VAR_IN_XML_APP_INFO);
		String content = xml.toString();

		Variable text = (Variable) scraper.getContext().getVar("softsay");
		mSoftsay = text.toString();
		// 有效性判断
		if (!StringUtil.isEmpty(content)) {
			ValidateContentAccord(content);
			if (!this.isContentAccord) {
				// 解析content xml，并将数据写入数据库
				if (!content2database(urlContent, content)) {
					urlContent.setOper_flag(3);// 非正常下载
				}
			}
		} else {
			urlContent.setOper_flag(3);// 非正常下载
		}
	}

	@Override
	protected boolean content2database(UrlContent urlContent, String content) {
		mAppMap.clear();
		// 解析xml获取应用信息
		HashMap<String, String> app = parseAppInfoFromXml(content);
		if (app.get("title").equals("unknown") || app.get("downpath").isEmpty()) {
			return false;
		}
		mAppMap.putAll(app);

		if (mDBManager.isAppExist(mAppMap, HarvestDBManager.PLATFORM_IPHONE)) {
			logger.info(mAppMap.get("title") + " alreay exist , skip it !!!");
			return false;
		}
		// 应用分类
		mAppMap.put("classid", "22");

		mDBManager.insertApp(mAppMap, HarvestDBManager.PLATFORM_IPHONE);
		int id = mDBManager.getInsertId(mAppMap, HarvestDBManager.PLATFORM_IPHONE);

		mAppMap.put("id", String.valueOf(id));
		// now process download task
		if (doDownloadIcon(mAppMap, mAppMap.get("titlepic"))) {
			if (doDownloadImages(mAppMap, mAppMap.get("morepic"))) {
				String url = mAppMap.get("downpath");
				if (url.startsWith("https://itunes.apple.com") || url.startsWith("http://itunes.apple.com/")) {
					url = "官方下载::::::" + url + "::::::0::::::0::::::";
					mAppMap.put("downpath", url);
					mDBManager.updateDownPath(mAppMap, HarvestDBManager.PLATFORM_IPHONE);
					return true;
				}
				// 下载到本地
				if (doDownloadIpa(mAppMap, url)) {
					logger.info("Download sucess: " + mAppMap.get("title"));
					return true;
				}
			}
		}
		mAppMap.put("id", String.valueOf(id));
		if (mDBManager.isIdExist(mAppMap, HarvestDBManager.PLATFORM_IPHONE)) {
			mDBManager.deleteById(mAppMap, HarvestDBManager.PLATFORM_IPHONE);
		}
		return false;
	}

	private HashMap<String, String> parseAppInfoFromXml(String xml) {
		HashMap<String, String> app = new HashMap<String, String>();
		// 将字符串转换为XML Document对象
		Document dom = XmlUtil.str2dom(xml);
		StringBuffer sb = new StringBuffer();
		sb.append("app [ ");
		Element root = dom.getRootElement();
		for (Iterator iter = root.elementIterator(); iter.hasNext();) {
			if (iter.hasNext()) {
				Element element = (Element) iter.next();
				// 应用名称
				if ("title".equalsIgnoreCase(element.getName())) {
					String title = element.getTextTrim();
					app.put("title", title.trim());
					sb.append("title : " + title);
				}
				// 应用分类
				if ("link".equalsIgnoreCase(element.getName())) {
					String link = element.getTextTrim();
					app.put("link", link.trim());
					sb.append(", link : " + link);
				}
				// 应用版本
				if ("version".equalsIgnoreCase(element.getName())) {
					String version = element.getTextTrim();
					app.put("version", version.trim());
					sb.append(", version : " + version);
				}
				// 应用图标
				if ("titlepic".equalsIgnoreCase(element.getName())) {
					String titlepic = element.getTextTrim();
					app.put("titlepic", titlepic.trim());
					sb.append(", titlepic : " + titlepic);
				}
				// 应用大小
				if ("filesize".equalsIgnoreCase(element.getName())) {
					String filesize = element.getTextTrim();
					app.put("filesize", filesize.trim());
					sb.append(", filesize : " + filesize);
				}
				// 应用评分
				if ("star".equalsIgnoreCase(element.getName())) {
					String star = element.getTextTrim();
					app.put("star", star.trim());
					sb.append(", star : " + star);
				}
				// 更新时间
				if ("update".equalsIgnoreCase(element.getName())) {
					String update = element.getTextTrim();
					app.put("update", update.trim());
					sb.append(", update : " + update);
				}
				// 下载地址
				if ("downpath".equalsIgnoreCase(element.getName())) {
					String downpath = element.getTextTrim();
					// FIXME 因为有重定向，需要转一次取到Itunes上的下载地址
					downpath = getDownloadUrl(downpath);
					app.put("downpath", downpath.trim());
					sb.append(", downpath : " + downpath);
				}
				// 应用截图
				if ("morepic".equalsIgnoreCase(element.getName())) {
					String morepic = element.getTextTrim();
					app.put("morepic", morepic.trim());
					sb.append(", morepic : " + morepic);
				}
				// FIXME 没有平台sdk信息
				if ("system".equalsIgnoreCase(element.getName())) {
					String system = element.getTextTrim();
					app.put("system", system.trim());
					sb.append(", system : " + system);
				}

			}
		}
		{
			// 游戏说明
			if (!StringUtil.isEmpty(mSoftsay)) {
				Document textDom = XmlUtil.str2dom(mSoftsay);
				String softsay = textDom.getStringValue();
				app.put("softsay", softsay.trim());
				sb.append(", softsay : " + softsay);
			}
		}

		// md5
		String md5 = Util.getMD5(app.get("title") + app.get("version"));
		app.put("md5", md5);
		sb.append(", md5 : " + md5);
		sb.append("]");
		logger.debug(sb.toString());

		return app;
	}

	private boolean doDownloadIcon(HashMap<String, String> map, String rawUrl) {
		if (!mDBManager.isIdExist(map, HarvestDBManager.PLATFORM_IPHONE)) {
			return false;
		}
		String id = map.get("id");
		String classid = map.get("classid");

		String name = classid + "_" + id;
		String suffix = rawUrl.contains(".png") ? ".png" : ".jpg";
		String iconName = name + suffix;
		String absolutePath = compileFilePath(iconName, "pic");
		String finalPath = rawUrl;

		mDistinctMap.clear();
		if (download(rawUrl, absolutePath)) {
			finalPath = absolutePath.replace(TOP_DIR, HOST_URL);
			File file = new File(absolutePath);
			String fileMd5 = Util.getFileMD5String(file);
			mDistinctMap.put("file_md5", fileMd5);
			if (mDBManager.isImgExist(mDistinctMap)) {
				file.deleteOnExit();
				finalPath = mDBManager.getImgFilePath(mDistinctMap).replace(TOP_DIR, HOST_URL);
			} else {
				mDistinctMap.put("file_abs_path", absolutePath);
				mDistinctMap.put("insert_time", String.valueOf(System.currentTimeMillis() / 1000));
				mDistinctMap.put("file_path", finalPath);
				mDBManager.insertImgMd5(mDistinctMap);
			}
		} else {
			if (download(rawUrl, absolutePath)) {
				finalPath = absolutePath.replace(TOP_DIR, HOST_URL);

				File file = new File(absolutePath);
				String fileMd5 = Util.getFileMD5String(file);
				mDistinctMap.put("file_md5", fileMd5);
				if (mDBManager.isImgExist(mDistinctMap)) {
					file.deleteOnExit();
					finalPath = mDBManager.getImgFilePath(mDistinctMap).replace(TOP_DIR, HOST_URL);
				} else {
					mDistinctMap.put("file_abs_path", absolutePath);
					mDistinctMap.put("insert_time", String.valueOf(System.currentTimeMillis() / 1000));
					mDistinctMap.put("file_path", finalPath);
					mDBManager.insertImgMd5(mDistinctMap);
				}
			} else {
				mDBManager.deleteById(map, HarvestDBManager.PLATFORM_IPHONE);
				return false;
			}
		}

		mAppMap.put("titlepic", finalPath);
		mDBManager.updateTitlePic(map, HarvestDBManager.PLATFORM_IPHONE);
		return true;
	}

	private boolean doDownloadImages(HashMap<String, String> map, String rawUrl) {
		if (!mDBManager.isIdExist(map, HarvestDBManager.PLATFORM_IPHONE)) {
			return false;
		}
		String id = map.get("id");
		String classid = map.get("classid");

		String name = classid + "_" + id;
		String urls[] = rawUrl.split(" ");
		int index = 0;
		StringBuilder sb = new StringBuilder();

		for (String url : urls) {
			mDistinctMap.clear();
			String suffix = url.contains(".png") ? ".png" : ".jpg";
			String fileName = String.format("%s_%d%s", name, index, suffix);
			String absolutePath = compileFilePath(fileName, "pic");

			String downUrl = url;
			if (!url.startsWith("http://")) {
				downUrl = FENG_URL + url;
			}
			String finalPath = downUrl;
			if (download(downUrl, absolutePath)) {
				finalPath = absolutePath.replace(TOP_DIR, HOST_URL);
				File file = new File(absolutePath);
				String fileMd5 = Util.getFileMD5String(file);
				mDistinctMap.put("file_md5", fileMd5);
				if (mDBManager.isImgExist(mDistinctMap)) {
					file.deleteOnExit();
					finalPath = mDBManager.getImgFilePath(mDistinctMap).replace(TOP_DIR, HOST_URL);
				} else {
					mDistinctMap.put("file_abs_path", absolutePath);
					mDistinctMap.put("insert_time", String.valueOf(System.currentTimeMillis() / 1000));
					mDistinctMap.put("file_path", finalPath.replace(HOST_URL, ""));
					mDBManager.insertImgMd5(mDistinctMap);
				}
			} else {
				if (download(downUrl, absolutePath)) {
					finalPath = absolutePath.replace(TOP_DIR, HOST_URL);
					File file = new File(absolutePath);
					String fileMd5 = Util.getFileMD5String(file);
					mDistinctMap.put("file_md5", fileMd5);
					if (mDBManager.isImgExist(mDistinctMap)) {
						file.deleteOnExit();
						finalPath = mDBManager.getImgFilePath(mDistinctMap).replace(TOP_DIR, HOST_URL);
					} else {
						mDistinctMap.put("file_abs_path", absolutePath);
						mDistinctMap.put("insert_time", String.valueOf(System.currentTimeMillis() / 1000));
						mDistinctMap.put("file_path", finalPath.replace(HOST_URL, ""));
						mDBManager.insertImgMd5(mDistinctMap);
					}
				} else {
					mDBManager.deleteById(map, HarvestDBManager.PLATFORM_IPHONE);
					return false;
				}
			}

			if (index > 0) {
				sb.append("::::::");
				sb.append(finalPath);
			} else {
				sb.append(finalPath);
			}
			index++;
		}

		String morepic = sb.toString();
		mAppMap.put("morepic", morepic);
		mDBManager.updateMorePic(map, HarvestDBManager.PLATFORM_IPHONE);
		return true;
	}

	private boolean doDownloadIpa(HashMap<String, String> map, String rawUrl) {
		if (!mDBManager.isIdExist(map, HarvestDBManager.PLATFORM_IPHONE)) {
			return false;
		}
		if (rawUrl.isEmpty()) {
			logger.debug("download url is empty.");
			return false;
		}

		String id = map.get("id");
		String classid = map.get("classid");

		String name = classid + "_" + id;
		String appName = name + ".ipa";

		String absolutePath = compileFilePath(appName, "ipa");
		String finalPath = rawUrl;

		mDistinctMap.clear();
		if (download(rawUrl, absolutePath)) {
			finalPath = absolutePath.replace(TOP_DIR, HOST_URL);
			File file = new File(absolutePath);
			String fileMd5 = Util.getFileMD5String(file);

			mDistinctMap.put("file_md5", fileMd5);
			if (mDBManager.isAppExist(mDistinctMap, HarvestDBManager.PLATFORM_IPHONE)) {
				file.deleteOnExit();
				finalPath = mDBManager.getAppFilePath(mDistinctMap, HarvestDBManager.PLATFORM_IPHONE).replace(TOP_DIR,
						HOST_URL);
			} else {
				mDistinctMap.put("file_abs_path", absolutePath);
				mDistinctMap.put("insert_time", String.valueOf(System.currentTimeMillis() / 1000));
				mDistinctMap.put("file_path", finalPath);
				mDBManager.insertAppMd5(mDistinctMap, HarvestDBManager.PLATFORM_IPHONE);
			}
		} else {

			if (download(rawUrl, absolutePath)) {
				finalPath = absolutePath.replace(TOP_DIR, HOST_URL);
				File file = new File(absolutePath);
				String fileMd5 = Util.getFileMD5String(file);

				mDistinctMap.put("file_md5", fileMd5);
				if (mDBManager.isAppExist(mDistinctMap, HarvestDBManager.PLATFORM_IPHONE)) {
					file.deleteOnExit();
					finalPath = mDBManager.getAppFilePath(mDistinctMap, HarvestDBManager.PLATFORM_IPHONE).replace(TOP_DIR,
							HOST_URL);
				} else {
					mDistinctMap.put("file_abs_path", absolutePath);
					mDistinctMap.put("insert_time", String.valueOf(System.currentTimeMillis() / 1000));
					mDistinctMap.put("file_path", finalPath);
					mDBManager.insertAppMd5(mDistinctMap, HarvestDBManager.PLATFORM_IPHONE);
				}
			} else {
				mDBManager.deleteById(map, HarvestDBManager.PLATFORM_IPHONE);
				return false;
			}
		}

		finalPath = "本地下载::::::" + finalPath + "::::::0::::::0::::::";
		mAppMap.put("ipadown", finalPath);
		mDBManager.updateIpaDown(map, HarvestDBManager.PLATFORM_IPHONE);

		return true;
	}

	private String compileFilePath(String fileName, String type) {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String today[] = format.format(date).split("-");
		return TOP_DIR + File.separatorChar + "iPhone" + File.separatorChar + type + File.separatorChar + today[0]
				+ File.separatorChar + today[1] + File.separatorChar + today[2] + File.separatorChar + fileName;
	}

	private boolean download(String url, String path) {
		logger.debug("path:" + path + ", url:" + url);
		if (StringUtil.isEmpty(url) || StringUtil.isEmpty(path)) {
			return false;
		}

		HttpClient client = null;
		try {

			client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			HttpResponse response = client.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				byte[] result = EntityUtils.toByteArray(response.getEntity());
				BufferedOutputStream bw = null;
				File f = new File(path);
				if (!f.getParentFile().exists())
					f.getParentFile().mkdirs();
				bw = new BufferedOutputStream(new FileOutputStream(path));
				bw.write(result);
				if (bw != null)
					bw.close();
			} else {
				if (client != null) {
					client.getConnectionManager().shutdown();
				}
				return false;
			}

		} catch (Exception e) {
			new File(path).deleteOnExit();
			logger.error(e.getMessage());
			logger.error("!!!!!!!!!!!!!!!!!! download file failed:" + url);
			return false;
		} finally {
			if (client != null) {
				client.getConnectionManager().shutdown();
			}
		}

		return true;
	}

	private static String getDownloadUrl(String urlStr) {
		String downloadUrl = "";
		HttpURLConnection conn = null;
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(20000);
			conn.setReadTimeout(5000);
			conn.setRequestMethod("HEAD");
			int responseCode = conn.getResponseCode();
			if (200 == responseCode || 302 == responseCode) {
				downloadUrl = conn.getHeaderField("Location");
			}
		} catch (Exception e) {
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		logger.debug("downloadUrl : " + downloadUrl);

		return downloadUrl;
	}
}
