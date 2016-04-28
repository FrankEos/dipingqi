package org.harvest.web.authority;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.harvest.web.bean.User;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginAnnotationInterceptor extends HandlerInterceptorAdapter {

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		HandlerMethod handler2 = (HandlerMethod) handler;
		// Login login = handler2.getMethodAnnotation(Login.class);
		Login login = handler2.getMethod().getAnnotation(Login.class);
		if (null == login) {
			// 没有声明权限,放行
			return true;
		}

		HttpSession session = request.getSession();
		// 取得session中的用户信息, 以便判断是否登录了系统
		User user = (User) session.getAttribute(SessionHelper.WorkerHandler);
		if (null == user) {
			// 需要登录
			if (login.value() == ResultTypeEnum.page) {
				// 传统页面的登录
				response.sendRedirect("/HarvestExt/preLogin");
			} else if (login.value() == ResultTypeEnum.json) {
				// ajax页面的登录
				response.setCharacterEncoding("utf-8");
				response.setContentType("text/html;charset=UTF-8");
				OutputStream out = response.getOutputStream();
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(out, "utf-8"));
				// 返回json格式的提示
				pw.println("{\"result\":false,\"code\":11,\"errorMessage\":\"您未登录,请先登录\"}");
				pw.flush();
				pw.close();
			}
			return false;
		}
		return true;
	}
}