package com.cyj.mystock.page.bean;

import lombok.Data;

@Data
public class FollowStockBean {

    private Long id;
    private String stockcode;
    private String stockname;
    private String followPrice;
    private String followDate;
    private String nowPrice;
    private String dateDiff;
    private String ramarks;
}
