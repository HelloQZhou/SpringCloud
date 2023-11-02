package com.qzhou.order.feignLogger;


import feign.Logger;
import org.springframework.context.annotation.Bean;

/*
配置类修改feign日志级别
 */
public class DefaultFeignConfiguration {
    @Bean
    public Logger.Level feignLoggerLevel(){
        return Logger.Level.BASIC;
    }
}
