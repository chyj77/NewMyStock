package com.cyj.mystock.websocket;


import com.cyj.mystock.websocket.service.WebsocketService;
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
public class WebsocketApplicationRunner implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketApplicationRunner.class);

    @Autowired
    private WebsocketService websocketService;
    @Override
    public void run(ApplicationArguments var1) throws Exception{
        LOGGER.info("WebsocketApplicationRunner!");
        websocketService.initGetStock();
    }
}
