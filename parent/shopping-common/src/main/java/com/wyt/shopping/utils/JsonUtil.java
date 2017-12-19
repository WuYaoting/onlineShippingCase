package com.wyt.shopping.utils;

import java.io.StringWriter;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

	public static String parseObjectToJson(Object object) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setSerializationInclusion(Include.NON_NULL);
			StringWriter w = new StringWriter();
			objectMapper.writeValue(w, object);
			return w.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
