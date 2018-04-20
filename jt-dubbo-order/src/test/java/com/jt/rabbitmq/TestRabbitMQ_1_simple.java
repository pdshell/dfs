package com.jt.rabbitmq;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

//测试rabbitMQ中的的简单模式
public class TestRabbitMQ_1_simple {
	
	//定义链接
	private Connection connection = null;
	
	/**
	 * rabbitMQ的连接步骤
	 * 1.通过用户jtadmin连接rabbitmq:ip:端口/用户名/密码/虚拟主机
	 * 2.定义消息的提供者provider
	 * 		2.1创建channel对象(控制队列/路由/交换机等)
	 * 		2.2定义消息队列
	 *      2.3发送消息到队列中
	 * @throws IOException 
	 */
	
	//表示从rabbitMq工厂中获取链接
	@Before
	public void initConnection() throws IOException{
		//创建工厂对象
		ConnectionFactory factory = new ConnectionFactory();
		
		//设定连接主机ip
		factory.setHost("192.168.146.135");
		//设定链接端口
		factory.setPort(5672);
		//设定用户名
		factory.setUsername("dhadmin");
		factory.setPassword("dhadmin");
		//设定虚拟主机
		factory.setVirtualHost("/dh");
		//获取链接
		connection = factory.newConnection();	
	}
	
	/**
	 * 创建生产者对象
	 * @throws IOException 
	 */
	
	@Test
	public void provider() throws IOException{
		//1.获取Channel对象 控制队列和路由和交换机
		Channel channel = connection.createChannel();
		
		//2.定义队列
		/**
		 * rabbitMQ中的参数说明
		 * queue : 队列名称
		 * durable: 是否持久化  true当消息队列重新启动后,队列还存在  
		 * 					  false 当队列重启后,队列不存在
		 * exclusive: 独有的.  
		 * 			    当前的消息队列是否由生产者独占,如果配置为true表示消费者不能 消息该队列
		 * autoDelete:是否自动删除. 
		 * 			    如果为true,则消息队列中没有消息时该队列自动删除
		 * 
		 * arguments: 提交的参数 一般为null
		 */
		channel.queueDeclare("queue_simple", false, false, false, null);
		
		//定义需要发送的消息
		String msg = "我是简单模式";
		
		//将消息与队列绑定,并且发送
		/**
		 * exchange:表示交换机的名称  如果没有交换机则为""串
		 * routingKey:消息发往队列的参数  如果没有路由key则写队列名称
		 * props :消息发送的额外的参数,如果没有参数 为null
		 * body: 发送的消息的二进制字节码文件
		 */
		channel.basicPublish("", "queue_simple", null, msg.getBytes());
		
		//关闭流
		channel.close();
		connection.close();
		
		System.out.println("恭喜你 会用队列了!!!!!!!");
		
	}
	
	
	//定义消费者
	/**
	 * 1.先获取Channel对象
	 * 2.定义消息队列
	 * 3.定义消费者对象
	 * 4.将消费者与队列信息绑定
	 * 5.通过循环的方式获取队列中的内容.
	 * 6.将获取的数据转化为字符串
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws ConsumerCancelledException 
	 * @throws ShutdownSignalException 
	 */
	@Test
	public void consumer() throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException{
		Channel channel = connection.createChannel();
		
		//定义队列
		channel.queueDeclare
		("queue_simple", false, false, false, null);
		
		//定义消费者
		QueueingConsumer consumer = 
				new QueueingConsumer(channel);
		
		//将队列与消费者绑定
		/**
		 * 参数说明:
		 * 	queue:队列的名称
		 * 	autoAck:是否自动回复 队列确认后方能执行下次消息
		 *  callback: 回调参数  写的的消费者对象
		 */
		channel.basicConsume("queue_simple", true, consumer);
		
		//通过循环的方式获取消息
		while(true){
			//获取消息队列的内容 ~~Delivery
			QueueingConsumer.Delivery  delivery = 
					consumer.nextDelivery();
			
			//字符转化
			String msg = new String(delivery.getBody());
			
			System.out.println("消费者消费队列消息:"+msg);
		}
	}
}
