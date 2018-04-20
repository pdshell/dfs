package com.jt.order.rabbit.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jt.dubbo.pojo.Order;
import com.jt.dubbo.pojo.OrderItem;
import com.jt.dubbo.pojo.OrderShipping;
import com.jt.order.mapper.OrderItemMapper;
import com.jt.order.mapper.OrderMapper;
import com.jt.order.mapper.OrderShippingMapper;

public class OrderServiceImpl {
	
	//将消息队列中的内容写入数据库中
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private OrderItemMapper orderItemMapper;
	
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	
	
	public void saveOrder(Order order){
		String orderId = order.getOrderId();
		//将Order对象入库操作
		//order.setOrderId(orderId);
		order.setStatus(1);	//表示未支付状态
		order.setCreated(new Date());
		order.setUpdated(order.getCreated());
		orderMapper.insert(order);
		System.out.println("订单插入完成");
		//插入订单物流信息
		OrderShipping orderShipping = order.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(order.getCreated());
		orderShipping.setUpdated(order.getCreated());
		orderShippingMapper.insert(orderShipping);
		System.out.println("物流插入完成");
		
		List<OrderItem> orderItems = order.getOrderItems();
		
		for (OrderItem orderItem : orderItems) {
			orderItem.setOrderId(orderId);
			orderItem.setCreated(order.getCreated());
			orderItem.setUpdated(order.getCreated());
			orderItemMapper.insert(orderItem);
		}
		System.out.println("消息队列入库成功!!!!!");
	}
	
}
