package com.jt.web.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.common.vo.SysResult;
import com.jt.web.pojo.User;

@Service
public class UserServiceImpl implements UserService {

	//通过HttpClient实现跨域的访问
	@Autowired
	private HttpClientService httpClientService;
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	
	/**
	 * 跨域的步骤
	 * 1.定义目标url
	 * 2.添加参数
	 * 3.实现跨域
	 * 4.获取结果
	 * 5.结果解析,获取需要的数据
	 */
	@Override
	public String saveUser(User user) {
		String uri = "http://sso.jt.com/user/register";
		Map<String, String> map = new HashMap<String,String>();
		map.put("username", user.getUsername());
		map.put("password", user.getPassword());
		map.put("phone", user.getPhone());
		map.put("email", user.getEmail());
		try {
			String resultData = httpClientService.doPost(uri, map);
			//接收后端返回值,并且获取data数据
			SysResult sysResult = 
					objectMapper.readValue(resultData, SysResult.class);
			return (String) sysResult.getData();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 1.定位目标uri
	 * 2.准备数据
	 * 3.发出请求
	 * 4.解析数据
	 * 5.返回数据
	 */
	@Override
	public String findUserByUP(User user) {
		String uri = "http://sso.jt.com/user/login";
		Map<String, String> map = new HashMap<String,String>();
		map.put("u", user.getUsername());
		map.put("p", user.getPassword());
		try {
			String resultJSON = 
					httpClientService.doPost(uri, map);
			SysResult sysResult = 
					objectMapper.readValue(resultJSON, SysResult.class);
			//获取服务端数据
			String ticket = (String) sysResult.getData();
			return ticket;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}	
	}
	
	
	
	
	
	
	
	
	
	
	

}
