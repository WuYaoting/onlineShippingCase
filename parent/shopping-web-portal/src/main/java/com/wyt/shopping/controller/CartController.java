package com.wyt.shopping.controller;

import java.io.StringWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyt.shopping.pojo.BuyerCart;
import com.wyt.shopping.pojo.BuyerItem;
import com.wyt.shopping.pojo.Order;
import com.wyt.shopping.pojo.Sku;
import com.wyt.shopping.service.BuyerService;
import com.wyt.shopping.service.OrderService;
import com.wyt.shopping.service.SessionProvider;
import com.wyt.shopping.utils.TokenUtil;

@Controller
public class CartController {

	@Resource 
	private BuyerService buyerService;
	@Resource
	private SessionProvider sessionProvider;
	@Resource
	private OrderService orderService;
	
	// 添加购物项至购物车
	@RequestMapping("/shopping/buyerCart")
	public String buyerCart(Long skuId, Integer amount, Model model,
			HttpServletRequest req, HttpServletResponse resp) throws Exception {
		// 获取购物车对象 抽取到本地方法中
		ObjectMapper objectMapper = new ObjectMapper();
		BuyerCart buyerCart = this.getBuyerCartFromCookie(req,objectMapper);
		if(buyerCart == null) {
			// 第一次 创建购物车
			buyerCart = new BuyerCart();
		}
		// 创建购物项 并装车
		// sku瘦身，用于存储再本地cookie中
		Sku sku = new Sku();
		sku.setId(skuId);
		BuyerItem buyerItem = new BuyerItem();
		buyerItem.setAmount(amount);
		buyerItem.setSku(sku);
		buyerCart.addBuyerItem(buyerItem);
		
		// 登录状态 讲购物车保存至redis 同时清空本地cookie中的购物车信息
		String username = sessionProvider.getAttributeForUsername(TokenUtil.getSessionId(req, resp));
		if (username != null) {
			buyerService.insertBuyerCartIntoRedis(username, buyerCart);
			// 清空cookie中的信息
			this.addBuyerCartToCookie(resp, null, 0, "buyerCart");
		} else {
			// 未登录状态保存购物车对象至cookie中 
			StringWriter w = new StringWriter();
			objectMapper.writeValue(w, buyerCart);
			this.addBuyerCartToCookie(resp, w.toString(), 60*60, "buyerCart");
		}
		
		// 瘦身后的sku数据不全
//		List<BuyerItem> buyerItems = buyerCart.getBuyerItems();
//		for (BuyerItem item : buyerItems) {
//			item.setSku(buyerService.selectSkuById(item.getSku().getId()));
//		}
//		
//		model.addAttribute("buyerCart", buyerCart);
//		return "cart";
		
		return "redirect:/shopping/toCart";
		
	}

	// 讲购物车信息存入cookie
	private void addBuyerCartToCookie(HttpServletResponse resp, String w, int time, String cookieName) {
		Cookie cookie = new Cookie(cookieName, w);
		cookie.setPath("/");
		cookie.setMaxAge(time);
		resp.addCookie(cookie);
	}
	
	// 解决刷新页面时，商品订单项重复提交问题，重定向至添加新方法
	@RequestMapping("/shopping/toCart")
	public String toCart(Model model, HttpServletRequest req, HttpServletResponse resp) throws Exception {
		// 未登录从cookie中获取购物车          
		ObjectMapper objectMapper = new ObjectMapper();
		BuyerCart buyerCart = getBuyerCartFromCookie(req, objectMapper);
		
		// 已登陆，从redis中去除购物车
		String username = sessionProvider.getAttributeForUsername(TokenUtil.getSessionId(req, resp));
		if (username != null) {
			// 先将本地cookie中的购物车同步到redis中 然后清除本地
			if (buyerCart != null) {
				buyerService.insertBuyerCartIntoRedis(username, buyerCart);
				this.addBuyerCartToCookie(resp, null, 0, "buyerCart");
			}
			buyerCart = buyerService.getBuyerCartFromRedis(username);
		}
		
		// 返回页面
		if(buyerCart != null) {
			List<BuyerItem> buyerItems = buyerCart.getBuyerItems();
			if(buyerItems != null && buyerItems.size() > 0){
				for (BuyerItem item : buyerItems) {
					// 回填sku的数据
					item.setSku(buyerService.selectSkuById(item.getSku().getId()));
				}
			}
			model.addAttribute("buyerCart", buyerCart);
		}

		return "cart";
		
	}
	
	// 从cookie中获取购物车
	BuyerCart getBuyerCartFromCookie(HttpServletRequest req, ObjectMapper objectMapper) throws Exception {
		// 定义一个空购物车
		BuyerCart buyerCart = null;
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		// 判断是否已有购物车对象
		Cookie[] cookies = req.getCookies();
		if(cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if("buyerCart".equals(cookie.getName())) {
					// 说明cookie中已有购物车对象
					String content = cookie.getValue();
					buyerCart = objectMapper.readValue(content, BuyerCart.class);
					break;
				}
			}
		}
		return buyerCart;
	}
	
	// 去结算
	@RequestMapping("/buyer/trueBuy")
	public String trueBuy(HttpServletRequest req, HttpServletResponse resp, Model model) {
		// 能够进来说明已经登陆
		String username = sessionProvider.getAttributeForUsername(TokenUtil.getSessionId(req, resp));
		BuyerCart buyerCart = buyerService.getBuyerCartFromRedis(username);
		List<BuyerItem> buyerItems = buyerCart.getBuyerItems();
		if (buyerItems != null && buyerItems.size() > 0) {
			Boolean flag = true;
			// 获取完整的库存信息
			for (BuyerItem buyerItem : buyerItems) {
				Sku sku = buyerService.selectSkuById(buyerItem.getSku().getId());
				buyerItem.setSku(sku);
				// 如果购买数量超出库存数量则显示无货
				if (buyerItem.getAmount() > sku.getStock()) {
					buyerItem.setIsHave(false);
					flag = false;
				}
			}
			if (!flag) {
				// 如果无货 则返回 刷新购物车页面 显示无货
				model.addAttribute("buyerCart", buyerCart);
				return "cart";
			}
		} else {
			// 无商品 刷新购物车 去购物
			return "redirect:/shopping/toCart";
		}
 		return "order";
	}
	
	// 下单
	@RequestMapping("/buyer/submitOrder")
	public String submitOrder(Order order, HttpServletRequest req, HttpServletResponse resp) {
		String username = sessionProvider.getAttributeForUsername(TokenUtil.getSessionId(req, resp));
		orderService.insertOrder(order, username);
		return "success";
	}
	
	
	
	
}
