package com.wyt.shopping.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wyt.shopping.mapper.ColorMapper;
import com.wyt.shopping.mapper.SkuMapper;
import com.wyt.shopping.pojo.Color;
import com.wyt.shopping.pojo.Sku;
import com.wyt.shopping.pojo.SkuQuery;
import com.wyt.shopping.pojo.SkuQuery.Criteria;
import com.wyt.shopping.service.SkuService;

@Service("skuService")
public class SkuServiceImpl implements SkuService {

	@Resource
	private SkuMapper skuMapper;
	@Resource
	private ColorMapper colorMapper;
	
	// 根据商品id查询库存信息
	@Override
	public List<Sku> selectSkusByProductId(Long productId) {
		SkuQuery skuQuery = new SkuQuery();
		Criteria criteria = skuQuery.createCriteria();
		criteria.andProductIdEqualTo(productId);
		List<Sku> skus = skuMapper.selectByExample(skuQuery);
		for (Sku sku : skus) {
			Color color = colorMapper.selectByPrimaryKey(sku.getColorId());
			sku.setColor(color);
		}
		return skus;
	}

	@Override
	public void updateSkuById(Sku sku) {
		skuMapper.updateByPrimaryKeySelective(sku);
	}

}
