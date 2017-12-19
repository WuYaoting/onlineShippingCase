package com.wyt.shopping.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wyt.shopping.mapper.BuyerMapper;
import com.wyt.shopping.mapper.ColorMapper;
import com.wyt.shopping.mapper.ProductMapper;
import com.wyt.shopping.mapper.SkuMapper;
import com.wyt.shopping.pojo.Buyer;
import com.wyt.shopping.pojo.BuyerCart;
import com.wyt.shopping.pojo.BuyerItem;
import com.wyt.shopping.pojo.BuyerQuery;
import com.wyt.shopping.pojo.Sku;
import com.wyt.shopping.service.BuyerService;

import redis.clients.jedis.Jedis;

@Service("buyerService")
public class BuyerServiceImpl implements BuyerService {

	@Resource
	private BuyerMapper buyerMapper;
	@Resource
	private SkuMapper skuMapper;
	@Resource
	private ColorMapper colorMapper;
	@Resource
	private ProductMapper productMapper;
	@Resource
	private Jedis jedis;
	
	
	
	// 根据用户名查出用户
	@Override
	public Buyer selectBuyerByUsername(String username) {
		BuyerQuery buyerQuery = new BuyerQuery();
		buyerQuery.createCriteria().andUsernameEqualTo(username);
		List<Buyer> list = buyerMapper.selectByExample(buyerQuery);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	// 根据skuId查询sku信息,用于购物车页面会显商品信息
	@Override
	public Sku selectSkuById(Long skuId) {
		Sku sku = skuMapper.selectByPrimaryKey(skuId);
		sku.setColor(colorMapper.selectByPrimaryKey(sku.getColorId()));
		sku.setProduct(productMapper.selectByPrimaryKey(sku.getProductId()));
		return sku;
	}

	// 登录状态保存购物车至redis
	/**
	 * 保存什么数据 采用哪种数据结构
	 */
	@Override
	public void insertBuyerCartIntoRedis(String username, BuyerCart buyerCart) {
		List<BuyerItem> buyerItems = buyerCart.getBuyerItems();
		if (buyerItems != null && buyerItems.size() > 0) {
			for (BuyerItem buyerItem : buyerItems) {
				Long skuId = buyerItem.getSku().getId();
				Integer amount = buyerItem.getAmount();
				// 防止同款商品被覆盖
				if (jedis.hexists("buyerCart:" + username, String.valueOf(skuId))) {
					jedis.hincrBy("buyerCart:" + username, String.valueOf(skuId), amount);
				} else {
					jedis.hset("buyerCart:" + username, String.valueOf(skuId), String.valueOf(amount));
				}
			}
			
		}
	}

	// 登录状态下从reids中取出购物车信息
	@Override
	public BuyerCart getBuyerCartFromRedis(String username) {
		// 创建购物车对象 用于封装数据
		BuyerCart buyerCart = new BuyerCart();
		Map<String, String> map = jedis.hgetAll("buyerCart:" + username);
		Set<Entry<String,String>> entrySet = map.entrySet();
		for (Entry<String, String> entry : entrySet) {
			Long skuId = Long.parseLong(entry.getKey());
			Integer amount = Integer.valueOf(entry.getValue());
			BuyerItem buyerItem = new BuyerItem();
			Sku sku = new Sku();
			sku.setId(skuId);
			buyerItem.setSku(sku);
			buyerItem.setAmount(amount);
			buyerCart.addBuyerItem(buyerItem);
		}
		
		return buyerCart;
	}

}
