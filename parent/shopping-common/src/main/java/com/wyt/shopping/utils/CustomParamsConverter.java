package com.wyt.shopping.utils;

import org.springframework.core.convert.converter.Converter;

public class CustomParamsConverter implements Converter<String, String> {

	@Override
	public String convert(String source) {
		if (null != source && !"".equals(source)) {
			source = source.trim();
			if (!"".equals(source)) {
				return source;
			}
		}
		return null;
	}

}
