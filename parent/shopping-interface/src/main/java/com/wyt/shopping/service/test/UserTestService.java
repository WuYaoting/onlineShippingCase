package com.wyt.shopping.service.test;

import java.util.List;

import com.wyt.shopping.pojo.test.UserTest;

public interface UserTestService {

	void insertUser(UserTest userTest);
	
	List<UserTest> findUserList();
	
}
