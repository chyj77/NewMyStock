package com.cyj.mystock.info.service;

import com.cyj.mystock.info.bean.FollowStockBean;
import com.cyj.mystock.info.bean.GfjgdBean;
import com.cyj.mystock.info.mapper.GfjgdMapper;
import com.cyj.mystock.info.utils.MyStringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class GfjgdService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GfjgdService.class);

    @Autowired
    private GfjgdMapper mapper;


    public String getAll() {
        Date date1 = new Date();
        List<GfjgdBean> list = mapper.getAll();
        LOGGER.info("查找股票交割单耗时={}毫秒",(new Date().getTime()-date1.getTime()));
        JSONArray jsonArray = new JSONArray();
        if(list!=null && list.size()>0){
            for(GfjgdBean bean:list){
                JSONObject jsonObject = MyStringUtils.beanToJSON(bean);
                jsonArray.add(jsonObject);
            }
        }
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("Rows", jsonArray);
        jsonObject1.put("Total",list.size());
        LOGGER.info("查找股票交割单转换成json耗时={}毫秒",(new Date().getTime()-date1.getTime()));
        return jsonObject1.toJSONString();
    }

    public String queryGfStock() throws Exception {
        Date date1 = new Date();
        List<Map> list = mapper.queryGfStock();
        LOGGER.info("查找股票交割单分析数据耗时={}毫秒",(new Date().getTime()-date1.getTime()));
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
        LOGGER.info("转换股票交割单分析数据耗时={}毫秒",(new Date().getTime()-date1.getTime()));
        return jsonObject.toJSONString();
    }

}
