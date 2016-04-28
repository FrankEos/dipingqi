package org.harvest.crawler.util;

import java.io.File;
import java.io.FileReader;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

public class XmlUtil {

	private final static Logger logger = Logger.getLogger(XmlUtil.class);

	public static Document parseXml(String filepath, boolean isUtf8) {

		SAXReader xmlReader = new SAXReader();

		try {
			File config = new File(XmlUtil.class.getClassLoader().getResource("../rule/" + filepath).toURI());
			if (isUtf8) {
				xmlReader.setEncoding("utf-8");
				return xmlReader.read(config);
			} else {
				xmlReader.setEncoding("gb2312");
				return xmlReader.read(new InputSource(new FileReader(config)));

			}
		} catch (DocumentException e) {
			logger.error(e.toString());
		} catch (Exception e) {
			logger.error(e.toString());
		}

		return null;
	}

	@SuppressWarnings("rawtypes")
	public static Document replaceDomAttr(Document dom, String exp, String attrName, String attvalue) {

		if (dom == null)
			return null;

		List list = dom.selectNodes(exp);
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Attribute attr = (Attribute) iter.next();
			if (attrName.equalsIgnoreCase(attr.getValue())) {
				Element element = attr.getParent();
				element.setText(attvalue);
				break;
			}

		}
		return dom;
	}

	public static Document str2dom(String xmlStr) {

		Document document = null;
		try {
			document = DocumentHelper.parseText(xmlStr);
		} catch (DocumentException e) {
			logger.error(e.getMessage());
			return null;
		}

		return document;
	}

	public static String dom2str(Document dom) {

		return dom.asXML();
	}
}
