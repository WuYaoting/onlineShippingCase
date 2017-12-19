package com.wyt.shopping.service;

import java.util.List;

import com.wyt.shopping.pojo.Advertising;
import com.wyt.shopping.pojo.Position;

public interface AdService {

	// 根据位置的parentId查询广告的位置信息
	List<Position> selectPosByParentId(Long id);
	
	// 根据positionId查询广告信息
	List<Advertising> selectAdsByPositionId(Long id);
	
	// 添加广告信息
	void insertAd(Advertising advertising);
	
	// 广告轮播图
	String selectAdsByPositionIdForPortal(Long id);
	
}
