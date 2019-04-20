package com.pikaqiu.rabbitmq.api.ack;

import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {


    public static void main(String[] args) throws Exception {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("134.175.5.236");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        String exchange = "test_ack_exchange";
        String routingKey = "ack.save";


		//循环发送五条消息
		for(int i =0; i<5; i ++){

			Map<String, Object> headers = new HashMap<String, Object>();
			headers.put("num", i);
			//创建基础属性
			AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
					.deliveryMode(2)
					.contentEncoding("UTF-8")
					.headers(headers)
					.build();
			String msg = "Hello RabbitMQ ACK Message " + i;
			//当mandatory标志位设置为true时，如果exchange根据自身类型和消息routingKey无法找到一个合适的queue存储消息，
			// 那么broker会调用basic.return方法将消息返还给生产者;当mandatory设置为false时，出现上述情况broker会直接将消息丢弃;通俗的讲，
			// mandatory标志告诉broker代理服务器至少将消息route到一个队列中，否则就将消息return给发送者;
			//想channl 推送消息 交换机名称				mandatory	参数		消息内容字节
			channel.basicPublish(exchange, routingKey, true, properties, msg.getBytes());
		}
		
	}
}
