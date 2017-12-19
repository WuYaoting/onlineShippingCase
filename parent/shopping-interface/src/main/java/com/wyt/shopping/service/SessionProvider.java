package com.wyt.shopping.service;

public interface SessionProvider {

	// 将用户信息保存至redis
	void setAttributeForUsername(String key, String username);
	
	// 从redis中拿到用户信息
	String getAttributeForUsername(String key);
	
}
