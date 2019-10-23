package com.pikaqiu.rabbitmq.api.ack;

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
		
		
		String exchangeName = "test_ack_exchange";
		String queueName = "test_ack_queue";
		String routingKey = "ack.#";
		//定义一个 交换机      交换机名称   交换机类型     是否持久化  是否自动删除   参数
		channel.exchangeDeclare(exchangeName, "topic", true, false, null);
		//定义一个queue 		queue名称			持久化		专有的		自动删除    参数
		channel.queueDeclare(queueName, true, false, false, null);
		//绑定交换机 队列 和 routingKey的关系
		channel.queueBind(queueName, exchangeName, routingKey);
		
		// 手工签收 必须要关闭 autoAck = false
		channel.basicConsume(queueName, false, new MyConsumer(channel));
		
		
	}
}
