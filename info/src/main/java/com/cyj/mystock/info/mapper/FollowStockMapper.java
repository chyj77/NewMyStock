package com.cyj.mystock.info.mapper;

import com.cyj.mystock.info.bean.FollowStockBean;
import com.cyj.mystock.info.bean.SpmmBean;

import java.util.List;
import java.util.Map;

public interface FollowStockMapper {

    List<FollowStockBean> getAll();

    FollowStockBean getByStockCode(String stockcode);

    void deleteByPrimaryKey(Long recId);

    void insert(FollowStockBean bean);

    void updateByPrimaryKeySelective(FollowStockBean bean);


}
