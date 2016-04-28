package org.harvest.web.bean;

import java.util.List;

public class Menu {
	/**
	 * 主键id
	 */
	private Integer id;
	/**
	 * 栏目id
	 */
	private Integer pid;
	/**
	 * 栏目id
	 */
	private String name;
	/**
	 * 栏目父级链接
	 */
	private String link;
	/**
	 * 栏目列表
	 */
	private List<Menu> sonMenus;
	/**
	 * 排序
	 */
	private String order_by;
	/**
	 * 状态
	 */
	private String status;
	/**
	 * show_flag
	 */
	private String show_flag;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public List<Menu> getSonMenus() {
		return sonMenus;
	}

	public void setSonMenus(List<Menu> sonMenus) {
		this.sonMenus = sonMenus;
	}

	public String getOrder_by() {
		return order_by;
	}

	public void setOrder_by(String order_by) {
		this.order_by = order_by;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getShow_flag() {
		return show_flag;
	}

	public void setShow_flag(String show_flag) {
		this.show_flag = show_flag;
	}

	public Menu() {

	}

	public Menu(Integer id, Integer pid, String name, String link,
			List<Menu> sonMenus, String order_by, String status,
			String show_flag) {
		this.id = id;
		this.name = name;
		this.link = link;
		this.sonMenus = sonMenus;
		this.order_by = order_by;
		this.status = status;
		this.show_flag = show_flag;
	}

	@Override
	public String toString() {
		return "Menu [id=" + id + ", pid=" + pid + ", name=" + name + ", link="
				+ link + ", sonMenus=" + sonMenus + "]";
	}
}
