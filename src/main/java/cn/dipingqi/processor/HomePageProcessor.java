
package cn.dipingqi.processor;

import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.Element;
import org.harvest.crawler.framework.BaseProcessor;
import org.harvest.crawler.util.StringUtil;
import org.harvest.crawler.util.XmlUtil;
import org.harvest.crawler.util.format_tool;
import org.harvest.web.bean.UrlContent;

import cn.dipingqi.wrap.ScraperWrapper;

public class HomePageProcessor extends BaseProcessor {

	private Long pgmOrder;

	@Override
	protected void extractContentUrls(UrlContent curi) {
		super.extractContentUrls(curi);

		pgmOrder = Long.parseLong(format_tool.getToday() + "99999");

		String currentUrl = curi.getUrl();
		String contentStr = "";
		contentStr = ScraperWrapper.extractContent(this.getRulefile(), currentUrl);
		if (!StringUtil.isEmpty(contentStr)) {
			content2database(curi, contentStr);
		} else {
			curi.setOper_flag(4);
		}

	}

	@SuppressWarnings("rawtypes")
	@Override
	protected boolean content2database(UrlContent curi, String content) {
		Document dom = XmlUtil.str2dom(content);
		Element root = dom.getRootElement();
		for (Iterator iter = root.elementIterator(); iter.hasNext();) {
			Element element = (Element) iter.next();
			if ("catgory".equalsIgnoreCase(element.getName())) {
				String url = element.elementText("url").trim();
				if (this.controller.getFrontier().addNew(url, pgmOrder, curi.getTag(), "")) {
					this.controller.getCrawlerInf().addAllUrl();
				} else {
					this.controller.getCrawlerInf().delProUrl();
				}
				logger.debug("process url : " + url);
				pgmOrder--;
			}
		}
		return isContentAccord;
	}
}
