package com.cyj.mystock.info.service;

import com.cyj.mystock.info.bean.ZtsjBean;
import com.cyj.mystock.info.mapper.ZtsjMapper;
import com.cyj.mystock.info.utils.MyStringUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Date date1 = new Date();
        redisUtil.remove(gnkey);
        LOGGER.info("删除redis涨停概念耗时={}毫秒",(new Date().getTime()-date1.getTime()));
        date1 = new Date();
        List<Map> list = ztsjMapper.getZtgn();
        String key2= "ztzdgn";
        redisUtil.pipelineSet(gnkey,key2,list);

        LOGGER.info("保存redis涨停概念耗时={}毫秒",(new Date().getTime()-date1.getTime()));

    }

    public String getZtgn() {
        Set<String> set = redisUtil.range(gnkey);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Rows", set);
        return jsonObject.toJSONString();
    }

    public void setAll() {
        Date date1 = new Date();
        redisUtil.remove(ztsjKey);
        LOGGER.info("删除redis涨停数据耗时={}毫秒",(new Date().getTime()-date1.getTime()));
        date1 = new Date();
        final List list = ztsjMapper.getAll();
        //pipeline
        redisUtil.pipelineSet(ztsjKey,list);

        LOGGER.info("保存redis涨停数据耗时={}毫秒",(new Date().getTime()-date1.getTime()));
//        if (list != null && list.size() > 0) {
//            double d = 0;
//            for (ZtsjBean bean : list) {
//                JSONObject jsonObject = beanToJSON(bean);
//                redisUtil.add(ztsjKey, jsonObject.toJSONString(), d);
//                d++;
//            }
//            LOGGER.info("保存redis涨停数据耗时={}毫秒",(new Date().getTime()-date1.getTime()));
//        }
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
        LOGGER.info("删除涨停数据耗时={}毫秒",(new Date().getTime()-date1.getTime()));
        date1 = new Date();
        setAll();
        LOGGER.info("重置涨停数据耗时={}毫秒",(new Date().getTime()-date1.getTime()));
    }
    public String getZtsjFx() {
        Date date1 = new Date();
        Set<String> set = redisUtil.range(ztsjKey);
        LOGGER.info("获取redis涨停数据耗时={}毫秒",(new Date().getTime()-date1.getTime()));
        ZtsjBean bean = ztsjMapper.getZtsjfx();
        LOGGER.info("获取mysql涨停统计数据耗时={}毫秒",(new Date().getTime()-date1.getTime()));

        JSONObject beanJson = MyStringUtils.beanToJSON(bean);
        Set<String> set1 = new LinkedHashSet<String>();
        set1.add(beanJson.toJSONString());
        set1.addAll(set);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Rows", set1);
        jsonObject.put("Total", set.size());
        return jsonObject.toJSONString();
    }
}
