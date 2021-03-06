package com.pikaqiu.rabbitmq.api.dlx;

import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public class Consumer {


    public static void main(String[] args) throws Exception {


        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("134.175.5.236");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        // 这就是一个普通的交换机 和 队列 以及路由
        String exchangeName = "test_dlx_exchange";
        String routingKey = "dlx.#";
        String queueName = "test_dlx_queue";
        //普通队列申明
        channel.exchangeDeclare(exchangeName, "topic", true, false, null);

        Map<String, Object> agruments = new HashMap<String, Object>();
        //设置死信队列的名称  一般用于消息补偿
        agruments.put("x-dead-letter-exchange", "dlx.exchange");
        //设置整个对上消息的过期时间
        agruments.put("x-message-ttl", 6000);

		//这个agruments属性，要设置到声明队列上
		channel.queueDeclare(queueName, true, false, false, agruments);
		channel.queueBind(queueName, exchangeName, routingKey);
		
		//要进行死信队列的声明:  队列死信后会转发到这里
        //1 没人消费 2消息超时          就会成为死信
		channel.exchangeDeclare("dlx.exchange", "topic", true, false, null);
		channel.queueDeclare("dlx.queue", true, false, false, null);
		channel.queueBind("dlx.queue", "dlx.exchange", "#");

        //死信后的队列
        channel.basicConsume("dlx.queue", true, new MyConsumer(channel));
        //正常队列
        channel.basicConsume(queueName, true, new MyConsumer(channel));

    }
}
