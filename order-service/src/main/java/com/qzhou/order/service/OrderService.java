package com.qzhou.order.service;

import com.qzhou.order.client.UserClient;
import com.qzhou.order.mapper.OrderMapper;
import com.qzhou.order.pojo.Order;
import com.qzhou.order.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserClient userClient;

    public Order queryOrderById(Long orderId) {
        // 1.查询订单
        Order order = orderMapper.findById(orderId);
        //2.向user-service服务发送http请求 使用eureka 服务注册和服务发现（远程调用）
//        String url="http://userservice/user/"+order.getUserId();
//        User user = restTemplate.getForObject(url, User.class);
        //2.用feign注入
        User user = userClient.findById(order.getUserId());
        //3.封装user
        order.setUser(user);
        // 4.返回
        return order;
    }
}
