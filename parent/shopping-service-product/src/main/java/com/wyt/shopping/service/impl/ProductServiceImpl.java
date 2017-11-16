package com.wyt.shopping.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.solr.client.solrj.SolrServer;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wyt.shopping.mapper.ProductMapper;
import com.wyt.shopping.mapper.SkuMapper;
import com.wyt.shopping.pojo.Pagination;
import com.wyt.shopping.pojo.Product;
import com.wyt.shopping.pojo.ProductQuery;
import com.wyt.shopping.pojo.ProductQuery.Criteria;
import com.wyt.shopping.pojo.Sku;
import com.wyt.shopping.service.ProductService;

import redis.clients.jedis.Jedis;

@Service("productService")
public class ProductServiceImpl implements ProductService {

	@Resource
	private ProductMapper productMapper;
	@Resource
	private SkuMapper skuMapper;
	@Resource
	private Jedis jedis;
	@Resource
	private SolrServer solrServer;
	@Resource
	private JmsTemplate jmsTemplate;
	

	// 查询品牌列表 分页，封装Pagination数据暂时交给controller
	@Override
	public Pagination selectProductListHavePage(String name, Long brandId, Boolean isShow, Integer pageNo) {
		// 封装productQuery对象，传入Mapper进行条件查询
		ProductQuery productQuery = new ProductQuery();
		Criteria criteria = productQuery.createCriteria();
		StringBuffer params = new StringBuffer();
		// 查询条件信息
		if (null != name && !"".equals(name)) {
			criteria.andNameLike("%" + name + "%");
			params.append("name=" + name); // view视图
		}
		if (null != brandId) {
			criteria.andBrandIdEqualTo(brandId);
			params.append("&brandId=").append(brandId);
		}
		if (null != isShow) {
			criteria.andIsShowEqualTo(isShow);
			params.append("&isShow=" + isShow); // view视图信息
		} else {
			criteria.andIsShowEqualTo(false);
			params.append("&isShow=" + false); // view视图信息
		}
		// 分页信息
		productQuery.setPageNo(Pagination.cpn(pageNo));
		productQuery.setPageSize(5);
		productQuery.setOrderByClause("id desc");
		List<Product> productList = productMapper.selectByExample(productQuery);
		int totalCount = productMapper.countByExample(productQuery);

		// 封装pagination对象，返回controller
		Pagination pagination = new Pagination(productQuery.getPageNo(), productQuery.getPageSize(), totalCount, productList);
		String url = "/product/list.do";
		pagination.pageView(url, params.toString());

		return pagination;
	}

	// 添加商品
	@Transactional
	@Override
	public void insertProduct(Product product) {
		// 添加页面未提交信息 默认下架
		product.setId(jedis.incr("pno"));
		product.setIsShow(false);
		product.setCreateTime(new Date());
		// 保存商品
		productMapper.insertSelective(product);
		// 初始化库存信息
		String[] colors = product.getColors().split(",");
		String[] sizes = product.getSizes().split(",");
		for (String size : sizes) {
			for (String color : colors) {
				Sku sku = new Sku();
				sku.setProductId(product.getId());
				sku.setColorId(Long.parseLong(color));
				sku.setCreateTime(new Date());
				sku.setDeliveFee(0f);
				sku.setMarketPrice(0f);
				sku.setPrice(0f);
				sku.setSize(size);
				sku.setStock(0);
				sku.setUpperLimit(0);
				skuMapper.insertSelective(sku);
			}
		}
	}

	// 商品上架
	@Transactional
	@Override
	public void isShow(Long[] ids) throws Exception {
		// 添加商品信息至索引库
		Product p = new Product();
		p.setIsShow(true);
		for (Long id : ids) {
			p.setId(id);
			productMapper.updateByPrimaryKeySelective(p);
			
			// 发布消息
			jmsTemplate.send(new MessageCreator() {
				
				@Override
				public Message createMessage(Session session) throws JMSException {
					return session.createTextMessage(String.valueOf(id));
				}
			});
			
//			Product product = productMapper.selectByPrimaryKey(id);
//			SolrInputDocument doc = new SolrInputDocument();
//			doc.setField("id", id);
//			doc.setField("name_ik", product.getName());
//			doc.setField("url", product.getImgUrl());
//			doc.setField("brandId", product.getBrandId());
//			// 库存信息 商品最低价
//			doc.setField("price", this.getLowestPrice(id));
//			solrServer.add(doc);
//			solrServer.commit();
		}
	}

	// 根据商品id 获取本商品的最低价
//	private Float getLowestPrice(Long id) {
//		SkuQuery skuQuery = new SkuQuery();
//		skuQuery.createCriteria().andProductIdEqualTo(id);
//		skuQuery.setOrderByClause("price asc");
//		skuQuery.setPageNo(1);
//		skuQuery.setPageSize(1);
//		List<Sku> skus = skuMapper.selectByExample(skuQuery);
//		Sku sku = skus.get(0);
//		Float price = sku.getPrice();
//		return price;
//	}

	
	
	
	
	
}
