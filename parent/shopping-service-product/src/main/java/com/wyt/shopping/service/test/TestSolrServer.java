package com.wyt.shopping.service.test;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations={"classpath:spring-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestSolrServer {

	@Resource
	private SolrServer solrServer;
	
	// 测试solr
	@Test
	public void testSolrServer() throws Exception {
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", "1");
		doc.addField("name_ik", "测试Solr与spring的整合");
		solrServer.add(doc);
		solrServer.commit();
	}

}
