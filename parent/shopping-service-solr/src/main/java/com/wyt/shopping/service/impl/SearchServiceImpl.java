package com.wyt.shopping.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import com.wyt.shopping.mapper.ProductMapper;
import com.wyt.shopping.mapper.SkuMapper;
import com.wyt.shopping.pojo.Brand;
import com.wyt.shopping.pojo.Pagination;
import com.wyt.shopping.pojo.Product;
import com.wyt.shopping.pojo.ProductQuery;
import com.wyt.shopping.pojo.Sku;
import com.wyt.shopping.pojo.SkuQuery;
import com.wyt.shopping.service.SearchService;

import redis.clients.jedis.Jedis;

@Service("searchService")
public class SearchServiceImpl implements SearchService {

	@Resource
	private SolrServer solrServer;
	@Resource
	private Jedis jedis;
	@Resource
	private SkuMapper skuMapper;
	@Resource
	private ProductMapper productMapper;
	
	// 查询solr搜索的商品信息 查询到商品的id,name,price,imgUrl,brandId 五个信息 封装成分页对象返回前台页面
	@Override
	public Pagination selectProductsForPortal(String keyword, Long brandId, String price, Integer pageNo) throws Exception {
		// 1.通过solr查询得到 符合条件的上架商品的相关信息
		SolrQuery solrQuery = new SolrQuery();
		// 根据关键字检索  关键字过滤
		StringBuilder params = new StringBuilder();
		if (keyword != null && !"".equals(keyword)) {
			solrQuery.setQuery("name_ik:" + keyword);
			params.append("keyword=").append(keyword);
		}
		// 品牌过滤
		if (brandId != null) {
			solrQuery.addFilterQuery("brandId:" + brandId);
			params.append("&brandId=").append(brandId);
		}
		// 价格过滤
		if (price != null && !"".equals(price)) {
			String[] prices = price.split("-");
			if (prices.length == 2) {
				solrQuery.addFilterQuery("price:[" + prices[0] + " TO " + prices[1] + "]");
			} else {
				solrQuery.addFilterQuery("price:[" + price + " TO *]");
			}
			params.append("&price=").append(price);
		}
		// 设置高亮
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("name_ik");
		solrQuery.setHighlightSimplePre("<font color='red'>");
		solrQuery.setHighlightSimplePost("</font>");
		// 根据价格排序
		solrQuery.addSort("price", ORDER.asc);
		// 结果分页显示
		ProductQuery productQuery = new ProductQuery();
		productQuery.setPageNo(Pagination.cpn(pageNo));
		productQuery.setPageSize(8);
		solrQuery.setStart(productQuery.getStartRow());
		solrQuery.setRows(productQuery.getPageSize());
		// 查询结果
		QueryResponse response = solrServer.query(solrQuery);
		
		// 2.结果集
		// 普通结果集
		SolrDocumentList results = response.getResults();
		long numFound = results.getNumFound();	// 总记录数
		// 高亮结果集
		Map<String, Map<String, List<String>>> highLightResults = response.getHighlighting();
		
		// 3.结果集处理
		List<Product> list = getProductList(results, highLightResults);
		
		// 4.返回分页对象
		Pagination pagination = new Pagination(productQuery.getPageNo(), productQuery.getPageSize(), (int)numFound, list);
		String url = "/search";
		pagination.pageView(url, params.toString());
		
		return pagination;
	}

	// 处理结果集
	private List<Product> getProductList(SolrDocumentList results,
			Map<String, Map<String, List<String>>> highLightResults) {
		List<Product> list = new ArrayList<Product>();
		for (SolrDocument solrDocument : results) {
			Product product = new Product();
			// 普通结果集封装
			String id = solrDocument.getFieldValue("id").toString();
			product.setId(Long.parseLong(id));
			product.setBrandId(Long.parseLong(solrDocument.getFieldValue("brandId").toString()));
			product.setPrice(solrDocument.getFieldValue("price").toString());
			product.setImgUrl(solrDocument.getFieldValue("url").toString());
			// 高亮结果集封装
			List<String> names = highLightResults.get(id).get("name_ik");
			if(names != null && names.size() > 0) {
				product.setName(names.get(0));
			} else {
				product.setName(solrDocument.getFieldValue("name_ik").toString());
			}
			list.add(product);
		}
		return list;
	}

	// 返回redis中的品牌信息
	@Override
	public List<Brand> selectBrandsFromRedis() {
		Map<String, String> mapBrands = jedis.hgetAll("brand");
		List<Brand> brands = new ArrayList<>();
		for (Entry<String, String> entry : mapBrands.entrySet()) {
			Brand brand = new Brand();
			brand.setId(Long.parseLong(entry.getKey()));
			brand.setName(entry.getValue());
			brands.add(brand);
		}
		return brands;
	}

	// 根据品牌的id从redis中去除品牌的name
	@Override
	public String selectBrandNameByIdFromRedis(Long id) {
		return jedis.hget("brand", String.valueOf(id));
	}

	// 将商品信息存入solr中
	@Override
	public void insertProductToSolr(Long id) throws Exception {
		Product product = productMapper.selectByPrimaryKey(id);
		SolrInputDocument doc = new SolrInputDocument();
		doc.setField("id", id);
		doc.setField("name_ik", product.getName());
		doc.setField("url", product.getImgUrl());
		doc.setField("brandId", product.getBrandId());
		// 库存信息 商品最低价
		doc.setField("price", this.getLowestPrice(id));
		solrServer.add(doc);
		solrServer.commit();
	}
	
	
	// 根据商品id 获取本商品的最低价
	private Float getLowestPrice(Long id) {
		SkuQuery skuQuery = new SkuQuery();
		skuQuery.createCriteria().andProductIdEqualTo(id);
		skuQuery.setOrderByClause("price asc");
		skuQuery.setPageNo(1);
		skuQuery.setPageSize(1);
		List<Sku> skus = skuMapper.selectByExample(skuQuery);
		Sku sku = skus.get(0);
		Float price = sku.getPrice();
		return price;
	}

	
	
	
	
	
	
	
}
