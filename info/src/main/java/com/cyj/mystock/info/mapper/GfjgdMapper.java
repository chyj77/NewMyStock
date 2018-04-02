package com.cyj.mystock.info.mapper;

import com.cyj.mystock.info.bean.FollowStockBean;
import com.cyj.mystock.info.bean.GfjgdBean;

import java.util.List;
import java.util.Map;

public interface GfjgdMapper {

    List<GfjgdBean> getAll();

    List<Map> queryGfStock();

}
