package com.cyj.mystock.info.bean;

import lombok.Data;

import java.util.Date;

@Data
public class ZdtBean {
    private Date tradeDate;
    private Integer ztzs;
    private Integer dtzs;
    private Integer z8to10;
    private Integer z6to8;
    private Integer z4to6;
    private Integer z2to4;
    private Integer z0to2;
    private Integer d0to2;
    private Integer d2to4;
    private Integer d4to6;
    private Integer d6to8;
    private Integer d8to10;
    private Integer znum;
    private Integer dnum;
}
