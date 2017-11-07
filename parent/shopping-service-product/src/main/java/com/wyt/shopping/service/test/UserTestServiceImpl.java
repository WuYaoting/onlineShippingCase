package com.wyt.shopping.service.test;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wyt.shopping.mapper.test.UserTestMapper;
import com.wyt.shopping.pojo.test.UserTest;

@Service("userTestService")
public class UserTestServiceImpl implements UserTestService {

	@Resource
	private UserTestMapper userTestMapper;
	
	@Override
	public List<UserTest> findUserList() {
		return userTestMapper.findUserList();
	}

	@Transactional
	@Override
	public void insertUser(UserTest userTest) {
		userTestMapper.insertUser(userTest);
		System.out.println(1/0);
	}

}
