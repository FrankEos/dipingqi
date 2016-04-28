package org.harvest.crawler.framework;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.harvest.crawler.util.CommUtil;
import org.harvest.crawler.util.XmlUtil;
import org.harvest.web.bean.UrlContent;
import org.harvest.web.util.MD5Tool;
import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.exception.HttpException;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.variables.Variable;
import org.xml.sax.InputSource;

public abstract class BaseProcessor {

	public static final Logger logger = Logger.getLogger(BaseProcessor.class);

	// start statistics
	// 爬取内容统计
	protected Integer totalCount = 0;

	protected Integer localCount = 0;

	protected Integer procsCount = 0;

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getLocalCount() {
		return localCount;
	}

	public void setLocalCount(Integer localCount) {
		this.localCount = localCount;
	}

	public Integer getProcsCount() {
		return procsCount;
	}

	public void setProcsCount(Integer procsCount) {
		this.procsCount = procsCount;
	}

	/**
	 * 当前蜘蛛控制器
	 */
	protected CrawlController controller;

	/**
	 * 当前要处理的UrlContent
	 */
	protected UrlContent currentCuri;

	/**
	 * 当前内容是否和以前处理内容是否一致的标识
	 * 
	 * @return true 是一致的<处理器就不需要处理当前的内容了> flase 不一致<处理就要处理新的内容>
	 */
	protected boolean isContentAccord = false;

	/**
	 * 处理器要使用的规则文件
	 */
	protected String rulefile = null;

	/**
	 * 执行得到的url处理
	 */
	public final void process(UrlContent curi) {
		currentCuri = curi;
		extractContentUrls(curi);
	}

	/**
	 * 处理URL，现实类具体实现
	 */
	protected void extractContentUrls(UrlContent curi) {

	}

	/**
	 * 内容入库
	 * 
	 * @param content
	 * @return
	 */
	protected abstract boolean content2database(UrlContent curi, String content);

	/**
	 * 根据规则文件抓取内容，现实类具体实现
	 * 
	 * @param strXml
	 *            规则文件
	 * @return 返回按规则提取的数据
	 */
	@Deprecated
	protected String harvest(UrlContent curi, String strXml) {
		// 提取引擎读取配置文件，并生成指定的数据文件
		ScraperConfiguration config = new ScraperConfiguration(new InputSource(new StringReader(strXml)));
		Scraper scraper = new Scraper(config, "C:/");
		try {
			scraper.addVariableToContext("pageUrl", new String(curi.getUrl()));
			scraper.addVariableToContext("xmlcontent", new String(""));
			scraper.getHttpClientManager().getHttpClient().getParams().setConnectionManagerTimeout(16 * 1000);
			scraper.getHttpClientManager().getHttpClient().getHttpConnectionManager().getParams()
					.setSoTimeout(16 * 1000);
			scraper.getHttpClientManager().getHttpClient().getHttpConnectionManager().getParams()
					.setConnectionTimeout(16 * 1000);
			scraper.setDebug(true);
			scraper.execute();

			Variable xmlcontent = (Variable) scraper.getContext().getVar("xmlcontent");

			// 将提取到的xml内容做MD5运算
			ValidateContentAccord(xmlcontent.toString());

			return xmlcontent.toString();
		} catch (HttpException e) {
			logger.error("URL地址请求超时：" + e.getMessage());
			return "";// 连接超时，返回空。
		} catch (IllegalArgumentException e) {
			logger.error("无效的URL地址：" + e.getMessage());
			return "";// 连接超时，返回空。
		} catch (Exception e) {
			logger.error("在解析网页  " + this.currentCuri.getUrl() + " 时发生错误 ：" + e.getMessage());
		} finally {
			// 释放内存资源
			scraper.dispose();
		}
		return "";
	}

	/**
	 * 是否是增量采集
	 * 
	 * @param pageNumer
	 * @return
	 */
	@Deprecated
	protected boolean isIncrement(Integer pageNumer) {
		return ((controller.isByhand() && !(controller.getCrawlerInf().getPortal().getIsAllByHand() == 0)
				&& pageNumer > controller.getCrawlerInf().getPortal().getIncrementPageCount())
				|| (!controller.isByhand() && (controller.getCrawlerInf().getPortal().getIsAllByHand() == 0)
						&& pageNumer > controller.getCrawlerInf().getPortal().getIncrementPageCount()));
	}

	/**
	 * 设置当前内容是否和以前处理的一致。通过传入《body内容》来判断。如果内容为空并判断是否产生IP锁定
	 */
	public void ValidateContentAccord(String content) {

		String newMd5 = MD5Tool.calcMD5(content);
		String oldMd5 = this.currentCuri.getContent_md5();
		this.currentCuri.setContent_md5(newMd5);
		// 初次存储要检测的url时，MD5值为空。当发送检测命令时，检测url并计算MD5值。如果新旧MD5值一致，则该url已经检测过。如果不一样，则这次发送的命令与上次发送的不一致，需要重新检测
		if (CommUtil.isBlank(oldMd5)) {
			this.isContentAccord = false;
			return;
		}

		if (oldMd5.equals(newMd5)) {
			this.isContentAccord = true;
			return;
		}

		this.isContentAccord = false;
	}

	/**
	 * 判断IP是否锁定或者网断了
	 */
	@Deprecated
	public final void isIPBlock() {
		// 百度不能访问 认为网断 设置ip锁定为true
		// 如果百度可以访问 currentCuri不能访问 controller.IP_BLOCK_OUT=true;

		String url = this.currentCuri.getUrl();

		if (url.length() < 8)
			return;

		// 得到主页地址
		String temp = url.substring(7);
		url = url.substring(0, temp.indexOf("/") + 7);

		try {
			URL cUrl = new URL(url);

			URL baidu = new URL("http://www.baidu.com");

			InputStream input = cUrl.openStream();

			InputStream inbaidu = baidu.openStream();

			boolean flag = false;

			if (inbaidu == null || (inbaidu != null && input == null))
				flag = true;

			input.close();
			inbaidu.close();

			this.controller.getCrawlerInf().setIpLock(flag);

		} catch (MalformedURLException e) {
			logger.error("Malformed URL ..." + e.toString());
			this.controller.getCrawlerInf().setIpLock(false);
		} catch (IOException e) {
			logger.error("Please check your net config...");
			this.controller.getCrawlerInf().setIpLock(false);
		}
	}

	public CrawlController getController() {
		return controller;
	}

	public void setController(CrawlController controller) {
		this.controller = controller;
	}

	public UrlContent getCurrentCuri() {
		return currentCuri;
	}

	public void setCurrentCuri(UrlContent currentCuri) {
		this.currentCuri = currentCuri;
	}

	public boolean isContentAccord() {
		return isContentAccord;
	}

	public void setContentAccord(boolean isContentAccord) {
		this.isContentAccord = isContentAccord;
	}

	public String getRulefile() {
		return rulefile;
	}

	public void setRulefile(String rulefile) {
		this.rulefile = rulefile;
	}

	// common code
	protected static final String workingDir = "/home/spider/spiderdownload/xunjk/workdir";

	protected static final String rootDir = "/home/spider/spiderdownload/xunjk";

	protected static final String siteDir = "/upload/health/xunjk";

	protected static final String siteUrl = "http://m.xunjk.com";

	protected final boolean isDebug = false;

	protected final int TIMEOUT_PARAM = 60 * 1000;

	public Scraper createScraper() {
		Document dom = XmlUtil.parseXml(this.getRulefile(), true);
		ScraperConfiguration config = new ScraperConfiguration(new InputSource(new StringReader(XmlUtil.dom2str(dom))));
		Scraper scraper = new Scraper(config, workingDir);
		return scraper;
	}

	public String getXmlContent(Scraper scraper, String type, String url) {
		scraper.addVariableToContext(type, url);
		scraper.getHttpClientManager().getHttpClient().getParams().setConnectionManagerTimeout(TIMEOUT_PARAM);
		scraper.getHttpClientManager().getHttpClient().getHttpConnectionManager().getParams()
				.setSoTimeout(TIMEOUT_PARAM);
		scraper.getHttpClientManager().getHttpClient().getHttpConnectionManager().getParams()
				.setConnectionTimeout(TIMEOUT_PARAM);
		scraper.setDebug(isDebug);
		scraper.execute();
		Variable xmlContent = (Variable) scraper.getContext().getVar("xmlcontent");
		scraper.dispose();
		return xmlContent.toString();
	}
}
