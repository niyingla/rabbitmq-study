package com.pikaqiu.rabbitmq.api.message;

import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Procuder {

	
	public static void main(String[] args) throws Exception {
		//1 创建一个ConnectionFactory, 并进行配置
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("134.175.5.236");
		connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/");
		
		//2 通过连接工厂创建连接
		Connection connection = connectionFactory.newConnection();
		
		//3 通过connection创建一个Channel
		Channel channel = connection.createChannel();

		//创建headers map 里面可以携带一些消息参数
		Map<String, Object> headers = new HashMap<>();
		headers.put("my1", "111");
		headers.put("my2", "222");
		
		
		AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
				//投递模式  1非持久化投递 （重启就没了） 2持久化投递 （重启还有）
				.deliveryMode(2)
				//设置编码格式
				.contentEncoding("UTF-8")
				//设置过期毫秒
                //可以用来做订单有效时间
                //当创建未支付订单时创建消息队列设置过期时间为30分钟
                //支付后来读取本队列 过期了就告诉订单已取消 未过期就同意支付
				.expiration("10000")
				//设置到消息属性
				.headers(headers)
				.build();
		
		//4 通过Channel发送数据
		for(int i=0; i < 5; i++){
			String msg = "Hello RabbitMQ!";
			//1 exchange   2 routingKey
			channel.basicPublish("", "test001", properties, msg.getBytes());
		}

		//5 记得要关闭相关的连接
		channel.close();
		connection.close();
	}
}
