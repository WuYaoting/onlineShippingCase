package com.wyt.shopping.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.wyt.shopping.service.StaticPageService;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class StaticPageServiceImpl implements StaticPageService, ServletContextAware{

	// 实现ServletContextAware接口  获取serveletContext对象
	private ServletContext servletContext;
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	private Configuration configuration;
	
	// 通过注入freeMarkerConfigurer获取configuration对象
	private FreeMarkerConfigurer freeMarkerConfigurer;
	public FreeMarkerConfigurer getFreeMarkerConfigurer() {
		return freeMarkerConfigurer;
	}
	public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
		this.configuration = freeMarkerConfigurer.getConfiguration();
	}

	// 生成静态页面
	@Override
	public void index(Long id, Map<String, Object> rootMap) {
		try {
			String pathname = "/html/product/" + id +".html";
			String path = servletContext.getRealPath(pathname);
			File file = new File(path);
			File parentFile = file.getParentFile();
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
			Template template = configuration.getTemplate("product.html");
			Writer out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			template.process(rootMap, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
