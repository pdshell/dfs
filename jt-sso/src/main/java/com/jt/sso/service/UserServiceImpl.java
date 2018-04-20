package com.jt.sso.service;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.sso.mapper.UserMapper;
import com.jt.sso.pojo.User;

import redis.clients.jedis.JedisCluster;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private JedisCluster jedisCluster;
	
	
	
	//SELECT COUNT(*) FROM tb_user WHERE username="admin123"
	//1 username、2 phone、3 email
	@Override
	public Boolean findCheckUser(String param, int type) {
		
		String cloumn = null;
		switch (type) {
		case 1: cloumn = "username"; break;
		case 2: cloumn = "phone"; break;
		case 3: cloumn = "email"; break;
		}
		int count = userMapper.findCheckUser(cloumn,param);
		//返回数据是否存在
		return count !=0 ? true : false;
	}


	@Override
	public String saveUser(User user) {
		//为User信息补全数据
		user.setPassword(DigestUtils.md5Hex(user.getPassword()));  //将密码加密
		user.setEmail(user.getPhone()); //用电话号码代替邮箱
		user.setCreated(new Date());	//设定时间
		user.setUpdated(user.getCreated());
		
		//将user进行入库操作
		userMapper.insert(user);
		
		//将用户名返回
		return user.getUsername();
	}

	
	/**
	 * 1.将密码进行加密处理
	 * 2.根据用户名和密码查询用户信息
	 * 3.如果能够获取数据 则表示用户名和密码是正确的
	 * 4.如果正确生成秘钥
	 * 5.将userJSON数据存入redis中
	 * 6.如果用户名和密码不正确直接返回null
	 */
	@Override
	public String findUserByUP(String username, String password) {
		
		String md5Password = DigestUtils.md5Hex(password);
		
		User user = userMapper.findUserByUP(username,md5Password);
		try {
			if(user !=null){
				//用户名和密码正确
				String ticket = DigestUtils.md5Hex("JT_TICKET_"+
				System.currentTimeMillis() + username);
				
				//将user对象转化为JSON数据
				String jsonData = objectMapper.writeValueAsString(user);
				
				//将用户信息写入Redis中
				jedisCluster.set(ticket,jsonData);
				
				return ticket;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;	
	}
	
	
	
	
	
	
	
	
	
	
}
