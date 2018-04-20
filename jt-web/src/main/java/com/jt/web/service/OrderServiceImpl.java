package com.jt.web.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.common.vo.SysResult;
import com.jt.web.pojo.Order;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private HttpClientService httpClient;
	

	private static ObjectMapper objectMapper = new ObjectMapper();
	
	/**
	 * 1.定位uri
	 * 2.封装数据
	 * 3.发出请求获取JSON数据
	 * 4.处理数据,之后正确返回
	 */
	@Override
	public String saveOrder(Order order) {
		
		String uri = "http://order.jt.com/order/create";
		
		try {
			//将order对象转化为JSON串
			String orderJSON = 
					objectMapper.writeValueAsString(order);
			Map<String, String> map = new HashMap<String,String>();
			map.put("orderJSON", orderJSON);
			
			String resultJSON = httpClient.doPost(uri, map);
			//获取sysResult对象
			SysResult sysResult = 
				objectMapper.readValue(resultJSON, SysResult.class);
			
			//判断后台调用是否正确
			if(sysResult.getStatus() == 200){
				String orderId = (String) sysResult.getData();
				return orderId;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	
	/**
	 * 1.定义uri
	 * 2.封装数据
	 * 3.发送请求
	 * 4.获取数据并且解析
	 */
	@Override
	public Order findOrderById(String id) {
		String uri = "http://order.jt.com/order/query/" + id;
		try {
			String orderJSON = httpClient.doGet(uri);
			if(!StringUtils.isEmpty(orderJSON)){
				//数据不为空
				Order order = 
						objectMapper.readValue(orderJSON,Order.class);
				return order;
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
