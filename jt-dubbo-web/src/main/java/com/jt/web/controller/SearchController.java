package com.jt.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jt.dubbo.pojo.Item;
import com.jt.dubbo.service.DubboOrderService;
import com.jt.dubbo.service.DubboSearchService;

@Controller
public class SearchController {
	
	@Autowired
	private DubboSearchService  searchService;
	
	//search.html?q=娃娃
	@RequestMapping("/search")
	public String search(@RequestParam("q")String keyword,Model model){
		try {
			keyword = new String(keyword.getBytes("ISO-8859-1"), "UTF-8");
			List<Item> itemList = searchService.findItemListBykey(keyword);
			model.addAttribute("itemList", itemList);	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "search";
		
		
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	

}
