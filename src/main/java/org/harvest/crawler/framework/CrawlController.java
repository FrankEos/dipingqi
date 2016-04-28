/***********************************************************************
 * Module:  CrawlController.java
 * Author:  Owner
 * Purpose: Defines the Class CrawlController
 ***********************************************************************/

package org.harvest.crawler.framework;

import org.apache.log4j.Logger;
import org.harvest.web.bean.Portal;

/** 每个网站的爬取控制中心 */
public class CrawlController {
	
	
	
	private static Logger logger = Logger.getLogger(CrawlController.class);
	
	private boolean byhand = false; // 该任务是否手动启动 true即为手动启动 默认为自动

	// 因为一个抓取工作必须要有一个portalinf对象，它保存了对该次抓取任务中，数据库中的相关配置
	private transient CrawlerInf crawlerInf;

	// 一次抓取任务需要设定一个Frontier，以此来不断为其每个线程提供URI
	private transient DBFrontier frontier;

	// 这是一个线程池，它管理了所有该抓取任务所创建的子线程
	private ToePool toePool;
	
	

	
	/**
	 * 1、初始化站点的配置信息和状态信息
	 * 2、实例化了线程池，并启动
	 * 
	 * @param portal
	 */
	public void initialize(Portal portal) {
		frontier = new DBFrontier(portal.getId());
		
		crawlerInf = new CrawlerInf();
		crawlerInf.setPortal(portal);
		crawlerInf.setPortalProcessors(frontier.queryPortalProcessor());
		crawlerInf.setRunState(1);
		crawlerInf.setStartTime((System.currentTimeMillis()));
		crawlerInf.setRunTime(0l);
		crawlerInf.setCurThread(0);
		crawlerInf.setMaxThread(portal.getMax_thread());
		crawlerInf.setProUrl(frontier.discoveredProcUriCount());
		crawlerInf.setAllUrl(frontier.discoveredUriCount());
		crawlerInf.setIpLock(false);
		
		// 最后实例化了线程池,并启动线程。
		toePool = new ToePool(this, portal.getPortal_name());
		toePool.setSize(portal.getMax_thread());
		logger.debug(portal.getPortal_name() + "即将进行抓取操作.");
	}
	


	// 对一个portal抓取对象消毁
	public void destroy() {
		// 消毁线程
		toePool.setSize(0);
		toePool.cleanup();
		logger.debug(crawlerInf.getPortal().getPortal_name() + "即将消毁.");
	}
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	/**   geter and seter   **/
	



	public boolean isByhand() {
		return byhand;
	}

	public void setByhand(boolean byhand) {
		this.byhand = byhand;
	}

	public CrawlerInf getCrawlerInf() {
		return crawlerInf;
	}

	public void setCrawlerInf(CrawlerInf crawlerInf) {
		this.crawlerInf = crawlerInf;
	}

	public DBFrontier getFrontier() {
		return frontier;
	}

	public void setFrontier(DBFrontier frontier) {
		this.frontier = frontier;
	}

	public ToePool getToePool() {
		return toePool;
	}

	public void setToePool(ToePool toePool) {
		this.toePool = toePool;
	}

	

}