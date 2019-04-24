package com.pikaqiu.rabbitmq.api.returnlistener;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ReturnListener;

import java.io.IOException;

public class Producer {
    /**
     * ReturnListener用来不可达消息的跟踪 （比如 没有exchange/routingKey无效）
     * <p>
     * <p>
     * 不可达消息队列 mandatory 要设置为true
     *
     * @param args
     * @throws Exception
     */

    public static void main(String[] args) throws Exception {


        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("134.175.5.236");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        String exchange = "test_return_exchange";
        String routingKey = "return.save";
        String routingKeyError = "abc.save";

        String msg = "Hello RabbitMQ Return Message";


        channel.addReturnListener((replyCode, replyText, exchange1, routingKey1, properties, body) -> {

            System.err.println("---------handle  return----------");
            //响应码
            System.err.println("replyCode: " + replyCode);
            //响应文本
            System.err.println("replyText: " + replyText);
            System.err.println("exchange: " + exchange1);
            System.err.println("routingKey: " + routingKey1);
            System.err.println("properties: " + properties);
            System.err.println("body: " + new String(body));
        });

        //默认false				当消息不可达				mandatory 为true ReturnListener会自动监听不可达消息
        //												为false broke会自动删除该消息 ReturnListener不会监听
        channel.basicPublish(exchange, routingKeyError, true, null, msg.getBytes());

        //channel.basicPublish(exchange, routingKeyError, true, null, msg.getBytes());


    }
}
