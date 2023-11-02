package com.qzhou.order.client;

import com.qzhou.order.feignLogger.DefaultFeignConfiguration;
import com.qzhou.order.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


/*
- 服务名称：userservice
- 请求方式：GET
- 请求路径：/user/{id}
- 请求参数：Long id
- 返回值类型：User

加上 configuration = DefaultFeignConfiguration.class 表示日志级别配置文件只是局部生效
 */

@FeignClient(value = "userservice",configuration = DefaultFeignConfiguration.class) //指定服务名称
public interface UserClient {
    @GetMapping("/user/{id}")
    User findById(@PathVariable("id") Long id);
}