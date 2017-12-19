package com.wyt.shopping.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wyt.shopping.mapper.AdvertisingMapper;
import com.wyt.shopping.mapper.PositionMapper;
import com.wyt.shopping.pojo.Advertising;
import com.wyt.shopping.pojo.AdvertisingQuery;
import com.wyt.shopping.pojo.Position;
import com.wyt.shopping.pojo.PositionQuery;
import com.wyt.shopping.service.AdService;
import com.wyt.shopping.utils.JsonUtil;

import redis.clients.jedis.Jedis;

@Service("adService")
public class AdServiceImpl implements AdService {

	@Resource
	private PositionMapper positionMapper;
	@Resource
	private AdvertisingMapper advertisingMapper;
	@Resource
	private Jedis jedis;
	
	// 根据parentId查询位置信息
	@Override
	public List<Position> selectPosByParentId(Long id) {
		PositionQuery positionQuery = new PositionQuery();
		positionQuery.createCriteria().andParentIdEqualTo(id);
		List<Position> list = positionMapper.selectByExample(positionQuery);
		return list;
	}

	// 根据positionId查询广告信息
	@Override
	public List<Advertising> selectAdsByPositionId(Long id) {
		Position position = positionMapper.selectByPrimaryKey(id);
		AdvertisingQuery advertisingQuery = new AdvertisingQuery();
		advertisingQuery.createCriteria().andPositionIdEqualTo(id);
		List<Advertising> ads = advertisingMapper.selectByExample(advertisingQuery);
		for (Advertising ad : ads) {
			ad.setPosition(position);	// 用于页面回显
		}
		return ads;
	}

	// 添加广告信息
	@Transactional
	@Override
	public void insertAd(Advertising advertising) {
		advertisingMapper.insertSelective(advertising);
	}

	// 轮播图广告
	@Override
	public String selectAdsByPositionIdForPortal(Long id) {
		String ads = jedis.get("ads");
		if (ads == null) {
			AdvertisingQuery advertisingQuery = new AdvertisingQuery();
			advertisingQuery.createCriteria().andPositionIdEqualTo(id);
			List<Advertising> list = advertisingMapper.selectByExample(advertisingQuery);
			ads = JsonUtil.parseObjectToJson(list);
			jedis.set("ads", ads);
		}
		return ads;
	}
	
	
	
	
	
	

}
