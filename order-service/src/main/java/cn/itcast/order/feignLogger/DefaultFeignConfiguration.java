package cn.itcast.order.feignLogger;


import feign.Logger;
import org.springframework.context.annotation.Bean;


public class DefaultFeignConfiguration {
    @Bean
    public Logger.Level feignLoggerLevel(){
        return Logger.Level.BASIC;
    }
}
