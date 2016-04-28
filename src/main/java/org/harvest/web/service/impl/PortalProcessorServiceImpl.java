package org.harvest.web.service.impl;

import java.util.List;

import org.harvest.web.bean.PortalProcessor;
import org.harvest.web.dao.IPortalProcessorDao;
import org.harvest.web.service.IPortalProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PortalProcessorServiceImpl implements IPortalProcessorService {

	@Autowired
	private IPortalProcessorDao mPortalProcessorDao;

	public List<PortalProcessor> queryPortalProcessorList(Integer portalId, int page, int rows) {
		return mPortalProcessorDao.queryPortalProcessorList(portalId, (page - 1) * rows, rows);
	}

	public Integer queryPortalProcessorCount(Integer portalId) {
		return mPortalProcessorDao.queryPortalProcessorCount(portalId);
	}

	public int updatePortalProcessor(PortalProcessor portalProcessor) {
		return mPortalProcessorDao.updatePortalProcessor(portalProcessor);
	}

	public int addPortalProcessor(PortalProcessor portalProcessor) {
		Integer regx_id = mPortalProcessorDao.queryRegxMaxId(portalProcessor.getPortal_id());
		regx_id = regx_id == null ? 1 : (regx_id+1);
		portalProcessor.setRegx_id(regx_id);
		return mPortalProcessorDao.addPortalProcessor(portalProcessor);
	}

	public int deletePortalProcessor(Integer portalId, List<Integer> ids) {
		Integer result = 0;
		for (Integer regx_id : ids) {
			result += this.mPortalProcessorDao.deletePortalProcessor(portalId,regx_id);
		}
		return result;
	}

	
}
