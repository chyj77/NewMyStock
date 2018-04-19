package com.cyj.mystock.info.mapper;

import com.cyj.mystock.info.bean.MaretStockBean;
import com.cyj.mystock.info.bean.SpmmBean;
import com.cyj.mystock.info.bean.ZdtBean;
import com.cyj.mystock.info.bean.ZtsjBean;

import java.util.List;
import java.util.Map;

public interface SpmmMapper {

    List<SpmmBean> getAll();

    List<Map> getLuoji();

    List<MaretStockBean> getStock();

    void deleteByPrimaryKey(Long recId);

    void insert(SpmmBean bean);

    void updateByPrimaryKeySelective(SpmmBean bean);

    List<Map> querySpmmFx();

    void insertZdtBean(ZdtBean bean);
}
