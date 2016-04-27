
package cn.dipingqi.processor;

import java.io.StringReader;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.Element;
import org.harvest.crawler.framework.BaseProcessor;
import org.harvest.crawler.util.StringUtil;
import org.harvest.crawler.util.XmlUtil;
import org.harvest.crawler.util.format_tool;
import org.harvest.web.bean.UrlContent;
import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.ScraperContext;
import org.webharvest.runtime.variables.Variable;
import org.xml.sax.InputSource;

public class ListUrlProcessor extends BaseProcessor {

	private static final String WORK_DIR = "~/harvest/feng/";

	private Long pgmOrder;

	@Override
	protected void extractContentUrls(UrlContent curi) {
		super.extractContentUrls(curi);

		pgmOrder = Long.parseLong(format_tool.getToday() + "99999");

		Document dom = XmlUtil.parseXml(getRulefile(), true);// 替换提取规则文件中的URL
		String strXml = XmlUtil.dom2str(dom);// 将dom转换成String

		String currentUrl = curi.getUrl();
		String contentStr = "";
		int pageNumer = 1;

		int start = currentUrl.lastIndexOf('/');
		int end = currentUrl.lastIndexOf('-');
		String pagePrefix = currentUrl.substring(0, start);
		String suffix = currentUrl.substring(end);
		currentUrl = String.format("%s/index-page-%d-type%s", pagePrefix, pageNumer, suffix);
		do {
			// 提取引擎读取配置文件，并生成指定的数据文件
			Scraper scraper = new Scraper(new ScraperConfiguration(new InputSource(new StringReader(strXml))),
					WORK_DIR);
			logger.debug(Thread.currentThread() + " currentUrl:" + currentUrl);
			try {
				scraper.addVariableToContext("pageUrl", new String(currentUrl));
				scraper.addVariableToContext("xmlcontent", new String(""));
				scraper.getHttpClientManager().getHttpClient().getParams().setConnectionManagerTimeout(16 * 1000);
				scraper.getHttpClientManager().getHttpClient().getHttpConnectionManager().getParams()
						.setSoTimeout(16 * 1000);
				scraper.getHttpClientManager().getHttpClient().getHttpConnectionManager().getParams()
						.setConnectionTimeout(16 * 1000);
				scraper.setDebug(true);
				scraper.execute();

				ScraperContext context = scraper.getContext();
				Variable xmlcontent = (Variable) context.getVar("xmlcontent");
				if (StringUtil.isEmpty(xmlcontent) || xmlcontent.toList().size() < 2
						|| contentStr.equals(xmlcontent.toString())) {
					curi.setOper_flag(3);
					break;
				} else {
					contentStr = xmlcontent.toString();
				}

				content2database(curi, contentStr);

				// if (isIncrement(pageNumer)) {
				// break;// 增量采集
				// } // debug on

				currentUrl = String.format("%s/index-page-%d-type%s", pagePrefix, ++pageNumer, suffix);

			} catch (Exception e) {
				curi.setOper_flag(4);
				logger.error("需要解析的List网页 ： " + currentUrl + "  不能访问或者无法解析出节目数据", e);
			} finally {
				scraper.dispose();
			}

		} while (true);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected boolean content2database(UrlContent curi, String content) {
		Document dom = XmlUtil.str2dom(content);
		Element root = dom.getRootElement();
		for (Iterator iter = root.elementIterator(); iter.hasNext();) {
			Element element = (Element) iter.next();
			if ("list".equalsIgnoreCase(element.getName())) {
				String detailStr = element.elementText("detail").trim();
				if (this.controller.getFrontier().addNew(detailStr, pgmOrder, curi.getTag(), "")) {
					this.controller.getCrawlerInf().addAllUrl();// 状态变更-添加一个发现的url
				} else {
					this.controller.getCrawlerInf().delProUrl();// 未处理数增加(已处理减少)
				}
				logger.debug(Thread.currentThread() + "::" + pgmOrder + "::" + detailStr);
				pgmOrder--;
			}
		}
		return isContentAccord;
	}
}
