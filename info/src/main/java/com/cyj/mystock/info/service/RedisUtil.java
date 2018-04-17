package com.cyj.mystock.info.service;

import com.cyj.mystock.info.bean.MaretStockBean;
import com.cyj.mystock.info.bean.ZtsjBean;
import com.cyj.mystock.info.utils.MyStringUtils;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redicache 工具类
 *
 */
@SuppressWarnings("unchecked")
@Component
@Log4j2
public class RedisUtil {


    @SuppressWarnings("rawtypes")
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }
    /**
     * 批量删除key
     *
     * @param pattern
     */
    public void removePattern(final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0)
            redisTemplate.delete(keys);
    }
    /**
     * 删除对应的value
     *
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }
    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }
    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public String get(final String key) {
        Object result = null;
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        if(result==null){
            return null;
        }
        return result.toString();
    }
    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
//    public boolean set(final String key, String value) {
//        boolean result = false;
//        try {
//            redisTemplate.opsForSet().add(key,value);
//            result = true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public  boolean hmset(String key, Map<String, String> value) {
        boolean result = false;
        try {
            redisTemplate.opsForHash().putAll(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public  Map<String,String> hmget(String key) {
        Map<String,String> result =null;
        try {
            result=  redisTemplate.opsForHash().entries(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Long size(Object key){
        Long size = 0L;
        try {
            size =redisTemplate.opsForList().size(key);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }
    public Object index(Object key,Long index){
        Object o = null;
        try {
            o =redisTemplate.opsForList().index(key,index);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return o;
    }
    public boolean leftPushAll(Object key , Collection collection){
        boolean result = false;
        try {
            redisTemplate.opsForList().leftPushAll(key,collection);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public boolean add(Object key , Object value,double d){
//        Date date1 = new Date();
        boolean result = false;
        try {
            redisTemplate.opsForZSet().add(key,value,d);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
//        LOGGER.info("保存redis数据耗时={}毫秒",(new Date().getTime()-date1.getTime()));
        return result;
    }
    public Set range(Object key){
        Set result = null;
        try {
            result = redisTemplate.opsForZSet().range(key,0,-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Object pipelineSet(String key,List<?> list){
        RedisCallback<List<Object>> pipelineCallback = new RedisCallback<List<Object>>() {
            @Override
            public List<Object> doInRedis(RedisConnection connection) throws DataAccessException {
                connection.openPipeline();
                byte[] rawKey = redisTemplate.getKeySerializer().serialize(key);
                if (list != null && list.size() > 0) {
                    double d = 0;
                    for (Object bean : list) {
                        JSONObject jsonObject = MyStringUtils.beanToJSON(bean);
                        byte[] v = redisTemplate.getValueSerializer().serialize(jsonObject.toJSONString());
                        connection.zAdd(rawKey,d,v);
                        d++;
                    }
                }
                return connection.closePipeline();
            }
        };
        return redisTemplate.execute(pipelineCallback);
    }
    public Object pipelineSet2(String key,List<MaretStockBean> list){
        RedisCallback<List<Object>> pipelineCallback = new RedisCallback<List<Object>>() {
            @Override
            public List<Object> doInRedis(RedisConnection connection) throws DataAccessException {
                connection.openPipeline();
//                byte[] rawKey = redisTemplate.getKeySerializer().serialize(key);
                if (list != null && list.size() > 0) {
                    double d = 0;
                    for (MaretStockBean bean : list) {
                        String redisKey = key +":"+bean.getStockcode();
                        JSONObject jsonObject = MyStringUtils.beanToJSON(bean);
                        byte[] rawKey = redisTemplate.getKeySerializer().serialize(redisKey);
                        byte[] v = redisTemplate.getValueSerializer().serialize(jsonObject.toJSONString());
                        connection.set(rawKey,v);
                        d++;
                    }
                }
                return connection.closePipeline();
            }
        };
        return redisTemplate.execute(pipelineCallback);
    }

    public Object pipelineSet(String key1,String key2,List<Map> list){
        //pipeline
        RedisCallback<List<Object>> pipelineCallback = new RedisCallback<List<Object>>() {
            @Override
            public List<Object> doInRedis(RedisConnection connection) throws DataAccessException {
                connection.openPipeline();
                byte[] rawKey = redisTemplate.getKeySerializer().serialize(key1);
                if (list != null && list.size() > 0) {
                    double d = 0;
                    for (Map map : list) {
                        Object o = map.get(key2);
                        if(o!=null) {
                            byte[] v = redisTemplate.getValueSerializer().serialize(JSONObject.toJSONString(map));
                            connection.zAdd(rawKey,d,v);
                            d++;
                        }
                    }
                }
                return connection.closePipeline();
            }
        };
        return redisTemplate.execute(pipelineCallback);
    }

}
