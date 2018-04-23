package com.cyj.mystock.menu.mapper;

import com.cyj.mystock.menu.bean.Menu;

import java.util.List;
import java.util.Map;

public interface  MenuMapper {

    List<Menu> getAll(Map map);
}
