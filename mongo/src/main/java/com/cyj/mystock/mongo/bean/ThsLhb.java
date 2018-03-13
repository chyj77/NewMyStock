package com.cyj.mystock.mongo.bean;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class ThsLhb {
    private String _id;
    private String rq;
    private Object data;
}
