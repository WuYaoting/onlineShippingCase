package com.wyt.shopping.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wyt.shopping.mapper.ColorMapper;
import com.wyt.shopping.mapper.ProductMapper;
import com.wyt.shopping.mapper.SkuMapper;
import com.wyt.shopping.pojo.Product;
import com.wyt.shopping.pojo.Sku;
import com.wyt.shopping.pojo.SkuQuery;
import com.wyt.shopping.service.CmsService;

@Service("cmsService")
public class CmsServiceImpl implements CmsService {

	
	@Resource
	private ProductMapper productMapper;
	@Resource
	private SkuMapper skuMapper;
	@Resource
	private ColorMapper colorMapper;

	// 根据商品id查询商品信息
	@Override
	public Product selectProductById(Long id) {
		return productMapper.selectByPrimaryKey(id);
	}

	
	// 根据商品id 查询库存信息
	@Override
	public List<Sku> selectSkusByPidAndStockGreaterThanZero(Long productId) {
		SkuQuery skuQuery = new SkuQuery();
		skuQuery.createCriteria().andProductIdEqualTo(productId).andStockGreaterThan(0);
		List<Sku> skus = skuMapper.selectByExample(skuQuery);
		for (Sku sku : skus) {
			sku.setColor(colorMapper.selectByPrimaryKey(sku.getColorId()));
		}
		return skus;
	}

}
