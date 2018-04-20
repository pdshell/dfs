package com.jt.web.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.web.pojo.Item;

import redis.clients.jedis.JedisCluster;

@Service
public class ItemServiceImpl implements ItemService {
	
	//@Value("${item.key}") //配置文件中动态获取前缀:ITEM_ 
	//private Long key;
	
	@Autowired
	private HttpClientService httpClientService;
	
	@Autowired
	private JedisCluster jedisCluster;
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	/**
	 * 经过京淘前台的业务层，去访问后台的业务代码?
	 * 解决策略:跨域
	 * 问题:在业务层中不能采用JSONP的形式进行跨域调用
	 * 解决:采用HttpClient方式进行调用
	 * 
	 */
	@Override
	public Item findItemById(Long itemId) {
		
		//添加缓存处理
		String key = "ITEM_"+ itemId;	//通过配置文件方式动态引入
		String resultData =  jedisCluster.get(key);
		try {
			
			if(StringUtils.isEmpty(resultData)){
				//进行跨域访问数据
				String uri = 
						"http://manage.jt.com/web/item/findItemById/"+itemId;
				String jsonData = httpClientService.doGet(uri);
				if(!StringUtils.isEmpty(jsonData)){
					//需要将JSON串转化为Item对象
					Item item = 
					objectMapper.readValue(jsonData, Item.class);
					//将数据写入redis中
					String itemJSON = 
							objectMapper.writeValueAsString(item);
					jedisCluster.set(key, itemJSON);
					return item;
				}
			}else{
				Item item = objectMapper.readValue(resultData, Item.class);
				return item;	
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
