package com.cyj.mystock.info;


import com.cyj.mystock.info.service.FollowStockService;
import com.cyj.mystock.info.service.SpmmInfoService;
import com.cyj.mystock.info.service.WebsocketService;
import com.cyj.mystock.info.service.ZtsjInfoService;
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
public class InfoApplicationRunner implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(InfoApplicationRunner.class);

    @Autowired
    private ZtsjInfoService ztsjInfoService;
    @Autowired
    private SpmmInfoService spmmInfoService;
    @Autowired
    private FollowStockService followStockService;
    @Autowired
    private WebsocketService websocketService;

    @Override
    public void run(ApplicationArguments var1) throws Exception{
        LOGGER.info("MyApplicationRunner2!");
        websocketService.initGetStock();
        ztsjInfoService.setZtgn();
        ztsjInfoService.setAll();
        spmmInfoService.setLuoji();
        spmmInfoService.setStock();
        followStockService.setRedisData();
    }
}
