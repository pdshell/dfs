package com.jt.manage.controller.web;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.vo.ItemCatResult;
import com.jt.manage.service.ItemCatService;

@Controller
public class WebItemCatController {
	
	@Autowired
	private ItemCatService itemCatService;
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	
	//编辑调用的方法
	//web/itemcat/all?callback=category.getDataService
	//@RequestMapping("/web/itemcat/all")
	public void findItemCatAll(String callback,HttpServletResponse response){
		//采用传统Ajax方式进行数据传递
		try {
			ItemCatResult catResult = itemCatService.findItemCatAll();
			String jsonData = objectMapper.writeValueAsString(catResult);
			System.out.println(jsonData);
			String result = callback+"(" + jsonData +")";
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(result);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * 利用工具类直接返回JSONP的对象 callback({JSON})
	 * @param callback
	 * @return
	 */
	@RequestMapping("/web/itemcat/all")
	@ResponseBody
	public Object findItemCat(String callback){
		
		ItemCatResult itemCatresult = itemCatService.findCacheItemCatAll();
		
		//负责JSONP对象返回 构造方法中添加返回的数据
		MappingJacksonValue jacksonValue = 
				new MappingJacksonValue(itemCatresult);
		
		//设定返回值方法
		jacksonValue.setJsonpFunction(callback);
		return jacksonValue;
	}
	
	
	
	
	
	
	
	

}
