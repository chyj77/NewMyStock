package com.cyj.mystock.page.queue;

import com.cyj.mystock.page.websocket.MyWebSocket;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component

@Log4j2
public class QueueDxjlReceiver {

//    @Autowired
//    MyWebSocket myWebSocket;

    @RabbitHandler
    @RabbitListener(queues = "dxjl")
    public void process(String context) {
//        LOGGER.info("Receiver  : {}" , context);
        try {
            MyWebSocket.sendInfo(context);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("QueueDxjlReceiver  : {}" , e);
        }
    }
}
