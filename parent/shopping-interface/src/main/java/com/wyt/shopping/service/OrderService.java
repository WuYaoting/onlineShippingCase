package com.wyt.shopping.service;

import com.wyt.shopping.pojo.Order;

public interface OrderService {

	// 保存订单
	void insertOrder(Order order, String username);
	
}
