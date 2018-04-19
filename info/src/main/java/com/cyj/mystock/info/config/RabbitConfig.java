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
    @Bean
    public Queue Zdt() {
        return new Queue("zdt");
    }
    @Bean
    public Queue Zfb() {
        return new Queue("zfb");
    }
    @Bean
    public Queue Dfb() {
        return new Queue("dfb");
    }
}
