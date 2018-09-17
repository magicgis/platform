package com.junl.frame.core.fastdfs;

public class UploadResponse {

	/**
	 * fastdfs上传文件成功后会返回的group名
	 * 这个名字用于分段上传用
	 */
	private String group;
	
	/**
	 * 片段上传返回的状态码
	 */
	private int code;
	
	/**
	 * 上传至fastdfs后的路径
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
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
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
