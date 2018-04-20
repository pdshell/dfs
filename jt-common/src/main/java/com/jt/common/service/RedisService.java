package com.jt.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;


@Service
public class RedisService {
	//当对象实例化时,不会自动的赋值该属性.
	//但是如果该对象被调用,该属性才会依赖注入
	/**
	 * 该注解的用法,一般用到工具类中,因为工具类被引入的位置不固定
	 */
	@Autowired(required=false)  
	private JedisSentinelPool jedisSentinelPool;
	
	public void set(String key,String value){
		Jedis jedis = jedisSentinelPool.getResource();
		jedis.set(key, value);
		//将链接还回池中
		jedisSentinelPool.returnResource(jedis);
	}
	
	public String get(String key){
		Jedis jedis = jedisSentinelPool.getResource();
		String result = jedis.get(key);
		jedisSentinelPool.returnResource(jedis);
		return result;
	}
	
	
	
	
	
	
	/*//引入分片的方式操作redis
	@Autowired
	private ShardedJedisPool jedisPool;
	
	public void set(String key,String value){
		ShardedJedis jedis = jedisPool.getResource();
		jedis.set(key, value);
	}
	
	public String get(String key){
		ShardedJedis jedis = jedisPool.getResource();
		
		return jedis.get(key);
	}*/
	
	
	
	
	
	
	/*@Autowired	//注入spring的模板工具
	private StringRedisTemplate redisTemplate;
	
	
	public void set(String key,String value){
		ValueOperations<String, String> operations = 
				redisTemplate.opsForValue();
		//将数据写入redis中
		operations.set(key, value);
	}
	
	//通过key获取数据
	public String get(String key){
		ValueOperations<String, String> operations = 
				redisTemplate.opsForValue();
		return operations.get(key);
	}	*/
}
