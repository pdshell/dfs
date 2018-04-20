package com.jt.manage.service;

import java.util.List;

import com.jt.manage.pojo.User;

public interface UserService {
	
	//查询用户的全部信息
	List<User> findAll();
}
