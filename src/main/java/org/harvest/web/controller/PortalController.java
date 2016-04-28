package org.harvest.web.controller;

import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.harvest.crawler.CrawlTaskCenter;
import org.harvest.crawler.framework.CrawlerInf;
import org.harvest.web.authority.Login;
import org.harvest.web.bean.Portal;
import org.harvest.web.bean.PortalProcessor;
import org.harvest.web.bean.Processor;
import org.harvest.web.bean.UrlContent;
import org.harvest.web.bean.base.BaseListResponse;
import org.harvest.web.bean.base.BaseResponse;
import org.harvest.web.bean.base.ResultCode;
import org.harvest.web.service.IPortalProcessorService;
import org.harvest.web.service.IPortalService;
import org.harvest.web.service.IProcessorService;
import org.harvest.web.service.IUrlContentService;
import org.harvest.web.util.Str;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
@RequestMapping("/portal")
public class PortalController {

	private static final Logger logger = LoggerFactory.getLogger(PortalController.class);

	@Autowired
	private IPortalService mPortalService;
	
	@Autowired
	private IUrlContentService mContentService;
	
	@Autowired
	private IPortalProcessorService mPortalProcessorService;
	
	@Autowired
	private IProcessorService mProcessorService;

	/**
	 * 站点页面
	 * 
	 * @return
	 */
	@Login()
	@RequestMapping("/list")
	public String list() {
		return "portal/list";
	}

	/**
	 * 站点列表
	 * 
	 * @param page
	 * @param rows
	 * @return
	 */
	@Login()
	@RequestMapping("/listData")
	@ResponseBody
	public BaseListResponse<Portal> listData(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = "15") int rows, String portal_name) {
		BaseListResponse<Portal> response = new BaseListResponse<Portal>();
		portal_name = portal_name == null ? "" : portal_name;
		response.setRows(this.mPortalService.queryPortalList(portal_name, page, rows));
		response.setTotal(this.mPortalService.queryPortalCount(portal_name));
		return response;
	}

	/**
	 * 保存站点
	 * 
	 * @param portal
	 * @return
	 */
	@Login()
	@RequestMapping("/save")
	@ResponseBody
	public BaseResponse save(Portal portal) {
		logger.info("save portal :" + portal);

		BaseResponse response = new BaseResponse();
		Integer result = 0;
		try {
			if (portal.getId() == null) {
				result = this.mPortalService.addPortal(portal);
			} else {
				result = this.mPortalService.updatePortal(portal);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("save portal error :" + e.getMessage());
			response.setResultCode(ResultCode.SYSTEM_CONFIG_SAVE_ERROR.getCode());
			response.setResultDesc(ResultCode.SYSTEM_CONFIG_SAVE_ERROR.getDesc() + ",error:" + e.getMessage());
			return response;
		}
		response.setResultCode(ResultCode.SUCCESS.getCode());
		response.setResultDesc(ResultCode.SUCCESS.getDesc());
		if (result < 0) {
			response.setResultCode(ResultCode.SYSTEM_CONFIG_SAVE_ERROR.getCode());
			response.setResultDesc(ResultCode.SYSTEM_CONFIG_SAVE_ERROR.getDesc());
		}
		return response;
	}

	/**
	 * 站点删除
	 * 
	 * @param ids
	 * @return
	 */
	@Login()
	@RequestMapping("/delete")
	@ResponseBody
	public BaseResponse delete(@RequestParam("ids[]") List<Integer> ids) {
		logger.info("delete portal ids :" + ids);
		BaseResponse response = new BaseResponse();
		response.setResultCode(ResultCode.SUCCESS.getCode());
		response.setResultDesc(ResultCode.SUCCESS.getDesc());

		try {
			Integer result = this.mPortalService.deletePortal(ids);
			if (result <= 0) {
				response.setResultCode(ResultCode.SYSTEM_CONFIG_DELETE_ERROR.getCode());
				response.setResultDesc(ResultCode.SYSTEM_CONFIG_DELETE_ERROR.getDesc());
			}
		} catch (Exception e) {
			logger.error("delete portal error :" + e.getMessage());
			response.setResultCode(ResultCode.SYSTEM_CONFIG_DELETE_ERROR.getCode());
			response.setResultDesc(ResultCode.SYSTEM_CONFIG_DELETE_ERROR.getDesc() + ",error :" + e.getMessage());
		}
		return response;
	}

	
	
	
	/**
	 * 内容页面
	 * 
	 * @return
	 */
	@Login()
	@RequestMapping("/listContent")
	public String listContent(HttpServletRequest request) {
		request.getSession().setAttribute("portalId", Integer.valueOf(request.getParameter("portalId")));
		return "portal/contentList";
	}
	
	
	/**
	 * 内容列表
	 * 
	 * @param page
	 * @param rows
	 * @return
	 */
	@Login()
	@RequestMapping("/listContentData")
	@ResponseBody
	public BaseListResponse<UrlContent> listContentData(HttpServletRequest request,@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = "15") int rows) {
		BaseListResponse<UrlContent> response = new BaseListResponse<UrlContent>();
		Integer portalId = (Integer)request.getSession().getAttribute("portalId");
		response.setRows(this.mContentService.queryContentList(portalId, page, rows));
		response.setTotal(this.mContentService.queryContentCount(portalId));
		return response;
	}
	
	


	/**
	 * 保存Url Content
	 * 
	 * @param content
	 * @return
	 */
	@Login()
	@RequestMapping("/saveContent")
	@ResponseBody
	public BaseResponse saveContent(HttpServletRequest request,UrlContent content) {
		logger.info("save content :" + content);
		Integer portalId = (Integer)request.getSession().getAttribute("portalId");
		BaseResponse response = new BaseResponse();
		Integer result = 0;
		try {
			if (Str.isNull(content.getUrl_md5())) {
				result = this.mContentService.addContent(portalId, content);
			} else {
				result = this.mContentService.updateContent(portalId, content);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("save content error :" + e.getMessage());
			response.setResultCode(ResultCode.SYSTEM_CONFIG_SAVE_ERROR.getCode());
			response.setResultDesc(ResultCode.SYSTEM_CONFIG_SAVE_ERROR.getDesc() + ",error:" + e.getMessage());
			return response;
		}
		response.setResultCode(ResultCode.SUCCESS.getCode());
		response.setResultDesc(ResultCode.SUCCESS.getDesc());
		if (result < 0) {
			response.setResultCode(ResultCode.SYSTEM_CONFIG_SAVE_ERROR.getCode());
			response.setResultDesc(ResultCode.SYSTEM_CONFIG_SAVE_ERROR.getDesc());
		}
		return response;
	}

	/**
	 * Url Content 删除
	 * 
	 * @param ids
	 * @return
	 */
	@Login()
	@RequestMapping("/deleteContent")
	@ResponseBody
	public BaseResponse deleteContent(HttpServletRequest request,@RequestParam("ids[]") List<String> ids) {
		logger.info("delete content ids :" + ids);
		Integer portalId = (Integer)request.getSession().getAttribute("portalId");
		BaseResponse response = new BaseResponse();
		response.setResultCode(ResultCode.SUCCESS.getCode());
		response.setResultDesc(ResultCode.SUCCESS.getDesc());

		try {
			Integer result = this.mContentService.deleteContent(portalId,ids);
			if (result <= 0) {
				response.setResultCode(ResultCode.SYSTEM_CONFIG_DELETE_ERROR.getCode());
				response.setResultDesc(ResultCode.SYSTEM_CONFIG_DELETE_ERROR.getDesc());
			}
		} catch (Exception e) {
			logger.error("delete content error :" + e.getMessage());
			response.setResultCode(ResultCode.SYSTEM_CONFIG_DELETE_ERROR.getCode());
			response.setResultDesc(ResultCode.SYSTEM_CONFIG_DELETE_ERROR.getDesc() + ",error :" + e.getMessage());
		}
		return response;
	}
	
	
	


	
	
	
	/**
	 * 处理器页面
	 * 
	 * @return
	 */
	@Login()
	@RequestMapping("/listPortalProcessor")
	public String listPortalProcessor(HttpServletRequest request) {
		request.getSession().setAttribute("portalId", Integer.valueOf(request.getParameter("portalId")));
		return "portal/portalProcessorList";
	}
	
	
	/**
	 * 处理器列表
	 * 
	 * @param page
	 * @param rows
	 * @return
	 */
	@Login()
	@RequestMapping("/listPortalProcessorData")
	@ResponseBody
	public BaseListResponse<PortalProcessor> listPortalProcessorData(HttpServletRequest request,@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = "15") int rows) {
		BaseListResponse<PortalProcessor> response = new BaseListResponse<PortalProcessor>();
		Integer portalId = (Integer)request.getSession().getAttribute("portalId");
		response.setRows(this.mPortalProcessorService.queryPortalProcessorList(portalId, page, rows));
		response.setTotal(this.mPortalProcessorService.queryPortalProcessorCount(portalId));
		return response;
	}
	
	


	/**
	 * 保存处理器
	 * 
	 * @param portalProcessor
	 * @return
	 */
	@Login()
	@RequestMapping("/savePortalProcessor")
	@ResponseBody
	public BaseResponse savePortalProcessor(HttpServletRequest request,PortalProcessor portalProcessor) {
		logger.info("save portalProcessor :" + portalProcessor);
		Integer portalId = (Integer)request.getSession().getAttribute("portalId");
		BaseResponse response = new BaseResponse();
		Integer result = 0;
		try {
			if (portalProcessor.getRegx_id() == null) {
				portalProcessor.setPortal_id(portalId);
				result = this.mPortalProcessorService.addPortalProcessor(portalProcessor);
			} else {
				result = this.mPortalProcessorService.updatePortalProcessor(portalProcessor);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("save portalProcessor error :" + e.getMessage());
			response.setResultCode(ResultCode.SYSTEM_CONFIG_SAVE_ERROR.getCode());
			response.setResultDesc(ResultCode.SYSTEM_CONFIG_SAVE_ERROR.getDesc() + ",error:" + e.getMessage());
			return response;
		}
		response.setResultCode(ResultCode.SUCCESS.getCode());
		response.setResultDesc(ResultCode.SUCCESS.getDesc());
		if (result < 0) {
			response.setResultCode(ResultCode.SYSTEM_CONFIG_SAVE_ERROR.getCode());
			response.setResultDesc(ResultCode.SYSTEM_CONFIG_SAVE_ERROR.getDesc());
		}
		return response;
	}

	/**
	 * 处理器删除
	 * 
	 * @param ids
	 * @return
	 */
	@Login()
	@RequestMapping("/deletePortalProcessor")
	@ResponseBody
	public BaseResponse deletePortalProcessor(HttpServletRequest request,@RequestParam("ids[]") List<Integer> ids) {
		logger.info("delete portalProcessor ids :" + ids);
		Integer portalId = (Integer)request.getSession().getAttribute("portalId");
		BaseResponse response = new BaseResponse();
		response.setResultCode(ResultCode.SUCCESS.getCode());
		response.setResultDesc(ResultCode.SUCCESS.getDesc());

		try {
			Integer result = this.mPortalProcessorService.deletePortalProcessor(portalId,ids);
			if (result <= 0) {
				response.setResultCode(ResultCode.SYSTEM_CONFIG_DELETE_ERROR.getCode());
				response.setResultDesc(ResultCode.SYSTEM_CONFIG_DELETE_ERROR.getDesc());
			}
		} catch (Exception e) {
			logger.error("delete portalProcessor error :" + e.getMessage());
			response.setResultCode(ResultCode.SYSTEM_CONFIG_DELETE_ERROR.getCode());
			response.setResultDesc(ResultCode.SYSTEM_CONFIG_DELETE_ERROR.getDesc() + ",error :" + e.getMessage());
		}
		return response;
	}
	
	
	/**
	 * 获取站点爬虫状态信息
	 * @param request
	 * @param response
	 */
	@RequestMapping("/status")
	public void status(HttpServletRequest request, HttpServletResponse response){
		String portalId = request.getParameter("portalId");
		CrawlerInf crawler = CrawlTaskCenter.getStatic(Integer.parseInt(portalId));
		try {
			Writer writer = response.getWriter();
			String options = new Gson().toJson(crawler);
			writer.write(options);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 启动站点爬虫
	 * @param request
	 * @param response
	 */
	@RequestMapping("/start")
	public void start(HttpServletRequest request, HttpServletResponse response){
		String portalId = request.getParameter("portalId");
		CrawlTaskCenter.startCrawler(Integer.parseInt(portalId), true);
	}
	
	

	/**
	 * 停止站点爬虫
	 * @param request
	 * @param response
	 */
	@RequestMapping("/stop")
	public void stop(HttpServletRequest request, HttpServletResponse response){
		String portalId = request.getParameter("portalId");
		CrawlTaskCenter.stopCrawler(Integer.parseInt(portalId));
	}
	
	
	@RequestMapping("/queryProcessorList")
	public void queryProcessorList(HttpServletResponse response){
		List<Processor> procList = mProcessorService.queryProcessorOptions();
		try {
			Writer writer = response.getWriter();
			String options = new Gson().toJson(procList);
			writer.write(options);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
