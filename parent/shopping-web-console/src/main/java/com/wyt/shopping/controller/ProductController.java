package com.wyt.shopping.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wyt.shopping.pojo.Brand;
import com.wyt.shopping.pojo.Color;
import com.wyt.shopping.pojo.Pagination;
import com.wyt.shopping.pojo.Product;
import com.wyt.shopping.service.BrandService;
import com.wyt.shopping.service.ColorService;
import com.wyt.shopping.service.ProductService;

@Controller
@RequestMapping("/product")
public class ProductController {

	@Resource
	private ProductService productService;
	@Resource
	private BrandService brandService;
	@Resource
	private ColorService colorService;
	
	@RequestMapping("/list.do")
	public String list(Model model, String name, Long brandId, boolean isShow, Integer pageNo) {
		// 查询所有品牌信息 用于回显
		List<Brand> brands = brandService.selectBrandListNoPage(null, 1);
		model.addAttribute("brands", brands);
		// 列表显示
		Pagination pagination = productService.selectProductListHavePage(name, brandId, isShow, pageNo);
		model.addAttribute("pagination", pagination);
		// 查询条件
		model.addAttribute("name", name);
		model.addAttribute("brandId", brandId);
		model.addAttribute("isShow", isShow);
		model.addAttribute("pageNo", pageNo);
		return "product/list";
	}
	
	@RequestMapping("/add.do")
	public String add(Model model) {
		List<Brand> brands = brandService.selectBrandListNoPage(null, 1);
		List<Color> colors = colorService.selectColorsAndParentIdNoZero();
		model.addAttribute("brands", brands);
		model.addAttribute("colors", colors);
		return "product/add";
	}
	
	@RequestMapping("/save.do")
	public String save(Product product) {
		productService.insertProduct(product);
		return "redirect:list.do";
	}
	
	
}
