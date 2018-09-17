package com.junl.frame.core.fastdfs;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.TrackerClient;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class FastDfsConfigurer implements ApplicationContextAware {
	
	// 配置文件路径
	private String propertyRef;
	// 线程池最小个数
	private String taskref;
	
	// 线程池
	private ThreadPoolTaskExecutor poolExecutor;
	// fastDfs tracker客户端
	private TrackerClient trackerClient;
	
	private ApplicationContext applicationContext;
	
	public void initialize () {
		// 初始化线程池
		poolExecutor = (ThreadPoolTaskExecutor) applicationContext.getBean(this.taskref);
		
		try {
			ClientGlobal.init(FastDfsConfigurer.class.getResource(
							"/" + this.propertyRef).getPath().toString());
			this.trackerClient = new TrackerClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public ThreadPoolTaskExecutor getPoolExecutor () {
		return this.poolExecutor;
	}
	
	public TrackerClient getTrackerClient () {
		return this.trackerClient;
	}
	
	

	/**
	 * @return the taskref
	 */
	public String getTaskref() {
		return taskref;
	}

	/**
	 * @param taskref the taskref to set
	 */
	public void setTaskref(String taskref) {
		this.taskref = taskref;
	}
	
	/**
	 * @return the propertyRef
	 */
	public String getPropertyRef() {
		return propertyRef;
	}

	/**
	 * @param propertyRef the propertyRef to set
	 */
	public void setPropertyRef(String propertyRef) {
		this.propertyRef = propertyRef;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
