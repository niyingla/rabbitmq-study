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
	 *
	 *
	 * 不可达消息队列 mandatory 要设置为true
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
		
		
		channel.addReturnListener(new ReturnListener() {
			@Override
			public void handleReturn(int replyCode, String replyText, String exchange,
					String routingKey, BasicProperties properties, byte[] body) throws IOException {

				System.err.println("---------handle  return----------");
				//相应码
				System.err.println("replyCode: " + replyCode);
				//相应文本
				System.err.println("replyText: " + replyText);
				System.err.println("exchange: " + exchange);
				System.err.println("routingKey: " + routingKey);
				System.err.println("properties: " + properties);
				System.err.println("body: " + new String(body));
			}
		});

		//默认false										mandatory 为true会自动监听不可达消息
		//												为false broke会自动删除该消息
		channel.basicPublish(exchange, routingKeyError, true, null, msg.getBytes());
		
		//channel.basicPublish(exchange, routingKeyError, true, null, msg.getBytes());
		
		
		
		
	}
}
