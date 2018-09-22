package com.pikaqiu.rabbitmq.api.limit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public class Consumer {

    /**
     * 限流
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


        String exchangeName = "test_qos_exchange";
        String queueName = "test_qos_queue";
        String routingKey = "qos.#";

        channel.exchangeDeclare(exchangeName, "topic", true, false, null);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        //1 限流方式  第一件事就是 autoAck设置为 false

        //一定数量的消息未被确认前 不会有新的消息推送过来

        //1大小限制多少m 0 不限制 2最多处理多少消息 一般设置 1
        //3是否全局 true channel级别 false consumer级别
        //自动应答下 1 3 不生效
        channel.basicQos(0, 2, false);

        channel.basicConsume(queueName, false, new MyConsumer(channel));
    }
}
