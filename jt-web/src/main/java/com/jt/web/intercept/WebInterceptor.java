package com.jt.web.intercept;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.util.CookieUtils;
import com.jt.web.pojo.User;
import com.jt.web.util.UserThreadLocal;

import redis.clients.jedis.JedisCluster;

//定义SpringMVC的拦截器
public class WebInterceptor implements HandlerInterceptor{
	
	@Autowired
	private JedisCluster jedisCluster;
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	//在执行请求之前执行 
	/**
	 * 说明:如果用户没有登陆京淘系统,则应该在点击购物车按钮时,访问后台
	 * 数据前生效.
	 * 思路:
	 * 	1.从request对象中获取ticket信息
	 *  2.判断ticket信息中是否含有数据.
	 *  	如果有数据:
	 *  		从redis中获取userJSON数据.
	 *  			如果数据不为null
	 *  				获取user信息,之后保存~~~~~~~
	 * 		除此之外 直接跳转到登陆页面
	 * 
	 *  3.参数解析
	 *  	return false; 表示拦截  不能访问目标url
	 *      return true ; 表示放行   可以访问目标url
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		String ticket = CookieUtils.getCookieValue(request, "JT_TICKET");
		if(!StringUtils.isEmpty(ticket)){
			//表示数据不为null
			String userJSON = jedisCluster.get(ticket);
			if(!StringUtils.isEmpty(userJSON)){
				//redis中的数据不为null
				User user = 
				objectMapper.readValue(userJSON, User.class);
				//通过ThreadLocal实现数据传输
				UserThreadLocal.set(user);
				return true;	//表示放行
			}
		}
		
		//如果上述代码不能执行.则跳转登录页面
		response.sendRedirect("/user/login.html");
		return false;
	}
	
	//在url执行之后执行
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	//在进行页面转向前执行
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
