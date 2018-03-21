package com.cyj.mystock.info.service;

import com.cyj.mystock.info.bean.SpmmBean;
import com.cyj.mystock.info.bean.ZtsjBean;
import com.cyj.mystock.info.mapper.SpmmMapper;
import com.cyj.mystock.info.mapper.ZtsjMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

@Service
public class SpmmInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpmmInfoService.class);

    @Autowired
    private SpmmMapper spmmMapper;
    @Autowired
    private RedisUtil redisUtil;

    final String ljkey = "luoji";

    final String stockKey = "stock";

    public void setLuoji() {
        List<Map> list = spmmMapper.getLuoji();
        redisUtil.remove(ljkey);
        if (list != null && list.size() > 0) {
            double d = 0;
            for (Map map : list) {
                Object o = map.get("luoji");
                if(o!=null) {
                    redisUtil.add(ljkey, JSONObject.toJSONString(map), d);
                    d++;
                }
            }
        }
    }

    public String getLuoji() {
        Set<String> set = redisUtil.range(ljkey);
        JSONArray jsonArray= new JSONArray();
        try {
            for(String json:set){
                JSONObject jsonObj = (JSONObject)(new JSONParser().parse(json));
                jsonArray.add(jsonObj);
            }
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("",e);
        }
        return jsonArray.toJSONString();
    }
    public void setStock() {
        List<Map> list = spmmMapper.getStock();
        redisUtil.remove(stockKey);
        if (list != null && list.size() > 0) {
            double d = 0;
            for (Map map : list) {
                Object o = map.get("stockcode");
                if(o!=null) {
                    redisUtil.add(stockKey, JSONObject.toJSONString(map), d);
                    d++;
                }
            }
        }
    }

    public String getStock() {
        Set<String> set = redisUtil.range(stockKey);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Rows", set);
        jsonObject.put("Total",set.size());
        return jsonObject.toJSONString();
    }


    public String getAll() {
        List<SpmmBean> list = spmmMapper.getAll();
        JSONArray jsonArray = new JSONArray();
        if(list!=null && list.size()>0){
            for(SpmmBean bean:list){
                JSONObject jsonObject = beanToJSON(bean);
                jsonArray.add(jsonObject);
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Rows", jsonArray);
        jsonObject.put("Total",list.size());
        return jsonObject.toJSONString();
    }

    public void save(SpmmBean bean) throws Exception {
        Date date1 = new Date();
        if (bean.getRecid() == null) {
            spmmMapper.insert(bean);
        } else {
            spmmMapper.updateByPrimaryKeySelective(bean);
        }
        LOGGER.info("保存实盘买卖数据耗时={}毫秒",(new Date().getTime()-date1.getTime()));
    }
    public void delete(Long recId) throws Exception {
        Date date1 = new Date();
        spmmMapper.deleteByPrimaryKey(recId);
        LOGGER.info("删除实盘买卖数据耗时={}毫秒",(new Date().getTime()-date1.getTime()));
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
