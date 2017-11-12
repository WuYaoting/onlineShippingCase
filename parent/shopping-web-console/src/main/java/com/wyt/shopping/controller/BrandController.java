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
public class BrandController {

	@Resource
	private BrandService brandService;

	// 列表查询 分页显示
	@RequestMapping("/list.do")
	public String list(String name, @RequestParam(defaultValue="1") Integer isDisplay, Integer pageNo, Model model) {
		// 查询条件回显
		model.addAttribute("name", name);
		model.addAttribute("isDisplay", isDisplay);
		model.addAttribute("pageNo", pageNo);
		// 条件查询
		Pagination pagination = brandService.selectBrandListHavePage(name, isDisplay, pageNo);
		model.addAttribute("pagination", pagination);
		
		return "brand/list";
	}
	
	// 查询信息 跳转编辑页面
	@RequestMapping("/edit.do")
	public String edit(Long id, Model model) {
		Brand brand = brandService.selectBrandById(id);
		model.addAttribute("brand", brand);
		return "brand/edit";
	}
	// 更新商品品牌信息
	@RequestMapping("/update.do")
	public String update(Brand brand) {
		brandService.updateBrand(brand);
		return "redirect:list.do";
	}
	
	// 跳转添加品牌信息页面
	@RequestMapping("/add.do")
	public String add() {
		return "brand/add";
	}
	// 添加品牌信息 
	@RequestMapping("/save.do")
	public String save(Brand brand) {
		brandService.insertBrand(brand);
		return "redirect:list.do";
	}
	
	// 删除品牌信息
	@RequestMapping("/delete.do")
	public String delete(Long[] ids) {
		brandService.deleteBrands(ids);
		return "forward:list.do";
	}
	
	
	
	
}
