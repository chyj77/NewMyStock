package com.cyj.mystock.page.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
//@EnableWebSocketMessageBroker //通过@EnableWebSocketMessageBroker 注解凯旗使用STOMP协议来传输基于代理（message broker）的消息
//这时控制器支持使用@MessageMapping，就像使用@RequestMapping一样
public class WebSocketConfig {//extends DelegatingWebSocketMessageBrokerConfiguration {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
/*
    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        stompEndpointRegistry.addEndpoint("/endpoint").withSockJS();//注册STOMP协议的节点，映射指定的URL，并指定使用SockJS协议
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {//配置消息代码（Message Broker）
        registry.enableSimpleBroker("/topic");//广播式应配置一个/topic消息代理
    }
    */
}
