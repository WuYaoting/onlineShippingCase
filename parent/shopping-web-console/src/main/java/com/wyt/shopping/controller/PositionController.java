package com.wyt.shopping.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wyt.shopping.pojo.Position;
import com.wyt.shopping.service.AdService;

@Controller
@RequestMapping("/position")
public class PositionController {

	@Resource
	private AdService adService;
	
	// 查询广告位置信息，返回页面显示
	@RequestMapping("/tree.do")
	public String tree(String root, Model model) {
		List<Position> list = null;
		if ("source".equals(root)) {
			list = adService.selectPosByParentId(0L);
		} else {
			list = adService.selectPosByParentId(Long.parseLong(root));
		}
		model.addAttribute("list", list);
		return "position/tree";
	}

	
}
