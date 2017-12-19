package com.wyt.shopping.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wyt.shopping.pojo.Brand;
import com.wyt.shopping.pojo.Color;
import com.wyt.shopping.pojo.Pagination;
import com.wyt.shopping.pojo.Product;
import com.wyt.shopping.pojo.Sku;
import com.wyt.shopping.service.AdService;
import com.wyt.shopping.service.CmsService;
import com.wyt.shopping.service.SearchService;

// 前台系统
@Controller
public class SearchController {

	@Resource
	private SearchService searchService;
	@Resource
	private CmsService cmsService;
	@Resource
	private AdService adService;
	
	// 前台系统首页
	@RequestMapping("/")
	public String index(Model model){
		String ads = adService.selectAdsByPositionIdForPortal(89L);
		model.addAttribute("ads", ads);
		return "index";
	}
	
	// 前台页面查询商品信息
	@RequestMapping("/search")
	public String search(String keyword, Long brandId, String price, Integer pageNo, Model model) throws Exception {
		List<Brand> brands = searchService.selectBrandsFromRedis();
		model.addAttribute("brands", brands);
		// 回显信息,用于在页面叠加过滤条件
		model.addAttribute("keyword", keyword);
		model.addAttribute("brandId", brandId);
		model.addAttribute("price", price);
		// 返回分页对象
		Pagination pagination = searchService.selectProductsForPortal(keyword, brandId, price, pageNo);
		model.addAttribute("pagination", pagination);
		
		// 回显过滤条件
		Map<String, String> map = new HashMap<>();
		if (brandId != null) {
			map.put("品牌", searchService.selectBrandNameByIdFromRedis(brandId));
		}
		if (price != null && !"".equals(price)) {
			String[] prices = price.split("-");
			if(prices.length == 2) {
				map.put("价格", price);
			} else {
				map.put("价格", price + "以上");
			}
		}
		model.addAttribute("map", map);
		
		return "search";
	}
	
	// 查看商品详情
	@RequestMapping("/product/detail")
	public String detail(Long id, Model model) {
		// 商品存入model
		Product product = cmsService.selectProductById(id);
		model.addAttribute("product", product);
		// 库存信息存入model中
		List<Sku> skus = cmsService.selectSkusByPidAndStockGreaterThanZero(id);
		model.addAttribute("skus", skus);
		// 颜色信息存入model中
		Set<Color> colors = new HashSet<>();
		for (Sku sku : skus) {
			colors.add(sku.getColor());
		}
		model.addAttribute("colors", colors);
		
		return "product";
	}
	
	
	
}
