package com.cyj.mystock.info;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.cyj.mystock.info.mapper")
@EnableScheduling
@EnableCaching// 开启缓存，需要显示的指定
public class InfoApplication {

	public static void main(String[] args) {
		SpringApplication.run(InfoApplication.class, args);
	}

	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
