package com.jt.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	
	//实现京淘前台页面跳转   localhost:8092/index.html
	@RequestMapping("/index")
	public String index(){
		return "index";
	}

}
