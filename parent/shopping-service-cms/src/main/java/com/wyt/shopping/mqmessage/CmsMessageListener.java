package com.wyt.shopping.mqmessage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;

import com.wyt.shopping.pojo.Color;
import com.wyt.shopping.pojo.Product;
import com.wyt.shopping.pojo.Sku;
import com.wyt.shopping.service.CmsService;
import com.wyt.shopping.service.StaticPageService;

public class CmsMessageListener implements MessageListener {

	@Resource
	private CmsService cmsService;
	@Resource
	private StaticPageService staticPageService;
	
	@Override
	public void onMessage(Message message) {
		try {
			ActiveMQTextMessage activeMQTextMessage = (ActiveMQTextMessage) message;
			long id = Long.parseLong(activeMQTextMessage.getText());
			Map<String, Object> map = new HashMap<>();
			Product product = cmsService.selectProductById(id);
			map.put("product", product);
			List<Sku> skus = cmsService.selectSkusByPidAndStockGreaterThanZero(id);
			map.put("skus", skus);
			Set<Color> colors = new HashSet<>();
			for (Sku sku : skus) {
				colors.add(sku.getColor());
			}
			map.put("colors", colors);
			staticPageService.index(id, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
