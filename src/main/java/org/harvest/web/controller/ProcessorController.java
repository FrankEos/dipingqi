package org.harvest.web.controller;

import java.util.List;

import org.harvest.web.authority.Login;
import org.harvest.web.bean.Processor;
import org.harvest.web.bean.base.BaseListResponse;
import org.harvest.web.bean.base.BaseResponse;
import org.harvest.web.bean.base.ResultCode;
import org.harvest.web.service.IProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/processor")
public class ProcessorController {

	private static final Logger logger = LoggerFactory.getLogger(ProcessorController.class);

	@Autowired
	private IProcessorService mProcessorService;

	/**
	 * 处理器页面
	 * 
	 * @return
	 */
	@Login()
	@RequestMapping("/list")
	public String list() {
		return "processor/list";
	}

	/**
	 * 处理器列表
	 * 
	 * @param page
	 * @param rows
	 * @return
	 */
	@Login()
	@RequestMapping("/listData")
	@ResponseBody
	public BaseListResponse<Processor> listData(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = "15") int rows, String proc_name) {
		BaseListResponse<Processor> response = new BaseListResponse<Processor>();
		proc_name = proc_name == null ? "" : proc_name;
		response.setRows(this.mProcessorService.queryProcessorList(proc_name, page, rows));
		response.setTotal(this.mProcessorService.queryProcessorCount(proc_name));
		return response;
	}

	/**
	 * 保存处理器
	 * 
	 * @param processor
	 * @return
	 */
	@Login()
	@RequestMapping("/save")
	@ResponseBody
	public BaseResponse save(Processor processor,String save_flag) {
		logger.info("save processor :" + processor);
		BaseResponse response = new BaseResponse();
		Integer result = 0;
		try {
			if (save_flag.equals("add")) {
				result = this.mProcessorService.addProcessor(processor);
			} else {
				result = this.mProcessorService.updateProcessor(processor);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("save processor error :" + e.getMessage());
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
	@RequestMapping("/delete")
	@ResponseBody
	public BaseResponse delete(@RequestParam("ids[]") List<String> ids) {
		logger.info("delete processor ids :" + ids);
		BaseResponse response = new BaseResponse();
		response.setResultCode(ResultCode.SUCCESS.getCode());
		response.setResultDesc(ResultCode.SUCCESS.getDesc());

		try {
			Integer result = this.mProcessorService.deleteProcessor(ids);
			if (result <= 0) {
				response.setResultCode(ResultCode.SYSTEM_CONFIG_DELETE_ERROR.getCode());
				response.setResultDesc(ResultCode.SYSTEM_CONFIG_DELETE_ERROR.getDesc());
			}
		} catch (Exception e) {
			logger.error("delete processor error :" + e.getMessage());
			response.setResultCode(ResultCode.SYSTEM_CONFIG_DELETE_ERROR.getCode());
			response.setResultDesc(ResultCode.SYSTEM_CONFIG_DELETE_ERROR.getDesc() + ",error :" + e.getMessage());
		}
		return response;
	}

}
