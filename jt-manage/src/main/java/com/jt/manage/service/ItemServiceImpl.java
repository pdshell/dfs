package com.jt.manage.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.vo.EasyUIResult;
import com.jt.manage.mapper.ItemDescMapper;
import com.jt.manage.mapper.ItemMapper;
import com.jt.manage.pojo.Item;
import com.jt.manage.pojo.ItemDesc;

import redis.clients.jedis.JedisCluster;
@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private ItemMapper itemMapper;
	
	@Autowired
	private ItemDescMapper itemDescMapper;
	
	@Autowired
	private JedisCluster JedisCluster;
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	//查询全部的商品信息
	@Override
	public List<Item> findItemAll() {
		
		return itemMapper.findItemAll();
	}
	
	/**
	 * page:当前查询的页数
	 * rows:每页展现的行数
	 * 要求:
	 * 	查询第1页数据,每页20条
	 * 	sql:SELECT * FROM tb_item LIMIT 0,20
	 * 	
	 *  查询第2页
	 * 	sql:SELECT * FROM tb_item LIMIT 20,20
	 * 	
	 * 	查询第3页
	 * 	sql:SELECT * FROM tb_item LIMIT 40,20
	 * 	
	 *  查询第n页
	 *  sql:SELECT * FROM tb_item LIMIT (n-1)*rows,rows
	 */
	@Override
	public EasyUIResult findItemByPage(Integer page, Integer rows) {
		//获取记录总数
		int total  = itemMapper.findItemCount();
		
		//计算分页的起始位置
		int begin = (page-1) * rows;
		
		List<Item> itemList = itemMapper.findItemByPage(begin,rows);
		
		return new EasyUIResult(total, itemList);
	}
	
	//感兴趣自己维护 为ItemCatMapper
	@Override
	public String findItemCatName(Long itemCatId) {
		
		return itemMapper.findItemCatName(itemCatId);
	}
	
	/**
	 * 说明:
	 * 	Item对象的主键是自增的,所以item中的id现在为null
	 *  ItemDesc对象入库操作时,必须含有主键Id信息.
	 * 困难:如果获取已经入库的id??
	 * 解决:
	 * 利用通用Mapper的机制.当执行插入操作时,会先查询当前最大的id.
	 * 之后回显.
	 *   SELECT LAST_INSERT_ID() 
	 * @param item
	 * @param desc
	 */
	@Override
	public void saveItem(Item item,String desc) {
		//补齐Item数据
		item.setStatus(1); //启用
		item.setCreated(new Date());
		item.setUpdated(item.getCreated());
		
		//利用通用Mapper实现入库操作
		itemMapper.insert(item);
		//为ItemDesc补齐数据
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemDesc(desc);
		itemDesc.setItemId(item.getId());
		itemDesc.setCreated(item.getCreated());
		itemDesc.setUpdated(item.getCreated());
		itemDescMapper.insert(itemDesc);
		
	}
	
	/**
	 * 动态的更新操作
	 * id name age
	 * 1  jerry 18
	 * 
	 * User user = new User();
	 * user.setId(1);
	 * user.setName("jerry");
	 * 
	 * userMapper.updateUser(user);
	 */
	@Override
	public void updateItem(Item item,String desc) {
		item.setUpdated(new Date());
		//动态更新
		itemMapper.updateByPrimaryKeySelective(item);
		
		//修改itemDesc
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(item.getCreated());
		itemDesc.setUpdated(item.getCreated());
		
		itemDescMapper.updateByPrimaryKeySelective(itemDesc);
		
		//当数据进行修改时应该将redis的缓存数据删除
		JedisCluster.del("ITEM_"+item.getId());
	}
	
	/**
	 * 删除操作时,应该先删除从表的数据,之后再删除主表数据
	 */
	@Override
	public void deleteItems(Long[] ids) {
		
		itemDescMapper.deleteByIDS(ids);
		itemMapper.deleteByIDS(ids);
		
		//删除缓存操作
		for (Long id : ids) {	
			JedisCluster.del("ITEM_" + id);
		}
	}

	@Override
	public void updateStatus(int status, Long[] ids) {
		
		/**
		 * 方案1:
		 * 	在service层通过循环遍历的形式实现操作
		 * 方案2:
		 * 	通过Mybatis实现一次批量修改数据的操作
		 */
		
		itemMapper.updateStatus(status,ids);
		
		/*for (Long id : ids) {
			Item item = new Item();
			item.setId(id); //封装主键
			item.setStatus(status);
			item.setUpdated(new Date());
			itemMapper.updateByPrimaryKeySelective(item);
		}*/
	}

	@Override
	public int findItemCount() {
		
		//该方法调用的是通用Mapper中的接口方法
		return itemMapper.findCount();
	}
	
	//查询商品表述信息
	@Override
	public ItemDesc findItemDescById(Long itemId) {
		
		return itemDescMapper.selectByPrimaryKey(itemId);
	}

	@Override
	public Item findItemById(Long itemId) {
		
		Item item = itemMapper.selectByPrimaryKey(itemId);

		return item;
	}
	
	
	
	
	
	
	
	
	
	
	
}
