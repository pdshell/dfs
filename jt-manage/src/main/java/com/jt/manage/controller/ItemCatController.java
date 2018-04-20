package com.jt.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.manage.pojo.ItemCat;
import com.jt.manage.service.ItemCatService;

@Controller
@RequestMapping("/item/cat")
public class ItemCatController {
	
	@Autowired
	private ItemCatService itemCatService;
	
	//通用Mapper的入门案例  查询全部的ItemCat表数据
	
	
	@RequestMapping("/ItemAll")
	@ResponseBody
	public List<ItemCat> findAll(){
		
		return itemCatService.findItemCat();
		
	}
	
	
	/**
	 * 1.@ResponseBody 
	 *  作用:
	 *  	如果返回的数据时对象则自动的转化为JSON{key:value}
	 *      如果返回的数据为String  则按照字符串原样返回 String
	 *  注意:转化JSON数据时,调用的是对象中的getXXX()方法
	 *  
	 * 2.@RequestParam
	 *   作用:能够将url中的参数灵活的为接收参数赋值
	 *   value="id"  url中的参数
	 *   defaultValue="0" 默认值   如果Id为null,默认值才会启用
	 * 	 required=true 表示必须传递该参数
	 * 	 
	 */
	//商品分类实现
	@RequestMapping("/list")
	@ResponseBody
	public List<ItemCat> findItemCat
	(@RequestParam(value="id",defaultValue="0") Long parentId){
		//Long parentId = 0L;	//定义一级菜单的父级
		
		//根据parentId查询商品的分类信息
		return itemCatService.findItemCatByParentId(parentId);
	}
	
	
	
	
	
	
	

}
