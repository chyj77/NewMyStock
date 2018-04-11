package com.cyj.mystock.info.service;

import com.cyj.mystock.info.bean.FollowStockBean;
import com.cyj.mystock.info.bean.ZtsjBean;
import com.cyj.mystock.info.mapper.FollowStockMapper;
import com.cyj.mystock.info.mapper.ZtsjMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class FollowStockService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FollowStockService.class);

    @Autowired
    private FollowStockMapper mapper;
    @Autowired
    private RedisUtil redisUtil;

    final String FOLLOWSTOCKKEY = "followStock";


    public void setRedisData() {
        Date date1 = new Date();
        redisUtil.remove(FOLLOWSTOCKKEY);
        LOGGER.info("删除redis关注股票耗时={}毫秒",(new Date().getTime()-date1.getTime()));
        date1 = new Date();
        List list = mapper.getAll();
        redisUtil.pipelineSet(FOLLOWSTOCKKEY,list);
        LOGGER.info("保存redis关注股票耗时={}毫秒",(new Date().getTime()-date1.getTime()));
    }

    public boolean isExist(String stockCode) {
        boolean flag = false;
        FollowStockBean bean = mapper.getByStockCode(stockCode);
        if(bean!=null){
            flag = true;
        }
        return flag;
    }
    public String getAll() {
        Set<String> set = redisUtil.range(FOLLOWSTOCKKEY);
        JSONObject jsonObject = new JSONObject();
        if(set!=null && set.size()>0) {
            jsonObject.put("Rows", set);
            jsonObject.put("Total", set.size());
        }
        return jsonObject.toJSONString();
    }

    public void save(FollowStockBean bean) throws Exception {
        Date date1 = new Date();
        if (bean.getId() == null) {
            mapper.insert(bean);
        } else {
            mapper.updateByPrimaryKeySelective(bean);
        }
        setRedisData();
//        redisUtil.add(ztsjKey, beanToJSON(ztsj).toJSONString(), 0);
        LOGGER.info("保存关注股票耗时={}毫秒",(new Date().getTime()-date1.getTime()));
    }
    public void delete(Long recId) throws Exception {
        Date date1 = new Date();
        mapper.deleteByPrimaryKey(recId);
        LOGGER.info("删除关注股票耗时={}毫秒",(new Date().getTime()-date1.getTime()));
        date1 = new Date();
        setRedisData();
        LOGGER.info("重置关注股票耗时={}毫秒",(new Date().getTime()-date1.getTime()));
    }

}
