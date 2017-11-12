package com.wyt.shopping.utils;

import org.apache.commons.io.FilenameUtils;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.core.io.ClassPathResource;

public class FastDFSUtil {

	public static String uploadPicToFastDFS(byte[] file_buff, String filename) throws Exception {
		
		// 1.加载Fast配置文件
		ClassPathResource resource = new ClassPathResource("fdfs_client.conf");
		// 2.初始化配置文件
		ClientGlobal.init(resource.getClassLoader().getResource("fdfs_client.conf").getPath());
		// 3.创建跟踪服务器客户端
		TrackerClient trackerClient = new TrackerClient();
		// 4.根据客户端获取服务端
		TrackerServer trackerServer = trackerClient.getConnection();
		// 5.根据跟踪服务器创建存储服务器
		StorageClient1 storageClient1 = new StorageClient1(trackerServer, null);
		// 6.附件上传，赶回附件路径,并非完整路径
		String path = storageClient1.upload_file1(file_buff, FilenameUtils.getExtension(filename), null);
		
		return path;
	}
	
}
