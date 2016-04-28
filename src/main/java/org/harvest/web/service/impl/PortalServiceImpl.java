package org.harvest.web.service.impl;

import java.util.Calendar;
import java.util.List;

import org.harvest.crawler.CrawlTaskCenter;
import org.harvest.web.bean.Portal;
import org.harvest.web.dao.IPortalDao;
import org.harvest.web.dao.IStatisticsDao;
import org.harvest.web.dao.IUrlContentDao;
import org.harvest.web.service.IPortalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PortalServiceImpl implements IPortalService {

	@Autowired
	private IPortalDao mPortalDao;
	
	@Autowired
	private IUrlContentDao mContentDao;
	
	@Autowired
	private IStatisticsDao mStatDao;
	
	public List<Portal> queryPortalList(String portal_name, int page, int rows) {
		List<Portal> portalList = mPortalDao.queryPortalList(portal_name, (page - 1) * rows, rows);
		List<Integer> portalIds = CrawlTaskCenter.getRunningPortalIds();
		for(Portal portal : portalList){
			Long lastUpdateTime = mContentDao.queryListUrlUpdateTime(portal.getId());
			if(lastUpdateTime == null){
				portal.setLastStartTime(0l);
				portal.setNextStartTime(0l);
			}else{
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(lastUpdateTime);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Long nextStartTime = c.getTimeInMillis() + 1000 * 60 * 60 * portal.getCycle();// 上次执行时间加周期取本次执行日期
				portal.setLastStartTime(lastUpdateTime);
				portal.setNextStartTime(nextStartTime);
			}
			if(portalIds.contains(portal.getId())){
				portal.setShow_flag(2);//0:无效，1:有效，2:运行中
			}
		}
		return portalList;
	}
	
	public Integer queryPortalCount(String portal_name) {
		return mPortalDao.queryPortalCount(portal_name);
	}

	public int updatePortal(Portal portal) {
		return mPortalDao.updatePortal(portal);
	}

	public int addPortal(Portal portal) {
		Integer result = 0;
		result += mPortalDao.addPortal(portal);
		Integer portalId = mPortalDao.queryPortalId(portal);
		result += this.mContentDao.dropContentTab(portalId);
		result += mContentDao.createContentTab(portalId);
		
		// 删除并创建 统计表
		result += mStatDao.dropStatisticsTable(portalId);
		result += mStatDao.createStatisticsTable(portalId);
		return result;
	}

	public int deletePortal(List<Integer> ids) {
		Integer result = 0;
		for (Integer id : ids) {
			result += this.mPortalDao.deletePortal(id);
			result += this.mContentDao.dropContentTab(id);
			// 删除statistics
			result += mStatDao.dropStatisticsTable(id);
		}
		return result;
	}

}
