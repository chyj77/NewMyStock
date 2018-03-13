package com.cyj.mystock.menu.bean;

import lombok.Data;

@Data
public class Menu {
    private Long menuId;
    private String menuCode;
    private String menuName;
    private String menuUrl;
    private Long pMenuId;
    private String menuType;
    private String menuLevel;
    private String isexpand;

}
