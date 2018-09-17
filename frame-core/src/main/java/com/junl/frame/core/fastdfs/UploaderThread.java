package com.junl.frame.core.fastdfs;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

/**
 * 
 * @author xus
 * @date 2016年5月5日 下午6:04:26
 * @description 
 *		TODO
 */
public class UploaderThread implements Runnable {
	
	private TrackerClient trackerClient;
	// 文件
	private byte[] fileBytes;
	// 后缀
	private String suffix;
	
	private NameValuePair[] nameValuePair; 
	
	public UploaderThread (byte[] fileBytes, String suffix
			, NameValuePair[] nameValuePair, TrackerClient trackerClient) {
		
		this.fileBytes = fileBytes;
		this.suffix = suffix;
		this.nameValuePair = nameValuePair;
		this.trackerClient = trackerClient;
	}
	
	
	public void run () {
		try {
			TrackerServer trackerServer = trackerClient.getConnection();
			StorageServer storageServer = null;
			StorageClient storageClient = new StorageClient(trackerServer, storageServer);
			String fileIds[] = storageClient.upload_file(
						this.fileBytes, this.suffix, this.nameValuePair);
			System.out.println("组名：" + fileIds[0]); 
	        System.out.println("路径: " + fileIds[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
