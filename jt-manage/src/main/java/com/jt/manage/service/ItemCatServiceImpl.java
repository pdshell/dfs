package com.jt.manage.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.RedisService;
import com.jt.common.vo.ItemCatData;
import com.jt.common.vo.ItemCatResult;
import com.jt.manage.mapper.ItemCatMapper;
import com.jt.manage.pojo.Item;
import com.jt.manage.pojo.ItemCat;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

@Service
public class ItemCatServiceImpl implements ItemCatService {
	
	@Autowired
	private ItemCatMapper itemCatMapper;
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	//注入jedis对象
	//@Autowired
	//private Jedis jedis;
	
	//注入redis的工具类
	//@Autowired
	//private RedisService redisService;
	
	
	//引入集群的配置
	/**
	 * 该对象实例化的过程
	 * 1.spring容器需要注入jedisCluster时,会根据ID查找对应的Bean
	 * 2.Spring会根据bean中class属性调用对象.
	 * 3.由于RedisCluster implements FactoryBean<JedisCluster> 接口文件
	 *   所以调用工厂模式的getObject().获取对象,实现注入
	 * <bean id="jedisCluster"  
	 * class="com.jt.common.util.RedisCluster" >
	 */
	
	@Autowired   //set注入 id class
	private JedisCluster jedisCluster; //工厂生产的对象
	

	/**
	 * 使用通用Mapper(JPA),传入的对象最终充当了查询的where条件
	 * select * from tb_item_cat where id = 100 and status = 1
	 * 
	 * 总结:ItemCat对象会将不为Null的属性充当where条件/如果需要添加查询条件
	 * 为对象的属性赋值即可!!!
	 * 
	 */
	@Override
	public List<ItemCat> findItemCat() {
		ItemCat itemCat = new ItemCat();
		//itemCat.setId(100L);
		//itemCat.setStatus(1);
		return itemCatMapper.select(itemCat);
	}
	
	/**
	 * 1.当用户查询数据时,先查询缓存
	   2.当缓存中没有数据,应该查询后台的数据库
       3.将查询的结果转化为JSON串,存入redis中
       4.当用户再次查询时,缓存中已经含有该数据
       5.将redis中的JSON串转化为List集合
       6.将list集合返回
	 */
	@Override
	public List<ItemCat> findItemCatByParentId(Long parentId) {
		//创建List集合
		List<ItemCat> itemCatList = new ArrayList<ItemCat>();
		
		//定义key 定义前缀保证key不重复
		String key = "ITEM_CAT_"+parentId;
		
		//从缓存中获取数据
		//String jsonData = redis.get(key);  //直接链接
		String jsonData = jedisCluster.get(key);
		
		try {
			//判断返回值是否为空
			if(StringUtils.isEmpty(jsonData)){
				//表示没有缓存 查询数据库
				ItemCat itemCat = new ItemCat();
				itemCat.setParentId(parentId);
				itemCat.setStatus(1); //正常的分类信息	
				itemCatList = itemCatMapper.select(itemCat);
				
				//将itemcatList集合转化为JSON串
				String resultJSON = 
						objectMapper.writeValueAsString(itemCatList);
				
				//将数据存入redis缓存中
				//jedis.set(key, resultJSON);  直接链接
				jedisCluster.set(key, resultJSON);
			}else{
				//表示redis中含有数据,将JSON数据转化为List集合
				ItemCat[] itemCats = 
						objectMapper.readValue(jsonData,ItemCat[].class);
				
				//将返回值转化为List集合
				itemCatList = Arrays.asList(itemCats);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("恭喜你redis测试成功！！！！！！");
		return itemCatList;
	}
	
	/*
	 * 分析:	由于商品的分类信息较多,如果按照1级查询2级查询3级这样的结构
	 * 会查询很多次,数据库压力较大
	 * 解决:	
	 * 	首先查询全部的商品分类信息.之后进行数据的整理
	 * 	Map<parentId,List<ItemCat> itemcatLists>
	 *  parentId 表示父级的Id
	 *  itemCatLists.表示的是当前父级Id下所有的子级菜单
	 *  
	 * 步骤:
	 * 	1.封装一级商品分类信息
	 *  2.封装二级商品分类信息
	 *  3.封装三级商品分类信息
	 * 
	 */
	@Override
	public ItemCatResult findItemCatAll() {
		//查询全部的商品分类信息
		ItemCat tempItemCat = new ItemCat();
		tempItemCat.setStatus(1); //查询正常的商品分类信息
		List<ItemCat> itemCatLists = 
				itemCatMapper.select(tempItemCat);
		
		//整理数据 一个parentId 下的全部的自己list集合
		Map<Long, List<ItemCat>> itemCatMap = 
				new HashMap<Long, List<ItemCat>>();
		
		for (ItemCat itemCat : itemCatLists) {
			if(itemCatMap.containsKey(itemCat.getParentId())){
				//表示已经含有parentId,做数据的追加操作
				itemCatMap.get(itemCat.getParentId()).add(itemCat);
			}else{
				//为父级创建List集合
				List<ItemCat> itemCatList = new ArrayList<ItemCat>();
				itemCatList.add(itemCat);
				itemCatMap.put(itemCat.getParentId(), itemCatList);
			}
		}
		
		//构建返回对象
		ItemCatResult itemCatResult = new ItemCatResult();
		
		//封装一级商品分类菜单
		List<ItemCatData> itemCatDataList = new ArrayList<ItemCatData>();
		
		for (ItemCat itemCat1 : itemCatMap.get(0L)) {
			
			//定义一级商品分类对象
			ItemCatData itemCatData1 = new ItemCatData();
			itemCatData1.setUrl("/products/"+ itemCat1.getId()+".html");
			itemCatData1.setName
			("<a href='"+itemCatData1.getUrl()+"'>"+itemCat1.getName()+"</a>");
			
			
			//封装商品分类的二级菜单
			List<ItemCatData> itemCatDataList2 = new ArrayList<ItemCatData>();
			for (ItemCat itemCat2 : itemCatMap.get(itemCat1.getId())) {
				
				ItemCatData itemCatData2 = new ItemCatData();
				itemCatData2.setUrl("/products/"+itemCat2.getId());
				itemCatData2.setName(itemCat2.getName());
				
				//准备三级商品分类菜单
				List<String> itemCatDataList3 = new ArrayList<String>();
				
				for (ItemCat itemCat3 : itemCatMap.get(itemCat2.getId())) {
					itemCatDataList3.add("/products/"+itemCat3.getId() + "|" +itemCat3.getName());
				}
				
				//将三级分类的集合注入到二级对象中
				itemCatData2.setItems(itemCatDataList3);
				itemCatDataList2.add(itemCatData2);
			}
			itemCatData1.setItems(itemCatDataList2);
			itemCatDataList.add(itemCatData1);
			
			//封装参数 多余的直接跳出
			if(itemCatDataList.size() >13)
			break;
		}
	
		//封装一级商品分类的菜单
		itemCatResult.setItemCats(itemCatDataList);
		
		return itemCatResult;
	}
	
	/**
	 * 1.查询时应该先查询缓存
	 * 2.如果缓存中没有缓存数据则执行业务操作查询数据
	 * 3.将查询结果返回,将查询的结果存入缓存中
	 * 4.如果缓存中含有该数据
	 * 5.将缓存数据转化对象返回.满足编程的规范
	 * @return
	 */
	//实现三级商品分类的缓存操作
	@Override
	public ItemCatResult findCacheItemCatAll(){
		
		String key = "ITEM_CAT_ALL";
		
		String jsonData = jedisCluster.get(key);
		
		try {
			//判断数据是否为空
			if(StringUtils.isEmpty(jsonData)){
				ItemCatResult itemCatResult = 
						findItemCatAll();
				//将对象转化为JSON串
				String restJSON = 
						objectMapper.writeValueAsString(itemCatResult);
				//将数据存入redis中
				jedisCluster.set(key, restJSON);
				return itemCatResult;
				
			}else {
				ItemCatResult itemCatResult =
						objectMapper.readValue(jsonData, ItemCatResult.class);
				return itemCatResult;	
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
