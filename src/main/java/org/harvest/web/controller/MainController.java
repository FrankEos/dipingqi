package org.harvest.web.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.harvest.web.authority.Login;
import org.harvest.web.authority.SessionHelper;
import org.harvest.web.bean.User;
import org.harvest.web.bean.base.BaseResponse;
import org.harvest.web.bean.base.ResultCode;
import org.harvest.web.service.IMenuService;
import org.harvest.web.service.IUserService;
import org.harvest.web.util.MD5Tool;
import org.harvest.web.util.Str;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class MainController {

	private static final Logger logger = LoggerFactory.getLogger(MainController.class);

	@Autowired
	private IMenuService mMenuService;

	@Autowired
	private IUserService mUserService;

	/**
	 * 主页面
	 * 
	 * @return
	 */
	@Login
	@RequestMapping(value = "index", method = RequestMethod.GET)
	public ModelAndView index() {
		logger.debug("index");
		Map<String, Object> menus = new HashMap<String, Object>();
		menus.put("menus", this.mMenuService.queryAllMenus());
		return new ModelAndView("index", menus);
	}

	/**
	 * 登陆页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "userLogin", method = RequestMethod.POST)
	public ModelAndView Login() {
		logger.debug("userLogin");
		return new ModelAndView("userLogin");
	}

	/**
	 * 退出登录
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/logout")
	public void logout(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.getSession(true).removeAttribute(SessionHelper.WorkerHandler);
		response.sendRedirect("preLogin");
	}

	/**
	 * 密码修改
	 * 
	 * @param user
	 */
	@ResponseBody
	@RequestMapping("/upwd")
	public BaseResponse updatePasswd(User user, HttpServletRequest request,
			HttpServletResponse response) {
		User upUser = (User) request.getSession().getAttribute(SessionHelper.WorkerHandler);
		BaseResponse baseResponse = new BaseResponse();
		try {
			if (upUser == null) {
				baseResponse.setResultCode(ResultCode.USER_NOLOGIN.getCode());
				baseResponse.setResultDesc("error:" + ResultCode.USER_NOLOGIN.getDesc());
				return baseResponse;
			}
			// 密码不正确
			if (!(upUser.getPassword().equals(MD5Tool.calcMD5(user.getPassword())))) {
				baseResponse.setResultCode(ResultCode.PASSWORD_INCORRECT.getCode());
				baseResponse.setResultDesc("error:" + ResultCode.PASSWORD_INCORRECT.getDesc());
				return baseResponse;
			}
			this.mUserService.updatePasswd(upUser.getUserName(), user.getRpwd());
			baseResponse.setResultCode(ResultCode.SUCCESS.getCode());
			baseResponse.setResultDesc(ResultCode.SUCCESS.getDesc());
		} catch (Exception e) {
			e.printStackTrace();
			baseResponse.setResultCode(ResultCode.SYSTEM_CONFIG_SAVE_ERROR
					.getCode());
			baseResponse.setResultDesc(ResultCode.SYSTEM_CONFIG_SAVE_ERROR
					.getDesc() + ",error:" + e.getMessage());
			return baseResponse;
		}
		return baseResponse;
	}

	/**
	 * 登陆
	 * 
	 * @param request
	 * @param userName
	 * @param password
	 * @return
	 */
	@RequestMapping("/login")
	@ResponseBody
	public BaseResponse login(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("userName") String userName,
			@RequestParam("password") String password,String verifyCode) {
		BaseResponse baseResponse = new BaseResponse();
		String vcode = (String) request.getSession().getAttribute("validateCode");
		request.getSession(true).removeAttribute(vcode);
		if (Str.isNull(vcode)) {
			baseResponse.setResultCode(ResultCode.VCODE_ERROR.getCode());
			baseResponse.setResultDesc(ResultCode.VCODE_ERROR.getDesc());
			HTMLMessage(response,baseResponse);
			return baseResponse;
		}
		//判断验证码是否正确
		if(!verifyCode.toLowerCase().equals(vcode)){
			baseResponse.setResultCode(ResultCode.VCODE_WRONG.getCode());
			baseResponse.setResultDesc(ResultCode.VCODE_WRONG.getDesc());
			HTMLMessage(response,baseResponse);
			return baseResponse;
		}
		User user = this.mUserService.login(userName, password);
		if (user == null) {
			baseResponse.setResultCode(ResultCode.PASSWORD_ERROR.getCode());
			baseResponse.setResultDesc(ResultCode.PASSWORD_ERROR.getDesc());
			HTMLMessage(response,baseResponse);
			return baseResponse;
		} else {
			baseResponse.setResultCode(ResultCode.SUCCESS.getCode());
			baseResponse.setResultDesc(ResultCode.SUCCESS.getDesc());
			request.getSession().setAttribute(SessionHelper.WorkerHandler,user);
			request.getSession().setAttribute("user", user);
			HTMLMessage(response,baseResponse);
			return baseResponse;
			// 返回json格式的提示
		}
	}

	@RequestMapping("/preLogin")
	public String preLogin() {
		return "/login";
	}
	
	public void HTMLMessage(HttpServletResponse response,BaseResponse baseResponse){
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=UTF-8");
			OutputStream out = response.getOutputStream();
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(out,"utf-8"));
			pw.println("{'resultCode':'" + baseResponse.getResultCode() + "','resultDesc':'" + baseResponse.getResultDesc() + "'}");
			pw.flush();
			pw.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}

}
