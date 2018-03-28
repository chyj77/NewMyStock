package com.cyj.mystock.info.service;

import com.cyj.mystock.info.bean.FollowStockBean;
import com.cyj.mystock.info.bean.SpmmBean;
import com.cyj.mystock.info.bean.ZtsjBean;
import com.cyj.mystock.info.mapper.SpmmMapper;
import com.cyj.mystock.info.mapper.ZtsjMapper;
import com.cyj.mystock.info.utils.MyStringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

@Service
public class SpmmInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpmmInfoService.class);

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
        LOGGER.info("删除买卖逻辑耗时={}毫秒",(new Date().getTime()-date1.getTime()));
        date1 = new Date();
        List<Map> list = spmmMapper.getLuoji();
        String key2= "luoji";
        redisUtil.pipelineSet(ljkey,key2,list);
        LOGGER.info("保存买卖逻辑概念耗时={}毫秒",(new Date().getTime()-date1.getTime()));

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
        Date date1 = new Date();
        redisUtil.remove(stockKey);
        LOGGER.info("删除股票池耗时={}毫秒",(new Date().getTime()-date1.getTime()));
        date1 = new Date();
        List<Map> list = spmmMapper.getStock();
        String key2= "stockcode";
        redisUtil.pipelineSet(stockKey,key2,list);
        LOGGER.info("保存股票池耗时={}毫秒",(new Date().getTime()-date1.getTime()));

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
        LOGGER.info("保存实盘买卖数据耗时={}毫秒",(new Date().getTime()-date1.getTime()));
    }
    public void delete(Long recId) throws Exception {
        Date date1 = new Date();
        spmmMapper.deleteByPrimaryKey(recId);
        LOGGER.info("删除实盘买卖数据耗时={}毫秒",(new Date().getTime()-date1.getTime()));
    }


}
