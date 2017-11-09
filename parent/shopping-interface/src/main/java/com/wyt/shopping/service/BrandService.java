package com.wyt.shopping.service;

import com.wyt.shopping.pojo.Brand;
import com.wyt.shopping.pojo.Pagination;

public interface BrandService {

	// 分页查询
	Pagination selectBrandListHavePage(String name, Integer isDisplay, Integer pageNo);
	
	// 根据id 查询
	Brand selectBrandById(Long id);
	
	
}
