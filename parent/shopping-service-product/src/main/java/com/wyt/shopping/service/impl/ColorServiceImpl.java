package com.wyt.shopping.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wyt.shopping.mapper.ColorMapper;
import com.wyt.shopping.pojo.Color;
import com.wyt.shopping.pojo.ColorQuery;
import com.wyt.shopping.service.ColorService;

@Service("colorService")
public class ColorServiceImpl implements ColorService {

	@Resource
	private ColorMapper colorMapper;

	@Override
	public List<Color> selectColorsAndParentIdNoZero() {
		ColorQuery colorQuery = new ColorQuery();
		colorQuery.createCriteria().andParentIdNotEqualTo(0L);
		return colorMapper.selectByExample(colorQuery);
	}


}
