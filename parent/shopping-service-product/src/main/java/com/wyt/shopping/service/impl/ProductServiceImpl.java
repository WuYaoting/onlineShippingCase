package com.wyt.shopping.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wyt.shopping.mapper.ProductMapper;
import com.wyt.shopping.mapper.SkuMapper;
import com.wyt.shopping.pojo.Pagination;
import com.wyt.shopping.pojo.Product;
import com.wyt.shopping.pojo.ProductQuery;
import com.wyt.shopping.pojo.ProductQuery.Criteria;
import com.wyt.shopping.pojo.Sku;
import com.wyt.shopping.service.ProductService;

import redis.clients.jedis.Jedis;

@Service("productService")
public class ProductServiceImpl implements ProductService {

	@Resource
	private ProductMapper productMapper;
	@Resource
	private SkuMapper skuMapper;
	@Resource
	private Jedis jedis;

	// 查询品牌列表 分页，封装Pagination数据暂时交给controller
	@Override
	public Pagination selectProductListHavePage(String name, Long brandId, Boolean isShow, Integer pageNo) {
		// 封装productQuery对象，传入Mapper进行条件查询
		ProductQuery productQuery = new ProductQuery();
		Criteria criteria = productQuery.createCriteria();
		StringBuffer params = new StringBuffer();
		// 查询条件信息
		if (null != name && !"".equals(name)) {
			criteria.andNameLike("%" + name + "%");
			params.append("name=" + name); // view视图
		}
		if (null != brandId) {
			criteria.andBrandIdEqualTo(brandId);
			params.append("&brandId=").append(brandId);
		}
		if (null != isShow) {
			criteria.andIsShowEqualTo(isShow);
			params.append("&isShow=" + isShow); // view视图信息
		} else {
			criteria.andIsShowEqualTo(false);
			params.append("&isShow=" + false); // view视图信息
		}
		// 分页信息
		productQuery.setPageNo(Pagination.cpn(pageNo));
		productQuery.setPageSize(5);
		productQuery.setOrderByClause("id desc");
		List<Product> productList = productMapper.selectByExample(productQuery);
		int totalCount = productMapper.countByExample(productQuery);

		// 封装pagination对象，返回controller
		Pagination pagination = new Pagination(productQuery.getPageNo(), productQuery.getPageSize(), totalCount, productList);
		String url = "/product/list.do";
		pagination.pageView(url, params.toString());

		return pagination;
	}

	// 添加商品
	@Override
	public void insertProduct(Product product) {
		// 添加页面未提交信息 默认下架
		product.setId(jedis.incr("pno"));
		product.setIsShow(false);
		product.setCreateTime(new Date());
		// 保存商品
		productMapper.insertSelective(product);
		// 初始化库存信息
		String[] colors = product.getColors().split(",");
		String[] sizes = product.getSizes().split(",");
		for (String size : sizes) {
			for (String color : colors) {
				Sku sku = new Sku();
				sku.setProductId(product.getId());
				sku.setColorId(Long.parseLong(color));
				sku.setCreateTime(new Date());
				sku.setDeliveFee(0f);
				sku.setMarketPrice(0f);
				sku.setPrice(0f);
				sku.setSize(size);
				sku.setStock(0);
				sku.setUpperLimit(0);
				skuMapper.insertSelective(sku);
			}
		}
		
		
		
	}

}
