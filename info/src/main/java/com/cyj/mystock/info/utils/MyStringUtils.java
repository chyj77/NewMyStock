package com.cyj.mystock.info.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

@Log4j2
public class MyStringUtils {


    public static ObjectMapper mapper=new ObjectMapper();

    static{
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true) ;
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true) ;
    }

    public static JSONObject beanToJSON(Object obj) {
        JSONObject jsonObject = new JSONObject();
        try {
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                Object o = field.get(obj) == null ? "" : field.get(obj);
                jsonObject.put(field.getName(), o);
            }
        } catch (Exception e) {
            log.error("", e);
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     *
     * 用途：获取集合类型
     * @date 2016年9月29日
     * @param collectionClass
     * @param elementClasses
     * @return
     */
    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    /**
     *
     * 用途：将字符串解析成map
     * @date 2016年9月29日
     * @param params
     * @return
     */
    public static Map<String,Object> fromJson(String params){
        try {
            JavaType jType=getCollectionType(Map.class,String.class,Object.class);
            return mapper.readValue(params, jType);
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    /**
     *
     * 用途：将字符串解析成指定javatype
     *
     * @date 2016年9月29日
     * @param params
     * @param javaType
     * @return
     */
    public static <T>T toBean(String params,JavaType javaType){
        try {
            return mapper.readValue(params, javaType);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     *
     * 用途：将字符串解析成指定class的对象
     * @date 2016年9月29日
     * @param params
     * @param clazz
     * @return
     */
    public static <T>T toBean(String params,Class<T> clazz) {
        try {
            return mapper.readValue(params, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * 用途：将对象序列化成字符串
     *
     * @date 2016年9月29日
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            return null;
        }
    }
}
