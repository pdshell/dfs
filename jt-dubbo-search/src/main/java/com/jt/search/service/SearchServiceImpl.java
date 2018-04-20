package com.jt.search.service;

import java.util.List;

import com.jt.dubbo.pojo.Item;
import com.jt.dubbo.service.DubboSearchService;
//非常哈
public class SearchServiceImpl implements DubboSearchService{

	@Override
	public List<Item> findItemListBykey(String keyword) {
		//利用全完检索的方式查询
		
		return null;
	}

}
