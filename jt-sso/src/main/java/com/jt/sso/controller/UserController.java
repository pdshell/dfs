package com.jt.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.vo.SysResult;
import com.jt.sso.pojo.User;
import com.jt.sso.service.UserService;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JedisCluster jedisCluster;
	//http://sso.jt.com/user/check/"+escape(pin)+"/1
	@RequestMapping("/check/{param}/{type}")
	@ResponseBody
	public Object findCheckUser(@PathVariable String param,
			@PathVariable int type,String callback){
		try {
			Boolean flag = userService.findCheckUser(param,type);
			//通过JSONP的形式返回
			MappingJacksonValue jacksonValue = 
					new MappingJacksonValue(SysResult.oK(flag));
			jacksonValue.setJsonpFunction(callback);
			
			return jacksonValue;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}	
	}
	
	//http://sso.jt.com/user/register 用户的新增操作
	@RequestMapping("/register")
	@ResponseBody
	public SysResult saveUser(User user){
		try {
			String username = userService.saveUser(user);
			return SysResult.oK(username);
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "新增用户失败");
		}
	}
	
	
	//http://sso.jt.com/user/login
	@RequestMapping("/login")
	@ResponseBody
	public SysResult doLogin(@RequestParam("u")String username,
			@RequestParam("p")String password){
		
		try {
			String ticket = userService.findUserByUP(username,password);
			return SysResult.oK(ticket);
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "用户登陆失败");
		}
	}
	
	
	//http://sso.jt.com/user/query/b19f199b835d0cd12759ee85133afa94?callback=jsonp1521020217218&_=1521020217266
	@RequestMapping("/query/{ticket}")
	@ResponseBody
	public Object findUserByTicket(@PathVariable String ticket,String callback){
		String jsonData = jedisCluster.get(ticket);
		if(StringUtils.isEmpty(jsonData)){
			//证明缓存中没有改数据
			MappingJacksonValue jacksonValue = 
					new MappingJacksonValue(SysResult.build(201,"用户查询失败"));
			jacksonValue.setJsonpFunction(callback);
			return jacksonValue;
		}else{
			MappingJacksonValue jacksonValue = 
					new MappingJacksonValue(SysResult.oK(jsonData));
			jacksonValue.setJsonpFunction(callback);
			return jacksonValue;
		}
	}
	
	
	
	
	
	
	
	
	
}
