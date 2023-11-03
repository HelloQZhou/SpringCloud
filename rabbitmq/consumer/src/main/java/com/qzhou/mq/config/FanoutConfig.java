package com.qzhou.mq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FanoutConfig {
    //声明交换机 exchange
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange("fanout_exchange");
    }

    //声明队列1
    @Bean
    public Queue queue1(){
        return new Queue("queue1");
    }
    //声明队列2
    @Bean
    public Queue queue2(){
        return new Queue("queue2");
    }

    //把队列1 绑定到交换机
    @Bean
    public Binding bindingQueue1ToExchange(Queue queue1,FanoutExchange fanoutExchange){
        return BindingBuilder.bind(queue1()).to(fanoutExchange());
    }

    //声明队列2 绑定到交换机
    @Bean
    public Binding bindingQueue2ToExchange(Queue queue2,FanoutExchange fanoutExchange){
        return BindingBuilder.bind(queue2()).to(fanoutExchange());
    }


}
