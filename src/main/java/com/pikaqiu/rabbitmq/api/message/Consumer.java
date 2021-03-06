package com.pikaqiu.rabbitmq.api.message;

import java.util.Map;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public class Consumer {

    public static void main(String[] args) throws Exception {

        //1 创建一个ConnectionFactory, 并进行配置
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("134.175.5.236");
        connectionFactory.setPort(5672);
        //类似redis的第几个数据库
        // 同一个virtualHost不能有相同名称主机
        connectionFactory.setVirtualHost("/");

        //2 通过连接工厂创建连接
        Connection connection = connectionFactory.newConnection();

        //3 通过connection创建一个Channel
        Channel channel = connection.createChannel();

        //4 声明（创建）一个队列
        String queueName = "test001";


//		Map<String, Object>  argss = new HashMap<String, Object>();
//		argss.put("vhost", "/");
//		argss.put("username","root");
//		argss.put("password", "root");
//		argss.put("x-message-ttl",6000); //消息队列内的消息都采用的过期时间

        channel.queueDeclare(queueName, true, false, false, null);//argss

        //5 创建消费者
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);

        //6 设置Channel
        channel.basicConsume(queueName, true, queueingConsumer);

        while (true) {
            //7 获取消息
            Delivery delivery = queueingConsumer.nextDelivery();
            //获取消息体
            String msg = new String(delivery.getBody());
            System.err.println("消费端: " + msg);

            //获取headers 类型就是 Map<String, Object>
            Map<String, Object> headers = delivery.getProperties().getHeaders();
            System.err.println("headers get my1 value: " + headers.get("my1"));

            //Envelope envelope = delivery.getEnvelope();
        }

    }
}
