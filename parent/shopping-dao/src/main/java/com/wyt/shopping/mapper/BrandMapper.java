package com.wyt.shopping.mapper;

import java.util.List;

import com.wyt.shopping.pojo.Brand;
import com.wyt.shopping.pojo.BrandQuery;

public interface BrandMapper {

	// 查询品牌 不分页显示
	List<Brand> selectBrandListNoPage(BrandQuery brandQuery);

	// 查询品牌 分页显示
	List<Brand> selectBrandListHavePage(BrandQuery brandQuery);
	
	// 查询总记录数
	int selectBrandCount(BrandQuery brandQuery);
	
	// 根据id查询品牌信息
	Brand selectBrandById(Long id);
	
}
