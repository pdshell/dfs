package com.jt.manage.service;

import java.util.List;

import com.jt.common.vo.EasyUIResult;
import com.jt.manage.pojo.Item;
import com.jt.manage.pojo.ItemDesc;

public interface ItemService {
	List<Item> findItemAll();

	EasyUIResult findItemByPage(Integer page, Integer rows);
	
	//通过cid查询商品分类信息
	String findItemCatName(Long itemCatId);

	void saveItem(Item item, String desc);
	
	//修改商品
	void updateItem(Item item, String desc);
	
	//批量删除
	void deleteItems(Long[] ids);
	
	//批量执行状态的修改
	void updateStatus(int status, Long[] ids);
	
	//该方法是测试方法 不具有通用性!!!!!!该方法是一个demo
	int findItemCount();

	ItemDesc findItemDescById(Long itemId);
	
	//根据Id获取Item对象
	Item findItemById(Long itemId);
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
