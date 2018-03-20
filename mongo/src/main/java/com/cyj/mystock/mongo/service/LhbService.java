package com.cyj.mystock.mongo.service;

import com.cyj.mystock.mongo.bean.ThsLhb;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.BsonDocument;
import org.bson.BsonType;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class LhbService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LhbService.class);
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 查询所有的Student信息
     * @return
     */
    public JSONArray findAll(String day){
        Date date1 = new Date();
//        String jsonSql="{distinct:'thslhb',key:'rq'}";
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DAY_OF_MONTH, -1);
//        String day = pasrserTime(cal.getTime());
        MongoCollection<Document> collection = mongoTemplate.getCollection("thslhb");
        FindIterable<Document> findIterable = collection.find(new Bson() {
            @Override
            public <TDocument> BsonDocument toBsonDocument(Class<TDocument> aClass, CodecRegistry codecRegistry) {

                return BsonDocument.parse("{rq:'"+day+"'}");
            }
        });
        MongoCursor cursor =  findIterable.iterator();
        JSONArray jsonArray = new JSONArray();
        while (cursor.hasNext()){
            Document document = (Document)cursor.next();
            document.remove("_id");
//            String json = document.toJson();
            jsonArray.add(document);
        }
        LOGGER.info("获取MONGODB数据耗时={}毫秒",(new Date().getTime()-date1.getTime()));
//        List<JSONArray> list =  mongoTemplate.findAll(JSONArray.class);
        return jsonArray;
    }

    private String pasrserTime(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        return sdf.format(date);
    }
}
