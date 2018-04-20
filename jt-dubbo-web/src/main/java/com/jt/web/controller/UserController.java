package com.jt.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.util.CookieUtils;
import com.jt.common.util.jedisClusterFactory;
import com.jt.common.vo.SysResult;
import com.jt.web.pojo.User;
import com.jt.web.service.UserService;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JedisCluster jedisCluster;
	
	
	
	///register.html  注册
	@RequestMapping("/register")
	public String register(){
		
		return "register";
	}
	
	//登录
	@RequestMapping("/login")
	public String login(){
		
		return "login";
	}
	
	
	///service/user/doRegister
	@RequestMapping("/doRegister")
	@ResponseBody
	public SysResult saveUser(User user){
		try {
			String username = userService.saveUser(user);
			return SysResult.oK(username);
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "用户新增失败");
		}
	}
	
	
	/**
	 * url:service/user/doLogin
	 * 1.用户需要登录操作
	 * 2.获取ticket信息
	 * 3.将ticket信息存入Cookie中
	 * 4.将cookie写入后正确返回
	 * @param user
	 * @return
	 */
	@RequestMapping("doLogin")
	@ResponseBody
	public SysResult doLogin(User user,
			HttpServletRequest request,HttpServletResponse response){
		try {
			String ticket = userService.findUserByUP(user);
			//判断ticket信息是否有效
			if(StringUtils.isEmpty(ticket)){
				//如果程序执行到这里表示ticket没有获取
				//1.前端的业务代码出错了 返回null
				//2.SSO单点登录系统出错 返回null	
				return SysResult.build(201, "用户登陆失败");
			}else{
				//表示用户的ticket不为空 将数据写入cookie中
				CookieUtils.setCookie
				(request, response, "JT_TICKET", ticket);		
				return SysResult.oK();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "登录失败");
		}
	}
	
	
	//http://www.jt.com/user/logout.html
	/**
	 * 1.应该获取Cookie中的ticket信息
	 * 2.从redis中删除用户信息
	 * 3.在应该删除Cookie
	 * 4.当系统返回后重定向到index.html
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request,
			HttpServletResponse response){
		
		String ticket = 
				CookieUtils.getCookieValue(request,"JT_TICKET");
		
		jedisCluster.del(ticket);
		CookieUtils.deleteCookie(request, response, "JT_TICKET");
		
		return "redirect:/index.html";	
		//进行重定向
		//return "forward:/index.html";	//进行转发
	}
}
