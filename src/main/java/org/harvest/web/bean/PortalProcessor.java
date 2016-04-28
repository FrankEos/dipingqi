package org.harvest.web.bean;

/**
 * 站点处理器
 * @author jone.yuan
 *
 */
public class PortalProcessor {

	/**
	 * 站点ID
	 */
	private Integer portal_id;
	
	/**
	 * 站点名称
	 */
	private String portal_name;
	
	/**
	 * 处理器正则ID
	 */
	private Integer regx_id;
	
	/**
	 * 正则表达式
	 */
	private String url_regx;
	
	/**
	 * 处理器类名
	 */
	private String proc_class;
	
	/**
	 * 采集规则文件
	 */
	private String rule_file;

	public Integer getPortal_id() {
		return portal_id;
	}

	public void setPortal_id(Integer portal_id) {
		this.portal_id = portal_id;
	}

	public String getPortal_name() {
		return portal_name;
	}

	public void setPortal_name(String portal_name) {
		this.portal_name = portal_name;
	}

	public Integer getRegx_id() {
		return regx_id;
	}

	public void setRegx_id(Integer regx_id) {
		this.regx_id = regx_id;
	}

	public String getUrl_regx() {
		return url_regx;
	}

	public void setUrl_regx(String url_regx) {
		this.url_regx = url_regx;
	}

	public String getProc_class() {
		return proc_class;
	}

	public void setProc_class(String proc_class) {
		this.proc_class = proc_class;
	}

	public String getRule_file() {
		return rule_file;
	}

	public void setRule_file(String rule_file) {
		this.rule_file = rule_file;
	}
	
	
	
	
}
