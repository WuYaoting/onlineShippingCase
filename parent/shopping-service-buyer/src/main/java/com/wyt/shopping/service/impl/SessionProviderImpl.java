package com.wyt.shopping.service.impl;

import javax.annotation.Resource;

import com.wyt.shopping.service.SessionProvider;

import redis.clients.jedis.Jedis;

public class SessionProviderImpl implements SessionProvider {

	@Resource
	private Jedis jedis;
	
	// 注入登录用户信息过期时间
	private Integer seconds = 60;
	public Integer getSeconds() {
		return seconds;
	}
	public void setSeconds(Integer seconds) {
		this.seconds = seconds;
	}

	// 用户信息保存至redis
	@Override
	public void setAttributeForUsername(String key, String username) {
		jedis.set("userSessionId:" + key, username);
		jedis.expire("userSessionId:" + key, 60*seconds);
	}

	// redis中拿到用户信息
	@Override
	public String getAttributeForUsername(String key) {
		String username = jedis.get("userSessionId:" + key);
		if (username != null) {
			// 刷新用户登陆过期时间
			jedis.expire("userSessionId:" + key, 60*seconds);
			return username;
		}
		return null;
	}

}
