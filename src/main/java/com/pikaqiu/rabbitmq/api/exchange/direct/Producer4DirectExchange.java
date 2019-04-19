package com.pikaqiu.rabbitmq.api.exchange.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer4DirectExchange {

    /**
     * Direct Exchange 处理路由键。需要将一个队列绑定到交换机上，
     * 要求该消息与一个特定的路由键完全匹配。这是一个完整的匹配。
     * 如果一个队列绑定到该交换机上要求路由键 “test”, 则只有被标记为“test”
     * 的消息才被转发 ，不会转发test.aaa，也不会转发dog.123，只会转发test。
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        //1 创建ConnectionFactory
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("134.175.5.236");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        //2 创建Connection
        Connection connection = connectionFactory.newConnection();
        //3 创建Channel
        Channel channel = connection.createChannel();
        //4 声明
        String exchangeName = "test_direct_exchange";
        String routingKey = "test.direct";
        //5 发送

        String msg = "Hello World RabbitMQ 4  Direct Exchange Message 111 ... ";
        channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());

        channel.close();

        connection.close();
    }

}
