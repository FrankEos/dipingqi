package cn.dipingqi.wrap;

import java.io.StringReader;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.harvest.crawler.util.StringUtil;
import org.harvest.crawler.util.XmlUtil;
import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.variables.Variable;
import org.xml.sax.InputSource;

public class ScraperWrapper {
	protected static final Logger logger = Logger.getLogger(ScraperWrapper.class);

	public static String extractContent(String configFile, String pageUrl) {
		logger.debug("--- rule:" + configFile + ", pageUrl:" + pageUrl);

		if (StringUtil.isEmpty(configFile) || StringUtil.isEmpty(pageUrl)) {
			return null;
		}

		Scraper scraper = null;
		String content = null;
		try {
			Document dom = XmlUtil.parseXml(configFile, true);
			String strXml = XmlUtil.dom2str(dom);
			ScraperConfiguration config = new ScraperConfiguration(new InputSource(new StringReader(strXml)));

			scraper = new Scraper(config, "~");
			scraper.addVariableToContext("pageUrl", pageUrl);

			scraper.getHttpClientManager().getHttpClient().getParams().setConnectionManagerTimeout(16 * 1000);
			scraper.getHttpClientManager().getHttpClient().getHttpConnectionManager().getParams()
					.setSoTimeout(16 * 1000);
			scraper.getHttpClientManager().getHttpClient().getHttpConnectionManager().getParams()
					.setConnectionTimeout(16 * 1000);
			// scraper.setDebug(true);

			scraper.execute();

			Variable xmlcontent = (Variable) scraper.getContext().getVar("xmlcontent");
			content = xmlcontent.toString();

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (scraper != null)
				scraper.dispose();
		}

		logger.debug("--- xmlcontent --- :" + content);

		return content;
	}
}
