package com.pikaqiu.rabbitmq.api.exchange.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public class Consumer4TopicExchange {

	public static void main(String[] args) throws Exception {
		
		
        ConnectionFactory connectionFactory = new ConnectionFactory() ;  
        
        connectionFactory.setHost("134.175.5.236");
        connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/");
        //设置网络异常重连
        connectionFactory.setAutomaticRecoveryEnabled(true);
        //设置 每10s重试一次
        connectionFactory.setNetworkRecoveryInterval(3000);
        //设置重新声明交换器，队列等信息。
        //connectionFactory.setTopologyRecoveryEnabled(true);
        Connection connection = connectionFactory.newConnection();
        
        Channel channel = connection.createChannel();  
		//4 声明
		String exchangeName = "test_topic_exchange";
		String exchangeType = "topic";
		String queueName = "test_topic_queue";
		//String routingKey = "user.*";
		String routingKey = "user.*";
        // 1 声明交换机  String exchange, String type, boolean durable 是否持久化, boolean autoDelete, boolean internal, Map<String, Object> arguments
		channel.exchangeDeclare(exchangeName, exchangeType, true, false, false, null);
		// 2 声明队列 String queue, boolean durable 是否持久化, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
		channel.queueDeclare(queueName, false, false, false, null);
		// 3 建立交换机和队列的绑定关系: * 只匹配一层  #匹配两层
		channel.queueBind(queueName, exchangeName, routingKey);
		
        //durable 是否持久化消息
        QueueingConsumer consumer = new QueueingConsumer(channel);
        //参数：队列名称、是否自动ACK（自动签收）、Consumer
        channel.basicConsume(queueName, true, consumer);  
        //循环获取消息  
        while(true){  
            //获取消息，如果没有消息，这一步将会一直阻塞  
            Delivery delivery = consumer.nextDelivery();  
            String msg = new String(delivery.getBody());    
            System.out.println("收到消息：" + msg);  
        } 
	}
}
