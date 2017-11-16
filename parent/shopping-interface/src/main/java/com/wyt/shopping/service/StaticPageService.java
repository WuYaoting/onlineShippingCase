package com.wyt.shopping.service;

import java.util.Map;

public interface StaticPageService {

	// 接收数据 创建静态页面
	void index(Long id, Map<String, Object> rootMap);
	
}
