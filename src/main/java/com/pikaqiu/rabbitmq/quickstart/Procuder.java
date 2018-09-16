package com.pikaqiu.rabbitmq.quickstart;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 功能描述
 *
 * @author xiaoye
 * @date 2018/9/16
 */
public class Procuder {

    public static void main(String[] args) throws Exception {

        //1 创建一个ConnectionFactory 连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost("134.175.5.236");

        connectionFactory.setPort(5672);

        connectionFactory.setVirtualHost("/");

        //2 通过连接工厂创建连接
        Connection connection = connectionFactory.newConnection();

        //3 创建通道
        Channel channel = connection.createChannel();

        String queueName = "pikaqiu";

        //发送队列
        for (int i = 0; i < 5; i++) {

            String hello_小_张_情 = new String("hello 小 张 情---");

            //exchange routingKey 基本属性 发送字节
            //不指定exchange 就是AMQP default (可以绑定任意队列)会按照 routingKey 去匹配  匹配不上 会删除消息
            channel.basicPublish("", queueName, null, (hello_小_张_情 + i).getBytes());
        }
        channel.close();

        connection.close();

    }
}
