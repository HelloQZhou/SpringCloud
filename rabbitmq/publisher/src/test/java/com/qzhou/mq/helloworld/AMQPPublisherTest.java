package com.qzhou.mq.helloworld;



import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AMQPPublisherTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSimpleQueue() throws InterruptedException {
        // 队列名称
        String queueName = "simple.queue";
        // 消息
        String message = "hello, spring amqp !?!";
        // 发送消息
        rabbitTemplate.convertAndSend(queueName, message);

    }

    @Test
    public void testWorkQueue() throws InterruptedException {
        // 队列名称
        String queueName = "simple.queue";
        // 消息
        String message = "hello, spring amqp _";
        // 发送消息
        for(int i=1;i<=50;i++){
            rabbitTemplate.convertAndSend(queueName, message);
            Thread.sleep(20);
        }
    }

    @Test
    public void testFanoutExchange(){
        //交换机名称
        String exchange="fanout_exchange";
        //消息
        String message="hello this is test fanout queue";

        rabbitTemplate.convertAndSend(exchange,"",message);
    }

    @Test
    public void testDirectExchange(){
        //交换机名称
        String exchange="direct_exchange";
        //消息
        String message="hello this is test direct queue";

        rabbitTemplate.convertAndSend(exchange,"blue",message);
    }

    @Test
    public void testTopicExchange(){
        //交换机名称
        String exchange="topic_exchange";
        //消息
        String message="hello this is test topic queue";

        rabbitTemplate.convertAndSend(exchange,"china.11.news",message);
    }

    @Test
    public void testSendMap() throws InterruptedException {
        // 准备消息
        Map<String,Object> msg = new HashMap<>();
        msg.put("name", "Jack");
        msg.put("age", 21);
        // 发送消息
        rabbitTemplate.convertAndSend("Object.queue", msg);
    }

}
