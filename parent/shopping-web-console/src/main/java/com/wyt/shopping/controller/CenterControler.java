package com.wyt.shopping.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author WuYaoting
 * @project_name shopping-web-console
 * @discription 
 */
@Controller
@RequestMapping("/center")
public class CenterControler {

	/**
	 * @discription 返回后台主页
	 * @return
	 */
	@RequestMapping("/index.do")
	public String index(){
		return "index";
	}
	/**
	 * @discription 返回后台的main页面
	 * @return
	 */
	@RequestMapping("/main.do")
	public String main(){
		return "main";
	}
	/**
	 * @discription 返回后台的top页面
	 * @return
	 */
	@RequestMapping("/top.do")
	public String top(){
		return "top";
	}
	/**
	 * @discription 返回后台的right页面
	 * @return
	 */
	@RequestMapping("/right.do")
	public String right(){
		return "right";
	}
	/**
	 * @discription 返回后台的left页面
	 * @return
	 */
	@RequestMapping("/left.do")
	public String left(){
		return "left";
	}
	
}
