package com.wyt.shopping.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wyt.shopping.pojo.Advertising;
import com.wyt.shopping.service.AdService;

@Controller
@RequestMapping("/ad")
public class AdController {

	@Resource
	private AdService adService;

	// 根据positionId选择广告信息，返回页面显示
	@RequestMapping("/list.do")
	public String list(Long root, Model model) {
		List<Advertising> ads = adService.selectAdsByPositionId(root);
		model.addAttribute("ads", ads);
		model.addAttribute("positionId", root);
		return "ad/list";
	}

	// 跳转至添加广告页面
	@RequestMapping("/add.do")
	public String add(Long positionId, Model model) {
		model.addAttribute("positionId", positionId);
		return "ad/add";
	}

	// 添加广告信息
	@RequestMapping("/save.do")
	public String save(Advertising advertising) {
		adService.insertAd(advertising);
		return "redirect:list.do?root=" + advertising.getPositionId();
	}

}
