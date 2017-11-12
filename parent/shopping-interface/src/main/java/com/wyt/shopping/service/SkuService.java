package com.wyt.shopping.service;

import java.util.List;

import com.wyt.shopping.pojo.Sku;

public interface SkuService {

	// 根据商品id查询库存信息
	List<Sku> selectSkusByProductId(Long productId);
	
	// 更新库存信息
	void updateSkuById(Sku sku);
	
}
