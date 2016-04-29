package cn.dipingqi.processor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.harvest.crawler.framework.BaseProcessor;
import org.harvest.crawler.util.StringUtil;
import org.harvest.crawler.util.Util;
import org.harvest.crawler.util.XmlUtil;
import org.harvest.web.bean.UrlContent;
import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.variables.Variable;
import org.xml.sax.InputSource;

import cn.dipingqi.dao.DipingqiDBManager;

public class ContentUrlProcessor extends BaseProcessor {

	private static final String HOST = "http://www.dipingqi.net.cn";

	private static final String WEB_IMG_DIR = "/var/www/dipingqi/data/images";

	private static final String WEB_ROOT = "/var/www/dipingqi";

	DipingqiDBManager mDBManager = DipingqiDBManager.getInstance();

	HashMap<String, String> mRecordMap = new HashMap<String, String>();

	private String mHtmlText;

	@Override
	protected void extractContentUrls(UrlContent curi) {
		super.extractContentUrls(curi);

		String currentUrl = curi.getUrl();
		String contentStr = "";

		logger.debug("--- rule:" + getRulefile() + ", pageUrl:" + currentUrl);

		Scraper scraper = null;
		try {
			Document dom = XmlUtil.parseXml(this.getRulefile(), true);
			String strXml = XmlUtil.dom2str(dom);
			ScraperConfiguration config = new ScraperConfiguration(new InputSource(new StringReader(strXml)));

			scraper = new Scraper(config, "~");
			scraper.addVariableToContext("pageUrl", currentUrl);

			scraper.getHttpClientManager().getHttpClient().getParams().setConnectionManagerTimeout(16 * 1000);
			scraper.getHttpClientManager().getHttpClient().getHttpConnectionManager().getParams()
					.setSoTimeout(16 * 1000);
			scraper.getHttpClientManager().getHttpClient().getHttpConnectionManager().getParams()
					.setConnectionTimeout(16 * 1000);
			scraper.setDebug(true);

			scraper.execute();

			Variable xmlcontent = (Variable) scraper.getContext().getVar("xmlcontent");
			contentStr = xmlcontent.toString();

			xmlcontent = (Variable) scraper.getContext().getVar("htmltext");
			mHtmlText = xmlcontent.toString();

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (scraper != null)
				scraper.dispose();
		}
		if (!StringUtil.isEmpty(contentStr) && !StringUtil.isEmpty(mHtmlText)) {
			boolean success = content2database(curi, contentStr);
			if (success) {
				curi.setOper_flag(2);
			} else {
				curi.setOper_flag(3);
			}
		} else {
			curi.setOper_flag(3);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected boolean content2database(UrlContent curi, String xmlcontent) {
		Document dom = XmlUtil.str2dom(xmlcontent);
		if (dom != null) {
			Element root = dom.getRootElement();

			mRecordMap.clear();

			String content = mHtmlText;
			String title = null;
			String excerpt = null;
			String imageUrls = null;

			if (!StringUtil.isEmpty(content)) {
				content = content.replace("\t\t", "");
				mRecordMap.put("post_content", content);
			} else {
				logger.debug("content is null !");
				return false;
			}

			mRecordMap.put("post_source", curi.getUrl());
			for (Iterator iter = root.elementIterator(); iter.hasNext();) {
				Element element = (Element) iter.next();
				if ("title".equalsIgnoreCase(element.getName())) {
					title = element.getTextTrim();
					if (!StringUtil.isEmpty(title)) {
						mRecordMap.put("post_title", title);
					} else {
						logger.debug("title is null !");
						return false;
					}

				} else if ("excerpt".equalsIgnoreCase(element.getName())) {
					excerpt = element.getTextTrim();
					excerpt = excerpt.replace("\t\t", "");
					excerpt = excerpt.replace(" ", "");
					excerpt = excerpt.substring(0, excerpt.length() > 150 ? 150 : excerpt.length());
					mRecordMap.put("post_excerpt", excerpt);
				} else if ("imageUrls".equalsIgnoreCase(element.getName())) {
					imageUrls = element.getTextTrim();
				}

			}

			logger.debug(String.format("title:%s imageUrls:%s", title, imageUrls));

			// harvest
			mRecordMap.put("content_md5", Util.getMD5(content));
			if (mDBManager.isMd5Exist(mRecordMap)) {
				logger.info(title + " alreay exist , skip it !!!");
				return false;
			}

			if (StringUtil.isEmpty(imageUrls)) {
				int id = mDBManager.insert(mRecordMap, DipingqiDBManager.KNOWLEDGE);

				return (id > 0);
			} else {
				if (downloadImages(mRecordMap, imageUrls)) {
					int id = mDBManager.insert(mRecordMap, DipingqiDBManager.KNOWLEDGE);
					return (id > 0);
				} else {
					if (mDBManager.isMd5Exist(mRecordMap)) {
						mDBManager.deleteById(mRecordMap);
					}
				}
			}

		}
		logger.debug("insert into database faile !");

		return false;
	}

	public boolean downloadImages(HashMap<String, String> map, String rawUrl) {
		if (StringUtil.isEmpty(rawUrl)) {
			return false;
		}
		String content = map.get("post_content");

		String urls[] = null;
		if (rawUrl.contains(" ")) {
			urls = rawUrl.split(" ");
		} else {
			urls = new String[] { rawUrl };
		}

		for (String url : urls) {
			String suffix = url.contains(".png") ? ".png" : ".jpg";
			String fileName = String.format("%d%s", System.currentTimeMillis(), suffix);
			String path = getDownloadPath(fileName);

			String fullUrl = url;
			if (!url.startsWith("http")) {
				fullUrl = HOST + url;
			}
			// fullUrl = getDownloadUrl(fullUrl);

			logger.debug("download image from " + fullUrl + " to " + path);
			if (download(fullUrl, path)) {
				String newUrl = path.replace(WEB_ROOT, "");
				content = content.replace(url, newUrl);
			} else {
				if (download(fullUrl, path)) {
					String newUrl = path.replace(WEB_ROOT, "");
					content = content.replace(url, newUrl);
				} else {
					return false;
				}
			}
		}
		map.put("post_content", content);
		return true;
	}

	private String getDownloadPath(String fileName) {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String today = format.format(date);
		return String.format("%s%c%s%c%s", WEB_IMG_DIR, File.separatorChar, today, File.separatorChar, fileName);
	}

	private boolean download(String url, String path) {
		logger.debug("path:" + path + ", url:" + url);
		if (StringUtil.isEmpty(url) || StringUtil.isEmpty(path)) {
			return false;
		}

		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {

			HttpGet httpGet = new HttpGet(url);

			HttpResponse response = httpclient.execute(httpGet);

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
				return true;
			} else {
				if (httpclient != null) {
					httpclient.close();
				}
			}

		} catch (Exception e) {
			new File(path).deleteOnExit();
			logger.error(e.getMessage());

		} finally {
			if (httpclient != null) {
				try {
					httpclient.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}

		logger.error("--- download failed --- : " + url);
		return false;
	}

	public static String getDownloadUrl(String urlStr) {
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
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		logger.debug("downloadUrl : " + downloadUrl);

		return downloadUrl;
	}
}
