package com.wyt.shopping.service;

import java.util.List;

import com.wyt.shopping.pojo.Brand;
import com.wyt.shopping.pojo.Pagination;

public interface SearchService {

	// 返回前台页面商品列表分页对象
	Pagination selectProductsForPortal(String keyword, Long brandId, String price, Integer pageNo) throws Exception;
	
	// 返回在redis中查到的品牌信息
	List<Brand> selectBrandsFromRedis();
	
	// 根据id选择品牌,获取品牌名称用于页面回显
	String selectBrandNameByIdFromRedis(Long id);
	
	// 将商品信息上传至solr中
	void insertProductToSolr(Long id) throws Exception;
	
}
