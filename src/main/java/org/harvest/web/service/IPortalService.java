package org.harvest.web.service;

import java.util.List;

import org.harvest.web.bean.Portal;



public interface IPortalService {


	/**
	 * 根据站占名称查询
	 * @param portal_name
	 * @return
	 */
	public List<Portal> queryPortalList(String portal_name, int page, int rows);

	public Integer queryPortalCount(String portal_name);
	
	/**
	 * 修改
	 * @param portal
	 * @return
	 */
	public int updatePortal(Portal portal);
	
	/**
	 * 新增
	 * @param portal
	 * @return
	 */
	public int addPortal(Portal portal);
	
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public int deletePortal(List<Integer> ids);
}
