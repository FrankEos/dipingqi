package org.harvest.web.bean;

/**
 * 站点类
 * @author jone.yuan
 *
 */
public class Portal {

	private Integer id;
	
	/**
	 * 站点名称
	 */
	private String portal_name;
	
	/**
	 * 描述
	 */
	private String portal_desc;
	
	/**
	 * 最大线程数
	 */
	private Integer max_thread;
	
	/**
	 * 采集周期
	 */
	private Integer cycle;
	
	/**
	 * 是否显示(有效)
	 */
	private Integer show_flag;
	
	/**
	 * 手动全量采集,0:手动全量，自动增量，1：自动全量，手动增量
	 */
	private Integer isAllByHand;
	
	/**
	 * 增量采集页数
	 */
	private Integer incrementPageCount;
	
	/**
	 * 上次启动时间
	 */
	private Long lastStartTime;
	
	/**
	 * 下次启动时间
	 */
	private Long nextStartTime;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPortal_name() {
		return portal_name;
	}
	public void setPortal_name(String portal_name) {
		this.portal_name = portal_name;
	}
	public String getPortal_desc() {
		return portal_desc;
	}
	public void setPortal_desc(String portal_desc) {
		this.portal_desc = portal_desc;
	}
	public Integer getMax_thread() {
		return max_thread;
	}
	public void setMax_thread(Integer max_thread) {
		this.max_thread = max_thread;
	}
	public Integer getCycle() {
		return cycle;
	}
	public void setCycle(Integer cycle) {
		this.cycle = cycle;
	}
	public Integer getShow_flag() {
		return show_flag;
	}
	public void setShow_flag(Integer show_flag) {
		this.show_flag = show_flag;
	}
	public Integer getIsAllByHand() {
		return isAllByHand;
	}
	public void setIsAllByHand(Integer isAllByHand) {
		this.isAllByHand = isAllByHand;
	}
	public Integer getIncrementPageCount() {
		return incrementPageCount;
	}
	public void setIncrementPageCount(Integer incrementPageCount) {
		this.incrementPageCount = incrementPageCount;
	}
	public Long getLastStartTime() {
		return lastStartTime;
	}
	public void setLastStartTime(Long lastStartTime) {
		this.lastStartTime = lastStartTime;
	}
	public Long getNextStartTime() {
		return nextStartTime;
	}
	public void setNextStartTime(Long nextStartTime) {
		this.nextStartTime = nextStartTime;
	}
	
	
	
	
	
	
}
