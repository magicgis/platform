package com.junl.wpwx.utils;

import org.apache.commons.collections.map.LinkedMap;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FuxinUtil {

    /**
     * 根据某个属性，将list拆分为map
     * @author fuxin
     * @date 2018年1月26日 下午6:44:19
     * @description
     *		TODO
     * @param clazz
     * @param objs
     * @param param
     * @return
     *
     */
    @SuppressWarnings("unchecked")
    public static  <T> Map<Object, List<T>> getTreeDateByParam(Class<T> clazz, List<T> objs, String param){
        Map<Object, List<T>> map = new LinkedMap();
        if(clazz == null || objs == null || objs.size() == 0 || param == null){
            return map;
        }
        try {
            Method methd = clazz.getMethod("get" + param.substring(0,1).toUpperCase() + param.substring(1));
            for(T t : objs){
                Object result = methd.invoke(t);
                if(map.containsKey(result)){
                    map.get(result).add(t);
                }else{
                    List<T> temp = new ArrayList<T>();
                    temp.add(t);
                    map.put(result, temp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return map;
    }
}
