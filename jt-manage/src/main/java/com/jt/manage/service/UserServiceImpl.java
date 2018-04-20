package com.jt.manage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.manage.mapper.UserMapper;
import com.jt.manage.pojo.User;
@Service
public class UserServiceImpl implements UserService {
	
	//@Resource  
	/**如果是单个项目用该注解可以实现功能,
	     如果是分布式项目必须使用 @autowired
	**/
	@Autowired
	private UserMapper userMapper;
	
	//大型公司要求 如果是接口方法必须添加Override.方便后期维护
	@Override	
	public List<User> findAll() {
		
		return userMapper.findAll();
	}
	
	
	
	
}
