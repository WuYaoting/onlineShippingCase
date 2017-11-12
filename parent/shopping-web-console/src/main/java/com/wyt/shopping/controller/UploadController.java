package com.wyt.shopping.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.wyt.shopping.service.UploadService;
import com.wyt.shopping.utils.MyConstants;

/**
 * @author WuYaoting
 * @project_name shopping-web-console
 * @discription 
 */
@Controller
@RequestMapping("/upload")
public class UploadController {

	@Resource
	private UploadService uploadService;
	
	// 图片上传
	@RequestMapping("/uploadPic.do")
	public void uploadPic(HttpServletRequest req, HttpServletResponse resp, MultipartFile pic) throws Exception {
		if (pic != null && pic.getSize() > 0) {
			// 图片上传至指定地点
			String path = uploadService.uploadPicToFastDFS(pic.getBytes(), pic.getOriginalFilename());
			String allUrl = MyConstants.IMG_URL + path;
			
			// 回显图片
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("allUrl", allUrl);
			jsonObject.put("imgUrl", path);
			resp.setContentType("application/json;charset=utf-8");
			resp.getWriter().write(jsonObject.toString());
		}
	}
	
	// 多图片上传
	@RequestMapping("/uploadPics.do")
	@ResponseBody
	public List<String> uploadPics(@RequestParam MultipartFile[] pics) throws Exception {
		if (pics != null && pics.length > 0) {
			List<String> urls = new ArrayList<String>();
			// 图片上传至指定地点
			for (MultipartFile pic : pics) {
				String path = uploadService.uploadPicToFastDFS(pic.getBytes(), pic.getOriginalFilename());
				String allUrl = MyConstants.IMG_URL + path;
				urls.add(allUrl);
			}
			return urls;
		}
		return null;
	}

	// 利用富文本编辑插件 上传
	@RequestMapping("/uploadFck.do")
	public void uploadFck(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		// 强转 取得附件信息
		MultipartRequest request = (MultipartRequest) req;
		Set<Entry<String,MultipartFile>> entrySet = request.getFileMap().entrySet();
		for (Entry<String, MultipartFile> entry : entrySet) {
			MultipartFile pic = entry.getValue();
			String path = uploadService.uploadPicToFastDFS(pic.getBytes(), pic.getOriginalFilename());
			String url = MyConstants.IMG_URL + path;
			JSONObject jsonObject = new JSONObject();
			// 回显
			jsonObject.put("url", url);
			jsonObject.put("error", 0);
			resp.setContentType("application/json;charset=utf-8");
			resp.getWriter().write(jsonObject.toString());
		}
	}
	
	
}

