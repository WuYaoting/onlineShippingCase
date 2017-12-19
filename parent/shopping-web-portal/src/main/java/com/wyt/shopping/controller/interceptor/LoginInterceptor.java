package com.wyt.shopping.controller.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.wyt.shopping.service.SessionProvider;
import com.wyt.shopping.utils.TokenUtil;

public class LoginInterceptor implements HandlerInterceptor {

	@Resource
	private SessionProvider sessionProvider;
	
	// 拦截登录请求
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String username = sessionProvider.getAttributeForUsername(TokenUtil.getSessionId(request, response));
		if (username == null) {
			response.sendRedirect("http://localhost:8086/login.aspx?ReturnUrl=http://localhost:8082/");
			return false;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
