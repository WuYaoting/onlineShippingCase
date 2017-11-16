package com.wyt.shopping.mqmessage;

import javax.annotation.Resource;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;

import com.wyt.shopping.service.SearchService;

public class CustomMessageListener implements MessageListener {

	@Resource
	private SearchService searchService;
	
	// 商品信息保存至索引库
	@Override
	public void onMessage(Message message) {
		try {
			ActiveMQTextMessage activeMQTextMessage = (ActiveMQTextMessage) message;
			String id = activeMQTextMessage.getText();
			searchService.insertProductToSolr(Long.parseLong(id));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
