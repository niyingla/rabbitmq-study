package com.pikaqiu.rabbitmq.quickstart;

import com.rabbitmq.client.*;

/**
 * 功能描述
 *
 * @author xiaoye
 * @date 2018/9/16
 */
public class Consumer {

    public static void main(String[] args) throws Exception {

        //1 创建一个ConnectionFactory 连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost("134.175.5.236");

        connectionFactory.setPort(5672);

        connectionFactory.setVirtualHost("/");

        //2 通过连接工厂创建连接
        Connection connection = connectionFactory.newConnection();

        //3 通过connection 创建channel
        Channel channel = connection.createChannel();

        //4 申明队列 (String queue 名字, boolean durable 是否持久化 （重启不消失）,
        // boolean exclusive 是否独占（队列 与 channel 一对一）集群消费时无法保证消费顺序可以设置为true 保证消费循序,
        // boolean autoDelete 没有consumer关系时自动删除, Map<String, Object> arguments 参数)
        String queueName = "pikaqiu";

        channel.queueDeclare(queueName, true, false, false, null);

        //5创建消费者
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);

        //6 设置channel     队列名称  是否自动签收
        channel.basicConsume(queueName, true, queueingConsumer);

        //7 获取消息
        while (true) {
           QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();

            String message = new String(delivery.getBody());

            System.out.println(message);

            /*Envelope envelope = delivery.getEnvelope();

            long deliveryTag = envelope.getDeliveryTag();*/

        }

    }
}
