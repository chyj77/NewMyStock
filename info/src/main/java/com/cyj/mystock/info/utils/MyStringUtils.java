package com.cyj.mystock.info.utils;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class MyStringUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyStringUtils.class);

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
            LOGGER.error("", e);
            e.printStackTrace();
        }
        return jsonObject;
    }
}
