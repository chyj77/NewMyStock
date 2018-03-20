package com.cyj.mystock.info.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class SpmmBean implements Serializable {

    private Long recid;
    private String rq;
    private String code;
    private String name;
    private String caozuo;
    private String jiage;
    private String sl;
    private String luoji;

}
