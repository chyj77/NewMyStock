package com.cyj.mystock.page.queue;

import com.cyj.mystock.page.websocket.MyWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RabbitListener(queues = "stock")
public class QueueReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueueReceiver.class);

//    @Autowired
//    MyWebSocket myWebSocket;

    @RabbitHandler
    public void process(String context) {
//        LOGGER.info("Receiver  : {}" , context);
        try {
            MyWebSocket.sendInfo(context);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("QueueReceiver  : {}" , e);
        }
    }
}
