package com.wyt.shopping.service;

import java.util.List;

import com.wyt.shopping.pojo.Product;
import com.wyt.shopping.pojo.Sku;

public interface CmsService {

	// 根据id 查询商品信息
	Product selectProductById(Long id);
	
	// 根据商品id查询库存信息
	List<Sku> selectSkusByPidAndStockGreaterThanZero(Long productId);
	
}
