package org.harvest.crawler.framework;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.harvest.web.bean.PortalProcessor;
import org.harvest.web.bean.Statistics;
import org.harvest.web.bean.UrlContent;

public class ToeThread extends Thread {

	private static Logger logger = Logger.getLogger(ToeThread.class);

	private String coreName;

	private CrawlController controller;

	private static final int DEFAULT_PRIORITY = Thread.NORM_PRIORITY - 2;

	private UrlContent currentCuri;// 当前要处理的UrlContent

	private long lastStartTime;

	private long lastFinishTime;

	public static boolean isHarvestAll = false;

	// 该线程是否结束标志器
	private volatile boolean shouldRetire = false;

	private HashMap<String, BaseProcessor> localProcessors = new HashMap<String, BaseProcessor>();

	/**
	 * 创建一个toe线程
	 */
	public ToeThread(ToePool g, int sn) {
		super(g, g.getName() + "Thread #" + sn);
		coreName = g.getName() + "Thread #" + sn + ": ";
		controller = g.getController();
		logger.debug("==========================controller.getCrawlerInf():" + controller.getCrawlerInf());
		setPriority(DEFAULT_PRIORITY);
		setDaemon(true);
	}

	/**
	 * 返回正在处理url的线程数
	 */
	public boolean isActive() {
		logger.debug("=============isActive()===>>>==="+getName());
		// if alive and not waiting in/for frontier.next(), we're 'active'
		return this.isAlive() && (currentCuri != null);
	}

	/**
	 * 设置该线程处理完后自动结束
	 */
	public void retire() {
		shouldRetire = true;
	}

	public void run() {
		logger.error(coreName + " started --------------------");
		
		// 统计开始
		Integer havePage = CrawlCommService.getInstance().queryHavePageInContent(controller.getCrawlerInf().getPortal().getId());
		
		Statistics statistics = new Statistics();
		statistics.setStart_time(System.currentTimeMillis() / 1000);
		statistics.setHave_page(havePage);
		
		while (true) {
			try {

				// 释放线程 要求释放或已没url
				if (shouldRetire || controller.getFrontier().isEmpty()) {
					if (shouldRetire) {
						logger.error(coreName + "[释放线程]网站提取结束时间到了，强制释放线程。");
					}
					if (controller.getFrontier().isEmpty()) {
						logger.error(coreName + "[释放线程]网站已经没有需要提取的节目，强制释放线程或已处理完全部URL。");
					}
					controller.getCrawlerInf().delCurThread();// 状态变更-减少一个活动线程
					// 直接销毁网站控制器
					controller.destroy();
					break; // break out while(true)
				}

				// 使用Frontier的next方法从Frontier中
				// 取出下一个要处理的链接
				currentCuri = controller.getFrontier().next();
				if (currentCuri == null) {
					logger.error(coreName + "[线程暂停]暂时没有要处理的URL了 " + " Thread sleep : 1 minute ");
					Thread.sleep(1 * 60 * 1000);// 暂停1分钟
					continue;
				}

				// 处理取出的链接
				processCrawlUri();

				// 自动任务
				if (!controller.isByhand()) {
					currentCuri.setUpdate_time(System.currentTimeMillis());// 自动任务，更新处理时间
				}
				// 使用Frontier的finished()方法 来对刚才处理的链接做收尾工作
				controller.getFrontier().finished(currentCuri);
				// 状态变更-添加一个已处理的url
				controller.getCrawlerInf().addProUrl();

				Thread.sleep(200);// 不要占了所有的资源

			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
				// 使用Frontier的finished()方法 来对刚才处理的链接做收尾工作
				currentCuri.setOper_flag(3);// 下载失败
				controller.getFrontier().finished(currentCuri);
				controller.getCrawlerInf().addProUrl();// 状态变更-添加一个已处理的url
			}
		}
		
		Integer listNum = CrawlCommService.getInstance().queryListPageInContent(controller.getCrawlerInf().getPortal().getId());
		Integer allPage = CrawlCommService.getInstance().queryAllPageInContent(controller.getCrawlerInf().getPortal().getId());
		Integer addPage = CrawlCommService.getInstance().queryHavePageInContent(controller.getCrawlerInf().getPortal().getId())-havePage;
		Integer failPage = CrawlCommService.getInstance().queryFailPageInContent(controller.getCrawlerInf().getPortal().getId());
		statistics.setList_num(listNum);
		statistics.setAll_page(allPage);
		statistics.setAdd_page(addPage);
		statistics.setFail_page(failPage);
		statistics.setEnd_time(System.currentTimeMillis() / 1000);
		statistics.setProc_name(controller.getCrawlerInf().getMaxThread());
		CrawlCommService.getInstance().insertStatistics(controller.getCrawlerInf().getPortal().getId(), statistics);
		// 统计结束

		logger.debug(coreName + " end --------------------");
	}

	/**
	 * 根据URL调用不能的处理器进行数据处理
	 * 
	 * @param statistics
	 * 
	 */
	private void processCrawlUri() {
		lastStartTime = System.currentTimeMillis();
		BaseProcessor currentProcessor = getProcessor();
		// 调用Process方法
		if (currentProcessor == null) {
			lastFinishTime = System.currentTimeMillis();
			currentCuri.setOper_flag(3);
			currentCuri.setCost_time((int) (lastFinishTime - lastStartTime) / 1000);
			return;
		}
		currentCuri.setOper_flag(2);// 如果没有正常下载要在处理器中设置
		StringBuffer strBuf = new StringBuffer(coreName);
		strBuf.append("处理器:");
		strBuf.append(currentProcessor.getClass().getName());
		strBuf.append(" 处理URL:");
		strBuf.append(currentCuri.getUrl());
		strBuf.append(" 任务队列中还有数量:");
		strBuf.append(controller.getFrontier().getQueSize());
		logger.debug(strBuf.toString());
		currentProcessor.process(currentCuri);
		lastFinishTime = System.currentTimeMillis();
		currentCuri.setCost_time((int) (lastFinishTime - lastStartTime) / 1000);

		// 从内存中移除
		currentProcessor = null;
	}

	private BaseProcessor getProcessor() {
		// 根据currentCuri 取对应处理器类名 及规则文件名
		PortalProcessor portalProcessor = controller.getCrawlerInf().getPortalProcessorByUrl(currentCuri.getUrl());
		logger.debug("=============currentCuri.getUrl():" + currentCuri.getUrl());
		if (portalProcessor == null) {
			logger.error("URL:" + currentCuri.getUrl() + "没有找到相应的处理器类.");
			return null;
		}

		BaseProcessor localProcessor = (BaseProcessor) localProcessors.get(portalProcessor.getProc_class());
		if (localProcessor == null) {
			try {

				// 增加是否是全量采集标示记录，最后联动消息发送是确定是否走MQ或JMS，对应/HarvestExtForTVFuns2.0/src/com/bq/crawler/v2/SciflyJMS.java
				if (((controller.isByhand() && (controller.getCrawlerInf().getPortal().getIsAllByHand() == 0)) || (!controller.isByhand() && !(controller
								.getCrawlerInf().getPortal().getIsAllByHand() == 0)))) {
					isHarvestAll = true;// 标记全量采集
				} else {
					isHarvestAll = false;
				}

				// 反射加载处理器类
				localProcessor = (BaseProcessor) Class.forName(portalProcessor.getProc_class()).newInstance();
				// 设置处理器属性
				localProcessor.setController(controller);
				// 设置抓取规则文件
				localProcessor.setRulefile(portalProcessor.getRule_file());
				
				// 内存不够用 试下不存可不可以
				localProcessors.put(portalProcessor.getProc_class(), localProcessor);
				
			} catch (InstantiationException e) {
				logger.error(" 初始化处理器时发生错误 ： " + portalProcessor.getProc_class() + " : " + e.getLocalizedMessage());
			} catch (IllegalAccessException e) {
				logger.error(e.getLocalizedMessage());
			} catch (ClassNotFoundException e) {
				logger.error(e.getLocalizedMessage());
			}
		}

		return localProcessor;
	}
}
