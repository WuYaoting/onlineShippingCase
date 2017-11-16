package com.wyt.shopping.service;

import com.wyt.shopping.pojo.Pagination;
import com.wyt.shopping.pojo.Product;

public interface ProductService {

	// 分页查询
	Pagination selectProductListHavePage(String name, Long brandId, Boolean isShow, Integer pageNo);
	
	// 添加商品信息
	void insertProduct(Product product);

	// 商品上架
	void isShow(Long[] ids) throws Exception;
	
}
