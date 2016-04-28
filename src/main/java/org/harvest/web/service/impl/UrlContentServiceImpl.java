package org.harvest.web.service.impl;

import java.util.List;

import org.harvest.web.bean.UrlContent;
import org.harvest.web.dao.IUrlContentDao;
import org.harvest.web.service.IUrlContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UrlContentServiceImpl implements IUrlContentService {

	@Autowired
	private IUrlContentDao mContentDao;

	public List<UrlContent> queryContentList(Integer portalId, int page, int rows) {
		return mContentDao.queryContentList(portalId, (page - 1) * rows, rows);
	}

	public Integer queryContentCount(Integer portalId) {
		return mContentDao.queryContentCount(portalId);
	}

	public int updateContent(Integer portalId, UrlContent content) {
		return mContentDao.updateContent(portalId,content);
	}

	public int addContent(Integer portalId, UrlContent content) {
		content.setUpdate_time(System.currentTimeMillis());
		content.setCost_time(0);
		return mContentDao.addContent(portalId, content);
	}

	public int deleteContent(Integer portalId, List<String> ids) {
		Integer result = 0;
		for (String url_md5 : ids) {
			result += this.mContentDao.deleteContent(portalId,url_md5);
		}
		return result;
	}

	
}
