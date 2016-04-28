package org.harvest.web.service;

import java.util.List;

import org.harvest.web.bean.PortalProcessor;



public interface IPortalProcessorService {


	/**
	 * 根据站占名称查询
	 * @param portalProcessor_name
	 * @return
	 */
	public List<PortalProcessor> queryPortalProcessorList(Integer portalId, int page, int rows);

	public Integer queryPortalProcessorCount(Integer portalId);
	
	/**
	 * 修改
	 * @param portalProcessor
	 * @return
	 */
	public int updatePortalProcessor(PortalProcessor portalProcessor);
	
	/**
	 * 新增
	 * @param portalProcessor
	 * @return
	 */
	public int addPortalProcessor(PortalProcessor portalProcessor);
	
	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	public int deletePortalProcessor(Integer portalId,List<Integer> ids);
	
}
