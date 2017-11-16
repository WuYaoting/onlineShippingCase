package com.wyt.shopping.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wyt.shopping.service.UploadService;
import com.wyt.shopping.utils.FastDFSUtil;

@Service("uploadService")
public class UploadServiceImpl implements UploadService{

	// 上传图片，返回图片存储路径 非完整路径
	@Transactional
	@Override
	public String uploadPicToFastDFS(byte[] file_buff, String filename) throws Exception {
		return FastDFSUtil.uploadPicToFastDFS(file_buff, filename);
	}
	
}
