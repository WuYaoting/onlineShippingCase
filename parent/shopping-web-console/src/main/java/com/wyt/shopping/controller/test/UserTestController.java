package com.wyt.shopping.controller.test;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wyt.shopping.pojo.test.UserTest;
import com.wyt.shopping.service.test.UserTestService;

@Controller
@RequestMapping("/test")
public class UserTestController {

	@Resource
	private UserTestService userTestService;
	
	@RequestMapping("/test.do")
	public String test(){
		List<UserTest> userList = userTestService.findUserList();
		for (UserTest userTest : userList) {
			System.out.println(userTest);
		}
		return "test";
	}
	
	@RequestMapping("/test1.do")
	public String test1(){
		return "test1";
	}
	
	
}
