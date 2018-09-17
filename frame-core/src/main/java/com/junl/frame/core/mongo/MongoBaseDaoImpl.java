package com.junl.frame.core.mongo;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class MongoBaseDaoImpl<T> implements MongoBaseDao<T> {
	
	@Autowired
	public MongoTemplate mongoTemplate;
	
	/**
	 *   
	 * @author xus
	 * @date 2016年5月11日 下午2:27:30
	 * @description 
	 *		获取需要操作的实体类class 
	 * @return
	 *
	 */
    public Class<T> getEntityClass(){  
        return ReflectionUtils.getSuperClassGenricType(getClass());  
    }
    
    /**
     * 
     * @author xus
     * @date 2016年5月11日 下午2:34:22
     * @description 
     *		对map参数集合转query
     * @param map
     * @return
     *
     */
    public Query getQuery(Map<String, Object> map) {
    	
    	if(map == null || map.isEmpty()) {
    		return null;
    	}
    	Query query = new Query();
        Iterator<Entry<String, Object>> iterable = map.entrySet().iterator();  
        while (iterable.hasNext()) {  
        	Entry<String, Object> entry = iterable.next();
        	query.addCriteria(Criteria.where(entry.getKey()).is(entry.getValue()));
        }
        return query;  
    }

    /*
     * (non-Javadoc)
     * @see com.junl.frame.core.mongo.MongoBaseDao#find(java.lang.String, java.util.Map)
     */
	public List<T> find(String tableName, Map<String, Object> params) throws Exception {
		
		return mongoTemplate.find(this.getQuery(params), this.getEntityClass(), tableName);
	}

	/*
	 * (non-Javadoc)
	 * @see com.junl.frame.core.mongo.MongoBaseDao#findAll(java.lang.String)
	 */
	public List<T> findAll(String tableName) throws Exception {
		
		return mongoTemplate.findAll(this.getEntityClass(), tableName);
	}

	/*
	 * (non-Javadoc)
	 * @see com.junl.frame.core.mongo.MongoBaseDao#findOne(java.lang.String, java.util.Map)
	 */
	public T findOne(String tableName, Map<String, Object> params) throws Exception {
		
		return mongoTemplate.findOne(this.getQuery(params), this.getEntityClass(), tableName);
	}

	/*
	 * (non-Javadoc)
	 * @see com.junl.frame.core.mongo.MongoBaseDao#findOne(java.lang.String, java.lang.String)
	 */
	public T findOne(String tableName, String id) throws Exception {
		
		return mongoTemplate.findOne(new Query(Criteria.where("id").is(id))
						, this.getEntityClass(), tableName);
	}

	/*
	 * (non-Javadoc)
	 * @see com.junl.frame.core.mongo.MongoBaseDao#save(java.lang.String, java.lang.Object)
	 */
	public void save(String tableName, T entity) throws Exception {
		
		mongoTemplate.save(entity, tableName);
	}

	/*
	 * (non-Javadoc)
	 * @see com.junl.frame.core.mongo.MongoBaseDao#remove(java.lang.String, java.lang.String)
	 */
	public void remove(String tableName, String id) throws Exception {
		
		mongoTemplate.remove(new Query(Criteria.where("id").is(id)), this.getEntityClass(), tableName);
	}
    
    
}
