package com.cyj.mystock.info.mapper;

import com.cyj.mystock.info.bean.ZtsjBean;

import java.util.List;
import java.util.Map;

public interface ZtsjMapper {

    List<ZtsjBean> getAll();

    List<Map> getZtgn();

    void deleteByPrimaryKey(Long recId);

    void insert(ZtsjBean bean);

    void updateByPrimaryKeySelective(ZtsjBean bean);
}
