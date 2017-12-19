package com.wyt.shopping.utils;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author WuYaoting
 * @project_name shopping-common
 * @discription 令牌信息
 */
public class TokenUtil {

	public static String getSessionId(HttpServletRequest req, HttpServletResponse resp) {
		// cookie中有令牌信息，则取出返回
		Cookie[] cookies = req.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if ("sessionId".equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		// 没有令牌信息,生成令牌信息存入cookie，返回
		String sessionId = UUID.randomUUID().toString().replace("-", "");
		Cookie cookie = new Cookie("sessionId", sessionId);
		cookie.setPath("/");
		cookie.setMaxAge(60*60);
		resp.addCookie(cookie);
		return sessionId;
	}
	
}
