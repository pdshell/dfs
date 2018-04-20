package com.jt.test.redis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.ShardInfo;

public class TestRedis {
	
	/**
	 * 1.连接远程redis的客户端 ip:6379
	 * 2.通过jedis操作数据的增和删
	 */
	//@Test
	public void test01(){
		Jedis jedis = new Jedis("192.168.126.142", 6379);
		jedis.set("name", "jerry");
		System.out.println("获取redis中的数据:"+jedis.get("name"));
	}
	
	//分片的测试  192.168.126.142  6379 6380 6381
	//@Test
	public void test02(){
		/**
		 * 定义分片的连接池
		 * 参数介绍
		 * 1.poolConfig 定义链接池的大小
		 * 2.shards     表示List<Shardinfo> 表示redis的信息的集合
		 * 
		 * 补充知识:
		 * 	Jedis 引入会有线程安全性问题.所以通过线程池的方式动态获取
		 * jedis链接.
		 */
		
		//定义链接池大小
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(1000);
		poolConfig.setTestOnBorrow(true);//测试链接是否正常,如果不正常会重新获取
		poolConfig.setMaxIdle(30);
		//定义分片的list集合
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		shards.add(new JedisShardInfo("192.168.126.142",6379));
		shards.add(new JedisShardInfo("192.168.126.142",6380));
		shards.add(new JedisShardInfo("192.168.126.142",6381));
		ShardedJedisPool jedisPool = 
				new ShardedJedisPool(poolConfig, shards);
		//获取redis的链接
		ShardedJedis jedis = jedisPool.getResource();
		for(int i=1;i<=20;i++){
			jedis.set("KEY"+i, ""+i+10);
		}
		System.out.println(jedis.get("KEY"+1));
		System.out.println("redis插入操作成功!!!");
	}
	
	//哨兵的测试
	@Test
	public void test03(){
		//创建哨兵的连接池
		//String类型表示的是哨兵的IP:端口号
		Set<String> sentinels = new HashSet<String>();
		
		String msg = new HostAndPort("192.168.126.142",26379).toString();
		
		System.out.println("通过对象输出哨兵的信息格式:"+msg);
		
		//为set集合赋值 保存全部的哨兵信息
		sentinels.add(new HostAndPort("192.168.126.142",26379).toString());
		sentinels.add(new HostAndPort("192.168.126.142",26380).toString());
		sentinels.add(new HostAndPort("192.168.126.142",26381).toString());
		
		/**
		 * 参数介绍
		 * 1.masterName 表示链接哨兵的主机的名称一般是字符串mymaster
		 * 2.sentinels 哨兵的集合Set<>
		 */
		JedisSentinelPool sentinelPool = 
				new JedisSentinelPool("mymaster", sentinels);
		
		Jedis jedis = sentinelPool.getResource();
		
		jedis.set("name", "tom");
		System.out.println("获取数据:"+jedis.get("name"));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
