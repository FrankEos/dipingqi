package org.harvest.web.service;

import java.util.List;

import org.harvest.web.bean.UrlContent;



public interface IUrlContentService {


	/**
	 * 根据站占名称查询
	 * @param content_name
	 * @return
	 */
	public List<UrlContent> queryContentList(Integer portalId, int page, int rows);

	public Integer queryContentCount(Integer portalId);
	
	/**
	 * 修改
	 * @param content
	 * @return
	 */
	public int updateContent(Integer portalId,UrlContent content);
	
	/**
	 * 新增
	 * @param content
	 * @return
	 */
	public int addContent(Integer portalId,UrlContent content);
	
	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	public int deleteContent(Integer portalId,List<String> ids);
	
}
