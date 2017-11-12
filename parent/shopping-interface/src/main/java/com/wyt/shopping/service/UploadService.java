package com.wyt.shopping.service;

public interface UploadService {

	String uploadPicToFastDFS(byte[] file_buff, String filename) throws Exception;
	
}
