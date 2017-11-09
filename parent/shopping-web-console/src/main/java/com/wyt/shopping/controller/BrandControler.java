package com.wyt.shopping.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wyt.shopping.pojo.Brand;
import com.wyt.shopping.pojo.Pagination;
import com.wyt.shopping.service.BrandService;

/**
 * @author WuYaoting
 * @project_name shopping-web-console
 * @discription 
 */
@Controller
@RequestMapping("/brand")
public class BrandControler {

	@Resource
	private BrandService brandService;

	@RequestMapping("/list.do")
	public String list(String name, @RequestParam(defaultValue="1") Integer isDisplay, Integer pageNo, Model model) {
		// 查询条件回显
		model.addAttribute("name", name);
		model.addAttribute("isDisplay", isDisplay);
		// 条件查询
		Pagination pagination = brandService.selectBrandListHavePage(name, isDisplay, pageNo);
		model.addAttribute("pagination", pagination);
		
		return "brand/list";
	}
	
	@RequestMapping("/edit.do")
	public String edit(Long id, Model model) {
		Brand brand = brandService.selectBrandById(id);
		model.addAttribute("brand", brand);
		return "brand/edit";
	}
	
}
