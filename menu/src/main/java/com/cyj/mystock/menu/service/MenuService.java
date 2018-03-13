package com.cyj.mystock.menu.service;

import com.cyj.mystock.menu.bean.Menu;
import com.cyj.mystock.menu.mapper.MenuMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuService.class);

    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RedisUtil redisUtil;

    final String key = "menu";

    public void getAll() {
        List<Menu> list = menuMapper.getAll();

        if (redisUtil.exists(key)) {
            redisUtil.remove(key);
        }
        if (list != null && list.size() > 0) {
            JSONArray rootArray = new JSONArray();
            int nodeIndex = -1;
            int leafIndex = -1;
            for (Menu menu : list) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("menuId", menu.getMenuId());
                jsonObject.put("menuCode", menu.getMenuCode());
                jsonObject.put("menuName", menu.getMenuName());
                jsonObject.put("menuUrl", menu.getMenuUrl());
                jsonObject.put("pMenuId", menu.getPMenuId());
                jsonObject.put("menuType", menu.getMenuType());
                jsonObject.put("menuLevel", menu.getMenuLevel());
                jsonObject.put("isexpand", menu.getIsexpand()==null?false:Boolean.parseBoolean(menu.getIsexpand()));
                if (menu.getMenuType().equals("root")) {
                    rootArray.add(jsonObject);
                    nodeIndex++;
                    leafIndex = -1;
                } else if (menu.getMenuType().equals("node")) {
                    if (rootArray.size() > 0) {
                        JSONObject rootObject = (JSONObject) rootArray.get(nodeIndex);
                        JSONArray nodeArray = new JSONArray();
                        if (rootObject.get("node") != null) {
                            nodeArray = (JSONArray) rootObject.get("node");
                        }
                        nodeArray.add(jsonObject);
                        rootObject.put("node", nodeArray);
                        leafIndex++;
                    }
                } else {
                    if (rootArray.size() > 0) {
                        JSONObject rootObject = (JSONObject) rootArray.get(nodeIndex);
                        JSONArray nodeArray = (JSONArray) rootObject.get("node");
                        if(nodeArray!=null && nodeArray.size()>0) {
                            JSONObject nodeObject = (JSONObject) nodeArray.get(leafIndex);
                            JSONArray childArray = new JSONArray();
                            if (nodeObject.get("children") != null) {
                                childArray = (JSONArray) nodeObject.get("children");
                            }
                            childArray.add(jsonObject);
                            nodeObject.put("children", childArray);
                        }else{
                            JSONArray childArray = new JSONArray();
                            if (rootObject.get("children") != null) {
                                childArray = (JSONArray) rootObject.get("children");
                            }
                            childArray.add(jsonObject);
                            rootObject.put("children", childArray);
                        }
                    }
                }

            }
            LOGGER.info(rootArray.toString());
            redisUtil.set(key, rootArray.toString());
        }
    }

    public String getMenu() {
        return redisUtil.get(key);
    }
}
