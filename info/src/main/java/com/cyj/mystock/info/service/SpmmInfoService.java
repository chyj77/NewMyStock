package com.cyj.mystock.info.service;

import com.cyj.mystock.info.bean.FollowStockBean;
import com.cyj.mystock.info.bean.MaretStockBean;
import com.cyj.mystock.info.bean.SpmmBean;
import com.cyj.mystock.info.bean.ZtsjBean;
import com.cyj.mystock.info.mapper.SpmmMapper;
import com.cyj.mystock.info.mapper.ZtsjMapper;
import com.cyj.mystock.info.utils.MyStringUtils;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

@Service
@Log4j2
public class SpmmInfoService {

    @Autowired
    private SpmmMapper spmmMapper;
    @Autowired
    private FollowStockService followStockService;
    @Autowired
    private RedisUtil redisUtil;

    final String ljkey = "luoji";

    final String stockKey = "stock";

    public void setLuoji() {
        Date date1 = new Date();
        redisUtil.remove(ljkey);
        log.info("删除买卖逻辑耗时={}毫秒",(new Date().getTime()-date1.getTime()));
        date1 = new Date();
        List<Map> list = spmmMapper.getLuoji();
        String key2= "luoji";
        redisUtil.pipelineSet(ljkey,key2,list);
        log.info("保存买卖逻辑概念耗时={}毫秒",(new Date().getTime()-date1.getTime()));

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
            log.error("",e);
        }
        return jsonArray.toJSONString();
    }
    public void setStock() {
        Date date1 = new Date();
        redisUtil.remove(stockKey);
        log.info("删除股票池耗时={}毫秒",(new Date().getTime()-date1.getTime()));
        date1 = new Date();
        List<MaretStockBean> list = spmmMapper.getStock();
//        String key2= "stockcode";
        redisUtil.pipelineSet2(stockKey,list);
        log.info("保存股票池耗时={}毫秒",(new Date().getTime()-date1.getTime()));

    }
    public String getStock() {
        Date date1 = new Date();
        List<MaretStockBean> list = spmmMapper.getStock();
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for(MaretStockBean bean:list){
            JSONObject jsonBean = MyStringUtils.beanToJSON(bean);
            jsonArray.add(jsonBean);
        }
        jsonObject.put("Rows", jsonArray);
        jsonObject.put("Total",list.size());
        log.info("MYSQL获取股票池耗时={}毫秒",(new Date().getTime()-date1.getTime()));
        return jsonObject.toJSONString();
    }

    public String getStock(String stockcode) {
        String key = stockKey +":"+stockcode;
        String resultStr = redisUtil.get(key);
        return resultStr;
    }

    public String getAll() {
        List<SpmmBean> list = spmmMapper.getAll();
        JSONArray jsonArray = new JSONArray();
        if(list!=null && list.size()>0){
            for(SpmmBean bean:list){
                JSONObject jsonObject = MyStringUtils.beanToJSON(bean);
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
            if(!followStockService.isExist(bean.getCode())){
                FollowStockBean followStockBean = new FollowStockBean();
                followStockBean.setFollowPrice(bean.getJiage());
                followStockBean.setStockcode(bean.getCode());
                followStockBean.setFollowDate(bean.getRq());
                followStockService.save(followStockBean);
            }
        } else {
            spmmMapper.updateByPrimaryKeySelective(bean);
        }
        log.info("保存实盘买卖数据耗时={}毫秒",(new Date().getTime()-date1.getTime()));
    }
    public void delete(Long recId) throws Exception {
        Date date1 = new Date();
        spmmMapper.deleteByPrimaryKey(recId);
        log.info("删除实盘买卖数据耗时={}毫秒",(new Date().getTime()-date1.getTime()));
    }
    public String querySpmmFx() throws Exception{
        Date date1 = new Date();
        List<Map> list = spmmMapper.querySpmmFx();
        log.info("查找实盘分析数据耗时={}毫秒",(new Date().getTime()-date1.getTime()));
        JSONArray jsonArray = new JSONArray();
        if(list!=null && list.size()>0){
            for(Map map:list){
                JSONObject jsonObject1 = ((JSONObject) (new JSONParser().parse(JSONObject.toJSONString(map))));
                jsonArray.add(jsonObject1);
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Rows", jsonArray);
        jsonObject.put("Total",list.size());
        log.info("转换实盘分析数据耗时={}毫秒",(new Date().getTime()-date1.getTime()));
        return jsonObject.toJSONString();
    }

}
