package org.harvest.crawler.framework;

import org.harvest.web.bean.UrlContent;

public abstract class Frontier {

	public static final String ATTR_NAME = "frontier";

	public abstract UrlContent next();

	/**
	 * 回填一个已处理的url信息,包括花费时间及处理结果
	 * 
	 */
	public abstract void finished(UrlContent cURI);

	/**
	 * 已经添加到内容表中的url
	 */
	public abstract int discoveredUriCount();

	/**
	 * 已经添加到内容表中的已处理url
	 */
	public abstract int discoveredProcUriCount();

	/**
	 * 添加一个要处理的url
	 */
	public abstract boolean addNew(String url, Long order, String tag, String bak) ;

	/**
	 * 判断是否一个网站抓取结束 即 1.判断种子是否都执行完了. 2.再判断队列是否完了
	 */
	public abstract boolean isEmpty();

	/**
	 * 得到任务队列中的Size大小
	 * 
	 * @return 队列Size
	 */
	public abstract int getQueSize();

}
