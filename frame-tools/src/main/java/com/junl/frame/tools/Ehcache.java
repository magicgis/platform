package com.junl.frame.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

@Component
public class Ehcache {
	
	/** 地区缓存pid */
	public final static String KEY_AREA_PID = "AREA_PID_" ;
	/** 地区缓存id */
	public final static String KEY_AREA_ID = "AREA_ID_" ;
	/** 民族缓存id */
	public final static String KEY_NATION_ALL = "NATION_ID_ALL" ;
	/** 出生医院缓存id */
	public final static String KEY_HOSTIPAL_ALL = "KEY_HOSTIPAL_ALL" ;
	/** 社区缓存id */
	public final static String KEY_COMMUNITYLIST_ALL = "KEY_COMMUNITYLIST_ALL" ;
	/** 产品缓存id */
	public final static String KEY_PRODUCT_ID = "KEY_PRODUCT_ID_" ;
	/** 风险告知书缓存小类id */
	public final static String KEY_DISCLOSURE_VACID = "KEY_DISCLOSURE_VACID_" ;
	/** 字典类缓存 */
	public final static String KEY_DICT = "KEY_DICT_NAME_" ;
	
	@Autowired
	private CacheManager cacheManager;
	
	public void putCache(String key,String value,String typeCache) throws Exception {
		cacheManager = CacheManager.create();
		Cache cache = cacheManager.getCache(typeCache);
		Element element = new Element(key, value);
		cache.put(element);
		
	}
	
/*	public String getCache(String key,String typeCache) throws Exception {
		cacheManager = CacheManager.create();
		Cache cache = cacheManager.getCache(typeCache);
		Element element = cache.get(key);
		if(element == null){
			return "";
		}
		cache.get(element);
		return element.getValue().toString();
	}*/
	
	public void putCache(String key,Object value,String typeCache) throws Exception {
		cacheManager = CacheManager.create();
		Cache cache = cacheManager.getCache(typeCache);
		Element element = new Element(key, value);
		cache.put(element);
		
	}
	
	public Object getCache(String key,String typeCache) throws Exception {
		cacheManager = CacheManager.create();
		Cache cache = cacheManager.getCache(typeCache);
		Element element = cache.get(key);
		if(element == null){
			return null;
		}
		cache.get(element);
		return element.getValue();
	}
}	