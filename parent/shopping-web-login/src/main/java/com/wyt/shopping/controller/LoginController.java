package com.wyt.shopping.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Hex;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wyt.shopping.pojo.Buyer;
import com.wyt.shopping.service.BuyerService;
import com.wyt.shopping.service.SessionProvider;
import com.wyt.shopping.utils.TokenUtil;

@Controller
public class LoginController {

	@Resource
	private BuyerService buyerService;
	@Resource
	private SessionProvider sessionProvider;

	// 跳转登录页面
	@RequestMapping("/login.aspx")
	public String login(String ReturnUrl) {
		return "login";
	}

	// 用户登录
	@RequestMapping(value = "/login.aspx", method = { RequestMethod.POST })
	public String login(String username, String password, String ReturnUrl, HttpServletRequest req,
			HttpServletResponse resp, Model model) {
		// 用户名不为空 从数据库中查询得到用户信息
		if (username != null && !"".equals(username)) {
			Buyer buyer = buyerService.selectBuyerByUsername(username);
			// 查询结果不为空 则说明用户存在
			if (buyer != null) {
				// 判断密码 不为空
				if (password != null && !"".equals(password)) {
					// 比较密码
					if (buyer.getPassword().equals(this.encodePassword(password))) {
						// 验证通过 保存用户信息
						sessionProvider.setAttributeForUsername(TokenUtil.getSessionId(req, resp), username);
						return "redirect:" + ReturnUrl;
					} else {
						model.addAttribute("error", "密码错误");
					}
				} else {
					model.addAttribute("error", "密码不能为空");
				}
			} else {
				model.addAttribute("error", "用户不存在");
			}
		} else {
			model.addAttribute("error", "用户名不能为空");
		}

		return "login";
	}

	// 判断用户是否登陆
	@RequestMapping("/isLogin.aspx")
	@ResponseBody
	public MappingJacksonValue isLogin(String callback, HttpServletRequest req, HttpServletResponse resp) {
		String flag = "0";
		String username = sessionProvider.getAttributeForUsername(TokenUtil.getSessionId(req, resp));
		if(username != null) {
			flag = "1";
		}
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(flag);
		mappingJacksonValue.setJsonpFunction(callback);
		return mappingJacksonValue;
	}

	// 加密规则
	public String encodePassword(String password) {

		String algorithm = "MD5";
		char[] encodeHex = null;
		try {
			MessageDigest instance = MessageDigest.getInstance(algorithm);
			// MD5加密后密文
			byte[] digest = instance.digest(password.getBytes());
			// 十六进制再加密一次
			encodeHex = Hex.encodeHex(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return new String(encodeHex);
	}

}
