package com.wyt.shopping.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wyt.shopping.mapper.DetailMapper;
import com.wyt.shopping.mapper.OrderMapper;
import com.wyt.shopping.pojo.BuyerCart;
import com.wyt.shopping.pojo.BuyerItem;
import com.wyt.shopping.pojo.Detail;
import com.wyt.shopping.pojo.Order;
import com.wyt.shopping.pojo.Sku;
import com.wyt.shopping.service.BuyerService;
import com.wyt.shopping.service.OrderService;

import redis.clients.jedis.Jedis;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

	@Resource
	private OrderMapper orderMapper;
	@Resource
	private DetailMapper detailMapper;
	@Resource
	private Jedis jedis;
	@Resource
	private BuyerService buyerService;
	
	
	// 保存订单
	@Override
	public void insertOrder(Order order, String username) {
		// 封装订单信息
		// 1.封装订单id，redis生成
		Long id = jedis.incr("oid");
		order.setId(id);
		// 2.从购物车中取出库存相关信息，封装订单  价格、运费、总价
		BuyerCart buyerCart = buyerService.getBuyerCartFromRedis(username);
		List<BuyerItem> buyerItems = buyerCart.getBuyerItems();
		for (BuyerItem buyerItem : buyerItems) {
			buyerItem.setSku(buyerService.selectSkuById(buyerItem.getSku().getId()));
		}
		order.setDeliverFee(buyerCart.getFee());
		order.setOrderPrice(buyerCart.getProductPrice());
		order.setTotalPrice(buyerCart.getTotalPrice());
		// 3.支付方式 订单状态 创建订单日期
		if (order.getPaymentWay() == 0) {
			order.setIsPaiy(0);
		} else {
			order.setIsPaiy(1);
		}
		order.setOrderState(0);
		order.setCreateDate(new Date());
		order.setBuyerId(1L);
		orderMapper.insertSelective(order);
		// 封装订单详情表
		for (BuyerItem buyerItem : buyerItems) {
			Detail detail = new Detail();
			Sku sku = buyerItem.getSku();
			detail.setAmount(buyerItem.getAmount());
			detail.setColor(sku.getColor().getName());
			detail.setOrderId(order.getId());
			detail.setPrice(sku.getPrice() * buyerItem.getAmount());
			detail.setProductId(sku.getProductId());
			detail.setProductName(sku.getProduct().getName());
			detail.setSize(sku.getSize());
			detailMapper.insertSelective(detail);
		}
		
		// 所有工作完成后 redis中的购物车不在有用
		jedis.del("buyerCart:" + username);
		
	}

}
