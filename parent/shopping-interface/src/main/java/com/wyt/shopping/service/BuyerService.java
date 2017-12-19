package com.wyt.shopping.service;

import com.wyt.shopping.pojo.Buyer;
import com.wyt.shopping.pojo.BuyerCart;
import com.wyt.shopping.pojo.Sku;

public interface BuyerService {

	// 根据用户名称查询用户 用于判断前台登陆
	Buyer selectBuyerByUsername(String username);
	
	// 根据skuId查询sku信息,用于购物车页面会显商品信息
	Sku selectSkuById(Long skuId);
	
	// 登录状态购物车信息保存至redis
	void insertBuyerCartIntoRedis(String username, BuyerCart buyerCart);
	
	// 登录状态下从reids中取出购物车信息
	BuyerCart getBuyerCartFromRedis(String username);
	
}
