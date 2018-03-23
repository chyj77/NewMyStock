package com.cyj.mystock.info.service;

import com.cyj.mystock.info.bean.ZtsjBean;
import com.cyj.mystock.info.mapper.ZtsjMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonSimpleJsonParser;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

@Service
public class ZtsjInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZtsjInfoService.class);

    @Autowired
    private ZtsjMapper ztsjMapper;
    @Autowired
    private RedisUtil redisUtil;

    final String gnkey = "ztsjgn";

    final String ztsjKey = "ztsj";

    public void setZtgn() {

        List<Map> list = ztsjMapper.getZtgn();
        redisUtil.remove(gnkey);
        if (list != null && list.size() > 0) {
            double d = 0;
            for (Map map : list) {
                Object o = map.get("ztzdgn");
                if(o!=null) {
                    redisUtil.add(gnkey, JSONObject.toJSONString(map), d);
                    d++;
                }
            }
        }
    }

    public String getZtgn() {
        Set<String> set = redisUtil.range(gnkey);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Rows", set);
        return jsonObject.toJSONString();
    }

    public void setAll() {
        redisUtil.remove(ztsjKey);
        List<ZtsjBean> list = ztsjMapper.getAll();
        if (list != null && list.size() > 0) {
            double d = 0;
            for (ZtsjBean bean : list) {
                JSONObject jsonObject = beanToJSON(bean);
                redisUtil.add(ztsjKey, jsonObject.toJSONString(), d);
                d++;
            }
        }
    }

    public String getAll() {
        Set<String> set = redisUtil.range(ztsjKey);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Rows", set);
        jsonObject.put("Total", set.size());
        return jsonObject.toJSONString();
    }

    public void save(ZtsjBean ztsj) throws Exception {
        Date date1 = new Date();
        if (ztsj.getRecid() == null) {
            ztsjMapper.insert(ztsj);
        } else {
            ztsjMapper.updateByPrimaryKeySelective(ztsj);
        }
        setAll();
//        redisUtil.add(ztsjKey, beanToJSON(ztsj).toJSONString(), 0);
        LOGGER.info("保存涨停数据耗时={}毫秒",(new Date().getTime()-date1.getTime()));
    }
    public void delete(Long recId) throws Exception {
        Date date1 = new Date();
        ztsjMapper.deleteByPrimaryKey(recId);
        setAll();
        LOGGER.info("删除涨停数据耗时={}毫秒",(new Date().getTime()-date1.getTime()));
    }

    private JSONObject beanToJSON(Object obj) {
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
