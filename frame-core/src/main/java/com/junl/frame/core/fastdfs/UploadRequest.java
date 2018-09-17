package com.junl.frame.core.fastdfs;

/**
 * 
 * @author xus
 * @date 2016年5月9日 下午1:49:40
 * @description 
 *		用于接收文件上传分段请求
 */
public class UploadRequest {

	/**
	 * 文件存放fastdfs中的group名称
	 * fastdfs上传后会返回出来
	 */
	private String group;
	
	/**
	 * 当前文件分段索引
	 */
	private String chunk;
	
	/**
	 * 文件名（包含路径）
	 * fastdfs返回的文件名 (/00/00/SDHFJSF.xx)
	 */
	private String fileName;

	/**
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * @return the chunk
	 */
	public String getChunk() {
		return chunk;
	}

	/**
	 * @param chunk the chunk to set
	 */
	public void setChunk(String chunk) {
		this.chunk = chunk;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
