package com.qzhou.mq.config;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicConfig {
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("topic_queue1"),
            exchange = @Exchange(name = "topic_exchange",type = ExchangeTypes.TOPIC),
            key = "china.#"
    )
    )
    public void listenMessage1(String msg) throws InterruptedException {
        System.out.println("topic 1接受消息:"+msg);

    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("topic_queue2"),
            exchange = @Exchange(name = "topic_exchange",type = ExchangeTypes.TOPIC),
            key = "#.news"
    )
    )
    public void listenMessage2(String msg) throws InterruptedException {
        System.err.println("topic 2接受消息:"+msg);

    }
}
