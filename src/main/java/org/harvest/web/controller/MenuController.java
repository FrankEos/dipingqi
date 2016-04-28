package org.harvest.web.controller;

import java.util.List;

import org.harvest.web.authority.Login;
import org.harvest.web.bean.Menu;
import org.harvest.web.bean.base.BaseListResponse;
import org.harvest.web.bean.base.BaseResponse;
import org.harvest.web.bean.base.ResultCode;
import org.harvest.web.service.IMenuService;
import org.harvest.web.util.Str;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/menu")
public class MenuController {

	private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

	@Autowired
	private IMenuService mMenuService;

	/**
	 * 菜单页面
	 * @return
	 */
	@Login()
	@RequestMapping("/list")
	public String list() {
		return "menu/list";
	}
	/**
	 * 菜单列表
	 * @param page
	 * @param rows
	 * @return
	 */
	@Login()
	@RequestMapping("/listData")
	@ResponseBody
	public BaseListResponse<Menu> listData(
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "rows", defaultValue = "10") int rows,Menu menu) {
		BaseListResponse<Menu> response = new BaseListResponse<Menu>();
		if(!Str.isNull(menu.getName())){
			response.setTotal(this.mMenuService.queryMenuContionCount(menu.getName()));
			response.setRows(this.mMenuService.queryContionMenus(page, rows,menu.getName()));
		}else{
			response.setTotal(this.mMenuService.queryMenuCount());
			response.setRows(this.mMenuService.queryMenus(page, rows));
		}
		return response;
	}

	/**
	 * 保存菜单
	 * @param menu
	 * @return
	 */
	@Login()
	@RequestMapping("/save")
	@ResponseBody
	public BaseResponse save(Menu menu) {
		logger.info("save menu :" + menu);

		BaseResponse response = new BaseResponse();
		Integer result = 0;
		try {
			menu.setOrder_by("0");
			if (menu.getId() == null) {
				result = this.mMenuService.addMenu(menu);
			} else {
				result = this.mMenuService.updateMenu(menu);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("save menu error :" + e.getMessage());
			response.setResultCode(ResultCode.SYSTEM_CONFIG_SAVE_ERROR
					.getCode());
			response.setResultDesc(ResultCode.SYSTEM_CONFIG_SAVE_ERROR
					.getDesc() + ",error:" + e.getMessage());
			return response;
		}
		response.setResultCode(ResultCode.SUCCESS.getCode());
		response.setResultDesc(ResultCode.SUCCESS.getDesc());
		if (result < 0) {
			response.setResultCode(ResultCode.SYSTEM_CONFIG_SAVE_ERROR
					.getCode());
			response.setResultDesc(ResultCode.SYSTEM_CONFIG_SAVE_ERROR
					.getDesc());
		}
		return response;
	}

	/**
	 * 菜单删除
	 * @param ids
	 * @return
	 */
	@Login()
	@RequestMapping("/delete")
	@ResponseBody
	public BaseResponse delete(@RequestParam("ids[]") List<Integer> ids) {
		logger.info("delete menu ids :" + ids);
		BaseResponse response = new BaseResponse();
		response.setResultCode(ResultCode.SUCCESS.getCode());
		response.setResultDesc(ResultCode.SUCCESS.getDesc());

		try {
			Integer result = this.mMenuService.deleteMenu(ids);
			if (result <= 0) {
				response.setResultCode(ResultCode.SYSTEM_CONFIG_DELETE_ERROR
						.getCode());
				response.setResultDesc(ResultCode.SYSTEM_CONFIG_DELETE_ERROR
						.getDesc());
			}
		} catch (Exception e) {
			logger.error("delete menu error :" + e.getMessage());
			response.setResultCode(ResultCode.SYSTEM_CONFIG_DELETE_ERROR
					.getCode());
			response.setResultDesc(ResultCode.SYSTEM_CONFIG_DELETE_ERROR
					.getDesc() + ",error :" + e.getMessage());
		}
		return response;
	}
	
	/**
	 * 复制菜单
	 * @param ids
	 * @return
	 */
	@Login()
	@RequestMapping("/copy")
	@ResponseBody
	public BaseResponse copy(@RequestParam("ids[]") List<Integer> ids) {
		logger.info("copy sysconf ids :" + ids);
		BaseResponse response = new BaseResponse();
		response.setResultCode(ResultCode.SUCCESS.getCode());
		response.setResultDesc(ResultCode.SUCCESS.getDesc());

		try {
			Integer result = this.mMenuService.copyMenu(ids);
			if (result <= 0) {
				response.setResultCode(ResultCode.SYSTEM_CONFIG_COPY_ERROR.getCode());
				response.setResultDesc(ResultCode.SYSTEM_CONFIG_COPY_ERROR.getDesc());
			}
		} catch (Exception e) {
			logger.error("cope menu error :" + e.getMessage());
			response.setResultCode(ResultCode.SYSTEM_CONFIG_COPY_ERROR.getCode());
			response.setResultDesc(ResultCode.SYSTEM_CONFIG_COPY_ERROR.getDesc() + ",error :" + e.getMessage());
		}
		return response;
	}

}
