package com.qzhou.mq.listen;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SpringRabbitMQListen {

    @RabbitListener(queues = "simple.queue")
    public void listenMessage1(String msg) throws InterruptedException {
        System.out.println("listenMessage1接受消息:"+msg+ LocalDateTime.now());
        Thread.sleep(20);
    }

    @RabbitListener(queues = "simple.queue")
    public void listenMessage2(String msg) throws InterruptedException {
        System.err.println("listenMessage2接受消息:"+msg+ LocalDateTime.now());
        Thread.sleep(100);
    }
}
