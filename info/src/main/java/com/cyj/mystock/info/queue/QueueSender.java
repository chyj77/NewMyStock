package com.cyj.mystock.info.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QueueSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueueSender.class);

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send(String context) {
        LOGGER.info("Sender : {}" , context);
        this.rabbitTemplate.convertAndSend("stock", context);
    }
}
