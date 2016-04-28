package org.harvest.crawler.framework;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;
import org.harvest.web.bean.PortalProcessor;
import org.harvest.web.bean.UrlContent;
import org.harvest.web.dao.IPortalProcessorDao;
import org.harvest.web.dao.IUrlContentDao;
import org.harvest.web.util.MD5Tool;

public class DBFrontier extends Frontier {

	private static Logger logger = Logger.getLogger(DBFrontier.class);

	private IPortalProcessorDao mPortalProcessorDao;

	private IUrlContentDao mContentDao;

	protected int portalId;

	private boolean finishSeed = false;// 添加完所有的URL

	protected ConcurrentLinkedQueue<UrlContent> urlQue = new ConcurrentLinkedQueue<UrlContent>();// url队列

	protected Map<String, String> urlHash = new ConcurrentHashMap<String, String>();// 用于重复url校验

	public DBFrontier(int portalId) {
		this.portalId = portalId;

		mPortalProcessorDao = (IPortalProcessorDao) ToolSpring.getBean(IPortalProcessorDao.class);
		mContentDao = (IUrlContentDao) ToolSpring.getBean(IUrlContentDao.class);

		// 添加一个空URL 来触发一个删除处理器
		UrlContent delUrl = new UrlContent();
		delUrl.setUrl("");
		urlQue.add(delUrl);

	}

	public DBFrontier() {

	}

	/**
	 * 回填一个已处理的url信息,包括花费时间及处理结果
	 * 
	 */
	public void finished(UrlContent uc) {
		mContentDao.updateContent(this.portalId, uc);// 写入数据库状态

		StringBuffer strBuf = new StringBuffer("处理完一个URL:");
		strBuf.append(uc.getUrl()).append(" 内容MD5:").append(uc.getContent_md5());
		logger.debug(strBuf.toString());
	}

	// 取下一个URL,如果没有返回null
	public synchronized UrlContent next() {

		if (urlQue.size() < 1) {
			// 添加500个url到队列中去
			List<UrlContent> list = null;

			list = mContentDao.queryValidUrlContent(this.portalId, 0);// 获取ListUrl
			if (list == null || list.size() <= 0) {
				list = mContentDao.queryValidUrlContent(this.portalId, 1);// 获取ContentUrl
			}

			for (UrlContent uc : list) {
				if (!urlHash.containsKey(uc.getUrl_md5())){// 过滤加进重复的
					if (!urlQue.contains(uc)) {
						urlQue.add(uc);
					}
					urlHash.put(uc.getUrl_md5(), "1");
				}else{
					continue;
				}
			}
			// 判断是否所有种子和URL已下载完成
			if (!finishSeed) {
				if (list.size() == 0) {
					finishSeed = true;
				} else {
					finishSeed = false;
				}
			}
		}

		UrlContent urlc = urlQue.poll();
		if (urlc != null) {
			StringBuffer strBuf = new StringBuffer("取处理的URL:");
			strBuf.append(urlc.getUrl()).append(" 任务队列中还有任务：").append(urlQue.size());
			logger.debug(strBuf.toString());
			if (urlc.getUrl_md5() != null) {
				urlHash.remove(urlc.getUrl_md5());
			}
		}
		return urlc;
	}

	/**
	 * 判断是否一个网站抓取结束 即1.判断数据库中种子和URL是否都执行完了. 2.再判断队列是否完了
	 */
	public boolean isEmpty() {
		// 1.判断数据库中种子和URL是否都执行完了. 2.再判断队列是否完了
		if (finishSeed && urlQue.isEmpty()) {
			logger.debug("队列中已没有url.   finishSeed:" + finishSeed + " urlQue.isEmpty():" + urlQue.isEmpty());

			return true;
		}
		return false;
	}

	/**
	 * 得到任务队列中的Size大小
	 * 
	 * @return 队列Size
	 */
	public int getQueSize() {
		return urlQue.size();
	}

	public int getSeqId() {
		return portalId;
	}

	@Override
	public boolean addNew(String url, Long order, String tag, String bak) {
		boolean new_flag = false;
		// 1 添加到数据库
		UrlContent uc = mContentDao.queryUrlContent(this.portalId, url, tag);
		if(uc != null){
			uc.setOper_flag(1);
			uc.setOrder(order);
			uc.setBak(bak);
			mContentDao.updateContent(this.portalId, uc);
			new_flag = false;
		}else{
			uc = new UrlContent();
			uc.setUrl_md5(MD5Tool.calcMD5(url + tag));
			uc.setUrl(url);
			uc.setUrl_type(1);
			uc.setCost_time(0);
			uc.setOper_flag(1);
			uc.setContent_md5("");
			uc.setUpdate_time(System.currentTimeMillis());
			uc.setTag(tag);
			uc.setOrder(order);
			uc.setBak(bak);
			mContentDao.addContent(this.portalId, uc);
			new_flag = true;
		}
		// 2 添加到队列
		if (urlHash.containsKey(uc.getUrl_md5())) {// 过滤加进重复的
			urlQue.remove(uc);
			urlHash.remove(uc.getUrl_md5());
		}
		urlQue.add(uc);
		urlHash.put(uc.getUrl_md5(), "1");
		return new_flag;
	}
	
	public boolean addNew(String url, Long order, String tag, String bak,Integer type) {
		boolean new_flag = false;
		// 1 添加到数据库
		UrlContent uc = mContentDao.queryUrlContent(this.portalId, url, tag);
		if(uc != null){
			uc.setOper_flag(1);
			uc.setOrder(order);
			uc.setBak(bak);
			mContentDao.updateContent(this.portalId, uc);
			new_flag = false;
		}else{
			uc = new UrlContent();
			uc.setUrl_md5(MD5Tool.calcMD5(url + tag));
			uc.setUrl(url);
			uc.setUrl_type(type);
			uc.setCost_time(0);
			uc.setOper_flag(1);
			uc.setContent_md5("");
			uc.setUpdate_time(System.currentTimeMillis());
			uc.setTag(tag);
			uc.setOrder(order);
			uc.setBak(bak);
			mContentDao.addContent(this.portalId, uc);
			new_flag = true;
		}
		// 2 添加到队列
		if (urlHash.containsKey(uc.getUrl_md5())) {// 过滤加进重复的
			urlQue.remove(uc);
			urlHash.remove(uc.getUrl_md5());
		}
		urlQue.add(uc);
		urlHash.put(uc.getUrl_md5(), "1");
		return new_flag;
	}

	/**
	 * 已经添加到内容表中的url
	 */
	public int discoveredUriCount() {
		return mContentDao.discoveredUriCount(this.portalId);
	}

	/**
	 * 已经添加到内容表中的已处理url
	 */
	public int discoveredProcUriCount() {
		return mContentDao.discoveredProcUriCount(this.portalId);
	}

	/**
	 * 获取站点的处理器
	 * 
	 * @param portalId
	 * @return
	 */
	public List<PortalProcessor> queryPortalProcessor() {
		return mPortalProcessorDao.queryPortalProcessorByPortalId(this.portalId);
	}

}
