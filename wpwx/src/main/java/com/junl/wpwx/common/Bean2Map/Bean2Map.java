package com.junl.wpwx.common.Bean2Map;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bean2Map implements Serializable{

    /** key变量名 */
    private static final String KEY_NAME = "dataName";
    /** value变量名 */
    private static final String VALUE_NAME = "dataValue";

    /**
     * 将对象的变量转变未map
     * @param obj
     * @param clazz
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static List<Map<String, String>> Bean2MapList(Class clazz, Object obj) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return Bean2MapList(clazz,obj, KEY_NAME, VALUE_NAME);
    }

    /**
     * 将对象的变量转变未map
     * @param obj
     * @param clazz
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static List<Map<String, String>> Bean2MapList(Class clazz, Object obj, String keyName, String valueName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        List<Map<String, String>> returnList = new ArrayList<>();
        Map<String, String> tempMap = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            Bean2MapField an = field.getAnnotation(Bean2MapField.class);
                if(an != null){
                tempMap.put(keyName,an.value());
                String methodName = "get" + field.getName().substring(0,1).toUpperCase() + field.getName().substring(1);
                Method getter = clazz.getDeclaredMethod(methodName);
                tempMap.put(valueName, getter.invoke(clazz, obj).toString());
                returnList.add(tempMap);
                tempMap  = new HashMap<>();
            }
        }
        return returnList;
    }
}
