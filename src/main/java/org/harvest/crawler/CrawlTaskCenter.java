package org.harvest.crawler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.harvest.crawler.framework.CrawlCommService;
import org.harvest.crawler.framework.CrawlController;
import org.harvest.crawler.framework.CrawlerInf;
import org.harvest.crawler.framework.Frontier;
import org.harvest.crawler.framework.ToePool;
import org.harvest.web.bean.Portal;

public class CrawlTaskCenter implements Runnable {

	private static Logger logger = Logger.getLogger(CrawlTaskCenter.class);

	private static CrawlCommService commService;

	private Thread myThread = new Thread(this);

	private static HashMap<Integer, CrawlController> localCrawlController = new HashMap<Integer, CrawlController>();

	private List<Integer> portalIds;// 站点


	/**
	 * 启动初始化
	 */
	public void start() {
		commService = CrawlCommService.getInstance();
		myThread.setDaemon(true);
		myThread.start();
	}

	/**
	 * 手动启动一个爬虫任务
	 * 
	 * @param portalId
	 * @param byhand
	 */
	public static void startCrawler(int portalId, boolean byhand) {
		if (!localCrawlController.containsKey(portalId)) {
			Portal portal = commService.queryPortalById(portalId);
			Long lastUpdateTime = commService.queryListUrlUpdateTime(portalId);
			portal.setLastStartTime(lastUpdateTime);// 上一次采集时间
			portal.setNextStartTime(getNextOperateDay(lastUpdateTime, portal.getCycle()));// 下次采集时间

			// 设置所有的List Url 为未处理状态
			commService.updateListUrlStatus(portalId);

			// 设置所有未成功提取的Content Url 为未处理状态
			commService.updateContentUrlStatus(portalId);

			CrawlController cc = new CrawlController();
			cc.setByhand(byhand); // 设置手动标志位
			cc.initialize(portal);
			localCrawlController.put(portalId, cc);
		} else {
			logger.debug("该网站爬取已在内存中,不需要启动.");
		}

	}

	/**
	 * 停止一个爬虫任务
	 * 
	 * @param portalId
	 */
	public static void stopCrawler(int portalId) {
		if (localCrawlController.containsKey(portalId)) {
			CrawlController cc = localCrawlController.get(portalId);
			if (cc != null) {
				cc.destroy();
			}
			localCrawlController.remove(portalId);
		} else {
			logger.debug("该网站爬取已停止,不需要再次停止.");
		}
	}

	/**
	 * 后台使用，得到portal的状态信息
	 * 
	 * @param seqId
	 * @return
	 */
	public static CrawlerInf getStatic(int portalId) {
		CrawlController cc = localCrawlController.get(portalId);
		if (cc != null) {
			CrawlerInf crawlerInf = cc.getCrawlerInf();
			crawlerInf.setRunTime(System.currentTimeMillis() - crawlerInf.getStartTime());
			return crawlerInf;
		} else {
			Portal portal = commService.queryPortalById(portalId);
			Long lastUpdateTime = commService.queryListUrlUpdateTime(portalId);
			portal.setLastStartTime(lastUpdateTime);// 上一次采集时间
			portal.setNextStartTime(getNextOperateDay(lastUpdateTime, portal.getCycle()));// 下次采集时间
			CrawlerInf crawlerInf = new CrawlerInf();
			crawlerInf.setPortal(portal);
			crawlerInf.setRunState(0);
			crawlerInf.setStartTime(0l);
			crawlerInf.setRunTime(0l);
			crawlerInf.setCurThread(0);
			crawlerInf.setMaxThread(portal.getMax_thread());
			crawlerInf.setProUrl(0);
			crawlerInf.setAllUrl(0);
			crawlerInf.setIpLock(false);
			return crawlerInf;
		}
	}

	/**
	 * 运行维护
	 */
	public void run() {
		logger.debug("运行维护程序开始.......... ");
		while (true) {

			List<Portal> list = commService.queryValidPortal();
			getPortalIds(list);
			for (Portal portal : list) {
				//　站点id
				int portalId = portal.getId();
				// 站点名称
				String portalName = portal.getPortal_name();
				logger.debug("任务控制中心 网站:" + portalName);
				// 采集周期
				int cyc = portal.getCycle();
				// 上次更新时间
				long lastUpdateTime = commService.queryListUrlUpdateTime(portalId);
				// 计算下次启动时间
				long startTime = getNextOperateDay(lastUpdateTime, cyc);
				// 上一次采集时间
				portal.setLastStartTime(lastUpdateTime);
				// 下次采集时间
				portal.setNextStartTime(startTime);
				// 当前时间
				long curTime = System.currentTimeMillis();
				

				// 结束任务
				if (localCrawlController.containsKey(portalId)) {// 此处判断无意义
					CrawlController cc = localCrawlController.get(portalId);
					// 判断Controller是否已经被置为Null
					if (null == cc) {
						//合并数据
//						portalHarvestFinished(portal); 
					} else if (cc.getToePool() != null && cc.getToePool().getToeCount() == 0) {// 1.判断该任务是不是死任务,如果是没有一个线程就干掉
						cc.destroy();
						// 移除站点
						localCrawlController.remove(portalId);
//						portalHarvestFinished(portal);
					}
				}

				// 检查是否有需要启动的portal 如果有就启动
				if (startTime != 0 && startTime < curTime && !localCrawlController.containsKey(portalId)) {
					logger.debug("[启动任务]到了启动时间 网站:" + portalName);

					// 设置所有的List Url 为未处理状态
					commService.updateListUrlStatus(portalId);

					// 设置所有未成功提取的Content Url 为未处理状态
					commService.updateContentUrlStatus(portalId);

					logger.debug(portalName + "运行标识更新为未处理");

					// 初始化站点，准备爬取
					CrawlController cc = new CrawlController();
					cc.initialize(portal);

					localCrawlController.put(portalId, cc);
				}

				// 定期修正任务队列中的 线程数 统计数据
				if (localCrawlController.containsKey(portalId)) {
					CrawlController cc = localCrawlController.get(portalId);
					// 1.修正线程数
					Frontier frontier = cc.getFrontier();
					ToePool toePool = cc.getToePool();
					CrawlerInf crawlerInf = cc.getCrawlerInf();
					if (!frontier.isEmpty()) {
						logger.debug("[修正任务线程数] 网站:" + portalName + "线程数:" + crawlerInf.getPortal().getMax_thread());
						logger.debug("[===修正任务线程数===] 网站:" + portalName + "===最大线程数:" + crawlerInf.getPortal().getMax_thread());
						toePool.setSize(crawlerInf.getPortal().getMax_thread());
					}

					// 2.修正统计数据[线程数量 全部url,已处理URL数]
					crawlerInf.setCurThread(toePool.getActiveToeCount());// 修正统计活动线程数
					crawlerInf.setMaxThread(crawlerInf.getPortal().getMax_thread());// 修正统计中最大线程数

					crawlerInf.setAllUrl(frontier.discoveredUriCount());// 修正统计全部URL数
					crawlerInf.setProUrl(frontier.discoveredProcUriCount());// 修正统计处理URL数
				}
			}
			
			try {
				 Thread.sleep(5 * 60 * 1000);
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}

	}


	// 得到该网站任务下次执行的日期
	private static Long getNextOperateDay(Long lastUpdateTime, Integer cyc) {
		if (lastUpdateTime != 0) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(lastUpdateTime);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			return c.getTimeInMillis() + 1000 * 60 * 60 * cyc;// 上次执行时间加周期取本次执行日期

		}
		return 0l;
	}

	private void getPortalIds(List<Portal> allPortal) {
		portalIds = new ArrayList<Integer>();
		for (Portal p : allPortal) {
			portalIds.add(p.getId());
		}
	}

	public static List<Integer> getRunningPortalIds() {
		List<Integer> portalIds = new ArrayList<Integer>();
		portalIds.addAll(localCrawlController.keySet());
		return portalIds;
	}
}
