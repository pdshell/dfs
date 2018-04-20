package com.jt.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.manage.pojo.User;
import com.jt.manage.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/findAll")
	public String findAll(Model model){
		
		List<User> userList = userService.findAll();
		
		//将数据保存到request域中
		model.addAttribute("userList", userList);
		//request.setAttribute("userList", userList);
		//通过springMVC的视图解析器来维护路径 /WEB-INF/views/XXXXX.jsp
		return "userList";
	}	
}
