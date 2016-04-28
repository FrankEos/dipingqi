package org.harvest.web.bean;

/**
 * 采集内容
 * @author jone.yuan
 *
 */
public class UrlContent {

	/**
	 * ID
	 */
	private String url_md5;
	
	/**
	 * url
	 */
	private String url;
	
	/**
	 * url类型
	 * 0 list url
	 * 1 content url
	 */
	private Integer url_type;
	
	/**
	 * 耗时
	 */
	private Integer cost_time;
	
	/**
	 * 操作状态，1、未处理，2、已处理，3、处理失败
	 */
	private Integer oper_flag;
	
	/**
	 * 内容标识
	 */
	private String content_md5;
	
	/**
	 * 更新时间
	 */
	private Long update_time;
	
	/**
	 * 类型
	 */
	private String tag;
	
	/**
	 * 排序
	 */
	private Long order;
	
	/**
	 * 保留字段
	 */
	private String bak;
	
	
	public String getUrl_md5() {
		return url_md5;
	}
	public void setUrl_md5(String url_md5) {
		this.url_md5 = url_md5;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getUrl_type() {
		return url_type;
	}
	public void setUrl_type(Integer url_type) {
		this.url_type = url_type;
	}
	public Integer getCost_time() {
		return cost_time;
	}
	public void setCost_time(Integer cost_time) {
		this.cost_time = cost_time;
	}
	public Integer getOper_flag() {
		return oper_flag;
	}
	public void setOper_flag(Integer oper_flag) {
		this.oper_flag = oper_flag;
	}
	public String getContent_md5() {
		return content_md5;
	}
	public void setContent_md5(String content_md5) {
		this.content_md5 = content_md5;
	}
	public Long getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Long update_time) {
		this.update_time = update_time;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public Long getOrder() {
		return order;
	}
	public void setOrder(Long order) {
		this.order = order;
	}
	public String getBak() {
		return bak;
	}
	public void setBak(String bak) {
		this.bak = bak;
	}
	
	
	
}
