package com.wyt.shopping.service;

import java.util.List;

import com.wyt.shopping.pojo.Brand;
import com.wyt.shopping.pojo.Pagination;

public interface BrandService {

	// 不分页查询
	List<Brand> selectBrandListNoPage(String name, Integer isDisplay);
	
	// 分页查询
	Pagination selectBrandListHavePage(String name, Integer isDisplay, Integer pageNo);
	
	// 根据id 查询
	Brand selectBrandById(Long id);
	
	// 更新品牌信息
	void updateBrand(Brand brand);
	
	// 添加品牌信息
	void insertBrand(Brand brand);
	
	// 删除
	void deleteBrands(Long[] ids);
	
}
