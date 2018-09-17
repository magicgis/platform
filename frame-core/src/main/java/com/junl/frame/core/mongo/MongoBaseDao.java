package com.junl.frame.core.mongo;

import java.util.List;
import java.util.Map;

public interface MongoBaseDao<T> {

	/**
	 * 
	 * @author xus
	 * @date 2016年5月11日 下午1:19:20
	 * @description 
	 *		通过条件查询实体集合
	 *
	 * @param tableName 查询的表名
	 * @param params 参数集合
	 * @return
	 * @throws Exception
	 *
	 */
	public List<T> find(String tableName, Map<String, Object> params) throws Exception;
	
	/**
	 * 
	 * @author xus
	 * @date 2016年5月11日 下午1:24:30
	 * @description 
	 *		查询所有
	 * @param tableName 表名
	 * @return
	 * @throws Exception
	 *
	 */
	public List<T> findAll(String tableName) throws Exception;
	
	/**
	 * 
	 * @author xus
	 * @date 2016年5月11日 下午1:21:46
	 * @description 
	 *		通过条件查询单个实体对象
	 *
	 * @param tableName 查询的表名
	 * @param params 参数集合
	 * @return
	 * @throws Exception
	 *
	 */
	public T findOne(String tableName, Map<String, Object> params) throws Exception;
	
	/**
	 * 
	 * @author xus
	 * @date 2016年5月11日 下午1:28:03
	 * @description 
	 *		根据id查询
	 *
	 * @param tableName 表名
	 * @param id id参数值
	 * @return
	 * @throws Exception
	 *
	 */
	public T findOne(String tableName, String id) throws Exception;
	
	/**
	 * 
	 * @author xus
	 * @date 2016年5月11日 下午1:26:03
	 * @description 
	 *		保存实体
	 *
	 * @param tableName 表名
	 * @param entity 对象实体
	 * @throws Exception
	 *
	 */
	public void save(String tableName, T entity) throws Exception;
	
	/**
	 * 
	 * @author xus
	 * @date 2016年5月11日 下午2:07:33
	 * @description 
	 *		根据id删除
	 *
	 * @param tableNam 表名
	 * @param id 主键id
	 * @throws Exception
	 *
	 */
	public void remove(String tableNam, String id) throws Exception;
	
}
