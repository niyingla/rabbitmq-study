package com.pikaqiu.rabbitmq.api.exchange.direct;

import com.rabbitmq.client.*;
import com.rabbitmq.client.QueueingConsumer.Delivery;

import java.io.IOException;
import java.util.Map;

public class Consumer4DirectExchange {

    /**
     * 先有消费者 再有生产者
     * 不同的consumer注册到broker上
     * 同样的生产者生产消息
     * 消息将路由到符合规则的consumer上
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {


        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost("134.175.5.236");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        //自动重连
        connectionFactory.setAutomaticRecoveryEnabled(true);
        //重试时间
        connectionFactory.setNetworkRecoveryInterval(3000);
        Connection connection = connectionFactory.newConnection();

        Channel channel = connection.createChannel();
        //4 声明
        String exchangeName = "test_direct_exchange";
        String exchangeType = "direct";
        String queueName = "test_direct_queue";
        String routingKey = "test.direct";

        //表示声明了一个交换机 exchange和queue一对多
        //(String exchange, String type, boolean durable(持久化 重启不消失), boolean autoDelete（没有exchange自动删除）, boolean internal(内部使用), Map<String, Object> arguments) throws IOException {

        channel.exchangeDeclare(exchangeName, exchangeType, true, false, false, null);
        //表示声明了一个队列
        channel.queueDeclare(queueName, false, false, false, null);
        //建立一个绑定关系:
        channel.queueBind(queueName, exchangeName, routingKey);

        //durable 是否持久化消息
        QueueingConsumer consumer = new QueueingConsumer(channel);
        //参数：队列名称、是否自动ACK、Consumer
        channel.basicConsume(queueName, true, consumer);
        //循环获取消息  
        while (true) {
            //获取消息，如果没有消息，这一步将会一直阻塞  
            Delivery delivery = consumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println("收到消息：" + msg);
        }
    }
}
