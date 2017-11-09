package com.wyt.shopping.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wyt.shopping.mapper.BrandMapper;
import com.wyt.shopping.pojo.Brand;
import com.wyt.shopping.pojo.BrandQuery;
import com.wyt.shopping.pojo.Pagination;
import com.wyt.shopping.service.BrandService;

@Service("brandService")
public class BrandServiceImpl implements BrandService {

	@Resource
	private BrandMapper brandMapper;
	
	// 查询品牌列表 分页，封装Pagination数据暂时交给controller
	@Override
	public Pagination selectBrandListHavePage(String name, Integer isDisplay, Integer pageNo) {
		// 封装brandQuery对象，传入Mapper进行条件查询
		BrandQuery brandQuery = new BrandQuery();
		StringBuffer params = new StringBuffer();
		// 查询条件信息
		if (null != name && !"".equals(name)) {
			brandQuery.setName(name);
			params.append("name=" + name); // view视图
		}
		if (null != isDisplay) {
			brandQuery.setIsDisplay(isDisplay);
			params.append("&isDisplay=" + isDisplay); // view视图信息
		}
		// 分页信息
		brandQuery.setPageNo(Pagination.cpn(pageNo));
		brandQuery.setPageSize(5);
		List<Brand> brandList = brandMapper.selectBrandListHavePage(brandQuery);
		int totalCount = brandMapper.selectBrandCount(brandQuery);
		
		// 封装pagination对象，返回controller
		Pagination pagination = new Pagination(brandQuery.getPageNo(), brandQuery.getPageSize(), totalCount, brandList);
		String url = "/brand/list.do";
		pagination.pageView(url, params.toString());
		
		return pagination;
	}

	// 根据id 查询brand
	@Override
	public Brand selectBrandById(Long id) {
		return brandMapper.selectBrandById(id);
	}

}
