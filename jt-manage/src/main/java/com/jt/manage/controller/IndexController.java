package com.jt.manage.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	
	//实现页面转向
	@RequestMapping("/index")
	public String index(){
		
		return "index";
	}
	
	//跳转到商品的新增页面  /page/item-add
	/**
	 * 问题:如果index页面中需要跳转多个页面,
	 * 则需要编辑多个Controller方法用来.处理请求.
	 * 要求:采用一个方法.实现页面的通用跳转
	 * 办法:采用restFul结构实现页面通用跳转
	 * 
	 * 回顾:
	 * 	get方法: localhost:8091/addUser?id=1&name=tom....
	 *  restFel: localhost:8091/addUser/1/tom
	 *  
	 *  解决办法:
	 *  localhost:8091/page/item-add
	 *  @PathVariable 的作用:可以将{module}的数据,传值交给参数列表
	 *  中的module.要求名称必须一致.
	 *  
	 *  总结:
	 *  	1.restFul结构参数用{}包裹
	 *      2.参数与参数之间用"/"分割
	 *      3.接收参数和变量应该一致
	 *      4.使用@PathVariable 实现数据传参
	 * @return
	 */
	@RequestMapping("/page/{module}")
	public String toItemAdd(@PathVariable String module){
		
		return module;
	}
	
	/*///page/item-list
	@RequestMapping("/page/item-list")
	public String toItemList(){
		
		return "item-add";
	}*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
