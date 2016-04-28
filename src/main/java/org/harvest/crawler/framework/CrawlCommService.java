package org.harvest.crawler.framework;

import java.util.List;

import org.harvest.web.bean.Portal;
import org.harvest.web.bean.Statistics;
import org.harvest.web.dao.IPortalDao;
import org.harvest.web.dao.IStatisticsDao;
import org.harvest.web.dao.IUrlContentDao;

public class CrawlCommService {

	private static CrawlCommService mInstance;

	private IPortalDao mPortalDao;

	private IUrlContentDao mContentDao;
	
	private IStatisticsDao mStatisticsDao;

	/**
	 * 单例，获取实例对象
	 * 
	 * @return
	 */
	public static synchronized CrawlCommService getInstance() {
		if (mInstance == null) {
			mInstance = new CrawlCommService();
		}
		return mInstance;
	}

	/**
	 * 构造函数
	 */
	private CrawlCommService() {
		mPortalDao = (IPortalDao) ToolSpring.getBean(IPortalDao.class);
		mContentDao = (IUrlContentDao) ToolSpring.getBean(IUrlContentDao.class);
		// 统计
		mStatisticsDao = (IStatisticsDao) ToolSpring.getBean(IStatisticsDao.class);
	}

	/**
	 * 获取站点信息
	 * 
	 * @param portalId
	 * @return
	 */
	public Portal queryPortalById(Integer portalId) {
		return mPortalDao.queryPortalById(portalId);
	}

	/**
	 * 设置所有的List Url 为未处理状态
	 * 
	 * @param portalId
	 * @return
	 */
	public Integer updateListUrlStatus(Integer portalId) {
		return mContentDao.updateListUrlStatus(portalId);
	}

	/**
	 * 获取所有有效的站点信息
	 * 
	 * @return
	 */
	public List<Portal> queryValidPortal() {
		return mPortalDao.queryValidPortal();
	}

	/**
	 * 设置所有未成功提取的Content Url 为未处理状态
	 * 
	 * @param portalId
	 * @return
	 */
	public Integer updateContentUrlStatus(Integer portalId) {
		return mContentDao.updateContentUrlStatus(portalId);
	}

	/**
	 * 获取站点上次执行采集的时间
	 * 
	 * @param portalId
	 * @return
	 */
	public Long queryListUrlUpdateTime(Integer portalId) {
		Long lastUpdateTime = mContentDao.queryListUrlUpdateTime(portalId);
		return lastUpdateTime == null ? 0 : lastUpdateTime;
	}
	
	
	/**
	 * 插入统计数据
	 * @param portalId
	 * @param statistics
	 */
	public void insertStatistics(Integer portalId, Statistics statistics) {
		mStatisticsDao.createStatisticsTable(portalId);
		mStatisticsDao.insertStatistics(portalId, statistics);
	}
	
	public Integer queryAllPageInContent(Integer portalId) {
		return mContentDao.queryAllPageContent(portalId);
	}

	public Integer queryHavePageInContent(Integer portalId) {
		return mContentDao.queryHavePageContent(portalId);
	}

	public Integer queryFailPageInContent(Integer portalId) {
		return mContentDao.queryFailPageContent(portalId);
	}

	public Integer queryListPageInContent(Integer portalId) {
		return mContentDao.queryListPageContent(portalId);
	}
	
	
}
