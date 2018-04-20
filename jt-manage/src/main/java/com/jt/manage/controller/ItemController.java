package com.jt.manage.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.EasyUIResult;
import com.jt.common.vo.SysResult;
import com.jt.manage.pojo.Item;
import com.jt.manage.pojo.ItemDesc;
import com.jt.manage.service.ItemService;

@Controller
@RequestMapping("/item")
public class ItemController {
	
	//引入日志工具类
	private static final Logger logger = Logger.getLogger(ItemController.class);
	
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/findAll")
	@ResponseBody	//将对象通过jackSONjar包转化为JSON串
	public List<Item> findItemAll(){
		
		return itemService.findItemAll();
	}
	
	//通过分页的方式实现数据的查询
	//http://localhost:8091/item/query?page=1&rows=20
	@RequestMapping("/query")
	@ResponseBody
	public EasyUIResult  findItemByPage(Integer page,Integer rows){
		
		return itemService.findItemByPage(page,rows);
	}
	
	/**
	 * 1.采用传统的Ajax实现数据的回显
	 * url:/item/cat/queryItemCatName
	 */
	/*@RequestMapping("/cat/queryItemCatName")
	public void findItemCatNameById(Long itemCatId,HttpServletResponse response){
		
		String name = itemService.findItemCatName(itemCatId);
		
		//设定字符集编码
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(name);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	
	/**
	 * @ResponseBody的注意事项
	 * 1.如果使用该注解 返回的是String字符串则必须处理乱码问题.
	 * 原因:springMVC源码中有如下的下发
	 * 1.如果返回值是对象,解析的编码格式utf-8
	 * 2.如果返回值是String类型,则采用ISO-8859-1的编码格式
	 * 
	 * 解决方法:
	 * 	1.将返回值封装为对象
	 *  2.执行字符集
	 * 
	 * 总结:以后的返回值尽量使用对象封装
	 * @param itemCatId
	 * @return
	 */
	@RequestMapping(value="/cat/queryItemCatName",produces="text/html;charset=utf-8")
	@ResponseBody   
	public String findItemCatNameById(Long itemCatId){
		
		return itemService.findItemCatName(itemCatId);
		
	}
	
	//商品的新增
	@RequestMapping("/save")
	@ResponseBody
	public SysResult saveItem(Item item,String desc){
		try {
			itemService.saveItem(item,desc);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "新增商品失败");
		}
	}
	
	@RequestMapping("/update")
	@ResponseBody
	public SysResult updateItem(Item item,String desc){
		try {
			itemService.updateItem(item,desc);
			logger.info("{~~~~~更新成功}");
			return SysResult.build(200,"更新成功");
		} catch (Exception e) {
			e.printStackTrace();
			//throw new Exception();
			//记录日志
			//System.out.println("sssssss");
			logger.error("{更新操作失败}");
			return SysResult.build(201, "更新失败");
		}
	}
	
	//{ids:1,2,3,4,5,6} SpringMVC会将数据进行强制类型的转换,
	//形成想要的结构
	@RequestMapping("/delete")
	@ResponseBody
	public SysResult deleteItem(Long[] ids){
		try {
			itemService.deleteItems(ids);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "删除失败");
		}
	}
	
	
	//商品的下架
	@RequestMapping("/instock")
	@ResponseBody
	public SysResult itemInstock(Long[] ids){
		
		try {
			int status = 2;
			itemService.updateStatus(status,ids);
			return SysResult.build(200, "下架成功");
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "下架失败");
		}
	}
	
	
	//商品的上架架
	@RequestMapping("/reshelf")
	@ResponseBody
	public SysResult itemReshelf(Long[] ids){
		
		try {
			int status = 1;
			itemService.updateStatus(status,ids);
			return SysResult.build(200, "下架成功");
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "下架失败");
		}
	}
	
	
	/**
	 * 通用Mapper的高级应用
	 * 功能:通过该方法实现记录总数的查询
	 * 该方法是一个测试方法!!!!
	 * @return
	 */
	@RequestMapping("/findItemCount")
	@ResponseBody
	public int findItemCount(){
		
		return itemService.findItemCount();
		
	}
	
	//查询商品描述信息    /item/query/item/desc/1898191
	@RequestMapping("/query/item/desc/{itemId}")
	@ResponseBody
	public SysResult findItemDescById(@PathVariable Long itemId){
		try {
			ItemDesc itemDesc = itemService.findItemDescById(itemId);
			
			return SysResult.oK(itemDesc);
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "商品的描述信息查询失败");
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
