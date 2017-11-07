package com.wyt.shopping.mapper.test;

import java.util.List;

import com.wyt.shopping.pojo.test.UserTest;

public interface UserTestMapper {

	void insertUser(UserTest userTest);
	
	List<UserTest> findUserList();
	
}
