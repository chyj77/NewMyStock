package com.cyj.mystock.info.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean
    public Queue Stock() {
        return new Queue("stock");
    }
    @Bean
    public Queue Dxjl() {
        return new Queue("dxjl");
    }
}
