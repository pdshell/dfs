package com.jt.sso.service;

import com.jt.sso.pojo.User;

public interface UserService {

	Boolean findCheckUser(String param, int type);
	
	//保存用户信息
	String saveUser(User user);
	
	//后台的登陆操作
	String findUserByUP(String username, String password);

}
