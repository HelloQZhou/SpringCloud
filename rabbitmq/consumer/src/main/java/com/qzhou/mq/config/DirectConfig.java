package com.qzhou.mq.config;


import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DirectConfig {
    @RabbitListener(bindings = @QueueBinding(
          value = @Queue("direct_queue1"),
          exchange = @Exchange(name = "direct_exchange",type = ExchangeTypes.DIRECT),
          key = {"red","blue"}
    )
    )
    public void listenMessage1(String msg) throws InterruptedException {
        System.out.println("listenMessage1接受消息:"+msg);

    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("direct_queue2"),
            exchange = @Exchange(name = "direct_exchange",type = ExchangeTypes.DIRECT),
            key = {"red","yellow"}
    )
    )
    public void listenMessage2(String msg) throws InterruptedException {
        System.err.println("listenMessage2接受消息:"+msg);

    }
}
