package com.wyt.shopping.service;

import java.util.List;

import com.wyt.shopping.pojo.Color;

public interface ColorService {

	// 查询所有颜色信息
	List<Color> selectColorsAndParentIdNoZero();
	
}
