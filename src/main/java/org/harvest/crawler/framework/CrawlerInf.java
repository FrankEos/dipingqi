/**
 * 
 * 一个网站站点的配置信息和状态信息
 * @author fancs
 *
 */
package org.harvest.crawler.framework;

import java.util.List;

import org.apache.log4j.Logger;
import org.harvest.web.bean.Portal;
import org.harvest.web.bean.PortalProcessor;

public class CrawlerInf {

	private Portal portal;//站点信息

	private List<PortalProcessor> portalProcessors;//对应处理器
	
	private Integer runState;// 运行状态 0 停止 1 运行

	private Long startTime;// 启动时间

	private Long runTime;// 运行时间

	private Integer curThread;// 当前活动线程数

	private Integer maxThread;// 最大线程数

	private Integer proUrl;// 已处理URL数

	private Integer allUrl;// 全部Url数

	private boolean ipLock;//ip被封标志

	
	
	

	// 根据currentCuri 取对应处理器类名
	public PortalProcessor getPortalProcessorByUrl(String url) {
		Logger.getLogger(CrawlerInf.class).error("===============>"+portalProcessors.size());;
		Logger.getLogger(CrawlerInf.class).error("===============url>"+url);
		
		for (PortalProcessor bean : portalProcessors) {
			Logger.getLogger(CrawlerInf.class).error("============bean===>"+bean.getUrl_regx());
			if (url.trim().matches(bean.getUrl_regx().trim())) {
				return bean;
			}
		}
		return null;
	}
	
	public void addCurThread() {
		this.curThread++;
	}

	public void delCurThread() {
		if(curThread>0)
			this.curThread--;
	}
	
	public void addProUrl() {
		this.proUrl++;
	}
	
	public void delProUrl() {
		if(proUrl>0)
			this.proUrl--;
	}
	
	public void addAllUrl() {
		this.allUrl++;
	}
	
	
	
	
	
	
	
	
	
	
	
	/**   geter and seter   **/
	


	public Portal getPortal() {
		return portal;
	}

	public void setPortal(Portal portal) {
		this.portal = portal;
	}

	public List<PortalProcessor> getPortalProcessors() {
		return portalProcessors;
	}

	public void setPortalProcessors(List<PortalProcessor> portalProcessors) {
		this.portalProcessors = portalProcessors;
	}

	public Integer getRunState() {
		return runState;
	}

	public void setRunState(Integer runState) {
		this.runState = runState;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getRunTime() {
		return runTime;
	}

	public void setRunTime(Long runTime) {
		this.runTime = runTime;
	}

	public Integer getCurThread() {
		return curThread;
	}

	public void setCurThread(Integer curThread) {
		this.curThread = curThread;
	}

	public Integer getMaxThread() {
		return maxThread;
	}

	public void setMaxThread(Integer maxThread) {
		this.maxThread = maxThread;
	}

	public Integer getProUrl() {
		return proUrl;
	}

	public void setProUrl(Integer proUrl) {
		this.proUrl = proUrl;
	}

	public Integer getAllUrl() {
		return allUrl;
	}

	public void setAllUrl(Integer allUrl) {
		this.allUrl = allUrl;
	}

	public boolean isIpLock() {
		return ipLock;
	}

	public void setIpLock(boolean ipLock) {
		this.ipLock = ipLock;
	}
	
	
	
}
