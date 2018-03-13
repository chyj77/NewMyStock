package com.cyj.mystock.menu;


import com.cyj.mystock.menu.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 *
 * 这里通过设定value的值来指定执行顺序
 */
@Component
@Order(value = 1)
public class MenuApplicationRunner implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuApplicationRunner.class);

    @Autowired
    private MenuService menuService;

    @Override
    public void run(ApplicationArguments var1) throws Exception{
        LOGGER.info("MyApplicationRunner1!");
        menuService.getAll();
    }
}
