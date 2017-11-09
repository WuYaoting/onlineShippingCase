package com.wyt.shopping.controller;

import java.io.File;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author WuYaoting
 * @project_name shopping-web-console
 * @discription 
 */
@Controller
@RequestMapping("/upload")
public class UploadControler {

	// 
	@RequestMapping("/uploadPic.do")
	public void uploadPic(HttpServletRequest req, HttpServletResponse resp, MultipartFile pic) throws Exception {
		if (pic != null && pic.getSize() > 0) {
			// 图片上传至指定地点
			String originalFilename = pic.getOriginalFilename();
			String uuid = UUID.randomUUID().toString().replace("-", "");
			String newFileName = uuid + "." + FilenameUtils.getExtension(originalFilename);
			String realPath = req.getServletContext().getRealPath("");
			String pathname = "\\upload\\" +newFileName;
			String path = realPath + pathname;
			// File file = new File(path);
			pic.transferTo(new File(path));
			
			// 回显图片
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("allUrl", pathname);
			resp.setContentType("application/json;charset=utf-8");
			resp.getWriter().write(jsonObject.toString());
		}
	}
	
	
	
}
