package com.jt.manage.controller.web;

import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.manage.pojo.Item;

@Controller
@RequestMapping("/web")
public class WebJSONPController {
	
	
	@RequestMapping("/testJSONP")
	@ResponseBody
	public Object hello(String callback){
		System.out.println("回调参数:"+callback);
		
		String json = "{\"id\":\"100\",\"name\":\"tom\"}";
		
		MappingJacksonValue jacksonValue = 
				new MappingJacksonValue(json);
		jacksonValue.setJsonpFunction(callback);
		System.out.println("后台调用成功");
		/*Item item = new Item();
		item.setId(1L);
		item.setTitle("卖女孩的小火柴");*/
		return jacksonValue;
	}
}
