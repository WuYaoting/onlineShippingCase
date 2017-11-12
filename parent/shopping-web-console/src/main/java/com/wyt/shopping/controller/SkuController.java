package com.wyt.shopping.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wyt.shopping.pojo.Sku;
import com.wyt.shopping.service.SkuService;

/**
 * @author WuYaoting
 * @project_name shopping-web-console
 * @discription 
 */
@Controller
@RequestMapping("/sku")
public class SkuController {

	@Resource
	private SkuService skuService;

	// 根据商品id查询库存信息
	@RequestMapping("/list.do")
	public String list(Model model, Long productId) {
		List<Sku> skus = skuService.selectSkusByProductId(productId);
		model.addAttribute("skus", skus);
		return "sku/list";
	}
	
	// 更新库存信息
	@RequestMapping("/update.do")
	public void update(Sku sku, HttpServletResponse resp) throws IOException {
		skuService.updateSkuById(sku);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg", "保存成功");
		resp.setContentType("application/json;charset=utf-8");
		resp.getWriter().write(jsonObject.toString());
	}
	
}
